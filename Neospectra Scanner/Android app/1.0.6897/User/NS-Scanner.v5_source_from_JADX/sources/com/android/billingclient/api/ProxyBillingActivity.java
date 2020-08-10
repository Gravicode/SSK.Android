package com.android.billingclient.api;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import com.android.billingclient.util.BillingHelper;

public class ProxyBillingActivity extends Activity {
    private static final int REQUEST_CODE = 100;
    static final String RESPONSE_BUNDLE = "response_bundle_key";
    static final String RESPONSE_CODE = "response_code_key";
    static final String RESPONSE_INTENT_ACTION = "proxy_activity_response_intent_action";
    private static final String TAG = "ProxyBillingActivity";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BillingHelper.logVerbose(TAG, "Launching Play Store billing flow");
        try {
            startIntentSenderForResult(((PendingIntent) getIntent().getParcelableExtra(BillingHelper.RESPONSE_BUY_INTENT)).getIntentSender(), 100, new Intent(), 0, 0, 0);
        } catch (SendIntentException e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Got exception while trying to start a purchase flow: ");
            sb.append(e);
            BillingHelper.logWarn(str, sb.toString());
            broadcastResult(6, null);
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            int responseCode = BillingHelper.getResponseCodeFromIntent(data, TAG);
            if (!(resultCode == -1 && responseCode == 0)) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Got purchases updated result with resultCode ");
                sb.append(resultCode);
                sb.append(" and billing's responseCode: ");
                sb.append(responseCode);
                BillingHelper.logWarn(str, sb.toString());
            }
            broadcastResult(responseCode, data == null ? null : data.getExtras());
        } else {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Got onActivityResult with wrong requestCode: ");
            sb2.append(requestCode);
            sb2.append("; skipping...");
            BillingHelper.logWarn(str2, sb2.toString());
        }
        finish();
    }

    private void broadcastResult(int responseCode, Bundle resultBundle) {
        Intent intent = new Intent(RESPONSE_INTENT_ACTION);
        intent.putExtra(RESPONSE_CODE, responseCode);
        intent.putExtra(RESPONSE_BUNDLE, resultBundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
