package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import com.jakewharton.rxbinding.view.ViewEvent;

public final class AdapterViewItemClickEvent extends ViewEvent<AdapterView<?>> {
    private final View clickedView;

    /* renamed from: id */
    private final long f59id;
    private final int position;

    @CheckResult
    @NonNull
    public static AdapterViewItemClickEvent create(@NonNull AdapterView<?> view, @NonNull View clickedView2, int position2, long id) {
        AdapterViewItemClickEvent adapterViewItemClickEvent = new AdapterViewItemClickEvent(view, clickedView2, position2, id);
        return adapterViewItemClickEvent;
    }

    private AdapterViewItemClickEvent(@NonNull AdapterView<?> view, @NonNull View clickedView2, int position2, long id) {
        super(view);
        this.clickedView = clickedView2;
        this.position = position2;
        this.f59id = id;
    }

    @NonNull
    public View clickedView() {
        return this.clickedView;
    }

    public int position() {
        return this.position;
    }

    /* renamed from: id */
    public long mo12861id() {
        return this.f59id;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof AdapterViewItemClickEvent)) {
            return false;
        }
        AdapterViewItemClickEvent other = (AdapterViewItemClickEvent) o;
        if (!(other.view() == view() && other.clickedView == this.clickedView && other.position == this.position && other.f59id == this.f59id)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((((((17 * 37) + ((AdapterView) view()).hashCode()) * 37) + this.clickedView.hashCode()) * 37) + this.position) * 37) + ((int) (this.f59id ^ (this.f59id >>> 32)));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AdapterViewItemClickEvent{view=");
        sb.append(view());
        sb.append(", clickedView=");
        sb.append(this.clickedView);
        sb.append(", position=");
        sb.append(this.position);
        sb.append(", id=");
        sb.append(this.f59id);
        sb.append('}');
        return sb.toString();
    }
}
