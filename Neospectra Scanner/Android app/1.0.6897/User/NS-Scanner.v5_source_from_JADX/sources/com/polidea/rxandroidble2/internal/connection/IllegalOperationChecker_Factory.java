package com.polidea.rxandroidble2.internal.connection;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;

public final class IllegalOperationChecker_Factory implements Factory<IllegalOperationChecker> {
    private final Provider<IllegalOperationHandler> resultHandlerProvider;

    public IllegalOperationChecker_Factory(Provider<IllegalOperationHandler> resultHandlerProvider2) {
        this.resultHandlerProvider = resultHandlerProvider2;
    }

    public IllegalOperationChecker get() {
        return new IllegalOperationChecker((IllegalOperationHandler) this.resultHandlerProvider.get());
    }

    public static IllegalOperationChecker_Factory create(Provider<IllegalOperationHandler> resultHandlerProvider2) {
        return new IllegalOperationChecker_Factory(resultHandlerProvider2);
    }
}
