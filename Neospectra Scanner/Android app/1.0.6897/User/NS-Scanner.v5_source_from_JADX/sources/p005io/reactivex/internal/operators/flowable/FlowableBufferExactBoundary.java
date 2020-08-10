package p005io.reactivex.internal.operators.flowable;

import java.util.Collection;
import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.queue.MpscLinkedQueue;
import p005io.reactivex.internal.subscribers.QueueDrainSubscriber;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.subscribers.DisposableSubscriber;
import p005io.reactivex.subscribers.SerializedSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableBufferExactBoundary */
public final class FlowableBufferExactBoundary<T, U extends Collection<? super T>, B> extends AbstractFlowableWithUpstream<T, U> {
    final Publisher<B> boundary;
    final Callable<U> bufferSupplier;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableBufferExactBoundary$BufferBoundarySubscriber */
    static final class BufferBoundarySubscriber<T, U extends Collection<? super T>, B> extends DisposableSubscriber<B> {
        final BufferExactBoundarySubscriber<T, U, B> parent;

        BufferBoundarySubscriber(BufferExactBoundarySubscriber<T, U, B> parent2) {
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

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableBufferExactBoundary$BufferExactBoundarySubscriber */
    static final class BufferExactBoundarySubscriber<T, U extends Collection<? super T>, B> extends QueueDrainSubscriber<T, U, U> implements FlowableSubscriber<T>, Subscription, Disposable {
        final Publisher<B> boundary;
        U buffer;
        final Callable<U> bufferSupplier;
        Disposable other;

        /* renamed from: s */
        Subscription f126s;

        BufferExactBoundarySubscriber(Subscriber<? super U> actual, Callable<U> bufferSupplier2, Publisher<B> boundary2) {
            super(actual, new MpscLinkedQueue());
            this.bufferSupplier = bufferSupplier2;
            this.boundary = boundary2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f126s, s)) {
                this.f126s = s;
                try {
                    this.buffer = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
                    BufferBoundarySubscriber<T, U, B> bs = new BufferBoundarySubscriber<>(this);
                    this.other = bs;
                    this.actual.onSubscribe(this);
                    if (!this.cancelled) {
                        s.request(Long.MAX_VALUE);
                        this.boundary.subscribe(bs);
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.cancelled = true;
                    s.cancel();
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
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.flowable.FlowableBufferExactBoundary.BufferExactBoundarySubscriber.onComplete():void");
        }

        public void request(long n) {
            requested(n);
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.other.dispose();
                this.f126s.cancel();
                if (enter()) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void next() {
            try {
                U next = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
                synchronized (this) {
                    U b = this.buffer;
                    if (b != null) {
                        this.buffer = next;
                        fastPathEmitMax(b, false, this);
                    }
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                cancel();
                this.actual.onError(e);
            }
        }

        public void dispose() {
            cancel();
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public boolean accept(Subscriber<? super U> subscriber, U v) {
            this.actual.onNext(v);
            return true;
        }
    }

    public FlowableBufferExactBoundary(Flowable<T> source, Publisher<B> boundary2, Callable<U> bufferSupplier2) {
        super(source);
        this.boundary = boundary2;
        this.bufferSupplier = bufferSupplier2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super U> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new BufferExactBoundarySubscriber<Object>(new SerializedSubscriber(s), this.bufferSupplier, this.boundary));
    }
}
