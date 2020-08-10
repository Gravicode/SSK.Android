package com.google.firebase.storage;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzexr;
import com.google.android.gms.internal.zzeyc;
import com.google.firebase.storage.StorageTask.ProvideError;
import com.google.firebase.storage.StorageTask.SnapshotBase;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileDownloadTask extends StorageTask<TaskSnapshot> {
    private int mResultCode;
    private long zzgmb = -1;
    private volatile Exception zzkuq = null;
    private StorageReference zzoht;
    private zzexr zzohv;
    private final Uri zzohw;
    private long zzohx;
    private String zzohy = null;
    private long zzohz = 0;

    public class TaskSnapshot extends SnapshotBase {
        private final long zzohx;

        TaskSnapshot(@Nullable Exception exc, long j) {
            super(exc);
            this.zzohx = j;
        }

        public long getBytesTransferred() {
            return this.zzohx;
        }

        public long getTotalByteCount() {
            return FileDownloadTask.this.getTotalBytes();
        }
    }

    FileDownloadTask(@NonNull StorageReference storageReference, @NonNull Uri uri) {
        this.zzoht = storageReference;
        this.zzohw = uri;
        this.zzohv = new zzexr(this.zzoht.getStorage().getApp(), this.zzoht.getStorage().getMaxDownloadRetryTimeMillis());
    }

    private final int zza(InputStream inputStream, byte[] bArr) {
        int i = 0;
        boolean z = false;
        while (i != bArr.length) {
            try {
                int read = inputStream.read(bArr, i, bArr.length - i);
                if (read == -1) {
                    break;
                }
                z = true;
                i += read;
            } catch (IOException e) {
                this.zzkuq = e;
            }
        }
        if (z) {
            return i;
        }
        return -1;
    }

    /* JADX INFO: finally extract failed */
    private final boolean zza(zzeyc zzeyc) throws IOException {
        FileOutputStream fileOutputStream;
        InputStream stream = zzeyc.getStream();
        if (stream != null) {
            File file = new File(this.zzohw.getPath());
            if (!file.exists()) {
                if (this.zzohz > 0) {
                    String str = "FileDownloadTask";
                    String str2 = "The file downloading to has been deleted:";
                    String valueOf = String.valueOf(file.getAbsolutePath());
                    Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
                    throw new IllegalStateException("expected a file to resume from.");
                } else if (!file.createNewFile()) {
                    String str3 = "FileDownloadTask";
                    String str4 = "unable to create file:";
                    String valueOf2 = String.valueOf(file.getAbsolutePath());
                    Log.w(str3, valueOf2.length() != 0 ? str4.concat(valueOf2) : new String(str4));
                }
            }
            boolean z = true;
            if (this.zzohz > 0) {
                String absolutePath = file.getAbsolutePath();
                long j = this.zzohz;
                StringBuilder sb = new StringBuilder(String.valueOf(absolutePath).length() + 47);
                sb.append("Resuming download file ");
                sb.append(absolutePath);
                sb.append(" at ");
                sb.append(j);
                Log.d("FileDownloadTask", sb.toString());
                fileOutputStream = new FileOutputStream(file, true);
            } else {
                fileOutputStream = new FileOutputStream(file);
            }
            try {
                byte[] bArr = new byte[262144];
                while (z) {
                    int zza = zza(stream, bArr);
                    if (zza == -1) {
                        break;
                    }
                    fileOutputStream.write(bArr, 0, zza);
                    this.zzohx += (long) zza;
                    if (this.zzkuq != null) {
                        Log.d("FileDownloadTask", "Exception occurred during file download. Retrying.", this.zzkuq);
                        this.zzkuq = null;
                        z = false;
                    }
                    if (!zzk(4, false)) {
                        z = false;
                    }
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                stream.close();
                return z;
            } catch (Throwable th) {
                fileOutputStream.flush();
                fileOutputStream.close();
                stream.close();
                throw th;
            }
        } else {
            this.zzkuq = new IllegalStateException("Unable to open Firebase Storage stream.");
            return false;
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

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00d3  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x00cd A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
            r12 = this;
            java.lang.Exception r0 = r12.zzkuq
            r1 = 64
            r2 = 0
            if (r0 == 0) goto L_0x000b
            r12.zzk(r1, r2)
            return
        L_0x000b:
            r0 = 4
            boolean r3 = r12.zzk(r0, r2)
            if (r3 != 0) goto L_0x0013
            return
        L_0x0013:
            r3 = 0
            r12.zzohx = r3
            r5 = 0
            r12.zzkuq = r5
            com.google.android.gms.internal.zzexr r6 = r12.zzohv
            r6.reset()
            com.google.firebase.storage.StorageReference r6 = r12.zzoht     // Catch:{ RemoteException -> 0x0132 }
            com.google.firebase.storage.FirebaseStorage r6 = r6.getStorage()     // Catch:{ RemoteException -> 0x0132 }
            com.google.firebase.FirebaseApp r6 = r6.getApp()     // Catch:{ RemoteException -> 0x0132 }
            com.google.android.gms.internal.zzeyb r6 = com.google.android.gms.internal.zzeyb.zzi(r6)     // Catch:{ RemoteException -> 0x0132 }
            com.google.firebase.storage.StorageReference r7 = r12.zzoht     // Catch:{ RemoteException -> 0x0132 }
            android.net.Uri r7 = r7.zzclr()     // Catch:{ RemoteException -> 0x0132 }
            long r8 = r12.zzohz     // Catch:{ RemoteException -> 0x0132 }
            com.google.android.gms.internal.zzeyc r6 = r6.zza(r7, r8)     // Catch:{ RemoteException -> 0x0132 }
            com.google.android.gms.internal.zzexr r7 = r12.zzohv
            r7.zza(r6, r2)
            int r7 = r6.getResultCode()
            r12.mResultCode = r7
            java.lang.Exception r7 = r6.getException()
            if (r7 == 0) goto L_0x004f
            java.lang.Exception r7 = r6.getException()
            goto L_0x0051
        L_0x004f:
            java.lang.Exception r7 = r12.zzkuq
        L_0x0051:
            r12.zzkuq = r7
            int r7 = r12.mResultCode
            r8 = 308(0x134, float:4.32E-43)
            r9 = 1
            if (r7 == r8) goto L_0x0065
            r8 = 200(0xc8, float:2.8E-43)
            if (r7 < r8) goto L_0x0063
            r8 = 300(0x12c, float:4.2E-43)
            if (r7 >= r8) goto L_0x0063
            goto L_0x0065
        L_0x0063:
            r7 = 0
            goto L_0x0066
        L_0x0065:
            r7 = 1
        L_0x0066:
            if (r7 == 0) goto L_0x0074
            java.lang.Exception r7 = r12.zzkuq
            if (r7 != 0) goto L_0x0074
            int r7 = r12.zzclt()
            if (r7 != r0) goto L_0x0074
            r7 = 1
            goto L_0x0075
        L_0x0074:
            r7 = 0
        L_0x0075:
            if (r7 == 0) goto L_0x00b9
            int r8 = r6.zzcmn()
            long r10 = (long) r8
            r12.zzgmb = r10
            java.lang.String r8 = "ETag"
            java.lang.String r8 = r6.zzsn(r8)
            boolean r10 = android.text.TextUtils.isEmpty(r8)
            if (r10 != 0) goto L_0x00a8
            java.lang.String r10 = r12.zzohy
            if (r10 == 0) goto L_0x00a8
            java.lang.String r10 = r12.zzohy
            boolean r10 = r10.equals(r8)
            if (r10 != 0) goto L_0x00a8
            java.lang.String r0 = "FileDownloadTask"
            java.lang.String r1 = "The file at the server has changed.  Restarting from the beginning."
            android.util.Log.w(r0, r1)
            r12.zzohz = r3
            r12.zzohy = r5
            r6.zzcmh()
            r12.schedule()
            return
        L_0x00a8:
            r12.zzohy = r8
            boolean r5 = r12.zza(r6)     // Catch:{ IOException -> 0x00af }
            goto L_0x00ba
        L_0x00af:
            r5 = move-exception
            java.lang.String r8 = "FileDownloadTask"
            java.lang.String r10 = "Exception occurred during file write.  Aborting."
            android.util.Log.e(r8, r10, r5)
            r12.zzkuq = r5
        L_0x00b9:
            r5 = r7
        L_0x00ba:
            r6.zzcmh()
            if (r5 == 0) goto L_0x00ca
            java.lang.Exception r5 = r12.zzkuq
            if (r5 != 0) goto L_0x00ca
            int r5 = r12.zzclt()
            if (r5 != r0) goto L_0x00ca
            goto L_0x00cb
        L_0x00ca:
            r9 = 0
        L_0x00cb:
            if (r9 == 0) goto L_0x00d3
            r0 = 128(0x80, float:1.794E-43)
            r12.zzk(r0, r2)
            return
        L_0x00d3:
            java.io.File r5 = new java.io.File
            android.net.Uri r6 = r12.zzohw
            java.lang.String r6 = r6.getPath()
            r5.<init>(r6)
            boolean r6 = r5.exists()
            if (r6 == 0) goto L_0x00eb
            long r5 = r5.length()
            r12.zzohz = r5
            goto L_0x00ed
        L_0x00eb:
            r12.zzohz = r3
        L_0x00ed:
            int r5 = r12.zzclt()
            r6 = 8
            if (r5 != r6) goto L_0x00fb
            r0 = 16
            r12.zzk(r0, r2)
            return
        L_0x00fb:
            int r5 = r12.zzclt()
            r6 = 32
            if (r5 != r6) goto L_0x0128
            r0 = 256(0x100, float:3.59E-43)
            boolean r0 = r12.zzk(r0, r2)
            if (r0 != 0) goto L_0x0127
            java.lang.String r0 = "FileDownloadTask"
            int r1 = r12.zzclt()
            r2 = 62
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>(r2)
            java.lang.String r2 = "Unable to change download task to final state from "
            r3.append(r2)
            r3.append(r1)
            java.lang.String r1 = r3.toString()
            android.util.Log.w(r0, r1)
        L_0x0127:
            return
        L_0x0128:
            long r5 = r12.zzohx
            int r3 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r3 > 0) goto L_0x0013
            r12.zzk(r1, r2)
            return
        L_0x0132:
            r0 = move-exception
            java.lang.String r3 = "FileDownloadTask"
            java.lang.String r4 = "Unable to create firebase storage network request."
            android.util.Log.e(r3, r4, r0)
            r12.zzkuq = r0
            r12.zzk(r1, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.storage.FileDownloadTask.run():void");
    }

    /* access modifiers changed from: protected */
    public void schedule() {
        zzu.zzu(zzbko());
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public final /* synthetic */ ProvideError zzcln() {
        return new TaskSnapshot(StorageException.fromExceptionAndHttpCode(this.zzkuq, this.mResultCode), this.zzohx + this.zzohz);
    }
}
