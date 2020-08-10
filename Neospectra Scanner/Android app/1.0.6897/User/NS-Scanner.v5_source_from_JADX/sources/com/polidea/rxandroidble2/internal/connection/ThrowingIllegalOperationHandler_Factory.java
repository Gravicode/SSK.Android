package com.polidea.rxandroidble2.internal.connection;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;

public final class ThrowingIllegalOperationHandler_Factory implements Factory<ThrowingIllegalOperationHandler> {
    private final Provider<IllegalOperationMessageCreator> messageCreatorProvider;

    public ThrowingIllegalOperationHandler_Factory(Provider<IllegalOperationMessageCreator> messageCreatorProvider2) {
        this.messageCreatorProvider = messageCreatorProvider2;
    }

    public ThrowingIllegalOperationHandler get() {
        return new ThrowingIllegalOperationHandler((IllegalOperationMessageCreator) this.messageCreatorProvider.get());
    }

    public static ThrowingIllegalOperationHandler_Factory create(Provider<IllegalOperationMessageCreator> messageCreatorProvider2) {
        return new ThrowingIllegalOperationHandler_Factory(messageCreatorProvider2);
    }
}
