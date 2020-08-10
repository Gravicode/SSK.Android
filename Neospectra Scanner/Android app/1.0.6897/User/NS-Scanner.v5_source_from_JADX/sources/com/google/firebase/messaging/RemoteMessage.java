package com.google.firebase.messaging;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.p001v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.internal.zzbfm;
import com.google.android.gms.internal.zzbfp;
import java.util.Map;
import java.util.Map.Entry;

public final class RemoteMessage extends zzbfm {
    public static final Creator<RemoteMessage> CREATOR = new zzf();
    Bundle mBundle;
    private Map<String, String> zzdpq;
    private Notification zzoah;

    public static class Builder {
        private final Bundle mBundle = new Bundle();
        private final Map<String, String> zzdpq = new ArrayMap();

        public Builder(String str) {
            if (TextUtils.isEmpty(str)) {
                String str2 = "Invalid to: ";
                String valueOf = String.valueOf(str);
                throw new IllegalArgumentException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
            }
            this.mBundle.putString("google.to", str);
        }

        public Builder addData(String str, String str2) {
            this.zzdpq.put(str, str2);
            return this;
        }

        public RemoteMessage build() {
            Bundle bundle = new Bundle();
            for (Entry entry : this.zzdpq.entrySet()) {
                bundle.putString((String) entry.getKey(), (String) entry.getValue());
            }
            bundle.putAll(this.mBundle);
            this.mBundle.remove("from");
            return new RemoteMessage(bundle);
        }

        public Builder clearData() {
            this.zzdpq.clear();
            return this;
        }

        public Builder setCollapseKey(String str) {
            this.mBundle.putString("collapse_key", str);
            return this;
        }

        public Builder setData(Map<String, String> map) {
            this.zzdpq.clear();
            this.zzdpq.putAll(map);
            return this;
        }

        public Builder setMessageId(String str) {
            this.mBundle.putString("google.message_id", str);
            return this;
        }

        public Builder setMessageType(String str) {
            this.mBundle.putString("message_type", str);
            return this;
        }

        public Builder setTtl(@IntRange(from = 0, mo190to = 86400) int i) {
            this.mBundle.putString("google.ttl", String.valueOf(i));
            return this;
        }
    }

    public static class Notification {
        private final String mTag;
        private final String zzbtu;
        private final String zzemt;
        private final String zzoai;
        private final String[] zzoaj;
        private final String zzoak;
        private final String[] zzoal;
        private final String zzoam;
        private final String zzoan;
        private final String zzoao;
        private final String zzoap;
        private final Uri zzoaq;

        private Notification(Bundle bundle) {
            this.zzemt = zza.zze(bundle, "gcm.n.title");
            this.zzoai = zza.zzh(bundle, "gcm.n.title");
            this.zzoaj = zzk(bundle, "gcm.n.title");
            this.zzbtu = zza.zze(bundle, "gcm.n.body");
            this.zzoak = zza.zzh(bundle, "gcm.n.body");
            this.zzoal = zzk(bundle, "gcm.n.body");
            this.zzoam = zza.zze(bundle, "gcm.n.icon");
            this.zzoan = zza.zzai(bundle);
            this.mTag = zza.zze(bundle, "gcm.n.tag");
            this.zzoao = zza.zze(bundle, "gcm.n.color");
            this.zzoap = zza.zze(bundle, "gcm.n.click_action");
            this.zzoaq = zza.zzah(bundle);
        }

        private static String[] zzk(Bundle bundle, String str) {
            Object[] zzi = zza.zzi(bundle, str);
            if (zzi == null) {
                return null;
            }
            String[] strArr = new String[zzi.length];
            for (int i = 0; i < zzi.length; i++) {
                strArr[i] = String.valueOf(zzi[i]);
            }
            return strArr;
        }

        @Nullable
        public String getBody() {
            return this.zzbtu;
        }

        @Nullable
        public String[] getBodyLocalizationArgs() {
            return this.zzoal;
        }

        @Nullable
        public String getBodyLocalizationKey() {
            return this.zzoak;
        }

        @Nullable
        public String getClickAction() {
            return this.zzoap;
        }

        @Nullable
        public String getColor() {
            return this.zzoao;
        }

        @Nullable
        public String getIcon() {
            return this.zzoam;
        }

        @Nullable
        public Uri getLink() {
            return this.zzoaq;
        }

        @Nullable
        public String getSound() {
            return this.zzoan;
        }

        @Nullable
        public String getTag() {
            return this.mTag;
        }

        @Nullable
        public String getTitle() {
            return this.zzemt;
        }

        @Nullable
        public String[] getTitleLocalizationArgs() {
            return this.zzoaj;
        }

        @Nullable
        public String getTitleLocalizationKey() {
            return this.zzoai;
        }
    }

    RemoteMessage(Bundle bundle) {
        this.mBundle = bundle;
    }

    @Nullable
    public final String getCollapseKey() {
        return this.mBundle.getString("collapse_key");
    }

    public final Map<String, String> getData() {
        if (this.zzdpq == null) {
            this.zzdpq = new ArrayMap();
            for (String str : this.mBundle.keySet()) {
                Object obj = this.mBundle.get(str);
                if (obj instanceof String) {
                    String str2 = (String) obj;
                    if (!str.startsWith("google.") && !str.startsWith("gcm.") && !str.equals("from") && !str.equals("message_type") && !str.equals("collapse_key")) {
                        this.zzdpq.put(str, str2);
                    }
                }
            }
        }
        return this.zzdpq;
    }

    @Nullable
    public final String getFrom() {
        return this.mBundle.getString("from");
    }

    @Nullable
    public final String getMessageId() {
        String string = this.mBundle.getString("google.message_id");
        return string == null ? this.mBundle.getString("message_id") : string;
    }

    @Nullable
    public final String getMessageType() {
        return this.mBundle.getString("message_type");
    }

    @Nullable
    public final Notification getNotification() {
        if (this.zzoah == null && zza.zzag(this.mBundle)) {
            this.zzoah = new Notification(this.mBundle);
        }
        return this.zzoah;
    }

    public final long getSentTime() {
        Object obj = this.mBundle.get("google.sent_time");
        if (obj instanceof Long) {
            return ((Long) obj).longValue();
        }
        if (obj instanceof String) {
            try {
                return Long.parseLong((String) obj);
            } catch (NumberFormatException e) {
                String valueOf = String.valueOf(obj);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 19);
                sb.append("Invalid sent time: ");
                sb.append(valueOf);
                Log.w("FirebaseMessaging", sb.toString());
            }
        }
        return 0;
    }

    @Nullable
    public final String getTo() {
        return this.mBundle.getString("google.to");
    }

    public final int getTtl() {
        Object obj = this.mBundle.get("google.ttl");
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        if (obj instanceof String) {
            try {
                return Integer.parseInt((String) obj);
            } catch (NumberFormatException e) {
                String valueOf = String.valueOf(obj);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 13);
                sb.append("Invalid TTL: ");
                sb.append(valueOf);
                Log.w("FirebaseMessaging", sb.toString());
            }
        }
        return 0;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        zzbfp.zza(parcel, 2, this.mBundle, false);
        zzbfp.zzai(parcel, zze);
    }
}
