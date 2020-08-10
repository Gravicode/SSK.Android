package com.jakewharton.rxbinding.view;

import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;

final class ViewTreeObserverGlobalLayoutOnSubscribe implements OnSubscribe<Void> {
    final View view;

    ViewTreeObserverGlobalLayoutOnSubscribe(View view2) {
        this.view = view2;
    }

    public void call(final Subscriber<? super Void> subscriber) {
        Preconditions.checkUiThread();
        final OnGlobalLayoutListener listener = new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(null);
                }
            }
        };
        this.view.getViewTreeObserver().addOnGlobalLayoutListener(listener);
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                if (VERSION.SDK_INT >= 16) {
                    ViewTreeObserverGlobalLayoutOnSubscribe.this.view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
                } else {
                    ViewTreeObserverGlobalLayoutOnSubscribe.this.view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
                }
            }
        });
    }
}
