package com.jakewharton.rxbinding.view;

import android.view.View;
import android.view.View.OnClickListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;

final class ViewClickOnSubscribe implements OnSubscribe<Void> {
    final View view;

    ViewClickOnSubscribe(View view2) {
        this.view = view2;
    }

    public void call(final Subscriber<? super Void> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(null);
                }
            }
        });
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                ViewClickOnSubscribe.this.view.setOnClickListener(null);
            }
        });
    }
}
