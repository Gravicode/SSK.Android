package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.AdapterView;

public final class AdapterViewNothingSelectionEvent extends AdapterViewSelectionEvent {
    @CheckResult
    @NonNull
    public static AdapterViewSelectionEvent create(@NonNull AdapterView<?> view) {
        return new AdapterViewNothingSelectionEvent(view);
    }

    private AdapterViewNothingSelectionEvent(@NonNull AdapterView<?> view) {
        super(view);
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof AdapterViewNothingSelectionEvent)) {
            return false;
        }
        if (((AdapterViewNothingSelectionEvent) o).view() != view()) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return ((AdapterView) view()).hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AdapterViewNothingSelectionEvent{view=");
        sb.append(view());
        sb.append('}');
        return sb.toString();
    }
}
