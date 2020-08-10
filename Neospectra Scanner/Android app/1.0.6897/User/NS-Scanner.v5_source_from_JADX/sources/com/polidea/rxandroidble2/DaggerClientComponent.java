package com.polidea.rxandroidble2;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import bleshadow.dagger.internal.DelegateFactory;
import bleshadow.dagger.internal.DoubleCheck;
import bleshadow.dagger.internal.Preconditions;
import bleshadow.dagger.internal.SetBuilder;
import bleshadow.javax.inject.Provider;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;
import com.polidea.rxandroidble2.RxBleConnection.RxBleConnectionState;
import com.polidea.rxandroidble2.helpers.LocationServicesOkObservable;
import com.polidea.rxandroidble2.helpers.LocationServicesOkObservable_Factory;
import com.polidea.rxandroidble2.internal.DeviceComponent;
import com.polidea.rxandroidble2.internal.DeviceModule;
import com.polidea.rxandroidble2.internal.DeviceModule_ProvideBluetoothDeviceFactory;
import com.polidea.rxandroidble2.internal.DeviceModule_ProvideConnectionStateChangeListenerFactory;
import com.polidea.rxandroidble2.internal.DeviceModule_ProvideConnectionStateRelayFactory;
import com.polidea.rxandroidble2.internal.DeviceModule_ProvideMacAddressFactory;
import com.polidea.rxandroidble2.internal.DeviceModule_ProvidesConnectTimeoutConfFactory;
import com.polidea.rxandroidble2.internal.DeviceModule_ProvidesDisconnectTimeoutConfFactory;
import com.polidea.rxandroidble2.internal.RxBleDeviceImpl_Factory;
import com.polidea.rxandroidble2.internal.RxBleDeviceProvider;
import com.polidea.rxandroidble2.internal.RxBleDeviceProvider_Factory;
import com.polidea.rxandroidble2.internal.cache.DeviceComponentCache;
import com.polidea.rxandroidble2.internal.cache.DeviceComponentCache_Factory;
import com.polidea.rxandroidble2.internal.connection.BluetoothGattProvider;
import com.polidea.rxandroidble2.internal.connection.BluetoothGattProvider_Factory;
import com.polidea.rxandroidble2.internal.connection.ConnectionComponent;
import com.polidea.rxandroidble2.internal.connection.ConnectionModule;
import com.polidea.rxandroidble2.internal.connection.ConnectionModuleBinder_GattWriteMtuOverheadFactory;
import com.polidea.rxandroidble2.internal.connection.ConnectionModuleBinder_MinimumMtuFactory;
import com.polidea.rxandroidble2.internal.connection.ConnectionModuleBinder_ProvideBluetoothGattFactory;
import com.polidea.rxandroidble2.internal.connection.ConnectionModule_ProvideAutoConnectFactory;
import com.polidea.rxandroidble2.internal.connection.ConnectionModule_ProvideCharacteristicPropertiesParserFactory;
import com.polidea.rxandroidble2.internal.connection.ConnectionModule_ProvideIllegalOperationHandlerFactory;
import com.polidea.rxandroidble2.internal.connection.ConnectionModule_ProvidesOperationTimeoutConfFactory;
import com.polidea.rxandroidble2.internal.connection.ConnectionStateChangeListener;
import com.polidea.rxandroidble2.internal.connection.ConnectionSubscriptionWatcher;
import com.polidea.rxandroidble2.internal.connection.ConnectorImpl_Factory;
import com.polidea.rxandroidble2.internal.connection.DescriptorWriter_Factory;
import com.polidea.rxandroidble2.internal.connection.DisconnectAction_Factory;
import com.polidea.rxandroidble2.internal.connection.DisconnectionRouter_Factory;
import com.polidea.rxandroidble2.internal.connection.IllegalOperationChecker_Factory;
import com.polidea.rxandroidble2.internal.connection.IllegalOperationMessageCreator_Factory;
import com.polidea.rxandroidble2.internal.connection.LoggingIllegalOperationHandler_Factory;
import com.polidea.rxandroidble2.internal.connection.LongWriteOperationBuilderImpl_Factory;
import com.polidea.rxandroidble2.internal.connection.MtuBasedPayloadSizeLimit_Factory;
import com.polidea.rxandroidble2.internal.connection.MtuWatcher_Factory;
import com.polidea.rxandroidble2.internal.connection.NativeCallbackDispatcher_Factory;
import com.polidea.rxandroidble2.internal.connection.NotificationAndIndicationManager_Factory;
import com.polidea.rxandroidble2.internal.connection.RxBleConnectionImpl;
import com.polidea.rxandroidble2.internal.connection.RxBleConnectionImpl_Factory;
import com.polidea.rxandroidble2.internal.connection.RxBleGattCallback;
import com.polidea.rxandroidble2.internal.connection.RxBleGattCallback_Factory;
import com.polidea.rxandroidble2.internal.connection.ServiceDiscoveryManager_Factory;
import com.polidea.rxandroidble2.internal.connection.ThrowingIllegalOperationHandler_Factory;
import com.polidea.rxandroidble2.internal.operations.ConnectOperation;
import com.polidea.rxandroidble2.internal.operations.ConnectOperation_Factory;
import com.polidea.rxandroidble2.internal.operations.DisconnectOperation_Factory;
import com.polidea.rxandroidble2.internal.operations.OperationsProviderImpl_Factory;
import com.polidea.rxandroidble2.internal.operations.ReadRssiOperation_Factory;
import com.polidea.rxandroidble2.internal.operations.TimeoutConfiguration;
import com.polidea.rxandroidble2.internal.scan.AndroidScanObjectsConverter_Factory;
import com.polidea.rxandroidble2.internal.scan.InternalScanResultCreator;
import com.polidea.rxandroidble2.internal.scan.InternalScanResultCreator_Factory;
import com.polidea.rxandroidble2.internal.scan.InternalToExternalScanResultConverter_Factory;
import com.polidea.rxandroidble2.internal.scan.ScanPreconditionsVerifierApi18_Factory;
import com.polidea.rxandroidble2.internal.scan.ScanPreconditionsVerifierApi24_Factory;
import com.polidea.rxandroidble2.internal.scan.ScanSettingsEmulator_Factory;
import com.polidea.rxandroidble2.internal.scan.ScanSetupBuilder;
import com.polidea.rxandroidble2.internal.scan.ScanSetupBuilderImplApi18_Factory;
import com.polidea.rxandroidble2.internal.scan.ScanSetupBuilderImplApi21_Factory;
import com.polidea.rxandroidble2.internal.scan.ScanSetupBuilderImplApi23_Factory;
import com.polidea.rxandroidble2.internal.serialization.ClientOperationQueue;
import com.polidea.rxandroidble2.internal.serialization.ClientOperationQueueImpl_Factory;
import com.polidea.rxandroidble2.internal.serialization.ConnectionOperationQueueImpl;
import com.polidea.rxandroidble2.internal.serialization.ConnectionOperationQueueImpl_Factory;
import com.polidea.rxandroidble2.internal.util.BleConnectionCompat;
import com.polidea.rxandroidble2.internal.util.CheckerLocationPermission_Factory;
import com.polidea.rxandroidble2.internal.util.CheckerLocationProvider_Factory;
import com.polidea.rxandroidble2.internal.util.ClientStateObservable_Factory;
import com.polidea.rxandroidble2.internal.util.LocationServicesOkObservableApi23_Factory;
import com.polidea.rxandroidble2.internal.util.LocationServicesStatusApi18_Factory;
import com.polidea.rxandroidble2.internal.util.LocationServicesStatusApi23_Factory;
import com.polidea.rxandroidble2.internal.util.RxBleAdapterWrapper;
import com.polidea.rxandroidble2.internal.util.RxBleAdapterWrapper_Factory;
import com.polidea.rxandroidble2.internal.util.RxBleServicesLogger_Factory;
import com.polidea.rxandroidble2.internal.util.UUIDUtil_Factory;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import p005io.reactivex.Observable;
import p005io.reactivex.Scheduler;

