package com.jakewharton.rxbinding.view;

import android.annotation.TargetApi;
import android.view.View;
import android.view.View.OnScrollChangeListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;

@TargetApi(23)
final class ViewScrollChangeEventOnSubscribe implements OnSubscribe<ViewScrollChangeEvent> {
    final View view;

    ViewScrollChangeEventOnSubscribe(View view2) {
        this.view = view2;
    }

    public void call(final Subscriber<? super ViewScrollChangeEvent> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnScrollChangeListener(new OnScrollChangeListener() {
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(ViewScrollChangeEvent.create(ViewScrollChangeEventOnSubscribe.this.view, scrollX, scrollY, oldScrollX, oldScrollY));
                }
            }
        });
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                ViewScrollChangeEventOnSubscribe.this.view.setOnScrollChangeListener(null);
            }
        });
    }
}
