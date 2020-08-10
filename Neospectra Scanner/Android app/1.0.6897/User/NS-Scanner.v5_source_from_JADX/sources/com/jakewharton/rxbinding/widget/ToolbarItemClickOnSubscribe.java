package com.jakewharton.rxbinding.widget;

import android.annotation.TargetApi;
import android.view.MenuItem;
import android.widget.Toolbar;
import android.widget.Toolbar.OnMenuItemClickListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;

@TargetApi(21)
final class ToolbarItemClickOnSubscribe implements OnSubscribe<MenuItem> {
    final Toolbar view;

    ToolbarItemClickOnSubscribe(Toolbar view2) {
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
                ToolbarItemClickOnSubscribe.this.view.setOnMenuItemClickListener(null);
            }
        });
    }
}
