package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.internal.util.HalfSerializer;
import p005io.reactivex.subjects.PublishSubject;
import p005io.reactivex.subjects.Subject;

/* renamed from: io.reactivex.internal.operators.observable.ObservableRetryWhen */
public final class ObservableRetryWhen<T> extends AbstractObservableWithUpstream<T, T> {
    final Function<? super Observable<Throwable>, ? extends ObservableSource<?>> handler;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableRetryWhen$RepeatWhenObserver */
    static final class RepeatWhenObserver<T> extends AtomicInteger implements Observer<T>, Disposable {
        private static final long serialVersionUID = 802743776666017014L;
        volatile boolean active;
        final Observer<? super T> actual;

        /* renamed from: d */
        final AtomicReference<Disposable> f339d = new AtomicReference<>();
        final AtomicThrowable error = new AtomicThrowable();
        final InnerRepeatObserver inner = new InnerRepeatObserver<>();
        final Subject<Throwable> signaller;
        final ObservableSource<T> source;
        final AtomicInteger wip = new AtomicInteger();

        /* renamed from: io.reactivex.internal.operators.observable.ObservableRetryWhen$RepeatWhenObserver$InnerRepeatObserver */
        final class InnerRepeatObserver extends AtomicReference<Disposable> implements Observer<Object> {
            private static final long serialVersionUID = 3254781284376480842L;

            InnerRepeatObserver() {
            }

            public void onSubscribe(Disposable d) {
                DisposableHelper.setOnce(this, d);
            }

            public void onNext(Object t) {
                RepeatWhenObserver.this.innerNext();
            }

            public void onError(Throwable e) {
                RepeatWhenObserver.this.innerError(e);
            }

            public void onComplete() {
                RepeatWhenObserver.this.innerComplete();
            }
        }

        RepeatWhenObserver(Observer<? super T> actual2, Subject<Throwable> signaller2, ObservableSource<T> source2) {
            this.actual = actual2;
            this.signaller = signaller2;
            this.source = source2;
        }

        public void onSubscribe(Disposable d) {
            DisposableHelper.replace(this.f339d, d);
        }

        public void onNext(T t) {
            HalfSerializer.onNext(this.actual, t, (AtomicInteger) this, this.error);
        }

        public void onError(Throwable e) {
            this.active = false;
            this.signaller.onNext(e);
        }

        public void onComplete() {
            DisposableHelper.dispose(this.inner);
            HalfSerializer.onComplete(this.actual, (AtomicInteger) this, this.error);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) this.f339d.get());
        }

        public void dispose() {
            DisposableHelper.dispose(this.f339d);
            DisposableHelper.dispose(this.inner);
        }

        /* access modifiers changed from: 0000 */
        public void innerNext() {
            subscribeNext();
        }

        /* access modifiers changed from: 0000 */
        public void innerError(Throwable ex) {
            DisposableHelper.dispose(this.f339d);
            HalfSerializer.onError(this.actual, ex, (AtomicInteger) this, this.error);
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete() {
            DisposableHelper.dispose(this.f339d);
            HalfSerializer.onComplete(this.actual, (AtomicInteger) this, this.error);
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            if (this.wip.getAndIncrement() == 0) {
                while (!isDisposed()) {
                    if (!this.active) {
                        this.active = true;
                        this.source.subscribe(this);
                    }
                    if (this.wip.decrementAndGet() == 0) {
                    }
                }
            }
        }
    }

    public ObservableRetryWhen(ObservableSource<T> source, Function<? super Observable<Throwable>, ? extends ObservableSource<?>> handler2) {
        super(source);
        this.handler = handler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        Subject<Throwable> signaller = PublishSubject.create().toSerialized();
        try {
            ObservableSource<?> other = (ObservableSource) ObjectHelper.requireNonNull(this.handler.apply(signaller), "The handler returned a null ObservableSource");
            RepeatWhenObserver<T> parent = new RepeatWhenObserver<>(observer, signaller, this.source);
            observer.onSubscribe(parent);
            other.subscribe(parent.inner);
            parent.subscribeNext();
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptyDisposable.error(ex, observer);
        }
    }
}
