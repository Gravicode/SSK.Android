package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.p001v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.widget.Button;
import com.google.android.gms.C1133R;
import com.google.android.gms.common.util.zzi;

public final class zzby extends Button {
    public zzby(Context context) {
        this(context, null);
    }

    private zzby(Context context, AttributeSet attributeSet) {
        super(context, null, 16842824);
    }

    private static int zzf(int i, int i2, int i3, int i4) {
        switch (i) {
            case 0:
                return i2;
            case 1:
                return i3;
            case 2:
                return i4;
            default:
                StringBuilder sb = new StringBuilder(33);
                sb.append("Unknown color scheme: ");
                sb.append(i);
                throw new IllegalStateException(sb.toString());
        }
    }

    public final void zza(Resources resources, int i, int i2) {
        int i3;
        setTypeface(Typeface.DEFAULT_BOLD);
        setTextSize(14.0f);
        int i4 = (int) ((resources.getDisplayMetrics().density * 48.0f) + 0.5f);
        setMinHeight(i4);
        setMinWidth(i4);
        int zzf = zzf(i2, C1133R.C1134drawable.common_google_signin_btn_icon_dark, C1133R.C1134drawable.common_google_signin_btn_icon_light, C1133R.C1134drawable.common_google_signin_btn_icon_light);
        int zzf2 = zzf(i2, C1133R.C1134drawable.common_google_signin_btn_text_dark, C1133R.C1134drawable.common_google_signin_btn_text_light, C1133R.C1134drawable.common_google_signin_btn_text_light);
        switch (i) {
            case 0:
            case 1:
                zzf = zzf2;
                break;
            case 2:
                break;
            default:
                StringBuilder sb = new StringBuilder(32);
                sb.append("Unknown button size: ");
                sb.append(i);
                throw new IllegalStateException(sb.toString());
        }
        Drawable wrap = DrawableCompat.wrap(resources.getDrawable(zzf));
        DrawableCompat.setTintList(wrap, resources.getColorStateList(C1133R.color.common_google_signin_btn_tint));
        DrawableCompat.setTintMode(wrap, Mode.SRC_ATOP);
        setBackgroundDrawable(wrap);
        setTextColor((ColorStateList) zzbq.checkNotNull(resources.getColorStateList(zzf(i2, C1133R.color.common_google_signin_btn_text_dark, C1133R.color.common_google_signin_btn_text_light, C1133R.color.common_google_signin_btn_text_light))));
        switch (i) {
            case 0:
                i3 = C1133R.string.common_signin_button_text;
                break;
            case 1:
                i3 = C1133R.string.common_signin_button_text_long;
                break;
            case 2:
                setText(null);
                break;
            default:
                StringBuilder sb2 = new StringBuilder(32);
                sb2.append("Unknown button size: ");
                sb2.append(i);
                throw new IllegalStateException(sb2.toString());
        }
        setText(resources.getString(i3));
        setTransformationMethod(null);
        if (zzi.zzcs(getContext())) {
            setGravity(19);
        }
    }
}
