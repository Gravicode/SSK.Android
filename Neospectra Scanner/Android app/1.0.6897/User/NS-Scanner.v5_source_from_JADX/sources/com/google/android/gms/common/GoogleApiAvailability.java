package com.google.android.gms.common;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.Notification.BigTextStyle;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.GuardedBy;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.p001v4.app.FragmentActivity;
import android.support.p001v4.app.NotificationCompat;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ProgressBar;
import com.google.android.gms.C1133R;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.internal.zzbm;
import com.google.android.gms.common.api.internal.zzbx;
import com.google.android.gms.common.api.internal.zzby;
import com.google.android.gms.common.api.internal.zzcf;
import com.google.android.gms.common.api.internal.zzcn;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.internal.zzu;
import com.google.android.gms.common.internal.zzv;
import com.google.android.gms.common.util.zzi;
import com.google.android.gms.common.util.zzq;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import java.util.ArrayList;
import java.util.Arrays;

public class GoogleApiAvailability extends zzf {
    public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = zzf.GOOGLE_PLAY_SERVICES_VERSION_CODE;
    private static final Object mLock = new Object();
    private static final GoogleApiAvailability zzfku = new GoogleApiAvailability();
    @GuardedBy("mLock")
    private String zzfkv;

    @SuppressLint({"HandlerLeak"})
    class zza extends Handler {
        private final Context mApplicationContext;

        public zza(Context context) {
            super(Looper.myLooper() == null ? Looper.getMainLooper() : Looper.myLooper());
            this.mApplicationContext = context.getApplicationContext();
        }

        public final void handleMessage(Message message) {
            if (message.what != 1) {
                int i = message.what;
                StringBuilder sb = new StringBuilder(50);
                sb.append("Don't know how to handle this message: ");
                sb.append(i);
                Log.w("GoogleApiAvailability", sb.toString());
                return;
            }
            int isGooglePlayServicesAvailable = GoogleApiAvailability.this.isGooglePlayServicesAvailable(this.mApplicationContext);
            if (GoogleApiAvailability.this.isUserResolvableError(isGooglePlayServicesAvailable)) {
                GoogleApiAvailability.this.showErrorNotification(this.mApplicationContext, isGooglePlayServicesAvailable);
            }
        }
    }

    GoogleApiAvailability() {
    }

    public static GoogleApiAvailability getInstance() {
        return zzfku;
    }

    public static Dialog zza(Activity activity, OnCancelListener onCancelListener) {
        ProgressBar progressBar = new ProgressBar(activity, null, 16842874);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(0);
        Builder builder = new Builder(activity);
        builder.setView(progressBar);
        builder.setMessage(zzu.zzi(activity, 18));
        builder.setPositiveButton("", null);
        AlertDialog create = builder.create();
        zza(activity, (Dialog) create, "GooglePlayServicesUpdatingDialog", onCancelListener);
        return create;
    }

