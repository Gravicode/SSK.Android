package com.polidea.rxandroidble2.internal.connection;

import bleshadow.dagger.Module;
import bleshadow.dagger.Provides;
import bleshadow.javax.inject.Named;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.ConnectionSetup;
import com.polidea.rxandroidble2.Timeout;
import com.polidea.rxandroidble2.internal.operations.TimeoutConfiguration;
import com.polidea.rxandroidble2.internal.util.CharacteristicPropertiesParser;
import p005io.reactivex.Scheduler;

@Module
public class ConnectionModule {
    public static final String OPERATION_TIMEOUT = "operation-timeout";
    final boolean autoConnect;
    private final Timeout operationTimeout;
    final boolean suppressOperationCheck;

    ConnectionModule(ConnectionSetup connectionSetup) {
        this.autoConnect = connectionSetup.autoConnect;
        this.suppressOperationCheck = connectionSetup.suppressOperationCheck;
        this.operationTimeout = connectionSetup.operationTimeout;
    }

    /* access modifiers changed from: 0000 */
    @ConnectionScope
    @Provides
    @Named("autoConnect")
    public boolean provideAutoConnect() {
        return this.autoConnect;
    }

    /* access modifiers changed from: 0000 */
    @Provides
    @Named("operation-timeout")
    public TimeoutConfiguration providesOperationTimeoutConf(@Named("timeout") Scheduler timeoutScheduler) {
        return new TimeoutConfiguration(this.operationTimeout.timeout, this.operationTimeout.timeUnit, timeoutScheduler);
    }

    /* access modifiers changed from: 0000 */
    @Provides
    public IllegalOperationHandler provideIllegalOperationHandler(Provider<LoggingIllegalOperationHandler> loggingIllegalOperationHandlerProvider, Provider<ThrowingIllegalOperationHandler> throwingIllegalOperationHandlerProvider) {
        if (this.suppressOperationCheck) {
            return (IllegalOperationHandler) loggingIllegalOperationHandlerProvider.get();
        }
        return (IllegalOperationHandler) throwingIllegalOperationHandlerProvider.get();
    }

    /* access modifiers changed from: 0000 */
    @Provides
    public CharacteristicPropertiesParser provideCharacteristicPropertiesParser() {
        CharacteristicPropertiesParser characteristicPropertiesParser = new CharacteristicPropertiesParser(1, 2, 4, 8, 16, 32, 64);
        return characteristicPropertiesParser;
    }
}
