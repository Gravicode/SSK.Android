package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.fuseable.SimplePlainQueue;
import p005io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableMergeWithMaybe */
public final class ObservableMergeWithMaybe<T> extends AbstractObservableWithUpstream<T, T> {
    final MaybeSource<? extends T> other;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableMergeWithMaybe$MergeWithObserver */
    static final class MergeWithObserver<T> extends AtomicInteger implements Observer<T>, Disposable {
        static final int OTHER_STATE_CONSUMED_OR_EMPTY = 2;
        static final int OTHER_STATE_HAS_VALUE = 1;
        private static final long serialVersionUID = -4592979584110982903L;
        final Observer<? super T> actual;
        volatile boolean disposed;
        final AtomicThrowable error = new AtomicThrowable();
        final AtomicReference<Disposable> mainDisposable = new AtomicReference<>();
        volatile boolean mainDone;
        final OtherObserver<T> otherObserver = new OtherObserver<>(this);
        volatile int otherState;
        volatile SimplePlainQueue<T> queue;
        T singleItem;

        /* renamed from: io.reactivex.internal.operators.observable.ObservableMergeWithMaybe$MergeWithObserver$OtherObserver */
        static final class OtherObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T> {
            private static final long serialVersionUID = -2935427570954647017L;
            final MergeWithObserver<T> parent;

            OtherObserver(MergeWithObserver<T> parent2) {
                this.parent = parent2;
            }

            public void onSubscribe(Disposable d) {
                DisposableHelper.setOnce(this, d);
            }

            public void onSuccess(T t) {
                this.parent.otherSuccess(t);
            }

            public void onError(Throwable e) {
                this.parent.otherError(e);
            }

            public void onComplete() {
                this.parent.otherComplete();
            }
        }

        MergeWithObserver(Observer<? super T> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Disposable d) {
            DisposableHelper.setOnce(this.mainDisposable, d);
        }

        public void onNext(T t) {
            if (compareAndSet(0, 1)) {
                this.actual.onNext(t);
                if (decrementAndGet() == 0) {
                    return;
                }
            } else {
                getOrCreateQueue().offer(t);
                if (getAndIncrement() != 0) {
                    return;
                }
            }
            drainLoop();
        }

        public void onError(Throwable ex) {
            if (this.error.addThrowable(ex)) {
                DisposableHelper.dispose(this.mainDisposable);
                drain();
                return;
            }
            RxJavaPlugins.onError(ex);
        }

        public void onComplete() {
            this.mainDone = true;
            drain();
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) this.mainDisposable.get());
        }

        public void dispose() {
            this.disposed = true;
            DisposableHelper.dispose(this.mainDisposable);
            DisposableHelper.dispose(this.otherObserver);
            if (getAndIncrement() == 0) {
                this.queue = null;
                this.singleItem = null;
            }
        }

        /* access modifiers changed from: 0000 */
        public void otherSuccess(T value) {
            if (compareAndSet(0, 1)) {
                this.actual.onNext(value);
                this.otherState = 2;
            } else {
                this.singleItem = value;
                this.otherState = 1;
                if (getAndIncrement() != 0) {
                    return;
                }
            }
            drainLoop();
        }

        /* access modifiers changed from: 0000 */
        public void otherError(Throwable ex) {
            if (this.error.addThrowable(ex)) {
                DisposableHelper.dispose(this.mainDisposable);
                drain();
                return;
            }
            RxJavaPlugins.onError(ex);
        }

        /* access modifiers changed from: 0000 */
        public void otherComplete() {
            this.otherState = 2;
            drain();
        }

        /* access modifiers changed from: 0000 */
        public SimplePlainQueue<T> getOrCreateQueue() {
            SimplePlainQueue<T> q = this.queue;
            if (q != null) {
                return q;
            }
            SimplePlainQueue<T> q2 = new SpscLinkedArrayQueue<>(Observable.bufferSize());
            this.queue = q2;
            return q2;
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                drainLoop();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drainLoop() {
            Observer<? super T> actual2 = this.actual;
            int missed = 1;
            while (!this.disposed) {
                if (this.error.get() != null) {
                    this.singleItem = null;
                    this.queue = null;
                    actual2.onError(this.error.terminate());
                    return;
                }
                int os = this.otherState;
                if (os == 1) {
                    T v = this.singleItem;
                    this.singleItem = null;
                    this.otherState = 2;
                    os = 2;
                    actual2.onNext(v);
                }
                boolean d = this.mainDone;
                SimplePlainQueue<T> q = this.queue;
                Object poll = q != null ? q.poll() : null;
                boolean empty = poll == null;
                if (d && empty && os == 2) {
                    this.queue = null;
                    actual2.onComplete();
                    return;
                } else if (empty) {
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                } else {
                    actual2.onNext(poll);
                }
            }
            this.singleItem = null;
            this.queue = null;
        }
    }

    public ObservableMergeWithMaybe(Observable<T> source, MaybeSource<? extends T> other2) {
        super(source);
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        MergeWithObserver<T> parent = new MergeWithObserver<>(observer);
        observer.onSubscribe(parent);
        this.source.subscribe(parent);
        this.other.subscribe(parent.otherObserver);
    }
}
