package com.polidea.rxandroidble2.internal.util;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.RxBleAdapterStateObservable.BleAdapterState;
import p005io.reactivex.Observable;
import p005io.reactivex.Scheduler;

public final class ClientStateObservable_Factory implements Factory<ClientStateObservable> {
    private final Provider<Observable<BleAdapterState>> bleAdapterStateObservableProvider;
    private final Provider<Observable<Boolean>> locationServicesOkObservableProvider;
    private final Provider<LocationServicesStatus> locationServicesStatusProvider;
    private final Provider<RxBleAdapterWrapper> rxBleAdapterWrapperProvider;
    private final Provider<Scheduler> timerSchedulerProvider;

    public ClientStateObservable_Factory(Provider<RxBleAdapterWrapper> rxBleAdapterWrapperProvider2, Provider<Observable<BleAdapterState>> bleAdapterStateObservableProvider2, Provider<Observable<Boolean>> locationServicesOkObservableProvider2, Provider<LocationServicesStatus> locationServicesStatusProvider2, Provider<Scheduler> timerSchedulerProvider2) {
        this.rxBleAdapterWrapperProvider = rxBleAdapterWrapperProvider2;
        this.bleAdapterStateObservableProvider = bleAdapterStateObservableProvider2;
        this.locationServicesOkObservableProvider = locationServicesOkObservableProvider2;
        this.locationServicesStatusProvider = locationServicesStatusProvider2;
        this.timerSchedulerProvider = timerSchedulerProvider2;
    }

    public ClientStateObservable get() {
        ClientStateObservable clientStateObservable = new ClientStateObservable((RxBleAdapterWrapper) this.rxBleAdapterWrapperProvider.get(), (Observable) this.bleAdapterStateObservableProvider.get(), (Observable) this.locationServicesOkObservableProvider.get(), (LocationServicesStatus) this.locationServicesStatusProvider.get(), (Scheduler) this.timerSchedulerProvider.get());
        return clientStateObservable;
    }

    public static ClientStateObservable_Factory create(Provider<RxBleAdapterWrapper> rxBleAdapterWrapperProvider2, Provider<Observable<BleAdapterState>> bleAdapterStateObservableProvider2, Provider<Observable<Boolean>> locationServicesOkObservableProvider2, Provider<LocationServicesStatus> locationServicesStatusProvider2, Provider<Scheduler> timerSchedulerProvider2) {
        ClientStateObservable_Factory clientStateObservable_Factory = new ClientStateObservable_Factory(rxBleAdapterWrapperProvider2, bleAdapterStateObservableProvider2, locationServicesOkObservableProvider2, locationServicesStatusProvider2, timerSchedulerProvider2);
        return clientStateObservable_Factory;
    }

    public static ClientStateObservable newClientStateObservable(RxBleAdapterWrapper rxBleAdapterWrapper, Observable<BleAdapterState> bleAdapterStateObservable, Observable<Boolean> locationServicesOkObservable, LocationServicesStatus locationServicesStatus, Scheduler timerScheduler) {
        ClientStateObservable clientStateObservable = new ClientStateObservable(rxBleAdapterWrapper, bleAdapterStateObservable, locationServicesOkObservable, locationServicesStatus, timerScheduler);
        return clientStateObservable;
    }
}
