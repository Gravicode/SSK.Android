package p008rx.internal.util;

import p008rx.Subscriber;
import p008rx.functions.Action0;
import p008rx.functions.Action1;

/* renamed from: rx.internal.util.ActionSubscriber */
public final class ActionSubscriber<T> extends Subscriber<T> {
    final Action0 onCompleted;
    final Action1<Throwable> onError;
    final Action1<? super T> onNext;

    public ActionSubscriber(Action1<? super T> onNext2, Action1<Throwable> onError2, Action0 onCompleted2) {
        this.onNext = onNext2;
        this.onError = onError2;
        this.onCompleted = onCompleted2;
    }

    public void onNext(T t) {
        this.onNext.call(t);
    }

    public void onError(Throwable e) {
        this.onError.call(e);
    }

    public void onCompleted() {
        this.onCompleted.call();
    }
}
