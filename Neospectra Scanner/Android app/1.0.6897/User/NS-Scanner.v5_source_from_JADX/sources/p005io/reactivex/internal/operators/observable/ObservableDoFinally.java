package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Action;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.fuseable.QueueDisposable;
import p005io.reactivex.internal.observers.BasicIntQueueDisposable;
import p005io.reactivex.plugins.RxJavaPlugins;

@Experimental
/* renamed from: io.reactivex.internal.operators.observable.ObservableDoFinally */
public final class ObservableDoFinally<T> extends AbstractObservableWithUpstream<T, T> {
    final Action onFinally;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableDoFinally$DoFinallyObserver */
    static final class DoFinallyObserver<T> extends BasicIntQueueDisposable<T> implements Observer<T> {
        private static final long serialVersionUID = 4109457741734051389L;
        final Observer<? super T> actual;

        /* renamed from: d */
        Disposable f302d;
        final Action onFinally;

        /* renamed from: qd */
        QueueDisposable<T> f303qd;
        boolean syncFused;

        DoFinallyObserver(Observer<? super T> actual2, Action onFinally2) {
            this.actual = actual2;
            this.onFinally = onFinally2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f302d, d)) {
                this.f302d = d;
                if (d instanceof QueueDisposable) {
                    this.f303qd = (QueueDisposable) d;
                }
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
            runFinally();
        }

        public void onComplete() {
            this.actual.onComplete();
            runFinally();
        }

        public void dispose() {
            this.f302d.dispose();
            runFinally();
        }

        public boolean isDisposed() {
            return this.f302d.isDisposed();
        }

        public int requestFusion(int mode) {
            QueueDisposable<T> qd = this.f303qd;
            boolean z = false;
            if (qd == null || (mode & 4) != 0) {
                return 0;
            }
            int m = qd.requestFusion(mode);
            if (m != 0) {
                if (m == 1) {
                    z = true;
                }
                this.syncFused = z;
            }
            return m;
        }

        public void clear() {
            this.f303qd.clear();
        }

        public boolean isEmpty() {
            return this.f303qd.isEmpty();
        }

        @Nullable
        public T poll() throws Exception {
            T v = this.f303qd.poll();
            if (v == null && this.syncFused) {
                runFinally();
            }
            return v;
        }

        /* access modifiers changed from: 0000 */
        public void runFinally() {
            if (compareAndSet(0, 1)) {
                try {
                    this.onFinally.run();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    RxJavaPlugins.onError(ex);
                }
            }
        }
    }

    public ObservableDoFinally(ObservableSource<T> source, Action onFinally2) {
        super(source);
        this.onFinally = onFinally2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> s) {
        this.source.subscribe(new DoFinallyObserver(s, this.onFinally));
    }
}
