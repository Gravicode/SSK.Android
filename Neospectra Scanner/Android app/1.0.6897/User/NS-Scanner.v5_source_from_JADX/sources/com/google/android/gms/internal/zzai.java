package com.google.android.gms.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;

public class zzai implements zzm {
    private static boolean DEBUG = zzae.DEBUG;
    @Deprecated
    private zzaq zzbp;
    private final zzah zzbq;
    private zzaj zzbr;

    public zzai(zzah zzah) {
        this(zzah, new zzaj(4096));
    }

    private zzai(zzah zzah, zzaj zzaj) {
        this.zzbq = zzah;
        this.zzbp = zzah;
        this.zzbr = zzaj;
    }

    @Deprecated
    public zzai(zzaq zzaq) {
        this(zzaq, new zzaj(4096));
    }

    @Deprecated
    private zzai(zzaq zzaq, zzaj zzaj) {
        this.zzbp = zzaq;
        this.zzbq = new zzag(zzaq);
        this.zzbr = zzaj;
    }

    private static List<zzl> zza(List<zzl> list, zzc zzc) {
        TreeSet treeSet = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        if (!list.isEmpty()) {
            for (zzl name : list) {
                treeSet.add(name.getName());
            }
        }
        ArrayList arrayList = new ArrayList(list);
        if (zzc.zzg != null) {
            if (!zzc.zzg.isEmpty()) {
                for (zzl zzl : zzc.zzg) {
                    if (!treeSet.contains(zzl.getName())) {
                        arrayList.add(zzl);
                    }
                }
            }
        } else if (!zzc.zzf.isEmpty()) {
            for (Entry entry : zzc.zzf.entrySet()) {
                if (!treeSet.contains(entry.getKey())) {
                    arrayList.add(new zzl((String) entry.getKey(), (String) entry.getValue()));
                }
            }
        }
        return arrayList;
    }

