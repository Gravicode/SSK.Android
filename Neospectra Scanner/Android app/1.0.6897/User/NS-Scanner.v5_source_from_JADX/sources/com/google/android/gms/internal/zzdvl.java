package com.google.android.gms.internal;

import java.io.PrintStream;
import java.io.PrintWriter;

public final class zzdvl {
    private static zzdvm zzmag;
    private static int zzmah;

    static final class zza extends zzdvm {
        zza() {
        }

        public final void zza(Throwable th, PrintStream printStream) {
            th.printStackTrace(printStream);
        }

        public final void zza(Throwable th, PrintWriter printWriter) {
            th.printStackTrace(printWriter);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0068  */
    static {
        /*
            r0 = 1
            java.lang.Integer r1 = zzbqm()     // Catch:{ Throwable -> 0x002c }
            if (r1 == 0) goto L_0x0017
            int r2 = r1.intValue()     // Catch:{ Throwable -> 0x0015 }
            r3 = 19
            if (r2 < r3) goto L_0x0017
            com.google.android.gms.internal.zzdvq r2 = new com.google.android.gms.internal.zzdvq     // Catch:{ Throwable -> 0x0015 }
            r2.<init>()     // Catch:{ Throwable -> 0x0015 }
            goto L_0x0063
        L_0x0015:
            r2 = move-exception
            goto L_0x002e
        L_0x0017:
            java.lang.String r2 = "com.google.devtools.build.android.desugar.runtime.twr_disable_mimic"
            boolean r2 = java.lang.Boolean.getBoolean(r2)     // Catch:{ Throwable -> 0x0015 }
            r2 = r2 ^ r0
            if (r2 == 0) goto L_0x0026
            com.google.android.gms.internal.zzdvp r2 = new com.google.android.gms.internal.zzdvp     // Catch:{ Throwable -> 0x0015 }
            r2.<init>()     // Catch:{ Throwable -> 0x0015 }
            goto L_0x0063
        L_0x0026:
            com.google.android.gms.internal.zzdvl$zza r2 = new com.google.android.gms.internal.zzdvl$zza     // Catch:{ Throwable -> 0x0015 }
            r2.<init>()     // Catch:{ Throwable -> 0x0015 }
            goto L_0x0063
        L_0x002c:
            r2 = move-exception
            r1 = 0
        L_0x002e:
            java.io.PrintStream r3 = java.lang.System.err
            java.lang.Class<com.google.android.gms.internal.zzdvl$zza> r4 = com.google.android.gms.internal.zzdvl.zza.class
            java.lang.String r4 = r4.getName()
            java.lang.String r5 = java.lang.String.valueOf(r4)
            int r5 = r5.length()
            int r5 = r5 + 132
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>(r5)
            java.lang.String r5 = "An error has occured when initializing the try-with-resources desuguring strategy. The default strategy "
            r6.append(r5)
            r6.append(r4)
            java.lang.String r4 = "will be used. The error is: "
            r6.append(r4)
            java.lang.String r4 = r6.toString()
            r3.println(r4)
            java.io.PrintStream r3 = java.lang.System.err
            r2.printStackTrace(r3)
            com.google.android.gms.internal.zzdvl$zza r2 = new com.google.android.gms.internal.zzdvl$zza
            r2.<init>()
        L_0x0063:
            zzmag = r2
            if (r1 != 0) goto L_0x0068
            goto L_0x006c
        L_0x0068:
            int r0 = r1.intValue()
        L_0x006c:
            zzmah = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzdvl.<clinit>():void");
    }

    public static void zza(Throwable th, PrintStream printStream) {
        zzmag.zza(th, printStream);
    }

    public static void zza(Throwable th, PrintWriter printWriter) {
        zzmag.zza(th, printWriter);
    }

    private static Integer zzbqm() {
        try {
            return (Integer) Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null);
        } catch (Exception e) {
            System.err.println("Failed to retrieve value from android.os.Build$VERSION.SDK_INT due to the following exception.");
            e.printStackTrace(System.err);
            return null;
        }
    }
}
