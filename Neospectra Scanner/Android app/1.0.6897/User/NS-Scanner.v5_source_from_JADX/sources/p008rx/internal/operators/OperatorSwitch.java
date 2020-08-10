package p008rx.internal.operators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import p008rx.Observable;
import p008rx.Observable.Operator;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.exceptions.CompositeException;
import p008rx.functions.Action0;
import p008rx.internal.util.RxRingBuffer;
import p008rx.internal.util.atomic.SpscLinkedArrayQueue;
import p008rx.plugins.RxJavaHooks;
import p008rx.subscriptions.SerialSubscription;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.OperatorSwitch */
public final class OperatorSwitch<T> implements Operator<T, Observable<? extends T>> {
    final boolean delayError;

    /* renamed from: rx.internal.operators.OperatorSwitch$Holder */
    static final class Holder {
        static final OperatorSwitch<Object> INSTANCE = new OperatorSwitch<>(false);

        Holder() {
        }
    }

    /* renamed from: rx.internal.operators.OperatorSwitch$HolderDelayError */
    static final class HolderDelayError {
        static final OperatorSwitch<Object> INSTANCE = new OperatorSwitch<>(true);

        HolderDelayError() {
        }
    }

    /* renamed from: rx.internal.operators.OperatorSwitch$InnerSubscriber */
    static final class InnerSubscriber<T> extends Subscriber<T> {
        /* access modifiers changed from: private */

        /* renamed from: id */
        public final long f910id;
        private final SwitchSubscriber<T> parent;

        InnerSubscriber(long id, SwitchSubscriber<T> parent2) {
            this.f910id = id;
            this.parent = parent2;
        }

        public void setProducer(Producer p) {
            this.parent.innerProducer(p, this.f910id);
        }

        public void onNext(T t) {
            this.parent.emit(t, this);
        }

        public void onError(Throwable e) {
            this.parent.error(e, this.f910id);
        }

        public void onCompleted() {
            this.parent.complete(this.f910id);
        }
    }

    /* renamed from: rx.internal.operators.OperatorSwitch$SwitchSubscriber */
    static final class SwitchSubscriber<T> extends Subscriber<Observable<? extends T>> {
        static final Throwable TERMINAL_ERROR = new Throwable("Terminal error");
        final Subscriber<? super T> child;
        final boolean delayError;
        boolean emitting;
        Throwable error;
        final AtomicLong index;
        boolean innerActive;
        volatile boolean mainDone;
        boolean missed;
        Producer producer;
        final SpscLinkedArrayQueue<Object> queue;
        long requested;
        final SerialSubscription serial = new SerialSubscription();

        SwitchSubscriber(Subscriber<? super T> child2, boolean delayError2) {
            this.child = child2;
            this.delayError = delayError2;
            this.index = new AtomicLong();
            this.queue = new SpscLinkedArrayQueue<>(RxRingBuffer.SIZE);
        }

        /* access modifiers changed from: 0000 */
        public void init() {
            this.child.add(this.serial);
            this.child.add(Subscriptions.create(new Action0() {
                public void call() {
                    SwitchSubscriber.this.clearProducer();
                }
            }));
            this.child.setProducer(new Producer() {
                public void request(long n) {
                    if (n > 0) {
                        SwitchSubscriber.this.childRequested(n);
                    } else if (n < 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("n >= 0 expected but it was ");
                        sb.append(n);
                        throw new IllegalArgumentException(sb.toString());
                    }
                }
            });
        }

        /* access modifiers changed from: 0000 */
        public void clearProducer() {
            synchronized (this) {
                this.producer = null;
            }
        }

