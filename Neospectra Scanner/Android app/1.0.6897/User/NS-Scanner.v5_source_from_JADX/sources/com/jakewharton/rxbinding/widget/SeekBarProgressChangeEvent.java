package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.SeekBar;

public final class SeekBarProgressChangeEvent extends SeekBarChangeEvent {
    private final boolean fromUser;
    private final int progress;

    @CheckResult
    @NonNull
    public static SeekBarProgressChangeEvent create(@NonNull SeekBar view, int progress2, boolean fromUser2) {
        return new SeekBarProgressChangeEvent(view, progress2, fromUser2);
    }

    private SeekBarProgressChangeEvent(@NonNull SeekBar view, int progress2, boolean fromUser2) {
        super(view);
        this.progress = progress2;
        this.fromUser = fromUser2;
    }

    public int progress() {
        return this.progress;
    }

    public boolean fromUser() {
        return this.fromUser;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof SeekBarProgressChangeEvent)) {
            return false;
        }
        SeekBarProgressChangeEvent other = (SeekBarProgressChangeEvent) o;
        if (!(other.view() == view() && other.progress == this.progress && other.fromUser == this.fromUser)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((((17 * 37) + ((SeekBar) view()).hashCode()) * 37) + this.progress) * 37) + (this.fromUser ? 1 : 0);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SeekBarProgressChangeEvent{view=");
        sb.append(view());
        sb.append(", progress=");
        sb.append(this.progress);
        sb.append(", fromUser=");
        sb.append(this.fromUser);
        sb.append('}');
        return sb.toString();
    }
}
