package com.jakewharton.rxbinding.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;

public final class ViewScrollChangeEvent extends ViewEvent<View> {
    private final int oldScrollX;
    private final int oldScrollY;
    private final int scrollX;
    private final int scrollY;

    @CheckResult
    @NonNull
    public static ViewScrollChangeEvent create(@NonNull View view, int scrollX2, int scrollY2, int oldScrollX2, int oldScrollY2) {
        ViewScrollChangeEvent viewScrollChangeEvent = new ViewScrollChangeEvent(view, scrollX2, scrollY2, oldScrollX2, oldScrollY2);
        return viewScrollChangeEvent;
    }

    protected ViewScrollChangeEvent(@NonNull View view, int scrollX2, int scrollY2, int oldScrollX2, int oldScrollY2) {
        super(view);
        this.scrollX = scrollX2;
        this.scrollY = scrollY2;
        this.oldScrollX = oldScrollX2;
        this.oldScrollY = oldScrollY2;
    }

    public int scrollX() {
        return this.scrollX;
    }

    public int scrollY() {
        return this.scrollY;
    }

    public int oldScrollX() {
        return this.oldScrollX;
    }

    public int oldScrollY() {
        return this.oldScrollY;
    }

    public int hashCode() {
        return (((((((((17 * 37) + view().hashCode()) * 37) + this.scrollX) * 37) + this.scrollY) * 37) + this.oldScrollX) * 37) + this.oldScrollY;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof ViewScrollChangeEvent)) {
            return false;
        }
        ViewScrollChangeEvent other = (ViewScrollChangeEvent) o;
        if (!(other.view() == view() && other.scrollX == this.scrollX && other.scrollY == this.scrollY && other.oldScrollX == this.oldScrollX && other.oldScrollY == this.oldScrollY)) {
            z = false;
        }
        return z;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ViewScrollChangeEvent{scrollX=");
        sb.append(this.scrollX);
        sb.append(", scrollY=");
        sb.append(this.scrollY);
        sb.append(", oldScrollX=");
        sb.append(this.oldScrollX);
        sb.append(", oldScrollY=");
        sb.append(this.oldScrollY);
        sb.append('}');
        return sb.toString();
    }
}
