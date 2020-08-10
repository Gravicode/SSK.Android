package p008rx.subjects;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.annotations.Experimental;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action0;
import p008rx.internal.operators.BackpressureUtils;
import p008rx.internal.operators.NotificationLite;
import p008rx.internal.util.atomic.SpscLinkedAtomicQueue;
import p008rx.internal.util.atomic.SpscUnboundedAtomicArrayQueue;
import p008rx.internal.util.unsafe.SpscLinkedQueue;
import p008rx.internal.util.unsafe.SpscUnboundedArrayQueue;
import p008rx.internal.util.unsafe.UnsafeAccess;

@Experimental
/* renamed from: rx.subjects.UnicastSubject */
public final class UnicastSubject<T> extends Subject<T, T> {
    final State<T> state;

    /* renamed from: rx.subjects.UnicastSubject$State */
    static final class State<T> extends AtomicLong implements Producer, Observer<T>, OnSubscribe<T>, Subscription {
        private static final long serialVersionUID = -9044104859202255786L;
        volatile boolean caughtUp;
        volatile boolean done;
        boolean emitting;
        Throwable error;
        boolean missed;
        final Queue<Object> queue;
        final AtomicReference<Subscriber<? super T>> subscriber = new AtomicReference<>();
        final AtomicReference<Action0> terminateOnce;

        public State(int capacityHint, Action0 onTerminated) {
            this.terminateOnce = onTerminated != null ? new AtomicReference<>(onTerminated) : null;
            Queue<Object> q = capacityHint > 1 ? UnsafeAccess.isUnsafeAvailable() ? new SpscUnboundedArrayQueue<>(capacityHint) : new SpscUnboundedAtomicArrayQueue<>(capacityHint) : UnsafeAccess.isUnsafeAvailable() ? new SpscLinkedQueue<>() : new SpscLinkedAtomicQueue<>();
            this.queue = q;
        }

