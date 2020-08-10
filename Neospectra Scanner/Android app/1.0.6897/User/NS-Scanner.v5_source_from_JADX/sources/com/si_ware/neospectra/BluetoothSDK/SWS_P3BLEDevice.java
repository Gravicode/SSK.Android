package com.si_ware.neospectra.BluetoothSDK;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import com.polidea.rxandroidble2.RxBleDevice;

public class SWS_P3BLEDevice {
    private RxBleDevice DeviceInstance;
    private String DeviceMacAddress;
    private String DeviceName;
    private int DeviceRSSI;
    public boolean connected = false;
    private BluetoothDevice gattDeviceInstance = null;
    private boolean isConnectedToGatt = false;
    private BluetoothGatt myGatBand = null;

    SWS_P3BLEDevice(String mName, String mMacAddress, int mRSSI, RxBleDevice mInstance) {
        this.DeviceName = mName;
        this.DeviceMacAddress = mMacAddress;
        this.DeviceRSSI = mRSSI;
        this.DeviceInstance = mInstance;
    }

    SWS_P3BLEDevice(String mName, String mMacAddress, int mRSSI, BluetoothDevice mInstance) {
        this.DeviceName = mName;
        this.DeviceMacAddress = mMacAddress;
        this.DeviceRSSI = mRSSI;
        this.gattDeviceInstance = mInstance;
        this.isConnectedToGatt = false;
    }

    public String getDeviceName() {
        return this.DeviceName;
    }

    public void setDeviceName(String deviceName) {
        this.DeviceName = deviceName;
    }

    public String getDeviceMacAddress() {
        return this.DeviceMacAddress;
    }

    public void setDeviceMacAddress(String deviceMacAddress) {
        this.DeviceMacAddress = deviceMacAddress;
    }

    public int getDeviceRSSI() {
        return this.DeviceRSSI;
    }

    public void setDeviceRSSI(int deviceRSSI) {
        this.DeviceRSSI = deviceRSSI;
    }

    public RxBleDevice getDeviceInstance() {
        return this.DeviceInstance;
    }

    public void setDeviceInstance(RxBleDevice deviceInstance) {
        this.DeviceInstance = deviceInstance;
    }

    public BluetoothDevice getGattDeviceInstance() {
        return this.gattDeviceInstance;
    }

    public void setGattDeviceInstance(BluetoothDevice gattDeviceInstance2) {
        this.gattDeviceInstance = gattDeviceInstance2;
    }

    public BluetoothGatt getMyGatBand() {
        return this.myGatBand;
    }

    public void setMyGatBand(BluetoothGatt myGatBand2) {
        this.myGatBand = myGatBand2;
    }

    public boolean isConnectedToGatt() {
        return this.isConnectedToGatt;
    }

    public void setConnectedToGatt(boolean connectedToGatt) {
        this.isConnectedToGatt = connectedToGatt;
    }
}
