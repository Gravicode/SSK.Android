package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.p001v4.util.ArrayMap;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.util.zzd;
import com.google.android.gms.measurement.AppMeasurement.ConditionalUserProperty;
import com.google.android.gms.measurement.AppMeasurement.Event;
import com.google.android.gms.measurement.AppMeasurement.EventInterceptor;
import com.google.android.gms.measurement.AppMeasurement.OnEventListener;
import com.google.android.gms.measurement.AppMeasurement.zzb;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;

public final class zzcjn extends zzcjl {
    protected zzckb zzjgx;
    private EventInterceptor zzjgy;
    private final Set<OnEventListener> zzjgz = new CopyOnWriteArraySet();
    private boolean zzjha;
    private final AtomicReference<String> zzjhb = new AtomicReference<>();

    protected zzcjn(zzcim zzcim) {
        super(zzcim);
    }

    private final void zza(ConditionalUserProperty conditionalUserProperty) {
        long currentTimeMillis = zzws().currentTimeMillis();
        zzbq.checkNotNull(conditionalUserProperty);
        zzbq.zzgm(conditionalUserProperty.mName);
        zzbq.zzgm(conditionalUserProperty.mOrigin);
        zzbq.checkNotNull(conditionalUserProperty.mValue);
        conditionalUserProperty.mCreationTimestamp = currentTimeMillis;
        String str = conditionalUserProperty.mName;
        Object obj = conditionalUserProperty.mValue;
        if (zzawu().zzkd(str) != 0) {
            zzawy().zzazd().zzj("Invalid conditional user property name", zzawt().zzjj(str));
        } else if (zzawu().zzl(str, obj) != 0) {
            zzawy().zzazd().zze("Invalid conditional user property value", zzawt().zzjj(str), obj);
        } else {
            Object zzm = zzawu().zzm(str, obj);
            if (zzm == null) {
                zzawy().zzazd().zze("Unable to normalize conditional user property value", zzawt().zzjj(str), obj);
                return;
            }
            conditionalUserProperty.mValue = zzm;
            long j = conditionalUserProperty.mTriggerTimeout;
            if (TextUtils.isEmpty(conditionalUserProperty.mTriggerEventName) || (j <= 15552000000L && j >= 1)) {
                long j2 = conditionalUserProperty.mTimeToLive;
                if (j2 > 15552000000L || j2 < 1) {
                    zzawy().zzazd().zze("Invalid conditional user property time to live", zzawt().zzjj(str), Long.valueOf(j2));
                } else {
                    zzawx().zzg(new zzcjp(this, conditionalUserProperty));
                }
            } else {
                zzawy().zzazd().zze("Invalid conditional user property timeout", zzawt().zzjj(str), Long.valueOf(j));
            }
        }
    }

    private final void zza(String str, String str2, long j, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        Bundle bundle2;
        Bundle bundle3 = bundle;
        if (bundle3 == null) {
            bundle2 = new Bundle();
        } else {
            Bundle bundle4 = new Bundle(bundle3);
            for (String str4 : bundle4.keySet()) {
                Object obj = bundle4.get(str4);
                if (obj instanceof Bundle) {
                    bundle4.putBundle(str4, new Bundle((Bundle) obj));
                } else {
                    int i = 0;
                    if (obj instanceof Parcelable[]) {
                        Parcelable[] parcelableArr = (Parcelable[]) obj;
                        while (i < parcelableArr.length) {
                            if (parcelableArr[i] instanceof Bundle) {
                                parcelableArr[i] = new Bundle((Bundle) parcelableArr[i]);
                            }
                            i++;
                        }
                    } else if (obj instanceof ArrayList) {
                        ArrayList arrayList = (ArrayList) obj;
                        while (i < arrayList.size()) {
                            Object obj2 = arrayList.get(i);
                            if (obj2 instanceof Bundle) {
                                arrayList.set(i, new Bundle((Bundle) obj2));
                            }
                            i++;
                        }
                    }
                }
            }
            bundle2 = bundle4;
        }
        zzcih zzawx = zzawx();
        zzcjv zzcjv = new zzcjv(this, str, str2, j, bundle2, z, z2, z3, str3);
        zzawx.zzg(zzcjv);
    }

