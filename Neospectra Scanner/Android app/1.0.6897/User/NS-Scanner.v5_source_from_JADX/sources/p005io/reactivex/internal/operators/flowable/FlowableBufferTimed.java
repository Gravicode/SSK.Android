package p005io.reactivex.internal.operators.flowable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Scheduler.Worker;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.queue.MpscLinkedQueue;
import p005io.reactivex.internal.subscribers.QueueDrainSubscriber;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.QueueDrainHelper;
import p005io.reactivex.subscribers.SerializedSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableBufferTimed */
public final class FlowableBufferTimed<T, U extends Collection<? super T>> extends AbstractFlowableWithUpstream<T, U> {
    final Callable<U> bufferSupplier;
    final int maxSize;
    final boolean restartTimerOnMaxSize;
    final Scheduler scheduler;
    final long timeskip;
    final long timespan;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableBufferTimed$BufferExactBoundedSubscriber */
    static final class BufferExactBoundedSubscriber<T, U extends Collection<? super T>> extends QueueDrainSubscriber<T, U, U> implements Subscription, Runnable, Disposable {
        U buffer;
        final Callable<U> bufferSupplier;
        long consumerIndex;
        final int maxSize;
        long producerIndex;
        final boolean restartTimerOnMaxSize;

        /* renamed from: s */
        Subscription f127s;
        Disposable timer;
        final long timespan;
        final TimeUnit unit;

        /* renamed from: w */
        final Worker f128w;

        BufferExactBoundedSubscriber(Subscriber<? super U> actual, Callable<U> bufferSupplier2, long timespan2, TimeUnit unit2, int maxSize2, boolean restartOnMaxSize, Worker w) {
            super(actual, new MpscLinkedQueue());
            this.bufferSupplier = bufferSupplier2;
            this.timespan = timespan2;
            this.unit = unit2;
            this.maxSize = maxSize2;
            this.restartTimerOnMaxSize = restartOnMaxSize;
            this.f128w = w;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f127s, s)) {
                this.f127s = s;
                try {
                    this.buffer = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
                    this.actual.onSubscribe(this);
                    this.timer = this.f128w.schedulePeriodically(this, this.timespan, this.timespan, this.unit);
                    s.request(Long.MAX_VALUE);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.f128w.dispose();
                    s.cancel();
                    EmptySubscription.error(e, this.actual);
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
            fastPathOrderedEmitMax(r0, false, r9);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
            r1 = (java.util.Collection) p005io.reactivex.internal.functions.ObjectHelper.requireNonNull(r9.bufferSupplier.call(), "The supplied buffer is null");
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
            r9.timer = r9.f128w.schedulePeriodically(r9, r9.timespan, r9.timespan, r9.unit);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0058, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x005c, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x005d, code lost:
            p005io.reactivex.exceptions.Exceptions.throwIfFatal(r1);
            cancel();
            r9.actual.onError(r1);
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
                r9.fastPathOrderedEmitMax(r0, r1, r9)
                java.util.concurrent.Callable<U> r1 = r9.bufferSupplier     // Catch:{ Throwable -> 0x005c }
                java.lang.Object r1 = r1.call()     // Catch:{ Throwable -> 0x005c }
                java.lang.String r2 = "The supplied buffer is null"
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
                io.reactivex.Scheduler$Worker r2 = r9.f128w
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
                r9.cancel()
                org.reactivestreams.Subscriber r2 = r9.actual
                r2.onError(r1)
                return
            L_0x0069:
                r0 = move-exception
                monitor-exit(r9)     // Catch:{ all -> 0x0069 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.flowable.FlowableBufferTimed.BufferExactBoundedSubscriber.onNext(java.lang.Object):void");
        }

        public void onError(Throwable t) {
            synchronized (this) {
                this.buffer = null;
            }
            this.actual.onError(t);
            this.f128w.dispose();
        }

        public void onComplete() {
            U b;
            synchronized (this) {
                b = this.buffer;
                this.buffer = null;
            }
            this.queue.offer(b);
            this.done = true;
            if (enter()) {
                QueueDrainHelper.drainMaxLoop(this.queue, this.actual, false, this, this);
            }
            this.f128w.dispose();
        }

        public boolean accept(Subscriber<? super U> a, U v) {
            a.onNext(v);
            return true;
        }

        public void request(long n) {
            requested(n);
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                dispose();
            }
        }

        public void dispose() {
            synchronized (this) {
                this.buffer = null;
            }
            this.f127s.cancel();
            this.f128w.dispose();
        }

