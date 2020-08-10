package com.google.android.gms.internal;

import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;

final class zzdyd implements zzdyg {
    private /* synthetic */ String zzmfz;

    zzdyd(zzdya zzdya, String str) {
        this.zzmfz = str;
    }

    public final void zza(OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks, Object... objArr) {
        onVerificationStateChangedCallbacks.onCodeAutoRetrievalTimeOut(this.zzmfz);
    }
}
