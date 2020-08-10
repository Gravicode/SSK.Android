package p008rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.internal.util.LinkedArrayList;
import p008rx.subscriptions.SerialSubscription;

/* renamed from: rx.internal.operators.CachedObservable */
public final class CachedObservable<T> extends Observable<T> {
    private final CacheState<T> state;

    /* renamed from: rx.internal.operators.CachedObservable$CacheState */
    static final class CacheState<T> extends LinkedArrayList implements Observer<T> {
        static final ReplayProducer<?>[] EMPTY = new ReplayProducer[0];
        final SerialSubscription connection = new SerialSubscription();
        volatile boolean isConnected;
        volatile ReplayProducer<?>[] producers = EMPTY;
        final Observable<? extends T> source;
        boolean sourceDone;

        public CacheState(Observable<? extends T> source2, int capacityHint) {
            super(capacityHint);
            this.source = source2;
        }

        public void addProducer(ReplayProducer<T> p) {
            synchronized (this.connection) {
                ReplayProducer<?>[] a = this.producers;
                int n = a.length;
                ReplayProducer<?>[] b = new ReplayProducer[(n + 1)];
                System.arraycopy(a, 0, b, 0, n);
                b[n] = p;
                this.producers = b;
            }
        }

        public void removeProducer(ReplayProducer<T> p) {
            synchronized (this.connection) {
                ReplayProducer<?>[] a = this.producers;
                int n = a.length;
                int j = -1;
                int i = 0;
                while (true) {
                    if (i >= n) {
                        break;
                    } else if (a[i].equals(p)) {
                        j = i;
                        break;
                    } else {
                        i++;
                    }
                }
                if (j >= 0) {
                    if (n == 1) {
                        this.producers = EMPTY;
                        return;
                    }
                    ReplayProducer<?>[] b = new ReplayProducer[(n - 1)];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, (n - j) - 1);
                    this.producers = b;
                }
            }
        }

        public void connect() {
            Subscriber<T> subscriber = new Subscriber<T>() {
                public void onNext(T t) {
                    CacheState.this.onNext(t);
                }

                public void onError(Throwable e) {
                    CacheState.this.onError(e);
                }

                public void onCompleted() {
                    CacheState.this.onCompleted();
                }
            };
            this.connection.set(subscriber);
            this.source.unsafeSubscribe(subscriber);
            this.isConnected = true;
        }

        public void onNext(T t) {
            if (!this.sourceDone) {
                add(NotificationLite.next(t));
                dispatch();
            }
        }

        public void onError(Throwable e) {
            if (!this.sourceDone) {
                this.sourceDone = true;
                add(NotificationLite.error(e));
                this.connection.unsubscribe();
                dispatch();
            }
        }

        public void onCompleted() {
            if (!this.sourceDone) {
                this.sourceDone = true;
                add(NotificationLite.completed());
                this.connection.unsubscribe();
                dispatch();
            }
        }