public final class DaggerClientComponent implements ClientComponent {
    private AndroidScanObjectsConverter_Factory androidScanObjectsConverterProvider;
    /* access modifiers changed from: private */
    public Provider<ClientOperationQueue> bindClientOperationQueueProvider;
    private Provider<RxBleClient> bindRxBleClientProvider;
    private CheckerLocationPermission_Factory checkerLocationPermissionProvider;
    private CheckerLocationProvider_Factory checkerLocationProvider;
    /* access modifiers changed from: private */
    public ClientModule clientModule;
    private ClientOperationQueueImpl_Factory clientOperationQueueImplProvider;
    private ClientStateObservable_Factory clientStateObservableProvider;
    private Provider<com.polidea.rxandroidble2.internal.DeviceComponent.Builder> deviceComponentBuilderProvider;
    private Provider<DeviceComponentCache> deviceComponentCacheProvider;
    private Provider<InternalScanResultCreator> internalScanResultCreatorProvider;
    private InternalToExternalScanResultConverter_Factory internalToExternalScanResultConverterProvider;
    private LocationServicesOkObservableApi23_Factory locationServicesOkObservableApi23Provider;
    private LocationServicesStatusApi23_Factory locationServicesStatusApi23Provider;
    private ClientComponent_ClientModule_ProvideApplicationContextFactory provideApplicationContextProvider;
    private Provider<ExecutorService> provideBluetoothCallbacksExecutorServiceProvider;
    /* access modifiers changed from: private */
    public Provider<Scheduler> provideBluetoothCallbacksSchedulerProvider;
    private Provider<ExecutorService> provideBluetoothInteractionExecutorServiceProvider;
    /* access modifiers changed from: private */
    public Provider<Scheduler> provideBluetoothInteractionSchedulerProvider;
    /* access modifiers changed from: private */
    public ClientComponent_ClientModule_ProvideBluetoothManagerFactory provideBluetoothManagerProvider;
    /* access modifiers changed from: private */
    public Provider<ExecutorService> provideConnectionQueueExecutorServiceProvider;
    private ClientComponent_ClientModule_ProvideContentResolverFactory provideContentResolverProvider;
    private ClientComponent_ClientModule_ProvideFinalizationCloseableFactory provideFinalizationCloseableProvider;
    private ClientComponent_ClientModule_ProvideIsAndroidWearFactory provideIsAndroidWearProvider;
    private ClientComponent_ClientModule_ProvideLocationManagerFactory provideLocationManagerProvider;
    private C0699x61f40e72 provideLocationServicesOkObservableProvider;
    private C0700x9cd4449f provideLocationServicesStatusProvider;
    private C0701x8de81979 provideScanPreconditionVerifierProvider;
    private Provider<ScanSetupBuilder> provideScanSetupProvider;
    private ClientComponent_ClientModule_ProvideTargetSdkFactory provideTargetSdkProvider;
    /* access modifiers changed from: private */
    public RxBleAdapterStateObservable_Factory rxBleAdapterStateObservableProvider;
    /* access modifiers changed from: private */
    public RxBleAdapterWrapper_Factory rxBleAdapterWrapperProvider;
    private RxBleClientImpl_Factory rxBleClientImplProvider;
    private Provider<RxBleDeviceProvider> rxBleDeviceProvider;
    private ScanPreconditionsVerifierApi18_Factory scanPreconditionsVerifierApi18Provider;
    private ScanPreconditionsVerifierApi24_Factory scanPreconditionsVerifierApi24Provider;
    private ScanSettingsEmulator_Factory scanSettingsEmulatorProvider;
    private ScanSetupBuilderImplApi18_Factory scanSetupBuilderImplApi18Provider;
    private ScanSetupBuilderImplApi21_Factory scanSetupBuilderImplApi21Provider;
    private ScanSetupBuilderImplApi23_Factory scanSetupBuilderImplApi23Provider;

