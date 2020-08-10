package com.android.billingclient.util;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.android.billingclient.api.Purchase;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;

public final class BillingHelper {
    public static final String INAPP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN";
    public static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    public static final String RESPONSE_BUY_INTENT = "BUY_INTENT";
    public static final String RESPONSE_CODE = "RESPONSE_CODE";
    public static final String RESPONSE_GET_SKU_DETAILS_LIST = "DETAILS_LIST";
    public static final String RESPONSE_INAPP_ITEM_LIST = "INAPP_PURCHASE_ITEM_LIST";
    private static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
    public static final String RESPONSE_INAPP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST";
    private static final String RESPONSE_INAPP_SIGNATURE = "INAPP_DATA_SIGNATURE";
    public static final String RESPONSE_INAPP_SIGNATURE_LIST = "INAPP_DATA_SIGNATURE_LIST";
    private static final String TAG = "BillingHelper";

    public static void logVerbose(String tag, String msg) {
        if (Log.isLoggable(tag, 2)) {
            Log.v(tag, msg);
        }
    }

    public static void logWarn(String tag, String msg) {
        if (Log.isLoggable(tag, 5)) {
            Log.w(tag, msg);
        }
    }

    public static int getResponseCodeFromIntent(Intent intent, String tag) {
        if (intent != null) {
            return getResponseCodeFromBundle(intent.getExtras(), tag);
        }
        logWarn(TAG, "Got null intent!");
        return 6;
    }

    public static int getResponseCodeFromBundle(Bundle bundle, String tag) {
        if (bundle == null) {
            logWarn(tag, "Unexpected null bundle received!");
            return 6;
        }
        Object responseCode = bundle.get(RESPONSE_CODE);
        if (responseCode == null) {
            logVerbose(tag, "getResponseCodeFromBundle() got null response code, assuming OK");
            return 0;
        } else if (responseCode instanceof Integer) {
            return ((Integer) responseCode).intValue();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected type for bundle response code: ");
            sb.append(responseCode.getClass().getName());
            logWarn(tag, sb.toString());
            return 6;
        }
    }

    public static List<Purchase> extractPurchases(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        List<String> purchaseDataList = bundle.getStringArrayList(RESPONSE_INAPP_PURCHASE_DATA_LIST);
        List<String> dataSignatureList = bundle.getStringArrayList(RESPONSE_INAPP_SIGNATURE_LIST);
        List<Purchase> resultList = new ArrayList<>();
        if (purchaseDataList == null || dataSignatureList == null) {
            logWarn(TAG, "Couldn't find purchase lists, trying to find single data.");
            Purchase tmpPurchase = extractPurchase(bundle.getString(RESPONSE_INAPP_PURCHASE_DATA), bundle.getString(RESPONSE_INAPP_SIGNATURE));
            if (tmpPurchase == null) {
                logWarn(TAG, "Couldn't find single purchase data as well.");
                return null;
            }
            resultList.add(tmpPurchase);
        } else {
            int i = 0;
            while (i < purchaseDataList.size() && i < dataSignatureList.size()) {
                Purchase tmpPurchase2 = extractPurchase((String) purchaseDataList.get(i), (String) dataSignatureList.get(i));
                if (tmpPurchase2 != null) {
                    resultList.add(tmpPurchase2);
                }
                i++;
            }
        }
        return resultList;
    }

    private static Purchase extractPurchase(String purchaseData, String signatureData) {
        Purchase purchase = null;
        if (purchaseData == null || signatureData == null) {
            logWarn(TAG, "Received a bad purchase data.");
            return null;
        }
        try {
            purchase = new Purchase(purchaseData, signatureData);
        } catch (JSONException e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Got JSONException while parsing purchase data: ");
            sb.append(e);
            logWarn(str, sb.toString());
        }
        return purchase;
    }
}
