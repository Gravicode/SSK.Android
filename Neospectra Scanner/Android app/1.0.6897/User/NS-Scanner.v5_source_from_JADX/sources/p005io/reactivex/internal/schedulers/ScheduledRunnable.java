package p005io.reactivex.internal.schedulers;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReferenceArray;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableContainer;

/* renamed from: io.reactivex.internal.schedulers.ScheduledRunnable */
public final class ScheduledRunnable extends AtomicReferenceArray<Object> implements Runnable, Callable<Object>, Disposable {
    static final Object ASYNC_DISPOSED = new Object();
    static final Object DONE = new Object();
    static final int FUTURE_INDEX = 1;
    static final Object PARENT_DISPOSED = new Object();
    static final int PARENT_INDEX = 0;
    static final Object SYNC_DISPOSED = new Object();
    static final int THREAD_INDEX = 2;
    private static final long serialVersionUID = -6120223772001106981L;
    final Runnable actual;

    public ScheduledRunnable(Runnable actual2, DisposableContainer parent) {
        super(3);
        this.actual = actual2;
        lazySet(0, parent);
    }

    public Object call() {
        run();
        return null;
    }

    public void run() {
        Object o;
        Object o2;
        lazySet(2, Thread.currentThread());
        try {
            this.actual.run();
        } catch (Throwable th) {
            lazySet(2, null);
            Object o3 = get(0);
            if (!(o3 == PARENT_DISPOSED || !compareAndSet(0, o3, DONE) || o3 == null)) {
                ((DisposableContainer) o3).delete(this);
            }
            do {
                o2 = get(1);
                if (o2 == SYNC_DISPOSED || o2 == ASYNC_DISPOSED) {
                    throw th;
                }
            } while (!compareAndSet(1, o2, DONE));
            throw th;
        }
        lazySet(2, null);
        Object o4 = get(0);
        if (!(o4 == PARENT_DISPOSED || !compareAndSet(0, o4, DONE) || o4 == null)) {
            ((DisposableContainer) o4).delete(this);
        }
        do {
            o = get(1);
            if (o == SYNC_DISPOSED || o == ASYNC_DISPOSED) {
                return;
            }
        } while (!compareAndSet(1, o, DONE));
    }

    public void setFuture(Future<?> f) {
        Object o;
        do {
            o = get(1);
            if (o != DONE) {
                if (o == SYNC_DISPOSED) {
                    f.cancel(false);
                    return;
                } else if (o == ASYNC_DISPOSED) {
                    f.cancel(true);
                    return;
                }
            } else {
                return;
            }
        } while (!compareAndSet(1, o, f));
    }

    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dispose() {
        /*
            r5 = this;
        L_0x0000:
            r0 = 1
            java.lang.Object r1 = r5.get(r0)
            java.lang.Object r2 = DONE
            r3 = 0
            if (r1 == r2) goto L_0x0038
            java.lang.Object r2 = SYNC_DISPOSED
            if (r1 == r2) goto L_0x0038
            java.lang.Object r2 = ASYNC_DISPOSED
            if (r1 != r2) goto L_0x0013
            goto L_0x0038
        L_0x0013:
            r2 = 2
            java.lang.Object r2 = r5.get(r2)
            java.lang.Thread r4 = java.lang.Thread.currentThread()
            if (r2 == r4) goto L_0x0020
            r2 = 1
            goto L_0x0021
        L_0x0020:
            r2 = 0
        L_0x0021:
            if (r2 == 0) goto L_0x0026
            java.lang.Object r4 = ASYNC_DISPOSED
            goto L_0x0028
        L_0x0026:
            java.lang.Object r4 = SYNC_DISPOSED
        L_0x0028:
            boolean r0 = r5.compareAndSet(r0, r1, r4)
            if (r0 == 0) goto L_0x0037
            if (r1 == 0) goto L_0x0038
            r0 = r1
            java.util.concurrent.Future r0 = (java.util.concurrent.Future) r0
            r0.cancel(r2)
            goto L_0x0038
        L_0x0037:
            goto L_0x0000
        L_0x0038:
            java.lang.Object r0 = r5.get(r3)
            java.lang.Object r1 = DONE
            if (r0 == r1) goto L_0x0057
            java.lang.Object r1 = PARENT_DISPOSED
            if (r0 == r1) goto L_0x0057
            if (r0 != 0) goto L_0x0047
            goto L_0x0057
        L_0x0047:
            java.lang.Object r1 = PARENT_DISPOSED
            boolean r1 = r5.compareAndSet(r3, r0, r1)
            if (r1 == 0) goto L_0x0056
            r1 = r0
            io.reactivex.internal.disposables.DisposableContainer r1 = (p005io.reactivex.internal.disposables.DisposableContainer) r1
            r1.delete(r5)
            return
        L_0x0056:
            goto L_0x0038
        L_0x0057:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.schedulers.ScheduledRunnable.dispose():void");
    }

    public boolean isDisposed() {
        Object o = get(0);
        if (o == PARENT_DISPOSED || o == DONE) {
            return true;
        }
        return false;
    }
}
