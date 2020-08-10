package p008rx.android.schedulers;

import android.os.Handler;
import android.os.Looper;
import p008rx.Scheduler;
import p008rx.android.plugins.RxAndroidPlugins;

/* renamed from: rx.android.schedulers.AndroidSchedulers */
public final class AndroidSchedulers {

    /* renamed from: rx.android.schedulers.AndroidSchedulers$MainThreadSchedulerHolder */
    private static class MainThreadSchedulerHolder {
        static final Scheduler MAIN_THREAD_SCHEDULER = new HandlerScheduler(new Handler(Looper.getMainLooper()));

        private MainThreadSchedulerHolder() {
        }
    }

    private AndroidSchedulers() {
        throw new AssertionError("No instances");
    }

    public static Scheduler mainThread() {
        Scheduler scheduler = RxAndroidPlugins.getInstance().getSchedulersHook().getMainThreadScheduler();
        return scheduler != null ? scheduler : MainThreadSchedulerHolder.MAIN_THREAD_SCHEDULER;
    }
}
