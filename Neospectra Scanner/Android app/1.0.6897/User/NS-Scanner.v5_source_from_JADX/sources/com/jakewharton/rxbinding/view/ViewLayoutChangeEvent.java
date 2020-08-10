package com.jakewharton.rxbinding.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;

public final class ViewLayoutChangeEvent extends ViewEvent<View> {
    private final int bottom;
    private final int left;
    private final int oldBottom;
    private final int oldLeft;
    private final int oldRight;
    private final int oldTop;
    private final int right;
    private final int top;

    @CheckResult
    @NonNull
    public static ViewLayoutChangeEvent create(@NonNull View view, int left2, int top2, int right2, int bottom2, int oldLeft2, int oldTop2, int oldRight2, int oldBottom2) {
        ViewLayoutChangeEvent viewLayoutChangeEvent = new ViewLayoutChangeEvent(view, left2, top2, right2, bottom2, oldLeft2, oldTop2, oldRight2, oldBottom2);
        return viewLayoutChangeEvent;
    }

    private ViewLayoutChangeEvent(@NonNull View view, int left2, int top2, int right2, int bottom2, int oldLeft2, int oldTop2, int oldRight2, int oldBottom2) {
        super(view);
        this.left = left2;
        this.top = top2;
        this.right = right2;
        this.bottom = bottom2;
        this.oldLeft = oldLeft2;
        this.oldTop = oldTop2;
        this.oldRight = oldRight2;
        this.oldBottom = oldBottom2;
    }

    public int left() {
        return this.left;
    }

    public int top() {
        return this.top;
    }

    public int right() {
        return this.right;
    }

    public int bottom() {
        return this.bottom;
    }

    public int oldLeft() {
        return this.oldLeft;
    }

    public int oldTop() {
        return this.oldTop;
    }

    public int oldRight() {
        return this.oldRight;
    }

    public int oldBottom() {
        return this.oldBottom;
    }

    public int hashCode() {
        return (((((((((((((((((17 * 37) + view().hashCode()) * 37) + this.left) * 37) + this.top) * 37) + this.right) * 37) + this.bottom) * 37) + this.oldLeft) * 37) + this.oldTop) * 37) + this.oldRight) * 37) + this.oldBottom;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof ViewLayoutChangeEvent)) {
            return false;
        }
        ViewLayoutChangeEvent other = (ViewLayoutChangeEvent) o;
        if (!(other.view() == view() && other.left == this.left && other.top == this.top && other.right == this.right && other.bottom == this.bottom && other.oldLeft == this.oldLeft && other.oldTop == this.oldTop && other.oldRight == this.oldRight && other.oldBottom == this.oldBottom)) {
            z = false;
        }
        return z;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ViewLayoutChangeEvent{left=");
        sb.append(this.left);
        sb.append(", top=");
        sb.append(this.top);
        sb.append(", right=");
        sb.append(this.right);
        sb.append(", bottom=");
        sb.append(this.bottom);
        sb.append(", oldLeft=");
        sb.append(this.oldLeft);
        sb.append(", oldTop=");
        sb.append(this.oldTop);
        sb.append(", oldRight=");
        sb.append(this.oldRight);
        sb.append(", oldBottom=");
        sb.append(this.oldBottom);
        sb.append('}');
        return sb.toString();
    }
}