    private final void zza(String str, String str2, long j, Object obj) {
        zzcih zzawx = zzawx();
        zzcjw zzcjw = new zzcjw(this, str, str2, obj, j);
        zzawx.zzg(zzcjw);
    }

    private final void zza(String str, String str2, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        zza(str, str2, zzws().currentTimeMillis(), bundle, true, z2, z3, null);
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zza(String str, String str2, Object obj, long j) {
        zzbq.zzgm(str);
        zzbq.zzgm(str2);
        zzve();
        zzxf();
        if (!this.zziwf.isEnabled()) {
            zzawy().zzazi().log("User property not set since app measurement is disabled");
        } else if (this.zziwf.zzazv()) {
            zzawy().zzazi().zze("Setting user property (FE)", zzawt().zzjh(str2), obj);
            zzcln zzcln = new zzcln(str2, j, obj, str);
            zzawp().zzb(zzcln);
        }
    }

    private final void zza(String str, String str2, String str3, Bundle bundle) {
        long currentTimeMillis = zzws().currentTimeMillis();
        zzbq.zzgm(str2);
        ConditionalUserProperty conditionalUserProperty = new ConditionalUserProperty();
        conditionalUserProperty.mAppId = str;
        conditionalUserProperty.mName = str2;
        conditionalUserProperty.mCreationTimestamp = currentTimeMillis;
        if (str3 != null) {
            conditionalUserProperty.mExpiredEventName = str3;
            conditionalUserProperty.mExpiredEventParams = bundle;
        }
        zzawx().zzg(new zzcjq(this, conditionalUserProperty));
    }

    private final Map<String, Object> zzb(String str, String str2, String str3, boolean z) {
        zzcho zzazf;
        String str4;
        if (zzawx().zzazs()) {
            zzazf = zzawy().zzazd();
            str4 = "Cannot get user properties from analytics worker thread";
        } else {
            zzawx();
            if (zzcih.zzau()) {
                zzazf = zzawy().zzazd();
                str4 = "Cannot get user properties from main thread";
            } else {
                AtomicReference atomicReference = new AtomicReference();
                synchronized (atomicReference) {
                    zzcih zzawx = this.zziwf.zzawx();
                    zzcjs zzcjs = new zzcjs(this, atomicReference, str, str2, str3, z);
                    zzawx.zzg(zzcjs);
                    try {
                        atomicReference.wait(5000);
                    } catch (InterruptedException e) {
                        zzawy().zzazf().zzj("Interrupted waiting for get user properties", e);
                    }
                }
                List<zzcln> list = (List) atomicReference.get();
                if (list == null) {
                    zzazf = zzawy().zzazf();
                    str4 = "Timed out waiting for get user properties";
                } else {
                    ArrayMap arrayMap = new ArrayMap(list.size());
                    for (zzcln zzcln : list) {
                        arrayMap.put(zzcln.name, zzcln.getValue());
                    }
                    return arrayMap;
                }
            }
        }
        zzazf.log(str4);
        return Collections.emptyMap();
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzb(ConditionalUserProperty conditionalUserProperty) {
        ConditionalUserProperty conditionalUserProperty2 = conditionalUserProperty;
        zzve();
        zzxf();
        zzbq.checkNotNull(conditionalUserProperty);
        zzbq.zzgm(conditionalUserProperty2.mName);
        zzbq.zzgm(conditionalUserProperty2.mOrigin);
        zzbq.checkNotNull(conditionalUserProperty2.mValue);
        if (!this.zziwf.isEnabled()) {
            zzawy().zzazi().log("Conditional property not sent since Firebase Analytics is disabled");
            return;
        }
        zzcln zzcln = new zzcln(conditionalUserProperty2.mName, conditionalUserProperty2.mTriggeredTimestamp, conditionalUserProperty2.mValue, conditionalUserProperty2.mOrigin);
        try {
            zzcha zza = zzawu().zza(conditionalUserProperty2.mTriggeredEventName, conditionalUserProperty2.mTriggeredEventParams, conditionalUserProperty2.mOrigin, 0, true, false);
            zzcha zza2 = zzawu().zza(conditionalUserProperty2.mTimedOutEventName, conditionalUserProperty2.mTimedOutEventParams, conditionalUserProperty2.mOrigin, 0, true, false);
            zzcha zza3 = zzawu().zza(conditionalUserProperty2.mExpiredEventName, conditionalUserProperty2.mExpiredEventParams, conditionalUserProperty2.mOrigin, 0, true, false);
            zzcgl zzcgl = r4;
            zzcgl zzcgl2 = new zzcgl(conditionalUserProperty2.mAppId, conditionalUserProperty2.mOrigin, zzcln, conditionalUserProperty2.mCreationTimestamp, false, conditionalUserProperty2.mTriggerEventName, zza2, conditionalUserProperty2.mTriggerTimeout, zza, conditionalUserProperty2.mTimeToLive, zza3);
            zzawp().zzf(zzcgl);
        } catch (IllegalArgumentException e) {
        }
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzb(String str, String str2, long j, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        int i;
        boolean z4;
        int i2;
        String[] strArr;
        String str4 = str;
        String str5 = str2;
        zzbq.zzgm(str);
        zzbq.zzgm(str2);
        zzbq.checkNotNull(bundle);
        zzve();
        zzxf();
        if (!this.zziwf.isEnabled()) {
            zzawy().zzazi().log("Event not sent since app measurement is disabled");
            return;
        }
        if (!this.zzjha) {
            this.zzjha = true;
            try {
                try {
                    Class.forName("com.google.android.gms.tagmanager.TagManagerService").getDeclaredMethod("initialize", new Class[]{Context.class}).invoke(null, new Object[]{getContext()});
                } catch (Exception e) {
                    zzawy().zzazf().zzj("Failed to invoke Tag Manager's initialize() method", e);
                }
            } catch (ClassNotFoundException e2) {
                zzawy().zzazh().log("Tag Manager is not found and thus will not be used");
            }
        }
        boolean equals = "am".equals(str4);
        boolean zzki = zzclq.zzki(str2);
        if (!z || this.zzjgy == null || zzki || equals) {
            Bundle bundle2 = bundle;
            if (this.zziwf.zzazv()) {
                int zzkb = zzawu().zzkb(str5);
                if (zzkb != 0) {
                    zzawu();
                    this.zziwf.zzawu().zza(str3, zzkb, "_ev", zzclq.zza(str5, 40, true), str5 != null ? str2.length() : 0);
                    return;
                }
                List singletonList = Collections.singletonList("_o");
                Bundle zza = zzawu().zza(str5, bundle2, singletonList, z3, true);
                ArrayList arrayList = new ArrayList();
                arrayList.add(zza);
                long nextLong = zzawu().zzbaz().nextLong();
                String[] strArr2 = (String[]) zza.keySet().toArray(new String[bundle.size()]);
                Arrays.sort(strArr2);
                int length = strArr2.length;
                int i3 = 0;
                int i4 = 0;
                while (i4 < length) {
                    String str6 = strArr2[i4];
                    Object obj = zza.get(str6);
                    zzawu();
                    Bundle[] zzaf = zzclq.zzaf(obj);
                    if (zzaf != null) {
                        zza.putInt(str6, zzaf.length);
                        strArr = strArr2;
                        int i5 = 0;
                        while (i5 < zzaf.length) {
                            int i6 = length;
                            String str7 = str6;
                            boolean z5 = equals;
                            int i7 = i3;
                            int i8 = i4;
                            Bundle zza2 = zzawu().zza("_ep", zzaf[i5], singletonList, z3, false);
                            zza2.putString("_en", str5);
                            zza2.putLong("_eid", nextLong);
                            zza2.putString("_gn", str7);
                            zza2.putInt("_ll", zzaf.length);
                            zza2.putInt("_i", i5);
                            arrayList.add(zza2);
                            i5++;
                            str6 = str7;
                            i3 = i7;
                            length = i6;
                            equals = z5;
                            i4 = i8;
                        }
                        i2 = length;
                        z4 = equals;
                        i = i4;
                        i3 += zzaf.length;
                    } else {
                        strArr = strArr2;
                        i2 = length;
                        z4 = equals;
                        int i9 = i3;
                        i = i4;
                    }
                    i4 = i + 1;
                    strArr2 = strArr;
                    length = i2;
                    equals = z4;
                }
                boolean z6 = equals;
                int i10 = i3;
                if (i10 != 0) {
                    zza.putLong("_eid", nextLong);
                    zza.putInt("_epc", i10);
                }
                zzckf zzbao = zzawq().zzbao();
                if (zzbao != null && !zza.containsKey("_sc")) {
                    zzbao.zzjib = true;
                }
                int i11 = 0;
                while (i11 < arrayList.size()) {
                    Bundle bundle3 = (Bundle) arrayList.get(i11);
                    String str8 = i11 != 0 ? "_ep" : str5;
                    bundle3.putString("_o", str4);
                    if (!bundle3.containsKey("_sc")) {
                        zzckc.zza((zzb) zzbao, bundle3);
                    }
                    if (z2) {
                        bundle3 = zzawu().zzy(bundle3);
                    }
                    Bundle bundle4 = bundle3;
                    zzawy().zzazi().zze("Logging event (FE)", zzawt().zzjh(str5), zzawt().zzx(bundle4));
                    ArrayList arrayList2 = arrayList;
                    zzcha zzcha = new zzcha(str8, new zzcgx(bundle4), str4, j);
                    zzawp().zzc(zzcha, str3);
                    if (!z6) {
                        for (OnEventListener onEvent : this.zzjgz) {
                            onEvent.onEvent(str4, str5, new Bundle(bundle4), j);
                            String str9 = str3;
                        }
                    }
                    i11++;
                    arrayList = arrayList2;
                }
                if (zzawq().zzbao() != null && Event.APP_EXCEPTION.equals(str5)) {
                    zzaww().zzbs(true);
                }
                return;
            }
            return;
        }
        Bundle bundle5 = bundle;
        zzawy().zzazi().zze("Passing event to registered event handler (FE)", zzawt().zzjh(str5), zzawt().zzx(bundle5));
        this.zzjgy.interceptEvent(str4, str5, bundle5, j);
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzbp(boolean z) {
        zzve();
        zzxf();
        zzawy().zzazi().zzj("Setting app measurement enabled (FE)", Boolean.valueOf(z));
        zzawz().setMeasurementEnabled(z);
        zzawp().zzbaq();
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzc(ConditionalUserProperty conditionalUserProperty) {
        ConditionalUserProperty conditionalUserProperty2 = conditionalUserProperty;
        zzve();
        zzxf();
        zzbq.checkNotNull(conditionalUserProperty);
        zzbq.zzgm(conditionalUserProperty2.mName);
        if (!this.zziwf.isEnabled()) {
            zzawy().zzazi().log("Conditional property not cleared since Firebase Analytics is disabled");
            return;
        }
        zzcln zzcln = new zzcln(conditionalUserProperty2.mName, 0, null, null);
        try {
            zzcha zza = zzawu().zza(conditionalUserProperty2.mExpiredEventName, conditionalUserProperty2.mExpiredEventParams, conditionalUserProperty2.mOrigin, conditionalUserProperty2.mCreationTimestamp, true, false);
            String str = conditionalUserProperty2.mAppId;
            String str2 = conditionalUserProperty2.mOrigin;
            long j = conditionalUserProperty2.mCreationTimestamp;
            boolean z = conditionalUserProperty2.mActive;
            String str3 = conditionalUserProperty2.mTriggerEventName;
            long j2 = conditionalUserProperty2.mTriggerTimeout;
            zzcgl zzcgl = r4;
            zzcgl zzcgl2 = new zzcgl(str, str2, zzcln, j, z, str3, null, j2, null, conditionalUserProperty2.mTimeToLive, zza);
            zzawp().zzf(zzcgl);
        } catch (IllegalArgumentException e) {
        }
    }

    private final List<ConditionalUserProperty> zzk(String str, String str2, String str3) {
        zzcho zzazd;
        String str4;
        if (zzawx().zzazs()) {
            zzazd = zzawy().zzazd();
            str4 = "Cannot get conditional user properties from analytics worker thread";
        } else {
            zzawx();
            if (zzcih.zzau()) {
                zzazd = zzawy().zzazd();
                str4 = "Cannot get conditional user properties from main thread";
            } else {
                AtomicReference atomicReference = new AtomicReference();
                synchronized (atomicReference) {
                    zzcih zzawx = this.zziwf.zzawx();
                    zzcjr zzcjr = new zzcjr(this, atomicReference, str, str2, str3);
                    zzawx.zzg(zzcjr);
                    try {
                        atomicReference.wait(5000);
                    } catch (InterruptedException e) {
                        zzawy().zzazf().zze("Interrupted waiting for get conditional user properties", str, e);
                    }
                }
                List<zzcgl> list = (List) atomicReference.get();
                if (list == null) {
                    zzawy().zzazf().zzj("Timed out waiting for get conditional user properties", str);
                    return Collections.emptyList();
                }
                ArrayList arrayList = new ArrayList(list.size());
                for (zzcgl zzcgl : list) {
                    ConditionalUserProperty conditionalUserProperty = new ConditionalUserProperty();
                    conditionalUserProperty.mAppId = str;
                    conditionalUserProperty.mOrigin = str2;
                    conditionalUserProperty.mCreationTimestamp = zzcgl.zziyh;
                    conditionalUserProperty.mName = zzcgl.zziyg.name;
                    conditionalUserProperty.mValue = zzcgl.zziyg.getValue();
                    conditionalUserProperty.mActive = zzcgl.zziyi;
                    conditionalUserProperty.mTriggerEventName = zzcgl.zziyj;
                    if (zzcgl.zziyk != null) {
                        conditionalUserProperty.mTimedOutEventName = zzcgl.zziyk.name;
                        if (zzcgl.zziyk.zzizt != null) {
                            conditionalUserProperty.mTimedOutEventParams = zzcgl.zziyk.zzizt.zzayx();
                        }
                    }
                    conditionalUserProperty.mTriggerTimeout = zzcgl.zziyl;
                    if (zzcgl.zziym != null) {
                        conditionalUserProperty.mTriggeredEventName = zzcgl.zziym.name;
                        if (zzcgl.zziym.zzizt != null) {
                            conditionalUserProperty.mTriggeredEventParams = zzcgl.zziym.zzizt.zzayx();
                        }
                    }
                    conditionalUserProperty.mTriggeredTimestamp = zzcgl.zziyg.zzjji;
                    conditionalUserProperty.mTimeToLive = zzcgl.zziyn;
                    if (zzcgl.zziyo != null) {
                        conditionalUserProperty.mExpiredEventName = zzcgl.zziyo.name;
                        if (zzcgl.zziyo.zzizt != null) {
                            conditionalUserProperty.mExpiredEventParams = zzcgl.zziyo.zzizt.zzayx();
                        }
                    }
                    arrayList.add(conditionalUserProperty);
                }
                return arrayList;
            }
        }
        zzazd.log(str4);
        return Collections.emptyList();
    }

    public final void clearConditionalUserProperty(String str, String str2, Bundle bundle) {
        zza((String) null, str, str2, bundle);
    }

    public final void clearConditionalUserPropertyAs(String str, String str2, String str3, Bundle bundle) {
        zzbq.zzgm(str);
        zzawi();
        zza(str, str2, str3, bundle);
    }

    public final Task<String> getAppInstanceId() {
        try {
            String zzazn = zzawz().zzazn();
            return zzazn != null ? Tasks.forResult(zzazn) : Tasks.call(zzawx().zzazt(), new zzcjy(this));
        } catch (Exception e) {
            zzawy().zzazf().log("Failed to schedule task for getAppInstanceId");
            return Tasks.forException(e);
        }
    }

    public final List<ConditionalUserProperty> getConditionalUserProperties(String str, String str2) {
        return zzk(null, str, str2);
    }

    public final List<ConditionalUserProperty> getConditionalUserPropertiesAs(String str, String str2, String str3) {
        zzbq.zzgm(str);
        zzawi();
        return zzk(str, str2, str3);
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final Map<String, Object> getUserProperties(String str, String str2, boolean z) {
        return zzb(null, str, str2, z);
    }

    public final Map<String, Object> getUserPropertiesAs(String str, String str2, String str3, boolean z) {
        zzbq.zzgm(str);
        zzawi();
        return zzb(str, str2, str3, z);
    }

    public final void registerOnMeasurementEventListener(OnEventListener onEventListener) {
        zzxf();
        zzbq.checkNotNull(onEventListener);
        if (!this.zzjgz.add(onEventListener)) {
            zzawy().zzazf().log("OnEventListener already registered");
        }
    }

    public final void resetAnalyticsData() {
        zzawx().zzg(new zzcka(this));
    }

    public final void setConditionalUserProperty(ConditionalUserProperty conditionalUserProperty) {
        zzbq.checkNotNull(conditionalUserProperty);
        ConditionalUserProperty conditionalUserProperty2 = new ConditionalUserProperty(conditionalUserProperty);
        if (!TextUtils.isEmpty(conditionalUserProperty2.mAppId)) {
            zzawy().zzazf().log("Package name should be null when calling setConditionalUserProperty");
        }
        conditionalUserProperty2.mAppId = null;
        zza(conditionalUserProperty2);
    }

    public final void setConditionalUserPropertyAs(ConditionalUserProperty conditionalUserProperty) {
        zzbq.checkNotNull(conditionalUserProperty);
        zzbq.zzgm(conditionalUserProperty.mAppId);
        zzawi();
        zza(new ConditionalUserProperty(conditionalUserProperty));
    }

    @WorkerThread
    public final void setEventInterceptor(EventInterceptor eventInterceptor) {
        zzve();
        zzxf();
        if (!(eventInterceptor == null || eventInterceptor == this.zzjgy)) {
            zzbq.zza(this.zzjgy == null, (Object) "EventInterceptor already set.");
        }
        this.zzjgy = eventInterceptor;
    }

    public final void setMeasurementEnabled(boolean z) {
        zzxf();
        zzawx().zzg(new zzcjo(this, z));
    }

    public final void setMinimumSessionDuration(long j) {
        zzawx().zzg(new zzcjt(this, j));
    }

    public final void setSessionTimeoutDuration(long j) {
        zzawx().zzg(new zzcju(this, j));
    }

    public final void unregisterOnMeasurementEventListener(OnEventListener onEventListener) {
        zzxf();
        zzbq.checkNotNull(onEventListener);
        if (!this.zzjgz.remove(onEventListener)) {
            zzawy().zzazf().log("OnEventListener had not been registered");
        }
    }

    public final void zza(String str, String str2, Bundle bundle, long j) {
        zza(str, str2, j, bundle, false, true, true, null);
    }

    public final void zza(String str, String str2, Bundle bundle, boolean z) {
        zza(str, str2, bundle, true, this.zzjgy == null || zzclq.zzki(str2), true, null);
    }

    public final /* bridge */ /* synthetic */ void zzawi() {
        super.zzawi();
    }

    public final /* bridge */ /* synthetic */ void zzawj() {
        super.zzawj();
    }

    public final /* bridge */ /* synthetic */ zzcgd zzawk() {
        return super.zzawk();
    }

    public final /* bridge */ /* synthetic */ zzcgk zzawl() {
        return super.zzawl();
    }

    public final /* bridge */ /* synthetic */ zzcjn zzawm() {
        return super.zzawm();
    }

    public final /* bridge */ /* synthetic */ zzchh zzawn() {
        return super.zzawn();
    }

    public final /* bridge */ /* synthetic */ zzcgu zzawo() {
        return super.zzawo();
    }

    public final /* bridge */ /* synthetic */ zzckg zzawp() {
        return super.zzawp();
    }

    public final /* bridge */ /* synthetic */ zzckc zzawq() {
        return super.zzawq();
    }

    public final /* bridge */ /* synthetic */ zzchi zzawr() {
        return super.zzawr();
    }

    public final /* bridge */ /* synthetic */ zzcgo zzaws() {
        return super.zzaws();
    }

    public final /* bridge */ /* synthetic */ zzchk zzawt() {
        return super.zzawt();
    }

    public final /* bridge */ /* synthetic */ zzclq zzawu() {
        return super.zzawu();
    }

    public final /* bridge */ /* synthetic */ zzcig zzawv() {
        return super.zzawv();
    }

    public final /* bridge */ /* synthetic */ zzclf zzaww() {
        return super.zzaww();
    }

    public final /* bridge */ /* synthetic */ zzcih zzawx() {
        return super.zzawx();
    }

    public final /* bridge */ /* synthetic */ zzchm zzawy() {
        return super.zzawy();
    }

    public final /* bridge */ /* synthetic */ zzchx zzawz() {
        return super.zzawz();
    }

    public final /* bridge */ /* synthetic */ zzcgn zzaxa() {
        return super.zzaxa();
    }

    /* access modifiers changed from: protected */
    public final boolean zzaxz() {
        return false;
    }

    @Nullable
    public final String zzazn() {
        return (String) this.zzjhb.get();
    }

    public final void zzb(String str, String str2, Object obj) {
        zzbq.zzgm(str);
        long currentTimeMillis = zzws().currentTimeMillis();
        int zzkd = zzawu().zzkd(str2);
        int i = 0;
        if (zzkd != 0) {
            zzawu();
            String zza = zzclq.zza(str2, 24, true);
            if (str2 != null) {
                i = str2.length();
            }
            this.zziwf.zzawu().zza(zzkd, "_ev", zza, i);
        } else if (obj != null) {
            int zzl = zzawu().zzl(str2, obj);
            if (zzl != 0) {
                zzawu();
                String zza2 = zzclq.zza(str2, 24, true);
                if ((obj instanceof String) || (obj instanceof CharSequence)) {
                    i = String.valueOf(obj).length();
                }
                this.zziwf.zzawu().zza(zzl, "_ev", zza2, i);
                return;
            }
            Object zzm = zzawu().zzm(str2, obj);
            if (zzm != null) {
                zza(str, str2, currentTimeMillis, zzm);
            }
        } else {
            zza(str, str2, currentTimeMillis, (Object) null);
        }
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public final String zzbd(long j) {
        AtomicReference atomicReference = new AtomicReference();
        synchronized (atomicReference) {
            zzawx().zzg(new zzcjz(this, atomicReference));
            try {
                atomicReference.wait(j);
            } catch (InterruptedException e) {
                zzawy().zzazf().log("Interrupted waiting for app instance id");
                return null;
            }
        }
        return (String) atomicReference.get();
    }

    public final List<zzcln> zzbq(boolean z) {
        zzcho zzazf;
        String str;
        zzxf();
        zzawy().zzazi().log("Fetching user attributes (FE)");
        if (zzawx().zzazs()) {
            zzazf = zzawy().zzazd();
            str = "Cannot get all user properties from analytics worker thread";
        } else {
            zzawx();
            if (zzcih.zzau()) {
                zzazf = zzawy().zzazd();
                str = "Cannot get all user properties from main thread";
            } else {
                AtomicReference atomicReference = new AtomicReference();
                synchronized (atomicReference) {
                    this.zziwf.zzawx().zzg(new zzcjx(this, atomicReference, z));
                    try {
                        atomicReference.wait(5000);
                    } catch (InterruptedException e) {
                        zzawy().zzazf().zzj("Interrupted waiting for get user properties", e);
                    }
                }
                List<zzcln> list = (List) atomicReference.get();
                if (list != null) {
                    return list;
                }
                zzazf = zzawy().zzazf();
                str = "Timed out waiting for get user properties";
            }
        }
        zzazf.log(str);
        return Collections.emptyList();
    }

    public final void zzc(String str, String str2, Bundle bundle) {
        zza(str, str2, bundle, true, this.zzjgy == null || zzclq.zzki(str2), false, null);
    }

    /* access modifiers changed from: 0000 */
    public final void zzjp(@Nullable String str) {
        this.zzjhb.set(str);
    }

    public final /* bridge */ /* synthetic */ void zzve() {
        super.zzve();
    }

    public final /* bridge */ /* synthetic */ zzd zzws() {
        return super.zzws();
    }
}
