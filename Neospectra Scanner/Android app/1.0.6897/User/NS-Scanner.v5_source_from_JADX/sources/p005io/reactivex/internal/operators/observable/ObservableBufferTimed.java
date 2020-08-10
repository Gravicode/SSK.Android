package p005io.reactivex.internal.operators.observable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Scheduler.Worker;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.observers.QueueDrainObserver;
import p005io.reactivex.internal.queue.MpscLinkedQueue;
import p005io.reactivex.internal.util.QueueDrainHelper;
import p005io.reactivex.observers.SerializedObserver;

/* renamed from: io.reactivex.internal.operators.observable.ObservableBufferTimed */
public final class ObservableBufferTimed<T, U extends Collection<? super T>> extends AbstractObservableWithUpstream<T, U> {
    final Callable<U> bufferSupplier;
    final int maxSize;
    final boolean restartTimerOnMaxSize;
    final Scheduler scheduler;
    final long timeskip;
    final long timespan;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableBufferTimed$BufferExactBoundedObserver */
    static final class BufferExactBoundedObserver<T, U extends Collection<? super T>> extends QueueDrainObserver<T, U, U> implements Runnable, Disposable {
        U buffer;
        final Callable<U> bufferSupplier;
        long consumerIndex;
        final int maxSize;
        long producerIndex;
        final boolean restartTimerOnMaxSize;

        /* renamed from: s */
        Disposable f280s;
        Disposable timer;
        final long timespan;
        final TimeUnit unit;

        /* renamed from: w */
        final Worker f281w;

