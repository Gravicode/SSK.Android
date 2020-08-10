package p005io.reactivex.internal.operators.observable;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.observers.QueueDrainObserver;
import p005io.reactivex.internal.queue.MpscLinkedQueue;
import p005io.reactivex.observers.DisposableObserver;
import p005io.reactivex.observers.SerializedObserver;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableBufferBoundarySupplier */
public final class ObservableBufferBoundarySupplier<T, U extends Collection<? super T>, B> extends AbstractObservableWithUpstream<T, U> {
    final Callable<? extends ObservableSource<B>> boundarySupplier;
    final Callable<U> bufferSupplier;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableBufferBoundarySupplier$BufferBoundaryObserver */
    static final class BufferBoundaryObserver<T, U extends Collection<? super T>, B> extends DisposableObserver<B> {
        boolean once;
        final BufferBoundarySupplierObserver<T, U, B> parent;

        BufferBoundaryObserver(BufferBoundarySupplierObserver<T, U, B> parent2) {
            this.parent = parent2;
        }

        public void onNext(B b) {
            if (!this.once) {
                this.once = true;
                dispose();
                this.parent.next();
            }
        }

        public void onError(Throwable t) {
            if (this.once) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.once = true;
            this.parent.onError(t);
        }

        public void onComplete() {
            if (!this.once) {
                this.once = true;
                this.parent.next();
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableBufferBoundarySupplier$BufferBoundarySupplierObserver */
    static final class BufferBoundarySupplierObserver<T, U extends Collection<? super T>, B> extends QueueDrainObserver<T, U, U> implements Observer<T>, Disposable {
        final Callable<? extends ObservableSource<B>> boundarySupplier;
        U buffer;
        final Callable<U> bufferSupplier;
        final AtomicReference<Disposable> other = new AtomicReference<>();

        /* renamed from: s */
        Disposable f278s;

        BufferBoundarySupplierObserver(Observer<? super U> actual, Callable<U> bufferSupplier2, Callable<? extends ObservableSource<B>> boundarySupplier2) {
            super(actual, new MpscLinkedQueue());
            this.bufferSupplier = bufferSupplier2;
            this.boundarySupplier = boundarySupplier2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f278s, s)) {
                this.f278s = s;
                Observer<? super U> actual = this.actual;
                try {
                    this.buffer = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
                    try {
                        ObservableSource observableSource = (ObservableSource) ObjectHelper.requireNonNull(this.boundarySupplier.call(), "The boundary ObservableSource supplied is null");
                        BufferBoundaryObserver<T, U, B> bs = new BufferBoundaryObserver<>(this);
                        this.other.set(bs);
                        actual.onSubscribe(this);
                        if (!this.cancelled) {
                            observableSource.subscribe(bs);
                        }
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        this.cancelled = true;
                        s.dispose();
                        EmptyDisposable.error(ex, actual);
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.cancelled = true;
                    s.dispose();
                    EmptyDisposable.error(e, actual);
                }
            }
        }

        public void onNext(T t) {
            synchronized (this) {
                U b = this.buffer;
                if (b != null) {
                    b.add(t);
                }
            }
        }

        public void onError(Throwable t) {
            dispose();
            this.actual.onError(t);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0019, code lost:
            p005io.reactivex.internal.util.QueueDrainHelper.drainLoop(r4.queue, r4.actual, false, r4, r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0021, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x000b, code lost:
            r4.queue.offer(r0);
            r4.done = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0017, code lost:
            if (enter() == false) goto L_0x0021;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onComplete() {
            /*
                r4 = this;
                monitor-enter(r4)
                U r0 = r4.buffer     // Catch:{ all -> 0x0022 }
                if (r0 != 0) goto L_0x0007
                monitor-exit(r4)     // Catch:{ all -> 0x0022 }
                return
            L_0x0007:
                r1 = 0
                r4.buffer = r1     // Catch:{ all -> 0x0022 }
                monitor-exit(r4)     // Catch:{ all -> 0x0022 }
                io.reactivex.internal.fuseable.SimplePlainQueue r1 = r4.queue
                r1.offer(r0)
                r1 = 1
                r4.done = r1
                boolean r1 = r4.enter()
                if (r1 == 0) goto L_0x0021
                io.reactivex.internal.fuseable.SimplePlainQueue r1 = r4.queue
                io.reactivex.Observer r2 = r4.actual
                r3 = 0
                p005io.reactivex.internal.util.QueueDrainHelper.drainLoop(r1, r2, r3, r4, r4)
            L_0x0021:
                return
            L_0x0022:
                r0 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0022 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.observable.ObservableBufferBoundarySupplier.BufferBoundarySupplierObserver.onComplete():void");
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.f278s.dispose();
                disposeOther();
                if (enter()) {
                    this.queue.clear();
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void disposeOther() {
            DisposableHelper.dispose(this.other);
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0037, code lost:
            r1.subscribe(r2);
            fastPathEmit(r3, false, r5);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void next() {
            /*
                r5 = this;
                java.util.concurrent.Callable<U> r0 = r5.bufferSupplier     // Catch:{ Throwable -> 0x0055 }
                java.lang.Object r0 = r0.call()     // Catch:{ Throwable -> 0x0055 }
                java.lang.String r1 = "The buffer supplied is null"
                java.lang.Object r0 = p005io.reactivex.internal.functions.ObjectHelper.requireNonNull(r0, r1)     // Catch:{ Throwable -> 0x0055 }
                java.util.Collection r0 = (java.util.Collection) r0     // Catch:{ Throwable -> 0x0055 }
                java.util.concurrent.Callable<? extends io.reactivex.ObservableSource<B>> r1 = r5.boundarySupplier     // Catch:{ Throwable -> 0x0043 }
                java.lang.Object r1 = r1.call()     // Catch:{ Throwable -> 0x0043 }
                java.lang.String r2 = "The boundary ObservableSource supplied is null"
                java.lang.Object r1 = p005io.reactivex.internal.functions.ObjectHelper.requireNonNull(r1, r2)     // Catch:{ Throwable -> 0x0043 }
                io.reactivex.ObservableSource r1 = (p005io.reactivex.ObservableSource) r1     // Catch:{ Throwable -> 0x0043 }
                io.reactivex.internal.operators.observable.ObservableBufferBoundarySupplier$BufferBoundaryObserver r2 = new io.reactivex.internal.operators.observable.ObservableBufferBoundarySupplier$BufferBoundaryObserver
                r2.<init>(r5)
                java.util.concurrent.atomic.AtomicReference<io.reactivex.disposables.Disposable> r3 = r5.other
                boolean r3 = p005io.reactivex.internal.disposables.DisposableHelper.replace(r3, r2)
                if (r3 == 0) goto L_0x0042
                monitor-enter(r5)
                U r3 = r5.buffer     // Catch:{ all -> 0x003f }
                if (r3 != 0) goto L_0x0034
                monitor-exit(r5)     // Catch:{ all -> 0x003f }
                return
            L_0x0034:
                r5.buffer = r0     // Catch:{ all -> 0x003f }
                monitor-exit(r5)     // Catch:{ all -> 0x003f }
                r1.subscribe(r2)
                r4 = 0
                r5.fastPathEmit(r3, r4, r5)
                goto L_0x0042
            L_0x003f:
                r3 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x003f }
                throw r3
            L_0x0042:
                return
            L_0x0043:
                r1 = move-exception
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r1)
                r2 = 1
                r5.cancelled = r2
                io.reactivex.disposables.Disposable r2 = r5.f278s
                r2.dispose()
                io.reactivex.Observer r2 = r5.actual
                r2.onError(r1)
                return
            L_0x0055:
                r0 = move-exception
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r0)
                r5.dispose()
                io.reactivex.Observer r1 = r5.actual
                r1.onError(r0)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.observable.ObservableBufferBoundarySupplier.BufferBoundarySupplierObserver.next():void");
        }

        public void accept(Observer<? super U> observer, U v) {
            this.actual.onNext(v);
        }
    }

    public ObservableBufferBoundarySupplier(ObservableSource<T> source, Callable<? extends ObservableSource<B>> boundarySupplier2, Callable<U> bufferSupplier2) {
        super(source);
        this.boundarySupplier = boundarySupplier2;
        this.bufferSupplier = bufferSupplier2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super U> t) {
        this.source.subscribe(new BufferBoundarySupplierObserver(new SerializedObserver(t), this.bufferSupplier, this.boundarySupplier));
    }
}