    public static final class Builder {
        /* access modifiers changed from: private */
        public ClientModule clientModule;

        private Builder() {
        }

        public ClientComponent build() {
            if (this.clientModule != null) {
                return new DaggerClientComponent(this);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(ClientModule.class.getCanonicalName());
            sb.append(" must be set");
            throw new IllegalStateException(sb.toString());
        }

        public Builder clientModule(ClientModule clientModule2) {
            this.clientModule = (ClientModule) Preconditions.checkNotNull(clientModule2);
            return this;
        }
    }

    private final class DeviceComponentBuilder implements com.polidea.rxandroidble2.internal.DeviceComponent.Builder {
        /* access modifiers changed from: private */
        public DeviceModule deviceModule;

        private DeviceComponentBuilder() {
        }

        public DeviceComponent build() {
            if (this.deviceModule != null) {
                return new DeviceComponentImpl(this);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(DeviceModule.class.getCanonicalName());
            sb.append(" must be set");
            throw new IllegalStateException(sb.toString());
        }

        public DeviceComponentBuilder deviceModule(DeviceModule module) {
            this.deviceModule = (DeviceModule) Preconditions.checkNotNull(module);
            return this;
        }
    }

    private final class DeviceComponentImpl implements DeviceComponent {
        private Provider<com.polidea.rxandroidble2.internal.connection.ConnectionComponent.Builder> connectionComponentBuilderProvider;
        private ConnectorImpl_Factory connectorImplProvider;
        private DeviceModule deviceModule;
        private DeviceModule_ProvideBluetoothDeviceFactory provideBluetoothDeviceProvider;
        /* access modifiers changed from: private */
        public Provider<ConnectionStateChangeListener> provideConnectionStateChangeListenerProvider;
        private Provider<BehaviorRelay<RxBleConnectionState>> provideConnectionStateRelayProvider;
        /* access modifiers changed from: private */
        public DeviceModule_ProvideMacAddressFactory provideMacAddressProvider;
        /* access modifiers changed from: private */
        public DeviceModule_ProvidesDisconnectTimeoutConfFactory providesDisconnectTimeoutConfProvider;
        private Provider rxBleDeviceImplProvider;

