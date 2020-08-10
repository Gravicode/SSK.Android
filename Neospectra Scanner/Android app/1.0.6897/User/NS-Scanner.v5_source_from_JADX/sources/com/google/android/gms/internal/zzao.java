package com.google.android.gms.internal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.TreeMap;

public final class zzao {
    static List<zzl> zza(Map<String, String> map) {
        ArrayList arrayList = new ArrayList(map.size());
        for (Entry entry : map.entrySet()) {
            arrayList.add(new zzl((String) entry.getKey(), (String) entry.getValue()));
        }
        return arrayList;
    }

    static Map<String, String> zza(List<zzl> list) {
        TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (zzl zzl : list) {
            treeMap.put(zzl.getName(), zzl.getValue());
        }
        return treeMap;
    }

    public static zzc zzb(zzp zzp) {
        boolean z;
        long j;
        boolean z2;
        long j2;
        long j3;
        long j4;
        zzp zzp2 = zzp;
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, String> map = zzp2.zzac;
        String str = (String) map.get("Date");
        long zzf = str != null ? zzf(str) : 0;
        String str2 = (String) map.get("Cache-Control");
        if (str2 != null) {
            String[] split = str2.split(",");
            j2 = 0;
            z2 = false;
            j = 0;
            for (String trim : split) {
                String trim2 = trim.trim();
                if (trim2.equals("no-cache") || trim2.equals("no-store")) {
                    return null;
                }
                if (trim2.startsWith("max-age=")) {
                    try {
                        j2 = Long.parseLong(trim2.substring(8));
                    } catch (Exception e) {
                    }
                } else if (trim2.startsWith("stale-while-revalidate=")) {
                    j = Long.parseLong(trim2.substring(23));
                } else if (trim2.equals("must-revalidate") || trim2.equals("proxy-revalidate")) {
                    z2 = true;
                }
            }
            z = true;
        } else {
            j2 = 0;
            z2 = false;
            j = 0;
            z = false;
        }
        String str3 = (String) map.get("Expires");
        long zzf2 = str3 != null ? zzf(str3) : 0;
        String str4 = (String) map.get("Last-Modified");
        long zzf3 = str4 != null ? zzf(str4) : 0;
        String str5 = (String) map.get("ETag");
        if (z) {
            j4 = currentTimeMillis + (j2 * 1000);
            if (!z2) {
                j3 = (j * 1000) + j4;
                zzc zzc = new zzc();
                zzc.data = zzp2.data;
                zzc.zza = str5;
                zzc.zze = j4;
                zzc.zzd = j3;
                zzc.zzb = zzf;
                zzc.zzc = zzf3;
                zzc.zzf = map;
                zzc.zzg = zzp2.allHeaders;
                return zzc;
            }
        } else if (zzf <= 0 || zzf2 < zzf) {
            j4 = 0;
        } else {
            j3 = (zzf2 - zzf) + currentTimeMillis;
            j4 = j3;
            zzc zzc2 = new zzc();
            zzc2.data = zzp2.data;
            zzc2.zza = str5;
            zzc2.zze = j4;
            zzc2.zzd = j3;
            zzc2.zzb = zzf;
            zzc2.zzc = zzf3;
            zzc2.zzf = map;
            zzc2.zzg = zzp2.allHeaders;
            return zzc2;
        }
        j3 = j4;
        zzc zzc22 = new zzc();
        zzc22.data = zzp2.data;
        zzc22.zza = str5;
        zzc22.zze = j4;
        zzc22.zzd = j3;
        zzc22.zzb = zzf;
        zzc22.zzc = zzf3;
        zzc22.zzf = map;
        zzc22.zzg = zzp2.allHeaders;
        return zzc22;
    }

    static String zzb(long j) {
        return zzo().format(new Date(j));
    }

    public static String zzb(Map<String, String> map) {
        String str = "ISO-8859-1";
        String str2 = (String) map.get("Content-Type");
        if (str2 != null) {
            String[] split = str2.split(";");
            for (int i = 1; i < split.length; i++) {
                String[] split2 = split[i].trim().split("=");
                if (split2.length == 2 && split2[0].equals("charset")) {
                    return split2[1];
                }
            }
        }
        return str;
    }

    private static long zzf(String str) {
        try {
            return zzo().parse(str).getTime();
        } catch (ParseException e) {
            zzae.zza(e, "Unable to parse dateStr: %s, falling back to 0", str);
            return 0;
        }
    }

    private static SimpleDateFormat zzo() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat;
    }
}
