package com.si_ware.neospectra.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.p001v4.app.ActivityCompat;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.NotificationCompat;
import android.support.p001v4.content.ContextCompat;
import android.support.p001v4.content.LocalBroadcastManager;
import android.support.p001v4.widget.DrawerLayout;
import android.support.p004v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3API;
import com.si_ware.neospectra.C1284R;
import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.Global.GlobalVariables.apodization;
import com.si_ware.neospectra.Global.GlobalVariables.pointsCount;
import com.si_ware.neospectra.Global.GlobalVariables.runMode;
import com.si_ware.neospectra.Global.GlobalVariables.wavelengthCorrection;
import com.si_ware.neospectra.Global.GlobalVariables.zeroPadding;
import com.si_ware.neospectra.Global.MethodsFactory;
import com.si_ware.neospectra.Models.dbReading;
import com.si_ware.neospectra.Scan.Presenter.ScanPresenter;
import java.io.File;
import java.io.PrintStream;

public class MainActivity extends NavDrawerActivity implements OnClickListener {
    public static final String CAMERA_PREF = "camera_pref";
    public static boolean error_sensor_reading = false;
    /* access modifiers changed from: private */
    @NonNull
    public String TAG = "Main Activity";
    public boolean applyStopEnabled = false;
    public int backgroundScanTime = 2;
    FloatingActionButton btnBackground;
    FloatingActionButton btnScan;
    private Button btnViewScan;
    private int count = 1;
    DrawerLayout drawer;
    public boolean isScanBG = false;
    private boolean isStopEnabled = false;
    /* access modifiers changed from: private */
    public boolean isWaitingForBackGroundReading = false;
    private boolean isWaitingForGainReading = false;
    /* access modifiers changed from: private */
    public boolean isWaitingForSensorReading = false;
    /* access modifiers changed from: private */
    public Context mContext;
    @NonNull
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        /* JADX WARNING: Removed duplicated region for block: B:22:0x004d  */
        /* JADX WARNING: Removed duplicated region for block: B:23:0x006b  */
        /* JADX WARNING: Removed duplicated region for block: B:24:0x0074  */
        /* JADX WARNING: Removed duplicated region for block: B:25:0x011c  */
        /* JADX WARNING: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r7, android.content.Intent r8) {
            /*
                r6 = this;
                java.lang.String r0 = "iName"
                java.lang.String r0 = r8.getStringExtra(r0)
                int r1 = r0.hashCode()
                r2 = -831571810(0xffffffffce6f389e, float:-1.00336627E9)
                r3 = 1
                r4 = 0
                if (r1 == r2) goto L_0x003f
                r2 = 559518180(0x215991e4, float:7.3715544E-19)
                if (r1 == r2) goto L_0x0035
                r2 = 871069634(0x33eb77c2, float:1.0964824E-7)
                if (r1 == r2) goto L_0x002b
                r2 = 1603317008(0x5f90ad10, float:2.0850013E19)
                if (r1 == r2) goto L_0x0021
                goto L_0x0049
            L_0x0021:
                java.lang.String r1 = "sensorNotification_failure"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L_0x0049
                r1 = 1
                goto L_0x004a
            L_0x002b:
                java.lang.String r1 = "sensorWriting"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L_0x0049
                r1 = 2
                goto L_0x004a
            L_0x0035:
                java.lang.String r1 = "sensorNotification_data"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L_0x0049
                r1 = 0
                goto L_0x004a
            L_0x003f:
                java.lang.String r1 = "Disconnection_Notification"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L_0x0049
                r1 = 3
                goto L_0x004a
            L_0x0049:
                r1 = -1
            L_0x004a:
                switch(r1) {
                    case 0: goto L_0x011c;
                    case 1: goto L_0x0074;
                    case 2: goto L_0x0072;
                    case 3: goto L_0x006b;
                    default: goto L_0x004d;
                }
            L_0x004d:
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                com.si_ware.neospectra.Activities.MainActivity r2 = com.si_ware.neospectra.Activities.MainActivity.this
                java.lang.String r2 = r2.TAG
                r1.append(r2)
                java.lang.String r2 = "intent"
                r1.append(r2)
                java.lang.String r1 = r1.toString()
                java.lang.String r2 = "Got unknown broadcast intent"
                android.util.Log.v(r1, r2)
                goto L_0x0183
            L_0x006b:
                com.si_ware.neospectra.Activities.MainActivity r1 = com.si_ware.neospectra.Activities.MainActivity.this
                r1.endActivity()
                goto L_0x0183
            L_0x0072:
                goto L_0x0183
            L_0x0074:
                com.si_ware.neospectra.Activities.MainActivity r1 = com.si_ware.neospectra.Activities.MainActivity.this
                r1.gotSensorReading(r8)
                com.si_ware.neospectra.Activities.MainActivity r1 = com.si_ware.neospectra.Activities.MainActivity.this
                java.lang.String r1 = r1.TAG
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r5 = "Intent Received:\nName: "
                r2.append(r5)
                java.lang.String r5 = "iName"
                java.lang.String r5 = r8.getStringExtra(r5)
                r2.append(r5)
                java.lang.String r5 = "\nSuccess: "
                r2.append(r5)
                java.lang.String r5 = "isNotificationSuccess"
                boolean r5 = r8.getBooleanExtra(r5, r4)
                r2.append(r5)
                java.lang.String r5 = "\nReason: "
                r2.append(r5)
                java.lang.String r5 = "reason"
                java.lang.String r5 = r8.getStringExtra(r5)
                r2.append(r5)
                java.lang.String r5 = "\nError: "
                r2.append(r5)
                java.lang.String r5 = "err"
                java.lang.String r5 = r8.getStringExtra(r5)
                r2.append(r5)
                java.lang.String r5 = "\ndata : "
                r2.append(r5)
                java.lang.String r5 = "data"
                int r5 = r8.getIntExtra(r5, r4)
                java.lang.String r5 = java.lang.String.valueOf(r5)
                r2.append(r5)
                java.lang.String r5 = "\n"
                r2.append(r5)
                java.lang.String r2 = r2.toString()
                com.si_ware.neospectra.Global.MethodsFactory.logMessage(r1, r2)
                java.lang.String r1 = "data"
                int r1 = r8.getIntExtra(r1, r4)
                com.si_ware.neospectra.Activities.MainActivity r2 = com.si_ware.neospectra.Activities.MainActivity.this
                r2.enableButtonAndView(r3)
                com.si_ware.neospectra.Activities.MainActivity r2 = com.si_ware.neospectra.Activities.MainActivity.this
                r2.notifications_count = r4
                com.si_ware.neospectra.Activities.MainActivity r2 = com.si_ware.neospectra.Activities.MainActivity.this
                r2.isWaitingForBackGroundReading = r4
                com.si_ware.neospectra.Activities.MainActivity r2 = com.si_ware.neospectra.Activities.MainActivity.this
                r2.isWaitingForSensorReading = r4
                com.si_ware.neospectra.Activities.MainActivity r2 = com.si_ware.neospectra.Activities.MainActivity.this
                android.content.Context r2 = r2.mContext
                java.lang.String r3 = "Error"
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "Error "
                r4.append(r5)
                r5 = r1 & 255(0xff, float:3.57E-43)
                java.lang.String r5 = java.lang.String.valueOf(r5)
                r4.append(r5)
                java.lang.String r5 = " occurred during measurement!"
                r4.append(r5)
                java.lang.String r4 = r4.toString()
                com.si_ware.neospectra.Global.MethodsFactory.showAlertMessage(r2, r3, r4)
                goto L_0x0183
            L_0x011c:
                com.si_ware.neospectra.Activities.MainActivity r1 = com.si_ware.neospectra.Activities.MainActivity.this
                r1.gotSensorReading(r8)
                com.si_ware.neospectra.Activities.MainActivity r1 = com.si_ware.neospectra.Activities.MainActivity.this
                java.lang.String r1 = r1.TAG
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = "Intent Received:\nName: "
                r2.append(r3)
                java.lang.String r3 = "iName"
                java.lang.String r3 = r8.getStringExtra(r3)
                r2.append(r3)
                java.lang.String r3 = "\nSuccess: "
                r2.append(r3)
                java.lang.String r3 = "isNotificationSuccess"
                boolean r3 = r8.getBooleanExtra(r3, r4)
                r2.append(r3)
                java.lang.String r3 = "\nReason: "
                r2.append(r3)
                java.lang.String r3 = "reason"
                java.lang.String r3 = r8.getStringExtra(r3)
                r2.append(r3)
                java.lang.String r3 = "\nError: "
                r2.append(r3)
                java.lang.String r3 = "err"
                java.lang.String r3 = r8.getStringExtra(r3)
                r2.append(r3)
                java.lang.String r3 = "\ndata : "
                r2.append(r3)
                java.lang.String r3 = "data"
                double[] r3 = r8.getDoubleArrayExtra(r3)
                java.lang.String r3 = java.util.Arrays.toString(r3)
                r2.append(r3)
                java.lang.String r3 = "\n"
                r2.append(r3)
                java.lang.String r2 = r2.toString()
                com.si_ware.neospectra.Global.MethodsFactory.logMessage(r1, r2)
            L_0x0183:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.si_ware.neospectra.Activities.MainActivity.C11792.onReceive(android.content.Context, android.content.Intent):void");
        }
    };
    /* access modifiers changed from: private */
    public SeekBar mSeekBar;
    @Nullable
    private Fragment myFragment;
    /* access modifiers changed from: private */
    public int notifications_count = 0;
    private ProgressBar pbProgressBar;
    ScanPresenter scanPresenter;
    private TextView textScan;
    TextView text_scan;
    private TextView tv_progressCount;
    /* access modifiers changed from: private */
    public TextView tv_progressValue;
    private EditText tx_numberOfRuns;

