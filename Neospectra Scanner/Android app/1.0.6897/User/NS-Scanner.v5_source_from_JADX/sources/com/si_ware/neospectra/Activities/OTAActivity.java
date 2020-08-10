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
import android.support.annotation.NonNull;
import android.support.p001v4.content.ContextCompat;
import android.support.p001v4.widget.DrawerLayout;
import android.support.p004v7.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3Packet;
import com.si_ware.neospectra.C1284R;
import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.Scan.Presenter.ScanPresenter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class OTAActivity extends NavDrawerActivity implements OnClickListener {
    byte[] binaryOTAFileInBytes;
    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.bluetooth.device.action.FOUND".equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (device.getName() != null && device.getName().equals("NeoSpectra_Scaner_SPP_OTA")) {
                    OTAActivity.this.mmDevice = device;
                    try {
                        OTAActivity.this.openBT();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            } else if ("android.bluetooth.device.action.BOND_STATE_CHANGED".equals(action)) {
                try {
                    if (OTAActivity.this.mmDevice.getBondState() == 12) {
                        OTAActivity.this.openBT();
                        OTAActivity.this.unregisterReceiver(OTAActivity.this.blReceiver);
                    }
                } catch (IOException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    };
    DrawerLayout drawer;
    BluetoothAdapter mBluetoothAdapter;
    Context mContext;
    TextView mDisconnectDevice;
    Button mEndOTA;
    Button mResetOTA;
    Button mSendFile;
    Button mStartOTA;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    BluetoothSocket mmSocket;
    ScanPresenter scanPresenter;
    TextView tv_device_name;
    TextView tv_firmware_version;
    TextView tv_memory;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = ((LayoutInflater) getSystemService("layout_inflater")).inflate(C1284R.layout.activity_ota, null, false);
        if (GlobalVariables.gDeviceName.indexOf("_") != -1) {
            GlobalVariables.gDeviceName = GlobalVariables.gDeviceName.substring(GlobalVariables.gDeviceName.indexOf("_") + 1);
        }
        this.mContext = this;
        this.scanPresenter = new ScanPresenter();
        this.drawer = (DrawerLayout) findViewById(C1284R.C1286id.drawer_layout);
        this.drawer.addView(contentView, 0);
        this.mStartOTA = (Button) findViewById(C1284R.C1286id.btn_ota_start);
        this.mSendFile = (Button) findViewById(C1284R.C1286id.btn_ota_send);
        this.mEndOTA = (Button) findViewById(C1284R.C1286id.btn_ota_end);
        this.mResetOTA = (Button) findViewById(C1284R.C1286id.btn_ota_reset);
        this.mDisconnectDevice = (TextView) findViewById(C1284R.C1286id.btn_disconnect);
        this.tv_device_name = (TextView) findViewById(C1284R.C1286id.button_device_name);
        this.tv_firmware_version = (TextView) findViewById(C1284R.C1286id.button_firmware_version);
        this.tv_memory = (TextView) findViewById(C1284R.C1286id.tv_memory_percentage);
        this.tv_device_name.setText(GlobalVariables.gDeviceName);
        this.mSendFile.setEnabled(false);
        this.mSendFile.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.Button_Disabled));
        this.mEndOTA.setEnabled(false);
        this.mEndOTA.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.Button_Disabled));
        this.mStartOTA.setOnClickListener(new OTAActivity$$Lambda$0(this));
        this.mSendFile.setOnClickListener(new OTAActivity$$Lambda$1(this));
        this.mEndOTA.setOnClickListener(new OTAActivity$$Lambda$2(this));
        this.mResetOTA.setOnClickListener(new OTAActivity$$Lambda$3(this));
        this.mDisconnectDevice.setOnClickListener(new OTAActivity$$Lambda$4(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$0$OTAActivity(View v) {
        SWS_P3Packet newPacket = new SWS_P3Packet(4);
        newPacket.setPacketStream(new byte[]{(byte) Integer.parseInt("44", 16), (byte) Integer.parseInt("FE", 16), (byte) Integer.parseInt("9D", 16), (byte) Integer.parseInt("A6", 16)});
        if (this.scanPresenter == null) {
            this.scanPresenter = new ScanPresenter();
        }
        this.scanPresenter.sendOTACommand(newPacket);
        discover();
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$1$OTAActivity(View v) {
        try {
            sendData();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$2$OTAActivity(View v) {
        try {
            new SWS_P3Packet(4);
            this.mmOutputStream.write(new byte[]{(byte) Integer.parseInt("5F", 16), (byte) Integer.parseInt("83", 16), (byte) Integer.parseInt("F6", 16), (byte) Integer.parseInt("9E", 16)});
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$3$OTAActivity(View v) {
        SWS_P3Packet newPacket = new SWS_P3Packet(4);
        newPacket.setPacketStream(new byte[]{(byte) Integer.parseInt("E0", 16), (byte) Integer.parseInt("DA", 16), (byte) Integer.parseInt("D5", 16), (byte) Integer.parseInt("51", 16)});
        if (this.scanPresenter == null) {
            this.scanPresenter = new ScanPresenter();
        }
        this.scanPresenter.sendOTACommand(newPacket);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$4$OTAActivity(View v) {
        Builder myAlert = new Builder(this.mContext);
        myAlert.setTitle((CharSequence) "Disconnect");
        myAlert.setMessage((CharSequence) "Are you sure you want to disconnect the device?");
        myAlert.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (GlobalVariables.bluetoothAPI != null) {
                    GlobalVariables.bluetoothAPI.disconnectFromDevice();
                }
                Intent iMain = new Intent(OTAActivity.this.mContext, ConnectActivity.class);
                iMain.setFlags(268468224);
                OTAActivity.this.startActivity(iMain);
            }
        });
        myAlert.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        myAlert.show();
    }

    private void findBT() {
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.mBluetoothAdapter == null) {
            System.out.println("No bluetooth adapter available");
        }
        if (!this.mBluetoothAdapter.isEnabled()) {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 0);
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
            this.mSendFile.setEnabled(true);
            this.mSendFile.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.orange));
            return;
        }
        System.out.println("Bonding ......");
        this.mmDevice.createBond();
        registerReceiver(this.blReceiver, new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED"));
    }

    private void sendData() throws IOException {
        try {
            this.binaryOTAFileInBytes = fullyReadFileToBytes("image.bin");
            int commandsCount = this.binaryOTAFileInBytes.length / GlobalVariables.OTA_BT_MAX_TRANSMISSION_UNIT;
            for (int i = 0; i < commandsCount; i++) {
                this.mmOutputStream.write(this.binaryOTAFileInBytes, i * GlobalVariables.OTA_BT_MAX_TRANSMISSION_UNIT, GlobalVariables.OTA_BT_MAX_TRANSMISSION_UNIT);
                Thread.sleep(20);
            }
            if (this.binaryOTAFileInBytes.length % GlobalVariables.OTA_BT_MAX_TRANSMISSION_UNIT != 0) {
                this.mmOutputStream.write(this.binaryOTAFileInBytes, commandsCount * GlobalVariables.OTA_BT_MAX_TRANSMISSION_UNIT, this.binaryOTAFileInBytes.length % GlobalVariables.OTA_BT_MAX_TRANSMISSION_UNIT);
            }
            this.mEndOTA.setEnabled(true);
            this.mEndOTA.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.orange));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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

    public void onClick(View v) {
    }
}
