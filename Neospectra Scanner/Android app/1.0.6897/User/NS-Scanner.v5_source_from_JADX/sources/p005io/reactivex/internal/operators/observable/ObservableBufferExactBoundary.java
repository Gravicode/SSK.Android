package p005io.reactivex.internal.operators.observable;

import java.util.Collection;
import java.util.concurrent.Callable;
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

/* renamed from: io.reactivex.internal.operators.observable.ObservableBufferExactBoundary */
public final class ObservableBufferExactBoundary<T, U extends Collection<? super T>, B> extends AbstractObservableWithUpstream<T, U> {
    final ObservableSource<B> boundary;
    final Callable<U> bufferSupplier;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableBufferExactBoundary$BufferBoundaryObserver */
    static final class BufferBoundaryObserver<T, U extends Collection<? super T>, B> extends DisposableObserver<B> {
        final BufferExactBoundaryObserver<T, U, B> parent;

        BufferBoundaryObserver(BufferExactBoundaryObserver<T, U, B> parent2) {
            this.parent = parent2;
        }

        public void onNext(B b) {
            this.parent.next();
        }

        public void onError(Throwable t) {
            this.parent.onError(t);
        }

        public void onComplete() {
            this.parent.onComplete();
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableBufferExactBoundary$BufferExactBoundaryObserver */
    static final class BufferExactBoundaryObserver<T, U extends Collection<? super T>, B> extends QueueDrainObserver<T, U, U> implements Observer<T>, Disposable {
        final ObservableSource<B> boundary;
        U buffer;
        final Callable<U> bufferSupplier;
        Disposable other;

        /* renamed from: s */
        Disposable f279s;

        BufferExactBoundaryObserver(Observer<? super U> actual, Callable<U> bufferSupplier2, ObservableSource<B> boundary2) {
            super(actual, new MpscLinkedQueue());
            this.bufferSupplier = bufferSupplier2;
            this.boundary = boundary2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f279s, s)) {
                this.f279s = s;
                try {
                    this.buffer = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
                    BufferBoundaryObserver<T, U, B> bs = new BufferBoundaryObserver<>(this);
                    this.other = bs;
                    this.actual.onSubscribe(this);
                    if (!this.cancelled) {
                        this.boundary.subscribe(bs);
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.cancelled = true;
                    s.dispose();
                    EmptyDisposable.error(e, this.actual);
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
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.observable.ObservableBufferExactBoundary.BufferExactBoundaryObserver.onComplete():void");
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.other.dispose();
                this.f279s.dispose();
                if (enter()) {
                    this.queue.clear();
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void next() {
            try {
                U next = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
                synchronized (this) {
                    U b = this.buffer;
                    if (b != null) {
                        this.buffer = next;
                        fastPathEmit(b, false, this);
                    }
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                dispose();
                this.actual.onError(e);
            }
        }

        public void accept(Observer<? super U> observer, U v) {
            this.actual.onNext(v);
        }
    }

    public ObservableBufferExactBoundary(ObservableSource<T> source, ObservableSource<B> boundary2, Callable<U> bufferSupplier2) {
        super(source);
        this.boundary = boundary2;
        this.bufferSupplier = bufferSupplier2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super U> t) {
        this.source.subscribe(new BufferExactBoundaryObserver(new SerializedObserver(t), this.bufferSupplier, this.boundary));
    }
}
