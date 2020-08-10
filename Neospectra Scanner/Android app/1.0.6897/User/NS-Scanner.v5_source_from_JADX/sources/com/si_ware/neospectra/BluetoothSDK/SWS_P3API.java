package com.si_ware.neospectra.BluetoothSDK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.polidea.rxandroidble2.RxBleDevice;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3ConnectionServices.ConnectionStatus;
import com.si_ware.neospectra.C1284R;
import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.Global.GlobalVariables.apodization;
import com.si_ware.neospectra.Global.GlobalVariables.command;
import com.si_ware.neospectra.Global.GlobalVariables.opticalGain;
import com.si_ware.neospectra.Global.GlobalVariables.pointsCount;
import com.si_ware.neospectra.Global.GlobalVariables.runMode;
import com.si_ware.neospectra.Global.GlobalVariables.zeroPadding;
import com.si_ware.neospectra.Global.MethodsFactory;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import org.apache.poi.hssf.record.formula.MissingArgPtg;
import org.apache.poi.hssf.record.formula.NotEqualPtg;

public class SWS_P3API {
    private boolean InterpolationEnabled = false;
    private AsyncConnection_old asyncConnection;
    /* access modifiers changed from: private */
    public Thread connectionCheckThread;
    /* access modifiers changed from: private */
    public boolean isConnected = false;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public SWS_P3ConnectionServices mP3ConnectionServices;
    private SWS_P3Packet mP3Packet;

    class AsyncConnection extends AsyncTask<SWS_P3BLEDevice, Void, Boolean> {
        Button btnConnect;

        /* renamed from: pb */
        ProgressBar f827pb;
        boolean waitForConnectionResult = false;

        public AsyncConnection(ProgressBar progressBar, Button btn) {
            this.f827pb = progressBar;
            this.btnConnect = btn;
        }

        /* access modifiers changed from: protected */
        public Boolean doInBackground(SWS_P3BLEDevice... sws_p3BLEDevices) {
            this.waitForConnectionResult = true;
            GlobalVariables.currentConnectedDevice = sws_p3BLEDevices[0];
            SWS_P3API.this.mP3ConnectionServices.setmRxBleDevice(GlobalVariables.currentConnectedDevice.getDeviceInstance());
            SWS_P3API.this.mP3ConnectionServices.ConnectToP3();
            while (SWS_P3API.this.mP3ConnectionServices.getConnectionStatus() == ConnectionStatus.findingChannel) {
                SWS_P3API.this.isConnected = false;
                MethodsFactory.logMessage("bluetooth", "finding a channel");
            }
            if (SWS_P3API.this.mP3ConnectionServices.getConnectionStatus() == ConnectionStatus.failedToGetChannel) {
                MethodsFactory.logMessage("bluetooth", "Failed to get a channel to connect to the sensor");
                return Boolean.valueOf(false);
            }
            if (SWS_P3API.this.mP3ConnectionServices.getConnectionStatus() == ConnectionStatus.gotChannel) {
                SWS_P3API.this.mP3ConnectionServices.setConnectionStatus(ConnectionStatus.connecting);
            }
            SWS_P3API.this.isConnected = SWS_P3API.this.getCurrentConnectionStatus();
            while (SWS_P3API.this.mP3ConnectionServices.getConnectionStatus() != ConnectionStatus.connected) {
                MethodsFactory.logMessage("bluetooth", "Connecting ...");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Connecting result equals: ");
            sb.append(SWS_P3API.this.isConnected);
            MethodsFactory.logMessage("bluetooth", sb.toString());
            GlobalVariables.bluetoothAPI.setNotifications();
            SWS_P3API.this.isConnected = SWS_P3API.this.getCurrentConnectionStatus();
            return Boolean.valueOf(SWS_P3API.this.isConnected);
        }

        private void setCurrentDeviceNull() {
            GlobalVariables.currentConnectedDevice = null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (!aBoolean.booleanValue()) {
                MethodsFactory.logMessage("bluetooth", "Device failed to connect");
                return;
            }
            MethodsFactory.logMessage("bluetooth", "Device is connected successfully");
            this.f827pb.setVisibility(4);
            this.btnConnect.setText(SWS_P3API.this.mContext.getResources().getString(C1284R.string.disconnect));
            this.waitForConnectionResult = false;
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            super.onCancelled();
            MethodsFactory.logMessage("bluetooth", "AsyncTask has cancelled");
            setCurrentDeviceNull();
            SWS_P3API.this.disconnectFromDevice();
        }
    }

