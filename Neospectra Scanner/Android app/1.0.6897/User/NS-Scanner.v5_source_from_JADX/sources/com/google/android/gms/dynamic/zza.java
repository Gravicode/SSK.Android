package com.google.android.gms.dynamic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.internal.zzu;
import com.google.android.gms.common.zzf;
import com.google.android.gms.dynamic.LifecycleDelegate;
import java.util.LinkedList;

public abstract class zza<T extends LifecycleDelegate> {
    /* access modifiers changed from: private */
    public T zzgwd;
    /* access modifiers changed from: private */
    public Bundle zzgwe;
    /* access modifiers changed from: private */
    public LinkedList<zzi> zzgwf;
    private final zzo<T> zzgwg = new zzb(this);

    private final void zza(Bundle bundle, zzi zzi) {
        if (this.zzgwd != null) {
            zzi.zzb(this.zzgwd);
            return;
        }
        if (this.zzgwf == null) {
            this.zzgwf = new LinkedList<>();
        }
        this.zzgwf.add(zzi);
        if (bundle != null) {
            if (this.zzgwe == null) {
                this.zzgwe = (Bundle) bundle.clone();
            } else {
                this.zzgwe.putAll(bundle);
            }
        }
        zza(this.zzgwg);
    }

    public static void zzb(FrameLayout frameLayout) {
        GoogleApiAvailability instance = GoogleApiAvailability.getInstance();
        Context context = frameLayout.getContext();
        int isGooglePlayServicesAvailable = instance.isGooglePlayServicesAvailable(context);
        String zzi = zzu.zzi(context, isGooglePlayServicesAvailable);
        String zzk = zzu.zzk(context, isGooglePlayServicesAvailable);
        LinearLayout linearLayout = new LinearLayout(frameLayout.getContext());
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new LayoutParams(-2, -2));
        frameLayout.addView(linearLayout);
        TextView textView = new TextView(frameLayout.getContext());
        textView.setLayoutParams(new LayoutParams(-2, -2));
        textView.setText(zzi);
        linearLayout.addView(textView);
        Intent zza = zzf.zza(context, isGooglePlayServicesAvailable, null);
        if (zza != null) {
            Button button = new Button(context);
            button.setId(16908313);
            button.setLayoutParams(new LayoutParams(-2, -2));
            button.setText(zzk);
            linearLayout.addView(button);
            button.setOnClickListener(new zzf(context, zza));
        }
    }

    private final void zzcz(int i) {
        while (!this.zzgwf.isEmpty() && ((zzi) this.zzgwf.getLast()).getState() >= i) {
            this.zzgwf.removeLast();
        }
    }

    public final void onCreate(Bundle bundle) {
        zza(bundle, (zzi) new zzd(this, bundle));
    }

    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        FrameLayout frameLayout = new FrameLayout(layoutInflater.getContext());
        zze zze = new zze(this, frameLayout, layoutInflater, viewGroup, bundle);
        zza(bundle, (zzi) zze);
        if (this.zzgwd == null) {
            zza(frameLayout);
        }
        return frameLayout;
    }

    public final void onDestroy() {
        if (this.zzgwd != null) {
            this.zzgwd.onDestroy();
        } else {
            zzcz(1);
        }
    }

    public final void onDestroyView() {
        if (this.zzgwd != null) {
            this.zzgwd.onDestroyView();
        } else {
            zzcz(2);
        }
    }

    public final void onInflate(Activity activity, Bundle bundle, Bundle bundle2) {
        zza(bundle2, (zzi) new zzc(this, activity, bundle, bundle2));
    }

    public final void onLowMemory() {
        if (this.zzgwd != null) {
            this.zzgwd.onLowMemory();
        }
    }

    public final void onPause() {
        if (this.zzgwd != null) {
            this.zzgwd.onPause();
        } else {
            zzcz(5);
        }
    }

    public final void onResume() {
        zza((Bundle) null, (zzi) new zzh(this));
    }

    public final void onSaveInstanceState(Bundle bundle) {
        if (this.zzgwd != null) {
            this.zzgwd.onSaveInstanceState(bundle);
            return;
        }
        if (this.zzgwe != null) {
            bundle.putAll(this.zzgwe);
        }
    }

    public final void onStart() {
        zza((Bundle) null, (zzi) new zzg(this));
    }

    public final void onStop() {
        if (this.zzgwd != null) {
            this.zzgwd.onStop();
        } else {
            zzcz(4);
        }
    }

    /* access modifiers changed from: protected */
    public void zza(FrameLayout frameLayout) {
        GoogleApiAvailability instance = GoogleApiAvailability.getInstance();
        Context context = frameLayout.getContext();
        int isGooglePlayServicesAvailable = instance.isGooglePlayServicesAvailable(context);
        String zzi = zzu.zzi(context, isGooglePlayServicesAvailable);
        String zzk = zzu.zzk(context, isGooglePlayServicesAvailable);
        LinearLayout linearLayout = new LinearLayout(frameLayout.getContext());
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new LayoutParams(-2, -2));
        frameLayout.addView(linearLayout);
        TextView textView = new TextView(frameLayout.getContext());
        textView.setLayoutParams(new LayoutParams(-2, -2));
        textView.setText(zzi);
        linearLayout.addView(textView);
        Intent zza = zzf.zza(context, isGooglePlayServicesAvailable, null);
        if (zza != null) {
            Button button = new Button(context);
            button.setId(16908313);
            button.setLayoutParams(new LayoutParams(-2, -2));
            button.setText(zzk);
            linearLayout.addView(button);
            button.setOnClickListener(new zzf(context, zza));
        }
    }

    /* access modifiers changed from: protected */
    public abstract void zza(zzo<T> zzo);

    public final T zzapw() {
        return this.zzgwd;
    }
}
