package p008rx.internal.util;

import p008rx.Observer;
import p008rx.functions.Action0;
import p008rx.functions.Action1;

/* renamed from: rx.internal.util.ActionObserver */
public final class ActionObserver<T> implements Observer<T> {
    final Action0 onCompleted;
    final Action1<? super Throwable> onError;
    final Action1<? super T> onNext;

    public ActionObserver(Action1<? super T> onNext2, Action1<? super Throwable> onError2, Action0 onCompleted2) {
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
