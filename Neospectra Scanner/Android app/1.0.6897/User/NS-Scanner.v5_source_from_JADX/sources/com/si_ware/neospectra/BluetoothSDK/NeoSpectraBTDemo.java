package com.si_ware.neospectra.BluetoothSDK;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.p001v4.content.ContextCompat;
import android.support.p004v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.si_ware.neospectra.C1284R;
import com.si_ware.neospectra.Global.GlobalVariables;
import java.util.ArrayList;
import java.util.List;

public class NeoSpectraBTDemo extends AppCompatActivity {
    /* access modifiers changed from: private */
    public Button btnCancelDialog;
    /* access modifiers changed from: private */
    public Button btnSendPacket;
    /* access modifiers changed from: private */
    public TextView mAppStatus;
    /* access modifiers changed from: private */
    public List<SWS_P3BLEDevice> mBTDeviceList;
    /* access modifiers changed from: private */
    public Button mBTScanButton;
    private Switch mBTStatusSwitch;
    /* access modifiers changed from: private */
    public Button mConnectButton;
    /* access modifiers changed from: private */
    public int mCurrentDeviceID = -1;
    private Spinner mDeviceListSpinner;
    /* access modifiers changed from: private */
    public TextView mDeviceMacAddressTextView;
    /* access modifiers changed from: private */
    public TextView mDeviceNameTextView;
    /* access modifiers changed from: private */
    public TextView mDeviceRSSITextView;
    /* access modifiers changed from: private */
    public AlertDialog mDialog;
    private MyBroadcastReceiver mNotificationListener;
    /* access modifiers changed from: private */
    public ImageView mNotificationSet;
    /* access modifiers changed from: private */
    @NonNull
    public SWS_P3API mP3API = new SWS_P3API(this, this);
    /* access modifiers changed from: private */
    public ProgressBar mScanProgress;
    /* access modifiers changed from: private */
    public Button mSendPacketButton;
    /* access modifiers changed from: private */
    public Button mSetNotificationButton;
    /* access modifiers changed from: private */
    public Context mThisContext;
    /* access modifiers changed from: private */
    public Spinner spnrApodization;
    /* access modifiers changed from: private */
    public Spinner spnrOpticalGain;
    /* access modifiers changed from: private */
    public Spinner spnrPoints;
    /* access modifiers changed from: private */
    public Spinner spnrRunMode;
    /* access modifiers changed from: private */
    public Spinner spnrSelectCmd;
    /* access modifiers changed from: private */
    public Spinner spnrZeroPadding;
    /* access modifiers changed from: private */
    public EditText txtScanTime;
    /* access modifiers changed from: private */
    public TextView txtViewPacketContent_TV;

    public class MyBroadcastReceiver extends BroadcastReceiver {
        public MyBroadcastReceiver() {
        }

