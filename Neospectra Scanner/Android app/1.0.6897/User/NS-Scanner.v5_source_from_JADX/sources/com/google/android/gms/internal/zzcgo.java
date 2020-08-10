package com.google.android.gms.internal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Parcelable;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.poi.p009ss.usermodel.ShapeTypes;

final class zzcgo extends zzcjl {
    /* access modifiers changed from: private */
    public static final String[] zziyp = {"last_bundled_timestamp", "ALTER TABLE events ADD COLUMN last_bundled_timestamp INTEGER;", "last_sampled_complex_event_id", "ALTER TABLE events ADD COLUMN last_sampled_complex_event_id INTEGER;", "last_sampling_rate", "ALTER TABLE events ADD COLUMN last_sampling_rate INTEGER;", "last_exempt_from_sampling", "ALTER TABLE events ADD COLUMN last_exempt_from_sampling INTEGER;"};
    /* access modifiers changed from: private */
    public static final String[] zziyq = {Param.ORIGIN, "ALTER TABLE user_attributes ADD COLUMN origin TEXT;"};
    /* access modifiers changed from: private */
    public static final String[] zziyr = {"app_version", "ALTER TABLE apps ADD COLUMN app_version TEXT;", "app_store", "ALTER TABLE apps ADD COLUMN app_store TEXT;", "gmp_version", "ALTER TABLE apps ADD COLUMN gmp_version INTEGER;", "dev_cert_hash", "ALTER TABLE apps ADD COLUMN dev_cert_hash INTEGER;", "measurement_enabled", "ALTER TABLE apps ADD COLUMN measurement_enabled INTEGER;", "last_bundle_start_timestamp", "ALTER TABLE apps ADD COLUMN last_bundle_start_timestamp INTEGER;", "day", "ALTER TABLE apps ADD COLUMN day INTEGER;", "daily_public_events_count", "ALTER TABLE apps ADD COLUMN daily_public_events_count INTEGER;", "daily_events_count", "ALTER TABLE apps ADD COLUMN daily_events_count INTEGER;", "daily_conversions_count", "ALTER TABLE apps ADD COLUMN daily_conversions_count INTEGER;", "remote_config", "ALTER TABLE apps ADD COLUMN remote_config BLOB;", "config_fetched_time", "ALTER TABLE apps ADD COLUMN config_fetched_time INTEGER;", "failed_config_fetch_time", "ALTER TABLE apps ADD COLUMN failed_config_fetch_time INTEGER;", "app_version_int", "ALTER TABLE apps ADD COLUMN app_version_int INTEGER;", "firebase_instance_id", "ALTER TABLE apps ADD COLUMN firebase_instance_id TEXT;", "daily_error_events_count", "ALTER TABLE apps ADD COLUMN daily_error_events_count INTEGER;", "daily_realtime_events_count", "ALTER TABLE apps ADD COLUMN daily_realtime_events_count INTEGER;", "health_monitor_sample", "ALTER TABLE apps ADD COLUMN health_monitor_sample TEXT;", "android_id", "ALTER TABLE apps ADD COLUMN android_id INTEGER;", "adid_reporting_enabled", "ALTER TABLE apps ADD COLUMN adid_reporting_enabled INTEGER;"};
    /* access modifiers changed from: private */
    public static final String[] zziys = {"realtime", "ALTER TABLE raw_events ADD COLUMN realtime INTEGER;"};
    /* access modifiers changed from: private */
    public static final String[] zziyt = {"has_realtime", "ALTER TABLE queue ADD COLUMN has_realtime INTEGER;"};
    /* access modifiers changed from: private */
    public static final String[] zziyu = {"previous_install_count", "ALTER TABLE app2 ADD COLUMN previous_install_count INTEGER;"};
    private final zzcgr zziyv = new zzcgr(this, getContext(), "google_app_measurement.db");
    /* access modifiers changed from: private */
    public final zzclk zziyw = new zzclk(zzws());

