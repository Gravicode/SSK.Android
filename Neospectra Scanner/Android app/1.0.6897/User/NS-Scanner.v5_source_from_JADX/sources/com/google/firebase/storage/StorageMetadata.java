package com.google.firebase.storage;

import android.net.Uri;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.internal.zzexs;
import com.google.android.gms.internal.zzexw;
import com.google.android.gms.internal.zzeyb;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public class StorageMetadata {
    /* access modifiers changed from: private */
    public String mPath;
    /* access modifiers changed from: private */
    public StorageReference zzoht;
    private FirebaseStorage zzoim;
    /* access modifiers changed from: private */
    public String zzoin;
    /* access modifiers changed from: private */
    public String zzoio;
    /* access modifiers changed from: private */
    public zza<String> zzoip;
    /* access modifiers changed from: private */
    public String zzoiq;
    /* access modifiers changed from: private */
    public String zzoir;
    /* access modifiers changed from: private */
    public String zzois;
    /* access modifiers changed from: private */
    public long zzoit;
    /* access modifiers changed from: private */
    public String zzoiu;
    /* access modifiers changed from: private */
    public zza<String> zzoiv;
    /* access modifiers changed from: private */
    public zza<String> zzoiw;
    /* access modifiers changed from: private */
    public zza<String> zzoix;
    /* access modifiers changed from: private */
    public zza<String> zzoiy;
    /* access modifiers changed from: private */
    public zza<Map<String, String>> zzoiz;
    private String[] zzoja;

    public static class Builder {
        private StorageMetadata zzojb;
        private boolean zzojc;

        public Builder() {
            this.zzojb = new StorageMetadata();
        }

        public Builder(StorageMetadata storageMetadata) {
            this.zzojb = new StorageMetadata(false);
        }

        private Builder(JSONObject jSONObject) throws JSONException {
            this.zzojb = new StorageMetadata();
            if (jSONObject != null) {
                zzac(jSONObject);
                this.zzojc = true;
            }
        }

        Builder(JSONObject jSONObject, StorageReference storageReference) throws JSONException {
            this(jSONObject);
            this.zzojb.zzoht = storageReference;
        }

        private final void zzac(JSONObject jSONObject) throws JSONException {
            this.zzojb.zzoio = jSONObject.optString("generation");
            this.zzojb.mPath = jSONObject.optString("name");
            this.zzojb.zzoin = jSONObject.optString("bucket");
            this.zzojb.zzoiq = jSONObject.optString("metageneration");
            this.zzojb.zzoir = jSONObject.optString("timeCreated");
            this.zzojb.zzois = jSONObject.optString("updated");
            this.zzojb.zzoit = jSONObject.optLong("size");
            this.zzojb.zzoiu = jSONObject.optString("md5Hash");
            this.zzojb.zzsg(jSONObject.optString("downloadTokens"));
            if (jSONObject.has("metadata") && !jSONObject.isNull("metadata")) {
                JSONObject jSONObject2 = jSONObject.getJSONObject("metadata");
                Iterator keys = jSONObject2.keys();
                while (keys.hasNext()) {
                    String str = (String) keys.next();
                    setCustomMetadata(str, jSONObject2.getString(str));
                }
            }
            String zze = zze(jSONObject, "contentType");
            if (zze != null) {
                setContentType(zze);
            }
            String zze2 = zze(jSONObject, "cacheControl");
            if (zze2 != null) {
                setCacheControl(zze2);
            }
            String zze3 = zze(jSONObject, "contentDisposition");
            if (zze3 != null) {
                setContentDisposition(zze3);
            }
            String zze4 = zze(jSONObject, "contentEncoding");
            if (zze4 != null) {
                setContentEncoding(zze4);
            }
            String zze5 = zze(jSONObject, "contentLanguage");
            if (zze5 != null) {
                setContentLanguage(zze5);
            }
        }

        @Nullable
        private static String zze(JSONObject jSONObject, String str) throws JSONException {
            if (!jSONObject.has(str) || jSONObject.isNull(str)) {
                return null;
            }
            return jSONObject.getString(str);
        }

        public StorageMetadata build() {
            return new StorageMetadata(this.zzojc);
        }

        public Builder setCacheControl(@Nullable String str) {
            this.zzojb.zzoiv = zza.zzck(str);
            return this;
        }

        public Builder setContentDisposition(@Nullable String str) {
            this.zzojb.zzoiw = zza.zzck(str);
            return this;
        }

        public Builder setContentEncoding(@Nullable String str) {
            this.zzojb.zzoix = zza.zzck(str);
            return this;
        }

        public Builder setContentLanguage(@Nullable String str) {
            this.zzojb.zzoiy = zza.zzck(str);
            return this;
        }

        public Builder setContentType(@Nullable String str) {
            this.zzojb.zzoip = zza.zzck(str);
            return this;
        }

        public Builder setCustomMetadata(String str, String str2) {
            if (!this.zzojb.zzoiz.zzclp()) {
                this.zzojb.zzoiz = zza.zzck(new HashMap());
            }
            ((Map) this.zzojb.zzoiz.getValue()).put(str, str2);
            return this;
        }
    }

    static class zza<T> {
        @Nullable
        private final T value;
        private final boolean zzojd;

        private zza(@Nullable T t, boolean z) {
            this.zzojd = z;
            this.value = t;
        }

        static <T> zza<T> zzcj(T t) {
            return new zza<>(t, false);
        }

        static <T> zza<T> zzck(@Nullable T t) {
            return new zza<>(t, true);
        }

        /* access modifiers changed from: 0000 */
        @Nullable
        public final T getValue() {
            return this.value;
        }

        /* access modifiers changed from: 0000 */
        public final boolean zzclp() {
            return this.zzojd;
        }
    }

    public StorageMetadata() {
        this.mPath = null;
        this.zzoim = null;
        this.zzoht = null;
        this.zzoin = null;
        this.zzoio = null;
        this.zzoip = zza.zzcj("");
        this.zzoiq = null;
        this.zzoir = null;
        this.zzois = null;
        this.zzoiu = null;
        this.zzoiv = zza.zzcj("");
        this.zzoiw = zza.zzcj("");
        this.zzoix = zza.zzcj("");
        this.zzoiy = zza.zzcj("");
        this.zzoiz = zza.zzcj(Collections.emptyMap());
        this.zzoja = null;
    }

    private StorageMetadata(@NonNull StorageMetadata storageMetadata, boolean z) {
        this.mPath = null;
        this.zzoim = null;
        this.zzoht = null;
        this.zzoin = null;
        this.zzoio = null;
        this.zzoip = zza.zzcj("");
        this.zzoiq = null;
        this.zzoir = null;
        this.zzois = null;
        this.zzoiu = null;
        this.zzoiv = zza.zzcj("");
        this.zzoiw = zza.zzcj("");
        this.zzoix = zza.zzcj("");
        this.zzoiy = zza.zzcj("");
        this.zzoiz = zza.zzcj(Collections.emptyMap());
        this.zzoja = null;
        zzbq.checkNotNull(storageMetadata);
        this.mPath = storageMetadata.mPath;
        this.zzoim = storageMetadata.zzoim;
        this.zzoht = storageMetadata.zzoht;
        this.zzoin = storageMetadata.zzoin;
        this.zzoip = storageMetadata.zzoip;
        this.zzoiv = storageMetadata.zzoiv;
        this.zzoiw = storageMetadata.zzoiw;
        this.zzoix = storageMetadata.zzoix;
        this.zzoiy = storageMetadata.zzoiy;
        this.zzoiz = storageMetadata.zzoiz;
        this.zzoja = storageMetadata.zzoja;
        if (z) {
            this.zzoiu = storageMetadata.zzoiu;
            this.zzoit = storageMetadata.zzoit;
            this.zzois = storageMetadata.zzois;
            this.zzoir = storageMetadata.zzoir;
            this.zzoiq = storageMetadata.zzoiq;
            this.zzoio = storageMetadata.zzoio;
        }
    }

    /* access modifiers changed from: private */
    public final void zzsg(@Nullable String str) {
        if (!TextUtils.isEmpty(str)) {
            this.zzoja = str.split(",");
        }
    }

    @Nullable
    public String getBucket() {
        return this.zzoin;
    }

    @Nullable
    public String getCacheControl() {
        return (String) this.zzoiv.getValue();
    }

    @Nullable
    public String getContentDisposition() {
        return (String) this.zzoiw.getValue();
    }

    @Nullable
    public String getContentEncoding() {
        return (String) this.zzoix.getValue();
    }

    @Nullable
    public String getContentLanguage() {
        return (String) this.zzoiy.getValue();
    }

    public String getContentType() {
        return (String) this.zzoip.getValue();
    }

    public long getCreationTimeMillis() {
        return zzexw.zzsk(this.zzoir);
    }

    public String getCustomMetadata(@NonNull String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return (String) ((Map) this.zzoiz.getValue()).get(str);
    }

    @NonNull
    public Set<String> getCustomMetadataKeys() {
        return ((Map) this.zzoiz.getValue()).keySet();
    }

    @Nullable
    public Uri getDownloadUrl() {
        List downloadUrls = getDownloadUrls();
        if (downloadUrls == null || downloadUrls.size() <= 0) {
            return null;
        }
        return (Uri) downloadUrls.get(0);
    }

    @Nullable
    public List<Uri> getDownloadUrls() {
        String[] strArr;
        ArrayList arrayList = new ArrayList();
        if (!(this.zzoja == null || this.zzoht == null)) {
            try {
                String zzu = zzeyb.zzi(this.zzoht.getStorage().getApp()).zzu(this.zzoht.zzclr());
                if (!TextUtils.isEmpty(zzu)) {
                    for (String str : this.zzoja) {
                        if (!TextUtils.isEmpty(str)) {
                            StringBuilder sb = new StringBuilder(String.valueOf(zzu).length() + 17 + String.valueOf(str).length());
                            sb.append(zzu);
                            sb.append("?alt=media&token=");
                            sb.append(str);
                            arrayList.add(Uri.parse(sb.toString()));
                        }
                    }
                }
            } catch (RemoteException e) {
                Log.e("StorageMetadata", "Unexpected error getting DownloadUrls.", e);
            }
        }
        return arrayList;
    }

    @Nullable
    public String getGeneration() {
        return this.zzoio;
    }

    @Nullable
    public String getMd5Hash() {
        return this.zzoiu;
    }

    @Nullable
    public String getMetadataGeneration() {
        return this.zzoiq;
    }

    @Nullable
    public String getName() {
        String path = getPath();
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        int lastIndexOf = path.lastIndexOf(47);
        if (lastIndexOf != -1) {
            path = path.substring(lastIndexOf + 1);
        }
        return path;
    }

    @NonNull
    public String getPath() {
        return this.mPath != null ? this.mPath : "";
    }

    @Nullable
    public StorageReference getReference() {
        if (this.zzoht != null || this.zzoim == null) {
            return this.zzoht;
        }
        String bucket = getBucket();
        String path = getPath();
        if (TextUtils.isEmpty(bucket) || TextUtils.isEmpty(path)) {
            return null;
        }
        try {
            return new StorageReference(new android.net.Uri.Builder().scheme("gs").authority(bucket).encodedPath(zzexs.zzsh(path)).build(), this.zzoim);
        } catch (UnsupportedEncodingException e) {
            StringBuilder sb = new StringBuilder(String.valueOf(bucket).length() + 38 + String.valueOf(path).length());
            sb.append("Unable to create a valid default Uri. ");
            sb.append(bucket);
            sb.append(path);
            Log.e("StorageMetadata", sb.toString(), e);
            throw new IllegalStateException(e);
        }
    }

    public long getSizeBytes() {
        return this.zzoit;
    }

    public long getUpdatedTimeMillis() {
        return zzexw.zzsk(this.zzois);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public final JSONObject zzclo() throws JSONException {
        HashMap hashMap = new HashMap();
        if (this.zzoip.zzclp()) {
            hashMap.put("contentType", getContentType());
        }
        if (this.zzoiz.zzclp()) {
            hashMap.put("metadata", new JSONObject((Map) this.zzoiz.getValue()));
        }
        if (this.zzoiv.zzclp()) {
            hashMap.put("cacheControl", getCacheControl());
        }
        if (this.zzoiw.zzclp()) {
            hashMap.put("contentDisposition", getContentDisposition());
        }
        if (this.zzoix.zzclp()) {
            hashMap.put("contentEncoding", getContentEncoding());
        }
        if (this.zzoiy.zzclp()) {
            hashMap.put("contentLanguage", getContentLanguage());
        }
        return new JSONObject(hashMap);
    }
}