        public boolean isDisposed() {
            return this.f128w.isDisposed();
        }

        public void run() {
            try {
                U next = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
                synchronized (this) {
                    U current = this.buffer;
                    if (current != null) {
                        if (this.producerIndex == this.consumerIndex) {
                            this.buffer = next;
                            fastPathOrderedEmitMax(current, false, this);
                        }
                    }
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                cancel();
                this.actual.onError(e);
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableBufferTimed$BufferExactUnboundedSubscriber */
    static final class BufferExactUnboundedSubscriber<T, U extends Collection<? super T>> extends QueueDrainSubscriber<T, U, U> implements Subscription, Runnable, Disposable {
        U buffer;
        final Callable<U> bufferSupplier;

        /* renamed from: s */
        Subscription f129s;
        final Scheduler scheduler;
        final AtomicReference<Disposable> timer = new AtomicReference<>();
        final long timespan;
        final TimeUnit unit;

        BufferExactUnboundedSubscriber(Subscriber<? super U> actual, Callable<U> bufferSupplier2, long timespan2, TimeUnit unit2, Scheduler scheduler2) {
            super(actual, new MpscLinkedQueue());
            this.bufferSupplier = bufferSupplier2;
            this.timespan = timespan2;
            this.unit = unit2;
            this.scheduler = scheduler2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f129s, s)) {
                this.f129s = s;
                try {
                    this.buffer = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
                    this.actual.onSubscribe(this);
                    if (!this.cancelled) {
                        s.request(Long.MAX_VALUE);
                        Disposable d = this.scheduler.schedulePeriodicallyDirect(this, this.timespan, this.timespan, this.unit);
                        if (!this.timer.compareAndSet(null, d)) {
                            d.dispose();
                        }
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    cancel();
                    EmptySubscription.error(e, this.actual);
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
            DisposableHelper.dispose(this.timer);
            synchronized (this) {
                this.buffer = null;
            }
            this.actual.onError(t);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x001c, code lost:
            if (enter() == false) goto L_0x0026;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x001e, code lost:
            p005io.reactivex.internal.util.QueueDrainHelper.drainMaxLoop(r5.queue, r5.actual, false, null, r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0026, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0010, code lost:
            r5.queue.offer(r0);
            r5.done = true;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onComplete() {
            /*
                r5 = this;
                java.util.concurrent.atomic.AtomicReference<io.reactivex.disposables.Disposable> r0 = r5.timer
                p005io.reactivex.internal.disposables.DisposableHelper.dispose(r0)
                monitor-enter(r5)
                U r0 = r5.buffer     // Catch:{ all -> 0x0027 }
                if (r0 != 0) goto L_0x000c
                monitor-exit(r5)     // Catch:{ all -> 0x0027 }
                return
            L_0x000c:
                r1 = 0
                r5.buffer = r1     // Catch:{ all -> 0x0027 }
                monitor-exit(r5)     // Catch:{ all -> 0x0027 }
                io.reactivex.internal.fuseable.SimplePlainQueue r2 = r5.queue
                r2.offer(r0)
                r2 = 1
                r5.done = r2
                boolean r2 = r5.enter()
                if (r2 == 0) goto L_0x0026
                io.reactivex.internal.fuseable.SimplePlainQueue r2 = r5.queue
                org.reactivestreams.Subscriber r3 = r5.actual
                r4 = 0
                p005io.reactivex.internal.util.QueueDrainHelper.drainMaxLoop(r2, r3, r4, r1, r5)
            L_0x0026:
                return
            L_0x0027:
                r0 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0027 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.flowable.FlowableBufferTimed.BufferExactUnboundedSubscriber.onComplete():void");
        }

        public void request(long n) {
            requested(n);
        }

        public void cancel() {
            this.cancelled = true;
            this.f129s.cancel();
            DisposableHelper.dispose(this.timer);
        }

        public void run() {
            try {
                U next = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
                synchronized (this) {
                    U current = this.buffer;
                    if (current != null) {
                        this.buffer = next;
                        fastPathEmitMax(current, false, this);
                    }
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                cancel();
                this.actual.onError(e);
            }
        }

        public boolean accept(Subscriber<? super U> subscriber, U v) {
            this.actual.onNext(v);
            return true;
        }

        public void dispose() {
            cancel();
        }

        public boolean isDisposed() {
            return this.timer.get() == DisposableHelper.DISPOSED;
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableBufferTimed$BufferSkipBoundedSubscriber */
    static final class BufferSkipBoundedSubscriber<T, U extends Collection<? super T>> extends QueueDrainSubscriber<T, U, U> implements Subscription, Runnable {
        final Callable<U> bufferSupplier;
        final List<U> buffers = new LinkedList();

        /* renamed from: s */
        Subscription f130s;
        final long timeskip;
        final long timespan;
        final TimeUnit unit;

        /* renamed from: w */
        final Worker f131w;

        /* renamed from: io.reactivex.internal.operators.flowable.FlowableBufferTimed$BufferSkipBoundedSubscriber$RemoveFromBuffer */
        final class RemoveFromBuffer implements Runnable {
            private final U buffer;

            RemoveFromBuffer(U buffer2) {
                this.buffer = buffer2;
            }

            public void run() {
                synchronized (BufferSkipBoundedSubscriber.this) {
                    BufferSkipBoundedSubscriber.this.buffers.remove(this.buffer);
                }
                BufferSkipBoundedSubscriber.this.fastPathOrderedEmitMax(this.buffer, false, BufferSkipBoundedSubscriber.this.f131w);
            }
        }

        BufferSkipBoundedSubscriber(Subscriber<? super U> actual, Callable<U> bufferSupplier2, long timespan2, long timeskip2, TimeUnit unit2, Worker w) {
            super(actual, new MpscLinkedQueue());
            this.bufferSupplier = bufferSupplier2;
            this.timespan = timespan2;
            this.timeskip = timeskip2;
            this.unit = unit2;
            this.f131w = w;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f130s, s)) {
                this.f130s = s;
                try {
                    U b = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
                    this.buffers.add(b);
                    this.actual.onSubscribe(this);
                    s.request(Long.MAX_VALUE);
                    this.f131w.schedulePeriodically(this, this.timeskip, this.timeskip, this.unit);
                    this.f131w.schedule(new RemoveFromBuffer(b), this.timespan, this.unit);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.f131w.dispose();
                    s.cancel();
                    EmptySubscription.error(e, this.actual);
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
            this.f131w.dispose();
            clear();
            this.actual.onError(t);
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
                QueueDrainHelper.drainMaxLoop(this.queue, this.actual, false, this.f131w, this);
            }
        }

        public void request(long n) {
            requested(n);
        }

        public void cancel() {
            this.cancelled = true;
            this.f130s.cancel();
            this.f131w.dispose();
            clear();
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
                    U b = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
                    synchronized (this) {
                        if (!this.cancelled) {
                            this.buffers.add(b);
                            this.f131w.schedule(new RemoveFromBuffer(b), this.timespan, this.unit);
                        }
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    cancel();
                    this.actual.onError(e);
                }
            }
        }

        public boolean accept(Subscriber<? super U> a, U v) {
            a.onNext(v);
            return true;
        }
    }

    public FlowableBufferTimed(Flowable<T> source, long timespan2, long timeskip2, TimeUnit unit2, Scheduler scheduler2, Callable<U> bufferSupplier2, int maxSize2, boolean restartTimerOnMaxSize2) {
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
    public void subscribeActual(Subscriber<? super U> s) {
        if (this.timespan == this.timeskip && this.maxSize == Integer.MAX_VALUE) {
            Flowable flowable = this.source;
            BufferExactUnboundedSubscriber bufferExactUnboundedSubscriber = new BufferExactUnboundedSubscriber(new SerializedSubscriber(s), this.bufferSupplier, this.timespan, this.unit, this.scheduler);
            flowable.subscribe((FlowableSubscriber<? super T>) bufferExactUnboundedSubscriber);
            return;
        }
        Worker w = this.scheduler.createWorker();
        if (this.timespan == this.timeskip) {
            Flowable flowable2 = this.source;
            BufferExactBoundedSubscriber bufferExactBoundedSubscriber = new BufferExactBoundedSubscriber(new SerializedSubscriber(s), this.bufferSupplier, this.timespan, this.unit, this.maxSize, this.restartTimerOnMaxSize, w);
            flowable2.subscribe((FlowableSubscriber<? super T>) bufferExactBoundedSubscriber);
            return;
        }
        Flowable flowable3 = this.source;
        BufferSkipBoundedSubscriber bufferSkipBoundedSubscriber = new BufferSkipBoundedSubscriber(new SerializedSubscriber(s), this.bufferSupplier, this.timespan, this.timeskip, this.unit, w);
        flowable3.subscribe((FlowableSubscriber<? super T>) bufferSkipBoundedSubscriber);
    }
}
