package com.jakewharton.rxbinding.view;

import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;
import p008rx.functions.Func0;

final class ViewTreeObserverPreDrawOnSubscribe implements OnSubscribe<Void> {
    final Func0<Boolean> proceedDrawingPass;
    final View view;

    ViewTreeObserverPreDrawOnSubscribe(View view2, Func0<Boolean> proceedDrawingPass2) {
        this.view = view2;
        this.proceedDrawingPass = proceedDrawingPass2;
    }

    public void call(final Subscriber<? super Void> subscriber) {
        Preconditions.checkUiThread();
        final OnPreDrawListener listener = new OnPreDrawListener() {
            public boolean onPreDraw() {
                if (subscriber.isUnsubscribed()) {
                    return true;
                }
                subscriber.onNext(null);
                return ((Boolean) ViewTreeObserverPreDrawOnSubscribe.this.proceedDrawingPass.call()).booleanValue();
            }
        };
        this.view.getViewTreeObserver().addOnPreDrawListener(listener);
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                ViewTreeObserverPreDrawOnSubscribe.this.view.getViewTreeObserver().removeOnPreDrawListener(listener);
            }
        });
    }
}
