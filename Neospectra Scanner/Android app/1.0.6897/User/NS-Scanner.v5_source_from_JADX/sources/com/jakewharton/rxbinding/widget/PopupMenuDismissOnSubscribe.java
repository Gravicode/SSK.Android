package com.jakewharton.rxbinding.widget;

import android.widget.PopupMenu;
import android.widget.PopupMenu.OnDismissListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;

final class PopupMenuDismissOnSubscribe implements OnSubscribe<Void> {
    final PopupMenu view;

    public PopupMenuDismissOnSubscribe(PopupMenu view2) {
        this.view = view2;
    }

    public void call(final Subscriber<? super Void> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(PopupMenu menu) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(null);
                }
            }
        });
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                PopupMenuDismissOnSubscribe.this.view.setOnDismissListener(null);
            }
        });
    }
}