    private static void zza(String str, zzr<?> zzr, zzad zzad) throws zzad {
        zzaa zzi = zzr.zzi();
        int zzh = zzr.zzh();
        try {
            zzi.zza(zzad);
            zzr.zzb(String.format("%s-retry [timeout=%s]", new Object[]{str, Integer.valueOf(zzh)}));
        } catch (zzad e) {
            zzr.zzb(String.format("%s-timeout-giveup [timeout=%s]", new Object[]{str, Integer.valueOf(zzh)}));
            throw e;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0047 A[SYNTHETIC, Splitter:B:23:0x0047] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final byte[] zza(java.io.InputStream r6, int r7) throws java.io.IOException, com.google.android.gms.internal.zzab {
        /*
            r5 = this;
            com.google.android.gms.internal.zzat r0 = new com.google.android.gms.internal.zzat
            com.google.android.gms.internal.zzaj r1 = r5.zzbr
            r0.<init>(r1, r7)
            r7 = 0
            r1 = 0
            if (r6 != 0) goto L_0x0013
            com.google.android.gms.internal.zzab r2 = new com.google.android.gms.internal.zzab     // Catch:{ all -> 0x0011 }
            r2.<init>()     // Catch:{ all -> 0x0011 }
            throw r2     // Catch:{ all -> 0x0011 }
        L_0x0011:
            r2 = move-exception
            goto L_0x0045
        L_0x0013:
            com.google.android.gms.internal.zzaj r2 = r5.zzbr     // Catch:{ all -> 0x0011 }
            r3 = 1024(0x400, float:1.435E-42)
            byte[] r2 = r2.zzb(r3)     // Catch:{ all -> 0x0011 }
        L_0x001b:
            int r1 = r6.read(r2)     // Catch:{ all -> 0x0041 }
            r3 = -1
            if (r1 == r3) goto L_0x0026
            r0.write(r2, r7, r1)     // Catch:{ all -> 0x0041 }
            goto L_0x001b
        L_0x0026:
            byte[] r1 = r0.toByteArray()     // Catch:{ all -> 0x0041 }
            if (r6 == 0) goto L_0x0038
            r6.close()     // Catch:{ IOException -> 0x0030 }
            goto L_0x0038
        L_0x0030:
            r6 = move-exception
            java.lang.String r6 = "Error occurred when closing InputStream"
            java.lang.Object[] r7 = new java.lang.Object[r7]
            com.google.android.gms.internal.zzae.zza(r6, r7)
        L_0x0038:
            com.google.android.gms.internal.zzaj r6 = r5.zzbr
            r6.zza(r2)
            r0.close()
            return r1
        L_0x0041:
            r1 = move-exception
            r4 = r2
            r2 = r1
            r1 = r4
        L_0x0045:
            if (r6 == 0) goto L_0x0053
            r6.close()     // Catch:{ IOException -> 0x004b }
            goto L_0x0053
        L_0x004b:
            r6 = move-exception
            java.lang.String r6 = "Error occurred when closing InputStream"
            java.lang.Object[] r7 = new java.lang.Object[r7]
            com.google.android.gms.internal.zzae.zza(r6, r7)
        L_0x0053:
            com.google.android.gms.internal.zzaj r6 = r5.zzbr
            r6.zza(r1)
            r0.close()
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzai.zza(java.io.InputStream, int):byte[]");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:0x01af, code lost:
        r2 = new java.lang.String(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x01b7, code lost:
        throw new java.lang.RuntimeException(r2, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:104:0x01b9, code lost:
        r5 = "socket";
        r6 = new com.google.android.gms.internal.zzac();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:105:0x01c0, code lost:
        zza(r5, r2, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0088, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0089, code lost:
        r5 = r0;
        r13 = null;
        r17 = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x010c, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x010e, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x010f, code lost:
        r18 = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0111, code lost:
        r13 = r5;
        r17 = r18;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0115, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0116, code lost:
        r5 = r0;
        r13 = null;
        r17 = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x011d, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x011e, code lost:
        r17 = r5;
        r13 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x0122, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x0123, code lost:
        r17 = r5;
        r10 = null;
        r13 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x012a, code lost:
        r5 = r10.getStatusCode();
        com.google.android.gms.internal.zzae.zzc("Unexpected response code %d for %s", java.lang.Integer.valueOf(r5), r27.getUrl());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0141, code lost:
        if (r13 != null) goto L_0x0143;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x0143, code lost:
        r11 = new com.google.android.gms.internal.zzp(r5, r13, false, android.os.SystemClock.elapsedRealtime() - r3, r17);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x0153, code lost:
        if (r5 == 401) goto L_0x017c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x015c, code lost:
        if (r5 < 400) goto L_0x0168;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x0167, code lost:
        throw new com.google.android.gms.internal.zzg(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x016a, code lost:
        if (r5 < 500) goto L_0x0176;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x0175, code lost:
        throw new com.google.android.gms.internal.zzab(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x017b, code lost:
        throw new com.google.android.gms.internal.zzab(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x017c, code lost:
        zza("auth", r2, new com.google.android.gms.internal.zza(r11));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x0188, code lost:
        r5 = "network";
        r6 = new com.google.android.gms.internal.zzo();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x0195, code lost:
        throw new com.google.android.gms.internal.zzq(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x0196, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x0197, code lost:
        r3 = r0;
        r5 = "Bad URL ";
        r2 = java.lang.String.valueOf(r27.getUrl());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x01a8, code lost:
        if (r2.length() != 0) goto L_0x01aa;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x01aa, code lost:
        r2 = r5.concat(r2);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:103:0x01b8 A[ExcHandler: SocketTimeoutException (e java.net.SocketTimeoutException), Splitter:B:2:0x0010] */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x0190 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x012a  */
    /* JADX WARNING: Removed duplicated region for block: B:96:0x0196 A[ExcHandler: MalformedURLException (r0v1 'e' java.net.MalformedURLException A[CUSTOM_DECLARE]), Splitter:B:2:0x0010] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.android.gms.internal.zzp zzc(com.google.android.gms.internal.zzr<?> r27) throws com.google.android.gms.internal.zzad {
        /*
            r26 = this;
            r1 = r26
            r2 = r27
            long r3 = android.os.SystemClock.elapsedRealtime()
        L_0x0008:
            java.util.List r5 = java.util.Collections.emptyList()
            r6 = 2
            r7 = 1
            r8 = 0
            r9 = 0
            com.google.android.gms.internal.zzc r10 = r27.zze()     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0122 }
            if (r10 != 0) goto L_0x001b
            java.util.Map r10 = java.util.Collections.emptyMap()     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0122 }
            goto L_0x003f
        L_0x001b:
            java.util.HashMap r11 = new java.util.HashMap     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0122 }
            r11.<init>()     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0122 }
            java.lang.String r12 = r10.zza     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0122 }
            if (r12 == 0) goto L_0x002b
            java.lang.String r12 = "If-None-Match"
            java.lang.String r13 = r10.zza     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0122 }
            r11.put(r12, r13)     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0122 }
        L_0x002b:
            long r12 = r10.zzc     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0122 }
            r14 = 0
            int r12 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1))
            if (r12 <= 0) goto L_0x003e
            java.lang.String r12 = "If-Modified-Since"
            long r13 = r10.zzc     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0122 }
            java.lang.String r10 = com.google.android.gms.internal.zzao.zzb(r13)     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0122 }
            r11.put(r12, r10)     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0122 }
        L_0x003e:
            r10 = r11
        L_0x003f:
            com.google.android.gms.internal.zzah r11 = r1.zzbq     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0122 }
            com.google.android.gms.internal.zzap r10 = r11.zza(r2, r10)     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0122 }
            int r12 = r10.getStatusCode()     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x011d }
            java.util.List r11 = r10.zzp()     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x011d }
            r5 = 304(0x130, float:4.26E-43)
            if (r12 != r5) goto L_0x008f
            com.google.android.gms.internal.zzc r5 = r27.zze()     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0088 }
            if (r5 != 0) goto L_0x006d
            com.google.android.gms.internal.zzp r5 = new com.google.android.gms.internal.zzp     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0088 }
            r14 = 304(0x130, float:4.26E-43)
            r15 = 0
            r16 = 1
            long r12 = android.os.SystemClock.elapsedRealtime()     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0088 }
            r17 = 0
            long r17 = r12 - r3
            r13 = r5
            r19 = r11
            r13.<init>(r14, r15, r16, r17, r19)     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0088 }
            return r5
        L_0x006d:
            java.util.List r25 = zza(r11, r5)     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0088 }
            com.google.android.gms.internal.zzp r12 = new com.google.android.gms.internal.zzp     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0088 }
            r20 = 304(0x130, float:4.26E-43)
            byte[] r5 = r5.data     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0088 }
            r22 = 1
            long r13 = android.os.SystemClock.elapsedRealtime()     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0088 }
            r15 = 0
            long r23 = r13 - r3
            r19 = r12
            r21 = r5
            r19.<init>(r20, r21, r22, r23, r25)     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0088 }
            return r12
        L_0x0088:
            r0 = move-exception
            r5 = r0
            r13 = r8
            r17 = r11
            goto L_0x0128
        L_0x008f:
            java.io.InputStream r5 = r10.getContent()     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0115 }
            if (r5 == 0) goto L_0x009e
            int r13 = r10.getContentLength()     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0088 }
            byte[] r5 = r1.zza(r5, r13)     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0088 }
            goto L_0x00a0
        L_0x009e:
            byte[] r5 = new byte[r9]     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x0115 }
        L_0x00a0:
            long r13 = android.os.SystemClock.elapsedRealtime()     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x010e }
            r8 = 0
            long r13 = r13 - r3
            boolean r8 = DEBUG     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x010e }
            if (r8 != 0) goto L_0x00b0
            r15 = 3000(0xbb8, double:1.482E-320)
            int r8 = (r13 > r15 ? 1 : (r13 == r15 ? 0 : -1))
            if (r8 <= 0) goto L_0x00e8
        L_0x00b0:
            java.lang.String r8 = "HTTP response for request=<%s> [lifetime=%d], [size=%s], [rc=%d], [retryCount=%s]"
            r15 = 5
            java.lang.Object[] r15 = new java.lang.Object[r15]     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x010e }
            r15[r9] = r2     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x010e }
            java.lang.Long r13 = java.lang.Long.valueOf(r13)     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x010e }
            r15[r7] = r13     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x010e }
            if (r5 == 0) goto L_0x00cb
            int r13 = r5.length     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x00c5 }
            java.lang.Integer r13 = java.lang.Integer.valueOf(r13)     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x00c5 }
            goto L_0x00cd
        L_0x00c5:
            r0 = move-exception
            r13 = r5
            r17 = r11
            goto L_0x0127
        L_0x00cb:
            java.lang.String r13 = "null"
        L_0x00cd:
            r15[r6] = r13     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x010e }
            r13 = 3
            java.lang.Integer r14 = java.lang.Integer.valueOf(r12)     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x010e }
            r15[r13] = r14     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x010e }
            r13 = 4
            com.google.android.gms.internal.zzaa r14 = r27.zzi()     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x010e }
            int r14 = r14.zzc()     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x010e }
            java.lang.Integer r14 = java.lang.Integer.valueOf(r14)     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x010e }
            r15[r13] = r14     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x010e }
            com.google.android.gms.internal.zzae.zzb(r8, r15)     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x010e }
        L_0x00e8:
            r8 = 200(0xc8, float:2.8E-43)
            if (r12 < r8) goto L_0x0104
            r8 = 299(0x12b, float:4.19E-43)
            if (r12 <= r8) goto L_0x00f1
            goto L_0x0104
        L_0x00f1:
            com.google.android.gms.internal.zzp r8 = new com.google.android.gms.internal.zzp     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x010e }
            r14 = 0
            long r15 = android.os.SystemClock.elapsedRealtime()     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x010e }
            r13 = 0
            long r15 = r15 - r3
            r18 = r11
            r11 = r8
            r13 = r5
            r17 = r18
            r11.<init>(r12, r13, r14, r15, r17)     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x010c }
            return r8
        L_0x0104:
            r18 = r11
            java.io.IOException r8 = new java.io.IOException     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x010c }
            r8.<init>()     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x010c }
            throw r8     // Catch:{ SocketTimeoutException -> 0x01b8, MalformedURLException -> 0x0196, IOException -> 0x010c }
        L_0x010c:
            r0 = move-exception
            goto L_0x0111
        L_0x010e:
            r0 = move-exception
            r18 = r11
        L_0x0111:
            r13 = r5
            r17 = r18
            goto L_0x0127
        L_0x0115:
            r0 = move-exception
            r18 = r11
            r5 = r0
            r13 = r8
            r17 = r18
            goto L_0x0128
        L_0x011d:
            r0 = move-exception
            r17 = r5
            r13 = r8
            goto L_0x0127
        L_0x0122:
            r0 = move-exception
            r17 = r5
            r10 = r8
            r13 = r10
        L_0x0127:
            r5 = r0
        L_0x0128:
            if (r10 == 0) goto L_0x0190
            int r5 = r10.getStatusCode()
            java.lang.String r8 = "Unexpected response code %d for %s"
            java.lang.Object[] r6 = new java.lang.Object[r6]
            java.lang.Integer r10 = java.lang.Integer.valueOf(r5)
            r6[r9] = r10
            java.lang.String r9 = r27.getUrl()
            r6[r7] = r9
            com.google.android.gms.internal.zzae.zzc(r8, r6)
            if (r13 == 0) goto L_0x0188
            com.google.android.gms.internal.zzp r6 = new com.google.android.gms.internal.zzp
            r14 = 0
            long r7 = android.os.SystemClock.elapsedRealtime()
            long r15 = r7 - r3
            r11 = r6
            r12 = r5
            r11.<init>(r12, r13, r14, r15, r17)
            r7 = 401(0x191, float:5.62E-43)
            if (r5 == r7) goto L_0x017c
            r7 = 403(0x193, float:5.65E-43)
            if (r5 != r7) goto L_0x015a
            goto L_0x017c
        L_0x015a:
            r2 = 400(0x190, float:5.6E-43)
            if (r5 < r2) goto L_0x0168
            r2 = 499(0x1f3, float:6.99E-43)
            if (r5 > r2) goto L_0x0168
            com.google.android.gms.internal.zzg r2 = new com.google.android.gms.internal.zzg
            r2.<init>(r6)
            throw r2
        L_0x0168:
            r2 = 500(0x1f4, float:7.0E-43)
            if (r5 < r2) goto L_0x0176
            r2 = 599(0x257, float:8.4E-43)
            if (r5 > r2) goto L_0x0176
            com.google.android.gms.internal.zzab r2 = new com.google.android.gms.internal.zzab
            r2.<init>(r6)
            throw r2
        L_0x0176:
            com.google.android.gms.internal.zzab r2 = new com.google.android.gms.internal.zzab
            r2.<init>(r6)
            throw r2
        L_0x017c:
            java.lang.String r5 = "auth"
            com.google.android.gms.internal.zza r7 = new com.google.android.gms.internal.zza
            r7.<init>(r6)
            zza(r5, r2, r7)
            goto L_0x0008
        L_0x0188:
            java.lang.String r5 = "network"
            com.google.android.gms.internal.zzo r6 = new com.google.android.gms.internal.zzo
            r6.<init>()
            goto L_0x01c0
        L_0x0190:
            com.google.android.gms.internal.zzq r2 = new com.google.android.gms.internal.zzq
            r2.<init>(r5)
            throw r2
        L_0x0196:
            r0 = move-exception
            r3 = r0
            java.lang.RuntimeException r4 = new java.lang.RuntimeException
            java.lang.String r5 = "Bad URL "
            java.lang.String r2 = r27.getUrl()
            java.lang.String r2 = java.lang.String.valueOf(r2)
            int r6 = r2.length()
            if (r6 == 0) goto L_0x01af
            java.lang.String r2 = r5.concat(r2)
            goto L_0x01b4
        L_0x01af:
            java.lang.String r2 = new java.lang.String
            r2.<init>(r5)
        L_0x01b4:
            r4.<init>(r2, r3)
            throw r4
        L_0x01b8:
            r0 = move-exception
            java.lang.String r5 = "socket"
            com.google.android.gms.internal.zzac r6 = new com.google.android.gms.internal.zzac
            r6.<init>()
        L_0x01c0:
            zza(r5, r2, r6)
            goto L_0x0008
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzai.zzc(com.google.android.gms.internal.zzr):com.google.android.gms.internal.zzp");
    }
}
