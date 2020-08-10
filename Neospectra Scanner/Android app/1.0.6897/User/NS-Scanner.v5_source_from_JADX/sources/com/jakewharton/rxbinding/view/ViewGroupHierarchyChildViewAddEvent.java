package com.jakewharton.rxbinding.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

public final class ViewGroupHierarchyChildViewAddEvent extends ViewGroupHierarchyChangeEvent {
    @CheckResult
    @NonNull
    public static ViewGroupHierarchyChildViewAddEvent create(@NonNull ViewGroup viewGroup, View child) {
        return new ViewGroupHierarchyChildViewAddEvent(viewGroup, child);
    }

    private ViewGroupHierarchyChildViewAddEvent(@NonNull ViewGroup viewGroup, View child) {
        super(viewGroup, child);
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof ViewGroupHierarchyChildViewAddEvent)) {
            return false;
        }
        ViewGroupHierarchyChildViewAddEvent other = (ViewGroupHierarchyChildViewAddEvent) o;
        if (!(other.view() == view() && other.child() == child())) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((17 * 37) + ((ViewGroup) view()).hashCode()) * 37) + child().hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ViewGroupHierarchyChildViewAddEvent{view=");
        sb.append(view());
        sb.append(", child=");
        sb.append(child());
        sb.append('}');
        return sb.toString();
    }
}
