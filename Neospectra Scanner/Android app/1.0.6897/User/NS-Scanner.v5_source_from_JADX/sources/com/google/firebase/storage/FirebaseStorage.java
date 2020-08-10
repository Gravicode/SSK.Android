package com.google.firebase.storage;

import android.net.Uri;
import android.net.Uri.Builder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.internal.zzexw;
import com.google.firebase.FirebaseApp;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class FirebaseStorage {
    private static final Map<String, Map<String, FirebaseStorage>> zzoib = new HashMap();
    @NonNull
    private final FirebaseApp zzoic;
    @Nullable
    private final String zzoid;
    private long zzoie = 600000;
    private long zzoif = 600000;
    private long zzoig = 120000;

    private FirebaseStorage(@Nullable String str, @NonNull FirebaseApp firebaseApp) {
        this.zzoid = str;
        this.zzoic = firebaseApp;
    }

    @NonNull
    public static FirebaseStorage getInstance() {
        FirebaseApp instance = FirebaseApp.getInstance();
        zzbq.checkArgument(instance != null, "You must call FirebaseApp.initialize() first.");
        return getInstance(instance);
    }

    @NonNull
    public static FirebaseStorage getInstance(@NonNull FirebaseApp firebaseApp) {
        zzbq.checkArgument(firebaseApp != null, "Null is not a valid value for the FirebaseApp.");
        String storageBucket = firebaseApp.getOptions().getStorageBucket();
        if (storageBucket == null) {
            return zza(firebaseApp, null);
        }
        String str = "gs://";
        try {
            String valueOf = String.valueOf(firebaseApp.getOptions().getStorageBucket());
            return zza(firebaseApp, zzexw.zzf(firebaseApp, valueOf.length() != 0 ? str.concat(valueOf) : new String(str)));
        } catch (UnsupportedEncodingException e) {
            String str2 = "FirebaseStorage";
            String str3 = "Unable to parse bucket:";
            String valueOf2 = String.valueOf(storageBucket);
            Log.e(str2, valueOf2.length() != 0 ? str3.concat(valueOf2) : new String(str3), e);
            throw new IllegalArgumentException("The storage Uri could not be parsed.");
        }
    }

    @NonNull
    public static FirebaseStorage getInstance(@NonNull FirebaseApp firebaseApp, @NonNull String str) {
        zzbq.checkArgument(firebaseApp != null, "Null is not a valid value for the FirebaseApp.");
        if (!str.toLowerCase().startsWith("gs://")) {
            throw new IllegalArgumentException("Please use a gs:// URL for your Firebase Storage bucket.");
        }
        try {
            return zza(firebaseApp, zzexw.zzf(firebaseApp, str));
        } catch (UnsupportedEncodingException e) {
            String str2 = "FirebaseStorage";
            String str3 = "Unable to parse url:";
            String valueOf = String.valueOf(str);
            Log.e(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3), e);
            throw new IllegalArgumentException("The storage Uri could not be parsed.");
        }
    }

    @NonNull
    public static FirebaseStorage getInstance(@NonNull String str) {
        FirebaseApp instance = FirebaseApp.getInstance();
        zzbq.checkArgument(instance != null, "You must call FirebaseApp.initialize() first.");
        return getInstance(instance, str);
    }

    private static FirebaseStorage zza(@NonNull FirebaseApp firebaseApp, @Nullable Uri uri) {
        FirebaseStorage firebaseStorage;
        String host = uri != null ? uri.getHost() : null;
        if (uri == null || TextUtils.isEmpty(uri.getPath())) {
            synchronized (zzoib) {
                Map map = (Map) zzoib.get(firebaseApp.getName());
                if (map == null) {
                    map = new HashMap();
                    zzoib.put(firebaseApp.getName(), map);
                }
                firebaseStorage = (FirebaseStorage) map.get(host);
                if (firebaseStorage == null) {
                    firebaseStorage = new FirebaseStorage(host, firebaseApp);
                    map.put(host, firebaseStorage);
                }
            }
            return firebaseStorage;
        }
        throw new IllegalArgumentException("The storage Uri cannot contain a path element.");
    }

    @NonNull
    private final StorageReference zzt(@NonNull Uri uri) {
        zzbq.checkNotNull(uri, "uri must not be null");
        String str = this.zzoid;
        zzbq.checkArgument(TextUtils.isEmpty(str) || uri.getAuthority().equalsIgnoreCase(str), "The supplied bucketname does not match the storage bucket of the current instance.");
        return new StorageReference(uri, this);
    }

    @NonNull
    public FirebaseApp getApp() {
        return this.zzoic;
    }

    public long getMaxDownloadRetryTimeMillis() {
        return this.zzoif;
    }

    public long getMaxOperationRetryTimeMillis() {
        return this.zzoig;
    }

    public long getMaxUploadRetryTimeMillis() {
        return this.zzoie;
    }

    @NonNull
    public StorageReference getReference() {
        if (!TextUtils.isEmpty(this.zzoid)) {
            return zzt(new Builder().scheme("gs").authority(this.zzoid).path("/").build());
        }
        throw new IllegalStateException("FirebaseApp was not initialized with a bucket name.");
    }

    @NonNull
    public StorageReference getReference(@NonNull String str) {
        zzbq.checkArgument(!TextUtils.isEmpty(str), "location must not be null or empty");
        String lowerCase = str.toLowerCase();
        if (!lowerCase.startsWith("gs://") && !lowerCase.startsWith("https://") && !lowerCase.startsWith("http://")) {
            return getReference().child(str);
        }
        throw new IllegalArgumentException("location should not be a full URL.");
    }

    @NonNull
    public StorageReference getReferenceFromUrl(@NonNull String str) {
        zzbq.checkArgument(!TextUtils.isEmpty(str), "location must not be null or empty");
        String lowerCase = str.toLowerCase();
        if (lowerCase.startsWith("gs://") || lowerCase.startsWith("https://") || lowerCase.startsWith("http://")) {
            try {
                Uri zzf = zzexw.zzf(this.zzoic, str);
                if (zzf != null) {
                    return zzt(zzf);
                }
                throw new IllegalArgumentException("The storage Uri could not be parsed.");
            } catch (UnsupportedEncodingException e) {
                String str2 = "FirebaseStorage";
                String str3 = "Unable to parse location:";
                String valueOf = String.valueOf(str);
                Log.e(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3), e);
                throw new IllegalArgumentException("The storage Uri could not be parsed.");
            }
        } else {
            throw new IllegalArgumentException("The storage Uri could not be parsed.");
        }
    }

    public void setMaxDownloadRetryTimeMillis(long j) {
        this.zzoif = j;
    }

    public void setMaxOperationRetryTimeMillis(long j) {
        this.zzoig = j;
    }

    public void setMaxUploadRetryTimeMillis(long j) {
        this.zzoie = j;
    }
}
