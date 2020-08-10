package com.jakewharton.rxbinding.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;

public final class ViewAttachEvent extends ViewEvent<View> {
    private final Kind kind;

    public enum Kind {
        ATTACH,
        DETACH
    }

    @CheckResult
    @NonNull
    public static ViewAttachEvent create(@NonNull View view, @NonNull Kind kind2) {
        return new ViewAttachEvent(view, kind2);
    }

    private ViewAttachEvent(@NonNull View view, @NonNull Kind kind2) {
        super(view);
        this.kind = kind2;
    }

    @NonNull
    public Kind kind() {
        return this.kind;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof ViewAttachEvent)) {
            return false;
        }
        ViewAttachEvent other = (ViewAttachEvent) o;
        if (!(other.view() == view() && other.kind() == kind())) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((17 * 37) + view().hashCode()) * 37) + kind().hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ViewAttachEvent{view=");
        sb.append(view());
        sb.append(", kind=");
        sb.append(kind());
        sb.append('}');
        return sb.toString();
    }
}
