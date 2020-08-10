package com.google.android.gms.internal;

import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken;
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;

final class zzdyb implements zzdyg {
    private /* synthetic */ String zzmfz;

    zzdyb(zzdya zzdya, String str) {
        this.zzmfz = str;
    }

    public final void zza(OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks, Object... objArr) {
        onVerificationStateChangedCallbacks.onCodeSent(this.zzmfz, ForceResendingToken.zzbri());
    }
}