    zzcgo(zzcim zzcim) {
        super(zzcim);
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0039  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final long zza(java.lang.String r3, java.lang.String[] r4, long r5) {
        /*
            r2 = this;
            android.database.sqlite.SQLiteDatabase r0 = r2.getWritableDatabase()
            r1 = 0
            android.database.Cursor r4 = r0.rawQuery(r3, r4)     // Catch:{ SQLiteException -> 0x0028 }
            boolean r0 = r4.moveToFirst()     // Catch:{ SQLiteException -> 0x0023, all -> 0x0020 }
            if (r0 == 0) goto L_0x001a
            r5 = 0
            long r5 = r4.getLong(r5)     // Catch:{ SQLiteException -> 0x0023, all -> 0x0020 }
            if (r4 == 0) goto L_0x0019
            r4.close()
        L_0x0019:
            return r5
        L_0x001a:
            if (r4 == 0) goto L_0x001f
            r4.close()
        L_0x001f:
            return r5
        L_0x0020:
            r3 = move-exception
            r1 = r4
            goto L_0x0037
        L_0x0023:
            r5 = move-exception
            r1 = r4
            goto L_0x0029
        L_0x0026:
            r3 = move-exception
            goto L_0x0037
        L_0x0028:
            r5 = move-exception
        L_0x0029:
            com.google.android.gms.internal.zzchm r4 = r2.zzawy()     // Catch:{ all -> 0x0026 }
            com.google.android.gms.internal.zzcho r4 = r4.zzazd()     // Catch:{ all -> 0x0026 }
            java.lang.String r6 = "Database error"
            r4.zze(r6, r3, r5)     // Catch:{ all -> 0x0026 }
            throw r5     // Catch:{ all -> 0x0026 }
        L_0x0037:
            if (r1 == 0) goto L_0x003c
            r1.close()
        L_0x003c:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgo.zza(java.lang.String, java.lang.String[], long):long");
    }

    @WorkerThread
    private final Object zza(Cursor cursor, int i) {
        int type = cursor.getType(i);
        switch (type) {
            case 0:
                zzawy().zzazd().log("Loaded invalid null value from database");
                return null;
            case 1:
                return Long.valueOf(cursor.getLong(i));
            case 2:
                return Double.valueOf(cursor.getDouble(i));
            case 3:
                return cursor.getString(i);
            case 4:
                zzawy().zzazd().log("Loaded invalid blob type value, ignoring it");
                return null;
            default:
                zzawy().zzazd().zzj("Loaded invalid unknown value type, ignoring it", Integer.valueOf(type));
                return null;
        }
    }

    @WorkerThread
    private static void zza(ContentValues contentValues, String str, Object obj) {
        zzbq.zzgm(str);
        zzbq.checkNotNull(obj);
        if (obj instanceof String) {
            contentValues.put(str, (String) obj);
        } else if (obj instanceof Long) {
            contentValues.put(str, (Long) obj);
        } else if (obj instanceof Double) {
            contentValues.put(str, (Double) obj);
        } else {
            throw new IllegalArgumentException("Invalid value type");
        }
    }

    static void zza(zzchm zzchm, SQLiteDatabase sQLiteDatabase) {
        if (zzchm == null) {
            throw new IllegalArgumentException("Monitor must not be null");
        }
        File file = new File(sQLiteDatabase.getPath());
        if (!file.setReadable(false, false)) {
            zzchm.zzazf().log("Failed to turn off database read permission");
        }
        if (!file.setWritable(false, false)) {
            zzchm.zzazf().log("Failed to turn off database write permission");
        }
        if (!file.setReadable(true, true)) {
            zzchm.zzazf().log("Failed to turn on database read permission for owner");
        }
        if (!file.setWritable(true, true)) {
            zzchm.zzazf().log("Failed to turn on database write permission for owner");
        }
    }

    @WorkerThread
    static void zza(zzchm zzchm, SQLiteDatabase sQLiteDatabase, String str, String str2, String str3, String[] strArr) throws SQLiteException {
        if (zzchm == null) {
            throw new IllegalArgumentException("Monitor must not be null");
        }
        if (!zza(zzchm, sQLiteDatabase, str)) {
            sQLiteDatabase.execSQL(str2);
        }
        try {
            zza(zzchm, sQLiteDatabase, str, str3, strArr);
        } catch (SQLiteException e) {
            zzchm.zzazd().zzj("Failed to verify columns on table that was just created", str);
            throw e;
        }
    }

    @WorkerThread
    private static void zza(zzchm zzchm, SQLiteDatabase sQLiteDatabase, String str, String str2, String[] strArr) throws SQLiteException {
        String[] split;
        if (zzchm == null) {
            throw new IllegalArgumentException("Monitor must not be null");
        }
        Set zzb = zzb(sQLiteDatabase, str);
        for (String str3 : str2.split(",")) {
            if (!zzb.remove(str3)) {
                StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 35 + String.valueOf(str3).length());
                sb.append("Table ");
                sb.append(str);
                sb.append(" is missing required column: ");
                sb.append(str3);
                throw new SQLiteException(sb.toString());
            }
        }
        if (strArr != null) {
            for (int i = 0; i < strArr.length; i += 2) {
                if (!zzb.remove(strArr[i])) {
                    sQLiteDatabase.execSQL(strArr[i + 1]);
                }
            }
        }
        if (!zzb.isEmpty()) {
            zzchm.zzazf().zze("Table has extra columns. table, columns", str, TextUtils.join(", ", zzb));
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0049  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean zza(com.google.android.gms.internal.zzchm r11, android.database.sqlite.SQLiteDatabase r12, java.lang.String r13) {
        /*
            if (r11 != 0) goto L_0x000a
            java.lang.IllegalArgumentException r11 = new java.lang.IllegalArgumentException
            java.lang.String r12 = "Monitor must not be null"
            r11.<init>(r12)
            throw r11
        L_0x000a:
            r0 = 0
            r1 = 0
            java.lang.String r3 = "SQLITE_MASTER"
            java.lang.String r2 = "name"
            java.lang.String[] r4 = new java.lang.String[]{r2}     // Catch:{ SQLiteException -> 0x0037 }
            java.lang.String r5 = "name=?"
            r2 = 1
            java.lang.String[] r6 = new java.lang.String[r2]     // Catch:{ SQLiteException -> 0x0037 }
            r6[r1] = r13     // Catch:{ SQLiteException -> 0x0037 }
            r7 = 0
            r8 = 0
            r9 = 0
            r2 = r12
            android.database.Cursor r12 = r2.query(r3, r4, r5, r6, r7, r8, r9)     // Catch:{ SQLiteException -> 0x0037 }
            boolean r0 = r12.moveToFirst()     // Catch:{ SQLiteException -> 0x0030, all -> 0x002d }
            if (r12 == 0) goto L_0x002c
            r12.close()
        L_0x002c:
            return r0
        L_0x002d:
            r11 = move-exception
            r0 = r12
            goto L_0x0047
        L_0x0030:
            r0 = move-exception
            r10 = r0
            r0 = r12
            r12 = r10
            goto L_0x0038
        L_0x0035:
            r11 = move-exception
            goto L_0x0047
        L_0x0037:
            r12 = move-exception
        L_0x0038:
            com.google.android.gms.internal.zzcho r11 = r11.zzazf()     // Catch:{ all -> 0x0035 }
            java.lang.String r2 = "Error querying for table"
            r11.zze(r2, r13, r12)     // Catch:{ all -> 0x0035 }
            if (r0 == 0) goto L_0x0046
            r0.close()
        L_0x0046:
            return r1
        L_0x0047:
            if (r0 == 0) goto L_0x004c
            r0.close()
        L_0x004c:
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgo.zza(com.google.android.gms.internal.zzchm, android.database.sqlite.SQLiteDatabase, java.lang.String):boolean");
    }

    @WorkerThread
    private final boolean zza(String str, int i, zzcls zzcls) {
        zzxf();
        zzve();
        zzbq.zzgm(str);
        zzbq.checkNotNull(zzcls);
        if (TextUtils.isEmpty(zzcls.zzjjx)) {
            zzawy().zzazf().zzd("Event filter had no event name. Audience definition ignored. appId, audienceId, filterId", zzchm.zzjk(str), Integer.valueOf(i), String.valueOf(zzcls.zzjjw));
            return false;
        }
        try {
            byte[] bArr = new byte[zzcls.zzho()];
            zzfjk zzo = zzfjk.zzo(bArr, 0, bArr.length);
            zzcls.zza(zzo);
            zzo.zzcwt();
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("audience_id", Integer.valueOf(i));
            contentValues.put("filter_id", zzcls.zzjjw);
            contentValues.put("event_name", zzcls.zzjjx);
            contentValues.put("data", bArr);
            try {
                if (getWritableDatabase().insertWithOnConflict("event_filters", null, contentValues, 5) == -1) {
                    zzawy().zzazd().zzj("Failed to insert event filter (got -1). appId", zzchm.zzjk(str));
                }
                return true;
            } catch (SQLiteException e) {
                zzawy().zzazd().zze("Error storing event filter. appId", zzchm.zzjk(str), e);
                return false;
            }
        } catch (IOException e2) {
            zzawy().zzazd().zze("Configuration loss. Failed to serialize event filter. appId", zzchm.zzjk(str), e2);
            return false;
        }
    }

    @WorkerThread
    private final boolean zza(String str, int i, zzclv zzclv) {
        zzxf();
        zzve();
        zzbq.zzgm(str);
        zzbq.checkNotNull(zzclv);
        if (TextUtils.isEmpty(zzclv.zzjkm)) {
            zzawy().zzazf().zzd("Property filter had no property name. Audience definition ignored. appId, audienceId, filterId", zzchm.zzjk(str), Integer.valueOf(i), String.valueOf(zzclv.zzjjw));
            return false;
        }
        try {
            byte[] bArr = new byte[zzclv.zzho()];
            zzfjk zzo = zzfjk.zzo(bArr, 0, bArr.length);
            zzclv.zza(zzo);
            zzo.zzcwt();
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("audience_id", Integer.valueOf(i));
            contentValues.put("filter_id", zzclv.zzjjw);
            contentValues.put("property_name", zzclv.zzjkm);
            contentValues.put("data", bArr);
            try {
                if (getWritableDatabase().insertWithOnConflict("property_filters", null, contentValues, 5) != -1) {
                    return true;
                }
                zzawy().zzazd().zzj("Failed to insert property filter (got -1). appId", zzchm.zzjk(str));
                return false;
            } catch (SQLiteException e) {
                zzawy().zzazd().zze("Error storing property filter. appId", zzchm.zzjk(str), e);
                return false;
            }
        } catch (IOException e2) {
            zzawy().zzazd().zze("Configuration loss. Failed to serialize property filter. appId", zzchm.zzjk(str), e2);
            return false;
        }
    }

    private final boolean zzayn() {
        return getContext().getDatabasePath("google_app_measurement.db").exists();
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x003b  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final long zzb(java.lang.String r4, java.lang.String[] r5) {
        /*
            r3 = this;
            android.database.sqlite.SQLiteDatabase r0 = r3.getWritableDatabase()
            r1 = 0
            android.database.Cursor r5 = r0.rawQuery(r4, r5)     // Catch:{ SQLiteException -> 0x002a }
            boolean r0 = r5.moveToFirst()     // Catch:{ SQLiteException -> 0x0024, all -> 0x0022 }
            if (r0 == 0) goto L_0x001a
            r0 = 0
            long r0 = r5.getLong(r0)     // Catch:{ SQLiteException -> 0x0024, all -> 0x0022 }
            if (r5 == 0) goto L_0x0019
            r5.close()
        L_0x0019:
            return r0
        L_0x001a:
            android.database.sqlite.SQLiteException r0 = new android.database.sqlite.SQLiteException     // Catch:{ SQLiteException -> 0x0024, all -> 0x0022 }
            java.lang.String r1 = "Database returned empty set"
            r0.<init>(r1)     // Catch:{ SQLiteException -> 0x0024, all -> 0x0022 }
            throw r0     // Catch:{ SQLiteException -> 0x0024, all -> 0x0022 }
        L_0x0022:
            r4 = move-exception
            goto L_0x0039
        L_0x0024:
            r0 = move-exception
            r1 = r5
            goto L_0x002b
        L_0x0027:
            r4 = move-exception
            r5 = r1
            goto L_0x0039
        L_0x002a:
            r0 = move-exception
        L_0x002b:
            com.google.android.gms.internal.zzchm r5 = r3.zzawy()     // Catch:{ all -> 0x0027 }
            com.google.android.gms.internal.zzcho r5 = r5.zzazd()     // Catch:{ all -> 0x0027 }
            java.lang.String r2 = "Database error"
            r5.zze(r2, r4, r0)     // Catch:{ all -> 0x0027 }
            throw r0     // Catch:{ all -> 0x0027 }
        L_0x0039:
            if (r5 == 0) goto L_0x003e
            r5.close()
        L_0x003e:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgo.zzb(java.lang.String, java.lang.String[]):long");
    }

    @WorkerThread
    private static Set<String> zzb(SQLiteDatabase sQLiteDatabase, String str) {
        HashSet hashSet = new HashSet();
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 22);
        sb.append("SELECT * FROM ");
        sb.append(str);
        sb.append(" LIMIT 0");
        Cursor rawQuery = sQLiteDatabase.rawQuery(sb.toString(), null);
        try {
            Collections.addAll(hashSet, rawQuery.getColumnNames());
            return hashSet;
        } finally {
            rawQuery.close();
        }
    }

    private final boolean zze(String str, List<Integer> list) {
        zzbq.zzgm(str);
        zzxf();
        zzve();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            long zzb = zzb("select count(1) from audience_filter_values where app_id=?", new String[]{str});
            int max = Math.max(0, Math.min(2000, zzaxa().zzb(str, zzchc.zzjbi)));
            if (zzb <= ((long) max)) {
                return false;
            }
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Integer num = (Integer) list.get(i);
                if (num == null || !(num instanceof Integer)) {
                    return false;
                }
                arrayList.add(Integer.toString(num.intValue()));
            }
            String join = TextUtils.join(",", arrayList);
            StringBuilder sb = new StringBuilder(String.valueOf(join).length() + 2);
            sb.append("(");
            sb.append(join);
            sb.append(")");
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder(String.valueOf(sb2).length() + ShapeTypes.FLOW_CHART_PREPARATION);
            sb3.append("audience_id in (select audience_id from audience_filter_values where app_id=? and audience_id not in ");
            sb3.append(sb2);
            sb3.append(" order by rowid desc limit -1 offset ?)");
            return writableDatabase.delete("audience_filter_values", sb3.toString(), new String[]{str, Integer.toString(max)}) > 0;
        } catch (SQLiteException e) {
            zzawy().zzazd().zze("Database error querying filters. appId", zzchm.zzjk(str), e);
            return false;
        }
    }

    @WorkerThread
    public final void beginTransaction() {
        zzxf();
        getWritableDatabase().beginTransaction();
    }

    @WorkerThread
    public final void endTransaction() {
        zzxf();
        getWritableDatabase().endTransaction();
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final SQLiteDatabase getWritableDatabase() {
        zzve();
        try {
            return this.zziyv.getWritableDatabase();
        } catch (SQLiteException e) {
            zzawy().zzazf().zzj("Error opening database", e);
            throw e;
        }
    }

    @WorkerThread
    public final void setTransactionSuccessful() {
        zzxf();
        getWritableDatabase().setTransactionSuccessful();
    }

    public final long zza(zzcme zzcme) throws IOException {
        long j;
        zzve();
        zzxf();
        zzbq.checkNotNull(zzcme);
        zzbq.zzgm(zzcme.zzcn);
        try {
            byte[] bArr = new byte[zzcme.zzho()];
            zzfjk zzo = zzfjk.zzo(bArr, 0, bArr.length);
            zzcme.zza(zzo);
            zzo.zzcwt();
            zzclq zzawu = zzawu();
            zzbq.checkNotNull(bArr);
            zzawu.zzve();
            MessageDigest zzek = zzclq.zzek("MD5");
            if (zzek == null) {
                zzawu.zzawy().zzazd().log("Failed to get MD5");
                j = 0;
            } else {
                j = zzclq.zzs(zzek.digest(bArr));
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", zzcme.zzcn);
            contentValues.put("metadata_fingerprint", Long.valueOf(j));
            contentValues.put("metadata", bArr);
            try {
                getWritableDatabase().insertWithOnConflict("raw_events_metadata", null, contentValues, 4);
                return j;
            } catch (SQLiteException e) {
                zzawy().zzazd().zze("Error storing raw event metadata. appId", zzchm.zzjk(zzcme.zzcn), e);
                throw e;
            }
        } catch (IOException e2) {
            zzawy().zzazd().zze("Data loss. Failed to serialize event metadata. appId", zzchm.zzjk(zzcme.zzcn), e2);
            throw e2;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x011c  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0122  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.internal.zzcgp zza(long r17, java.lang.String r19, boolean r20, boolean r21, boolean r22, boolean r23, boolean r24) {
        /*
            r16 = this;
            com.google.android.gms.common.internal.zzbq.zzgm(r19)
            r16.zzve()
            r16.zzxf()
            r2 = 1
            java.lang.String[] r3 = new java.lang.String[r2]
            r4 = 0
            r3[r4] = r19
            com.google.android.gms.internal.zzcgp r5 = new com.google.android.gms.internal.zzcgp
            r5.<init>()
            r6 = 0
            android.database.sqlite.SQLiteDatabase r15 = r16.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0107 }
            java.lang.String r8 = "apps"
            java.lang.String r9 = "day"
            java.lang.String r10 = "daily_events_count"
            java.lang.String r11 = "daily_public_events_count"
            java.lang.String r12 = "daily_conversions_count"
            java.lang.String r13 = "daily_error_events_count"
            java.lang.String r14 = "daily_realtime_events_count"
            java.lang.String[] r9 = new java.lang.String[]{r9, r10, r11, r12, r13, r14}     // Catch:{ SQLiteException -> 0x0107 }
            java.lang.String r10 = "app_id=?"
            java.lang.String[] r11 = new java.lang.String[r2]     // Catch:{ SQLiteException -> 0x0107 }
            r11[r4] = r19     // Catch:{ SQLiteException -> 0x0107 }
            r12 = 0
            r13 = 0
            r14 = 0
            r7 = r15
            android.database.Cursor r7 = r7.query(r8, r9, r10, r11, r12, r13, r14)     // Catch:{ SQLiteException -> 0x0107 }
            boolean r6 = r7.moveToFirst()     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            if (r6 != 0) goto L_0x0056
            com.google.android.gms.internal.zzchm r2 = r16.zzawy()     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            com.google.android.gms.internal.zzcho r2 = r2.zzazf()     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            java.lang.String r3 = "Not updating daily counts, app is not known. appId"
            java.lang.Object r4 = com.google.android.gms.internal.zzchm.zzjk(r19)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r2.zzj(r3, r4)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            if (r7 == 0) goto L_0x0055
            r7.close()
        L_0x0055:
            return r5
        L_0x0056:
            long r8 = r7.getLong(r4)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            int r4 = (r8 > r17 ? 1 : (r8 == r17 ? 0 : -1))
            if (r4 != 0) goto L_0x0080
            long r8 = r7.getLong(r2)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r5.zziyy = r8     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r2 = 2
            long r8 = r7.getLong(r2)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r5.zziyx = r8     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r2 = 3
            long r8 = r7.getLong(r2)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r5.zziyz = r8     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r2 = 4
            long r8 = r7.getLong(r2)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r5.zziza = r8     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r2 = 5
            long r8 = r7.getLong(r2)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r5.zzizb = r8     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
        L_0x0080:
            r8 = 1
            if (r20 == 0) goto L_0x008a
            long r12 = r5.zziyy     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r2 = 0
            long r12 = r12 + r8
            r5.zziyy = r12     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
        L_0x008a:
            if (r21 == 0) goto L_0x0092
            long r12 = r5.zziyx     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r2 = 0
            long r12 = r12 + r8
            r5.zziyx = r12     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
        L_0x0092:
            if (r22 == 0) goto L_0x009a
            long r12 = r5.zziyz     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r2 = 0
            long r12 = r12 + r8
            r5.zziyz = r12     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
        L_0x009a:
            if (r23 == 0) goto L_0x00a2
            long r12 = r5.zziza     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r2 = 0
            long r12 = r12 + r8
            r5.zziza = r12     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
        L_0x00a2:
            if (r24 == 0) goto L_0x00aa
            long r12 = r5.zzizb     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r2 = 0
            long r12 = r12 + r8
            r5.zzizb = r12     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
        L_0x00aa:
            android.content.ContentValues r2 = new android.content.ContentValues     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r2.<init>()     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            java.lang.String r4 = "day"
            java.lang.Long r6 = java.lang.Long.valueOf(r17)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r2.put(r4, r6)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            java.lang.String r4 = "daily_public_events_count"
            long r8 = r5.zziyx     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            java.lang.Long r6 = java.lang.Long.valueOf(r8)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r2.put(r4, r6)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            java.lang.String r4 = "daily_events_count"
            long r8 = r5.zziyy     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            java.lang.Long r6 = java.lang.Long.valueOf(r8)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r2.put(r4, r6)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            java.lang.String r4 = "daily_conversions_count"
            long r8 = r5.zziyz     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            java.lang.Long r6 = java.lang.Long.valueOf(r8)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r2.put(r4, r6)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            java.lang.String r4 = "daily_error_events_count"
            long r8 = r5.zziza     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            java.lang.Long r6 = java.lang.Long.valueOf(r8)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r2.put(r4, r6)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            java.lang.String r4 = "daily_realtime_events_count"
            long r8 = r5.zzizb     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            java.lang.Long r6 = java.lang.Long.valueOf(r8)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            r2.put(r4, r6)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            java.lang.String r4 = "apps"
            java.lang.String r6 = "app_id=?"
            r15.update(r4, r2, r6, r3)     // Catch:{ SQLiteException -> 0x00ff, all -> 0x00fc }
            if (r7 == 0) goto L_0x00fb
            r7.close()
        L_0x00fb:
            return r5
        L_0x00fc:
            r0 = move-exception
            r1 = r0
            goto L_0x0120
        L_0x00ff:
            r0 = move-exception
            r2 = r0
            r6 = r7
            goto L_0x0109
        L_0x0103:
            r0 = move-exception
            r1 = r0
            r7 = r6
            goto L_0x0120
        L_0x0107:
            r0 = move-exception
            r2 = r0
        L_0x0109:
            com.google.android.gms.internal.zzchm r3 = r16.zzawy()     // Catch:{ all -> 0x0103 }
            com.google.android.gms.internal.zzcho r3 = r3.zzazd()     // Catch:{ all -> 0x0103 }
            java.lang.String r4 = "Error updating daily counts. appId"
            java.lang.Object r1 = com.google.android.gms.internal.zzchm.zzjk(r19)     // Catch:{ all -> 0x0103 }
            r3.zze(r4, r1, r2)     // Catch:{ all -> 0x0103 }
            if (r6 == 0) goto L_0x011f
            r6.close()
        L_0x011f:
            return r5
        L_0x0120:
            if (r7 == 0) goto L_0x0125
            r7.close()
        L_0x0125:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgo.zza(long, java.lang.String, boolean, boolean, boolean, boolean, boolean):com.google.android.gms.internal.zzcgp");
    }

    @WorkerThread
    public final void zza(zzcgh zzcgh) {
        zzbq.checkNotNull(zzcgh);
        zzve();
        zzxf();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzcgh.getAppId());
        contentValues.put("app_instance_id", zzcgh.getAppInstanceId());
        contentValues.put("gmp_app_id", zzcgh.getGmpAppId());
        contentValues.put("resettable_device_id_hash", zzcgh.zzaxc());
        contentValues.put("last_bundle_index", Long.valueOf(zzcgh.zzaxl()));
        contentValues.put("last_bundle_start_timestamp", Long.valueOf(zzcgh.zzaxe()));
        contentValues.put("last_bundle_end_timestamp", Long.valueOf(zzcgh.zzaxf()));
        contentValues.put("app_version", zzcgh.zzvj());
        contentValues.put("app_store", zzcgh.zzaxh());
        contentValues.put("gmp_version", Long.valueOf(zzcgh.zzaxi()));
        contentValues.put("dev_cert_hash", Long.valueOf(zzcgh.zzaxj()));
        contentValues.put("measurement_enabled", Boolean.valueOf(zzcgh.zzaxk()));
        contentValues.put("day", Long.valueOf(zzcgh.zzaxp()));
        contentValues.put("daily_public_events_count", Long.valueOf(zzcgh.zzaxq()));
        contentValues.put("daily_events_count", Long.valueOf(zzcgh.zzaxr()));
        contentValues.put("daily_conversions_count", Long.valueOf(zzcgh.zzaxs()));
        contentValues.put("config_fetched_time", Long.valueOf(zzcgh.zzaxm()));
        contentValues.put("failed_config_fetch_time", Long.valueOf(zzcgh.zzaxn()));
        contentValues.put("app_version_int", Long.valueOf(zzcgh.zzaxg()));
        contentValues.put("firebase_instance_id", zzcgh.zzaxd());
        contentValues.put("daily_error_events_count", Long.valueOf(zzcgh.zzaxu()));
        contentValues.put("daily_realtime_events_count", Long.valueOf(zzcgh.zzaxt()));
        contentValues.put("health_monitor_sample", zzcgh.zzaxv());
        contentValues.put("android_id", Long.valueOf(zzcgh.zzaxx()));
        contentValues.put("adid_reporting_enabled", Boolean.valueOf(zzcgh.zzaxy()));
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            if (((long) writableDatabase.update("apps", contentValues, "app_id = ?", new String[]{zzcgh.getAppId()})) == 0 && writableDatabase.insertWithOnConflict("apps", null, contentValues, 5) == -1) {
                zzawy().zzazd().zzj("Failed to insert/update app (got -1). appId", zzchm.zzjk(zzcgh.getAppId()));
            }
        } catch (SQLiteException e) {
            zzawy().zzazd().zze("Error storing app. appId", zzchm.zzjk(zzcgh.getAppId()), e);
        }
    }

    @WorkerThread
    public final void zza(zzcgw zzcgw) {
        zzbq.checkNotNull(zzcgw);
        zzve();
        zzxf();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzcgw.mAppId);
        contentValues.put("name", zzcgw.mName);
        contentValues.put("lifetime_count", Long.valueOf(zzcgw.zzizk));
        contentValues.put("current_bundle_count", Long.valueOf(zzcgw.zzizl));
        contentValues.put("last_fire_timestamp", Long.valueOf(zzcgw.zzizm));
        contentValues.put("last_bundled_timestamp", Long.valueOf(zzcgw.zzizn));
        contentValues.put("last_sampled_complex_event_id", zzcgw.zzizo);
        contentValues.put("last_sampling_rate", zzcgw.zzizp);
        contentValues.put("last_exempt_from_sampling", (zzcgw.zzizq == null || !zzcgw.zzizq.booleanValue()) ? null : Long.valueOf(1));
        try {
            if (getWritableDatabase().insertWithOnConflict("events", null, contentValues, 5) == -1) {
                zzawy().zzazd().zzj("Failed to insert/update event aggregates (got -1). appId", zzchm.zzjk(zzcgw.mAppId));
            }
        } catch (SQLiteException e) {
            zzawy().zzazd().zze("Error storing event aggregates. appId", zzchm.zzjk(zzcgw.mAppId), e);
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zza(String str, zzclr[] zzclrArr) {
        boolean z;
        zzcho zzazf;
        String str2;
        Object zzjk;
        Integer num;
        zzxf();
        zzve();
        zzbq.zzgm(str);
        zzbq.checkNotNull(zzclrArr);
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            zzxf();
            zzve();
            zzbq.zzgm(str);
            SQLiteDatabase writableDatabase2 = getWritableDatabase();
            writableDatabase2.delete("property_filters", "app_id=?", new String[]{str});
            writableDatabase2.delete("event_filters", "app_id=?", new String[]{str});
            for (zzclr zzclr : zzclrArr) {
                zzxf();
                zzve();
                zzbq.zzgm(str);
                zzbq.checkNotNull(zzclr);
                zzbq.checkNotNull(zzclr.zzjju);
                zzbq.checkNotNull(zzclr.zzjjt);
                if (zzclr.zzjjs != null) {
                    int intValue = zzclr.zzjjs.intValue();
                    zzcls[] zzclsArr = zzclr.zzjju;
                    int length = zzclsArr.length;
                    int i = 0;
                    while (true) {
                        if (i >= length) {
                            zzclv[] zzclvArr = zzclr.zzjjt;
                            int length2 = zzclvArr.length;
                            int i2 = 0;
                            while (i2 < length2) {
                                if (zzclvArr[i2].zzjjw == null) {
                                    zzazf = zzawy().zzazf();
                                    str2 = "Property filter with no ID. Audience definition ignored. appId, audienceId";
                                    zzjk = zzchm.zzjk(str);
                                    num = zzclr.zzjjs;
                                } else {
                                    i2++;
                                }
                            }
                            zzcls[] zzclsArr2 = zzclr.zzjju;
                            int length3 = zzclsArr2.length;
                            int i3 = 0;
                            while (true) {
                                if (i3 >= length3) {
                                    z = true;
                                    break;
                                } else if (!zza(str, intValue, zzclsArr2[i3])) {
                                    z = false;
                                    break;
                                } else {
                                    i3++;
                                }
                            }
                            if (z) {
                                zzclv[] zzclvArr2 = zzclr.zzjjt;
                                int length4 = zzclvArr2.length;
                                int i4 = 0;
                                while (true) {
                                    if (i4 >= length4) {
                                        break;
                                    } else if (!zza(str, intValue, zzclvArr2[i4])) {
                                        z = false;
                                        break;
                                    } else {
                                        i4++;
                                    }
                                }
                            }
                            if (!z) {
                                zzxf();
                                zzve();
                                zzbq.zzgm(str);
                                SQLiteDatabase writableDatabase3 = getWritableDatabase();
                                writableDatabase3.delete("property_filters", "app_id=? and audience_id=?", new String[]{str, String.valueOf(intValue)});
                                writableDatabase3.delete("event_filters", "app_id=? and audience_id=?", new String[]{str, String.valueOf(intValue)});
                            }
                        } else if (zzclsArr[i].zzjjw == null) {
                            zzazf = zzawy().zzazf();
                            str2 = "Event filter with no ID. Audience definition ignored. appId, audienceId";
                            zzjk = zzchm.zzjk(str);
                            num = zzclr.zzjjs;
                            break;
                        } else {
                            i++;
                        }
                    }
                    zzazf.zze(str2, zzjk, num);
                    break;
                } else {
                    zzawy().zzazf().zzj("Audience with no ID. appId", zzchm.zzjk(str));
                }
            }
            ArrayList arrayList = new ArrayList();
            for (zzclr zzclr2 : zzclrArr) {
                arrayList.add(zzclr2.zzjjs);
            }
            zze(str, arrayList);
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    @WorkerThread
    public final boolean zza(zzcgl zzcgl) {
        zzbq.checkNotNull(zzcgl);
        zzve();
        zzxf();
        if (zzag(zzcgl.packageName, zzcgl.zziyg.name) == null) {
            if (zzb("SELECT COUNT(1) FROM conditional_properties WHERE app_id=?", new String[]{zzcgl.packageName}) >= 1000) {
                return false;
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzcgl.packageName);
        contentValues.put(Param.ORIGIN, zzcgl.zziyf);
        contentValues.put("name", zzcgl.zziyg.name);
        zza(contentValues, Param.VALUE, zzcgl.zziyg.getValue());
        contentValues.put("active", Boolean.valueOf(zzcgl.zziyi));
        contentValues.put("trigger_event_name", zzcgl.zziyj);
        contentValues.put("trigger_timeout", Long.valueOf(zzcgl.zziyl));
        zzawu();
        contentValues.put("timed_out_event", zzclq.zza((Parcelable) zzcgl.zziyk));
        contentValues.put("creation_timestamp", Long.valueOf(zzcgl.zziyh));
        zzawu();
        contentValues.put("triggered_event", zzclq.zza((Parcelable) zzcgl.zziym));
        contentValues.put("triggered_timestamp", Long.valueOf(zzcgl.zziyg.zzjji));
        contentValues.put("time_to_live", Long.valueOf(zzcgl.zziyn));
        zzawu();
        contentValues.put("expired_event", zzclq.zza((Parcelable) zzcgl.zziyo));
        try {
            if (getWritableDatabase().insertWithOnConflict("conditional_properties", null, contentValues, 5) == -1) {
                zzawy().zzazd().zzj("Failed to insert/update conditional user property (got -1)", zzchm.zzjk(zzcgl.packageName));
                return true;
            }
        } catch (SQLiteException e) {
            zzawy().zzazd().zze("Error storing conditional user property", zzchm.zzjk(zzcgl.packageName), e);
        }
        return true;
    }

    public final boolean zza(zzcgv zzcgv, long j, boolean z) {
        zzcho zzazd;
        String str;
        zzve();
        zzxf();
        zzbq.checkNotNull(zzcgv);
        zzbq.zzgm(zzcgv.mAppId);
        zzcmb zzcmb = new zzcmb();
        zzcmb.zzjlj = Long.valueOf(zzcgv.zzizi);
        zzcmb.zzjlh = new zzcmc[zzcgv.zzizj.size()];
        Iterator it = zzcgv.zzizj.iterator();
        int i = 0;
        while (it.hasNext()) {
            String str2 = (String) it.next();
            zzcmc zzcmc = new zzcmc();
            int i2 = i + 1;
            zzcmb.zzjlh[i] = zzcmc;
            zzcmc.name = str2;
            zzawu().zza(zzcmc, zzcgv.zzizj.get(str2));
            i = i2;
        }
        try {
            byte[] bArr = new byte[zzcmb.zzho()];
            zzfjk zzo = zzfjk.zzo(bArr, 0, bArr.length);
            zzcmb.zza(zzo);
            zzo.zzcwt();
            zzawy().zzazj().zze("Saving event, name, data size", zzawt().zzjh(zzcgv.mName), Integer.valueOf(bArr.length));
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", zzcgv.mAppId);
            contentValues.put("name", zzcgv.mName);
            contentValues.put(AppMeasurement.Param.TIMESTAMP, Long.valueOf(zzcgv.zzfij));
            contentValues.put("metadata_fingerprint", Long.valueOf(j));
            contentValues.put("data", bArr);
            contentValues.put("realtime", Integer.valueOf(z ? 1 : 0));
            try {
                if (getWritableDatabase().insert("raw_events", null, contentValues) != -1) {
                    return true;
                }
                zzawy().zzazd().zzj("Failed to insert raw event (got -1). appId", zzchm.zzjk(zzcgv.mAppId));
                return false;
            } catch (SQLiteException e) {
                e = e;
                zzazd = zzawy().zzazd();
                str = "Error storing raw event. appId";
                zzazd.zze(str, zzchm.zzjk(zzcgv.mAppId), e);
                return false;
            }
        } catch (IOException e2) {
            e = e2;
            zzazd = zzawy().zzazd();
            str = "Data loss. Failed to serialize event params/data. appId";
            zzazd.zze(str, zzchm.zzjk(zzcgv.mAppId), e);
            return false;
        }
    }

    @WorkerThread
    public final boolean zza(zzclp zzclp) {
        zzbq.checkNotNull(zzclp);
        zzve();
        zzxf();
        if (zzag(zzclp.mAppId, zzclp.mName) == null) {
            if (zzclq.zzjz(zzclp.mName)) {
                if (zzb("select count(1) from user_attributes where app_id=? and name not like '!_%' escape '!'", new String[]{zzclp.mAppId}) >= 25) {
                    return false;
                }
            } else {
                if (zzb("select count(1) from user_attributes where app_id=? and origin=? AND name like '!_%' escape '!'", new String[]{zzclp.mAppId, zzclp.mOrigin}) >= 25) {
                    return false;
                }
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzclp.mAppId);
        contentValues.put(Param.ORIGIN, zzclp.mOrigin);
        contentValues.put("name", zzclp.mName);
        contentValues.put("set_timestamp", Long.valueOf(zzclp.zzjjm));
        zza(contentValues, Param.VALUE, zzclp.mValue);
        try {
            if (getWritableDatabase().insertWithOnConflict("user_attributes", null, contentValues, 5) == -1) {
                zzawy().zzazd().zzj("Failed to insert/update user property (got -1). appId", zzchm.zzjk(zzclp.mAppId));
                return true;
            }
        } catch (SQLiteException e) {
            zzawy().zzazd().zze("Error storing user property. appId", zzchm.zzjk(zzclp.mAppId), e);
        }
        return true;
    }

    @WorkerThread
    public final boolean zza(zzcme zzcme, boolean z) {
        zzcho zzazd;
        String str;
        zzve();
        zzxf();
        zzbq.checkNotNull(zzcme);
        zzbq.zzgm(zzcme.zzcn);
        zzbq.checkNotNull(zzcme.zzjlt);
        zzayh();
        long currentTimeMillis = zzws().currentTimeMillis();
        if (zzcme.zzjlt.longValue() < currentTimeMillis - zzcgn.zzayb() || zzcme.zzjlt.longValue() > zzcgn.zzayb() + currentTimeMillis) {
            zzawy().zzazf().zzd("Storing bundle outside of the max uploading time span. appId, now, timestamp", zzchm.zzjk(zzcme.zzcn), Long.valueOf(currentTimeMillis), zzcme.zzjlt);
        }
        try {
            byte[] bArr = new byte[zzcme.zzho()];
            zzfjk zzo = zzfjk.zzo(bArr, 0, bArr.length);
            zzcme.zza(zzo);
            zzo.zzcwt();
            byte[] zzq = zzawu().zzq(bArr);
            zzawy().zzazj().zzj("Saving bundle, size", Integer.valueOf(zzq.length));
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", zzcme.zzcn);
            contentValues.put("bundle_end_timestamp", zzcme.zzjlt);
            contentValues.put("data", zzq);
            contentValues.put("has_realtime", Integer.valueOf(z ? 1 : 0));
            try {
                if (getWritableDatabase().insert("queue", null, contentValues) != -1) {
                    return true;
                }
                zzawy().zzazd().zzj("Failed to insert bundle (got -1). appId", zzchm.zzjk(zzcme.zzcn));
                return false;
            } catch (SQLiteException e) {
                e = e;
                zzazd = zzawy().zzazd();
                str = "Error storing bundle. appId";
                zzazd.zze(str, zzchm.zzjk(zzcme.zzcn), e);
                return false;
            }
        } catch (IOException e2) {
            e = e2;
            zzazd = zzawy().zzazd();
            str = "Data loss. Failed to serialize bundle. appId";
            zzazd.zze(str, zzchm.zzjk(zzcme.zzcn), e);
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:60:0x010d  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0115  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.internal.zzcgw zzae(java.lang.String r23, java.lang.String r24) {
        /*
            r22 = this;
            r15 = r24
            com.google.android.gms.common.internal.zzbq.zzgm(r23)
            com.google.android.gms.common.internal.zzbq.zzgm(r24)
            r22.zzve()
            r22.zzxf()
            r16 = 0
            android.database.sqlite.SQLiteDatabase r1 = r22.getWritableDatabase()     // Catch:{ SQLiteException -> 0x00ec, all -> 0x00e7 }
            java.lang.String r2 = "events"
            java.lang.String r3 = "lifetime_count"
            java.lang.String r4 = "current_bundle_count"
            java.lang.String r5 = "last_fire_timestamp"
            java.lang.String r6 = "last_bundled_timestamp"
            java.lang.String r7 = "last_sampled_complex_event_id"
            java.lang.String r8 = "last_sampling_rate"
            java.lang.String r9 = "last_exempt_from_sampling"
            java.lang.String[] r3 = new java.lang.String[]{r3, r4, r5, r6, r7, r8, r9}     // Catch:{ SQLiteException -> 0x00ec, all -> 0x00e7 }
            java.lang.String r4 = "app_id=? and name=?"
            r9 = 2
            java.lang.String[] r5 = new java.lang.String[r9]     // Catch:{ SQLiteException -> 0x00ec, all -> 0x00e7 }
            r10 = 0
            r5[r10] = r23     // Catch:{ SQLiteException -> 0x00ec, all -> 0x00e7 }
            r11 = 1
            r5[r11] = r15     // Catch:{ SQLiteException -> 0x00ec, all -> 0x00e7 }
            r6 = 0
            r7 = 0
            r8 = 0
            android.database.Cursor r14 = r1.query(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ SQLiteException -> 0x00ec, all -> 0x00e7 }
            boolean r1 = r14.moveToFirst()     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00e0 }
            if (r1 != 0) goto L_0x0046
            if (r14 == 0) goto L_0x0045
            r14.close()
        L_0x0045:
            return r16
        L_0x0046:
            long r4 = r14.getLong(r10)     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00e0 }
            long r6 = r14.getLong(r11)     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00e0 }
            long r8 = r14.getLong(r9)     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00e0 }
            r1 = 3
            boolean r2 = r14.isNull(r1)     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00e0 }
            if (r2 == 0) goto L_0x005d
            r1 = 0
        L_0x005b:
            r12 = r1
            goto L_0x0062
        L_0x005d:
            long r1 = r14.getLong(r1)     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00e0 }
            goto L_0x005b
        L_0x0062:
            r1 = 4
            boolean r2 = r14.isNull(r1)     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00e0 }
            if (r2 == 0) goto L_0x006c
            r17 = r16
            goto L_0x0076
        L_0x006c:
            long r1 = r14.getLong(r1)     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00e0 }
            java.lang.Long r1 = java.lang.Long.valueOf(r1)     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00e0 }
            r17 = r1
        L_0x0076:
            r1 = 5
            boolean r2 = r14.isNull(r1)     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00e0 }
            if (r2 == 0) goto L_0x0080
            r18 = r16
            goto L_0x008a
        L_0x0080:
            long r1 = r14.getLong(r1)     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00e0 }
            java.lang.Long r1 = java.lang.Long.valueOf(r1)     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00e0 }
            r18 = r1
        L_0x008a:
            r1 = 6
            boolean r2 = r14.isNull(r1)     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00e0 }
            if (r2 != 0) goto L_0x00ad
            long r1 = r14.getLong(r1)     // Catch:{ SQLiteException -> 0x00a9, all -> 0x00a4 }
            r19 = 1
            int r1 = (r1 > r19 ? 1 : (r1 == r19 ? 0 : -1))
            if (r1 != 0) goto L_0x009c
            goto L_0x009d
        L_0x009c:
            r11 = 0
        L_0x009d:
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r11)     // Catch:{ SQLiteException -> 0x00a9, all -> 0x00a4 }
            r19 = r1
            goto L_0x00af
        L_0x00a4:
            r0 = move-exception
            r1 = r0
            r15 = r14
            goto L_0x0113
        L_0x00a9:
            r0 = move-exception
            r1 = r0
            r15 = r14
            goto L_0x00f0
        L_0x00ad:
            r19 = r16
        L_0x00af:
            com.google.android.gms.internal.zzcgw r20 = new com.google.android.gms.internal.zzcgw     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00e0 }
            r1 = r20
            r2 = r23
            r3 = r15
            r10 = r12
            r12 = r17
            r13 = r18
            r15 = r14
            r14 = r19
            r1.<init>(r2, r3, r4, r6, r8, r10, r12, r13, r14)     // Catch:{ SQLiteException -> 0x00de }
            boolean r1 = r15.moveToNext()     // Catch:{ SQLiteException -> 0x00de }
            if (r1 == 0) goto L_0x00d8
            com.google.android.gms.internal.zzchm r1 = r22.zzawy()     // Catch:{ SQLiteException -> 0x00de }
            com.google.android.gms.internal.zzcho r1 = r1.zzazd()     // Catch:{ SQLiteException -> 0x00de }
            java.lang.String r2 = "Got multiple records for event aggregates, expected one. appId"
            java.lang.Object r3 = com.google.android.gms.internal.zzchm.zzjk(r23)     // Catch:{ SQLiteException -> 0x00de }
            r1.zzj(r2, r3)     // Catch:{ SQLiteException -> 0x00de }
        L_0x00d8:
            if (r15 == 0) goto L_0x00dd
            r15.close()
        L_0x00dd:
            return r20
        L_0x00de:
            r0 = move-exception
            goto L_0x00e5
        L_0x00e0:
            r0 = move-exception
            r15 = r14
            goto L_0x0112
        L_0x00e3:
            r0 = move-exception
            r15 = r14
        L_0x00e5:
            r1 = r0
            goto L_0x00f0
        L_0x00e7:
            r0 = move-exception
            r1 = r0
            r15 = r16
            goto L_0x0113
        L_0x00ec:
            r0 = move-exception
            r1 = r0
            r15 = r16
        L_0x00f0:
            com.google.android.gms.internal.zzchm r2 = r22.zzawy()     // Catch:{ all -> 0x0111 }
            com.google.android.gms.internal.zzcho r2 = r2.zzazd()     // Catch:{ all -> 0x0111 }
            java.lang.String r3 = "Error querying events. appId"
            java.lang.Object r4 = com.google.android.gms.internal.zzchm.zzjk(r23)     // Catch:{ all -> 0x0111 }
            com.google.android.gms.internal.zzchk r5 = r22.zzawt()     // Catch:{ all -> 0x0111 }
            r6 = r24
            java.lang.String r5 = r5.zzjh(r6)     // Catch:{ all -> 0x0111 }
            r2.zzd(r3, r4, r5, r1)     // Catch:{ all -> 0x0111 }
            if (r15 == 0) goto L_0x0110
            r15.close()
        L_0x0110:
            return r16
        L_0x0111:
            r0 = move-exception
        L_0x0112:
            r1 = r0
        L_0x0113:
            if (r15 == 0) goto L_0x0118
            r15.close()
        L_0x0118:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgo.zzae(java.lang.String, java.lang.String):com.google.android.gms.internal.zzcgw");
    }

    @WorkerThread
    public final void zzaf(String str, String str2) {
        zzbq.zzgm(str);
        zzbq.zzgm(str2);
        zzve();
        zzxf();
        try {
            zzawy().zzazj().zzj("Deleted user attribute rows", Integer.valueOf(getWritableDatabase().delete("user_attributes", "app_id=? and name=?", new String[]{str, str2})));
        } catch (SQLiteException e) {
            zzawy().zzazd().zzd("Error deleting user attribute. appId", zzchm.zzjk(str), zzawt().zzjj(str2), e);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x00a4  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00ac  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.internal.zzclp zzag(java.lang.String r20, java.lang.String r21) {
        /*
            r19 = this;
            r8 = r21
            com.google.android.gms.common.internal.zzbq.zzgm(r20)
            com.google.android.gms.common.internal.zzbq.zzgm(r21)
            r19.zzve()
            r19.zzxf()
            r9 = 0
            android.database.sqlite.SQLiteDatabase r10 = r19.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0084, all -> 0x007e }
            java.lang.String r11 = "user_attributes"
            java.lang.String r1 = "set_timestamp"
            java.lang.String r2 = "value"
            java.lang.String r3 = "origin"
            java.lang.String[] r12 = new java.lang.String[]{r1, r2, r3}     // Catch:{ SQLiteException -> 0x0084, all -> 0x007e }
            java.lang.String r13 = "app_id=? and name=?"
            r1 = 2
            java.lang.String[] r14 = new java.lang.String[r1]     // Catch:{ SQLiteException -> 0x0084, all -> 0x007e }
            r2 = 0
            r14[r2] = r20     // Catch:{ SQLiteException -> 0x0084, all -> 0x007e }
            r3 = 1
            r14[r3] = r8     // Catch:{ SQLiteException -> 0x0084, all -> 0x007e }
            r15 = 0
            r16 = 0
            r17 = 0
            android.database.Cursor r10 = r10.query(r11, r12, r13, r14, r15, r16, r17)     // Catch:{ SQLiteException -> 0x0084, all -> 0x007e }
            boolean r4 = r10.moveToFirst()     // Catch:{ SQLiteException -> 0x0079, all -> 0x0075 }
            if (r4 != 0) goto L_0x003f
            if (r10 == 0) goto L_0x003e
            r10.close()
        L_0x003e:
            return r9
        L_0x003f:
            long r5 = r10.getLong(r2)     // Catch:{ SQLiteException -> 0x0079, all -> 0x0075 }
            r11 = r19
            java.lang.Object r7 = r11.zza(r10, r3)     // Catch:{ SQLiteException -> 0x0073 }
            java.lang.String r3 = r10.getString(r1)     // Catch:{ SQLiteException -> 0x0073 }
            com.google.android.gms.internal.zzclp r12 = new com.google.android.gms.internal.zzclp     // Catch:{ SQLiteException -> 0x0073 }
            r1 = r12
            r2 = r20
            r4 = r8
            r1.<init>(r2, r3, r4, r5, r7)     // Catch:{ SQLiteException -> 0x0073 }
            boolean r1 = r10.moveToNext()     // Catch:{ SQLiteException -> 0x0073 }
            if (r1 == 0) goto L_0x006d
            com.google.android.gms.internal.zzchm r1 = r19.zzawy()     // Catch:{ SQLiteException -> 0x0073 }
            com.google.android.gms.internal.zzcho r1 = r1.zzazd()     // Catch:{ SQLiteException -> 0x0073 }
            java.lang.String r2 = "Got multiple records for user property, expected one. appId"
            java.lang.Object r3 = com.google.android.gms.internal.zzchm.zzjk(r20)     // Catch:{ SQLiteException -> 0x0073 }
            r1.zzj(r2, r3)     // Catch:{ SQLiteException -> 0x0073 }
        L_0x006d:
            if (r10 == 0) goto L_0x0072
            r10.close()
        L_0x0072:
            return r12
        L_0x0073:
            r0 = move-exception
            goto L_0x007c
        L_0x0075:
            r0 = move-exception
            r11 = r19
            goto L_0x00a9
        L_0x0079:
            r0 = move-exception
            r11 = r19
        L_0x007c:
            r1 = r0
            goto L_0x0089
        L_0x007e:
            r0 = move-exception
            r11 = r19
            r1 = r0
            r10 = r9
            goto L_0x00aa
        L_0x0084:
            r0 = move-exception
            r11 = r19
            r1 = r0
            r10 = r9
        L_0x0089:
            com.google.android.gms.internal.zzchm r2 = r19.zzawy()     // Catch:{ all -> 0x00a8 }
            com.google.android.gms.internal.zzcho r2 = r2.zzazd()     // Catch:{ all -> 0x00a8 }
            java.lang.String r3 = "Error querying user property. appId"
            java.lang.Object r4 = com.google.android.gms.internal.zzchm.zzjk(r20)     // Catch:{ all -> 0x00a8 }
            com.google.android.gms.internal.zzchk r5 = r19.zzawt()     // Catch:{ all -> 0x00a8 }
            java.lang.String r5 = r5.zzjj(r8)     // Catch:{ all -> 0x00a8 }
            r2.zzd(r3, r4, r5, r1)     // Catch:{ all -> 0x00a8 }
            if (r10 == 0) goto L_0x00a7
            r10.close()
        L_0x00a7:
            return r9
        L_0x00a8:
            r0 = move-exception
        L_0x00a9:
            r1 = r0
        L_0x00aa:
            if (r10 == 0) goto L_0x00af
            r10.close()
        L_0x00af:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgo.zzag(java.lang.String, java.lang.String):com.google.android.gms.internal.zzclp");
    }

    /* JADX WARNING: Removed duplicated region for block: B:36:0x0120  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0128  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.internal.zzcgl zzah(java.lang.String r31, java.lang.String r32) {
        /*
            r30 = this;
            r7 = r32
            com.google.android.gms.common.internal.zzbq.zzgm(r31)
            com.google.android.gms.common.internal.zzbq.zzgm(r32)
            r30.zzve()
            r30.zzxf()
            r8 = 0
            android.database.sqlite.SQLiteDatabase r9 = r30.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0100, all -> 0x00fa }
            java.lang.String r10 = "conditional_properties"
            java.lang.String r11 = "origin"
            java.lang.String r12 = "value"
            java.lang.String r13 = "active"
            java.lang.String r14 = "trigger_event_name"
            java.lang.String r15 = "trigger_timeout"
            java.lang.String r16 = "timed_out_event"
            java.lang.String r17 = "creation_timestamp"
            java.lang.String r18 = "triggered_event"
            java.lang.String r19 = "triggered_timestamp"
            java.lang.String r20 = "time_to_live"
            java.lang.String r21 = "expired_event"
            java.lang.String[] r11 = new java.lang.String[]{r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21}     // Catch:{ SQLiteException -> 0x0100, all -> 0x00fa }
            java.lang.String r12 = "app_id=? and name=?"
            r1 = 2
            java.lang.String[] r13 = new java.lang.String[r1]     // Catch:{ SQLiteException -> 0x0100, all -> 0x00fa }
            r2 = 0
            r13[r2] = r31     // Catch:{ SQLiteException -> 0x0100, all -> 0x00fa }
            r3 = 1
            r13[r3] = r7     // Catch:{ SQLiteException -> 0x0100, all -> 0x00fa }
            r14 = 0
            r15 = 0
            r16 = 0
            android.database.Cursor r9 = r9.query(r10, r11, r12, r13, r14, r15, r16)     // Catch:{ SQLiteException -> 0x0100, all -> 0x00fa }
            boolean r4 = r9.moveToFirst()     // Catch:{ SQLiteException -> 0x00f5, all -> 0x00f1 }
            if (r4 != 0) goto L_0x004e
            if (r9 == 0) goto L_0x004d
            r9.close()
        L_0x004d:
            return r8
        L_0x004e:
            java.lang.String r16 = r9.getString(r2)     // Catch:{ SQLiteException -> 0x00f5, all -> 0x00f1 }
            r10 = r30
            java.lang.Object r5 = r10.zza(r9, r3)     // Catch:{ SQLiteException -> 0x00ef }
            int r1 = r9.getInt(r1)     // Catch:{ SQLiteException -> 0x00ef }
            if (r1 == 0) goto L_0x0061
            r20 = 1
            goto L_0x0063
        L_0x0061:
            r20 = 0
        L_0x0063:
            r1 = 3
            java.lang.String r21 = r9.getString(r1)     // Catch:{ SQLiteException -> 0x00ef }
            r1 = 4
            long r23 = r9.getLong(r1)     // Catch:{ SQLiteException -> 0x00ef }
            com.google.android.gms.internal.zzclq r1 = r30.zzawu()     // Catch:{ SQLiteException -> 0x00ef }
            r2 = 5
            byte[] r2 = r9.getBlob(r2)     // Catch:{ SQLiteException -> 0x00ef }
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcha> r3 = com.google.android.gms.internal.zzcha.CREATOR     // Catch:{ SQLiteException -> 0x00ef }
            android.os.Parcelable r1 = r1.zzb(r2, r3)     // Catch:{ SQLiteException -> 0x00ef }
            r22 = r1
            com.google.android.gms.internal.zzcha r22 = (com.google.android.gms.internal.zzcha) r22     // Catch:{ SQLiteException -> 0x00ef }
            r1 = 6
            long r18 = r9.getLong(r1)     // Catch:{ SQLiteException -> 0x00ef }
            com.google.android.gms.internal.zzclq r1 = r30.zzawu()     // Catch:{ SQLiteException -> 0x00ef }
            r2 = 7
            byte[] r2 = r9.getBlob(r2)     // Catch:{ SQLiteException -> 0x00ef }
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcha> r3 = com.google.android.gms.internal.zzcha.CREATOR     // Catch:{ SQLiteException -> 0x00ef }
            android.os.Parcelable r1 = r1.zzb(r2, r3)     // Catch:{ SQLiteException -> 0x00ef }
            r25 = r1
            com.google.android.gms.internal.zzcha r25 = (com.google.android.gms.internal.zzcha) r25     // Catch:{ SQLiteException -> 0x00ef }
            r1 = 8
            long r3 = r9.getLong(r1)     // Catch:{ SQLiteException -> 0x00ef }
            r1 = 9
            long r26 = r9.getLong(r1)     // Catch:{ SQLiteException -> 0x00ef }
            com.google.android.gms.internal.zzclq r1 = r30.zzawu()     // Catch:{ SQLiteException -> 0x00ef }
            r2 = 10
            byte[] r2 = r9.getBlob(r2)     // Catch:{ SQLiteException -> 0x00ef }
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcha> r6 = com.google.android.gms.internal.zzcha.CREATOR     // Catch:{ SQLiteException -> 0x00ef }
            android.os.Parcelable r1 = r1.zzb(r2, r6)     // Catch:{ SQLiteException -> 0x00ef }
            r28 = r1
            com.google.android.gms.internal.zzcha r28 = (com.google.android.gms.internal.zzcha) r28     // Catch:{ SQLiteException -> 0x00ef }
            com.google.android.gms.internal.zzcln r17 = new com.google.android.gms.internal.zzcln     // Catch:{ SQLiteException -> 0x00ef }
            r1 = r17
            r2 = r7
            r6 = r16
            r1.<init>(r2, r3, r5, r6)     // Catch:{ SQLiteException -> 0x00ef }
            com.google.android.gms.internal.zzcgl r1 = new com.google.android.gms.internal.zzcgl     // Catch:{ SQLiteException -> 0x00ef }
            r14 = r1
            r15 = r31
            r14.<init>(r15, r16, r17, r18, r20, r21, r22, r23, r25, r26, r28)     // Catch:{ SQLiteException -> 0x00ef }
            boolean r2 = r9.moveToNext()     // Catch:{ SQLiteException -> 0x00ef }
            if (r2 == 0) goto L_0x00e9
            com.google.android.gms.internal.zzchm r2 = r30.zzawy()     // Catch:{ SQLiteException -> 0x00ef }
            com.google.android.gms.internal.zzcho r2 = r2.zzazd()     // Catch:{ SQLiteException -> 0x00ef }
            java.lang.String r3 = "Got multiple records for conditional property, expected one"
            java.lang.Object r4 = com.google.android.gms.internal.zzchm.zzjk(r31)     // Catch:{ SQLiteException -> 0x00ef }
            com.google.android.gms.internal.zzchk r5 = r30.zzawt()     // Catch:{ SQLiteException -> 0x00ef }
            java.lang.String r5 = r5.zzjj(r7)     // Catch:{ SQLiteException -> 0x00ef }
            r2.zze(r3, r4, r5)     // Catch:{ SQLiteException -> 0x00ef }
        L_0x00e9:
            if (r9 == 0) goto L_0x00ee
            r9.close()
        L_0x00ee:
            return r1
        L_0x00ef:
            r0 = move-exception
            goto L_0x00f8
        L_0x00f1:
            r0 = move-exception
            r10 = r30
            goto L_0x0125
        L_0x00f5:
            r0 = move-exception
            r10 = r30
        L_0x00f8:
            r1 = r0
            goto L_0x0105
        L_0x00fa:
            r0 = move-exception
            r10 = r30
            r1 = r0
            r9 = r8
            goto L_0x0126
        L_0x0100:
            r0 = move-exception
            r10 = r30
            r1 = r0
            r9 = r8
        L_0x0105:
            com.google.android.gms.internal.zzchm r2 = r30.zzawy()     // Catch:{ all -> 0x0124 }
            com.google.android.gms.internal.zzcho r2 = r2.zzazd()     // Catch:{ all -> 0x0124 }
            java.lang.String r3 = "Error querying conditional property"
            java.lang.Object r4 = com.google.android.gms.internal.zzchm.zzjk(r31)     // Catch:{ all -> 0x0124 }
            com.google.android.gms.internal.zzchk r5 = r30.zzawt()     // Catch:{ all -> 0x0124 }
            java.lang.String r5 = r5.zzjj(r7)     // Catch:{ all -> 0x0124 }
            r2.zzd(r3, r4, r5, r1)     // Catch:{ all -> 0x0124 }
            if (r9 == 0) goto L_0x0123
            r9.close()
        L_0x0123:
            return r8
        L_0x0124:
            r0 = move-exception
        L_0x0125:
            r1 = r0
        L_0x0126:
            if (r9 == 0) goto L_0x012b
            r9.close()
        L_0x012b:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgo.zzah(java.lang.String, java.lang.String):com.google.android.gms.internal.zzcgl");
    }

    public final void zzah(List<Long> list) {
        zzbq.checkNotNull(list);
        zzve();
        zzxf();
        StringBuilder sb = new StringBuilder("rowid in (");
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(((Long) list.get(i)).longValue());
        }
        sb.append(")");
        int delete = getWritableDatabase().delete("raw_events", sb.toString(), null);
        if (delete != list.size()) {
            zzawy().zzazd().zze("Deleted fewer rows from raw events table than expected", Integer.valueOf(delete), Integer.valueOf(list.size()));
        }
    }

    @WorkerThread
    public final int zzai(String str, String str2) {
        zzbq.zzgm(str);
        zzbq.zzgm(str2);
        zzve();
        zzxf();
        try {
            return getWritableDatabase().delete("conditional_properties", "app_id=? and name=?", new String[]{str, str2});
        } catch (SQLiteException e) {
            zzawy().zzazd().zzd("Error deleting conditional property", zzchm.zzjk(str), zzawt().zzjj(str2), e);
            return 0;
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00ab  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00b2  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.Map<java.lang.Integer, java.util.List<com.google.android.gms.internal.zzcls>> zzaj(java.lang.String r13, java.lang.String r14) {
        /*
            r12 = this;
            r12.zzxf()
            r12.zzve()
            com.google.android.gms.common.internal.zzbq.zzgm(r13)
            com.google.android.gms.common.internal.zzbq.zzgm(r14)
            android.support.v4.util.ArrayMap r0 = new android.support.v4.util.ArrayMap
            r0.<init>()
            android.database.sqlite.SQLiteDatabase r1 = r12.getWritableDatabase()
            r9 = 0
            java.lang.String r2 = "event_filters"
            java.lang.String r3 = "audience_id"
            java.lang.String r4 = "data"
            java.lang.String[] r3 = new java.lang.String[]{r3, r4}     // Catch:{ SQLiteException -> 0x0096, all -> 0x0093 }
            java.lang.String r4 = "app_id=? AND event_name=?"
            r5 = 2
            java.lang.String[] r5 = new java.lang.String[r5]     // Catch:{ SQLiteException -> 0x0096, all -> 0x0093 }
            r10 = 0
            r5[r10] = r13     // Catch:{ SQLiteException -> 0x0096, all -> 0x0093 }
            r11 = 1
            r5[r11] = r14     // Catch:{ SQLiteException -> 0x0096, all -> 0x0093 }
            r6 = 0
            r7 = 0
            r8 = 0
            android.database.Cursor r14 = r1.query(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ SQLiteException -> 0x0096, all -> 0x0093 }
            boolean r1 = r14.moveToFirst()     // Catch:{ SQLiteException -> 0x0091 }
            if (r1 != 0) goto L_0x0042
            java.util.Map r0 = java.util.Collections.emptyMap()     // Catch:{ SQLiteException -> 0x0091 }
            if (r14 == 0) goto L_0x0041
            r14.close()
        L_0x0041:
            return r0
        L_0x0042:
            byte[] r1 = r14.getBlob(r11)     // Catch:{ SQLiteException -> 0x0091 }
            int r2 = r1.length     // Catch:{ SQLiteException -> 0x0091 }
            com.google.android.gms.internal.zzfjj r1 = com.google.android.gms.internal.zzfjj.zzn(r1, r10, r2)     // Catch:{ SQLiteException -> 0x0091 }
            com.google.android.gms.internal.zzcls r2 = new com.google.android.gms.internal.zzcls     // Catch:{ SQLiteException -> 0x0091 }
            r2.<init>()     // Catch:{ SQLiteException -> 0x0091 }
            r2.zza(r1)     // Catch:{ IOException -> 0x0073 }
            int r1 = r14.getInt(r10)     // Catch:{ SQLiteException -> 0x0091 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r1)     // Catch:{ SQLiteException -> 0x0091 }
            java.lang.Object r3 = r0.get(r3)     // Catch:{ SQLiteException -> 0x0091 }
            java.util.List r3 = (java.util.List) r3     // Catch:{ SQLiteException -> 0x0091 }
            if (r3 != 0) goto L_0x006f
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ SQLiteException -> 0x0091 }
            r3.<init>()     // Catch:{ SQLiteException -> 0x0091 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ SQLiteException -> 0x0091 }
            r0.put(r1, r3)     // Catch:{ SQLiteException -> 0x0091 }
        L_0x006f:
            r3.add(r2)     // Catch:{ SQLiteException -> 0x0091 }
            goto L_0x0085
        L_0x0073:
            r1 = move-exception
            com.google.android.gms.internal.zzchm r2 = r12.zzawy()     // Catch:{ SQLiteException -> 0x0091 }
            com.google.android.gms.internal.zzcho r2 = r2.zzazd()     // Catch:{ SQLiteException -> 0x0091 }
            java.lang.String r3 = "Failed to merge filter. appId"
            java.lang.Object r4 = com.google.android.gms.internal.zzchm.zzjk(r13)     // Catch:{ SQLiteException -> 0x0091 }
            r2.zze(r3, r4, r1)     // Catch:{ SQLiteException -> 0x0091 }
        L_0x0085:
            boolean r1 = r14.moveToNext()     // Catch:{ SQLiteException -> 0x0091 }
            if (r1 != 0) goto L_0x0042
            if (r14 == 0) goto L_0x0090
            r14.close()
        L_0x0090:
            return r0
        L_0x0091:
            r0 = move-exception
            goto L_0x0098
        L_0x0093:
            r13 = move-exception
            r14 = r9
            goto L_0x00b0
        L_0x0096:
            r0 = move-exception
            r14 = r9
        L_0x0098:
            com.google.android.gms.internal.zzchm r1 = r12.zzawy()     // Catch:{ all -> 0x00af }
            com.google.android.gms.internal.zzcho r1 = r1.zzazd()     // Catch:{ all -> 0x00af }
            java.lang.String r2 = "Database error querying filters. appId"
            java.lang.Object r13 = com.google.android.gms.internal.zzchm.zzjk(r13)     // Catch:{ all -> 0x00af }
            r1.zze(r2, r13, r0)     // Catch:{ all -> 0x00af }
            if (r14 == 0) goto L_0x00ae
            r14.close()
        L_0x00ae:
            return r9
        L_0x00af:
            r13 = move-exception
        L_0x00b0:
            if (r14 == 0) goto L_0x00b5
            r14.close()
        L_0x00b5:
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgo.zzaj(java.lang.String, java.lang.String):java.util.Map");
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00ab  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00b2  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.Map<java.lang.Integer, java.util.List<com.google.android.gms.internal.zzclv>> zzak(java.lang.String r13, java.lang.String r14) {
        /*
            r12 = this;
            r12.zzxf()
            r12.zzve()
            com.google.android.gms.common.internal.zzbq.zzgm(r13)
            com.google.android.gms.common.internal.zzbq.zzgm(r14)
            android.support.v4.util.ArrayMap r0 = new android.support.v4.util.ArrayMap
            r0.<init>()
            android.database.sqlite.SQLiteDatabase r1 = r12.getWritableDatabase()
            r9 = 0
            java.lang.String r2 = "property_filters"
            java.lang.String r3 = "audience_id"
            java.lang.String r4 = "data"
            java.lang.String[] r3 = new java.lang.String[]{r3, r4}     // Catch:{ SQLiteException -> 0x0096, all -> 0x0093 }
            java.lang.String r4 = "app_id=? AND property_name=?"
            r5 = 2
            java.lang.String[] r5 = new java.lang.String[r5]     // Catch:{ SQLiteException -> 0x0096, all -> 0x0093 }
            r10 = 0
            r5[r10] = r13     // Catch:{ SQLiteException -> 0x0096, all -> 0x0093 }
            r11 = 1
            r5[r11] = r14     // Catch:{ SQLiteException -> 0x0096, all -> 0x0093 }
            r6 = 0
            r7 = 0
            r8 = 0
            android.database.Cursor r14 = r1.query(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ SQLiteException -> 0x0096, all -> 0x0093 }
            boolean r1 = r14.moveToFirst()     // Catch:{ SQLiteException -> 0x0091 }
            if (r1 != 0) goto L_0x0042
            java.util.Map r0 = java.util.Collections.emptyMap()     // Catch:{ SQLiteException -> 0x0091 }
            if (r14 == 0) goto L_0x0041
            r14.close()
        L_0x0041:
            return r0
        L_0x0042:
            byte[] r1 = r14.getBlob(r11)     // Catch:{ SQLiteException -> 0x0091 }
            int r2 = r1.length     // Catch:{ SQLiteException -> 0x0091 }
            com.google.android.gms.internal.zzfjj r1 = com.google.android.gms.internal.zzfjj.zzn(r1, r10, r2)     // Catch:{ SQLiteException -> 0x0091 }
            com.google.android.gms.internal.zzclv r2 = new com.google.android.gms.internal.zzclv     // Catch:{ SQLiteException -> 0x0091 }
            r2.<init>()     // Catch:{ SQLiteException -> 0x0091 }
            r2.zza(r1)     // Catch:{ IOException -> 0x0073 }
            int r1 = r14.getInt(r10)     // Catch:{ SQLiteException -> 0x0091 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r1)     // Catch:{ SQLiteException -> 0x0091 }
            java.lang.Object r3 = r0.get(r3)     // Catch:{ SQLiteException -> 0x0091 }
            java.util.List r3 = (java.util.List) r3     // Catch:{ SQLiteException -> 0x0091 }
            if (r3 != 0) goto L_0x006f
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ SQLiteException -> 0x0091 }
            r3.<init>()     // Catch:{ SQLiteException -> 0x0091 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ SQLiteException -> 0x0091 }
            r0.put(r1, r3)     // Catch:{ SQLiteException -> 0x0091 }
        L_0x006f:
            r3.add(r2)     // Catch:{ SQLiteException -> 0x0091 }
            goto L_0x0085
        L_0x0073:
            r1 = move-exception
            com.google.android.gms.internal.zzchm r2 = r12.zzawy()     // Catch:{ SQLiteException -> 0x0091 }
            com.google.android.gms.internal.zzcho r2 = r2.zzazd()     // Catch:{ SQLiteException -> 0x0091 }
            java.lang.String r3 = "Failed to merge filter"
            java.lang.Object r4 = com.google.android.gms.internal.zzchm.zzjk(r13)     // Catch:{ SQLiteException -> 0x0091 }
            r2.zze(r3, r4, r1)     // Catch:{ SQLiteException -> 0x0091 }
        L_0x0085:
            boolean r1 = r14.moveToNext()     // Catch:{ SQLiteException -> 0x0091 }
            if (r1 != 0) goto L_0x0042
            if (r14 == 0) goto L_0x0090
            r14.close()
        L_0x0090:
            return r0
        L_0x0091:
            r0 = move-exception
            goto L_0x0098
        L_0x0093:
            r13 = move-exception
            r14 = r9
            goto L_0x00b0
        L_0x0096:
            r0 = move-exception
            r14 = r9
        L_0x0098:
            com.google.android.gms.internal.zzchm r1 = r12.zzawy()     // Catch:{ all -> 0x00af }
            com.google.android.gms.internal.zzcho r1 = r1.zzazd()     // Catch:{ all -> 0x00af }
            java.lang.String r2 = "Database error querying filters. appId"
            java.lang.Object r13 = com.google.android.gms.internal.zzchm.zzjk(r13)     // Catch:{ all -> 0x00af }
            r1.zze(r2, r13, r0)     // Catch:{ all -> 0x00af }
            if (r14 == 0) goto L_0x00ae
            r14.close()
        L_0x00ae:
            return r9
        L_0x00af:
            r13 = move-exception
        L_0x00b0:
            if (r14 == 0) goto L_0x00b5
            r14.close()
        L_0x00b5:
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgo.zzak(java.lang.String, java.lang.String):java.util.Map");
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final long zzal(String str, String str2) {
        long j;
        zzbq.zzgm(str);
        zzbq.zzgm(str2);
        zzve();
        zzxf();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            StringBuilder sb = new StringBuilder(String.valueOf(str2).length() + 32);
            sb.append("select ");
            sb.append(str2);
            sb.append(" from app2 where app_id=?");
            j = zza(sb.toString(), new String[]{str}, -1);
            if (j == -1) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("app_id", str);
                contentValues.put("first_open_count", Integer.valueOf(0));
                contentValues.put("previous_install_count", Integer.valueOf(0));
                if (writableDatabase.insertWithOnConflict("app2", null, contentValues, 5) == -1) {
                    zzawy().zzazd().zze("Failed to insert column (got -1). appId", zzchm.zzjk(str), str2);
                    writableDatabase.endTransaction();
                    return -1;
                }
                j = 0;
            }
            try {
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("app_id", str);
                contentValues2.put(str2, Long.valueOf(1 + j));
                if (((long) writableDatabase.update("app2", contentValues2, "app_id = ?", new String[]{str})) == 0) {
                    zzawy().zzazd().zze("Failed to update column (got 0). appId", zzchm.zzjk(str), str2);
                    writableDatabase.endTransaction();
                    return -1;
                }
                writableDatabase.setTransactionSuccessful();
                writableDatabase.endTransaction();
                return j;
            } catch (SQLiteException e) {
                e = e;
            }
        } catch (SQLiteException e2) {
            e = e2;
            j = 0;
            try {
                zzawy().zzazd().zzd("Error inserting column. appId", zzchm.zzjk(str), str2, e);
                writableDatabase.endTransaction();
                return j;
            } catch (Throwable th) {
                writableDatabase.endTransaction();
                throw th;
            }
        }
    }

    /* access modifiers changed from: protected */
    public final boolean zzaxz() {
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x003a  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0041  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.String zzayf() {
        /*
            r6 = this;
            android.database.sqlite.SQLiteDatabase r0 = r6.getWritableDatabase()
            r1 = 0
            java.lang.String r2 = "select app_id from queue order by has_realtime desc, rowid asc limit 1;"
            android.database.Cursor r0 = r0.rawQuery(r2, r1)     // Catch:{ SQLiteException -> 0x0029, all -> 0x0024 }
            boolean r2 = r0.moveToFirst()     // Catch:{ SQLiteException -> 0x0022 }
            if (r2 == 0) goto L_0x001c
            r2 = 0
            java.lang.String r2 = r0.getString(r2)     // Catch:{ SQLiteException -> 0x0022 }
            if (r0 == 0) goto L_0x001b
            r0.close()
        L_0x001b:
            return r2
        L_0x001c:
            if (r0 == 0) goto L_0x0021
            r0.close()
        L_0x0021:
            return r1
        L_0x0022:
            r2 = move-exception
            goto L_0x002b
        L_0x0024:
            r0 = move-exception
            r5 = r1
            r1 = r0
            r0 = r5
            goto L_0x003f
        L_0x0029:
            r2 = move-exception
            r0 = r1
        L_0x002b:
            com.google.android.gms.internal.zzchm r3 = r6.zzawy()     // Catch:{ all -> 0x003e }
            com.google.android.gms.internal.zzcho r3 = r3.zzazd()     // Catch:{ all -> 0x003e }
            java.lang.String r4 = "Database error getting next bundle app id"
            r3.zzj(r4, r2)     // Catch:{ all -> 0x003e }
            if (r0 == 0) goto L_0x003d
            r0.close()
        L_0x003d:
            return r1
        L_0x003e:
            r1 = move-exception
        L_0x003f:
            if (r0 == 0) goto L_0x0044
            r0.close()
        L_0x0044:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgo.zzayf():java.lang.String");
    }

    public final boolean zzayg() {
        return zzb("select count(1) > 0 from queue where has_realtime = 1", (String[]) null) != 0;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzayh() {
        zzve();
        zzxf();
        if (zzayn()) {
            long j = zzawz().zzjcu.get();
            long elapsedRealtime = zzws().elapsedRealtime();
            if (Math.abs(elapsedRealtime - j) > ((Long) zzchc.zzjbb.get()).longValue()) {
                zzawz().zzjcu.set(elapsedRealtime);
                zzve();
                zzxf();
                if (zzayn()) {
                    int delete = getWritableDatabase().delete("queue", "abs(bundle_end_timestamp - ?) > cast(? as integer)", new String[]{String.valueOf(zzws().currentTimeMillis()), String.valueOf(zzcgn.zzayb())});
                    if (delete > 0) {
                        zzawy().zzazj().zzj("Deleted stale rows. rowsDeleted", Integer.valueOf(delete));
                    }
                }
            }
        }
    }

    @WorkerThread
    public final long zzayi() {
        return zza("select max(bundle_end_timestamp) from queue", (String[]) null, 0);
    }

    @WorkerThread
    public final long zzayj() {
        return zza("select max(timestamp) from raw_events", (String[]) null, 0);
    }

    public final boolean zzayk() {
        return zzb("select count(1) > 0 from raw_events", (String[]) null) != 0;
    }

    public final boolean zzayl() {
        return zzb("select count(1) > 0 from raw_events where realtime = 1", (String[]) null) != 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0044  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final long zzaym() {
        /*
            r7 = this;
            r0 = -1
            r2 = 0
            android.database.sqlite.SQLiteDatabase r3 = r7.getWritableDatabase()     // Catch:{ SQLiteException -> 0x002e }
            java.lang.String r4 = "select rowid from raw_events order by rowid desc limit 1;"
            android.database.Cursor r3 = r3.rawQuery(r4, r2)     // Catch:{ SQLiteException -> 0x002e }
            boolean r2 = r3.moveToFirst()     // Catch:{ SQLiteException -> 0x0027, all -> 0x0024 }
            if (r2 != 0) goto L_0x0019
            if (r3 == 0) goto L_0x0018
            r3.close()
        L_0x0018:
            return r0
        L_0x0019:
            r2 = 0
            long r4 = r3.getLong(r2)     // Catch:{ SQLiteException -> 0x0027, all -> 0x0024 }
            if (r3 == 0) goto L_0x0023
            r3.close()
        L_0x0023:
            return r4
        L_0x0024:
            r0 = move-exception
            r2 = r3
            goto L_0x0042
        L_0x0027:
            r2 = move-exception
            r6 = r3
            r3 = r2
            r2 = r6
            goto L_0x002f
        L_0x002c:
            r0 = move-exception
            goto L_0x0042
        L_0x002e:
            r3 = move-exception
        L_0x002f:
            com.google.android.gms.internal.zzchm r4 = r7.zzawy()     // Catch:{ all -> 0x002c }
            com.google.android.gms.internal.zzcho r4 = r4.zzazd()     // Catch:{ all -> 0x002c }
            java.lang.String r5 = "Error querying raw events"
            r4.zzj(r5, r3)     // Catch:{ all -> 0x002c }
            if (r2 == 0) goto L_0x0041
            r2.close()
        L_0x0041:
            return r0
        L_0x0042:
            if (r2 == 0) goto L_0x0047
            r2.close()
        L_0x0047:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgo.zzaym():long");
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0054  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x005b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.String zzba(long r5) {
        /*
            r4 = this;
            r4.zzve()
            r4.zzxf()
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r4.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0043, all -> 0x0040 }
            java.lang.String r2 = "select app_id from apps where app_id in (select distinct app_id from raw_events) and config_fetched_time < ? order by failed_config_fetch_time limit 1;"
            r3 = 1
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch:{ SQLiteException -> 0x0043, all -> 0x0040 }
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ SQLiteException -> 0x0043, all -> 0x0040 }
            r6 = 0
            r3[r6] = r5     // Catch:{ SQLiteException -> 0x0043, all -> 0x0040 }
            android.database.Cursor r5 = r1.rawQuery(r2, r3)     // Catch:{ SQLiteException -> 0x0043, all -> 0x0040 }
            boolean r1 = r5.moveToFirst()     // Catch:{ SQLiteException -> 0x003e }
            if (r1 != 0) goto L_0x0034
            com.google.android.gms.internal.zzchm r6 = r4.zzawy()     // Catch:{ SQLiteException -> 0x003e }
            com.google.android.gms.internal.zzcho r6 = r6.zzazj()     // Catch:{ SQLiteException -> 0x003e }
            java.lang.String r1 = "No expired configs for apps with pending events"
            r6.log(r1)     // Catch:{ SQLiteException -> 0x003e }
            if (r5 == 0) goto L_0x0033
            r5.close()
        L_0x0033:
            return r0
        L_0x0034:
            java.lang.String r6 = r5.getString(r6)     // Catch:{ SQLiteException -> 0x003e }
            if (r5 == 0) goto L_0x003d
            r5.close()
        L_0x003d:
            return r6
        L_0x003e:
            r6 = move-exception
            goto L_0x0045
        L_0x0040:
            r6 = move-exception
            r5 = r0
            goto L_0x0059
        L_0x0043:
            r6 = move-exception
            r5 = r0
        L_0x0045:
            com.google.android.gms.internal.zzchm r1 = r4.zzawy()     // Catch:{ all -> 0x0058 }
            com.google.android.gms.internal.zzcho r1 = r1.zzazd()     // Catch:{ all -> 0x0058 }
            java.lang.String r2 = "Error selecting expired configs"
            r1.zzj(r2, r6)     // Catch:{ all -> 0x0058 }
            if (r5 == 0) goto L_0x0057
            r5.close()
        L_0x0057:
            return r0
        L_0x0058:
            r6 = move-exception
        L_0x0059:
            if (r5 == 0) goto L_0x005e
            r5.close()
        L_0x005e:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgo.zzba(long):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0127  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x012d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.List<com.google.android.gms.internal.zzcgl> zzc(java.lang.String r26, java.lang.String[] r27) {
        /*
            r25 = this;
            r25.zzve()
            r25.zzxf()
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            r2 = 0
            android.database.sqlite.SQLiteDatabase r3 = r25.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0112 }
            java.lang.String r4 = "conditional_properties"
            java.lang.String r5 = "app_id"
            java.lang.String r6 = "origin"
            java.lang.String r7 = "name"
            java.lang.String r8 = "value"
            java.lang.String r9 = "active"
            java.lang.String r10 = "trigger_event_name"
            java.lang.String r11 = "trigger_timeout"
            java.lang.String r12 = "timed_out_event"
            java.lang.String r13 = "creation_timestamp"
            java.lang.String r14 = "triggered_event"
            java.lang.String r15 = "triggered_timestamp"
            java.lang.String r16 = "time_to_live"
            java.lang.String r17 = "expired_event"
            java.lang.String[] r5 = new java.lang.String[]{r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17}     // Catch:{ SQLiteException -> 0x0112 }
            r8 = 0
            r9 = 0
            java.lang.String r10 = "rowid"
            java.lang.String r11 = "1001"
            r6 = r26
            r7 = r27
            android.database.Cursor r3 = r3.query(r4, r5, r6, r7, r8, r9, r10, r11)     // Catch:{ SQLiteException -> 0x0112 }
            boolean r2 = r3.moveToFirst()     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            if (r2 != 0) goto L_0x004a
            if (r3 == 0) goto L_0x0049
            r3.close()
        L_0x0049:
            return r1
        L_0x004a:
            int r2 = r1.size()     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            r4 = 1000(0x3e8, float:1.401E-42)
            if (r2 < r4) goto L_0x0065
            com.google.android.gms.internal.zzchm r2 = r25.zzawy()     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            com.google.android.gms.internal.zzcho r2 = r2.zzazd()     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            java.lang.String r5 = "Read more than the max allowed conditional properties, ignoring extra"
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            r2.zzj(r5, r4)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            goto L_0x0101
        L_0x0065:
            r2 = 0
            java.lang.String r5 = r3.getString(r2)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            r4 = 1
            java.lang.String r12 = r3.getString(r4)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            r6 = 2
            java.lang.String r7 = r3.getString(r6)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            r6 = 3
            r15 = r25
            java.lang.Object r10 = r15.zza(r3, r6)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            r6 = 4
            int r6 = r3.getInt(r6)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            if (r6 == 0) goto L_0x0083
            r2 = 1
        L_0x0083:
            r4 = 5
            java.lang.String r13 = r3.getString(r4)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            r4 = 6
            long r16 = r3.getLong(r4)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            com.google.android.gms.internal.zzclq r4 = r25.zzawu()     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            r6 = 7
            byte[] r6 = r3.getBlob(r6)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcha> r8 = com.google.android.gms.internal.zzcha.CREATOR     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            android.os.Parcelable r4 = r4.zzb(r6, r8)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            r14 = r4
            com.google.android.gms.internal.zzcha r14 = (com.google.android.gms.internal.zzcha) r14     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            r4 = 8
            long r18 = r3.getLong(r4)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            com.google.android.gms.internal.zzclq r4 = r25.zzawu()     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            r6 = 9
            byte[] r6 = r3.getBlob(r6)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcha> r8 = com.google.android.gms.internal.zzcha.CREATOR     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            android.os.Parcelable r4 = r4.zzb(r6, r8)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            r20 = r4
            com.google.android.gms.internal.zzcha r20 = (com.google.android.gms.internal.zzcha) r20     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            r4 = 10
            long r8 = r3.getLong(r4)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            r4 = 11
            long r21 = r3.getLong(r4)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            com.google.android.gms.internal.zzclq r4 = r25.zzawu()     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            r6 = 12
            byte[] r6 = r3.getBlob(r6)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcha> r11 = com.google.android.gms.internal.zzcha.CREATOR     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            android.os.Parcelable r4 = r4.zzb(r6, r11)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            r23 = r4
            com.google.android.gms.internal.zzcha r23 = (com.google.android.gms.internal.zzcha) r23     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            com.google.android.gms.internal.zzcln r24 = new com.google.android.gms.internal.zzcln     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            r6 = r24
            r11 = r12
            r6.<init>(r7, r8, r10, r11)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            com.google.android.gms.internal.zzcgl r11 = new com.google.android.gms.internal.zzcgl     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            r4 = r11
            r6 = r12
            r7 = r24
            r8 = r18
            r10 = r2
            r2 = r11
            r11 = r13
            r12 = r14
            r13 = r16
            r15 = r20
            r16 = r21
            r18 = r23
            r4.<init>(r5, r6, r7, r8, r10, r11, r12, r13, r15, r16, r18)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            r1.add(r2)     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            boolean r2 = r3.moveToNext()     // Catch:{ SQLiteException -> 0x010a, all -> 0x0107 }
            if (r2 != 0) goto L_0x004a
        L_0x0101:
            if (r3 == 0) goto L_0x0106
            r3.close()
        L_0x0106:
            return r1
        L_0x0107:
            r0 = move-exception
            r1 = r0
            goto L_0x012b
        L_0x010a:
            r0 = move-exception
            r1 = r0
            r2 = r3
            goto L_0x0114
        L_0x010e:
            r0 = move-exception
            r1 = r0
            r3 = r2
            goto L_0x012b
        L_0x0112:
            r0 = move-exception
            r1 = r0
        L_0x0114:
            com.google.android.gms.internal.zzchm r3 = r25.zzawy()     // Catch:{ all -> 0x010e }
            com.google.android.gms.internal.zzcho r3 = r3.zzazd()     // Catch:{ all -> 0x010e }
            java.lang.String r4 = "Error querying conditional user property value"
            r3.zzj(r4, r1)     // Catch:{ all -> 0x010e }
            java.util.List r1 = java.util.Collections.emptyList()     // Catch:{ all -> 0x010e }
            if (r2 == 0) goto L_0x012a
            r2.close()
        L_0x012a:
            return r1
        L_0x012b:
            if (r3 == 0) goto L_0x0130
            r3.close()
        L_0x0130:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgo.zzc(java.lang.String, java.lang.String[]):java.util.List");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0032, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0033, code lost:
        r13 = r23;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0104, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0105, code lost:
        r13 = r23;
        r1 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0109, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x010a, code lost:
        r13 = r23;
        r12 = r24;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0125, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x012e, code lost:
        r2.close();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0104 A[ExcHandler: all (r0v5 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:1:0x000f] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0125  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x012e  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.List<com.google.android.gms.internal.zzclp> zzg(java.lang.String r24, java.lang.String r25, java.lang.String r26) {
        /*
            r23 = this;
            com.google.android.gms.common.internal.zzbq.zzgm(r24)
            r23.zzve()
            r23.zzxf()
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            r2 = 0
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ SQLiteException -> 0x0109, all -> 0x0104 }
            r4 = 3
            r3.<init>(r4)     // Catch:{ SQLiteException -> 0x0109, all -> 0x0104 }
            r12 = r24
            r3.add(r12)     // Catch:{ SQLiteException -> 0x0100, all -> 0x0104 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ SQLiteException -> 0x0100, all -> 0x0104 }
            java.lang.String r6 = "app_id=?"
            r5.<init>(r6)     // Catch:{ SQLiteException -> 0x0100, all -> 0x0104 }
            boolean r6 = android.text.TextUtils.isEmpty(r25)     // Catch:{ SQLiteException -> 0x0100, all -> 0x0104 }
            if (r6 != 0) goto L_0x0037
            r6 = r25
            r3.add(r6)     // Catch:{ SQLiteException -> 0x0032, all -> 0x0104 }
            java.lang.String r7 = " and origin=?"
            r5.append(r7)     // Catch:{ SQLiteException -> 0x0032, all -> 0x0104 }
            goto L_0x0039
        L_0x0032:
            r0 = move-exception
            r13 = r23
            goto L_0x0110
        L_0x0037:
            r6 = r25
        L_0x0039:
            boolean r7 = android.text.TextUtils.isEmpty(r26)     // Catch:{ SQLiteException -> 0x0032, all -> 0x0104 }
            if (r7 != 0) goto L_0x0051
            java.lang.String r7 = java.lang.String.valueOf(r26)     // Catch:{ SQLiteException -> 0x0032, all -> 0x0104 }
            java.lang.String r8 = "*"
            java.lang.String r7 = r7.concat(r8)     // Catch:{ SQLiteException -> 0x0032, all -> 0x0104 }
            r3.add(r7)     // Catch:{ SQLiteException -> 0x0032, all -> 0x0104 }
            java.lang.String r7 = " and name glob ?"
            r5.append(r7)     // Catch:{ SQLiteException -> 0x0032, all -> 0x0104 }
        L_0x0051:
            int r7 = r3.size()     // Catch:{ SQLiteException -> 0x0032, all -> 0x0104 }
            java.lang.String[] r7 = new java.lang.String[r7]     // Catch:{ SQLiteException -> 0x0032, all -> 0x0104 }
            java.lang.Object[] r3 = r3.toArray(r7)     // Catch:{ SQLiteException -> 0x0032, all -> 0x0104 }
            r17 = r3
            java.lang.String[] r17 = (java.lang.String[]) r17     // Catch:{ SQLiteException -> 0x0032, all -> 0x0104 }
            android.database.sqlite.SQLiteDatabase r13 = r23.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0032, all -> 0x0104 }
            java.lang.String r14 = "user_attributes"
            java.lang.String r3 = "name"
            java.lang.String r7 = "set_timestamp"
            java.lang.String r8 = "value"
            java.lang.String r9 = "origin"
            java.lang.String[] r15 = new java.lang.String[]{r3, r7, r8, r9}     // Catch:{ SQLiteException -> 0x0032, all -> 0x0104 }
            java.lang.String r16 = r5.toString()     // Catch:{ SQLiteException -> 0x0032, all -> 0x0104 }
            r18 = 0
            r19 = 0
            java.lang.String r20 = "rowid"
            java.lang.String r21 = "1001"
            android.database.Cursor r3 = r13.query(r14, r15, r16, r17, r18, r19, r20, r21)     // Catch:{ SQLiteException -> 0x0032, all -> 0x0104 }
            boolean r5 = r3.moveToFirst()     // Catch:{ SQLiteException -> 0x00fb, all -> 0x00f7 }
            if (r5 != 0) goto L_0x008d
            if (r3 == 0) goto L_0x008c
            r3.close()
        L_0x008c:
            return r1
        L_0x008d:
            int r5 = r1.size()     // Catch:{ SQLiteException -> 0x00fb, all -> 0x00f7 }
            r7 = 1000(0x3e8, float:1.401E-42)
            if (r5 < r7) goto L_0x00a9
            com.google.android.gms.internal.zzchm r4 = r23.zzawy()     // Catch:{ SQLiteException -> 0x00fb, all -> 0x00f7 }
            com.google.android.gms.internal.zzcho r4 = r4.zzazd()     // Catch:{ SQLiteException -> 0x00fb, all -> 0x00f7 }
            java.lang.String r5 = "Read more than the max allowed user properties, ignoring excess"
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ SQLiteException -> 0x00fb, all -> 0x00f7 }
            r4.zzj(r5, r7)     // Catch:{ SQLiteException -> 0x00fb, all -> 0x00f7 }
            r13 = r23
            goto L_0x00ec
        L_0x00a9:
            r5 = 0
            java.lang.String r8 = r3.getString(r5)     // Catch:{ SQLiteException -> 0x00fb, all -> 0x00f7 }
            r5 = 1
            long r9 = r3.getLong(r5)     // Catch:{ SQLiteException -> 0x00fb, all -> 0x00f7 }
            r5 = 2
            r13 = r23
            java.lang.Object r11 = r13.zza(r3, r5)     // Catch:{ SQLiteException -> 0x00f5 }
            java.lang.String r14 = r3.getString(r4)     // Catch:{ SQLiteException -> 0x00f5 }
            if (r11 != 0) goto L_0x00d8
            com.google.android.gms.internal.zzchm r5 = r23.zzawy()     // Catch:{ SQLiteException -> 0x00d4 }
            com.google.android.gms.internal.zzcho r5 = r5.zzazd()     // Catch:{ SQLiteException -> 0x00d4 }
            java.lang.String r6 = "(2)Read invalid user property value, ignoring it"
            java.lang.Object r7 = com.google.android.gms.internal.zzchm.zzjk(r24)     // Catch:{ SQLiteException -> 0x00d4 }
            r15 = r26
            r5.zzd(r6, r7, r14, r15)     // Catch:{ SQLiteException -> 0x00d4 }
            goto L_0x00e6
        L_0x00d4:
            r0 = move-exception
            r1 = r0
            r6 = r14
            goto L_0x0112
        L_0x00d8:
            r15 = r26
            com.google.android.gms.internal.zzclp r7 = new com.google.android.gms.internal.zzclp     // Catch:{ SQLiteException -> 0x00d4 }
            r5 = r7
            r6 = r12
            r4 = r7
            r7 = r14
            r5.<init>(r6, r7, r8, r9, r11)     // Catch:{ SQLiteException -> 0x00d4 }
            r1.add(r4)     // Catch:{ SQLiteException -> 0x00d4 }
        L_0x00e6:
            boolean r4 = r3.moveToNext()     // Catch:{ SQLiteException -> 0x00d4 }
            if (r4 != 0) goto L_0x00f2
        L_0x00ec:
            if (r3 == 0) goto L_0x00f1
            r3.close()
        L_0x00f1:
            return r1
        L_0x00f2:
            r6 = r14
            r4 = 3
            goto L_0x008d
        L_0x00f5:
            r0 = move-exception
            goto L_0x00fe
        L_0x00f7:
            r0 = move-exception
            r13 = r23
            goto L_0x012a
        L_0x00fb:
            r0 = move-exception
            r13 = r23
        L_0x00fe:
            r1 = r0
            goto L_0x0112
        L_0x0100:
            r0 = move-exception
            r13 = r23
            goto L_0x010e
        L_0x0104:
            r0 = move-exception
            r13 = r23
            r1 = r0
            goto L_0x012c
        L_0x0109:
            r0 = move-exception
            r13 = r23
            r12 = r24
        L_0x010e:
            r6 = r25
        L_0x0110:
            r1 = r0
            r3 = r2
        L_0x0112:
            com.google.android.gms.internal.zzchm r4 = r23.zzawy()     // Catch:{ all -> 0x0129 }
            com.google.android.gms.internal.zzcho r4 = r4.zzazd()     // Catch:{ all -> 0x0129 }
            java.lang.String r5 = "(2)Error querying user properties"
            java.lang.Object r7 = com.google.android.gms.internal.zzchm.zzjk(r24)     // Catch:{ all -> 0x0129 }
            r4.zzd(r5, r7, r6, r1)     // Catch:{ all -> 0x0129 }
            if (r3 == 0) goto L_0x0128
            r3.close()
        L_0x0128:
            return r2
        L_0x0129:
            r0 = move-exception
        L_0x012a:
            r1 = r0
            r2 = r3
        L_0x012c:
            if (r2 == 0) goto L_0x0131
            r2.close()
        L_0x0131:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgo.zzg(java.lang.String, java.lang.String, java.lang.String):java.util.List");
    }

    @WorkerThread
    public final List<zzcgl> zzh(String str, String str2, String str3) {
        zzbq.zzgm(str);
        zzve();
        zzxf();
        ArrayList arrayList = new ArrayList(3);
        arrayList.add(str);
        StringBuilder sb = new StringBuilder("app_id=?");
        if (!TextUtils.isEmpty(str2)) {
            arrayList.add(str2);
            sb.append(" and origin=?");
        }
        if (!TextUtils.isEmpty(str3)) {
            arrayList.add(String.valueOf(str3).concat("*"));
            sb.append(" and name glob ?");
        }
        return zzc(sb.toString(), (String[]) arrayList.toArray(new String[arrayList.size()]));
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x009a  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00a1  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.List<com.google.android.gms.internal.zzclp> zzja(java.lang.String r14) {
        /*
            r13 = this;
            com.google.android.gms.common.internal.zzbq.zzgm(r14)
            r13.zzve()
            r13.zzxf()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1 = 0
            android.database.sqlite.SQLiteDatabase r2 = r13.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0085, all -> 0x0082 }
            java.lang.String r3 = "user_attributes"
            java.lang.String r4 = "name"
            java.lang.String r5 = "origin"
            java.lang.String r6 = "set_timestamp"
            java.lang.String r7 = "value"
            java.lang.String[] r4 = new java.lang.String[]{r4, r5, r6, r7}     // Catch:{ SQLiteException -> 0x0085, all -> 0x0082 }
            java.lang.String r5 = "app_id=?"
            r11 = 1
            java.lang.String[] r6 = new java.lang.String[r11]     // Catch:{ SQLiteException -> 0x0085, all -> 0x0082 }
            r12 = 0
            r6[r12] = r14     // Catch:{ SQLiteException -> 0x0085, all -> 0x0082 }
            r7 = 0
            r8 = 0
            java.lang.String r9 = "rowid"
            java.lang.String r10 = "1000"
            android.database.Cursor r2 = r2.query(r3, r4, r5, r6, r7, r8, r9, r10)     // Catch:{ SQLiteException -> 0x0085, all -> 0x0082 }
            boolean r3 = r2.moveToFirst()     // Catch:{ SQLiteException -> 0x0080 }
            if (r3 != 0) goto L_0x003f
            if (r2 == 0) goto L_0x003e
            r2.close()
        L_0x003e:
            return r0
        L_0x003f:
            java.lang.String r7 = r2.getString(r12)     // Catch:{ SQLiteException -> 0x0080 }
            java.lang.String r3 = r2.getString(r11)     // Catch:{ SQLiteException -> 0x0080 }
            if (r3 != 0) goto L_0x004b
            java.lang.String r3 = ""
        L_0x004b:
            r6 = r3
            r3 = 2
            long r8 = r2.getLong(r3)     // Catch:{ SQLiteException -> 0x0080 }
            r3 = 3
            java.lang.Object r10 = r13.zza(r2, r3)     // Catch:{ SQLiteException -> 0x0080 }
            if (r10 != 0) goto L_0x006a
            com.google.android.gms.internal.zzchm r3 = r13.zzawy()     // Catch:{ SQLiteException -> 0x0080 }
            com.google.android.gms.internal.zzcho r3 = r3.zzazd()     // Catch:{ SQLiteException -> 0x0080 }
            java.lang.String r4 = "Read invalid user property value, ignoring it. appId"
            java.lang.Object r5 = com.google.android.gms.internal.zzchm.zzjk(r14)     // Catch:{ SQLiteException -> 0x0080 }
            r3.zzj(r4, r5)     // Catch:{ SQLiteException -> 0x0080 }
            goto L_0x0074
        L_0x006a:
            com.google.android.gms.internal.zzclp r3 = new com.google.android.gms.internal.zzclp     // Catch:{ SQLiteException -> 0x0080 }
            r4 = r3
            r5 = r14
            r4.<init>(r5, r6, r7, r8, r10)     // Catch:{ SQLiteException -> 0x0080 }
            r0.add(r3)     // Catch:{ SQLiteException -> 0x0080 }
        L_0x0074:
            boolean r3 = r2.moveToNext()     // Catch:{ SQLiteException -> 0x0080 }
            if (r3 != 0) goto L_0x003f
            if (r2 == 0) goto L_0x007f
            r2.close()
        L_0x007f:
            return r0
        L_0x0080:
            r0 = move-exception
            goto L_0x0087
        L_0x0082:
            r14 = move-exception
            r2 = r1
            goto L_0x009f
        L_0x0085:
            r0 = move-exception
            r2 = r1
        L_0x0087:
            com.google.android.gms.internal.zzchm r3 = r13.zzawy()     // Catch:{ all -> 0x009e }
            com.google.android.gms.internal.zzcho r3 = r3.zzazd()     // Catch:{ all -> 0x009e }
            java.lang.String r4 = "Error querying user properties. appId"
            java.lang.Object r14 = com.google.android.gms.internal.zzchm.zzjk(r14)     // Catch:{ all -> 0x009e }
            r3.zze(r4, r14, r0)     // Catch:{ all -> 0x009e }
            if (r2 == 0) goto L_0x009d
            r2.close()
        L_0x009d:
            return r1
        L_0x009e:
            r14 = move-exception
        L_0x009f:
            if (r2 == 0) goto L_0x00a4
            r2.close()
        L_0x00a4:
            throw r14
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgo.zzja(java.lang.String):java.util.List");
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x010d A[Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0111 A[Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0145 A[Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0148 A[Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x016a A[Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x017d  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x01ae  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x01b7  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.internal.zzcgh zzjb(java.lang.String r30) {
        /*
            r29 = this;
            r1 = r30
            com.google.android.gms.common.internal.zzbq.zzgm(r30)
            r29.zzve()
            r29.zzxf()
            r2 = 0
            android.database.sqlite.SQLiteDatabase r3 = r29.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0196, all -> 0x0190 }
            java.lang.String r4 = "apps"
            java.lang.String r5 = "app_instance_id"
            java.lang.String r6 = "gmp_app_id"
            java.lang.String r7 = "resettable_device_id_hash"
            java.lang.String r8 = "last_bundle_index"
            java.lang.String r9 = "last_bundle_start_timestamp"
            java.lang.String r10 = "last_bundle_end_timestamp"
            java.lang.String r11 = "app_version"
            java.lang.String r12 = "app_store"
            java.lang.String r13 = "gmp_version"
            java.lang.String r14 = "dev_cert_hash"
            java.lang.String r15 = "measurement_enabled"
            java.lang.String r16 = "day"
            java.lang.String r17 = "daily_public_events_count"
            java.lang.String r18 = "daily_events_count"
            java.lang.String r19 = "daily_conversions_count"
            java.lang.String r20 = "config_fetched_time"
            java.lang.String r21 = "failed_config_fetch_time"
            java.lang.String r22 = "app_version_int"
            java.lang.String r23 = "firebase_instance_id"
            java.lang.String r24 = "daily_error_events_count"
            java.lang.String r25 = "daily_realtime_events_count"
            java.lang.String r26 = "health_monitor_sample"
            java.lang.String r27 = "android_id"
            java.lang.String r28 = "adid_reporting_enabled"
            java.lang.String[] r5 = new java.lang.String[]{r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27, r28}     // Catch:{ SQLiteException -> 0x0196, all -> 0x0190 }
            java.lang.String r6 = "app_id=?"
            r11 = 1
            java.lang.String[] r7 = new java.lang.String[r11]     // Catch:{ SQLiteException -> 0x0196, all -> 0x0190 }
            r12 = 0
            r7[r12] = r1     // Catch:{ SQLiteException -> 0x0196, all -> 0x0190 }
            r8 = 0
            r9 = 0
            r10 = 0
            android.database.Cursor r3 = r3.query(r4, r5, r6, r7, r8, r9, r10)     // Catch:{ SQLiteException -> 0x0196, all -> 0x0190 }
            boolean r4 = r3.moveToFirst()     // Catch:{ SQLiteException -> 0x018a, all -> 0x0185 }
            if (r4 != 0) goto L_0x0061
            if (r3 == 0) goto L_0x0060
            r3.close()
        L_0x0060:
            return r2
        L_0x0061:
            com.google.android.gms.internal.zzcgh r4 = new com.google.android.gms.internal.zzcgh     // Catch:{ SQLiteException -> 0x018a, all -> 0x0185 }
            r5 = r29
            com.google.android.gms.internal.zzcim r6 = r5.zziwf     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.<init>(r6, r1)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            java.lang.String r6 = r3.getString(r12)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.zzir(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            java.lang.String r6 = r3.getString(r11)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.zzis(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 2
            java.lang.String r6 = r3.getString(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.zzit(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 3
            long r6 = r3.getLong(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.zzaq(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 4
            long r6 = r3.getLong(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.zzal(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 5
            long r6 = r3.getLong(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.zzam(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 6
            java.lang.String r6 = r3.getString(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.setAppVersion(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 7
            java.lang.String r6 = r3.getString(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.zziv(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 8
            long r6 = r3.getLong(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.zzao(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 9
            long r6 = r3.getLong(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.zzap(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 10
            boolean r7 = r3.isNull(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            if (r7 != 0) goto L_0x00cb
            int r6 = r3.getInt(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            if (r6 == 0) goto L_0x00c9
            goto L_0x00cb
        L_0x00c9:
            r6 = 0
            goto L_0x00cc
        L_0x00cb:
            r6 = 1
        L_0x00cc:
            r4.setMeasurementEnabled(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 11
            long r6 = r3.getLong(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.zzat(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 12
            long r6 = r3.getLong(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.zzau(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 13
            long r6 = r3.getLong(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.zzav(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 14
            long r6 = r3.getLong(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.zzaw(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 15
            long r6 = r3.getLong(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.zzar(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 16
            long r6 = r3.getLong(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.zzas(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 17
            boolean r7 = r3.isNull(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            if (r7 == 0) goto L_0x0111
            r6 = -2147483648(0xffffffff80000000, double:NaN)
            goto L_0x0116
        L_0x0111:
            int r6 = r3.getInt(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            long r6 = (long) r6     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
        L_0x0116:
            r4.zzan(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 18
            java.lang.String r6 = r3.getString(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.zziu(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 19
            long r6 = r3.getLong(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.zzay(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 20
            long r6 = r3.getLong(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.zzax(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 21
            java.lang.String r6 = r3.getString(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.zziw(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 22
            boolean r7 = r3.isNull(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            if (r7 == 0) goto L_0x0148
            r6 = 0
            goto L_0x014c
        L_0x0148:
            long r6 = r3.getLong(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
        L_0x014c:
            r4.zzaz(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6 = 23
            boolean r7 = r3.isNull(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            if (r7 != 0) goto L_0x015d
            int r6 = r3.getInt(r6)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            if (r6 == 0) goto L_0x015e
        L_0x015d:
            r12 = 1
        L_0x015e:
            r4.zzbl(r12)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r4.zzaxb()     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            boolean r6 = r3.moveToNext()     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            if (r6 == 0) goto L_0x017b
            com.google.android.gms.internal.zzchm r6 = r29.zzawy()     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            com.google.android.gms.internal.zzcho r6 = r6.zzazd()     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            java.lang.String r7 = "Got multiple records for app, expected one. appId"
            java.lang.Object r8 = com.google.android.gms.internal.zzchm.zzjk(r30)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
            r6.zzj(r7, r8)     // Catch:{ SQLiteException -> 0x0183, all -> 0x0181 }
        L_0x017b:
            if (r3 == 0) goto L_0x0180
            r3.close()
        L_0x0180:
            return r4
        L_0x0181:
            r0 = move-exception
            goto L_0x0188
        L_0x0183:
            r0 = move-exception
            goto L_0x018d
        L_0x0185:
            r0 = move-exception
            r5 = r29
        L_0x0188:
            r1 = r0
            goto L_0x01b5
        L_0x018a:
            r0 = move-exception
            r5 = r29
        L_0x018d:
            r4 = r3
            r3 = r0
            goto L_0x019b
        L_0x0190:
            r0 = move-exception
            r5 = r29
            r1 = r0
            r3 = r2
            goto L_0x01b5
        L_0x0196:
            r0 = move-exception
            r5 = r29
            r3 = r0
            r4 = r2
        L_0x019b:
            com.google.android.gms.internal.zzchm r6 = r29.zzawy()     // Catch:{ all -> 0x01b2 }
            com.google.android.gms.internal.zzcho r6 = r6.zzazd()     // Catch:{ all -> 0x01b2 }
            java.lang.String r7 = "Error querying app. appId"
            java.lang.Object r1 = com.google.android.gms.internal.zzchm.zzjk(r30)     // Catch:{ all -> 0x01b2 }
            r6.zze(r7, r1, r3)     // Catch:{ all -> 0x01b2 }
            if (r4 == 0) goto L_0x01b1
            r4.close()
        L_0x01b1:
            return r2
        L_0x01b2:
            r0 = move-exception
            r1 = r0
            r3 = r4
        L_0x01b5:
            if (r3 == 0) goto L_0x01ba
            r3.close()
        L_0x01ba:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgo.zzjb(java.lang.String):com.google.android.gms.internal.zzcgh");
    }

    public final long zzjc(String str) {
        zzbq.zzgm(str);
        zzve();
        zzxf();
        try {
            return (long) getWritableDatabase().delete("raw_events", "rowid in (select rowid from raw_events where app_id=? order by rowid desc limit -1 offset ?)", new String[]{str, String.valueOf(Math.max(0, Math.min(1000000, zzaxa().zzb(str, zzchc.zzjas))))});
        } catch (SQLiteException e) {
            zzawy().zzazd().zze("Error deleting over the limit events. appId", zzchm.zzjk(str), e);
            return 0;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x006c  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0073  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final byte[] zzjd(java.lang.String r11) {
        /*
            r10 = this;
            com.google.android.gms.common.internal.zzbq.zzgm(r11)
            r10.zzve()
            r10.zzxf()
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r10.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0057, all -> 0x0054 }
            java.lang.String r2 = "apps"
            java.lang.String r3 = "remote_config"
            java.lang.String[] r3 = new java.lang.String[]{r3}     // Catch:{ SQLiteException -> 0x0057, all -> 0x0054 }
            java.lang.String r4 = "app_id=?"
            r5 = 1
            java.lang.String[] r5 = new java.lang.String[r5]     // Catch:{ SQLiteException -> 0x0057, all -> 0x0054 }
            r9 = 0
            r5[r9] = r11     // Catch:{ SQLiteException -> 0x0057, all -> 0x0054 }
            r6 = 0
            r7 = 0
            r8 = 0
            android.database.Cursor r1 = r1.query(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ SQLiteException -> 0x0057, all -> 0x0054 }
            boolean r2 = r1.moveToFirst()     // Catch:{ SQLiteException -> 0x0052 }
            if (r2 != 0) goto L_0x0031
            if (r1 == 0) goto L_0x0030
            r1.close()
        L_0x0030:
            return r0
        L_0x0031:
            byte[] r2 = r1.getBlob(r9)     // Catch:{ SQLiteException -> 0x0052 }
            boolean r3 = r1.moveToNext()     // Catch:{ SQLiteException -> 0x0052 }
            if (r3 == 0) goto L_0x004c
            com.google.android.gms.internal.zzchm r3 = r10.zzawy()     // Catch:{ SQLiteException -> 0x0052 }
            com.google.android.gms.internal.zzcho r3 = r3.zzazd()     // Catch:{ SQLiteException -> 0x0052 }
            java.lang.String r4 = "Got multiple records for app config, expected one. appId"
            java.lang.Object r5 = com.google.android.gms.internal.zzchm.zzjk(r11)     // Catch:{ SQLiteException -> 0x0052 }
            r3.zzj(r4, r5)     // Catch:{ SQLiteException -> 0x0052 }
        L_0x004c:
            if (r1 == 0) goto L_0x0051
            r1.close()
        L_0x0051:
            return r2
        L_0x0052:
            r2 = move-exception
            goto L_0x0059
        L_0x0054:
            r11 = move-exception
            r1 = r0
            goto L_0x0071
        L_0x0057:
            r2 = move-exception
            r1 = r0
        L_0x0059:
            com.google.android.gms.internal.zzchm r3 = r10.zzawy()     // Catch:{ all -> 0x0070 }
            com.google.android.gms.internal.zzcho r3 = r3.zzazd()     // Catch:{ all -> 0x0070 }
            java.lang.String r4 = "Error querying remote config. appId"
            java.lang.Object r11 = com.google.android.gms.internal.zzchm.zzjk(r11)     // Catch:{ all -> 0x0070 }
            r3.zze(r4, r11, r2)     // Catch:{ all -> 0x0070 }
            if (r1 == 0) goto L_0x006f
            r1.close()
        L_0x006f:
            return r0
        L_0x0070:
            r11 = move-exception
        L_0x0071:
            if (r1 == 0) goto L_0x0076
            r1.close()
        L_0x0076:
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgo.zzjd(java.lang.String):byte[]");
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0091  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0098  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.Map<java.lang.Integer, com.google.android.gms.internal.zzcmf> zzje(java.lang.String r12) {
        /*
            r11 = this;
            r11.zzxf()
            r11.zzve()
            com.google.android.gms.common.internal.zzbq.zzgm(r12)
            android.database.sqlite.SQLiteDatabase r0 = r11.getWritableDatabase()
            r8 = 0
            java.lang.String r1 = "audience_filter_values"
            java.lang.String r2 = "audience_id"
            java.lang.String r3 = "current_results"
            java.lang.String[] r2 = new java.lang.String[]{r2, r3}     // Catch:{ SQLiteException -> 0x007c, all -> 0x0079 }
            java.lang.String r3 = "app_id=?"
            r9 = 1
            java.lang.String[] r4 = new java.lang.String[r9]     // Catch:{ SQLiteException -> 0x007c, all -> 0x0079 }
            r10 = 0
            r4[r10] = r12     // Catch:{ SQLiteException -> 0x007c, all -> 0x0079 }
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r0 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ SQLiteException -> 0x007c, all -> 0x0079 }
            boolean r1 = r0.moveToFirst()     // Catch:{ SQLiteException -> 0x0077 }
            if (r1 != 0) goto L_0x0033
            if (r0 == 0) goto L_0x0032
            r0.close()
        L_0x0032:
            return r8
        L_0x0033:
            android.support.v4.util.ArrayMap r1 = new android.support.v4.util.ArrayMap     // Catch:{ SQLiteException -> 0x0077 }
            r1.<init>()     // Catch:{ SQLiteException -> 0x0077 }
        L_0x0038:
            int r2 = r0.getInt(r10)     // Catch:{ SQLiteException -> 0x0077 }
            byte[] r3 = r0.getBlob(r9)     // Catch:{ SQLiteException -> 0x0077 }
            int r4 = r3.length     // Catch:{ SQLiteException -> 0x0077 }
            com.google.android.gms.internal.zzfjj r3 = com.google.android.gms.internal.zzfjj.zzn(r3, r10, r4)     // Catch:{ SQLiteException -> 0x0077 }
            com.google.android.gms.internal.zzcmf r4 = new com.google.android.gms.internal.zzcmf     // Catch:{ SQLiteException -> 0x0077 }
            r4.<init>()     // Catch:{ SQLiteException -> 0x0077 }
            r4.zza(r3)     // Catch:{ IOException -> 0x0055 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ SQLiteException -> 0x0077 }
            r1.put(r2, r4)     // Catch:{ SQLiteException -> 0x0077 }
            goto L_0x006b
        L_0x0055:
            r3 = move-exception
            com.google.android.gms.internal.zzchm r4 = r11.zzawy()     // Catch:{ SQLiteException -> 0x0077 }
            com.google.android.gms.internal.zzcho r4 = r4.zzazd()     // Catch:{ SQLiteException -> 0x0077 }
            java.lang.String r5 = "Failed to merge filter results. appId, audienceId, error"
            java.lang.Object r6 = com.google.android.gms.internal.zzchm.zzjk(r12)     // Catch:{ SQLiteException -> 0x0077 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ SQLiteException -> 0x0077 }
            r4.zzd(r5, r6, r2, r3)     // Catch:{ SQLiteException -> 0x0077 }
        L_0x006b:
            boolean r2 = r0.moveToNext()     // Catch:{ SQLiteException -> 0x0077 }
            if (r2 != 0) goto L_0x0038
            if (r0 == 0) goto L_0x0076
            r0.close()
        L_0x0076:
            return r1
        L_0x0077:
            r1 = move-exception
            goto L_0x007e
        L_0x0079:
            r12 = move-exception
            r0 = r8
            goto L_0x0096
        L_0x007c:
            r1 = move-exception
            r0 = r8
        L_0x007e:
            com.google.android.gms.internal.zzchm r2 = r11.zzawy()     // Catch:{ all -> 0x0095 }
            com.google.android.gms.internal.zzcho r2 = r2.zzazd()     // Catch:{ all -> 0x0095 }
            java.lang.String r3 = "Database error querying filter results. appId"
            java.lang.Object r12 = com.google.android.gms.internal.zzchm.zzjk(r12)     // Catch:{ all -> 0x0095 }
            r2.zze(r3, r12, r1)     // Catch:{ all -> 0x0095 }
            if (r0 == 0) goto L_0x0094
            r0.close()
        L_0x0094:
            return r8
        L_0x0095:
            r12 = move-exception
        L_0x0096:
            if (r0 == 0) goto L_0x009b
            r0.close()
        L_0x009b:
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgo.zzje(java.lang.String):java.util.Map");
    }

    public final long zzjf(String str) {
        zzbq.zzgm(str);
        return zza("select count(1) from events where app_id=? and name not like '!_%' escape '!'", new String[]{str}, 0);
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x00b0 A[LOOP:0: B:20:0x0052->B:40:0x00b0, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00b4  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00d8  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00de  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x00b2 A[EDGE_INSN: B:59:0x00b2->B:41:0x00b2 ?: BREAK  , SYNTHETIC] */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.List<android.util.Pair<com.google.android.gms.internal.zzcme, java.lang.Long>> zzl(java.lang.String r13, int r14, int r15) {
        /*
            r12 = this;
            r12.zzve()
            r12.zzxf()
            r0 = 1
            r1 = 0
            if (r14 <= 0) goto L_0x000c
            r2 = 1
            goto L_0x000d
        L_0x000c:
            r2 = 0
        L_0x000d:
            com.google.android.gms.common.internal.zzbq.checkArgument(r2)
            if (r15 <= 0) goto L_0x0014
            r2 = 1
            goto L_0x0015
        L_0x0014:
            r2 = 0
        L_0x0015:
            com.google.android.gms.common.internal.zzbq.checkArgument(r2)
            com.google.android.gms.common.internal.zzbq.zzgm(r13)
            r2 = 0
            android.database.sqlite.SQLiteDatabase r3 = r12.getWritableDatabase()     // Catch:{ SQLiteException -> 0x00c0 }
            java.lang.String r4 = "queue"
            java.lang.String r5 = "rowid"
            java.lang.String r6 = "data"
            java.lang.String[] r5 = new java.lang.String[]{r5, r6}     // Catch:{ SQLiteException -> 0x00c0 }
            java.lang.String r6 = "app_id=?"
            java.lang.String[] r7 = new java.lang.String[r0]     // Catch:{ SQLiteException -> 0x00c0 }
            r7[r1] = r13     // Catch:{ SQLiteException -> 0x00c0 }
            r8 = 0
            r9 = 0
            java.lang.String r10 = "rowid"
            java.lang.String r11 = java.lang.String.valueOf(r14)     // Catch:{ SQLiteException -> 0x00c0 }
            android.database.Cursor r14 = r3.query(r4, r5, r6, r7, r8, r9, r10, r11)     // Catch:{ SQLiteException -> 0x00c0 }
            boolean r2 = r14.moveToFirst()     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            if (r2 != 0) goto L_0x004c
            java.util.List r15 = java.util.Collections.emptyList()     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            if (r14 == 0) goto L_0x004b
            r14.close()
        L_0x004b:
            return r15
        L_0x004c:
            java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            r2.<init>()     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            r3 = 0
        L_0x0052:
            long r4 = r14.getLong(r1)     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            byte[] r6 = r14.getBlob(r0)     // Catch:{ IOException -> 0x009a }
            com.google.android.gms.internal.zzclq r7 = r12.zzawu()     // Catch:{ IOException -> 0x009a }
            byte[] r6 = r7.zzr(r6)     // Catch:{ IOException -> 0x009a }
            boolean r7 = r2.isEmpty()     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            if (r7 != 0) goto L_0x006c
            int r7 = r6.length     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            int r7 = r7 + r3
            if (r7 > r15) goto L_0x00b2
        L_0x006c:
            int r7 = r6.length     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            com.google.android.gms.internal.zzfjj r7 = com.google.android.gms.internal.zzfjj.zzn(r6, r1, r7)     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            com.google.android.gms.internal.zzcme r8 = new com.google.android.gms.internal.zzcme     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            r8.<init>()     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            r8.zza(r7)     // Catch:{ IOException -> 0x0087 }
            int r6 = r6.length     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            int r3 = r3 + r6
            java.lang.Long r4 = java.lang.Long.valueOf(r4)     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            android.util.Pair r4 = android.util.Pair.create(r8, r4)     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            r2.add(r4)     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            goto L_0x00aa
        L_0x0087:
            r4 = move-exception
            com.google.android.gms.internal.zzchm r5 = r12.zzawy()     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            com.google.android.gms.internal.zzcho r5 = r5.zzazd()     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            java.lang.String r6 = "Failed to merge queued bundle. appId"
            java.lang.Object r7 = com.google.android.gms.internal.zzchm.zzjk(r13)     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
        L_0x0096:
            r5.zze(r6, r7, r4)     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            goto L_0x00aa
        L_0x009a:
            r4 = move-exception
            com.google.android.gms.internal.zzchm r5 = r12.zzawy()     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            com.google.android.gms.internal.zzcho r5 = r5.zzazd()     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            java.lang.String r6 = "Failed to unzip queued bundle. appId"
            java.lang.Object r7 = com.google.android.gms.internal.zzchm.zzjk(r13)     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            goto L_0x0096
        L_0x00aa:
            boolean r4 = r14.moveToNext()     // Catch:{ SQLiteException -> 0x00ba, all -> 0x00b8 }
            if (r4 == 0) goto L_0x00b2
            if (r3 <= r15) goto L_0x0052
        L_0x00b2:
            if (r14 == 0) goto L_0x00b7
            r14.close()
        L_0x00b7:
            return r2
        L_0x00b8:
            r13 = move-exception
            goto L_0x00dc
        L_0x00ba:
            r15 = move-exception
            r2 = r14
            goto L_0x00c1
        L_0x00bd:
            r13 = move-exception
            r14 = r2
            goto L_0x00dc
        L_0x00c0:
            r15 = move-exception
        L_0x00c1:
            com.google.android.gms.internal.zzchm r14 = r12.zzawy()     // Catch:{ all -> 0x00bd }
            com.google.android.gms.internal.zzcho r14 = r14.zzazd()     // Catch:{ all -> 0x00bd }
            java.lang.String r0 = "Error querying bundles. appId"
            java.lang.Object r13 = com.google.android.gms.internal.zzchm.zzjk(r13)     // Catch:{ all -> 0x00bd }
            r14.zze(r0, r13, r15)     // Catch:{ all -> 0x00bd }
            java.util.List r13 = java.util.Collections.emptyList()     // Catch:{ all -> 0x00bd }
            if (r2 == 0) goto L_0x00db
            r2.close()
        L_0x00db:
            return r13
        L_0x00dc:
            if (r14 == 0) goto L_0x00e1
            r14.close()
        L_0x00e1:
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgo.zzl(java.lang.String, int, int):java.util.List");
    }
}
