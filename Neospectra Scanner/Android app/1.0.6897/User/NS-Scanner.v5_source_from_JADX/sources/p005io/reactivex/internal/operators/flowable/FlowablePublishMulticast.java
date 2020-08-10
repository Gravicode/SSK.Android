package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.exceptions.MissingBackpressureException;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.QueueSubscription;
import p005io.reactivex.internal.fuseable.SimpleQueue;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.internal.util.QueueDrainHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowablePublishMulticast */
public final class FlowablePublishMulticast<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final boolean delayError;
    final int prefetch;
    final Function<? super Flowable<T>, ? extends Publisher<? extends R>> selector;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowablePublishMulticast$MulticastProcessor */
    static final class MulticastProcessor<T> extends Flowable<T> implements FlowableSubscriber<T>, Disposable {
        static final MulticastSubscription[] EMPTY = new MulticastSubscription[0];
        static final MulticastSubscription[] TERMINATED = new MulticastSubscription[0];
        int consumed;
        final boolean delayError;
        volatile boolean done;
        Throwable error;
        final int limit;
        final int prefetch;
        volatile SimpleQueue<T> queue;

        /* renamed from: s */
        final AtomicReference<Subscription> f181s = new AtomicReference<>();
        int sourceMode;
        final AtomicReference<MulticastSubscription<T>[]> subscribers = new AtomicReference<>(EMPTY);
        final AtomicInteger wip = new AtomicInteger();

        MulticastProcessor(int prefetch2, boolean delayError2) {
            this.prefetch = prefetch2;
            this.limit = prefetch2 - (prefetch2 >> 2);
            this.delayError = delayError2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.setOnce(this.f181s, s)) {
                if (s instanceof QueueSubscription) {
                    QueueSubscription<T> qs = (QueueSubscription) s;
                    int m = qs.requestFusion(3);
                    if (m == 1) {
                        this.sourceMode = m;
                        this.queue = qs;
                        this.done = true;
                        drain();
                        return;
                    } else if (m == 2) {
                        this.sourceMode = m;
                        this.queue = qs;
                        QueueDrainHelper.request(s, this.prefetch);
                        return;
                    }
                }
                this.queue = QueueDrainHelper.createQueue(this.prefetch);
                QueueDrainHelper.request(s, this.prefetch);
            }
        }

        public void dispose() {
            SubscriptionHelper.cancel(this.f181s);
            if (this.wip.getAndIncrement() == 0) {
                SimpleQueue<T> q = this.queue;
                if (q != null) {
                    q.clear();
                }
            }
        }

        public boolean isDisposed() {
            return SubscriptionHelper.isCancelled((Subscription) this.f181s.get());
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.sourceMode != 0 || this.queue.offer(t)) {
                    drain();
                    return;
                }
                ((Subscription) this.f181s.get()).cancel();
                onError(new MissingBackpressureException());
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.error = t;
            this.done = true;
            drain();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                drain();
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean add(MulticastSubscription<T> s) {
            MulticastSubscription<T>[] current;
            MulticastSubscription<T>[] next;
            do {
                current = (MulticastSubscription[]) this.subscribers.get();
                if (current == TERMINATED) {
                    return false;
                }
                int n = current.length;
                next = new MulticastSubscription[(n + 1)];
                System.arraycopy(current, 0, next, 0, n);
                next[n] = s;
            } while (!this.subscribers.compareAndSet(current, next));
            return true;
        }

        /* access modifiers changed from: 0000 */
        public void remove(MulticastSubscription<T> s) {
            MulticastSubscription<T>[] current;
            MulticastSubscription<T>[] next;
            do {
                current = (MulticastSubscription[]) this.subscribers.get();
                int n = current.length;
                if (n != 0) {
                    int j = -1;
                    int i = 0;
                    while (true) {
                        if (i >= n) {
                            break;
                        } else if (current[i] == s) {
                            j = i;
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (j >= 0) {
                        if (n == 1) {
                            next = EMPTY;
                        } else {
                            MulticastSubscription<T>[] next2 = new MulticastSubscription[(n - 1)];
                            System.arraycopy(current, 0, next2, 0, j);
                            System.arraycopy(current, j + 1, next2, j, (n - j) - 1);
                            next = next2;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } while (!this.subscribers.compareAndSet(current, next));
        }

        /* access modifiers changed from: protected */
        public void subscribeActual(Subscriber<? super T> s) {
            MulticastSubscription<T> ms = new MulticastSubscription<>(s, this);
            s.onSubscribe(ms);
            if (!add(ms)) {
                Throwable ex = this.error;
                if (ex != null) {
                    s.onError(ex);
                } else {
                    s.onComplete();
                }
            } else if (ms.isCancelled()) {
                remove(ms);
            } else {
                drain();
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:78:0x011b, code lost:
            if (r15 != 0) goto L_0x014d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:80:0x0121, code lost:
            if (isDisposed() == false) goto L_0x0127;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:81:0x0123, code lost:
            r3.clear();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:82:0x0126, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:83:0x0127, code lost:
            r7 = r1.done;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:84:0x0129, code lost:
            if (r7 == false) goto L_0x0137;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:86:0x012d, code lost:
            if (r1.delayError != false) goto L_0x0137;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:87:0x012f, code lost:
            r8 = r1.error;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:88:0x0131, code lost:
            if (r8 == null) goto L_0x0137;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:89:0x0133, code lost:
            errorAll(r8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:90:0x0136, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:91:0x0137, code lost:
            if (r7 == false) goto L_0x014d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:93:0x013d, code lost:
            if (r3.isEmpty() == false) goto L_0x014d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:94:0x013f, code lost:
            r8 = r1.error;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:95:0x0141, code lost:
            if (r8 == null) goto L_0x0147;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:96:0x0143, code lost:
            errorAll(r8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:97:0x0147, code lost:
            completeAll();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:98:0x014a, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            /*
                r30 = this;
                r1 = r30
                java.util.concurrent.atomic.AtomicInteger r2 = r1.wip
                int r2 = r2.getAndIncrement()
                if (r2 == 0) goto L_0x000b
                return
            L_0x000b:
                r2 = 1
                io.reactivex.internal.fuseable.SimpleQueue<T> r3 = r1.queue
                int r4 = r1.consumed
                int r5 = r1.limit
                int r6 = r1.sourceMode
                r8 = 1
                if (r6 == r8) goto L_0x0019
                r6 = 1
                goto L_0x001a
            L_0x0019:
                r6 = 0
            L_0x001a:
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowablePublishMulticast$MulticastSubscription<T>[]> r9 = r1.subscribers
                java.lang.Object r10 = r9.get()
                io.reactivex.internal.operators.flowable.FlowablePublishMulticast$MulticastSubscription[] r10 = (p005io.reactivex.internal.operators.flowable.FlowablePublishMulticast.MulticastSubscription[]) r10
            L_0x0022:
                int r11 = r10.length
                if (r3 == 0) goto L_0x014b
                if (r11 == 0) goto L_0x014b
                r12 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r14 = r10.length
                r15 = r12
                r12 = r11
                r11 = 0
            L_0x0030:
                r17 = -9223372036854775808
                if (r11 >= r14) goto L_0x0050
                r13 = r10[r11]
                long r19 = r13.get()
                long r7 = r13.emitted
                long r19 = r19 - r7
                int r7 = (r19 > r17 ? 1 : (r19 == r17 ? 0 : -1))
                if (r7 == 0) goto L_0x004a
                int r7 = (r15 > r19 ? 1 : (r15 == r19 ? 0 : -1))
                if (r7 <= 0) goto L_0x004c
                r7 = r19
                r15 = r7
                goto L_0x004c
            L_0x004a:
                int r12 = r12 + -1
            L_0x004c:
                int r11 = r11 + 1
                r8 = 1
                goto L_0x0030
            L_0x0050:
                if (r12 != 0) goto L_0x0054
                r15 = 0
            L_0x0054:
                r7 = 0
                int r11 = (r15 > r7 ? 1 : (r15 == r7 ? 0 : -1))
                if (r11 == 0) goto L_0x0117
                boolean r11 = r30.isDisposed()
                if (r11 == 0) goto L_0x0064
                r3.clear()
                return
            L_0x0064:
                boolean r11 = r1.done
                if (r11 == 0) goto L_0x0074
                boolean r13 = r1.delayError
                if (r13 != 0) goto L_0x0074
                java.lang.Throwable r13 = r1.error
                if (r13 == 0) goto L_0x0074
                r1.errorAll(r13)
                return
            L_0x0074:
                java.lang.Object r13 = r3.poll()     // Catch:{ Throwable -> 0x0105 }
                if (r13 != 0) goto L_0x007e
                r14 = 1
                goto L_0x007f
            L_0x007e:
                r14 = 0
            L_0x007f:
                if (r11 == 0) goto L_0x008f
                if (r14 == 0) goto L_0x008f
                java.lang.Throwable r7 = r1.error
                if (r7 == 0) goto L_0x008b
                r1.errorAll(r7)
                goto L_0x008e
            L_0x008b:
                r30.completeAll()
            L_0x008e:
                return
            L_0x008f:
                if (r14 == 0) goto L_0x0096
                r28 = r12
                goto L_0x0119
            L_0x0096:
                r7 = 0
                int r8 = r10.length
                r19 = r7
                r7 = 0
            L_0x009b:
                r21 = 1
                if (r7 >= r8) goto L_0x00d9
                r23 = r8
                r8 = r10[r7]
                long r24 = r8.get()
                int r20 = (r24 > r17 ? 1 : (r24 == r17 ? 0 : -1))
                if (r20 == 0) goto L_0x00c9
                r26 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r20 = (r24 > r26 ? 1 : (r24 == r26 ? 0 : -1))
                if (r20 == 0) goto L_0x00bf
                r29 = r11
                r28 = r12
                long r11 = r8.emitted
                long r11 = r11 + r21
                r8.emitted = r11
                goto L_0x00c3
            L_0x00bf:
                r29 = r11
                r28 = r12
            L_0x00c3:
                org.reactivestreams.Subscriber<? super T> r11 = r8.actual
                r11.onNext(r13)
                goto L_0x00d0
            L_0x00c9:
                r29 = r11
                r28 = r12
                r8 = 1
                r19 = r8
            L_0x00d0:
                int r7 = r7 + 1
                r8 = r23
                r12 = r28
                r11 = r29
                goto L_0x009b
            L_0x00d9:
                r29 = r11
                r28 = r12
                r7 = 0
                long r15 = r15 - r21
                if (r6 == 0) goto L_0x00f3
                int r4 = r4 + 1
                if (r4 != r5) goto L_0x00f3
                r4 = 0
                java.util.concurrent.atomic.AtomicReference<org.reactivestreams.Subscription> r7 = r1.f181s
                java.lang.Object r7 = r7.get()
                org.reactivestreams.Subscription r7 = (org.reactivestreams.Subscription) r7
                long r11 = (long) r5
                r7.request(r11)
            L_0x00f3:
                java.lang.Object r7 = r9.get()
                io.reactivex.internal.operators.flowable.FlowablePublishMulticast$MulticastSubscription[] r7 = (p005io.reactivex.internal.operators.flowable.FlowablePublishMulticast.MulticastSubscription[]) r7
                if (r19 != 0) goto L_0x0103
                if (r7 == r10) goto L_0x00fe
                goto L_0x0103
            L_0x00fe:
                r12 = r28
                goto L_0x0054
            L_0x0103:
                r10 = r7
                goto L_0x0166
            L_0x0105:
                r0 = move-exception
                r29 = r11
                r28 = r12
                r7 = r0
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r7)
                java.util.concurrent.atomic.AtomicReference<org.reactivestreams.Subscription> r8 = r1.f181s
                p005io.reactivex.internal.subscriptions.SubscriptionHelper.cancel(r8)
                r1.errorAll(r7)
                return
            L_0x0117:
                r28 = r12
            L_0x0119:
                int r7 = (r15 > r7 ? 1 : (r15 == r7 ? 0 : -1))
                if (r7 != 0) goto L_0x014d
                boolean r7 = r30.isDisposed()
                if (r7 == 0) goto L_0x0127
                r3.clear()
                return
            L_0x0127:
                boolean r7 = r1.done
                if (r7 == 0) goto L_0x0137
                boolean r8 = r1.delayError
                if (r8 != 0) goto L_0x0137
                java.lang.Throwable r8 = r1.error
                if (r8 == 0) goto L_0x0137
                r1.errorAll(r8)
                return
            L_0x0137:
                if (r7 == 0) goto L_0x014d
                boolean r8 = r3.isEmpty()
                if (r8 == 0) goto L_0x014d
                java.lang.Throwable r8 = r1.error
                if (r8 == 0) goto L_0x0147
                r1.errorAll(r8)
                goto L_0x014a
            L_0x0147:
                r30.completeAll()
            L_0x014a:
                return
            L_0x014b:
                r28 = r11
            L_0x014d:
                r1.consumed = r4
                java.util.concurrent.atomic.AtomicInteger r7 = r1.wip
                int r8 = -r2
                int r2 = r7.addAndGet(r8)
                if (r2 != 0) goto L_0x015a
                return
            L_0x015a:
                if (r3 != 0) goto L_0x015e
                io.reactivex.internal.fuseable.SimpleQueue<T> r3 = r1.queue
            L_0x015e:
                java.lang.Object r7 = r9.get()
                r10 = r7
                io.reactivex.internal.operators.flowable.FlowablePublishMulticast$MulticastSubscription[] r10 = (p005io.reactivex.internal.operators.flowable.FlowablePublishMulticast.MulticastSubscription[]) r10
            L_0x0166:
                r8 = 1
                goto L_0x0022
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.flowable.FlowablePublishMulticast.MulticastProcessor.drain():void");
        }

        /* access modifiers changed from: 0000 */
        public void errorAll(Throwable ex) {
            MulticastSubscription<T>[] multicastSubscriptionArr;
            for (MulticastSubscription<T> ms : (MulticastSubscription[]) this.subscribers.getAndSet(TERMINATED)) {
                if (ms.get() != Long.MIN_VALUE) {
                    ms.actual.onError(ex);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void completeAll() {
            MulticastSubscription<T>[] multicastSubscriptionArr;
            for (MulticastSubscription<T> ms : (MulticastSubscription[]) this.subscribers.getAndSet(TERMINATED)) {
                if (ms.get() != Long.MIN_VALUE) {
                    ms.actual.onComplete();
                }
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowablePublishMulticast$MulticastSubscription */
    static final class MulticastSubscription<T> extends AtomicLong implements Subscription {
        private static final long serialVersionUID = 8664815189257569791L;
        final Subscriber<? super T> actual;
        long emitted;
        final MulticastProcessor<T> parent;

        MulticastSubscription(Subscriber<? super T> actual2, MulticastProcessor<T> parent2) {
            this.actual = actual2;
            this.parent = parent2;
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.addCancel(this, n);
                this.parent.drain();
            }
        }

        public void cancel() {
            if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
                this.parent.remove(this);
                this.parent.drain();
            }
        }

        public boolean isCancelled() {
            return get() == Long.MIN_VALUE;
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowablePublishMulticast$OutputCanceller */
    static final class OutputCanceller<R> implements FlowableSubscriber<R>, Subscription {
        final Subscriber<? super R> actual;
        final MulticastProcessor<?> processor;

        /* renamed from: s */
        Subscription f182s;

        OutputCanceller(Subscriber<? super R> actual2, MulticastProcessor<?> processor2) {
            this.actual = actual2;
            this.processor = processor2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f182s, s)) {
                this.f182s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(R t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
            this.processor.dispose();
        }

        public void onComplete() {
            this.actual.onComplete();
            this.processor.dispose();
        }

        public void request(long n) {
            this.f182s.request(n);
        }

        public void cancel() {
            this.f182s.cancel();
            this.processor.dispose();
        }
    }

    public FlowablePublishMulticast(Flowable<T> source, Function<? super Flowable<T>, ? extends Publisher<? extends R>> selector2, int prefetch2, boolean delayError2) {
        super(source);
        this.selector = selector2;
        this.prefetch = prefetch2;
        this.delayError = delayError2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> s) {
        MulticastProcessor<T> mp = new MulticastProcessor<>(this.prefetch, this.delayError);
        try {
            ((Publisher) ObjectHelper.requireNonNull(this.selector.apply(mp), "selector returned a null Publisher")).subscribe(new OutputCanceller<>(s, mp));
            this.source.subscribe((FlowableSubscriber<? super T>) mp);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptySubscription.error(ex, s);
        }
    }
}
