package com.trello.rxlifecycle.android;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.OutsideLifecycleException;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.internal.Preconditions;
import p008rx.Observable;
import p008rx.functions.Func1;

public class RxLifecycleAndroid {
    private static final Func1<ActivityEvent, ActivityEvent> ACTIVITY_LIFECYCLE = new Func1<ActivityEvent, ActivityEvent>() {
        public ActivityEvent call(ActivityEvent lastEvent) {
            switch (C08493.$SwitchMap$com$trello$rxlifecycle$android$ActivityEvent[lastEvent.ordinal()]) {
                case 1:
                    return ActivityEvent.DESTROY;
                case 2:
                    return ActivityEvent.STOP;
                case 3:
                    return ActivityEvent.PAUSE;
                case 4:
                    return ActivityEvent.STOP;
                case 5:
                    return ActivityEvent.DESTROY;
                case 6:
                    throw new OutsideLifecycleException("Cannot bind to Activity lifecycle when outside of it.");
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Binding to ");
                    sb.append(lastEvent);
                    sb.append(" not yet implemented");
                    throw new UnsupportedOperationException(sb.toString());
            }
        }
    };
    private static final Func1<FragmentEvent, FragmentEvent> FRAGMENT_LIFECYCLE = new Func1<FragmentEvent, FragmentEvent>() {
        public FragmentEvent call(FragmentEvent lastEvent) {
            switch (C08493.$SwitchMap$com$trello$rxlifecycle$android$FragmentEvent[lastEvent.ordinal()]) {
                case 1:
                    return FragmentEvent.DETACH;
                case 2:
                    return FragmentEvent.DESTROY;
                case 3:
                    return FragmentEvent.DESTROY_VIEW;
                case 4:
                    return FragmentEvent.STOP;
                case 5:
                    return FragmentEvent.PAUSE;
                case 6:
                    return FragmentEvent.STOP;
                case 7:
                    return FragmentEvent.DESTROY_VIEW;
                case 8:
                    return FragmentEvent.DESTROY;
                case 9:
                    return FragmentEvent.DETACH;
                case 10:
                    throw new OutsideLifecycleException("Cannot bind to Fragment lifecycle when outside of it.");
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Binding to ");
                    sb.append(lastEvent);
                    sb.append(" not yet implemented");
                    throw new UnsupportedOperationException(sb.toString());
            }
        }
    };

    /* renamed from: com.trello.rxlifecycle.android.RxLifecycleAndroid$3 */
    static /* synthetic */ class C08493 {
        static final /* synthetic */ int[] $SwitchMap$com$trello$rxlifecycle$android$ActivityEvent = new int[ActivityEvent.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$trello$rxlifecycle$android$FragmentEvent = new int[FragmentEvent.values().length];

        static {
            try {
                $SwitchMap$com$trello$rxlifecycle$android$FragmentEvent[FragmentEvent.ATTACH.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$trello$rxlifecycle$android$FragmentEvent[FragmentEvent.CREATE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$trello$rxlifecycle$android$FragmentEvent[FragmentEvent.CREATE_VIEW.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$trello$rxlifecycle$android$FragmentEvent[FragmentEvent.START.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$trello$rxlifecycle$android$FragmentEvent[FragmentEvent.RESUME.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$trello$rxlifecycle$android$FragmentEvent[FragmentEvent.PAUSE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$trello$rxlifecycle$android$FragmentEvent[FragmentEvent.STOP.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$trello$rxlifecycle$android$FragmentEvent[FragmentEvent.DESTROY_VIEW.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$trello$rxlifecycle$android$FragmentEvent[FragmentEvent.DESTROY.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$trello$rxlifecycle$android$FragmentEvent[FragmentEvent.DETACH.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$trello$rxlifecycle$android$ActivityEvent[ActivityEvent.CREATE.ordinal()] = 1;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$trello$rxlifecycle$android$ActivityEvent[ActivityEvent.START.ordinal()] = 2;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$trello$rxlifecycle$android$ActivityEvent[ActivityEvent.RESUME.ordinal()] = 3;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$trello$rxlifecycle$android$ActivityEvent[ActivityEvent.PAUSE.ordinal()] = 4;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$trello$rxlifecycle$android$ActivityEvent[ActivityEvent.STOP.ordinal()] = 5;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$com$trello$rxlifecycle$android$ActivityEvent[ActivityEvent.DESTROY.ordinal()] = 6;
            } catch (NoSuchFieldError e16) {
            }
        }
    }

    private RxLifecycleAndroid() {
        throw new AssertionError("No instances");
    }

    @CheckResult
    @NonNull
    public static <T> LifecycleTransformer<T> bindActivity(@NonNull Observable<ActivityEvent> lifecycle) {
        return RxLifecycle.bind(lifecycle, ACTIVITY_LIFECYCLE);
    }

    @CheckResult
    @NonNull
    public static <T> LifecycleTransformer<T> bindFragment(@NonNull Observable<FragmentEvent> lifecycle) {
        return RxLifecycle.bind(lifecycle, FRAGMENT_LIFECYCLE);
    }

    @CheckResult
    @NonNull
    public static <T> LifecycleTransformer<T> bindView(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        return RxLifecycle.bind(RxView.detaches(view));
    }
}
