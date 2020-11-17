package com.si_ware.neospectra.Activities;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.si_ware.neospectra.Activities.Interfaces.DataPage.DataPageFragment;
import com.si_ware.neospectra.Activities.Interfaces.ExportPage.ExportPageFragment;
import com.si_ware.neospectra.Activities.Interfaces.LocationPage.LocationPageFragment;
import com.si_ware.neospectra.Activities.Interfaces.RecomendationPage.RecomendationPageFragment;
import com.si_ware.neospectra.Activities.Interfaces.ScanPage.ScanPageFragment;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3API;
import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.R;
import com.si_ware.neospectra.Scan.Presenter.ScanPresenter;

import static com.si_ware.neospectra.Global.GlobalVariables.bluetoothAPI;
import static com.si_ware.neospectra.Global.GlobalVariables.gApodizationFunction;
import static com.si_ware.neospectra.Global.GlobalVariables.gCorrectionMode;
import static com.si_ware.neospectra.Global.GlobalVariables.gFftPoints;
import static com.si_ware.neospectra.Global.GlobalVariables.gInterpolationPoints;
import static com.si_ware.neospectra.Global.GlobalVariables.gIsFftEnabled;
import static com.si_ware.neospectra.Global.GlobalVariables.gIsInterpolationEnabled;
import static com.si_ware.neospectra.Global.GlobalVariables.gOpticalGainSettings;
import static com.si_ware.neospectra.Global.GlobalVariables.gOpticalGainValue;
import static com.si_ware.neospectra.Global.GlobalVariables.gRunMode;
import static com.si_ware.neospectra.Global.GlobalVariables.progressBarPosition;
import static com.si_ware.neospectra.Global.GlobalVariables.scanTime;

public class MainPage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Context mContext;
    MenuInflater menuInflater;
    boolean isExportPage = false;
    boolean isDataUnsur = false;
    final int share_item_id = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_keyboard_arrow_left);

        menuInflater = getMenuInflater();

        setContentView(R.layout.activity_main_page);

        loadFragment(new ScanPageFragment());
        BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

//         Create new instance from bluetoothApi if it null
        if (bluetoothAPI == null) {
            bluetoothAPI = new SWS_P3API(this, mContext);
        }

        mContext = this;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

//        // Get all needed configuration settings
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        gRunMode = preferences.getString("run_mode", GlobalVariables.runMode.Single_Mode.toString());
        gIsInterpolationEnabled = preferences.getBoolean("linear_interpolation_switch", false);
        gInterpolationPoints = preferences.getString("data_points", GlobalVariables.pointsCount.points_257.toString());
        gIsFftEnabled = preferences.getBoolean("fft_settings_switch", false);
        gApodizationFunction = preferences.getString("apodization_function", GlobalVariables.apodization.Boxcar.toString());
        gFftPoints = preferences.getString("fft_points", GlobalVariables.zeroPadding.points_8k.toString());
        gOpticalGainSettings = preferences.getString("optical_gain_settings", "Default");
        gOpticalGainValue = preferences.getInt(gOpticalGainSettings, 0);
        gCorrectionMode = preferences.getString("wavelength_correction", GlobalVariables.wavelengthCorrection.Self_Calibration.toString());
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Menu menu = null;
        Fragment fragment = null;
        AppCompatActivity appCompatActivity = null;
        switch (menuItem.getItemId()) {
            case R.id.scan_page:
                fragment = new ScanPageFragment();
                isDataUnsur = false;
                isExportPage = false;
                break;
            case R.id.data_page:
                fragment = new DataPageFragment();
                isDataUnsur = true;
                isExportPage = false;
                break;
            case R.id.location_page:
                fragment = new LocationPageFragment();
                isDataUnsur = false;
                isExportPage = false;
                break;
            case R.id.recomendation_page:
                fragment = new RecomendationPageFragment();
                isDataUnsur = false;
                isExportPage = false;
                break;
            case R.id.export_page:
                fragment = new ExportPageFragment();
                isDataUnsur = false;
                isExportPage = true;
                break;
        }
        loadFragment(fragment);
        return true;
    }

    @Override
    public void onResume() {
        //comment or uncomment this function to test the aplication without connect to device
        if (bluetoothAPI != null) {
            if (!bluetoothAPI.isDeviceConnected()) {
                endActivity();
                return;
            }
        }
        super.onResume();
    }

    private void endActivity() {
        bluetoothAPI = null;
        Intent mIntent = new Intent(this, IntroActivity.class);
        startActivity(mIntent);
    }
}
