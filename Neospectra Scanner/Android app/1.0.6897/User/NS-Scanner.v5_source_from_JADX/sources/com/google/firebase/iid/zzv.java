package com.google.firebase.iid;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.support.p001v4.util.SimpleArrayMap;
import android.util.Log;
import com.google.android.gms.iid.MessengerCompat;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class zzv {
    private static PendingIntent zzicn;
    private static int zzift = 0;
    private final Context zzair;
    private Messenger zzicr;
    private Messenger zzifw;
    private MessengerCompat zzifx;
    private final zzu zznys;
    private final SimpleArrayMap<String, TaskCompletionSource<Bundle>> zznzn = new SimpleArrayMap<>();

    public zzv(Context context, zzu zzu) {
        this.zzair = context;
        this.zznys = zzu;
        this.zzicr = new Messenger(new zzw(this, Looper.getMainLooper()));
    }

    private final Bundle zzae(Bundle bundle) throws IOException {
        Bundle zzaf = zzaf(bundle);
        if (zzaf == null || !zzaf.containsKey("google.messenger")) {
            return zzaf;
        }
        Bundle zzaf2 = zzaf(bundle);
        if (zzaf2 == null || !zzaf2.containsKey("google.messenger")) {
            return zzaf2;
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x00f7 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final android.os.Bundle zzaf(android.os.Bundle r9) throws java.io.IOException {
        /*
            r8 = this;
            java.lang.String r0 = zzavi()
            com.google.android.gms.tasks.TaskCompletionSource r1 = new com.google.android.gms.tasks.TaskCompletionSource
            r1.<init>()
            android.support.v4.util.SimpleArrayMap<java.lang.String, com.google.android.gms.tasks.TaskCompletionSource<android.os.Bundle>> r2 = r8.zznzn
            monitor-enter(r2)
            android.support.v4.util.SimpleArrayMap<java.lang.String, com.google.android.gms.tasks.TaskCompletionSource<android.os.Bundle>> r3 = r8.zznzn     // Catch:{ all -> 0x0132 }
            r3.put(r0, r1)     // Catch:{ all -> 0x0132 }
            monitor-exit(r2)     // Catch:{ all -> 0x0132 }
            com.google.firebase.iid.zzu r2 = r8.zznys
            int r2 = r2.zzcjf()
            if (r2 != 0) goto L_0x0022
            java.io.IOException r9 = new java.io.IOException
            java.lang.String r0 = "MISSING_INSTANCEID_SERVICE"
            r9.<init>(r0)
            throw r9
        L_0x0022:
            android.content.Intent r2 = new android.content.Intent
            r2.<init>()
            java.lang.String r3 = "com.google.android.gms"
            r2.setPackage(r3)
            com.google.firebase.iid.zzu r3 = r8.zznys
            int r3 = r3.zzcjf()
            r4 = 2
            if (r3 != r4) goto L_0x003b
            java.lang.String r3 = "com.google.iid.TOKEN_REQUEST"
        L_0x0037:
            r2.setAction(r3)
            goto L_0x003e
        L_0x003b:
            java.lang.String r3 = "com.google.android.c2dm.intent.REGISTER"
            goto L_0x0037
        L_0x003e:
            r2.putExtras(r9)
            android.content.Context r9 = r8.zzair
            zzd(r9, r2)
            java.lang.String r9 = "kid"
            java.lang.String r3 = java.lang.String.valueOf(r0)
            int r3 = r3.length()
            int r3 = r3 + 5
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>(r3)
            java.lang.String r3 = "|ID|"
            r5.append(r3)
            r5.append(r0)
            java.lang.String r3 = "|"
            r5.append(r3)
            java.lang.String r3 = r5.toString()
            r2.putExtra(r9, r3)
            java.lang.String r9 = "FirebaseInstanceId"
            r3 = 3
            boolean r9 = android.util.Log.isLoggable(r9, r3)
            if (r9 == 0) goto L_0x009e
            java.lang.String r9 = "FirebaseInstanceId"
            android.os.Bundle r5 = r2.getExtras()
            java.lang.String r5 = java.lang.String.valueOf(r5)
            java.lang.String r6 = java.lang.String.valueOf(r5)
            int r6 = r6.length()
            int r6 = r6 + 8
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>(r6)
            java.lang.String r6 = "Sending "
            r7.append(r6)
            r7.append(r5)
            java.lang.String r5 = r7.toString()
            android.util.Log.d(r9, r5)
        L_0x009e:
            java.lang.String r9 = "google.messenger"
            android.os.Messenger r5 = r8.zzicr
            r2.putExtra(r9, r5)
            android.os.Messenger r9 = r8.zzifw
            if (r9 != 0) goto L_0x00ad
            com.google.android.gms.iid.MessengerCompat r9 = r8.zzifx
            if (r9 == 0) goto L_0x00d3
        L_0x00ad:
            android.os.Message r9 = android.os.Message.obtain()
            r9.obj = r2
            android.os.Messenger r5 = r8.zzifw     // Catch:{ RemoteException -> 0x00c3 }
            if (r5 == 0) goto L_0x00bd
            android.os.Messenger r5 = r8.zzifw     // Catch:{ RemoteException -> 0x00c3 }
            r5.send(r9)     // Catch:{ RemoteException -> 0x00c3 }
            goto L_0x00e6
        L_0x00bd:
            com.google.android.gms.iid.MessengerCompat r5 = r8.zzifx     // Catch:{ RemoteException -> 0x00c3 }
            r5.send(r9)     // Catch:{ RemoteException -> 0x00c3 }
            goto L_0x00e6
        L_0x00c3:
            r9 = move-exception
            java.lang.String r9 = "FirebaseInstanceId"
            boolean r9 = android.util.Log.isLoggable(r9, r3)
            if (r9 == 0) goto L_0x00d3
            java.lang.String r9 = "FirebaseInstanceId"
            java.lang.String r3 = "Messenger failed, fallback to startService"
            android.util.Log.d(r9, r3)
        L_0x00d3:
            com.google.firebase.iid.zzu r9 = r8.zznys
            int r9 = r9.zzcjf()
            if (r9 != r4) goto L_0x00e1
            android.content.Context r9 = r8.zzair
            r9.sendBroadcast(r2)
            goto L_0x00e6
        L_0x00e1:
            android.content.Context r9 = r8.zzair
            r9.startService(r2)
        L_0x00e6:
            com.google.android.gms.tasks.Task r9 = r1.getTask()     // Catch:{ InterruptedException | TimeoutException -> 0x0115, ExecutionException -> 0x0103 }
            r1 = 30000(0x7530, double:1.4822E-319)
            java.util.concurrent.TimeUnit r3 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ InterruptedException | TimeoutException -> 0x0115, ExecutionException -> 0x0103 }
            java.lang.Object r9 = com.google.android.gms.tasks.Tasks.await(r9, r1, r3)     // Catch:{ InterruptedException | TimeoutException -> 0x0115, ExecutionException -> 0x0103 }
            android.os.Bundle r9 = (android.os.Bundle) r9     // Catch:{ InterruptedException | TimeoutException -> 0x0115, ExecutionException -> 0x0103 }
            android.support.v4.util.SimpleArrayMap<java.lang.String, com.google.android.gms.tasks.TaskCompletionSource<android.os.Bundle>> r1 = r8.zznzn
            monitor-enter(r1)
            android.support.v4.util.SimpleArrayMap<java.lang.String, com.google.android.gms.tasks.TaskCompletionSource<android.os.Bundle>> r2 = r8.zznzn     // Catch:{ all -> 0x00fe }
            r2.remove(r0)     // Catch:{ all -> 0x00fe }
            monitor-exit(r1)     // Catch:{ all -> 0x00fe }
            return r9
        L_0x00fe:
            r9 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x00fe }
            throw r9
        L_0x0101:
            r9 = move-exception
            goto L_0x0125
        L_0x0103:
            r9 = move-exception
            java.lang.Throwable r9 = r9.getCause()     // Catch:{ all -> 0x0101 }
            boolean r1 = r9 instanceof java.io.IOException     // Catch:{ all -> 0x0101 }
            if (r1 == 0) goto L_0x010f
            java.io.IOException r9 = (java.io.IOException) r9     // Catch:{ all -> 0x0101 }
            throw r9     // Catch:{ all -> 0x0101 }
        L_0x010f:
            java.io.IOException r1 = new java.io.IOException     // Catch:{ all -> 0x0101 }
            r1.<init>(r9)     // Catch:{ all -> 0x0101 }
            throw r1     // Catch:{ all -> 0x0101 }
        L_0x0115:
            r9 = move-exception
            java.lang.String r9 = "FirebaseInstanceId"
            java.lang.String r1 = "No response"
            android.util.Log.w(r9, r1)     // Catch:{ all -> 0x0101 }
            java.io.IOException r9 = new java.io.IOException     // Catch:{ all -> 0x0101 }
            java.lang.String r1 = "TIMEOUT"
            r9.<init>(r1)     // Catch:{ all -> 0x0101 }
            throw r9     // Catch:{ all -> 0x0101 }
        L_0x0125:
            android.support.v4.util.SimpleArrayMap<java.lang.String, com.google.android.gms.tasks.TaskCompletionSource<android.os.Bundle>> r1 = r8.zznzn
            monitor-enter(r1)
            android.support.v4.util.SimpleArrayMap<java.lang.String, com.google.android.gms.tasks.TaskCompletionSource<android.os.Bundle>> r2 = r8.zznzn     // Catch:{ all -> 0x012f }
            r2.remove(r0)     // Catch:{ all -> 0x012f }
            monitor-exit(r1)     // Catch:{ all -> 0x012f }
            throw r9
        L_0x012f:
            r9 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x012f }
            throw r9
        L_0x0132:
            r9 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0132 }
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzv.zzaf(android.os.Bundle):android.os.Bundle");
    }

    private static synchronized String zzavi() {
        String num;
        synchronized (zzv.class) {
            int i = zzift;
            zzift = i + 1;
            num = Integer.toString(i);
        }
        return num;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0057, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void zzbl(java.lang.String r4, java.lang.String r5) {
        /*
            r3 = this;
            android.support.v4.util.SimpleArrayMap<java.lang.String, com.google.android.gms.tasks.TaskCompletionSource<android.os.Bundle>> r0 = r3.zznzn
            monitor-enter(r0)
            if (r4 != 0) goto L_0x0027
            r4 = 0
        L_0x0006:
            android.support.v4.util.SimpleArrayMap<java.lang.String, com.google.android.gms.tasks.TaskCompletionSource<android.os.Bundle>> r1 = r3.zznzn     // Catch:{ all -> 0x0058 }
            int r1 = r1.size()     // Catch:{ all -> 0x0058 }
            if (r4 >= r1) goto L_0x0021
            android.support.v4.util.SimpleArrayMap<java.lang.String, com.google.android.gms.tasks.TaskCompletionSource<android.os.Bundle>> r1 = r3.zznzn     // Catch:{ all -> 0x0058 }
            java.lang.Object r1 = r1.valueAt(r4)     // Catch:{ all -> 0x0058 }
            com.google.android.gms.tasks.TaskCompletionSource r1 = (com.google.android.gms.tasks.TaskCompletionSource) r1     // Catch:{ all -> 0x0058 }
            java.io.IOException r2 = new java.io.IOException     // Catch:{ all -> 0x0058 }
            r2.<init>(r5)     // Catch:{ all -> 0x0058 }
            r1.setException(r2)     // Catch:{ all -> 0x0058 }
            int r4 = r4 + 1
            goto L_0x0006
        L_0x0021:
            android.support.v4.util.SimpleArrayMap<java.lang.String, com.google.android.gms.tasks.TaskCompletionSource<android.os.Bundle>> r4 = r3.zznzn     // Catch:{ all -> 0x0058 }
            r4.clear()     // Catch:{ all -> 0x0058 }
            goto L_0x0056
        L_0x0027:
            android.support.v4.util.SimpleArrayMap<java.lang.String, com.google.android.gms.tasks.TaskCompletionSource<android.os.Bundle>> r1 = r3.zznzn     // Catch:{ all -> 0x0058 }
            java.lang.Object r1 = r1.remove(r4)     // Catch:{ all -> 0x0058 }
            com.google.android.gms.tasks.TaskCompletionSource r1 = (com.google.android.gms.tasks.TaskCompletionSource) r1     // Catch:{ all -> 0x0058 }
            if (r1 != 0) goto L_0x004e
            java.lang.String r5 = "FirebaseInstanceId"
            java.lang.String r1 = "Missing callback for "
            java.lang.String r4 = java.lang.String.valueOf(r4)     // Catch:{ all -> 0x0058 }
            int r2 = r4.length()     // Catch:{ all -> 0x0058 }
            if (r2 == 0) goto L_0x0044
            java.lang.String r4 = r1.concat(r4)     // Catch:{ all -> 0x0058 }
            goto L_0x0049
        L_0x0044:
            java.lang.String r4 = new java.lang.String     // Catch:{ all -> 0x0058 }
            r4.<init>(r1)     // Catch:{ all -> 0x0058 }
        L_0x0049:
            android.util.Log.w(r5, r4)     // Catch:{ all -> 0x0058 }
            monitor-exit(r0)     // Catch:{ all -> 0x0058 }
            return
        L_0x004e:
            java.io.IOException r4 = new java.io.IOException     // Catch:{ all -> 0x0058 }
            r4.<init>(r5)     // Catch:{ all -> 0x0058 }
            r1.setException(r4)     // Catch:{ all -> 0x0058 }
        L_0x0056:
            monitor-exit(r0)     // Catch:{ all -> 0x0058 }
            return
        L_0x0058:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0058 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzv.zzbl(java.lang.String, java.lang.String):void");
    }

    private static synchronized void zzd(Context context, Intent intent) {
        synchronized (zzv.class) {
            if (zzicn == null) {
                Intent intent2 = new Intent();
                intent2.setPackage("com.google.example.invalidpackage");
                zzicn = PendingIntent.getBroadcast(context, 0, intent2, 0);
            }
            intent.putExtra("app", zzicn);
        }
    }

    /* access modifiers changed from: private */
    public final void zze(Message message) {
        if (message == null || !(message.obj instanceof Intent)) {
            Log.w("FirebaseInstanceId", "Dropping invalid message");
            return;
        }
        Intent intent = (Intent) message.obj;
        intent.setExtrasClassLoader(MessengerCompat.class.getClassLoader());
        if (intent.hasExtra("google.messenger")) {
            Parcelable parcelableExtra = intent.getParcelableExtra("google.messenger");
            if (parcelableExtra instanceof MessengerCompat) {
                this.zzifx = (MessengerCompat) parcelableExtra;
            }
            if (parcelableExtra instanceof Messenger) {
                this.zzifw = (Messenger) parcelableExtra;
            }
        }
        Intent intent2 = (Intent) message.obj;
        String action = intent2.getAction();
        if (!"com.google.android.c2dm.intent.REGISTRATION".equals(action)) {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                String str = "FirebaseInstanceId";
                String str2 = "Unexpected response action: ";
                String valueOf = String.valueOf(action);
                Log.d(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
            }
            return;
        }
        String stringExtra = intent2.getStringExtra("registration_id");
        if (stringExtra == null) {
            stringExtra = intent2.getStringExtra("unregistered");
        }
        if (stringExtra == null) {
            String stringExtra2 = intent2.getStringExtra("error");
            if (stringExtra2 == null) {
                String valueOf2 = String.valueOf(intent2.getExtras());
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf2).length() + 49);
                sb.append("Unexpected response, no error or registration id ");
                sb.append(valueOf2);
                Log.w("FirebaseInstanceId", sb.toString());
                return;
            }
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                String str3 = "FirebaseInstanceId";
                String str4 = "Received InstanceID error ";
                String valueOf3 = String.valueOf(stringExtra2);
                Log.d(str3, valueOf3.length() != 0 ? str4.concat(valueOf3) : new String(str4));
            }
            String str5 = null;
            if (stringExtra2.startsWith("|")) {
                String[] split = stringExtra2.split("\\|");
                if (!"ID".equals(split[1])) {
                    String str6 = "FirebaseInstanceId";
                    String str7 = "Unexpected structured response ";
                    String valueOf4 = String.valueOf(stringExtra2);
                    Log.w(str6, valueOf4.length() != 0 ? str7.concat(valueOf4) : new String(str7));
                }
                if (split.length > 2) {
                    String str8 = split[2];
                    String str9 = split[3];
                    if (str9.startsWith(":")) {
                        str9 = str9.substring(1);
                    }
                    str5 = str8;
                    stringExtra2 = str9;
                } else {
                    stringExtra2 = "UNKNOWN";
                }
                intent2.putExtra("error", stringExtra2);
            }
            zzbl(str5, stringExtra2);
            return;
        }
        Matcher matcher = Pattern.compile("\\|ID\\|([^|]+)\\|:?+(.*)").matcher(stringExtra);
        if (!matcher.matches()) {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                String str10 = "FirebaseInstanceId";
                String str11 = "Unexpected response string: ";
                String valueOf5 = String.valueOf(stringExtra);
                Log.d(str10, valueOf5.length() != 0 ? str11.concat(valueOf5) : new String(str11));
            }
            return;
        }
        String group = matcher.group(1);
        String group2 = matcher.group(2);
        Bundle extras = intent2.getExtras();
        extras.putString("registration_id", group2);
        synchronized (this.zznzn) {
            TaskCompletionSource taskCompletionSource = (TaskCompletionSource) this.zznzn.remove(group);
            if (taskCompletionSource == null) {
                String str12 = "FirebaseInstanceId";
                String str13 = "Missing callback for ";
                String valueOf6 = String.valueOf(group);
                Log.w(str12, valueOf6.length() != 0 ? str13.concat(valueOf6) : new String(str13));
                return;
            }
            taskCompletionSource.setResult(extras);
        }
    }

    /* access modifiers changed from: 0000 */
    public final Bundle zzad(Bundle bundle) throws IOException {
        if (this.zznys.zzcji() < 12000000) {
            return zzae(bundle);
        }
        try {
            return (Bundle) Tasks.await(zzi.zzev(this.zzair).zzi(1, bundle));
        } catch (InterruptedException | ExecutionException e) {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                String valueOf = String.valueOf(e);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 22);
                sb.append("Error making request: ");
                sb.append(valueOf);
                Log.d("FirebaseInstanceId", sb.toString());
            }
            if (!(e.getCause() instanceof zzs) || ((zzs) e.getCause()).getErrorCode() != 4) {
                return null;
            }
            return zzae(bundle);
        }
    }
}
