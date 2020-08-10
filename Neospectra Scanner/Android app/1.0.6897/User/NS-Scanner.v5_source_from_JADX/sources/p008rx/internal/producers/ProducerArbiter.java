package p008rx.internal.producers;

import p008rx.Producer;

/* renamed from: rx.internal.producers.ProducerArbiter */
public final class ProducerArbiter implements Producer {
    static final Producer NULL_PRODUCER = new Producer() {
        public void request(long n) {
        }
    };
    Producer currentProducer;
    boolean emitting;
    long missedProduced;
    Producer missedProducer;
    long missedRequested;
    long requested;

    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r6 = r8.requested + r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x002d, code lost:
        if (r6 >= 0) goto L_0x0034;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002f, code lost:
        r6 = Long.MAX_VALUE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0034, code lost:
        r8.requested = r6;
        r0 = r8.currentProducer;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0038, code lost:
        if (r0 == null) goto L_0x003d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x003a, code lost:
        r0.request(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x003d, code lost:
        emitLoop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0041, code lost:
        if (1 != 0) goto L_0x004b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0043, code lost:
        monitor-enter(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        r8.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0046, code lost:
        monitor-exit(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x004b, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x004c, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x004d, code lost:
        if (0 == 0) goto L_0x004f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x004f, code lost:
        monitor-enter(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
        r8.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0057, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void request(long r9) {
        /*
            r8 = this;
            r0 = 0
            int r2 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r2 >= 0) goto L_0x000e
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "n >= 0 required"
            r0.<init>(r1)
            throw r0
        L_0x000e:
            int r2 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r2 != 0) goto L_0x0013
            return
        L_0x0013:
            monitor-enter(r8)
            boolean r2 = r8.emitting     // Catch:{ all -> 0x0058 }
            if (r2 == 0) goto L_0x0020
            long r0 = r8.missedRequested     // Catch:{ all -> 0x0058 }
            r2 = 0
            long r0 = r0 + r9
            r8.missedRequested = r0     // Catch:{ all -> 0x0058 }
            monitor-exit(r8)     // Catch:{ all -> 0x0058 }
            return
        L_0x0020:
            r2 = 1
            r8.emitting = r2     // Catch:{ all -> 0x0058 }
            monitor-exit(r8)     // Catch:{ all -> 0x0058 }
            r2 = 0
            r3 = r2
            long r4 = r8.requested     // Catch:{ all -> 0x004c }
            r6 = 0
            long r6 = r4 + r9
            int r0 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
            if (r0 >= 0) goto L_0x0034
            r6 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
        L_0x0034:
            r8.requested = r6     // Catch:{ all -> 0x004c }
            rx.Producer r0 = r8.currentProducer     // Catch:{ all -> 0x004c }
            if (r0 == 0) goto L_0x003d
            r0.request(r9)     // Catch:{ all -> 0x004c }
        L_0x003d:
            r8.emitLoop()     // Catch:{ all -> 0x004c }
            r0 = 1
            if (r0 != 0) goto L_0x004b
            monitor-enter(r8)
            r8.emitting = r2     // Catch:{ all -> 0x0048 }
            monitor-exit(r8)     // Catch:{ all -> 0x0048 }
            goto L_0x004b
        L_0x0048:
            r1 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x0048 }
            throw r1
        L_0x004b:
            return
        L_0x004c:
            r0 = move-exception
            if (r3 != 0) goto L_0x0057
            monitor-enter(r8)
            r8.emitting = r2     // Catch:{ all -> 0x0054 }
            monitor-exit(r8)     // Catch:{ all -> 0x0054 }
            goto L_0x0057
        L_0x0054:
            r0 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x0054 }
            throw r0
        L_0x0057:
            throw r0
        L_0x0058:
            r0 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x0058 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.producers.ProducerArbiter.request(long):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r4 = r8.requested;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002a, code lost:
        if (r4 == Long.MAX_VALUE) goto L_0x003c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002c, code lost:
        r6 = r4 - r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0030, code lost:
        if (r6 >= 0) goto L_0x003a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0039, code lost:
        throw new java.lang.IllegalStateException("more items arrived than were requested");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003a, code lost:
        r8.requested = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x003c, code lost:
        emitLoop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0040, code lost:
        if (1 != 0) goto L_0x004a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0042, code lost:
        monitor-enter(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        r8.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0045, code lost:
        monitor-exit(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x004a, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x004b, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x004c, code lost:
        if (0 == 0) goto L_0x004e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x004e, code lost:
        monitor-enter(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
        r8.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0056, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void produced(long r9) {
        /*
            r8 = this;
            r0 = 0
            int r2 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r2 > 0) goto L_0x000e
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "n > 0 required"
            r0.<init>(r1)
            throw r0
        L_0x000e:
            monitor-enter(r8)
            boolean r2 = r8.emitting     // Catch:{ all -> 0x0057 }
            if (r2 == 0) goto L_0x001b
            long r0 = r8.missedProduced     // Catch:{ all -> 0x0057 }
            r2 = 0
            long r0 = r0 + r9
            r8.missedProduced = r0     // Catch:{ all -> 0x0057 }
            monitor-exit(r8)     // Catch:{ all -> 0x0057 }
            return
        L_0x001b:
            r2 = 1
            r8.emitting = r2     // Catch:{ all -> 0x0057 }
            monitor-exit(r8)     // Catch:{ all -> 0x0057 }
            r2 = 0
            r3 = r2
            long r4 = r8.requested     // Catch:{ all -> 0x004b }
            r6 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            int r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r6 == 0) goto L_0x003c
            long r6 = r4 - r9
            int r0 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
            if (r0 >= 0) goto L_0x003a
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException     // Catch:{ all -> 0x004b }
            java.lang.String r1 = "more items arrived than were requested"
            r0.<init>(r1)     // Catch:{ all -> 0x004b }
            throw r0     // Catch:{ all -> 0x004b }
        L_0x003a:
            r8.requested = r6     // Catch:{ all -> 0x004b }
        L_0x003c:
            r8.emitLoop()     // Catch:{ all -> 0x004b }
            r0 = 1
            if (r0 != 0) goto L_0x004a
            monitor-enter(r8)
            r8.emitting = r2     // Catch:{ all -> 0x0047 }
            monitor-exit(r8)     // Catch:{ all -> 0x0047 }
            goto L_0x004a
        L_0x0047:
            r1 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x0047 }
            throw r1
        L_0x004a:
            return
        L_0x004b:
            r0 = move-exception
            if (r3 != 0) goto L_0x0056
            monitor-enter(r8)
            r8.emitting = r2     // Catch:{ all -> 0x0053 }
            monitor-exit(r8)     // Catch:{ all -> 0x0053 }
            goto L_0x0056
        L_0x0053:
            r0 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x0053 }
            throw r0
        L_0x0056:
            throw r0
        L_0x0057:
            r0 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x0057 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.producers.ProducerArbiter.produced(long):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        r4.currentProducer = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0017, code lost:
        if (r5 == null) goto L_0x001e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0019, code lost:
        r5.request(r4.requested);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x001e, code lost:
        emitLoop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0022, code lost:
        if (1 != 0) goto L_0x002c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0024, code lost:
        monitor-enter(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r4.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0027, code lost:
        monitor-exit(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x002c, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x002d, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x002e, code lost:
        if (0 == 0) goto L_0x0030;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0030, code lost:
        monitor-enter(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
        r4.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0038, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setProducer(p008rx.Producer r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            boolean r0 = r4.emitting     // Catch:{ all -> 0x0039 }
            if (r0 == 0) goto L_0x000f
            if (r5 != 0) goto L_0x000a
            rx.Producer r0 = NULL_PRODUCER     // Catch:{ all -> 0x0039 }
            goto L_0x000b
        L_0x000a:
            r0 = r5
        L_0x000b:
            r4.missedProducer = r0     // Catch:{ all -> 0x0039 }
            monitor-exit(r4)     // Catch:{ all -> 0x0039 }
            return
        L_0x000f:
            r0 = 1
            r4.emitting = r0     // Catch:{ all -> 0x0039 }
            monitor-exit(r4)     // Catch:{ all -> 0x0039 }
            r0 = 0
            r1 = r0
            r4.currentProducer = r5     // Catch:{ all -> 0x002d }
            if (r5 == 0) goto L_0x001e
            long r2 = r4.requested     // Catch:{ all -> 0x002d }
            r5.request(r2)     // Catch:{ all -> 0x002d }
        L_0x001e:
            r4.emitLoop()     // Catch:{ all -> 0x002d }
            r1 = 1
            if (r1 != 0) goto L_0x002c
            monitor-enter(r4)
            r4.emitting = r0     // Catch:{ all -> 0x0029 }
            monitor-exit(r4)     // Catch:{ all -> 0x0029 }
            goto L_0x002c
        L_0x0029:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0029 }
            throw r0
        L_0x002c:
            return
        L_0x002d:
            r2 = move-exception
            if (r1 != 0) goto L_0x0038
            monitor-enter(r4)
            r4.emitting = r0     // Catch:{ all -> 0x0035 }
            monitor-exit(r4)     // Catch:{ all -> 0x0035 }
            goto L_0x0038
        L_0x0035:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0035 }
            throw r0
        L_0x0038:
            throw r2
        L_0x0039:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0039 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.producers.ProducerArbiter.setProducer(rx.Producer):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0026, code lost:
        r8 = r15.requested;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002f, code lost:
        if (r8 == Long.MAX_VALUE) goto L_0x0056;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0031, code lost:
        r12 = r8 + r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0035, code lost:
        if (r12 < 0) goto L_0x004f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0039, code lost:
        if (r12 != Long.MAX_VALUE) goto L_0x003c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003c, code lost:
        r10 = r12 - r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0041, code lost:
        if (r10 >= 0) goto L_0x004b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x004a, code lost:
        throw new java.lang.IllegalStateException("more produced than requested");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x004b, code lost:
        r8 = r10;
        r15.requested = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x004f, code lost:
        r8 = Long.MAX_VALUE;
        r15.requested = Long.MAX_VALUE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0056, code lost:
        if (r3 == null) goto L_0x0065;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x005a, code lost:
        if (r3 != NULL_PRODUCER) goto L_0x005f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x005c, code lost:
        r15.currentProducer = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x005f, code lost:
        r15.currentProducer = r3;
        r3.request(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0065, code lost:
        r10 = r15.currentProducer;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0067, code lost:
        if (r10 == null) goto L_0x0006;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x006b, code lost:
        if (r4 == 0) goto L_0x0006;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x006d, code lost:
        r10.request(r4);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void emitLoop() {
        /*
            r15 = this;
            r0 = 0
            r1 = 0
            r3 = r0
            r4 = r1
            r6 = r4
        L_0x0006:
            monitor-enter(r15)
            long r8 = r15.missedRequested     // Catch:{ all -> 0x0071 }
            r4 = r8
            long r8 = r15.missedProduced     // Catch:{ all -> 0x0071 }
            r6 = r8
            rx.Producer r8 = r15.missedProducer     // Catch:{ all -> 0x0071 }
            r3 = r8
            int r8 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1))
            if (r8 != 0) goto L_0x001f
            int r8 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1))
            if (r8 != 0) goto L_0x001f
            if (r3 != 0) goto L_0x001f
            r0 = 0
            r15.emitting = r0     // Catch:{ all -> 0x0071 }
            monitor-exit(r15)     // Catch:{ all -> 0x0071 }
            return
        L_0x001f:
            r15.missedRequested = r1     // Catch:{ all -> 0x0071 }
            r15.missedProduced = r1     // Catch:{ all -> 0x0071 }
            r15.missedProducer = r0     // Catch:{ all -> 0x0071 }
            monitor-exit(r15)     // Catch:{ all -> 0x0071 }
            long r8 = r15.requested
            r10 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            int r12 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r12 == 0) goto L_0x0056
            long r12 = r8 + r4
            int r14 = (r12 > r1 ? 1 : (r12 == r1 ? 0 : -1))
            if (r14 < 0) goto L_0x004f
            int r10 = (r12 > r10 ? 1 : (r12 == r10 ? 0 : -1))
            if (r10 != 0) goto L_0x003c
            goto L_0x004f
        L_0x003c:
            r10 = 0
            long r10 = r12 - r6
            int r14 = (r10 > r1 ? 1 : (r10 == r1 ? 0 : -1))
            if (r14 >= 0) goto L_0x004b
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "more produced than requested"
            r0.<init>(r1)
            throw r0
        L_0x004b:
            r8 = r10
            r15.requested = r10
            goto L_0x0056
        L_0x004f:
            r8 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            r15.requested = r8
        L_0x0056:
            if (r3 == 0) goto L_0x0065
            rx.Producer r10 = NULL_PRODUCER
            if (r3 != r10) goto L_0x005f
            r15.currentProducer = r0
            goto L_0x0070
        L_0x005f:
            r15.currentProducer = r3
            r3.request(r8)
            goto L_0x0070
        L_0x0065:
            rx.Producer r10 = r15.currentProducer
            if (r10 == 0) goto L_0x0070
            int r11 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1))
            if (r11 == 0) goto L_0x0070
            r10.request(r4)
        L_0x0070:
            goto L_0x0006
        L_0x0071:
            r0 = move-exception
            monitor-exit(r15)     // Catch:{ all -> 0x0071 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.producers.ProducerArbiter.emitLoop():void");
    }
}
