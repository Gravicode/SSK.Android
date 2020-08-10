package com.android.billingclient.api;

import android.text.TextUtils;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class SkuDetails {
    private final String mOriginalJson;
    private final JSONObject mParsedJson = new JSONObject(this.mOriginalJson);

    static class SkuDetailsResult {
        private int mResponseCode;
        private List<SkuDetails> mSkuDetailsList;

        SkuDetailsResult(int responseCode, List<SkuDetails> skuDetailsList) {
            this.mSkuDetailsList = skuDetailsList;
            this.mResponseCode = responseCode;
        }

        /* access modifiers changed from: 0000 */
        public List<SkuDetails> getSkuDetailsList() {
            return this.mSkuDetailsList;
        }

        /* access modifiers changed from: 0000 */
        public int getResponseCode() {
            return this.mResponseCode;
        }
    }

    public SkuDetails(String jsonSkuDetails) throws JSONException {
        this.mOriginalJson = jsonSkuDetails;
    }

    public String getSku() {
        return this.mParsedJson.optString("productId");
    }

    public String getType() {
        return this.mParsedJson.optString("type");
    }

    public String getPrice() {
        return this.mParsedJson.optString(Param.PRICE);
    }

    public long getPriceAmountMicros() {
        return this.mParsedJson.optLong("price_amount_micros");
    }

    public String getPriceCurrencyCode() {
        return this.mParsedJson.optString("price_currency_code");
    }

    public String getTitle() {
        return this.mParsedJson.optString("title");
    }

    public String getDescription() {
        return this.mParsedJson.optString("description");
    }

    public String getSubscriptionPeriod() {
        return this.mParsedJson.optString("subscriptionPeriod");
    }

    public String getFreeTrialPeriod() {
        return this.mParsedJson.optString("freeTrialPeriod");
    }

    public String getIntroductoryPrice() {
        return this.mParsedJson.optString("introductoryPrice");
    }

    public String getIntroductoryPriceAmountMicros() {
        return this.mParsedJson.optString("introductoryPriceAmountMicros");
    }

    public String getIntroductoryPricePeriod() {
        return this.mParsedJson.optString("introductoryPricePeriod");
    }

    public String getIntroductoryPriceCycles() {
        return this.mParsedJson.optString("introductoryPriceCycles");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SkuDetails: ");
        sb.append(this.mOriginalJson);
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return TextUtils.equals(this.mOriginalJson, ((SkuDetails) o).mOriginalJson);
    }

    public int hashCode() {
        return this.mOriginalJson.hashCode();
    }
}
