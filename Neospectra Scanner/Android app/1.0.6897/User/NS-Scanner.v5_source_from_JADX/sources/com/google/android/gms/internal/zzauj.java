package com.google.android.gms.internal;

import android.accounts.Account;
import com.google.android.gms.common.api.Status;

final class zzauj extends zzaum {
    private /* synthetic */ zzaui zzect;

    zzauj(zzaui zzaui) {
        this.zzect = zzaui;
    }

    public final void zzd(Account account) {
        this.zzect.setResult(new zzaun(account != null ? Status.zzfni : zzaug.zzecr, account));
    }
}
