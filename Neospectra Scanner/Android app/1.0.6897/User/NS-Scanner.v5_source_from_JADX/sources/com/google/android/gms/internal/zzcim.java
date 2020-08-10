package com.google.android.gms.internal;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.WorkerThread;
import android.support.p001v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.common.api.internal.zzbz;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.util.zzd;
import com.google.android.gms.common.util.zzh;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.analytics.FirebaseAnalytics.Event;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public class zzcim {
    private static volatile zzcim zzjev;
    private final Context mContext;
    private final zzd zzata;
    private boolean zzdtb = false;
    private final zzcgn zzjew;
    private final zzchx zzjex;
    private final zzchm zzjey;
    private final zzcih zzjez;
    private final zzclf zzjfa;
    private final zzcig zzjfb;
    private final AppMeasurement zzjfc;
    private final FirebaseAnalytics zzjfd;
    private final zzclq zzjfe;
    private final zzchk zzjff;
    private final zzcgo zzjfg;
    private final zzchi zzjfh;
    private final zzchq zzjfi;
    private final zzckc zzjfj;
    private final zzckg zzjfk;
    private final zzcgu zzjfl;
    private final zzcjn zzjfm;
    private final zzchh zzjfn;
    private final zzchv zzjfo;
    private final zzcll zzjfp;
    private final zzcgk zzjfq;
    private final zzcgd zzjfr;
    private boolean zzjfs;
    private Boolean zzjft;
    private long zzjfu;
    private FileLock zzjfv;
    private FileChannel zzjfw;
    private List<Long> zzjfx;
    private List<Runnable> zzjfy;
    private int zzjfz;
    private int zzjga;
    private long zzjgb;
    private long zzjgc;
    private boolean zzjgd;
    private boolean zzjge;
    private boolean zzjgf;
    private final long zzjgg;

    class zza implements zzcgq {
        List<zzcmb> zzapa;
        zzcme zzjgi;
        List<Long> zzjgj;
        private long zzjgk;

        private zza() {
        }

        /* synthetic */ zza(zzcim zzcim, zzcin zzcin) {
            this();
        }

        private static long zza(zzcmb zzcmb) {
            return ((zzcmb.zzjli.longValue() / 1000) / 60) / 60;
        }

        public final boolean zza(long j, zzcmb zzcmb) {
            zzbq.checkNotNull(zzcmb);
            if (this.zzapa == null) {
                this.zzapa = new ArrayList();
            }
            if (this.zzjgj == null) {
                this.zzjgj = new ArrayList();
            }
            if (this.zzapa.size() > 0 && zza((zzcmb) this.zzapa.get(0)) != zza(zzcmb)) {
                return false;
            }
            long zzho = this.zzjgk + ((long) zzcmb.zzho());
            if (zzho >= ((long) Math.max(0, ((Integer) zzchc.zzjal.get()).intValue()))) {
                return false;
            }
            this.zzjgk = zzho;
            this.zzapa.add(zzcmb);
            this.zzjgj.add(Long.valueOf(j));
            return this.zzapa.size() < Math.max(1, ((Integer) zzchc.zzjam.get()).intValue());
        }

        public final void zzb(zzcme zzcme) {
            zzbq.checkNotNull(zzcme);
            this.zzjgi = zzcme;
        }
    }

    private zzcim(zzcjm zzcjm) {
        zzcho zzcho;
        String str;
        zzbq.checkNotNull(zzcjm);
        this.mContext = zzcjm.mContext;
        this.zzjgb = -1;
        this.zzata = zzh.zzamg();
        this.zzjgg = this.zzata.currentTimeMillis();
        this.zzjew = new zzcgn(this);
        zzchx zzchx = new zzchx(this);
        zzchx.initialize();
        this.zzjex = zzchx;
        zzchm zzchm = new zzchm(this);
        zzchm.initialize();
        this.zzjey = zzchm;
        zzclq zzclq = new zzclq(this);
        zzclq.initialize();
        this.zzjfe = zzclq;
        zzchk zzchk = new zzchk(this);
        zzchk.initialize();
        this.zzjff = zzchk;
        zzcgu zzcgu = new zzcgu(this);
        zzcgu.initialize();
        this.zzjfl = zzcgu;
        zzchh zzchh = new zzchh(this);
        zzchh.initialize();
        this.zzjfn = zzchh;
        zzcgo zzcgo = new zzcgo(this);
        zzcgo.initialize();
        this.zzjfg = zzcgo;
        zzchi zzchi = new zzchi(this);
        zzchi.initialize();
        this.zzjfh = zzchi;
        zzcgk zzcgk = new zzcgk(this);
        zzcgk.initialize();
        this.zzjfq = zzcgk;
        this.zzjfr = new zzcgd(this);
        zzchq zzchq = new zzchq(this);
        zzchq.initialize();
        this.zzjfi = zzchq;
        zzckc zzckc = new zzckc(this);
        zzckc.initialize();
        this.zzjfj = zzckc;
        zzckg zzckg = new zzckg(this);
        zzckg.initialize();
        this.zzjfk = zzckg;
        zzcjn zzcjn = new zzcjn(this);
        zzcjn.initialize();
        this.zzjfm = zzcjn;
        zzcll zzcll = new zzcll(this);
        zzcll.initialize();
        this.zzjfp = zzcll;
        this.zzjfo = new zzchv(this);
        this.zzjfc = new AppMeasurement(this);
        this.zzjfd = new FirebaseAnalytics(this);
        zzclf zzclf = new zzclf(this);
        zzclf.initialize();
        this.zzjfa = zzclf;
        zzcig zzcig = new zzcig(this);
        zzcig.initialize();
        this.zzjfb = zzcig;
        zzcih zzcih = new zzcih(this);
        zzcih.initialize();
        this.zzjez = zzcih;
        if (this.mContext.getApplicationContext() instanceof Application) {
            zzcjn zzawm = zzawm();
            if (zzawm.getContext().getApplicationContext() instanceof Application) {
                Application application = (Application) zzawm.getContext().getApplicationContext();
                if (zzawm.zzjgx == null) {
                    zzawm.zzjgx = new zzckb(zzawm, null);
                }
                application.unregisterActivityLifecycleCallbacks(zzawm.zzjgx);
                application.registerActivityLifecycleCallbacks(zzawm.zzjgx);
                zzcho = zzawm.zzawy().zzazj();
                str = "Registered activity lifecycle callback";
            }
            this.zzjez.zzg(new zzcin(this));
        }
        zzcho = zzawy().zzazf();
        str = "Application context is not an Application";
        zzcho.log(str);
        this.zzjez.zzg(new zzcin(this));
    }

    @WorkerThread
    private final int zza(FileChannel fileChannel) {
        zzawx().zzve();
        if (fileChannel == null || !fileChannel.isOpen()) {
            zzawy().zzazd().log("Bad chanel to read from");
            return 0;
        }
        ByteBuffer allocate = ByteBuffer.allocate(4);
        try {
            fileChannel.position(0);
            int read = fileChannel.read(allocate);
            if (read != 4) {
                if (read != -1) {
                    zzawy().zzazf().zzj("Unexpected data length. Bytes read", Integer.valueOf(read));
                }
                return 0;
            }
            allocate.flip();
            return allocate.getInt();
        } catch (IOException e) {
            zzawy().zzazd().zzj("Failed to read from channel", e);
            return 0;
        }
    }

    private final zzcgi zza(Context context, String str, String str2, boolean z, boolean z2) {
        int i;
        String str3 = str;
        String str4 = "Unknown";
        String str5 = "Unknown";
        String str6 = "Unknown";
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            zzawy().zzazd().log("PackageManager is null, can not log app install information");
            return null;
        }
        try {
            str4 = packageManager.getInstallerPackageName(str3);
        } catch (IllegalArgumentException e) {
            zzawy().zzazd().zzj("Error retrieving installer package name. appId", zzchm.zzjk(str));
        }
        if (str4 == null) {
            str4 = "manual_install";
        } else if ("com.android.vending".equals(str4)) {
            str4 = "";
        }
        String str7 = str4;
        try {
            PackageInfo packageInfo = zzbhf.zzdb(context).getPackageInfo(str3, 0);
            if (packageInfo != null) {
                CharSequence zzgt = zzbhf.zzdb(context).zzgt(str3);
                if (!TextUtils.isEmpty(zzgt)) {
                    String charSequence = zzgt.toString();
                }
                str5 = packageInfo.versionName;
                i = packageInfo.versionCode;
            } else {
                i = Integer.MIN_VALUE;
            }
            zzcgi zzcgi = new zzcgi(str3, str2, str5, (long) i, str7, 11910, zzawu().zzaf(context, str3), (String) null, z, false, "", 0, 0, 0, z2);
            return zzcgi;
        } catch (NameNotFoundException e2) {
            zzawy().zzazd().zze("Error retrieving newly installed package info. appId, appName", zzchm.zzjk(str), str6);
            return null;
        }
    }

    private static void zza(zzcjk zzcjk) {
        if (zzcjk == null) {
            throw new IllegalStateException("Component not created");
        }
    }

    private static void zza(zzcjl zzcjl) {
        if (zzcjl == null) {
            throw new IllegalStateException("Component not created");
        } else if (!zzcjl.isInitialized()) {
            throw new IllegalStateException("Component not initialized");
        }
    }

    @WorkerThread
    private final boolean zza(int i, FileChannel fileChannel) {
        zzawx().zzve();
        if (fileChannel == null || !fileChannel.isOpen()) {
            zzawy().zzazd().log("Bad chanel to read from");
            return false;
        }
        ByteBuffer allocate = ByteBuffer.allocate(4);
        allocate.putInt(i);
        allocate.flip();
        try {
            fileChannel.truncate(0);
            fileChannel.write(allocate);
            fileChannel.force(true);
            if (fileChannel.size() != 4) {
                zzawy().zzazd().zzj("Error writing to channel. Bytes written", Long.valueOf(fileChannel.size()));
            }
            return true;
        } catch (IOException e) {
            zzawy().zzazd().zzj("Failed to write to channel", e);
            return false;
        }
    }

    private static boolean zza(zzcmb zzcmb, String str, Object obj) {
        zzcmc[] zzcmcArr;
        if (TextUtils.isEmpty(str) || obj == null) {
            return false;
        }
        for (zzcmc zzcmc : zzcmb.zzjlh) {
            if (str.equals(zzcmc.name)) {
                return ((obj instanceof Long) && obj.equals(zzcmc.zzjll)) || ((obj instanceof String) && obj.equals(zzcmc.zzgcc)) || ((obj instanceof Double) && obj.equals(zzcmc.zzjjl));
            }
        }
        return false;
    }

    private final boolean zza(String str, zzcha zzcha) {
        long j;
        zzclp zzclp;
        String string = zzcha.zzizt.getString(Param.CURRENCY);
        if (Event.ECOMMERCE_PURCHASE.equals(zzcha.name)) {
            double doubleValue = zzcha.zzizt.getDouble(Param.VALUE).doubleValue() * 1000000.0d;
            if (doubleValue == 0.0d) {
                doubleValue = ((double) zzcha.zzizt.getLong(Param.VALUE).longValue()) * 1000000.0d;
            }
            if (doubleValue > 9.223372036854776E18d || doubleValue < -9.223372036854776E18d) {
                zzawy().zzazf().zze("Data lost. Currency value is too big. appId", zzchm.zzjk(str), Double.valueOf(doubleValue));
                return false;
            }
            j = Math.round(doubleValue);
        } else {
            j = zzcha.zzizt.getLong(Param.VALUE).longValue();
        }
        if (!TextUtils.isEmpty(string)) {
            String upperCase = string.toUpperCase(Locale.US);
            if (upperCase.matches("[A-Z]{3}")) {
                String valueOf = String.valueOf("_ltv_");
                String valueOf2 = String.valueOf(upperCase);
                String concat = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
                zzclp zzag = zzaws().zzag(str, concat);
                if (zzag == null || !(zzag.mValue instanceof Long)) {
                    zzcgo zzaws = zzaws();
                    int zzb = this.zzjew.zzb(str, zzchc.zzjbh) - 1;
                    zzbq.zzgm(str);
                    zzaws.zzve();
                    zzaws.zzxf();
                    try {
                        zzaws.getWritableDatabase().execSQL("delete from user_attributes where app_id=? and name in (select name from user_attributes where app_id=? and name like '_ltv_%' order by set_timestamp desc limit ?,10);", new String[]{str, str, String.valueOf(zzb)});
                    } catch (SQLiteException e) {
                        zzaws.zzawy().zzazd().zze("Error pruning currencies. appId", zzchm.zzjk(str), e);
                    }
                    zzclp = new zzclp(str, zzcha.zziyf, concat, this.zzata.currentTimeMillis(), Long.valueOf(j));
                } else {
                    zzclp zzclp2 = new zzclp(str, zzcha.zziyf, concat, this.zzata.currentTimeMillis(), Long.valueOf(((Long) zzag.mValue).longValue() + j));
                    zzclp = zzclp2;
                }
                if (!zzaws().zza(zzclp)) {
                    zzawy().zzazd().zzd("Too many unique user properties are set. Ignoring user property. appId", zzchm.zzjk(str), zzawt().zzjj(zzclp.mName), zzclp.mValue);
                    zzawu().zza(str, 9, (String) null, (String) null, 0);
                }
            }
        }
        return true;
    }

    private final zzcma[] zza(String str, zzcmg[] zzcmgArr, zzcmb[] zzcmbArr) {
        zzbq.zzgm(str);
        return zzawl().zza(str, zzcmbArr, zzcmgArr);
    }

    static void zzawi() {
        throw new IllegalStateException("Unexpected call on client side");
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzazw() {
        zzcho zzcho;
        String str;
        zzawx().zzve();
        this.zzjfe.zzazw();
        this.zzjex.zzazw();
        this.zzjfn.zzazw();
        zzawy().zzazh().zzj("App measurement is starting up, version", Long.valueOf(11910));
        zzawy().zzazh().log("To enable debug logging run: adb shell setprop log.tag.FA VERBOSE");
        String appId = this.zzjfn.getAppId();
        if (zzawu().zzkj(appId)) {
            zzcho = zzawy().zzazh();
            str = "Faster debug mode event logging enabled. To disable, run:\n  adb shell setprop debug.firebase.analytics.app .none.";
        } else {
            zzcho = zzawy().zzazh();
            String str2 = "To enable faster debug mode event logging run:\n  adb shell setprop debug.firebase.analytics.app ";
            String valueOf = String.valueOf(appId);
            str = valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2);
        }
        zzcho.log(str);
        zzawy().zzazi().log("Debug-level message logging enabled");
        if (this.zzjfz != this.zzjga) {
            zzawy().zzazd().zze("Not all components initialized", Integer.valueOf(this.zzjfz), Integer.valueOf(this.zzjga));
        }
        this.zzdtb = true;
    }

    @WorkerThread
    private final void zzb(zzcgh zzcgh) {
        Map map;
        zzawx().zzve();
        if (TextUtils.isEmpty(zzcgh.getGmpAppId())) {
            zzb(zzcgh.getAppId(), 204, null, null, null);
            return;
        }
        String gmpAppId = zzcgh.getGmpAppId();
        String appInstanceId = zzcgh.getAppInstanceId();
        Builder builder = new Builder();
        Builder encodedAuthority = builder.scheme((String) zzchc.zzjah.get()).encodedAuthority((String) zzchc.zzjai.get());
        String str = "config/app/";
        String valueOf = String.valueOf(gmpAppId);
        encodedAuthority.path(valueOf.length() != 0 ? str.concat(valueOf) : new String(str)).appendQueryParameter("app_instance_id", appInstanceId).appendQueryParameter("platform", "android").appendQueryParameter("gmp_version", "11910");
        String uri = builder.build().toString();
        try {
            URL url = new URL(uri);
            zzawy().zzazj().zzj("Fetching remote configuration", zzcgh.getAppId());
            zzcly zzjs = zzawv().zzjs(zzcgh.getAppId());
            String zzjt = zzawv().zzjt(zzcgh.getAppId());
            if (zzjs == null || TextUtils.isEmpty(zzjt)) {
                map = null;
            } else {
                ArrayMap arrayMap = new ArrayMap();
                arrayMap.put("If-Modified-Since", zzjt);
                map = arrayMap;
            }
            this.zzjgd = true;
            zzchq zzbab = zzbab();
            String appId = zzcgh.getAppId();
            zzciq zzciq = new zzciq(this);
            zzbab.zzve();
            zzbab.zzxf();
            zzbq.checkNotNull(url);
            zzbq.checkNotNull(zzciq);
            zzcih zzawx = zzbab.zzawx();
            zzchu zzchu = new zzchu(zzbab, appId, url, null, map, zzciq);
            zzawx.zzh(zzchu);
        } catch (MalformedURLException e) {
            zzawy().zzazd().zze("Failed to parse config URL. Not fetching. appId", zzchm.zzjk(zzcgh.getAppId()), uri);
        }
    }

    private final zzchv zzbac() {
        if (this.zzjfo != null) {
            return this.zzjfo;
        }
        throw new IllegalStateException("Network broadcast receiver not created");
    }

    private final zzcll zzbad() {
        zza((zzcjl) this.zzjfp);
        return this.zzjfp;
    }

    @WorkerThread
    private final boolean zzbae() {
        String str;
        zzcho zzcho;
        zzawx().zzve();
        try {
            this.zzjfw = new RandomAccessFile(new File(this.mContext.getFilesDir(), "google_app_measurement.db"), "rw").getChannel();
            this.zzjfv = this.zzjfw.tryLock();
            if (this.zzjfv != null) {
                zzawy().zzazj().log("Storage concurrent access okay");
                return true;
            }
            zzawy().zzazd().log("Storage concurrent data access panic");
            return false;
        } catch (FileNotFoundException e) {
            e = e;
            zzcho = zzawy().zzazd();
            str = "Failed to acquire storage lock";
            zzcho.zzj(str, e);
            return false;
        } catch (IOException e2) {
            e = e2;
            zzcho = zzawy().zzazd();
            str = "Failed to access storage lock file";
            zzcho.zzj(str, e);
            return false;
        }
    }

    private final long zzbag() {
        long currentTimeMillis = this.zzata.currentTimeMillis();
        zzchx zzawz = zzawz();
        zzawz.zzxf();
        zzawz.zzve();
        long j = zzawz.zzjcv.get();
        if (j == 0) {
            j = 1 + ((long) zzawz.zzawu().zzbaz().nextInt(86400000));
            zzawz.zzjcv.set(j);
        }
        return ((((currentTimeMillis + j) / 1000) / 60) / 60) / 24;
    }

    private final boolean zzbai() {
        zzawx().zzve();
        zzxf();
        return zzaws().zzayk() || !TextUtils.isEmpty(zzaws().zzayf());
    }

    /* JADX WARNING: Removed duplicated region for block: B:52:0x0167  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0183  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void zzbaj() {
        /*
            r20 = this;
            r0 = r20
            com.google.android.gms.internal.zzcih r1 = r20.zzawx()
            r1.zzve()
            r20.zzxf()
            boolean r1 = r20.zzbam()
            if (r1 != 0) goto L_0x0013
            return
        L_0x0013:
            long r1 = r0.zzjgc
            r3 = 0
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 <= 0) goto L_0x0052
            com.google.android.gms.common.util.zzd r1 = r0.zzata
            long r1 = r1.elapsedRealtime()
            r5 = 3600000(0x36ee80, double:1.7786363E-317)
            long r7 = r0.zzjgc
            long r1 = r1 - r7
            long r1 = java.lang.Math.abs(r1)
            long r5 = r5 - r1
            int r1 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r1 <= 0) goto L_0x0050
            com.google.android.gms.internal.zzchm r1 = r20.zzawy()
            com.google.android.gms.internal.zzcho r1 = r1.zzazj()
            java.lang.String r2 = "Upload has been suspended. Will update scheduling later in approximately ms"
            java.lang.Long r3 = java.lang.Long.valueOf(r5)
            r1.zzj(r2, r3)
            com.google.android.gms.internal.zzchv r1 = r20.zzbac()
            r1.unregister()
            com.google.android.gms.internal.zzcll r1 = r20.zzbad()
            r1.cancel()
            return
        L_0x0050:
            r0.zzjgc = r3
        L_0x0052:
            boolean r1 = r20.zzazv()
            if (r1 == 0) goto L_0x021c
            boolean r1 = r20.zzbai()
            if (r1 != 0) goto L_0x0060
            goto L_0x021c
        L_0x0060:
            com.google.android.gms.common.util.zzd r1 = r0.zzata
            long r1 = r1.currentTimeMillis()
            com.google.android.gms.internal.zzchd<java.lang.Long> r5 = com.google.android.gms.internal.zzchc.zzjbd
            java.lang.Object r5 = r5.get()
            java.lang.Long r5 = (java.lang.Long) r5
            long r5 = r5.longValue()
            long r5 = java.lang.Math.max(r3, r5)
            com.google.android.gms.internal.zzcgo r7 = r20.zzaws()
            boolean r7 = r7.zzayl()
            if (r7 != 0) goto L_0x008d
            com.google.android.gms.internal.zzcgo r7 = r20.zzaws()
            boolean r7 = r7.zzayg()
            if (r7 == 0) goto L_0x008b
            goto L_0x008d
        L_0x008b:
            r7 = 0
            goto L_0x008e
        L_0x008d:
            r7 = 1
        L_0x008e:
            if (r7 == 0) goto L_0x00aa
            com.google.android.gms.internal.zzcgn r9 = r0.zzjew
            java.lang.String r9 = r9.zzayd()
            boolean r10 = android.text.TextUtils.isEmpty(r9)
            if (r10 != 0) goto L_0x00a7
            java.lang.String r10 = ".none."
            boolean r9 = r10.equals(r9)
            if (r9 != 0) goto L_0x00a7
            com.google.android.gms.internal.zzchd<java.lang.Long> r9 = com.google.android.gms.internal.zzchc.zzjay
            goto L_0x00ac
        L_0x00a7:
            com.google.android.gms.internal.zzchd<java.lang.Long> r9 = com.google.android.gms.internal.zzchc.zzjax
            goto L_0x00ac
        L_0x00aa:
            com.google.android.gms.internal.zzchd<java.lang.Long> r9 = com.google.android.gms.internal.zzchc.zzjaw
        L_0x00ac:
            java.lang.Object r9 = r9.get()
            java.lang.Long r9 = (java.lang.Long) r9
            long r9 = r9.longValue()
            long r9 = java.lang.Math.max(r3, r9)
            com.google.android.gms.internal.zzchx r11 = r20.zzawz()
            com.google.android.gms.internal.zzcia r11 = r11.zzjcr
            long r11 = r11.get()
            com.google.android.gms.internal.zzchx r13 = r20.zzawz()
            com.google.android.gms.internal.zzcia r13 = r13.zzjcs
            long r13 = r13.get()
            com.google.android.gms.internal.zzcgo r15 = r20.zzaws()
            r16 = r9
            long r8 = r15.zzayi()
            com.google.android.gms.internal.zzcgo r10 = r20.zzaws()
            r18 = r5
            long r5 = r10.zzayj()
            long r5 = java.lang.Math.max(r8, r5)
            int r8 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r8 != 0) goto L_0x00ed
        L_0x00ea:
            r8 = r3
            goto L_0x0163
        L_0x00ed:
            r8 = 0
            long r5 = r5 - r1
            long r5 = java.lang.Math.abs(r5)
            long r5 = r1 - r5
            long r11 = r11 - r1
            long r8 = java.lang.Math.abs(r11)
            long r8 = r1 - r8
            long r13 = r13 - r1
            long r10 = java.lang.Math.abs(r13)
            long r1 = r1 - r10
            long r8 = java.lang.Math.max(r8, r1)
            long r10 = r5 + r18
            if (r7 == 0) goto L_0x0114
            int r7 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
            if (r7 <= 0) goto L_0x0114
            long r10 = java.lang.Math.min(r5, r8)
            long r10 = r10 + r16
        L_0x0114:
            com.google.android.gms.internal.zzclq r7 = r20.zzawu()
            r12 = r16
            boolean r7 = r7.zzf(r8, r12)
            if (r7 != 0) goto L_0x0122
            long r8 = r8 + r12
            goto L_0x0123
        L_0x0122:
            r8 = r10
        L_0x0123:
            int r7 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r7 == 0) goto L_0x0163
            int r5 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r5 < 0) goto L_0x0163
            r5 = 0
        L_0x012c:
            r6 = 20
            com.google.android.gms.internal.zzchd<java.lang.Integer> r7 = com.google.android.gms.internal.zzchc.zzjbf
            java.lang.Object r7 = r7.get()
            java.lang.Integer r7 = (java.lang.Integer) r7
            int r7 = r7.intValue()
            r10 = 0
            int r7 = java.lang.Math.max(r10, r7)
            int r6 = java.lang.Math.min(r6, r7)
            if (r5 >= r6) goto L_0x00ea
            r6 = 1
            long r6 = r6 << r5
            com.google.android.gms.internal.zzchd<java.lang.Long> r11 = com.google.android.gms.internal.zzchc.zzjbe
            java.lang.Object r11 = r11.get()
            java.lang.Long r11 = (java.lang.Long) r11
            long r11 = r11.longValue()
            long r11 = java.lang.Math.max(r3, r11)
            long r11 = r11 * r6
            long r8 = r8 + r11
            int r6 = (r8 > r1 ? 1 : (r8 == r1 ? 0 : -1))
            if (r6 <= 0) goto L_0x0160
            goto L_0x0163
        L_0x0160:
            int r5 = r5 + 1
            goto L_0x012c
        L_0x0163:
            int r1 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
            if (r1 != 0) goto L_0x0183
            com.google.android.gms.internal.zzchm r1 = r20.zzawy()
            com.google.android.gms.internal.zzcho r1 = r1.zzazj()
            java.lang.String r2 = "Next upload time is 0"
            r1.log(r2)
            com.google.android.gms.internal.zzchv r1 = r20.zzbac()
            r1.unregister()
            com.google.android.gms.internal.zzcll r1 = r20.zzbad()
            r1.cancel()
            return
        L_0x0183:
            com.google.android.gms.internal.zzchq r1 = r20.zzbab()
            boolean r1 = r1.zzzs()
            if (r1 != 0) goto L_0x01a9
            com.google.android.gms.internal.zzchm r1 = r20.zzawy()
            com.google.android.gms.internal.zzcho r1 = r1.zzazj()
            java.lang.String r2 = "No network"
            r1.log(r2)
            com.google.android.gms.internal.zzchv r1 = r20.zzbac()
            r1.zzzp()
            com.google.android.gms.internal.zzcll r1 = r20.zzbad()
            r1.cancel()
            return
        L_0x01a9:
            com.google.android.gms.internal.zzchx r1 = r20.zzawz()
            com.google.android.gms.internal.zzcia r1 = r1.zzjct
            long r1 = r1.get()
            com.google.android.gms.internal.zzchd<java.lang.Long> r5 = com.google.android.gms.internal.zzchc.zzjau
            java.lang.Object r5 = r5.get()
            java.lang.Long r5 = (java.lang.Long) r5
            long r5 = r5.longValue()
            long r5 = java.lang.Math.max(r3, r5)
            com.google.android.gms.internal.zzclq r7 = r20.zzawu()
            boolean r7 = r7.zzf(r1, r5)
            if (r7 != 0) goto L_0x01d2
            long r1 = r1 + r5
            long r8 = java.lang.Math.max(r8, r1)
        L_0x01d2:
            com.google.android.gms.internal.zzchv r1 = r20.zzbac()
            r1.unregister()
            com.google.android.gms.common.util.zzd r1 = r0.zzata
            long r1 = r1.currentTimeMillis()
            long r8 = r8 - r1
            int r1 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
            if (r1 > 0) goto L_0x0203
            com.google.android.gms.internal.zzchd<java.lang.Long> r1 = com.google.android.gms.internal.zzchc.zzjaz
            java.lang.Object r1 = r1.get()
            java.lang.Long r1 = (java.lang.Long) r1
            long r1 = r1.longValue()
            long r8 = java.lang.Math.max(r3, r1)
            com.google.android.gms.internal.zzchx r1 = r20.zzawz()
            com.google.android.gms.internal.zzcia r1 = r1.zzjcr
            com.google.android.gms.common.util.zzd r2 = r0.zzata
            long r2 = r2.currentTimeMillis()
            r1.set(r2)
        L_0x0203:
            com.google.android.gms.internal.zzchm r1 = r20.zzawy()
            com.google.android.gms.internal.zzcho r1 = r1.zzazj()
            java.lang.String r2 = "Upload scheduled in approximately ms"
            java.lang.Long r3 = java.lang.Long.valueOf(r8)
            r1.zzj(r2, r3)
            com.google.android.gms.internal.zzcll r1 = r20.zzbad()
            r1.zzs(r8)
            return
        L_0x021c:
            com.google.android.gms.internal.zzchm r1 = r20.zzawy()
            com.google.android.gms.internal.zzcho r1 = r1.zzazj()
            java.lang.String r2 = "Nothing to upload or uploading impossible"
            r1.log(r2)
            com.google.android.gms.internal.zzchv r1 = r20.zzbac()
            r1.unregister()
            com.google.android.gms.internal.zzcll r1 = r20.zzbad()
            r1.cancel()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcim.zzbaj():void");
    }

    @WorkerThread
    private final boolean zzbam() {
        zzawx().zzve();
        zzxf();
        return this.zzjfs;
    }

    @WorkerThread
    private final void zzban() {
        zzawx().zzve();
        if (this.zzjgd || this.zzjge || this.zzjgf) {
            zzawy().zzazj().zzd("Not stopping services. fetch, network, upload", Boolean.valueOf(this.zzjgd), Boolean.valueOf(this.zzjge), Boolean.valueOf(this.zzjgf));
            return;
        }
        zzawy().zzazj().log("Stopping uploading service(s)");
        if (this.zzjfy != null) {
            for (Runnable run : this.zzjfy) {
                run.run();
            }
            this.zzjfy.clear();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:143:0x0584 A[Catch:{ IOException -> 0x0589, all -> 0x05ef }] */
    /* JADX WARNING: Removed duplicated region for block: B:148:0x05b0 A[Catch:{ IOException -> 0x0589, all -> 0x05ef }] */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void zzc(com.google.android.gms.internal.zzcha r34, com.google.android.gms.internal.zzcgi r35) {
        /*
            r33 = this;
            r11 = r33
            r1 = r34
            r12 = r35
            com.google.android.gms.common.internal.zzbq.checkNotNull(r35)
            java.lang.String r2 = r12.packageName
            com.google.android.gms.common.internal.zzbq.zzgm(r2)
            long r13 = java.lang.System.nanoTime()
            com.google.android.gms.internal.zzcih r2 = r33.zzawx()
            r2.zzve()
            r33.zzxf()
            java.lang.String r10 = r12.packageName
            r33.zzawu()
            boolean r2 = com.google.android.gms.internal.zzclq.zzd(r34, r35)
            if (r2 != 0) goto L_0x0028
            return
        L_0x0028:
            boolean r2 = r12.zzixx
            if (r2 != 0) goto L_0x0030
            r11.zzg(r12)
            return
        L_0x0030:
            com.google.android.gms.internal.zzcig r2 = r33.zzawv()
            java.lang.String r3 = r1.name
            boolean r2 = r2.zzan(r10, r3)
            if (r2 == 0) goto L_0x00ce
            com.google.android.gms.internal.zzchm r2 = r33.zzawy()
            com.google.android.gms.internal.zzcho r2 = r2.zzazf()
            java.lang.String r3 = "Dropping blacklisted event. appId"
            java.lang.Object r4 = com.google.android.gms.internal.zzchm.zzjk(r10)
            com.google.android.gms.internal.zzchk r5 = r33.zzawt()
            java.lang.String r6 = r1.name
            java.lang.String r5 = r5.zzjh(r6)
            r2.zze(r3, r4, r5)
            com.google.android.gms.internal.zzclq r2 = r33.zzawu()
            boolean r2 = r2.zzkl(r10)
            if (r2 != 0) goto L_0x006e
            com.google.android.gms.internal.zzclq r2 = r33.zzawu()
            boolean r2 = r2.zzkm(r10)
            if (r2 == 0) goto L_0x006c
            goto L_0x006e
        L_0x006c:
            r9 = 0
            goto L_0x006f
        L_0x006e:
            r9 = 1
        L_0x006f:
            if (r9 != 0) goto L_0x008a
            java.lang.String r2 = "_err"
            java.lang.String r3 = r1.name
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x008a
            com.google.android.gms.internal.zzclq r3 = r33.zzawu()
            r5 = 11
            java.lang.String r6 = "_ev"
            java.lang.String r7 = r1.name
            r8 = 0
            r4 = r10
            r3.zza(r4, r5, r6, r7, r8)
        L_0x008a:
            if (r9 == 0) goto L_0x00cd
            com.google.android.gms.internal.zzcgo r1 = r33.zzaws()
            com.google.android.gms.internal.zzcgh r1 = r1.zzjb(r10)
            if (r1 == 0) goto L_0x00cd
            long r2 = r1.zzaxn()
            long r4 = r1.zzaxm()
            long r2 = java.lang.Math.max(r2, r4)
            com.google.android.gms.common.util.zzd r4 = r11.zzata
            long r4 = r4.currentTimeMillis()
            long r4 = r4 - r2
            long r2 = java.lang.Math.abs(r4)
            com.google.android.gms.internal.zzchd<java.lang.Long> r4 = com.google.android.gms.internal.zzchc.zzjbc
            java.lang.Object r4 = r4.get()
            java.lang.Long r4 = (java.lang.Long) r4
            long r4 = r4.longValue()
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 <= 0) goto L_0x00cd
            com.google.android.gms.internal.zzchm r2 = r33.zzawy()
            com.google.android.gms.internal.zzcho r2 = r2.zzazi()
            java.lang.String r3 = "Fetching config for blacklisted app"
            r2.log(r3)
            r11.zzb(r1)
        L_0x00cd:
            return
        L_0x00ce:
            com.google.android.gms.internal.zzchm r2 = r33.zzawy()
            r6 = 2
            boolean r2 = r2.zzae(r6)
            if (r2 == 0) goto L_0x00ee
            com.google.android.gms.internal.zzchm r2 = r33.zzawy()
            com.google.android.gms.internal.zzcho r2 = r2.zzazj()
            java.lang.String r3 = "Logging event"
            com.google.android.gms.internal.zzchk r4 = r33.zzawt()
            java.lang.String r4 = r4.zzb(r1)
            r2.zzj(r3, r4)
        L_0x00ee:
            com.google.android.gms.internal.zzcgo r2 = r33.zzaws()
            r2.beginTransaction()
            r11.zzg(r12)     // Catch:{ all -> 0x05ef }
            java.lang.String r2 = "_iap"
            java.lang.String r3 = r1.name     // Catch:{ all -> 0x05ef }
            boolean r2 = r2.equals(r3)     // Catch:{ all -> 0x05ef }
            if (r2 != 0) goto L_0x010c
            java.lang.String r2 = "ecommerce_purchase"
            java.lang.String r3 = r1.name     // Catch:{ all -> 0x05ef }
            boolean r2 = r2.equals(r3)     // Catch:{ all -> 0x05ef }
            if (r2 == 0) goto L_0x0121
        L_0x010c:
            boolean r2 = r11.zza(r10, r1)     // Catch:{ all -> 0x05ef }
            if (r2 != 0) goto L_0x0121
            com.google.android.gms.internal.zzcgo r1 = r33.zzaws()     // Catch:{ all -> 0x05ef }
            r1.setTransactionSuccessful()     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgo r1 = r33.zzaws()
            r1.endTransaction()
            return
        L_0x0121:
            java.lang.String r2 = r1.name     // Catch:{ all -> 0x05ef }
            boolean r24 = com.google.android.gms.internal.zzclq.zzjz(r2)     // Catch:{ all -> 0x05ef }
            java.lang.String r2 = "_err"
            java.lang.String r3 = r1.name     // Catch:{ all -> 0x05ef }
            boolean r2 = r2.equals(r3)     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgo r15 = r33.zzaws()     // Catch:{ all -> 0x05ef }
            long r16 = r33.zzbag()     // Catch:{ all -> 0x05ef }
            r19 = 1
            r21 = 0
            r23 = 0
            r18 = r10
            r20 = r24
            r22 = r2
            com.google.android.gms.internal.zzcgp r3 = r15.zza(r16, r18, r19, r20, r21, r22, r23)     // Catch:{ all -> 0x05ef }
            long r4 = r3.zziyy     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzchd<java.lang.Integer> r7 = com.google.android.gms.internal.zzchc.zzjan     // Catch:{ all -> 0x05ef }
            java.lang.Object r7 = r7.get()     // Catch:{ all -> 0x05ef }
            java.lang.Integer r7 = (java.lang.Integer) r7     // Catch:{ all -> 0x05ef }
            int r7 = r7.intValue()     // Catch:{ all -> 0x05ef }
            long r6 = (long) r7     // Catch:{ all -> 0x05ef }
            long r4 = r4 - r6
            r6 = 0
            int r15 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            r16 = 1000(0x3e8, double:4.94E-321)
            r8 = 1
            if (r15 <= 0) goto L_0x018d
            long r4 = r4 % r16
            int r1 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r1 != 0) goto L_0x017e
            com.google.android.gms.internal.zzchm r1 = r33.zzawy()     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcho r1 = r1.zzazd()     // Catch:{ all -> 0x05ef }
            java.lang.String r2 = "Data loss. Too many events logged. appId, count"
            java.lang.Object r4 = com.google.android.gms.internal.zzchm.zzjk(r10)     // Catch:{ all -> 0x05ef }
            long r5 = r3.zziyy     // Catch:{ all -> 0x05ef }
            java.lang.Long r3 = java.lang.Long.valueOf(r5)     // Catch:{ all -> 0x05ef }
            r1.zze(r2, r4, r3)     // Catch:{ all -> 0x05ef }
        L_0x017e:
            com.google.android.gms.internal.zzcgo r1 = r33.zzaws()     // Catch:{ all -> 0x05ef }
            r1.setTransactionSuccessful()     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgo r1 = r33.zzaws()
            r1.endTransaction()
            return
        L_0x018d:
            if (r24 == 0) goto L_0x01e0
            long r4 = r3.zziyx     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzchd<java.lang.Integer> r15 = com.google.android.gms.internal.zzchc.zzjap     // Catch:{ all -> 0x05ef }
            java.lang.Object r15 = r15.get()     // Catch:{ all -> 0x05ef }
            java.lang.Integer r15 = (java.lang.Integer) r15     // Catch:{ all -> 0x05ef }
            int r15 = r15.intValue()     // Catch:{ all -> 0x05ef }
            long r8 = (long) r15     // Catch:{ all -> 0x05ef }
            long r4 = r4 - r8
            int r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r8 <= 0) goto L_0x01e0
            long r4 = r4 % r16
            r6 = 1
            int r2 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r2 != 0) goto L_0x01c2
            com.google.android.gms.internal.zzchm r2 = r33.zzawy()     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcho r2 = r2.zzazd()     // Catch:{ all -> 0x05ef }
            java.lang.String r4 = "Data loss. Too many public events logged. appId, count"
            java.lang.Object r5 = com.google.android.gms.internal.zzchm.zzjk(r10)     // Catch:{ all -> 0x05ef }
            long r6 = r3.zziyx     // Catch:{ all -> 0x05ef }
            java.lang.Long r3 = java.lang.Long.valueOf(r6)     // Catch:{ all -> 0x05ef }
            r2.zze(r4, r5, r3)     // Catch:{ all -> 0x05ef }
        L_0x01c2:
            com.google.android.gms.internal.zzclq r3 = r33.zzawu()     // Catch:{ all -> 0x05ef }
            r5 = 16
            java.lang.String r6 = "_ev"
            java.lang.String r7 = r1.name     // Catch:{ all -> 0x05ef }
            r8 = 0
            r4 = r10
            r3.zza(r4, r5, r6, r7, r8)     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgo r1 = r33.zzaws()     // Catch:{ all -> 0x05ef }
            r1.setTransactionSuccessful()     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgo r1 = r33.zzaws()
            r1.endTransaction()
            return
        L_0x01e0:
            if (r2 == 0) goto L_0x022c
            long r4 = r3.zziza     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgn r2 = r11.zzjew     // Catch:{ all -> 0x05ef }
            java.lang.String r8 = r12.packageName     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzchd<java.lang.Integer> r9 = com.google.android.gms.internal.zzchc.zzjao     // Catch:{ all -> 0x05ef }
            int r2 = r2.zzb(r8, r9)     // Catch:{ all -> 0x05ef }
            r8 = 1000000(0xf4240, float:1.401298E-39)
            int r2 = java.lang.Math.min(r8, r2)     // Catch:{ all -> 0x05ef }
            r8 = 0
            int r2 = java.lang.Math.max(r8, r2)     // Catch:{ all -> 0x05ef }
            long r8 = (long) r2     // Catch:{ all -> 0x05ef }
            long r4 = r4 - r8
            int r2 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r2 <= 0) goto L_0x022c
            r8 = 1
            int r1 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r1 != 0) goto L_0x021d
            com.google.android.gms.internal.zzchm r1 = r33.zzawy()     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcho r1 = r1.zzazd()     // Catch:{ all -> 0x05ef }
            java.lang.String r2 = "Too many error events logged. appId, count"
            java.lang.Object r4 = com.google.android.gms.internal.zzchm.zzjk(r10)     // Catch:{ all -> 0x05ef }
            long r5 = r3.zziza     // Catch:{ all -> 0x05ef }
            java.lang.Long r3 = java.lang.Long.valueOf(r5)     // Catch:{ all -> 0x05ef }
            r1.zze(r2, r4, r3)     // Catch:{ all -> 0x05ef }
        L_0x021d:
            com.google.android.gms.internal.zzcgo r1 = r33.zzaws()     // Catch:{ all -> 0x05ef }
            r1.setTransactionSuccessful()     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgo r1 = r33.zzaws()
            r1.endTransaction()
            return
        L_0x022c:
            com.google.android.gms.internal.zzcgx r2 = r1.zzizt     // Catch:{ all -> 0x05ef }
            android.os.Bundle r15 = r2.zzayx()     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzclq r2 = r33.zzawu()     // Catch:{ all -> 0x05ef }
            java.lang.String r3 = "_o"
            java.lang.String r4 = r1.zziyf     // Catch:{ all -> 0x05ef }
            r2.zza(r15, r3, r4)     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzclq r2 = r33.zzawu()     // Catch:{ all -> 0x05ef }
            boolean r2 = r2.zzkj(r10)     // Catch:{ all -> 0x05ef }
            if (r2 == 0) goto L_0x0263
            com.google.android.gms.internal.zzclq r2 = r33.zzawu()     // Catch:{ all -> 0x05ef }
            java.lang.String r3 = "_dbg"
            r4 = 1
            java.lang.Long r8 = java.lang.Long.valueOf(r4)     // Catch:{ all -> 0x05ef }
            r2.zza(r15, r3, r8)     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzclq r2 = r33.zzawu()     // Catch:{ all -> 0x05ef }
            java.lang.String r3 = "_r"
            java.lang.Long r4 = java.lang.Long.valueOf(r4)     // Catch:{ all -> 0x05ef }
            r2.zza(r15, r3, r4)     // Catch:{ all -> 0x05ef }
        L_0x0263:
            com.google.android.gms.internal.zzcgo r2 = r33.zzaws()     // Catch:{ all -> 0x05ef }
            long r2 = r2.zzjc(r10)     // Catch:{ all -> 0x05ef }
            int r4 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r4 <= 0) goto L_0x0284
            com.google.android.gms.internal.zzchm r4 = r33.zzawy()     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcho r4 = r4.zzazf()     // Catch:{ all -> 0x05ef }
            java.lang.String r5 = "Data lost. Too many events stored on disk, deleted. appId"
            java.lang.Object r8 = com.google.android.gms.internal.zzchm.zzjk(r10)     // Catch:{ all -> 0x05ef }
            java.lang.Long r2 = java.lang.Long.valueOf(r2)     // Catch:{ all -> 0x05ef }
            r4.zze(r5, r8, r2)     // Catch:{ all -> 0x05ef }
        L_0x0284:
            com.google.android.gms.internal.zzcgv r8 = new com.google.android.gms.internal.zzcgv     // Catch:{ all -> 0x05ef }
            java.lang.String r3 = r1.zziyf     // Catch:{ all -> 0x05ef }
            java.lang.String r5 = r1.name     // Catch:{ all -> 0x05ef }
            long r1 = r1.zzizu     // Catch:{ all -> 0x05ef }
            r16 = 0
            r18 = r1
            r1 = r8
            r2 = r11
            r4 = r10
            r30 = r13
            r9 = 2
            r13 = r6
            r6 = r18
            r13 = r8
            r14 = 1
            r29 = 0
            r8 = r16
            r14 = r10
            r10 = r15
            r1.<init>(r2, r3, r4, r5, r6, r8, r10)     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgo r1 = r33.zzaws()     // Catch:{ all -> 0x05ef }
            java.lang.String r2 = r13.mName     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgw r1 = r1.zzae(r14, r2)     // Catch:{ all -> 0x05ef }
            if (r1 != 0) goto L_0x0313
            com.google.android.gms.internal.zzcgo r1 = r33.zzaws()     // Catch:{ all -> 0x05ef }
            long r1 = r1.zzjf(r14)     // Catch:{ all -> 0x05ef }
            r3 = 500(0x1f4, double:2.47E-321)
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 < 0) goto L_0x02f6
            if (r24 == 0) goto L_0x02f6
            com.google.android.gms.internal.zzchm r1 = r33.zzawy()     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcho r1 = r1.zzazd()     // Catch:{ all -> 0x05ef }
            java.lang.String r2 = "Too many event names used, ignoring event. appId, name, supported count"
            java.lang.Object r3 = com.google.android.gms.internal.zzchm.zzjk(r14)     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzchk r4 = r33.zzawt()     // Catch:{ all -> 0x05ef }
            java.lang.String r5 = r13.mName     // Catch:{ all -> 0x05ef }
            java.lang.String r4 = r4.zzjh(r5)     // Catch:{ all -> 0x05ef }
            r5 = 500(0x1f4, float:7.0E-43)
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ all -> 0x05ef }
            r1.zzd(r2, r3, r4, r5)     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzclq r3 = r33.zzawu()     // Catch:{ all -> 0x05ef }
            r5 = 8
            r6 = 0
            r7 = 0
            r8 = 0
            r4 = r14
            r3.zza(r4, r5, r6, r7, r8)     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgo r1 = r33.zzaws()
            r1.endTransaction()
            return
        L_0x02f6:
            com.google.android.gms.internal.zzcgw r1 = new com.google.android.gms.internal.zzcgw     // Catch:{ all -> 0x05ef }
            java.lang.String r2 = r13.mName     // Catch:{ all -> 0x05ef }
            r18 = 0
            r20 = 0
            long r3 = r13.zzfij     // Catch:{ all -> 0x05ef }
            r24 = 0
            r26 = 0
            r27 = 0
            r28 = 0
            r15 = r1
            r16 = r14
            r17 = r2
            r22 = r3
            r15.<init>(r16, r17, r18, r20, r22, r24, r26, r27, r28)     // Catch:{ all -> 0x05ef }
            goto L_0x0320
        L_0x0313:
            long r2 = r1.zzizm     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgv r8 = r13.zza(r11, r2)     // Catch:{ all -> 0x05ef }
            long r2 = r8.zzfij     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgw r1 = r1.zzbb(r2)     // Catch:{ all -> 0x05ef }
            r13 = r8
        L_0x0320:
            com.google.android.gms.internal.zzcgo r2 = r33.zzaws()     // Catch:{ all -> 0x05ef }
            r2.zza(r1)     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcih r1 = r33.zzawx()     // Catch:{ all -> 0x05ef }
            r1.zzve()     // Catch:{ all -> 0x05ef }
            r33.zzxf()     // Catch:{ all -> 0x05ef }
            com.google.android.gms.common.internal.zzbq.checkNotNull(r13)     // Catch:{ all -> 0x05ef }
            com.google.android.gms.common.internal.zzbq.checkNotNull(r35)     // Catch:{ all -> 0x05ef }
            java.lang.String r1 = r13.mAppId     // Catch:{ all -> 0x05ef }
            com.google.android.gms.common.internal.zzbq.zzgm(r1)     // Catch:{ all -> 0x05ef }
            java.lang.String r1 = r13.mAppId     // Catch:{ all -> 0x05ef }
            java.lang.String r2 = r12.packageName     // Catch:{ all -> 0x05ef }
            boolean r1 = r1.equals(r2)     // Catch:{ all -> 0x05ef }
            com.google.android.gms.common.internal.zzbq.checkArgument(r1)     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcme r1 = new com.google.android.gms.internal.zzcme     // Catch:{ all -> 0x05ef }
            r1.<init>()     // Catch:{ all -> 0x05ef }
            r2 = 1
            java.lang.Integer r3 = java.lang.Integer.valueOf(r2)     // Catch:{ all -> 0x05ef }
            r1.zzjlo = r3     // Catch:{ all -> 0x05ef }
            java.lang.String r3 = "android"
            r1.zzjlw = r3     // Catch:{ all -> 0x05ef }
            java.lang.String r3 = r12.packageName     // Catch:{ all -> 0x05ef }
            r1.zzcn = r3     // Catch:{ all -> 0x05ef }
            java.lang.String r3 = r12.zzixt     // Catch:{ all -> 0x05ef }
            r1.zzixt = r3     // Catch:{ all -> 0x05ef }
            java.lang.String r3 = r12.zzifm     // Catch:{ all -> 0x05ef }
            r1.zzifm = r3     // Catch:{ all -> 0x05ef }
            long r3 = r12.zzixz     // Catch:{ all -> 0x05ef }
            r5 = -2147483648(0xffffffff80000000, double:NaN)
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            r4 = 0
            if (r3 != 0) goto L_0x036f
            r3 = r4
            goto L_0x0376
        L_0x036f:
            long r5 = r12.zzixz     // Catch:{ all -> 0x05ef }
            int r3 = (int) r5     // Catch:{ all -> 0x05ef }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x05ef }
        L_0x0376:
            r1.zzjmj = r3     // Catch:{ all -> 0x05ef }
            long r5 = r12.zzixu     // Catch:{ all -> 0x05ef }
            java.lang.Long r3 = java.lang.Long.valueOf(r5)     // Catch:{ all -> 0x05ef }
            r1.zzjma = r3     // Catch:{ all -> 0x05ef }
            java.lang.String r3 = r12.zzixs     // Catch:{ all -> 0x05ef }
            r1.zzixs = r3     // Catch:{ all -> 0x05ef }
            long r5 = r12.zzixv     // Catch:{ all -> 0x05ef }
            r7 = 0
            int r3 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r3 != 0) goto L_0x038e
            r3 = r4
            goto L_0x0394
        L_0x038e:
            long r5 = r12.zzixv     // Catch:{ all -> 0x05ef }
            java.lang.Long r3 = java.lang.Long.valueOf(r5)     // Catch:{ all -> 0x05ef }
        L_0x0394:
            r1.zzjmf = r3     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzchx r3 = r33.zzawz()     // Catch:{ all -> 0x05ef }
            java.lang.String r5 = r12.packageName     // Catch:{ all -> 0x05ef }
            android.util.Pair r3 = r3.zzjm(r5)     // Catch:{ all -> 0x05ef }
            if (r3 == 0) goto L_0x03bd
            java.lang.Object r5 = r3.first     // Catch:{ all -> 0x05ef }
            java.lang.CharSequence r5 = (java.lang.CharSequence) r5     // Catch:{ all -> 0x05ef }
            boolean r5 = android.text.TextUtils.isEmpty(r5)     // Catch:{ all -> 0x05ef }
            if (r5 != 0) goto L_0x03bd
            boolean r5 = r12.zziye     // Catch:{ all -> 0x05ef }
            if (r5 == 0) goto L_0x0408
            java.lang.Object r5 = r3.first     // Catch:{ all -> 0x05ef }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ all -> 0x05ef }
            r1.zzjmc = r5     // Catch:{ all -> 0x05ef }
            java.lang.Object r3 = r3.second     // Catch:{ all -> 0x05ef }
            java.lang.Boolean r3 = (java.lang.Boolean) r3     // Catch:{ all -> 0x05ef }
            r1.zzjmd = r3     // Catch:{ all -> 0x05ef }
            goto L_0x0408
        L_0x03bd:
            com.google.android.gms.internal.zzcgu r3 = r33.zzawo()     // Catch:{ all -> 0x05ef }
            android.content.Context r5 = r11.mContext     // Catch:{ all -> 0x05ef }
            boolean r3 = r3.zzdw(r5)     // Catch:{ all -> 0x05ef }
            if (r3 != 0) goto L_0x0408
            android.content.Context r3 = r11.mContext     // Catch:{ all -> 0x05ef }
            android.content.ContentResolver r3 = r3.getContentResolver()     // Catch:{ all -> 0x05ef }
            java.lang.String r5 = "android_id"
            java.lang.String r3 = android.provider.Settings.Secure.getString(r3, r5)     // Catch:{ all -> 0x05ef }
            if (r3 != 0) goto L_0x03ed
            com.google.android.gms.internal.zzchm r3 = r33.zzawy()     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcho r3 = r3.zzazf()     // Catch:{ all -> 0x05ef }
            java.lang.String r5 = "null secure ID. appId"
            java.lang.String r6 = r1.zzcn     // Catch:{ all -> 0x05ef }
            java.lang.Object r6 = com.google.android.gms.internal.zzchm.zzjk(r6)     // Catch:{ all -> 0x05ef }
            r3.zzj(r5, r6)     // Catch:{ all -> 0x05ef }
            java.lang.String r3 = "null"
            goto L_0x0406
        L_0x03ed:
            boolean r5 = r3.isEmpty()     // Catch:{ all -> 0x05ef }
            if (r5 == 0) goto L_0x0406
            com.google.android.gms.internal.zzchm r5 = r33.zzawy()     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcho r5 = r5.zzazf()     // Catch:{ all -> 0x05ef }
            java.lang.String r6 = "empty secure ID. appId"
            java.lang.String r7 = r1.zzcn     // Catch:{ all -> 0x05ef }
            java.lang.Object r7 = com.google.android.gms.internal.zzchm.zzjk(r7)     // Catch:{ all -> 0x05ef }
            r5.zzj(r6, r7)     // Catch:{ all -> 0x05ef }
        L_0x0406:
            r1.zzjmm = r3     // Catch:{ all -> 0x05ef }
        L_0x0408:
            com.google.android.gms.internal.zzcgu r3 = r33.zzawo()     // Catch:{ all -> 0x05ef }
            r3.zzxf()     // Catch:{ all -> 0x05ef }
            java.lang.String r3 = android.os.Build.MODEL     // Catch:{ all -> 0x05ef }
            r1.zzjlx = r3     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgu r3 = r33.zzawo()     // Catch:{ all -> 0x05ef }
            r3.zzxf()     // Catch:{ all -> 0x05ef }
            java.lang.String r3 = android.os.Build.VERSION.RELEASE     // Catch:{ all -> 0x05ef }
            r1.zzdb = r3     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgu r3 = r33.zzawo()     // Catch:{ all -> 0x05ef }
            long r5 = r3.zzayu()     // Catch:{ all -> 0x05ef }
            int r3 = (int) r5     // Catch:{ all -> 0x05ef }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x05ef }
            r1.zzjlz = r3     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgu r3 = r33.zzawo()     // Catch:{ all -> 0x05ef }
            java.lang.String r3 = r3.zzayv()     // Catch:{ all -> 0x05ef }
            r1.zzjly = r3     // Catch:{ all -> 0x05ef }
            r1.zzjmb = r4     // Catch:{ all -> 0x05ef }
            r1.zzjlr = r4     // Catch:{ all -> 0x05ef }
            r1.zzjls = r4     // Catch:{ all -> 0x05ef }
            r1.zzjlt = r4     // Catch:{ all -> 0x05ef }
            long r5 = r12.zziyb     // Catch:{ all -> 0x05ef }
            java.lang.Long r3 = java.lang.Long.valueOf(r5)     // Catch:{ all -> 0x05ef }
            r1.zzfkk = r3     // Catch:{ all -> 0x05ef }
            boolean r3 = r33.isEnabled()     // Catch:{ all -> 0x05ef }
            if (r3 == 0) goto L_0x0458
            boolean r3 = com.google.android.gms.internal.zzcgn.zzaye()     // Catch:{ all -> 0x05ef }
            if (r3 == 0) goto L_0x0458
            r33.zzawn()     // Catch:{ all -> 0x05ef }
            r1.zzjmo = r4     // Catch:{ all -> 0x05ef }
        L_0x0458:
            com.google.android.gms.internal.zzcgo r3 = r33.zzaws()     // Catch:{ all -> 0x05ef }
            java.lang.String r4 = r12.packageName     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgh r3 = r3.zzjb(r4)     // Catch:{ all -> 0x05ef }
            if (r3 != 0) goto L_0x04c2
            com.google.android.gms.internal.zzcgh r3 = new com.google.android.gms.internal.zzcgh     // Catch:{ all -> 0x05ef }
            java.lang.String r4 = r12.packageName     // Catch:{ all -> 0x05ef }
            r3.<init>(r11, r4)     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzchh r4 = r33.zzawn()     // Catch:{ all -> 0x05ef }
            java.lang.String r4 = r4.zzayz()     // Catch:{ all -> 0x05ef }
            r3.zzir(r4)     // Catch:{ all -> 0x05ef }
            java.lang.String r4 = r12.zziya     // Catch:{ all -> 0x05ef }
            r3.zziu(r4)     // Catch:{ all -> 0x05ef }
            java.lang.String r4 = r12.zzixs     // Catch:{ all -> 0x05ef }
            r3.zzis(r4)     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzchx r4 = r33.zzawz()     // Catch:{ all -> 0x05ef }
            java.lang.String r5 = r12.packageName     // Catch:{ all -> 0x05ef }
            java.lang.String r4 = r4.zzjn(r5)     // Catch:{ all -> 0x05ef }
            r3.zzit(r4)     // Catch:{ all -> 0x05ef }
            r4 = 0
            r3.zzaq(r4)     // Catch:{ all -> 0x05ef }
            r3.zzal(r4)     // Catch:{ all -> 0x05ef }
            r3.zzam(r4)     // Catch:{ all -> 0x05ef }
            java.lang.String r4 = r12.zzifm     // Catch:{ all -> 0x05ef }
            r3.setAppVersion(r4)     // Catch:{ all -> 0x05ef }
            long r4 = r12.zzixz     // Catch:{ all -> 0x05ef }
            r3.zzan(r4)     // Catch:{ all -> 0x05ef }
            java.lang.String r4 = r12.zzixt     // Catch:{ all -> 0x05ef }
            r3.zziv(r4)     // Catch:{ all -> 0x05ef }
            long r4 = r12.zzixu     // Catch:{ all -> 0x05ef }
            r3.zzao(r4)     // Catch:{ all -> 0x05ef }
            long r4 = r12.zzixv     // Catch:{ all -> 0x05ef }
            r3.zzap(r4)     // Catch:{ all -> 0x05ef }
            boolean r4 = r12.zzixx     // Catch:{ all -> 0x05ef }
            r3.setMeasurementEnabled(r4)     // Catch:{ all -> 0x05ef }
            long r4 = r12.zziyb     // Catch:{ all -> 0x05ef }
            r3.zzaz(r4)     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgo r4 = r33.zzaws()     // Catch:{ all -> 0x05ef }
            r4.zza(r3)     // Catch:{ all -> 0x05ef }
        L_0x04c2:
            java.lang.String r4 = r3.getAppInstanceId()     // Catch:{ all -> 0x05ef }
            r1.zzjme = r4     // Catch:{ all -> 0x05ef }
            java.lang.String r3 = r3.zzaxd()     // Catch:{ all -> 0x05ef }
            r1.zziya = r3     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgo r3 = r33.zzaws()     // Catch:{ all -> 0x05ef }
            java.lang.String r4 = r12.packageName     // Catch:{ all -> 0x05ef }
            java.util.List r3 = r3.zzja(r4)     // Catch:{ all -> 0x05ef }
            int r4 = r3.size()     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcmg[] r4 = new com.google.android.gms.internal.zzcmg[r4]     // Catch:{ all -> 0x05ef }
            r1.zzjlq = r4     // Catch:{ all -> 0x05ef }
            r4 = 0
        L_0x04e1:
            int r5 = r3.size()     // Catch:{ all -> 0x05ef }
            if (r4 >= r5) goto L_0x051a
            com.google.android.gms.internal.zzcmg r5 = new com.google.android.gms.internal.zzcmg     // Catch:{ all -> 0x05ef }
            r5.<init>()     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcmg[] r6 = r1.zzjlq     // Catch:{ all -> 0x05ef }
            r6[r4] = r5     // Catch:{ all -> 0x05ef }
            java.lang.Object r6 = r3.get(r4)     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzclp r6 = (com.google.android.gms.internal.zzclp) r6     // Catch:{ all -> 0x05ef }
            java.lang.String r6 = r6.mName     // Catch:{ all -> 0x05ef }
            r5.name = r6     // Catch:{ all -> 0x05ef }
            java.lang.Object r6 = r3.get(r4)     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzclp r6 = (com.google.android.gms.internal.zzclp) r6     // Catch:{ all -> 0x05ef }
            long r6 = r6.zzjjm     // Catch:{ all -> 0x05ef }
            java.lang.Long r6 = java.lang.Long.valueOf(r6)     // Catch:{ all -> 0x05ef }
            r5.zzjms = r6     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzclq r6 = r33.zzawu()     // Catch:{ all -> 0x05ef }
            java.lang.Object r7 = r3.get(r4)     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzclp r7 = (com.google.android.gms.internal.zzclp) r7     // Catch:{ all -> 0x05ef }
            java.lang.Object r7 = r7.mValue     // Catch:{ all -> 0x05ef }
            r6.zza(r5, r7)     // Catch:{ all -> 0x05ef }
            int r4 = r4 + 1
            goto L_0x04e1
        L_0x051a:
            com.google.android.gms.internal.zzcgo r3 = r33.zzaws()     // Catch:{ IOException -> 0x0589 }
            long r3 = r3.zza(r1)     // Catch:{ IOException -> 0x0589 }
            com.google.android.gms.internal.zzcgo r1 = r33.zzaws()     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgx r5 = r13.zzizj     // Catch:{ all -> 0x05ef }
            if (r5 == 0) goto L_0x057d
            com.google.android.gms.internal.zzcgx r5 = r13.zzizj     // Catch:{ all -> 0x05ef }
            java.util.Iterator r5 = r5.iterator()     // Catch:{ all -> 0x05ef }
        L_0x0530:
            boolean r6 = r5.hasNext()     // Catch:{ all -> 0x05ef }
            if (r6 == 0) goto L_0x0545
            java.lang.Object r6 = r5.next()     // Catch:{ all -> 0x05ef }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ all -> 0x05ef }
            java.lang.String r7 = "_r"
            boolean r6 = r7.equals(r6)     // Catch:{ all -> 0x05ef }
            if (r6 == 0) goto L_0x0530
            goto L_0x057e
        L_0x0545:
            com.google.android.gms.internal.zzcig r5 = r33.zzawv()     // Catch:{ all -> 0x05ef }
            java.lang.String r6 = r13.mAppId     // Catch:{ all -> 0x05ef }
            java.lang.String r7 = r13.mName     // Catch:{ all -> 0x05ef }
            boolean r5 = r5.zzao(r6, r7)     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgo r14 = r33.zzaws()     // Catch:{ all -> 0x05ef }
            long r15 = r33.zzbag()     // Catch:{ all -> 0x05ef }
            java.lang.String r6 = r13.mAppId     // Catch:{ all -> 0x05ef }
            r18 = 0
            r19 = 0
            r20 = 0
            r21 = 0
            r22 = 0
            r17 = r6
            com.google.android.gms.internal.zzcgp r6 = r14.zza(r15, r17, r18, r19, r20, r21, r22)     // Catch:{ all -> 0x05ef }
            if (r5 == 0) goto L_0x057d
            long r5 = r6.zzizb     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcgn r7 = r11.zzjew     // Catch:{ all -> 0x05ef }
            java.lang.String r8 = r13.mAppId     // Catch:{ all -> 0x05ef }
            int r7 = r7.zzix(r8)     // Catch:{ all -> 0x05ef }
            long r7 = (long) r7     // Catch:{ all -> 0x05ef }
            int r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r5 >= 0) goto L_0x057d
            goto L_0x057e
        L_0x057d:
            r2 = 0
        L_0x057e:
            boolean r1 = r1.zza(r13, r3, r2)     // Catch:{ all -> 0x05ef }
            if (r1 == 0) goto L_0x059e
            r1 = 0
            r11.zzjgc = r1     // Catch:{ all -> 0x05ef }
            goto L_0x059e
        L_0x0589:
            r0 = move-exception
            r2 = r0
            com.google.android.gms.internal.zzchm r3 = r33.zzawy()     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcho r3 = r3.zzazd()     // Catch:{ all -> 0x05ef }
            java.lang.String r4 = "Data loss. Failed to insert raw event metadata. appId"
            java.lang.String r1 = r1.zzcn     // Catch:{ all -> 0x05ef }
            java.lang.Object r1 = com.google.android.gms.internal.zzchm.zzjk(r1)     // Catch:{ all -> 0x05ef }
            r3.zze(r4, r1, r2)     // Catch:{ all -> 0x05ef }
        L_0x059e:
            com.google.android.gms.internal.zzcgo r1 = r33.zzaws()     // Catch:{ all -> 0x05ef }
            r1.setTransactionSuccessful()     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzchm r1 = r33.zzawy()     // Catch:{ all -> 0x05ef }
            r2 = 2
            boolean r1 = r1.zzae(r2)     // Catch:{ all -> 0x05ef }
            if (r1 == 0) goto L_0x05c5
            com.google.android.gms.internal.zzchm r1 = r33.zzawy()     // Catch:{ all -> 0x05ef }
            com.google.android.gms.internal.zzcho r1 = r1.zzazj()     // Catch:{ all -> 0x05ef }
            java.lang.String r2 = "Event recorded"
            com.google.android.gms.internal.zzchk r3 = r33.zzawt()     // Catch:{ all -> 0x05ef }
            java.lang.String r3 = r3.zza(r13)     // Catch:{ all -> 0x05ef }
            r1.zzj(r2, r3)     // Catch:{ all -> 0x05ef }
        L_0x05c5:
            com.google.android.gms.internal.zzcgo r1 = r33.zzaws()
            r1.endTransaction()
            r33.zzbaj()
            com.google.android.gms.internal.zzchm r1 = r33.zzawy()
            com.google.android.gms.internal.zzcho r1 = r1.zzazj()
            java.lang.String r2 = "Background event processing time, ms"
            long r3 = java.lang.System.nanoTime()
            long r3 = r3 - r30
            r5 = 500000(0x7a120, double:2.47033E-318)
            long r3 = r3 + r5
            r5 = 1000000(0xf4240, double:4.940656E-318)
            long r3 = r3 / r5
            java.lang.Long r3 = java.lang.Long.valueOf(r3)
            r1.zzj(r2, r3)
            return
        L_0x05ef:
            r0 = move-exception
            r1 = r0
            com.google.android.gms.internal.zzcgo r2 = r33.zzaws()
            r2.endTransaction()
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcim.zzc(com.google.android.gms.internal.zzcha, com.google.android.gms.internal.zzcgi):void");
    }

    public static zzcim zzdx(Context context) {
        zzbq.checkNotNull(context);
        zzbq.checkNotNull(context.getApplicationContext());
        if (zzjev == null) {
            synchronized (zzcim.class) {
                if (zzjev == null) {
                    zzjev = new zzcim(new zzcjm(context));
                }
            }
        }
        return zzjev;
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x00ca  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00f0  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00fe  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0128  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0136  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x013e  */
    /* JADX WARNING: Removed duplicated region for block: B:56:? A[RETURN, SYNTHETIC] */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void zzg(com.google.android.gms.internal.zzcgi r8) {
        /*
            r7 = this;
            com.google.android.gms.internal.zzcih r0 = r7.zzawx()
            r0.zzve()
            r7.zzxf()
            com.google.android.gms.common.internal.zzbq.checkNotNull(r8)
            java.lang.String r0 = r8.packageName
            com.google.android.gms.common.internal.zzbq.zzgm(r0)
            com.google.android.gms.internal.zzcgo r0 = r7.zzaws()
            java.lang.String r1 = r8.packageName
            com.google.android.gms.internal.zzcgh r0 = r0.zzjb(r1)
            com.google.android.gms.internal.zzchx r1 = r7.zzawz()
            java.lang.String r2 = r8.packageName
            java.lang.String r1 = r1.zzjn(r2)
            r2 = 1
            if (r0 != 0) goto L_0x0040
            com.google.android.gms.internal.zzcgh r0 = new com.google.android.gms.internal.zzcgh
            java.lang.String r3 = r8.packageName
            r0.<init>(r7, r3)
            com.google.android.gms.internal.zzchh r3 = r7.zzawn()
            java.lang.String r3 = r3.zzayz()
            r0.zzir(r3)
            r0.zzit(r1)
        L_0x003e:
            r1 = 1
            goto L_0x005a
        L_0x0040:
            java.lang.String r3 = r0.zzaxc()
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x0059
            r0.zzit(r1)
            com.google.android.gms.internal.zzchh r1 = r7.zzawn()
            java.lang.String r1 = r1.zzayz()
            r0.zzir(r1)
            goto L_0x003e
        L_0x0059:
            r1 = 0
        L_0x005a:
            java.lang.String r3 = r8.zzixs
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 != 0) goto L_0x0074
            java.lang.String r3 = r8.zzixs
            java.lang.String r4 = r0.getGmpAppId()
            boolean r3 = r3.equals(r4)
            if (r3 != 0) goto L_0x0074
            java.lang.String r1 = r8.zzixs
            r0.zzis(r1)
            r1 = 1
        L_0x0074:
            java.lang.String r3 = r8.zziya
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 != 0) goto L_0x008e
            java.lang.String r3 = r8.zziya
            java.lang.String r4 = r0.zzaxd()
            boolean r3 = r3.equals(r4)
            if (r3 != 0) goto L_0x008e
            java.lang.String r1 = r8.zziya
            r0.zziu(r1)
            r1 = 1
        L_0x008e:
            long r3 = r8.zzixu
            r5 = 0
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 == 0) goto L_0x00a6
            long r3 = r8.zzixu
            long r5 = r0.zzaxi()
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 == 0) goto L_0x00a6
            long r3 = r8.zzixu
            r0.zzao(r3)
            r1 = 1
        L_0x00a6:
            java.lang.String r3 = r8.zzifm
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 != 0) goto L_0x00c0
            java.lang.String r3 = r8.zzifm
            java.lang.String r4 = r0.zzvj()
            boolean r3 = r3.equals(r4)
            if (r3 != 0) goto L_0x00c0
            java.lang.String r1 = r8.zzifm
            r0.setAppVersion(r1)
            r1 = 1
        L_0x00c0:
            long r3 = r8.zzixz
            long r5 = r0.zzaxg()
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 == 0) goto L_0x00d0
            long r3 = r8.zzixz
            r0.zzan(r3)
            r1 = 1
        L_0x00d0:
            java.lang.String r3 = r8.zzixt
            if (r3 == 0) goto L_0x00e6
            java.lang.String r3 = r8.zzixt
            java.lang.String r4 = r0.zzaxh()
            boolean r3 = r3.equals(r4)
            if (r3 != 0) goto L_0x00e6
            java.lang.String r1 = r8.zzixt
            r0.zziv(r1)
            r1 = 1
        L_0x00e6:
            long r3 = r8.zzixv
            long r5 = r0.zzaxj()
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 == 0) goto L_0x00f6
            long r3 = r8.zzixv
            r0.zzap(r3)
            r1 = 1
        L_0x00f6:
            boolean r3 = r8.zzixx
            boolean r4 = r0.zzaxk()
            if (r3 == r4) goto L_0x0104
            boolean r1 = r8.zzixx
            r0.setMeasurementEnabled(r1)
            r1 = 1
        L_0x0104:
            java.lang.String r3 = r8.zzixw
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 != 0) goto L_0x011e
            java.lang.String r3 = r8.zzixw
            java.lang.String r4 = r0.zzaxv()
            boolean r3 = r3.equals(r4)
            if (r3 != 0) goto L_0x011e
            java.lang.String r1 = r8.zzixw
            r0.zziw(r1)
            r1 = 1
        L_0x011e:
            long r3 = r8.zziyb
            long r5 = r0.zzaxx()
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 == 0) goto L_0x012e
            long r3 = r8.zziyb
            r0.zzaz(r3)
            r1 = 1
        L_0x012e:
            boolean r3 = r8.zziye
            boolean r4 = r0.zzaxy()
            if (r3 == r4) goto L_0x013c
            boolean r8 = r8.zziye
            r0.zzbl(r8)
            r1 = 1
        L_0x013c:
            if (r1 == 0) goto L_0x0145
            com.google.android.gms.internal.zzcgo r8 = r7.zzaws()
            r8.zza(r0)
        L_0x0145:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcim.zzg(com.google.android.gms.internal.zzcgi):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:111:0x0217, code lost:
        if (r5 != null) goto L_0x01cb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0040, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:121:0x023b, code lost:
        if (r9 == null) goto L_0x027a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:123:?, code lost:
        r9.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0041, code lost:
        r2 = r0;
        r9 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:134:0x0258, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:135:0x0259, code lost:
        r9 = r3;
        r12 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0045, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:142:0x0277, code lost:
        if (r9 != null) goto L_0x023d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0046, code lost:
        r9 = null;
        r12 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:171:0x035e, code lost:
        if (com.google.android.gms.internal.zzclq.zzkn(((com.google.android.gms.internal.zzcmb) r2.zzapa.get(r4)).name) != false) goto L_0x0360;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0084, code lost:
        if (r3 != null) goto L_0x0086;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x01c9, code lost:
        if (r5 != null) goto L_0x01cb;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0040 A[ExcHandler: all (r0v20 'th' java.lang.Throwable A[CUSTOM_DECLARE]), PHI: r3 
      PHI: (r3v63 android.database.Cursor) = (r3v57 android.database.Cursor), (r3v66 android.database.Cursor), (r3v66 android.database.Cursor), (r3v66 android.database.Cursor), (r3v66 android.database.Cursor), (r3v1 android.database.Cursor), (r3v1 android.database.Cursor) binds: [B:45:0x00e0, B:23:0x007e, B:29:0x008b, B:31:0x008f, B:32:?, B:9:0x0031, B:10:?] A[DONT_GENERATE, DONT_INLINE], Splitter:B:9:0x0031] */
    /* JADX WARNING: Removed duplicated region for block: B:146:0x027e A[Catch:{ SQLiteException -> 0x09af, all -> 0x09ea }] */
    /* JADX WARNING: Removed duplicated region for block: B:152:0x028c A[Catch:{ SQLiteException -> 0x09af, all -> 0x09ea }] */
    /* JADX WARNING: Removed duplicated region for block: B:312:0x0897 A[Catch:{ SQLiteException -> 0x09af, all -> 0x09ea }] */
    /* JADX WARNING: Removed duplicated region for block: B:321:0x08d6 A[Catch:{ SQLiteException -> 0x09af, all -> 0x09ea }] */
    /* JADX WARNING: Removed duplicated region for block: B:322:0x08ec A[Catch:{ SQLiteException -> 0x09af, all -> 0x09ea }] */
    /* JADX WARNING: Removed duplicated region for block: B:337:0x0948 A[Catch:{ SQLiteException -> 0x09af, all -> 0x09ea }] */
    /* JADX WARNING: Removed duplicated region for block: B:360:0x09d2 A[SYNTHETIC, Splitter:B:360:0x09d2] */
    /* JADX WARNING: Removed duplicated region for block: B:367:0x09e6 A[SYNTHETIC, Splitter:B:367:0x09e6] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0120 A[SYNTHETIC, Splitter:B:58:0x0120] */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0142 A[SYNTHETIC, Splitter:B:67:0x0142] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:27:0x0086=Splitter:B:27:0x0086, B:143:0x027a=Splitter:B:143:0x027a, B:96:0x01cb=Splitter:B:96:0x01cb, B:122:0x023d=Splitter:B:122:0x023d} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean zzg(java.lang.String r47, long r48) {
        /*
            r46 = this;
            r1 = r46
            com.google.android.gms.internal.zzcgo r2 = r46.zzaws()
            r2.beginTransaction()
            com.google.android.gms.internal.zzcim$zza r2 = new com.google.android.gms.internal.zzcim$zza     // Catch:{ all -> 0x09ea }
            r3 = 0
            r2.<init>(r1, r3)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcgo r4 = r46.zzaws()     // Catch:{ all -> 0x09ea }
            long r5 = r1.zzjgb     // Catch:{ all -> 0x09ea }
            com.google.android.gms.common.internal.zzbq.checkNotNull(r2)     // Catch:{ all -> 0x09ea }
            r4.zzve()     // Catch:{ all -> 0x09ea }
            r4.zzxf()     // Catch:{ all -> 0x09ea }
            r7 = -1
            r9 = 2
            r10 = 0
            r11 = 1
            android.database.sqlite.SQLiteDatabase r15 = r4.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            boolean r12 = android.text.TextUtils.isEmpty(r3)     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            if (r12 == 0) goto L_0x009f
            int r12 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r12 == 0) goto L_0x004b
            java.lang.String[] r13 = new java.lang.String[r9]     // Catch:{ SQLiteException -> 0x0045, all -> 0x0040 }
            java.lang.String r14 = java.lang.String.valueOf(r5)     // Catch:{ SQLiteException -> 0x0045, all -> 0x0040 }
            r13[r10] = r14     // Catch:{ SQLiteException -> 0x0045, all -> 0x0040 }
            java.lang.String r14 = java.lang.String.valueOf(r48)     // Catch:{ SQLiteException -> 0x0045, all -> 0x0040 }
            r13[r11] = r14     // Catch:{ SQLiteException -> 0x0045, all -> 0x0040 }
            goto L_0x0053
        L_0x0040:
            r0 = move-exception
            r2 = r0
            r9 = r3
            goto L_0x09e4
        L_0x0045:
            r0 = move-exception
            r9 = r3
            r12 = r9
        L_0x0048:
            r3 = r0
            goto L_0x0266
        L_0x004b:
            java.lang.String[] r13 = new java.lang.String[r11]     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            java.lang.String r14 = java.lang.String.valueOf(r48)     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            r13[r10] = r14     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
        L_0x0053:
            if (r12 == 0) goto L_0x0058
            java.lang.String r12 = "rowid <= ? and "
            goto L_0x005a
        L_0x0058:
            java.lang.String r12 = ""
        L_0x005a:
            java.lang.String r14 = java.lang.String.valueOf(r12)     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            int r14 = r14.length()     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            int r14 = r14 + 148
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            r3.<init>(r14)     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            java.lang.String r14 = "select app_id, metadata_fingerprint from raw_events where "
            r3.append(r14)     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            r3.append(r12)     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            java.lang.String r12 = "app_id in (select app_id from apps where config_fetched_time >= ?) order by rowid limit 1;"
            r3.append(r12)     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            java.lang.String r3 = r3.toString()     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            android.database.Cursor r3 = r15.rawQuery(r3, r13)     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            boolean r12 = r3.moveToFirst()     // Catch:{ SQLiteException -> 0x0258, all -> 0x0040 }
            if (r12 != 0) goto L_0x008b
            if (r3 == 0) goto L_0x027a
        L_0x0086:
            r3.close()     // Catch:{ all -> 0x09ea }
            goto L_0x027a
        L_0x008b:
            java.lang.String r12 = r3.getString(r10)     // Catch:{ SQLiteException -> 0x0258, all -> 0x0040 }
            java.lang.String r13 = r3.getString(r11)     // Catch:{ SQLiteException -> 0x009c, all -> 0x0040 }
            r3.close()     // Catch:{ SQLiteException -> 0x009c, all -> 0x0040 }
            r22 = r3
            r3 = r12
            r21 = r13
            goto L_0x00f5
        L_0x009c:
            r0 = move-exception
            r9 = r3
            goto L_0x0048
        L_0x009f:
            int r3 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r3 == 0) goto L_0x00af
            java.lang.String[] r12 = new java.lang.String[r9]     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            r13 = 0
            r12[r10] = r13     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            java.lang.String r13 = java.lang.String.valueOf(r5)     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            r12[r11] = r13     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            goto L_0x00b5
        L_0x00af:
            r12 = 0
            java.lang.String[] r13 = new java.lang.String[]{r12}     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            r12 = r13
        L_0x00b5:
            if (r3 == 0) goto L_0x00ba
            java.lang.String r3 = " and rowid <= ?"
            goto L_0x00bc
        L_0x00ba:
            java.lang.String r3 = ""
        L_0x00bc:
            java.lang.String r13 = java.lang.String.valueOf(r3)     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            int r13 = r13.length()     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            int r13 = r13 + 84
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            r14.<init>(r13)     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            java.lang.String r13 = "select metadata_fingerprint from raw_events where app_id = ?"
            r14.append(r13)     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            r14.append(r3)     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            java.lang.String r3 = " order by rowid limit 1;"
            r14.append(r3)     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            java.lang.String r3 = r14.toString()     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            android.database.Cursor r3 = r15.rawQuery(r3, r12)     // Catch:{ SQLiteException -> 0x0262, all -> 0x025d }
            boolean r12 = r3.moveToFirst()     // Catch:{ SQLiteException -> 0x0258, all -> 0x0040 }
            if (r12 != 0) goto L_0x00e9
            if (r3 == 0) goto L_0x027a
            goto L_0x0086
        L_0x00e9:
            java.lang.String r13 = r3.getString(r10)     // Catch:{ SQLiteException -> 0x0258, all -> 0x0040 }
            r3.close()     // Catch:{ SQLiteException -> 0x0258, all -> 0x0040 }
            r22 = r3
            r21 = r13
            r3 = 0
        L_0x00f5:
            java.lang.String r13 = "raw_events_metadata"
            java.lang.String r12 = "metadata"
            java.lang.String[] r14 = new java.lang.String[]{r12}     // Catch:{ SQLiteException -> 0x0252, all -> 0x024c }
            java.lang.String r16 = "app_id = ? and metadata_fingerprint = ?"
            java.lang.String[] r12 = new java.lang.String[r9]     // Catch:{ SQLiteException -> 0x0252, all -> 0x024c }
            r12[r10] = r3     // Catch:{ SQLiteException -> 0x0252, all -> 0x024c }
            r12[r11] = r21     // Catch:{ SQLiteException -> 0x0252, all -> 0x024c }
            r17 = 0
            r18 = 0
            java.lang.String r19 = "rowid"
            java.lang.String r20 = "2"
            r23 = r12
            r12 = r15
            r24 = r15
            r15 = r16
            r16 = r23
            android.database.Cursor r15 = r12.query(r13, r14, r15, r16, r17, r18, r19, r20)     // Catch:{ SQLiteException -> 0x0252, all -> 0x024c }
            boolean r12 = r15.moveToFirst()     // Catch:{ SQLiteException -> 0x0247, all -> 0x0243 }
            if (r12 != 0) goto L_0x0142
            com.google.android.gms.internal.zzchm r5 = r4.zzawy()     // Catch:{ SQLiteException -> 0x013d, all -> 0x0138 }
            com.google.android.gms.internal.zzcho r5 = r5.zzazd()     // Catch:{ SQLiteException -> 0x013d, all -> 0x0138 }
            java.lang.String r6 = "Raw event metadata record is missing. appId"
            java.lang.Object r12 = com.google.android.gms.internal.zzchm.zzjk(r3)     // Catch:{ SQLiteException -> 0x013d, all -> 0x0138 }
            r5.zzj(r6, r12)     // Catch:{ SQLiteException -> 0x013d, all -> 0x0138 }
            if (r15 == 0) goto L_0x027a
            r15.close()     // Catch:{ all -> 0x09ea }
            goto L_0x027a
        L_0x0138:
            r0 = move-exception
            r2 = r0
            r9 = r15
            goto L_0x09e4
        L_0x013d:
            r0 = move-exception
            r12 = r3
            r9 = r15
            goto L_0x0048
        L_0x0142:
            byte[] r12 = r15.getBlob(r10)     // Catch:{ SQLiteException -> 0x0247, all -> 0x0243 }
            int r13 = r12.length     // Catch:{ SQLiteException -> 0x0247, all -> 0x0243 }
            com.google.android.gms.internal.zzfjj r12 = com.google.android.gms.internal.zzfjj.zzn(r12, r10, r13)     // Catch:{ SQLiteException -> 0x0247, all -> 0x0243 }
            com.google.android.gms.internal.zzcme r13 = new com.google.android.gms.internal.zzcme     // Catch:{ SQLiteException -> 0x0247, all -> 0x0243 }
            r13.<init>()     // Catch:{ SQLiteException -> 0x0247, all -> 0x0243 }
            r13.zza(r12)     // Catch:{ IOException -> 0x0228 }
            boolean r12 = r15.moveToNext()     // Catch:{ SQLiteException -> 0x0247, all -> 0x0243 }
            if (r12 == 0) goto L_0x016a
            com.google.android.gms.internal.zzchm r12 = r4.zzawy()     // Catch:{ SQLiteException -> 0x013d, all -> 0x0138 }
            com.google.android.gms.internal.zzcho r12 = r12.zzazf()     // Catch:{ SQLiteException -> 0x013d, all -> 0x0138 }
            java.lang.String r14 = "Get multiple raw event metadata records, expected one. appId"
            java.lang.Object r9 = com.google.android.gms.internal.zzchm.zzjk(r3)     // Catch:{ SQLiteException -> 0x013d, all -> 0x0138 }
            r12.zzj(r14, r9)     // Catch:{ SQLiteException -> 0x013d, all -> 0x0138 }
        L_0x016a:
            r15.close()     // Catch:{ SQLiteException -> 0x0247, all -> 0x0243 }
            r2.zzb(r13)     // Catch:{ SQLiteException -> 0x0247, all -> 0x0243 }
            int r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            r14 = 3
            if (r9 == 0) goto L_0x0188
            java.lang.String r9 = "app_id = ? and metadata_fingerprint = ? and rowid <= ?"
            java.lang.String[] r12 = new java.lang.String[r14]     // Catch:{ SQLiteException -> 0x013d, all -> 0x0138 }
            r12[r10] = r3     // Catch:{ SQLiteException -> 0x013d, all -> 0x0138 }
            r12[r11] = r21     // Catch:{ SQLiteException -> 0x013d, all -> 0x0138 }
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ SQLiteException -> 0x013d, all -> 0x0138 }
            r6 = 2
            r12[r6] = r5     // Catch:{ SQLiteException -> 0x013d, all -> 0x0138 }
            r5 = r9
            r16 = r12
            goto L_0x0193
        L_0x0188:
            java.lang.String r5 = "app_id = ? and metadata_fingerprint = ?"
            r6 = 2
            java.lang.String[] r9 = new java.lang.String[r6]     // Catch:{ SQLiteException -> 0x0247, all -> 0x0243 }
            r9[r10] = r3     // Catch:{ SQLiteException -> 0x0247, all -> 0x0243 }
            r9[r11] = r21     // Catch:{ SQLiteException -> 0x0247, all -> 0x0243 }
            r16 = r9
        L_0x0193:
            java.lang.String r13 = "raw_events"
            java.lang.String r6 = "rowid"
            java.lang.String r9 = "name"
            java.lang.String r12 = "timestamp"
            java.lang.String r14 = "data"
            java.lang.String[] r14 = new java.lang.String[]{r6, r9, r12, r14}     // Catch:{ SQLiteException -> 0x0247, all -> 0x0243 }
            r17 = 0
            r18 = 0
            java.lang.String r19 = "rowid"
            r20 = 0
            r12 = r24
            r6 = 3
            r9 = r15
            r15 = r5
            android.database.Cursor r5 = r12.query(r13, r14, r15, r16, r17, r18, r19, r20)     // Catch:{ SQLiteException -> 0x0241 }
            boolean r9 = r5.moveToFirst()     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            if (r9 != 0) goto L_0x01d0
            com.google.android.gms.internal.zzchm r6 = r4.zzawy()     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            com.google.android.gms.internal.zzcho r6 = r6.zzazf()     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            java.lang.String r9 = "Raw event data disappeared while in transaction. appId"
            java.lang.Object r12 = com.google.android.gms.internal.zzchm.zzjk(r3)     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            r6.zzj(r9, r12)     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            if (r5 == 0) goto L_0x027a
        L_0x01cb:
            r5.close()     // Catch:{ all -> 0x09ea }
            goto L_0x027a
        L_0x01d0:
            long r12 = r5.getLong(r10)     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            byte[] r9 = r5.getBlob(r6)     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            int r14 = r9.length     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            com.google.android.gms.internal.zzfjj r9 = com.google.android.gms.internal.zzfjj.zzn(r9, r10, r14)     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            com.google.android.gms.internal.zzcmb r14 = new com.google.android.gms.internal.zzcmb     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            r14.<init>()     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            r14.zza(r9)     // Catch:{ IOException -> 0x01ff }
            java.lang.String r9 = r5.getString(r11)     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            r14.name = r9     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            r9 = 2
            long r6 = r5.getLong(r9)     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            java.lang.Long r6 = java.lang.Long.valueOf(r6)     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            r14.zzjli = r6     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            boolean r6 = r2.zza(r12, r14)     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            if (r6 != 0) goto L_0x0211
            if (r5 == 0) goto L_0x027a
            goto L_0x01cb
        L_0x01ff:
            r0 = move-exception
            com.google.android.gms.internal.zzchm r6 = r4.zzawy()     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            com.google.android.gms.internal.zzcho r6 = r6.zzazd()     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            java.lang.String r7 = "Data loss. Failed to merge raw event. appId"
            java.lang.Object r8 = com.google.android.gms.internal.zzchm.zzjk(r3)     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            r6.zze(r7, r8, r0)     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
        L_0x0211:
            boolean r6 = r5.moveToNext()     // Catch:{ SQLiteException -> 0x0223, all -> 0x021e }
            if (r6 != 0) goto L_0x021a
            if (r5 == 0) goto L_0x027a
            goto L_0x01cb
        L_0x021a:
            r6 = 3
            r7 = -1
            goto L_0x01d0
        L_0x021e:
            r0 = move-exception
            r2 = r0
            r9 = r5
            goto L_0x09e4
        L_0x0223:
            r0 = move-exception
            r12 = r3
            r9 = r5
            goto L_0x0048
        L_0x0228:
            r0 = move-exception
            r9 = r15
            com.google.android.gms.internal.zzchm r5 = r4.zzawy()     // Catch:{ SQLiteException -> 0x0241 }
            com.google.android.gms.internal.zzcho r5 = r5.zzazd()     // Catch:{ SQLiteException -> 0x0241 }
            java.lang.String r6 = "Data loss. Failed to merge raw event metadata. appId"
            java.lang.Object r7 = com.google.android.gms.internal.zzchm.zzjk(r3)     // Catch:{ SQLiteException -> 0x0241 }
            r5.zze(r6, r7, r0)     // Catch:{ SQLiteException -> 0x0241 }
            if (r9 == 0) goto L_0x027a
        L_0x023d:
            r9.close()     // Catch:{ all -> 0x09ea }
            goto L_0x027a
        L_0x0241:
            r0 = move-exception
            goto L_0x0249
        L_0x0243:
            r0 = move-exception
            r9 = r15
            goto L_0x09e3
        L_0x0247:
            r0 = move-exception
            r9 = r15
        L_0x0249:
            r12 = r3
            goto L_0x0048
        L_0x024c:
            r0 = move-exception
            r2 = r0
            r9 = r22
            goto L_0x09e4
        L_0x0252:
            r0 = move-exception
            r12 = r3
            r9 = r22
            goto L_0x0048
        L_0x0258:
            r0 = move-exception
            r9 = r3
            r12 = 0
            goto L_0x0048
        L_0x025d:
            r0 = move-exception
            r2 = r0
            r9 = 0
            goto L_0x09e4
        L_0x0262:
            r0 = move-exception
            r3 = r0
            r9 = 0
            r12 = 0
        L_0x0266:
            com.google.android.gms.internal.zzchm r4 = r4.zzawy()     // Catch:{ all -> 0x09e2 }
            com.google.android.gms.internal.zzcho r4 = r4.zzazd()     // Catch:{ all -> 0x09e2 }
            java.lang.String r5 = "Data loss. Error selecting raw event. appId"
            java.lang.Object r6 = com.google.android.gms.internal.zzchm.zzjk(r12)     // Catch:{ all -> 0x09e2 }
            r4.zze(r5, r6, r3)     // Catch:{ all -> 0x09e2 }
            if (r9 == 0) goto L_0x027a
            goto L_0x023d
        L_0x027a:
            java.util.List<com.google.android.gms.internal.zzcmb> r3 = r2.zzapa     // Catch:{ all -> 0x09ea }
            if (r3 == 0) goto L_0x0289
            java.util.List<com.google.android.gms.internal.zzcmb> r3 = r2.zzapa     // Catch:{ all -> 0x09ea }
            boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x09ea }
            if (r3 == 0) goto L_0x0287
            goto L_0x0289
        L_0x0287:
            r3 = 0
            goto L_0x028a
        L_0x0289:
            r3 = 1
        L_0x028a:
            if (r3 != 0) goto L_0x09d2
            com.google.android.gms.internal.zzcme r3 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            java.util.List<com.google.android.gms.internal.zzcmb> r4 = r2.zzapa     // Catch:{ all -> 0x09ea }
            int r4 = r4.size()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb[] r4 = new com.google.android.gms.internal.zzcmb[r4]     // Catch:{ all -> 0x09ea }
            r3.zzjlp = r4     // Catch:{ all -> 0x09ea }
            r4 = 0
            r5 = 0
            r6 = 0
        L_0x029b:
            java.util.List<com.google.android.gms.internal.zzcmb> r7 = r2.zzapa     // Catch:{ all -> 0x09ea }
            int r7 = r7.size()     // Catch:{ all -> 0x09ea }
            if (r4 >= r7) goto L_0x05a8
            com.google.android.gms.internal.zzcig r7 = r46.zzawv()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcme r12 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r12 = r12.zzcn     // Catch:{ all -> 0x09ea }
            java.util.List<com.google.android.gms.internal.zzcmb> r13 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r13 = r13.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r13 = (com.google.android.gms.internal.zzcmb) r13     // Catch:{ all -> 0x09ea }
            java.lang.String r13 = r13.name     // Catch:{ all -> 0x09ea }
            boolean r7 = r7.zzan(r12, r13)     // Catch:{ all -> 0x09ea }
            if (r7 == 0) goto L_0x0335
            com.google.android.gms.internal.zzchm r7 = r46.zzawy()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcho r7 = r7.zzazf()     // Catch:{ all -> 0x09ea }
            java.lang.String r8 = "Dropping blacklisted raw event. appId"
            com.google.android.gms.internal.zzcme r9 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r9 = r9.zzcn     // Catch:{ all -> 0x09ea }
            java.lang.Object r9 = com.google.android.gms.internal.zzchm.zzjk(r9)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzchk r12 = r46.zzawt()     // Catch:{ all -> 0x09ea }
            java.util.List<com.google.android.gms.internal.zzcmb> r13 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r13 = r13.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r13 = (com.google.android.gms.internal.zzcmb) r13     // Catch:{ all -> 0x09ea }
            java.lang.String r13 = r13.name     // Catch:{ all -> 0x09ea }
            java.lang.String r12 = r12.zzjh(r13)     // Catch:{ all -> 0x09ea }
            r7.zze(r8, r9, r12)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzclq r7 = r46.zzawu()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcme r8 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r8 = r8.zzcn     // Catch:{ all -> 0x09ea }
            boolean r7 = r7.zzkl(r8)     // Catch:{ all -> 0x09ea }
            if (r7 != 0) goto L_0x0301
            com.google.android.gms.internal.zzclq r7 = r46.zzawu()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcme r8 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r8 = r8.zzcn     // Catch:{ all -> 0x09ea }
            boolean r7 = r7.zzkm(r8)     // Catch:{ all -> 0x09ea }
            if (r7 == 0) goto L_0x02ff
            goto L_0x0301
        L_0x02ff:
            r7 = 0
            goto L_0x0302
        L_0x0301:
            r7 = 1
        L_0x0302:
            if (r7 != 0) goto L_0x05a2
            java.lang.String r7 = "_err"
            java.util.List<com.google.android.gms.internal.zzcmb> r8 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r8 = r8.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r8 = (com.google.android.gms.internal.zzcmb) r8     // Catch:{ all -> 0x09ea }
            java.lang.String r8 = r8.name     // Catch:{ all -> 0x09ea }
            boolean r7 = r7.equals(r8)     // Catch:{ all -> 0x09ea }
            if (r7 != 0) goto L_0x05a2
            com.google.android.gms.internal.zzclq r12 = r46.zzawu()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcme r7 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r13 = r7.zzcn     // Catch:{ all -> 0x09ea }
            r14 = 11
            java.lang.String r15 = "_ev"
            java.util.List<com.google.android.gms.internal.zzcmb> r7 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r7 = r7.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r7 = (com.google.android.gms.internal.zzcmb) r7     // Catch:{ all -> 0x09ea }
            java.lang.String r7 = r7.name     // Catch:{ all -> 0x09ea }
            r17 = 0
            r16 = r7
            r12.zza(r13, r14, r15, r16, r17)     // Catch:{ all -> 0x09ea }
            goto L_0x05a2
        L_0x0335:
            com.google.android.gms.internal.zzcig r7 = r46.zzawv()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcme r12 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r12 = r12.zzcn     // Catch:{ all -> 0x09ea }
            java.util.List<com.google.android.gms.internal.zzcmb> r13 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r13 = r13.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r13 = (com.google.android.gms.internal.zzcmb) r13     // Catch:{ all -> 0x09ea }
            java.lang.String r13 = r13.name     // Catch:{ all -> 0x09ea }
            boolean r7 = r7.zzao(r12, r13)     // Catch:{ all -> 0x09ea }
            if (r7 != 0) goto L_0x0360
            r46.zzawu()     // Catch:{ all -> 0x09ea }
            java.util.List<com.google.android.gms.internal.zzcmb> r12 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r12 = r12.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r12 = (com.google.android.gms.internal.zzcmb) r12     // Catch:{ all -> 0x09ea }
            java.lang.String r12 = r12.name     // Catch:{ all -> 0x09ea }
            boolean r12 = com.google.android.gms.internal.zzclq.zzkn(r12)     // Catch:{ all -> 0x09ea }
            if (r12 == 0) goto L_0x0593
        L_0x0360:
            java.util.List<com.google.android.gms.internal.zzcmb> r12 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r12 = r12.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r12 = (com.google.android.gms.internal.zzcmb) r12     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r12 = r12.zzjlh     // Catch:{ all -> 0x09ea }
            if (r12 != 0) goto L_0x0378
            java.util.List<com.google.android.gms.internal.zzcmb> r12 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r12 = r12.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r12 = (com.google.android.gms.internal.zzcmb) r12     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r13 = new com.google.android.gms.internal.zzcmc[r10]     // Catch:{ all -> 0x09ea }
            r12.zzjlh = r13     // Catch:{ all -> 0x09ea }
        L_0x0378:
            java.util.List<com.google.android.gms.internal.zzcmb> r12 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r12 = r12.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r12 = (com.google.android.gms.internal.zzcmb) r12     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r12 = r12.zzjlh     // Catch:{ all -> 0x09ea }
            int r13 = r12.length     // Catch:{ all -> 0x09ea }
            r14 = 0
            r15 = 0
            r16 = 0
        L_0x0387:
            if (r14 >= r13) goto L_0x03b8
            r10 = r12[r14]     // Catch:{ all -> 0x09ea }
            java.lang.String r11 = "_c"
            java.lang.String r8 = r10.name     // Catch:{ all -> 0x09ea }
            boolean r8 = r11.equals(r8)     // Catch:{ all -> 0x09ea }
            if (r8 == 0) goto L_0x039f
            r8 = 1
            java.lang.Long r11 = java.lang.Long.valueOf(r8)     // Catch:{ all -> 0x09ea }
            r10.zzjll = r11     // Catch:{ all -> 0x09ea }
            r15 = 1
            goto L_0x03b3
        L_0x039f:
            java.lang.String r8 = "_r"
            java.lang.String r9 = r10.name     // Catch:{ all -> 0x09ea }
            boolean r8 = r8.equals(r9)     // Catch:{ all -> 0x09ea }
            if (r8 == 0) goto L_0x03b3
            r8 = 1
            java.lang.Long r11 = java.lang.Long.valueOf(r8)     // Catch:{ all -> 0x09ea }
            r10.zzjll = r11     // Catch:{ all -> 0x09ea }
            r16 = 1
        L_0x03b3:
            int r14 = r14 + 1
            r10 = 0
            r11 = 1
            goto L_0x0387
        L_0x03b8:
            if (r15 != 0) goto L_0x0418
            if (r7 == 0) goto L_0x0418
            com.google.android.gms.internal.zzchm r8 = r46.zzawy()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcho r8 = r8.zzazj()     // Catch:{ all -> 0x09ea }
            java.lang.String r9 = "Marking event as conversion"
            com.google.android.gms.internal.zzchk r10 = r46.zzawt()     // Catch:{ all -> 0x09ea }
            java.util.List<com.google.android.gms.internal.zzcmb> r11 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r11 = r11.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r11 = (com.google.android.gms.internal.zzcmb) r11     // Catch:{ all -> 0x09ea }
            java.lang.String r11 = r11.name     // Catch:{ all -> 0x09ea }
            java.lang.String r10 = r10.zzjh(r11)     // Catch:{ all -> 0x09ea }
            r8.zzj(r9, r10)     // Catch:{ all -> 0x09ea }
            java.util.List<com.google.android.gms.internal.zzcmb> r8 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r8 = r8.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r8 = (com.google.android.gms.internal.zzcmb) r8     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r8 = r8.zzjlh     // Catch:{ all -> 0x09ea }
            java.util.List<com.google.android.gms.internal.zzcmb> r9 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r9 = r9.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r9 = (com.google.android.gms.internal.zzcmb) r9     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r9 = r9.zzjlh     // Catch:{ all -> 0x09ea }
            int r9 = r9.length     // Catch:{ all -> 0x09ea }
            r10 = 1
            int r9 = r9 + r10
            java.lang.Object[] r8 = java.util.Arrays.copyOf(r8, r9)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r8 = (com.google.android.gms.internal.zzcmc[]) r8     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc r9 = new com.google.android.gms.internal.zzcmc     // Catch:{ all -> 0x09ea }
            r9.<init>()     // Catch:{ all -> 0x09ea }
            java.lang.String r10 = "_c"
            r9.name = r10     // Catch:{ all -> 0x09ea }
            r10 = 1
            java.lang.Long r12 = java.lang.Long.valueOf(r10)     // Catch:{ all -> 0x09ea }
            r9.zzjll = r12     // Catch:{ all -> 0x09ea }
            int r10 = r8.length     // Catch:{ all -> 0x09ea }
            r11 = 1
            int r10 = r10 - r11
            r8[r10] = r9     // Catch:{ all -> 0x09ea }
            java.util.List<com.google.android.gms.internal.zzcmb> r9 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r9 = r9.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r9 = (com.google.android.gms.internal.zzcmb) r9     // Catch:{ all -> 0x09ea }
            r9.zzjlh = r8     // Catch:{ all -> 0x09ea }
        L_0x0418:
            if (r16 != 0) goto L_0x0476
            com.google.android.gms.internal.zzchm r8 = r46.zzawy()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcho r8 = r8.zzazj()     // Catch:{ all -> 0x09ea }
            java.lang.String r9 = "Marking event as real-time"
            com.google.android.gms.internal.zzchk r10 = r46.zzawt()     // Catch:{ all -> 0x09ea }
            java.util.List<com.google.android.gms.internal.zzcmb> r11 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r11 = r11.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r11 = (com.google.android.gms.internal.zzcmb) r11     // Catch:{ all -> 0x09ea }
            java.lang.String r11 = r11.name     // Catch:{ all -> 0x09ea }
            java.lang.String r10 = r10.zzjh(r11)     // Catch:{ all -> 0x09ea }
            r8.zzj(r9, r10)     // Catch:{ all -> 0x09ea }
            java.util.List<com.google.android.gms.internal.zzcmb> r8 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r8 = r8.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r8 = (com.google.android.gms.internal.zzcmb) r8     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r8 = r8.zzjlh     // Catch:{ all -> 0x09ea }
            java.util.List<com.google.android.gms.internal.zzcmb> r9 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r9 = r9.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r9 = (com.google.android.gms.internal.zzcmb) r9     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r9 = r9.zzjlh     // Catch:{ all -> 0x09ea }
            int r9 = r9.length     // Catch:{ all -> 0x09ea }
            r10 = 1
            int r9 = r9 + r10
            java.lang.Object[] r8 = java.util.Arrays.copyOf(r8, r9)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r8 = (com.google.android.gms.internal.zzcmc[]) r8     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc r9 = new com.google.android.gms.internal.zzcmc     // Catch:{ all -> 0x09ea }
            r9.<init>()     // Catch:{ all -> 0x09ea }
            java.lang.String r10 = "_r"
            r9.name = r10     // Catch:{ all -> 0x09ea }
            r10 = 1
            java.lang.Long r10 = java.lang.Long.valueOf(r10)     // Catch:{ all -> 0x09ea }
            r9.zzjll = r10     // Catch:{ all -> 0x09ea }
            int r10 = r8.length     // Catch:{ all -> 0x09ea }
            r11 = 1
            int r10 = r10 - r11
            r8[r10] = r9     // Catch:{ all -> 0x09ea }
            java.util.List<com.google.android.gms.internal.zzcmb> r9 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r9 = r9.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r9 = (com.google.android.gms.internal.zzcmb) r9     // Catch:{ all -> 0x09ea }
            r9.zzjlh = r8     // Catch:{ all -> 0x09ea }
        L_0x0476:
            com.google.android.gms.internal.zzcgo r10 = r46.zzaws()     // Catch:{ all -> 0x09ea }
            long r11 = r46.zzbag()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcme r8 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r13 = r8.zzcn     // Catch:{ all -> 0x09ea }
            r14 = 0
            r15 = 0
            r16 = 0
            r17 = 0
            r18 = 1
            com.google.android.gms.internal.zzcgp r8 = r10.zza(r11, r13, r14, r15, r16, r17, r18)     // Catch:{ all -> 0x09ea }
            long r8 = r8.zzizb     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcgn r10 = r1.zzjew     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcme r11 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r11 = r11.zzcn     // Catch:{ all -> 0x09ea }
            int r10 = r10.zzix(r11)     // Catch:{ all -> 0x09ea }
            long r10 = (long) r10     // Catch:{ all -> 0x09ea }
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 <= 0) goto L_0x04dc
            java.util.List<com.google.android.gms.internal.zzcmb> r8 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r8 = r8.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r8 = (com.google.android.gms.internal.zzcmb) r8     // Catch:{ all -> 0x09ea }
            r9 = 0
        L_0x04a8:
            com.google.android.gms.internal.zzcmc[] r10 = r8.zzjlh     // Catch:{ all -> 0x09ea }
            int r10 = r10.length     // Catch:{ all -> 0x09ea }
            if (r9 >= r10) goto L_0x04dd
            java.lang.String r10 = "_r"
            com.google.android.gms.internal.zzcmc[] r11 = r8.zzjlh     // Catch:{ all -> 0x09ea }
            r11 = r11[r9]     // Catch:{ all -> 0x09ea }
            java.lang.String r11 = r11.name     // Catch:{ all -> 0x09ea }
            boolean r10 = r10.equals(r11)     // Catch:{ all -> 0x09ea }
            if (r10 == 0) goto L_0x04d9
            com.google.android.gms.internal.zzcmc[] r10 = r8.zzjlh     // Catch:{ all -> 0x09ea }
            int r10 = r10.length     // Catch:{ all -> 0x09ea }
            r11 = 1
            int r10 = r10 - r11
            com.google.android.gms.internal.zzcmc[] r10 = new com.google.android.gms.internal.zzcmc[r10]     // Catch:{ all -> 0x09ea }
            if (r9 <= 0) goto L_0x04ca
            com.google.android.gms.internal.zzcmc[] r11 = r8.zzjlh     // Catch:{ all -> 0x09ea }
            r12 = 0
            java.lang.System.arraycopy(r11, r12, r10, r12, r9)     // Catch:{ all -> 0x09ea }
        L_0x04ca:
            int r11 = r10.length     // Catch:{ all -> 0x09ea }
            if (r9 >= r11) goto L_0x04d6
            com.google.android.gms.internal.zzcmc[] r11 = r8.zzjlh     // Catch:{ all -> 0x09ea }
            int r12 = r9 + 1
            int r13 = r10.length     // Catch:{ all -> 0x09ea }
            int r13 = r13 - r9
            java.lang.System.arraycopy(r11, r12, r10, r9, r13)     // Catch:{ all -> 0x09ea }
        L_0x04d6:
            r8.zzjlh = r10     // Catch:{ all -> 0x09ea }
            goto L_0x04dd
        L_0x04d9:
            int r9 = r9 + 1
            goto L_0x04a8
        L_0x04dc:
            r5 = 1
        L_0x04dd:
            java.util.List<com.google.android.gms.internal.zzcmb> r8 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r8 = r8.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r8 = (com.google.android.gms.internal.zzcmb) r8     // Catch:{ all -> 0x09ea }
            java.lang.String r8 = r8.name     // Catch:{ all -> 0x09ea }
            boolean r8 = com.google.android.gms.internal.zzclq.zzjz(r8)     // Catch:{ all -> 0x09ea }
            if (r8 == 0) goto L_0x0593
            if (r7 == 0) goto L_0x0593
            com.google.android.gms.internal.zzcgo r9 = r46.zzaws()     // Catch:{ all -> 0x09ea }
            long r10 = r46.zzbag()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcme r7 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r12 = r7.zzcn     // Catch:{ all -> 0x09ea }
            r13 = 0
            r14 = 0
            r15 = 1
            r16 = 0
            r17 = 0
            com.google.android.gms.internal.zzcgp r7 = r9.zza(r10, r12, r13, r14, r15, r16, r17)     // Catch:{ all -> 0x09ea }
            long r7 = r7.zziyz     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcgn r9 = r1.zzjew     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcme r10 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r10 = r10.zzcn     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzchd<java.lang.Integer> r11 = com.google.android.gms.internal.zzchc.zzjaq     // Catch:{ all -> 0x09ea }
            int r9 = r9.zzb(r10, r11)     // Catch:{ all -> 0x09ea }
            long r9 = (long) r9     // Catch:{ all -> 0x09ea }
            int r7 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r7 <= 0) goto L_0x0593
            com.google.android.gms.internal.zzchm r7 = r46.zzawy()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcho r7 = r7.zzazf()     // Catch:{ all -> 0x09ea }
            java.lang.String r8 = "Too many conversions. Not logging as conversion. appId"
            com.google.android.gms.internal.zzcme r9 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r9 = r9.zzcn     // Catch:{ all -> 0x09ea }
            java.lang.Object r9 = com.google.android.gms.internal.zzchm.zzjk(r9)     // Catch:{ all -> 0x09ea }
            r7.zzj(r8, r9)     // Catch:{ all -> 0x09ea }
            java.util.List<com.google.android.gms.internal.zzcmb> r7 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r7 = r7.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r7 = (com.google.android.gms.internal.zzcmb) r7     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r8 = r7.zzjlh     // Catch:{ all -> 0x09ea }
            int r9 = r8.length     // Catch:{ all -> 0x09ea }
            r10 = 0
            r11 = 0
            r12 = 0
        L_0x053c:
            if (r10 >= r9) goto L_0x055a
            r13 = r8[r10]     // Catch:{ all -> 0x09ea }
            java.lang.String r14 = "_c"
            java.lang.String r15 = r13.name     // Catch:{ all -> 0x09ea }
            boolean r14 = r14.equals(r15)     // Catch:{ all -> 0x09ea }
            if (r14 == 0) goto L_0x054c
            r12 = r13
            goto L_0x0557
        L_0x054c:
            java.lang.String r14 = "_err"
            java.lang.String r13 = r13.name     // Catch:{ all -> 0x09ea }
            boolean r13 = r14.equals(r13)     // Catch:{ all -> 0x09ea }
            if (r13 == 0) goto L_0x0557
            r11 = 1
        L_0x0557:
            int r10 = r10 + 1
            goto L_0x053c
        L_0x055a:
            if (r11 == 0) goto L_0x056f
            if (r12 == 0) goto L_0x056f
            com.google.android.gms.internal.zzcmc[] r8 = r7.zzjlh     // Catch:{ all -> 0x09ea }
            r9 = 1
            com.google.android.gms.internal.zzcmc[] r10 = new com.google.android.gms.internal.zzcmc[r9]     // Catch:{ all -> 0x09ea }
            r9 = 0
            r10[r9] = r12     // Catch:{ all -> 0x09ea }
            java.lang.Object[] r8 = com.google.android.gms.common.util.zza.zza((T[]) r8, (T[]) r10)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r8 = (com.google.android.gms.internal.zzcmc[]) r8     // Catch:{ all -> 0x09ea }
            r7.zzjlh = r8     // Catch:{ all -> 0x09ea }
            goto L_0x0593
        L_0x056f:
            if (r12 == 0) goto L_0x057e
            java.lang.String r7 = "_err"
            r12.name = r7     // Catch:{ all -> 0x09ea }
            r7 = 10
            java.lang.Long r7 = java.lang.Long.valueOf(r7)     // Catch:{ all -> 0x09ea }
            r12.zzjll = r7     // Catch:{ all -> 0x09ea }
            goto L_0x0593
        L_0x057e:
            com.google.android.gms.internal.zzchm r7 = r46.zzawy()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcho r7 = r7.zzazd()     // Catch:{ all -> 0x09ea }
            java.lang.String r8 = "Did not find conversion parameter. appId"
            com.google.android.gms.internal.zzcme r9 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r9 = r9.zzcn     // Catch:{ all -> 0x09ea }
            java.lang.Object r9 = com.google.android.gms.internal.zzchm.zzjk(r9)     // Catch:{ all -> 0x09ea }
            r7.zzj(r8, r9)     // Catch:{ all -> 0x09ea }
        L_0x0593:
            com.google.android.gms.internal.zzcmb[] r7 = r3.zzjlp     // Catch:{ all -> 0x09ea }
            int r8 = r6 + 1
            java.util.List<com.google.android.gms.internal.zzcmb> r9 = r2.zzapa     // Catch:{ all -> 0x09ea }
            java.lang.Object r9 = r9.get(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb r9 = (com.google.android.gms.internal.zzcmb) r9     // Catch:{ all -> 0x09ea }
            r7[r6] = r9     // Catch:{ all -> 0x09ea }
            r6 = r8
        L_0x05a2:
            int r4 = r4 + 1
            r10 = 0
            r11 = 1
            goto L_0x029b
        L_0x05a8:
            java.util.List<com.google.android.gms.internal.zzcmb> r4 = r2.zzapa     // Catch:{ all -> 0x09ea }
            int r4 = r4.size()     // Catch:{ all -> 0x09ea }
            if (r6 >= r4) goto L_0x05ba
            com.google.android.gms.internal.zzcmb[] r4 = r3.zzjlp     // Catch:{ all -> 0x09ea }
            java.lang.Object[] r4 = java.util.Arrays.copyOf(r4, r6)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb[] r4 = (com.google.android.gms.internal.zzcmb[]) r4     // Catch:{ all -> 0x09ea }
            r3.zzjlp = r4     // Catch:{ all -> 0x09ea }
        L_0x05ba:
            com.google.android.gms.internal.zzcme r4 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r4 = r4.zzcn     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcme r6 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmg[] r6 = r6.zzjlq     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb[] r7 = r3.zzjlp     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcma[] r4 = r1.zza(r4, r6, r7)     // Catch:{ all -> 0x09ea }
            r3.zzjmi = r4     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzchd<java.lang.Boolean> r4 = com.google.android.gms.internal.zzchc.zzjac     // Catch:{ all -> 0x09ea }
            java.lang.Object r4 = r4.get()     // Catch:{ all -> 0x09ea }
            java.lang.Boolean r4 = (java.lang.Boolean) r4     // Catch:{ all -> 0x09ea }
            boolean r4 = r4.booleanValue()     // Catch:{ all -> 0x09ea }
            if (r4 == 0) goto L_0x0878
            com.google.android.gms.internal.zzcgn r4 = r1.zzjew     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcme r6 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r6 = r6.zzcn     // Catch:{ all -> 0x09ea }
            java.lang.String r7 = "1"
            com.google.android.gms.internal.zzcig r4 = r4.zzawv()     // Catch:{ all -> 0x09ea }
            java.lang.String r8 = "measurement.event_sampling_enabled"
            java.lang.String r4 = r4.zzam(r6, r8)     // Catch:{ all -> 0x09ea }
            boolean r4 = r7.equals(r4)     // Catch:{ all -> 0x09ea }
            if (r4 == 0) goto L_0x0878
            java.util.HashMap r4 = new java.util.HashMap     // Catch:{ all -> 0x09ea }
            r4.<init>()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb[] r6 = r3.zzjlp     // Catch:{ all -> 0x09ea }
            int r6 = r6.length     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb[] r6 = new com.google.android.gms.internal.zzcmb[r6]     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzclq r7 = r46.zzawu()     // Catch:{ all -> 0x09ea }
            java.security.SecureRandom r7 = r7.zzbaz()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb[] r8 = r3.zzjlp     // Catch:{ all -> 0x09ea }
            int r9 = r8.length     // Catch:{ all -> 0x09ea }
            r10 = 0
            r11 = 0
        L_0x0607:
            if (r10 >= r9) goto L_0x0842
            r12 = r8[r10]     // Catch:{ all -> 0x09ea }
            java.lang.String r13 = r12.name     // Catch:{ all -> 0x09ea }
            java.lang.String r14 = "_ep"
            boolean r13 = r13.equals(r14)     // Catch:{ all -> 0x09ea }
            if (r13 == 0) goto L_0x0693
            r46.zzawu()     // Catch:{ all -> 0x09ea }
            java.lang.String r13 = "_en"
            java.lang.Object r13 = com.google.android.gms.internal.zzclq.zza(r12, r13)     // Catch:{ all -> 0x09ea }
            java.lang.String r13 = (java.lang.String) r13     // Catch:{ all -> 0x09ea }
            java.lang.Object r14 = r4.get(r13)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcgw r14 = (com.google.android.gms.internal.zzcgw) r14     // Catch:{ all -> 0x09ea }
            if (r14 != 0) goto L_0x0637
            com.google.android.gms.internal.zzcgo r14 = r46.zzaws()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcme r15 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r15 = r15.zzcn     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcgw r14 = r14.zzae(r15, r13)     // Catch:{ all -> 0x09ea }
            r4.put(r13, r14)     // Catch:{ all -> 0x09ea }
        L_0x0637:
            java.lang.Long r13 = r14.zzizo     // Catch:{ all -> 0x09ea }
            if (r13 != 0) goto L_0x0684
            java.lang.Long r13 = r14.zzizp     // Catch:{ all -> 0x09ea }
            long r15 = r13.longValue()     // Catch:{ all -> 0x09ea }
            r17 = 1
            int r13 = (r15 > r17 ? 1 : (r15 == r17 ? 0 : -1))
            if (r13 <= 0) goto L_0x0659
            r46.zzawu()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r13 = r12.zzjlh     // Catch:{ all -> 0x09ea }
            java.lang.String r15 = "_sr"
            r25 = r8
            java.lang.Long r8 = r14.zzizp     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r8 = com.google.android.gms.internal.zzclq.zza(r13, r15, r8)     // Catch:{ all -> 0x09ea }
            r12.zzjlh = r8     // Catch:{ all -> 0x09ea }
            goto L_0x065b
        L_0x0659:
            r25 = r8
        L_0x065b:
            java.lang.Boolean r8 = r14.zzizq     // Catch:{ all -> 0x09ea }
            if (r8 == 0) goto L_0x067d
            java.lang.Boolean r8 = r14.zzizq     // Catch:{ all -> 0x09ea }
            boolean r8 = r8.booleanValue()     // Catch:{ all -> 0x09ea }
            if (r8 == 0) goto L_0x067d
            r46.zzawu()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r8 = r12.zzjlh     // Catch:{ all -> 0x09ea }
            java.lang.String r13 = "_efs"
            r26 = r9
            r14 = 1
            java.lang.Long r9 = java.lang.Long.valueOf(r14)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r8 = com.google.android.gms.internal.zzclq.zza(r8, r13, r9)     // Catch:{ all -> 0x09ea }
            r12.zzjlh = r8     // Catch:{ all -> 0x09ea }
            goto L_0x067f
        L_0x067d:
            r26 = r9
        L_0x067f:
            int r8 = r11 + 1
            r6[r11] = r12     // Catch:{ all -> 0x09ea }
            goto L_0x06ce
        L_0x0684:
            r25 = r8
            r26 = r9
            r43 = r2
            r44 = r3
            r8 = r4
            r42 = r5
            r41 = r7
            goto L_0x07aa
        L_0x0693:
            r25 = r8
            r26 = r9
            java.lang.String r8 = "_dbg"
            r13 = 1
            java.lang.Long r9 = java.lang.Long.valueOf(r13)     // Catch:{ all -> 0x09ea }
            boolean r8 = zza(r12, r8, r9)     // Catch:{ all -> 0x09ea }
            if (r8 != 0) goto L_0x06b4
            com.google.android.gms.internal.zzcig r8 = r46.zzawv()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcme r9 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r9 = r9.zzcn     // Catch:{ all -> 0x09ea }
            java.lang.String r13 = r12.name     // Catch:{ all -> 0x09ea }
            int r8 = r8.zzap(r9, r13)     // Catch:{ all -> 0x09ea }
            goto L_0x06b5
        L_0x06b4:
            r8 = 1
        L_0x06b5:
            if (r8 > 0) goto L_0x06dd
            com.google.android.gms.internal.zzchm r9 = r46.zzawy()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcho r9 = r9.zzazf()     // Catch:{ all -> 0x09ea }
            java.lang.String r13 = "Sample rate must be positive. event, rate"
            java.lang.String r14 = r12.name     // Catch:{ all -> 0x09ea }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ all -> 0x09ea }
            r9.zze(r13, r14, r8)     // Catch:{ all -> 0x09ea }
            int r8 = r11 + 1
            r6[r11] = r12     // Catch:{ all -> 0x09ea }
        L_0x06ce:
            r43 = r2
            r44 = r3
            r42 = r5
            r41 = r7
            r11 = r8
            r3 = 0
            r8 = r4
        L_0x06d9:
            r4 = 1
            goto L_0x0831
        L_0x06dd:
            java.lang.String r9 = r12.name     // Catch:{ all -> 0x09ea }
            java.lang.Object r9 = r4.get(r9)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcgw r9 = (com.google.android.gms.internal.zzcgw) r9     // Catch:{ all -> 0x09ea }
            if (r9 != 0) goto L_0x072d
            com.google.android.gms.internal.zzcgo r9 = r46.zzaws()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcme r13 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r13 = r13.zzcn     // Catch:{ all -> 0x09ea }
            java.lang.String r14 = r12.name     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcgw r9 = r9.zzae(r13, r14)     // Catch:{ all -> 0x09ea }
            if (r9 != 0) goto L_0x072d
            com.google.android.gms.internal.zzchm r9 = r46.zzawy()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcho r9 = r9.zzazf()     // Catch:{ all -> 0x09ea }
            java.lang.String r13 = "Event being bundled has no eventAggregate. appId, eventName"
            com.google.android.gms.internal.zzcme r14 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r14 = r14.zzcn     // Catch:{ all -> 0x09ea }
            java.lang.String r15 = r12.name     // Catch:{ all -> 0x09ea }
            r9.zze(r13, r14, r15)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcgw r9 = new com.google.android.gms.internal.zzcgw     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcme r13 = r2.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r13 = r13.zzcn     // Catch:{ all -> 0x09ea }
            java.lang.String r14 = r12.name     // Catch:{ all -> 0x09ea }
            r30 = 1
            r32 = 1
            java.lang.Long r15 = r12.zzjli     // Catch:{ all -> 0x09ea }
            long r34 = r15.longValue()     // Catch:{ all -> 0x09ea }
            r36 = 0
            r38 = 0
            r39 = 0
            r40 = 0
            r27 = r9
            r28 = r13
            r29 = r14
            r27.<init>(r28, r29, r30, r32, r34, r36, r38, r39, r40)     // Catch:{ all -> 0x09ea }
        L_0x072d:
            r46.zzawu()     // Catch:{ all -> 0x09ea }
            java.lang.String r13 = "_eid"
            java.lang.Object r13 = com.google.android.gms.internal.zzclq.zza(r12, r13)     // Catch:{ all -> 0x09ea }
            java.lang.Long r13 = (java.lang.Long) r13     // Catch:{ all -> 0x09ea }
            if (r13 == 0) goto L_0x073c
            r14 = 1
            goto L_0x073d
        L_0x073c:
            r14 = 0
        L_0x073d:
            java.lang.Boolean r14 = java.lang.Boolean.valueOf(r14)     // Catch:{ all -> 0x09ea }
            r15 = 1
            if (r8 != r15) goto L_0x0766
            int r8 = r11 + 1
            r6[r11] = r12     // Catch:{ all -> 0x09ea }
            boolean r11 = r14.booleanValue()     // Catch:{ all -> 0x09ea }
            if (r11 == 0) goto L_0x06ce
            java.lang.Long r11 = r9.zzizo     // Catch:{ all -> 0x09ea }
            if (r11 != 0) goto L_0x075a
            java.lang.Long r11 = r9.zzizp     // Catch:{ all -> 0x09ea }
            if (r11 != 0) goto L_0x075a
            java.lang.Boolean r11 = r9.zzizq     // Catch:{ all -> 0x09ea }
            if (r11 == 0) goto L_0x06ce
        L_0x075a:
            r11 = 0
            com.google.android.gms.internal.zzcgw r9 = r9.zza(r11, r11, r11)     // Catch:{ all -> 0x09ea }
            java.lang.String r11 = r12.name     // Catch:{ all -> 0x09ea }
            r4.put(r11, r9)     // Catch:{ all -> 0x09ea }
            goto L_0x06ce
        L_0x0766:
            int r15 = r7.nextInt(r8)     // Catch:{ all -> 0x09ea }
            if (r15 != 0) goto L_0x07ad
            r46.zzawu()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r13 = r12.zzjlh     // Catch:{ all -> 0x09ea }
            java.lang.String r15 = "_sr"
            r41 = r7
            long r7 = (long) r8     // Catch:{ all -> 0x09ea }
            r42 = r5
            java.lang.Long r5 = java.lang.Long.valueOf(r7)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r5 = com.google.android.gms.internal.zzclq.zza(r13, r15, r5)     // Catch:{ all -> 0x09ea }
            r12.zzjlh = r5     // Catch:{ all -> 0x09ea }
            int r5 = r11 + 1
            r6[r11] = r12     // Catch:{ all -> 0x09ea }
            boolean r11 = r14.booleanValue()     // Catch:{ all -> 0x09ea }
            if (r11 == 0) goto L_0x0795
            java.lang.Long r7 = java.lang.Long.valueOf(r7)     // Catch:{ all -> 0x09ea }
            r8 = 0
            com.google.android.gms.internal.zzcgw r9 = r9.zza(r8, r7, r8)     // Catch:{ all -> 0x09ea }
        L_0x0795:
            java.lang.String r7 = r12.name     // Catch:{ all -> 0x09ea }
            java.lang.Long r8 = r12.zzjli     // Catch:{ all -> 0x09ea }
            long r11 = r8.longValue()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcgw r8 = r9.zzbc(r11)     // Catch:{ all -> 0x09ea }
            r4.put(r7, r8)     // Catch:{ all -> 0x09ea }
            r43 = r2
            r44 = r3
            r8 = r4
            r11 = r5
        L_0x07aa:
            r3 = 0
            goto L_0x06d9
        L_0x07ad:
            r43 = r2
            r44 = r3
            r42 = r5
            r41 = r7
            long r2 = r9.zzizn     // Catch:{ all -> 0x09ea }
            java.lang.Long r5 = r12.zzjli     // Catch:{ all -> 0x09ea }
            long r15 = r5.longValue()     // Catch:{ all -> 0x09ea }
            r5 = 0
            long r2 = r15 - r2
            long r2 = java.lang.Math.abs(r2)     // Catch:{ all -> 0x09ea }
            r15 = 86400000(0x5265c00, double:4.2687272E-316)
            int r2 = (r2 > r15 ? 1 : (r2 == r15 ? 0 : -1))
            if (r2 < 0) goto L_0x081e
            r46.zzawu()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r2 = r12.zzjlh     // Catch:{ all -> 0x09ea }
            java.lang.String r3 = "_efs"
            r45 = r4
            r4 = 1
            java.lang.Long r7 = java.lang.Long.valueOf(r4)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r2 = com.google.android.gms.internal.zzclq.zza(r2, r3, r7)     // Catch:{ all -> 0x09ea }
            r12.zzjlh = r2     // Catch:{ all -> 0x09ea }
            r46.zzawu()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r2 = r12.zzjlh     // Catch:{ all -> 0x09ea }
            java.lang.String r3 = "_sr"
            long r7 = (long) r8     // Catch:{ all -> 0x09ea }
            java.lang.Long r13 = java.lang.Long.valueOf(r7)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmc[] r2 = com.google.android.gms.internal.zzclq.zza(r2, r3, r13)     // Catch:{ all -> 0x09ea }
            r12.zzjlh = r2     // Catch:{ all -> 0x09ea }
            int r2 = r11 + 1
            r6[r11] = r12     // Catch:{ all -> 0x09ea }
            boolean r3 = r14.booleanValue()     // Catch:{ all -> 0x09ea }
            if (r3 == 0) goto L_0x080a
            java.lang.Long r3 = java.lang.Long.valueOf(r7)     // Catch:{ all -> 0x09ea }
            r7 = 1
            java.lang.Boolean r8 = java.lang.Boolean.valueOf(r7)     // Catch:{ all -> 0x09ea }
            r7 = 0
            com.google.android.gms.internal.zzcgw r9 = r9.zza(r7, r3, r8)     // Catch:{ all -> 0x09ea }
        L_0x080a:
            java.lang.String r3 = r12.name     // Catch:{ all -> 0x09ea }
            java.lang.Long r7 = r12.zzjli     // Catch:{ all -> 0x09ea }
            long r7 = r7.longValue()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcgw r7 = r9.zzbc(r7)     // Catch:{ all -> 0x09ea }
            r8 = r45
            r8.put(r3, r7)     // Catch:{ all -> 0x09ea }
            r11 = r2
        L_0x081c:
            r3 = 0
            goto L_0x0831
        L_0x081e:
            r8 = r4
            r4 = 1
            boolean r2 = r14.booleanValue()     // Catch:{ all -> 0x09ea }
            if (r2 == 0) goto L_0x081c
            java.lang.String r2 = r12.name     // Catch:{ all -> 0x09ea }
            r3 = 0
            com.google.android.gms.internal.zzcgw r7 = r9.zza(r13, r3, r3)     // Catch:{ all -> 0x09ea }
            r8.put(r2, r7)     // Catch:{ all -> 0x09ea }
        L_0x0831:
            int r10 = r10 + 1
            r4 = r8
            r8 = r25
            r9 = r26
            r7 = r41
            r5 = r42
            r2 = r43
            r3 = r44
            goto L_0x0607
        L_0x0842:
            r43 = r2
            r2 = r3
            r8 = r4
            r42 = r5
            r3 = 0
            com.google.android.gms.internal.zzcmb[] r4 = r2.zzjlp     // Catch:{ all -> 0x09ea }
            int r4 = r4.length     // Catch:{ all -> 0x09ea }
            if (r11 >= r4) goto L_0x0856
            java.lang.Object[] r4 = java.util.Arrays.copyOf(r6, r11)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcmb[] r4 = (com.google.android.gms.internal.zzcmb[]) r4     // Catch:{ all -> 0x09ea }
            r2.zzjlp = r4     // Catch:{ all -> 0x09ea }
        L_0x0856:
            java.util.Set r4 = r8.entrySet()     // Catch:{ all -> 0x09ea }
            java.util.Iterator r4 = r4.iterator()     // Catch:{ all -> 0x09ea }
        L_0x085e:
            boolean r5 = r4.hasNext()     // Catch:{ all -> 0x09ea }
            if (r5 == 0) goto L_0x087e
            java.lang.Object r5 = r4.next()     // Catch:{ all -> 0x09ea }
            java.util.Map$Entry r5 = (java.util.Map.Entry) r5     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcgo r6 = r46.zzaws()     // Catch:{ all -> 0x09ea }
            java.lang.Object r5 = r5.getValue()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcgw r5 = (com.google.android.gms.internal.zzcgw) r5     // Catch:{ all -> 0x09ea }
            r6.zza(r5)     // Catch:{ all -> 0x09ea }
            goto L_0x085e
        L_0x0878:
            r43 = r2
            r2 = r3
            r42 = r5
            r3 = 0
        L_0x087e:
            r4 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            java.lang.Long r4 = java.lang.Long.valueOf(r4)     // Catch:{ all -> 0x09ea }
            r2.zzjls = r4     // Catch:{ all -> 0x09ea }
            r4 = -9223372036854775808
            java.lang.Long r4 = java.lang.Long.valueOf(r4)     // Catch:{ all -> 0x09ea }
            r2.zzjlt = r4     // Catch:{ all -> 0x09ea }
            r4 = 0
        L_0x0892:
            com.google.android.gms.internal.zzcmb[] r5 = r2.zzjlp     // Catch:{ all -> 0x09ea }
            int r5 = r5.length     // Catch:{ all -> 0x09ea }
            if (r4 >= r5) goto L_0x08c6
            com.google.android.gms.internal.zzcmb[] r5 = r2.zzjlp     // Catch:{ all -> 0x09ea }
            r5 = r5[r4]     // Catch:{ all -> 0x09ea }
            java.lang.Long r6 = r5.zzjli     // Catch:{ all -> 0x09ea }
            long r6 = r6.longValue()     // Catch:{ all -> 0x09ea }
            java.lang.Long r8 = r2.zzjls     // Catch:{ all -> 0x09ea }
            long r8 = r8.longValue()     // Catch:{ all -> 0x09ea }
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 >= 0) goto L_0x08af
            java.lang.Long r6 = r5.zzjli     // Catch:{ all -> 0x09ea }
            r2.zzjls = r6     // Catch:{ all -> 0x09ea }
        L_0x08af:
            java.lang.Long r6 = r5.zzjli     // Catch:{ all -> 0x09ea }
            long r6 = r6.longValue()     // Catch:{ all -> 0x09ea }
            java.lang.Long r8 = r2.zzjlt     // Catch:{ all -> 0x09ea }
            long r8 = r8.longValue()     // Catch:{ all -> 0x09ea }
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 <= 0) goto L_0x08c3
            java.lang.Long r5 = r5.zzjli     // Catch:{ all -> 0x09ea }
            r2.zzjlt = r5     // Catch:{ all -> 0x09ea }
        L_0x08c3:
            int r4 = r4 + 1
            goto L_0x0892
        L_0x08c6:
            r4 = r43
            com.google.android.gms.internal.zzcme r5 = r4.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r5 = r5.zzcn     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcgo r6 = r46.zzaws()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcgh r6 = r6.zzjb(r5)     // Catch:{ all -> 0x09ea }
            if (r6 != 0) goto L_0x08ec
            com.google.android.gms.internal.zzchm r3 = r46.zzawy()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcho r3 = r3.zzazd()     // Catch:{ all -> 0x09ea }
            java.lang.String r6 = "Bundling raw events w/o app info. appId"
            com.google.android.gms.internal.zzcme r7 = r4.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r7 = r7.zzcn     // Catch:{ all -> 0x09ea }
            java.lang.Object r7 = com.google.android.gms.internal.zzchm.zzjk(r7)     // Catch:{ all -> 0x09ea }
            r3.zzj(r6, r7)     // Catch:{ all -> 0x09ea }
            goto L_0x0943
        L_0x08ec:
            com.google.android.gms.internal.zzcmb[] r7 = r2.zzjlp     // Catch:{ all -> 0x09ea }
            int r7 = r7.length     // Catch:{ all -> 0x09ea }
            if (r7 <= 0) goto L_0x0943
            long r7 = r6.zzaxf()     // Catch:{ all -> 0x09ea }
            r9 = 0
            int r11 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r11 == 0) goto L_0x0900
            java.lang.Long r11 = java.lang.Long.valueOf(r7)     // Catch:{ all -> 0x09ea }
            goto L_0x0901
        L_0x0900:
            r11 = r3
        L_0x0901:
            r2.zzjlv = r11     // Catch:{ all -> 0x09ea }
            long r11 = r6.zzaxe()     // Catch:{ all -> 0x09ea }
            int r13 = (r11 > r9 ? 1 : (r11 == r9 ? 0 : -1))
            if (r13 != 0) goto L_0x090c
            r11 = r7
        L_0x090c:
            int r7 = (r11 > r9 ? 1 : (r11 == r9 ? 0 : -1))
            if (r7 == 0) goto L_0x0914
            java.lang.Long r3 = java.lang.Long.valueOf(r11)     // Catch:{ all -> 0x09ea }
        L_0x0914:
            r2.zzjlu = r3     // Catch:{ all -> 0x09ea }
            r6.zzaxo()     // Catch:{ all -> 0x09ea }
            long r7 = r6.zzaxl()     // Catch:{ all -> 0x09ea }
            int r3 = (int) r7     // Catch:{ all -> 0x09ea }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x09ea }
            r2.zzjmg = r3     // Catch:{ all -> 0x09ea }
            java.lang.Long r3 = r2.zzjls     // Catch:{ all -> 0x09ea }
            long r7 = r3.longValue()     // Catch:{ all -> 0x09ea }
            r6.zzal(r7)     // Catch:{ all -> 0x09ea }
            java.lang.Long r3 = r2.zzjlt     // Catch:{ all -> 0x09ea }
            long r7 = r3.longValue()     // Catch:{ all -> 0x09ea }
            r6.zzam(r7)     // Catch:{ all -> 0x09ea }
            java.lang.String r3 = r6.zzaxw()     // Catch:{ all -> 0x09ea }
            r2.zzixw = r3     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcgo r3 = r46.zzaws()     // Catch:{ all -> 0x09ea }
            r3.zza(r6)     // Catch:{ all -> 0x09ea }
        L_0x0943:
            com.google.android.gms.internal.zzcmb[] r3 = r2.zzjlp     // Catch:{ all -> 0x09ea }
            int r3 = r3.length     // Catch:{ all -> 0x09ea }
            if (r3 <= 0) goto L_0x098f
            com.google.android.gms.internal.zzcig r3 = r46.zzawv()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcme r6 = r4.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r6 = r6.zzcn     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcly r3 = r3.zzjs(r6)     // Catch:{ all -> 0x09ea }
            if (r3 == 0) goto L_0x0960
            java.lang.Long r6 = r3.zzjkw     // Catch:{ all -> 0x09ea }
            if (r6 != 0) goto L_0x095b
            goto L_0x0960
        L_0x095b:
            java.lang.Long r3 = r3.zzjkw     // Catch:{ all -> 0x09ea }
        L_0x095d:
            r2.zzjmn = r3     // Catch:{ all -> 0x09ea }
            goto L_0x0986
        L_0x0960:
            com.google.android.gms.internal.zzcme r3 = r4.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r3 = r3.zzixs     // Catch:{ all -> 0x09ea }
            boolean r3 = android.text.TextUtils.isEmpty(r3)     // Catch:{ all -> 0x09ea }
            if (r3 == 0) goto L_0x0971
            r6 = -1
            java.lang.Long r3 = java.lang.Long.valueOf(r6)     // Catch:{ all -> 0x09ea }
            goto L_0x095d
        L_0x0971:
            com.google.android.gms.internal.zzchm r3 = r46.zzawy()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcho r3 = r3.zzazf()     // Catch:{ all -> 0x09ea }
            java.lang.String r6 = "Did not find measurement config or missing version info. appId"
            com.google.android.gms.internal.zzcme r7 = r4.zzjgi     // Catch:{ all -> 0x09ea }
            java.lang.String r7 = r7.zzcn     // Catch:{ all -> 0x09ea }
            java.lang.Object r7 = com.google.android.gms.internal.zzchm.zzjk(r7)     // Catch:{ all -> 0x09ea }
            r3.zzj(r6, r7)     // Catch:{ all -> 0x09ea }
        L_0x0986:
            com.google.android.gms.internal.zzcgo r3 = r46.zzaws()     // Catch:{ all -> 0x09ea }
            r10 = r42
            r3.zza(r2, r10)     // Catch:{ all -> 0x09ea }
        L_0x098f:
            com.google.android.gms.internal.zzcgo r2 = r46.zzaws()     // Catch:{ all -> 0x09ea }
            java.util.List<java.lang.Long> r3 = r4.zzjgj     // Catch:{ all -> 0x09ea }
            r2.zzah(r3)     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcgo r2 = r46.zzaws()     // Catch:{ all -> 0x09ea }
            android.database.sqlite.SQLiteDatabase r3 = r2.getWritableDatabase()     // Catch:{ all -> 0x09ea }
            java.lang.String r4 = "delete from raw_events_metadata where app_id=? and metadata_fingerprint not in (select distinct metadata_fingerprint from raw_events where app_id=?)"
            r6 = 2
            java.lang.String[] r6 = new java.lang.String[r6]     // Catch:{ SQLiteException -> 0x09af }
            r7 = 0
            r6[r7] = r5     // Catch:{ SQLiteException -> 0x09af }
            r7 = 1
            r6[r7] = r5     // Catch:{ SQLiteException -> 0x09af }
            r3.execSQL(r4, r6)     // Catch:{ SQLiteException -> 0x09af }
            goto L_0x09c2
        L_0x09af:
            r0 = move-exception
            r3 = r0
            com.google.android.gms.internal.zzchm r2 = r2.zzawy()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcho r2 = r2.zzazd()     // Catch:{ all -> 0x09ea }
            java.lang.String r4 = "Failed to remove unused event metadata. appId"
            java.lang.Object r5 = com.google.android.gms.internal.zzchm.zzjk(r5)     // Catch:{ all -> 0x09ea }
            r2.zze(r4, r5, r3)     // Catch:{ all -> 0x09ea }
        L_0x09c2:
            com.google.android.gms.internal.zzcgo r2 = r46.zzaws()     // Catch:{ all -> 0x09ea }
            r2.setTransactionSuccessful()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcgo r2 = r46.zzaws()
            r2.endTransaction()
            r2 = 1
            return r2
        L_0x09d2:
            com.google.android.gms.internal.zzcgo r2 = r46.zzaws()     // Catch:{ all -> 0x09ea }
            r2.setTransactionSuccessful()     // Catch:{ all -> 0x09ea }
            com.google.android.gms.internal.zzcgo r2 = r46.zzaws()
            r2.endTransaction()
            r2 = 0
            return r2
        L_0x09e2:
            r0 = move-exception
        L_0x09e3:
            r2 = r0
        L_0x09e4:
            if (r9 == 0) goto L_0x09e9
            r9.close()     // Catch:{ all -> 0x09ea }
        L_0x09e9:
            throw r2     // Catch:{ all -> 0x09ea }
        L_0x09ea:
            r0 = move-exception
            r2 = r0
            com.google.android.gms.internal.zzcgo r3 = r46.zzaws()
            r3.endTransaction()
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcim.zzg(java.lang.String, long):boolean");
    }

    @WorkerThread
    private final zzcgi zzjw(String str) {
        String str2 = str;
        zzcgh zzjb = zzaws().zzjb(str2);
        if (zzjb == null || TextUtils.isEmpty(zzjb.zzvj())) {
            zzawy().zzazi().zzj("No app data available; dropping", str2);
            return null;
        }
        try {
            String str3 = zzbhf.zzdb(this.mContext).getPackageInfo(str2, 0).versionName;
            if (zzjb.zzvj() != null && !zzjb.zzvj().equals(str3)) {
                zzawy().zzazf().zzj("App version does not match; dropping. appId", zzchm.zzjk(str));
                return null;
            }
        } catch (NameNotFoundException e) {
        }
        zzcgi zzcgi = new zzcgi(str2, zzjb.getGmpAppId(), zzjb.zzvj(), zzjb.zzaxg(), zzjb.zzaxh(), zzjb.zzaxi(), zzjb.zzaxj(), (String) null, zzjb.zzaxk(), false, zzjb.zzaxd(), zzjb.zzaxx(), 0, 0, zzjb.zzaxy());
        return zzcgi;
    }

    public final Context getContext() {
        return this.mContext;
    }

    @WorkerThread
    public final boolean isEnabled() {
        zzawx().zzve();
        zzxf();
        boolean z = false;
        if (this.zzjew.zzaya()) {
            return false;
        }
        Boolean zziy = this.zzjew.zziy("firebase_analytics_collection_enabled");
        if (zziy != null) {
            z = zziy.booleanValue();
        } else if (!zzbz.zzaji()) {
            z = true;
        }
        return zzawz().zzbn(z);
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void start() {
        zzawx().zzve();
        zzaws().zzayh();
        if (zzawz().zzjcr.get() == 0) {
            zzawz().zzjcr.set(this.zzata.currentTimeMillis());
        }
        if (Long.valueOf(zzawz().zzjcw.get()).longValue() == 0) {
            zzawy().zzazj().zzj("Persisting first open", Long.valueOf(this.zzjgg));
            zzawz().zzjcw.set(this.zzjgg);
        }
        if (zzazv()) {
            if (!TextUtils.isEmpty(zzawn().getGmpAppId())) {
                String zzazm = zzawz().zzazm();
                if (zzazm == null) {
                    zzawz().zzjo(zzawn().getGmpAppId());
                } else if (!zzazm.equals(zzawn().getGmpAppId())) {
                    zzawy().zzazh().log("Rechecking which service to use due to a GMP App Id change");
                    zzawz().zzazp();
                    this.zzjfk.disconnect();
                    this.zzjfk.zzyc();
                    zzawz().zzjo(zzawn().getGmpAppId());
                    zzawz().zzjcw.set(this.zzjgg);
                    zzawz().zzjcx.zzjq(null);
                }
            }
            zzawm().zzjp(zzawz().zzjcx.zzazr());
            if (!TextUtils.isEmpty(zzawn().getGmpAppId())) {
                zzcjn zzawm = zzawm();
                zzawm.zzve();
                zzawm.zzxf();
                if (zzawm.zziwf.zzazv()) {
                    zzawm.zzawp().zzbar();
                    String zzazq = zzawm.zzawz().zzazq();
                    if (!TextUtils.isEmpty(zzazq)) {
                        zzawm.zzawo().zzxf();
                        if (!zzazq.equals(VERSION.RELEASE)) {
                            Bundle bundle = new Bundle();
                            bundle.putString("_po", zzazq);
                            zzawm.zzc("auto", "_ou", bundle);
                        }
                    }
                }
                zzawp().zza(new AtomicReference<>());
            }
        } else if (isEnabled()) {
            if (!zzawu().zzeb("android.permission.INTERNET")) {
                zzawy().zzazd().log("App is missing INTERNET permission");
            }
            if (!zzawu().zzeb("android.permission.ACCESS_NETWORK_STATE")) {
                zzawy().zzazd().log("App is missing ACCESS_NETWORK_STATE permission");
            }
            if (!zzbhf.zzdb(this.mContext).zzamu()) {
                if (!zzcid.zzbk(this.mContext)) {
                    zzawy().zzazd().log("AppMeasurementReceiver not registered/enabled");
                }
                if (!zzcla.zzk(this.mContext, false)) {
                    zzawy().zzazd().log("AppMeasurementService not registered/enabled");
                }
            }
            zzawy().zzazd().log("Uploading is not possible. App measurement disabled");
        }
        zzbaj();
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zza(int i, Throwable th, byte[] bArr) {
        zzcgo zzaws;
        zzawx().zzve();
        zzxf();
        if (bArr == null) {
            try {
                bArr = new byte[0];
            } catch (Throwable th2) {
                this.zzjge = false;
                zzban();
                throw th2;
            }
        }
        List<Long> list = this.zzjfx;
        this.zzjfx = null;
        boolean z = true;
        if ((i == 200 || i == 204) && th == null) {
            try {
                zzawz().zzjcr.set(this.zzata.currentTimeMillis());
                zzawz().zzjcs.set(0);
                zzbaj();
                zzawy().zzazj().zze("Successful upload. Got network response. code, size", Integer.valueOf(i), Integer.valueOf(bArr.length));
                zzaws().beginTransaction();
                try {
                    for (Long l : list) {
                        zzaws = zzaws();
                        long longValue = l.longValue();
                        zzaws.zzve();
                        zzaws.zzxf();
                        if (zzaws.getWritableDatabase().delete("queue", "rowid=?", new String[]{String.valueOf(longValue)}) != 1) {
                            throw new SQLiteException("Deleted fewer rows from queue than expected");
                        }
                    }
                    zzaws().setTransactionSuccessful();
                    zzaws().endTransaction();
                    if (!zzbab().zzzs() || !zzbai()) {
                        this.zzjgb = -1;
                        zzbaj();
                    } else {
                        zzbah();
                    }
                    this.zzjgc = 0;
                } catch (SQLiteException e) {
                    zzaws.zzawy().zzazd().zzj("Failed to delete a bundle in a queue table", e);
                    throw e;
                } catch (Throwable th3) {
                    zzaws().endTransaction();
                    throw th3;
                }
            } catch (SQLiteException e2) {
                zzawy().zzazd().zzj("Database error while trying to delete uploaded bundles", e2);
                this.zzjgc = this.zzata.elapsedRealtime();
                zzawy().zzazj().zzj("Disable upload, time", Long.valueOf(this.zzjgc));
            }
        } else {
            zzawy().zzazj().zze("Network upload failed. Will retry later. code, error", Integer.valueOf(i), th);
            zzawz().zzjcs.set(this.zzata.currentTimeMillis());
            if (i != 503) {
                if (i != 429) {
                    z = false;
                }
            }
            if (z) {
                zzawz().zzjct.set(this.zzata.currentTimeMillis());
            }
            zzbaj();
        }
        this.zzjge = false;
        zzban();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0072, code lost:
        if (com.google.firebase.analytics.FirebaseAnalytics.Event.ECOMMERCE_PURCHASE.equals(r1.name) != false) goto L_0x0074;
     */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final byte[] zza(@android.support.annotation.NonNull com.google.android.gms.internal.zzcha r34, @android.support.annotation.Size(min = 1) java.lang.String r35) {
        /*
            r33 = this;
            r11 = r33
            r1 = r34
            r10 = r35
            r33.zzxf()
            com.google.android.gms.internal.zzcih r2 = r33.zzawx()
            r2.zzve()
            zzawi()
            com.google.android.gms.common.internal.zzbq.checkNotNull(r34)
            com.google.android.gms.common.internal.zzbq.zzgm(r35)
            com.google.android.gms.internal.zzcmd r8 = new com.google.android.gms.internal.zzcmd
            r8.<init>()
            com.google.android.gms.internal.zzcgo r2 = r33.zzaws()
            r2.beginTransaction()
            com.google.android.gms.internal.zzcgo r2 = r33.zzaws()     // Catch:{ all -> 0x0392 }
            com.google.android.gms.internal.zzcgh r9 = r2.zzjb(r10)     // Catch:{ all -> 0x0392 }
            r6 = 0
            if (r9 != 0) goto L_0x004c
            com.google.android.gms.internal.zzchm r1 = r33.zzawy()     // Catch:{ all -> 0x0047 }
            com.google.android.gms.internal.zzcho r1 = r1.zzazi()     // Catch:{ all -> 0x0047 }
            java.lang.String r2 = "Log and bundle not available. package_name"
            r1.zzj(r2, r10)     // Catch:{ all -> 0x0047 }
        L_0x003d:
            byte[] r1 = new byte[r6]     // Catch:{ all -> 0x0047 }
            com.google.android.gms.internal.zzcgo r2 = r33.zzaws()
            r2.endTransaction()
            return r1
        L_0x0047:
            r0 = move-exception
            r1 = r0
            r5 = r11
            goto L_0x0395
        L_0x004c:
            boolean r2 = r9.zzaxk()     // Catch:{ all -> 0x0392 }
            if (r2 != 0) goto L_0x0060
            com.google.android.gms.internal.zzchm r1 = r33.zzawy()     // Catch:{ all -> 0x0047 }
            com.google.android.gms.internal.zzcho r1 = r1.zzazi()     // Catch:{ all -> 0x0047 }
            java.lang.String r2 = "Log and bundle disabled. package_name"
            r1.zzj(r2, r10)     // Catch:{ all -> 0x0047 }
            goto L_0x003d
        L_0x0060:
            java.lang.String r2 = "_iap"
            java.lang.String r3 = r1.name     // Catch:{ all -> 0x0392 }
            boolean r2 = r2.equals(r3)     // Catch:{ all -> 0x0392 }
            if (r2 != 0) goto L_0x0074
            java.lang.String r2 = "ecommerce_purchase"
            java.lang.String r3 = r1.name     // Catch:{ all -> 0x0047 }
            boolean r2 = r2.equals(r3)     // Catch:{ all -> 0x0047 }
            if (r2 == 0) goto L_0x008b
        L_0x0074:
            boolean r2 = r11.zza(r10, r1)     // Catch:{ all -> 0x0392 }
            if (r2 != 0) goto L_0x008b
            com.google.android.gms.internal.zzchm r2 = r33.zzawy()     // Catch:{ all -> 0x0047 }
            com.google.android.gms.internal.zzcho r2 = r2.zzazf()     // Catch:{ all -> 0x0047 }
            java.lang.String r3 = "Failed to handle purchase event at single event bundle creation. appId"
            java.lang.Object r4 = com.google.android.gms.internal.zzchm.zzjk(r35)     // Catch:{ all -> 0x0047 }
            r2.zzj(r3, r4)     // Catch:{ all -> 0x0047 }
        L_0x008b:
            com.google.android.gms.internal.zzcme r7 = new com.google.android.gms.internal.zzcme     // Catch:{ all -> 0x0392 }
            r7.<init>()     // Catch:{ all -> 0x0392 }
            r5 = 1
            com.google.android.gms.internal.zzcme[] r2 = new com.google.android.gms.internal.zzcme[r5]     // Catch:{ all -> 0x0392 }
            r2[r6] = r7     // Catch:{ all -> 0x0392 }
            r8.zzjlm = r2     // Catch:{ all -> 0x0392 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r5)     // Catch:{ all -> 0x0392 }
            r7.zzjlo = r2     // Catch:{ all -> 0x0392 }
            java.lang.String r2 = "android"
            r7.zzjlw = r2     // Catch:{ all -> 0x0392 }
            java.lang.String r2 = r9.getAppId()     // Catch:{ all -> 0x0392 }
            r7.zzcn = r2     // Catch:{ all -> 0x0392 }
            java.lang.String r2 = r9.zzaxh()     // Catch:{ all -> 0x0392 }
            r7.zzixt = r2     // Catch:{ all -> 0x0392 }
            java.lang.String r2 = r9.zzvj()     // Catch:{ all -> 0x0392 }
            r7.zzifm = r2     // Catch:{ all -> 0x0392 }
            long r2 = r9.zzaxg()     // Catch:{ all -> 0x0392 }
            r12 = -2147483648(0xffffffff80000000, double:NaN)
            int r4 = (r2 > r12 ? 1 : (r2 == r12 ? 0 : -1))
            r15 = 0
            if (r4 != 0) goto L_0x00c1
            r2 = r15
            goto L_0x00c6
        L_0x00c1:
            int r2 = (int) r2     // Catch:{ all -> 0x0392 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ all -> 0x0392 }
        L_0x00c6:
            r7.zzjmj = r2     // Catch:{ all -> 0x0392 }
            long r2 = r9.zzaxi()     // Catch:{ all -> 0x0392 }
            java.lang.Long r2 = java.lang.Long.valueOf(r2)     // Catch:{ all -> 0x0392 }
            r7.zzjma = r2     // Catch:{ all -> 0x0392 }
            java.lang.String r2 = r9.getGmpAppId()     // Catch:{ all -> 0x0392 }
            r7.zzixs = r2     // Catch:{ all -> 0x0392 }
            long r2 = r9.zzaxj()     // Catch:{ all -> 0x0392 }
            java.lang.Long r2 = java.lang.Long.valueOf(r2)     // Catch:{ all -> 0x0392 }
            r7.zzjmf = r2     // Catch:{ all -> 0x0392 }
            boolean r2 = r33.isEnabled()     // Catch:{ all -> 0x0392 }
            if (r2 == 0) goto L_0x00fd
            boolean r2 = com.google.android.gms.internal.zzcgn.zzaye()     // Catch:{ all -> 0x0047 }
            if (r2 == 0) goto L_0x00fd
            com.google.android.gms.internal.zzcgn r2 = r11.zzjew     // Catch:{ all -> 0x0047 }
            java.lang.String r3 = r7.zzcn     // Catch:{ all -> 0x0047 }
            boolean r2 = r2.zziz(r3)     // Catch:{ all -> 0x0047 }
            if (r2 == 0) goto L_0x00fd
            r33.zzawn()     // Catch:{ all -> 0x0047 }
            r7.zzjmo = r15     // Catch:{ all -> 0x0047 }
        L_0x00fd:
            com.google.android.gms.internal.zzchx r2 = r33.zzawz()     // Catch:{ all -> 0x0392 }
            java.lang.String r3 = r9.getAppId()     // Catch:{ all -> 0x0392 }
            android.util.Pair r2 = r2.zzjm(r3)     // Catch:{ all -> 0x0392 }
            boolean r3 = r9.zzaxy()     // Catch:{ all -> 0x0392 }
            if (r3 == 0) goto L_0x0127
            if (r2 == 0) goto L_0x0127
            java.lang.Object r3 = r2.first     // Catch:{ all -> 0x0047 }
            java.lang.CharSequence r3 = (java.lang.CharSequence) r3     // Catch:{ all -> 0x0047 }
            boolean r3 = android.text.TextUtils.isEmpty(r3)     // Catch:{ all -> 0x0047 }
            if (r3 != 0) goto L_0x0127
            java.lang.Object r3 = r2.first     // Catch:{ all -> 0x0047 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ all -> 0x0047 }
            r7.zzjmc = r3     // Catch:{ all -> 0x0047 }
            java.lang.Object r2 = r2.second     // Catch:{ all -> 0x0047 }
            java.lang.Boolean r2 = (java.lang.Boolean) r2     // Catch:{ all -> 0x0047 }
            r7.zzjmd = r2     // Catch:{ all -> 0x0047 }
        L_0x0127:
            com.google.android.gms.internal.zzcgu r2 = r33.zzawo()     // Catch:{ all -> 0x0392 }
            r2.zzxf()     // Catch:{ all -> 0x0392 }
            java.lang.String r2 = android.os.Build.MODEL     // Catch:{ all -> 0x0392 }
            r7.zzjlx = r2     // Catch:{ all -> 0x0392 }
            com.google.android.gms.internal.zzcgu r2 = r33.zzawo()     // Catch:{ all -> 0x0392 }
            r2.zzxf()     // Catch:{ all -> 0x0392 }
            java.lang.String r2 = android.os.Build.VERSION.RELEASE     // Catch:{ all -> 0x0392 }
            r7.zzdb = r2     // Catch:{ all -> 0x0392 }
            com.google.android.gms.internal.zzcgu r2 = r33.zzawo()     // Catch:{ all -> 0x0392 }
            long r2 = r2.zzayu()     // Catch:{ all -> 0x0392 }
            int r2 = (int) r2     // Catch:{ all -> 0x0392 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ all -> 0x0392 }
            r7.zzjlz = r2     // Catch:{ all -> 0x0392 }
            com.google.android.gms.internal.zzcgu r2 = r33.zzawo()     // Catch:{ all -> 0x0392 }
            java.lang.String r2 = r2.zzayv()     // Catch:{ all -> 0x0392 }
            r7.zzjly = r2     // Catch:{ all -> 0x0392 }
            java.lang.String r2 = r9.getAppInstanceId()     // Catch:{ all -> 0x0392 }
            r7.zzjme = r2     // Catch:{ all -> 0x0392 }
            java.lang.String r2 = r9.zzaxd()     // Catch:{ all -> 0x0392 }
            r7.zziya = r2     // Catch:{ all -> 0x0392 }
            com.google.android.gms.internal.zzcgo r2 = r33.zzaws()     // Catch:{ all -> 0x0392 }
            java.lang.String r3 = r9.getAppId()     // Catch:{ all -> 0x0392 }
            java.util.List r2 = r2.zzja(r3)     // Catch:{ all -> 0x0392 }
            int r3 = r2.size()     // Catch:{ all -> 0x0392 }
            com.google.android.gms.internal.zzcmg[] r3 = new com.google.android.gms.internal.zzcmg[r3]     // Catch:{ all -> 0x0392 }
            r7.zzjlq = r3     // Catch:{ all -> 0x0392 }
            r3 = 0
        L_0x0177:
            int r4 = r2.size()     // Catch:{ all -> 0x0392 }
            if (r3 >= r4) goto L_0x01b0
            com.google.android.gms.internal.zzcmg r4 = new com.google.android.gms.internal.zzcmg     // Catch:{ all -> 0x0047 }
            r4.<init>()     // Catch:{ all -> 0x0047 }
            com.google.android.gms.internal.zzcmg[] r12 = r7.zzjlq     // Catch:{ all -> 0x0047 }
            r12[r3] = r4     // Catch:{ all -> 0x0047 }
            java.lang.Object r12 = r2.get(r3)     // Catch:{ all -> 0x0047 }
            com.google.android.gms.internal.zzclp r12 = (com.google.android.gms.internal.zzclp) r12     // Catch:{ all -> 0x0047 }
            java.lang.String r12 = r12.mName     // Catch:{ all -> 0x0047 }
            r4.name = r12     // Catch:{ all -> 0x0047 }
            java.lang.Object r12 = r2.get(r3)     // Catch:{ all -> 0x0047 }
            com.google.android.gms.internal.zzclp r12 = (com.google.android.gms.internal.zzclp) r12     // Catch:{ all -> 0x0047 }
            long r12 = r12.zzjjm     // Catch:{ all -> 0x0047 }
            java.lang.Long r12 = java.lang.Long.valueOf(r12)     // Catch:{ all -> 0x0047 }
            r4.zzjms = r12     // Catch:{ all -> 0x0047 }
            com.google.android.gms.internal.zzclq r12 = r33.zzawu()     // Catch:{ all -> 0x0047 }
            java.lang.Object r13 = r2.get(r3)     // Catch:{ all -> 0x0047 }
            com.google.android.gms.internal.zzclp r13 = (com.google.android.gms.internal.zzclp) r13     // Catch:{ all -> 0x0047 }
            java.lang.Object r13 = r13.mValue     // Catch:{ all -> 0x0047 }
            r12.zza(r4, r13)     // Catch:{ all -> 0x0047 }
            int r3 = r3 + 1
            goto L_0x0177
        L_0x01b0:
            com.google.android.gms.internal.zzcgx r2 = r1.zzizt     // Catch:{ all -> 0x0392 }
            android.os.Bundle r4 = r2.zzayx()     // Catch:{ all -> 0x0392 }
            java.lang.String r2 = "_iap"
            java.lang.String r3 = r1.name     // Catch:{ all -> 0x0392 }
            boolean r2 = r2.equals(r3)     // Catch:{ all -> 0x0392 }
            r12 = 1
            if (r2 == 0) goto L_0x01d9
            java.lang.String r2 = "_c"
            r4.putLong(r2, r12)     // Catch:{ all -> 0x0047 }
            com.google.android.gms.internal.zzchm r2 = r33.zzawy()     // Catch:{ all -> 0x0047 }
            com.google.android.gms.internal.zzcho r2 = r2.zzazi()     // Catch:{ all -> 0x0047 }
            java.lang.String r3 = "Marking in-app purchase as real-time"
            r2.log(r3)     // Catch:{ all -> 0x0047 }
            java.lang.String r2 = "_r"
            r4.putLong(r2, r12)     // Catch:{ all -> 0x0047 }
        L_0x01d9:
            java.lang.String r2 = "_o"
            java.lang.String r3 = r1.zziyf     // Catch:{ all -> 0x0392 }
            r4.putString(r2, r3)     // Catch:{ all -> 0x0392 }
            com.google.android.gms.internal.zzclq r2 = r33.zzawu()     // Catch:{ all -> 0x0392 }
            java.lang.String r3 = r7.zzcn     // Catch:{ all -> 0x0392 }
            boolean r2 = r2.zzkj(r3)     // Catch:{ all -> 0x0392 }
            if (r2 == 0) goto L_0x0206
            com.google.android.gms.internal.zzclq r2 = r33.zzawu()     // Catch:{ all -> 0x0047 }
            java.lang.String r3 = "_dbg"
            java.lang.Long r14 = java.lang.Long.valueOf(r12)     // Catch:{ all -> 0x0047 }
            r2.zza(r4, r3, r14)     // Catch:{ all -> 0x0047 }
            com.google.android.gms.internal.zzclq r2 = r33.zzawu()     // Catch:{ all -> 0x0047 }
            java.lang.String r3 = "_r"
            java.lang.Long r12 = java.lang.Long.valueOf(r12)     // Catch:{ all -> 0x0047 }
            r2.zza(r4, r3, r12)     // Catch:{ all -> 0x0047 }
        L_0x0206:
            com.google.android.gms.internal.zzcgo r2 = r33.zzaws()     // Catch:{ all -> 0x0392 }
            java.lang.String r3 = r1.name     // Catch:{ all -> 0x0392 }
            com.google.android.gms.internal.zzcgw r2 = r2.zzae(r10, r3)     // Catch:{ all -> 0x0392 }
            r26 = 0
            if (r2 != 0) goto L_0x023f
            com.google.android.gms.internal.zzcgw r2 = new com.google.android.gms.internal.zzcgw     // Catch:{ all -> 0x0047 }
            java.lang.String r14 = r1.name     // Catch:{ all -> 0x0047 }
            r16 = 1
            r18 = 0
            long r12 = r1.zzizu     // Catch:{ all -> 0x0047 }
            r21 = 0
            r23 = 0
            r24 = 0
            r25 = 0
            r28 = r12
            r12 = r2
            r13 = r10
            r30 = r15
            r15 = r16
            r17 = r18
            r19 = r28
            r12.<init>(r13, r14, r15, r17, r19, r21, r23, r24, r25)     // Catch:{ all -> 0x0047 }
            com.google.android.gms.internal.zzcgo r3 = r33.zzaws()     // Catch:{ all -> 0x0047 }
            r3.zza(r2)     // Catch:{ all -> 0x0047 }
            r12 = r26
            goto L_0x0254
        L_0x023f:
            r30 = r15
            long r12 = r2.zzizm     // Catch:{ all -> 0x0392 }
            long r14 = r1.zzizu     // Catch:{ all -> 0x0392 }
            com.google.android.gms.internal.zzcgw r2 = r2.zzbb(r14)     // Catch:{ all -> 0x0392 }
            com.google.android.gms.internal.zzcgw r2 = r2.zzayw()     // Catch:{ all -> 0x0392 }
            com.google.android.gms.internal.zzcgo r3 = r33.zzaws()     // Catch:{ all -> 0x0392 }
            r3.zza(r2)     // Catch:{ all -> 0x0392 }
        L_0x0254:
            com.google.android.gms.internal.zzcgv r14 = new com.google.android.gms.internal.zzcgv     // Catch:{ all -> 0x0392 }
            java.lang.String r3 = r1.zziyf     // Catch:{ all -> 0x0392 }
            java.lang.String r15 = r1.name     // Catch:{ all -> 0x0392 }
            long r1 = r1.zzizu     // Catch:{ all -> 0x0392 }
            r16 = r1
            r1 = r14
            r2 = r11
            r18 = r4
            r4 = r10
            r11 = 1
            r5 = r15
            r15 = r7
            r6 = r16
            r31 = r8
            r32 = r9
            r8 = r12
            r10 = r18
            r1.<init>(r2, r3, r4, r5, r6, r8, r10)     // Catch:{ all -> 0x038e }
            com.google.android.gms.internal.zzcmb r1 = new com.google.android.gms.internal.zzcmb     // Catch:{ all -> 0x038e }
            r1.<init>()     // Catch:{ all -> 0x038e }
            com.google.android.gms.internal.zzcmb[] r2 = new com.google.android.gms.internal.zzcmb[r11]     // Catch:{ all -> 0x038e }
            r3 = 0
            r2[r3] = r1     // Catch:{ all -> 0x038e }
            r15.zzjlp = r2     // Catch:{ all -> 0x038e }
            long r4 = r14.zzfij     // Catch:{ all -> 0x038e }
            java.lang.Long r2 = java.lang.Long.valueOf(r4)     // Catch:{ all -> 0x038e }
            r1.zzjli = r2     // Catch:{ all -> 0x038e }
            java.lang.String r2 = r14.mName     // Catch:{ all -> 0x038e }
            r1.name = r2     // Catch:{ all -> 0x038e }
            long r4 = r14.zzizi     // Catch:{ all -> 0x038e }
            java.lang.Long r2 = java.lang.Long.valueOf(r4)     // Catch:{ all -> 0x038e }
            r1.zzjlj = r2     // Catch:{ all -> 0x038e }
            com.google.android.gms.internal.zzcgx r2 = r14.zzizj     // Catch:{ all -> 0x038e }
            int r2 = r2.size()     // Catch:{ all -> 0x038e }
            com.google.android.gms.internal.zzcmc[] r2 = new com.google.android.gms.internal.zzcmc[r2]     // Catch:{ all -> 0x038e }
            r1.zzjlh = r2     // Catch:{ all -> 0x038e }
            com.google.android.gms.internal.zzcgx r2 = r14.zzizj     // Catch:{ all -> 0x038e }
            java.util.Iterator r2 = r2.iterator()     // Catch:{ all -> 0x038e }
            r4 = 0
        L_0x02a3:
            boolean r5 = r2.hasNext()     // Catch:{ all -> 0x038e }
            if (r5 == 0) goto L_0x02cd
            java.lang.Object r5 = r2.next()     // Catch:{ all -> 0x038e }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ all -> 0x038e }
            com.google.android.gms.internal.zzcmc r6 = new com.google.android.gms.internal.zzcmc     // Catch:{ all -> 0x038e }
            r6.<init>()     // Catch:{ all -> 0x038e }
            com.google.android.gms.internal.zzcmc[] r7 = r1.zzjlh     // Catch:{ all -> 0x038e }
            int r8 = r4 + 1
            r7[r4] = r6     // Catch:{ all -> 0x038e }
            r6.name = r5     // Catch:{ all -> 0x038e }
            com.google.android.gms.internal.zzcgx r4 = r14.zzizj     // Catch:{ all -> 0x038e }
            java.lang.Object r4 = r4.get(r5)     // Catch:{ all -> 0x038e }
            r5 = r33
            com.google.android.gms.internal.zzclq r7 = r33.zzawu()     // Catch:{ all -> 0x038c }
            r7.zza(r6, r4)     // Catch:{ all -> 0x038c }
            r4 = r8
            goto L_0x02a3
        L_0x02cd:
            r2 = r32
            r5 = r33
            java.lang.String r4 = r2.getAppId()     // Catch:{ all -> 0x038c }
            com.google.android.gms.internal.zzcmg[] r6 = r15.zzjlq     // Catch:{ all -> 0x038c }
            com.google.android.gms.internal.zzcmb[] r7 = r15.zzjlp     // Catch:{ all -> 0x038c }
            com.google.android.gms.internal.zzcma[] r4 = r5.zza(r4, r6, r7)     // Catch:{ all -> 0x038c }
            r15.zzjmi = r4     // Catch:{ all -> 0x038c }
            java.lang.Long r4 = r1.zzjli     // Catch:{ all -> 0x038c }
            r15.zzjls = r4     // Catch:{ all -> 0x038c }
            java.lang.Long r1 = r1.zzjli     // Catch:{ all -> 0x038c }
            r15.zzjlt = r1     // Catch:{ all -> 0x038c }
            long r6 = r2.zzaxf()     // Catch:{ all -> 0x038c }
            int r1 = (r6 > r26 ? 1 : (r6 == r26 ? 0 : -1))
            if (r1 == 0) goto L_0x02f4
            java.lang.Long r1 = java.lang.Long.valueOf(r6)     // Catch:{ all -> 0x038c }
            goto L_0x02f6
        L_0x02f4:
            r1 = r30
        L_0x02f6:
            r15.zzjlv = r1     // Catch:{ all -> 0x038c }
            long r8 = r2.zzaxe()     // Catch:{ all -> 0x038c }
            int r1 = (r8 > r26 ? 1 : (r8 == r26 ? 0 : -1))
            if (r1 != 0) goto L_0x0301
            goto L_0x0302
        L_0x0301:
            r6 = r8
        L_0x0302:
            int r1 = (r6 > r26 ? 1 : (r6 == r26 ? 0 : -1))
            if (r1 == 0) goto L_0x030b
            java.lang.Long r1 = java.lang.Long.valueOf(r6)     // Catch:{ all -> 0x038c }
            goto L_0x030d
        L_0x030b:
            r1 = r30
        L_0x030d:
            r15.zzjlu = r1     // Catch:{ all -> 0x038c }
            r2.zzaxo()     // Catch:{ all -> 0x038c }
            long r6 = r2.zzaxl()     // Catch:{ all -> 0x038c }
            int r1 = (int) r6     // Catch:{ all -> 0x038c }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x038c }
            r15.zzjmg = r1     // Catch:{ all -> 0x038c }
            r6 = 11910(0x2e86, double:5.8843E-320)
            java.lang.Long r1 = java.lang.Long.valueOf(r6)     // Catch:{ all -> 0x038c }
            r15.zzjmb = r1     // Catch:{ all -> 0x038c }
            com.google.android.gms.common.util.zzd r1 = r5.zzata     // Catch:{ all -> 0x038c }
            long r6 = r1.currentTimeMillis()     // Catch:{ all -> 0x038c }
            java.lang.Long r1 = java.lang.Long.valueOf(r6)     // Catch:{ all -> 0x038c }
            r15.zzjlr = r1     // Catch:{ all -> 0x038c }
            java.lang.Boolean r1 = java.lang.Boolean.TRUE     // Catch:{ all -> 0x038c }
            r15.zzjmh = r1     // Catch:{ all -> 0x038c }
            java.lang.Long r1 = r15.zzjls     // Catch:{ all -> 0x038c }
            long r6 = r1.longValue()     // Catch:{ all -> 0x038c }
            r2.zzal(r6)     // Catch:{ all -> 0x038c }
            java.lang.Long r1 = r15.zzjlt     // Catch:{ all -> 0x038c }
            long r6 = r1.longValue()     // Catch:{ all -> 0x038c }
            r2.zzam(r6)     // Catch:{ all -> 0x038c }
            com.google.android.gms.internal.zzcgo r1 = r33.zzaws()     // Catch:{ all -> 0x038c }
            r1.zza(r2)     // Catch:{ all -> 0x038c }
            com.google.android.gms.internal.zzcgo r1 = r33.zzaws()     // Catch:{ all -> 0x038c }
            r1.setTransactionSuccessful()     // Catch:{ all -> 0x038c }
            com.google.android.gms.internal.zzcgo r1 = r33.zzaws()
            r1.endTransaction()
            r1 = r31
            int r2 = r1.zzho()     // Catch:{ IOException -> 0x0378 }
            byte[] r2 = new byte[r2]     // Catch:{ IOException -> 0x0378 }
            int r4 = r2.length     // Catch:{ IOException -> 0x0378 }
            com.google.android.gms.internal.zzfjk r3 = com.google.android.gms.internal.zzfjk.zzo(r2, r3, r4)     // Catch:{ IOException -> 0x0378 }
            r1.zza(r3)     // Catch:{ IOException -> 0x0378 }
            r3.zzcwt()     // Catch:{ IOException -> 0x0378 }
            com.google.android.gms.internal.zzclq r1 = r33.zzawu()     // Catch:{ IOException -> 0x0378 }
            byte[] r1 = r1.zzq(r2)     // Catch:{ IOException -> 0x0378 }
            return r1
        L_0x0378:
            r0 = move-exception
            r1 = r0
            com.google.android.gms.internal.zzchm r2 = r33.zzawy()
            com.google.android.gms.internal.zzcho r2 = r2.zzazd()
            java.lang.String r3 = "Data loss. Failed to bundle and serialize. appId"
            java.lang.Object r4 = com.google.android.gms.internal.zzchm.zzjk(r35)
            r2.zze(r3, r4, r1)
            return r30
        L_0x038c:
            r0 = move-exception
            goto L_0x0394
        L_0x038e:
            r0 = move-exception
            r5 = r33
            goto L_0x0394
        L_0x0392:
            r0 = move-exception
            r5 = r11
        L_0x0394:
            r1 = r0
        L_0x0395:
            com.google.android.gms.internal.zzcgo r2 = r33.zzaws()
            r2.endTransaction()
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcim.zza(com.google.android.gms.internal.zzcha, java.lang.String):byte[]");
    }

    public final zzcgd zzawk() {
        zza((zzcjk) this.zzjfr);
        return this.zzjfr;
    }

    public final zzcgk zzawl() {
        zza((zzcjl) this.zzjfq);
        return this.zzjfq;
    }

    public final zzcjn zzawm() {
        zza((zzcjl) this.zzjfm);
        return this.zzjfm;
    }

    public final zzchh zzawn() {
        zza((zzcjl) this.zzjfn);
        return this.zzjfn;
    }

    public final zzcgu zzawo() {
        zza((zzcjl) this.zzjfl);
        return this.zzjfl;
    }

    public final zzckg zzawp() {
        zza((zzcjl) this.zzjfk);
        return this.zzjfk;
    }

    public final zzckc zzawq() {
        zza((zzcjl) this.zzjfj);
        return this.zzjfj;
    }

    public final zzchi zzawr() {
        zza((zzcjl) this.zzjfh);
        return this.zzjfh;
    }

    public final zzcgo zzaws() {
        zza((zzcjl) this.zzjfg);
        return this.zzjfg;
    }

    public final zzchk zzawt() {
        zza((zzcjk) this.zzjff);
        return this.zzjff;
    }

    public final zzclq zzawu() {
        zza((zzcjk) this.zzjfe);
        return this.zzjfe;
    }

    public final zzcig zzawv() {
        zza((zzcjl) this.zzjfb);
        return this.zzjfb;
    }

    public final zzclf zzaww() {
        zza((zzcjl) this.zzjfa);
        return this.zzjfa;
    }

    public final zzcih zzawx() {
        zza((zzcjl) this.zzjez);
        return this.zzjez;
    }

    public final zzchm zzawy() {
        zza((zzcjl) this.zzjey);
        return this.zzjey;
    }

    public final zzchx zzawz() {
        zza((zzcjk) this.zzjex);
        return this.zzjex;
    }

    public final zzcgn zzaxa() {
        return this.zzjew;
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final boolean zzazv() {
        zzxf();
        zzawx().zzve();
        if (this.zzjft == null || this.zzjfu == 0 || (this.zzjft != null && !this.zzjft.booleanValue() && Math.abs(this.zzata.elapsedRealtime() - this.zzjfu) > 1000)) {
            this.zzjfu = this.zzata.elapsedRealtime();
            boolean z = false;
            if (zzawu().zzeb("android.permission.INTERNET") && zzawu().zzeb("android.permission.ACCESS_NETWORK_STATE") && (zzbhf.zzdb(this.mContext).zzamu() || (zzcid.zzbk(this.mContext) && zzcla.zzk(this.mContext, false)))) {
                z = true;
            }
            this.zzjft = Boolean.valueOf(z);
            if (this.zzjft.booleanValue()) {
                this.zzjft = Boolean.valueOf(zzawu().zzkg(zzawn().getGmpAppId()));
            }
        }
        return this.zzjft.booleanValue();
    }

    public final zzchm zzazx() {
        if (this.zzjey == null || !this.zzjey.isInitialized()) {
            return null;
        }
        return this.zzjey;
    }

    /* access modifiers changed from: 0000 */
    public final zzcih zzazy() {
        return this.zzjez;
    }

    public final AppMeasurement zzazz() {
        return this.zzjfc;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzb(zzcgl zzcgl, zzcgi zzcgi) {
        zzcho zzazd;
        String str;
        Object zzjk;
        String zzjj;
        Object value;
        zzcho zzazd2;
        String str2;
        Object zzjk2;
        String zzjj2;
        Object obj;
        zzbq.checkNotNull(zzcgl);
        zzbq.zzgm(zzcgl.packageName);
        zzbq.checkNotNull(zzcgl.zziyf);
        zzbq.checkNotNull(zzcgl.zziyg);
        zzbq.zzgm(zzcgl.zziyg.name);
        zzawx().zzve();
        zzxf();
        if (!TextUtils.isEmpty(zzcgi.zzixs)) {
            if (!zzcgi.zzixx) {
                zzg(zzcgi);
                return;
            }
            zzcgl zzcgl2 = new zzcgl(zzcgl);
            boolean z = false;
            zzcgl2.zziyi = false;
            zzaws().beginTransaction();
            try {
                zzcgl zzah = zzaws().zzah(zzcgl2.packageName, zzcgl2.zziyg.name);
                if (zzah != null && !zzah.zziyf.equals(zzcgl2.zziyf)) {
                    zzawy().zzazf().zzd("Updating a conditional user property with different origin. name, origin, origin (from DB)", zzawt().zzjj(zzcgl2.zziyg.name), zzcgl2.zziyf, zzah.zziyf);
                }
                if (zzah != null && zzah.zziyi) {
                    zzcgl2.zziyf = zzah.zziyf;
                    zzcgl2.zziyh = zzah.zziyh;
                    zzcgl2.zziyl = zzah.zziyl;
                    zzcgl2.zziyj = zzah.zziyj;
                    zzcgl2.zziym = zzah.zziym;
                    zzcgl2.zziyi = zzah.zziyi;
                    zzcln zzcln = new zzcln(zzcgl2.zziyg.name, zzah.zziyg.zzjji, zzcgl2.zziyg.getValue(), zzah.zziyg.zziyf);
                    zzcgl2.zziyg = zzcln;
                } else if (TextUtils.isEmpty(zzcgl2.zziyj)) {
                    zzcln zzcln2 = new zzcln(zzcgl2.zziyg.name, zzcgl2.zziyh, zzcgl2.zziyg.getValue(), zzcgl2.zziyg.zziyf);
                    zzcgl2.zziyg = zzcln2;
                    zzcgl2.zziyi = true;
                    z = true;
                }
                if (zzcgl2.zziyi) {
                    zzcln zzcln3 = zzcgl2.zziyg;
                    zzclp zzclp = new zzclp(zzcgl2.packageName, zzcgl2.zziyf, zzcln3.name, zzcln3.zzjji, zzcln3.getValue());
                    if (zzaws().zza(zzclp)) {
                        zzazd2 = zzawy().zzazi();
                        str2 = "User property updated immediately";
                        zzjk2 = zzcgl2.packageName;
                        zzjj2 = zzawt().zzjj(zzclp.mName);
                        obj = zzclp.mValue;
                    } else {
                        zzazd2 = zzawy().zzazd();
                        str2 = "(2)Too many active user properties, ignoring";
                        zzjk2 = zzchm.zzjk(zzcgl2.packageName);
                        zzjj2 = zzawt().zzjj(zzclp.mName);
                        obj = zzclp.mValue;
                    }
                    zzazd2.zzd(str2, zzjk2, zzjj2, obj);
                    if (z && zzcgl2.zziym != null) {
                        zzc(new zzcha(zzcgl2.zziym, zzcgl2.zziyh), zzcgi);
                    }
                }
                if (zzaws().zza(zzcgl2)) {
                    zzazd = zzawy().zzazi();
                    str = "Conditional property added";
                    zzjk = zzcgl2.packageName;
                    zzjj = zzawt().zzjj(zzcgl2.zziyg.name);
                    value = zzcgl2.zziyg.getValue();
                } else {
                    zzazd = zzawy().zzazd();
                    str = "Too many conditional properties, ignoring";
                    zzjk = zzchm.zzjk(zzcgl2.packageName);
                    zzjj = zzawt().zzjj(zzcgl2.zziyg.name);
                    value = zzcgl2.zziyg.getValue();
                }
                zzazd.zzd(str, zzjk, zzjj, value);
                zzaws().setTransactionSuccessful();
            } finally {
                zzaws().endTransaction();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzb(zzcha zzcha, zzcgi zzcgi) {
        List<zzcgl> list;
        List<zzcgl> list2;
        List list3;
        zzcho zzazd;
        String str;
        Object zzjk;
        String zzjj;
        Object obj;
        zzcha zzcha2 = zzcha;
        zzcgi zzcgi2 = zzcgi;
        zzbq.checkNotNull(zzcgi);
        zzbq.zzgm(zzcgi2.packageName);
        zzawx().zzve();
        zzxf();
        String str2 = zzcgi2.packageName;
        long j = zzcha2.zzizu;
        zzawu();
        if (zzclq.zzd(zzcha, zzcgi)) {
            if (!zzcgi2.zzixx) {
                zzg(zzcgi2);
                return;
            }
            zzaws().beginTransaction();
            try {
                zzcgo zzaws = zzaws();
                zzbq.zzgm(str2);
                zzaws.zzve();
                zzaws.zzxf();
                int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
                if (i < 0) {
                    zzaws.zzawy().zzazf().zze("Invalid time querying timed out conditional properties", zzchm.zzjk(str2), Long.valueOf(j));
                    list = Collections.emptyList();
                } else {
                    list = zzaws.zzc("active=0 and app_id=? and abs(? - creation_timestamp) > trigger_timeout", new String[]{str2, String.valueOf(j)});
                }
                for (zzcgl zzcgl : list) {
                    if (zzcgl != null) {
                        zzawy().zzazi().zzd("User property timed out", zzcgl.packageName, zzawt().zzjj(zzcgl.zziyg.name), zzcgl.zziyg.getValue());
                        if (zzcgl.zziyk != null) {
                            zzc(new zzcha(zzcgl.zziyk, j), zzcgi2);
                        }
                        zzaws().zzai(str2, zzcgl.zziyg.name);
                    }
                }
                zzcgo zzaws2 = zzaws();
                zzbq.zzgm(str2);
                zzaws2.zzve();
                zzaws2.zzxf();
                if (i < 0) {
                    zzaws2.zzawy().zzazf().zze("Invalid time querying expired conditional properties", zzchm.zzjk(str2), Long.valueOf(j));
                    list2 = Collections.emptyList();
                } else {
                    list2 = zzaws2.zzc("active<>0 and app_id=? and abs(? - triggered_timestamp) > time_to_live", new String[]{str2, String.valueOf(j)});
                }
                ArrayList arrayList = new ArrayList(list2.size());
                for (zzcgl zzcgl2 : list2) {
                    if (zzcgl2 != null) {
                        zzawy().zzazi().zzd("User property expired", zzcgl2.packageName, zzawt().zzjj(zzcgl2.zziyg.name), zzcgl2.zziyg.getValue());
                        zzaws().zzaf(str2, zzcgl2.zziyg.name);
                        if (zzcgl2.zziyo != null) {
                            arrayList.add(zzcgl2.zziyo);
                        }
                        zzaws().zzai(str2, zzcgl2.zziyg.name);
                    }
                }
                ArrayList arrayList2 = arrayList;
                int size = arrayList2.size();
                int i2 = 0;
                while (i2 < size) {
                    Object obj2 = arrayList2.get(i2);
                    i2++;
                    zzc(new zzcha((zzcha) obj2, j), zzcgi2);
                }
                zzcgo zzaws3 = zzaws();
                String str3 = zzcha2.name;
                zzbq.zzgm(str2);
                zzbq.zzgm(str3);
                zzaws3.zzve();
                zzaws3.zzxf();
                if (i < 0) {
                    zzaws3.zzawy().zzazf().zzd("Invalid time querying triggered conditional properties", zzchm.zzjk(str2), zzaws3.zzawt().zzjh(str3), Long.valueOf(j));
                    list3 = Collections.emptyList();
                } else {
                    list3 = zzaws3.zzc("active=0 and app_id=? and trigger_event_name=? and abs(? - creation_timestamp) <= trigger_timeout", new String[]{str2, str3, String.valueOf(j)});
                }
                ArrayList arrayList3 = new ArrayList(list3.size());
                Iterator it = list3.iterator();
                while (it.hasNext()) {
                    zzcgl zzcgl3 = (zzcgl) it.next();
                    if (zzcgl3 != null) {
                        zzcln zzcln = zzcgl3.zziyg;
                        zzclp zzclp = r5;
                        Iterator it2 = it;
                        zzcgl zzcgl4 = zzcgl3;
                        zzclp zzclp2 = new zzclp(zzcgl3.packageName, zzcgl3.zziyf, zzcln.name, j, zzcln.getValue());
                        if (zzaws().zza(zzclp)) {
                            zzazd = zzawy().zzazi();
                            str = "User property triggered";
                            zzjk = zzcgl4.packageName;
                            zzjj = zzawt().zzjj(zzclp.mName);
                            obj = zzclp.mValue;
                        } else {
                            zzazd = zzawy().zzazd();
                            str = "Too many active user properties, ignoring";
                            zzjk = zzchm.zzjk(zzcgl4.packageName);
                            zzjj = zzawt().zzjj(zzclp.mName);
                            obj = zzclp.mValue;
                        }
                        zzazd.zzd(str, zzjk, zzjj, obj);
                        if (zzcgl4.zziym != null) {
                            arrayList3.add(zzcgl4.zziym);
                        }
                        zzcgl4.zziyg = new zzcln(zzclp);
                        zzcgl4.zziyi = true;
                        zzaws().zza(zzcgl4);
                        it = it2;
                    }
                }
                zzc(zzcha, zzcgi);
                ArrayList arrayList4 = arrayList3;
                int size2 = arrayList4.size();
                int i3 = 0;
                while (i3 < size2) {
                    Object obj3 = arrayList4.get(i3);
                    i3++;
                    zzc(new zzcha((zzcha) obj3, j), zzcgi2);
                }
                zzaws().setTransactionSuccessful();
                zzaws().endTransaction();
            } catch (Throwable th) {
                Throwable th2 = th;
                zzaws().endTransaction();
                throw th2;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzb(zzcha zzcha, String str) {
        zzcha zzcha2 = zzcha;
        String str2 = str;
        zzcgh zzjb = zzaws().zzjb(str2);
        if (zzjb == null || TextUtils.isEmpty(zzjb.zzvj())) {
            zzawy().zzazi().zzj("No app data available; dropping event", str2);
            return;
        }
        try {
            String str3 = zzbhf.zzdb(this.mContext).getPackageInfo(str2, 0).versionName;
            if (zzjb.zzvj() != null && !zzjb.zzvj().equals(str3)) {
                zzawy().zzazf().zzj("App version does not match; dropping event. appId", zzchm.zzjk(str));
                return;
            }
        } catch (NameNotFoundException e) {
            if (!"_ui".equals(zzcha2.name)) {
                zzawy().zzazf().zzj("Could not find package. appId", zzchm.zzjk(str));
            }
        }
        zzcgi zzcgi = r3;
        zzcgi zzcgi2 = new zzcgi(str2, zzjb.getGmpAppId(), zzjb.zzvj(), zzjb.zzaxg(), zzjb.zzaxh(), zzjb.zzaxi(), zzjb.zzaxj(), (String) null, zzjb.zzaxk(), false, zzjb.zzaxd(), zzjb.zzaxx(), 0, 0, zzjb.zzaxy());
        zzb(zzcha2, zzcgi);
    }

    /* access modifiers changed from: 0000 */
    public final void zzb(zzcjl zzcjl) {
        this.zzjfz++;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzb(zzcln zzcln, zzcgi zzcgi) {
        zzawx().zzve();
        zzxf();
        if (!TextUtils.isEmpty(zzcgi.zzixs)) {
            if (!zzcgi.zzixx) {
                zzg(zzcgi);
                return;
            }
            int zzkd = zzawu().zzkd(zzcln.name);
            if (zzkd != 0) {
                zzawu();
                zzawu().zza(zzcgi.packageName, zzkd, "_ev", zzclq.zza(zzcln.name, 24, true), zzcln.name != null ? zzcln.name.length() : 0);
                return;
            }
            int zzl = zzawu().zzl(zzcln.name, zzcln.getValue());
            if (zzl != 0) {
                zzawu();
                String zza2 = zzclq.zza(zzcln.name, 24, true);
                Object value = zzcln.getValue();
                zzawu().zza(zzcgi.packageName, zzl, "_ev", zza2, (value == null || (!(value instanceof String) && !(value instanceof CharSequence))) ? 0 : String.valueOf(value).length());
                return;
            }
            Object zzm = zzawu().zzm(zzcln.name, zzcln.getValue());
            if (zzm != null) {
                zzclp zzclp = new zzclp(zzcgi.packageName, zzcln.zziyf, zzcln.name, zzcln.zzjji, zzm);
                zzawy().zzazi().zze("Setting user property", zzawt().zzjj(zzclp.mName), zzm);
                zzaws().beginTransaction();
                try {
                    zzg(zzcgi);
                    boolean zza3 = zzaws().zza(zzclp);
                    zzaws().setTransactionSuccessful();
                    if (zza3) {
                        zzawy().zzazi().zze("User property set", zzawt().zzjj(zzclp.mName), zzclp.mValue);
                    } else {
                        zzawy().zzazd().zze("Too many unique user properties are set. Ignoring user property", zzawt().zzjj(zzclp.mName), zzclp.mValue);
                        zzawu().zza(zzcgi.packageName, 9, (String) null, (String) null, 0);
                    }
                } finally {
                    zzaws().endTransaction();
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0120 A[Catch:{ all -> 0x0163, all -> 0x0013 }] */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x012e A[Catch:{ all -> 0x0163, all -> 0x0013 }] */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zzb(java.lang.String r7, int r8, java.lang.Throwable r9, byte[] r10, java.util.Map<java.lang.String, java.util.List<java.lang.String>> r11) {
        /*
            r6 = this;
            com.google.android.gms.internal.zzcih r0 = r6.zzawx()
            r0.zzve()
            r6.zzxf()
            com.google.android.gms.common.internal.zzbq.zzgm(r7)
            r0 = 0
            if (r10 != 0) goto L_0x0016
            byte[] r10 = new byte[r0]     // Catch:{ all -> 0x0013 }
            goto L_0x0016
        L_0x0013:
            r7 = move-exception
            goto L_0x016c
        L_0x0016:
            com.google.android.gms.internal.zzchm r1 = r6.zzawy()     // Catch:{ all -> 0x0013 }
            com.google.android.gms.internal.zzcho r1 = r1.zzazj()     // Catch:{ all -> 0x0013 }
            java.lang.String r2 = "onConfigFetched. Response size"
            int r3 = r10.length     // Catch:{ all -> 0x0013 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x0013 }
            r1.zzj(r2, r3)     // Catch:{ all -> 0x0013 }
            com.google.android.gms.internal.zzcgo r1 = r6.zzaws()     // Catch:{ all -> 0x0013 }
            r1.beginTransaction()     // Catch:{ all -> 0x0013 }
            com.google.android.gms.internal.zzcgo r1 = r6.zzaws()     // Catch:{ all -> 0x0163 }
            com.google.android.gms.internal.zzcgh r1 = r1.zzjb(r7)     // Catch:{ all -> 0x0163 }
            r2 = 200(0xc8, float:2.8E-43)
            r3 = 1
            r4 = 304(0x130, float:4.26E-43)
            if (r8 == r2) goto L_0x0044
            r2 = 204(0xcc, float:2.86E-43)
            if (r8 == r2) goto L_0x0044
            if (r8 != r4) goto L_0x0048
        L_0x0044:
            if (r9 != 0) goto L_0x0048
            r2 = 1
            goto L_0x0049
        L_0x0048:
            r2 = 0
        L_0x0049:
            if (r1 != 0) goto L_0x005e
            com.google.android.gms.internal.zzchm r8 = r6.zzawy()     // Catch:{ all -> 0x0163 }
            com.google.android.gms.internal.zzcho r8 = r8.zzazf()     // Catch:{ all -> 0x0163 }
            java.lang.String r9 = "App does not exist in onConfigFetched. appId"
            java.lang.Object r7 = com.google.android.gms.internal.zzchm.zzjk(r7)     // Catch:{ all -> 0x0163 }
            r8.zzj(r9, r7)     // Catch:{ all -> 0x0163 }
            goto L_0x0157
        L_0x005e:
            r5 = 404(0x194, float:5.66E-43)
            if (r2 != 0) goto L_0x00bc
            if (r8 != r5) goto L_0x0065
            goto L_0x00bc
        L_0x0065:
            com.google.android.gms.common.util.zzd r10 = r6.zzata     // Catch:{ all -> 0x0163 }
            long r10 = r10.currentTimeMillis()     // Catch:{ all -> 0x0163 }
            r1.zzas(r10)     // Catch:{ all -> 0x0163 }
            com.google.android.gms.internal.zzcgo r10 = r6.zzaws()     // Catch:{ all -> 0x0163 }
            r10.zza(r1)     // Catch:{ all -> 0x0163 }
            com.google.android.gms.internal.zzchm r10 = r6.zzawy()     // Catch:{ all -> 0x0163 }
            com.google.android.gms.internal.zzcho r10 = r10.zzazj()     // Catch:{ all -> 0x0163 }
            java.lang.String r11 = "Fetching config failed. code, error"
            java.lang.Integer r1 = java.lang.Integer.valueOf(r8)     // Catch:{ all -> 0x0163 }
            r10.zze(r11, r1, r9)     // Catch:{ all -> 0x0163 }
            com.google.android.gms.internal.zzcig r9 = r6.zzawv()     // Catch:{ all -> 0x0163 }
            r9.zzju(r7)     // Catch:{ all -> 0x0163 }
            com.google.android.gms.internal.zzchx r7 = r6.zzawz()     // Catch:{ all -> 0x0163 }
            com.google.android.gms.internal.zzcia r7 = r7.zzjcs     // Catch:{ all -> 0x0163 }
            com.google.android.gms.common.util.zzd r9 = r6.zzata     // Catch:{ all -> 0x0163 }
            long r9 = r9.currentTimeMillis()     // Catch:{ all -> 0x0163 }
            r7.set(r9)     // Catch:{ all -> 0x0163 }
            r7 = 503(0x1f7, float:7.05E-43)
            if (r8 == r7) goto L_0x00a6
            r7 = 429(0x1ad, float:6.01E-43)
            if (r8 != r7) goto L_0x00a5
            goto L_0x00a6
        L_0x00a5:
            r3 = 0
        L_0x00a6:
            if (r3 == 0) goto L_0x00b7
            com.google.android.gms.internal.zzchx r7 = r6.zzawz()     // Catch:{ all -> 0x0163 }
            com.google.android.gms.internal.zzcia r7 = r7.zzjct     // Catch:{ all -> 0x0163 }
            com.google.android.gms.common.util.zzd r8 = r6.zzata     // Catch:{ all -> 0x0163 }
            long r8 = r8.currentTimeMillis()     // Catch:{ all -> 0x0163 }
            r7.set(r8)     // Catch:{ all -> 0x0163 }
        L_0x00b7:
            r6.zzbaj()     // Catch:{ all -> 0x0163 }
            goto L_0x0157
        L_0x00bc:
            r9 = 0
            if (r11 == 0) goto L_0x00c8
            java.lang.String r2 = "Last-Modified"
            java.lang.Object r11 = r11.get(r2)     // Catch:{ all -> 0x0163 }
            java.util.List r11 = (java.util.List) r11     // Catch:{ all -> 0x0163 }
            goto L_0x00c9
        L_0x00c8:
            r11 = r9
        L_0x00c9:
            if (r11 == 0) goto L_0x00d8
            int r2 = r11.size()     // Catch:{ all -> 0x0163 }
            if (r2 <= 0) goto L_0x00d8
            java.lang.Object r11 = r11.get(r0)     // Catch:{ all -> 0x0163 }
            java.lang.String r11 = (java.lang.String) r11     // Catch:{ all -> 0x0163 }
            goto L_0x00d9
        L_0x00d8:
            r11 = r9
        L_0x00d9:
            if (r8 == r5) goto L_0x00f5
            if (r8 != r4) goto L_0x00de
            goto L_0x00f5
        L_0x00de:
            com.google.android.gms.internal.zzcig r9 = r6.zzawv()     // Catch:{ all -> 0x0163 }
            boolean r9 = r9.zzb(r7, r10, r11)     // Catch:{ all -> 0x0163 }
            if (r9 != 0) goto L_0x010e
            com.google.android.gms.internal.zzcgo r7 = r6.zzaws()     // Catch:{ all -> 0x0013 }
        L_0x00ec:
            r7.endTransaction()     // Catch:{ all -> 0x0013 }
            r6.zzjgd = r0
            r6.zzban()
            return
        L_0x00f5:
            com.google.android.gms.internal.zzcig r11 = r6.zzawv()     // Catch:{ all -> 0x0163 }
            com.google.android.gms.internal.zzcly r11 = r11.zzjs(r7)     // Catch:{ all -> 0x0163 }
            if (r11 != 0) goto L_0x010e
            com.google.android.gms.internal.zzcig r11 = r6.zzawv()     // Catch:{ all -> 0x0163 }
            boolean r9 = r11.zzb(r7, r9, r9)     // Catch:{ all -> 0x0163 }
            if (r9 != 0) goto L_0x010e
            com.google.android.gms.internal.zzcgo r7 = r6.zzaws()     // Catch:{ all -> 0x0013 }
            goto L_0x00ec
        L_0x010e:
            com.google.android.gms.common.util.zzd r9 = r6.zzata     // Catch:{ all -> 0x0163 }
            long r2 = r9.currentTimeMillis()     // Catch:{ all -> 0x0163 }
            r1.zzar(r2)     // Catch:{ all -> 0x0163 }
            com.google.android.gms.internal.zzcgo r9 = r6.zzaws()     // Catch:{ all -> 0x0163 }
            r9.zza(r1)     // Catch:{ all -> 0x0163 }
            if (r8 != r5) goto L_0x012e
            com.google.android.gms.internal.zzchm r8 = r6.zzawy()     // Catch:{ all -> 0x0163 }
            com.google.android.gms.internal.zzcho r8 = r8.zzazg()     // Catch:{ all -> 0x0163 }
            java.lang.String r9 = "Config not found. Using empty config. appId"
            r8.zzj(r9, r7)     // Catch:{ all -> 0x0163 }
            goto L_0x0144
        L_0x012e:
            com.google.android.gms.internal.zzchm r7 = r6.zzawy()     // Catch:{ all -> 0x0163 }
            com.google.android.gms.internal.zzcho r7 = r7.zzazj()     // Catch:{ all -> 0x0163 }
            java.lang.String r9 = "Successfully fetched config. Got network response. code, size"
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ all -> 0x0163 }
            int r10 = r10.length     // Catch:{ all -> 0x0163 }
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)     // Catch:{ all -> 0x0163 }
            r7.zze(r9, r8, r10)     // Catch:{ all -> 0x0163 }
        L_0x0144:
            com.google.android.gms.internal.zzchq r7 = r6.zzbab()     // Catch:{ all -> 0x0163 }
            boolean r7 = r7.zzzs()     // Catch:{ all -> 0x0163 }
            if (r7 == 0) goto L_0x00b7
            boolean r7 = r6.zzbai()     // Catch:{ all -> 0x0163 }
            if (r7 == 0) goto L_0x00b7
            r6.zzbah()     // Catch:{ all -> 0x0163 }
        L_0x0157:
            com.google.android.gms.internal.zzcgo r7 = r6.zzaws()     // Catch:{ all -> 0x0163 }
            r7.setTransactionSuccessful()     // Catch:{ all -> 0x0163 }
            com.google.android.gms.internal.zzcgo r7 = r6.zzaws()     // Catch:{ all -> 0x0013 }
            goto L_0x00ec
        L_0x0163:
            r7 = move-exception
            com.google.android.gms.internal.zzcgo r8 = r6.zzaws()     // Catch:{ all -> 0x0013 }
            r8.endTransaction()     // Catch:{ all -> 0x0013 }
            throw r7     // Catch:{ all -> 0x0013 }
        L_0x016c:
            r6.zzjgd = r0
            r6.zzban()
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcim.zzb(java.lang.String, int, java.lang.Throwable, byte[], java.util.Map):void");
    }

    public final FirebaseAnalytics zzbaa() {
        return this.zzjfd;
    }

    public final zzchq zzbab() {
        zza((zzcjl) this.zzjfi);
        return this.zzjfi;
    }

    /* access modifiers changed from: 0000 */
    public final long zzbaf() {
        Long valueOf = Long.valueOf(zzawz().zzjcw.get());
        return valueOf.longValue() == 0 ? this.zzjgg : Math.min(this.zzjgg, valueOf.longValue());
    }

    @WorkerThread
    public final void zzbah() {
        String zzayf;
        String str;
        String str2;
        zzcho zzazj;
        String str3;
        zzawx().zzve();
        zzxf();
        this.zzjgf = true;
        try {
            Boolean zzbas = zzawp().zzbas();
            if (zzbas == null) {
                zzazj = zzawy().zzazf();
                str3 = "Upload data called on the client side before use of service was decided";
            } else if (zzbas.booleanValue()) {
                zzazj = zzawy().zzazd();
                str3 = "Upload called in the client side when service should be used";
            } else {
                if (this.zzjgc <= 0) {
                    zzawx().zzve();
                    if (this.zzjfx != null) {
                        zzazj = zzawy().zzazj();
                        str3 = "Uploading requested multiple times";
                    } else if (!zzbab().zzzs()) {
                        zzawy().zzazj().log("Network not connected, ignoring upload request");
                    } else {
                        long currentTimeMillis = this.zzata.currentTimeMillis();
                        String str4 = null;
                        zzg(null, currentTimeMillis - zzcgn.zzayc());
                        long j = zzawz().zzjcr.get();
                        if (j != 0) {
                            zzawy().zzazi().zzj("Uploading events. Elapsed time since last upload attempt (ms)", Long.valueOf(Math.abs(currentTimeMillis - j)));
                        }
                        zzayf = zzaws().zzayf();
                        if (!TextUtils.isEmpty(zzayf)) {
                            if (this.zzjgb == -1) {
                                this.zzjgb = zzaws().zzaym();
                            }
                            List zzl = zzaws().zzl(zzayf, this.zzjew.zzb(zzayf, zzchc.zzjaj), Math.max(0, this.zzjew.zzb(zzayf, zzchc.zzjak)));
                            if (!zzl.isEmpty()) {
                                Iterator it = zzl.iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        str = null;
                                        break;
                                    }
                                    zzcme zzcme = (zzcme) ((Pair) it.next()).first;
                                    if (!TextUtils.isEmpty(zzcme.zzjmc)) {
                                        str = zzcme.zzjmc;
                                        break;
                                    }
                                }
                                if (str != null) {
                                    int i = 0;
                                    while (true) {
                                        if (i >= zzl.size()) {
                                            break;
                                        }
                                        zzcme zzcme2 = (zzcme) ((Pair) zzl.get(i)).first;
                                        if (!TextUtils.isEmpty(zzcme2.zzjmc) && !zzcme2.zzjmc.equals(str)) {
                                            zzl = zzl.subList(0, i);
                                            break;
                                        }
                                        i++;
                                    }
                                }
                                zzcmd zzcmd = new zzcmd();
                                zzcmd.zzjlm = new zzcme[zzl.size()];
                                ArrayList arrayList = new ArrayList(zzl.size());
                                boolean z = zzcgn.zzaye() && this.zzjew.zziz(zzayf);
                                for (int i2 = 0; i2 < zzcmd.zzjlm.length; i2++) {
                                    zzcmd.zzjlm[i2] = (zzcme) ((Pair) zzl.get(i2)).first;
                                    arrayList.add((Long) ((Pair) zzl.get(i2)).second);
                                    zzcmd.zzjlm[i2].zzjmb = Long.valueOf(11910);
                                    zzcmd.zzjlm[i2].zzjlr = Long.valueOf(currentTimeMillis);
                                    zzcmd.zzjlm[i2].zzjmh = Boolean.valueOf(false);
                                    if (!z) {
                                        zzcmd.zzjlm[i2].zzjmo = null;
                                    }
                                }
                                if (zzawy().zzae(2)) {
                                    str4 = zzawt().zza(zzcmd);
                                }
                                byte[] zzb = zzawu().zzb(zzcmd);
                                str2 = (String) zzchc.zzjat.get();
                                URL url = new URL(str2);
                                zzbq.checkArgument(!arrayList.isEmpty());
                                if (this.zzjfx != null) {
                                    zzawy().zzazd().log("Set uploading progress before finishing the previous upload");
                                } else {
                                    this.zzjfx = new ArrayList(arrayList);
                                }
                                zzawz().zzjcs.set(currentTimeMillis);
                                String str5 = "?";
                                if (zzcmd.zzjlm.length > 0) {
                                    str5 = zzcmd.zzjlm[0].zzcn;
                                }
                                zzawy().zzazj().zzd("Uploading data. app, uncompressed size, data", str5, Integer.valueOf(zzb.length), str4);
                                this.zzjge = true;
                                zzchq zzbab = zzbab();
                                zzcip zzcip = new zzcip(this);
                                zzbab.zzve();
                                zzbab.zzxf();
                                zzbq.checkNotNull(url);
                                zzbq.checkNotNull(zzb);
                                zzbq.checkNotNull(zzcip);
                                zzcih zzawx = zzbab.zzawx();
                                zzchu zzchu = new zzchu(zzbab, zzayf, url, zzb, null, zzcip);
                                zzawx.zzh(zzchu);
                            }
                        } else {
                            this.zzjgb = -1;
                            String zzba = zzaws().zzba(currentTimeMillis - zzcgn.zzayc());
                            if (!TextUtils.isEmpty(zzba)) {
                                zzcgh zzjb = zzaws().zzjb(zzba);
                                if (zzjb != null) {
                                    zzb(zzjb);
                                }
                            }
                        }
                        this.zzjgf = false;
                        zzban();
                    }
                }
                zzbaj();
                this.zzjgf = false;
                zzban();
            }
            zzazj.log(str3);
        } catch (MalformedURLException e) {
            zzawy().zzazd().zze("Failed to parse upload URL. Not uploading. appId", zzchm.zzjk(zzayf), str2);
        } catch (Throwable th) {
            Throwable th2 = th;
            this.zzjgf = false;
            zzban();
            throw th2;
        }
        this.zzjgf = false;
        zzban();
    }

    /* access modifiers changed from: 0000 */
    public final void zzbak() {
        this.zzjga++;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzbal() {
        zzcho zzazd;
        String str;
        zzawx().zzve();
        zzxf();
        if (!this.zzjfs) {
            zzawy().zzazh().log("This instance being marked as an uploader");
            zzawx().zzve();
            zzxf();
            if (zzbam() && zzbae()) {
                int zza2 = zza(this.zzjfw);
                int zzaza = zzawn().zzaza();
                zzawx().zzve();
                if (zza2 > zzaza) {
                    zzazd = zzawy().zzazd();
                    str = "Panic: can't downgrade version. Previous, current version";
                } else if (zza2 < zzaza) {
                    if (zza(zzaza, this.zzjfw)) {
                        zzazd = zzawy().zzazj();
                        str = "Storage version upgraded. Previous, current version";
                    } else {
                        zzazd = zzawy().zzazd();
                        str = "Storage version upgrade failed. Previous, current version";
                    }
                }
                zzazd.zze(str, Integer.valueOf(zza2), Integer.valueOf(zzaza));
            }
            this.zzjfs = true;
            zzbaj();
        }
    }

    public final void zzbo(boolean z) {
        zzbaj();
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzc(zzcgl zzcgl, zzcgi zzcgi) {
        zzbq.checkNotNull(zzcgl);
        zzbq.zzgm(zzcgl.packageName);
        zzbq.checkNotNull(zzcgl.zziyg);
        zzbq.zzgm(zzcgl.zziyg.name);
        zzawx().zzve();
        zzxf();
        if (!TextUtils.isEmpty(zzcgi.zzixs)) {
            if (!zzcgi.zzixx) {
                zzg(zzcgi);
                return;
            }
            zzaws().beginTransaction();
            try {
                zzg(zzcgi);
                zzcgl zzah = zzaws().zzah(zzcgl.packageName, zzcgl.zziyg.name);
                if (zzah != null) {
                    zzawy().zzazi().zze("Removing conditional user property", zzcgl.packageName, zzawt().zzjj(zzcgl.zziyg.name));
                    zzaws().zzai(zzcgl.packageName, zzcgl.zziyg.name);
                    if (zzah.zziyi) {
                        zzaws().zzaf(zzcgl.packageName, zzcgl.zziyg.name);
                    }
                    if (zzcgl.zziyo != null) {
                        Bundle bundle = null;
                        if (zzcgl.zziyo.zzizt != null) {
                            bundle = zzcgl.zziyo.zzizt.zzayx();
                        }
                        Bundle bundle2 = bundle;
                        zzc(zzawu().zza(zzcgl.zziyo.name, bundle2, zzah.zziyf, zzcgl.zziyo.zzizu, true, false), zzcgi);
                    }
                } else {
                    zzawy().zzazf().zze("Conditional user property doesn't exist", zzchm.zzjk(zzcgl.packageName), zzawt().zzjj(zzcgl.zziyg.name));
                }
                zzaws().setTransactionSuccessful();
            } finally {
                zzaws().endTransaction();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzc(zzcln zzcln, zzcgi zzcgi) {
        zzawx().zzve();
        zzxf();
        if (!TextUtils.isEmpty(zzcgi.zzixs)) {
            if (!zzcgi.zzixx) {
                zzg(zzcgi);
                return;
            }
            zzawy().zzazi().zzj("Removing user property", zzawt().zzjj(zzcln.name));
            zzaws().beginTransaction();
            try {
                zzg(zzcgi);
                zzaws().zzaf(zzcgi.packageName, zzcln.name);
                zzaws().setTransactionSuccessful();
                zzawy().zzazi().zzj("User property removed", zzawt().zzjj(zzcln.name));
            } finally {
                zzaws().endTransaction();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public final void zzd(zzcgi zzcgi) {
        zzaws().zzjb(zzcgi.packageName);
        zzcgo zzaws = zzaws();
        String str = zzcgi.packageName;
        zzbq.zzgm(str);
        zzaws.zzve();
        zzaws.zzxf();
        try {
            SQLiteDatabase writableDatabase = zzaws.getWritableDatabase();
            String[] strArr = {str};
            int delete = writableDatabase.delete("apps", "app_id=?", strArr) + 0 + writableDatabase.delete("events", "app_id=?", strArr) + writableDatabase.delete("user_attributes", "app_id=?", strArr) + writableDatabase.delete("conditional_properties", "app_id=?", strArr) + writableDatabase.delete("raw_events", "app_id=?", strArr) + writableDatabase.delete("raw_events_metadata", "app_id=?", strArr) + writableDatabase.delete("queue", "app_id=?", strArr) + writableDatabase.delete("audience_filter_values", "app_id=?", strArr);
            if (delete > 0) {
                zzaws.zzawy().zzazj().zze("Reset analytics data. app, records", str, Integer.valueOf(delete));
            }
        } catch (SQLiteException e) {
            zzaws.zzawy().zzazd().zze("Error resetting analytics data. appId, error", zzchm.zzjk(str), e);
        }
        zzf(zza(this.mContext, zzcgi.packageName, zzcgi.zzixs, zzcgi.zzixx, zzcgi.zziye));
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzd(zzcgl zzcgl) {
        zzcgi zzjw = zzjw(zzcgl.packageName);
        if (zzjw != null) {
            zzb(zzcgl, zzjw);
        }
    }

    /* access modifiers changed from: 0000 */
    public final void zze(zzcgi zzcgi) {
        zzawx().zzve();
        zzxf();
        zzbq.zzgm(zzcgi.packageName);
        zzg(zzcgi);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zze(zzcgl zzcgl) {
        zzcgi zzjw = zzjw(zzcgl.packageName);
        if (zzjw != null) {
            zzc(zzcgl, zzjw);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:53:0x01ae A[Catch:{ SQLiteException -> 0x0140, all -> 0x0368 }] */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x033f A[Catch:{ SQLiteException -> 0x0140, all -> 0x0368 }] */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zzf(com.google.android.gms.internal.zzcgi r20) {
        /*
            r19 = this;
            r1 = r19
            r2 = r20
            com.google.android.gms.internal.zzcih r3 = r19.zzawx()
            r3.zzve()
            r19.zzxf()
            com.google.android.gms.common.internal.zzbq.checkNotNull(r20)
            java.lang.String r3 = r2.packageName
            com.google.android.gms.common.internal.zzbq.zzgm(r3)
            java.lang.String r3 = r2.zzixs
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 == 0) goto L_0x001f
            return
        L_0x001f:
            com.google.android.gms.internal.zzcgo r3 = r19.zzaws()
            java.lang.String r4 = r2.packageName
            com.google.android.gms.internal.zzcgh r3 = r3.zzjb(r4)
            r4 = 0
            if (r3 == 0) goto L_0x0052
            java.lang.String r6 = r3.getGmpAppId()
            boolean r6 = android.text.TextUtils.isEmpty(r6)
            if (r6 == 0) goto L_0x0052
            java.lang.String r6 = r2.zzixs
            boolean r6 = android.text.TextUtils.isEmpty(r6)
            if (r6 != 0) goto L_0x0052
            r3.zzar(r4)
            com.google.android.gms.internal.zzcgo r6 = r19.zzaws()
            r6.zza(r3)
            com.google.android.gms.internal.zzcig r3 = r19.zzawv()
            java.lang.String r6 = r2.packageName
            r3.zzjv(r6)
        L_0x0052:
            boolean r3 = r2.zzixx
            if (r3 != 0) goto L_0x005a
            r19.zzg(r20)
            return
        L_0x005a:
            long r6 = r2.zziyc
            int r3 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
            if (r3 != 0) goto L_0x0066
            com.google.android.gms.common.util.zzd r3 = r1.zzata
            long r6 = r3.currentTimeMillis()
        L_0x0066:
            int r3 = r2.zziyd
            r14 = 0
            r15 = 1
            if (r3 == 0) goto L_0x0086
            if (r3 == r15) goto L_0x0086
            com.google.android.gms.internal.zzchm r8 = r19.zzawy()
            com.google.android.gms.internal.zzcho r8 = r8.zzazf()
            java.lang.String r9 = "Incorrect app type, assuming installed app. appId, appType"
            java.lang.String r10 = r2.packageName
            java.lang.Object r10 = com.google.android.gms.internal.zzchm.zzjk(r10)
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            r8.zze(r9, r10, r3)
            r3 = 0
        L_0x0086:
            com.google.android.gms.internal.zzcgo r8 = r19.zzaws()
            r8.beginTransaction()
            com.google.android.gms.internal.zzcgo r8 = r19.zzaws()     // Catch:{ all -> 0x0368 }
            java.lang.String r9 = r2.packageName     // Catch:{ all -> 0x0368 }
            com.google.android.gms.internal.zzcgh r8 = r8.zzjb(r9)     // Catch:{ all -> 0x0368 }
            r16 = 0
            if (r8 == 0) goto L_0x0155
            java.lang.String r9 = r8.getGmpAppId()     // Catch:{ all -> 0x0368 }
            if (r9 == 0) goto L_0x0155
            java.lang.String r9 = r8.getGmpAppId()     // Catch:{ all -> 0x0368 }
            java.lang.String r10 = r2.zzixs     // Catch:{ all -> 0x0368 }
            boolean r9 = r9.equals(r10)     // Catch:{ all -> 0x0368 }
            if (r9 != 0) goto L_0x0155
            com.google.android.gms.internal.zzchm r9 = r19.zzawy()     // Catch:{ all -> 0x0368 }
            com.google.android.gms.internal.zzcho r9 = r9.zzazf()     // Catch:{ all -> 0x0368 }
            java.lang.String r10 = "New GMP App Id passed in. Removing cached database data. appId"
            java.lang.String r11 = r8.getAppId()     // Catch:{ all -> 0x0368 }
            java.lang.Object r11 = com.google.android.gms.internal.zzchm.zzjk(r11)     // Catch:{ all -> 0x0368 }
            r9.zzj(r10, r11)     // Catch:{ all -> 0x0368 }
            com.google.android.gms.internal.zzcgo r9 = r19.zzaws()     // Catch:{ all -> 0x0368 }
            java.lang.String r8 = r8.getAppId()     // Catch:{ all -> 0x0368 }
            r9.zzxf()     // Catch:{ all -> 0x0368 }
            r9.zzve()     // Catch:{ all -> 0x0368 }
            com.google.android.gms.common.internal.zzbq.zzgm(r8)     // Catch:{ all -> 0x0368 }
            android.database.sqlite.SQLiteDatabase r10 = r9.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0140 }
            java.lang.String[] r11 = new java.lang.String[r15]     // Catch:{ SQLiteException -> 0x0140 }
            r11[r14] = r8     // Catch:{ SQLiteException -> 0x0140 }
            java.lang.String r12 = "events"
            java.lang.String r13 = "app_id=?"
            int r12 = r10.delete(r12, r13, r11)     // Catch:{ SQLiteException -> 0x0140 }
            int r12 = r12 + r14
            java.lang.String r13 = "user_attributes"
            java.lang.String r14 = "app_id=?"
            int r13 = r10.delete(r13, r14, r11)     // Catch:{ SQLiteException -> 0x0140 }
            int r12 = r12 + r13
            java.lang.String r13 = "conditional_properties"
            java.lang.String r14 = "app_id=?"
            int r13 = r10.delete(r13, r14, r11)     // Catch:{ SQLiteException -> 0x0140 }
            int r12 = r12 + r13
            java.lang.String r13 = "apps"
            java.lang.String r14 = "app_id=?"
            int r13 = r10.delete(r13, r14, r11)     // Catch:{ SQLiteException -> 0x0140 }
            int r12 = r12 + r13
            java.lang.String r13 = "raw_events"
            java.lang.String r14 = "app_id=?"
            int r13 = r10.delete(r13, r14, r11)     // Catch:{ SQLiteException -> 0x0140 }
            int r12 = r12 + r13
            java.lang.String r13 = "raw_events_metadata"
            java.lang.String r14 = "app_id=?"
            int r13 = r10.delete(r13, r14, r11)     // Catch:{ SQLiteException -> 0x0140 }
            int r12 = r12 + r13
            java.lang.String r13 = "event_filters"
            java.lang.String r14 = "app_id=?"
            int r13 = r10.delete(r13, r14, r11)     // Catch:{ SQLiteException -> 0x0140 }
            int r12 = r12 + r13
            java.lang.String r13 = "property_filters"
            java.lang.String r14 = "app_id=?"
            int r13 = r10.delete(r13, r14, r11)     // Catch:{ SQLiteException -> 0x0140 }
            int r12 = r12 + r13
            java.lang.String r13 = "audience_filter_values"
            java.lang.String r14 = "app_id=?"
            int r10 = r10.delete(r13, r14, r11)     // Catch:{ SQLiteException -> 0x0140 }
            int r12 = r12 + r10
            if (r12 <= 0) goto L_0x0153
            com.google.android.gms.internal.zzchm r10 = r9.zzawy()     // Catch:{ SQLiteException -> 0x0140 }
            com.google.android.gms.internal.zzcho r10 = r10.zzazj()     // Catch:{ SQLiteException -> 0x0140 }
            java.lang.String r11 = "Deleted application data. app, records"
            java.lang.Integer r12 = java.lang.Integer.valueOf(r12)     // Catch:{ SQLiteException -> 0x0140 }
            r10.zze(r11, r8, r12)     // Catch:{ SQLiteException -> 0x0140 }
            goto L_0x0153
        L_0x0140:
            r0 = move-exception
            r10 = r0
            com.google.android.gms.internal.zzchm r9 = r9.zzawy()     // Catch:{ all -> 0x0368 }
            com.google.android.gms.internal.zzcho r9 = r9.zzazd()     // Catch:{ all -> 0x0368 }
            java.lang.String r11 = "Error deleting application data. appId, error"
            java.lang.Object r8 = com.google.android.gms.internal.zzchm.zzjk(r8)     // Catch:{ all -> 0x0368 }
            r9.zze(r11, r8, r10)     // Catch:{ all -> 0x0368 }
        L_0x0153:
            r8 = r16
        L_0x0155:
            if (r8 == 0) goto L_0x018d
            java.lang.String r9 = r8.zzvj()     // Catch:{ all -> 0x0368 }
            if (r9 == 0) goto L_0x018d
            java.lang.String r9 = r8.zzvj()     // Catch:{ all -> 0x0368 }
            java.lang.String r10 = r2.zzifm     // Catch:{ all -> 0x0368 }
            boolean r9 = r9.equals(r10)     // Catch:{ all -> 0x0368 }
            if (r9 != 0) goto L_0x018d
            android.os.Bundle r9 = new android.os.Bundle     // Catch:{ all -> 0x0368 }
            r9.<init>()     // Catch:{ all -> 0x0368 }
            java.lang.String r10 = "_pv"
            java.lang.String r8 = r8.zzvj()     // Catch:{ all -> 0x0368 }
            r9.putString(r10, r8)     // Catch:{ all -> 0x0368 }
            com.google.android.gms.internal.zzcha r14 = new com.google.android.gms.internal.zzcha     // Catch:{ all -> 0x0368 }
            java.lang.String r10 = "_au"
            com.google.android.gms.internal.zzcgx r11 = new com.google.android.gms.internal.zzcgx     // Catch:{ all -> 0x0368 }
            r11.<init>(r9)     // Catch:{ all -> 0x0368 }
            java.lang.String r12 = "auto"
            r8 = r14
            r9 = r10
            r10 = r11
            r11 = r12
            r12 = r6
            r8.<init>(r9, r10, r11, r12)     // Catch:{ all -> 0x0368 }
            r1.zzb(r14, r2)     // Catch:{ all -> 0x0368 }
        L_0x018d:
            r19.zzg(r20)     // Catch:{ all -> 0x0368 }
            if (r3 != 0) goto L_0x019f
            com.google.android.gms.internal.zzcgo r8 = r19.zzaws()     // Catch:{ all -> 0x0368 }
            java.lang.String r9 = r2.packageName     // Catch:{ all -> 0x0368 }
            java.lang.String r10 = "_f"
        L_0x019a:
            com.google.android.gms.internal.zzcgw r8 = r8.zzae(r9, r10)     // Catch:{ all -> 0x0368 }
            goto L_0x01ac
        L_0x019f:
            if (r3 != r15) goto L_0x01aa
            com.google.android.gms.internal.zzcgo r8 = r19.zzaws()     // Catch:{ all -> 0x0368 }
            java.lang.String r9 = r2.packageName     // Catch:{ all -> 0x0368 }
            java.lang.String r10 = "_v"
            goto L_0x019a
        L_0x01aa:
            r8 = r16
        L_0x01ac:
            if (r8 != 0) goto L_0x033f
            r8 = 3600000(0x36ee80, double:1.7786363E-317)
            long r10 = r6 / r8
            r13 = 1
            long r10 = r10 + r13
            long r10 = r10 * r8
            if (r3 != 0) goto L_0x02e2
            com.google.android.gms.internal.zzcln r3 = new com.google.android.gms.internal.zzcln     // Catch:{ all -> 0x0368 }
            java.lang.String r9 = "_fot"
            java.lang.Long r12 = java.lang.Long.valueOf(r10)     // Catch:{ all -> 0x0368 }
            java.lang.String r17 = "auto"
            r8 = r3
            r10 = r6
            r4 = r13
            r13 = r17
            r8.<init>(r9, r10, r12, r13)     // Catch:{ all -> 0x0368 }
            r1.zzb(r3, r2)     // Catch:{ all -> 0x0368 }
            com.google.android.gms.internal.zzcih r3 = r19.zzawx()     // Catch:{ all -> 0x0368 }
            r3.zzve()     // Catch:{ all -> 0x0368 }
            r19.zzxf()     // Catch:{ all -> 0x0368 }
            android.os.Bundle r3 = new android.os.Bundle     // Catch:{ all -> 0x0368 }
            r3.<init>()     // Catch:{ all -> 0x0368 }
            java.lang.String r8 = "_c"
            r3.putLong(r8, r4)     // Catch:{ all -> 0x0368 }
            java.lang.String r8 = "_r"
            r3.putLong(r8, r4)     // Catch:{ all -> 0x0368 }
            java.lang.String r8 = "_uwa"
            r9 = 0
            r3.putLong(r8, r9)     // Catch:{ all -> 0x0368 }
            java.lang.String r8 = "_pfo"
            r3.putLong(r8, r9)     // Catch:{ all -> 0x0368 }
            java.lang.String r8 = "_sys"
            r3.putLong(r8, r9)     // Catch:{ all -> 0x0368 }
            java.lang.String r8 = "_sysu"
            r3.putLong(r8, r9)     // Catch:{ all -> 0x0368 }
            android.content.Context r8 = r1.mContext     // Catch:{ all -> 0x0368 }
            android.content.pm.PackageManager r8 = r8.getPackageManager()     // Catch:{ all -> 0x0368 }
            if (r8 != 0) goto L_0x021b
            com.google.android.gms.internal.zzchm r8 = r19.zzawy()     // Catch:{ all -> 0x0368 }
            com.google.android.gms.internal.zzcho r8 = r8.zzazd()     // Catch:{ all -> 0x0368 }
            java.lang.String r9 = "PackageManager is null, first open report might be inaccurate. appId"
            java.lang.String r10 = r2.packageName     // Catch:{ all -> 0x0368 }
            java.lang.Object r10 = com.google.android.gms.internal.zzchm.zzjk(r10)     // Catch:{ all -> 0x0368 }
            r8.zzj(r9, r10)     // Catch:{ all -> 0x0368 }
            goto L_0x02ae
        L_0x021b:
            android.content.Context r8 = r1.mContext     // Catch:{ NameNotFoundException -> 0x0229 }
            com.google.android.gms.internal.zzbhe r8 = com.google.android.gms.internal.zzbhf.zzdb(r8)     // Catch:{ NameNotFoundException -> 0x0229 }
            java.lang.String r9 = r2.packageName     // Catch:{ NameNotFoundException -> 0x0229 }
            r10 = 0
            android.content.pm.PackageInfo r8 = r8.getPackageInfo(r9, r10)     // Catch:{ NameNotFoundException -> 0x0229 }
            goto L_0x0240
        L_0x0229:
            r0 = move-exception
            r8 = r0
            com.google.android.gms.internal.zzchm r9 = r19.zzawy()     // Catch:{ all -> 0x0368 }
            com.google.android.gms.internal.zzcho r9 = r9.zzazd()     // Catch:{ all -> 0x0368 }
            java.lang.String r10 = "Package info is null, first open report might be inaccurate. appId"
            java.lang.String r11 = r2.packageName     // Catch:{ all -> 0x0368 }
            java.lang.Object r11 = com.google.android.gms.internal.zzchm.zzjk(r11)     // Catch:{ all -> 0x0368 }
            r9.zze(r10, r11, r8)     // Catch:{ all -> 0x0368 }
            r8 = r16
        L_0x0240:
            if (r8 == 0) goto L_0x0272
            long r9 = r8.firstInstallTime     // Catch:{ all -> 0x0368 }
            r11 = 0
            int r9 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
            if (r9 == 0) goto L_0x0272
            long r9 = r8.firstInstallTime     // Catch:{ all -> 0x0368 }
            long r11 = r8.lastUpdateTime     // Catch:{ all -> 0x0368 }
            int r8 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
            if (r8 == 0) goto L_0x0259
            java.lang.String r8 = "_uwa"
            r3.putLong(r8, r4)     // Catch:{ all -> 0x0368 }
            r8 = 0
            goto L_0x025a
        L_0x0259:
            r8 = 1
        L_0x025a:
            com.google.android.gms.internal.zzcln r14 = new com.google.android.gms.internal.zzcln     // Catch:{ all -> 0x0368 }
            java.lang.String r9 = "_fi"
            if (r8 == 0) goto L_0x0262
            r10 = r4
            goto L_0x0264
        L_0x0262:
            r10 = 0
        L_0x0264:
            java.lang.Long r12 = java.lang.Long.valueOf(r10)     // Catch:{ all -> 0x0368 }
            java.lang.String r13 = "auto"
            r8 = r14
            r10 = r6
            r8.<init>(r9, r10, r12, r13)     // Catch:{ all -> 0x0368 }
            r1.zzb(r14, r2)     // Catch:{ all -> 0x0368 }
        L_0x0272:
            android.content.Context r8 = r1.mContext     // Catch:{ NameNotFoundException -> 0x0280 }
            com.google.android.gms.internal.zzbhe r8 = com.google.android.gms.internal.zzbhf.zzdb(r8)     // Catch:{ NameNotFoundException -> 0x0280 }
            java.lang.String r9 = r2.packageName     // Catch:{ NameNotFoundException -> 0x0280 }
            r10 = 0
            android.content.pm.ApplicationInfo r8 = r8.getApplicationInfo(r9, r10)     // Catch:{ NameNotFoundException -> 0x0280 }
            goto L_0x0297
        L_0x0280:
            r0 = move-exception
            r8 = r0
            com.google.android.gms.internal.zzchm r9 = r19.zzawy()     // Catch:{ all -> 0x0368 }
            com.google.android.gms.internal.zzcho r9 = r9.zzazd()     // Catch:{ all -> 0x0368 }
            java.lang.String r10 = "Application info is null, first open report might be inaccurate. appId"
            java.lang.String r11 = r2.packageName     // Catch:{ all -> 0x0368 }
            java.lang.Object r11 = com.google.android.gms.internal.zzchm.zzjk(r11)     // Catch:{ all -> 0x0368 }
            r9.zze(r10, r11, r8)     // Catch:{ all -> 0x0368 }
            r8 = r16
        L_0x0297:
            if (r8 == 0) goto L_0x02ae
            int r9 = r8.flags     // Catch:{ all -> 0x0368 }
            r9 = r9 & r15
            if (r9 == 0) goto L_0x02a3
            java.lang.String r9 = "_sys"
            r3.putLong(r9, r4)     // Catch:{ all -> 0x0368 }
        L_0x02a3:
            int r8 = r8.flags     // Catch:{ all -> 0x0368 }
            r8 = r8 & 128(0x80, float:1.794E-43)
            if (r8 == 0) goto L_0x02ae
            java.lang.String r8 = "_sysu"
            r3.putLong(r8, r4)     // Catch:{ all -> 0x0368 }
        L_0x02ae:
            com.google.android.gms.internal.zzcgo r8 = r19.zzaws()     // Catch:{ all -> 0x0368 }
            java.lang.String r9 = r2.packageName     // Catch:{ all -> 0x0368 }
            com.google.android.gms.common.internal.zzbq.zzgm(r9)     // Catch:{ all -> 0x0368 }
            r8.zzve()     // Catch:{ all -> 0x0368 }
            r8.zzxf()     // Catch:{ all -> 0x0368 }
            java.lang.String r10 = "first_open_count"
            long r8 = r8.zzal(r9, r10)     // Catch:{ all -> 0x0368 }
            r10 = 0
            int r10 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r10 < 0) goto L_0x02ce
            java.lang.String r10 = "_pfo"
            r3.putLong(r10, r8)     // Catch:{ all -> 0x0368 }
        L_0x02ce:
            com.google.android.gms.internal.zzcha r14 = new com.google.android.gms.internal.zzcha     // Catch:{ all -> 0x0368 }
            java.lang.String r9 = "_f"
            com.google.android.gms.internal.zzcgx r10 = new com.google.android.gms.internal.zzcgx     // Catch:{ all -> 0x0368 }
            r10.<init>(r3)     // Catch:{ all -> 0x0368 }
            java.lang.String r11 = "auto"
            r8 = r14
            r12 = r6
            r8.<init>(r9, r10, r11, r12)     // Catch:{ all -> 0x0368 }
        L_0x02de:
            r1.zzb(r14, r2)     // Catch:{ all -> 0x0368 }
            goto L_0x0321
        L_0x02e2:
            r4 = r13
            if (r3 != r15) goto L_0x0321
            com.google.android.gms.internal.zzcln r3 = new com.google.android.gms.internal.zzcln     // Catch:{ all -> 0x0368 }
            java.lang.String r9 = "_fvt"
            java.lang.Long r12 = java.lang.Long.valueOf(r10)     // Catch:{ all -> 0x0368 }
            java.lang.String r13 = "auto"
            r8 = r3
            r10 = r6
            r8.<init>(r9, r10, r12, r13)     // Catch:{ all -> 0x0368 }
            r1.zzb(r3, r2)     // Catch:{ all -> 0x0368 }
            com.google.android.gms.internal.zzcih r3 = r19.zzawx()     // Catch:{ all -> 0x0368 }
            r3.zzve()     // Catch:{ all -> 0x0368 }
            r19.zzxf()     // Catch:{ all -> 0x0368 }
            android.os.Bundle r3 = new android.os.Bundle     // Catch:{ all -> 0x0368 }
            r3.<init>()     // Catch:{ all -> 0x0368 }
            java.lang.String r8 = "_c"
            r3.putLong(r8, r4)     // Catch:{ all -> 0x0368 }
            java.lang.String r8 = "_r"
            r3.putLong(r8, r4)     // Catch:{ all -> 0x0368 }
            com.google.android.gms.internal.zzcha r14 = new com.google.android.gms.internal.zzcha     // Catch:{ all -> 0x0368 }
            java.lang.String r9 = "_v"
            com.google.android.gms.internal.zzcgx r10 = new com.google.android.gms.internal.zzcgx     // Catch:{ all -> 0x0368 }
            r10.<init>(r3)     // Catch:{ all -> 0x0368 }
            java.lang.String r11 = "auto"
            r8 = r14
            r12 = r6
            r8.<init>(r9, r10, r11, r12)     // Catch:{ all -> 0x0368 }
            goto L_0x02de
        L_0x0321:
            android.os.Bundle r3 = new android.os.Bundle     // Catch:{ all -> 0x0368 }
            r3.<init>()     // Catch:{ all -> 0x0368 }
            java.lang.String r8 = "_et"
            r3.putLong(r8, r4)     // Catch:{ all -> 0x0368 }
            com.google.android.gms.internal.zzcha r4 = new com.google.android.gms.internal.zzcha     // Catch:{ all -> 0x0368 }
            java.lang.String r9 = "_e"
            com.google.android.gms.internal.zzcgx r10 = new com.google.android.gms.internal.zzcgx     // Catch:{ all -> 0x0368 }
            r10.<init>(r3)     // Catch:{ all -> 0x0368 }
            java.lang.String r11 = "auto"
            r8 = r4
            r12 = r6
            r8.<init>(r9, r10, r11, r12)     // Catch:{ all -> 0x0368 }
        L_0x033b:
            r1.zzb(r4, r2)     // Catch:{ all -> 0x0368 }
            goto L_0x0359
        L_0x033f:
            boolean r3 = r2.zzixy     // Catch:{ all -> 0x0368 }
            if (r3 == 0) goto L_0x0359
            android.os.Bundle r3 = new android.os.Bundle     // Catch:{ all -> 0x0368 }
            r3.<init>()     // Catch:{ all -> 0x0368 }
            com.google.android.gms.internal.zzcha r4 = new com.google.android.gms.internal.zzcha     // Catch:{ all -> 0x0368 }
            java.lang.String r9 = "_cd"
            com.google.android.gms.internal.zzcgx r10 = new com.google.android.gms.internal.zzcgx     // Catch:{ all -> 0x0368 }
            r10.<init>(r3)     // Catch:{ all -> 0x0368 }
            java.lang.String r11 = "auto"
            r8 = r4
            r12 = r6
            r8.<init>(r9, r10, r11, r12)     // Catch:{ all -> 0x0368 }
            goto L_0x033b
        L_0x0359:
            com.google.android.gms.internal.zzcgo r2 = r19.zzaws()     // Catch:{ all -> 0x0368 }
            r2.setTransactionSuccessful()     // Catch:{ all -> 0x0368 }
            com.google.android.gms.internal.zzcgo r2 = r19.zzaws()
            r2.endTransaction()
            return
        L_0x0368:
            r0 = move-exception
            r2 = r0
            com.google.android.gms.internal.zzcgo r3 = r19.zzaws()
            r3.endTransaction()
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcim.zzf(com.google.android.gms.internal.zzcgi):void");
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzi(Runnable runnable) {
        zzawx().zzve();
        if (this.zzjfy == null) {
            this.zzjfy = new ArrayList();
        }
        this.zzjfy.add(runnable);
    }

    public final String zzjx(String str) {
        try {
            return (String) zzawx().zzc((Callable<V>) new zzcio<V>(this, str)).get(30000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            zzawy().zzazd().zze("Failed to get app instance id. appId", zzchm.zzjk(str), e);
            return null;
        }
    }

    public final zzd zzws() {
        return this.zzata;
    }

    /* access modifiers changed from: 0000 */
    public final void zzxf() {
        if (!this.zzdtb) {
            throw new IllegalStateException("AppMeasurement is not initialized");
        }
    }
}
