package com.android.billingclient.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.math3.geometry.VectorFormat;

class LocalBroadcastManager {
    private static final boolean DEBUG = false;
    static final int MSG_EXEC_PENDING_BROADCASTS = 1;
    private static final String TAG = "LocalBroadcastManager";
    private static LocalBroadcastManager mInstance;
    private static final Object mLock = new Object();
    private final HashMap<String, ArrayList<ReceiverRecord>> mActions = new HashMap<>();
    private final Context mAppContext;
    private final Handler mHandler;
    private final ArrayList<BroadcastRecord> mPendingBroadcasts = new ArrayList<>();
    private final HashMap<BroadcastReceiver, ArrayList<IntentFilter>> mReceivers = new HashMap<>();

    private static class BroadcastRecord {
        final Intent intent;
        final ArrayList<ReceiverRecord> receivers;

        BroadcastRecord(Intent intent2, ArrayList<ReceiverRecord> receivers2) {
            this.intent = intent2;
            this.receivers = receivers2;
        }
    }

    private static class ReceiverRecord {
        boolean broadcasting;
        final IntentFilter filter;
        final BroadcastReceiver receiver;

        ReceiverRecord(IntentFilter filter2, BroadcastReceiver receiver2) {
            this.filter = filter2;
            this.receiver = receiver2;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder(128);
            builder.append("Receiver{");
            builder.append(this.receiver);
            builder.append(" filter=");
            builder.append(this.filter);
            builder.append(VectorFormat.DEFAULT_SUFFIX);
            return builder.toString();
        }
    }

