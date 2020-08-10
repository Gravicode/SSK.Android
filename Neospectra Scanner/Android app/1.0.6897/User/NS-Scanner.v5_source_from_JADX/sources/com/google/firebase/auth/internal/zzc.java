package com.google.firebase.auth.internal;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.gms.internal.zzdys;
import com.google.firebase.auth.ActionCodeResult;

public final class zzc implements ActionCodeResult {
    private final String zzegs;
    private final int zzikz;
    private final String zzmhd;

    public zzc(@NonNull zzdys zzdys) {
        int i;
        this.zzegs = TextUtils.isEmpty(zzdys.zzbry()) ? zzdys.getEmail() : zzdys.zzbry();
        this.zzmhd = zzdys.getEmail();
        if (!TextUtils.isEmpty(zzdys.zzbrz())) {
            if (zzdys.zzbrz().equals("PASSWORD_RESET")) {
                i = 0;
            } else if (zzdys.zzbrz().equals("VERIFY_EMAIL")) {
                i = 1;
            } else if (zzdys.zzbrz().equals("RECOVER_EMAIL")) {
                i = 2;
            } else {
                this.zzikz = 3;
                return;
            }
            this.zzikz = i;
            return;
        }
        this.zzikz = 3;
    }

    @Nullable
    public final String getData(int i) {
        switch (i) {
            case 0:
                return this.zzegs;
            case 1:
                return this.zzmhd;
            default:
                return null;
        }
    }

    public final int getOperation() {
        return this.zzikz;
    }
}
