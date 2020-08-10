package com.google.android.gms.internal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.WorkerThread;
import com.google.android.gms.common.util.zzd;

public final class zzchi extends zzcjl {
    private final zzchj zzjbn = new zzchj(this, getContext(), "google_app_measurement_local.db");
    private boolean zzjbo;

    zzchi(zzcim zzcim) {
        super(zzcim);
    }

    @WorkerThread
    private final SQLiteDatabase getWritableDatabase() {
        if (this.zzjbo) {
            return null;
        }
        SQLiteDatabase writableDatabase = this.zzjbn.getWritableDatabase();
        if (writableDatabase != null) {
            return writableDatabase;
        }
        this.zzjbo = true;
        return null;
    }

    /* JADX WARNING: type inference failed for: r3v0 */
    /* JADX WARNING: type inference failed for: r3v1, types: [int, boolean] */
    /* JADX WARNING: type inference failed for: r7v0 */
    /* JADX WARNING: type inference failed for: r12v0, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r9v0, types: [android.database.sqlite.SQLiteDatabase] */
    /* JADX WARNING: type inference failed for: r3v3 */
    /* JADX WARNING: type inference failed for: r9v1, types: [android.database.sqlite.SQLiteDatabase] */
    /* JADX WARNING: type inference failed for: r7v1, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r12v1 */
    /* JADX WARNING: type inference failed for: r9v2 */
    /* JADX WARNING: type inference failed for: r12v2, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r7v2, types: [android.database.sqlite.SQLiteDatabase] */
    /* JADX WARNING: type inference failed for: r9v3 */
    /* JADX WARNING: type inference failed for: r12v3 */
    /* JADX WARNING: type inference failed for: r9v4 */
    /* JADX WARNING: type inference failed for: r12v4 */
    /* JADX WARNING: type inference failed for: r9v5, types: [android.database.sqlite.SQLiteDatabase] */
    /* JADX WARNING: type inference failed for: r12v6, types: [android.database.Cursor] */
    /* JADX WARNING: type inference failed for: r7v3 */
    /* JADX WARNING: type inference failed for: r12v7 */
    /* JADX WARNING: type inference failed for: r7v4 */
    /* JADX WARNING: type inference failed for: r12v8 */
    /* JADX WARNING: type inference failed for: r3v18 */
    /* JADX WARNING: type inference failed for: r7v5 */
    /* JADX WARNING: type inference failed for: r7v6 */
    /* JADX WARNING: type inference failed for: r7v7 */
    /* JADX WARNING: type inference failed for: r3v19 */
    /* JADX WARNING: type inference failed for: r9v6 */
    /* JADX WARNING: type inference failed for: r12v9 */
    /* JADX WARNING: type inference failed for: r9v7 */
    /* JADX WARNING: type inference failed for: r9v8 */
    /* JADX WARNING: type inference failed for: r9v9 */
    /* JADX WARNING: type inference failed for: r12v10 */
    /* JADX WARNING: type inference failed for: r12v11 */
    /* JADX WARNING: type inference failed for: r12v12 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r3v1, types: [int, boolean]
      assigns: []
      uses: [?[int, short, byte, char], int, boolean]
      mth insns count: 157
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00d3 A[Catch:{ all -> 0x0104 }] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00fb  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0100  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x011d  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0122  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0130  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x0135  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0125 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x0125 A[SYNTHETIC] */
    /* JADX WARNING: Unknown variable types count: 13 */
    @android.support.annotation.WorkerThread
    @android.annotation.TargetApi(11)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean zzb(int r18, byte[] r19) {
        /*
            r17 = this;
            r1 = r17
            r17.zzve()
            boolean r2 = r1.zzjbo
            r3 = 0
            if (r2 == 0) goto L_0x000b
            return r3
        L_0x000b:
            android.content.ContentValues r2 = new android.content.ContentValues
            r2.<init>()
            java.lang.String r4 = "type"
            java.lang.Integer r5 = java.lang.Integer.valueOf(r18)
            r2.put(r4, r5)
            java.lang.String r4 = "entry"
            r5 = r19
            r2.put(r4, r5)
            r4 = 5
            r5 = 0
            r6 = 5
        L_0x0023:
            if (r5 >= r4) goto L_0x0139
            r7 = 0
            r8 = 1
            android.database.sqlite.SQLiteDatabase r9 = r17.getWritableDatabase()     // Catch:{ SQLiteFullException -> 0x0108, SQLiteException -> 0x00ca, all -> 0x00c4 }
            if (r9 != 0) goto L_0x003f
            r1.zzjbo = r8     // Catch:{ SQLiteFullException -> 0x003b, SQLiteException -> 0x0035 }
            if (r9 == 0) goto L_0x0034
            r9.close()
        L_0x0034:
            return r3
        L_0x0035:
            r0 = move-exception
            r3 = r0
            r12 = r7
        L_0x0038:
            r7 = r9
            goto L_0x00cd
        L_0x003b:
            r0 = move-exception
            r3 = r0
            goto L_0x010b
        L_0x003f:
            r9.beginTransaction()     // Catch:{ SQLiteFullException -> 0x003b, SQLiteException -> 0x0035 }
            r10 = 0
            java.lang.String r12 = "select count(1) from messages"
            android.database.Cursor r12 = r9.rawQuery(r12, r7)     // Catch:{ SQLiteFullException -> 0x003b, SQLiteException -> 0x0035 }
            if (r12 == 0) goto L_0x0063
            boolean r13 = r12.moveToFirst()     // Catch:{ SQLiteFullException -> 0x005e, SQLiteException -> 0x005b, all -> 0x0057 }
            if (r13 == 0) goto L_0x0063
            long r10 = r12.getLong(r3)     // Catch:{ SQLiteFullException -> 0x005e, SQLiteException -> 0x005b, all -> 0x0057 }
            goto L_0x0063
        L_0x0057:
            r0 = move-exception
            r2 = r0
            goto L_0x012e
        L_0x005b:
            r0 = move-exception
            r3 = r0
            goto L_0x0038
        L_0x005e:
            r0 = move-exception
            r3 = r0
            r7 = r12
            goto L_0x010b
        L_0x0063:
            r13 = 100000(0x186a0, double:4.94066E-319)
            int r15 = (r10 > r13 ? 1 : (r10 == r13 ? 0 : -1))
            if (r15 < 0) goto L_0x00ad
            com.google.android.gms.internal.zzchm r15 = r17.zzawy()     // Catch:{ SQLiteFullException -> 0x005e, SQLiteException -> 0x005b, all -> 0x0057 }
            com.google.android.gms.internal.zzcho r15 = r15.zzazd()     // Catch:{ SQLiteFullException -> 0x005e, SQLiteException -> 0x005b, all -> 0x0057 }
            java.lang.String r4 = "Data loss, local db full"
            r15.log(r4)     // Catch:{ SQLiteFullException -> 0x005e, SQLiteException -> 0x005b, all -> 0x0057 }
            r4 = 0
            long r13 = r13 - r10
            r10 = 1
            long r13 = r13 + r10
            java.lang.String r4 = "messages"
            java.lang.String r10 = "rowid in (select rowid from messages order by rowid asc limit ?)"
            java.lang.String[] r11 = new java.lang.String[r8]     // Catch:{ SQLiteFullException -> 0x005e, SQLiteException -> 0x005b, all -> 0x0057 }
            java.lang.String r15 = java.lang.Long.toString(r13)     // Catch:{ SQLiteFullException -> 0x005e, SQLiteException -> 0x005b, all -> 0x0057 }
            r11[r3] = r15     // Catch:{ SQLiteFullException -> 0x005e, SQLiteException -> 0x005b, all -> 0x0057 }
            int r4 = r9.delete(r4, r10, r11)     // Catch:{ SQLiteFullException -> 0x005e, SQLiteException -> 0x005b, all -> 0x0057 }
            long r10 = (long) r4     // Catch:{ SQLiteFullException -> 0x005e, SQLiteException -> 0x005b, all -> 0x0057 }
            int r4 = (r10 > r13 ? 1 : (r10 == r13 ? 0 : -1))
            if (r4 == 0) goto L_0x00ad
            com.google.android.gms.internal.zzchm r4 = r17.zzawy()     // Catch:{ SQLiteFullException -> 0x005e, SQLiteException -> 0x005b, all -> 0x0057 }
            com.google.android.gms.internal.zzcho r4 = r4.zzazd()     // Catch:{ SQLiteFullException -> 0x005e, SQLiteException -> 0x005b, all -> 0x0057 }
            java.lang.String r15 = "Different delete count than expected in local db. expected, received, difference"
            java.lang.Long r3 = java.lang.Long.valueOf(r13)     // Catch:{ SQLiteFullException -> 0x005e, SQLiteException -> 0x005b, all -> 0x0057 }
            java.lang.Long r8 = java.lang.Long.valueOf(r10)     // Catch:{ SQLiteFullException -> 0x005e, SQLiteException -> 0x005b, all -> 0x0057 }
            r16 = 0
            long r13 = r13 - r10
            java.lang.Long r10 = java.lang.Long.valueOf(r13)     // Catch:{ SQLiteFullException -> 0x005e, SQLiteException -> 0x005b, all -> 0x0057 }
            r4.zzd(r15, r3, r8, r10)     // Catch:{ SQLiteFullException -> 0x005e, SQLiteException -> 0x005b, all -> 0x0057 }
        L_0x00ad:
            java.lang.String r3 = "messages"
            r9.insertOrThrow(r3, r7, r2)     // Catch:{ SQLiteFullException -> 0x005e, SQLiteException -> 0x005b, all -> 0x0057 }
            r9.setTransactionSuccessful()     // Catch:{ SQLiteFullException -> 0x005e, SQLiteException -> 0x005b, all -> 0x0057 }
            r9.endTransaction()     // Catch:{ SQLiteFullException -> 0x005e, SQLiteException -> 0x005b, all -> 0x0057 }
            if (r12 == 0) goto L_0x00bd
            r12.close()
        L_0x00bd:
            if (r9 == 0) goto L_0x00c2
            r9.close()
        L_0x00c2:
            r2 = 1
            return r2
        L_0x00c4:
            r0 = move-exception
            r2 = r0
            r9 = r7
            r12 = r9
            goto L_0x012e
        L_0x00ca:
            r0 = move-exception
            r3 = r0
            r12 = r7
        L_0x00cd:
            int r4 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x0104 }
            r8 = 11
            if (r4 < r8) goto L_0x00de
            boolean r4 = r3 instanceof android.database.sqlite.SQLiteDatabaseLockedException     // Catch:{ all -> 0x0104 }
            if (r4 == 0) goto L_0x00de
            long r3 = (long) r6     // Catch:{ all -> 0x0104 }
            android.os.SystemClock.sleep(r3)     // Catch:{ all -> 0x0104 }
            int r6 = r6 + 20
            goto L_0x00f9
        L_0x00de:
            if (r7 == 0) goto L_0x00e9
            boolean r4 = r7.inTransaction()     // Catch:{ all -> 0x0104 }
            if (r4 == 0) goto L_0x00e9
            r7.endTransaction()     // Catch:{ all -> 0x0104 }
        L_0x00e9:
            com.google.android.gms.internal.zzchm r4 = r17.zzawy()     // Catch:{ all -> 0x0104 }
            com.google.android.gms.internal.zzcho r4 = r4.zzazd()     // Catch:{ all -> 0x0104 }
            java.lang.String r8 = "Error writing entry to local database"
            r4.zzj(r8, r3)     // Catch:{ all -> 0x0104 }
            r3 = 1
            r1.zzjbo = r3     // Catch:{ all -> 0x0104 }
        L_0x00f9:
            if (r12 == 0) goto L_0x00fe
            r12.close()
        L_0x00fe:
            if (r7 == 0) goto L_0x0125
            r7.close()
            goto L_0x0125
        L_0x0104:
            r0 = move-exception
            r2 = r0
            r9 = r7
            goto L_0x012e
        L_0x0108:
            r0 = move-exception
            r3 = r0
            r9 = r7
        L_0x010b:
            com.google.android.gms.internal.zzchm r4 = r17.zzawy()     // Catch:{ all -> 0x012b }
            com.google.android.gms.internal.zzcho r4 = r4.zzazd()     // Catch:{ all -> 0x012b }
            java.lang.String r8 = "Error writing entry to local database"
            r4.zzj(r8, r3)     // Catch:{ all -> 0x012b }
            r3 = 1
            r1.zzjbo = r3     // Catch:{ all -> 0x012b }
            if (r7 == 0) goto L_0x0120
            r7.close()
        L_0x0120:
            if (r9 == 0) goto L_0x0125
            r9.close()
        L_0x0125:
            int r5 = r5 + 1
            r3 = 0
            r4 = 5
            goto L_0x0023
        L_0x012b:
            r0 = move-exception
            r2 = r0
            r12 = r7
        L_0x012e:
            if (r12 == 0) goto L_0x0133
            r12.close()
        L_0x0133:
            if (r9 == 0) goto L_0x0138
            r9.close()
        L_0x0138:
            throw r2
        L_0x0139:
            com.google.android.gms.internal.zzchm r2 = r17.zzawy()
            com.google.android.gms.internal.zzcho r2 = r2.zzazf()
            java.lang.String r3 = "Failed to write entry to local database"
            r2.log(r3)
            r2 = 0
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzchi.zzb(int, byte[]):boolean");
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @WorkerThread
    public final void resetAnalyticsData() {
        zzve();
        try {
            int delete = getWritableDatabase().delete("messages", null, null) + 0;
            if (delete > 0) {
                zzawy().zzazj().zzj("Reset local analytics data. records", Integer.valueOf(delete));
            }
        } catch (SQLiteException e) {
            zzawy().zzazd().zzj("Error resetting local analytics data. error", e);
        }
    }

    public final boolean zza(zzcha zzcha) {
        Parcel obtain = Parcel.obtain();
        zzcha.writeToParcel(obtain, 0);
        byte[] marshall = obtain.marshall();
        obtain.recycle();
        if (marshall.length <= 131072) {
            return zzb(0, marshall);
        }
        zzawy().zzazf().log("Event is too long for local database. Sending event directly to service");
        return false;
    }

    public final boolean zza(zzcln zzcln) {
        Parcel obtain = Parcel.obtain();
        zzcln.writeToParcel(obtain, 0);
        byte[] marshall = obtain.marshall();
        obtain.recycle();
        if (marshall.length <= 131072) {
            return zzb(1, marshall);
        }
        zzawy().zzazf().log("User property too long for local database. Sending directly to service");
        return false;
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

    public final boolean zzc(zzcgl zzcgl) {
        zzawu();
        byte[] zza = zzclq.zza((Parcelable) zzcgl);
        if (zza.length <= 131072) {
            return zzb(2, zza);
        }
        zzawy().zzazf().log("Conditional user property too long for local database. Sending directly to service");
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:121:0x0198 A[Catch:{ all -> 0x01e8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:130:0x01bf  */
    /* JADX WARNING: Removed duplicated region for block: B:138:0x01dd  */
    /* JADX WARNING: Removed duplicated region for block: B:145:0x01ed  */
    /* JADX WARNING: Removed duplicated region for block: B:147:0x01f2  */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x01e3 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:156:0x01e3 A[SYNTHETIC] */
    @android.annotation.TargetApi(11)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.List<com.google.android.gms.internal.zzbfm> zzeb(int r21) {
        /*
            r20 = this;
            r1 = r20
            r20.zzve()
            boolean r2 = r1.zzjbo
            r3 = 0
            if (r2 == 0) goto L_0x000b
            return r3
        L_0x000b:
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            android.content.Context r4 = r20.getContext()
            java.lang.String r5 = "google_app_measurement_local.db"
            java.io.File r4 = r4.getDatabasePath(r5)
            boolean r4 = r4.exists()
            if (r4 != 0) goto L_0x0021
            return r2
        L_0x0021:
            r4 = 5
            r5 = 0
            r6 = 0
            r7 = 5
        L_0x0025:
            if (r6 >= r4) goto L_0x01f6
            r8 = 1
            android.database.sqlite.SQLiteDatabase r15 = r20.getWritableDatabase()     // Catch:{ SQLiteFullException -> 0x01c8, SQLiteException -> 0x018e, all -> 0x0189 }
            if (r15 != 0) goto L_0x0045
            r1.zzjbo = r8     // Catch:{ SQLiteFullException -> 0x0040, SQLiteException -> 0x003b, all -> 0x0036 }
            if (r15 == 0) goto L_0x0035
            r15.close()
        L_0x0035:
            return r3
        L_0x0036:
            r0 = move-exception
            r2 = r0
            r9 = r3
            goto L_0x01ea
        L_0x003b:
            r0 = move-exception
            r4 = r0
            r9 = r3
            goto L_0x0192
        L_0x0040:
            r0 = move-exception
            r4 = r0
            r9 = r3
            goto L_0x01cc
        L_0x0045:
            r15.beginTransaction()     // Catch:{ SQLiteFullException -> 0x0184, SQLiteException -> 0x017f, all -> 0x0179 }
            java.lang.String r10 = "messages"
            java.lang.String r9 = "rowid"
            java.lang.String r11 = "type"
            java.lang.String r12 = "entry"
            java.lang.String[] r11 = new java.lang.String[]{r9, r11, r12}     // Catch:{ SQLiteFullException -> 0x0184, SQLiteException -> 0x017f, all -> 0x0179 }
            r12 = 0
            r13 = 0
            r14 = 0
            r16 = 0
            java.lang.String r17 = "rowid asc"
            r9 = 100
            java.lang.String r18 = java.lang.Integer.toString(r9)     // Catch:{ SQLiteFullException -> 0x0184, SQLiteException -> 0x017f, all -> 0x0179 }
            r9 = r15
            r4 = r15
            r15 = r16
            r16 = r17
            r17 = r18
            android.database.Cursor r9 = r9.query(r10, r11, r12, r13, r14, r15, r16, r17)     // Catch:{ SQLiteFullException -> 0x0175, SQLiteException -> 0x0171, all -> 0x016f }
            r10 = -1
        L_0x006f:
            boolean r12 = r9.moveToNext()     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            if (r12 == 0) goto L_0x0133
            long r10 = r9.getLong(r5)     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            int r12 = r9.getInt(r8)     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            r13 = 2
            byte[] r14 = r9.getBlob(r13)     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            if (r12 != 0) goto L_0x00b9
            android.os.Parcel r12 = android.os.Parcel.obtain()     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            int r13 = r14.length     // Catch:{ zzbfo -> 0x00a3 }
            r12.unmarshall(r14, r5, r13)     // Catch:{ zzbfo -> 0x00a3 }
            r12.setDataPosition(r5)     // Catch:{ zzbfo -> 0x00a3 }
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcha> r13 = com.google.android.gms.internal.zzcha.CREATOR     // Catch:{ zzbfo -> 0x00a3 }
            java.lang.Object r13 = r13.createFromParcel(r12)     // Catch:{ zzbfo -> 0x00a3 }
            com.google.android.gms.internal.zzcha r13 = (com.google.android.gms.internal.zzcha) r13     // Catch:{ zzbfo -> 0x00a3 }
            r12.recycle()     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            if (r13 == 0) goto L_0x006f
        L_0x009c:
            r2.add(r13)     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            goto L_0x006f
        L_0x00a0:
            r0 = move-exception
            r10 = r0
            goto L_0x00b5
        L_0x00a3:
            r0 = move-exception
            com.google.android.gms.internal.zzchm r13 = r20.zzawy()     // Catch:{ all -> 0x00a0 }
            com.google.android.gms.internal.zzcho r13 = r13.zzazd()     // Catch:{ all -> 0x00a0 }
            java.lang.String r14 = "Failed to load event from local database"
            r13.log(r14)     // Catch:{ all -> 0x00a0 }
            r12.recycle()     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            goto L_0x006f
        L_0x00b5:
            r12.recycle()     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            throw r10     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
        L_0x00b9:
            if (r12 != r8) goto L_0x00ee
            android.os.Parcel r12 = android.os.Parcel.obtain()     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            int r13 = r14.length     // Catch:{ zzbfo -> 0x00d5 }
            r12.unmarshall(r14, r5, r13)     // Catch:{ zzbfo -> 0x00d5 }
            r12.setDataPosition(r5)     // Catch:{ zzbfo -> 0x00d5 }
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcln> r13 = com.google.android.gms.internal.zzcln.CREATOR     // Catch:{ zzbfo -> 0x00d5 }
            java.lang.Object r13 = r13.createFromParcel(r12)     // Catch:{ zzbfo -> 0x00d5 }
            com.google.android.gms.internal.zzcln r13 = (com.google.android.gms.internal.zzcln) r13     // Catch:{ zzbfo -> 0x00d5 }
            r12.recycle()     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            goto L_0x00e7
        L_0x00d2:
            r0 = move-exception
            r10 = r0
            goto L_0x00ea
        L_0x00d5:
            r0 = move-exception
            com.google.android.gms.internal.zzchm r13 = r20.zzawy()     // Catch:{ all -> 0x00d2 }
            com.google.android.gms.internal.zzcho r13 = r13.zzazd()     // Catch:{ all -> 0x00d2 }
            java.lang.String r14 = "Failed to load user property from local database"
            r13.log(r14)     // Catch:{ all -> 0x00d2 }
            r12.recycle()     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            r13 = r3
        L_0x00e7:
            if (r13 == 0) goto L_0x006f
            goto L_0x009c
        L_0x00ea:
            r12.recycle()     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            throw r10     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
        L_0x00ee:
            if (r12 != r13) goto L_0x0124
            android.os.Parcel r12 = android.os.Parcel.obtain()     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            int r13 = r14.length     // Catch:{ zzbfo -> 0x010a }
            r12.unmarshall(r14, r5, r13)     // Catch:{ zzbfo -> 0x010a }
            r12.setDataPosition(r5)     // Catch:{ zzbfo -> 0x010a }
            android.os.Parcelable$Creator<com.google.android.gms.internal.zzcgl> r13 = com.google.android.gms.internal.zzcgl.CREATOR     // Catch:{ zzbfo -> 0x010a }
            java.lang.Object r13 = r13.createFromParcel(r12)     // Catch:{ zzbfo -> 0x010a }
            com.google.android.gms.internal.zzcgl r13 = (com.google.android.gms.internal.zzcgl) r13     // Catch:{ zzbfo -> 0x010a }
            r12.recycle()     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            goto L_0x011c
        L_0x0107:
            r0 = move-exception
            r10 = r0
            goto L_0x0120
        L_0x010a:
            r0 = move-exception
            com.google.android.gms.internal.zzchm r13 = r20.zzawy()     // Catch:{ all -> 0x0107 }
            com.google.android.gms.internal.zzcho r13 = r13.zzazd()     // Catch:{ all -> 0x0107 }
            java.lang.String r14 = "Failed to load user property from local database"
            r13.log(r14)     // Catch:{ all -> 0x0107 }
            r12.recycle()     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            r13 = r3
        L_0x011c:
            if (r13 == 0) goto L_0x006f
            goto L_0x009c
        L_0x0120:
            r12.recycle()     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            throw r10     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
        L_0x0124:
            com.google.android.gms.internal.zzchm r12 = r20.zzawy()     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            com.google.android.gms.internal.zzcho r12 = r12.zzazd()     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            java.lang.String r13 = "Unknown record type in local database"
            r12.log(r13)     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            goto L_0x006f
        L_0x0133:
            java.lang.String r12 = "messages"
            java.lang.String r13 = "rowid <= ?"
            java.lang.String[] r14 = new java.lang.String[r8]     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            java.lang.String r10 = java.lang.Long.toString(r10)     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            r14[r5] = r10     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            int r10 = r4.delete(r12, r13, r14)     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            int r11 = r2.size()     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            if (r10 >= r11) goto L_0x0156
            com.google.android.gms.internal.zzchm r10 = r20.zzawy()     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            com.google.android.gms.internal.zzcho r10 = r10.zzazd()     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            java.lang.String r11 = "Fewer entries removed from local database than expected"
            r10.log(r11)     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
        L_0x0156:
            r4.setTransactionSuccessful()     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            r4.endTransaction()     // Catch:{ SQLiteFullException -> 0x016d, SQLiteException -> 0x016b, all -> 0x0167 }
            if (r9 == 0) goto L_0x0161
            r9.close()
        L_0x0161:
            if (r4 == 0) goto L_0x0166
            r4.close()
        L_0x0166:
            return r2
        L_0x0167:
            r0 = move-exception
            r2 = r0
            goto L_0x01eb
        L_0x016b:
            r0 = move-exception
            goto L_0x0173
        L_0x016d:
            r0 = move-exception
            goto L_0x0177
        L_0x016f:
            r0 = move-exception
            goto L_0x017b
        L_0x0171:
            r0 = move-exception
            r9 = r3
        L_0x0173:
            r15 = r4
            goto L_0x0182
        L_0x0175:
            r0 = move-exception
            r9 = r3
        L_0x0177:
            r15 = r4
            goto L_0x0187
        L_0x0179:
            r0 = move-exception
            r4 = r15
        L_0x017b:
            r2 = r0
            r9 = r3
            goto L_0x01eb
        L_0x017f:
            r0 = move-exception
            r4 = r15
            r9 = r3
        L_0x0182:
            r4 = r0
            goto L_0x0192
        L_0x0184:
            r0 = move-exception
            r4 = r15
            r9 = r3
        L_0x0187:
            r4 = r0
            goto L_0x01cc
        L_0x0189:
            r0 = move-exception
            r2 = r0
            r4 = r3
            r9 = r4
            goto L_0x01eb
        L_0x018e:
            r0 = move-exception
            r4 = r0
            r9 = r3
            r15 = r9
        L_0x0192:
            int r10 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x01e8 }
            r11 = 11
            if (r10 < r11) goto L_0x01a3
            boolean r10 = r4 instanceof android.database.sqlite.SQLiteDatabaseLockedException     // Catch:{ all -> 0x01e8 }
            if (r10 == 0) goto L_0x01a3
            long r10 = (long) r7     // Catch:{ all -> 0x01e8 }
            android.os.SystemClock.sleep(r10)     // Catch:{ all -> 0x01e8 }
            int r7 = r7 + 20
            goto L_0x01bd
        L_0x01a3:
            if (r15 == 0) goto L_0x01ae
            boolean r10 = r15.inTransaction()     // Catch:{ all -> 0x01e8 }
            if (r10 == 0) goto L_0x01ae
            r15.endTransaction()     // Catch:{ all -> 0x01e8 }
        L_0x01ae:
            com.google.android.gms.internal.zzchm r10 = r20.zzawy()     // Catch:{ all -> 0x01e8 }
            com.google.android.gms.internal.zzcho r10 = r10.zzazd()     // Catch:{ all -> 0x01e8 }
            java.lang.String r11 = "Error reading entries from local database"
            r10.zzj(r11, r4)     // Catch:{ all -> 0x01e8 }
            r1.zzjbo = r8     // Catch:{ all -> 0x01e8 }
        L_0x01bd:
            if (r9 == 0) goto L_0x01c2
            r9.close()
        L_0x01c2:
            if (r15 == 0) goto L_0x01e3
        L_0x01c4:
            r15.close()
            goto L_0x01e3
        L_0x01c8:
            r0 = move-exception
            r4 = r0
            r9 = r3
            r15 = r9
        L_0x01cc:
            com.google.android.gms.internal.zzchm r10 = r20.zzawy()     // Catch:{ all -> 0x01e8 }
            com.google.android.gms.internal.zzcho r10 = r10.zzazd()     // Catch:{ all -> 0x01e8 }
            java.lang.String r11 = "Error reading entries from local database"
            r10.zzj(r11, r4)     // Catch:{ all -> 0x01e8 }
            r1.zzjbo = r8     // Catch:{ all -> 0x01e8 }
            if (r9 == 0) goto L_0x01e0
            r9.close()
        L_0x01e0:
            if (r15 == 0) goto L_0x01e3
            goto L_0x01c4
        L_0x01e3:
            int r6 = r6 + 1
            r4 = 5
            goto L_0x0025
        L_0x01e8:
            r0 = move-exception
            r2 = r0
        L_0x01ea:
            r4 = r15
        L_0x01eb:
            if (r9 == 0) goto L_0x01f0
            r9.close()
        L_0x01f0:
            if (r4 == 0) goto L_0x01f5
            r4.close()
        L_0x01f5:
            throw r2
        L_0x01f6:
            com.google.android.gms.internal.zzchm r2 = r20.zzawy()
            com.google.android.gms.internal.zzcho r2 = r2.zzazf()
            java.lang.String r4 = "Failed to read events from database in reasonable time"
            r2.log(r4)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzchi.zzeb(int):java.util.List");
    }

    public final /* bridge */ /* synthetic */ void zzve() {
        super.zzve();
    }

    public final /* bridge */ /* synthetic */ zzd zzws() {
        return super.zzws();
    }
}
