package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public class zzeu implements IInterface {
    private final IBinder zzalc;
    private final String zzald;

    protected zzeu(IBinder iBinder, String str) {
        this.zzalc = iBinder;
        this.zzald = str;
    }

    public IBinder asBinder() {
        return this.zzalc;
    }

    /* access modifiers changed from: protected */
    public final Parcel zza(int i, Parcel parcel) throws RemoteException {
        parcel = Parcel.obtain();
        try {
            this.zzalc.transact(i, parcel, parcel, 0);
            parcel.readException();
            return parcel;
        } catch (RuntimeException e) {
            throw e;
        } finally {
            parcel.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public final void zzb(int i, Parcel parcel) throws RemoteException {
        Parcel obtain = Parcel.obtain();
        try {
            this.zzalc.transact(i, parcel, obtain, 0);
            obtain.readException();
        } finally {
            parcel.recycle();
            obtain.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public final Parcel zzbe() {
        Parcel obtain = Parcel.obtain();
        obtain.writeInterfaceToken(this.zzald);
        return obtain;
    }

    /* access modifiers changed from: protected */
    public final void zzc(int i, Parcel parcel) throws RemoteException {
        try {
            this.zzalc.transact(i, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
    }
}
