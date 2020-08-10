package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import com.jakewharton.rxbinding.view.ViewEvent;

public final class AdapterViewItemLongClickEvent extends ViewEvent<AdapterView<?>> {
    private final View clickedView;

    /* renamed from: id */
    private final long f60id;
    private final int position;

    @CheckResult
    @NonNull
    public static AdapterViewItemLongClickEvent create(@NonNull AdapterView<?> view, @NonNull View clickedView2, int position2, long id) {
        AdapterViewItemLongClickEvent adapterViewItemLongClickEvent = new AdapterViewItemLongClickEvent(view, clickedView2, position2, id);
        return adapterViewItemLongClickEvent;
    }

    private AdapterViewItemLongClickEvent(@NonNull AdapterView<?> view, @NonNull View clickedView2, int position2, long id) {
        super(view);
        this.clickedView = clickedView2;
        this.position = position2;
        this.f60id = id;
    }

    @NonNull
    public View clickedView() {
        return this.clickedView;
    }

    public int position() {
        return this.position;
    }

    /* renamed from: id */
    public long mo12871id() {
        return this.f60id;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof AdapterViewItemLongClickEvent)) {
            return false;
        }
        AdapterViewItemLongClickEvent other = (AdapterViewItemLongClickEvent) o;
        if (!(other.view() == view() && other.clickedView == this.clickedView && other.position == this.position && other.f60id == this.f60id)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((((((17 * 37) + ((AdapterView) view()).hashCode()) * 37) + this.clickedView.hashCode()) * 37) + this.position) * 37) + ((int) (this.f60id ^ (this.f60id >>> 32)));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AdapterViewItemLongClickEvent{view=");
        sb.append(view());
        sb.append(", clickedView=");
        sb.append(this.clickedView);
        sb.append(", position=");
        sb.append(this.position);
        sb.append(", id=");
        sb.append(this.f60id);
        sb.append('}');
        return sb.toString();
    }
}
