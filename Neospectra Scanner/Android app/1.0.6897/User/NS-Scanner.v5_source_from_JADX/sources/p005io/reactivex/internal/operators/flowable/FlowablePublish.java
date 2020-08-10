package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.MissingBackpressureException;
import p005io.reactivex.flowables.ConnectableFlowable;
import p005io.reactivex.internal.fuseable.HasUpstreamPublisher;
import p005io.reactivex.internal.fuseable.QueueSubscription;
import p005io.reactivex.internal.fuseable.SimpleQueue;
import p005io.reactivex.internal.queue.SpscArrayQueue;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.internal.util.NotificationLite;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowablePublish */
public final class FlowablePublish<T> extends ConnectableFlowable<T> implements HasUpstreamPublisher<T> {
    static final long CANCELLED = Long.MIN_VALUE;
    final int bufferSize;
    final AtomicReference<PublishSubscriber<T>> current;
    final Publisher<T> onSubscribe;
    final Flowable<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowablePublish$FlowablePublisher */
    static final class FlowablePublisher<T> implements Publisher<T> {
        private final int bufferSize;
        private final AtomicReference<PublishSubscriber<T>> curr;

        FlowablePublisher(AtomicReference<PublishSubscriber<T>> curr2, int bufferSize2) {
            this.curr = curr2;
            this.bufferSize = bufferSize2;
        }

