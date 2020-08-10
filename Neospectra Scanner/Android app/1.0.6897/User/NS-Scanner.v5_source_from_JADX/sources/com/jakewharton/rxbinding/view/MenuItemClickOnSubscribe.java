package com.jakewharton.rxbinding.view;

import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;
import p008rx.functions.Func1;

final class MenuItemClickOnSubscribe implements OnSubscribe<Void> {
    final Func1<? super MenuItem, Boolean> handled;
    final MenuItem menuItem;

    MenuItemClickOnSubscribe(MenuItem menuItem2, Func1<? super MenuItem, Boolean> handled2) {
        this.menuItem = menuItem2;
        this.handled = handled2;
    }

    public void call(final Subscriber<? super Void> subscriber) {
        Preconditions.checkUiThread();
        this.menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (!((Boolean) MenuItemClickOnSubscribe.this.handled.call(MenuItemClickOnSubscribe.this.menuItem)).booleanValue()) {
                    return false;
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(null);
                }
                return true;
            }
        });
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                MenuItemClickOnSubscribe.this.menuItem.setOnMenuItemClickListener(null);
            }
        });
    }
}
