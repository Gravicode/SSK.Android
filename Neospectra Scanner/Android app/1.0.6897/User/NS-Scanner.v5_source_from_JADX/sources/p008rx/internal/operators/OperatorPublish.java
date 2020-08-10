package p008rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.exceptions.MissingBackpressureException;
import p008rx.functions.Action0;
import p008rx.functions.Func1;
import p008rx.internal.util.RxRingBuffer;
import p008rx.internal.util.SynchronizedQueue;
import p008rx.internal.util.unsafe.SpscArrayQueue;
import p008rx.internal.util.unsafe.UnsafeAccess;
import p008rx.observables.ConnectableObservable;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.OperatorPublish */
public final class OperatorPublish<T> extends ConnectableObservable<T> {
    final AtomicReference<PublishSubscriber<T>> current;
    final Observable<? extends T> source;

    /* renamed from: rx.internal.operators.OperatorPublish$InnerProducer */
    static final class InnerProducer<T> extends AtomicLong implements Producer, Subscription {
        static final long NOT_REQUESTED = -4611686018427387904L;
        static final long UNSUBSCRIBED = Long.MIN_VALUE;
        private static final long serialVersionUID = -4453897557930727610L;
        final Subscriber<? super T> child;
        final PublishSubscriber<T> parent;

        public InnerProducer(PublishSubscriber<T> parent2, Subscriber<? super T> child2) {
            this.parent = parent2;
            this.child = child2;
            lazySet(NOT_REQUESTED);
        }

        public void request(long n) {
            long r;
            long u;
            if (n >= 0) {
                do {
                    r = get();
                    if (r != UNSUBSCRIBED) {
                        if (r >= 0 && n == 0) {
                            return;
                        }
                        if (r == NOT_REQUESTED) {
                            u = n;
                        } else {
                            u = r + n;
                            if (u < 0) {
                                u = Long.MAX_VALUE;
                            }
                        }
                    } else {
                        return;
                    }
                } while (!compareAndSet(r, u));
                this.parent.dispatch();
            }
        }

        public long produced(long n) {
            long r;
            long u;
            if (n <= 0) {
                throw new IllegalArgumentException("Cant produce zero or less");
            }
            do {
                r = get();
                if (r == NOT_REQUESTED) {
                    throw new IllegalStateException("Produced without request");
                } else if (r == UNSUBSCRIBED) {
                    return UNSUBSCRIBED;
                } else {
                    u = r - n;
                    if (u < 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("More produced (");
                        sb.append(n);
                        sb.append(") than requested (");
                        sb.append(r);
                        sb.append(")");
                        throw new IllegalStateException(sb.toString());
                    }
                }
            } while (!compareAndSet(r, u));
            return u;
        }

        public boolean isUnsubscribed() {
            return get() == UNSUBSCRIBED;
        }

