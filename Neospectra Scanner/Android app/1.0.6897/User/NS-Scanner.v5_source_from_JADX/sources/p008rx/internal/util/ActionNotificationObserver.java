package p008rx.internal.util;

import p008rx.Notification;
import p008rx.Observer;
import p008rx.functions.Action1;

/* renamed from: rx.internal.util.ActionNotificationObserver */
public final class ActionNotificationObserver<T> implements Observer<T> {
    final Action1<Notification<? super T>> onNotification;

    public ActionNotificationObserver(Action1<Notification<? super T>> onNotification2) {
        this.onNotification = onNotification2;
    }

    public void onNext(T t) {
        this.onNotification.call(Notification.createOnNext(t));
    }

    public void onError(Throwable e) {
        this.onNotification.call(Notification.createOnError(e));
    }

    public void onCompleted() {
        this.onNotification.call(Notification.createOnCompleted());
    }
}
