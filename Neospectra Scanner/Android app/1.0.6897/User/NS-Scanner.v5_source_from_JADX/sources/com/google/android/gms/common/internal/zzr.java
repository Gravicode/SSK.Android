package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.content.Context;
import android.view.View;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.internal.zzcxe;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class zzr {
    private final String zzebs;
    private final Account zzebz;
    private final Set<Scope> zzfmo;
    private final int zzfmq;
    private final View zzfmr;
    private final String zzfms;
    private final Set<Scope> zzfzg;
    private final Map<Api<?>, zzt> zzfzh;
    private final zzcxe zzfzi;
    private Integer zzfzj;

    public zzr(Account account, Set<Scope> set, Map<Api<?>, zzt> map, int i, View view, String str, String str2, zzcxe zzcxe) {
        this.zzebz = account;
        this.zzfmo = set == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(set);
        if (map == null) {
            map = Collections.EMPTY_MAP;
        }
        this.zzfzh = map;
        this.zzfmr = view;
        this.zzfmq = i;
        this.zzebs = str;
        this.zzfms = str2;
        this.zzfzi = zzcxe;
        HashSet hashSet = new HashSet(this.zzfmo);
        for (zzt zzt : this.zzfzh.values()) {
            hashSet.addAll(zzt.zzehs);
        }
        this.zzfzg = Collections.unmodifiableSet(hashSet);
    }

    public static zzr zzcl(Context context) {
        return new Builder(context).zzagu();
    }

    public final Account getAccount() {
        return this.zzebz;
    }

    @Deprecated
    public final String getAccountName() {
        if (this.zzebz != null) {
            return this.zzebz.name;
        }
        return null;
    }

    public final Account zzakt() {
        return this.zzebz != null ? this.zzebz : new Account("<<default account>>", "com.google");
    }

    public final int zzaku() {
        return this.zzfmq;
    }

    public final Set<Scope> zzakv() {
        return this.zzfmo;
    }

    public final Set<Scope> zzakw() {
        return this.zzfzg;
    }

    public final Map<Api<?>, zzt> zzakx() {
        return this.zzfzh;
    }

    public final String zzaky() {
        return this.zzebs;
    }

    public final String zzakz() {
        return this.zzfms;
    }

    public final View zzala() {
        return this.zzfmr;
    }

    public final zzcxe zzalb() {
        return this.zzfzi;
    }

    public final Integer zzalc() {
        return this.zzfzj;
    }

    public final Set<Scope> zzc(Api<?> api) {
        zzt zzt = (zzt) this.zzfzh.get(api);
        if (zzt == null || zzt.zzehs.isEmpty()) {
            return this.zzfmo;
        }
        HashSet hashSet = new HashSet(this.zzfmo);
        hashSet.addAll(zzt.zzehs);
        return hashSet;
    }

    public final void zzc(Integer num) {
        this.zzfzj = num;
    }
}
