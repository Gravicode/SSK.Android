package com.polidea.rxandroidble2.internal.connection;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;

public final class LoggingIllegalOperationHandler_Factory implements Factory<LoggingIllegalOperationHandler> {
    private final Provider<IllegalOperationMessageCreator> messageCreatorProvider;

    public LoggingIllegalOperationHandler_Factory(Provider<IllegalOperationMessageCreator> messageCreatorProvider2) {
        this.messageCreatorProvider = messageCreatorProvider2;
    }

    public LoggingIllegalOperationHandler get() {
        return new LoggingIllegalOperationHandler((IllegalOperationMessageCreator) this.messageCreatorProvider.get());
    }

    public static LoggingIllegalOperationHandler_Factory create(Provider<IllegalOperationMessageCreator> messageCreatorProvider2) {
        return new LoggingIllegalOperationHandler_Factory(messageCreatorProvider2);
    }
}
