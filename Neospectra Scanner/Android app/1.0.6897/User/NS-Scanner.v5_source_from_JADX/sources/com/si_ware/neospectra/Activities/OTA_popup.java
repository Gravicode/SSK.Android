package com.si_ware.neospectra.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.p004v7.app.AlertDialog.Builder;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.si_ware.neospectra.C1284R;
import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.Global.GlobalVariables.OTA_Functions;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class OTA_popup extends NavDrawerActivity implements OnClickListener {
    int action = 0;
    byte[] binaryOTAFileInBytes = null;
    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.bluetooth.device.action.FOUND".equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (device.getName() != null) {
                    String name = device.getName();
                    StringBuilder sb = new StringBuilder();
                    sb.append("NeoSpectra_Scaner_OTA_");
                    sb.append(GlobalVariables.gDeviceName);
                    if (name.equals(sb.toString())) {
                        OTA_popup.this.mmDevice = device;
                        try {
                            OTA_popup.this.openBT();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            } else if ("android.bluetooth.device.action.BOND_STATE_CHANGED".equals(action)) {
                try {
                    if (OTA_popup.this.mmDevice.getBondState() == 12) {
                        OTA_popup.this.openBT();
                        OTA_popup.this.unregisterReceiver(OTA_popup.this.blReceiver);
                    }
                } catch (IOException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    };
    BluetoothAdapter mBluetoothAdapter;
    Handler mHandler = new Handler();
    ProgressBar mProgressBar;
    TextView mTextViewTitle;
    ViewGroup mainView;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream = null;
    BluetoothSocket mmSocket;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1284R.layout.popup_ota);
        this.mProgressBar = (ProgressBar) findViewById(C1284R.C1286id.popupOTAprogressBar);
        this.mTextViewTitle = (TextView) findViewById(C1284R.C1286id.popupOTATitle);
        this.mainView = (ViewGroup) findViewById(C1284R.C1286id.popupOTA_mainView);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout((int) (((double) dm.widthPixels) * 0.85d), (int) (((double) dm.heightPixels) * 0.15d));
        this.action = getIntent().getIntExtra("Action", OTA_Functions.UPDATE_FIRMWARE.getValue());
        if (this.action == OTA_Functions.UPDATE_FIRMWARE.getValue()) {
            this.mTextViewTitle.setText("Firmware updating ...");
            this.mProgressBar.setProgress(0);
        } else if (this.action == OTA_Functions.RESET_FIRMWARE.getValue()) {
            this.mTextViewTitle.setText("Restore Default Firmware ...");
        }
        try {
            this.binaryOTAFileInBytes = fullyReadFileToBytes("image.bin");
            new Thread(new Runnable() {
                public void run() {
                    if (OTA_popup.this.action == OTA_Functions.UPDATE_FIRMWARE.getValue()) {
                        OTA_popup.this.startOTA();
                    } else if (OTA_popup.this.action == OTA_Functions.RESET_FIRMWARE.getValue()) {
                        OTA_popup.this.mProgressBar.setProgress(0);
                        OTA_popup.this.resetOTA();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        OTA_popup.this.mProgressBar.setProgress(100);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        OTA_popup.this.finish();
                    }
                }
            }).start();
            new Thread(new Runnable() {
                public void run() {
                    do {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } while (OTA_popup.this.mmOutputStream == null);
                    try {
                        OTA_popup.this.sendData();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            Builder myAlert = new Builder(this);
            myAlert.setTitle((CharSequence) "File not Found!");
            myAlert.setMessage((CharSequence) "Please make sure that firmware (\"image.bin\") is placed in Downloads Folder.");
            myAlert.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    OTA_popup.this.finish();
                }
            });
            myAlert.show();
        }
    }

    /* access modifiers changed from: private */
    public void startOTA() {
        byte[] otaStartCommand = {(byte) Integer.parseInt("44", 16), (byte) Integer.parseInt("FE", 16), (byte) Integer.parseInt("9D", 16), (byte) Integer.parseInt("A6", 16)};
        if (GlobalVariables.bluetoothAPI != null) {
            GlobalVariables.bluetoothAPI.sendOTAPacket(otaStartCommand);
        }
        discover();
    }

    private void endOTA() {
        try {
            this.mmOutputStream.write(new byte[]{(byte) Integer.parseInt("5F", 16), (byte) Integer.parseInt("83", 16), (byte) Integer.parseInt("F6", 16), (byte) Integer.parseInt("9E", 16)});
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /* access modifiers changed from: private */
    public void resetOTA() {
        byte[] otaResetCommand = {(byte) Integer.parseInt("E0", 16), (byte) Integer.parseInt("DA", 16), (byte) Integer.parseInt("D5", 16), (byte) Integer.parseInt("51", 16)};
        if (GlobalVariables.bluetoothAPI != null) {
            GlobalVariables.bluetoothAPI.sendOTAPacket(otaResetCommand);
        }
    }

    private void discover() {
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.mBluetoothAdapter == null) {
            System.out.println("No bluetooth adapter available");
        }
        if (!this.mBluetoothAdapter.isEnabled()) {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 0);
        }
        if (this.mBluetoothAdapter.isDiscovering()) {
            this.mBluetoothAdapter.cancelDiscovery();
            System.out.println("Discovery stopped");
        } else if (this.mBluetoothAdapter.isEnabled()) {
            this.mBluetoothAdapter.startDiscovery();
            System.out.println("Discovery started");
            registerReceiver(this.blReceiver, new IntentFilter("android.bluetooth.device.action.FOUND"));
        } else {
            System.out.println("Bluetooth not on");
        }
    }

    /* access modifiers changed from: private */
    public void openBT() throws IOException {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        if (this.mmDevice.getBondState() == 12) {
            this.mmSocket = this.mmDevice.createRfcommSocketToServiceRecord(uuid);
            this.mmSocket.connect();
            this.mmOutputStream = this.mmSocket.getOutputStream();
            System.out.println("Bluetooth Opened");
            return;
        }
        System.out.println("Bonding ......");
        this.mmDevice.createBond();
        registerReceiver(this.blReceiver, new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED"));
    }

    /* access modifiers changed from: private */
    public void sendData() throws IOException {
        try {
            int commandsCount = this.binaryOTAFileInBytes.length / GlobalVariables.OTA_BT_MAX_TRANSMISSION_UNIT;
            for (int i = 0; i < commandsCount; i++) {
                this.mmOutputStream.write(this.binaryOTAFileInBytes, i * GlobalVariables.OTA_BT_MAX_TRANSMISSION_UNIT, GlobalVariables.OTA_BT_MAX_TRANSMISSION_UNIT);
                Thread.sleep(20);
                final int progessvalue = (int) (((((double) i) * 1.0d) / ((double) commandsCount)) * 100.0d);
                this.mHandler.post(new Runnable() {
                    public void run() {
                        OTA_popup.this.mProgressBar.setProgress(progessvalue);
                    }
                });
            }
            if (this.binaryOTAFileInBytes.length % GlobalVariables.OTA_BT_MAX_TRANSMISSION_UNIT != 0) {
                this.mmOutputStream.write(this.binaryOTAFileInBytes, commandsCount * GlobalVariables.OTA_BT_MAX_TRANSMISSION_UNIT, this.binaryOTAFileInBytes.length % GlobalVariables.OTA_BT_MAX_TRANSMISSION_UNIT);
                Thread.sleep(20);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        endOTA();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        this.mProgressBar.setProgress(100);
        finish();
    }

    public static byte[] fullyReadFileToBytes(String fileName) throws IOException {
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        int size = (int) f.length();
        byte[] bytes = new byte[size];
        byte[] tmpBuff = new byte[size];
        FileInputStream fis = new FileInputStream(f);
        try {
            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    int read2 = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read2);
                    remain -= read2;
                }
            }
            fis.close();
            return bytes;
        } catch (IOException e) {
            throw e;
        } catch (Throwable th) {
            fis.close();
            throw th;
        }
    }

    private void endActivity() {
        GlobalVariables.bluetoothAPI = null;
        startActivity(new Intent(this, ConnectActivity.class));
    }

    public void onClick(View v) {
    }
}
