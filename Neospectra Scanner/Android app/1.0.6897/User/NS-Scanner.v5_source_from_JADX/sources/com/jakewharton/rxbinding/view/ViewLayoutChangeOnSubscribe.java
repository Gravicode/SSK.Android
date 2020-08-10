package com.jakewharton.rxbinding.view;

import android.view.View;
import android.view.View.OnLayoutChangeListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;

final class ViewLayoutChangeOnSubscribe implements OnSubscribe<Void> {
    final View view;

    ViewLayoutChangeOnSubscribe(View view2) {
        this.view = view2;
    }

    public void call(final Subscriber<? super Void> subscriber) {
        Preconditions.checkUiThread();
        final OnLayoutChangeListener listener = new OnLayoutChangeListener() {
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(null);
                }
            }
        };
        this.view.addOnLayoutChangeListener(listener);
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                ViewLayoutChangeOnSubscribe.this.view.removeOnLayoutChangeListener(listener);
            }
        });
    }
}