        private final class ConnectionComponentBuilder implements com.polidea.rxandroidble2.internal.connection.ConnectionComponent.Builder {
            /* access modifiers changed from: private */
            public ConnectionModule connectionModule;

            private ConnectionComponentBuilder() {
            }

            public ConnectionComponent build() {
                if (this.connectionModule != null) {
                    return new ConnectionComponentImpl(this);
                }
                StringBuilder sb = new StringBuilder();
                sb.append(ConnectionModule.class.getCanonicalName());
                sb.append(" must be set");
                throw new IllegalStateException(sb.toString());
            }

            public ConnectionComponentBuilder connectionModule(ConnectionModule connectionModule2) {
                this.connectionModule = (ConnectionModule) Preconditions.checkNotNull(connectionModule2);
                return this;
            }
        }

        private final class ConnectionComponentImpl implements ConnectionComponent {
            private Provider<BluetoothGattProvider> bluetoothGattProvider;
            private Provider<ConnectionOperationQueueImpl> connectionOperationQueueImplProvider;
            private Provider descriptorWriterProvider;
            private Provider disconnectActionProvider;
            private DisconnectOperation_Factory disconnectOperationProvider;
            private Provider disconnectionRouterProvider;
            private IllegalOperationChecker_Factory illegalOperationCheckerProvider;
            private IllegalOperationMessageCreator_Factory illegalOperationMessageCreatorProvider;
            private LoggingIllegalOperationHandler_Factory loggingIllegalOperationHandlerProvider;
            private LongWriteOperationBuilderImpl_Factory longWriteOperationBuilderImplProvider;
            private Provider mtuBasedPayloadSizeLimitProvider;
            private Provider mtuWatcherProvider;
            private Provider notificationAndIndicationManagerProvider;
            private OperationsProviderImpl_Factory operationsProviderImplProvider;
            private Provider<Boolean> provideAutoConnectProvider;
            private Provider<BluetoothGatt> provideBluetoothGattProvider;
            private ConnectionModule_ProvideCharacteristicPropertiesParserFactory provideCharacteristicPropertiesParserProvider;
            private ConnectionModule_ProvideIllegalOperationHandlerFactory provideIllegalOperationHandlerProvider;
            private ConnectionModule_ProvidesOperationTimeoutConfFactory providesOperationTimeoutConfProvider;
            private ReadRssiOperation_Factory readRssiOperationProvider;
            private Provider<RxBleConnectionImpl> rxBleConnectionImplProvider;
            private Provider<RxBleGattCallback> rxBleGattCallbackProvider;
            private RxBleServicesLogger_Factory rxBleServicesLoggerProvider;
            private Provider serviceDiscoveryManagerProvider;
            private ThrowingIllegalOperationHandler_Factory throwingIllegalOperationHandlerProvider;

            private ConnectionComponentImpl(ConnectionComponentBuilder builder) {
                initialize(builder);
            }

            private BleConnectionCompat getBleConnectionCompat() {
                return new BleConnectionCompat(ClientComponent_ClientModule_ProvideApplicationContextFactory.proxyProvideApplicationContext(DaggerClientComponent.this.clientModule));
            }

