package com.jakewharton.rxbinding.view;

import android.view.View;
import android.view.View.OnLongClickListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;
import p008rx.functions.Func0;

final class ViewLongClickOnSubscribe implements OnSubscribe<Void> {
    final Func0<Boolean> handled;
    final View view;

    ViewLongClickOnSubscribe(View view2, Func0<Boolean> handled2) {
        this.view = view2;
        this.handled = handled2;
    }

    public void call(final Subscriber<? super Void> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (!((Boolean) ViewLongClickOnSubscribe.this.handled.call()).booleanValue()) {
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
                ViewLongClickOnSubscribe.this.view.setOnLongClickListener(null);
            }
        });
    }
}
