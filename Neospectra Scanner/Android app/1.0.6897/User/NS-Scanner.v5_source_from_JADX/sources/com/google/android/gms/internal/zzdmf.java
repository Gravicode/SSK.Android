package com.google.android.gms.internal;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class zzdmf {
    private static Uri CONTENT_URI = Uri.parse("content://com.google.android.gsf.gservices");
    private static Uri zzlnb = Uri.parse("content://com.google.android.gsf.gservices/prefix");
    private static Pattern zzlnc = Pattern.compile("^(1|true|t|on|yes|y)$", 2);
    private static Pattern zzlnd = Pattern.compile("^(0|false|f|off|no|n)$", 2);
    /* access modifiers changed from: private */
    public static final AtomicBoolean zzlne = new AtomicBoolean();
    private static HashMap<String, String> zzlnf;
    private static HashMap<String, Boolean> zzlng = new HashMap<>();
    private static HashMap<String, Integer> zzlnh = new HashMap<>();
    private static HashMap<String, Long> zzlni = new HashMap<>();
    private static HashMap<String, Float> zzlnj = new HashMap<>();
    private static Object zzlnk;
    private static boolean zzlnl;
    private static String[] zzlnm = new String[0];

    public static long getLong(ContentResolver contentResolver, String str, long j) {
        Object zzb = zzb(contentResolver);
        long j2 = 0;
        Long l = (Long) zza(zzlni, str, (T) Long.valueOf(0));
        if (l != null) {
            return l.longValue();
        }
        String zza = zza(contentResolver, str, (String) null);
        if (zza != null) {
            try {
                long parseLong = Long.parseLong(zza);
                l = Long.valueOf(parseLong);
                j2 = parseLong;
            } catch (NumberFormatException e) {
            }
        }
        zza(zzb, zzlni, str, l);
        return j2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0012, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static <T> T zza(java.util.HashMap<java.lang.String, T> r2, java.lang.String r3, T r4) {
        /*
            java.lang.Class<com.google.android.gms.internal.zzdmf> r0 = com.google.android.gms.internal.zzdmf.class
            monitor-enter(r0)
            boolean r1 = r2.containsKey(r3)     // Catch:{ all -> 0x0016 }
            if (r1 == 0) goto L_0x0013
            java.lang.Object r2 = r2.get(r3)     // Catch:{ all -> 0x0016 }
            if (r2 == 0) goto L_0x0010
            goto L_0x0011
        L_0x0010:
            r2 = r4
        L_0x0011:
            monitor-exit(r0)     // Catch:{ all -> 0x0016 }
            return r2
        L_0x0013:
            monitor-exit(r0)     // Catch:{ all -> 0x0016 }
            r2 = 0
            return r2
        L_0x0016:
            r2 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0016 }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzdmf.zza(java.util.HashMap, java.lang.String, java.lang.Object):java.lang.Object");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001e, code lost:
        return r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x005d, code lost:
        return r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x005f, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0064, code lost:
        r13 = r13.query(CONTENT_URI, null, null, new java.lang.String[]{r14}, null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0072, code lost:
        if (r13 == null) goto L_0x0097;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0078, code lost:
        if (r13.moveToFirst() != false) goto L_0x007b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x007b, code lost:
        r15 = r13.getString(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x007f, code lost:
        if (r15 == null) goto L_0x0088;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0085, code lost:
        if (r15.equals(null) == false) goto L_0x0088;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0087, code lost:
        r15 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0088, code lost:
        zza(r0, r14, r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x008b, code lost:
        if (r15 == null) goto L_0x008e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x008e, code lost:
        r15 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x008f, code lost:
        if (r13 == null) goto L_0x0094;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0091, code lost:
        r13.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0094, code lost:
        return r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0095, code lost:
        r14 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:?, code lost:
        zza(r0, r14, (java.lang.String) null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x009a, code lost:
        if (r13 == null) goto L_0x009f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x009c, code lost:
        r13.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x009f, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00a0, code lost:
        if (r13 != null) goto L_0x00a2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00a2, code lost:
        r13.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00a5, code lost:
        throw r14;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String zza(android.content.ContentResolver r13, java.lang.String r14, java.lang.String r15) {
        /*
            java.lang.Class<com.google.android.gms.internal.zzdmf> r15 = com.google.android.gms.internal.zzdmf.class
            monitor-enter(r15)
            zza(r13)     // Catch:{ all -> 0x00a6 }
            java.lang.Object r0 = zzlnk     // Catch:{ all -> 0x00a6 }
            java.util.HashMap<java.lang.String, java.lang.String> r1 = zzlnf     // Catch:{ all -> 0x00a6 }
            boolean r1 = r1.containsKey(r14)     // Catch:{ all -> 0x00a6 }
            r2 = 0
            if (r1 == 0) goto L_0x001f
            java.util.HashMap<java.lang.String, java.lang.String> r13 = zzlnf     // Catch:{ all -> 0x00a6 }
            java.lang.Object r13 = r13.get(r14)     // Catch:{ all -> 0x00a6 }
            java.lang.String r13 = (java.lang.String) r13     // Catch:{ all -> 0x00a6 }
            if (r13 == 0) goto L_0x001c
            goto L_0x001d
        L_0x001c:
            r13 = r2
        L_0x001d:
            monitor-exit(r15)     // Catch:{ all -> 0x00a6 }
            return r13
        L_0x001f:
            java.lang.String[] r1 = zzlnm     // Catch:{ all -> 0x00a6 }
            int r3 = r1.length     // Catch:{ all -> 0x00a6 }
            r4 = 0
            r5 = 0
        L_0x0024:
            r6 = 1
            if (r5 >= r3) goto L_0x0063
            r7 = r1[r5]     // Catch:{ all -> 0x00a6 }
            boolean r7 = r14.startsWith(r7)     // Catch:{ all -> 0x00a6 }
            if (r7 == 0) goto L_0x0060
            boolean r0 = zzlnl     // Catch:{ all -> 0x00a6 }
            if (r0 == 0) goto L_0x003b
            java.util.HashMap<java.lang.String, java.lang.String> r0 = zzlnf     // Catch:{ all -> 0x00a6 }
            boolean r0 = r0.isEmpty()     // Catch:{ all -> 0x00a6 }
            if (r0 == 0) goto L_0x005e
        L_0x003b:
            java.lang.String[] r0 = zzlnm     // Catch:{ all -> 0x00a6 }
            java.util.HashMap<java.lang.String, java.lang.String> r1 = zzlnf     // Catch:{ all -> 0x00a6 }
            java.util.Map r13 = zza(r13, r0)     // Catch:{ all -> 0x00a6 }
            r1.putAll(r13)     // Catch:{ all -> 0x00a6 }
            zzlnl = r6     // Catch:{ all -> 0x00a6 }
            java.util.HashMap<java.lang.String, java.lang.String> r13 = zzlnf     // Catch:{ all -> 0x00a6 }
            boolean r13 = r13.containsKey(r14)     // Catch:{ all -> 0x00a6 }
            if (r13 == 0) goto L_0x005e
            java.util.HashMap<java.lang.String, java.lang.String> r13 = zzlnf     // Catch:{ all -> 0x00a6 }
            java.lang.Object r13 = r13.get(r14)     // Catch:{ all -> 0x00a6 }
            java.lang.String r13 = (java.lang.String) r13     // Catch:{ all -> 0x00a6 }
            if (r13 == 0) goto L_0x005b
            goto L_0x005c
        L_0x005b:
            r13 = r2
        L_0x005c:
            monitor-exit(r15)     // Catch:{ all -> 0x00a6 }
            return r13
        L_0x005e:
            monitor-exit(r15)     // Catch:{ all -> 0x00a6 }
            return r2
        L_0x0060:
            int r5 = r5 + 1
            goto L_0x0024
        L_0x0063:
            monitor-exit(r15)     // Catch:{ all -> 0x00a6 }
            android.net.Uri r8 = CONTENT_URI
            r9 = 0
            r10 = 0
            java.lang.String[] r11 = new java.lang.String[r6]
            r11[r4] = r14
            r12 = 0
            r7 = r13
            android.database.Cursor r13 = r7.query(r8, r9, r10, r11, r12)
            if (r13 == 0) goto L_0x0097
            boolean r15 = r13.moveToFirst()     // Catch:{ all -> 0x0095 }
            if (r15 != 0) goto L_0x007b
            goto L_0x0097
        L_0x007b:
            java.lang.String r15 = r13.getString(r6)     // Catch:{ all -> 0x0095 }
            if (r15 == 0) goto L_0x0088
            boolean r1 = r15.equals(r2)     // Catch:{ all -> 0x0095 }
            if (r1 == 0) goto L_0x0088
            r15 = r2
        L_0x0088:
            zza(r0, r14, r15)     // Catch:{ all -> 0x0095 }
            if (r15 == 0) goto L_0x008e
            goto L_0x008f
        L_0x008e:
            r15 = r2
        L_0x008f:
            if (r13 == 0) goto L_0x0094
            r13.close()
        L_0x0094:
            return r15
        L_0x0095:
            r14 = move-exception
            goto L_0x00a0
        L_0x0097:
            zza(r0, r14, r2)     // Catch:{ all -> 0x0095 }
            if (r13 == 0) goto L_0x009f
            r13.close()
        L_0x009f:
            return r2
        L_0x00a0:
            if (r13 == 0) goto L_0x00a5
            r13.close()
        L_0x00a5:
            throw r14
        L_0x00a6:
            r13 = move-exception
            monitor-exit(r15)     // Catch:{ all -> 0x00a6 }
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzdmf.zza(android.content.ContentResolver, java.lang.String, java.lang.String):java.lang.String");
    }

    private static Map<String, String> zza(ContentResolver contentResolver, String... strArr) {
        Cursor query = contentResolver.query(zzlnb, null, null, strArr, null);
        TreeMap treeMap = new TreeMap();
        if (query == null) {
            return treeMap;
        }
        while (query.moveToNext()) {
            try {
                treeMap.put(query.getString(0), query.getString(1));
            } finally {
                query.close();
            }
        }
        return treeMap;
    }

    private static void zza(ContentResolver contentResolver) {
        if (zzlnf == null) {
            zzlne.set(false);
            zzlnf = new HashMap<>();
            zzlnk = new Object();
            zzlnl = false;
            contentResolver.registerContentObserver(CONTENT_URI, true, new zzdmg(null));
            return;
        }
        if (zzlne.getAndSet(false)) {
            zzlnf.clear();
            zzlng.clear();
            zzlnh.clear();
            zzlni.clear();
            zzlnj.clear();
            zzlnk = new Object();
            zzlnl = false;
        }
    }

    private static void zza(Object obj, String str, String str2) {
        synchronized (zzdmf.class) {
            if (obj == zzlnk) {
                zzlnf.put(str, str2);
            }
        }
    }

    private static <T> void zza(Object obj, HashMap<String, T> hashMap, String str, T t) {
        synchronized (zzdmf.class) {
            if (obj == zzlnk) {
                hashMap.put(str, t);
                zzlnf.remove(str);
            }
        }
    }

    public static boolean zza(ContentResolver contentResolver, String str, boolean z) {
        Object zzb = zzb(contentResolver);
        Boolean bool = (Boolean) zza(zzlng, str, (T) Boolean.valueOf(z));
        if (bool != null) {
            return bool.booleanValue();
        }
        String zza = zza(contentResolver, str, (String) null);
        if (zza != null && !zza.equals("")) {
            if (zzlnc.matcher(zza).matches()) {
                bool = Boolean.valueOf(true);
                z = true;
            } else if (zzlnd.matcher(zza).matches()) {
                bool = Boolean.valueOf(false);
                z = false;
            } else {
                StringBuilder sb = new StringBuilder("attempt to read gservices key ");
                sb.append(str);
                sb.append(" (value \"");
                sb.append(zza);
                sb.append("\") as boolean");
                Log.w("Gservices", sb.toString());
            }
        }
        zza(zzb, zzlng, str, bool);
        return z;
    }

    private static Object zzb(ContentResolver contentResolver) {
        Object obj;
        synchronized (zzdmf.class) {
            zza(contentResolver);
            obj = zzlnk;
        }
        return obj;
    }
}
