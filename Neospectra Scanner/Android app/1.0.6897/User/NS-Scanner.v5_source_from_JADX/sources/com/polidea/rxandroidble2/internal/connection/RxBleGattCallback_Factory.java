package com.polidea.rxandroidble2.internal.connection;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import p005io.reactivex.Scheduler;

public final class RxBleGattCallback_Factory implements Factory<RxBleGattCallback> {
    private final Provider<BluetoothGattProvider> bluetoothGattProvider;
    private final Provider<Scheduler> callbackSchedulerProvider;
    private final Provider<DisconnectionRouter> disconnectionRouterProvider;
    private final Provider<NativeCallbackDispatcher> nativeCallbackDispatcherProvider;

    public RxBleGattCallback_Factory(Provider<Scheduler> callbackSchedulerProvider2, Provider<BluetoothGattProvider> bluetoothGattProvider2, Provider<DisconnectionRouter> disconnectionRouterProvider2, Provider<NativeCallbackDispatcher> nativeCallbackDispatcherProvider2) {
        this.callbackSchedulerProvider = callbackSchedulerProvider2;
        this.bluetoothGattProvider = bluetoothGattProvider2;
        this.disconnectionRouterProvider = disconnectionRouterProvider2;
        this.nativeCallbackDispatcherProvider = nativeCallbackDispatcherProvider2;
    }

    public RxBleGattCallback get() {
        return new RxBleGattCallback((Scheduler) this.callbackSchedulerProvider.get(), (BluetoothGattProvider) this.bluetoothGattProvider.get(), (DisconnectionRouter) this.disconnectionRouterProvider.get(), (NativeCallbackDispatcher) this.nativeCallbackDispatcherProvider.get());
    }

    public static RxBleGattCallback_Factory create(Provider<Scheduler> callbackSchedulerProvider2, Provider<BluetoothGattProvider> bluetoothGattProvider2, Provider<DisconnectionRouter> disconnectionRouterProvider2, Provider<NativeCallbackDispatcher> nativeCallbackDispatcherProvider2) {
        return new RxBleGattCallback_Factory(callbackSchedulerProvider2, bluetoothGattProvider2, disconnectionRouterProvider2, nativeCallbackDispatcherProvider2);
    }

    public static RxBleGattCallback newRxBleGattCallback(Scheduler callbackScheduler, BluetoothGattProvider bluetoothGattProvider2, Object disconnectionRouter, Object nativeCallbackDispatcher) {
        return new RxBleGattCallback(callbackScheduler, bluetoothGattProvider2, (DisconnectionRouter) disconnectionRouter, (NativeCallbackDispatcher) nativeCallbackDispatcher);
    }
}
