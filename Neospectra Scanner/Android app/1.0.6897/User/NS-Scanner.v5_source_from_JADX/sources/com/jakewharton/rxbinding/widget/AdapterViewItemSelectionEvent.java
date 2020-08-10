package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;

public final class AdapterViewItemSelectionEvent extends AdapterViewSelectionEvent {

    /* renamed from: id */
    private final long f61id;
    private final int position;
    private final View selectedView;

    @CheckResult
    @NonNull
    public static AdapterViewSelectionEvent create(@NonNull AdapterView<?> view, @NonNull View selectedView2, int position2, long id) {
        AdapterViewItemSelectionEvent adapterViewItemSelectionEvent = new AdapterViewItemSelectionEvent(view, selectedView2, position2, id);
        return adapterViewItemSelectionEvent;
    }

    private AdapterViewItemSelectionEvent(@NonNull AdapterView<?> view, @NonNull View selectedView2, int position2, long id) {
        super(view);
        this.selectedView = selectedView2;
        this.position = position2;
        this.f61id = id;
    }

    @NonNull
    public View selectedView() {
        return this.selectedView;
    }

    public int position() {
        return this.position;
    }

    /* renamed from: id */
    public long mo12880id() {
        return this.f61id;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof AdapterViewItemSelectionEvent)) {
            return false;
        }
        AdapterViewItemSelectionEvent other = (AdapterViewItemSelectionEvent) o;
        if (!(other.view() == view() && other.selectedView == this.selectedView && other.position == this.position && other.f61id == this.f61id)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((((((17 * 37) + ((AdapterView) view()).hashCode()) * 37) + this.selectedView.hashCode()) * 37) + this.position) * 37) + ((int) (this.f61id ^ (this.f61id >>> 32)));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AdapterViewItemSelectionEvent{view=");
        sb.append(view());
        sb.append(", selectedView=");
        sb.append(this.selectedView);
        sb.append(", position=");
        sb.append(this.position);
        sb.append(", id=");
        sb.append(this.f61id);
        sb.append('}');
        return sb.toString();
    }
}
