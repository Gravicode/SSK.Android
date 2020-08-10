package com.jakewharton.rxbinding.widget;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;
import p008rx.functions.Func1;

final class TextViewEditorActionOnSubscribe implements OnSubscribe<Integer> {
    final Func1<? super Integer, Boolean> handled;
    final TextView view;

    TextViewEditorActionOnSubscribe(TextView view2, Func1<? super Integer, Boolean> handled2) {
        this.view = view2;
        this.handled = handled2;
    }

    public void call(final Subscriber<? super Integer> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!((Boolean) TextViewEditorActionOnSubscribe.this.handled.call(Integer.valueOf(actionId))).booleanValue()) {
                    return false;
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(Integer.valueOf(actionId));
                }
                return true;
            }
        });
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                TextViewEditorActionOnSubscribe.this.view.setOnEditorActionListener(null);
            }
        });
    }
}
