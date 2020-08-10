package com.android.billingclient.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import com.android.billingclient.util.BillingHelper;

class BillingBroadcastManager {
    private static final String ACTION = "com.android.vending.billing.PURCHASES_UPDATED";
    private static final String TAG = "BillingBroadcastManager";
    private final Context mContext;
    private final BillingBroadcastReceiver mReceiver;

    private class BillingBroadcastReceiver extends BroadcastReceiver {
        /* access modifiers changed from: private */
        public final PurchasesUpdatedListener mListener;

        private BillingBroadcastReceiver(@NonNull PurchasesUpdatedListener listener) {
            this.mListener = listener;
        }

        public void onReceive(Context context, Intent intent) {
            this.mListener.onPurchasesUpdated(BillingHelper.getResponseCodeFromIntent(intent, BillingBroadcastManager.TAG), BillingHelper.extractPurchases(intent.getExtras()));
        }
    }

    BillingBroadcastManager(Context context, @NonNull PurchasesUpdatedListener listener) {
        this.mContext = context;
        this.mReceiver = new BillingBroadcastReceiver(listener);
    }

    /* access modifiers changed from: 0000 */
    public void registerReceiver() {
        this.mContext.registerReceiver(this.mReceiver, new IntentFilter(ACTION));
    }

    /* access modifiers changed from: 0000 */
    public PurchasesUpdatedListener getListener() {
        return this.mReceiver.mListener;
    }

    /* access modifiers changed from: 0000 */
    public void destroy() {
        try {
            this.mContext.unregisterReceiver(this.mReceiver);
        } catch (IllegalArgumentException ex) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Receiver was already unregistered: ");
            sb.append(ex);
            BillingHelper.logWarn(str, sb.toString());
        }
    }
}
