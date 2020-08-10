package com.polidea.rxandroidble2.internal.connection;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;

public final class MtuWatcher_Factory implements Factory<MtuWatcher> {
    private final Provider<Integer> initialValueProvider;
    private final Provider<RxBleGattCallback> rxBleGattCallbackProvider;

    public MtuWatcher_Factory(Provider<RxBleGattCallback> rxBleGattCallbackProvider2, Provider<Integer> initialValueProvider2) {
        this.rxBleGattCallbackProvider = rxBleGattCallbackProvider2;
        this.initialValueProvider = initialValueProvider2;
    }

    public MtuWatcher get() {
        return new MtuWatcher((RxBleGattCallback) this.rxBleGattCallbackProvider.get(), ((Integer) this.initialValueProvider.get()).intValue());
    }

    public static MtuWatcher_Factory create(Provider<RxBleGattCallback> rxBleGattCallbackProvider2, Provider<Integer> initialValueProvider2) {
        return new MtuWatcher_Factory(rxBleGattCallbackProvider2, initialValueProvider2);
    }

    public static MtuWatcher newMtuWatcher(RxBleGattCallback rxBleGattCallback, int initialValue) {
        return new MtuWatcher(rxBleGattCallback, initialValue);
    }
}
