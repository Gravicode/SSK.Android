package p005io.reactivex.internal.operators.flowable;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.queue.MpscLinkedQueue;
import p005io.reactivex.internal.subscribers.QueueDrainSubscriber;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;
import p005io.reactivex.subscribers.DisposableSubscriber;
import p005io.reactivex.subscribers.SerializedSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableBufferBoundarySupplier */
public final class FlowableBufferBoundarySupplier<T, U extends Collection<? super T>, B> extends AbstractFlowableWithUpstream<T, U> {
    final Callable<? extends Publisher<B>> boundarySupplier;
    final Callable<U> bufferSupplier;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableBufferBoundarySupplier$BufferBoundarySubscriber */
    static final class BufferBoundarySubscriber<T, U extends Collection<? super T>, B> extends DisposableSubscriber<B> {
        boolean once;
        final BufferBoundarySupplierSubscriber<T, U, B> parent;

        BufferBoundarySubscriber(BufferBoundarySupplierSubscriber<T, U, B> parent2) {
            this.parent = parent2;
        }

        public void onNext(B b) {
            if (!this.once) {
                this.once = true;
                cancel();
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

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableBufferBoundarySupplier$BufferBoundarySupplierSubscriber */
    static final class BufferBoundarySupplierSubscriber<T, U extends Collection<? super T>, B> extends QueueDrainSubscriber<T, U, U> implements FlowableSubscriber<T>, Subscription, Disposable {
        final Callable<? extends Publisher<B>> boundarySupplier;
        U buffer;
        final Callable<U> bufferSupplier;
        final AtomicReference<Disposable> other = new AtomicReference<>();

        /* renamed from: s */
        Subscription f125s;

        BufferBoundarySupplierSubscriber(Subscriber<? super U> actual, Callable<U> bufferSupplier2, Callable<? extends Publisher<B>> boundarySupplier2) {
            super(actual, new MpscLinkedQueue());
            this.bufferSupplier = bufferSupplier2;
            this.boundarySupplier = boundarySupplier2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f125s, s)) {
                this.f125s = s;
                Subscriber<? super U> actual = this.actual;
                try {
                    this.buffer = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
                    try {
                        Publisher publisher = (Publisher) ObjectHelper.requireNonNull(this.boundarySupplier.call(), "The boundary publisher supplied is null");
                        BufferBoundarySubscriber<T, U, B> bs = new BufferBoundarySubscriber<>(this);
                        this.other.set(bs);
                        actual.onSubscribe(this);
                        if (!this.cancelled) {
                            s.request(Long.MAX_VALUE);
                            publisher.subscribe(bs);
                        }
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        this.cancelled = true;
                        s.cancel();
                        EmptySubscription.error(ex, actual);
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.cancelled = true;
                    s.cancel();
                    EmptySubscription.error(e, actual);
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
            cancel();
            this.actual.onError(t);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0019, code lost:
            p005io.reactivex.internal.util.QueueDrainHelper.drainMaxLoop(r4.queue, r4.actual, false, r4, r4);
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
                org.reactivestreams.Subscriber r2 = r4.actual
                r3 = 0
                p005io.reactivex.internal.util.QueueDrainHelper.drainMaxLoop(r1, r2, r3, r4, r4)
            L_0x0021:
                return
            L_0x0022:
                r0 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0022 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.flowable.FlowableBufferBoundarySupplier.BufferBoundarySupplierSubscriber.onComplete():void");
        }

        public void request(long n) {
            requested(n);
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.f125s.cancel();
                disposeOther();
                if (enter()) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void disposeOther() {
            DisposableHelper.dispose(this.other);
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0037, code lost:
            r1.subscribe(r2);
            fastPathEmitMax(r3, false, r5);
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
                java.util.concurrent.Callable<? extends org.reactivestreams.Publisher<B>> r1 = r5.boundarySupplier     // Catch:{ Throwable -> 0x0043 }
                java.lang.Object r1 = r1.call()     // Catch:{ Throwable -> 0x0043 }
                java.lang.String r2 = "The boundary publisher supplied is null"
                java.lang.Object r1 = p005io.reactivex.internal.functions.ObjectHelper.requireNonNull(r1, r2)     // Catch:{ Throwable -> 0x0043 }
                org.reactivestreams.Publisher r1 = (org.reactivestreams.Publisher) r1     // Catch:{ Throwable -> 0x0043 }
                io.reactivex.internal.operators.flowable.FlowableBufferBoundarySupplier$BufferBoundarySubscriber r2 = new io.reactivex.internal.operators.flowable.FlowableBufferBoundarySupplier$BufferBoundarySubscriber
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
                r5.fastPathEmitMax(r3, r4, r5)
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
                org.reactivestreams.Subscription r2 = r5.f125s
                r2.cancel()
                org.reactivestreams.Subscriber r2 = r5.actual
                r2.onError(r1)
                return
            L_0x0055:
                r0 = move-exception
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r0)
                r5.cancel()
                org.reactivestreams.Subscriber r1 = r5.actual
                r1.onError(r0)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.flowable.FlowableBufferBoundarySupplier.BufferBoundarySupplierSubscriber.next():void");
        }

        public void dispose() {
            this.f125s.cancel();
            disposeOther();
        }

        public boolean isDisposed() {
            return this.other.get() == DisposableHelper.DISPOSED;
        }

        public boolean accept(Subscriber<? super U> subscriber, U v) {
            this.actual.onNext(v);
            return true;
        }
    }

    public FlowableBufferBoundarySupplier(Flowable<T> source, Callable<? extends Publisher<B>> boundarySupplier2, Callable<U> bufferSupplier2) {
        super(source);
        this.boundarySupplier = boundarySupplier2;
        this.bufferSupplier = bufferSupplier2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super U> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new BufferBoundarySupplierSubscriber<Object>(new SerializedSubscriber(s), this.bufferSupplier, this.boundarySupplier));
    }
}
