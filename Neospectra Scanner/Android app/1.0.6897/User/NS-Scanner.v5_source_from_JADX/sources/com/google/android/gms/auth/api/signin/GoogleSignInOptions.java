package com.google.android.gms.auth.api.signin;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.gms.auth.api.signin.internal.zzn;
import com.google.android.gms.auth.api.signin.internal.zzp;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Api.ApiOptions.Optional;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.internal.zzbfm;
import com.google.android.gms.internal.zzbfp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoogleSignInOptions extends zzbfm implements Optional, ReflectedParcelable {
    public static final Creator<GoogleSignInOptions> CREATOR = new zze();
    public static final GoogleSignInOptions DEFAULT_GAMES_SIGN_IN = new Builder().requestScopes(SCOPE_GAMES_LITE, new Scope[0]).build();
    public static final GoogleSignInOptions DEFAULT_SIGN_IN = new Builder().requestId().requestProfile().build();
    public static final Scope SCOPE_GAMES = new Scope(Scopes.GAMES);
    public static final Scope SCOPE_GAMES_LITE = new Scope("https://www.googleapis.com/auth/games_lite");
    public static final Scope zzehi = new Scope(Scopes.PROFILE);
    public static final Scope zzehj = new Scope("email");
    public static final Scope zzehk = new Scope("openid");
    private static Comparator<Scope> zzehr = new zzd();
    private int versionCode;
    /* access modifiers changed from: private */
    public Account zzebz;
    /* access modifiers changed from: private */
    public boolean zzefl;
    /* access modifiers changed from: private */
    public String zzefm;
    /* access modifiers changed from: private */
    public final ArrayList<Scope> zzehl;
    /* access modifiers changed from: private */
    public final boolean zzehm;
    /* access modifiers changed from: private */
    public final boolean zzehn;
    /* access modifiers changed from: private */
    public String zzeho;
    /* access modifiers changed from: private */
    public ArrayList<zzn> zzehp;
    private Map<Integer, zzn> zzehq;

    public static final class Builder {
        private Account zzebz;
        private boolean zzefl;
        private String zzefm;
        private boolean zzehm;
        private boolean zzehn;
        private String zzeho;
        private Set<Scope> zzehs = new HashSet();
        private Map<Integer, zzn> zzeht = new HashMap();

        public Builder() {
        }

        public Builder(@NonNull GoogleSignInOptions googleSignInOptions) {
            zzbq.checkNotNull(googleSignInOptions);
            this.zzehs = new HashSet(googleSignInOptions.zzehl);
            this.zzehm = googleSignInOptions.zzehm;
            this.zzehn = googleSignInOptions.zzehn;
            this.zzefl = googleSignInOptions.zzefl;
            this.zzefm = googleSignInOptions.zzefm;
            this.zzebz = googleSignInOptions.zzebz;
            this.zzeho = googleSignInOptions.zzeho;
            this.zzeht = GoogleSignInOptions.zzx(googleSignInOptions.zzehp);
        }

        private final String zzew(String str) {
            zzbq.zzgm(str);
            zzbq.checkArgument(this.zzefm == null || this.zzefm.equals(str), "two different server client ids provided");
            return str;
        }

        public final Builder addExtension(GoogleSignInOptionsExtension googleSignInOptionsExtension) {
            if (this.zzeht.containsKey(Integer.valueOf(googleSignInOptionsExtension.getExtensionType()))) {
                throw new IllegalStateException("Only one extension per type may be added");
            }
            if (googleSignInOptionsExtension.getImpliedScopes() != null) {
                this.zzehs.addAll(googleSignInOptionsExtension.getImpliedScopes());
            }
            this.zzeht.put(Integer.valueOf(googleSignInOptionsExtension.getExtensionType()), new zzn(googleSignInOptionsExtension));
            return this;
        }

        public final GoogleSignInOptions build() {
            if (this.zzehs.contains(GoogleSignInOptions.SCOPE_GAMES) && this.zzehs.contains(GoogleSignInOptions.SCOPE_GAMES_LITE)) {
                this.zzehs.remove(GoogleSignInOptions.SCOPE_GAMES_LITE);
            }
            if (this.zzefl && (this.zzebz == null || !this.zzehs.isEmpty())) {
                requestId();
            }
            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions(3, new ArrayList(this.zzehs), this.zzebz, this.zzefl, this.zzehm, this.zzehn, this.zzefm, this.zzeho, this.zzeht, null);
            return googleSignInOptions;
        }

        public final Builder requestEmail() {
            this.zzehs.add(GoogleSignInOptions.zzehj);
            return this;
        }

        public final Builder requestId() {
            this.zzehs.add(GoogleSignInOptions.zzehk);
            return this;
        }

        public final Builder requestIdToken(String str) {
            this.zzefl = true;
            this.zzefm = zzew(str);
            return this;
        }

        public final Builder requestProfile() {
            this.zzehs.add(GoogleSignInOptions.zzehi);
            return this;
        }

        public final Builder requestScopes(Scope scope, Scope... scopeArr) {
            this.zzehs.add(scope);
            this.zzehs.addAll(Arrays.asList(scopeArr));
            return this;
        }

        public final Builder requestServerAuthCode(String str) {
            return requestServerAuthCode(str, false);
        }

        public final Builder requestServerAuthCode(String str, boolean z) {
            this.zzehm = true;
            this.zzefm = zzew(str);
            this.zzehn = z;
            return this;
        }

        public final Builder setAccountName(String str) {
            this.zzebz = new Account(zzbq.zzgm(str), "com.google");
            return this;
        }

        public final Builder setHostedDomain(String str) {
            this.zzeho = zzbq.zzgm(str);
            return this;
        }
    }

    GoogleSignInOptions(int i, ArrayList<Scope> arrayList, Account account, boolean z, boolean z2, boolean z3, String str, String str2, ArrayList<zzn> arrayList2) {
        this(i, arrayList, account, z, z2, z3, str, str2, zzx(arrayList2));
    }

    private GoogleSignInOptions(int i, ArrayList<Scope> arrayList, Account account, boolean z, boolean z2, boolean z3, String str, String str2, Map<Integer, zzn> map) {
        this.versionCode = i;
        this.zzehl = arrayList;
        this.zzebz = account;
        this.zzefl = z;
        this.zzehm = z2;
        this.zzehn = z3;
        this.zzefm = str;
        this.zzeho = str2;
        this.zzehp = new ArrayList<>(map.values());
        this.zzehq = map;
    }

    /* synthetic */ GoogleSignInOptions(int i, ArrayList arrayList, Account account, boolean z, boolean z2, boolean z3, String str, String str2, Map map, zzd zzd) {
        this(3, arrayList, account, z, z2, z3, str, str2, map);
    }

    private final JSONObject toJsonObject() {
        JSONObject jSONObject = new JSONObject();
        try {
            JSONArray jSONArray = new JSONArray();
            Collections.sort(this.zzehl, zzehr);
            ArrayList arrayList = this.zzehl;
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                jSONArray.put(((Scope) obj).zzagw());
            }
            jSONObject.put("scopes", jSONArray);
            if (this.zzebz != null) {
                jSONObject.put("accountName", this.zzebz.name);
            }
            jSONObject.put("idTokenRequested", this.zzefl);
            jSONObject.put("forceCodeForRefreshToken", this.zzehn);
            jSONObject.put("serverAuthRequested", this.zzehm);
            if (!TextUtils.isEmpty(this.zzefm)) {
                jSONObject.put("serverClientId", this.zzefm);
            }
            if (!TextUtils.isEmpty(this.zzeho)) {
                jSONObject.put("hostedDomain", this.zzeho);
            }
            return jSONObject;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public static GoogleSignInOptions zzev(@Nullable String str) throws JSONException {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        JSONObject jSONObject = new JSONObject(str);
        HashSet hashSet = new HashSet();
        JSONArray jSONArray = jSONObject.getJSONArray("scopes");
        int length = jSONArray.length();
        for (int i = 0; i < length; i++) {
            hashSet.add(new Scope(jSONArray.getString(i)));
        }
        String optString = jSONObject.optString("accountName", null);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions(3, new ArrayList<>(hashSet), !TextUtils.isEmpty(optString) ? new Account(optString, "com.google") : null, jSONObject.getBoolean("idTokenRequested"), jSONObject.getBoolean("serverAuthRequested"), jSONObject.getBoolean("forceCodeForRefreshToken"), jSONObject.optString("serverClientId", null), jSONObject.optString("hostedDomain", null), (Map<Integer, zzn>) new HashMap<Integer,zzn>());
        return googleSignInOptions;
    }

    /* access modifiers changed from: private */
    public static Map<Integer, zzn> zzx(@Nullable List<zzn> list) {
        HashMap hashMap = new HashMap();
        if (list == null) {
            return hashMap;
        }
        for (zzn zzn : list) {
            hashMap.put(Integer.valueOf(zzn.getType()), zzn);
        }
        return hashMap;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0045, code lost:
        if (r3.zzebz.equals(r4.zzebz) != false) goto L_0x0047;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0060, code lost:
        if (r3.zzefm.equals(r4.zzefm) != false) goto L_0x0062;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = 0
            if (r4 != 0) goto L_0x0004
            return r0
        L_0x0004:
            com.google.android.gms.auth.api.signin.GoogleSignInOptions r4 = (com.google.android.gms.auth.api.signin.GoogleSignInOptions) r4     // Catch:{ ClassCastException -> 0x0077 }
            java.util.ArrayList<com.google.android.gms.auth.api.signin.internal.zzn> r1 = r3.zzehp     // Catch:{ ClassCastException -> 0x0077 }
            int r1 = r1.size()     // Catch:{ ClassCastException -> 0x0077 }
            if (r1 > 0) goto L_0x0076
            java.util.ArrayList<com.google.android.gms.auth.api.signin.internal.zzn> r1 = r4.zzehp     // Catch:{ ClassCastException -> 0x0077 }
            int r1 = r1.size()     // Catch:{ ClassCastException -> 0x0077 }
            if (r1 <= 0) goto L_0x0017
            return r0
        L_0x0017:
            java.util.ArrayList<com.google.android.gms.common.api.Scope> r1 = r3.zzehl     // Catch:{ ClassCastException -> 0x0077 }
            int r1 = r1.size()     // Catch:{ ClassCastException -> 0x0077 }
            java.util.ArrayList r2 = r4.zzabe()     // Catch:{ ClassCastException -> 0x0077 }
            int r2 = r2.size()     // Catch:{ ClassCastException -> 0x0077 }
            if (r1 != r2) goto L_0x0076
            java.util.ArrayList<com.google.android.gms.common.api.Scope> r1 = r3.zzehl     // Catch:{ ClassCastException -> 0x0077 }
            java.util.ArrayList r2 = r4.zzabe()     // Catch:{ ClassCastException -> 0x0077 }
            boolean r1 = r1.containsAll(r2)     // Catch:{ ClassCastException -> 0x0077 }
            if (r1 != 0) goto L_0x0034
            return r0
        L_0x0034:
            android.accounts.Account r1 = r3.zzebz     // Catch:{ ClassCastException -> 0x0077 }
            if (r1 != 0) goto L_0x003d
            android.accounts.Account r1 = r4.zzebz     // Catch:{ ClassCastException -> 0x0077 }
            if (r1 != 0) goto L_0x0076
            goto L_0x0047
        L_0x003d:
            android.accounts.Account r1 = r3.zzebz     // Catch:{ ClassCastException -> 0x0077 }
            android.accounts.Account r2 = r4.zzebz     // Catch:{ ClassCastException -> 0x0077 }
            boolean r1 = r1.equals(r2)     // Catch:{ ClassCastException -> 0x0077 }
            if (r1 == 0) goto L_0x0076
        L_0x0047:
            java.lang.String r1 = r3.zzefm     // Catch:{ ClassCastException -> 0x0077 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ ClassCastException -> 0x0077 }
            if (r1 == 0) goto L_0x0058
            java.lang.String r1 = r4.zzefm     // Catch:{ ClassCastException -> 0x0077 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ ClassCastException -> 0x0077 }
            if (r1 == 0) goto L_0x0076
            goto L_0x0062
        L_0x0058:
            java.lang.String r1 = r3.zzefm     // Catch:{ ClassCastException -> 0x0077 }
            java.lang.String r2 = r4.zzefm     // Catch:{ ClassCastException -> 0x0077 }
            boolean r1 = r1.equals(r2)     // Catch:{ ClassCastException -> 0x0077 }
            if (r1 == 0) goto L_0x0076
        L_0x0062:
            boolean r1 = r3.zzehn     // Catch:{ ClassCastException -> 0x0077 }
            boolean r2 = r4.zzehn     // Catch:{ ClassCastException -> 0x0077 }
            if (r1 != r2) goto L_0x0076
            boolean r1 = r3.zzefl     // Catch:{ ClassCastException -> 0x0077 }
            boolean r2 = r4.zzefl     // Catch:{ ClassCastException -> 0x0077 }
            if (r1 != r2) goto L_0x0076
            boolean r1 = r3.zzehm     // Catch:{ ClassCastException -> 0x0077 }
            boolean r4 = r4.zzehm     // Catch:{ ClassCastException -> 0x0077 }
            if (r1 != r4) goto L_0x0076
            r4 = 1
            return r4
        L_0x0076:
            return r0
        L_0x0077:
            r4 = move-exception
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.api.signin.GoogleSignInOptions.equals(java.lang.Object):boolean");
    }

    public final Account getAccount() {
        return this.zzebz;
    }

    public Scope[] getScopeArray() {
        return (Scope[]) this.zzehl.toArray(new Scope[this.zzehl.size()]);
    }

    public final String getServerClientId() {
        return this.zzefm;
    }

    public int hashCode() {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = this.zzehl;
        int size = arrayList2.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList2.get(i);
            i++;
            arrayList.add(((Scope) obj).zzagw());
        }
        Collections.sort(arrayList);
        return new zzp().zzs(arrayList).zzs(this.zzebz).zzs(this.zzefm).zzar(this.zzehn).zzar(this.zzefl).zzar(this.zzehm).zzabn();
    }

    public final boolean isIdTokenRequested() {
        return this.zzefl;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        zzbfp.zzc(parcel, 1, this.versionCode);
        zzbfp.zzc(parcel, 2, zzabe(), false);
        zzbfp.zza(parcel, 3, (Parcelable) this.zzebz, i, false);
        zzbfp.zza(parcel, 4, this.zzefl);
        zzbfp.zza(parcel, 5, this.zzehm);
        zzbfp.zza(parcel, 6, this.zzehn);
        zzbfp.zza(parcel, 7, this.zzefm, false);
        zzbfp.zza(parcel, 8, this.zzeho, false);
        zzbfp.zzc(parcel, 9, this.zzehp, false);
        zzbfp.zzai(parcel, zze);
    }

    public final ArrayList<Scope> zzabe() {
        return new ArrayList<>(this.zzehl);
    }

    public final boolean zzabf() {
        return this.zzehm;
    }

    public final String zzabg() {
        return toJsonObject().toString();
    }
}
