package com.polidea.rxandroidble2;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.content.ContentResolver;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import bleshadow.dagger.Binds;
import bleshadow.dagger.Component;
import bleshadow.dagger.Module;
import bleshadow.dagger.Provides;
import bleshadow.javax.inject.Named;
import bleshadow.javax.inject.Provider;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.polidea.rxandroidble2.RxBleAdapterStateObservable.BleAdapterState;
import com.polidea.rxandroidble2.helpers.LocationServicesOkObservable;
import com.polidea.rxandroidble2.internal.DeviceComponent;
import com.polidea.rxandroidble2.internal.scan.InternalToExternalScanResultConverter;
import com.polidea.rxandroidble2.internal.scan.RxBleInternalScanResult;
import com.polidea.rxandroidble2.internal.scan.ScanPreconditionsVerifier;
import com.polidea.rxandroidble2.internal.scan.ScanPreconditionsVerifierApi18;
import com.polidea.rxandroidble2.internal.scan.ScanPreconditionsVerifierApi24;
import com.polidea.rxandroidble2.internal.scan.ScanSetupBuilder;
import com.polidea.rxandroidble2.internal.scan.ScanSetupBuilderImplApi18;
import com.polidea.rxandroidble2.internal.scan.ScanSetupBuilderImplApi21;
import com.polidea.rxandroidble2.internal.scan.ScanSetupBuilderImplApi23;
import com.polidea.rxandroidble2.internal.serialization.ClientOperationQueue;
import com.polidea.rxandroidble2.internal.serialization.ClientOperationQueueImpl;
import com.polidea.rxandroidble2.internal.util.LocationServicesOkObservableApi23;
import com.polidea.rxandroidble2.internal.util.LocationServicesStatus;
import com.polidea.rxandroidble2.internal.util.LocationServicesStatusApi18;
import com.polidea.rxandroidble2.internal.util.LocationServicesStatusApi23;
import com.polidea.rxandroidble2.internal.util.ObservableUtil;
import com.polidea.rxandroidble2.scan.ScanResult;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import p005io.reactivex.Observable;
import p005io.reactivex.Scheduler;
import p005io.reactivex.functions.Function;
import p005io.reactivex.schedulers.Schedulers;

@ClientScope
@Component(modules = {ClientModule.class, ClientModuleBinder.class})
public interface ClientComponent {

    public static class BluetoothConstants {
        public static final String DISABLE_NOTIFICATION_VALUE = "disable-notification-value";
        public static final String ENABLE_INDICATION_VALUE = "enable-indication-value";
        public static final String ENABLE_NOTIFICATION_VALUE = "enable-notification-value";