        BufferExactBoundedObserver(Observer<? super U> actual, Callable<U> bufferSupplier2, long timespan2, TimeUnit unit2, int maxSize2, boolean restartOnMaxSize, Worker w) {
            super(actual, new MpscLinkedQueue());
            this.bufferSupplier = bufferSupplier2;
            this.timespan = timespan2;
            this.unit = unit2;
            this.maxSize = maxSize2;
            this.restartTimerOnMaxSize = restartOnMaxSize;
            this.f281w = w;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f280s, s)) {
                this.f280s = s;
                try {
                    this.buffer = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
                    this.actual.onSubscribe(this);
                    this.timer = this.f281w.schedulePeriodically(this, this.timespan, this.timespan, this.unit);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    s.dispose();
                    EmptyDisposable.error(e, this.actual);
                    this.f281w.dispose();
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0021, code lost:
            if (r9.restartTimerOnMaxSize == false) goto L_0x0028;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0023, code lost:
            r9.timer.dispose();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0028, code lost:
            fastPathOrderedEmit(r0, false, r9);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
            r1 = (java.util.Collection) p005io.reactivex.internal.functions.ObjectHelper.requireNonNull(r9.bufferSupplier.call(), "The buffer supplied is null");
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x003b, code lost:
            monitor-enter(r9);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
            r9.buffer = r1;
            r9.consumerIndex++;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0044, code lost:
            monitor-exit(r9);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0047, code lost:
            if (r9.restartTimerOnMaxSize == false) goto L_0x0058;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0049, code lost:
            r9.timer = r9.f281w.schedulePeriodically(r9, r9.timespan, r9.timespan, r9.unit);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0058, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x005c, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x005d, code lost:
            p005io.reactivex.exceptions.Exceptions.throwIfFatal(r1);
            r9.actual.onError(r1);
            dispose();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0068, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r10) {
            /*
                r9 = this;
                monitor-enter(r9)
                U r0 = r9.buffer     // Catch:{ all -> 0x0069 }
                if (r0 != 0) goto L_0x0007
                monitor-exit(r9)     // Catch:{ all -> 0x0069 }
                return
            L_0x0007:
                r0.add(r10)     // Catch:{ all -> 0x0069 }
                int r1 = r0.size()     // Catch:{ all -> 0x0069 }
                int r2 = r9.maxSize     // Catch:{ all -> 0x0069 }
                if (r1 >= r2) goto L_0x0014
                monitor-exit(r9)     // Catch:{ all -> 0x0069 }
                return
            L_0x0014:
                r1 = 0
                r9.buffer = r1     // Catch:{ all -> 0x0069 }
                long r1 = r9.producerIndex     // Catch:{ all -> 0x0069 }
                r3 = 1
                long r1 = r1 + r3
                r9.producerIndex = r1     // Catch:{ all -> 0x0069 }
                monitor-exit(r9)     // Catch:{ all -> 0x0069 }
                boolean r1 = r9.restartTimerOnMaxSize
                if (r1 == 0) goto L_0x0028
                io.reactivex.disposables.Disposable r1 = r9.timer
                r1.dispose()
            L_0x0028:
                r1 = 0
                r9.fastPathOrderedEmit(r0, r1, r9)
                java.util.concurrent.Callable<U> r1 = r9.bufferSupplier     // Catch:{ Throwable -> 0x005c }
                java.lang.Object r1 = r1.call()     // Catch:{ Throwable -> 0x005c }
                java.lang.String r2 = "The buffer supplied is null"
                java.lang.Object r1 = p005io.reactivex.internal.functions.ObjectHelper.requireNonNull(r1, r2)     // Catch:{ Throwable -> 0x005c }
                java.util.Collection r1 = (java.util.Collection) r1     // Catch:{ Throwable -> 0x005c }
                monitor-enter(r9)
                r9.buffer = r1     // Catch:{ all -> 0x0059 }
                long r5 = r9.consumerIndex     // Catch:{ all -> 0x0059 }
                r0 = 0
                long r5 = r5 + r3
                r9.consumerIndex = r5     // Catch:{ all -> 0x0059 }
                monitor-exit(r9)     // Catch:{ all -> 0x0059 }
                boolean r0 = r9.restartTimerOnMaxSize
                if (r0 == 0) goto L_0x0058
                io.reactivex.Scheduler$Worker r2 = r9.f281w
                long r4 = r9.timespan
                long r6 = r9.timespan
                java.util.concurrent.TimeUnit r8 = r9.unit
                r3 = r9
                io.reactivex.disposables.Disposable r0 = r2.schedulePeriodically(r3, r4, r6, r8)
                r9.timer = r0
            L_0x0058:
                return
            L_0x0059:
                r0 = move-exception
                monitor-exit(r9)     // Catch:{ all -> 0x0059 }
                throw r0
            L_0x005c:
                r1 = move-exception
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r1)
                io.reactivex.Observer r2 = r9.actual
                r2.onError(r1)
                r9.dispose()
                return
            L_0x0069:
                r0 = move-exception
                monitor-exit(r9)     // Catch:{ all -> 0x0069 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.observable.ObservableBufferTimed.BufferExactBoundedObserver.onNext(java.lang.Object):void");
        }

        public void onError(Throwable t) {
            synchronized (this) {
                this.buffer = null;
            }
            this.actual.onError(t);
            this.f281w.dispose();
        }

        public void onComplete() {
            U b;
            this.f281w.dispose();
            synchronized (this) {
                b = this.buffer;
                this.buffer = null;
            }
            this.queue.offer(b);
            this.done = true;
            if (enter()) {
                QueueDrainHelper.drainLoop(this.queue, this.actual, false, this, this);
            }
        }

        public void accept(Observer<? super U> a, U v) {
            a.onNext(v);
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.f280s.dispose();
                this.f281w.dispose();
                synchronized (this) {
                    this.buffer = null;
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void run() {
            try {
                U next = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null buffer");
                synchronized (this) {
                    U current = this.buffer;
                    if (current != null) {
                        if (this.producerIndex == this.consumerIndex) {
                            this.buffer = next;
                            fastPathOrderedEmit(current, false, this);
                        }
                    }
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                dispose();
                this.actual.onError(e);
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableBufferTimed$BufferExactUnboundedObserver */
    static final class BufferExactUnboundedObserver<T, U extends Collection<? super T>> extends QueueDrainObserver<T, U, U> implements Runnable, Disposable {
        U buffer;
        final Callable<U> bufferSupplier;

        /* renamed from: s */
        Disposable f282s;
        final Scheduler scheduler;
        final AtomicReference<Disposable> timer = new AtomicReference<>();
        final long timespan;
        final TimeUnit unit;

        BufferExactUnboundedObserver(Observer<? super U> actual, Callable<U> bufferSupplier2, long timespan2, TimeUnit unit2, Scheduler scheduler2) {
            super(actual, new MpscLinkedQueue());
            this.bufferSupplier = bufferSupplier2;
            this.timespan = timespan2;
            this.unit = unit2;
            this.scheduler = scheduler2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f282s, s)) {
                this.f282s = s;
                try {
                    this.buffer = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
                    this.actual.onSubscribe(this);
                    if (!this.cancelled) {
                        Disposable d = this.scheduler.schedulePeriodicallyDirect(this, this.timespan, this.timespan, this.unit);
                        if (!this.timer.compareAndSet(null, d)) {
                            d.dispose();
                        }
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    dispose();
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
            synchronized (this) {
                this.buffer = null;
            }
            this.actual.onError(t);
            DisposableHelper.dispose(this.timer);
        }

        public void onComplete() {
            U b;
            synchronized (this) {
                b = this.buffer;
                this.buffer = null;
            }
            if (b != null) {
                this.queue.offer(b);
                this.done = true;
                if (enter()) {
                    QueueDrainHelper.drainLoop(this.queue, this.actual, false, null, this);
                }
            }
            DisposableHelper.dispose(this.timer);
        }

        public void dispose() {
            DisposableHelper.dispose(this.timer);
            this.f282s.dispose();
        }

        public boolean isDisposed() {
            return this.timer.get() == DisposableHelper.DISPOSED;
        }

        public void run() {
            U current;
            try {
                U next = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null buffer");
                synchronized (this) {
                    current = this.buffer;
                    if (current != null) {
                        this.buffer = next;
                    }
                }
                if (current == null) {
                    DisposableHelper.dispose(this.timer);
                } else {
                    fastPathEmit(current, false, this);
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                this.actual.onError(e);
                dispose();
            }
        }

        public void accept(Observer<? super U> observer, U v) {
            this.actual.onNext(v);
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableBufferTimed$BufferSkipBoundedObserver */
    static final class BufferSkipBoundedObserver<T, U extends Collection<? super T>> extends QueueDrainObserver<T, U, U> implements Runnable, Disposable {
        final Callable<U> bufferSupplier;
        final List<U> buffers = new LinkedList();

        /* renamed from: s */
        Disposable f283s;
        final long timeskip;
        final long timespan;
        final TimeUnit unit;

        /* renamed from: w */
        final Worker f284w;

        /* renamed from: io.reactivex.internal.operators.observable.ObservableBufferTimed$BufferSkipBoundedObserver$RemoveFromBuffer */
        final class RemoveFromBuffer implements Runnable {

            /* renamed from: b */
            private final U f285b;

            RemoveFromBuffer(U b) {
                this.f285b = b;
            }

            public void run() {
                synchronized (BufferSkipBoundedObserver.this) {
                    BufferSkipBoundedObserver.this.buffers.remove(this.f285b);
                }
                BufferSkipBoundedObserver.this.fastPathOrderedEmit(this.f285b, false, BufferSkipBoundedObserver.this.f284w);
            }
        }

        /* renamed from: io.reactivex.internal.operators.observable.ObservableBufferTimed$BufferSkipBoundedObserver$RemoveFromBufferEmit */
        final class RemoveFromBufferEmit implements Runnable {
            private final U buffer;

            RemoveFromBufferEmit(U buffer2) {
                this.buffer = buffer2;
            }

            public void run() {
                synchronized (BufferSkipBoundedObserver.this) {
                    BufferSkipBoundedObserver.this.buffers.remove(this.buffer);
                }
                BufferSkipBoundedObserver.this.fastPathOrderedEmit(this.buffer, false, BufferSkipBoundedObserver.this.f284w);
            }
        }

        BufferSkipBoundedObserver(Observer<? super U> actual, Callable<U> bufferSupplier2, long timespan2, long timeskip2, TimeUnit unit2, Worker w) {
            super(actual, new MpscLinkedQueue());
            this.bufferSupplier = bufferSupplier2;
            this.timespan = timespan2;
            this.timeskip = timeskip2;
            this.unit = unit2;
            this.f284w = w;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f283s, s)) {
                this.f283s = s;
                try {
                    U b = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
                    this.buffers.add(b);
                    this.actual.onSubscribe(this);
                    this.f284w.schedulePeriodically(this, this.timeskip, this.timeskip, this.unit);
                    this.f284w.schedule(new RemoveFromBufferEmit(b), this.timespan, this.unit);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    s.dispose();
                    EmptyDisposable.error(e, this.actual);
                    this.f284w.dispose();
                }
            }
        }

        public void onNext(T t) {
            synchronized (this) {
                for (U b : this.buffers) {
                    b.add(t);
                }
            }
        }

        public void onError(Throwable t) {
            this.done = true;
            clear();
            this.actual.onError(t);
            this.f284w.dispose();
        }

        public void onComplete() {
            List<U> bs;
            synchronized (this) {
                bs = new ArrayList<>(this.buffers);
                this.buffers.clear();
            }
            for (U b : bs) {
                this.queue.offer(b);
            }
            this.done = true;
            if (enter()) {
                QueueDrainHelper.drainLoop(this.queue, this.actual, false, this.f284w, this);
            }
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                clear();
                this.f283s.dispose();
                this.f284w.dispose();
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void clear() {
            synchronized (this) {
                this.buffers.clear();
            }
        }

        public void run() {
            if (!this.cancelled) {
                try {
                    U b = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null buffer");
                    synchronized (this) {
                        if (!this.cancelled) {
                            this.buffers.add(b);
                            this.f284w.schedule(new RemoveFromBuffer(b), this.timespan, this.unit);
                        }
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.actual.onError(e);
                    dispose();
                }
            }
        }

        public void accept(Observer<? super U> a, U v) {
            a.onNext(v);
        }
    }

    public ObservableBufferTimed(ObservableSource<T> source, long timespan2, long timeskip2, TimeUnit unit2, Scheduler scheduler2, Callable<U> bufferSupplier2, int maxSize2, boolean restartTimerOnMaxSize2) {
        super(source);
        this.timespan = timespan2;
        this.timeskip = timeskip2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.bufferSupplier = bufferSupplier2;
        this.maxSize = maxSize2;
        this.restartTimerOnMaxSize = restartTimerOnMaxSize2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super U> t) {
        if (this.timespan == this.timeskip && this.maxSize == Integer.MAX_VALUE) {
            ObservableSource observableSource = this.source;
            BufferExactUnboundedObserver bufferExactUnboundedObserver = new BufferExactUnboundedObserver(new SerializedObserver(t), this.bufferSupplier, this.timespan, this.unit, this.scheduler);
            observableSource.subscribe(bufferExactUnboundedObserver);
            return;
        }
        Worker w = this.scheduler.createWorker();
        if (this.timespan == this.timeskip) {
            ObservableSource observableSource2 = this.source;
            BufferExactBoundedObserver bufferExactBoundedObserver = new BufferExactBoundedObserver(new SerializedObserver(t), this.bufferSupplier, this.timespan, this.unit, this.maxSize, this.restartTimerOnMaxSize, w);
            observableSource2.subscribe(bufferExactBoundedObserver);
            return;
        }
        ObservableSource observableSource3 = this.source;
        BufferSkipBoundedObserver bufferSkipBoundedObserver = new BufferSkipBoundedObserver(new SerializedObserver(t), this.bufferSupplier, this.timespan, this.timeskip, this.unit, w);
        observableSource3.subscribe(bufferSkipBoundedObserver);
    }
}
