package com.google.firebase.storage;

import com.google.android.gms.tasks.OnSuccessListener;

final class zzq implements OnSuccessListener<TContinuationResult> {
    private /* synthetic */ zzp zzojt;

    zzq(zzp zzp) {
        this.zzojt = zzp;
    }

    public final void onSuccess(TContinuationResult tcontinuationresult) {
        this.zzojt.zzgbh.setResult(tcontinuationresult);
    }
}
