package com.google.firebase.messaging;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.google.firebase.iid.zzb;
import com.google.firebase.iid.zzx;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class FirebaseMessagingService extends zzb {
    private static final Queue<String> zzoag = new ArrayDeque(10);

    static boolean zzaj(Bundle bundle) {
        if (bundle == null) {
            return false;
        }
        return "1".equals(bundle.getString("google.c.a.e"));
    }

    static void zzq(Bundle bundle) {
        Iterator it = bundle.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (str != null && str.startsWith("google.c.")) {
                it.remove();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00e0, code lost:
        if (r1.equals("send_error") != false) goto L_0x00f8;
     */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0032  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0058  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00ad  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00fb  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x010f  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x012c  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0136  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x013a  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0187  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void handleIntent(android.content.Intent r10) {
        /*
            r9 = this;
            java.lang.String r0 = r10.getAction()
            if (r0 != 0) goto L_0x0008
            java.lang.String r0 = ""
        L_0x0008:
            int r1 = r0.hashCode()
            r2 = 75300319(0x47cfddf, float:2.973903E-36)
            r3 = -1
            r4 = 1
            r5 = 0
            if (r1 == r2) goto L_0x0024
            r2 = 366519424(0x15d8a480, float:8.750124E-26)
            if (r1 == r2) goto L_0x001a
            goto L_0x002e
        L_0x001a:
            java.lang.String r1 = "com.google.android.c2dm.intent.RECEIVE"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x002e
            r0 = 0
            goto L_0x002f
        L_0x0024:
            java.lang.String r1 = "com.google.firebase.messaging.NOTIFICATION_DISMISS"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x002e
            r0 = 1
            goto L_0x002f
        L_0x002e:
            r0 = -1
        L_0x002f:
            switch(r0) {
                case 0: goto L_0x0058;
                case 1: goto L_0x004a;
                default: goto L_0x0032;
            }
        L_0x0032:
            java.lang.String r0 = "FirebaseMessaging"
            java.lang.String r1 = "Unknown intent action: "
            java.lang.String r10 = r10.getAction()
            java.lang.String r10 = java.lang.String.valueOf(r10)
            int r2 = r10.length()
            if (r2 == 0) goto L_0x0199
            java.lang.String r10 = r1.concat(r10)
            goto L_0x019e
        L_0x004a:
            android.os.Bundle r0 = r10.getExtras()
            boolean r0 = zzaj(r0)
            if (r0 == 0) goto L_0x01a1
            com.google.firebase.messaging.zzd.zzh(r9, r10)
            return
        L_0x0058:
            java.lang.String r0 = "google.message_id"
            java.lang.String r0 = r10.getStringExtra(r0)
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            r2 = 3
            if (r1 == 0) goto L_0x0067
        L_0x0065:
            r1 = 0
            goto L_0x00aa
        L_0x0067:
            java.util.Queue<java.lang.String> r1 = zzoag
            boolean r1 = r1.contains(r0)
            if (r1 == 0) goto L_0x0095
            java.lang.String r1 = "FirebaseMessaging"
            boolean r1 = android.util.Log.isLoggable(r1, r2)
            if (r1 == 0) goto L_0x0093
            java.lang.String r1 = "FirebaseMessaging"
            java.lang.String r6 = "Received duplicate message: "
            java.lang.String r7 = java.lang.String.valueOf(r0)
            int r8 = r7.length()
            if (r8 == 0) goto L_0x008a
            java.lang.String r6 = r6.concat(r7)
            goto L_0x0090
        L_0x008a:
            java.lang.String r7 = new java.lang.String
            r7.<init>(r6)
            r6 = r7
        L_0x0090:
            android.util.Log.d(r1, r6)
        L_0x0093:
            r1 = 1
            goto L_0x00aa
        L_0x0095:
            java.util.Queue<java.lang.String> r1 = zzoag
            int r1 = r1.size()
            r6 = 10
            if (r1 < r6) goto L_0x00a4
            java.util.Queue<java.lang.String> r1 = zzoag
            r1.remove()
        L_0x00a4:
            java.util.Queue<java.lang.String> r1 = zzoag
            r1.add(r0)
            goto L_0x0065
        L_0x00aa:
            r6 = 2
            if (r1 != 0) goto L_0x0181
            java.lang.String r1 = "message_type"
            java.lang.String r1 = r10.getStringExtra(r1)
            if (r1 != 0) goto L_0x00b7
            java.lang.String r1 = "gcm"
        L_0x00b7:
            int r7 = r1.hashCode()
            r8 = -2062414158(0xffffffff85120eb2, float:-6.867586E-36)
            if (r7 == r8) goto L_0x00ed
            r4 = 102161(0x18f11, float:1.43158E-40)
            if (r7 == r4) goto L_0x00e3
            r4 = 814694033(0x308f3e91, float:1.0422402E-9)
            if (r7 == r4) goto L_0x00da
            r2 = 814800675(0x3090df23, float:1.0540798E-9)
            if (r7 == r2) goto L_0x00d0
            goto L_0x00f7
        L_0x00d0:
            java.lang.String r2 = "send_event"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x00f7
            r2 = 2
            goto L_0x00f8
        L_0x00da:
            java.lang.String r4 = "send_error"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x00f7
            goto L_0x00f8
        L_0x00e3:
            java.lang.String r2 = "gcm"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x00f7
            r2 = 0
            goto L_0x00f8
        L_0x00ed:
            java.lang.String r2 = "deleted_messages"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L_0x00f7
            r2 = 1
            goto L_0x00f8
        L_0x00f7:
            r2 = -1
        L_0x00f8:
            switch(r2) {
                case 0: goto L_0x013a;
                case 1: goto L_0x0136;
                case 2: goto L_0x012c;
                case 3: goto L_0x010f;
                default: goto L_0x00fb;
            }
        L_0x00fb:
            java.lang.String r10 = "FirebaseMessaging"
            java.lang.String r2 = "Received message with unknown type: "
            java.lang.String r1 = java.lang.String.valueOf(r1)
            int r3 = r1.length()
            if (r3 == 0) goto L_0x0179
            java.lang.String r1 = r2.concat(r1)
            goto L_0x017e
        L_0x010f:
            java.lang.String r1 = "google.message_id"
            java.lang.String r1 = r10.getStringExtra(r1)
            if (r1 != 0) goto L_0x011d
            java.lang.String r1 = "message_id"
            java.lang.String r1 = r10.getStringExtra(r1)
        L_0x011d:
            com.google.firebase.messaging.SendException r2 = new com.google.firebase.messaging.SendException
            java.lang.String r3 = "error"
            java.lang.String r10 = r10.getStringExtra(r3)
            r2.<init>(r10)
            r9.onSendError(r1, r2)
            goto L_0x0181
        L_0x012c:
            java.lang.String r1 = "google.message_id"
            java.lang.String r10 = r10.getStringExtra(r1)
            r9.onMessageSent(r10)
            goto L_0x0181
        L_0x0136:
            r9.onDeletedMessages()
            goto L_0x0181
        L_0x013a:
            android.os.Bundle r1 = r10.getExtras()
            boolean r1 = zzaj(r1)
            if (r1 == 0) goto L_0x0147
            com.google.firebase.messaging.zzd.zzf(r9, r10)
        L_0x0147:
            android.os.Bundle r1 = r10.getExtras()
            if (r1 != 0) goto L_0x0152
            android.os.Bundle r1 = new android.os.Bundle
            r1.<init>()
        L_0x0152:
            java.lang.String r2 = "android.support.content.wakelockid"
            r1.remove(r2)
            boolean r2 = com.google.firebase.messaging.zza.zzag(r1)
            if (r2 == 0) goto L_0x0170
            com.google.firebase.messaging.zza r2 = com.google.firebase.messaging.zza.zzex(r9)
            boolean r2 = r2.zzs(r1)
            if (r2 != 0) goto L_0x0181
            boolean r2 = zzaj(r1)
            if (r2 == 0) goto L_0x0170
            com.google.firebase.messaging.zzd.zzi(r9, r10)
        L_0x0170:
            com.google.firebase.messaging.RemoteMessage r10 = new com.google.firebase.messaging.RemoteMessage
            r10.<init>(r1)
            r9.onMessageReceived(r10)
            goto L_0x0181
        L_0x0179:
            java.lang.String r1 = new java.lang.String
            r1.<init>(r2)
        L_0x017e:
            android.util.Log.w(r10, r1)
        L_0x0181:
            boolean r10 = android.text.TextUtils.isEmpty(r0)
            if (r10 != 0) goto L_0x0198
            android.os.Bundle r10 = new android.os.Bundle
            r10.<init>()
            java.lang.String r1 = "google.message_id"
            r10.putString(r1, r0)
            com.google.firebase.iid.zzi r0 = com.google.firebase.iid.zzi.zzev(r9)
            r0.zzh(r6, r10)
        L_0x0198:
            return
        L_0x0199:
            java.lang.String r10 = new java.lang.String
            r10.<init>(r1)
        L_0x019e:
            android.util.Log.d(r0, r10)
        L_0x01a1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.messaging.FirebaseMessagingService.handleIntent(android.content.Intent):void");
    }

    @WorkerThread
    public void onDeletedMessages() {
    }

    @WorkerThread
    public void onMessageReceived(RemoteMessage remoteMessage) {
    }

    @WorkerThread
    public void onMessageSent(String str) {
    }

    @WorkerThread
    public void onSendError(String str, Exception exc) {
    }

    /* access modifiers changed from: protected */
    public final Intent zzp(Intent intent) {
        return zzx.zzcjk().zzcjl();
    }

    public final boolean zzq(Intent intent) {
        if (!"com.google.firebase.messaging.NOTIFICATION_OPEN".equals(intent.getAction())) {
            return false;
        }
        PendingIntent pendingIntent = (PendingIntent) intent.getParcelableExtra("pending_intent");
        if (pendingIntent != null) {
            try {
                pendingIntent.send();
            } catch (CanceledException e) {
                Log.e("FirebaseMessaging", "Notification pending intent canceled");
            }
        }
        if (zzaj(intent.getExtras())) {
            zzd.zzg(this, intent);
        }
        return true;
    }
}
