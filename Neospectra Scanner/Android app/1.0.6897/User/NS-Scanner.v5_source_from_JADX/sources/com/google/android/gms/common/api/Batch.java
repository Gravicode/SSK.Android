package com.google.android.gms.common.api;

import com.google.android.gms.common.api.internal.BasePendingResult;
import java.util.ArrayList;
import java.util.List;

public final class Batch extends BasePendingResult<BatchResult> {
    /* access modifiers changed from: private */
    public final Object mLock;
    /* access modifiers changed from: private */
    public int zzflx;
    /* access modifiers changed from: private */
    public boolean zzfly;
    /* access modifiers changed from: private */
    public boolean zzflz;
    /* access modifiers changed from: private */
    public final PendingResult<?>[] zzfma;

    public static final class Builder {
        private GoogleApiClient zzeuo;
        private List<PendingResult<?>> zzfmc = new ArrayList();

        public Builder(GoogleApiClient googleApiClient) {
            this.zzeuo = googleApiClient;
        }

        public final <R extends Result> BatchResultToken<R> add(PendingResult<R> pendingResult) {
            BatchResultToken<R> batchResultToken = new BatchResultToken<>(this.zzfmc.size());
            this.zzfmc.add(pendingResult);
            return batchResultToken;
        }

        public final Batch build() {
            return new Batch(this.zzfmc, this.zzeuo, null);
        }
    }

    private Batch(List<PendingResult<?>> list, GoogleApiClient googleApiClient) {
        super(googleApiClient);
        this.mLock = new Object();
        this.zzflx = list.size();
        this.zzfma = new PendingResult[this.zzflx];
        if (list.isEmpty()) {
            setResult(new BatchResult(Status.zzfni, this.zzfma));
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            PendingResult<?> pendingResult = (PendingResult) list.get(i);
            this.zzfma[i] = pendingResult;
            pendingResult.zza(new zza(this));
        }
    }

    /* synthetic */ Batch(List list, GoogleApiClient googleApiClient, zza zza) {
        this(list, googleApiClient);
    }

    public final void cancel() {
        super.cancel();
        for (PendingResult<?> cancel : this.zzfma) {
            cancel.cancel();
        }
    }

    /* renamed from: createFailedResult */
    public final BatchResult zzb(Status status) {
        return new BatchResult(status, this.zzfma);
    }
}
