package com.jakewharton.rxbinding.view;

import android.annotation.TargetApi;
import android.view.View;
import android.view.ViewTreeObserver.OnDrawListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;

@TargetApi(16)
final class ViewTreeObserverDrawOnSubscribe implements OnSubscribe<Void> {
    final View view;

    ViewTreeObserverDrawOnSubscribe(View view2) {
        this.view = view2;
    }

    public void call(final Subscriber<? super Void> subscriber) {
        Preconditions.checkUiThread();
        final OnDrawListener listener = new OnDrawListener() {
            public void onDraw() {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(null);
                }
            }
        };
        this.view.getViewTreeObserver().addOnDrawListener(listener);
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                ViewTreeObserverDrawOnSubscribe.this.view.getViewTreeObserver().removeOnDrawListener(listener);
            }
        });
    }
}
