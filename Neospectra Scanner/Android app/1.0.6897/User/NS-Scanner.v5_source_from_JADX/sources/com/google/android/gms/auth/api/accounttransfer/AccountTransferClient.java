package com.google.android.gms.auth.api.accounttransfer;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.zzf;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zzcz;
import com.google.android.gms.common.api.internal.zzdd;
import com.google.android.gms.common.api.internal.zzg;
import com.google.android.gms.common.api.zzd;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.internal.zzauw;
import com.google.android.gms.internal.zzauy;
import com.google.android.gms.internal.zzauz;
import com.google.android.gms.internal.zzavc;
import com.google.android.gms.internal.zzavd;
import com.google.android.gms.internal.zzavf;
import com.google.android.gms.internal.zzavh;
import com.google.android.gms.internal.zzavj;
import com.google.android.gms.internal.zzavl;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

public class AccountTransferClient extends GoogleApi<zzo> {
    private static final zzf<zzauy> zzedk = new zzf<>();
    private static final com.google.android.gms.common.api.Api.zza<zzauy, zzo> zzedl = new zzc();
    private static final Api<zzo> zzedm = new Api<>("AccountTransfer.ACCOUNT_TRANSFER_API", zzedl, zzedk);

    static class zza<T> extends zzauw {
        private zzb<T> zzedw;

        public zza(zzb<T> zzb) {
            this.zzedw = zzb;
        }

        public final void onFailure(Status status) {
            this.zzedw.zzd(status);
        }
    }

    static abstract class zzb<T> extends zzdd<zzauy, T> {
        private TaskCompletionSource<T> zzedx;

        private zzb() {
        }

        /* synthetic */ zzb(zzc zzc) {
            this();
        }

        /* access modifiers changed from: protected */
        public final void setResult(T t) {
            this.zzedx.setResult(t);
        }

        /* access modifiers changed from: protected */
        public final /* synthetic */ void zza(com.google.android.gms.common.api.Api.zzb zzb, TaskCompletionSource taskCompletionSource) throws RemoteException {
            zzauy zzauy = (zzauy) zzb;
            this.zzedx = taskCompletionSource;
            zza((zzavd) zzauy.zzakn());
        }

        /* access modifiers changed from: protected */
        public abstract void zza(zzavd zzavd) throws RemoteException;

        /* access modifiers changed from: protected */
        public final void zzd(Status status) {
            AccountTransferClient.zza(this.zzedx, status);
        }
    }

    static abstract class zzc extends zzb<Void> {
        zzavc zzedy;

        private zzc() {
            super(null);
            this.zzedy = new zzk(this);
        }

        /* synthetic */ zzc(zzc zzc) {
            this();
        }
    }

    AccountTransferClient(@NonNull Activity activity) {
        super(activity, zzedm, null, new zzd().zza((zzcz) new zzg()).zzagq());
    }

    AccountTransferClient(@NonNull Context context) {
        super(context, zzedm, null, new zzd().zza((zzcz) new zzg()).zzagq());
    }

    /* access modifiers changed from: private */
    public static void zza(TaskCompletionSource taskCompletionSource, Status status) {
        String str = "Exception with Status code=";
        String valueOf = String.valueOf(status.zzagx());
        taskCompletionSource.setException(new zzl(valueOf.length() != 0 ? str.concat(valueOf) : new String(str)));
    }

    public Task<DeviceMetaData> getDeviceMetaData(String str) {
        zzbq.checkNotNull(str);
        return zza((zzdd<A, TResult>) new zzg<A,TResult>(this, new zzauz(str)));
    }

    public Task<Void> notifyCompletion(String str, int i) {
        zzbq.checkNotNull(str);
        return zzb((zzdd<A, TResult>) new zzj<A,TResult>(this, new zzavf(str, i)));
    }

    public Task<byte[]> retrieveData(String str) {
        zzbq.checkNotNull(str);
        return zza((zzdd<A, TResult>) new zze<A,TResult>(this, new zzavh(str)));
    }

    public Task<Void> sendData(String str, byte[] bArr) {
        zzbq.checkNotNull(str);
        zzbq.checkNotNull(bArr);
        return zzb((zzdd<A, TResult>) new zzd<A,TResult>(this, new zzavj(str, bArr)));
    }

    public Task<Void> showUserChallenge(String str, PendingIntent pendingIntent) {
        zzbq.checkNotNull(str);
        zzbq.checkNotNull(pendingIntent);
        return zzb((zzdd<A, TResult>) new zzi<A,TResult>(this, new zzavl(str, pendingIntent)));
    }
}
