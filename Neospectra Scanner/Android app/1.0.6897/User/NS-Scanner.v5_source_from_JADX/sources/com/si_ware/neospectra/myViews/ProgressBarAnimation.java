package com.si_ware.neospectra.myViews;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

public class ProgressBarAnimation extends Animation {
    private float from;
    private ProgressBar progressBar;

    /* renamed from: to */
    private float f836to;

    public ProgressBarAnimation(ProgressBar progressBar2, float from2, float to) {
        this.progressBar = progressBar2;
        this.from = from2;
        this.f836to = to;
    }

    /* access modifiers changed from: protected */
    public void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        this.progressBar.setProgress((int) (this.from + ((this.f836to - this.from) * interpolatedTime)));
    }
}
