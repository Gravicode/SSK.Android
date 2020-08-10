package com.jakewharton.rxbinding.view;

import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import com.jakewharton.rxbinding.view.MenuItemActionViewEvent.Kind;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;
import p008rx.functions.Func1;

final class MenuItemActionViewEventOnSubscribe implements OnSubscribe<MenuItemActionViewEvent> {
    final Func1<? super MenuItemActionViewEvent, Boolean> handled;
    final MenuItem menuItem;

    MenuItemActionViewEventOnSubscribe(MenuItem menuItem2, Func1<? super MenuItemActionViewEvent, Boolean> handled2) {
        this.menuItem = menuItem2;
        this.handled = handled2;
    }

    public void call(final Subscriber<? super MenuItemActionViewEvent> subscriber) {
        Preconditions.checkUiThread();
        this.menuItem.setOnActionExpandListener(new OnActionExpandListener() {
            public boolean onMenuItemActionExpand(MenuItem item) {
                return onEvent(MenuItemActionViewEvent.create(MenuItemActionViewEventOnSubscribe.this.menuItem, Kind.EXPAND));
            }

            public boolean onMenuItemActionCollapse(MenuItem item) {
                return onEvent(MenuItemActionViewEvent.create(MenuItemActionViewEventOnSubscribe.this.menuItem, Kind.COLLAPSE));
            }

            private boolean onEvent(MenuItemActionViewEvent event) {
                if (!((Boolean) MenuItemActionViewEventOnSubscribe.this.handled.call(event)).booleanValue()) {
                    return false;
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(event);
                }
                return true;
            }
        });
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                MenuItemActionViewEventOnSubscribe.this.menuItem.setOnActionExpandListener(null);
            }
        });
    }
}
