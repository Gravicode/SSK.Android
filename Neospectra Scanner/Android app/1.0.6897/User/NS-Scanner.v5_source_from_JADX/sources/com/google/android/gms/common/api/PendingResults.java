package com.google.android.gms.common.api;

import android.os.Looper;
import com.google.android.gms.common.api.internal.BasePendingResult;
import com.google.android.gms.common.api.internal.zzco;
import com.google.android.gms.common.api.internal.zzda;
import com.google.android.gms.common.internal.zzbq;

public final class PendingResults {

    static final class zza<R extends Result> extends BasePendingResult<R> {
        private final R zzfnd;

        public zza(R r) {
            super(Looper.getMainLooper());
            this.zzfnd = r;
        }

        /* access modifiers changed from: protected */
        public final R zzb(Status status) {
            if (status.getStatusCode() == this.zzfnd.getStatus().getStatusCode()) {
                return this.zzfnd;
            }
            throw new UnsupportedOperationException("Creating failed results is not supported");
        }
    }

    static final class zzb<R extends Result> extends BasePendingResult<R> {
        private final R zzfne;

        public zzb(GoogleApiClient googleApiClient, R r) {
            super(googleApiClient);
            this.zzfne = r;
        }

        /* access modifiers changed from: protected */
        public final R zzb(Status status) {
            return this.zzfne;
        }
    }

    static final class zzc<R extends Result> extends BasePendingResult<R> {
        public zzc(GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }

        /* access modifiers changed from: protected */
        public final R zzb(Status status) {
            throw new UnsupportedOperationException("Creating failed results is not supported");
        }
    }

    private PendingResults() {
    }

    public static PendingResult<Status> canceledPendingResult() {
        zzda zzda = new zzda(Looper.getMainLooper());
        zzda.cancel();
        return zzda;
    }

    public static <R extends Result> PendingResult<R> canceledPendingResult(R r) {
        zzbq.checkNotNull(r, "Result must not be null");
        zzbq.checkArgument(r.getStatus().getStatusCode() == 16, "Status code must be CommonStatusCodes.CANCELED");
        zza zza2 = new zza(r);
        zza2.cancel();
        return zza2;
    }

    public static <R extends Result> OptionalPendingResult<R> immediatePendingResult(R r) {
        zzbq.checkNotNull(r, "Result must not be null");
        zzc zzc2 = new zzc(null);
        zzc2.setResult(r);
        return new zzco(zzc2);
    }

    public static PendingResult<Status> immediatePendingResult(Status status) {
        zzbq.checkNotNull(status, "Result must not be null");
        zzda zzda = new zzda(Looper.getMainLooper());
        zzda.setResult(status);
        return zzda;
    }

    public static <R extends Result> PendingResult<R> zza(R r, GoogleApiClient googleApiClient) {
        zzbq.checkNotNull(r, "Result must not be null");
        zzbq.checkArgument(!r.getStatus().isSuccess(), "Status code must not be SUCCESS");
        zzb zzb2 = new zzb(googleApiClient, r);
        zzb2.setResult(r);
        return zzb2;
    }

    public static PendingResult<Status> zza(Status status, GoogleApiClient googleApiClient) {
        zzbq.checkNotNull(status, "Result must not be null");
        zzda zzda = new zzda(googleApiClient);
        zzda.setResult(status);
        return zzda;
    }

    public static <R extends Result> OptionalPendingResult<R> zzb(R r, GoogleApiClient googleApiClient) {
        zzbq.checkNotNull(r, "Result must not be null");
        zzc zzc2 = new zzc(googleApiClient);
        zzc2.setResult(r);
        return new zzco(zzc2);
    }
}
