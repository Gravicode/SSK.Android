package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.RatingBar;
import com.jakewharton.rxbinding.internal.Preconditions;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.functions.Action1;

public final class RxRatingBar {
    @CheckResult
    @NonNull
    public static Observable<Float> ratingChanges(@NonNull RatingBar view) {
        Preconditions.checkNotNull(view, "view == null");
        return Observable.create((OnSubscribe<T>) new RatingBarRatingChangeOnSubscribe<T>(view));
    }

    @CheckResult
    @NonNull
    public static Observable<RatingBarChangeEvent> ratingChangeEvents(@NonNull RatingBar view) {
        Preconditions.checkNotNull(view, "view == null");
        return Observable.create((OnSubscribe<T>) new RatingBarRatingChangeEventOnSubscribe<T>(view));
    }

    @CheckResult
    @NonNull
    public static Action1<? super Float> rating(@NonNull final RatingBar view) {
        Preconditions.checkNotNull(view, "view == null");
        return new Action1<Float>() {
            public void call(Float value) {
                view.setRating(value.floatValue());
            }
        };
    }

    @CheckResult
    @NonNull
    public static Action1<? super Boolean> isIndicator(@NonNull final RatingBar view) {
        Preconditions.checkNotNull(view, "view == null");
        return new Action1<Boolean>() {
            public void call(Boolean value) {
                view.setIsIndicator(value.booleanValue());
            }
        };
    }

    private RxRatingBar() {
        throw new AssertionError("No instances.");
    }
}
