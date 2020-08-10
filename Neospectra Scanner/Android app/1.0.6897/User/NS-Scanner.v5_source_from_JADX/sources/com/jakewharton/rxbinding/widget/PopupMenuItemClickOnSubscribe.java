package com.jakewharton.rxbinding.widget;

import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;

final class PopupMenuItemClickOnSubscribe implements OnSubscribe<MenuItem> {
    final PopupMenu view;

    public PopupMenuItemClickOnSubscribe(PopupMenu view2) {
        this.view = view2;
    }

    public void call(final Subscriber<? super MenuItem> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(item);
                }
                return true;
            }
        });
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                PopupMenuItemClickOnSubscribe.this.view.setOnMenuItemClickListener(null);
            }
        });
    }
}
