package p008rx.plugins;

import p008rx.Completable;
import p008rx.Completable.OnSubscribe;
import p008rx.Completable.Operator;
import p008rx.annotations.Experimental;

@Experimental
/* renamed from: rx.plugins.RxJavaCompletableExecutionHook */
public abstract class RxJavaCompletableExecutionHook {
    @Deprecated
    public OnSubscribe onCreate(OnSubscribe f) {
        return f;
    }

    @Deprecated
    public OnSubscribe onSubscribeStart(Completable completableInstance, OnSubscribe onSubscribe) {
        return onSubscribe;
    }

    @Deprecated
    public Throwable onSubscribeError(Throwable e) {
        return e;
    }

    @Deprecated
    public Operator onLift(Operator lift) {
        return lift;
    }
}
