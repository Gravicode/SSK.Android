package com.polidea.rxandroidble2;

import bleshadow.dagger.Lazy;
import bleshadow.dagger.internal.DoubleCheck;
import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.ClientComponent.ClientComponentFinalizer;
import com.polidea.rxandroidble2.RxBleAdapterStateObservable.BleAdapterState;
import com.polidea.rxandroidble2.internal.RxBleDeviceProvider;
import com.polidea.rxandroidble2.internal.scan.RxBleInternalScanResult;
import com.polidea.rxandroidble2.internal.scan.ScanPreconditionsVerifier;
import com.polidea.rxandroidble2.internal.scan.ScanSetupBuilder;
import com.polidea.rxandroidble2.internal.serialization.ClientOperationQueue;
import com.polidea.rxandroidble2.internal.util.ClientStateObservable;
import com.polidea.rxandroidble2.internal.util.LocationServicesStatus;
import com.polidea.rxandroidble2.internal.util.RxBleAdapterWrapper;
import com.polidea.rxandroidble2.internal.util.UUIDUtil;
import com.polidea.rxandroidble2.scan.ScanResult;
import p005io.reactivex.Observable;
import p005io.reactivex.Scheduler;
import p005io.reactivex.functions.Function;

public final class RxBleClientImpl_Factory implements Factory<RxBleClientImpl> {
    private final Provider<Observable<BleAdapterState>> adapterStateObservableProvider;
    private final Provider<Scheduler> bluetoothInteractionSchedulerProvider;
    private final Provider<ClientComponentFinalizer> clientComponentFinalizerProvider;
    private final Provider<ClientStateObservable> clientStateObservableProvider;
    private final Provider<Function<RxBleInternalScanResult, ScanResult>> internalToExternalScanResultMapFunctionProvider;
    private final Provider<LocationServicesStatus> locationServicesStatusProvider;
    private final Provider<ClientOperationQueue> operationQueueProvider;
    private final Provider<RxBleAdapterWrapper> rxBleAdapterWrapperProvider;
    private final Provider<RxBleDeviceProvider> rxBleDeviceProvider;
    private final Provider<ScanPreconditionsVerifier> scanPreconditionVerifierProvider;
    private final Provider<ScanSetupBuilder> scanSetupBuilderProvider;
    private final Provider<UUIDUtil> uuidUtilProvider;

    public RxBleClientImpl_Factory(Provider<RxBleAdapterWrapper> rxBleAdapterWrapperProvider2, Provider<ClientOperationQueue> operationQueueProvider2, Provider<Observable<BleAdapterState>> adapterStateObservableProvider2, Provider<UUIDUtil> uuidUtilProvider2, Provider<LocationServicesStatus> locationServicesStatusProvider2, Provider<ClientStateObservable> clientStateObservableProvider2, Provider<RxBleDeviceProvider> rxBleDeviceProvider2, Provider<ScanSetupBuilder> scanSetupBuilderProvider2, Provider<ScanPreconditionsVerifier> scanPreconditionVerifierProvider2, Provider<Function<RxBleInternalScanResult, ScanResult>> internalToExternalScanResultMapFunctionProvider2, Provider<Scheduler> bluetoothInteractionSchedulerProvider2, Provider<ClientComponentFinalizer> clientComponentFinalizerProvider2) {
        this.rxBleAdapterWrapperProvider = rxBleAdapterWrapperProvider2;
        this.operationQueueProvider = operationQueueProvider2;
        this.adapterStateObservableProvider = adapterStateObservableProvider2;
        this.uuidUtilProvider = uuidUtilProvider2;
        this.locationServicesStatusProvider = locationServicesStatusProvider2;
        this.clientStateObservableProvider = clientStateObservableProvider2;
        this.rxBleDeviceProvider = rxBleDeviceProvider2;
        this.scanSetupBuilderProvider = scanSetupBuilderProvider2;
        this.scanPreconditionVerifierProvider = scanPreconditionVerifierProvider2;
        this.internalToExternalScanResultMapFunctionProvider = internalToExternalScanResultMapFunctionProvider2;
        this.bluetoothInteractionSchedulerProvider = bluetoothInteractionSchedulerProvider2;
        this.clientComponentFinalizerProvider = clientComponentFinalizerProvider2;
    }

