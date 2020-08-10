package com.polidea.rxandroidble2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;
import com.polidea.rxandroidble2.internal.RxBleLog;
import com.polidea.rxandroidble2.scan.ScanFilter;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;
import java.util.Set;
import java.util.UUID;
import p005io.reactivex.Observable;

public abstract class RxBleClient {

    public enum State {
        BLUETOOTH_NOT_AVAILABLE,
        LOCATION_PERMISSION_NOT_GRANTED,
        BLUETOOTH_NOT_ENABLED,
        LOCATION_SERVICES_NOT_ENABLED,
        READY
    }

    public abstract RxBleDevice getBleDevice(@NonNull String str);

    public abstract Set<RxBleDevice> getBondedDevices();

    public abstract State getState();

    public abstract Observable<State> observeStateChanges();

    public abstract Observable<ScanResult> scanBleDevices(ScanSettings scanSettings, ScanFilter... scanFilterArr);

    @Deprecated
    public abstract Observable<RxBleScanResult> scanBleDevices(@Nullable UUID... uuidArr);

    public static RxBleClient create(@NonNull Context context) {
        return DaggerClientComponent.builder().clientModule(new ClientModule(context)).build().rxBleClient();
    }

    public static void setLogLevel(int logLevel) {
        RxBleLog.setLogLevel(logLevel);
    }
}
