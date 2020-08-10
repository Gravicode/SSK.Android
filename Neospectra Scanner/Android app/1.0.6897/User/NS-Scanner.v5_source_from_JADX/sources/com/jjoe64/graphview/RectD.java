package com.jjoe64.graphview;

import android.graphics.RectF;

public class RectD {
    public double bottom;
    public double left;
    public double right;
    public double top;

    public RectD() {
    }

    public RectD(double lLeft, double lTop, double lRight, double lBottom) {
        set(lLeft, lTop, lRight, lBottom);
    }

    public double width() {
        return this.right - this.left;
    }

    public double height() {
        return this.bottom - this.top;
    }

    public void set(double lLeft, double lTop, double lRight, double lBottom) {
        this.left = lLeft;
        this.right = lRight;
        this.top = lTop;
        this.bottom = lBottom;
    }

    public RectF toRectF() {
        return new RectF((float) this.left, (float) this.top, (float) this.right, (float) this.bottom);
    }
}
