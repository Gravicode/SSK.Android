package com.si_ware.neospectra.Activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Wave;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3API;
import com.si_ware.neospectra.PredictionEngine;
import com.si_ware.neospectra.R;
import com.si_ware.neospectra.Data.ResultPrediction;

import static com.si_ware.neospectra.Activities.ConnectActivity.hasPermissions;
import static com.si_ware.neospectra.Global.GlobalVariables.bluetoothAPI;


public class IntroActivity extends AppCompatActivity {

    Button btnConnect;

    static private Context mContext;


    final int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        mContext = this;

        bluetoothAPI = new SWS_P3API(this, mContext);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Enable Bluetooth Adapter
        mBluetoothAdapter.enable();

        btnConnect = findViewById(R.id.btnConnect);

        checkStoragePermission();

        // Request permission if it was not enable
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send();
                Intent connectActivity = new Intent(IntroActivity.this, SettingsActivity.class);
                startActivity(connectActivity);
            }
        });
    }


    //for testing purpose
    public void send() {
        // sample static data

        double[] reflectance = new double[]{22.03331267,22.35729641,22.73445873,23.11636747,23.5179432,23.94717988,24.34484381,24.64338166,24.86704238,25.11317606,25.42344988,25.72185653,25.91404808,26.0137858,26.1171252,26.26886757,26.41679261,26.51308049,26.59533363,26.70587531,26.76650692,26.62477288,26.25090212,25.82984843,25.62394736,25.75372599,26.12924266,26.58860938,27.04150017,27.45384854,27.76501754,27.89982819,27.85806252,27.73014271,27.60878198,27.51392069,27.41433077,27.29580778,27.17629624,27.06429248,26.93675851,26.76739155,26.55543193,26.31443504,26.05150092,25.77276843,25.49376238,25.22535988,24.96028963,24.69331151,24.44020786,24.19892461,23.8976084,23.45303264,22.94137728,22.67871395,23.01842494,23.99174723,25.20745233,26.17536489,26.69339626,26.90807499,27.05605731,27.2077227,27.28419168,27.2489937,27.18426787,27.17738616,27.2096805,27.20778521,27.16207585,27.13592556,27.16979896,27.22884271,27.26101208,27.26440109,27.26848533,27.27876804,27.2795072,27.27864971,27.30495901,27.3530633,27.37143978,27.32964094,27.27029942,27.2618033,27.31048664,27.35627443,27.35562172,27.32905125,27.3128575,27.29899542,27.25710343,27.19751972,27.17010391,27.19657952,27.23799345,27.24794051,27.23094449,27.22209807,27.2274759,27.21794705,27.18269621,27.15515574,27.16964511,27.21656438,27.25844453,27.2760981,27.27777077,27.27150842,27.25738415,27.24561002,27.25572758,27.28294656,27.28551053,27.23213833,27.14802969,27.08966487,27.07575382,27.07068938,27.04478484,27.01889641,27.02830741,27.05786093,27.05109108,26.98579183,26.9080425,26.88314684,26.92463606,26.98589956,27.01027294,26.96853216,26.86309944,26.72189483,26.58506788,26.47660829,26.38615591,26.29641411,26.23448962,26.26813406,26.42707477,26.65193504,26.85497009,27.01661089,27.18195846,27.36494351,27.51051098,27.58342699,27.64125712,27.76546435,27.94027226,28.0547838,28.05028384,28.00373787};

        if (reflectance == null) {
            Toast.makeText(this, "Sorry can't connect to device", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            ResultPrediction[] predicts = PredictionEngine.DoInference(reflectance, this);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Sorry we got some error :" + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }


    }

    private boolean checkStoragePermission() {
        //check if storage permission is enabled or not and return true/false
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

        // Check self permission if enabled or not
        public static boolean hasPermissions(Context context, String... permissions) {
            if (context != null && permissions != null) {
                for (String permission : permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
            return true;
        }
}