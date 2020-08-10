package com.jakewharton.rxbinding.widget;

import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.android.MainThreadSubscription;

final class RatingBarRatingChangeOnSubscribe implements OnSubscribe<Float> {
    final RatingBar view;

    public RatingBarRatingChangeOnSubscribe(RatingBar view2) {
        this.view = view2;
    }

    public void call(final Subscriber<? super Float> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(Float.valueOf(rating));
                }
            }
        });
        subscriber.add(new MainThreadSubscription() {
            /* access modifiers changed from: protected */
            public void onUnsubscribe() {
                RatingBarRatingChangeOnSubscribe.this.view.setOnRatingBarChangeListener(null);
            }
        });
        subscriber.onNext(Float.valueOf(this.view.getRating()));
    }
}