        public void unsubscribe() {
            if (get() != UNSUBSCRIBED && getAndSet(UNSUBSCRIBED) != UNSUBSCRIBED) {
                this.parent.remove(this);
                this.parent.dispatch();
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorPublish$PublishSubscriber */
    static final class PublishSubscriber<T> extends Subscriber<T> implements Subscription {
        static final InnerProducer[] EMPTY = new InnerProducer[0];
        static final InnerProducer[] TERMINATED = new InnerProducer[0];
        final AtomicReference<PublishSubscriber<T>> current;
        boolean emitting;
        boolean missed;
        final AtomicReference<InnerProducer[]> producers;
        final Queue<Object> queue;
        final AtomicBoolean shouldConnect;
        volatile Object terminalEvent;

        public PublishSubscriber(AtomicReference<PublishSubscriber<T>> current2) {
            this.queue = UnsafeAccess.isUnsafeAvailable() ? new SpscArrayQueue<>(RxRingBuffer.SIZE) : new SynchronizedQueue<>(RxRingBuffer.SIZE);
            this.producers = new AtomicReference<>(EMPTY);
            this.current = current2;
            this.shouldConnect = new AtomicBoolean();
        }

        /* access modifiers changed from: 0000 */
        public void init() {
            add(Subscriptions.create(new Action0() {
                public void call() {
                    PublishSubscriber.this.producers.getAndSet(PublishSubscriber.TERMINATED);
                    PublishSubscriber.this.current.compareAndSet(PublishSubscriber.this, null);
                }
            }));
        }

        public void onStart() {
            request((long) RxRingBuffer.SIZE);
        }

        public void onNext(T t) {
            if (!this.queue.offer(NotificationLite.next(t))) {
                onError(new MissingBackpressureException());
            } else {
                dispatch();
            }
        }

        public void onError(Throwable e) {
            if (this.terminalEvent == null) {
                this.terminalEvent = NotificationLite.error(e);
                dispatch();
            }
        }

        public void onCompleted() {
            if (this.terminalEvent == null) {
                this.terminalEvent = NotificationLite.completed();
                dispatch();
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean add(InnerProducer<T> producer) {
            InnerProducer[] c;
            InnerProducer[] u;
            if (producer == null) {
                throw new NullPointerException();
            }
            do {
                c = (InnerProducer[]) this.producers.get();
                if (c == TERMINATED) {
                    return false;
                }
                int len = c.length;
                u = new InnerProducer[(len + 1)];
                System.arraycopy(c, 0, u, 0, len);
                u[len] = producer;
            } while (!this.producers.compareAndSet(c, u));
            return true;
        }

        /* access modifiers changed from: 0000 */
        public void remove(InnerProducer<T> producer) {
            InnerProducer[] c;
            InnerProducer[] u;
            do {
                c = (InnerProducer[]) this.producers.get();
                if (c != EMPTY && c != TERMINATED) {
                    int j = -1;
                    int len = c.length;
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
                            InnerProducer[] u2 = new InnerProducer[(len - 1)];
                            System.arraycopy(c, 0, u2, 0, j);
                            System.arraycopy(c, j + 1, u2, j, (len - j) - 1);
                            u = u2;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } while (!this.producers.compareAndSet(c, u));
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(Object term, boolean empty) {
            int i$ = 0;
            if (term != null) {
                if (!NotificationLite.isCompleted(term)) {
                    Throwable t = NotificationLite.getError(term);
                    this.current.compareAndSet(this, null);
                    try {
                        InnerProducer<?>[] arr$ = (InnerProducer[]) this.producers.getAndSet(TERMINATED);
                        int len$ = arr$.length;
                        while (i$ < len$) {
                            arr$[i$].child.onError(t);
                            i$++;
                        }
                        return true;
                    } finally {
                        unsubscribe();
                    }
                } else if (empty) {
                    this.current.compareAndSet(this, null);
                    try {
                        InnerProducer<?>[] arr$2 = (InnerProducer[]) this.producers.getAndSet(TERMINATED);
                        int len$2 = arr$2.length;
                        while (i$ < len$2) {
                            arr$2[i$].child.onCompleted();
                            i$++;
                        }
                        return true;
                    } finally {
                        unsubscribe();
                    }
                }
            }
            return false;
        }

        /* JADX INFO: finally extract failed */
        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:100:0x0106, code lost:
            r24 = r2;
            r26 = r6;
            r25 = r7;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:102:0x0117, code lost:
            r26 = r6;
            r25 = r7;
            r5 = r5 + 1;
            r2 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:103:0x0123, code lost:
            r25 = r7;
            r2 = r6;
            r6 = r9;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:104:0x0127, code lost:
            if (r5 <= 0) goto L_0x012d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:105:0x0129, code lost:
            request((long) r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:107:0x012f, code lost:
            if (r14 == 0) goto L_0x0137;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:108:0x0131, code lost:
            if (r6 != false) goto L_0x0137;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0012, code lost:
            r4 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:110:0x0134, code lost:
            r22 = r4;
            r2 = r5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:111:0x0137, code lost:
            monitor-enter(r28);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:114:0x013a, code lost:
            if (r1.missed != false) goto L_0x0151;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:115:0x013c, code lost:
            r1.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:118:?, code lost:
            monitor-exit(r28);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:119:0x0141, code lost:
            if (1 != 0) goto L_0x014d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:120:0x0143, code lost:
            monitor-enter(r28);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:123:?, code lost:
            r1.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:124:0x0147, code lost:
            monitor-exit(r28);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:126:0x0149, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:129:0x014c, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
            r5 = r1.terminalEvent;
            r6 = r1.queue.isEmpty();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:130:0x014d, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:131:0x014e, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:132:0x014f, code lost:
            r4 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:135:?, code lost:
            r1.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:136:0x0154, code lost:
            monitor-exit(r28);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:137:0x0156, code lost:
            r4 = r22;
            r2 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:138:0x015c, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:139:0x015d, code lost:
            r3 = r0;
            r4 = r22;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x001f, code lost:
            if (checkTerminated(r5, r6) == false) goto L_0x002e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:141:?, code lost:
            monitor-exit(r28);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:143:?, code lost:
            throw r3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:144:0x0162, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:145:0x0163, code lost:
            r2 = r0;
            r22 = r4;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:146:0x0167, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:147:0x0168, code lost:
            r3 = r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:148:0x016a, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:149:0x016b, code lost:
            r2 = r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:150:0x016d, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:151:0x016e, code lost:
            r22 = r4;
            r2 = r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:152:0x0171, code lost:
            if (r22 == false) goto L_0x0173;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:153:0x0173, code lost:
            monitor-enter(r28);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:156:?, code lost:
            r1.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:159:0x0179, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0022, code lost:
            if (1 != 0) goto L_0x002d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:162:0x017c, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:163:0x017d, code lost:
            throw r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0024, code lost:
            monitor-enter(r28);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
            r1.emitting = r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0027, code lost:
            monitor-exit(r28);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0029, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x002c, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x002d, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x002e, code lost:
            if (r6 != false) goto L_0x0134;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
            r7 = (p008rx.internal.operators.OperatorPublish.InnerProducer[]) r1.producers.get();
            r8 = r7.length;
            r11 = 0;
            r12 = r7;
            r13 = r12.length;
            r14 = Long.MAX_VALUE;
            r9 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0045, code lost:
            if (r9 >= r13) goto L_0x006c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x004d, code lost:
            r22 = r4;
            r3 = r12[r9].get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x0055, code lost:
            if (r3 < 0) goto L_0x005e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
            r14 = java.lang.Math.min(r14, r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0062, code lost:
            if (r3 != Long.MIN_VALUE) goto L_0x0066;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x0064, code lost:
            r11 = r11 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x0066, code lost:
            r9 = r9 + 1;
            r4 = r22;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x006c, code lost:
            r22 = r4;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0070, code lost:
            if (r8 != r11) goto L_0x0098;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x0072, code lost:
            r5 = r1.terminalEvent;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x007b, code lost:
            if (r1.queue.poll() != null) goto L_0x007f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x007d, code lost:
            r10 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x007f, code lost:
            r10 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x0084, code lost:
            if (checkTerminated(r5, r10) == false) goto L_0x0093;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x0087, code lost:
            if (1 != 0) goto L_0x0092;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x0089, code lost:
            monitor-enter(r28);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:52:?, code lost:
            r1.emitting = r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x008c, code lost:
            monitor-exit(r28);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x008e, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:58:0x0091, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x0092, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:?, code lost:
            request(1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:62:0x0098, code lost:
            r9 = r6;
            r6 = r5;
            r5 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:64:0x009e, code lost:
            if (((long) r5) >= r14) goto L_0x0123;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:65:0x00a0, code lost:
            r6 = r1.terminalEvent;
            r10 = r1.queue.poll();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:66:0x00a9, code lost:
            if (r10 != null) goto L_0x00ad;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:67:0x00ab, code lost:
            r12 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:0x00ad, code lost:
            r12 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:69:0x00ae, code lost:
            r9 = r12;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:70:0x00b3, code lost:
            if (checkTerminated(r6, r9) == false) goto L_0x00c2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:72:0x00b6, code lost:
            if (1 != 0) goto L_0x00c1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:73:0x00b8, code lost:
            monitor-enter(r28);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:75:?, code lost:
            r1.emitting = r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:76:0x00bb, code lost:
            monitor-exit(r28);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:78:0x00bd, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:81:0x00c0, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:82:0x00c1, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:83:0x00c2, code lost:
            if (r9 == false) goto L_0x00cb;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:84:0x00c4, code lost:
            r2 = r6;
            r25 = r7;
            r6 = r9;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:86:?, code lost:
            r12 = p008rx.internal.operators.NotificationLite.getValue(r10);
            r13 = r7;
            r2 = r13.length;
            r18 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:87:0x00d3, code lost:
            r3 = r18;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:88:0x00d7, code lost:
            if (r3 >= r2) goto L_0x0117;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:89:0x00d9, code lost:
            r4 = r13[r3];
         */
        /* JADX WARNING: Code restructure failed: missing block: B:91:0x00e1, code lost:
            if (r4.get() <= 0) goto L_0x0106;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:92:0x00e3, code lost:
            r24 = r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:94:?, code lost:
            r4.child.onNext(r12);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:95:0x00ea, code lost:
            r26 = r6;
            r25 = r7;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:97:?, code lost:
            r4.produced(1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:98:0x00f5, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:99:0x00f6, code lost:
            r26 = r6;
            r25 = r7;
            r2 = r0;
            r4.unsubscribe();
            p008rx.exceptions.Exceptions.throwOrReport(r2, (p008rx.Observer<?>) r4.child, (java.lang.Object) r12);
         */
        /* JADX WARNING: Removed duplicated region for block: B:153:0x0173  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void dispatch() {
            /*
                r28 = this;
                r1 = r28
                monitor-enter(r28)
                boolean r2 = r1.emitting     // Catch:{ all -> 0x017e }
                r3 = 1
                if (r2 == 0) goto L_0x000c
                r1.missed = r3     // Catch:{ all -> 0x017e }
                monitor-exit(r28)     // Catch:{ all -> 0x017e }
                return
            L_0x000c:
                r1.emitting = r3     // Catch:{ all -> 0x017e }
                r2 = 0
                r1.missed = r2     // Catch:{ all -> 0x017e }
                monitor-exit(r28)     // Catch:{ all -> 0x017e }
                r4 = 0
            L_0x0013:
                java.lang.Object r5 = r1.terminalEvent     // Catch:{ all -> 0x016d }
                java.util.Queue<java.lang.Object> r6 = r1.queue     // Catch:{ all -> 0x016d }
                boolean r6 = r6.isEmpty()     // Catch:{ all -> 0x016d }
                boolean r7 = r1.checkTerminated(r5, r6)     // Catch:{ all -> 0x016d }
                if (r7 == 0) goto L_0x002e
                r3 = 1
                if (r3 != 0) goto L_0x002d
                monitor-enter(r28)
                r1.emitting = r2     // Catch:{ all -> 0x0029 }
                monitor-exit(r28)     // Catch:{ all -> 0x0029 }
                goto L_0x002d
            L_0x0029:
                r0 = move-exception
                r2 = r0
                monitor-exit(r28)     // Catch:{ all -> 0x0029 }
                throw r2
            L_0x002d:
                return
            L_0x002e:
                if (r6 != 0) goto L_0x0134
                java.util.concurrent.atomic.AtomicReference<rx.internal.operators.OperatorPublish$InnerProducer[]> r7 = r1.producers     // Catch:{ all -> 0x016d }
                java.lang.Object r7 = r7.get()     // Catch:{ all -> 0x016d }
                rx.internal.operators.OperatorPublish$InnerProducer[] r7 = (p008rx.internal.operators.OperatorPublish.InnerProducer[]) r7     // Catch:{ all -> 0x016d }
                int r8 = r7.length     // Catch:{ all -> 0x016d }
                r9 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                r11 = 0
                r12 = r7
                int r13 = r12.length     // Catch:{ all -> 0x016d }
                r14 = r9
                r9 = 0
            L_0x0043:
                r16 = 0
                if (r9 >= r13) goto L_0x006c
                r10 = r12[r9]     // Catch:{ all -> 0x016d }
                long r18 = r10.get()     // Catch:{ all -> 0x016d }
                r20 = r18
                r22 = r4
                r3 = r20
                int r16 = (r3 > r16 ? 1 : (r3 == r16 ? 0 : -1))
                if (r16 < 0) goto L_0x005e
                long r16 = java.lang.Math.min(r14, r3)     // Catch:{ all -> 0x016a }
                r14 = r16
                goto L_0x0066
            L_0x005e:
                r16 = -9223372036854775808
                int r16 = (r3 > r16 ? 1 : (r3 == r16 ? 0 : -1))
                if (r16 != 0) goto L_0x0066
                int r11 = r11 + 1
            L_0x0066:
                int r9 = r9 + 1
                r4 = r22
                r3 = 1
                goto L_0x0043
            L_0x006c:
                r22 = r4
                r3 = 1
                if (r8 != r11) goto L_0x0098
                java.lang.Object r9 = r1.terminalEvent     // Catch:{ all -> 0x016a }
                r5 = r9
                java.util.Queue<java.lang.Object> r9 = r1.queue     // Catch:{ all -> 0x016a }
                java.lang.Object r9 = r9.poll()     // Catch:{ all -> 0x016a }
                if (r9 != 0) goto L_0x007f
                r10 = 1
                goto L_0x0080
            L_0x007f:
                r10 = 0
            L_0x0080:
                boolean r10 = r1.checkTerminated(r5, r10)     // Catch:{ all -> 0x016a }
                if (r10 == 0) goto L_0x0093
                r3 = 1
                if (r3 != 0) goto L_0x0092
                monitor-enter(r28)
                r1.emitting = r2     // Catch:{ all -> 0x008e }
                monitor-exit(r28)     // Catch:{ all -> 0x008e }
                goto L_0x0092
            L_0x008e:
                r0 = move-exception
                r2 = r0
                monitor-exit(r28)     // Catch:{ all -> 0x008e }
                throw r2
            L_0x0092:
                return
            L_0x0093:
                r1.request(r3)     // Catch:{ all -> 0x016a }
                goto L_0x0156
            L_0x0098:
                r9 = r6
                r6 = r5
                r5 = 0
            L_0x009b:
                long r12 = (long) r5     // Catch:{ all -> 0x016a }
                int r10 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1))
                if (r10 >= 0) goto L_0x0123
                java.lang.Object r10 = r1.terminalEvent     // Catch:{ all -> 0x016a }
                r6 = r10
                java.util.Queue<java.lang.Object> r10 = r1.queue     // Catch:{ all -> 0x016a }
                java.lang.Object r10 = r10.poll()     // Catch:{ all -> 0x016a }
                if (r10 != 0) goto L_0x00ad
                r12 = 1
                goto L_0x00ae
            L_0x00ad:
                r12 = 0
            L_0x00ae:
                r9 = r12
                boolean r12 = r1.checkTerminated(r6, r9)     // Catch:{ all -> 0x016a }
                if (r12 == 0) goto L_0x00c2
                r3 = 1
                if (r3 != 0) goto L_0x00c1
                monitor-enter(r28)
                r1.emitting = r2     // Catch:{ all -> 0x00bd }
                monitor-exit(r28)     // Catch:{ all -> 0x00bd }
                goto L_0x00c1
            L_0x00bd:
                r0 = move-exception
                r2 = r0
                monitor-exit(r28)     // Catch:{ all -> 0x00bd }
                throw r2
            L_0x00c1:
                return
            L_0x00c2:
                if (r9 == 0) goto L_0x00cb
                r2 = r6
                r25 = r7
                r6 = r9
                goto L_0x0127
            L_0x00cb:
                java.lang.Object r12 = p008rx.internal.operators.NotificationLite.getValue(r10)     // Catch:{ all -> 0x016a }
                r13 = r7
                int r2 = r13.length     // Catch:{ all -> 0x016a }
                r18 = 0
            L_0x00d3:
                r23 = r18
                r3 = r23
                if (r3 >= r2) goto L_0x0117
                r4 = r13[r3]     // Catch:{ all -> 0x016a }
                long r18 = r4.get()     // Catch:{ all -> 0x016a }
                int r18 = (r18 > r16 ? 1 : (r18 == r16 ? 0 : -1))
                if (r18 <= 0) goto L_0x0106
                r24 = r2
                rx.Subscriber<? super T> r2 = r4.child     // Catch:{ Throwable -> 0x00f5 }
                r2.onNext(r12)     // Catch:{ Throwable -> 0x00f5 }
                r26 = r6
                r25 = r7
                r6 = 1
                r4.produced(r6)     // Catch:{ all -> 0x016a }
                goto L_0x010c
            L_0x00f5:
                r0 = move-exception
                r26 = r6
                r25 = r7
                r6 = 1
                r2 = r0
                r4.unsubscribe()     // Catch:{ all -> 0x016a }
                rx.Subscriber<? super T> r6 = r4.child     // Catch:{ all -> 0x016a }
                p008rx.exceptions.Exceptions.throwOrReport(r2, r6, r12)     // Catch:{ all -> 0x016a }
                goto L_0x010c
            L_0x0106:
                r24 = r2
                r26 = r6
                r25 = r7
            L_0x010c:
                int r18 = r3 + 1
                r2 = r24
                r7 = r25
                r6 = r26
                r3 = 1
                goto L_0x00d3
            L_0x0117:
                r26 = r6
                r25 = r7
                int r5 = r5 + 1
                r2 = 0
                r3 = 1
                goto L_0x009b
            L_0x0123:
                r25 = r7
                r2 = r6
                r6 = r9
            L_0x0127:
                if (r5 <= 0) goto L_0x012d
                long r3 = (long) r5     // Catch:{ all -> 0x016a }
                r1.request(r3)     // Catch:{ all -> 0x016a }
            L_0x012d:
                int r3 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1))
                if (r3 == 0) goto L_0x0137
                if (r6 != 0) goto L_0x0137
                goto L_0x0156
            L_0x0134:
                r22 = r4
                r2 = r5
            L_0x0137:
                monitor-enter(r28)     // Catch:{ all -> 0x016a }
                boolean r3 = r1.missed     // Catch:{ all -> 0x015c }
                if (r3 != 0) goto L_0x0151
                r3 = 0
                r1.emitting = r3     // Catch:{ all -> 0x015c }
                r3 = 1
                monitor-exit(r28)     // Catch:{ all -> 0x014e }
                if (r3 != 0) goto L_0x014d
                monitor-enter(r28)
                r4 = 0
                r1.emitting = r4     // Catch:{ all -> 0x0149 }
                monitor-exit(r28)     // Catch:{ all -> 0x0149 }
                goto L_0x014d
            L_0x0149:
                r0 = move-exception
                r4 = r0
                monitor-exit(r28)     // Catch:{ all -> 0x0149 }
                throw r4
            L_0x014d:
                return
            L_0x014e:
                r0 = move-exception
                r4 = r3
                goto L_0x0168
            L_0x0151:
                r3 = 0
                r1.missed = r3     // Catch:{ all -> 0x015c }
                monitor-exit(r28)     // Catch:{ all -> 0x015c }
            L_0x0156:
                r4 = r22
                r2 = 0
                r3 = 1
                goto L_0x0013
            L_0x015c:
                r0 = move-exception
                r3 = r0
                r4 = r22
            L_0x0160:
                monitor-exit(r28)     // Catch:{ all -> 0x0167 }
                throw r3     // Catch:{ all -> 0x0162 }
            L_0x0162:
                r0 = move-exception
                r2 = r0
                r22 = r4
                goto L_0x0171
            L_0x0167:
                r0 = move-exception
            L_0x0168:
                r3 = r0
                goto L_0x0160
            L_0x016a:
                r0 = move-exception
                r2 = r0
                goto L_0x0171
            L_0x016d:
                r0 = move-exception
                r22 = r4
                r2 = r0
            L_0x0171:
                if (r22 != 0) goto L_0x017d
                monitor-enter(r28)
                r3 = 0
                r1.emitting = r3     // Catch:{ all -> 0x0179 }
                monitor-exit(r28)     // Catch:{ all -> 0x0179 }
                goto L_0x017d
            L_0x0179:
                r0 = move-exception
                r2 = r0
                monitor-exit(r28)     // Catch:{ all -> 0x0179 }
                throw r2
            L_0x017d:
                throw r2
            L_0x017e:
                r0 = move-exception
                r2 = r0
                monitor-exit(r28)     // Catch:{ all -> 0x017e }
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorPublish.PublishSubscriber.dispatch():void");
        }
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> source2) {
        final AtomicReference<PublishSubscriber<T>> curr = new AtomicReference<>();
        return new OperatorPublish(new OnSubscribe<T>() {
            public void call(Subscriber<? super T> child) {
                while (true) {
                    PublishSubscriber<T> r = (PublishSubscriber) curr.get();
                    if (r == null || r.isUnsubscribed()) {
                        PublishSubscriber<T> u = new PublishSubscriber<>(curr);
                        u.init();
                        if (!curr.compareAndSet(r, u)) {
                            continue;
                        } else {
                            r = u;
                        }
                    }
                    InnerProducer<T> inner = new InnerProducer<>(r, child);
                    if (r.add(inner)) {
                        child.add(inner);
                        child.setProducer(inner);
                        return;
                    }
                }
            }
        }, source2, curr);
    }

    public static <T, R> Observable<R> create(Observable<? extends T> source2, Func1<? super Observable<T>, ? extends Observable<R>> selector) {
        return create(source2, selector, false);
    }

    public static <T, R> Observable<R> create(final Observable<? extends T> source2, final Func1<? super Observable<T>, ? extends Observable<R>> selector, final boolean delayError) {
        return create((OnSubscribe<T>) new OnSubscribe<R>() {
            public void call(final Subscriber<? super R> child) {
                final OnSubscribePublishMulticast<T> op = new OnSubscribePublishMulticast<>(RxRingBuffer.SIZE, delayError);
                Subscriber<R> subscriber = new Subscriber<R>() {
                    public void onNext(R t) {
                        child.onNext(t);
                    }

                    public void onError(Throwable e) {
                        op.unsubscribe();
                        child.onError(e);
                    }

                    public void onCompleted() {
                        op.unsubscribe();
                        child.onCompleted();
                    }

                    public void setProducer(Producer p) {
                        child.setProducer(p);
                    }
                };
                child.add(op);
                child.add(subscriber);
                ((Observable) selector.call(Observable.create((OnSubscribe<T>) op))).unsafeSubscribe(subscriber);
                source2.unsafeSubscribe(op.subscriber());
            }
        });
    }

    private OperatorPublish(OnSubscribe<T> onSubscribe, Observable<? extends T> source2, AtomicReference<PublishSubscriber<T>> current2) {
        super(onSubscribe);
        this.source = source2;
        this.current = current2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void connect(p008rx.functions.Action1<? super p008rx.Subscription> r5) {
        /*
            r4 = this;
        L_0x0000:
            java.util.concurrent.atomic.AtomicReference<rx.internal.operators.OperatorPublish$PublishSubscriber<T>> r0 = r4.current
            java.lang.Object r0 = r0.get()
            rx.internal.operators.OperatorPublish$PublishSubscriber r0 = (p008rx.internal.operators.OperatorPublish.PublishSubscriber) r0
            if (r0 == 0) goto L_0x0010
            boolean r1 = r0.isUnsubscribed()
            if (r1 == 0) goto L_0x0024
        L_0x0010:
            rx.internal.operators.OperatorPublish$PublishSubscriber r1 = new rx.internal.operators.OperatorPublish$PublishSubscriber
            java.util.concurrent.atomic.AtomicReference<rx.internal.operators.OperatorPublish$PublishSubscriber<T>> r2 = r4.current
            r1.<init>(r2)
            r1.init()
            java.util.concurrent.atomic.AtomicReference<rx.internal.operators.OperatorPublish$PublishSubscriber<T>> r2 = r4.current
            boolean r2 = r2.compareAndSet(r0, r1)
            if (r2 != 0) goto L_0x0023
            goto L_0x0000
        L_0x0023:
            r0 = r1
        L_0x0024:
            java.util.concurrent.atomic.AtomicBoolean r1 = r0.shouldConnect
            boolean r1 = r1.get()
            r2 = 1
            r3 = 0
            if (r1 != 0) goto L_0x0037
            java.util.concurrent.atomic.AtomicBoolean r1 = r0.shouldConnect
            boolean r1 = r1.compareAndSet(r3, r2)
            if (r1 == 0) goto L_0x0037
            goto L_0x0038
        L_0x0037:
            r2 = 0
        L_0x0038:
            r1 = r2
            r5.call(r0)
            if (r1 == 0) goto L_0x0044
            rx.Observable<? extends T> r2 = r4.source
            r2.unsafeSubscribe(r0)
        L_0x0044:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorPublish.connect(rx.functions.Action1):void");
    }
}
