package com.polidea.rxandroidble2;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import bleshadow.dagger.Lazy;
import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.ClientComponent.ClientComponentFinalizer;
import com.polidea.rxandroidble2.RxBleAdapterStateObservable.BleAdapterState;
import com.polidea.rxandroidble2.RxBleClient.State;
import com.polidea.rxandroidble2.exceptions.BleScanException;
import com.polidea.rxandroidble2.internal.RxBleDeviceProvider;
import com.polidea.rxandroidble2.internal.operations.LegacyScanOperation;
import com.polidea.rxandroidble2.internal.scan.RxBleInternalScanResult;
import com.polidea.rxandroidble2.internal.scan.RxBleInternalScanResultLegacy;
import com.polidea.rxandroidble2.internal.scan.ScanPreconditionsVerifier;
import com.polidea.rxandroidble2.internal.scan.ScanSetup;
import com.polidea.rxandroidble2.internal.scan.ScanSetupBuilder;
import com.polidea.rxandroidble2.internal.serialization.ClientOperationQueue;
import com.polidea.rxandroidble2.internal.util.ClientStateObservable;
import com.polidea.rxandroidble2.internal.util.LocationServicesStatus;
import com.polidea.rxandroidble2.internal.util.RxBleAdapterWrapper;
import com.polidea.rxandroidble2.internal.util.UUIDUtil;
import com.polidea.rxandroidble2.scan.ScanFilter;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Scheduler;
import p005io.reactivex.functions.Action;
import p005io.reactivex.functions.Function;
import p005io.reactivex.functions.Predicate;

class RxBleClientImpl extends RxBleClient {
    /* access modifiers changed from: private */
    public final Scheduler bluetoothInteractionScheduler;
    private final ClientComponentFinalizer clientComponentFinalizer;
    /* access modifiers changed from: private */
    public final Function<RxBleInternalScanResult, ScanResult> internalToExternalScanResultMapFunction;
    private final Lazy<ClientStateObservable> lazyClientStateObservable;
    private final LocationServicesStatus locationServicesStatus;
    /* access modifiers changed from: private */
    public final ClientOperationQueue operationQueue;
    /* access modifiers changed from: private */
    public final Map<Set<UUID>, Observable<RxBleScanResult>> queuedScanOperations = new HashMap();
    private final Observable<BleAdapterState> rxBleAdapterStateObservable;
    private final RxBleAdapterWrapper rxBleAdapterWrapper;
    private final RxBleDeviceProvider rxBleDeviceProvider;
    /* access modifiers changed from: private */
    public final ScanPreconditionsVerifier scanPreconditionVerifier;
    /* access modifiers changed from: private */
    public final ScanSetupBuilder scanSetupBuilder;
    private final UUIDUtil uuidUtil;

    @Inject
    RxBleClientImpl(RxBleAdapterWrapper rxBleAdapterWrapper2, ClientOperationQueue operationQueue2, Observable<BleAdapterState> adapterStateObservable, UUIDUtil uuidUtil2, LocationServicesStatus locationServicesStatus2, Lazy<ClientStateObservable> lazyClientStateObservable2, RxBleDeviceProvider rxBleDeviceProvider2, ScanSetupBuilder scanSetupBuilder2, ScanPreconditionsVerifier scanPreconditionVerifier2, Function<RxBleInternalScanResult, ScanResult> internalToExternalScanResultMapFunction2, @Named("bluetooth_interaction") Scheduler bluetoothInteractionScheduler2, ClientComponentFinalizer clientComponentFinalizer2) {
        this.uuidUtil = uuidUtil2;
        this.operationQueue = operationQueue2;
        this.rxBleAdapterWrapper = rxBleAdapterWrapper2;
        this.rxBleAdapterStateObservable = adapterStateObservable;
        this.locationServicesStatus = locationServicesStatus2;
        this.lazyClientStateObservable = lazyClientStateObservable2;
        this.rxBleDeviceProvider = rxBleDeviceProvider2;
        this.scanSetupBuilder = scanSetupBuilder2;
        this.scanPreconditionVerifier = scanPreconditionVerifier2;
        this.internalToExternalScanResultMapFunction = internalToExternalScanResultMapFunction2;
        this.bluetoothInteractionScheduler = bluetoothInteractionScheduler2;
        this.clientComponentFinalizer = clientComponentFinalizer2;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        this.clientComponentFinalizer.onFinalize();
        super.finalize();
    }

    public RxBleDevice getBleDevice(@NonNull String macAddress) {
        guardBluetoothAdapterAvailable();
        return this.rxBleDeviceProvider.getBleDevice(macAddress);
    }

    public Set<RxBleDevice> getBondedDevices() {
        guardBluetoothAdapterAvailable();
        Set<RxBleDevice> rxBleDevices = new HashSet<>();
        for (BluetoothDevice bluetoothDevice : this.rxBleAdapterWrapper.getBondedDevices()) {
            rxBleDevices.add(getBleDevice(bluetoothDevice.getAddress()));
        }
        return rxBleDevices;
    }

