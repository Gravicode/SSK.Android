package com.google.android.gms.internal;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzbq;
import java.io.UnsupportedEncodingException;

public final class zzexs {
    @NonNull
    public static String zzsh(@Nullable String str) throws UnsupportedEncodingException {
        return TextUtils.isEmpty(str) ? "" : zzsi(Uri.encode(str));
    }

    @NonNull
    public static String zzsi(@NonNull String str) {
        zzbq.checkNotNull(str);
        return str.replace("%2F", "/");
    }

    @NonNull
    public static String zzsj(@NonNull String str) {
        String[] split;
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (!str.startsWith("/") && !str.endsWith("/") && !str.contains("//")) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (String str2 : str.split("/")) {
            if (!TextUtils.isEmpty(str2)) {
                if (sb.length() > 0) {
                    sb.append("/");
                }
                sb.append(str2);
            }
        }
        return sb.toString();
    }
}