    class AsyncConnection_old extends AsyncTask<SWS_P3BLEDevice, Void, Boolean> {
        Button btnConnect;
        TextView devicename;

        /* renamed from: pb */
        ProgressBar f828pb;
        SWS_P3BLEDevice swsDevice;

        public AsyncConnection_old(SWS_P3BLEDevice swsDevice2, ProgressBar progressBar, Button btn, TextView devicenam) {
            this.f828pb = progressBar;
            this.btnConnect = btn;
            this.devicename = devicenam;
            this.swsDevice = swsDevice2;
        }

        /* access modifiers changed from: protected */
        public Boolean doInBackground(SWS_P3BLEDevice... sws_p3BLEDevices) {
            GlobalVariables.currentConnectedDevice = sws_p3BLEDevices[0];
            RxBleDevice myDevice = sws_p3BLEDevices[0].getDeviceInstance();
            if (SWS_P3API.this.mP3ConnectionServices.getmRxBleDevice() != myDevice) {
                SWS_P3API.this.mP3ConnectionServices.setmRxBleDevice(myDevice);
                SWS_P3API.this.mP3ConnectionServices.ConnectToP3();
                while (!SWS_P3API.this.getCurrentConnectionStatus()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Connecting ..., isConnected equals: ");
                    sb.append(SWS_P3API.this.getCurrentConnectionStatus());
                    MethodsFactory.logMessage("bluetooth", sb.toString());
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Connecting result equals: ");
                sb2.append(SWS_P3API.this.isConnected);
                MethodsFactory.logMessage("bluetooth", sb2.toString());
                GlobalVariables.bluetoothAPI.setNotifications();
                SWS_P3API.this.connectionCheckThread.start();
            }
            return Boolean.valueOf(SWS_P3API.this.getCurrentConnectionStatus());
        }

        private void setCurrentDeviceNull() {
            GlobalVariables.currentConnectedDevice = null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (!aBoolean.booleanValue()) {
                MethodsFactory.logMessage("bluetooth", "Device failed to connect");
                return;
            }
            MethodsFactory.logMessage("bluetooth", "Device is connected successfully");
            this.f828pb.setVisibility(4);
            this.btnConnect.setText(SWS_P3API.this.mContext.getResources().getString(C1284R.string.disconnect));
            this.swsDevice.connected = true;
            this.devicename.setTypeface(Typeface.DEFAULT_BOLD);
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            super.onCancelled();
            MethodsFactory.logMessage("bluetooth", "AsyncTask has cancelled");
        }
    }

    public SWS_P3ConnectionServices getmP3ConnectionServices() {
        return this.mP3ConnectionServices;
    }

    public void setmP3ConnectionServices(SWS_P3ConnectionServices mP3ConnectionServices2) {
        this.mP3ConnectionServices = mP3ConnectionServices2;
    }

    public SWS_P3Packet getmP3Packet() {
        return this.mP3Packet;
    }

    public void setmP3Packet(SWS_P3Packet mP3Packet2) {
        this.mP3Packet = mP3Packet2;
    }

    public boolean isInterpolationEnabled() {
        return this.InterpolationEnabled;
    }

    public void setInterpolationEnabled(boolean interpolationEnabled) {
        this.InterpolationEnabled = interpolationEnabled;
    }

    public SWS_P3API(Activity mActivity, Context pContext) {
        this.mContext = pContext;
        this.mP3ConnectionServices = new SWS_P3ConnectionServices(mActivity, pContext);
        this.connectionCheckThread = new Thread(new Runnable() {
            public void run() {
                do {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (SWS_P3API.this.mP3ConnectionServices == null) {
                        return;
                    }
                } while (SWS_P3API.this.mP3ConnectionServices.isConnected());
                SWS_P3API.this.mP3ConnectionServices.broadcastdisconnectionNotification();
            }
        });
    }

