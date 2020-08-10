package com.google.firebase.messaging;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.regex.Pattern;

public class FirebaseMessaging {
    public static final String INSTANCE_ID_SCOPE = "FCM";
    private static final Pattern zzoad = Pattern.compile("[a-zA-Z0-9-_.~%]{1,900}");
    private static FirebaseMessaging zzoae;
    private final FirebaseInstanceId zzoaf;

    private FirebaseMessaging(FirebaseInstanceId firebaseInstanceId) {
        this.zzoaf = firebaseInstanceId;
    }

    public static synchronized FirebaseMessaging getInstance() {
        FirebaseMessaging firebaseMessaging;
        synchronized (FirebaseMessaging.class) {
            if (zzoae == null) {
                zzoae = new FirebaseMessaging(FirebaseInstanceId.getInstance());
            }
            firebaseMessaging = zzoae;
        }
        return firebaseMessaging;
    }

    public void send(RemoteMessage remoteMessage) {
        if (TextUtils.isEmpty(remoteMessage.getTo())) {
            throw new IllegalArgumentException("Missing 'to'");
        }
        Context applicationContext = FirebaseApp.getInstance().getApplicationContext();
        Intent intent = new Intent("com.google.android.gcm.intent.SEND");
        Intent intent2 = new Intent();
        intent2.setPackage("com.google.example.invalidpackage");
        intent.putExtra("app", PendingIntent.getBroadcast(applicationContext, 0, intent2, 0));
        intent.setPackage("com.google.android.gms");
        intent.putExtras(remoteMessage.mBundle);
        applicationContext.sendOrderedBroadcast(intent, "com.google.android.gtalkservice.permission.GTALK_SERVICE");
    }

    public void subscribeToTopic(String str) {
        if (str != null && str.startsWith("/topics/")) {
            Log.w("FirebaseMessaging", "Format /topics/topic-name is deprecated. Only 'topic-name' should be used in subscribeToTopic.");
            str = str.substring(8);
        }
        if (str == null || !zzoad.matcher(str).matches()) {
            String str2 = "[a-zA-Z0-9-_.~%]{1,900}";
            StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 55 + String.valueOf(str2).length());
            sb.append("Invalid topic name: ");
            sb.append(str);
            sb.append(" does not match the allowed format ");
            sb.append(str2);
            throw new IllegalArgumentException(sb.toString());
        }
        FirebaseInstanceId firebaseInstanceId = this.zzoaf;
        String valueOf = String.valueOf("S!");
        String valueOf2 = String.valueOf(str);
        firebaseInstanceId.zzrf(valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
    }

    public void unsubscribeFromTopic(String str) {
        if (str != null && str.startsWith("/topics/")) {
            Log.w("FirebaseMessaging", "Format /topics/topic-name is deprecated. Only 'topic-name' should be used in unsubscribeFromTopic.");
            str = str.substring(8);
        }
        if (str == null || !zzoad.matcher(str).matches()) {
            String str2 = "[a-zA-Z0-9-_.~%]{1,900}";
            StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 55 + String.valueOf(str2).length());
            sb.append("Invalid topic name: ");
            sb.append(str);
            sb.append(" does not match the allowed format ");
            sb.append(str2);
            throw new IllegalArgumentException(sb.toString());
        }
        FirebaseInstanceId firebaseInstanceId = this.zzoaf;
        String valueOf = String.valueOf("U!");
        String valueOf2 = String.valueOf(str);
        firebaseInstanceId.zzrf(valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
    }
}
