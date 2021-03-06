package com.jakewharton.rxbinding.view;

import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;
import p008rx.functions.Func1;

final class ViewDragOnSubscribe implements OnSubscribe<DragEvent> {
    final Func1<? super DragEvent, Boolean> handled;
    final View view;

    ViewDragOnSubscribe(View view2, Func1<? super DragEvent, Boolean> handled2) {
        this.view = view2;
        this.handled = handled2;
    }

    public void call(final Subscriber<? super DragEvent> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnDragListener(new OnDragListener() {
            public boolean onDrag(View v, DragEvent event) {
                if (!((Boolean) ViewDragOnSubscribe.this.handled.call(event)).booleanValue()) {
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
                ViewDragOnSubscribe.this.view.setOnDragListener(null);
            }
        });
    }
}
