package p008rx.android.plugins;

import p008rx.Scheduler;
import p008rx.functions.Action0;

/* renamed from: rx.android.plugins.RxAndroidSchedulersHook */
public class RxAndroidSchedulersHook {
    private static final RxAndroidSchedulersHook DEFAULT_INSTANCE = new RxAndroidSchedulersHook();

    public static RxAndroidSchedulersHook getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public Scheduler getMainThreadScheduler() {
        return null;
    }

    public Action0 onSchedule(Action0 action) {
        return action;
    }
}
