package com.si_ware.neospectra.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.p001v4.app.ActivityCompat;
import android.support.p001v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.widget.TextView;
import com.si_ware.neospectra.C1284R;
import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.Global.MethodsFactory;
import java.io.File;
import java.io.PrintStream;

public class PopupActivity extends Activity {
    int fileIterator = 1;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String intentName = intent.getStringExtra("iName");
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append("Action: ");
            sb.append(intent.getAction());
            printStream.println(sb.toString());
            if (((intentName.hashCode() == -1962934264 && intentName.equals("MemoryScanData")) ? (char) 0 : 65535) == 0) {
                double[] reading = intent.getDoubleArrayExtra("data");
                if (reading == null) {
                    MethodsFactory.logMessage("PopupProgress", "Reading is NULL.");
                    return;
                }
                int middleOfArray = reading.length / 2;
                double[] x_reading = new double[middleOfArray];
                double[] y_reading = new double[middleOfArray];
                for (int i = 0; i < middleOfArray; i++) {
                    y_reading[i] = reading[i];
                    x_reading[i] = reading[middleOfArray + i];
                }
                double[] x_reading2 = MethodsFactory.switch_NM_CM(x_reading);
                double[] y_reading2 = MethodsFactory.convertDataToT(y_reading);
                PopupActivity.this.isWriteStoragePermissionGranted();
                if (y_reading2.length > 0 && x_reading2.length > 0) {
                    String filesName = PopupActivity.this.getIntent().getStringExtra("FilesName");
                    if (filesName.isEmpty() || filesName == null) {
                        filesName = GlobalVariables.SPECTRUM_DEFAULT_PATH_TEMPLATE;
                    }
                    String str = GlobalVariables.OutputDirectory;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(filesName);
                    sb2.append(GlobalVariables.SPECTRUM_REFL_PATH_TEMPLATE);
                    sb2.append(String.format("%03d", new Object[]{Integer.valueOf(PopupActivity.this.fileIterator)}));
                    sb2.append(GlobalVariables.SPECTRUM);
                    MethodsFactory.writeGraphFile(x_reading2, y_reading2, str, sb2.toString(), "x_Axis:Wavelength (nm)\ty_Axis:%Reflectance");
                }
                if (PopupActivity.this.fileIterator < PopupActivity.this.maxFileNum) {
                    PopupActivity.this.fileIterator++;
                    PopupActivity.this.updateProgressValue(PopupActivity.this.fileIterator);
                    if (GlobalVariables.bluetoothAPI != null) {
                        GlobalVariables.bluetoothAPI.sendScansHistoryRequest(PopupActivity.this.fileIterator);
                    }
                } else {
                    PopupActivity.this.finish();
                }
            }
        }
    };
    TextView mTextProgressValue;
    int maxFileNum = 0;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C1284R.layout.popup_progress);
        this.mTextProgressValue = (TextView) findViewById(C1284R.C1286id.popup_progressValue);
        TextView textView = this.mTextProgressValue;
        StringBuilder sb = new StringBuilder();
        sb.append("0/");
        sb.append(getIntent().getStringExtra("NumberOfSavedSpectra"));
        textView.setText(sb.toString());
        this.maxFileNum = Integer.valueOf(getIntent().getStringExtra("NumberOfSavedSpectra")).intValue();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout((int) (((double) dm.widthPixels) * 0.85d), (int) (((double) dm.heightPixels) * 0.15d));
        updateProgressValue(this.fileIterator);
        if (GlobalVariables.bluetoothAPI != null) {
            GlobalVariables.bluetoothAPI.sendScansHistoryRequest(this.fileIterator);
        }
    }

    /* access modifiers changed from: private */
    public void updateProgressValue(int value) {
        String[] strings = this.mTextProgressValue.getText().toString().split("/");
        strings[0] = String.valueOf(value);
        TextView textView = this.mTextProgressValue;
        StringBuilder sb = new StringBuilder();
        sb.append(strings[0]);
        sb.append("/");
        sb.append(strings[1]);
        textView.setText(sb.toString());
    }

    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mMessageReceiver, new IntentFilter(GlobalVariables.HOME_INTENT_ACTION));
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mMessageReceiver);
    }

    public void createOutDir(String dir) {
        File file = new File(Environment.getExternalStoragePublicDirectory(dir).toString());
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("************************* Path : ");
        sb.append(dir);
        printStream.println(sb.toString());
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public boolean isReadStoragePermissionGranted() {
        if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 3);
        return false;
    }

    public boolean isWriteStoragePermissionGranted() {
        if (VERSION.SDK_INT < 23) {
            createOutDir(GlobalVariables.OutputDirectory);
            return true;
        } else if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            createOutDir(GlobalVariables.OutputDirectory);
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 2);
            return false;
        }
    }
}