            private void initialize(ConnectionComponentBuilder builder) {
                this.bluetoothGattProvider = DoubleCheck.provider(BluetoothGattProvider_Factory.create());
                this.disconnectionRouterProvider = DoubleCheck.provider(DisconnectionRouter_Factory.create(DeviceComponentImpl.this.provideMacAddressProvider, DaggerClientComponent.this.rxBleAdapterWrapperProvider, DaggerClientComponent.this.rxBleAdapterStateObservableProvider));
                this.rxBleGattCallbackProvider = DoubleCheck.provider(RxBleGattCallback_Factory.create(DaggerClientComponent.this.provideBluetoothCallbacksSchedulerProvider, this.bluetoothGattProvider, this.disconnectionRouterProvider, NativeCallbackDispatcher_Factory.create()));
                this.provideAutoConnectProvider = DoubleCheck.provider(ConnectionModule_ProvideAutoConnectFactory.create(builder.connectionModule));
                this.connectionOperationQueueImplProvider = DoubleCheck.provider(ConnectionOperationQueueImpl_Factory.create(DeviceComponentImpl.this.provideMacAddressProvider, this.disconnectionRouterProvider, DaggerClientComponent.this.provideConnectionQueueExecutorServiceProvider, DaggerClientComponent.this.provideBluetoothInteractionSchedulerProvider));
                this.provideBluetoothGattProvider = DoubleCheck.provider(ConnectionModuleBinder_ProvideBluetoothGattFactory.create(this.bluetoothGattProvider));
                this.provideCharacteristicPropertiesParserProvider = ConnectionModule_ProvideCharacteristicPropertiesParserFactory.create(builder.connectionModule);
                this.rxBleServicesLoggerProvider = RxBleServicesLogger_Factory.create(this.provideCharacteristicPropertiesParserProvider);
                this.providesOperationTimeoutConfProvider = ConnectionModule_ProvidesOperationTimeoutConfFactory.create(builder.connectionModule, ClientComponent_ClientModule_ProvideComputationSchedulerFactory.create());
                this.readRssiOperationProvider = ReadRssiOperation_Factory.create(this.rxBleGattCallbackProvider, this.provideBluetoothGattProvider, this.providesOperationTimeoutConfProvider);
                this.operationsProviderImplProvider = OperationsProviderImpl_Factory.create(this.rxBleGattCallbackProvider, this.provideBluetoothGattProvider, this.rxBleServicesLoggerProvider, this.providesOperationTimeoutConfProvider, DaggerClientComponent.this.provideBluetoothInteractionSchedulerProvider, ClientComponent_ClientModule_ProvideComputationSchedulerFactory.create(), this.readRssiOperationProvider);
                this.serviceDiscoveryManagerProvider = DoubleCheck.provider(ServiceDiscoveryManager_Factory.create(this.connectionOperationQueueImplProvider, this.provideBluetoothGattProvider, this.operationsProviderImplProvider));
                this.descriptorWriterProvider = DoubleCheck.provider(DescriptorWriter_Factory.create(this.connectionOperationQueueImplProvider, this.operationsProviderImplProvider));
                this.notificationAndIndicationManagerProvider = DoubleCheck.provider(NotificationAndIndicationManager_Factory.create(C0698xd399626d.create(), ClientComponent_ClientModule_ProvideEnableIndicationValueFactory.create(), C0697x975cafc6.create(), this.provideBluetoothGattProvider, this.rxBleGattCallbackProvider, this.descriptorWriterProvider));
                this.mtuWatcherProvider = DoubleCheck.provider(MtuWatcher_Factory.create(this.rxBleGattCallbackProvider, ConnectionModuleBinder_MinimumMtuFactory.create()));
                this.rxBleConnectionImplProvider = new DelegateFactory();
                this.mtuBasedPayloadSizeLimitProvider = DoubleCheck.provider(MtuBasedPayloadSizeLimit_Factory.create(this.rxBleConnectionImplProvider, ConnectionModuleBinder_GattWriteMtuOverheadFactory.create()));
                this.longWriteOperationBuilderImplProvider = LongWriteOperationBuilderImpl_Factory.create(this.connectionOperationQueueImplProvider, this.mtuBasedPayloadSizeLimitProvider, this.rxBleConnectionImplProvider, this.operationsProviderImplProvider);
                this.illegalOperationMessageCreatorProvider = IllegalOperationMessageCreator_Factory.create(this.provideCharacteristicPropertiesParserProvider);
                this.loggingIllegalOperationHandlerProvider = LoggingIllegalOperationHandler_Factory.create(this.illegalOperationMessageCreatorProvider);
                this.throwingIllegalOperationHandlerProvider = ThrowingIllegalOperationHandler_Factory.create(this.illegalOperationMessageCreatorProvider);
                this.provideIllegalOperationHandlerProvider = ConnectionModule_ProvideIllegalOperationHandlerFactory.create(builder.connectionModule, this.loggingIllegalOperationHandlerProvider, this.throwingIllegalOperationHandlerProvider);
                this.illegalOperationCheckerProvider = IllegalOperationChecker_Factory.create(this.provideIllegalOperationHandlerProvider);
                DelegateFactory rxBleConnectionImplProviderDelegate = (DelegateFactory) this.rxBleConnectionImplProvider;
                this.rxBleConnectionImplProvider = DoubleCheck.provider(RxBleConnectionImpl_Factory.create(this.connectionOperationQueueImplProvider, this.rxBleGattCallbackProvider, this.provideBluetoothGattProvider, this.serviceDiscoveryManagerProvider, this.notificationAndIndicationManagerProvider, this.mtuWatcherProvider, this.descriptorWriterProvider, this.operationsProviderImplProvider, this.longWriteOperationBuilderImplProvider, DaggerClientComponent.this.provideBluetoothInteractionSchedulerProvider, this.illegalOperationCheckerProvider));
                rxBleConnectionImplProviderDelegate.setDelegatedProvider(this.rxBleConnectionImplProvider);
                this.disconnectOperationProvider = DisconnectOperation_Factory.create(this.rxBleGattCallbackProvider, this.bluetoothGattProvider, DeviceComponentImpl.this.provideMacAddressProvider, DaggerClientComponent.this.provideBluetoothManagerProvider, DaggerClientComponent.this.provideBluetoothInteractionSchedulerProvider, DeviceComponentImpl.this.providesDisconnectTimeoutConfProvider, DeviceComponentImpl.this.provideConnectionStateChangeListenerProvider);
                this.disconnectActionProvider = DoubleCheck.provider(DisconnectAction_Factory.create(DaggerClientComponent.this.bindClientOperationQueueProvider, this.disconnectOperationProvider));
            }