    public Observable<ScanResult> scanBleDevices(final ScanSettings scanSettings, final ScanFilter... scanFilters) {
        return Observable.defer(new Callable<ObservableSource<? extends ScanResult>>() {
            public Observable<ScanResult> call() {
                RxBleClientImpl.this.scanPreconditionVerifier.verify();
                ScanSetup scanSetup = RxBleClientImpl.this.scanSetupBuilder.build(scanSettings, scanFilters);
                return RxBleClientImpl.this.operationQueue.queue(scanSetup.scanOperation).unsubscribeOn(RxBleClientImpl.this.bluetoothInteractionScheduler).compose(scanSetup.scanOperationBehaviourEmulatorTransformer).map(RxBleClientImpl.this.internalToExternalScanResultMapFunction).mergeWith((ObservableSource<? extends T>) RxBleClientImpl.this.bluetoothAdapterOffExceptionObservable());
            }
        });
    }

    public Observable<RxBleScanResult> scanBleDevices(@Nullable final UUID... filterServiceUUIDs) {
        return Observable.defer(new Callable<ObservableSource<? extends RxBleScanResult>>() {
            public ObservableSource<? extends RxBleScanResult> call() throws Exception {
                RxBleClientImpl.this.scanPreconditionVerifier.verify();
                return RxBleClientImpl.this.initializeScan(filterServiceUUIDs);
            }
        });
    }

    /* access modifiers changed from: private */
    public Observable<RxBleScanResult> initializeScan(@Nullable UUID[] filterServiceUUIDs) {
        Observable<RxBleScanResult> matchingQueuedScan;
        Set<UUID> filteredUUIDs = this.uuidUtil.toDistinctSet(filterServiceUUIDs);
        synchronized (this.queuedScanOperations) {
            matchingQueuedScan = (Observable) this.queuedScanOperations.get(filteredUUIDs);
            if (matchingQueuedScan == null) {
                matchingQueuedScan = createScanOperationApi18(filterServiceUUIDs);
                this.queuedScanOperations.put(filteredUUIDs, matchingQueuedScan);
            }
        }
        return matchingQueuedScan;
    }

    /* access modifiers changed from: private */
    public <T> Observable<T> bluetoothAdapterOffExceptionObservable() {
        return this.rxBleAdapterStateObservable.filter(new Predicate<BleAdapterState>() {
            public boolean test(BleAdapterState state) throws Exception {
                return state != BleAdapterState.STATE_ON;
            }
        }).firstElement().flatMap(new Function<BleAdapterState, MaybeSource<T>>() {
            public MaybeSource<T> apply(BleAdapterState bleAdapterState) throws Exception {
                return Maybe.error((Throwable) new BleScanException(1));
            }
        }).toObservable();
    }

    /* access modifiers changed from: private */
    public RxBleScanResult convertToPublicScanResult(RxBleInternalScanResultLegacy scanResult) {
        return new RxBleScanResult(getBleDevice(scanResult.getBluetoothDevice().getAddress()), scanResult.getRssi(), scanResult.getScanRecord());
    }

    private Observable<RxBleScanResult> createScanOperationApi18(@Nullable UUID[] filterServiceUUIDs) {
        final Set<UUID> filteredUUIDs = this.uuidUtil.toDistinctSet(filterServiceUUIDs);
        return this.operationQueue.queue(new LegacyScanOperation(filterServiceUUIDs, this.rxBleAdapterWrapper, this.uuidUtil)).doFinally(new Action() {
            public void run() throws Exception {
                synchronized (RxBleClientImpl.this.queuedScanOperations) {
                    RxBleClientImpl.this.queuedScanOperations.remove(filteredUUIDs);
                }
            }
        }).mergeWith((ObservableSource<? extends T>) bluetoothAdapterOffExceptionObservable()).map(new Function<RxBleInternalScanResultLegacy, RxBleScanResult>() {
            public RxBleScanResult apply(RxBleInternalScanResultLegacy scanResult) {
                return RxBleClientImpl.this.convertToPublicScanResult(scanResult);
            }
        }).share();
    }

    private void guardBluetoothAdapterAvailable() {
        if (!this.rxBleAdapterWrapper.hasBluetoothAdapter()) {
            throw new UnsupportedOperationException("RxAndroidBle library needs a BluetoothAdapter to be available in the system to work. If this is a test on an emulator then you can use 'https://github.com/Polidea/RxAndroidBle/tree/master/mockrxandroidble'");
        }
    }

    public Observable<State> observeStateChanges() {
        return (Observable) this.lazyClientStateObservable.get();
    }

    public State getState() {
        if (!this.rxBleAdapterWrapper.hasBluetoothAdapter()) {
            return State.BLUETOOTH_NOT_AVAILABLE;
        }
        if (!this.locationServicesStatus.isLocationPermissionOk()) {
            return State.LOCATION_PERMISSION_NOT_GRANTED;
        }
        if (!this.rxBleAdapterWrapper.isBluetoothEnabled()) {
            return State.BLUETOOTH_NOT_ENABLED;
        }
        if (!this.locationServicesStatus.isLocationProviderOk()) {
            return State.LOCATION_SERVICES_NOT_ENABLED;
        }
        return State.READY;
    }
}
