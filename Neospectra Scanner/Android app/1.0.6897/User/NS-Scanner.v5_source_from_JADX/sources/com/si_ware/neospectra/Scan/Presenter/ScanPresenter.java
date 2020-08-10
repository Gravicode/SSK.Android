package com.si_ware.neospectra.Scan.Presenter;

import com.si_ware.neospectra.BluetoothSDK.SWS_P3Packet;
import com.si_ware.neospectra.Global.GlobalVariables;

public class ScanPresenter {
    private String TAG = getClass().getSimpleName();

    public void requestSensorReading(int scanTime) {
        GlobalVariables.bluetoothAPI.setSourceSettings();
        GlobalVariables.bluetoothAPI.setOpticalSettings();
        GlobalVariables.bluetoothAPI.sendDefaultPacket(scanTime);
    }

    public void requestBackgroundReading(int scanTime) {
        GlobalVariables.bluetoothAPI.setSourceSettings();
        GlobalVariables.bluetoothAPI.setOpticalSettings();
        GlobalVariables.bluetoothAPI.sendBackgroundPacket(scanTime);
    }

    public void requestSelfCalibration(int scanTime) {
        GlobalVariables.bluetoothAPI.setSourceSettings();
        GlobalVariables.bluetoothAPI.setOpticalSettings();
        GlobalVariables.bluetoothAPI.sendSelfCalibrationPacket(scanTime);
    }

    public void restoreToDefaultSettings() {
        GlobalVariables.bluetoothAPI.sendRestoreToDefaultPacket();
    }

    public void storingSettings() {
        GlobalVariables.bluetoothAPI.sendStoreGainPacket();
        GlobalVariables.bluetoothAPI.sendStoreSelfCorrectionPacket();
    }

    public void requestGainReading() {
        GlobalVariables.bluetoothAPI.sendGainAdjustmentPacket();
    }

    public void sendCustomPacket(SWS_P3Packet newPacket, String BLE_Service) {
        GlobalVariables.bluetoothAPI.sendPacket(newPacket.getPacketStream(), newPacket.isInterpolationMode(), BLE_Service);
    }

    public void sendOTACommand(SWS_P3Packet newPacket) {
        GlobalVariables.bluetoothAPI.sendOTAPacket(newPacket.getPacketStream());
    }
}