    @Deprecated
    public boolean connectToDevice(RxBleDevice mRxBleDevice) {
        this.mP3ConnectionServices.setmRxBleDevice(mRxBleDevice);
        this.mP3ConnectionServices.ConnectToP3();
        return this.mP3ConnectionServices.isConnected();
    }

    public boolean connectToDevice(SWS_P3BLEDevice swsDevice, ProgressBar progressBar, Button btn, TextView devicenam) {
        AsyncConnection_old asyncConnection_old = new AsyncConnection_old(swsDevice, progressBar, btn, devicenam);
        this.asyncConnection = asyncConnection_old;
        this.asyncConnection.execute(new SWS_P3BLEDevice[]{swsDevice});
        return getCurrentConnectionStatus();
    }

    public boolean isDeviceConnected() {
        return this.mP3ConnectionServices.isConnected();
    }

    public boolean disconnectFromDevice() {
        this.mP3ConnectionServices.DisconnectFromP3();
        if (this.asyncConnection != null) {
            this.asyncConnection.cancel(true);
        }
        if (GlobalVariables.currentConnectedDevice != null) {
            GlobalVariables.currentConnectedDevice = null;
        }
        MethodsFactory.logMessage("bluetooth", "The device has been disconnected");
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0037  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003d  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0049  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean sendPacket(byte[] r4, boolean r5, java.lang.String r6) {
        /*
            r3 = this;
            int r0 = r6.hashCode()
            r1 = -1847397738(0xffffffff91e2f296, float:-3.5806018E-28)
            r2 = 1
            if (r0 == r1) goto L_0x0029
            r1 = -1237880488(0xffffffffb6377158, float:-2.73351E-6)
            if (r0 == r1) goto L_0x001f
            r1 = -1067318430(0xffffffffc0620362, float:-3.5314565)
            if (r0 == r1) goto L_0x0015
            goto L_0x0033
        L_0x0015:
            java.lang.String r0 = "Battery Service"
            boolean r0 = r6.equals(r0)
            if (r0 == 0) goto L_0x0033
            r0 = 2
            goto L_0x0034
        L_0x001f:
            java.lang.String r0 = "P3 Service"
            boolean r0 = r6.equals(r0)
            if (r0 == 0) goto L_0x0033
            r0 = 0
            goto L_0x0034
        L_0x0029:
            java.lang.String r0 = "Memory Service"
            boolean r0 = r6.equals(r0)
            if (r0 == 0) goto L_0x0033
            r0 = 1
            goto L_0x0034
        L_0x0033:
            r0 = -1
        L_0x0034:
            switch(r0) {
                case 0: goto L_0x0049;
                case 1: goto L_0x0043;
                case 2: goto L_0x003d;
                default: goto L_0x0037;
            }
        L_0x0037:
            com.si_ware.neospectra.BluetoothSDK.SWS_P3ConnectionServices r0 = r3.mP3ConnectionServices
            r0.WriteToP3(r4)
            goto L_0x004f
        L_0x003d:
            com.si_ware.neospectra.BluetoothSDK.SWS_P3ConnectionServices r0 = r3.mP3ConnectionServices
            r0.WriteToSystemService(r4)
            goto L_0x004f
        L_0x0043:
            com.si_ware.neospectra.BluetoothSDK.SWS_P3ConnectionServices r0 = r3.mP3ConnectionServices
            r0.WriteToMemoryService(r4)
            goto L_0x004f
        L_0x0049:
            com.si_ware.neospectra.BluetoothSDK.SWS_P3ConnectionServices r0 = r3.mP3ConnectionServices
            r0.WriteToP3(r4)
        L_0x004f:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.si_ware.neospectra.BluetoothSDK.SWS_P3API.sendPacket(byte[], boolean, java.lang.String):boolean");
    }

    public boolean sendPacket(byte[] mPacketContent, boolean mInterpolationStatus) {
        this.mP3ConnectionServices.WriteToP3(mPacketContent);
        return true;
    }

    public boolean sendOTAPacket(byte[] packetStream) {
        this.mP3ConnectionServices.WriteOTAService(packetStream);
        return true;
    }

    public boolean setSourceSettings() {
        byte[] packetStream = new byte[20];
        for (int i = 0; i < 20; i++) {
            packetStream[i] = 0;
        }
        packetStream[0] = MissingArgPtg.sid;
        packetStream[1] = 2;
        packetStream[2] = 0;
        packetStream[3] = 0;
        packetStream[4] = 0;
        packetStream[5] = NotEqualPtg.sid;
        packetStream[6] = 2;
        packetStream[7] = 0;
        packetStream[8] = 0;
        packetStream[9] = 5;
        packetStream[10] = 35;
        packetStream[11] = 10;
        this.mP3ConnectionServices.WriteToP3(packetStream);
        return true;
    }

    public boolean setOpticalSettings() {
        byte[] packetStream = new byte[20];
        for (int i = 0; i < 20; i++) {
            packetStream[i] = 0;
        }
        packetStream[0] = 27;
        int gain = GlobalVariables.gOpticalGainValue;
        packetStream[1] = (byte) (gain & 255);
        packetStream[2] = (byte) ((gain >> 8) & 255);
        this.mP3ConnectionServices.WriteToP3(packetStream);
        return true;
    }

    public void setNotifications() {
        this.mP3ConnectionServices.SetNotificationOnTXInP3();
        this.mP3ConnectionServices.SetNotificationOnMemTx();
        this.mP3ConnectionServices.SetNotificationOnBatTx();
    }

    public void getDVKReading() {
        this.mP3ConnectionServices.ReadFromP3();
        MethodsFactory.logMessage("bluetooth", "testing");
    }

    public ConnectionStatus getConnectionStatus() {
        return this.mP3ConnectionServices.getConnectionStatus();
    }

    public boolean getCurrentConnectionStatus() {
        return this.mP3ConnectionServices.isConnected();
    }

    public List<SWS_P3BLEDevice> scanAvailableDevices() {
        return this.mP3ConnectionServices.ScanBTDevices();
    }

    public boolean isBluetoothEnabled() {
        return this.mP3ConnectionServices.isBluetoothEnabled();
    }

    public void disableBluetooth() {
        this.mP3ConnectionServices.disableBluetooth();
    }

    public Intent enableBluetooth() {
        return this.mP3ConnectionServices.enableBluetooth();
    }

    public void getLocationPermissions() {
        MethodsFactory.logMessage("bluetooth", "getLocationPermissions()");
        this.mP3ConnectionServices.askForLocationPermissions();
    }

    public boolean isLocationEnabled(@NonNull Context mContext2) {
        if (VERSION.SDK_INT < 19) {
            return true ^ TextUtils.isEmpty(Secure.getString(mContext2.getContentResolver(), "location_providers_allowed"));
        }
        boolean z = false;
        try {
            if (Secure.getInt(mContext2.getContentResolver(), "location_mode") != 0) {
                z = true;
            }
            return z;
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendDefaultPacket() {
        SWS_P3Packet newPacket = new SWS_P3Packet();
        newPacket.setSWS_P3Packet_Command(command.Run_PSD.toString());
        newPacket.setSWS_P3Packet_ScanTime("2000");
        newPacket.setSWS_P3Packet_PointsCount(pointsCount.disable.toString());
        newPacket.setSWS_P3Packet_OpticalGain(opticalGain.UseSettingsSavedonDVK.toString());
        newPacket.setSWS_P3Packet_Apodization(apodization.Boxcar.toString());
        newPacket.setSWS_P3Packet_ZeroPadding(zeroPadding.points_32k.toString());
        newPacket.setSWS_P3Packet_RunMode(runMode.Single_Mode.toString());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Command());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ScanTime());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_PointsCount());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_OpticalGain());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Apodization());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ZeroPadding());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_RunMode());
        newPacket.preparePacketContent();
        GlobalVariables.bluetoothAPI.sendPacket(newPacket.getPacketStream(), newPacket.isInterpolationMode());
    }

    public void sendDefaultPacket(int scanTime) {
        SWS_P3Packet newPacket = new SWS_P3Packet();
        newPacket.setSWS_P3Packet_Command(command.Run_Absorbance.toString());
        newPacket.setSWS_P3Packet_ScanTime(Integer.toString(scanTime * 1000));
        if (GlobalVariables.gIsInterpolationEnabled) {
            newPacket.setSWS_P3Packet_PointsCount(GlobalVariables.gInterpolationPoints);
        } else {
            newPacket.setSWS_P3Packet_PointsCount(pointsCount.disable.toString());
        }
        if (GlobalVariables.gOpticalGainSettings == null || GlobalVariables.gOpticalGainSettings.equals("") || GlobalVariables.gOpticalGainSettings.equals("Default")) {
            newPacket.setSWS_P3Packet_OpticalGain(opticalGain.UseSettingsSavedonDVK.toString());
        } else {
            newPacket.setSWS_P3Packet_OpticalGain(opticalGain.UseExternalSettings.toString());
        }
        if (GlobalVariables.gApodizationFunction == null) {
            newPacket.setSWS_P3Packet_Apodization(apodization.Boxcar.toString());
        } else {
            newPacket.setSWS_P3Packet_Apodization(GlobalVariables.gApodizationFunction);
        }
        if (GlobalVariables.gFftPoints == null) {
            newPacket.setSWS_P3Packet_ZeroPadding(zeroPadding.points_8k.toString());
        } else {
            newPacket.setSWS_P3Packet_ZeroPadding(GlobalVariables.gFftPoints);
        }
        if (GlobalVariables.gRunMode == null) {
            newPacket.setSWS_P3Packet_RunMode(runMode.Single_Mode.toString());
        } else {
            newPacket.setSWS_P3Packet_RunMode(GlobalVariables.gRunMode);
        }
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Command());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ScanTime());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_PointsCount());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_OpticalGain());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Apodization());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ZeroPadding());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_RunMode());
        newPacket.preparePacketContent();
        GlobalVariables.bluetoothAPI.sendPacket(newPacket.getPacketStream(), newPacket.isInterpolationMode());
    }

    public void sendBackgroundPacket() {
        SWS_P3Packet newPacket = new SWS_P3Packet();
        newPacket.setSWS_P3Packet_Command(command.Run_Background.toString());
        newPacket.setSWS_P3Packet_ScanTime("2000");
        newPacket.setSWS_P3Packet_PointsCount(pointsCount.disable.toString());
        newPacket.setSWS_P3Packet_OpticalGain(opticalGain.UseSettingsSavedonDVK.toString());
        newPacket.setSWS_P3Packet_Apodization(apodization.Boxcar.toString());
        newPacket.setSWS_P3Packet_ZeroPadding(zeroPadding.points_32k.toString());
        newPacket.setSWS_P3Packet_RunMode(runMode.Single_Mode.toString());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Command());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ScanTime());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_PointsCount());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_OpticalGain());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Apodization());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ZeroPadding());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_RunMode());
        newPacket.preparePacketContent();
        GlobalVariables.bluetoothAPI.sendPacket(newPacket.getPacketStream(), newPacket.isInterpolationMode());
    }

    public void sendBackgroundPacket(int scanTime) {
        SWS_P3Packet newPacket = new SWS_P3Packet();
        newPacket.setSWS_P3Packet_Command(command.Run_Background.toString());
        newPacket.setSWS_P3Packet_ScanTime(Integer.toString(scanTime * 1000));
        if (GlobalVariables.gIsInterpolationEnabled) {
            newPacket.setSWS_P3Packet_PointsCount(GlobalVariables.gInterpolationPoints);
        } else {
            newPacket.setSWS_P3Packet_PointsCount(pointsCount.disable.toString());
        }
        if (GlobalVariables.gOpticalGainSettings == null || GlobalVariables.gOpticalGainSettings.equals("") || GlobalVariables.gOpticalGainSettings.equals("Default")) {
            newPacket.setSWS_P3Packet_OpticalGain(opticalGain.UseSettingsSavedonDVK.toString());
        } else {
            newPacket.setSWS_P3Packet_OpticalGain(opticalGain.UseExternalSettings.toString());
        }
        if (GlobalVariables.gApodizationFunction == null) {
            newPacket.setSWS_P3Packet_Apodization(apodization.Boxcar.toString());
        } else {
            newPacket.setSWS_P3Packet_Apodization(GlobalVariables.gApodizationFunction);
        }
        if (GlobalVariables.gFftPoints == null) {
            newPacket.setSWS_P3Packet_ZeroPadding(zeroPadding.points_8k.toString());
        } else {
            newPacket.setSWS_P3Packet_ZeroPadding(GlobalVariables.gFftPoints);
        }
        if (GlobalVariables.gRunMode == null) {
            newPacket.setSWS_P3Packet_RunMode(runMode.Single_Mode.toString());
        } else {
            newPacket.setSWS_P3Packet_RunMode(GlobalVariables.gRunMode);
        }
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Command());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ScanTime());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_PointsCount());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_OpticalGain());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Apodization());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ZeroPadding());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_RunMode());
        newPacket.preparePacketContent();
        GlobalVariables.bluetoothAPI.sendPacket(newPacket.getPacketStream(), newPacket.isInterpolationMode());
    }

    public void sendSelfCalibrationPacket(int scanTime) {
        SWS_P3Packet newPacket = new SWS_P3Packet();
        newPacket.setSWS_P3Packet_Command(command.Run_SelfCorrection.toString());
        newPacket.setSWS_P3Packet_ScanTime(Integer.toString(scanTime * 1000));
        if (GlobalVariables.gIsInterpolationEnabled) {
            newPacket.setSWS_P3Packet_PointsCount(GlobalVariables.gInterpolationPoints);
        } else {
            newPacket.setSWS_P3Packet_PointsCount(pointsCount.disable.toString());
        }
        if (GlobalVariables.gOpticalGainSettings == null || GlobalVariables.gOpticalGainSettings.equals("") || GlobalVariables.gOpticalGainSettings.equals("Default")) {
            newPacket.setSWS_P3Packet_OpticalGain(opticalGain.UseSettingsSavedonDVK.toString());
        } else {
            newPacket.setSWS_P3Packet_OpticalGain(opticalGain.UseExternalSettings.toString());
        }
        if (GlobalVariables.gApodizationFunction == null) {
            newPacket.setSWS_P3Packet_Apodization(apodization.Boxcar.toString());
        } else {
            newPacket.setSWS_P3Packet_Apodization(GlobalVariables.gApodizationFunction);
        }
        if (GlobalVariables.gFftPoints == null) {
            newPacket.setSWS_P3Packet_ZeroPadding(zeroPadding.points_8k.toString());
        } else {
            newPacket.setSWS_P3Packet_ZeroPadding(GlobalVariables.gFftPoints);
        }
        newPacket.setSWS_P3Packet_RunMode(runMode.Single_Mode.toString());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Command());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ScanTime());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_PointsCount());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_OpticalGain());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Apodization());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ZeroPadding());
        newPacket.preparePacketContent();
        GlobalVariables.bluetoothAPI.sendPacket(newPacket.getPacketStream(), newPacket.isInterpolationMode());
    }

    public void sendRestoreToDefaultPacket() {
        SWS_P3Packet newPacket = new SWS_P3Packet();
        newPacket.setSWS_P3Packet_Command(command.Restore_Defaults.toString());
        newPacket.setSWS_P3Packet_ScanTime(Integer.toString(2000));
        if (GlobalVariables.gIsInterpolationEnabled) {
            newPacket.setSWS_P3Packet_PointsCount(GlobalVariables.gInterpolationPoints);
        } else {
            newPacket.setSWS_P3Packet_PointsCount(pointsCount.disable.toString());
        }
        if (GlobalVariables.gOpticalGainSettings == null || GlobalVariables.gOpticalGainSettings.equals("") || GlobalVariables.gOpticalGainSettings.equals("Default")) {
            newPacket.setSWS_P3Packet_OpticalGain(opticalGain.UseSettingsSavedonDVK.toString());
        } else {
            newPacket.setSWS_P3Packet_OpticalGain(opticalGain.UseExternalSettings.toString());
        }
        if (GlobalVariables.gApodizationFunction == null) {
            newPacket.setSWS_P3Packet_Apodization(apodization.Boxcar.toString());
        } else {
            newPacket.setSWS_P3Packet_Apodization(GlobalVariables.gApodizationFunction);
        }
        if (GlobalVariables.gFftPoints == null) {
            newPacket.setSWS_P3Packet_ZeroPadding(zeroPadding.points_8k.toString());
        } else {
            newPacket.setSWS_P3Packet_ZeroPadding(GlobalVariables.gFftPoints);
        }
        if (GlobalVariables.gRunMode == null) {
            newPacket.setSWS_P3Packet_RunMode(runMode.Single_Mode.toString());
        } else {
            newPacket.setSWS_P3Packet_RunMode(GlobalVariables.gRunMode);
        }
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Command());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ScanTime());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_PointsCount());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_OpticalGain());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Apodization());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ZeroPadding());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_RunMode());
        newPacket.preparePacketContent();
        GlobalVariables.bluetoothAPI.sendPacket(newPacket.getPacketStream(), newPacket.isInterpolationMode());
    }

    public void sendStoreGainPacket() {
        SWS_P3Packet newPacket = new SWS_P3Packet();
        newPacket.setSWS_P3Packet_Command(command.Burn_Gain.toString());
        newPacket.setSWS_P3Packet_ScanTime(Integer.toString(2000));
        if (GlobalVariables.gIsInterpolationEnabled) {
            newPacket.setSWS_P3Packet_PointsCount(GlobalVariables.gInterpolationPoints);
        } else {
            newPacket.setSWS_P3Packet_PointsCount(pointsCount.disable.toString());
        }
        if (GlobalVariables.gOpticalGainSettings == null || GlobalVariables.gOpticalGainSettings.equals("") || GlobalVariables.gOpticalGainSettings.equals("Default")) {
            newPacket.setSWS_P3Packet_OpticalGain(opticalGain.UseSettingsSavedonDVK.toString());
        } else {
            newPacket.setSWS_P3Packet_OpticalGain(opticalGain.UseExternalSettings.toString());
        }
        if (GlobalVariables.gApodizationFunction == null) {
            newPacket.setSWS_P3Packet_Apodization(apodization.Boxcar.toString());
        } else {
            newPacket.setSWS_P3Packet_Apodization(GlobalVariables.gApodizationFunction);
        }
        if (GlobalVariables.gFftPoints == null) {
            newPacket.setSWS_P3Packet_ZeroPadding(zeroPadding.points_8k.toString());
        } else {
            newPacket.setSWS_P3Packet_ZeroPadding(GlobalVariables.gFftPoints);
        }
        if (GlobalVariables.gRunMode == null) {
            newPacket.setSWS_P3Packet_RunMode(runMode.Single_Mode.toString());
        } else {
            newPacket.setSWS_P3Packet_RunMode(GlobalVariables.gRunMode);
        }
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Command());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ScanTime());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_PointsCount());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_OpticalGain());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Apodization());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ZeroPadding());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_RunMode());
        newPacket.preparePacketContent();
        GlobalVariables.bluetoothAPI.sendPacket(newPacket.getPacketStream(), newPacket.isInterpolationMode());
    }

    public void sendStoreSelfCorrectionPacket() {
        SWS_P3Packet newPacket = new SWS_P3Packet();
        newPacket.setSWS_P3Packet_Command(command.Burn_Self.toString());
        newPacket.setSWS_P3Packet_ScanTime(Integer.toString(2000));
        if (GlobalVariables.gIsInterpolationEnabled) {
            newPacket.setSWS_P3Packet_PointsCount(GlobalVariables.gInterpolationPoints);
        } else {
            newPacket.setSWS_P3Packet_PointsCount(pointsCount.disable.toString());
        }
        if (GlobalVariables.gOpticalGainSettings == null || GlobalVariables.gOpticalGainSettings.equals("") || GlobalVariables.gOpticalGainSettings.equals("Default")) {
            newPacket.setSWS_P3Packet_OpticalGain(opticalGain.UseSettingsSavedonDVK.toString());
        } else {
            newPacket.setSWS_P3Packet_OpticalGain(opticalGain.UseExternalSettings.toString());
        }
        if (GlobalVariables.gApodizationFunction == null) {
            newPacket.setSWS_P3Packet_Apodization(apodization.Boxcar.toString());
        } else {
            newPacket.setSWS_P3Packet_Apodization(GlobalVariables.gApodizationFunction);
        }
        if (GlobalVariables.gFftPoints == null) {
            newPacket.setSWS_P3Packet_ZeroPadding(zeroPadding.points_8k.toString());
        } else {
            newPacket.setSWS_P3Packet_ZeroPadding(GlobalVariables.gFftPoints);
        }
        if (GlobalVariables.gRunMode == null) {
            newPacket.setSWS_P3Packet_RunMode(runMode.Single_Mode.toString());
        } else {
            newPacket.setSWS_P3Packet_RunMode(GlobalVariables.gRunMode);
        }
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Command());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ScanTime());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_PointsCount());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_OpticalGain());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Apodization());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ZeroPadding());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_RunMode());
        newPacket.preparePacketContent();
        GlobalVariables.bluetoothAPI.sendPacket(newPacket.getPacketStream(), newPacket.isInterpolationMode());
    }

    public void sendGainAdjustmentPacket() {
        SWS_P3Packet newPacket = new SWS_P3Packet();
        newPacket.setSWS_P3Packet_Command(command.Run_GainAdjustment.toString());
        newPacket.setSWS_P3Packet_ScanTime("2000");
        newPacket.setSWS_P3Packet_PointsCount(pointsCount.disable.toString());
        newPacket.setSWS_P3Packet_OpticalGain(opticalGain.UseSettingsSavedonDVK.toString());
        newPacket.setSWS_P3Packet_Apodization(apodization.Boxcar.toString());
        newPacket.setSWS_P3Packet_ZeroPadding(zeroPadding.points_32k.toString());
        newPacket.setSWS_P3Packet_RunMode(runMode.Single_Mode.toString());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Command());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ScanTime());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_PointsCount());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_OpticalGain());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Apodization());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ZeroPadding());
        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_RunMode());
        newPacket.preparePacketContent();
        GlobalVariables.bluetoothAPI.sendPacket(newPacket.getPacketStream(), newPacket.isInterpolationMode());
    }

    public void setScannerID(long value) {
        ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(35);
        buffer.putLong(value);
        this.mP3ConnectionServices.WriteToMemoryService(buffer.array());
    }

    public void sendP3_ID_Request() {
        this.mP3ConnectionServices.WriteToSystemService(new byte[]{Byte.MIN_VALUE});
    }

    public void sendTemperatureRequest() {
        this.mP3ConnectionServices.WriteToSystemService(new byte[]{-127});
    }

    public void sendMemoryRequest() {
        this.mP3ConnectionServices.WriteToMemoryService(new byte[]{0});
    }

    public void sendScansHistoryRequest(int fileNum) {
        this.mP3ConnectionServices.WriteToMemoryService(new byte[]{1, (byte) fileNum});
    }

    public void sendClearMemoryRequest() {
        this.mP3ConnectionServices.WriteToMemoryService(new byte[]{2});
    }

    public void sendBatRequest() {
        this.mP3ConnectionServices.WriteToSystemService(new byte[]{0});
    }

    public void setHomeContext(Context context) {
        this.mP3ConnectionServices.setHomeActivityContext(context);
    }
}
