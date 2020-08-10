package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.TextView;
import com.jakewharton.rxbinding.view.ViewEvent;

public final class TextViewBeforeTextChangeEvent extends ViewEvent<TextView> {
    private final int after;
    private final int count;
    private final int start;
    private final CharSequence text;

    @CheckResult
    @NonNull
    public static TextViewBeforeTextChangeEvent create(@NonNull TextView view, @NonNull CharSequence text2, int start2, int count2, int after2) {
        TextViewBeforeTextChangeEvent textViewBeforeTextChangeEvent = new TextViewBeforeTextChangeEvent(view, text2, start2, count2, after2);
        return textViewBeforeTextChangeEvent;
    }

    private TextViewBeforeTextChangeEvent(@NonNull TextView view, @NonNull CharSequence text2, int start2, int count2, int after2) {
        super(view);
        this.text = text2;
        this.start = start2;
        this.count = count2;
        this.after = after2;
    }

    @NonNull
    public CharSequence text() {
        return this.text;
    }

    public int start() {
        return this.start;
    }

    public int count() {
        return this.count;
    }

    public int after() {
        return this.after;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof TextViewAfterTextChangeEvent)) {
            return false;
        }
        TextViewBeforeTextChangeEvent other = (TextViewBeforeTextChangeEvent) o;
        if (!(other.view() == view() && this.text.equals(other.text) && this.start == other.start && this.count == other.count && this.after == other.after)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((((((((17 * 37) + ((TextView) view()).hashCode()) * 37) + this.text.hashCode()) * 37) + this.start) * 37) + this.count) * 37) + this.after;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TextViewBeforeTextChangeEvent{text=");
        sb.append(this.text);
        sb.append(", start=");
        sb.append(this.start);
        sb.append(", count=");
        sb.append(this.count);
        sb.append(", after=");
        sb.append(this.after);
        sb.append(", view=");
        sb.append(view());
        sb.append('}');
        return sb.toString();
    }
}
