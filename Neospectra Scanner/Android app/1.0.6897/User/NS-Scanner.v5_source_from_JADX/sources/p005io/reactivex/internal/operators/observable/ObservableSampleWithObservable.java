package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.observers.SerializedObserver;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSampleWithObservable */
public final class ObservableSampleWithObservable<T> extends AbstractObservableWithUpstream<T, T> {
    final boolean emitLast;
    final ObservableSource<?> other;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSampleWithObservable$SampleMainEmitLast */
    static final class SampleMainEmitLast<T> extends SampleMainObserver<T> {
        private static final long serialVersionUID = -3029755663834015785L;
        volatile boolean done;
        final AtomicInteger wip = new AtomicInteger();

        SampleMainEmitLast(Observer<? super T> actual, ObservableSource<?> other) {
            super(actual, other);
        }

        /* access modifiers changed from: 0000 */
        public void completeMain() {
            this.done = true;
            if (this.wip.getAndIncrement() == 0) {
                emit();
                this.actual.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void completeOther() {
            this.done = true;
            if (this.wip.getAndIncrement() == 0) {
                emit();
                this.actual.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void run() {
            if (this.wip.getAndIncrement() == 0) {
                do {
                    boolean d = this.done;
                    emit();
                    if (d) {
                        this.actual.onComplete();
                        return;
                    }
                } while (this.wip.decrementAndGet() != 0);
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSampleWithObservable$SampleMainNoLast */
    static final class SampleMainNoLast<T> extends SampleMainObserver<T> {
        private static final long serialVersionUID = -3029755663834015785L;

        SampleMainNoLast(Observer<? super T> actual, ObservableSource<?> other) {
            super(actual, other);
        }

        /* access modifiers changed from: 0000 */
        public void completeMain() {
            this.actual.onComplete();
        }

        /* access modifiers changed from: 0000 */
        public void completeOther() {
            this.actual.onComplete();
        }

        /* access modifiers changed from: 0000 */
        public void run() {
            emit();
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSampleWithObservable$SampleMainObserver */
    static abstract class SampleMainObserver<T> extends AtomicReference<T> implements Observer<T>, Disposable {
        private static final long serialVersionUID = -3517602651313910099L;
        final Observer<? super T> actual;
        final AtomicReference<Disposable> other = new AtomicReference<>();

        /* renamed from: s */
        Disposable f341s;
        final ObservableSource<?> sampler;

        /* access modifiers changed from: 0000 */
        public abstract void completeMain();

        /* access modifiers changed from: 0000 */
        public abstract void completeOther();

        /* access modifiers changed from: 0000 */
        public abstract void run();

        SampleMainObserver(Observer<? super T> actual2, ObservableSource<?> other2) {
            this.actual = actual2;
            this.sampler = other2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f341s, s)) {
                this.f341s = s;
                this.actual.onSubscribe(this);
                if (this.other.get() == null) {
                    this.sampler.subscribe(new SamplerObserver(this));
                }
            }
        }

        public void onNext(T t) {
            lazySet(t);
        }

        public void onError(Throwable t) {
            DisposableHelper.dispose(this.other);
            this.actual.onError(t);
        }

        public void onComplete() {
            DisposableHelper.dispose(this.other);
            completeMain();
        }

        /* access modifiers changed from: 0000 */
        public boolean setOther(Disposable o) {
            return DisposableHelper.setOnce(this.other, o);
        }

        public void dispose() {
            DisposableHelper.dispose(this.other);
            this.f341s.dispose();
        }

        public boolean isDisposed() {
            return this.other.get() == DisposableHelper.DISPOSED;
        }

        public void error(Throwable e) {
            this.f341s.dispose();
            this.actual.onError(e);
        }

        public void complete() {
            this.f341s.dispose();
            completeOther();
        }

        /* access modifiers changed from: 0000 */
        public void emit() {
            T value = getAndSet(null);
            if (value != null) {
                this.actual.onNext(value);
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSampleWithObservable$SamplerObserver */
    static final class SamplerObserver<T> implements Observer<Object> {
        final SampleMainObserver<T> parent;

        SamplerObserver(SampleMainObserver<T> parent2) {
            this.parent = parent2;
        }

        public void onSubscribe(Disposable s) {
            this.parent.setOther(s);
        }

        public void onNext(Object t) {
            this.parent.run();
        }

        public void onError(Throwable t) {
            this.parent.error(t);
        }

        public void onComplete() {
            this.parent.complete();
        }
    }

    public ObservableSampleWithObservable(ObservableSource<T> source, ObservableSource<?> other2, boolean emitLast2) {
        super(source);
        this.other = other2;
        this.emitLast = emitLast2;
    }

    public void subscribeActual(Observer<? super T> t) {
        SerializedObserver<T> serial = new SerializedObserver<>(t);
        if (this.emitLast) {
            this.source.subscribe(new SampleMainEmitLast(serial, this.other));
        } else {
            this.source.subscribe(new SampleMainNoLast(serial, this.other));
        }
    }
}
