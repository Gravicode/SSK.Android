package com.polidea.rxandroidble2.internal.connection;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.RxBleAdapterStateObservable.BleAdapterState;
import com.polidea.rxandroidble2.internal.util.RxBleAdapterWrapper;
import p005io.reactivex.Observable;

public final class DisconnectionRouter_Factory implements Factory<DisconnectionRouter> {
    private final Provider<Observable<BleAdapterState>> adapterStateObservableProvider;
    private final Provider<RxBleAdapterWrapper> adapterWrapperProvider;
    private final Provider<String> macAddressProvider;

    public DisconnectionRouter_Factory(Provider<String> macAddressProvider2, Provider<RxBleAdapterWrapper> adapterWrapperProvider2, Provider<Observable<BleAdapterState>> adapterStateObservableProvider2) {
        this.macAddressProvider = macAddressProvider2;
        this.adapterWrapperProvider = adapterWrapperProvider2;
        this.adapterStateObservableProvider = adapterStateObservableProvider2;
    }

    public DisconnectionRouter get() {
        return new DisconnectionRouter((String) this.macAddressProvider.get(), (RxBleAdapterWrapper) this.adapterWrapperProvider.get(), (Observable) this.adapterStateObservableProvider.get());
    }

    public static DisconnectionRouter_Factory create(Provider<String> macAddressProvider2, Provider<RxBleAdapterWrapper> adapterWrapperProvider2, Provider<Observable<BleAdapterState>> adapterStateObservableProvider2) {
        return new DisconnectionRouter_Factory(macAddressProvider2, adapterWrapperProvider2, adapterStateObservableProvider2);
    }

    public static DisconnectionRouter newDisconnectionRouter(String macAddress, RxBleAdapterWrapper adapterWrapper, Observable<BleAdapterState> adapterStateObservable) {
        return new DisconnectionRouter(macAddress, adapterWrapper, adapterStateObservable);
    }
}
