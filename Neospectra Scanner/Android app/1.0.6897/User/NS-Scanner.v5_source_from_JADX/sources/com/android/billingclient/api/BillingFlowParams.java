package com.android.billingclient.api;

import java.util.ArrayList;

public class BillingFlowParams {
    /* access modifiers changed from: private */
    public String mAccountId;
    /* access modifiers changed from: private */
    public boolean mNotReplaceSkusProration;
    /* access modifiers changed from: private */
    public ArrayList<String> mOldSkus;
    /* access modifiers changed from: private */
    public String mSku;
    /* access modifiers changed from: private */
    public String mSkuType;
    /* access modifiers changed from: private */
    public boolean mVrPurchaseFlow;

    public static class Builder {
        private BillingFlowParams mParams;

        private Builder() {
            this.mParams = new BillingFlowParams();
        }

        public Builder setSku(String sku) {
            this.mParams.mSku = sku;
            return this;
        }

        public Builder setType(String type) {
            this.mParams.mSkuType = type;
            return this;
        }

        public Builder setOldSkus(ArrayList<String> oldSkus) {
            this.mParams.mOldSkus = oldSkus;
            return this;
        }

        public Builder addOldSku(String oldSku) {
            if (this.mParams.mOldSkus == null) {
                this.mParams.mOldSkus = new ArrayList();
            }
            this.mParams.mOldSkus.add(oldSku);
            return this;
        }

        public Builder setReplaceSkusProration(boolean bReplaceSkusProration) {
            this.mParams.mNotReplaceSkusProration = !bReplaceSkusProration;
            return this;
        }

        public Builder setAccountId(String accountId) {
            this.mParams.mAccountId = accountId;
            return this;
        }

        public Builder setVrPurchaseFlow(boolean isVrPurchaseFlow) {
            this.mParams.mVrPurchaseFlow = isVrPurchaseFlow;
            return this;
        }

        public BillingFlowParams build() {
            return this.mParams;
        }
    }

    public String getSku() {
        return this.mSku;
    }

    public String getSkuType() {
        return this.mSkuType;
    }

    public ArrayList<String> getOldSkus() {
        return this.mOldSkus;
    }

    public boolean getReplaceSkusProration() {
        return !this.mNotReplaceSkusProration;
    }

    public String getAccountId() {
        return this.mAccountId;
    }

    public boolean getVrPurchaseFlow() {
        return this.mVrPurchaseFlow;
    }

    public boolean hasExtraParams() {
        return this.mNotReplaceSkusProration || this.mAccountId != null || this.mVrPurchaseFlow;
    }

    public static Builder newBuilder() {
        return new Builder();
    }
}
