package com.android.billingclient.api;

import java.util.List;

public class SkuDetailsParams {
    /* access modifiers changed from: private */
    public String mSkuType;
    /* access modifiers changed from: private */
    public List<String> mSkusList;

    public static class Builder {
        private SkuDetailsParams mParams;

        private Builder() {
            this.mParams = new SkuDetailsParams();
        }

        public Builder setSkusList(List<String> skusList) {
            this.mParams.mSkusList = skusList;
            return this;
        }

        public Builder setType(String type) {
            this.mParams.mSkuType = type;
            return this;
        }

        public SkuDetailsParams build() {
            return this.mParams;
        }
    }

    public String getSkuType() {
        return this.mSkuType;
    }

    public List<String> getSkusList() {
        return this.mSkusList;
    }

    public static Builder newBuilder() {
        return new Builder();
    }
}
