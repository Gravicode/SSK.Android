package p008rx.internal.util;

import java.util.concurrent.atomic.AtomicLong;
import p008rx.Producer;
import p008rx.annotations.Experimental;

@Experimental
/* renamed from: rx.internal.util.BackpressureDrainManager */
public final class BackpressureDrainManager extends AtomicLong implements Producer {
    private static final long serialVersionUID = 2826241102729529449L;
    final BackpressureQueueCallback actual;
    boolean emitting;
    Throwable exception;
    volatile boolean terminated;

    /* renamed from: rx.internal.util.BackpressureDrainManager$BackpressureQueueCallback */
    public interface BackpressureQueueCallback {
        boolean accept(Object obj);

        void complete(Throwable th);

        Object peek();

        Object poll();
    }

    public BackpressureDrainManager(BackpressureQueueCallback actual2) {
        this.actual = actual2;
    }

    public boolean isTerminated() {
        return this.terminated;
    }

    public void terminate() {
        this.terminated = true;
    }

    public void terminate(Throwable error) {
        if (!this.terminated) {
            this.exception = error;
            this.terminated = true;
        }
    }

    public void terminateAndDrain() {
        this.terminated = true;
        drain();
    }

    public void terminateAndDrain(Throwable error) {
        if (!this.terminated) {
            this.exception = error;
            this.terminated = true;
            drain();
        }
    }

