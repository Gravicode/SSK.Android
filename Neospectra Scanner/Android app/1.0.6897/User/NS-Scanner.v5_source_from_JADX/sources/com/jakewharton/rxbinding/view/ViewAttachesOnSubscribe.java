package com.jakewharton.rxbinding.view;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;

final class ViewAttachesOnSubscribe implements OnSubscribe<Void> {
    final boolean callOnAttach;
    final View view;

    ViewAttachesOnSubscribe(View view2, boolean callOnAttach2) {
        this.view = view2;
        this.callOnAttach = callOnAttach2;
    }

    public void call(final Subscriber<? super Void> subscriber) {
        Preconditions.checkUiThread();
        final OnAttachStateChangeListener listener = new OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(@NonNull View v) {
                if (ViewAttachesOnSubscribe.this.callOnAttach && !subscriber.isUnsubscribed()) {
                    subscriber.onNext(null);
                }
            }

            public void onViewDetachedFromWindow(@NonNull View v) {
                if (!ViewAttachesOnSubscribe.this.callOnAttach && !subscriber.isUnsubscribed()) {
                    subscriber.onNext(null);
                }
            }
        };
        this.view.addOnAttachStateChangeListener(listener);
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                ViewAttachesOnSubscribe.this.view.removeOnAttachStateChangeListener(listener);
            }
        });
    }
}