    static Dialog zza(Context context, int i, zzv zzv, OnCancelListener onCancelListener) {
        Builder builder = null;
        if (i == 0) {
            return null;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(16843529, typedValue, true);
        if ("Theme.Dialog.Alert".equals(context.getResources().getResourceEntryName(typedValue.resourceId))) {
            builder = new Builder(context, 5);
        }
        if (builder == null) {
            builder = new Builder(context);
        }
        builder.setMessage(zzu.zzi(context, i));
        if (onCancelListener != null) {
            builder.setOnCancelListener(onCancelListener);
        }
        String zzk = zzu.zzk(context, i);
        if (zzk != null) {
            builder.setPositiveButton(zzk, zzv);
        }
        String zzg = zzu.zzg(context, i);
        if (zzg != null) {
            builder.setTitle(zzg);
        }
        return builder.create();
    }

    @Nullable
    public static zzbx zza(Context context, zzby zzby) {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme("package");
        zzbx zzbx = new zzbx(zzby);
        context.registerReceiver(zzbx, intentFilter);
        zzbx.setContext(context);
        if (zzp.zzv(context, "com.google.android.gms")) {
            return zzbx;
        }
        zzby.zzahg();
        zzbx.unregister();
        return null;
    }

    @TargetApi(26)
    private final String zza(Context context, NotificationManager notificationManager) {
        zzbq.checkState(zzq.isAtLeastO());
        String zzafx = zzafx();
        if (zzafx == null) {
            zzafx = "com.google.android.gms.availability";
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(zzafx);
            String zzcn = zzu.zzcn(context);
            if (notificationChannel == null) {
                notificationChannel = new NotificationChannel(zzafx, zzcn, 4);
            } else if (!zzcn.equals(notificationChannel.getName())) {
                notificationChannel.setName(zzcn);
            }
            notificationManager.createNotificationChannel(notificationChannel);
            return zzafx;
        }
        return zzafx;
    }

    static void zza(Activity activity, Dialog dialog, String str, OnCancelListener onCancelListener) {
        if (activity instanceof FragmentActivity) {
            SupportErrorDialogFragment.newInstance(dialog, onCancelListener).show(((FragmentActivity) activity).getSupportFragmentManager(), str);
            return;
        }
        ErrorDialogFragment.newInstance(dialog, onCancelListener).show(activity.getFragmentManager(), str);
    }

    @TargetApi(20)
    private final void zza(Context context, int i, String str, PendingIntent pendingIntent) {
        Notification notification;
        int i2;
        if (i == 18) {
            zzcc(context);
        } else if (pendingIntent == null) {
            if (i == 6) {
                Log.w("GoogleApiAvailability", "Missing resolution for ConnectionResult.RESOLUTION_REQUIRED. Call GoogleApiAvailability#showErrorNotification(Context, ConnectionResult) instead.");
            }
        } else {
            String zzh = zzu.zzh(context, i);
            String zzj = zzu.zzj(context, i);
            Resources resources = context.getResources();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            if (zzi.zzct(context)) {
                zzbq.checkState(zzq.zzamm());
                Notification.Builder addAction = new Notification.Builder(context).setSmallIcon(context.getApplicationInfo().icon).setPriority(2).setAutoCancel(true).setContentTitle(zzh).setStyle(new BigTextStyle().bigText(zzj)).addAction(C1133R.C1134drawable.common_full_open_on_phone, resources.getString(C1133R.string.common_open_on_phone), pendingIntent);
                if (zzq.isAtLeastO() && zzq.isAtLeastO()) {
                    addAction.setChannelId(zza(context, notificationManager));
                }
                notification = addAction.build();
            } else {
                NotificationCompat.Builder style = new NotificationCompat.Builder(context).setSmallIcon(17301642).setTicker(resources.getString(C1133R.string.common_google_play_services_notification_ticker)).setWhen(System.currentTimeMillis()).setAutoCancel(true).setContentIntent(pendingIntent).setContentTitle(zzh).setContentText(zzj).setLocalOnly(true).setStyle(new NotificationCompat.BigTextStyle().bigText(zzj));
                if (zzq.isAtLeastO() && zzq.isAtLeastO()) {
                    style.setChannelId(zza(context, notificationManager));
                }
                notification = style.build();
            }
            switch (i) {
                case 1:
                case 2:
                case 3:
                    i2 = 10436;
                    zzp.zzfln.set(false);
                    break;
                default:
                    i2 = 39789;
                    break;
            }
            notificationManager.notify(i2, notification);
        }
    }

    @VisibleForTesting(otherwise = 2)
    private final String zzafx() {
        String str;
        synchronized (mLock) {
            str = this.zzfkv;
        }
        return str;
    }

    public Task<Void> checkApiAvailability(GoogleApi<?> googleApi, GoogleApi<?>... googleApiArr) {
        zzbq.checkNotNull(googleApi, "Requested API must not be null.");
        for (GoogleApi<?> checkNotNull : googleApiArr) {
            zzbq.checkNotNull(checkNotNull, "Requested API must not be null.");
        }
        ArrayList arrayList = new ArrayList(googleApiArr.length + 1);
        arrayList.add(googleApi);
        arrayList.addAll(Arrays.asList(googleApiArr));
        return zzbm.zzaiq().zza((Iterable<? extends GoogleApi<?>>) arrayList).continueWith(new zze(this));
    }

    public Dialog getErrorDialog(Activity activity, int i, int i2) {
        return getErrorDialog(activity, i, i2, null);
    }

    public Dialog getErrorDialog(Activity activity, int i, int i2, OnCancelListener onCancelListener) {
        return zza((Context) activity, i, zzv.zza(activity, zzf.zza(activity, i, "d"), i2), onCancelListener);
    }

    @Nullable
    public PendingIntent getErrorResolutionPendingIntent(Context context, int i, int i2) {
        return super.getErrorResolutionPendingIntent(context, i, i2);
    }

    @Nullable
    public PendingIntent getErrorResolutionPendingIntent(Context context, ConnectionResult connectionResult) {
        return connectionResult.hasResolution() ? connectionResult.getResolution() : getErrorResolutionPendingIntent(context, connectionResult.getErrorCode(), 0);
    }

    public final String getErrorString(int i) {
        return super.getErrorString(i);
    }

    public int isGooglePlayServicesAvailable(Context context) {
        return super.isGooglePlayServicesAvailable(context);
    }

    public final boolean isUserResolvableError(int i) {
        return super.isUserResolvableError(i);
    }

    @MainThread
    public Task<Void> makeGooglePlayServicesAvailable(Activity activity) {
        zzbq.zzge("makeGooglePlayServicesAvailable must be called from the main thread");
        int isGooglePlayServicesAvailable = isGooglePlayServicesAvailable(activity);
        if (isGooglePlayServicesAvailable == 0) {
            return Tasks.forResult(null);
        }
        zzcn zzp = zzcn.zzp(activity);
        zzp.zzb(new ConnectionResult(isGooglePlayServicesAvailable, null), 0);
        return zzp.getTask();
    }

    @TargetApi(26)
    public void setDefaultNotificationChannelId(@NonNull Context context, @NonNull String str) {
        if (zzq.isAtLeastO()) {
            zzbq.checkNotNull(((NotificationManager) context.getSystemService("notification")).getNotificationChannel(str));
        }
        synchronized (mLock) {
            this.zzfkv = str;
        }
    }

    public boolean showErrorDialogFragment(Activity activity, int i, int i2) {
        return showErrorDialogFragment(activity, i, i2, null);
    }

    public boolean showErrorDialogFragment(Activity activity, int i, int i2, OnCancelListener onCancelListener) {
        Dialog errorDialog = getErrorDialog(activity, i, i2, onCancelListener);
        if (errorDialog == null) {
            return false;
        }
        zza(activity, errorDialog, GooglePlayServicesUtil.GMS_ERROR_DIALOG, onCancelListener);
        return true;
    }

    public void showErrorNotification(Context context, int i) {
        zza(context, i, (String) null, zza(context, i, 0, "n"));
    }

    public void showErrorNotification(Context context, ConnectionResult connectionResult) {
        zza(context, connectionResult.getErrorCode(), (String) null, getErrorResolutionPendingIntent(context, connectionResult));
    }

    public final boolean zza(Activity activity, @NonNull zzcf zzcf, int i, int i2, OnCancelListener onCancelListener) {
        Dialog zza2 = zza((Context) activity, i, zzv.zza(zzcf, zzf.zza(activity, i, "d"), 2), onCancelListener);
        if (zza2 == null) {
            return false;
        }
        zza(activity, zza2, GooglePlayServicesUtil.GMS_ERROR_DIALOG, onCancelListener);
        return true;
    }

    public final boolean zza(Context context, ConnectionResult connectionResult, int i) {
        PendingIntent errorResolutionPendingIntent = getErrorResolutionPendingIntent(context, connectionResult);
        if (errorResolutionPendingIntent == null) {
            return false;
        }
        zza(context, connectionResult.getErrorCode(), (String) null, GoogleApiActivity.zza(context, errorResolutionPendingIntent, i));
        return true;
    }

    /* access modifiers changed from: 0000 */
    public final void zzcc(Context context) {
        new zza(context).sendEmptyMessageDelayed(1, 120000);
    }
}
