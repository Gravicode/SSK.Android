package com.google.firebase.storage;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p001v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.internal.zzexr;
import com.google.android.gms.internal.zzeyc;
import com.google.firebase.storage.StorageTask.ProvideError;
import com.google.firebase.storage.StorageTask.SnapshotBase;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

public class StreamDownloadTask extends StorageTask<TaskSnapshot> {
    private volatile int mResultCode = 0;
    private long zzgmb = -1;
    private volatile Exception zzkuq = null;
    /* access modifiers changed from: private */
    public InputStream zzljj;
    private StorageReference zzoht;
    private zzexr zzohv;
    private long zzohx;
    private String zzohy;
    private StreamProcessor zzokh;
    private long zzoki;
    /* access modifiers changed from: private */
    public zzeyc zzokj;

    public interface StreamProcessor {
        void doInBackground(TaskSnapshot taskSnapshot, InputStream inputStream) throws IOException;
    }

    public class TaskSnapshot extends SnapshotBase {
        private final long zzohx;

        TaskSnapshot(@Nullable Exception exc, long j) {
            super(exc);
            this.zzohx = j;
        }

        public long getBytesTransferred() {
            return this.zzohx;
        }

        public InputStream getStream() {
            return StreamDownloadTask.this.zzljj;
        }

        public long getTotalByteCount() {
            return StreamDownloadTask.this.getTotalBytes();
        }
    }

    static class zza extends InputStream {
        @Nullable
        private StreamDownloadTask zzokl;
        @Nullable
        private InputStream zzokm;
        private Callable<InputStream> zzokn;
        private IOException zzoko;
        private int zzokp;
        private int zzokq;
        private boolean zzokr;

        zza(@NonNull Callable<InputStream> callable, @Nullable StreamDownloadTask streamDownloadTask) {
            this.zzokl = streamDownloadTask;
            this.zzokn = callable;
        }

        private final void zzclz() throws IOException {
            if (this.zzokl != null && this.zzokl.zzclt() == 32) {
                throw new zza();
            }
        }

        /* access modifiers changed from: private */
        public final boolean zzcma() throws IOException {
            zzclz();
            if (this.zzoko != null) {
                try {
                    if (this.zzokm != null) {
                        this.zzokm.close();
                    }
                } catch (IOException e) {
                }
                this.zzokm = null;
                if (this.zzokq == this.zzokp) {
                    Log.i("StreamDownloadTask", "Encountered exception during stream operation. Aborting.", this.zzoko);
                    return false;
                }
                int i = this.zzokp;
                StringBuilder sb = new StringBuilder(70);
                sb.append("Encountered exception during stream operation. Retrying at ");
                sb.append(i);
                Log.i("StreamDownloadTask", sb.toString(), this.zzoko);
                this.zzokq = this.zzokp;
                this.zzoko = null;
            }
            if (this.zzokr) {
                throw new IOException("Can't perform operation on closed stream");
            }
            if (this.zzokm == null) {
                try {
                    this.zzokm = (InputStream) this.zzokn.call();
                } catch (Exception e2) {
                    if (e2 instanceof IOException) {
                        throw ((IOException) e2);
                    }
                    throw new IOException("Unable to open stream", e2);
                }
            }
            return true;
        }

        private final void zzcp(long j) {
            if (this.zzokl != null) {
                this.zzokl.zzcp(j);
            }
            this.zzokp = (int) (((long) this.zzokp) + j);
        }

        public final int available() throws IOException {
            while (zzcma()) {
                try {
                    return this.zzokm.available();
                } catch (IOException e) {
                    this.zzoko = e;
                }
            }
            throw this.zzoko;
        }

        public final void close() throws IOException {
            if (this.zzokm != null) {
                this.zzokm.close();
            }
            this.zzokr = true;
            if (!(this.zzokl == null || this.zzokl.zzokj == null)) {
                this.zzokl.zzokj.zzcmh();
                this.zzokl.zzokj = null;
            }
            zzclz();
        }

        public final void mark(int i) {
        }

        public final boolean markSupported() {
            return false;
        }

        public final int read() throws IOException {
            while (zzcma()) {
                try {
                    int read = this.zzokm.read();
                    if (read != -1) {
                        zzcp(1);
                    }
                    return read;
                } catch (IOException e) {
                    this.zzoko = e;
                }
            }
            throw this.zzoko;
        }

        public final int read(@NonNull byte[] bArr, int i, int i2) throws IOException {
            int i3 = 0;
            while (zzcma()) {
                while (((long) i2) > PlaybackStateCompat.ACTION_SET_REPEAT_MODE) {
                    try {
                        int read = this.zzokm.read(bArr, i, 262144);
                        if (read != -1) {
                            i3 += read;
                            i += read;
                            i2 -= read;
                            zzcp((long) read);
                            zzclz();
                        } else if (i3 == 0) {
                            return -1;
                        } else {
                            return i3;
                        }
                    } catch (IOException e) {
                        this.zzoko = e;
                    }
                }
                if (i2 > 0) {
                    int read2 = this.zzokm.read(bArr, i, i2);
                    if (read2 != -1) {
                        i += read2;
                        i3 += read2;
                        i2 -= read2;
                        zzcp((long) read2);
                    } else if (i3 == 0) {
                        return -1;
                    } else {
                        return i3;
                    }
                }
                if (i2 == 0) {
                    return i3;
                }
            }
            throw this.zzoko;
        }