        public void onReceive(Context context, @NonNull Intent intent) {
            double[] recBytes = intent.getDoubleArrayExtra("P3_data");
            String simpleName = getClass().getSimpleName();
            StringBuilder sb = new StringBuilder();
            sb.append("##### Callback Received - ");
            sb.append(String.valueOf(recBytes.length));
            Log.i(simpleName, sb.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1284R.layout.activity_neo_spectra_btdemo);
        this.mThisContext = this;
        connectControlsToLayout();
        this.mNotificationListener = new MyBroadcastReceiver();
        registerReceiver(this.mNotificationListener, new IntentFilter(GlobalVariables.INTENT_ACTION));
        if (this.mP3API.isBluetoothEnabled()) {
            this.mBTStatusSwitch.setChecked(true);
            this.mBTScanButton.setEnabled(true);
        } else {
            this.mBTStatusSwitch.setChecked(false);
            this.mBTScanButton.setEnabled(false);
        }
        this.mScanProgress.setVisibility(4);
        this.mDeviceNameTextView.setVisibility(4);
        this.mDeviceMacAddressTextView.setVisibility(4);
        this.mDeviceRSSITextView.setVisibility(4);
        this.mConnectButton.setVisibility(4);
        this.mSetNotificationButton.setVisibility(4);
        this.mSendPacketButton.setVisibility(4);
        this.mNotificationSet.setVisibility(4);
        this.mAppStatus.setText("Ready");
        if (!(ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0)) {
            this.mP3API.getLocationPermissions();
        }
        this.mBTStatusSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    NeoSpectraBTDemo.this.startActivityForResult(NeoSpectraBTDemo.this.mP3API.enableBluetooth(), 1);
                    NeoSpectraBTDemo.this.mBTScanButton.setEnabled(true);
                    return;
                }
                NeoSpectraBTDemo.this.mP3API.disableBluetooth();
                NeoSpectraBTDemo.this.mBTScanButton.setEnabled(false);
            }
        });
        this.mBTScanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                NeoSpectraBTDemo.this.mAppStatus.setText("Scanning ... ");
                NeoSpectraBTDemo.this.mScanProgress.setVisibility(0);
                NeoSpectraBTDemo.this.mBTDeviceList = NeoSpectraBTDemo.this.mP3API.scanAvailableDevices();
                List<String> spinnerArray = new ArrayList<>();
                int counter = 0;
                for (SWS_P3BLEDevice device : NeoSpectraBTDemo.this.mBTDeviceList) {
                    counter++;
                    if (device.getDeviceName() != null) {
                        spinnerArray.add(device.getDeviceName());
                    } else if (spinnerArray.size() > 0) {
                        spinnerArray.remove(counter);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(NeoSpectraBTDemo.this.mThisContext, 17367048, spinnerArray);
                adapter.setDropDownViewResource(17367049);
                ((Spinner) NeoSpectraBTDemo.this.findViewById(C1284R.C1286id.devicesList_spinner)).setAdapter(adapter);
                if (NeoSpectraBTDemo.this.mBTDeviceList.size() > 0) {
                    NeoSpectraBTDemo.this.mScanProgress.setVisibility(4);
                }
                NeoSpectraBTDemo.this.mAppStatus.setText("Found Devices ... ");
            }
        });
        this.mDeviceListSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                TextView access$600 = NeoSpectraBTDemo.this.mDeviceNameTextView;
                StringBuilder sb = new StringBuilder();
                sb.append("Name: ");
                sb.append(((SWS_P3BLEDevice) NeoSpectraBTDemo.this.mBTDeviceList.get(position)).getDeviceName());
                access$600.setText(sb.toString());
                TextView access$700 = NeoSpectraBTDemo.this.mDeviceMacAddressTextView;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Mac Address: ");
                sb2.append(((SWS_P3BLEDevice) NeoSpectraBTDemo.this.mBTDeviceList.get(position)).getDeviceMacAddress());
                access$700.setText(sb2.toString());
                TextView access$800 = NeoSpectraBTDemo.this.mDeviceRSSITextView;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("RSSI: ");
                sb3.append(((SWS_P3BLEDevice) NeoSpectraBTDemo.this.mBTDeviceList.get(position)).getDeviceRSSI());
                access$800.setText(sb3.toString());
                NeoSpectraBTDemo.this.mDeviceNameTextView.setVisibility(0);
                NeoSpectraBTDemo.this.mDeviceMacAddressTextView.setVisibility(0);
                NeoSpectraBTDemo.this.mDeviceRSSITextView.setVisibility(0);
                NeoSpectraBTDemo.this.mConnectButton.setVisibility(0);
                NeoSpectraBTDemo.this.mCurrentDeviceID = position;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.mConnectButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (NeoSpectraBTDemo.this.mConnectButton.getText().equals("Disconnect")) {
                    NeoSpectraBTDemo.this.mAppStatus.setText("Disconnecting ... ");
                    NeoSpectraBTDemo.this.mP3API.disconnectFromDevice();
                    NeoSpectraBTDemo.this.mAppStatus.setText("Ready ");
                    NeoSpectraBTDemo.this.mConnectButton.setText("Connect");
                    return;
                }
                Boolean valueOf = Boolean.valueOf(NeoSpectraBTDemo.this.mP3API.connectToDevice(((SWS_P3BLEDevice) NeoSpectraBTDemo.this.mBTDeviceList.get(NeoSpectraBTDemo.this.mCurrentDeviceID)).getDeviceInstance()));
                while (!NeoSpectraBTDemo.this.mP3API.getCurrentConnectionStatus()) {
                    NeoSpectraBTDemo.this.mAppStatus.setText("Connecting ... ");
                    NeoSpectraBTDemo.this.mScanProgress.setVisibility(0);
                }
                TextView access$200 = NeoSpectraBTDemo.this.mAppStatus;
                StringBuilder sb = new StringBuilder();
                sb.append("Connected to: ");
                sb.append(((SWS_P3BLEDevice) NeoSpectraBTDemo.this.mBTDeviceList.get(NeoSpectraBTDemo.this.mCurrentDeviceID)).getDeviceName());
                access$200.setText(sb.toString());
                NeoSpectraBTDemo.this.mScanProgress.setVisibility(4);
                NeoSpectraBTDemo.this.mSetNotificationButton.setVisibility(0);
                NeoSpectraBTDemo.this.mSendPacketButton.setVisibility(0);
                NeoSpectraBTDemo.this.mConnectButton.setText("Disconnect");
                NeoSpectraBTDemo.this.mSetNotificationButton.setEnabled(true);
                NeoSpectraBTDemo.this.mNotificationSet.setVisibility(4);
            }
        });
        this.mSetNotificationButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                NeoSpectraBTDemo.this.mAppStatus.setText("Setting Notification ... ");
                NeoSpectraBTDemo.this.mP3API.setNotifications();
                NeoSpectraBTDemo.this.mNotificationSet.setVisibility(0);
                NeoSpectraBTDemo.this.mSetNotificationButton.setEnabled(false);
                TextView access$200 = NeoSpectraBTDemo.this.mAppStatus;
                StringBuilder sb = new StringBuilder();
                sb.append("Connected to: ");
                sb.append(((SWS_P3BLEDevice) NeoSpectraBTDemo.this.mBTDeviceList.get(NeoSpectraBTDemo.this.mCurrentDeviceID)).getDeviceName());
                access$200.setText(sb.toString());
            }
        });
        this.mSendPacketButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Builder mBuilder = new Builder(NeoSpectraBTDemo.this);
                View mView = NeoSpectraBTDemo.this.getLayoutInflater().inflate(C1284R.layout.packet_dialog_layout, null);
                NeoSpectraBTDemo.this.connectControlsToDialogLayout(mView);
                NeoSpectraBTDemo.this.populatePacketAlertDialogFields();
                NeoSpectraBTDemo.this.btnSendPacket.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        SWS_P3Packet newPacket = new SWS_P3Packet();
                        newPacket.setSWS_P3Packet_Command(NeoSpectraBTDemo.this.spnrSelectCmd.getSelectedItem().toString());
                        newPacket.setSWS_P3Packet_ScanTime(NeoSpectraBTDemo.this.txtScanTime.getText().toString());
                        newPacket.setSWS_P3Packet_PointsCount(NeoSpectraBTDemo.this.spnrPoints.getSelectedItem().toString());
                        newPacket.setSWS_P3Packet_OpticalGain(NeoSpectraBTDemo.this.spnrOpticalGain.getSelectedItem().toString());
                        newPacket.setSWS_P3Packet_Apodization(NeoSpectraBTDemo.this.spnrApodization.getSelectedItem().toString());
                        newPacket.setSWS_P3Packet_ZeroPadding(NeoSpectraBTDemo.this.spnrZeroPadding.getSelectedItem().toString());
                        newPacket.setSWS_P3Packet_RunMode(NeoSpectraBTDemo.this.spnrRunMode.getSelectedItem().toString());
                        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Command());
                        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ScanTime());
                        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_PointsCount());
                        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_OpticalGain());
                        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Apodization());
                        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ZeroPadding());
                        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_RunMode());
                        newPacket.preparePacketContent();
                        NeoSpectraBTDemo.this.txtViewPacketContent_TV.setText(newPacket.getPacketAsText());
                        NeoSpectraBTDemo.this.mP3API.sendPacket(newPacket.getPacketStream(), newPacket.isInterpolationMode());
                        NeoSpectraBTDemo.this.mDialog.cancel();
                        NeoSpectraBTDemo.this.mAppStatus.setText("Sending Packet ... ");
                        TextView access$200 = NeoSpectraBTDemo.this.mAppStatus;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Connected to: ");
                        sb.append(((SWS_P3BLEDevice) NeoSpectraBTDemo.this.mBTDeviceList.get(NeoSpectraBTDemo.this.mCurrentDeviceID)).getDeviceName());
                        access$200.setText(sb.toString());
                    }
                });
                NeoSpectraBTDemo.this.btnCancelDialog.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        NeoSpectraBTDemo.this.mDialog.cancel();
                    }
                });
                mBuilder.setView(mView);
                NeoSpectraBTDemo.this.mDialog = mBuilder.create();
                NeoSpectraBTDemo.this.mDialog.show();
            }
        });
    }

    private void connectControlsToLayout() {
        this.mBTStatusSwitch = (Switch) findViewById(C1284R.C1286id.BTStatus_SW);
        this.mBTScanButton = (Button) findViewById(C1284R.C1286id.ScanBT_btn);
        this.mDeviceListSpinner = (Spinner) findViewById(C1284R.C1286id.devicesList_spinner);
        this.mScanProgress = (ProgressBar) findViewById(C1284R.C1286id.scanProgress);
        this.mDeviceNameTextView = (TextView) findViewById(C1284R.C1286id.DeviceName_TV);
        this.mDeviceMacAddressTextView = (TextView) findViewById(C1284R.C1286id.DeviceMAC_TV);
        this.mDeviceRSSITextView = (TextView) findViewById(C1284R.C1286id.DeviceRSSI_TV);
        this.mConnectButton = (Button) findViewById(C1284R.C1286id.connect_btn);
        this.mAppStatus = (TextView) findViewById(C1284R.C1286id.AppStatus_TV);
        this.mSetNotificationButton = (Button) findViewById(C1284R.C1286id.SetNotify_btn);
        this.mSendPacketButton = (Button) findViewById(C1284R.C1286id.btn_view_received_packet);
        this.mNotificationSet = (ImageView) findViewById(C1284R.C1286id.notification_IV);
    }

    /* access modifiers changed from: private */
    public void connectControlsToDialogLayout(View mView) {
        this.btnSendPacket = (Button) mView.findViewById(C1284R.C1286id.btn_view_received_packet);
        this.btnCancelDialog = (Button) mView.findViewById(C1284R.C1286id.Cancel_btn);
        this.spnrSelectCmd = (Spinner) mView.findViewById(C1284R.C1286id.cmdSpinner);
        this.spnrPoints = (Spinner) mView.findViewById(C1284R.C1286id.PointsSpinner);
        this.spnrOpticalGain = (Spinner) mView.findViewById(C1284R.C1286id.opticalGainSpinner);
        this.spnrApodization = (Spinner) mView.findViewById(C1284R.C1286id.apodizationSpinner);
        this.spnrZeroPadding = (Spinner) mView.findViewById(C1284R.C1286id.zeroPaddingSpinner);
        this.spnrRunMode = (Spinner) mView.findViewById(C1284R.C1286id.runModeSpinner);
        this.txtScanTime = (EditText) mView.findViewById(C1284R.C1286id.ScanTimeTextField);
        this.txtViewPacketContent_TV = (TextView) mView.findViewById(C1284R.C1286id.packetContent_TV);
    }

    /* access modifiers changed from: private */
    public void populatePacketAlertDialogFields() {
        ArrayAdapter<CharSequence> CmdSpinneradapter = ArrayAdapter.createFromResource(this, C1284R.array.operations_spinner_content, 17367048);
        CmdSpinneradapter.setDropDownViewResource(17367049);
        this.spnrSelectCmd.setAdapter(CmdSpinneradapter);
        ArrayAdapter<CharSequence> PointsSpinneradapter = ArrayAdapter.createFromResource(this, C1284R.array.data_points_spinner_content, 17367048);
        PointsSpinneradapter.setDropDownViewResource(17367049);
        this.spnrPoints.setAdapter(PointsSpinneradapter);
        ArrayAdapter<CharSequence> OpticalGainSpinneradapter = ArrayAdapter.createFromResource(this, C1284R.array.optical_gain_spinner_content, 17367048);
        OpticalGainSpinneradapter.setDropDownViewResource(17367049);
        this.spnrOpticalGain.setAdapter(OpticalGainSpinneradapter);
        ArrayAdapter<CharSequence> ApodizationSpinneradapter = ArrayAdapter.createFromResource(this, C1284R.array.apodization_spinner_content, 17367048);
        ApodizationSpinneradapter.setDropDownViewResource(17367049);
        this.spnrApodization.setAdapter(ApodizationSpinneradapter);
        ArrayAdapter<CharSequence> ZeroPaddingSpinneradapter = ArrayAdapter.createFromResource(this, C1284R.array.zero_padding_spinner_content, 17367048);
        ZeroPaddingSpinneradapter.setDropDownViewResource(17367049);
        this.spnrZeroPadding.setAdapter(ZeroPaddingSpinneradapter);
        ArrayAdapter<CharSequence> RunModeSpinneradapter = ArrayAdapter.createFromResource(this, C1284R.array.run_mode_spinner_content, 17367048);
        RunModeSpinneradapter.setDropDownViewResource(17367049);
        this.spnrRunMode.setAdapter(RunModeSpinneradapter);
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mNotificationListener);
    }
}
