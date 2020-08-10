package com.google.android.gms.internal;

import android.content.BroadcastReceiver.PendingResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.MainThread;
import com.google.android.gms.common.internal.zzbq;

public final class zzcid {
    private final zzcif zzjds;

    public zzcid(zzcif zzcif) {
        zzbq.checkNotNull(zzcif);
        this.zzjds = zzcif;
    }

    public static boolean zzbk(Context context) {
        zzbq.checkNotNull(context);
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                return false;
            }
            ActivityInfo receiverInfo = packageManager.getReceiverInfo(new ComponentName(context, "com.google.android.gms.measurement.AppMeasurementReceiver"), 2);
            if (receiverInfo != null && receiverInfo.enabled) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
        }
    }

    @MainThread
    public final void onReceive(Context context, Intent intent) {
        zzcim zzdx = zzcim.zzdx(context);
        zzchm zzawy = zzdx.zzawy();
        if (intent == null) {
            zzawy.zzazf().log("Receiver called with null intent");
            return;
        }
        String action = intent.getAction();
        zzawy.zzazj().zzj("Local receiver got", action);
        if ("com.google.android.gms.measurement.UPLOAD".equals(action)) {
            Intent className = new Intent().setClassName(context, "com.google.android.gms.measurement.AppMeasurementService");
            className.setAction("com.google.android.gms.measurement.UPLOAD");
            zzawy.zzazj().log("Starting wakeful intent.");
            this.zzjds.doStartService(context, className);
            return;
        }
        if ("com.android.vending.INSTALL_REFERRER".equals(action)) {
            PendingResult doGoAsync = this.zzjds.doGoAsync();
            String stringExtra = intent.getStringExtra("referrer");
            if (stringExtra == null) {
                zzawy.zzazj().log("Install referrer extras are null");
                if (doGoAsync != null) {
                    doGoAsync.finish();
                }
                return;
            }
            zzawy.zzazh().zzj("Install referrer extras are", stringExtra);
            if (!stringExtra.contains("?")) {
                String str = "?";
                String valueOf = String.valueOf(stringExtra);
                stringExtra = valueOf.length() != 0 ? str.concat(valueOf) : new String(str);
            }
            Bundle zzp = zzdx.zzawu().zzp(Uri.parse(stringExtra));
            if (zzp == null) {
                zzawy.zzazj().log("No campaign defined in install referrer broadcast");
                if (doGoAsync != null) {
                    doGoAsync.finish();
                }
            } else {
                long longExtra = 1000 * intent.getLongExtra("referrer_timestamp_seconds", 0);
                if (longExtra == 0) {
                    zzawy.zzazf().log("Install referrer is missing timestamp");
                }
                zzcih zzawx = zzdx.zzawx();
                zzcie zzcie = new zzcie(this, zzdx, longExtra, zzp, context, zzawy, doGoAsync);
                zzawx.zzg(zzcie);
            }
        }
    }
}