            public ConnectOperation connectOperation() {
                return ConnectOperation_Factory.newConnectOperation(DeviceComponentImpl.this.getBluetoothDevice(), getBleConnectionCompat(), (RxBleGattCallback) this.rxBleGattCallbackProvider.get(), (BluetoothGattProvider) this.bluetoothGattProvider.get(), DeviceComponentImpl.this.getNamedTimeoutConfiguration(), ((Boolean) this.provideAutoConnectProvider.get()).booleanValue(), (ConnectionStateChangeListener) DeviceComponentImpl.this.provideConnectionStateChangeListenerProvider.get());
            }

            public RxBleConnection rxBleConnection() {
                return (RxBleConnection) this.rxBleConnectionImplProvider.get();
            }

            public RxBleGattCallback gattCallback() {
                return (RxBleGattCallback) this.rxBleGattCallbackProvider.get();
            }

            public Set<ConnectionSubscriptionWatcher> connectionSubscriptionWatchers() {
                return SetBuilder.newSetBuilder(3).add((ConnectionSubscriptionWatcher) this.mtuWatcherProvider.get()).add((ConnectionSubscriptionWatcher) this.disconnectActionProvider.get()).add(this.connectionOperationQueueImplProvider.get()).build();
            }
        }

        private DeviceComponentImpl(DeviceComponentBuilder builder) {
            initialize(builder);
        }

        /* access modifiers changed from: private */
        public BluetoothDevice getBluetoothDevice() {
            return DeviceModule_ProvideBluetoothDeviceFactory.proxyProvideBluetoothDevice(this.deviceModule, DaggerClientComponent.this.getRxBleAdapterWrapper());
        }

        /* access modifiers changed from: private */
        public TimeoutConfiguration getNamedTimeoutConfiguration() {
            return DeviceModule_ProvidesConnectTimeoutConfFactory.proxyProvidesConnectTimeoutConf(ClientComponent_ClientModule_ProvideComputationSchedulerFactory.proxyProvideComputationScheduler());
        }

        private void initialize(DeviceComponentBuilder builder) {
            this.provideBluetoothDeviceProvider = DeviceModule_ProvideBluetoothDeviceFactory.create(builder.deviceModule, DaggerClientComponent.this.rxBleAdapterWrapperProvider);
            this.connectionComponentBuilderProvider = new Provider<com.polidea.rxandroidble2.internal.connection.ConnectionComponent.Builder>() {
                public com.polidea.rxandroidble2.internal.connection.ConnectionComponent.Builder get() {
                    return new ConnectionComponentBuilder();
                }
            };
            this.connectorImplProvider = ConnectorImpl_Factory.create(DaggerClientComponent.this.bindClientOperationQueueProvider, this.connectionComponentBuilderProvider, DaggerClientComponent.this.provideBluetoothCallbacksSchedulerProvider);
            this.provideConnectionStateRelayProvider = DoubleCheck.provider(DeviceModule_ProvideConnectionStateRelayFactory.create());
            this.rxBleDeviceImplProvider = DoubleCheck.provider(RxBleDeviceImpl_Factory.create(this.provideBluetoothDeviceProvider, this.connectorImplProvider, this.provideConnectionStateRelayProvider));
            this.deviceModule = builder.deviceModule;
            this.provideMacAddressProvider = DeviceModule_ProvideMacAddressFactory.create(builder.deviceModule);
            this.provideConnectionStateChangeListenerProvider = DoubleCheck.provider(DeviceModule_ProvideConnectionStateChangeListenerFactory.create(this.provideConnectionStateRelayProvider));
            this.providesDisconnectTimeoutConfProvider = DeviceModule_ProvidesDisconnectTimeoutConfFactory.create(ClientComponent_ClientModule_ProvideComputationSchedulerFactory.create());
        }

