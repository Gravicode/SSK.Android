package p005io.reactivex.observers;

import p005io.reactivex.Observer;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.util.AppendOnlyLinkedArrayList;
import p005io.reactivex.internal.util.NotificationLite;

/* renamed from: io.reactivex.observers.SerializedObserver */
public final class SerializedObserver<T> implements Observer<T>, Disposable {
    static final int QUEUE_LINK_SIZE = 4;
    final Observer<? super T> actual;
    final boolean delayError;
    volatile boolean done;
    boolean emitting;
    AppendOnlyLinkedArrayList<Object> queue;

    /* renamed from: s */
    Disposable f468s;

    public SerializedObserver(@NonNull Observer<? super T> actual2) {
        this(actual2, false);
    }

    public SerializedObserver(@NonNull Observer<? super T> actual2, boolean delayError2) {
        this.actual = actual2;
        this.delayError = delayError2;
    }

    public void onSubscribe(@NonNull Disposable s) {
        if (DisposableHelper.validate(this.f468s, s)) {
            this.f468s = s;
            this.actual.onSubscribe(this);
        }
    }

    public void dispose() {
        this.f468s.dispose();
    }

    public boolean isDisposed() {
        return this.f468s.isDisposed();
    }

    public void onNext(@NonNull T t) {
        if (!this.done) {
            if (t == null) {
                this.f468s.dispose();
                onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
                return;
            }
            synchronized (this) {
                if (!this.done) {
                    if (this.emitting) {
                        AppendOnlyLinkedArrayList<Object> q = this.queue;
                        if (q == null) {
                            q = new AppendOnlyLinkedArrayList<>(4);
                            this.queue = q;
                        }
                        q.add(NotificationLite.next(t));
                        return;
                    }
                    this.emitting = true;
                    this.actual.onNext(t);
                    emitLoop();
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0033, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x003a, code lost:
        if (r0 == false) goto L_0x0040;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x003c, code lost:
        p005io.reactivex.plugins.RxJavaPlugins.onError(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x003f, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0040, code lost:
        r3.actual.onError(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0045, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onError(@p005io.reactivex.annotations.NonNull java.lang.Throwable r4) {
        /*
            r3 = this;
            boolean r0 = r3.done
            if (r0 == 0) goto L_0x0008
            p005io.reactivex.plugins.RxJavaPlugins.onError(r4)
            return
        L_0x0008:
            monitor-enter(r3)
            boolean r0 = r3.done     // Catch:{ all -> 0x0046 }
            if (r0 == 0) goto L_0x000f
            r0 = 1
            goto L_0x0039
        L_0x000f:
            boolean r0 = r3.emitting     // Catch:{ all -> 0x0046 }
            r1 = 1
            if (r0 == 0) goto L_0x0034
            r3.done = r1     // Catch:{ all -> 0x0046 }
            io.reactivex.internal.util.AppendOnlyLinkedArrayList<java.lang.Object> r0 = r3.queue     // Catch:{ all -> 0x0046 }
            if (r0 != 0) goto L_0x0023
            io.reactivex.internal.util.AppendOnlyLinkedArrayList r1 = new io.reactivex.internal.util.AppendOnlyLinkedArrayList     // Catch:{ all -> 0x0046 }
            r2 = 4
            r1.<init>(r2)     // Catch:{ all -> 0x0046 }
            r0 = r1
            r3.queue = r0     // Catch:{ all -> 0x0046 }
        L_0x0023:
            java.lang.Object r1 = p005io.reactivex.internal.util.NotificationLite.error(r4)     // Catch:{ all -> 0x0046 }
            boolean r2 = r3.delayError     // Catch:{ all -> 0x0046 }
            if (r2 == 0) goto L_0x002f
            r0.add(r1)     // Catch:{ all -> 0x0046 }
            goto L_0x0032
        L_0x002f:
            r0.setFirst(r1)     // Catch:{ all -> 0x0046 }
        L_0x0032:
            monitor-exit(r3)     // Catch:{ all -> 0x0046 }
            return
        L_0x0034:
            r3.done = r1     // Catch:{ all -> 0x0046 }
            r3.emitting = r1     // Catch:{ all -> 0x0046 }
            r0 = 0
        L_0x0039:
            monitor-exit(r3)     // Catch:{ all -> 0x0046 }
            if (r0 == 0) goto L_0x0040
            p005io.reactivex.plugins.RxJavaPlugins.onError(r4)
            return
        L_0x0040:
            io.reactivex.Observer<? super T> r1 = r3.actual
            r1.onError(r4)
            return
        L_0x0046:
            r0 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0046 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.observers.SerializedObserver.onError(java.lang.Throwable):void");
    }

    public void onComplete() {
        if (!this.done) {
            synchronized (this) {
                if (!this.done) {
                    if (this.emitting) {
                        AppendOnlyLinkedArrayList<Object> q = this.queue;
                        if (q == null) {
                            q = new AppendOnlyLinkedArrayList<>(4);
                            this.queue = q;
                        }
                        q.add(NotificationLite.complete());
                        return;
                    }
                    this.done = true;
                    this.emitting = true;
                    this.actual.onComplete();
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void emitLoop() {
        AppendOnlyLinkedArrayList<Object> q;
        do {
            synchronized (this) {
                q = this.queue;
                if (q == null) {
                    this.emitting = false;
                    return;
                }
                this.queue = null;
            }
        } while (!q.accept(this.actual));
    }
}
