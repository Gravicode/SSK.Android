package com.polidea.rxandroidble2.internal.connection;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import bleshadow.javax.inject.Provider;

public final class ConnectionModule_ProvideIllegalOperationHandlerFactory implements Factory<IllegalOperationHandler> {
    private final Provider<LoggingIllegalOperationHandler> loggingIllegalOperationHandlerProvider;
    private final ConnectionModule module;
    private final Provider<ThrowingIllegalOperationHandler> throwingIllegalOperationHandlerProvider;

    public ConnectionModule_ProvideIllegalOperationHandlerFactory(ConnectionModule module2, Provider<LoggingIllegalOperationHandler> loggingIllegalOperationHandlerProvider2, Provider<ThrowingIllegalOperationHandler> throwingIllegalOperationHandlerProvider2) {
        this.module = module2;
        this.loggingIllegalOperationHandlerProvider = loggingIllegalOperationHandlerProvider2;
        this.throwingIllegalOperationHandlerProvider = throwingIllegalOperationHandlerProvider2;
    }

    public IllegalOperationHandler get() {
        return (IllegalOperationHandler) Preconditions.checkNotNull(this.module.provideIllegalOperationHandler(this.loggingIllegalOperationHandlerProvider, this.throwingIllegalOperationHandlerProvider), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static ConnectionModule_ProvideIllegalOperationHandlerFactory create(ConnectionModule module2, Provider<LoggingIllegalOperationHandler> loggingIllegalOperationHandlerProvider2, Provider<ThrowingIllegalOperationHandler> throwingIllegalOperationHandlerProvider2) {
        return new ConnectionModule_ProvideIllegalOperationHandlerFactory(module2, loggingIllegalOperationHandlerProvider2, throwingIllegalOperationHandlerProvider2);
    }

    public static IllegalOperationHandler proxyProvideIllegalOperationHandler(ConnectionModule instance, Provider<LoggingIllegalOperationHandler> loggingIllegalOperationHandlerProvider2, Provider<ThrowingIllegalOperationHandler> throwingIllegalOperationHandlerProvider2) {
        return (IllegalOperationHandler) Preconditions.checkNotNull(instance.provideIllegalOperationHandler(loggingIllegalOperationHandlerProvider2, throwingIllegalOperationHandlerProvider2), "Cannot return null from a non-@Nullable @Provides method");
    }
}