    /* access modifiers changed from: protected */
    @RequiresApi(api = 21)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = ((LayoutInflater) getSystemService("layout_inflater")).inflate(C1284R.layout.activity_main, null, false);
        this.drawer = (DrawerLayout) findViewById(C1284R.C1286id.drawer_layout);
        this.drawer.addView(contentView, 0);
        if (GlobalVariables.bluetoothAPI == null) {
            GlobalVariables.bluetoothAPI = new SWS_P3API(this, this.mContext);
        }
        this.mContext = this;
        this.scanPresenter = new ScanPresenter();
        setRequestedOrientation(14);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        GlobalVariables.gRunMode = preferences.getString("run_mode", runMode.Single_Mode.toString());
        GlobalVariables.gIsInterpolationEnabled = preferences.getBoolean("linear_interpolation_switch", false);
        GlobalVariables.gInterpolationPoints = preferences.getString("data_points", pointsCount.points_257.toString());
        GlobalVariables.gIsFftEnabled = preferences.getBoolean("fft_settings_switch", false);
        GlobalVariables.gApodizationFunction = preferences.getString("apodization_function", apodization.Boxcar.toString());
        GlobalVariables.gFftPoints = preferences.getString("fft_points", zeroPadding.points_8k.toString());
        GlobalVariables.gOpticalGainSettings = preferences.getString("optical_gain_settings", "Default");
        GlobalVariables.gOpticalGainValue = preferences.getInt(GlobalVariables.gOpticalGainSettings, 0);
        GlobalVariables.gCorrectionMode = preferences.getString("wavelength_correction", wavelengthCorrection.Self_Calibration.toString());
        this.btnScan = (FloatingActionButton) findViewById(C1284R.C1286id.fab_scan);
        this.btnBackground = (FloatingActionButton) findViewById(C1284R.C1286id.fab_stop);
        this.mSeekBar = (SeekBar) findViewById(C1284R.C1286id.seek_scantime);
        this.textScan = (TextView) findViewById(C1284R.C1286id.text_scan);
        this.textScan.setText("Scan");
        this.mSeekBar.setProgress(GlobalVariables.scanTime);
        this.tv_progressValue = (TextView) findViewById(C1284R.C1286id.tv_progress);
        TextView textView = this.tv_progressValue;
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(GlobalVariables.scanTime);
        textView.setText(sb.toString());
        if (GlobalVariables.scanTime != 2) {
            this.tv_progressValue.setX(GlobalVariables.progressBarPosition / 2.0f);
        }
        this.pbProgressBar = (ProgressBar) findViewById(C1284R.C1286id.progressBarMain);
        this.pbProgressBar.setVisibility(4);
        this.btnViewScan = (Button) findViewById(C1284R.C1286id.button_viewScan);
        this.tx_numberOfRuns = (EditText) findViewById(C1284R.C1286id.tx_numberOfRuns);
        this.btnScan.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.orange));
        this.btnBackground.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.orange));
        this.btnBackground.setEnabled(true);
        this.tv_progressCount = (TextView) findViewById(C1284R.C1286id.countProgress);
        this.tv_progressCount.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.Button_Disabled));
        this.tv_progressCount.setEnabled(false);
        ArrayAdapter.createFromResource(this.mContext, C1284R.array.BLE_Services, 17367048).setDropDownViewResource(17367049);
        this.text_scan = (TextView) findViewById(C1284R.C1286id.text_scan);
        this.btnScan.setOnClickListener(new MainActivity$$Lambda$0(this));
        this.btnBackground.setOnClickListener(new MainActivity$$Lambda$1(this));
        this.mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (MainActivity.this.mSeekBar.getProgress() < 1) {
                    MainActivity.this.mSeekBar.setProgress(1);
                    return;
                }
                GlobalVariables.scanTime = progress;
                int val = (int) (((double) ((seekBar.getWidth() - (seekBar.getThumbOffset() * 2)) * progress)) / (((double) seekBar.getMax()) * 1.15d));
                TextView access$100 = MainActivity.this.tv_progressValue;
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(progress);
                access$100.setText(sb.toString());
                GlobalVariables.progressBarPosition = seekBar.getX() + ((float) val) + ((float) (seekBar.getThumbOffset() / 2));
                MainActivity.this.tv_progressValue.setX(seekBar.getX() + ((float) val) + ((float) (seekBar.getThumbOffset() / 2)));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.btnViewScan.setOnClickListener(new MainActivity$$Lambda$2(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$0$MainActivity(View v) {
        if (this.textScan.getText() != "Scan") {
            this.btnScan.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.Button_Disabled));
            this.btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.Button_Disabled));
            this.btnScan.setEnabled(false);
            this.btnViewScan.setEnabled(false);
            this.tv_progressCount.setEnabled(false);
            this.pbProgressBar.setVisibility(4);
            this.btnBackground.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.Button_Disabled));
            this.btnBackground.setEnabled(false);
            this.isStopEnabled = true;
        } else if (this.backgroundScanTime < GlobalVariables.scanTime) {
            MethodsFactory.logMessage(this.TAG, "Material Scan time is greater than reference material scan time ");
            MethodsFactory.showAlertMessage(this.mContext, "Error", "Material scan time should be less than or equal reference scan time");
        } else {
            this.btnScan.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.Button_Disabled));
            this.btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.Button_Disabled));
            this.btnViewScan.setEnabled(false);
            this.isScanBG = false;
            this.textScan.setText("Stop");
            this.btnBackground.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.Button_Disabled));
            this.btnBackground.setEnabled(false);
            if (Double.parseDouble(this.tx_numberOfRuns.getText().toString()) > 1.0d) {
                this.tv_progressCount.setEnabled(true);
                this.pbProgressBar.setVisibility(0);
                MethodsFactory.rotateProgressBar(this.mContext, this.pbProgressBar);
            }
            if (this.count == 1) {
                TextView textView = this.tv_progressCount;
                StringBuilder sb = new StringBuilder();
                sb.append("1/");
                sb.append(this.tx_numberOfRuns.getText().toString());
                textView.setText(sb.toString());
            }
            this.tv_progressCount.setTextColor(-16777216);
            sendAbsorbanceCommand();
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$1$MainActivity(View v) {
        this.btnScan.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.Button_Disabled));
        this.btnScan.setEnabled(false);
        this.btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.Button_Disabled));
        this.btnViewScan.setEnabled(false);
        this.isScanBG = true;
        this.btnBackground.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.Button_Disabled));
        this.btnBackground.setEnabled(false);
        this.tv_progressCount.setEnabled(false);
        this.tv_progressCount.setText("");
        this.backgroundScanTime = GlobalVariables.scanTime;
        this.pbProgressBar.setVisibility(4);
        sendBackgroundCommand();
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$2$MainActivity(View v) {
        GlobalVariables.measurmentsViewCaller = MainActivity.class;
        startActivity(new Intent(this, ResultsActivity.class));
    }

    private void updateProgressValue(int value) {
        String[] strings = this.tv_progressCount.getText().toString().split("/");
        strings[0] = String.valueOf(value);
        TextView textView = this.tv_progressCount;
        StringBuilder sb = new StringBuilder();
        sb.append(strings[0]);
        sb.append("/");
        sb.append(strings[1]);
        textView.setText(sb.toString());
        this.tv_progressCount.setTextColor(-16777216);
    }

    public boolean isWriteStoragePermissionGranted() {
        if (VERSION.SDK_INT < 23) {
            Log.v(this.TAG, "Permission is granted2");
            return true;
        } else if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            Log.v(this.TAG, "Permission is granted2");
            return true;
        } else {
            Log.v(this.TAG, "Permission is revoked2");
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 2);
            return false;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d(this.TAG, "External storage2");
                if (grantResults[0] == 0) {
                    String str = this.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Permission: ");
                    sb.append(permissions[0]);
                    sb.append("was ");
                    sb.append(grantResults[0]);
                    Log.v(str, sb.toString());
                    return;
                }
                return;
            case 3:
                Log.d(this.TAG, "External storage1");
                if (grantResults[0] == 0) {
                    String str2 = this.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Permission: ");
                    sb2.append(permissions[0]);
                    sb2.append("was ");
                    sb2.append(grantResults[0]);
                    Log.v(str2, sb2.toString());
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
    }

    public void onClick(View v) {
    }

    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mMessageReceiver, new IntentFilter(GlobalVariables.INTENT_ACTION));
        if (GlobalVariables.bluetoothAPI != null && !GlobalVariables.bluetoothAPI.isDeviceConnected()) {
            endActivity();
        }
    }

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mMessageReceiver);
    }

    /* access modifiers changed from: private */
    public void enableButtonAndView(boolean enable) {
        if (enable) {
            this.btnScan.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.orange));
            this.btnScan.setEnabled(true);
            this.textScan.setText("Scan");
            this.btnViewScan.setEnabled(true);
            this.btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.orange));
            this.btnBackground.setEnabled(true);
            this.btnBackground.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.orange));
            return;
        }
        this.btnScan.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.Button_Disabled));
        this.btnScan.setEnabled(false);
        this.btnViewScan.setEnabled(false);
    }

    public File getFileStreamPath(String name) {
        return super.getFileStreamPath(name);
    }

    /* access modifiers changed from: private */
    public void gotSensorReading(Intent intent) {
        double[] reading;
        Intent intent2 = intent;
        boolean isNotificationSuccessful = intent2.getBooleanExtra("isNotificationSuccess", false);
        String notificationReason = intent2.getStringExtra("reason");
        String errorMessage = intent2.getStringExtra(NotificationCompat.CATEGORY_ERROR);
        if (!isNotificationSuccessful) {
            this.tv_progressCount.setEnabled(false);
            this.tv_progressCount.setText("");
            this.pbProgressBar.setVisibility(4);
            MethodsFactory.logMessage(notificationReason, errorMessage);
        } else if (!notificationReason.equals("gotData")) {
            this.btnScan.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.orange));
            this.btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.orange));
            this.btnScan.setEnabled(true);
            this.btnViewScan.setEnabled(true);
            this.notifications_count = 0;
        } else {
            this.notifications_count++;
            if (this.isScanBG) {
                if (this.notifications_count % 3 == 0) {
                    enableButtonAndView(true);
                    this.isWaitingForBackGroundReading = false;
                    this.textScan.setText("Scan");
                    this.btnBackground.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.orange));
                    this.btnBackground.setEnabled(true);
                }
                return;
            }
            double[] reading2 = intent2.getDoubleArrayExtra("data");
            if (reading2 == null) {
                MethodsFactory.logMessage(this.TAG, "Reading is NULL.");
                enableButtonAndView(true);
                this.notifications_count = 0;
                return;
            }
            int middleOfArray = reading2.length / 2;
            double[] x_reading = new double[middleOfArray];
            double[] y_reading = new double[middleOfArray];
            for (int i = 0; i < middleOfArray; i++) {
                y_reading[i] = reading2[i];
                x_reading[i] = reading2[middleOfArray + i];
            }
            if (y_reading.length <= 0 || x_reading.length <= 0) {
                reading = reading2;
            } else {
                dbReading newReading = new dbReading();
                newReading.setReading(y_reading, x_reading);
                GlobalVariables.gAllSpectra.add(newReading);
                enableButtonAndView(true);
                this.isWaitingForSensorReading = false;
                reading = reading2;
                if (((double) this.count) >= Double.valueOf(Double.parseDouble(this.tx_numberOfRuns.getText().toString())).doubleValue()) {
                    this.tv_progressCount.setEnabled(false);
                    this.tv_progressCount.setText("");
                    this.pbProgressBar.setVisibility(4);
                    this.tv_progressCount.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.Button_Disabled));
                    this.btnScan.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.orange));
                    this.btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.orange));
                    this.btnScan.setEnabled(true);
                    this.btnViewScan.setEnabled(true);
                    this.btnBackground.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.orange));
                    this.btnBackground.setEnabled(true);
                    this.textScan.setText("Scan");
                    this.count = 1;
                } else if (this.isStopEnabled) {
                    this.count = 1;
                    this.tv_progressCount.setEnabled(false);
                    this.tv_progressCount.setText("");
                    this.pbProgressBar.setVisibility(4);
                    this.isStopEnabled = false;
                    return;
                } else {
                    this.btnScan.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.orange));
                    this.btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.orange));
                    this.btnScan.setEnabled(true);
                    this.btnViewScan.setEnabled(true);
                    this.btnBackground.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.orange));
                    this.btnBackground.setEnabled(true);
                    this.textScan.setText("Scan");
                    this.count++;
                    updateProgressValue(this.count);
                    this.btnScan.performClick();
                }
            }
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Sensor Reading Length = ");
            sb.append(reading.length);
            MethodsFactory.logMessage(str, sb.toString());
        }
    }

    private void sendAbsorbanceCommand() {
        System.out.println("inside sendAbsorbanceCommand");
        if (this.isWaitingForSensorReading) {
            MethodsFactory.logMessage(this.TAG, "Still waiting for sensor reading ... ");
            return;
        }
        this.isWaitingForSensorReading = true;
        askForSensorReading();
    }

    private void sendBackgroundCommand() {
        System.out.println("inside sendBackgroundCommand");
        if (this.isWaitingForBackGroundReading) {
            MethodsFactory.logMessage(this.TAG, "Still waiting for sensor reading ... ");
            return;
        }
        this.isWaitingForBackGroundReading = true;
        askForBackGroundReading();
    }

    private void askForSensorReading() {
        if (this.scanPresenter == null) {
            this.scanPresenter = new ScanPresenter();
        }
        if (GlobalVariables.bluetoothAPI == null || !GlobalVariables.bluetoothAPI.isDeviceConnected()) {
            MethodsFactory.showAlertMessage(this, "Device not connected", "Please! Ensure that you have a connected device firstly");
            Intent iMain = new Intent(this.mContext, ConnectActivity.class);
            iMain.setFlags(268468224);
            startActivity(iMain);
            return;
        }
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("error_sensor_reading= ");
        sb.append(error_sensor_reading);
        printStream.println(sb.toString());
        if (error_sensor_reading) {
            MethodsFactory.showAlertMessage(this, "Error in sensor reading", "Sorry,You have a problem with your Bluetooth version!");
        } else {
            this.scanPresenter.requestSensorReading(this.mSeekBar.getProgress());
        }
    }

    private void askForBackGroundReading() {
        if (this.scanPresenter == null) {
            this.scanPresenter = new ScanPresenter();
        }
        if (GlobalVariables.bluetoothAPI == null || !GlobalVariables.bluetoothAPI.isDeviceConnected()) {
            MethodsFactory.showAlertMessage(this, "Device not connected", "Please! Ensure that you have a connected device firstly");
            Intent iMain = new Intent(this.mContext, ConnectActivity.class);
            iMain.setFlags(268468224);
            startActivity(iMain);
            return;
        }
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("error_sensor_reading= ");
        sb.append(error_sensor_reading);
        printStream.println(sb.toString());
        if (error_sensor_reading) {
            System.out.println("error_sensor_reading==true");
            MethodsFactory.showAlertMessage(this, "Error in background reading", "Sorry,You have a problem with your Bluetooth version!");
            this.btnScan.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.orange));
            this.btnScan.setEnabled(false);
            this.btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(this.mContext, C1284R.color.orange));
            this.btnViewScan.setEnabled(false);
            return;
        }
        this.scanPresenter.requestBackgroundReading(this.mSeekBar.getProgress());
    }

    /* access modifiers changed from: private */
    public void endActivity() {
        GlobalVariables.bluetoothAPI = null;
        startActivity(new Intent(this, ConnectActivity.class));
    }
}
