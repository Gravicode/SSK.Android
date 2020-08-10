package com.jakewharton.rxbinding.view;

import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;

final class ViewSystemUiVisibilityChangeOnSubscribe implements OnSubscribe<Integer> {
    final View view;

    ViewSystemUiVisibilityChangeOnSubscribe(View view2) {
        this.view = view2;
    }

    public void call(final Subscriber<? super Integer> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener() {
            public void onSystemUiVisibilityChange(int visibility) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(Integer.valueOf(visibility));
                }
            }
        });
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                ViewSystemUiVisibilityChangeOnSubscribe.this.view.setOnSystemUiVisibilityChangeListener(null);
            }
        });
    }
}