        public RxBleDevice provideDevice() {
            return (RxBleDevice) this.rxBleDeviceImplProvider.get();
        }
    }

    private DaggerClientComponent(Builder builder) {
        initialize(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    private Observable<Boolean> getNamedObservableOfBoolean() {
        return C0699x61f40e72.proxyProvideLocationServicesOkObservable(this.clientModule, ClientModule.provideDeviceSdk(), this.locationServicesOkObservableApi23Provider);
    }

    /* access modifiers changed from: private */
    public RxBleAdapterWrapper getRxBleAdapterWrapper() {
        return new RxBleAdapterWrapper(ClientModule.provideBluetoothAdapter());
    }

    private void initialize(Builder builder) {
        this.clientModule = builder.clientModule;
        this.provideApplicationContextProvider = ClientComponent_ClientModule_ProvideApplicationContextFactory.create(builder.clientModule);
        this.provideContentResolverProvider = ClientComponent_ClientModule_ProvideContentResolverFactory.create(builder.clientModule);
        this.provideLocationManagerProvider = ClientComponent_ClientModule_ProvideLocationManagerFactory.create(builder.clientModule);
        this.checkerLocationProvider = CheckerLocationProvider_Factory.create(this.provideContentResolverProvider, this.provideLocationManagerProvider);
        this.checkerLocationPermissionProvider = CheckerLocationPermission_Factory.create(this.provideApplicationContextProvider);
        this.provideTargetSdkProvider = ClientComponent_ClientModule_ProvideTargetSdkFactory.create(builder.clientModule);
        this.provideIsAndroidWearProvider = ClientComponent_ClientModule_ProvideIsAndroidWearFactory.create(builder.clientModule, ClientComponent_ClientModule_ProvideDeviceSdkFactory.create());
        this.locationServicesStatusApi23Provider = LocationServicesStatusApi23_Factory.create(this.checkerLocationProvider, this.checkerLocationPermissionProvider, this.provideTargetSdkProvider, this.provideIsAndroidWearProvider);
        this.provideLocationServicesStatusProvider = C0700x9cd4449f.create(builder.clientModule, ClientComponent_ClientModule_ProvideDeviceSdkFactory.create(), LocationServicesStatusApi18_Factory.create(), this.locationServicesStatusApi23Provider);
        this.locationServicesOkObservableApi23Provider = LocationServicesOkObservableApi23_Factory.create(this.provideApplicationContextProvider, this.provideLocationServicesStatusProvider);
        this.rxBleAdapterWrapperProvider = RxBleAdapterWrapper_Factory.create(ClientComponent_ClientModule_ProvideBluetoothAdapterFactory.create());
        this.provideBluetoothInteractionExecutorServiceProvider = DoubleCheck.provider(C0694xa9312dd2.create());
        this.provideBluetoothInteractionSchedulerProvider = DoubleCheck.provider(C0695xd84c18d9.create(this.provideBluetoothInteractionExecutorServiceProvider));
        this.clientOperationQueueImplProvider = ClientOperationQueueImpl_Factory.create(this.provideBluetoothInteractionSchedulerProvider);
        this.bindClientOperationQueueProvider = DoubleCheck.provider(this.clientOperationQueueImplProvider);
        this.rxBleAdapterStateObservableProvider = RxBleAdapterStateObservable_Factory.create(this.provideApplicationContextProvider);
        this.provideLocationServicesOkObservableProvider = C0699x61f40e72.create(builder.clientModule, ClientComponent_ClientModule_ProvideDeviceSdkFactory.create(), this.locationServicesOkObservableApi23Provider);
        this.clientStateObservableProvider = ClientStateObservable_Factory.create(this.rxBleAdapterWrapperProvider, this.rxBleAdapterStateObservableProvider, this.provideLocationServicesOkObservableProvider, this.provideLocationServicesStatusProvider, ClientComponent_ClientModule_ProvideComputationSchedulerFactory.create());
        this.deviceComponentCacheProvider = DoubleCheck.provider(DeviceComponentCache_Factory.create());
        this.deviceComponentBuilderProvider = new Provider<com.polidea.rxandroidble2.internal.DeviceComponent.Builder>() {
            public com.polidea.rxandroidble2.internal.DeviceComponent.Builder get() {
                return new DeviceComponentBuilder();
            }
        };
        this.rxBleDeviceProvider = DoubleCheck.provider(RxBleDeviceProvider_Factory.create(this.deviceComponentCacheProvider, this.deviceComponentBuilderProvider));
        this.internalScanResultCreatorProvider = DoubleCheck.provider(InternalScanResultCreator_Factory.create(UUIDUtil_Factory.create()));
        this.scanSettingsEmulatorProvider = ScanSettingsEmulator_Factory.create(ClientComponent_ClientModule_ProvideComputationSchedulerFactory.create());
        this.scanSetupBuilderImplApi18Provider = ScanSetupBuilderImplApi18_Factory.create(this.rxBleAdapterWrapperProvider, this.internalScanResultCreatorProvider, this.scanSettingsEmulatorProvider);
        this.androidScanObjectsConverterProvider = AndroidScanObjectsConverter_Factory.create(ClientComponent_ClientModule_ProvideDeviceSdkFactory.create());
        this.scanSetupBuilderImplApi21Provider = ScanSetupBuilderImplApi21_Factory.create(this.rxBleAdapterWrapperProvider, this.internalScanResultCreatorProvider, this.scanSettingsEmulatorProvider, this.androidScanObjectsConverterProvider);
        this.scanSetupBuilderImplApi23Provider = ScanSetupBuilderImplApi23_Factory.create(this.rxBleAdapterWrapperProvider, this.internalScanResultCreatorProvider, this.androidScanObjectsConverterProvider);
        this.provideScanSetupProvider = DoubleCheck.provider(ClientComponent_ClientModule_ProvideScanSetupProviderFactory.create(ClientComponent_ClientModule_ProvideDeviceSdkFactory.create(), this.scanSetupBuilderImplApi18Provider, this.scanSetupBuilderImplApi21Provider, this.scanSetupBuilderImplApi23Provider));
        this.scanPreconditionsVerifierApi18Provider = ScanPreconditionsVerifierApi18_Factory.create(this.rxBleAdapterWrapperProvider, this.provideLocationServicesStatusProvider);
        this.scanPreconditionsVerifierApi24Provider = ScanPreconditionsVerifierApi24_Factory.create(this.scanPreconditionsVerifierApi18Provider, ClientComponent_ClientModule_ProvideComputationSchedulerFactory.create());
        this.provideScanPreconditionVerifierProvider = C0701x8de81979.create(ClientComponent_ClientModule_ProvideDeviceSdkFactory.create(), this.scanPreconditionsVerifierApi18Provider, this.scanPreconditionsVerifierApi24Provider);
        this.internalToExternalScanResultConverterProvider = InternalToExternalScanResultConverter_Factory.create(this.rxBleDeviceProvider);
        this.provideBluetoothCallbacksExecutorServiceProvider = DoubleCheck.provider(C0692x4a2ba98e.create());
        this.provideConnectionQueueExecutorServiceProvider = DoubleCheck.provider(C0696x11ee2f55.create());
        this.provideFinalizationCloseableProvider = ClientComponent_ClientModule_ProvideFinalizationCloseableFactory.create(this.provideBluetoothInteractionExecutorServiceProvider, this.provideBluetoothCallbacksExecutorServiceProvider, this.provideConnectionQueueExecutorServiceProvider);
        this.rxBleClientImplProvider = RxBleClientImpl_Factory.create(this.rxBleAdapterWrapperProvider, this.bindClientOperationQueueProvider, this.rxBleAdapterStateObservableProvider, UUIDUtil_Factory.create(), this.provideLocationServicesStatusProvider, this.clientStateObservableProvider, this.rxBleDeviceProvider, this.provideScanSetupProvider, this.provideScanPreconditionVerifierProvider, this.internalToExternalScanResultConverterProvider, this.provideBluetoothInteractionSchedulerProvider, this.provideFinalizationCloseableProvider);
        this.bindRxBleClientProvider = DoubleCheck.provider(this.rxBleClientImplProvider);
        this.provideBluetoothCallbacksSchedulerProvider = DoubleCheck.provider(C0693x76cd1195.create(this.provideBluetoothCallbacksExecutorServiceProvider));
        this.provideBluetoothManagerProvider = ClientComponent_ClientModule_ProvideBluetoothManagerFactory.create(builder.clientModule);
    }

    public LocationServicesOkObservable locationServicesOkObservable() {
        return LocationServicesOkObservable_Factory.newLocationServicesOkObservable(getNamedObservableOfBoolean());
    }

    public RxBleClient rxBleClient() {
        return (RxBleClient) this.bindRxBleClientProvider.get();
    }
}
