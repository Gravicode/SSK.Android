package com.jakewharton.rxbinding.widget;

import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;

final class RatingBarRatingChangeEventOnSubscribe implements OnSubscribe<RatingBarChangeEvent> {
    final RatingBar view;

    public RatingBarRatingChangeEventOnSubscribe(RatingBar view2) {
        this.view = view2;
    }

    public void call(final Subscriber<? super RatingBarChangeEvent> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(RatingBarChangeEvent.create(ratingBar, rating, fromUser));
                }
            }
        });
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                RatingBarRatingChangeEventOnSubscribe.this.view.setOnRatingBarChangeListener(null);
            }
        });
        subscriber.onNext(RatingBarChangeEvent.create(this.view, this.view.getRating(), false));
    }
}
