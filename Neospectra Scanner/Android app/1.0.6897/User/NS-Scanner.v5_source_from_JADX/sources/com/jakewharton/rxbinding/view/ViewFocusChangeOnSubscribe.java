package com.jakewharton.rxbinding.view;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;

final class ViewFocusChangeOnSubscribe implements OnSubscribe<Boolean> {
    final View view;

    ViewFocusChangeOnSubscribe(View view2) {
        this.view = view2;
    }

    public void call(final Subscriber<? super Boolean> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(Boolean.valueOf(hasFocus));
                }
            }
        });
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                ViewFocusChangeOnSubscribe.this.view.setOnFocusChangeListener(null);
            }
        });
        subscriber.onNext(Boolean.valueOf(this.view.hasFocus()));
    }
}
