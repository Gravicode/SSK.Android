package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.Scheduler;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.SequentialDisposable;
import p005io.reactivex.observables.ConnectableObservable;
import p005io.reactivex.plugins.RxJavaPlugins;
import p005io.reactivex.schedulers.Schedulers;

/* renamed from: io.reactivex.internal.operators.observable.ObservableRefCount */
public final class ObservableRefCount<T> extends Observable<T> {
    RefConnection connection;

    /* renamed from: n */
    final int f332n;
    final Scheduler scheduler;
    final ConnectableObservable<T> source;
    final long timeout;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableRefCount$RefConnection */
    static final class RefConnection extends AtomicReference<Disposable> implements Runnable, Consumer<Disposable> {
        private static final long serialVersionUID = -4552101107598366241L;
        boolean connected;
        final ObservableRefCount<?> parent;
        long subscriberCount;
        Disposable timer;

        RefConnection(ObservableRefCount<?> parent2) {
            this.parent = parent2;
        }

        public void run() {
            this.parent.timeout(this);
        }

        public void accept(Disposable t) throws Exception {
            DisposableHelper.replace(this, t);
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableRefCount$RefCountObserver */
    static final class RefCountObserver<T> extends AtomicBoolean implements Observer<T>, Disposable {
        private static final long serialVersionUID = -7419642935409022375L;
        final Observer<? super T> actual;
        final RefConnection connection;
        final ObservableRefCount<T> parent;
        Disposable upstream;

        RefCountObserver(Observer<? super T> actual2, ObservableRefCount<T> parent2, RefConnection connection2) {
            this.actual = actual2;
            this.parent = parent2;
            this.connection = connection2;
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            if (compareAndSet(false, true)) {
                this.parent.terminated(this.connection);
                this.actual.onError(t);
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            if (compareAndSet(false, true)) {
                this.parent.terminated(this.connection);
                this.actual.onComplete();
            }
        }

        public void dispose() {
            this.upstream.dispose();
            if (compareAndSet(false, true)) {
                this.parent.cancel(this.connection);
            }
        }

        public boolean isDisposed() {
            return this.upstream.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.upstream, d)) {
                this.upstream = d;
                this.actual.onSubscribe(this);
            }
        }
    }

    public ObservableRefCount(ConnectableObservable<T> source2) {
        this(source2, 1, 0, TimeUnit.NANOSECONDS, Schedulers.trampoline());
    }

    public ObservableRefCount(ConnectableObservable<T> source2, int n, long timeout2, TimeUnit unit2, Scheduler scheduler2) {
        this.source = source2;
        this.f332n = n;
        this.timeout = timeout2;
        this.unit = unit2;
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> s) {
        RefConnection conn;
        boolean connect = false;
        synchronized (this) {
            conn = this.connection;
            if (conn == null) {
                conn = new RefConnection(this);
                this.connection = conn;
            }
            long c = conn.subscriberCount;
            if (c == 0 && conn.timer != null) {
                conn.timer.dispose();
            }
            conn.subscriberCount = c + 1;
            if (!conn.connected && 1 + c == ((long) this.f332n)) {
                connect = true;
                conn.connected = true;
            }
        }
        this.source.subscribe((Observer<? super T>) new RefCountObserver<Object>(s, this, conn));
        if (connect) {
            this.source.connect(conn);
        }
    }

    /* access modifiers changed from: 0000 */
    public void cancel(RefConnection rc) {
        synchronized (this) {
            if (this.connection != null) {
                long c = rc.subscriberCount - 1;
                rc.subscriberCount = c;
                if (c == 0) {
                    if (rc.connected) {
                        if (this.timeout == 0) {
                            timeout(rc);
                            return;
                        }
                        SequentialDisposable sd = new SequentialDisposable();
                        rc.timer = sd;
                        sd.replace(this.scheduler.scheduleDirect(rc, this.timeout, this.unit));
                    }
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void terminated(RefConnection rc) {
        synchronized (this) {
            if (this.connection != null) {
                this.connection = null;
                if (rc.timer != null) {
                    rc.timer.dispose();
                }
                if (this.source instanceof Disposable) {
                    ((Disposable) this.source).dispose();
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void timeout(RefConnection rc) {
        synchronized (this) {
            if (rc.subscriberCount == 0 && rc == this.connection) {
                this.connection = null;
                DisposableHelper.dispose(rc);
                if (this.source instanceof Disposable) {
                    ((Disposable) this.source).dispose();
                }
            }
        }
    }
}
