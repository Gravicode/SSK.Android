package com.google.android.gms.common.util;

import android.os.Process;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public final class zzs {
    private static String zzget = null;
    private static final int zzgeu = Process.myPid();

    public static String zzamo() {
        if (zzget == null) {
            zzget = zzcj(zzgeu);
        }
        return zzget;
    }

    private static String zzcj(int i) {
        BufferedReader bufferedReader;
        ThreadPolicy allowThreadDiskReads;
        BufferedReader bufferedReader2 = null;
        if (i <= 0) {
            return null;
        }
        try {
            allowThreadDiskReads = StrictMode.allowThreadDiskReads();
            StringBuilder sb = new StringBuilder(25);
            sb.append("/proc/");
            sb.append(i);
            sb.append("/cmdline");
            bufferedReader = new BufferedReader(new FileReader(sb.toString()));
            try {
                StrictMode.setThreadPolicy(allowThreadDiskReads);
                String trim = bufferedReader.readLine().trim();
                zzn.closeQuietly(bufferedReader);
                return trim;
            } catch (IOException e) {
                zzn.closeQuietly(bufferedReader);
                return null;
            } catch (Throwable th) {
                th = th;
                bufferedReader2 = bufferedReader;
                zzn.closeQuietly(bufferedReader2);
                throw th;
            }
        } catch (IOException e2) {
            bufferedReader = null;
            zzn.closeQuietly(bufferedReader);
            return null;
        } catch (Throwable th2) {
            th = th2;
            zzn.closeQuietly(bufferedReader2);
            throw th;
        }
    }
}