    public RxBleClientImpl get() {
        RxBleClientImpl rxBleClientImpl = new RxBleClientImpl((RxBleAdapterWrapper) this.rxBleAdapterWrapperProvider.get(), (ClientOperationQueue) this.operationQueueProvider.get(), (Observable) this.adapterStateObservableProvider.get(), (UUIDUtil) this.uuidUtilProvider.get(), (LocationServicesStatus) this.locationServicesStatusProvider.get(), DoubleCheck.lazy(this.clientStateObservableProvider), (RxBleDeviceProvider) this.rxBleDeviceProvider.get(), (ScanSetupBuilder) this.scanSetupBuilderProvider.get(), (ScanPreconditionsVerifier) this.scanPreconditionVerifierProvider.get(), (Function) this.internalToExternalScanResultMapFunctionProvider.get(), (Scheduler) this.bluetoothInteractionSchedulerProvider.get(), (ClientComponentFinalizer) this.clientComponentFinalizerProvider.get());
        return rxBleClientImpl;
    }

    public static RxBleClientImpl_Factory create(Provider<RxBleAdapterWrapper> rxBleAdapterWrapperProvider2, Provider<ClientOperationQueue> operationQueueProvider2, Provider<Observable<BleAdapterState>> adapterStateObservableProvider2, Provider<UUIDUtil> uuidUtilProvider2, Provider<LocationServicesStatus> locationServicesStatusProvider2, Provider<ClientStateObservable> clientStateObservableProvider2, Provider<RxBleDeviceProvider> rxBleDeviceProvider2, Provider<ScanSetupBuilder> scanSetupBuilderProvider2, Provider<ScanPreconditionsVerifier> scanPreconditionVerifierProvider2, Provider<Function<RxBleInternalScanResult, ScanResult>> internalToExternalScanResultMapFunctionProvider2, Provider<Scheduler> bluetoothInteractionSchedulerProvider2, Provider<ClientComponentFinalizer> clientComponentFinalizerProvider2) {
        RxBleClientImpl_Factory rxBleClientImpl_Factory = new RxBleClientImpl_Factory(rxBleAdapterWrapperProvider2, operationQueueProvider2, adapterStateObservableProvider2, uuidUtilProvider2, locationServicesStatusProvider2, clientStateObservableProvider2, rxBleDeviceProvider2, scanSetupBuilderProvider2, scanPreconditionVerifierProvider2, internalToExternalScanResultMapFunctionProvider2, bluetoothInteractionSchedulerProvider2, clientComponentFinalizerProvider2);
        return rxBleClientImpl_Factory;
    }

    public static RxBleClientImpl newRxBleClientImpl(RxBleAdapterWrapper rxBleAdapterWrapper, ClientOperationQueue operationQueue, Observable<BleAdapterState> adapterStateObservable, UUIDUtil uuidUtil, LocationServicesStatus locationServicesStatus, Lazy<ClientStateObservable> lazyClientStateObservable, RxBleDeviceProvider rxBleDeviceProvider2, ScanSetupBuilder scanSetupBuilder, ScanPreconditionsVerifier scanPreconditionVerifier, Function<RxBleInternalScanResult, ScanResult> internalToExternalScanResultMapFunction, Scheduler bluetoothInteractionScheduler, ClientComponentFinalizer clientComponentFinalizer) {
        RxBleClientImpl rxBleClientImpl = new RxBleClientImpl(rxBleAdapterWrapper, operationQueue, adapterStateObservable, uuidUtil, locationServicesStatus, lazyClientStateObservable, rxBleDeviceProvider2, scanSetupBuilder, scanPreconditionVerifier, internalToExternalScanResultMapFunction, bluetoothInteractionScheduler, clientComponentFinalizer);
        return rxBleClientImpl;
    }
}
