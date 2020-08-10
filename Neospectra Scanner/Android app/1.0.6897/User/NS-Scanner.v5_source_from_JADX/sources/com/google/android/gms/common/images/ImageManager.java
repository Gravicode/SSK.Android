package com.google.android.gms.common.images;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.support.p001v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.internal.zzbfl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ImageManager {
    /* access modifiers changed from: private */
    public static final Object zzfwr = new Object();
    /* access modifiers changed from: private */
    public static HashSet<Uri> zzfws = new HashSet<>();
    private static ImageManager zzfwt;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public final ExecutorService zzfwu = Executors.newFixedThreadPool(4);
    /* access modifiers changed from: private */
    public final zza zzfwv = null;
    /* access modifiers changed from: private */
    public final zzbfl zzfww = new zzbfl();
    /* access modifiers changed from: private */
    public final Map<zza, ImageReceiver> zzfwx = new HashMap();
    /* access modifiers changed from: private */
    public final Map<Uri, ImageReceiver> zzfwy = new HashMap();
    /* access modifiers changed from: private */
    public final Map<Uri, Long> zzfwz = new HashMap();

    @KeepName
    final class ImageReceiver extends ResultReceiver {
        private final Uri mUri;
        /* access modifiers changed from: private */
        public final ArrayList<zza> zzfxa = new ArrayList<>();

        ImageReceiver(Uri uri) {
            super(new Handler(Looper.getMainLooper()));
            this.mUri = uri;
        }

        public final void onReceiveResult(int i, Bundle bundle) {
            ImageManager.this.zzfwu.execute(new zzb(this.mUri, (ParcelFileDescriptor) bundle.getParcelable("com.google.android.gms.extra.fileDescriptor")));
        }

        public final void zzakd() {
            Intent intent = new Intent("com.google.android.gms.common.images.LOAD_IMAGE");
            intent.putExtra("com.google.android.gms.extras.uri", this.mUri);
            intent.putExtra("com.google.android.gms.extras.resultReceiver", this);
            intent.putExtra("com.google.android.gms.extras.priority", 3);
            ImageManager.this.mContext.sendBroadcast(intent);
        }

        public final void zzb(zza zza) {
            com.google.android.gms.common.internal.zzc.zzge("ImageReceiver.addImageRequest() must be called in the main thread");
            this.zzfxa.add(zza);
        }

        public final void zzc(zza zza) {
            com.google.android.gms.common.internal.zzc.zzge("ImageReceiver.removeImageRequest() must be called in the main thread");
            this.zzfxa.remove(zza);
        }
    }

    public interface OnImageLoadedListener {
        void onImageLoaded(Uri uri, Drawable drawable, boolean z);
    }

    static final class zza extends LruCache<zzb, Bitmap> {
        /* access modifiers changed from: protected */
        public final /* synthetic */ void entryRemoved(boolean z, Object obj, Object obj2, Object obj3) {
            super.entryRemoved(z, (zzb) obj, (Bitmap) obj2, (Bitmap) obj3);
        }

        /* access modifiers changed from: protected */
        public final /* synthetic */ int sizeOf(Object obj, Object obj2) {
            Bitmap bitmap = (Bitmap) obj2;
            return bitmap.getHeight() * bitmap.getRowBytes();
        }
    }

    final class zzb implements Runnable {
        private final Uri mUri;
        private final ParcelFileDescriptor zzfxc;

        public zzb(Uri uri, ParcelFileDescriptor parcelFileDescriptor) {
            this.mUri = uri;
            this.zzfxc = parcelFileDescriptor;
        }

        public final void run() {
            boolean z;
            Bitmap bitmap;
            String str = "LoadBitmapFromDiskRunnable can't be executed in the main thread";
            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                String valueOf = String.valueOf(Thread.currentThread());
                String valueOf2 = String.valueOf(Looper.getMainLooper().getThread());
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 56 + String.valueOf(valueOf2).length());
                sb.append("checkNotMainThread: current thread ");
                sb.append(valueOf);
                sb.append(" IS the main thread ");
                sb.append(valueOf2);
                sb.append("!");
                Log.e("Asserts", sb.toString());
                throw new IllegalStateException(str);
            }
            boolean z2 = false;
            Bitmap bitmap2 = null;
            if (this.zzfxc != null) {
                try {
                    bitmap2 = BitmapFactory.decodeFileDescriptor(this.zzfxc.getFileDescriptor());
                } catch (OutOfMemoryError e) {
                    String valueOf3 = String.valueOf(this.mUri);
                    StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf3).length() + 34);
                    sb2.append("OOM while loading bitmap for uri: ");
                    sb2.append(valueOf3);
                    Log.e("ImageManager", sb2.toString(), e);
                    z2 = true;
                }
                try {
                    this.zzfxc.close();
                } catch (IOException e2) {
                    Log.e("ImageManager", "closed failed", e2);
                }
                z = z2;
                bitmap = bitmap2;
            } else {
                bitmap = null;
                z = false;
            }
            CountDownLatch countDownLatch = new CountDownLatch(1);
            Handler zzg = ImageManager.this.mHandler;
            zzd zzd = new zzd(this.mUri, bitmap, z, countDownLatch);
            zzg.post(zzd);
            try {
                countDownLatch.await();
            } catch (InterruptedException e3) {
                String valueOf4 = String.valueOf(this.mUri);
                StringBuilder sb3 = new StringBuilder(String.valueOf(valueOf4).length() + 32);
                sb3.append("Latch interrupted while posting ");
                sb3.append(valueOf4);
                Log.w("ImageManager", sb3.toString());
            }
        }
    }

    final class zzc implements Runnable {
        private final zza zzfxd;

        public zzc(zza zza) {
            this.zzfxd = zza;
        }

        public final void run() {
            com.google.android.gms.common.internal.zzc.zzge("LoadImageRunnable must be executed on the main thread");
            ImageReceiver imageReceiver = (ImageReceiver) ImageManager.this.zzfwx.get(this.zzfxd);
            if (imageReceiver != null) {
                ImageManager.this.zzfwx.remove(this.zzfxd);
                imageReceiver.zzc(this.zzfxd);
            }
            zzb zzb = this.zzfxd.zzfxf;
            if (zzb.uri == null) {
                this.zzfxd.zza(ImageManager.this.mContext, ImageManager.this.zzfww, true);
                return;
            }
            Bitmap zza = ImageManager.this.zza(zzb);
            if (zza != null) {
                this.zzfxd.zza(ImageManager.this.mContext, zza, true);
                return;
            }
            Long l = (Long) ImageManager.this.zzfwz.get(zzb.uri);
            if (l != null) {
                if (SystemClock.elapsedRealtime() - l.longValue() < 3600000) {
                    this.zzfxd.zza(ImageManager.this.mContext, ImageManager.this.zzfww, true);
                    return;
                }
                ImageManager.this.zzfwz.remove(zzb.uri);
            }
            this.zzfxd.zza(ImageManager.this.mContext, ImageManager.this.zzfww);
            ImageReceiver imageReceiver2 = (ImageReceiver) ImageManager.this.zzfwy.get(zzb.uri);
            if (imageReceiver2 == null) {
                imageReceiver2 = new ImageReceiver(zzb.uri);
                ImageManager.this.zzfwy.put(zzb.uri, imageReceiver2);
            }
            imageReceiver2.zzb(this.zzfxd);
            if (!(this.zzfxd instanceof zzd)) {
                ImageManager.this.zzfwx.put(this.zzfxd, imageReceiver2);
            }
            synchronized (ImageManager.zzfwr) {
                if (!ImageManager.zzfws.contains(zzb.uri)) {
                    ImageManager.zzfws.add(zzb.uri);
                    imageReceiver2.zzakd();
                }
            }
        }
    }

    final class zzd implements Runnable {
        private final Bitmap mBitmap;
        private final Uri mUri;
        private final CountDownLatch zzapd;
        private boolean zzfxe;

        public zzd(Uri uri, Bitmap bitmap, boolean z, CountDownLatch countDownLatch) {
            this.mUri = uri;
            this.mBitmap = bitmap;
            this.zzfxe = z;
            this.zzapd = countDownLatch;
        }

        public final void run() {
            com.google.android.gms.common.internal.zzc.zzge("OnBitmapLoadedRunnable must be executed in the main thread");
            boolean z = this.mBitmap != null;
            if (ImageManager.this.zzfwv != null) {
                if (this.zzfxe) {
                    ImageManager.this.zzfwv.evictAll();
                    System.gc();
                    this.zzfxe = false;
                    ImageManager.this.mHandler.post(this);
                    return;
                } else if (z) {
                    ImageManager.this.zzfwv.put(new zzb(this.mUri), this.mBitmap);
                }
            }
            ImageReceiver imageReceiver = (ImageReceiver) ImageManager.this.zzfwy.remove(this.mUri);
            if (imageReceiver != null) {
                ArrayList zza = imageReceiver.zzfxa;
                int size = zza.size();
                for (int i = 0; i < size; i++) {
                    zza zza2 = (zza) zza.get(i);
                    if (z) {
                        zza2.zza(ImageManager.this.mContext, this.mBitmap, false);
                    } else {
                        ImageManager.this.zzfwz.put(this.mUri, Long.valueOf(SystemClock.elapsedRealtime()));
                        zza2.zza(ImageManager.this.mContext, ImageManager.this.zzfww, false);
                    }
                    if (!(zza2 instanceof zzd)) {
                        ImageManager.this.zzfwx.remove(zza2);
                    }
                }
            }
            this.zzapd.countDown();
            synchronized (ImageManager.zzfwr) {
                ImageManager.zzfws.remove(this.mUri);
            }
        }
    }

    private ImageManager(Context context, boolean z) {
        this.mContext = context.getApplicationContext();
    }

    public static ImageManager create(Context context) {
        if (zzfwt == null) {
            zzfwt = new ImageManager(context, false);
        }
        return zzfwt;
    }

    /* access modifiers changed from: private */
    public final Bitmap zza(zzb zzb2) {
        if (this.zzfwv == null) {
            return null;
        }
        return (Bitmap) this.zzfwv.get(zzb2);
    }

    private final void zza(zza zza2) {
        com.google.android.gms.common.internal.zzc.zzge("ImageManager.loadImage() must be called in the main thread");
        new zzc(zza2).run();
    }

    public final void loadImage(ImageView imageView, int i) {
        zza((zza) new zzc(imageView, i));
    }

    public final void loadImage(ImageView imageView, Uri uri) {
        zza((zza) new zzc(imageView, uri));
    }

    public final void loadImage(ImageView imageView, Uri uri, int i) {
        zzc zzc2 = new zzc(imageView, uri);
        zzc2.zzfxh = i;
        zza((zza) zzc2);
    }

    public final void loadImage(OnImageLoadedListener onImageLoadedListener, Uri uri) {
        zza((zza) new zzd(onImageLoadedListener, uri));
    }

    public final void loadImage(OnImageLoadedListener onImageLoadedListener, Uri uri, int i) {
        zzd zzd2 = new zzd(onImageLoadedListener, uri);
        zzd2.zzfxh = i;
        zza((zza) zzd2);
    }
}