        public void onNext(Observable<? extends T> t) {
            long id = this.index.incrementAndGet();
            Subscription s = this.serial.get();
            if (s != null) {
                s.unsubscribe();
            }
            synchronized (this) {
                try {
                    InnerSubscriber<T> inner = new InnerSubscriber<>(id, this);
                    try {
                        this.innerActive = true;
                        this.producer = null;
                        this.serial.set(inner);
                        t.unsafeSubscribe(inner);
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:5:0x0006, code lost:
            if (r0 == false) goto L_0x000f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:6:0x0008, code lost:
            r2.mainDone = true;
            drain();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:7:0x000f, code lost:
            pluginError(r3);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onError(java.lang.Throwable r3) {
            /*
                r2 = this;
                monitor-enter(r2)
                boolean r0 = r2.updateError(r3)     // Catch:{ all -> 0x0015 }
                monitor-exit(r2)     // Catch:{ all -> 0x0013 }
                if (r0 == 0) goto L_0x000f
                r1 = 1
                r2.mainDone = r1
                r2.drain()
                goto L_0x0012
            L_0x000f:
                r2.pluginError(r3)
            L_0x0012:
                return
            L_0x0013:
                r1 = move-exception
                goto L_0x0017
            L_0x0015:
                r1 = move-exception
                r0 = 0
            L_0x0017:
                monitor-exit(r2)     // Catch:{ all -> 0x0013 }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorSwitch.SwitchSubscriber.onError(java.lang.Throwable):void");
        }

        /* access modifiers changed from: 0000 */
        public boolean updateError(Throwable next) {
            Throwable e = this.error;
            if (e == TERMINAL_ERROR) {
                return false;
            }
            if (e == null) {
                this.error = next;
            } else if (e instanceof CompositeException) {
                List<Throwable> list = new ArrayList<>(((CompositeException) e).getExceptions());
                list.add(next);
                this.error = new CompositeException((Collection<? extends Throwable>) list);
            } else {
                this.error = new CompositeException(e, next);
            }
            return true;
        }

        public void onCompleted() {
            this.mainDone = true;
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void emit(T value, InnerSubscriber<T> inner) {
            synchronized (this) {
                if (this.index.get() == inner.f910id) {
                    this.queue.offer(inner, NotificationLite.next(value));
                    drain();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x001e, code lost:
            if (r0 == false) goto L_0x0024;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0020, code lost:
            drain();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0024, code lost:
            pluginError(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void error(java.lang.Throwable r5, long r6) {
            /*
                r4 = this;
                monitor-enter(r4)
                r0 = 0
                java.util.concurrent.atomic.AtomicLong r1 = r4.index     // Catch:{ all -> 0x0028 }
                long r1 = r1.get()     // Catch:{ all -> 0x0028 }
                int r1 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1))
                if (r1 != 0) goto L_0x001b
                boolean r1 = r4.updateError(r5)     // Catch:{ all -> 0x0028 }
                r4.innerActive = r0     // Catch:{ all -> 0x0016 }
                r0 = 0
                r4.producer = r0     // Catch:{ all -> 0x0016 }
                goto L_0x001c
            L_0x0016:
                r0 = move-exception
                r3 = r1
                r1 = r0
                r0 = r3
                goto L_0x0029
            L_0x001b:
                r1 = 1
            L_0x001c:
                r0 = r1
                monitor-exit(r4)     // Catch:{ all -> 0x0028 }
                if (r0 == 0) goto L_0x0024
                r4.drain()
                goto L_0x0027
            L_0x0024:
                r4.pluginError(r5)
            L_0x0027:
                return
            L_0x0028:
                r1 = move-exception
            L_0x0029:
                monitor-exit(r4)     // Catch:{ all -> 0x0028 }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorSwitch.SwitchSubscriber.error(java.lang.Throwable, long):void");
        }

        /* access modifiers changed from: 0000 */
        public void complete(long id) {
            synchronized (this) {
                if (this.index.get() == id) {
                    this.innerActive = false;
                    this.producer = null;
                    drain();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void pluginError(Throwable e) {
            RxJavaHooks.onError(e);
        }

        /* access modifiers changed from: 0000 */
        public void innerProducer(Producer p, long id) {
            synchronized (this) {
                if (this.index.get() == id) {
                    long n = this.requested;
                    this.producer = p;
                    p.request(n);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:6:0x000c, code lost:
            if (r0 == null) goto L_0x0011;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:7:0x000e, code lost:
            r0.request(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x0011, code lost:
            drain();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0014, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void childRequested(long r4) {
            /*
                r3 = this;
                monitor-enter(r3)
                rx.Producer r0 = r3.producer     // Catch:{ all -> 0x0017 }
                long r1 = r3.requested     // Catch:{ all -> 0x0015 }
                long r1 = p008rx.internal.operators.BackpressureUtils.addCap(r1, r4)     // Catch:{ all -> 0x0015 }
                r3.requested = r1     // Catch:{ all -> 0x0015 }
                monitor-exit(r3)     // Catch:{ all -> 0x0015 }
                if (r0 == 0) goto L_0x0011
                r0.request(r4)
            L_0x0011:
                r3.drain()
                return
            L_0x0015:
                r1 = move-exception
                goto L_0x0019
            L_0x0017:
                r1 = move-exception
                r0 = 0
            L_0x0019:
                monitor-exit(r3)     // Catch:{ all -> 0x0015 }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorSwitch.SwitchSubscriber.childRequested(long):void");
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0028, code lost:
            r14 = r10.queue;
            r15 = r10.index;
            r9 = r10.child;
            r19 = r1;
            r18 = r2;
            r16 = r3;
            r2 = r10.mainDone;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0037, code lost:
            r3 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0038, code lost:
            r20 = r3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x003c, code lost:
            if (r20 == r16) goto L_0x007f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0042, code lost:
            if (r9.isUnsubscribed() == false) goto L_0x0045;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0044, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x0045, code lost:
            r8 = r14.isEmpty();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0055, code lost:
            if (checkTerminated(r2, r18, r19, r14, r9, r8) == false) goto L_0x0058;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0057, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0058, code lost:
            if (r8 == false) goto L_0x005b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x005b, code lost:
            r1 = (p008rx.internal.operators.OperatorSwitch.InnerSubscriber) r14.poll();
            r3 = p008rx.internal.operators.NotificationLite.getValue(r14.poll());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x0073, code lost:
            if (r15.get() != p008rx.internal.operators.OperatorSwitch.InnerSubscriber.access$000(r1)) goto L_0x007c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x0075, code lost:
            r9.onNext(r3);
            r20 = r20 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x007c, code lost:
            r3 = r20;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0081, code lost:
            if (r20 != r16) goto L_0x00a1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x0087, code lost:
            if (r9.isUnsubscribed() == false) goto L_0x008a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x0089, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x008a, code lost:
            r22 = r9;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x009e, code lost:
            if (checkTerminated(r10.mainDone, r18, r19, r14, r9, r14.isEmpty()) == false) goto L_0x00a3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x00a0, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x00a1, code lost:
            r22 = r9;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x00a3, code lost:
            monitor-enter(r23);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:?, code lost:
            r3 = r10.requested;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x00ad, code lost:
            if (r3 == Long.MAX_VALUE) goto L_0x00b9;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x00af, code lost:
            r3 = r3 - r20;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:52:?, code lost:
            r10.requested = r3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x00b4, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x00b5, code lost:
            r1 = r0;
            r16 = r3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x00b9, code lost:
            r16 = r3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:58:0x00bd, code lost:
            if (r10.missed != false) goto L_0x00c3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x00bf, code lost:
            r10.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:60:0x00c1, code lost:
            monitor-exit(r23);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:0x00c2, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:62:0x00c3, code lost:
            r10.missed = false;
            r2 = r10.mainDone;
            r18 = r10.innerActive;
            r1 = r10.error;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:0x00ce, code lost:
            if (r1 == null) goto L_0x00e1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:66:0x00d2, code lost:
            if (r1 == TERMINAL_ERROR) goto L_0x00e1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:0x00d6, code lost:
            if (r10.delayError != false) goto L_0x00e1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:69:0x00d8, code lost:
            r10.error = TERMINAL_ERROR;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:70:0x00dd, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:71:0x00de, code lost:
            r19 = r1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:72:0x00e1, code lost:
            monitor-exit(r23);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:73:0x00e2, code lost:
            r19 = r1;
            r9 = r22;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:74:0x00e9, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:75:0x00ea, code lost:
            r1 = r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:77:?, code lost:
            monitor-exit(r23);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:78:0x00ec, code lost:
            throw r1;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            /*
                r23 = this;
                r10 = r23
                monitor-enter(r23)
                r11 = 0
                r13 = 0
                r1 = 0
                boolean r2 = r10.emitting     // Catch:{ all -> 0x00f4 }
                r3 = 1
                if (r2 == 0) goto L_0x0010
                r10.missed = r3     // Catch:{ all -> 0x00f4 }
                monitor-exit(r23)     // Catch:{ all -> 0x00f4 }
                return
            L_0x0010:
                r10.emitting = r3     // Catch:{ all -> 0x00f4 }
                boolean r2 = r10.innerActive     // Catch:{ all -> 0x00f4 }
                long r3 = r10.requested     // Catch:{ all -> 0x00f1 }
                java.lang.Throwable r5 = r10.error     // Catch:{ all -> 0x00ed }
                r1 = r5
                if (r1 == 0) goto L_0x0027
                java.lang.Throwable r5 = TERMINAL_ERROR     // Catch:{ all -> 0x00ed }
                if (r1 == r5) goto L_0x0027
                boolean r5 = r10.delayError     // Catch:{ all -> 0x00ed }
                if (r5 != 0) goto L_0x0027
                java.lang.Throwable r5 = TERMINAL_ERROR     // Catch:{ all -> 0x00ed }
                r10.error = r5     // Catch:{ all -> 0x00ed }
            L_0x0027:
                monitor-exit(r23)     // Catch:{ all -> 0x00ed }
                rx.internal.util.atomic.SpscLinkedArrayQueue<java.lang.Object> r14 = r10.queue
                java.util.concurrent.atomic.AtomicLong r15 = r10.index
                rx.Subscriber<? super T> r9 = r10.child
                boolean r5 = r10.mainDone
                r19 = r1
                r18 = r2
                r16 = r3
                r2 = r5
            L_0x0037:
                r3 = r11
            L_0x0038:
                r20 = r3
                int r1 = (r20 > r16 ? 1 : (r20 == r16 ? 0 : -1))
                if (r1 == 0) goto L_0x007f
                boolean r1 = r9.isUnsubscribed()
                if (r1 == 0) goto L_0x0045
                return
            L_0x0045:
                boolean r8 = r14.isEmpty()
                r1 = r10
                r3 = r18
                r4 = r19
                r5 = r14
                r6 = r9
                r7 = r8
                boolean r1 = r1.checkTerminated(r2, r3, r4, r5, r6, r7)
                if (r1 == 0) goto L_0x0058
                return
            L_0x0058:
                if (r8 == 0) goto L_0x005b
                goto L_0x007f
            L_0x005b:
                java.lang.Object r1 = r14.poll()
                rx.internal.operators.OperatorSwitch$InnerSubscriber r1 = (p008rx.internal.operators.OperatorSwitch.InnerSubscriber) r1
                java.lang.Object r3 = r14.poll()
                java.lang.Object r3 = p008rx.internal.operators.NotificationLite.getValue(r3)
                long r4 = r15.get()
                long r6 = r1.f910id
                int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
                if (r4 != 0) goto L_0x007c
                r9.onNext(r3)
                r4 = 1
                long r20 = r20 + r4
            L_0x007c:
                r3 = r20
                goto L_0x0038
            L_0x007f:
                int r1 = (r20 > r16 ? 1 : (r20 == r16 ? 0 : -1))
                if (r1 != 0) goto L_0x00a1
                boolean r1 = r9.isUnsubscribed()
                if (r1 == 0) goto L_0x008a
                return
            L_0x008a:
                boolean r4 = r10.mainDone
                boolean r1 = r14.isEmpty()
                r3 = r10
                r5 = r18
                r6 = r19
                r7 = r14
                r8 = r9
                r22 = r9
                r9 = r1
                boolean r1 = r3.checkTerminated(r4, r5, r6, r7, r8, r9)
                if (r1 == 0) goto L_0x00a3
                return
            L_0x00a1:
                r22 = r9
            L_0x00a3:
                monitor-enter(r23)
                long r3 = r10.requested     // Catch:{ all -> 0x00e9 }
                r5 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r1 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
                if (r1 == 0) goto L_0x00b9
                long r3 = r3 - r20
                r10.requested = r3     // Catch:{ all -> 0x00b4 }
                goto L_0x00b9
            L_0x00b4:
                r0 = move-exception
                r1 = r0
                r16 = r3
                goto L_0x00eb
            L_0x00b9:
                r16 = r3
                boolean r1 = r10.missed     // Catch:{ all -> 0x00e9 }
                if (r1 != 0) goto L_0x00c3
                r10.emitting = r13     // Catch:{ all -> 0x00e9 }
                monitor-exit(r23)     // Catch:{ all -> 0x00e9 }
                return
            L_0x00c3:
                r10.missed = r13     // Catch:{ all -> 0x00e9 }
                boolean r1 = r10.mainDone     // Catch:{ all -> 0x00e9 }
                r2 = r1
                boolean r1 = r10.innerActive     // Catch:{ all -> 0x00e9 }
                r18 = r1
                java.lang.Throwable r1 = r10.error     // Catch:{ all -> 0x00e9 }
                if (r1 == 0) goto L_0x00e1
                java.lang.Throwable r3 = TERMINAL_ERROR     // Catch:{ all -> 0x00dd }
                if (r1 == r3) goto L_0x00e1
                boolean r3 = r10.delayError     // Catch:{ all -> 0x00dd }
                if (r3 != 0) goto L_0x00e1
                java.lang.Throwable r3 = TERMINAL_ERROR     // Catch:{ all -> 0x00dd }
                r10.error = r3     // Catch:{ all -> 0x00dd }
                goto L_0x00e1
            L_0x00dd:
                r0 = move-exception
                r19 = r1
                goto L_0x00ea
            L_0x00e1:
                monitor-exit(r23)     // Catch:{ all -> 0x00dd }
                r19 = r1
                r9 = r22
                goto L_0x0037
            L_0x00e9:
                r0 = move-exception
            L_0x00ea:
                r1 = r0
            L_0x00eb:
                monitor-exit(r23)     // Catch:{ all -> 0x00e9 }
                throw r1
            L_0x00ed:
                r0 = move-exception
                r13 = r2
                r11 = r3
                goto L_0x00f5
            L_0x00f1:
                r0 = move-exception
                r13 = r2
                goto L_0x00f5
            L_0x00f4:
                r0 = move-exception
            L_0x00f5:
                r2 = r1
            L_0x00f6:
                r1 = r0
                monitor-exit(r23)     // Catch:{ all -> 0x00f9 }
                throw r1
            L_0x00f9:
                r0 = move-exception
                goto L_0x00f6
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorSwitch.SwitchSubscriber.drain():void");
        }

        /* access modifiers changed from: protected */
        public boolean checkTerminated(boolean localMainDone, boolean localInnerActive, Throwable localError, SpscLinkedArrayQueue<Object> localQueue, Subscriber<? super T> localChild, boolean empty) {
            if (this.delayError) {
                if (localMainDone && !localInnerActive && empty) {
                    if (localError != null) {
                        localChild.onError(localError);
                    } else {
                        localChild.onCompleted();
                    }
                    return true;
                }
            } else if (localError != null) {
                localQueue.clear();
                localChild.onError(localError);
                return true;
            } else if (localMainDone && !localInnerActive && empty) {
                localChild.onCompleted();
                return true;
            }
            return false;
        }
    }

    public static <T> OperatorSwitch<T> instance(boolean delayError2) {
        if (delayError2) {
            return HolderDelayError.INSTANCE;
        }
        return Holder.INSTANCE;
    }

    OperatorSwitch(boolean delayError2) {
        this.delayError = delayError2;
    }

    public Subscriber<? super Observable<? extends T>> call(Subscriber<? super T> child) {
        SwitchSubscriber<T> sws = new SwitchSubscriber<>(child, this.delayError);
        child.add(sws);
        sws.init();
        return sws;
    }
}
