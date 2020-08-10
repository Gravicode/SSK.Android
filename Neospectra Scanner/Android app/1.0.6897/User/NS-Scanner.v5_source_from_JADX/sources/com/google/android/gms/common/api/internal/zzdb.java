package com.google.android.gms.common.api.internal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentActivity;
import android.support.p001v4.util.ArrayMap;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

public final class zzdb extends Fragment implements zzcf {
    private static WeakHashMap<FragmentActivity, WeakReference<zzdb>> zzfue = new WeakHashMap<>();
    /* access modifiers changed from: private */
    public int zzcbc = 0;
    private Map<String, LifecycleCallback> zzfuf = new ArrayMap();
    /* access modifiers changed from: private */
    public Bundle zzfug;

    public static zzdb zza(FragmentActivity fragmentActivity) {
        WeakReference weakReference = (WeakReference) zzfue.get(fragmentActivity);
        if (weakReference != null) {
            zzdb zzdb = (zzdb) weakReference.get();
            if (zzdb != null) {
                return zzdb;
            }
        }
        try {
            zzdb zzdb2 = (zzdb) fragmentActivity.getSupportFragmentManager().findFragmentByTag("SupportLifecycleFragmentImpl");
            if (zzdb2 == null || zzdb2.isRemoving()) {
                zzdb2 = new zzdb();
                fragmentActivity.getSupportFragmentManager().beginTransaction().add((Fragment) zzdb2, "SupportLifecycleFragmentImpl").commitAllowingStateLoss();
            }
            zzfue.put(fragmentActivity, new WeakReference(zzdb2));
            return zzdb2;
        } catch (ClassCastException e) {
            throw new IllegalStateException("Fragment with tag SupportLifecycleFragmentImpl is not a SupportLifecycleFragmentImpl", e);
        }
    }

    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(str, fileDescriptor, printWriter, strArr);
        for (LifecycleCallback dump : this.zzfuf.values()) {
            dump.dump(str, fileDescriptor, printWriter, strArr);
        }
    }

    public final void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        for (LifecycleCallback onActivityResult : this.zzfuf.values()) {
            onActivityResult.onActivityResult(i, i2, intent);
        }
    }

    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.zzcbc = 1;
        this.zzfug = bundle;
        for (Entry entry : this.zzfuf.entrySet()) {
            ((LifecycleCallback) entry.getValue()).onCreate(bundle != null ? bundle.getBundle((String) entry.getKey()) : null);
        }
    }

    public final void onDestroy() {
        super.onDestroy();
        this.zzcbc = 5;
        for (LifecycleCallback onDestroy : this.zzfuf.values()) {
            onDestroy.onDestroy();
        }
    }

    public final void onResume() {
        super.onResume();
        this.zzcbc = 3;
        for (LifecycleCallback onResume : this.zzfuf.values()) {
            onResume.onResume();
        }
    }

    public final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (bundle != null) {
            for (Entry entry : this.zzfuf.entrySet()) {
                Bundle bundle2 = new Bundle();
                ((LifecycleCallback) entry.getValue()).onSaveInstanceState(bundle2);
                bundle.putBundle((String) entry.getKey(), bundle2);
            }
        }
    }

    public final void onStart() {
        super.onStart();
        this.zzcbc = 2;
        for (LifecycleCallback onStart : this.zzfuf.values()) {
            onStart.onStart();
        }
    }

    public final void onStop() {
        super.onStop();
        this.zzcbc = 4;
        for (LifecycleCallback onStop : this.zzfuf.values()) {
            onStop.onStop();
        }
    }

    public final <T extends LifecycleCallback> T zza(String str, Class<T> cls) {
        return (LifecycleCallback) cls.cast(this.zzfuf.get(str));
    }

    public final void zza(String str, @NonNull LifecycleCallback lifecycleCallback) {
        if (!this.zzfuf.containsKey(str)) {
            this.zzfuf.put(str, lifecycleCallback);
            if (this.zzcbc > 0) {
                new Handler(Looper.getMainLooper()).post(new zzdc(this, lifecycleCallback, str));
                return;
            }
            return;
        }
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 59);
        sb.append("LifecycleCallback with tag ");
        sb.append(str);
        sb.append(" already added to this fragment.");
        throw new IllegalArgumentException(sb.toString());
    }

    public final /* synthetic */ Activity zzajn() {
        return getActivity();
    }
}