        public void onNext(T t) {
            if (!this.done) {
                if (!this.caughtUp) {
                    boolean stillReplay = false;
                    synchronized (this) {
                        if (!this.caughtUp) {
                            this.queue.offer(NotificationLite.next(t));
                            stillReplay = true;
                        }
                    }
                    if (stillReplay) {
                        replay();
                        return;
                    }
                }
                Subscriber<? super T> s = (Subscriber) this.subscriber.get();
                try {
                    s.onNext(t);
                } catch (Throwable ex) {
                    Exceptions.throwOrReport(ex, (Observer<?>) s, (Object) t);
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0015, code lost:
            if (r0 == false) goto L_0x0021;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0017, code lost:
            replay();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x001a, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onError(java.lang.Throwable r3) {
            /*
                r2 = this;
                boolean r0 = r2.done
                if (r0 != 0) goto L_0x002c
                r2.doTerminate()
                r2.error = r3
                r0 = 1
                r2.done = r0
                boolean r1 = r2.caughtUp
                if (r1 != 0) goto L_0x0021
                monitor-enter(r2)
                boolean r1 = r2.caughtUp     // Catch:{ all -> 0x001b }
                r0 = r0 ^ r1
                monitor-exit(r2)     // Catch:{ all -> 0x001f }
                if (r0 == 0) goto L_0x0021
                r2.replay()
                return
            L_0x001b:
                r1 = move-exception
                r0 = 0
            L_0x001d:
                monitor-exit(r2)     // Catch:{ all -> 0x001f }
                throw r1
            L_0x001f:
                r1 = move-exception
                goto L_0x001d
            L_0x0021:
                java.util.concurrent.atomic.AtomicReference<rx.Subscriber<? super T>> r0 = r2.subscriber
                java.lang.Object r0 = r0.get()
                rx.Subscriber r0 = (p008rx.Subscriber) r0
                r0.onError(r3)
            L_0x002c:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.subjects.UnicastSubject.State.onError(java.lang.Throwable):void");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0013, code lost:
            if (r0 == false) goto L_0x001f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0015, code lost:
            replay();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0018, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onCompleted() {
            /*
                r2 = this;
                boolean r0 = r2.done
                if (r0 != 0) goto L_0x002a
                r2.doTerminate()
                r0 = 1
                r2.done = r0
                boolean r1 = r2.caughtUp
                if (r1 != 0) goto L_0x001f
                monitor-enter(r2)
                boolean r1 = r2.caughtUp     // Catch:{ all -> 0x0019 }
                r0 = r0 ^ r1
                monitor-exit(r2)     // Catch:{ all -> 0x001d }
                if (r0 == 0) goto L_0x001f
                r2.replay()
                return
            L_0x0019:
                r1 = move-exception
                r0 = 0
            L_0x001b:
                monitor-exit(r2)     // Catch:{ all -> 0x001d }
                throw r1
            L_0x001d:
                r1 = move-exception
                goto L_0x001b
            L_0x001f:
                java.util.concurrent.atomic.AtomicReference<rx.Subscriber<? super T>> r0 = r2.subscriber
                java.lang.Object r0 = r0.get()
                rx.Subscriber r0 = (p008rx.Subscriber) r0
                r0.onCompleted()
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.subjects.UnicastSubject.State.onCompleted():void");
        }

        public void request(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("n >= 0 required");
            } else if (n > 0) {
                BackpressureUtils.getAndAddRequest(this, n);
                replay();
            } else if (this.done) {
                replay();
            }
        }

        public void call(Subscriber<? super T> subscriber2) {
            if (this.subscriber.compareAndSet(null, subscriber2)) {
                subscriber2.add(this);
                subscriber2.setProducer(this);
                return;
            }
            subscriber2.onError(new IllegalStateException("Only a single subscriber is allowed"));
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:10:0x000f, code lost:
            r2 = r1.queue;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0011, code lost:
            r4 = (p008rx.Subscriber) r1.subscriber.get();
            r5 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x001a, code lost:
            if (r4 == null) goto L_0x0089;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x001c, code lost:
            r7 = r1.done;
            r8 = r2.isEmpty();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0026, code lost:
            if (checkTerminated(r7, r8, r4) == false) goto L_0x0029;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0028, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0029, code lost:
            r9 = get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0034, code lost:
            if (r9 != Long.MAX_VALUE) goto L_0x0038;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0036, code lost:
            r11 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0038, code lost:
            r11 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0039, code lost:
            r5 = r11;
            r11 = 0;
            r13 = r7;
            r14 = r8;
            r7 = r11;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0041, code lost:
            if (r9 == r11) goto L_0x007f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0043, code lost:
            r13 = r1.done;
            r15 = r2.poll();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0049, code lost:
            if (r15 != null) goto L_0x004e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x004b, code lost:
            r16 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x004e, code lost:
            r16 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0050, code lost:
            r14 = r16;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x0056, code lost:
            if (checkTerminated(r13, r14, r4) == false) goto L_0x0059;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0058, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0059, code lost:
            if (r14 == false) goto L_0x005c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x005c, code lost:
            r6 = p008rx.internal.operators.NotificationLite.getValue(r15);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
            r4.onNext(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x0067, code lost:
            r9 = r9 - 1;
            r7 = r7 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x006f, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x0070, code lost:
            r3 = r0;
            r2.clear();
            p008rx.exceptions.Exceptions.throwIfFatal(r3);
            r4.onError(p008rx.exceptions.OnErrorThrowable.addValueAsLastCause(r3, r6));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x007e, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x007f, code lost:
            if (r5 != false) goto L_0x0089;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x0083, code lost:
            if (r7 == r11) goto L_0x0089;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0085, code lost:
            addAndGet(-r7);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x0089, code lost:
            monitor-enter(r19);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x008c, code lost:
            if (r1.missed != false) goto L_0x009d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x008e, code lost:
            if (r5 == false) goto L_0x0098;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x0094, code lost:
            if (r2.isEmpty() == false) goto L_0x0098;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x0096, code lost:
            r1.caughtUp = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x0098, code lost:
            r1.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:0x009b, code lost:
            monitor-exit(r19);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:52:0x009c, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x009d, code lost:
            r1.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x00a0, code lost:
            monitor-exit(r19);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x00a3, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x00a6, code lost:
            throw r0;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void replay() {
            /*
                r19 = this;
                r1 = r19
                monitor-enter(r19)
                boolean r2 = r1.emitting     // Catch:{ all -> 0x00a7 }
                r3 = 1
                if (r2 == 0) goto L_0x000c
                r1.missed = r3     // Catch:{ all -> 0x00a7 }
                monitor-exit(r19)     // Catch:{ all -> 0x00a7 }
                return
            L_0x000c:
                r1.emitting = r3     // Catch:{ all -> 0x00a7 }
                monitor-exit(r19)     // Catch:{ all -> 0x00a7 }
                java.util.Queue<java.lang.Object> r2 = r1.queue
            L_0x0011:
                java.util.concurrent.atomic.AtomicReference<rx.Subscriber<? super T>> r4 = r1.subscriber
                java.lang.Object r4 = r4.get()
                rx.Subscriber r4 = (p008rx.Subscriber) r4
                r5 = 0
                if (r4 == 0) goto L_0x0089
                boolean r7 = r1.done
                boolean r8 = r2.isEmpty()
                boolean r9 = r1.checkTerminated(r7, r8, r4)
                if (r9 == 0) goto L_0x0029
                return
            L_0x0029:
                long r9 = r19.get()
                r11 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r11 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
                if (r11 != 0) goto L_0x0038
                r11 = 1
                goto L_0x0039
            L_0x0038:
                r11 = 0
            L_0x0039:
                r5 = r11
                r11 = 0
                r13 = r7
                r14 = r8
                r7 = r11
            L_0x003f:
                int r15 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
                if (r15 == 0) goto L_0x007f
                boolean r13 = r1.done
                java.lang.Object r15 = r2.poll()
                if (r15 != 0) goto L_0x004e
                r16 = 1
                goto L_0x0050
            L_0x004e:
                r16 = 0
            L_0x0050:
                r14 = r16
                boolean r16 = r1.checkTerminated(r13, r14, r4)
                if (r16 == 0) goto L_0x0059
                return
            L_0x0059:
                if (r14 == 0) goto L_0x005c
                goto L_0x007f
            L_0x005c:
                java.lang.Object r16 = p008rx.internal.operators.NotificationLite.getValue(r15)
                r17 = r16
                r6 = r17
                r4.onNext(r6)     // Catch:{ Throwable -> 0x006f }
                r16 = 1
                long r9 = r9 - r16
                long r7 = r7 + r16
                goto L_0x003f
            L_0x006f:
                r0 = move-exception
                r3 = r0
                r2.clear()
                p008rx.exceptions.Exceptions.throwIfFatal(r3)
                java.lang.Throwable r11 = p008rx.exceptions.OnErrorThrowable.addValueAsLastCause(r3, r6)
                r4.onError(r11)
                return
            L_0x007f:
                if (r5 != 0) goto L_0x0089
                int r6 = (r7 > r11 ? 1 : (r7 == r11 ? 0 : -1))
                if (r6 == 0) goto L_0x0089
                long r11 = -r7
                r1.addAndGet(r11)
            L_0x0089:
                monitor-enter(r19)
                boolean r6 = r1.missed     // Catch:{ all -> 0x00a3 }
                if (r6 != 0) goto L_0x009d
                if (r5 == 0) goto L_0x0098
                boolean r6 = r2.isEmpty()     // Catch:{ all -> 0x00a3 }
                if (r6 == 0) goto L_0x0098
                r1.caughtUp = r3     // Catch:{ all -> 0x00a3 }
            L_0x0098:
                r3 = 0
                r1.emitting = r3     // Catch:{ all -> 0x00a3 }
                monitor-exit(r19)     // Catch:{ all -> 0x00a3 }
                return
            L_0x009d:
                r6 = 0
                r1.missed = r6     // Catch:{ all -> 0x00a3 }
                monitor-exit(r19)     // Catch:{ all -> 0x00a3 }
                goto L_0x0011
            L_0x00a3:
                r0 = move-exception
                r3 = r0
                monitor-exit(r19)     // Catch:{ all -> 0x00a3 }
                throw r3
            L_0x00a7:
                r0 = move-exception
                r2 = r0
                monitor-exit(r19)     // Catch:{ all -> 0x00a7 }
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.subjects.UnicastSubject.State.replay():void");
        }

        public void unsubscribe() {
            doTerminate();
            this.done = true;
            synchronized (this) {
                if (!this.emitting) {
                    this.emitting = true;
                    this.queue.clear();
                }
            }
        }

        public boolean isUnsubscribed() {
            return this.done;
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean done2, boolean empty, Subscriber<? super T> s) {
            if (s.isUnsubscribed()) {
                this.queue.clear();
                return true;
            }
            if (done2) {
                Throwable e = this.error;
                if (e != null) {
                    this.queue.clear();
                    s.onError(e);
                    return true;
                } else if (empty) {
                    s.onCompleted();
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        public void doTerminate() {
            AtomicReference<Action0> ref = this.terminateOnce;
            if (ref != null) {
                Action0 a = (Action0) ref.get();
                if (a != null && ref.compareAndSet(a, null)) {
                    a.call();
                }
            }
        }
    }

    public static <T> UnicastSubject<T> create() {
        return create(16);
    }

    public static <T> UnicastSubject<T> create(int capacityHint) {
        return new UnicastSubject<>(new State<>(capacityHint, null));
    }

    public static <T> UnicastSubject<T> create(int capacityHint, Action0 onTerminated) {
        return new UnicastSubject<>(new State<>(capacityHint, onTerminated));
    }

    private UnicastSubject(State<T> state2) {
        super(state2);
        this.state = state2;
    }

    public void onNext(T t) {
        this.state.onNext(t);
    }

    public void onError(Throwable e) {
        this.state.onError(e);
    }

    public void onCompleted() {
        this.state.onCompleted();
    }

    public boolean hasObservers() {
        return this.state.subscriber.get() != null;
    }
}
