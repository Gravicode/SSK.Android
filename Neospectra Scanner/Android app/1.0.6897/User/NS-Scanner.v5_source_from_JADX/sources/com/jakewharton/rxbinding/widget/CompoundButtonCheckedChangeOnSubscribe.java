package com.jakewharton.rxbinding.widget;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;

final class CompoundButtonCheckedChangeOnSubscribe implements OnSubscribe<Boolean> {
    final CompoundButton view;

    public CompoundButtonCheckedChangeOnSubscribe(CompoundButton view2) {
        this.view = view2;
    }

    public void call(final Subscriber<? super Boolean> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(Boolean.valueOf(isChecked));
                }
            }
        });
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                CompoundButtonCheckedChangeOnSubscribe.this.view.setOnCheckedChangeListener(null);
            }
        });
        subscriber.onNext(Boolean.valueOf(this.view.isChecked()));
    }
}
