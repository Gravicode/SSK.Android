package com.google.android.gms.internal;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;

final class zzbfj extends ConstantState {
    int mChangingConfigurations;
    int zzfyc;

    zzbfj(zzbfj zzbfj) {
        if (zzbfj != null) {
            this.mChangingConfigurations = zzbfj.mChangingConfigurations;
            this.zzfyc = zzbfj.zzfyc;
        }
    }

    public final int getChangingConfigurations() {
        return this.mChangingConfigurations;
    }

    public final Drawable newDrawable() {
        return new zzbff(this);
    }
}
