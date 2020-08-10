package com.google.firebase.storage;

import android.net.Uri;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.internal.zzexq;
import com.google.android.gms.internal.zzexr;
import com.google.android.gms.internal.zzexw;
import com.google.android.gms.internal.zzeyc;
import com.google.firebase.storage.StorageTask.ProvideError;
import com.google.firebase.storage.StorageTask.SnapshotBase;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

public class UploadTask extends StorageTask<TaskSnapshot> {
    private volatile int mResultCode = 0;
    private final Uri mUri;
    private volatile Exception zzkuq = null;
    /* access modifiers changed from: private */
    public final StorageReference zzoht;
    private zzexr zzohv;
    private volatile StorageMetadata zzojb;
    private final long zzolc;
    private final zzexq zzold;
    private final AtomicLong zzole = new AtomicLong(0);
    private int zzolf = 262144;
    private boolean zzolg;
    private volatile Uri zzolh = null;
    private volatile Exception zzoli = null;
    private volatile String zzolj;

    public class TaskSnapshot extends SnapshotBase {
        private final StorageMetadata zzojb;
        private final Uri zzolh;
        private final long zzolm;

        TaskSnapshot(@Nullable Exception exc, long j, Uri uri, StorageMetadata storageMetadata) {
            super(exc);
            this.zzolm = j;
            this.zzolh = uri;
            this.zzojb = storageMetadata;
        }

        public long getBytesTransferred() {
            return this.zzolm;
        }

        @Nullable
        public Uri getDownloadUrl() {
            StorageMetadata metadata = getMetadata();
            if (metadata != null) {
                return metadata.getDownloadUrl();
            }
            return null;
        }

        @Nullable
        public StorageMetadata getMetadata() {
            return this.zzojb;
        }

        public long getTotalByteCount() {
            return UploadTask.this.getTotalByteCount();
        }