        /* access modifiers changed from: 0000 */
        public void dispatch() {
            for (ReplayProducer replay : this.producers) {
                replay.replay();
            }
        }
    }

    /* renamed from: rx.internal.operators.CachedObservable$CachedSubscribe */
    static final class CachedSubscribe<T> extends AtomicBoolean implements OnSubscribe<T> {
        private static final long serialVersionUID = -2817751667698696782L;
        final CacheState<T> state;

        public CachedSubscribe(CacheState<T> state2) {
            this.state = state2;
        }

        public void call(Subscriber<? super T> t) {
            ReplayProducer<T> rp = new ReplayProducer<>(t, this.state);
            this.state.addProducer(rp);
            t.add(rp);
            t.setProducer(rp);
            if (!get() && compareAndSet(false, true)) {
                this.state.connect();
            }
        }
    }

    /* renamed from: rx.internal.operators.CachedObservable$ReplayProducer */
    static final class ReplayProducer<T> extends AtomicLong implements Producer, Subscription {
        private static final long serialVersionUID = -2557562030197141021L;
        final Subscriber<? super T> child;
        Object[] currentBuffer;
        int currentIndexInBuffer;
        boolean emitting;
        int index;
        boolean missed;
        final CacheState<T> state;

        public ReplayProducer(Subscriber<? super T> child2, CacheState<T> state2) {
            this.child = child2;
            this.state = state2;
        }

        public void request(long n) {
            long r;
            long u;
            do {
                r = get();
                if (r >= 0) {
                    u = r + n;
                    if (u < 0) {
                        u = Long.MAX_VALUE;
                    }
                } else {
                    return;
                }
            } while (!compareAndSet(r, u));
            replay();
        }

        public long produced(long n) {
            return addAndGet(-n);
        }

        public boolean isUnsubscribed() {
            return get() < 0;
        }

        public void unsubscribe() {
            if (get() >= 0 && getAndSet(-1) >= 0) {
                this.state.removeProducer(this);
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:101:0x00c6, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:104:0x00c9, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:105:0x00ca, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:106:0x00cb, code lost:
            r7 = r7 + 1;
            r13 = r13 + 1;
            r14 = r14 - 1;
            r6 = r6 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:107:0x00d9, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:108:0x00da, code lost:
            r8 = r4;
            r4 = r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:10:0x000f, code lost:
            r2 = false;
            r4 = r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:110:?, code lost:
            p008rx.exceptions.Exceptions.throwIfFatal(r4);
            unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:114:0x00ef, code lost:
            r5.onError(p008rx.exceptions.OnErrorThrowable.addValueAsLastCause(r4, p008rx.internal.operators.NotificationLite.getValue(r3)));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:115:0x00fa, code lost:
            if (1 == 0) goto L_0x00fc;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:116:0x00fc, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:118:?, code lost:
            r1.emitting = r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:121:0x0101, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:124:0x0104, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:125:0x0105, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:126:0x0106, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:127:0x0107, code lost:
            r3 = r0;
            r4 = r8;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
            r5 = r1.child;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:130:0x010e, code lost:
            if (r5.isUnsubscribed() == false) goto L_0x011d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:132:0x0111, code lost:
            if (1 != 0) goto L_0x011c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:133:0x0113, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:135:?, code lost:
            r1.emitting = r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:136:0x0116, code lost:
            monitor-exit(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:138:0x0118, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0013, code lost:
            r6 = get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:141:0x011b, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:142:0x011c, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:144:?, code lost:
            r1.index = r13;
            r1.currentIndexInBuffer = r7;
            r1.currentBuffer = r11;
            produced((long) r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:145:0x0128, code lost:
            r14 = r6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:146:0x0129, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:149:0x012c, code lost:
            if (r1.missed != false) goto L_0x0141;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:150:0x012e, code lost:
            r1.emitting = r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:153:?, code lost:
            monitor-exit(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:154:0x0132, code lost:
            if (1 != 0) goto L_0x013d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:155:0x0134, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:157:?, code lost:
            r1.emitting = r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:158:0x0137, code lost:
            monitor-exit(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x001b, code lost:
            if (r6 >= 0) goto L_0x002a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:160:0x0139, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:163:0x013c, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:164:0x013d, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:165:0x013e, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:166:0x013f, code lost:
            r4 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:168:?, code lost:
            r1.missed = r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:169:0x0143, code lost:
            monitor-exit(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:170:0x0144, code lost:
            r3 = 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:171:0x0148, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:172:0x0149, code lost:
            r3 = r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:173:0x014a, code lost:
            monitor-exit(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:175:?, code lost:
            throw r3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:176:0x014c, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:177:0x014d, code lost:
            r3 = r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:178:0x014e, code lost:
            if (r4 == false) goto L_0x0150;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:179:0x0150, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x001e, code lost:
            if (1 != 0) goto L_0x0029;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:181:?, code lost:
            r1.emitting = r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:184:0x0155, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:187:0x0158, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:188:0x0159, code lost:
            throw r3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0020, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
            r1.emitting = r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0023, code lost:
            monitor-exit(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0025, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0028, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0029, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
            r10 = r1.state.size();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0030, code lost:
            if (r10 == 0) goto L_0x0128;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0032, code lost:
            r11 = r1.currentBuffer;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0034, code lost:
            if (r11 != null) goto L_0x003f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x0036, code lost:
            r11 = r1.state.head();
            r1.currentBuffer = r11;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x003f, code lost:
            r12 = r11.length - r3;
            r13 = r1.index;
            r14 = r1.currentIndexInBuffer;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x0047, code lost:
            if (r6 != 0) goto L_0x0083;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x0049, code lost:
            r8 = r11[r14];
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x004f, code lost:
            if (p008rx.internal.operators.NotificationLite.isCompleted(r8) == false) goto L_0x0064;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x0051, code lost:
            r5.onCompleted();
            unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x0058, code lost:
            if (1 != 0) goto L_0x0063;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x005a, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
            r1.emitting = r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x005d, code lost:
            monitor-exit(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x005f, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x0062, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x0063, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:52:0x0068, code lost:
            if (p008rx.internal.operators.NotificationLite.isError(r8) == false) goto L_0x0128;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x006a, code lost:
            r5.onError(p008rx.internal.operators.NotificationLite.getError(r8));
            unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x0075, code lost:
            if (1 != 0) goto L_0x0080;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x0077, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:57:?, code lost:
            r1.emitting = r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:58:0x007a, code lost:
            monitor-exit(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:60:0x007c, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:0x007f, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:64:0x0080, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:66:0x0085, code lost:
            if (r6 <= 0) goto L_0x0128;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:67:0x0087, code lost:
            r7 = r14;
            r14 = r6;
            r6 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:0x008d, code lost:
            if (r13 >= r10) goto L_0x010a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:70:0x0091, code lost:
            if (r14 <= 0) goto L_0x010a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:73:0x0097, code lost:
            if (r5.isUnsubscribed() == false) goto L_0x00a6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:75:0x009a, code lost:
            if (1 != 0) goto L_0x00a5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:76:0x009c, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:78:?, code lost:
            r1.emitting = r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:79:0x009f, code lost:
            monitor-exit(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:81:0x00a1, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:84:0x00a4, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:85:0x00a5, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:86:0x00a6, code lost:
            if (r7 != r12) goto L_0x00af;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:88:?, code lost:
            r11 = (java.lang.Object[]) r11[r12];
            r7 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:90:0x00b1, code lost:
            r3 = r11[r7];
         */
        /* JADX WARNING: Code restructure failed: missing block: B:93:0x00b9, code lost:
            if (p008rx.internal.operators.NotificationLite.accept(r5, r3) == false) goto L_0x00cb;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:94:0x00bb, code lost:
            unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:95:0x00bf, code lost:
            if (1 != 0) goto L_0x00ca;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:96:0x00c1, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:98:?, code lost:
            r1.emitting = r2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:99:0x00c4, code lost:
            monitor-exit(r20);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void replay() {
            /*
                r20 = this;
                r1 = r20
                monitor-enter(r20)
                boolean r2 = r1.emitting     // Catch:{ all -> 0x015a }
                r3 = 1
                if (r2 == 0) goto L_0x000c
                r1.missed = r3     // Catch:{ all -> 0x015a }
                monitor-exit(r20)     // Catch:{ all -> 0x015a }
                return
            L_0x000c:
                r1.emitting = r3     // Catch:{ all -> 0x015a }
                monitor-exit(r20)     // Catch:{ all -> 0x015a }
                r2 = 0
                r4 = r2
                rx.Subscriber<? super T> r5 = r1.child     // Catch:{ all -> 0x014c }
            L_0x0013:
                long r6 = r20.get()     // Catch:{ all -> 0x014c }
                r8 = 0
                int r10 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r10 >= 0) goto L_0x002a
                r3 = 1
                if (r3 != 0) goto L_0x0029
                monitor-enter(r20)
                r1.emitting = r2     // Catch:{ all -> 0x0025 }
                monitor-exit(r20)     // Catch:{ all -> 0x0025 }
                goto L_0x0029
            L_0x0025:
                r0 = move-exception
                r2 = r0
                monitor-exit(r20)     // Catch:{ all -> 0x0025 }
                throw r2
            L_0x0029:
                return
            L_0x002a:
                rx.internal.operators.CachedObservable$CacheState<T> r10 = r1.state     // Catch:{ all -> 0x014c }
                int r10 = r10.size()     // Catch:{ all -> 0x014c }
                if (r10 == 0) goto L_0x0128
                java.lang.Object[] r11 = r1.currentBuffer     // Catch:{ all -> 0x014c }
                if (r11 != 0) goto L_0x003f
                rx.internal.operators.CachedObservable$CacheState<T> r12 = r1.state     // Catch:{ all -> 0x014c }
                java.lang.Object[] r12 = r12.head()     // Catch:{ all -> 0x014c }
                r11 = r12
                r1.currentBuffer = r11     // Catch:{ all -> 0x014c }
            L_0x003f:
                int r12 = r11.length     // Catch:{ all -> 0x014c }
                int r12 = r12 - r3
                int r13 = r1.index     // Catch:{ all -> 0x014c }
                int r14 = r1.currentIndexInBuffer     // Catch:{ all -> 0x014c }
                int r15 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r15 != 0) goto L_0x0083
                r8 = r11[r14]     // Catch:{ all -> 0x014c }
                boolean r9 = p008rx.internal.operators.NotificationLite.isCompleted(r8)     // Catch:{ all -> 0x014c }
                if (r9 == 0) goto L_0x0064
                r5.onCompleted()     // Catch:{ all -> 0x014c }
                r4 = 1
                r20.unsubscribe()     // Catch:{ all -> 0x014c }
                if (r4 != 0) goto L_0x0063
                monitor-enter(r20)
                r1.emitting = r2     // Catch:{ all -> 0x005f }
                monitor-exit(r20)     // Catch:{ all -> 0x005f }
                goto L_0x0063
            L_0x005f:
                r0 = move-exception
                r2 = r0
                monitor-exit(r20)     // Catch:{ all -> 0x005f }
                throw r2
            L_0x0063:
                return
            L_0x0064:
                boolean r9 = p008rx.internal.operators.NotificationLite.isError(r8)     // Catch:{ all -> 0x014c }
                if (r9 == 0) goto L_0x0081
                java.lang.Throwable r3 = p008rx.internal.operators.NotificationLite.getError(r8)     // Catch:{ all -> 0x014c }
                r5.onError(r3)     // Catch:{ all -> 0x014c }
                r4 = 1
                r20.unsubscribe()     // Catch:{ all -> 0x014c }
                if (r4 != 0) goto L_0x0080
                monitor-enter(r20)
                r1.emitting = r2     // Catch:{ all -> 0x007c }
                monitor-exit(r20)     // Catch:{ all -> 0x007c }
                goto L_0x0080
            L_0x007c:
                r0 = move-exception
                r2 = r0
                monitor-exit(r20)     // Catch:{ all -> 0x007c }
                throw r2
            L_0x0080:
                return
            L_0x0081:
                goto L_0x0128
            L_0x0083:
                int r15 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r15 <= 0) goto L_0x0128
                r18 = r6
                r7 = r14
                r14 = r18
                r6 = 0
            L_0x008d:
                if (r13 >= r10) goto L_0x010a
                int r16 = (r14 > r8 ? 1 : (r14 == r8 ? 0 : -1))
                if (r16 <= 0) goto L_0x010a
                boolean r16 = r5.isUnsubscribed()     // Catch:{ all -> 0x014c }
                if (r16 == 0) goto L_0x00a6
                r3 = 1
                if (r3 != 0) goto L_0x00a5
                monitor-enter(r20)
                r1.emitting = r2     // Catch:{ all -> 0x00a1 }
                monitor-exit(r20)     // Catch:{ all -> 0x00a1 }
                goto L_0x00a5
            L_0x00a1:
                r0 = move-exception
                r2 = r0
                monitor-exit(r20)     // Catch:{ all -> 0x00a1 }
                throw r2
            L_0x00a5:
                return
            L_0x00a6:
                if (r7 != r12) goto L_0x00af
                r16 = r11[r12]     // Catch:{ all -> 0x014c }
                java.lang.Object[] r16 = (java.lang.Object[]) r16     // Catch:{ all -> 0x014c }
                r11 = r16
                r7 = 0
            L_0x00af:
                r16 = r11[r7]     // Catch:{ all -> 0x014c }
                r17 = r16
                r3 = r17
                boolean r16 = p008rx.internal.operators.NotificationLite.accept(r5, r3)     // Catch:{ Throwable -> 0x00d9 }
                if (r16 == 0) goto L_0x00cb
                r4 = 1
                r20.unsubscribe()     // Catch:{ Throwable -> 0x00d9 }
                if (r4 != 0) goto L_0x00ca
                monitor-enter(r20)
                r1.emitting = r2     // Catch:{ all -> 0x00c6 }
                monitor-exit(r20)     // Catch:{ all -> 0x00c6 }
                goto L_0x00ca
            L_0x00c6:
                r0 = move-exception
                r2 = r0
                monitor-exit(r20)     // Catch:{ all -> 0x00c6 }
                throw r2
            L_0x00ca:
                return
            L_0x00cb:
                int r7 = r7 + 1
                int r13 = r13 + 1
                r16 = 1
                long r14 = r14 - r16
                int r6 = r6 + 1
                r3 = 1
                goto L_0x008d
            L_0x00d9:
                r0 = move-exception
                r8 = r4
                r4 = r0
                p008rx.exceptions.Exceptions.throwIfFatal(r4)     // Catch:{ all -> 0x0106 }
                r8 = 1
                r20.unsubscribe()     // Catch:{ all -> 0x0106 }
                boolean r9 = p008rx.internal.operators.NotificationLite.isError(r3)     // Catch:{ all -> 0x0106 }
                if (r9 != 0) goto L_0x00fa
                boolean r9 = p008rx.internal.operators.NotificationLite.isCompleted(r3)     // Catch:{ all -> 0x0106 }
                if (r9 != 0) goto L_0x00fa
                java.lang.Object r9 = p008rx.internal.operators.NotificationLite.getValue(r3)     // Catch:{ all -> 0x0106 }
                java.lang.Throwable r9 = p008rx.exceptions.OnErrorThrowable.addValueAsLastCause(r4, r9)     // Catch:{ all -> 0x0106 }
                r5.onError(r9)     // Catch:{ all -> 0x0106 }
            L_0x00fa:
                if (r8 != 0) goto L_0x0105
                monitor-enter(r20)
                r1.emitting = r2     // Catch:{ all -> 0x0101 }
                monitor-exit(r20)     // Catch:{ all -> 0x0101 }
                goto L_0x0105
            L_0x0101:
                r0 = move-exception
                r2 = r0
                monitor-exit(r20)     // Catch:{ all -> 0x0101 }
                throw r2
            L_0x0105:
                return
            L_0x0106:
                r0 = move-exception
                r3 = r0
                r4 = r8
                goto L_0x014e
            L_0x010a:
                boolean r3 = r5.isUnsubscribed()     // Catch:{ all -> 0x014c }
                if (r3 == 0) goto L_0x011d
                r3 = 1
                if (r3 != 0) goto L_0x011c
                monitor-enter(r20)
                r1.emitting = r2     // Catch:{ all -> 0x0118 }
                monitor-exit(r20)     // Catch:{ all -> 0x0118 }
                goto L_0x011c
            L_0x0118:
                r0 = move-exception
                r2 = r0
                monitor-exit(r20)     // Catch:{ all -> 0x0118 }
                throw r2
            L_0x011c:
                return
            L_0x011d:
                r1.index = r13     // Catch:{ all -> 0x014c }
                r1.currentIndexInBuffer = r7     // Catch:{ all -> 0x014c }
                r1.currentBuffer = r11     // Catch:{ all -> 0x014c }
                long r8 = (long) r6     // Catch:{ all -> 0x014c }
                r1.produced(r8)     // Catch:{ all -> 0x014c }
                goto L_0x0129
            L_0x0128:
                r14 = r6
            L_0x0129:
                monitor-enter(r20)     // Catch:{ all -> 0x014c }
                boolean r3 = r1.missed     // Catch:{ all -> 0x0148 }
                if (r3 != 0) goto L_0x0141
                r1.emitting = r2     // Catch:{ all -> 0x0148 }
                r3 = 1
                monitor-exit(r20)     // Catch:{ all -> 0x013e }
                if (r3 != 0) goto L_0x013d
                monitor-enter(r20)
                r1.emitting = r2     // Catch:{ all -> 0x0139 }
                monitor-exit(r20)     // Catch:{ all -> 0x0139 }
                goto L_0x013d
            L_0x0139:
                r0 = move-exception
                r2 = r0
                monitor-exit(r20)     // Catch:{ all -> 0x0139 }
                throw r2
            L_0x013d:
                return
            L_0x013e:
                r0 = move-exception
                r4 = r3
                goto L_0x0149
            L_0x0141:
                r1.missed = r2     // Catch:{ all -> 0x0148 }
                monitor-exit(r20)     // Catch:{ all -> 0x0148 }
                r3 = 1
                goto L_0x0013
            L_0x0148:
                r0 = move-exception
            L_0x0149:
                r3 = r0
                monitor-exit(r20)     // Catch:{ all -> 0x0148 }
                throw r3     // Catch:{ all -> 0x014c }
            L_0x014c:
                r0 = move-exception
                r3 = r0
            L_0x014e:
                if (r4 != 0) goto L_0x0159
                monitor-enter(r20)
                r1.emitting = r2     // Catch:{ all -> 0x0155 }
                monitor-exit(r20)     // Catch:{ all -> 0x0155 }
                goto L_0x0159
            L_0x0155:
                r0 = move-exception
                r2 = r0
                monitor-exit(r20)     // Catch:{ all -> 0x0155 }
                throw r2
            L_0x0159:
                throw r3
            L_0x015a:
                r0 = move-exception
                r2 = r0
                monitor-exit(r20)     // Catch:{ all -> 0x015a }
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.CachedObservable.ReplayProducer.replay():void");
        }
    }

    public static <T> CachedObservable<T> from(Observable<? extends T> source) {
        return from(source, 16);
    }

    public static <T> CachedObservable<T> from(Observable<? extends T> source, int capacityHint) {
        if (capacityHint < 1) {
            throw new IllegalArgumentException("capacityHint > 0 required");
        }
        CacheState<T> state2 = new CacheState<>(source, capacityHint);
        return new CachedObservable<>(new CachedSubscribe<>(state2), state2);
    }

    private CachedObservable(OnSubscribe<T> onSubscribe, CacheState<T> state2) {
        super(onSubscribe);
        this.state = state2;
    }

    /* access modifiers changed from: 0000 */
    public boolean isConnected() {
        return this.state.isConnected;
    }

    /* access modifiers changed from: 0000 */
    public boolean hasObservers() {
        return this.state.producers.length != 0;
    }
}
