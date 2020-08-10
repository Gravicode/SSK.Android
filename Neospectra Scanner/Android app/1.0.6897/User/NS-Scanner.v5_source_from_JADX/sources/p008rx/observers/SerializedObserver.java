package p008rx.observers;

import p008rx.Observer;
import p008rx.exceptions.Exceptions;
import p008rx.internal.operators.NotificationLite;

/* renamed from: rx.observers.SerializedObserver */
public class SerializedObserver<T> implements Observer<T> {
    private final Observer<? super T> actual;
    private boolean emitting;
    private FastList queue;
    private volatile boolean terminated;

    /* renamed from: rx.observers.SerializedObserver$FastList */
    static final class FastList {
        Object[] array;
        int size;

        FastList() {
        }

        public void add(Object o) {
            int s = this.size;
            Object[] a = this.array;
            if (a == null) {
                a = new Object[16];
                this.array = a;
            } else if (s == a.length) {
                Object[] array2 = new Object[((s >> 2) + s)];
                System.arraycopy(a, 0, array2, 0, s);
                a = array2;
                this.array = a;
            }
            a[s] = o;
            this.size = s + 1;
        }
    }

    public SerializedObserver(Observer<? super T> s) {
        this.actual = s;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        r8.actual.onNext(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x002e, code lost:
        r1 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0030, code lost:
        monitor-enter(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        r1 = r8.queue;
        r2 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0035, code lost:
        if (r1 != null) goto L_0x003b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0037, code lost:
        r8.emitting = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0039, code lost:
        monitor-exit(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x003a, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x003b, code lost:
        r8.queue = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x003e, code lost:
        monitor-exit(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x003f, code lost:
        r3 = r1.array;
        r4 = r3.length;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0042, code lost:
        if (r2 >= r4) goto L_0x0030;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0044, code lost:
        r5 = r3[r2];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0046, code lost:
        if (r5 != null) goto L_0x0049;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x004f, code lost:
        if (p008rx.internal.operators.NotificationLite.accept(r8.actual, r5) == false) goto L_0x0054;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0051, code lost:
        r8.terminated = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0053, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0054, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0058, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0059, code lost:
        r8.terminated = true;
        p008rx.exceptions.Exceptions.throwIfFatal(r6);
        r8.actual.onError(p008rx.exceptions.OnErrorThrowable.addValueAsLastCause(r6, r9));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0067, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x006c, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x006d, code lost:
        r8.terminated = true;
        p008rx.exceptions.Exceptions.throwOrReport(r1, r8.actual, (java.lang.Object) r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0074, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onNext(T r9) {
        /*
            r8 = this;
            boolean r0 = r8.terminated
            if (r0 == 0) goto L_0x0005
            return
        L_0x0005:
            monitor-enter(r8)
            boolean r0 = r8.terminated     // Catch:{ all -> 0x0075 }
            if (r0 == 0) goto L_0x000c
            monitor-exit(r8)     // Catch:{ all -> 0x0075 }
            return
        L_0x000c:
            boolean r0 = r8.emitting     // Catch:{ all -> 0x0075 }
            if (r0 == 0) goto L_0x0025
            rx.observers.SerializedObserver$FastList r0 = r8.queue     // Catch:{ all -> 0x0075 }
            if (r0 != 0) goto L_0x001c
            rx.observers.SerializedObserver$FastList r1 = new rx.observers.SerializedObserver$FastList     // Catch:{ all -> 0x0075 }
            r1.<init>()     // Catch:{ all -> 0x0075 }
            r0 = r1
            r8.queue = r0     // Catch:{ all -> 0x0075 }
        L_0x001c:
            java.lang.Object r1 = p008rx.internal.operators.NotificationLite.next(r9)     // Catch:{ all -> 0x0075 }
            r0.add(r1)     // Catch:{ all -> 0x0075 }
            monitor-exit(r8)     // Catch:{ all -> 0x0075 }
            return
        L_0x0025:
            r0 = 1
            r8.emitting = r0     // Catch:{ all -> 0x0075 }
            monitor-exit(r8)     // Catch:{ all -> 0x0075 }
            rx.Observer<? super T> r1 = r8.actual     // Catch:{ Throwable -> 0x006c }
            r1.onNext(r9)     // Catch:{ Throwable -> 0x006c }
            r1 = r8
        L_0x0030:
            monitor-enter(r8)
            rx.observers.SerializedObserver$FastList r2 = r8.queue     // Catch:{ all -> 0x0069 }
            r1 = r2
            r2 = 0
            if (r1 != 0) goto L_0x003b
            r8.emitting = r2     // Catch:{ all -> 0x0069 }
            monitor-exit(r8)     // Catch:{ all -> 0x0069 }
            return
        L_0x003b:
            r3 = 0
            r8.queue = r3     // Catch:{ all -> 0x0069 }
            monitor-exit(r8)     // Catch:{ all -> 0x0069 }
            java.lang.Object[] r3 = r1.array
            int r4 = r3.length
        L_0x0042:
            if (r2 >= r4) goto L_0x0068
            r5 = r3[r2]
            if (r5 != 0) goto L_0x0049
            goto L_0x0068
        L_0x0049:
            rx.Observer<? super T> r6 = r8.actual     // Catch:{ Throwable -> 0x0058 }
            boolean r6 = p008rx.internal.operators.NotificationLite.accept(r6, r5)     // Catch:{ Throwable -> 0x0058 }
            if (r6 == 0) goto L_0x0054
            r8.terminated = r0     // Catch:{ Throwable -> 0x0058 }
            return
        L_0x0054:
            int r2 = r2 + 1
            goto L_0x0042
        L_0x0058:
            r6 = move-exception
            r8.terminated = r0
            p008rx.exceptions.Exceptions.throwIfFatal(r6)
            rx.Observer<? super T> r0 = r8.actual
            java.lang.Throwable r7 = p008rx.exceptions.OnErrorThrowable.addValueAsLastCause(r6, r9)
            r0.onError(r7)
            return
        L_0x0068:
            goto L_0x0030
        L_0x0069:
            r0 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x0069 }
            throw r0
        L_0x006c:
            r1 = move-exception
            r8.terminated = r0
            rx.Observer<? super T> r0 = r8.actual
            p008rx.exceptions.Exceptions.throwOrReport(r1, r0, r9)
            return
        L_0x0075:
            r0 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x0075 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.observers.SerializedObserver.onNext(java.lang.Object):void");
    }

    public void onError(Throwable e) {
        Exceptions.throwIfFatal(e);
        if (!this.terminated) {
            synchronized (this) {
                if (!this.terminated) {
                    this.terminated = true;
                    if (this.emitting) {
                        FastList list = this.queue;
                        if (list == null) {
                            list = new FastList();
                            this.queue = list;
                        }
                        list.add(NotificationLite.error(e));
                        return;
                    }
                    this.emitting = true;
                    this.actual.onError(e);
                }
            }
        }
    }

    public void onCompleted() {
        if (!this.terminated) {
            synchronized (this) {
                if (!this.terminated) {
                    this.terminated = true;
                    if (this.emitting) {
                        FastList list = this.queue;
                        if (list == null) {
                            list = new FastList();
                            this.queue = list;
                        }
                        list.add(NotificationLite.completed());
                        return;
                    }
                    this.emitting = true;
                    this.actual.onCompleted();
                }
            }
        }
    }
}
