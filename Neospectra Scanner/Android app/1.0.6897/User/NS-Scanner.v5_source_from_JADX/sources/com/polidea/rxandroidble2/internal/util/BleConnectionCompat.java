package com.polidea.rxandroidble2.internal.util;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.content.Context;
import android.os.Build.VERSION;
import bleshadow.javax.inject.Inject;
import com.polidea.rxandroidble2.internal.RxBleLog;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BleConnectionCompat {
    private final Context context;

    @Inject
    public BleConnectionCompat(Context context2) {
        this.context = context2;
    }

    public BluetoothGatt connectGatt(BluetoothDevice remoteDevice, boolean autoConnect, BluetoothGattCallback bluetoothGattCallback) {
        if (remoteDevice == null) {
            return null;
        }
        if (VERSION.SDK_INT >= 24 || !autoConnect) {
            return connectGattCompat(bluetoothGattCallback, remoteDevice, autoConnect);
        }
        try {
            RxBleLog.m23v("Trying to connectGatt using reflection.", new Object[0]);
            Object iBluetoothGatt = getIBluetoothGatt(getIBluetoothManager());
            if (iBluetoothGatt == null) {
                RxBleLog.m25w("Couldn't get iBluetoothGatt object", new Object[0]);
                return connectGattCompat(bluetoothGattCallback, remoteDevice, true);
            }
            BluetoothGatt bluetoothGatt = createBluetoothGatt(iBluetoothGatt, remoteDevice);
            if (bluetoothGatt == null) {
                RxBleLog.m25w("Couldn't create BluetoothGatt object", new Object[0]);
                return connectGattCompat(bluetoothGattCallback, remoteDevice, true);
            }
            if (!connectUsingReflection(bluetoothGatt, bluetoothGattCallback, true)) {
                RxBleLog.m25w("Connection using reflection failed, closing gatt", new Object[0]);
                bluetoothGatt.close();
            }
            return bluetoothGatt;
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException exception) {
            RxBleLog.m26w(exception, "Error during reflection", new Object[0]);
            return connectGattCompat(bluetoothGattCallback, remoteDevice, true);
        }
    }

    private BluetoothGatt connectGattCompat(BluetoothGattCallback bluetoothGattCallback, BluetoothDevice device, boolean autoConnect) {
        RxBleLog.m23v("Connecting without reflection", new Object[0]);
        if (VERSION.SDK_INT >= 23) {
            return device.connectGatt(this.context, autoConnect, bluetoothGattCallback, 2);
        }
        return device.connectGatt(this.context, autoConnect, bluetoothGattCallback);
    }

    private boolean connectUsingReflection(BluetoothGatt bluetoothGatt, BluetoothGattCallback bluetoothGattCallback, boolean autoConnect) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        RxBleLog.m23v("Connecting using reflection", new Object[0]);
        setAutoConnectValue(bluetoothGatt, autoConnect);
        Method connectMethod = bluetoothGatt.getClass().getDeclaredMethod("connect", new Class[]{Boolean.class, BluetoothGattCallback.class});
        connectMethod.setAccessible(true);
        return ((Boolean) connectMethod.invoke(bluetoothGatt, new Object[]{Boolean.valueOf(true), bluetoothGattCallback})).booleanValue();
    }

    @TargetApi(23)
    private BluetoothGatt createBluetoothGatt(Object iBluetoothGatt, BluetoothDevice remoteDevice) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor bluetoothGattConstructor = BluetoothGatt.class.getDeclaredConstructors()[0];
        bluetoothGattConstructor.setAccessible(true);
        StringBuilder sb = new StringBuilder();
        sb.append("Found constructor with args count = ");
        sb.append(bluetoothGattConstructor.getParameterTypes().length);
        RxBleLog.m23v(sb.toString(), new Object[0]);
        if (bluetoothGattConstructor.getParameterTypes().length == 4) {
            return (BluetoothGatt) bluetoothGattConstructor.newInstance(new Object[]{this.context, iBluetoothGatt, remoteDevice, Integer.valueOf(2)});
        }
        return (BluetoothGatt) bluetoothGattConstructor.newInstance(new Object[]{this.context, iBluetoothGatt, remoteDevice});
    }

    private Object getIBluetoothGatt(Object iBluetoothManager) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (iBluetoothManager == null) {
            return null;
        }
        return getMethodFromClass(iBluetoothManager.getClass(), "getBluetoothGatt").invoke(iBluetoothManager, new Object[0]);
    }

    private Object getIBluetoothManager() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return null;
        }
        return getMethodFromClass(bluetoothAdapter.getClass(), "getBluetoothManager").invoke(bluetoothAdapter, new Object[0]);
    }

    private Method getMethodFromClass(Class<?> cls, String methodName) throws NoSuchMethodException {
        Method method = cls.getDeclaredMethod(methodName, new Class[0]);
        method.setAccessible(true);
        return method;
    }

    private void setAutoConnectValue(BluetoothGatt bluetoothGatt, boolean autoConnect) throws NoSuchFieldException, IllegalAccessException {
        Field autoConnectField = bluetoothGatt.getClass().getDeclaredField("mAutoConnect");
        autoConnectField.setAccessible(true);
        autoConnectField.setBoolean(bluetoothGatt, autoConnect);
    }
}