        private BluetoothConstants() {
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public interface ClientComponentFinalizer {
        void onFinalize();
    }

    @Module(subcomponents = {DeviceComponent.class})
    public static class ClientModule {
        private final Context context;

        public ClientModule(Context context2) {
            this.context = context2;
        }

        /* access modifiers changed from: 0000 */
        @Provides
        public Context provideApplicationContext() {
            return this.context;
        }

        /* access modifiers changed from: 0000 */
        @Provides
        public BluetoothManager provideBluetoothManager() {
            return (BluetoothManager) this.context.getSystemService("bluetooth");
        }

        @Nullable
        @Provides
        static BluetoothAdapter provideBluetoothAdapter() {
            return BluetoothAdapter.getDefaultAdapter();
        }

        @Provides
        @Named("computation")
        static Scheduler provideComputationScheduler() {
            return Schedulers.computation();
        }

        @Provides
        @Named("device-sdk")
        static int provideDeviceSdk() {
            return VERSION.SDK_INT;
        }

        /* access modifiers changed from: 0000 */
        @Provides
        public ContentResolver provideContentResolver() {
            return this.context.getContentResolver();
        }

        /* access modifiers changed from: 0000 */
        @Provides
        public LocationServicesStatus provideLocationServicesStatus(@Named("device-sdk") int deviceSdk, Provider<LocationServicesStatusApi18> locationServicesStatusApi18Provider, Provider<LocationServicesStatusApi23> locationServicesStatusApi23Provider) {
            if (deviceSdk < 23) {
                return (LocationServicesStatus) locationServicesStatusApi18Provider.get();
            }
            return (LocationServicesStatus) locationServicesStatusApi23Provider.get();
        }

        /* access modifiers changed from: 0000 */
        @Provides
        @Named("location-ok-boolean-observable")
        public Observable<Boolean> provideLocationServicesOkObservable(@Named("device-sdk") int deviceSdk, Provider<LocationServicesOkObservableApi23> locationServicesOkObservableApi23Provider) {
            if (deviceSdk < 23) {
                return ObservableUtil.justOnNext(Boolean.valueOf(true));
            }
            return (Observable) locationServicesOkObservableApi23Provider.get();
        }

        @ClientScope
        @Provides
        @Named("executor_connection_queue")
        static ExecutorService provideConnectionQueueExecutorService() {
            return Executors.newCachedThreadPool();
        }

        @ClientScope
        @Provides
        @Named("executor_bluetooth_interaction")
        static ExecutorService provideBluetoothInteractionExecutorService() {
            return Executors.newSingleThreadExecutor();
        }

        @ClientScope
        @Provides
        @Named("executor_bluetooth_callbacks")
        static ExecutorService provideBluetoothCallbacksExecutorService() {
            return Executors.newSingleThreadExecutor();
        }

        @ClientScope
        @Provides
        @Named("bluetooth_interaction")
        static Scheduler provideBluetoothInteractionScheduler(@Named("executor_bluetooth_interaction") ExecutorService service) {
            return Schedulers.from(service);
        }

        @ClientScope
        @Provides
        @Named("bluetooth_callbacks")
        static Scheduler provideBluetoothCallbacksScheduler(@Named("executor_bluetooth_callbacks") ExecutorService service) {
            return Schedulers.from(service);
        }

        @Provides
        static ClientComponentFinalizer provideFinalizationCloseable(@Named("executor_bluetooth_interaction") final ExecutorService interactionExecutorService, @Named("executor_bluetooth_callbacks") final ExecutorService callbacksExecutorService, @Named("executor_connection_queue") final ExecutorService connectionQueueExecutorService) {
            return new ClientComponentFinalizer() {
                public void onFinalize() {
                    interactionExecutorService.shutdown();
                    callbacksExecutorService.shutdown();
                    connectionQueueExecutorService.shutdown();
                }
            };
        }

        /* access modifiers changed from: 0000 */
        @Provides
        public LocationManager provideLocationManager() {
            return (LocationManager) this.context.getSystemService(Param.LOCATION);
        }

        /* access modifiers changed from: 0000 */
        @Provides
        @Named("target-sdk")
        public int provideTargetSdk() {
            try {
                return this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 0).targetSdkVersion;
            } catch (Throwable th) {
                return Integer.MAX_VALUE;
            }
        }

        /* access modifiers changed from: 0000 */
        @SuppressLint({"InlinedApi"})
        @Provides
        @Named("android-wear")
        public boolean provideIsAndroidWear(@Named("device-sdk") int deviceSdk) {
            return deviceSdk >= 20 && this.context.getPackageManager().hasSystemFeature("android.hardware.type.watch");
        }

        @ClientScope
        @Provides
        static ScanSetupBuilder provideScanSetupProvider(@Named("device-sdk") int deviceSdk, Provider<ScanSetupBuilderImplApi18> scanSetupBuilderProviderForApi18, Provider<ScanSetupBuilderImplApi21> scanSetupBuilderProviderForApi21, Provider<ScanSetupBuilderImplApi23> scanSetupBuilderProviderForApi23) {
            if (deviceSdk < 21) {
                return (ScanSetupBuilder) scanSetupBuilderProviderForApi18.get();
            }
            if (deviceSdk < 23) {
                return (ScanSetupBuilder) scanSetupBuilderProviderForApi21.get();
            }
            return (ScanSetupBuilder) scanSetupBuilderProviderForApi23.get();
        }

        @Provides
        @Named("enable-notification-value")
        static byte[] provideEnableNotificationValue() {
            return BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
        }

        @Provides
        @Named("enable-indication-value")
        static byte[] provideEnableIndicationValue() {
            return BluetoothGattDescriptor.ENABLE_INDICATION_VALUE;
        }

        @Provides
        @Named("disable-notification-value")
        static byte[] provideDisableNotificationValue() {
            return BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
        }

        @Provides
        static ScanPreconditionsVerifier provideScanPreconditionVerifier(@Named("device-sdk") int deviceSdk, Provider<ScanPreconditionsVerifierApi18> scanPreconditionVerifierForApi18, Provider<ScanPreconditionsVerifierApi24> scanPreconditionVerifierForApi24) {
            if (deviceSdk < 24) {
                return (ScanPreconditionsVerifier) scanPreconditionVerifierForApi18.get();
            }
            return (ScanPreconditionsVerifier) scanPreconditionVerifierForApi24.get();
        }
    }

    @Module
    public static abstract class ClientModuleBinder {
        /* access modifiers changed from: 0000 */
        @ClientScope
        @Binds
        public abstract ClientOperationQueue bindClientOperationQueue(ClientOperationQueueImpl clientOperationQueueImpl);

        /* access modifiers changed from: 0000 */
        @ClientScope
        @Binds
        public abstract RxBleClient bindRxBleClient(RxBleClientImpl rxBleClientImpl);

        /* access modifiers changed from: 0000 */
        @Binds
        public abstract Observable<BleAdapterState> bindStateObs(RxBleAdapterStateObservable rxBleAdapterStateObservable);

        /* access modifiers changed from: 0000 */
        @Binds
        @Named("timeout")
        public abstract Scheduler bindTimeoutScheduler(@Named("computation") Scheduler scheduler);

        /* access modifiers changed from: 0000 */
        @Binds
        public abstract Function<RxBleInternalScanResult, ScanResult> provideScanResultMapper(InternalToExternalScanResultConverter internalToExternalScanResultConverter);
    }

    public static class NamedBooleanObservables {
        public static final String LOCATION_SERVICES_OK = "location-ok-boolean-observable";

        private NamedBooleanObservables() {
        }
    }

    public static class NamedExecutors {
        public static final String BLUETOOTH_CALLBACKS = "executor_bluetooth_callbacks";
        public static final String BLUETOOTH_INTERACTION = "executor_bluetooth_interaction";
        public static final String CONNECTION_QUEUE = "executor_connection_queue";

        private NamedExecutors() {
        }
    }

    public static class NamedSchedulers {
        public static final String BLUETOOTH_CALLBACKS = "bluetooth_callbacks";
        public static final String BLUETOOTH_INTERACTION = "bluetooth_interaction";
        public static final String COMPUTATION = "computation";
        public static final String TIMEOUT = "timeout";

        private NamedSchedulers() {
        }
    }

    public static class PlatformConstants {
        public static final String BOOL_IS_ANDROID_WEAR = "android-wear";
        public static final String INT_DEVICE_SDK = "device-sdk";
        public static final String INT_TARGET_SDK = "target-sdk";

        private PlatformConstants() {
        }
    }

    LocationServicesOkObservable locationServicesOkObservable();

    RxBleClient rxBleClient();
}
