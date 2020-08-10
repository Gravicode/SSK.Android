package com.polidea.rxandroidble2.internal;

import android.bluetooth.BluetoothDevice;
import bleshadow.dagger.Module;
import bleshadow.dagger.Provides;
import bleshadow.javax.inject.Named;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.polidea.rxandroidble2.RxBleConnection.RxBleConnectionState;
import com.polidea.rxandroidble2.internal.connection.ConnectionComponent;
import com.polidea.rxandroidble2.internal.connection.ConnectionStateChangeListener;
import com.polidea.rxandroidble2.internal.operations.TimeoutConfiguration;
import com.polidea.rxandroidble2.internal.util.RxBleAdapterWrapper;
import java.util.concurrent.TimeUnit;
import p005io.reactivex.Scheduler;

@Module(subcomponents = {ConnectionComponent.class})
public class DeviceModule {
    public static final String CONNECT_TIMEOUT = "connect-timeout";
    private static final int DEFAULT_CONNECT_TIMEOUT = 35;
    private static final int DEFAULT_DISCONNECT_TIMEOUT = 10;
    private static final int DEFAULT_OPERATION_TIMEOUT = 30;
    public static final String DISCONNECT_TIMEOUT = "disconnect-timeout";
    public static final String MAC_ADDRESS = "mac-address";
    public static final String OPERATION_TIMEOUT = "operation-timeout";
    final String macAddress;

    DeviceModule(String macAddress2) {
        this.macAddress = macAddress2;
    }

    /* access modifiers changed from: 0000 */
    @Provides
    public BluetoothDevice provideBluetoothDevice(RxBleAdapterWrapper adapterWrapper) {
        return adapterWrapper.getRemoteDevice(this.macAddress);
    }

    /* access modifiers changed from: 0000 */
    @Provides
    @Named("mac-address")
    public String provideMacAddress() {
        return this.macAddress;
    }

    @Provides
    @Named("connect-timeout")
    static TimeoutConfiguration providesConnectTimeoutConf(@Named("timeout") Scheduler timeoutScheduler) {
        return new TimeoutConfiguration(35, TimeUnit.SECONDS, timeoutScheduler);
    }

    @Provides
    @Named("disconnect-timeout")
    static TimeoutConfiguration providesDisconnectTimeoutConf(@Named("timeout") Scheduler timeoutScheduler) {
        return new TimeoutConfiguration(10, TimeUnit.SECONDS, timeoutScheduler);
    }

    @Provides
    @DeviceScope
    static BehaviorRelay<RxBleConnectionState> provideConnectionStateRelay() {
        return BehaviorRelay.createDefault(RxBleConnectionState.DISCONNECTED);
    }

    @Provides
    @DeviceScope
    static ConnectionStateChangeListener provideConnectionStateChangeListener(final BehaviorRelay<RxBleConnectionState> connectionStateBehaviorRelay) {
        return new ConnectionStateChangeListener() {
            public void onConnectionStateChange(RxBleConnectionState rxBleConnectionState) {
                connectionStateBehaviorRelay.accept(rxBleConnectionState);
            }
        };
    }
}
