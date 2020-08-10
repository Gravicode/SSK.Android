package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.TextView;
import com.jakewharton.rxbinding.view.ViewEvent;

public final class TextViewTextChangeEvent extends ViewEvent<TextView> {
    private final int before;
    private final int count;
    private final int start;
    private final CharSequence text;

    @CheckResult
    @NonNull
    public static TextViewTextChangeEvent create(@NonNull TextView view, @NonNull CharSequence text2, int start2, int before2, int count2) {
        TextViewTextChangeEvent textViewTextChangeEvent = new TextViewTextChangeEvent(view, text2, start2, before2, count2);
        return textViewTextChangeEvent;
    }

    private TextViewTextChangeEvent(@NonNull TextView view, @NonNull CharSequence text2, int start2, int before2, int count2) {
        super(view);
        this.text = text2;
        this.start = start2;
        this.before = before2;
        this.count = count2;
    }

    @NonNull
    public CharSequence text() {
        return this.text;
    }

    public int start() {
        return this.start;
    }

    public int before() {
        return this.before;
    }

    public int count() {
        return this.count;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof TextViewTextChangeEvent)) {
            return false;
        }
        TextViewTextChangeEvent other = (TextViewTextChangeEvent) o;
        if (!(other.view() == view() && this.text.equals(other.text) && this.start == other.start && this.before == other.before && this.count == other.count)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((((((((17 * 37) + ((TextView) view()).hashCode()) * 37) + this.text.hashCode()) * 37) + this.start) * 37) + this.before) * 37) + this.count;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TextViewTextChangeEvent{text=");
        sb.append(this.text);
        sb.append(", start=");
        sb.append(this.start);
        sb.append(", before=");
        sb.append(this.before);
        sb.append(", count=");
        sb.append(this.count);
        sb.append(", view=");
        sb.append(view());
        sb.append('}');
        return sb.toString();
    }
}
