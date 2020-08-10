package com.polidea.rxandroidble2;

import java.util.concurrent.TimeUnit;

public class ConnectionSetup {
    public static final int DEFAULT_OPERATION_TIMEOUT = 30;
    public final boolean autoConnect;
    public final Timeout operationTimeout;
    public final boolean suppressOperationCheck;

    public static class Builder {
        private boolean autoConnect = false;
        private Timeout operationTimeout = new Timeout(30, TimeUnit.SECONDS);
        private boolean suppressOperationCheck = false;

        public Builder setAutoConnect(boolean autoConnect2) {
            this.autoConnect = autoConnect2;
            return this;
        }

        public Builder setSuppressIllegalOperationCheck(boolean suppressOperationCheck2) {
            this.suppressOperationCheck = suppressOperationCheck2;
            return this;
        }

        public Builder setOperationTimeout(Timeout operationTimeout2) {
            this.operationTimeout = operationTimeout2;
            return this;
        }

        public ConnectionSetup build() {
            return new ConnectionSetup(this.autoConnect, this.suppressOperationCheck, this.operationTimeout);
        }
    }

    private ConnectionSetup(boolean autoConnect2, boolean suppressOperationCheck2, Timeout operationTimeout2) {
        this.autoConnect = autoConnect2;
        this.suppressOperationCheck = suppressOperationCheck2;
        this.operationTimeout = operationTimeout2;
    }
}
