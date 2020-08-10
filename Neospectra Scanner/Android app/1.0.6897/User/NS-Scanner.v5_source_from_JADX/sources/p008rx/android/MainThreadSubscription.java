package p008rx.android;

import android.os.Looper;
import java.util.concurrent.atomic.AtomicBoolean;
import p008rx.Subscription;
import p008rx.android.schedulers.AndroidSchedulers;
import p008rx.functions.Action0;

/* renamed from: rx.android.MainThreadSubscription */
public abstract class MainThreadSubscription implements Subscription {
    private final AtomicBoolean unsubscribed = new AtomicBoolean();

    /* access modifiers changed from: protected */
    public abstract void onUnsubscribe();

    public static void verifyMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Expected to be called on the main thread but was ");
            sb.append(Thread.currentThread().getName());
            throw new IllegalStateException(sb.toString());
        }
    }

    public final boolean isUnsubscribed() {
        return this.unsubscribed.get();
    }

    public final void unsubscribe() {
        if (!this.unsubscribed.compareAndSet(false, true)) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            onUnsubscribe();
        } else {
            AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
                public void call() {
                    MainThreadSubscription.this.onUnsubscribe();
                }
            });
        }
    }
}
