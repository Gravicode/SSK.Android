package p008rx.internal.producers;

import java.util.List;
import p008rx.Observer;
import p008rx.Producer;
import p008rx.Subscriber;

/* renamed from: rx.internal.producers.ProducerObserverArbiter */
public final class ProducerObserverArbiter<T> implements Producer, Observer<T> {
    static final Producer NULL_PRODUCER = new Producer() {
        public void request(long n) {
        }
    };
    final Subscriber<? super T> child;
    Producer currentProducer;
    boolean emitting;
    volatile boolean hasError;
    Producer missedProducer;
    long missedRequested;
    Object missedTerminal;
    List<T> queue;
    long requested;

    public ProducerObserverArbiter(Subscriber<? super T> child2) {
        this.child = child2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        r6.child.onNext(r7);
        r2 = r6.requested;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002b, code lost:
        if (r2 == Long.MAX_VALUE) goto L_0x0033;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002d, code lost:
        r6.requested = r2 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0033, code lost:
        emitLoop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0037, code lost:
        if (1 != 0) goto L_0x0041;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0039, code lost:
        monitor-enter(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r6.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x003c, code lost:
        monitor-exit(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0041, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0042, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0043, code lost:
        if (0 == 0) goto L_0x0045;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0045, code lost:
        monitor-enter(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
        r6.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x004d, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onNext(T r7) {
        /*
            r6 = this;
            monitor-enter(r6)
            boolean r0 = r6.emitting     // Catch:{ all -> 0x004e }
            if (r0 == 0) goto L_0x0017
            java.util.List<T> r0 = r6.queue     // Catch:{ all -> 0x004e }
            if (r0 != 0) goto L_0x0012
            java.util.ArrayList r1 = new java.util.ArrayList     // Catch:{ all -> 0x004e }
            r2 = 4
            r1.<init>(r2)     // Catch:{ all -> 0x004e }
            r0 = r1
            r6.queue = r0     // Catch:{ all -> 0x004e }
        L_0x0012:
            r0.add(r7)     // Catch:{ all -> 0x004e }
            monitor-exit(r6)     // Catch:{ all -> 0x004e }
            return
        L_0x0017:
            r0 = 1
            r6.emitting = r0     // Catch:{ all -> 0x004e }
            monitor-exit(r6)     // Catch:{ all -> 0x004e }
            r0 = 0
            r1 = r0
            rx.Subscriber<? super T> r2 = r6.child     // Catch:{ all -> 0x0042 }
            r2.onNext(r7)     // Catch:{ all -> 0x0042 }
            long r2 = r6.requested     // Catch:{ all -> 0x0042 }
            r4 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r4 == 0) goto L_0x0033
            r4 = 1
            long r4 = r2 - r4
            r6.requested = r4     // Catch:{ all -> 0x0042 }
        L_0x0033:
            r6.emitLoop()     // Catch:{ all -> 0x0042 }
            r1 = 1
            if (r1 != 0) goto L_0x0041
            monitor-enter(r6)
            r6.emitting = r0     // Catch:{ all -> 0x003e }
            monitor-exit(r6)     // Catch:{ all -> 0x003e }
            goto L_0x0041
        L_0x003e:
            r0 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x003e }
            throw r0
        L_0x0041:
            return
        L_0x0042:
            r2 = move-exception
            if (r1 != 0) goto L_0x004d
            monitor-enter(r6)
            r6.emitting = r0     // Catch:{ all -> 0x004a }
            monitor-exit(r6)     // Catch:{ all -> 0x004a }
            goto L_0x004d
        L_0x004a:
            r0 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x004a }
            throw r0
        L_0x004d:
            throw r2
        L_0x004e:
            r0 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x004e }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.producers.ProducerObserverArbiter.onNext(java.lang.Object):void");
    }

    public void onError(Throwable e) {
        boolean emit;
        synchronized (this) {
            if (this.emitting) {
                this.missedTerminal = e;
                emit = false;
            } else {
                this.emitting = true;
                emit = true;
            }
        }
        if (emit) {
            this.child.onError(e);
        } else {
            this.hasError = true;
        }
    }

    public void onCompleted() {
        synchronized (this) {
            if (this.emitting) {
                this.missedTerminal = Boolean.valueOf(true);
                return;
            }
            this.emitting = true;
            this.child.onCompleted();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0024, code lost:
        r2 = r9.currentProducer;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r7 = r9.requested + r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x002f, code lost:
        if (r7 >= 0) goto L_0x0036;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0031, code lost:
        r7 = Long.MAX_VALUE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0036, code lost:
        r9.requested = r7;
        emitLoop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x003c, code lost:
        if (1 != 0) goto L_0x0046;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x003e, code lost:
        monitor-enter(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        r9.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0041, code lost:
        monitor-exit(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0046, code lost:
        if (r2 == null) goto L_0x004b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0048, code lost:
        r2.request(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x004b, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x004c, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x004d, code lost:
        if (0 == 0) goto L_0x004f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x004f, code lost:
        monitor-enter(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
        r9.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0057, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void request(long r10) {
        /*
            r9 = this;
            r0 = 0
            int r2 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1))
            if (r2 >= 0) goto L_0x000e
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "n >= 0 required"
            r0.<init>(r1)
            throw r0
        L_0x000e:
            int r2 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1))
            if (r2 != 0) goto L_0x0013
            return
        L_0x0013:
            monitor-enter(r9)
            boolean r2 = r9.emitting     // Catch:{ all -> 0x0058 }
            if (r2 == 0) goto L_0x0020
            long r0 = r9.missedRequested     // Catch:{ all -> 0x0058 }
            r2 = 0
            long r0 = r0 + r10
            r9.missedRequested = r0     // Catch:{ all -> 0x0058 }
            monitor-exit(r9)     // Catch:{ all -> 0x0058 }
            return
        L_0x0020:
            r2 = 1
            r9.emitting = r2     // Catch:{ all -> 0x0058 }
            monitor-exit(r9)     // Catch:{ all -> 0x0058 }
            rx.Producer r2 = r9.currentProducer
            r3 = 0
            r4 = r3
            long r5 = r9.requested     // Catch:{ all -> 0x004c }
            r7 = 0
            long r7 = r5 + r10
            int r0 = (r7 > r0 ? 1 : (r7 == r0 ? 0 : -1))
            if (r0 >= 0) goto L_0x0036
            r7 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
        L_0x0036:
            r9.requested = r7     // Catch:{ all -> 0x004c }
            r9.emitLoop()     // Catch:{ all -> 0x004c }
            r0 = 1
            if (r0 != 0) goto L_0x0046
            monitor-enter(r9)
            r9.emitting = r3     // Catch:{ all -> 0x0043 }
            monitor-exit(r9)     // Catch:{ all -> 0x0043 }
            goto L_0x0046
        L_0x0043:
            r1 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x0043 }
            throw r1
        L_0x0046:
            if (r2 == 0) goto L_0x004b
            r2.request(r10)
        L_0x004b:
            return
        L_0x004c:
            r0 = move-exception
            if (r4 != 0) goto L_0x0057
            monitor-enter(r9)
            r9.emitting = r3     // Catch:{ all -> 0x0054 }
            monitor-exit(r9)     // Catch:{ all -> 0x0054 }
            goto L_0x0057
        L_0x0054:
            r0 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x0054 }
            throw r0
        L_0x0057:
            throw r0
        L_0x0058:
            r0 = move-exception
            monitor-exit(r9)     // Catch:{ all -> 0x0058 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.producers.ProducerObserverArbiter.request(long):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0013, code lost:
        r5.currentProducer = r6;
        r1 = r5.requested;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        emitLoop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x001d, code lost:
        if (1 != 0) goto L_0x0027;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x001f, code lost:
        monitor-enter(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        r5.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0022, code lost:
        monitor-exit(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0027, code lost:
        if (r6 == null) goto L_0x0032;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x002d, code lost:
        if (r1 == 0) goto L_0x0032;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x002f, code lost:
        r6.request(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0032, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0033, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0034, code lost:
        if (0 == 0) goto L_0x0036;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0036, code lost:
        monitor-enter(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
        r5.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x003e, code lost:
        throw r4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setProducer(p008rx.Producer r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            boolean r0 = r5.emitting     // Catch:{ all -> 0x003f }
            if (r0 == 0) goto L_0x000f
            if (r6 == 0) goto L_0x0009
            r0 = r6
            goto L_0x000b
        L_0x0009:
            rx.Producer r0 = NULL_PRODUCER     // Catch:{ all -> 0x003f }
        L_0x000b:
            r5.missedProducer = r0     // Catch:{ all -> 0x003f }
            monitor-exit(r5)     // Catch:{ all -> 0x003f }
            return
        L_0x000f:
            r0 = 1
            r5.emitting = r0     // Catch:{ all -> 0x003f }
            monitor-exit(r5)     // Catch:{ all -> 0x003f }
            r0 = 0
            r5.currentProducer = r6
            long r1 = r5.requested
            r3 = 0
            r5.emitLoop()     // Catch:{ all -> 0x0033 }
            r0 = 1
            if (r0 != 0) goto L_0x0027
            monitor-enter(r5)
            r5.emitting = r3     // Catch:{ all -> 0x0024 }
            monitor-exit(r5)     // Catch:{ all -> 0x0024 }
            goto L_0x0027
        L_0x0024:
            r3 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0024 }
            throw r3
        L_0x0027:
            if (r6 == 0) goto L_0x0032
            r3 = 0
            int r3 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r3 == 0) goto L_0x0032
            r6.request(r1)
        L_0x0032:
            return
        L_0x0033:
            r4 = move-exception
            if (r0 != 0) goto L_0x003e
            monitor-enter(r5)
            r5.emitting = r3     // Catch:{ all -> 0x003b }
            monitor-exit(r5)     // Catch:{ all -> 0x003b }
            goto L_0x003e
        L_0x003b:
            r3 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x003b }
            throw r3
        L_0x003e:
            throw r4
        L_0x003f:
            r0 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x003f }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.producers.ProducerObserverArbiter.setProducer(rx.Producer):void");
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0040, code lost:
        if (r14 == false) goto L_0x004e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0046, code lost:
        if (r8 == 0) goto L_0x004d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0048, code lost:
        if (r3 == null) goto L_0x004d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x004a, code lost:
        r3.request(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x004d, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x004e, code lost:
        if (r11 == null) goto L_0x0058;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0054, code lost:
        if (r11.isEmpty() == false) goto L_0x0059;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0058, code lost:
        r5 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0059, code lost:
        if (r10 == null) goto L_0x006c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x005d, code lost:
        if (r10 == java.lang.Boolean.TRUE) goto L_0x0066;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x005f, code lost:
        r2.onError((java.lang.Throwable) r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0065, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0066, code lost:
        if (r5 == false) goto L_0x006c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0068, code lost:
        r2.onCompleted();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x006b, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x006c, code lost:
        r6 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x006e, code lost:
        if (r11 == null) goto L_0x00b5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0070, code lost:
        r18 = r11.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0074, code lost:
        r15 = r18;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x007c, code lost:
        if (r15.hasNext() == false) goto L_0x00a8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x007e, code lost:
        r20 = r3;
        r3 = r15.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0088, code lost:
        if (r2.isUnsubscribed() == false) goto L_0x008b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x008a, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x008b, code lost:
        r21 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x008f, code lost:
        if (r1.hasError == false) goto L_0x0096;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0091, code lost:
        r3 = r20;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:?, code lost:
        r2.onNext(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0099, code lost:
        r18 = r15;
        r3 = r20;
        r5 = r21;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00a2, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00a3, code lost:
        p008rx.exceptions.Exceptions.throwOrReport(r0, (p008rx.Observer<?>) r2, (java.lang.Object) r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00a7, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00a8, code lost:
        r20 = r3;
        r21 = r5;
        r22 = r2;
        r6 = 0 + ((long) r11.size());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00b5, code lost:
        r22 = r2;
        r20 = r3;
        r21 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00bb, code lost:
        r2 = r1.requested;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00c4, code lost:
        if (r2 == Long.MAX_VALUE) goto L_0x00f3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00ca, code lost:
        if (r12 == 0) goto L_0x00d9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00cc, code lost:
        r23 = r2 + r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00d0, code lost:
        if (r23 >= 0) goto L_0x00d7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00d2, code lost:
        r23 = Long.MAX_VALUE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00d7, code lost:
        r2 = r23;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00db, code lost:
        if (r6 == 0) goto L_0x00f1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00df, code lost:
        if (r2 == Long.MAX_VALUE) goto L_0x00f1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x00e1, code lost:
        r18 = r2 - r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x00e5, code lost:
        if (r18 >= 0) goto L_0x00ef;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x00ee, code lost:
        throw new java.lang.IllegalStateException("More produced than requested");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x00ef, code lost:
        r2 = r18;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x00f1, code lost:
        r1.requested = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x00f3, code lost:
        if (r4 == null) goto L_0x010f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x00f7, code lost:
        if (r4 != NULL_PRODUCER) goto L_0x00fd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x00f9, code lost:
        r1.currentProducer = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x00fd, code lost:
        r1.currentProducer = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0104, code lost:
        if (r2 == 0) goto L_0x0121;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x0106, code lost:
        r8 = p008rx.internal.operators.BackpressureUtils.addCap(r8, r2);
        r3 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x010f, code lost:
        r15 = r1.currentProducer;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x0112, code lost:
        if (r15 == null) goto L_0x0121;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x0118, code lost:
        if (r12 == 0) goto L_0x0123;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x011a, code lost:
        r8 = p008rx.internal.operators.BackpressureUtils.addCap(r8, r12);
        r3 = r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x0123, code lost:
        r3 = r20;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x0125, code lost:
        r2 = r22;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void emitLoop() {
        /*
            r25 = this;
            r1 = r25
            rx.Subscriber<? super T> r2 = r1.child
            r3 = 0
            r8 = r3
            r3 = 0
            r4 = 0
            r10 = 0
            r11 = 0
            r12 = 0
        L_0x000d:
            r14 = 0
            monitor-enter(r25)
            long r5 = r1.missedRequested     // Catch:{ all -> 0x0129 }
            r12 = r5
            rx.Producer r5 = r1.missedProducer     // Catch:{ all -> 0x0129 }
            r4 = r5
            java.lang.Object r5 = r1.missedTerminal     // Catch:{ all -> 0x0129 }
            r10 = r5
            java.util.List<T> r5 = r1.queue     // Catch:{ all -> 0x0129 }
            r11 = r5
            r5 = 0
            int r7 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1))
            r5 = 0
            if (r7 != 0) goto L_0x0034
            if (r4 != 0) goto L_0x0034
            if (r11 != 0) goto L_0x0034
            if (r10 != 0) goto L_0x0034
            r1.emitting = r5     // Catch:{ all -> 0x002d }
            r6 = 1
            r14 = r6
            goto L_0x003f
        L_0x002d:
            r0 = move-exception
            r22 = r2
            r20 = r3
            goto L_0x0132
        L_0x0034:
            r6 = 0
            r1.missedRequested = r6     // Catch:{ all -> 0x0129 }
            r6 = 0
            r1.missedProducer = r6     // Catch:{ all -> 0x0129 }
            r1.queue = r6     // Catch:{ all -> 0x0129 }
            r1.missedTerminal = r6     // Catch:{ all -> 0x0129 }
        L_0x003f:
            monitor-exit(r25)     // Catch:{ all -> 0x0129 }
            if (r14 == 0) goto L_0x004e
            r5 = 0
            int r5 = (r8 > r5 ? 1 : (r8 == r5 ? 0 : -1))
            if (r5 == 0) goto L_0x004d
            if (r3 == 0) goto L_0x004d
            r3.request(r8)
        L_0x004d:
            return
        L_0x004e:
            if (r11 == 0) goto L_0x0058
            boolean r6 = r11.isEmpty()
            if (r6 == 0) goto L_0x0057
            goto L_0x0058
        L_0x0057:
            goto L_0x0059
        L_0x0058:
            r5 = 1
        L_0x0059:
            if (r10 == 0) goto L_0x006c
            java.lang.Boolean r6 = java.lang.Boolean.TRUE
            if (r10 == r6) goto L_0x0066
            r6 = r10
            java.lang.Throwable r6 = (java.lang.Throwable) r6
            r2.onError(r6)
            return
        L_0x0066:
            if (r5 == 0) goto L_0x006c
            r2.onCompleted()
            return
        L_0x006c:
            r6 = 0
            if (r11 == 0) goto L_0x00b5
            java.util.Iterator r18 = r11.iterator()
        L_0x0074:
            r19 = r18
            r15 = r19
            boolean r18 = r15.hasNext()
            if (r18 == 0) goto L_0x00a8
            r20 = r3
            java.lang.Object r3 = r15.next()
            boolean r18 = r2.isUnsubscribed()
            if (r18 == 0) goto L_0x008b
            return
        L_0x008b:
            r21 = r5
            boolean r5 = r1.hasError
            if (r5 == 0) goto L_0x0096
            r3 = r20
            goto L_0x000d
        L_0x0096:
            r2.onNext(r3)     // Catch:{ Throwable -> 0x00a2 }
            r18 = r15
            r3 = r20
            r5 = r21
            goto L_0x0074
        L_0x00a2:
            r0 = move-exception
            r5 = r0
            p008rx.exceptions.Exceptions.throwOrReport(r5, r2, r3)
            return
        L_0x00a8:
            r20 = r3
            r21 = r5
            int r3 = r11.size()
            r22 = r2
            long r2 = (long) r3
            long r6 = r6 + r2
            goto L_0x00bb
        L_0x00b5:
            r22 = r2
            r20 = r3
            r21 = r5
        L_0x00bb:
            long r2 = r1.requested
            r18 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            int r5 = (r2 > r18 ? 1 : (r2 == r18 ? 0 : -1))
            if (r5 == 0) goto L_0x00f3
            r15 = 0
            int r5 = (r12 > r15 ? 1 : (r12 == r15 ? 0 : -1))
            if (r5 == 0) goto L_0x00d9
            long r23 = r2 + r12
            int r5 = (r23 > r15 ? 1 : (r23 == r15 ? 0 : -1))
            if (r5 >= 0) goto L_0x00d7
            r23 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
        L_0x00d7:
            r2 = r23
        L_0x00d9:
            int r5 = (r6 > r15 ? 1 : (r6 == r15 ? 0 : -1))
            if (r5 == 0) goto L_0x00f1
            int r5 = (r2 > r18 ? 1 : (r2 == r18 ? 0 : -1))
            if (r5 == 0) goto L_0x00f1
            long r18 = r2 - r6
            int r5 = (r18 > r15 ? 1 : (r18 == r15 ? 0 : -1))
            if (r5 >= 0) goto L_0x00ef
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r15 = "More produced than requested"
            r5.<init>(r15)
            throw r5
        L_0x00ef:
            r2 = r18
        L_0x00f1:
            r1.requested = r2
        L_0x00f3:
            if (r4 == 0) goto L_0x010f
            rx.Producer r5 = NULL_PRODUCER
            if (r4 != r5) goto L_0x00fd
            r5 = 0
            r1.currentProducer = r5
            goto L_0x0121
        L_0x00fd:
            r5 = 0
            r1.currentProducer = r4
            r15 = 0
            int r18 = (r2 > r15 ? 1 : (r2 == r15 ? 0 : -1))
            if (r18 == 0) goto L_0x0121
            long r8 = p008rx.internal.operators.BackpressureUtils.addCap(r8, r2)
            r15 = r4
            r3 = r15
            r16 = 0
            goto L_0x0125
        L_0x010f:
            r5 = 0
            rx.Producer r15 = r1.currentProducer
            if (r15 == 0) goto L_0x0121
            r16 = 0
            int r18 = (r12 > r16 ? 1 : (r12 == r16 ? 0 : -1))
            if (r18 == 0) goto L_0x0123
            long r8 = p008rx.internal.operators.BackpressureUtils.addCap(r8, r12)
            r2 = r15
            r3 = r2
            goto L_0x0125
        L_0x0121:
            r16 = 0
        L_0x0123:
            r3 = r20
        L_0x0125:
            r2 = r22
            goto L_0x000d
        L_0x0129:
            r0 = move-exception
            r22 = r2
            r20 = r3
            r2 = r0
        L_0x012f:
            monitor-exit(r25)     // Catch:{ all -> 0x0131 }
            throw r2
        L_0x0131:
            r0 = move-exception
        L_0x0132:
            r2 = r0
            goto L_0x012f
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.producers.ProducerObserverArbiter.emitLoop():void");
    }
}
