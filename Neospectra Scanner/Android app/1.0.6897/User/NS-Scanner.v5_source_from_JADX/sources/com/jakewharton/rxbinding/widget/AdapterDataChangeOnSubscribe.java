package com.jakewharton.rxbinding.widget;

import android.database.DataSetObserver;
import android.widget.Adapter;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;

final class AdapterDataChangeOnSubscribe<T extends Adapter> implements OnSubscribe<T> {
    final T adapter;

    public AdapterDataChangeOnSubscribe(T adapter2) {
        this.adapter = adapter2;
    }

    public void call(final Subscriber<? super T> subscriber) {
        Preconditions.checkUiThread();
        final DataSetObserver observer = new DataSetObserver() {
            public void onChanged() {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(AdapterDataChangeOnSubscribe.this.adapter);
                }
            }
        };
        this.adapter.registerDataSetObserver(observer);
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                AdapterDataChangeOnSubscribe.this.adapter.unregisterDataSetObserver(observer);
            }
        });
        subscriber.onNext(this.adapter);
    }
}