        public final long skip(long j) throws IOException {
            int i = 0;
            while (zzcma()) {
                while (j > PlaybackStateCompat.ACTION_SET_REPEAT_MODE) {
                    try {
                        long skip = this.zzokm.skip(PlaybackStateCompat.ACTION_SET_REPEAT_MODE);
                        if (skip >= 0) {
                            i = (int) (((long) i) + skip);
                            j -= skip;
                            zzcp(skip);
                            zzclz();
                        } else if (i == 0) {
                            return -1;
                        } else {
                            return (long) i;
                        }
                    } catch (IOException e) {
                        this.zzoko = e;
                    }
                }
                if (j > 0) {
                    long skip2 = this.zzokm.skip(j);
                    if (skip2 >= 0) {
                        i = (int) (((long) i) + skip2);
                        j -= skip2;
                        zzcp(skip2);
                    } else if (i == 0) {
                        return -1;
                    } else {
                        return (long) i;
                    }
                }
                if (j == 0) {
                    return (long) i;
                }
            }
            throw this.zzoko;
        }
    }

    StreamDownloadTask(@NonNull StorageReference storageReference) {
        this.zzoht = storageReference;
        this.zzohv = new zzexr(this.zzoht.getStorage().getApp(), this.zzoht.getStorage().getMaxDownloadRetryTimeMillis());
    }

    /* access modifiers changed from: private */
    public final InputStream zzcly() throws Exception {
        this.zzohv.reset();
        if (this.zzokj != null) {
            this.zzokj.zzcmh();
        }
        try {
            this.zzokj = this.zzoht.zzclq().zza(this.zzoht.zzclr(), this.zzohx);
            boolean z = false;
            this.zzohv.zza(this.zzokj, false);
            this.mResultCode = this.zzokj.getResultCode();
            this.zzkuq = this.zzokj.getException() != null ? this.zzokj.getException() : this.zzkuq;
            int i = this.mResultCode;
            if ((i == 308 || (i >= 200 && i < 300)) && this.zzkuq == null && zzclt() == 4) {
                z = true;
            }
            if (z) {
                String zzsn = this.zzokj.zzsn("ETag");
                if (TextUtils.isEmpty(zzsn) || this.zzohy == null || this.zzohy.equals(zzsn)) {
                    this.zzohy = zzsn;
                    if (this.zzgmb == -1) {
                        this.zzgmb = (long) this.zzokj.zzcmn();
                    }
                    return this.zzokj.getStream();
                }
                this.mResultCode = 409;
                throw new IOException("The ETag on the server changed.");
            }
            throw new IOException("Could not open resulting stream.");
        } catch (RemoteException e) {
            Log.e("StreamDownloadTask", "Unable to create firebase storage network request.", e);
            throw e;
        }
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public final StorageReference getStorage() {
        return this.zzoht;
    }

    /* access modifiers changed from: 0000 */
    public final long getTotalBytes() {
        return this.zzgmb;
    }

    /* access modifiers changed from: protected */
    public void onCanceled() {
        this.zzohv.cancel();
        this.zzkuq = StorageException.fromErrorStatus(Status.zzfnm);
    }

    /* access modifiers changed from: protected */
    public void onProgress() {
        this.zzoki = this.zzohx;
    }

    public boolean pause() {
        throw new UnsupportedOperationException("this operation is not supported on StreamDownloadTask.");
    }

    public boolean resume() {
        throw new UnsupportedOperationException("this operation is not supported on StreamDownloadTask.");
    }

    /* access modifiers changed from: 0000 */
    public final void run() {
        int i = 64;
        if (this.zzkuq != null) {
            zzk(64, false);
        } else if (zzk(4, false)) {
            zza zza2 = new zza(new zzw(this), this);
            this.zzljj = new BufferedInputStream(zza2);
            try {
                zza2.zzcma();
                if (this.zzokh != null) {
                    try {
                        this.zzokh.doInBackground((TaskSnapshot) zzclu(), this.zzljj);
                    } catch (Exception e) {
                        Log.w("StreamDownloadTask", "Exception occurred calling doInBackground.", e);
                        this.zzkuq = e;
                    }
                }
            } catch (IOException e2) {
                Log.d("StreamDownloadTask", "Initial opening of Stream failed", e2);
                this.zzkuq = e2;
            }
            if (this.zzljj == null) {
                this.zzokj.zzcmh();
                this.zzokj = null;
            }
            if (this.zzkuq == null && zzclt() == 4) {
                zzk(4, false);
                zzk(128, false);
                return;
            }
            if (zzclt() == 32) {
                i = 256;
            }
            if (!zzk(i, false)) {
                int zzclt = zzclt();
                StringBuilder sb = new StringBuilder(62);
                sb.append("Unable to change download task to final state from ");
                sb.append(zzclt);
                Log.w("StreamDownloadTask", sb.toString());
            }
        }
    }

    /* access modifiers changed from: protected */
    public void schedule() {
        zzu.zzu(zzbko());
    }

    /* access modifiers changed from: 0000 */
    public final StreamDownloadTask zza(@NonNull StreamProcessor streamProcessor) {
        zzbq.checkNotNull(streamProcessor);
        zzbq.checkState(this.zzokh == null);
        this.zzokh = streamProcessor;
        return this;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public final /* synthetic */ ProvideError zzcln() {
        return new TaskSnapshot(StorageException.fromExceptionAndHttpCode(this.zzkuq, this.mResultCode), this.zzoki);
    }

    /* access modifiers changed from: 0000 */
    public final void zzcp(long j) {
        this.zzohx += j;
        if (this.zzoki + PlaybackStateCompat.ACTION_SET_REPEAT_MODE <= this.zzohx) {
            if (zzclt() == 4) {
                zzk(4, false);
                return;
            }
            this.zzoki = this.zzohx;
        }
    }
}
