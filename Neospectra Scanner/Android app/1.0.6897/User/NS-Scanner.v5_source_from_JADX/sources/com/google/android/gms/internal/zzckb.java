package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.text.TextUtils;

@TargetApi(14)
@MainThread
final class zzckb implements ActivityLifecycleCallbacks {
    private /* synthetic */ zzcjn zzjhc;

    private zzckb(zzcjn zzcjn) {
        this.zzjhc = zzcjn;
    }

    /* synthetic */ zzckb(zzcjn zzcjn, zzcjo zzcjo) {
        this(zzcjn);
    }

    public final void onActivityCreated(Activity activity, Bundle bundle) {
        try {
            this.zzjhc.zzawy().zzazj().log("onActivityCreated");
            Intent intent = activity.getIntent();
            if (intent != null) {
                Uri data = intent.getData();
                if (data != null && data.isHierarchical()) {
                    if (bundle == null) {
                        Bundle zzp = this.zzjhc.zzawu().zzp(data);
                        this.zzjhc.zzawu();
                        String str = zzclq.zzo(intent) ? "gs" : "auto";
                        if (zzp != null) {
                            this.zzjhc.zzc(str, "_cmp", zzp);
                        }
                    }
                    String queryParameter = data.getQueryParameter("referrer");
                    if (!TextUtils.isEmpty(queryParameter)) {
                        if (!(queryParameter.contains("gclid") && (queryParameter.contains("utm_campaign") || queryParameter.contains("utm_source") || queryParameter.contains("utm_medium") || queryParameter.contains("utm_term") || queryParameter.contains("utm_content")))) {
                            this.zzjhc.zzawy().zzazi().log("Activity created with data 'referrer' param without gclid and at least one utm field");
                            return;
                        }
                        this.zzjhc.zzawy().zzazi().zzj("Activity created with referrer", queryParameter);
                        if (!TextUtils.isEmpty(queryParameter)) {
                            this.zzjhc.zzb("auto", "_ldl", queryParameter);
                        }
                    } else {
                        return;
                    }
                }
            }
        } catch (Throwable th) {
            this.zzjhc.zzawy().zzazd().zzj("Throwable caught in onActivityCreated", th);
        }
        zzckc zzawq = this.zzjhc.zzawq();
        if (bundle != null) {
            Bundle bundle2 = bundle.getBundle("com.google.firebase.analytics.screen_service");
            if (bundle2 != null) {
                zzckf zzq = zzawq.zzq(activity);
                zzq.zziwm = bundle2.getLong("id");
                zzq.zziwk = bundle2.getString("name");
                zzq.zziwl = bundle2.getString("referrer_name");
            }
        }
    }

    public final void onActivityDestroyed(Activity activity) {
        this.zzjhc.zzawq().onActivityDestroyed(activity);
    }

    @MainThread
    public final void onActivityPaused(Activity activity) {
        this.zzjhc.zzawq().onActivityPaused(activity);
        zzclf zzaww = this.zzjhc.zzaww();
        zzaww.zzawx().zzg(new zzclj(zzaww, zzaww.zzws().elapsedRealtime()));
    }

    @MainThread
    public final void onActivityResumed(Activity activity) {
        this.zzjhc.zzawq().onActivityResumed(activity);
        zzclf zzaww = this.zzjhc.zzaww();
        zzaww.zzawx().zzg(new zzcli(zzaww, zzaww.zzws().elapsedRealtime()));
    }

    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        this.zzjhc.zzawq().onActivitySaveInstanceState(activity, bundle);
    }

    public final void onActivityStarted(Activity activity) {
    }

    public final void onActivityStopped(Activity activity) {
    }
}
