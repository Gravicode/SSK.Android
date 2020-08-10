package com.google.android.gms.common.util;

import java.util.HashMap;
import org.apache.commons.math3.geometry.VectorFormat;

public final class zzp {
    public static void zza(StringBuilder sb, HashMap<String, String> hashMap) {
        String str;
        sb.append(VectorFormat.DEFAULT_PREFIX);
        boolean z = true;
        for (String str2 : hashMap.keySet()) {
            if (!z) {
                sb.append(",");
            } else {
                z = false;
            }
            String str3 = (String) hashMap.get(str2);
            sb.append("\"");
            sb.append(str2);
            sb.append("\":");
            if (str3 == null) {
                str = "null";
            } else {
                sb.append("\"");
                sb.append(str3);
                str = "\"";
            }
            sb.append(str);
        }
        sb.append(VectorFormat.DEFAULT_SUFFIX);
    }
}
