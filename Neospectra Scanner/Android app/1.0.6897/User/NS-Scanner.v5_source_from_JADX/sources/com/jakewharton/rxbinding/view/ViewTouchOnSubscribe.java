package com.jakewharton.rxbinding.view;

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;
import p008rx.functions.Func1;

final class ViewTouchOnSubscribe implements OnSubscribe<MotionEvent> {
    final Func1<? super MotionEvent, Boolean> handled;
    final View view;

    ViewTouchOnSubscribe(View view2, Func1<? super MotionEvent, Boolean> handled2) {
        this.view = view2;
        this.handled = handled2;
    }

    public void call(final Subscriber<? super MotionEvent> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, @NonNull MotionEvent event) {
                if (!((Boolean) ViewTouchOnSubscribe.this.handled.call(event)).booleanValue()) {
                    return false;
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(event);
                }
                return true;
            }
        });
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                ViewTouchOnSubscribe.this.view.setOnTouchListener(null);
            }
        });
    }
}