    public void request(long n) {
        long r;
        boolean mayDrain;
        long u;
        if (n != 0) {
            do {
                r = get();
                mayDrain = r == 0;
                if (r == Long.MAX_VALUE) {
                    break;
                } else if (n == Long.MAX_VALUE) {
                    u = n;
                    mayDrain = true;
                } else if (r > Long.MAX_VALUE - n) {
                    u = Long.MAX_VALUE;
                } else {
                    u = r + n;
                }
            } while (!compareAndSet(r, u));
            if (mayDrain) {
                drain();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:0x00ad, code lost:
        monitor-enter(r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:102:?, code lost:
        r15.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:103:0x00b0, code lost:
        monitor-exit(r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:108:0x00b5, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x00b6, code lost:
        r7 = r7 - 1;
        r3 = r3 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x000e, code lost:
        r3 = get();
        r5 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:0x00bd, code lost:
        r1 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:0x00be, code lost:
        r7 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:112:0x00bf, code lost:
        if (r5 == false) goto L_0x00c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:113:0x00c1, code lost:
        monitor-enter(r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:115:?, code lost:
        r15.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:121:0x00c9, code lost:
        throw r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        r6 = r15.actual;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0015, code lost:
        r7 = r3;
        r3 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x001b, code lost:
        if (r7 > 0) goto L_0x001f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x001d, code lost:
        if (r2 == false) goto L_0x0044;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x001f, code lost:
        if (r2 == false) goto L_0x003d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0025, code lost:
        if (r6.peek() != null) goto L_0x0038;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0027, code lost:
        r6.complete(r15.exception);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x002d, code lost:
        if (1 != 0) goto L_0x0037;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x002f, code lost:
        monitor-enter(r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        r15.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0032, code lost:
        monitor-exit(r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0037, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x003a, code lost:
        if (r7 != 0) goto L_0x003d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        r4 = r6.poll();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0041, code lost:
        if (r4 != null) goto L_0x00a4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0044, code lost:
        monitor-enter(r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
        r2 = r15.terminated;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x004c, code lost:
        if (r6.peek() == null) goto L_0x0050;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x004e, code lost:
        r4 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0050, code lost:
        r4 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x005c, code lost:
        if (get() != Long.MAX_VALUE) goto L_0x007a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x005e, code lost:
        if (r4 != false) goto L_0x0074;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0060, code lost:
        if (r2 != false) goto L_0x0074;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0062, code lost:
        r1 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:?, code lost:
        r15.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0065, code lost:
        monitor-exit(r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0066, code lost:
        if (1 != 0) goto L_0x0070;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0068, code lost:
        monitor-enter(r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:?, code lost:
        r15.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x006b, code lost:
        monitor-exit(r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0070, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0071, code lost:
        r4 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0072, code lost:
        r5 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0074, code lost:
        r7 = Long.MAX_VALUE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:?, code lost:
        r7 = addAndGet((long) (-r3));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x0083, code lost:
        if (r7 == 0) goto L_0x0087;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x0085, code lost:
        if (r4 != false) goto L_0x008c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x0087, code lost:
        if (r2 == false) goto L_0x0090;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x0089, code lost:
        if (r4 == false) goto L_0x008c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x008c, code lost:
        monitor-exit(r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x008d, code lost:
        r3 = r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x0090, code lost:
        r1 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:?, code lost:
        r15.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x0093, code lost:
        monitor-exit(r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0094, code lost:
        if (1 != 0) goto L_0x009e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x0096, code lost:
        monitor-enter(r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:?, code lost:
        r15.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x0099, code lost:
        monitor-exit(r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x009e, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x009f, code lost:
        r4 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:?, code lost:
        monitor-exit(r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:?, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x00a2, code lost:
        r1 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x00a8, code lost:
        if (r6.accept(r4) == false) goto L_0x00b6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x00ab, code lost:
        if (1 != 0) goto L_0x00b5;
     */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x00c1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void drain() {
        /*
            r15 = this;
            monitor-enter(r15)
            r0 = 0
            boolean r1 = r15.emitting     // Catch:{ all -> 0x00cd }
            if (r1 == 0) goto L_0x0008
            monitor-exit(r15)     // Catch:{ all -> 0x00cd }
            return
        L_0x0008:
            r1 = 1
            r15.emitting = r1     // Catch:{ all -> 0x00cd }
            boolean r2 = r15.terminated     // Catch:{ all -> 0x00cd }
            monitor-exit(r15)     // Catch:{ all -> 0x00ca }
            long r3 = r15.get()
            r5 = r0
            rx.internal.util.BackpressureDrainManager$BackpressureQueueCallback r6 = r15.actual     // Catch:{ all -> 0x00bd }
        L_0x0015:
            r7 = r3
            r3 = 0
        L_0x0017:
            r9 = 0
            int r4 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r4 > 0) goto L_0x001f
            if (r2 == 0) goto L_0x0044
        L_0x001f:
            if (r2 == 0) goto L_0x003d
            java.lang.Object r4 = r6.peek()     // Catch:{ all -> 0x00a2 }
            if (r4 != 0) goto L_0x0038
            r5 = 1
            java.lang.Throwable r1 = r15.exception     // Catch:{ all -> 0x00a2 }
            r6.complete(r1)     // Catch:{ all -> 0x00a2 }
            if (r5 != 0) goto L_0x0037
            monitor-enter(r15)
            r15.emitting = r0     // Catch:{ all -> 0x0034 }
            monitor-exit(r15)     // Catch:{ all -> 0x0034 }
            goto L_0x0037
        L_0x0034:
            r0 = move-exception
            monitor-exit(r15)     // Catch:{ all -> 0x0034 }
            throw r0
        L_0x0037:
            return
        L_0x0038:
            int r11 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r11 != 0) goto L_0x003d
            goto L_0x0044
        L_0x003d:
            java.lang.Object r4 = r6.poll()     // Catch:{ all -> 0x00a2 }
            if (r4 != 0) goto L_0x00a4
        L_0x0044:
            monitor-enter(r15)     // Catch:{ all -> 0x00a2 }
            boolean r4 = r15.terminated     // Catch:{ all -> 0x009f }
            r2 = r4
            java.lang.Object r4 = r6.peek()     // Catch:{ all -> 0x009f }
            if (r4 == 0) goto L_0x0050
            r4 = 1
            goto L_0x0051
        L_0x0050:
            r4 = 0
        L_0x0051:
            long r11 = r15.get()     // Catch:{ all -> 0x009f }
            r13 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            int r11 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
            if (r11 != 0) goto L_0x007a
            if (r4 != 0) goto L_0x0074
            if (r2 != 0) goto L_0x0074
            r1 = 1
            r15.emitting = r0     // Catch:{ all -> 0x0071 }
            monitor-exit(r15)     // Catch:{ all -> 0x0071 }
            if (r1 != 0) goto L_0x0070
            monitor-enter(r15)
            r15.emitting = r0     // Catch:{ all -> 0x006d }
            monitor-exit(r15)     // Catch:{ all -> 0x006d }
            goto L_0x0070
        L_0x006d:
            r0 = move-exception
            monitor-exit(r15)     // Catch:{ all -> 0x006d }
            throw r0
        L_0x0070:
            return
        L_0x0071:
            r4 = move-exception
            r5 = r1
            goto L_0x00a0
        L_0x0074:
            r7 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            goto L_0x008c
        L_0x007a:
            int r11 = -r3
            long r11 = (long) r11
            long r11 = r15.addAndGet(r11)     // Catch:{ all -> 0x009f }
            r7 = r11
            int r9 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r9 == 0) goto L_0x0087
            if (r4 != 0) goto L_0x008c
        L_0x0087:
            if (r2 == 0) goto L_0x0090
            if (r4 == 0) goto L_0x008c
            goto L_0x0090
        L_0x008c:
            monitor-exit(r15)     // Catch:{ all -> 0x009f }
            r3 = r7
            goto L_0x0015
        L_0x0090:
            r1 = 1
            r15.emitting = r0     // Catch:{ all -> 0x0071 }
            monitor-exit(r15)     // Catch:{ all -> 0x0071 }
            if (r1 != 0) goto L_0x009e
            monitor-enter(r15)
            r15.emitting = r0     // Catch:{ all -> 0x009b }
            monitor-exit(r15)     // Catch:{ all -> 0x009b }
            goto L_0x009e
        L_0x009b:
            r0 = move-exception
            monitor-exit(r15)     // Catch:{ all -> 0x009b }
            throw r0
        L_0x009e:
            return
        L_0x009f:
            r4 = move-exception
        L_0x00a0:
            monitor-exit(r15)     // Catch:{ all -> 0x009f }
            throw r4     // Catch:{ all -> 0x00a2 }
        L_0x00a2:
            r1 = move-exception
            goto L_0x00bf
        L_0x00a4:
            boolean r9 = r6.accept(r4)     // Catch:{ all -> 0x00a2 }
            if (r9 == 0) goto L_0x00b6
            r1 = 1
            if (r1 != 0) goto L_0x00b5
            monitor-enter(r15)
            r15.emitting = r0     // Catch:{ all -> 0x00b2 }
            monitor-exit(r15)     // Catch:{ all -> 0x00b2 }
            goto L_0x00b5
        L_0x00b2:
            r0 = move-exception
            monitor-exit(r15)     // Catch:{ all -> 0x00b2 }
            throw r0
        L_0x00b5:
            return
        L_0x00b6:
            r9 = 1
            long r7 = r7 - r9
            int r3 = r3 + 1
            goto L_0x0017
        L_0x00bd:
            r1 = move-exception
            r7 = r3
        L_0x00bf:
            if (r5 != 0) goto L_0x00c9
            monitor-enter(r15)
            r15.emitting = r0     // Catch:{ all -> 0x00c6 }
            monitor-exit(r15)     // Catch:{ all -> 0x00c6 }
            goto L_0x00c9
        L_0x00c6:
            r0 = move-exception
            monitor-exit(r15)     // Catch:{ all -> 0x00c6 }
            throw r0
        L_0x00c9:
            throw r1
        L_0x00ca:
            r1 = move-exception
            r0 = r2
            goto L_0x00ce
        L_0x00cd:
            r1 = move-exception
        L_0x00ce:
            monitor-exit(r15)     // Catch:{ all -> 0x00cd }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.util.BackpressureDrainManager.drain():void");
    }
}