        @Nullable
        public Uri getUploadSessionUri() {
            return this.zzolh;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0081 A[Catch:{ FileNotFoundException -> 0x00bd }] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0086 A[Catch:{ FileNotFoundException -> 0x00bd }] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00a2  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00d7  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00dc  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    UploadTask(com.google.firebase.storage.StorageReference r10, com.google.firebase.storage.StorageMetadata r11, android.net.Uri r12, android.net.Uri r13) {
        /*
            r9 = this;
            r9.<init>()
            java.util.concurrent.atomic.AtomicLong r0 = new java.util.concurrent.atomic.AtomicLong
            r1 = 0
            r0.<init>(r1)
            r9.zzole = r0
            r0 = 262144(0x40000, float:3.67342E-40)
            r9.zzolf = r0
            r1 = 0
            r9.zzolh = r1
            r9.zzkuq = r1
            r9.zzoli = r1
            r2 = 0
            r9.mResultCode = r2
            com.google.android.gms.common.internal.zzbq.checkNotNull(r10)
            com.google.android.gms.common.internal.zzbq.checkNotNull(r12)
            r9.zzoht = r10
            r9.zzojb = r11
            r9.mUri = r12
            com.google.android.gms.internal.zzexr r10 = new com.google.android.gms.internal.zzexr
            com.google.firebase.storage.StorageReference r11 = r9.zzoht
            com.google.firebase.storage.FirebaseStorage r11 = r11.getStorage()
            com.google.firebase.FirebaseApp r11 = r11.getApp()
            com.google.firebase.storage.StorageReference r12 = r9.zzoht
            com.google.firebase.storage.FirebaseStorage r12 = r12.getStorage()
            long r2 = r12.getMaxUploadRetryTimeMillis()
            r10.<init>(r11, r2)
            r9.zzohv = r10
            r10 = -1
            com.google.firebase.storage.StorageReference r12 = r9.zzoht     // Catch:{ FileNotFoundException -> 0x00bf }
            com.google.firebase.storage.FirebaseStorage r12 = r12.getStorage()     // Catch:{ FileNotFoundException -> 0x00bf }
            com.google.firebase.FirebaseApp r12 = r12.getApp()     // Catch:{ FileNotFoundException -> 0x00bf }
            android.content.Context r12 = r12.getApplicationContext()     // Catch:{ FileNotFoundException -> 0x00bf }
            android.content.ContentResolver r12 = r12.getContentResolver()     // Catch:{ FileNotFoundException -> 0x00bf }
            android.net.Uri r2 = r9.mUri     // Catch:{ NullPointerException -> 0x0090, IOException -> 0x006b }
            java.lang.String r3 = "r"
            android.os.ParcelFileDescriptor r2 = r12.openFileDescriptor(r2, r3)     // Catch:{ NullPointerException -> 0x0090, IOException -> 0x006b }
            if (r2 == 0) goto L_0x0099
            long r3 = r2.getStatSize()     // Catch:{ NullPointerException -> 0x0090, IOException -> 0x006b }
            r2.close()     // Catch:{ NullPointerException -> 0x0069, IOException -> 0x0067 }
            goto L_0x009a
        L_0x0067:
            r2 = move-exception
            goto L_0x006d
        L_0x0069:
            r2 = move-exception
            goto L_0x0092
        L_0x006b:
            r2 = move-exception
            r3 = r10
        L_0x006d:
            java.lang.String r5 = "UploadTask"
            java.lang.String r6 = "could not retrieve file size for upload "
            android.net.Uri r7 = r9.mUri     // Catch:{ FileNotFoundException -> 0x00bd }
            java.lang.String r7 = r7.toString()     // Catch:{ FileNotFoundException -> 0x00bd }
            java.lang.String r7 = java.lang.String.valueOf(r7)     // Catch:{ FileNotFoundException -> 0x00bd }
            int r8 = r7.length()     // Catch:{ FileNotFoundException -> 0x00bd }
            if (r8 == 0) goto L_0x0086
            java.lang.String r6 = r6.concat(r7)     // Catch:{ FileNotFoundException -> 0x00bd }
            goto L_0x008c
        L_0x0086:
            java.lang.String r7 = new java.lang.String     // Catch:{ FileNotFoundException -> 0x00bd }
            r7.<init>(r6)     // Catch:{ FileNotFoundException -> 0x00bd }
            r6 = r7
        L_0x008c:
            android.util.Log.w(r5, r6, r2)     // Catch:{ FileNotFoundException -> 0x00bd }
            goto L_0x009a
        L_0x0090:
            r2 = move-exception
            r3 = r10
        L_0x0092:
            java.lang.String r5 = "UploadTask"
            java.lang.String r6 = "NullPointerException during file size calculation."
            android.util.Log.w(r5, r6, r2)     // Catch:{ FileNotFoundException -> 0x00bd }
        L_0x0099:
            r3 = r10
        L_0x009a:
            android.net.Uri r2 = r9.mUri     // Catch:{ FileNotFoundException -> 0x00bd }
            java.io.InputStream r12 = r12.openInputStream(r2)     // Catch:{ FileNotFoundException -> 0x00bd }
            if (r12 == 0) goto L_0x00e7
            int r10 = (r3 > r10 ? 1 : (r3 == r10 ? 0 : -1))
            if (r10 != 0) goto L_0x00b1
            int r10 = r12.available()     // Catch:{ IOException -> 0x00b0, FileNotFoundException -> 0x00ae }
            if (r10 < 0) goto L_0x00b1
            long r3 = (long) r10
            goto L_0x00b1
        L_0x00ae:
            r10 = move-exception
            goto L_0x00c3
        L_0x00b0:
            r10 = move-exception
        L_0x00b1:
            r10 = r3
            java.io.BufferedInputStream r1 = new java.io.BufferedInputStream     // Catch:{ FileNotFoundException -> 0x00b9 }
            r1.<init>(r12)     // Catch:{ FileNotFoundException -> 0x00b9 }
            r12 = r1
            goto L_0x00e8
        L_0x00b9:
            r1 = move-exception
            r3 = r10
            r10 = r1
            goto L_0x00c3
        L_0x00bd:
            r10 = move-exception
            goto L_0x00c2
        L_0x00bf:
            r12 = move-exception
            r3 = r10
            r10 = r12
        L_0x00c2:
            r12 = r1
        L_0x00c3:
            java.lang.String r11 = "UploadTask"
            java.lang.String r1 = "could not locate file for uploading:"
            android.net.Uri r2 = r9.mUri
            java.lang.String r2 = r2.toString()
            java.lang.String r2 = java.lang.String.valueOf(r2)
            int r5 = r2.length()
            if (r5 == 0) goto L_0x00dc
            java.lang.String r1 = r1.concat(r2)
            goto L_0x00e2
        L_0x00dc:
            java.lang.String r2 = new java.lang.String
            r2.<init>(r1)
            r1 = r2
        L_0x00e2:
            android.util.Log.e(r11, r1)
            r9.zzkuq = r10
        L_0x00e7:
            r10 = r3
        L_0x00e8:
            r9.zzolc = r10
            com.google.android.gms.internal.zzexq r10 = new com.google.android.gms.internal.zzexq
            r10.<init>(r12, r0)
            r9.zzold = r10
            r10 = 1
            r9.zzolg = r10
            r9.zzolh = r13
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.storage.UploadTask.<init>(com.google.firebase.storage.StorageReference, com.google.firebase.storage.StorageMetadata, android.net.Uri, android.net.Uri):void");
    }

    UploadTask(StorageReference storageReference, StorageMetadata storageMetadata, InputStream inputStream) {
        zzbq.checkNotNull(storageReference);
        zzbq.checkNotNull(inputStream);
        this.zzolc = -1;
        this.zzoht = storageReference;
        this.zzojb = storageMetadata;
        this.zzold = new zzexq(inputStream, 262144);
        this.zzolg = false;
        this.mUri = null;
        this.zzohv = new zzexr(this.zzoht.getStorage().getApp(), this.zzoht.getStorage().getMaxUploadRetryTimeMillis());
    }

    UploadTask(StorageReference storageReference, StorageMetadata storageMetadata, byte[] bArr) {
        zzbq.checkNotNull(storageReference);
        zzbq.checkNotNull(bArr);
        this.zzolc = (long) bArr.length;
        this.zzoht = storageReference;
        this.zzojb = storageMetadata;
        this.mUri = null;
        this.zzold = new zzexq(new ByteArrayInputStream(bArr), 262144);
        this.zzolg = true;
        this.zzohv = new zzexr(this.zzoht.getStorage().getApp(), this.zzoht.getStorage().getMaxUploadRetryTimeMillis());
    }

    private final boolean zzb(zzeyc zzeyc) {
        zzeyc.zze(zzexw.zzh(this.zzoht.getStorage().getApp()), this.zzoht.getStorage().getApp().getApplicationContext());
        return zzd(zzeyc);
    }

    private final boolean zzc(zzeyc zzeyc) {
        this.zzohv.zza(zzeyc, true);
        return zzd(zzeyc);
    }

    private final boolean zzcmc() {
        if (zzclt() == 128) {
            return false;
        }
        if (Thread.interrupted()) {
            this.zzkuq = new InterruptedException();
            zzk(64, false);
            return false;
        } else if (zzclt() == 32) {
            zzk(256, false);
            return false;
        } else if (zzclt() == 8) {
            zzk(16, false);
            return false;
        } else if (!zzcmd()) {
            return false;
        } else {
            if (this.zzolh == null) {
                if (this.zzkuq == null) {
                    this.zzkuq = new IllegalStateException("Unable to obtain an upload URL.");
                }
                zzk(64, false);
                return false;
            } else if (this.zzkuq != null) {
                zzk(64, false);
                return false;
            } else {
                if (!(this.zzoli != null || this.mResultCode < 200 || this.mResultCode >= 300) || zzcw(true)) {
                    return true;
                }
                if (zzcmd()) {
                    zzk(64, false);
                }
                return false;
            }
        }
    }

    private final boolean zzcmd() {
        if (!"final".equals(this.zzolj)) {
            return true;
        }
        if (this.zzkuq == null) {
            this.zzkuq = new IOException("The server has terminated the upload session", this.zzoli);
        }
        zzk(64, false);
        return false;
    }

    private final boolean zzcw(boolean z) {
        String str;
        String str2;
        try {
            zzeyc zzb = this.zzoht.zzclq().zzb(this.zzoht.zzclr(), this.zzolh.toString());
            if ("final".equals(this.zzolj)) {
                return false;
            }
            if (z) {
                if (!zzc(zzb)) {
                    return false;
                }
            } else if (!zzb(zzb)) {
                return false;
            }
            if ("final".equals(zzb.zzsn("X-Goog-Upload-Status"))) {
                e = new IOException("The server has terminated the upload session");
            } else {
                String zzsn = zzb.zzsn("X-Goog-Upload-Size-Received");
                long parseLong = !TextUtils.isEmpty(zzsn) ? Long.parseLong(zzsn) : 0;
                long j = this.zzole.get();
                int i = (j > parseLong ? 1 : (j == parseLong ? 0 : -1));
                if (i > 0) {
                    e = new IOException("Unexpected error. The server lost a chunk update.");
                } else {
                    if (i < 0) {
                        try {
                            long j2 = parseLong - j;
                            if (((long) this.zzold.zzik((int) j2)) != j2) {
                                this.zzkuq = new IOException("Unexpected end of stream encountered.");
                                return false;
                            } else if (!this.zzole.compareAndSet(j, parseLong)) {
                                Log.e("UploadTask", "Somehow, the uploaded bytes changed during an uploaded.  This should nothappen");
                                this.zzkuq = new IllegalStateException("uploaded bytes changed unexpectedly.");
                                return false;
                            }
                        } catch (IOException e) {
                            e = e;
                            str = "UploadTask";
                            str2 = "Unable to recover position in Stream during resumable upload";
                            Log.e(str, str2, e);
                            this.zzkuq = e;
                            return false;
                        }
                    }
                    return true;
                }
            }
            this.zzkuq = e;
            return false;
        } catch (RemoteException e2) {
            e = e2;
            str = "UploadTask";
            str2 = "Unable to recover status during resumable upload";
            Log.e(str, str2, e);
            this.zzkuq = e;
            return false;
        }
    }

    private final boolean zzd(zzeyc zzeyc) {
        int resultCode = zzeyc.getResultCode();
        if (zzexr.zzin(resultCode)) {
            resultCode = -2;
        }
        this.mResultCode = resultCode;
        this.zzoli = zzeyc.getException();
        this.zzolj = zzeyc.zzsn("X-Goog-Upload-Status");
        int i = this.mResultCode;
        return (i == 308 || (i >= 200 && i < 300)) && this.zzoli == null;
    }

    /* access modifiers changed from: 0000 */
    public final StorageReference getStorage() {
        return this.zzoht;
    }

    /* access modifiers changed from: 0000 */
    public final long getTotalByteCount() {
        return this.zzolc;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x002b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onCanceled() {
        /*
            r3 = this;
            com.google.android.gms.internal.zzexr r0 = r3.zzohv
            r0.cancel()
            android.net.Uri r0 = r3.zzolh
            if (r0 == 0) goto L_0x0028
            com.google.firebase.storage.StorageReference r0 = r3.zzoht     // Catch:{ RemoteException -> 0x0020 }
            com.google.android.gms.internal.zzeyb r0 = r0.zzclq()     // Catch:{ RemoteException -> 0x0020 }
            com.google.firebase.storage.StorageReference r1 = r3.zzoht     // Catch:{ RemoteException -> 0x0020 }
            android.net.Uri r1 = r1.zzclr()     // Catch:{ RemoteException -> 0x0020 }
            android.net.Uri r2 = r3.zzolh     // Catch:{ RemoteException -> 0x0020 }
            java.lang.String r2 = r2.toString()     // Catch:{ RemoteException -> 0x0020 }
            com.google.android.gms.internal.zzeyc r0 = r0.zza(r1, r2)     // Catch:{ RemoteException -> 0x0020 }
            goto L_0x0029
        L_0x0020:
            r0 = move-exception
            java.lang.String r1 = "UploadTask"
            java.lang.String r2 = "Unable to create chunk upload request"
            android.util.Log.e(r1, r2, r0)
        L_0x0028:
            r0 = 0
        L_0x0029:
            if (r0 == 0) goto L_0x0033
            com.google.firebase.storage.zzad r1 = new com.google.firebase.storage.zzad
            r1.<init>(r3, r0)
            com.google.firebase.storage.zzu.zzs(r1)
        L_0x0033:
            com.google.android.gms.common.api.Status r0 = com.google.android.gms.common.api.Status.zzfnm
            com.google.firebase.storage.StorageException r0 = com.google.firebase.storage.StorageException.fromErrorStatus(r0)
            r3.zzkuq = r0
            super.onCanceled()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.storage.UploadTask.onCanceled():void");
    }

    /* access modifiers changed from: protected */
    public void resetState() {
        this.zzkuq = null;
        this.zzoli = null;
        this.mResultCode = 0;
        this.zzolj = null;
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x019c  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x00ab A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
            r12 = this;
            com.google.android.gms.internal.zzexr r0 = r12.zzohv
            r0.reset()
            r0 = 4
            r1 = 0
            boolean r2 = r12.zzk(r0, r1)
            if (r2 != 0) goto L_0x0015
            java.lang.String r0 = "UploadTask"
            java.lang.String r1 = "The upload cannot continue as it is not in a valid state."
            android.util.Log.d(r0, r1)
            return
        L_0x0015:
            com.google.firebase.storage.StorageReference r2 = r12.zzoht
            com.google.firebase.storage.StorageReference r2 = r2.getParent()
            if (r2 != 0) goto L_0x0026
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.String r3 = "Cannot upload to getRoot. You should upload to a storage location such as .getReference('image.png').putFile..."
            r2.<init>(r3)
            r12.zzkuq = r2
        L_0x0026:
            java.lang.Exception r2 = r12.zzkuq
            if (r2 == 0) goto L_0x002b
            return
        L_0x002b:
            android.net.Uri r2 = r12.zzolh
            if (r2 != 0) goto L_0x00a4
            com.google.firebase.storage.StorageMetadata r2 = r12.zzojb
            r3 = 0
            if (r2 == 0) goto L_0x003b
            com.google.firebase.storage.StorageMetadata r2 = r12.zzojb
            java.lang.String r2 = r2.getContentType()
            goto L_0x003c
        L_0x003b:
            r2 = r3
        L_0x003c:
            android.net.Uri r4 = r12.mUri
            if (r4 == 0) goto L_0x005e
            boolean r4 = android.text.TextUtils.isEmpty(r2)
            if (r4 == 0) goto L_0x005e
            com.google.firebase.storage.StorageReference r2 = r12.zzoht
            com.google.firebase.storage.FirebaseStorage r2 = r2.getStorage()
            com.google.firebase.FirebaseApp r2 = r2.getApp()
            android.content.Context r2 = r2.getApplicationContext()
            android.content.ContentResolver r2 = r2.getContentResolver()
            android.net.Uri r4 = r12.mUri
            java.lang.String r2 = r2.getType(r4)
        L_0x005e:
            boolean r4 = android.text.TextUtils.isEmpty(r2)
            if (r4 == 0) goto L_0x0066
            java.lang.String r2 = "application/octet-stream"
        L_0x0066:
            com.google.firebase.storage.StorageReference r4 = r12.zzoht     // Catch:{ RemoteException | JSONException -> 0x0099 }
            com.google.android.gms.internal.zzeyb r4 = r4.zzclq()     // Catch:{ RemoteException | JSONException -> 0x0099 }
            com.google.firebase.storage.StorageReference r5 = r12.zzoht     // Catch:{ RemoteException | JSONException -> 0x0099 }
            android.net.Uri r5 = r5.zzclr()     // Catch:{ RemoteException | JSONException -> 0x0099 }
            com.google.firebase.storage.StorageMetadata r6 = r12.zzojb     // Catch:{ RemoteException | JSONException -> 0x0099 }
            if (r6 == 0) goto L_0x007c
            com.google.firebase.storage.StorageMetadata r3 = r12.zzojb     // Catch:{ RemoteException | JSONException -> 0x0099 }
            org.json.JSONObject r3 = r3.zzclo()     // Catch:{ RemoteException | JSONException -> 0x0099 }
        L_0x007c:
            com.google.android.gms.internal.zzeyc r2 = r4.zza(r5, r3, r2)     // Catch:{ RemoteException | JSONException -> 0x0099 }
            boolean r3 = r12.zzc(r2)
            if (r3 == 0) goto L_0x00a7
            java.lang.String r3 = "X-Goog-Upload-URL"
            java.lang.String r2 = r2.zzsn(r3)
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 != 0) goto L_0x00a7
            android.net.Uri r2 = android.net.Uri.parse(r2)
            r12.zzolh = r2
            goto L_0x00a7
        L_0x0099:
            r2 = move-exception
            java.lang.String r3 = "UploadTask"
            java.lang.String r4 = "Unable to create a network request from metadata"
            android.util.Log.e(r3, r4, r2)
            r12.zzkuq = r2
            goto L_0x00a7
        L_0x00a4:
            r12.zzcw(r1)
        L_0x00a7:
            boolean r2 = r12.zzcmc()
        L_0x00ab:
            if (r2 == 0) goto L_0x01a1
            com.google.android.gms.internal.zzexq r2 = r12.zzold     // Catch:{ IOException -> 0x018c }
            int r3 = r12.zzolf     // Catch:{ IOException -> 0x018c }
            r2.zzil(r3)     // Catch:{ IOException -> 0x018c }
            int r2 = r12.zzolf     // Catch:{ IOException -> 0x018c }
            com.google.android.gms.internal.zzexq r3 = r12.zzold     // Catch:{ IOException -> 0x018c }
            int r3 = r3.available()     // Catch:{ IOException -> 0x018c }
            int r2 = java.lang.Math.min(r2, r3)     // Catch:{ IOException -> 0x018c }
            com.google.firebase.storage.StorageReference r3 = r12.zzoht     // Catch:{ RemoteException -> 0x0183 }
            com.google.android.gms.internal.zzeyb r4 = r3.zzclq()     // Catch:{ RemoteException -> 0x0183 }
            com.google.firebase.storage.StorageReference r3 = r12.zzoht     // Catch:{ RemoteException -> 0x0183 }
            android.net.Uri r5 = r3.zzclr()     // Catch:{ RemoteException -> 0x0183 }
            android.net.Uri r3 = r12.zzolh     // Catch:{ RemoteException -> 0x0183 }
            java.lang.String r6 = r3.toString()     // Catch:{ RemoteException -> 0x0183 }
            com.google.android.gms.internal.zzexq r3 = r12.zzold     // Catch:{ RemoteException -> 0x0183 }
            byte[] r7 = r3.zzcmg()     // Catch:{ RemoteException -> 0x0183 }
            java.util.concurrent.atomic.AtomicLong r3 = r12.zzole     // Catch:{ RemoteException -> 0x0183 }
            long r8 = r3.get()     // Catch:{ RemoteException -> 0x0183 }
            com.google.android.gms.internal.zzexq r3 = r12.zzold     // Catch:{ RemoteException -> 0x0183 }
            boolean r11 = r3.isFinished()     // Catch:{ RemoteException -> 0x0183 }
            r10 = r2
            com.google.android.gms.internal.zzeyc r3 = r4.zza(r5, r6, r7, r8, r10, r11)     // Catch:{ RemoteException -> 0x0183 }
            boolean r4 = r12.zzb(r3)     // Catch:{ IOException -> 0x018c }
            if (r4 != 0) goto L_0x010f
            r2 = 262144(0x40000, float:3.67342E-40)
            r12.zzolf = r2     // Catch:{ IOException -> 0x018c }
            java.lang.String r2 = "UploadTask"
            int r3 = r12.zzolf     // Catch:{ IOException -> 0x018c }
            r4 = 35
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x018c }
            r5.<init>(r4)     // Catch:{ IOException -> 0x018c }
            java.lang.String r4 = "Resetting chunk size to "
            r5.append(r4)     // Catch:{ IOException -> 0x018c }
            r5.append(r3)     // Catch:{ IOException -> 0x018c }
            java.lang.String r3 = r5.toString()     // Catch:{ IOException -> 0x018c }
        L_0x010a:
            android.util.Log.d(r2, r3)     // Catch:{ IOException -> 0x018c }
            goto L_0x0196
        L_0x010f:
            java.util.concurrent.atomic.AtomicLong r4 = r12.zzole     // Catch:{ IOException -> 0x018c }
            long r5 = (long) r2     // Catch:{ IOException -> 0x018c }
            r4.getAndAdd(r5)     // Catch:{ IOException -> 0x018c }
            com.google.android.gms.internal.zzexq r4 = r12.zzold     // Catch:{ IOException -> 0x018c }
            boolean r4 = r4.isFinished()     // Catch:{ IOException -> 0x018c }
            if (r4 != 0) goto L_0x0146
            com.google.android.gms.internal.zzexq r3 = r12.zzold     // Catch:{ IOException -> 0x018c }
            r3.zzik(r2)     // Catch:{ IOException -> 0x018c }
            int r2 = r12.zzolf     // Catch:{ IOException -> 0x018c }
            r3 = 33554432(0x2000000, float:9.403955E-38)
            if (r2 >= r3) goto L_0x0196
            int r2 = r12.zzolf     // Catch:{ IOException -> 0x018c }
            int r2 = r2 << 1
            r12.zzolf = r2     // Catch:{ IOException -> 0x018c }
            java.lang.String r2 = "UploadTask"
            int r3 = r12.zzolf     // Catch:{ IOException -> 0x018c }
            r4 = 36
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x018c }
            r5.<init>(r4)     // Catch:{ IOException -> 0x018c }
            java.lang.String r4 = "Increasing chunk size to "
            r5.append(r4)     // Catch:{ IOException -> 0x018c }
            r5.append(r3)     // Catch:{ IOException -> 0x018c }
            java.lang.String r3 = r5.toString()     // Catch:{ IOException -> 0x018c }
            goto L_0x010a
        L_0x0146:
            com.google.firebase.storage.StorageMetadata$Builder r2 = new com.google.firebase.storage.StorageMetadata$Builder     // Catch:{ RemoteException | JSONException -> 0x0160 }
            org.json.JSONObject r4 = r3.zzcmp()     // Catch:{ RemoteException | JSONException -> 0x0160 }
            com.google.firebase.storage.StorageReference r5 = r12.zzoht     // Catch:{ RemoteException | JSONException -> 0x0160 }
            r2.<init>(r4, r5)     // Catch:{ RemoteException | JSONException -> 0x0160 }
            com.google.firebase.storage.StorageMetadata r2 = r2.build()     // Catch:{ RemoteException | JSONException -> 0x0160 }
            r12.zzojb = r2     // Catch:{ RemoteException | JSONException -> 0x0160 }
            r12.zzk(r0, r1)     // Catch:{ IOException -> 0x018c }
            r2 = 128(0x80, float:1.794E-43)
            r12.zzk(r2, r1)     // Catch:{ IOException -> 0x018c }
            goto L_0x0196
        L_0x0160:
            r2 = move-exception
            java.lang.String r4 = "UploadTask"
            java.lang.String r5 = "Unable to parse resulting metadata from upload:"
            java.lang.String r3 = r3.zzcmk()     // Catch:{ IOException -> 0x018c }
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ IOException -> 0x018c }
            int r6 = r3.length()     // Catch:{ IOException -> 0x018c }
            if (r6 == 0) goto L_0x0178
            java.lang.String r3 = r5.concat(r3)     // Catch:{ IOException -> 0x018c }
            goto L_0x017d
        L_0x0178:
            java.lang.String r3 = new java.lang.String     // Catch:{ IOException -> 0x018c }
            r3.<init>(r5)     // Catch:{ IOException -> 0x018c }
        L_0x017d:
            android.util.Log.e(r4, r3, r2)     // Catch:{ IOException -> 0x018c }
        L_0x0180:
            r12.zzkuq = r2     // Catch:{ IOException -> 0x018c }
            goto L_0x0196
        L_0x0183:
            r2 = move-exception
            java.lang.String r3 = "UploadTask"
            java.lang.String r4 = "Unable to create chunk upload request"
            android.util.Log.e(r3, r4, r2)     // Catch:{ IOException -> 0x018c }
            goto L_0x0180
        L_0x018c:
            r2 = move-exception
            java.lang.String r3 = "UploadTask"
            java.lang.String r4 = "Unable to read bytes for uploading"
            android.util.Log.e(r3, r4, r2)
            r12.zzkuq = r2
        L_0x0196:
            boolean r2 = r12.zzcmc()
            if (r2 == 0) goto L_0x00ab
            r12.zzk(r0, r1)
            goto L_0x00ab
        L_0x01a1:
            boolean r0 = r12.zzolg
            if (r0 == 0) goto L_0x01bb
            int r0 = r12.zzclt()
            r1 = 16
            if (r0 == r1) goto L_0x01bb
            com.google.android.gms.internal.zzexq r0 = r12.zzold     // Catch:{ IOException -> 0x01b3 }
            r0.close()     // Catch:{ IOException -> 0x01b3 }
            return
        L_0x01b3:
            r0 = move-exception
            java.lang.String r1 = "UploadTask"
            java.lang.String r2 = "Unable to close stream."
            android.util.Log.e(r1, r2, r0)
        L_0x01bb:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.storage.UploadTask.run():void");
    }

    /* access modifiers changed from: protected */
    public void schedule() {
        zzu.zzt(zzbko());
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public final /* synthetic */ ProvideError zzcln() {
        TaskSnapshot taskSnapshot = new TaskSnapshot(StorageException.fromExceptionAndHttpCode(this.zzkuq != null ? this.zzkuq : this.zzoli, this.mResultCode), this.zzole.get(), this.zzolh, this.zzojb);
        return taskSnapshot;
    }
}