        public void subscribe(Subscriber<? super T> child) {
            PublishSubscriber<T> r;
            InnerSubscriber<T> inner = new InnerSubscriber<>(child);
            child.onSubscribe(inner);
            while (true) {
                r = (PublishSubscriber) this.curr.get();
                if (r == null || r.isDisposed()) {
                    PublishSubscriber<T> u = new PublishSubscriber<>(this.curr, this.bufferSize);
                    if (!this.curr.compareAndSet(r, u)) {
                        continue;
                    } else {
                        r = u;
                    }
                }
                if (r.add(inner)) {
                    break;
                }
            }
            if (inner.get() == FlowablePublish.CANCELLED) {
                r.remove(inner);
            } else {
                inner.parent = r;
            }
            r.dispatch();
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowablePublish$InnerSubscriber */
    static final class InnerSubscriber<T> extends AtomicLong implements Subscription {
        private static final long serialVersionUID = -4453897557930727610L;
        final Subscriber<? super T> child;
        long emitted;
        volatile PublishSubscriber<T> parent;

        InnerSubscriber(Subscriber<? super T> child2) {
            this.child = child2;
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.addCancel(this, n);
                PublishSubscriber<T> p = this.parent;
                if (p != null) {
                    p.dispatch();
                }
            }
        }

        public void cancel() {
            if (get() != FlowablePublish.CANCELLED && getAndSet(FlowablePublish.CANCELLED) != FlowablePublish.CANCELLED) {
                PublishSubscriber<T> p = this.parent;
                if (p != null) {
                    p.remove(this);
                    p.dispatch();
                }
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowablePublish$PublishSubscriber */
    static final class PublishSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Disposable {
        static final InnerSubscriber[] EMPTY = new InnerSubscriber[0];
        static final InnerSubscriber[] TERMINATED = new InnerSubscriber[0];
        private static final long serialVersionUID = -202316842419149694L;
        final int bufferSize;
        final AtomicReference<PublishSubscriber<T>> current;
        volatile SimpleQueue<T> queue;

        /* renamed from: s */
        final AtomicReference<Subscription> f180s = new AtomicReference<>();
        final AtomicBoolean shouldConnect;
        int sourceMode;
        final AtomicReference<InnerSubscriber<T>[]> subscribers = new AtomicReference<>(EMPTY);
        volatile Object terminalEvent;

        PublishSubscriber(AtomicReference<PublishSubscriber<T>> current2, int bufferSize2) {
            this.current = current2;
            this.shouldConnect = new AtomicBoolean();
            this.bufferSize = bufferSize2;
        }

        public void dispose() {
            if (this.subscribers.get() != TERMINATED && ((InnerSubscriber[]) this.subscribers.getAndSet(TERMINATED)) != TERMINATED) {
                this.current.compareAndSet(this, null);
                SubscriptionHelper.cancel(this.f180s);
            }
        }

        public boolean isDisposed() {
            return this.subscribers.get() == TERMINATED;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.setOnce(this.f180s, s)) {
                if (s instanceof QueueSubscription) {
                    QueueSubscription<T> qs = (QueueSubscription) s;
                    int m = qs.requestFusion(3);
                    if (m == 1) {
                        this.sourceMode = m;
                        this.queue = qs;
                        this.terminalEvent = NotificationLite.complete();
                        dispatch();
                        return;
                    } else if (m == 2) {
                        this.sourceMode = m;
                        this.queue = qs;
                        s.request((long) this.bufferSize);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.bufferSize);
                s.request((long) this.bufferSize);
            }
        }

        public void onNext(T t) {
            if (this.sourceMode != 0 || this.queue.offer(t)) {
                dispatch();
            } else {
                onError(new MissingBackpressureException("Prefetch queue is full?!"));
            }
        }

        public void onError(Throwable e) {
            if (this.terminalEvent == null) {
                this.terminalEvent = NotificationLite.error(e);
                dispatch();
                return;
            }
            RxJavaPlugins.onError(e);
        }

        public void onComplete() {
            if (this.terminalEvent == null) {
                this.terminalEvent = NotificationLite.complete();
                dispatch();
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean add(InnerSubscriber<T> producer) {
            InnerSubscriber<T>[] c;
            InnerSubscriber<T>[] u;
            do {
                c = (InnerSubscriber[]) this.subscribers.get();
                if (c == TERMINATED) {
                    return false;
                }
                int len = c.length;
                u = new InnerSubscriber[(len + 1)];
                System.arraycopy(c, 0, u, 0, len);
                u[len] = producer;
            } while (!this.subscribers.compareAndSet(c, u));
            return true;
        }

        /* access modifiers changed from: 0000 */
        public void remove(InnerSubscriber<T> producer) {
            InnerSubscriber<T>[] c;
            InnerSubscriber<T>[] u;
            do {
                c = (InnerSubscriber[]) this.subscribers.get();
                int len = c.length;
                if (len == 0) {
                    break;
                }
                int j = -1;
                int i = 0;
                while (true) {
                    if (i >= len) {
                        break;
                    } else if (c[i].equals(producer)) {
                        j = i;
                        break;
                    } else {
                        i++;
                    }
                }
                if (j >= 0) {
                    if (len == 1) {
                        u = EMPTY;
                    } else {
                        InnerSubscriber<T>[] u2 = new InnerSubscriber[(len - 1)];
                        System.arraycopy(c, 0, u2, 0, j);
                        System.arraycopy(c, j + 1, u2, j, (len - j) - 1);
                        u = u2;
                    }
                } else {
                    return;
                }
            } while (!this.subscribers.compareAndSet(c, u));
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(Object term, boolean empty) {
            int i = 0;
            if (term != null) {
                if (!NotificationLite.isComplete(term)) {
                    Throwable t = NotificationLite.getError(term);
                    this.current.compareAndSet(this, null);
                    InnerSubscriber<?>[] a = (InnerSubscriber[]) this.subscribers.getAndSet(TERMINATED);
                    if (a.length != 0) {
                        int length = a.length;
                        while (i < length) {
                            a[i].child.onError(t);
                            i++;
                        }
                    } else {
                        RxJavaPlugins.onError(t);
                    }
                    return true;
                } else if (empty) {
                    this.current.compareAndSet(this, null);
                    InnerSubscriber<?>[] innerSubscriberArr = (InnerSubscriber[]) this.subscribers.getAndSet(TERMINATED);
                    int length2 = innerSubscriberArr.length;
                    while (i < length2) {
                        innerSubscriberArr[i].child.onComplete();
                        i++;
                    }
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:73:0x014f, code lost:
            if (r5 <= 0) goto L_0x0162;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:75:0x0154, code lost:
            if (r1.sourceMode == 1) goto L_0x0162;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:76:0x0156, code lost:
            ((org.reactivestreams.Subscription) r1.f180s.get()).request((long) r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:78:0x0166, code lost:
            if (r7 == 0) goto L_0x016c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:79:0x0168, code lost:
            if (r9 != false) goto L_0x016c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:80:0x016c, code lost:
            r5 = r6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:94:0x0012, code lost:
            continue;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void dispatch() {
            /*
                r30 = this;
                r1 = r30
                int r2 = r30.getAndIncrement()
                if (r2 == 0) goto L_0x0009
                return
            L_0x0009:
                r2 = 1
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowablePublish$InnerSubscriber<T>[]> r3 = r1.subscribers
                java.lang.Object r4 = r3.get()
                io.reactivex.internal.operators.flowable.FlowablePublish$InnerSubscriber[] r4 = (p005io.reactivex.internal.operators.flowable.FlowablePublish.InnerSubscriber[]) r4
            L_0x0012:
                java.lang.Object r5 = r1.terminalEvent
                io.reactivex.internal.fuseable.SimpleQueue<T> r6 = r1.queue
                if (r6 == 0) goto L_0x0021
                boolean r9 = r6.isEmpty()
                if (r9 == 0) goto L_0x001f
                goto L_0x0021
            L_0x001f:
                r9 = 0
                goto L_0x0022
            L_0x0021:
                r9 = 1
            L_0x0022:
                boolean r10 = r1.checkTerminated(r5, r9)
                if (r10 == 0) goto L_0x0029
                return
            L_0x0029:
                if (r9 != 0) goto L_0x016e
                int r10 = r4.length
                r11 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                r13 = 0
                int r14 = r4.length
                r7 = r11
                r11 = 0
            L_0x0035:
                r16 = -9223372036854775808
                if (r11 >= r14) goto L_0x0057
                r12 = r4[r11]
                long r18 = r12.get()
                int r16 = (r18 > r16 ? 1 : (r18 == r16 ? 0 : -1))
                if (r16 == 0) goto L_0x004e
                r20 = r14
                long r14 = r12.emitted
                long r14 = r18 - r14
                long r7 = java.lang.Math.min(r7, r14)
                goto L_0x0052
            L_0x004e:
                r20 = r14
                int r13 = r13 + 1
            L_0x0052:
                int r11 = r11 + 1
                r14 = r20
                goto L_0x0035
            L_0x0057:
                if (r10 != r13) goto L_0x009d
                java.lang.Object r5 = r1.terminalEvent
                java.lang.Object r15 = r6.poll()     // Catch:{ Throwable -> 0x0061 }
                r14 = r15
                goto L_0x0079
            L_0x0061:
                r0 = move-exception
                r15 = r0
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r15)
                java.util.concurrent.atomic.AtomicReference<org.reactivestreams.Subscription> r14 = r1.f180s
                java.lang.Object r14 = r14.get()
                org.reactivestreams.Subscription r14 = (org.reactivestreams.Subscription) r14
                r14.cancel()
                java.lang.Object r5 = p005io.reactivex.internal.util.NotificationLite.error(r15)
                r1.terminalEvent = r5
                r14 = 0
            L_0x0079:
                if (r14 != 0) goto L_0x007d
                r15 = 1
                goto L_0x007e
            L_0x007d:
                r15 = 0
            L_0x007e:
                boolean r15 = r1.checkTerminated(r5, r15)
                if (r15 == 0) goto L_0x0085
                return
            L_0x0085:
                int r15 = r1.sourceMode
                r11 = 1
                if (r15 == r11) goto L_0x0012
                java.util.concurrent.atomic.AtomicReference<org.reactivestreams.Subscription> r11 = r1.f180s
                java.lang.Object r11 = r11.get()
                org.reactivestreams.Subscription r11 = (org.reactivestreams.Subscription) r11
                r23 = r9
                r24 = r10
                r9 = 1
                r11.request(r9)
                goto L_0x0012
            L_0x009d:
                r23 = r9
                r24 = r10
                r9 = r5
                r5 = 0
            L_0x00a3:
                long r10 = (long) r5
                int r10 = (r10 > r7 ? 1 : (r10 == r7 ? 0 : -1))
                if (r10 >= 0) goto L_0x014a
                java.lang.Object r9 = r1.terminalEvent
                java.lang.Object r10 = r6.poll()     // Catch:{ Throwable -> 0x00b0 }
                r14 = r10
                goto L_0x00c8
            L_0x00b0:
                r0 = move-exception
                r10 = r0
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r10)
                java.util.concurrent.atomic.AtomicReference<org.reactivestreams.Subscription> r11 = r1.f180s
                java.lang.Object r11 = r11.get()
                org.reactivestreams.Subscription r11 = (org.reactivestreams.Subscription) r11
                r11.cancel()
                java.lang.Object r9 = p005io.reactivex.internal.util.NotificationLite.error(r10)
                r1.terminalEvent = r9
                r14 = 0
            L_0x00c8:
                r10 = r14
                if (r10 != 0) goto L_0x00cd
                r11 = 1
                goto L_0x00ce
            L_0x00cd:
                r11 = 0
            L_0x00ce:
                boolean r12 = r1.checkTerminated(r9, r11)
                if (r12 == 0) goto L_0x00d5
                return
            L_0x00d5:
                if (r11 == 0) goto L_0x00de
                r25 = r6
                r6 = r9
                r9 = r11
                goto L_0x014f
            L_0x00de:
                java.lang.Object r12 = p005io.reactivex.internal.util.NotificationLite.getValue(r10)
                r14 = 0
                int r15 = r4.length
                r18 = r14
                r14 = 0
            L_0x00e7:
                if (r14 >= r15) goto L_0x0129
                r25 = r6
                r6 = r4[r14]
                long r19 = r6.get()
                int r23 = (r19 > r16 ? 1 : (r19 == r16 ? 0 : -1))
                if (r23 == 0) goto L_0x0117
                r26 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r23 = (r19 > r26 ? 1 : (r19 == r26 ? 0 : -1))
                if (r23 == 0) goto L_0x010b
                r28 = r9
                r29 = r10
                long r9 = r6.emitted
                r21 = 1
                long r9 = r9 + r21
                r6.emitted = r9
                goto L_0x0111
            L_0x010b:
                r28 = r9
                r29 = r10
                r21 = 1
            L_0x0111:
                org.reactivestreams.Subscriber<? super T> r9 = r6.child
                r9.onNext(r12)
                goto L_0x0120
            L_0x0117:
                r28 = r9
                r29 = r10
                r21 = 1
                r6 = 1
                r18 = r6
            L_0x0120:
                int r14 = r14 + 1
                r6 = r25
                r9 = r28
                r10 = r29
                goto L_0x00e7
            L_0x0129:
                r25 = r6
                r28 = r9
                r29 = r10
                r21 = 1
                int r5 = r5 + 1
                java.lang.Object r6 = r3.get()
                io.reactivex.internal.operators.flowable.FlowablePublish$InnerSubscriber[] r6 = (p005io.reactivex.internal.operators.flowable.FlowablePublish.InnerSubscriber[]) r6
                if (r18 != 0) goto L_0x0147
                if (r6 == r4) goto L_0x013e
                goto L_0x0147
            L_0x013e:
                r23 = r11
                r6 = r25
                r9 = r28
                goto L_0x00a3
            L_0x0147:
                r4 = r6
                goto L_0x0012
            L_0x014a:
                r25 = r6
                r6 = r9
                r9 = r23
            L_0x014f:
                if (r5 <= 0) goto L_0x0162
                int r10 = r1.sourceMode
                r11 = 1
                if (r10 == r11) goto L_0x0162
                java.util.concurrent.atomic.AtomicReference<org.reactivestreams.Subscription> r10 = r1.f180s
                java.lang.Object r10 = r10.get()
                org.reactivestreams.Subscription r10 = (org.reactivestreams.Subscription) r10
                long r11 = (long) r5
                r10.request(r11)
            L_0x0162:
                r10 = 0
                int r10 = (r7 > r10 ? 1 : (r7 == r10 ? 0 : -1))
                if (r10 == 0) goto L_0x016c
                if (r9 != 0) goto L_0x016c
                goto L_0x0012
            L_0x016c:
                r5 = r6
                goto L_0x0172
            L_0x016e:
                r25 = r6
                r23 = r9
            L_0x0172:
                int r6 = -r2
                int r2 = r1.addAndGet(r6)
                if (r2 != 0) goto L_0x017b
                return
            L_0x017b:
                java.lang.Object r6 = r3.get()
                r4 = r6
                io.reactivex.internal.operators.flowable.FlowablePublish$InnerSubscriber[] r4 = (p005io.reactivex.internal.operators.flowable.FlowablePublish.InnerSubscriber[]) r4
                goto L_0x0012
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.flowable.FlowablePublish.PublishSubscriber.dispatch():void");
        }
    }

    public static <T> ConnectableFlowable<T> create(Flowable<T> source2, int bufferSize2) {
        AtomicReference<PublishSubscriber<T>> curr = new AtomicReference<>();
        return RxJavaPlugins.onAssembly((ConnectableFlowable<T>) new FlowablePublish<T>(new FlowablePublisher<>(curr, bufferSize2), source2, curr, bufferSize2));
    }

    private FlowablePublish(Publisher<T> onSubscribe2, Flowable<T> source2, AtomicReference<PublishSubscriber<T>> current2, int bufferSize2) {
        this.onSubscribe = onSubscribe2;
        this.source = source2;
        this.current = current2;
        this.bufferSize = bufferSize2;
    }

    public Publisher<T> source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.onSubscribe.subscribe(s);
    }

    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void connect(p005io.reactivex.functions.Consumer<? super p005io.reactivex.disposables.Disposable> r5) {
        /*
            r4 = this;
        L_0x0000:
            java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowablePublish$PublishSubscriber<T>> r0 = r4.current
            java.lang.Object r0 = r0.get()
            io.reactivex.internal.operators.flowable.FlowablePublish$PublishSubscriber r0 = (p005io.reactivex.internal.operators.flowable.FlowablePublish.PublishSubscriber) r0
            if (r0 == 0) goto L_0x0010
            boolean r1 = r0.isDisposed()
            if (r1 == 0) goto L_0x0023
        L_0x0010:
            io.reactivex.internal.operators.flowable.FlowablePublish$PublishSubscriber r1 = new io.reactivex.internal.operators.flowable.FlowablePublish$PublishSubscriber
            java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowablePublish$PublishSubscriber<T>> r2 = r4.current
            int r3 = r4.bufferSize
            r1.<init>(r2, r3)
            java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowablePublish$PublishSubscriber<T>> r2 = r4.current
            boolean r2 = r2.compareAndSet(r0, r1)
            if (r2 != 0) goto L_0x0022
            goto L_0x0000
        L_0x0022:
            r0 = r1
        L_0x0023:
            java.util.concurrent.atomic.AtomicBoolean r1 = r0.shouldConnect
            boolean r1 = r1.get()
            r2 = 1
            r3 = 0
            if (r1 != 0) goto L_0x0036
            java.util.concurrent.atomic.AtomicBoolean r1 = r0.shouldConnect
            boolean r1 = r1.compareAndSet(r3, r2)
            if (r1 == 0) goto L_0x0036
            goto L_0x0037
        L_0x0036:
            r2 = 0
        L_0x0037:
            r1 = r2
            r5.accept(r0)     // Catch:{ Throwable -> 0x0045 }
            if (r1 == 0) goto L_0x0044
            io.reactivex.Flowable<T> r2 = r4.source
            r2.subscribe(r0)
        L_0x0044:
            return
        L_0x0045:
            r2 = move-exception
            p005io.reactivex.exceptions.Exceptions.throwIfFatal(r2)
            java.lang.RuntimeException r3 = p005io.reactivex.internal.util.ExceptionHelper.wrapOrThrow(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.flowable.FlowablePublish.connect(io.reactivex.functions.Consumer):void");
    }
}
