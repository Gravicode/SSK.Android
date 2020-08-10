package com.google.android.gms.common.api.internal;

import android.content.DialogInterface.OnCancelListener;
import android.support.annotation.MainThread;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiActivity;

final class zzq implements Runnable {
    private final zzp zzfop;
    final /* synthetic */ zzo zzfoq;

    zzq(zzo zzo, zzp zzp) {
        this.zzfoq = zzo;
        this.zzfop = zzp;
    }

    @MainThread
    public final void run() {
        if (this.zzfoq.mStarted) {
            ConnectionResult zzahf = this.zzfop.zzahf();
            if (zzahf.hasResolution()) {
                this.zzfoq.zzfud.startActivityForResult(GoogleApiActivity.zza(this.zzfoq.getActivity(), zzahf.getResolution(), this.zzfop.zzahe(), false), 1);
            } else if (this.zzfoq.zzfmy.isUserResolvableError(zzahf.getErrorCode())) {
                this.zzfoq.zzfmy.zza(this.zzfoq.getActivity(), this.zzfoq.zzfud, zzahf.getErrorCode(), 2, this.zzfoq);
            } else if (zzahf.getErrorCode() == 18) {
                GoogleApiAvailability.zza(this.zzfoq.getActivity().getApplicationContext(), (zzby) new zzr(this, GoogleApiAvailability.zza(this.zzfoq.getActivity(), (OnCancelListener) this.zzfoq)));
            } else {
                this.zzfoq.zza(zzahf, this.zzfop.zzahe());
            }
        }
    }
}
