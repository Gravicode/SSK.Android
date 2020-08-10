package com.si_ware.neospectra.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.p001v4.widget.DrawerLayout;
import android.support.p004v7.widget.LinearLayoutManager;
import android.support.p004v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.si_ware.neospectra.Adapters.SensorsAdapter;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3API;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3BLEDevice;
import com.si_ware.neospectra.C1284R;
import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.Global.MethodsFactory;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends NavDrawerActivity implements OnClickListener {
    private static final String TAG = "SettingsActivity";
    private static SensorsAdapter adapter;
    /* access modifiers changed from: private */
    public static List<SWS_P3BLEDevice> bleDevices;
    private static Context mContext;
    private static ProgressBar pbSearching;
    private static RecyclerView rvDevices;
    private static TextView tvStatus;
    private LinearLayout bluetoothBoard;
    DrawerLayout drawer;
    private boolean isBluetoothEnabled;
    private boolean isLocationEnabled;
    private FirebaseAuth mAuth;
    private Paint paint;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = ((LayoutInflater) getSystemService("layout_inflater")).inflate(C1284R.layout.activity_settings, null, false);
        mContext = this;
        if (GlobalVariables.bluetoothAPI == null) {
            GlobalVariables.bluetoothAPI = new SWS_P3API(this, mContext);
        }
        setRequestedOrientation(14);
        this.bluetoothBoard = (LinearLayout) contentView.findViewById(C1284R.C1286id.board_bluetooth);
        initBluetoothArea();
        this.drawer = (DrawerLayout) findViewById(C1284R.C1286id.drawer_layout);
        this.drawer.addView(contentView, 0);
        this.mAuth = FirebaseAuth.getInstance();
    }

    public static void found_device(List<SWS_P3BLEDevice> list_Devices) {
        bleDevices = list_Devices;
        displayDevices("Searching button", bleDevices);
    }

    private void initBluetoothArea() {
        tvStatus = (TextView) this.bluetoothBoard.findViewById(C1284R.C1286id.tv_status);
        pbSearching = (ProgressBar) this.bluetoothBoard.findViewById(C1284R.C1286id.pb_bluetooth_scanning);
        if (pbSearching.getVisibility() == 0) {
            MethodsFactory.rotateProgressBar(this, pbSearching);
        }
        rvDevices = (RecyclerView) this.bluetoothBoard.findViewById(C1284R.C1286id.rv_bluetooth_devices);
        rvDevices.setVisibility(4);
        tvStatus.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View view) {
                SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                return true;
            }
        });
    }

    private void initListeners() {
        while (GlobalVariables.bluetoothAPI == null) {
            MethodsFactory.logMessage(TAG, "Initializing bluetooth API ...");
        }
        this.isBluetoothEnabled = GlobalVariables.bluetoothAPI.isBluetoothEnabled();
        this.isLocationEnabled = GlobalVariables.bluetoothAPI.isLocationEnabled(mContext);
        if (this.isBluetoothEnabled && this.isLocationEnabled) {
            tvStatus.setText(mContext.getResources().getString(C1284R.string.bluetooth_scan));
            pbSearching.setVisibility(0);
            MethodsFactory.rotateProgressBar(mContext, pbSearching);
            AsyncTask.execute(new Runnable() {
                public void run() {
                    SettingsActivity.bleDevices = GlobalVariables.bluetoothAPI.scanAvailableDevices();
                    SettingsActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            SettingsActivity.displayDevices("Searching button", SettingsActivity.bleDevices);
                        }
                    });
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public static void displayDevices(String from, List<SWS_P3BLEDevice> devices) {
        StringBuilder sb = new StringBuilder();
        sb.append("Displaying devices from: ");
        sb.append(from);
        MethodsFactory.logMessage("bluetooth", sb.toString());
        if (devices == null || devices.size() < 1) {
            Toast.makeText(mContext, "devices size <1", 0).show();
            return;
        }
        rvDevices.setVisibility(0);
        rvDevices.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new SensorsAdapter(mContext, devices);
        rvDevices.setAdapter(adapter);
        pbSearching.setVisibility(4);
        tvStatus.setText(mContext.getResources().getString(C1284R.string.bluetooth_found_devices));
        StringBuilder sb2 = new StringBuilder();
        sb2.append("displaying Done, length equals: ");
        sb2.append(devices.size());
        MethodsFactory.logMessage("bluetooth", sb2.toString());
    }

    private void updateSearchButtonStatus() {
        this.isBluetoothEnabled = GlobalVariables.bluetoothAPI.isBluetoothEnabled();
        this.isLocationEnabled = GlobalVariables.bluetoothAPI.isLocationEnabled(mContext);
        StringBuilder sb = new StringBuilder();
        sb.append("Bluetooth status is: ");
        sb.append(this.isBluetoothEnabled);
        sb.append(" Location service is: ");
        sb.append(this.isLocationEnabled);
        Log.d("bluetooth", sb.toString());
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (GlobalVariables.bluetoothAPI != null) {
            GlobalVariables.bluetoothAPI.disconnectFromDevice();
        }
    }

    private BitmapDrawable writeOnDrawable(int drawableId, @NonNull String text) {
        new Options().inScaled = false;
        Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), drawableId);
        Bitmap bm = bmp1.copy(bmp1.getConfig(), true);
        if (this.paint == null) {
            this.paint = new Paint(1);
        }
        this.paint.setStyle(Style.FILL);
        this.paint.setColor(getResources().getColor(C1284R.color.grey));
        this.paint.setTextSize(30.0f);
        new Canvas(bm).drawText(text, 10.0f, (float) (bm.getHeight() / 4), this.paint);
        return new BitmapDrawable(getResources(), bm);
    }

    public void onClick(View v) {
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        getCurrentStatusOfLocationAndBluetooth();
        initListeners();
        updateSearchButtonStatus();
        if (GlobalVariables.currentConnectedDevice != null) {
            MethodsFactory.logMessage("bluetooth", "Current device found");
            List<SWS_P3BLEDevice> mDevices = new ArrayList<>();
            GlobalVariables.currentConnectedDevice.connected = true;
            mDevices.add(GlobalVariables.currentConnectedDevice);
            displayDevices("OnResume", mDevices);
        }
    }

    private void getCurrentStatusOfLocationAndBluetooth() {
        this.isBluetoothEnabled = getBluetoothStatus();
        this.isLocationEnabled = getLocationStatus();
    }

    private boolean getLocationStatus() {
        return GlobalVariables.bluetoothAPI.isLocationEnabled(mContext);
    }

    private boolean getBluetoothStatus() {
        return GlobalVariables.bluetoothAPI.isBluetoothEnabled();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }
}
