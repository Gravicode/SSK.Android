package com.google.android.gms.common.images;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.p004v7.widget.helper.ItemTouchHelper.Callback;
import android.widget.ImageView;
import com.google.android.gms.common.internal.zzbg;
import com.google.android.gms.internal.zzbff;
import com.google.android.gms.internal.zzbfk;
import java.lang.ref.WeakReference;

public final class zzc extends zza {
    private WeakReference<ImageView> zzfxm;

    public zzc(ImageView imageView, int i) {
        super(null, i);
        com.google.android.gms.common.internal.zzc.zzv(imageView);
        this.zzfxm = new WeakReference<>(imageView);
    }

    public zzc(ImageView imageView, Uri uri) {
        super(uri, 0);
        com.google.android.gms.common.internal.zzc.zzv(imageView);
        this.zzfxm = new WeakReference<>(imageView);
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof zzc)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        ImageView imageView = (ImageView) this.zzfxm.get();
        ImageView imageView2 = (ImageView) ((zzc) obj).zzfxm.get();
        return (imageView2 == null || imageView == null || !zzbg.equal(imageView2, imageView)) ? false : true;
    }

    public final int hashCode() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public final void zza(Drawable drawable, boolean z, boolean z2, boolean z3) {
        ImageView imageView = (ImageView) this.zzfxm.get();
        if (imageView != null) {
            int i = 0;
            boolean z4 = !z2 && !z3;
            if (z4 && (imageView instanceof zzbfk)) {
                int zzakg = zzbfk.zzakg();
                if (this.zzfxh != 0 && zzakg == this.zzfxh) {
                    return;
                }
            }
            boolean zzc = zzc(z, z2);
            Uri uri = null;
            if (zzc) {
                Drawable drawable2 = imageView.getDrawable();
                if (drawable2 == null) {
                    drawable2 = null;
                } else if (drawable2 instanceof zzbff) {
                    drawable2 = ((zzbff) drawable2).zzake();
                }
                drawable = new zzbff(drawable2, drawable);
            }
            imageView.setImageDrawable(drawable);
            if (imageView instanceof zzbfk) {
                if (z3) {
                    uri = this.zzfxf.uri;
                }
                zzbfk.zzn(uri);
                if (z4) {
                    i = this.zzfxh;
                }
                zzbfk.zzcd(i);
            }
            if (zzc) {
                ((zzbff) drawable).startTransition(Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
            }
        }
    }
}
