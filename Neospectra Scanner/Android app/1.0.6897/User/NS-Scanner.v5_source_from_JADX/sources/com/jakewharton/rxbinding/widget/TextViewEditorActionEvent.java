package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.widget.TextView;
import com.jakewharton.rxbinding.view.ViewEvent;

public final class TextViewEditorActionEvent extends ViewEvent<TextView> {
    private final int actionId;
    private final KeyEvent keyEvent;

    @CheckResult
    @NonNull
    public static TextViewEditorActionEvent create(@NonNull TextView view, int actionId2, @NonNull KeyEvent keyEvent2) {
        return new TextViewEditorActionEvent(view, actionId2, keyEvent2);
    }

    private TextViewEditorActionEvent(@NonNull TextView view, int actionId2, @NonNull KeyEvent keyEvent2) {
        super(view);
        this.actionId = actionId2;
        this.keyEvent = keyEvent2;
    }

    public int actionId() {
        return this.actionId;
    }

    @NonNull
    public KeyEvent keyEvent() {
        return this.keyEvent;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof TextViewEditorActionEvent)) {
            return false;
        }
        TextViewEditorActionEvent other = (TextViewEditorActionEvent) o;
        if (!(other.view() == view() && other.actionId == this.actionId && other.keyEvent.equals(this.keyEvent))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((((17 * 37) + ((TextView) view()).hashCode()) * 37) + this.actionId) * 37) + this.keyEvent.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TextViewEditorActionEvent{view=");
        sb.append(view());
        sb.append(", actionId=");
        sb.append(this.actionId);
        sb.append(", keyEvent=");
        sb.append(this.keyEvent);
        sb.append('}');
        return sb.toString();
    }
}
