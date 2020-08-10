package com.android.billingclient.api;

import android.text.TextUtils;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class Purchase {
    private final String mOriginalJson;
    private final JSONObject mParsedJson = new JSONObject(this.mOriginalJson);
    private final String mSignature;

    public static class PurchasesResult {
        private List<Purchase> mPurchaseList;
        private int mResponseCode;

        PurchasesResult(int responseCode, List<Purchase> purchasesList) {
            this.mPurchaseList = purchasesList;
            this.mResponseCode = responseCode;
        }

        public int getResponseCode() {
            return this.mResponseCode;
        }

        public List<Purchase> getPurchasesList() {
            return this.mPurchaseList;
        }
    }

    public Purchase(String jsonPurchaseInfo, String signature) throws JSONException {
        this.mOriginalJson = jsonPurchaseInfo;
        this.mSignature = signature;
    }

    public String getOrderId() {
        return this.mParsedJson.optString("orderId");
    }

    public String getPackageName() {
        return this.mParsedJson.optString("packageName");
    }

    public String getSku() {
        return this.mParsedJson.optString("productId");
    }

    public long getPurchaseTime() {
        return this.mParsedJson.optLong("purchaseTime");
    }

    public String getPurchaseToken() {
        return this.mParsedJson.optString("token", this.mParsedJson.optString("purchaseToken"));
    }

    public boolean isAutoRenewing() {
        return this.mParsedJson.optBoolean("autoRenewing");
    }

    public String getOriginalJson() {
        return this.mOriginalJson;
    }

    public String getSignature() {
        return this.mSignature;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Purchase. Json: ");
        sb.append(this.mOriginalJson);
        return sb.toString();
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof Purchase)) {
            return false;
        }
        Purchase purchase = (Purchase) o;
        if (!TextUtils.equals(this.mOriginalJson, purchase.getOriginalJson()) || !TextUtils.equals(this.mSignature, purchase.getSignature())) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return this.mOriginalJson.hashCode();
    }
}
