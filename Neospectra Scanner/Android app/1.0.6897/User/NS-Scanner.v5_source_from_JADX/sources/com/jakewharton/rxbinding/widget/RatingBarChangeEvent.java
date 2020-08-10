package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.RatingBar;
import com.jakewharton.rxbinding.view.ViewEvent;

public final class RatingBarChangeEvent extends ViewEvent<RatingBar> {
    private final boolean fromUser;
    private final float rating;

    @CheckResult
    @NonNull
    public static RatingBarChangeEvent create(@NonNull RatingBar view, float rating2, boolean fromUser2) {
        return new RatingBarChangeEvent(view, rating2, fromUser2);
    }

    private RatingBarChangeEvent(@NonNull RatingBar view, float rating2, boolean fromUser2) {
        super(view);
        this.rating = rating2;
        this.fromUser = fromUser2;
    }

    public float rating() {
        return this.rating;
    }

    public boolean fromUser() {
        return this.fromUser;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof RatingBarChangeEvent)) {
            return false;
        }
        RatingBarChangeEvent other = (RatingBarChangeEvent) o;
        if (!(other.view() == view() && other.rating == this.rating && other.fromUser == this.fromUser)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((((17 * 37) + ((RatingBar) view()).hashCode()) * 37) + Float.floatToIntBits(this.rating)) * 37) + (this.fromUser ? 1 : 0);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RatingBarChangeEvent{view=");
        sb.append(view());
        sb.append(", rating=");
        sb.append(this.rating);
        sb.append(", fromUser=");
        sb.append(this.fromUser);
        sb.append('}');
        return sb.toString();
    }
}
