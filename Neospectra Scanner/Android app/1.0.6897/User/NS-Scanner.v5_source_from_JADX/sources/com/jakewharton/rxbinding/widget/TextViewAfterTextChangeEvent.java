package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.widget.TextView;
import com.jakewharton.rxbinding.view.ViewEvent;

public final class TextViewAfterTextChangeEvent extends ViewEvent<TextView> {
    private final Editable editable;

    @CheckResult
    @NonNull
    public static TextViewAfterTextChangeEvent create(@NonNull TextView view, @NonNull Editable editable2) {
        return new TextViewAfterTextChangeEvent(view, editable2);
    }

    private TextViewAfterTextChangeEvent(@NonNull TextView view, @NonNull Editable editable2) {
        super(view);
        this.editable = editable2;
    }

    @NonNull
    public Editable editable() {
        return this.editable;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof TextViewAfterTextChangeEvent)) {
            return false;
        }
        TextViewAfterTextChangeEvent other = (TextViewAfterTextChangeEvent) o;
        if (other.view() != view() || !this.editable.equals(other.editable)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((17 * 37) + ((TextView) view()).hashCode()) * 37) + this.editable.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TextViewAfterTextChangeEvent{editable=");
        sb.append(this.editable);
        sb.append(", view=");
        sb.append(view());
        sb.append('}');
        return sb.toString();
    }
}
