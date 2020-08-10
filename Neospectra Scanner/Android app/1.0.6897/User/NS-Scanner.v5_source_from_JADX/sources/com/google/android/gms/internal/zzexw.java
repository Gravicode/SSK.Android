package com.google.android.gms.internal;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.zzbg;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.GetTokenResult;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class zzexw {
    @Nullable
    public static Uri zzf(@NonNull FirebaseApp firebaseApp, @Nullable String str) throws UnsupportedEncodingException {
        String str2;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.toLowerCase().startsWith("gs://")) {
            String str3 = "gs://";
            String valueOf = String.valueOf(zzexs.zzsh(zzexs.zzsj(str.substring(5))));
            return Uri.parse(valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
        }
        Uri parse = Uri.parse(str);
        String scheme = parse.getScheme();
        if (scheme == null || (!zzbg.equal(scheme.toLowerCase(), "http") && !zzbg.equal(scheme.toLowerCase(), "https"))) {
            String str4 = "StorageUtil";
            String str5 = "FirebaseStorage is unable to support the scheme:";
            String valueOf2 = String.valueOf(scheme);
            Log.w(str4, valueOf2.length() != 0 ? str5.concat(valueOf2) : new String(str5));
            throw new IllegalArgumentException("Uri scheme");
        }
        try {
            int indexOf = parse.getAuthority().toLowerCase().indexOf(zzeyb.zzi(firebaseApp).zzcmo());
            String zzsi = zzexs.zzsi(parse.getEncodedPath());
            if (indexOf == 0 && zzsi.startsWith("/")) {
                int indexOf2 = zzsi.indexOf("/b/", 0);
                int i = indexOf2 + 3;
                int indexOf3 = zzsi.indexOf("/", i);
                int indexOf4 = zzsi.indexOf("/o/", 0);
                if (indexOf2 == -1 || indexOf3 == -1) {
                    Log.w("StorageUtil", "Firebase Storage URLs must point to an object in your Storage Bucket. Please obtain a URL using the Firebase Console or getDownloadUrl().");
                    throw new IllegalArgumentException("Firebase Storage URLs must point to an object in your Storage Bucket. Please obtain a URL using the Firebase Console or getDownloadUrl().");
                }
                str2 = zzsi.substring(i, indexOf3);
                zzsi = indexOf4 != -1 ? zzsi.substring(indexOf4 + 3) : "";
            } else if (indexOf > 1) {
                str2 = parse.getAuthority().substring(0, indexOf - 1);
            } else {
                Log.w("StorageUtil", "Firebase Storage URLs must point to an object in your Storage Bucket. Please obtain a URL using the Firebase Console or getDownloadUrl().");
                throw new IllegalArgumentException("Firebase Storage URLs must point to an object in your Storage Bucket. Please obtain a URL using the Firebase Console or getDownloadUrl().");
            }
            zzbq.zzh(str2, "No bucket specified");
            return new Builder().scheme("gs").authority(str2).encodedPath(zzsi).build();
        } catch (RemoteException e) {
            throw new UnsupportedEncodingException("Could not parse Url because the Storage network layer did not load");
        }
    }

    @Nullable
    public static String zzh(FirebaseApp firebaseApp) {
        try {
            String token = ((GetTokenResult) Tasks.await(firebaseApp.getToken(false), 30000, TimeUnit.MILLISECONDS)).getToken();
            if (!TextUtils.isEmpty(token)) {
                return token;
            }
            Log.w("StorageUtil", "no auth token for request");
            return null;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            String valueOf = String.valueOf(e);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 20);
            sb.append("error getting token ");
            sb.append(valueOf);
            Log.e("StorageUtil", sb.toString());
        }
    }

    public static long zzsk(@Nullable String str) {
        if (str == null) {
            return 0;
        }
        String replaceAll = str.replaceAll("Z$", "-0000");
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()).parse(replaceAll).getTime();
        } catch (ParseException e) {
            String str2 = "StorageUtil";
            String str3 = "unable to parse datetime:";
            String valueOf = String.valueOf(replaceAll);
            Log.w(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3), e);
            return 0;
        }
    }
}