    public static LocalBroadcastManager getInstance(Context context) {
        LocalBroadcastManager localBroadcastManager;
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new LocalBroadcastManager(context.getApplicationContext());
            }
            localBroadcastManager = mInstance;
        }
        return localBroadcastManager;
    }

    private LocalBroadcastManager(Context context) {
        this.mAppContext = context;
        this.mHandler = new Handler(context.getMainLooper()) {
            public void handleMessage(Message msg) {
                if (msg.what != 1) {
                    super.handleMessage(msg);
                } else {
                    LocalBroadcastManager.this.executePendingBroadcasts();
                }
            }
        };
    }

    public void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        synchronized (this.mReceivers) {
            ReceiverRecord entry = new ReceiverRecord(filter, receiver);
            ArrayList arrayList = (ArrayList) this.mReceivers.get(receiver);
            if (arrayList == null) {
                arrayList = new ArrayList(1);
                this.mReceivers.put(receiver, arrayList);
            }
            arrayList.add(filter);
            for (int i = 0; i < filter.countActions(); i++) {
                String action = filter.getAction(i);
                ArrayList arrayList2 = (ArrayList) this.mActions.get(action);
                if (arrayList2 == null) {
                    arrayList2 = new ArrayList(1);
                    this.mActions.put(action, arrayList2);
                }
                arrayList2.add(entry);
            }
        }
    }

    public void unregisterReceiver(BroadcastReceiver receiver) {
        synchronized (this.mReceivers) {
            ArrayList<IntentFilter> filters = (ArrayList) this.mReceivers.remove(receiver);
            if (filters != null) {
                for (int i = 0; i < filters.size(); i++) {
                    IntentFilter filter = (IntentFilter) filters.get(i);
                    for (int j = 0; j < filter.countActions(); j++) {
                        String action = filter.getAction(j);
                        ArrayList<ReceiverRecord> receivers = (ArrayList) this.mActions.get(action);
                        if (receivers != null) {
                            int k = 0;
                            while (k < receivers.size()) {
                                if (((ReceiverRecord) receivers.get(k)).receiver == receiver) {
                                    receivers.remove(k);
                                    k--;
                                }
                                k++;
                            }
                            if (receivers.size() <= 0) {
                                this.mActions.remove(action);
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:55:0x016a, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x016d, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean sendBroadcast(android.content.Intent r20) {
        /*
            r19 = this;
            r1 = r19
            r2 = r20
            java.util.HashMap<android.content.BroadcastReceiver, java.util.ArrayList<android.content.IntentFilter>> r3 = r1.mReceivers
            monitor-enter(r3)
            java.lang.String r5 = r20.getAction()     // Catch:{ all -> 0x016e }
            android.content.Context r4 = r1.mAppContext     // Catch:{ all -> 0x016e }
            android.content.ContentResolver r4 = r4.getContentResolver()     // Catch:{ all -> 0x016e }
            java.lang.String r4 = r2.resolveTypeIfNeeded(r4)     // Catch:{ all -> 0x016e }
            r11 = r4
            android.net.Uri r8 = r20.getData()     // Catch:{ all -> 0x016e }
            java.lang.String r4 = r20.getScheme()     // Catch:{ all -> 0x016e }
            r12 = r4
            java.util.Set r9 = r20.getCategories()     // Catch:{ all -> 0x016e }
            int r4 = r20.getFlags()     // Catch:{ all -> 0x016e }
            r4 = r4 & 8
            if (r4 == 0) goto L_0x002d
            r4 = 1
            goto L_0x002e
        L_0x002d:
            r4 = 0
        L_0x002e:
            r15 = r4
            if (r15 == 0) goto L_0x0057
            java.lang.String r4 = "LocalBroadcastManager"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x016e }
            r6.<init>()     // Catch:{ all -> 0x016e }
            java.lang.String r7 = "Resolving type "
            r6.append(r7)     // Catch:{ all -> 0x016e }
            r6.append(r11)     // Catch:{ all -> 0x016e }
            java.lang.String r7 = " scheme "
            r6.append(r7)     // Catch:{ all -> 0x016e }
            r6.append(r12)     // Catch:{ all -> 0x016e }
            java.lang.String r7 = " of intent "
            r6.append(r7)     // Catch:{ all -> 0x016e }
            r6.append(r2)     // Catch:{ all -> 0x016e }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x016e }
            android.util.Log.v(r4, r6)     // Catch:{ all -> 0x016e }
        L_0x0057:
            java.util.HashMap<java.lang.String, java.util.ArrayList<com.android.billingclient.api.LocalBroadcastManager$ReceiverRecord>> r4 = r1.mActions     // Catch:{ all -> 0x016e }
            java.lang.String r6 = r20.getAction()     // Catch:{ all -> 0x016e }
            java.lang.Object r4 = r4.get(r6)     // Catch:{ all -> 0x016e }
            java.util.ArrayList r4 = (java.util.ArrayList) r4     // Catch:{ all -> 0x016e }
            r10 = r4
            if (r10 == 0) goto L_0x016b
            if (r15 == 0) goto L_0x007e
            java.lang.String r4 = "LocalBroadcastManager"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x016e }
            r6.<init>()     // Catch:{ all -> 0x016e }
            java.lang.String r7 = "Action list: "
            r6.append(r7)     // Catch:{ all -> 0x016e }
            r6.append(r10)     // Catch:{ all -> 0x016e }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x016e }
            android.util.Log.v(r4, r6)     // Catch:{ all -> 0x016e }
        L_0x007e:
            r4 = 0
            r7 = r4
            r4 = 0
        L_0x0081:
            r6 = r4
            int r4 = r10.size()     // Catch:{ all -> 0x016e }
            if (r6 >= r4) goto L_0x0136
            java.lang.Object r4 = r10.get(r6)     // Catch:{ all -> 0x016e }
            com.android.billingclient.api.LocalBroadcastManager$ReceiverRecord r4 = (com.android.billingclient.api.LocalBroadcastManager.ReceiverRecord) r4     // Catch:{ all -> 0x016e }
            if (r15 == 0) goto L_0x00ab
            java.lang.String r13 = "LocalBroadcastManager"
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ all -> 0x016e }
            r14.<init>()     // Catch:{ all -> 0x016e }
            r16 = r6
            java.lang.String r6 = "Matching against filter "
            r14.append(r6)     // Catch:{ all -> 0x016e }
            android.content.IntentFilter r6 = r4.filter     // Catch:{ all -> 0x016e }
            r14.append(r6)     // Catch:{ all -> 0x016e }
            java.lang.String r6 = r14.toString()     // Catch:{ all -> 0x016e }
            android.util.Log.v(r13, r6)     // Catch:{ all -> 0x016e }
            goto L_0x00ad
        L_0x00ab:
            r16 = r6
        L_0x00ad:
            boolean r6 = r4.broadcasting     // Catch:{ all -> 0x016e }
            if (r6 == 0) goto L_0x00c1
            if (r15 == 0) goto L_0x00ba
            java.lang.String r6 = "LocalBroadcastManager"
            java.lang.String r13 = "  Filter's target already added"
            android.util.Log.v(r6, r13)     // Catch:{ all -> 0x016e }
        L_0x00ba:
            r18 = r10
            r17 = r11
            r11 = r7
            goto L_0x012d
        L_0x00c1:
            android.content.IntentFilter r6 = r4.filter     // Catch:{ all -> 0x016e }
            java.lang.String r13 = "LocalBroadcastManager"
            r14 = r4
            r4 = r6
            r6 = r11
            r17 = r11
            r11 = r7
            r7 = r12
            r18 = r10
            r10 = r13
            int r4 = r4.match(r5, r6, r7, r8, r9, r10)     // Catch:{ all -> 0x016e }
            if (r4 < 0) goto L_0x0102
            if (r15 == 0) goto L_0x00f1
            java.lang.String r6 = "LocalBroadcastManager"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x016e }
            r7.<init>()     // Catch:{ all -> 0x016e }
            java.lang.String r10 = "  Filter matched!  match=0x"
            r7.append(r10)     // Catch:{ all -> 0x016e }
            java.lang.String r10 = java.lang.Integer.toHexString(r4)     // Catch:{ all -> 0x016e }
            r7.append(r10)     // Catch:{ all -> 0x016e }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x016e }
            android.util.Log.v(r6, r7)     // Catch:{ all -> 0x016e }
        L_0x00f1:
            if (r11 != 0) goto L_0x00fa
            java.util.ArrayList r6 = new java.util.ArrayList     // Catch:{ all -> 0x016e }
            r6.<init>()     // Catch:{ all -> 0x016e }
            r7 = r6
            goto L_0x00fb
        L_0x00fa:
            r7 = r11
        L_0x00fb:
            r7.add(r14)     // Catch:{ all -> 0x016e }
            r6 = 1
            r14.broadcasting = r6     // Catch:{ all -> 0x016e }
            goto L_0x012e
        L_0x0102:
            if (r15 == 0) goto L_0x012d
            switch(r4) {
                case -4: goto L_0x0113;
                case -3: goto L_0x0110;
                case -2: goto L_0x010d;
                case -1: goto L_0x010a;
                default: goto L_0x0107;
            }     // Catch:{ all -> 0x016e }
        L_0x0107:
            java.lang.String r6 = "unknown reason"
            goto L_0x0116
        L_0x010a:
            java.lang.String r6 = "type"
            goto L_0x0116
        L_0x010d:
            java.lang.String r6 = "data"
            goto L_0x0116
        L_0x0110:
            java.lang.String r6 = "action"
            goto L_0x0116
        L_0x0113:
            java.lang.String r6 = "category"
        L_0x0116:
            java.lang.String r7 = "LocalBroadcastManager"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x016e }
            r10.<init>()     // Catch:{ all -> 0x016e }
            java.lang.String r13 = "  Filter did not match: "
            r10.append(r13)     // Catch:{ all -> 0x016e }
            r10.append(r6)     // Catch:{ all -> 0x016e }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x016e }
            android.util.Log.v(r7, r10)     // Catch:{ all -> 0x016e }
        L_0x012d:
            r7 = r11
        L_0x012e:
            int r4 = r16 + 1
            r11 = r17
            r10 = r18
            goto L_0x0081
        L_0x0136:
            r18 = r10
            r17 = r11
            r11 = r7
            if (r11 == 0) goto L_0x016b
            r4 = 0
        L_0x013e:
            int r6 = r11.size()     // Catch:{ all -> 0x016e }
            if (r4 >= r6) goto L_0x0150
            java.lang.Object r6 = r11.get(r4)     // Catch:{ all -> 0x016e }
            com.android.billingclient.api.LocalBroadcastManager$ReceiverRecord r6 = (com.android.billingclient.api.LocalBroadcastManager.ReceiverRecord) r6     // Catch:{ all -> 0x016e }
            r7 = 0
            r6.broadcasting = r7     // Catch:{ all -> 0x016e }
            int r4 = r4 + 1
            goto L_0x013e
        L_0x0150:
            java.util.ArrayList<com.android.billingclient.api.LocalBroadcastManager$BroadcastRecord> r4 = r1.mPendingBroadcasts     // Catch:{ all -> 0x016e }
            com.android.billingclient.api.LocalBroadcastManager$BroadcastRecord r6 = new com.android.billingclient.api.LocalBroadcastManager$BroadcastRecord     // Catch:{ all -> 0x016e }
            r6.<init>(r2, r11)     // Catch:{ all -> 0x016e }
            r4.add(r6)     // Catch:{ all -> 0x016e }
            android.os.Handler r4 = r1.mHandler     // Catch:{ all -> 0x016e }
            r6 = 1
            boolean r4 = r4.hasMessages(r6)     // Catch:{ all -> 0x016e }
            if (r4 != 0) goto L_0x0168
            android.os.Handler r4 = r1.mHandler     // Catch:{ all -> 0x016e }
            r4.sendEmptyMessage(r6)     // Catch:{ all -> 0x016e }
        L_0x0168:
            monitor-exit(r3)     // Catch:{ all -> 0x016e }
            r3 = 1
            return r3
        L_0x016b:
            monitor-exit(r3)     // Catch:{ all -> 0x016e }
            r3 = 0
            return r3
        L_0x016e:
            r0 = move-exception
            r4 = r0
            monitor-exit(r3)     // Catch:{ all -> 0x016e }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.billingclient.api.LocalBroadcastManager.sendBroadcast(android.content.Intent):boolean");
    }

    public void sendBroadcastSync(Intent intent) {
        if (sendBroadcast(intent)) {
            executePendingBroadcasts();
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001f, code lost:
        if (r2 >= r0.length) goto L_0x0000;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0021, code lost:
        r3 = r0[r2];
        r4 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002a, code lost:
        if (r4 >= r3.receivers.size()) goto L_0x0040;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002c, code lost:
        ((com.android.billingclient.api.LocalBroadcastManager.ReceiverRecord) r3.receivers.get(r4)).receiver.onReceive(r8.mAppContext, r3.intent);
        r4 = r4 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0040, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001c, code lost:
        r2 = 0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void executePendingBroadcasts() {
        /*
            r8 = this;
        L_0x0000:
            r0 = 0
            java.util.HashMap<android.content.BroadcastReceiver, java.util.ArrayList<android.content.IntentFilter>> r1 = r8.mReceivers
            monitor-enter(r1)
            java.util.ArrayList<com.android.billingclient.api.LocalBroadcastManager$BroadcastRecord> r2 = r8.mPendingBroadcasts     // Catch:{ all -> 0x0044 }
            int r2 = r2.size()     // Catch:{ all -> 0x0044 }
            if (r2 > 0) goto L_0x000e
            monitor-exit(r1)     // Catch:{ all -> 0x0044 }
            return
        L_0x000e:
            com.android.billingclient.api.LocalBroadcastManager$BroadcastRecord[] r3 = new com.android.billingclient.api.LocalBroadcastManager.BroadcastRecord[r2]     // Catch:{ all -> 0x0044 }
            r0 = r3
            java.util.ArrayList<com.android.billingclient.api.LocalBroadcastManager$BroadcastRecord> r3 = r8.mPendingBroadcasts     // Catch:{ all -> 0x0044 }
            r3.toArray(r0)     // Catch:{ all -> 0x0044 }
            java.util.ArrayList<com.android.billingclient.api.LocalBroadcastManager$BroadcastRecord> r3 = r8.mPendingBroadcasts     // Catch:{ all -> 0x0044 }
            r3.clear()     // Catch:{ all -> 0x0044 }
            monitor-exit(r1)     // Catch:{ all -> 0x0044 }
            r1 = 0
            r2 = 0
        L_0x001e:
            int r3 = r0.length
            if (r2 >= r3) goto L_0x0043
            r3 = r0[r2]
            r4 = 0
        L_0x0024:
            java.util.ArrayList<com.android.billingclient.api.LocalBroadcastManager$ReceiverRecord> r5 = r3.receivers
            int r5 = r5.size()
            if (r4 >= r5) goto L_0x0040
            java.util.ArrayList<com.android.billingclient.api.LocalBroadcastManager$ReceiverRecord> r5 = r3.receivers
            java.lang.Object r5 = r5.get(r4)
            com.android.billingclient.api.LocalBroadcastManager$ReceiverRecord r5 = (com.android.billingclient.api.LocalBroadcastManager.ReceiverRecord) r5
            android.content.BroadcastReceiver r5 = r5.receiver
            android.content.Context r6 = r8.mAppContext
            android.content.Intent r7 = r3.intent
            r5.onReceive(r6, r7)
            int r4 = r4 + 1
            goto L_0x0024
        L_0x0040:
            int r2 = r2 + 1
            goto L_0x001e
        L_0x0043:
            goto L_0x0000
        L_0x0044:
            r2 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0044 }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.billingclient.api.LocalBroadcastManager.executePendingBroadcasts():void");
    }
}
