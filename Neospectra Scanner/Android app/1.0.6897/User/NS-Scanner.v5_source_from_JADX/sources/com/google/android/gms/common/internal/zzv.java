package com.google.android.gms.common.internal;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.p001v4.app.Fragment;
import android.util.Log;
import com.google.android.gms.common.api.internal.zzcf;

public abstract class zzv implements OnClickListener {
    public static zzv zza(Activity activity, Intent intent, int i) {
        return new zzw(intent, activity, i);
    }

    public static zzv zza(@NonNull Fragment fragment, Intent intent, int i) {
        return new zzx(intent, fragment, i);
    }

    public static zzv zza(@NonNull zzcf zzcf, Intent intent, int i) {
        return new zzy(intent, zzcf, 2);
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        try {
            zzale();
        } catch (ActivityNotFoundException e) {
            Log.e("DialogRedirect", "Failed to start resolution intent", e);
        } finally {
            dialogInterface.dismiss();
        }
    }

    /* access modifiers changed from: protected */
    public abstract void zzale();
}
