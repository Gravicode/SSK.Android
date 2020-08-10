package com.google.android.gms.internal;

import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.PhoneAuthCredential;

public interface zzdxn extends IInterface {
    void onFailure(Status status) throws RemoteException;

    void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) throws RemoteException;

    void zza(Status status, PhoneAuthCredential phoneAuthCredential) throws RemoteException;

    void zza(zzdyi zzdyi) throws RemoteException;

    void zza(zzdym zzdym, zzdyk zzdyk) throws RemoteException;

    void zza(zzdys zzdys) throws RemoteException;

    void zzb(zzdym zzdym) throws RemoteException;

    void zzbrn() throws RemoteException;

    void zzbro() throws RemoteException;

    void zzbrp() throws RemoteException;

    void zzow(String str) throws RemoteException;

    void zzox(String str) throws RemoteException;

    void zzoy(String str) throws RemoteException;
}
