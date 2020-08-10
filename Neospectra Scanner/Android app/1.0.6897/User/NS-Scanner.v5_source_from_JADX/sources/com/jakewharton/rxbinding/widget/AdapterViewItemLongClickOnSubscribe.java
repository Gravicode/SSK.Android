package com.jakewharton.rxbinding.widget;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;
import p008rx.functions.Func0;

final class AdapterViewItemLongClickOnSubscribe implements OnSubscribe<Integer> {
    final Func0<Boolean> handled;
    final AdapterView<?> view;

    public AdapterViewItemLongClickOnSubscribe(AdapterView<?> view2, Func0<Boolean> handled2) {
        this.view = view2;
        this.handled = handled2;
    }

    public void call(final Subscriber<? super Integer> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (!((Boolean) AdapterViewItemLongClickOnSubscribe.this.handled.call()).booleanValue()) {
                    return false;
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(Integer.valueOf(position));
                }
                return true;
            }
        });
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                AdapterViewItemLongClickOnSubscribe.this.view.setOnItemLongClickListener(null);
            }
        });
    }
}
