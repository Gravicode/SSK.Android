package com.jakewharton.rxbinding.widget;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;

final class AdapterViewItemClickOnSubscribe implements OnSubscribe<Integer> {
    final AdapterView<?> view;

    public AdapterViewItemClickOnSubscribe(AdapterView<?> view2) {
        this.view = view2;
    }

    public void call(final Subscriber<? super Integer> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(Integer.valueOf(position));
                }
            }
        });
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                AdapterViewItemClickOnSubscribe.this.view.setOnItemClickListener(null);
            }
        });
    }
}
