package com.si_ware.neospectra.Activities.Interfaces.ScanPage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.balittanah.gravicode.pkdss.AkimaInterpolator;
import com.balittanah.gravicode.pkdss.FertilizerInfo;
import com.balittanah.gravicode.pkdss.SplineInterpolator;
import com.balittanah.gravicode.pkdss.SplineInterpolator2;
import com.github.ybq.android.spinkit.style.Wave;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.si_ware.neospectra.Activities.ConfigureActivity;
import com.si_ware.neospectra.Activities.Interfaces.MyObj;
import com.si_ware.neospectra.Activities.IntroActivity;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3API;
import com.si_ware.neospectra.Data.ConfigurableProperties;
import com.si_ware.neospectra.Data.DataElements;
import com.si_ware.neospectra.Data.DataRaw;
import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.Models.dbReading;
import com.si_ware.neospectra.PredictionEngine;
import com.si_ware.neospectra.Data.OutputData;
import com.si_ware.neospectra.R;
import com.si_ware.neospectra.Data.ResultPrediction;
import com.si_ware.neospectra.Scan.Presenter.ScanPresenter;

import org.apache.commons.math3.analysis.interpolation.AkimaSplineInterpolator;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.TimeZone;
import java.util.prefs.Preferences;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.balittanah.gravicode.pkdss.FertilizerCalculator;
import com.si_ware.neospectra.dbtable.DBHelper;

import static com.si_ware.neospectra.Global.GlobalVariables.MAX_SCANNER_MEMORY;
import static com.si_ware.neospectra.Global.GlobalVariables.bluetoothAPI;
import static com.si_ware.neospectra.Global.GlobalVariables.gAllSpectra;
import static com.si_ware.neospectra.Global.GlobalVariables.progressBarPosition;
import static com.si_ware.neospectra.Global.GlobalVariables.scanTime;
import static com.si_ware.neospectra.Global.MethodsFactory.logMessage;
import static com.si_ware.neospectra.Global.MethodsFactory.rotateProgressBar;
import static com.si_ware.neospectra.Global.MethodsFactory.showAlertMessage;
import static com.si_ware.neospectra.Global.GlobalVariables.gApodizationFunction;
import static com.si_ware.neospectra.Global.GlobalVariables.gCorrectionMode;
import static com.si_ware.neospectra.Global.GlobalVariables.gFftPoints;
import static com.si_ware.neospectra.Global.GlobalVariables.gInterpolationPoints;
import static com.si_ware.neospectra.Global.GlobalVariables.gIsFftEnabled;
import static com.si_ware.neospectra.Global.GlobalVariables.gIsInterpolationEnabled;
import static com.si_ware.neospectra.Global.GlobalVariables.gOpticalGainSettings;
import static com.si_ware.neospectra.Global.GlobalVariables.gOpticalGainValue;
import static com.si_ware.neospectra.Global.GlobalVariables.gRunMode;

public class ScanPageFragment extends Fragment {

    @NonNull
    private String TAG = "Main Activity";
    private Context mContext;
    DataRaw dataRaw;
    GraphView mGraphView;
    CardView btnRefresh, btnBackground, btnScan, btnProcess, btnClearRecord;
    Spinner edtResolution, edtOptical;
    private LinearLayout lProgress;
    private RelativeLayout lUtama;
    public boolean isScanBG = false;
    public int backgroundScanTime = 2;
    private ProgressBar pbProgressBar;
    private boolean isWaitingForBackGroundReading = false;
    ScanPresenter scanPresenter;
    public static boolean error_sensor_reading = false;
    private SeekBar mSeekBar;
    private TextView textScan;
    private EditText tx_numberOfRuns;
    private int count = 1;
    private boolean isWaitingForSensorReading = false;
    private boolean isStopEnabled = false;
    private TextView tv_progressCount, tv_progressValue, tv_scanRecord;
    private double maxValue = 0;
    private static int measurementCount_Spectroscopy = 0;
    private static int colors[] = {0xFFFF0000, 0xFF0000FF, 0xFF00FF00, 0xFF00FFFF, 0xFFFF007F, 0xFF7F00FF, 0xFFFF00FF, 0xFFFFFF00, 0xFF007FFF, 0xFFFF7F00, 0xFF00FF7F, 0xFF7FFF00};
    private int notifications_count = 0;
    public boolean loading = false;
    public double[] reading;
    ProgressBar progressBar;
    private double[] ySend;
    private MyObj myObj = new MyObj();
    private float[] yValsnew;
    private String bray, ca, clay, cn, hclk2o, hclp2o5, jumlah, k, kbadj, kjelhal, ktk, mg, morgan, na, olsen, phh2o, phkcl, retensip,
            sand, silt, wbc;
    private DBHelper dbHelper;

    String[] Resolution = {"1", "2", "3"};
    String[] Optical = {"2", "4", "6", "8", "10"};

    RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if(dataRaw==null)dataRaw = new DataRaw();
        super.onViewCreated(view, savedInstanceState);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_keyboard_arrow_left);

        queue = Volley.newRequestQueue(getActivity());
        queue.getCache().clear();

        dbHelper = new DBHelper(getActivity());

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter(GlobalVariables.HOME_INTENT_ACTION));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter(GlobalVariables.INTENT_ACTION));

        Toolbar toolbar = view.findViewById(R.id.titlebar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        //comment or uncomment this function to test the aplication without connect to device
        if (bluetoothAPI == null) {
            bluetoothAPI = new SWS_P3API(getActivity(), mContext);
        }

        /*if(bluetoothAPI != null)
        {
            bluetoothAPI.setHomeContext(this.mContext);
            bluetoothAPI.sendMemoryRequest();
            bluetoothAPI.sendBatRequest();

            /*Intent intent = null;
            String intentName = intent.getStringExtra("iName");

            switch (intentName) {
                case "Memory":
                    logMessage("MainPage", "Intent Received:\n" +
                            "Name: " + intent.getStringExtra("iName") + "\n" +
                            "Success: " + intent.getBooleanExtra("isNotificationSuccess", false) + "\n" +
                            "Reason: " + intent.getStringExtra("reason") + "\n" +
                            "Error: " + intent.getStringExtra("err") + "\n" +
                            "data : " + intent.getLongExtra("data", 0) + "\n");
                    tv_scanRecord.setTextSize(15);
                    tv_scanRecord.setText("" + intent.getLongExtra("data", 0) + "/"
                            + MAX_SCANNER_MEMORY);
                    break;
            }
        }*/

        mContext = getActivity();

        scanPresenter = new ScanPresenter();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        ArrayAdapter<String> itemResolution = new ArrayAdapter<String>
                (getActivity(), R.layout.support_simple_spinner_dropdown_item, Resolution);
        ArrayAdapter<String> itemOptical = new ArrayAdapter<String>
                (getActivity(), R.layout.support_simple_spinner_dropdown_item, Optical);

        mGraphView = view.findViewById(R.id.graph);
        btnRefresh = view.findViewById(R.id.btnRefresh);
        btnBackground = view.findViewById(R.id.btnBackground);
        btnScan = view.findViewById(R.id.btnScan);
        btnProcess = view.findViewById(R.id.btnProcess);
        btnClearRecord = view.findViewById(R.id.btnClearRecord);
        edtResolution = view.findViewById(R.id.edtResolution);
        edtOptical = view.findViewById(R.id.edtOptical);
        pbProgressBar = view.findViewById(R.id.progressBarMain);
        textScan = view.findViewById(R.id.text_scan);
        progressBar = view.findViewById(R.id.spin_kit);
        tv_scanRecord = view.findViewById(R.id.tv_memory_percentage);
        lProgress = view.findViewById(R.id.lProgressbar);
        lUtama = view.findViewById(R.id.lUtama);
        lProgress.setVisibility(View.GONE);
        lUtama.setVisibility(View.VISIBLE);
        textScan.setText("Scan");
        tx_numberOfRuns = view.findViewById(R.id.tx_numberOfRuns);
        tv_progressCount = view.findViewById(R.id.countProgress);
        tv_progressValue = view.findViewById(R.id.tv_progress);
        tv_progressValue.setText("" + scanTime);

        edtResolution.setAdapter(itemResolution);
        edtOptical.setAdapter(itemOptical);


        btnRefresh.setEnabled(false);
        btnRefresh.setCardBackgroundColor(Color.parseColor("#0A376A"));
        btnScan.setEnabled(false);
        btnScan.setCardBackgroundColor(Color.parseColor("#0A376A"));
        btnProcess.setEnabled(false);
        btnProcess.setCardBackgroundColor(Color.parseColor("#0A376A"));

        // Get all needed configuration settings
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

        ArrayAdapter<CharSequence> bluetoothSpinnerAdapter = ArrayAdapter.createFromResource(mContext,
                R.array.BLE_Services, android.R.layout.simple_spinner_item);
        bluetoothSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        enableButtonAndView(PredictionEngine.HasBackgroundScan);

        // Adjust progress value if scan time changed from its default value
        if (scanTime != 2) {
            tv_progressValue.setX(progressBarPosition / 2);
        }

        pbProgressBar.setVisibility(View.INVISIBLE);

        tv_progressCount.setBackgroundColor(Color.parseColor("#0A376A"));
        tv_progressCount.setEnabled(false);

        btnClearRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder myAlert = new android.support.v7.app.AlertDialog
                        .Builder(mContext);
                myAlert.setTitle("Clear Record");
                myAlert.setMessage("Are you sure you want to clear all record?");
                myAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {

                        if (bluetoothAPI != null)
                            bluetoothAPI.sendClearMemoryRequest();

                        dialogInterface.dismiss();

                        Toast.makeText(getActivity(), "Clear record succes", Toast.LENGTH_SHORT).show();
                    }
                });
                myAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                myAlert.show();
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fungsi refresh
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);

                bottomSheetDialog.setCanceledOnTouchOutside(false);

                LinearLayout bottom_sheet_refresh = bottomSheetDialog.findViewById(R.id.bottom_sheet_refresh);
                CardView btn_yes = bottomSheetDialog.findViewById(R.id.btn_yes);
                CardView btn_cancel = bottomSheetDialog.findViewById(R.id.btn_cancel);
                bottom_sheet_refresh.setVisibility(View.VISIBLE);

                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        fungsi yes pada bottom sheet
                        maxValue = 0;
                        if (!mGraphView.getSeries().isEmpty())
                            mGraphView.removeAllSeries();
                        mGraphView.getViewport().setMinY(90);
                        mGraphView.getViewport().setMaxY(110);
                        mGraphView.getViewport().setMinX(1100);
                        mGraphView.getViewport().setMaxX(2650);
                        mGraphView.getViewport().setScalable(false);
                        mGraphView.getViewport().setScrollable(false);
                        mGraphView.getViewport().setScalableY(false);
                        mGraphView.getViewport().setScrollableY(false);
                        gAllSpectra.clear();

                        measurementCount_Spectroscopy = 0;
                        tx_numberOfRuns.setText("1");
                        btnRefresh.setEnabled(false);
                        btnRefresh.setCardBackgroundColor(Color.parseColor("#0A376A"));
                        btnScan.setEnabled(false);
                        btnScan.setCardBackgroundColor(Color.parseColor("#0A376A"));
                        btnProcess.setEnabled(false);
                        btnProcess.setCardBackgroundColor(Color.parseColor("#0A376A"));
                        btnBackground.setEnabled(true);
                        btnBackground.setCardBackgroundColor(Color.parseColor("#1d86ff"));
                        progressBar.setVisibility(View.GONE);
                        lProgress.setVisibility(View.GONE);
                        lUtama.setVisibility(View.VISIBLE);
                        bottomSheetDialog.hide();

                        Toast.makeText(getActivity(), "Refresh succes", Toast.LENGTH_SHORT).show();

                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        fungsi cancel bottom sheet
                        bottomSheetDialog.hide();
                    }
                });
                bottomSheetDialog.show();
            }
        });

        btnBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                fungsi button background
                tx_numberOfRuns.setText(edtResolution.getSelectedItem().toString());

                btnScan.setCardBackgroundColor(Color.parseColor("#0A376A"));
                btnScan.setEnabled(false);
                btnClearRecord.setCardBackgroundColor(Color.parseColor("#0A376A"));
                btnClearRecord.setEnabled(false);
                btnProcess.setCardBackgroundColor(Color.parseColor("#0A376A"));
                btnProcess.setEnabled(false);
                btnBackground.setCardBackgroundColor(Color.parseColor("#0A376A"));
                btnBackground.setEnabled(false);
                btnRefresh.setCardBackgroundColor(Color.parseColor("#1D86FF"));
                btnRefresh.setEnabled(true);

                Wave wave = new Wave();
                lProgress.setVisibility(View.VISIBLE);
                lUtama.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminateDrawable(wave);

                isScanBG = true;

                tv_progressCount.setEnabled(false);
                tv_progressCount.setText("");
                backgroundScanTime = scanTime;
                pbProgressBar.setVisibility(View.INVISIBLE);

                // Request background reading from device
                sendBackgroundCommand();
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                fungsi scan
                tx_numberOfRuns.setText(edtResolution.getSelectedItem().toString());

                if (textScan.getText() == "Scan") {

                    if (backgroundScanTime < scanTime) {
                        logMessage(TAG, "Material Scan time is greater than reference material scan time ");
                        showAlertMessage(mContext, "Error", "Material scan time should be less than or equal reference scan time");
                        return;
                    }

                    isScanBG = false;

                    Wave wave = new Wave();
                    lProgress.setVisibility(View.VISIBLE);
                    lUtama.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminateDrawable(wave);

                    textScan.setText("Stop");

                    btnScan.setCardBackgroundColor(Color.parseColor("#ff1d21"));

                    btnRefresh.setEnabled(true);
                    btnRefresh.setCardBackgroundColor(Color.parseColor("#1D86FF"));

                    btnProcess.setEnabled(true);
                    btnProcess.setCardBackgroundColor(Color.parseColor("#1D86FF"));

                    btnBackground.setCardBackgroundColor(Color.parseColor("#0A376A"));
                    btnBackground.setEnabled(false);


                    if (Double.parseDouble(tx_numberOfRuns.getText().toString()) > 1) {
                        pbProgressBar.setVisibility(View.VISIBLE);
                        rotateProgressBar(mContext, pbProgressBar);
                    }
                    if (count == 1)
                        tv_progressCount.setText("1/" + tx_numberOfRuns.getText().toString());
                    tv_progressCount.setTextColor(Color.BLACK);

                    // Request scan reading from the device
                    sendAbsorbanceCommand();


                } else {
                    // Handling stop request
                    btnScan.setCardBackgroundColor(Color.parseColor("#0A376A"));
                    btnScan.setEnabled(false);
                    tv_progressCount.setEnabled(false);
                    pbProgressBar.setVisibility(View.INVISIBLE);
                    btnBackground.setCardBackgroundColor(Color.parseColor("#0A376A"));
                    btnBackground.setEnabled(false);
                    isStopEnabled = true;


                }
            }
        });

        btnProcess.setOnClickListener(v -> {
//                fungsi process
            tx_numberOfRuns.setText(edtResolution.getSelectedItem().toString());
            final BottomSheetDialog bottomSheetDialog2 = new BottomSheetDialog(getActivity());
            bottomSheetDialog2.setContentView(R.layout.bottom_sheet_dialog);

            bottomSheetDialog2.setCanceledOnTouchOutside(false);

            LinearLayout bottom_sheet_process = bottomSheetDialog2.findViewById(R.id.bottom_sheet_process);
            CardView btn_yes2 = bottomSheetDialog2.findViewById(R.id.btn_yes2);
            CardView btn_cancel2 = bottomSheetDialog2.findViewById(R.id.btn_cancel2);
            bottom_sheet_process.setVisibility(View.VISIBLE);

            btn_yes2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        fungsi yes pada bottom sheet
                    send();
                    bottomSheetDialog2.hide();
                }
            });

            btn_cancel2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        fungsi cancel bottom sheet
                    bottomSheetDialog2.hide();
                    tx_numberOfRuns.setText("1");
                }
            });
            bottomSheetDialog2.show();
        });

    }


    private void updateProgressValue(int value) {
        String[] strings = tv_progressCount.getText().toString().split("/");

        strings[0] = String.valueOf(value);
        tv_progressCount.setText(strings[0] + "/" + strings[1]);
        tv_progressCount.setTextColor(Color.BLACK);

    }

    @Override
    public void onResume() {
        //Register the broadcast receiver
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter(GlobalVariables.HOME_INTENT_ACTION));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter(GlobalVariables.INTENT_ACTION));

        //comment or uncomment this function to test the aplication without connect to device
        if (bluetoothAPI != null) {
            if (!bluetoothAPI.isDeviceConnected()) {
                endActivity();
                return;
            }
        }
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }

    // Handling of broadcast receiver
    @NonNull
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String intentName = intent.getStringExtra("iName");

            switch (intentName) {
                //Case data is received successfully
                case "sensorNotification_data":
                    gotSensorReading(intent);
                    logMessage(TAG, "Intent Received:\n" +
                            "Name: " + intent.getStringExtra("iName") + "\n" +
                            "Success: " + intent.getBooleanExtra("isNotificationSuccess", false) + "\n" +
                            "Reason: " + intent.getStringExtra("reason") + "\n" +
                            "Error: " + intent.getStringExtra("err") + "\n" +
                            "data : " + Arrays.toString(intent.getDoubleArrayExtra("data")) + "\n");
                    String s = "{\"Reflectance\":" + Arrays.toString(intent.getDoubleArrayExtra("data")) + "}";
                    break;
                // Case sensor notification with failure
                case "sensorNotification_failure":
                    gotSensorReading(intent);
                    logMessage(TAG, "Intent Received:\n" +
                            "Name: " + intent.getStringExtra("iName") + "\n" +
                            "Success: " + intent.getBooleanExtra("isNotificationSuccess", false) + "\n" +
                            "Reason: " + intent.getStringExtra("reason") + "\n" +
                            "Error: " + intent.getStringExtra("err") + "\n" +
                            "data : " + String.valueOf(intent.getIntExtra("data", 0)) + "\n");
                    int errorCode = intent.getIntExtra("data", 0);
                    enableButtonAndView(true);
                    notifications_count = 0;
                    isWaitingForBackGroundReading = false;
                    isWaitingForSensorReading = false;
                    showAlertMessage(mContext, "Error", "Error " + String.valueOf(0x000000FF & errorCode) + " occurred during measurement!");
                    break;
                case "sensorWriting":

                    break;
                //Case device is disconnected
                case "Disconnection_Notification":
                    endActivity();
                    break;
                // Receive a streamed data due to a taken scan
                case "MemoryScanData":
                    reading = intent.getDoubleArrayExtra("data");

                    if (reading == null) {
                        logMessage("HomeView", "Reading is NULL.");
                        return;
                    }

                    // The array constructed from two arrays have the same length, Y, then X.
                    int middleOfArray = reading.length / 2;
                    double[] x_reading = new double[middleOfArray],
                            y_reading = new double[middleOfArray];
                    // split the main array to two arrays, Y & X
                    for (int i = 0; i < middleOfArray; i++) {
                        //Added this fix for the inconsistency in size of received data
                        y_reading[i] = reading[i];
                        x_reading[i] = reading[middleOfArray + i];
                    }

                    if ((y_reading.length > 0) && (x_reading.length > 0)) {
                        dbReading newReading = new dbReading();
                        newReading.setReading(y_reading, x_reading);
                        gAllSpectra.add(newReading);
                    }

                    break;
                // Receive a memory information
                case "Memory":
                    logMessage("MainPage", "Intent Received:\n" +
                            "Name: " + intent.getStringExtra("iName") + "\n" +
                            "Success: " + intent.getBooleanExtra("isNotificationSuccess", false) + "\n" +
                            "Reason: " + intent.getStringExtra("reason") + "\n" +
                            "Error: " + intent.getStringExtra("err") + "\n" +
                            "data : " + intent.getLongExtra("data", 0) + "\n");
                    tv_scanRecord.setTextSize(15);
                    tv_scanRecord.setText("" + intent.getLongExtra("data", 0) + "/"
                            + MAX_SCANNER_MEMORY);
                    break;
                default:
                    Log.v(TAG + "intent", "Got unknown broadcast intent");
            }
        }
    };


    // Enable/Disable main activity buttons
    private void enableButtonAndView(boolean enable) {
        if (enable) {
            btnScan.setCardBackgroundColor(Color.parseColor("#1d86ff"));
            btnScan.setEnabled(true);
            textScan.setText("Scan");
            btnBackground.setEnabled(true);
            btnBackground.setCardBackgroundColor(Color.parseColor("#1d86ff"));
            btnClearRecord.setEnabled(true);
            btnClearRecord.setCardBackgroundColor(Color.parseColor("#1d86ff"));
            progressBar.setVisibility(View.GONE);
            lProgress.setVisibility(View.GONE);
            lUtama.setVisibility(View.VISIBLE);
        } else {
            btnScan.setCardBackgroundColor(Color.parseColor("#0A376A"));
            btnScan.setEnabled(false);
            btnProcess.setCardBackgroundColor(Color.parseColor("#0A376A"));
            btnProcess.setEnabled(false);
            progressBar.setVisibility(View.GONE);
            lProgress.setVisibility(View.GONE);
            lUtama.setVisibility(View.VISIBLE);
        }
    }

    // Handling receiving data from sensor
    private void gotSensorReading(Intent intent) {
        boolean isNotificationSuccessful = intent.getBooleanExtra("isNotificationSuccess", false);
        String notificationReason = intent.getStringExtra("reason");
        String errorMessage = intent.getStringExtra("err");

        /* If the notification is unsuccessful */
        if (!isNotificationSuccessful) {
            tv_progressCount.setEnabled(false);
            tv_progressCount.setText("");
            pbProgressBar.setVisibility(View.INVISIBLE);

            logMessage(notificationReason, errorMessage);
            return;
        }

        /* If an error occured */
        if (!notificationReason.equals("gotData")) {
            btnScan.setCardBackgroundColor(Color.parseColor("#1d86ff"));
            btnScan.setEnabled(true);

            notifications_count = 0;
            return;
        }

        // Number of the received notifications
        notifications_count++;

        if (isScanBG) {
            //tunggu lebih lama untuk data point lebih banyak
            if ((notifications_count % 6) == 0) {
                enableButtonAndView(true);
                isWaitingForBackGroundReading = false;
                textScan.setText("Scan");
                btnBackground.setCardBackgroundColor(Color.parseColor("#0A376A"));
                btnBackground.setEnabled(true);
            }
            PredictionEngine.HasBackgroundScan = true;
            return;
        }

        // Get readings
        reading = intent.getDoubleArrayExtra("data");

        if (reading == null) {
            logMessage(TAG, "Reading is NULL.");
            enableButtonAndView(true);
            notifications_count = 0;
            return;
        }

        // The array constructed from two arrays have the same length, Y, then X.
        int middleOfArray = reading.length / 2;
        double[] x_reading = new double[middleOfArray],
                y_reading = new double[middleOfArray];
        // split the main array to two arrays, Y & X
        for (int i = 0; i < middleOfArray; i++) {
            //Added this fix for the inconsistency in size of received data
            y_reading[i] = reading[i];
            x_reading[i] = reading[middleOfArray + i];
        }

        // Prepare the data to get ArrayRealVector with length 314 item,
        // and set its value to the singleton sensor reading model.
        if ((y_reading.length > 0) && (x_reading.length > 0)) {
            dbReading newReading = new dbReading();
            newReading.setReading(y_reading, x_reading);
            // Add the taken read to global ArrayList which holds all the taken readings
            gAllSpectra.add(newReading);
            enableButtonAndView(isScanBG);
            isWaitingForSensorReading = false;


            Double numberOfRuns = Double.parseDouble(tx_numberOfRuns.getText().toString());

            // Enable Stop action button in case there are a multiple number of runs
            if (count < numberOfRuns) {
                if (isStopEnabled) {
                    count = 1;
                    tv_progressCount.setEnabled(false);
                    tv_progressCount.setText("");
                    pbProgressBar.setVisibility(View.INVISIBLE);
                    isStopEnabled = false;
                    return;
                }

                btnScan.setCardBackgroundColor(Color.parseColor("#1d86ff"));

                btnBackground.setCardBackgroundColor(Color.parseColor("#1d86ff"));
                btnRefresh.setCardBackgroundColor(Color.parseColor("#1d86ff"));
                btnProcess.setCardBackgroundColor(Color.parseColor("#1d86ff"));
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                textScan.setText("Scan");

                count++;
                updateProgressValue(count);
                // Press scan button again as it is a continues mode
                btnScan.performClick();
            } else// Handle only one run
            {
                tv_progressCount.setEnabled(false);
                tv_progressCount.setText("");
                pbProgressBar.setVisibility(View.INVISIBLE);
                tv_progressCount.setBackgroundColor(Color.parseColor("#0A376A"));

                btnScan.setCardBackgroundColor(Color.parseColor("#1d86ff"));
                btnScan.setEnabled(true);

                btnBackground.setCardBackgroundColor(Color.parseColor("#1d86ff"));
                btnBackground.setEnabled(true);

                btnRefresh.setCardBackgroundColor(Color.parseColor("#1d86ff"));
                btnRefresh.setEnabled(true);

                btnProcess.setCardBackgroundColor(Color.parseColor("#1d86ff"));
                btnProcess.setEnabled(true);
                textScan.setText("Scan");
                count = 1;
                displayGraph();

                Toast.makeText(getContext(), "Scan is complete", Toast.LENGTH_LONG).show();

            }
        }

        logMessage(TAG, "Sensor Reading Length = " + reading.length);

    }

    private void sendAbsorbanceCommand() {
        System.out.println("inside sendAbsorbanceCommand");
        if (isWaitingForSensorReading) {
            logMessage(TAG, "Still waiting for sensor reading ... ");
            return;
        }
        isWaitingForSensorReading = true;
        askForSensorReading();
    }

    private void sendBackgroundCommand() {
        System.out.println("inside sendBackgroundCommand");

        if (isWaitingForBackGroundReading) {
            logMessage(TAG, "Still waiting for sensor reading ... ");

            return;
        }
        isWaitingForBackGroundReading = true;

        askForBackGroundReading();
    }


    private void askForSensorReading() {
        if (scanPresenter == null) {
            scanPresenter = new ScanPresenter();
        }
        // Don't complete the process if the device not connected
        if (bluetoothAPI == null || !bluetoothAPI.isDeviceConnected()) {
            showAlertMessage(getActivity(),
                    "Device not connected",
                    "Please! Ensure that you have a connected device firstly");

            Intent iMain = new Intent(mContext, IntroActivity.class);
            iMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(iMain);
            return;
        }
        System.out.println("error_sensor_reading= " + error_sensor_reading);

        if (error_sensor_reading == true) {
            showAlertMessage(getActivity(),
                    "Error in sensor reading",
                    "Sorry,You have a problem with your Bluetooth version!");
            return;
        }
        scanPresenter.requestSensorReading(2);

    }

    private void askForBackGroundReading() {
        if (scanPresenter == null) {
            scanPresenter = new ScanPresenter();
        }
        // Don't complete the process if the device not connected
        if (bluetoothAPI == null || !bluetoothAPI.isDeviceConnected()) {
            showAlertMessage(getActivity(),
                    "Device not connected",
                    "Please! Ensure that you have a connected device firstly");
            Intent iMain = new Intent(mContext, IntroActivity.class);
            iMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(iMain);
            return;
        }
        System.out.println("error_sensor_reading= " + error_sensor_reading);
        if (error_sensor_reading == true) {
            System.out.println("error_sensor_reading==true");
            showAlertMessage(getActivity(),
                    "Error in background reading",
                    "Sorry,You have a problem with your Bluetooth version!");
            btnScan.setCardBackgroundColor(Color.parseColor("#0A376A"));
            btnScan.setEnabled(false);
            return;
        }
        scanPresenter.requestBackgroundReading(2);
    }

    private void endActivity() {
        bluetoothAPI = null;
        Intent mIntent = new Intent(getActivity(), IntroActivity.class);
        startActivity(mIntent);
    }

    public void displayGraph() {
        for (int i = 0; i < gAllSpectra.size(); ++i) {
            dbReading sensorReading = gAllSpectra.get(i);
            ArrayList<DataPoint> dataPoints = new ArrayList<DataPoint>();

            if (sensorReading != null) {
                if ((sensorReading.getXReading().length != 0) && (sensorReading.getYReading().length != 0)) {
                    double[] xVals = sensorReading.getXReading();
                    double[] yVals = sensorReading.getYReading();

                    for (int j = xVals.length - 1; j >= 0; --j) {
                        dataPoints.add(new DataPoint(1e7 / xVals[j], yVals[j] * 100));
                        if (maxValue < yVals[j] * 100)
                            maxValue = yVals[j] * 100;
                    }

                    DataPoint dataPointsArray[] = dataPoints.toArray(new DataPoint[dataPoints.size()]);
                    Log.e("debugger", Arrays.toString(dataPointsArray));

                    LineGraphSeries<DataPoint> series = new LineGraphSeries(dataPointsArray);
                    series.setThickness(4);
                    series.setColor(colors[i % 12]);
                    series.setTitle("Meas. " + String.valueOf(i + 1));
                    mGraphView.addSeries(series);


                }
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.configure, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            AlertDialog.Builder myAlert = new AlertDialog.Builder(mContext);
            android.support.v7.app.AlertDialog.Builder myAlert = new android.support.v7.app.AlertDialog
                    .Builder(mContext);
            myAlert.setTitle("Disconnect");
            myAlert.setMessage("Are you sure you want to disconnect the device?");
            myAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    // Request of bluetooth disconnection
                    if (bluetoothAPI != null) {
                        bluetoothAPI.disconnectFromDevice();

                        Intent iMain = new Intent(mContext, IntroActivity.class);
                        iMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(iMain);
                        getActivity().finish();
                    }

                }
            });
            myAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            myAlert.show();
        }

        if (item.getItemId() == R.id.configure) {
            Intent iMain = new Intent(getActivity(), ConfigureActivity.class);
            startActivity(iMain);
        }
        return super.onOptionsItemSelected(item);
    }


    public void send() {
        // sample static data
//        double[] reflectance = new double[]{1.4923595577160977, 1.4858127248658966, 1.483462090254477, 1.4811798041653559, 1.478962913306713, 1.4855769966447907, 1.4980369452002196, 1.5107358150374268, 1.5237569761463843, 1.5439514620142998, 1.5649235190483481, 1.5867448877083867, 1.6051176087587002, 1.6177447487272167, 1.6306515729737083, 1.6438533274801315, 1.6400895455127638, 1.632582440983541, 1.6252176612788953, 1.6198725907564453, 1.6213354492364858, 1.6228053264183926, 1.6242822812641795, 1.6099011319227792, 1.5868067126139442, 1.5648237847516457, 1.544745673922674, 1.5636719309528466, 1.5836740725983514, 1.6048689016001756, 1.5833574932821064, 1.5116491299523145, 1.4495783456215772, 1.3947959447352576, 1.2918057677397976, 1.1992397828844443, 1.122286417387327, 1.058209672376652, 1.007455383951984, 0.9616205980336366, 0.919799976119593, 0.8816754541295957, 0.846415634923224, 0.8134035619782354, 0.7825288802788117, 0.7581106852285208, 0.7347173332245207, 0.712253776941942, 0.6959946688831977, 0.6868673557709583, 0.6777822485848614, 0.6687370993128493, 0.6634532674684838, 0.6588496038600996, 0.6542135218391817, 0.650439036485605, 0.6494303714709287, 0.6484085532337851, 0.6473733189682102, 0.6505096086613632, 0.6560675123165075, 0.6617844918500856, 0.6678320064423794, 0.6773980334664261, 0.687332888702839, 0.6976609392195393, 0.7077864440092541, 0.7175350110240191, 0.7276659878530138, 0.73820535178606, 0.7374533387312248, 0.7346946485373929, 0.7319157493729622, 0.7308950756427758, 0.7351101120376661, 0.7394239441643337, 0.7438404496531559, 0.7388678656148142, 0.7290543091186971, 0.7193294027380683, 0.7099151909196766, 0.70403827891758, 0.6981470703452413, 0.6922410365016064, 0.6903190692651037, 0.6930791228458978, 0.6959051509864557, 0.6987995815588827, 0.6956334462854157, 0.6914799136196662, 0.6872836231999556, 0.6848054759532685, 0.6871663435453333, 0.6895964758206469, 0.6920991381726384, 0.6881997514678868, 0.6811207774698556, 0.6740306857884835, 0.6675105117176813, 0.6683854189637848, 0.6692756243091352, 0.6701814495037131, 0.6716844131505948, 0.6738771613724109, 0.6761320170891266, 0.678451602444802, 0.6948243849381603, 0.7143272063658798, 0.7352377119243911, 0.7538648675483346, 0.7634400480774127, 0.7734757182625362, 0.78400913200663, 0.7918237369037693, 0.7984098355956745, 0.8052408495927444, 0.8111444969731852, 0.8044351815029898, 0.7976787256758275, 0.7908738034859917, 0.7841548107916759, 0.7775187541599177, 0.770818732192338, 0.7640528921612861, 0.7977884610770649, 0.8411180402148933, 0.8905199272907379, 0.9244988850601861, 0.9057541640924206, 0.887330474469635, 0.8692037451604195, 0.8856491935982594, 0.9199039669891226, 0.9579840144303058, 1.0003030333095635, 1.0438001310390779, 1.0933407551068128, 1.1506824136223224, 1.0894765877615242, 0.9454627210723413, 0.8355442387791785, 0.7462631846605977, 0.6637432281323373, 0.5924926552028972, 0.5301453236911169, 0.48125073981115035, 0.45066956195723673, 0.4216368791132292, 0.3939775868484456, 0.37149194190930995, 0.3513256753499769, 0.3317250220153034, 0.3131407785267416, 0.2989077723773725, 0.28493519458393496, 0.27120896669320177, 0.2629987969754946, 0.25983954212696614, 0.25664953991987993, 0.25342813762191524, 0.24960152947235756, 0.24568123315833998, 0.24171939608237425, 0.238101623523044, 0.23527594257716072, 0.2324141312944353, 0.2295155441563728, 0.22782156831320188, 0.22658716675319324, 0.22533190884787488, 0.22403231016346525, 0.22254695182739576, 0.22103637366931686, 0.21949994774221085, 0.21904981271046203, 0.219609059412783, 0.22018246090487753, 0.22077017247296676, 0.22303717826125743, 0.22549986941964445, 0.22803341709617733, 0.23118147780444603, 0.23555121429637055, 0.24007856001194133, 0.24477213503225362, 0.25245972876455547, 0.26152510502500387, 0.2709911285747663, 0.27890262661229726, 0.274278608942148, 0.2695808791948591, 0.26480715500720775, 0.2604653383026738, 0.25650110129093534, 0.2524811584144779, 0.24840397041427206, 0.24886572731886195, 0.249638296295987, 0.25043289043817996, 0.25154103107053394, 0.2532471330561095, 0.25500381041892867, 0.25681361748936843, 0.26073752051034915, 0.2655234002293547, 0.27049202702863523, 0.27559916289715847, 0.2805806171691589, 0.28575828540902476, 0.291145055803157, 0.30060574516686345, 0.3138064791234264, 0.3277592057718932, 0.3425384322994569, 0.3476534333532286, 0.35243566134093157, 0.3574020646420728, 0.3654625311350187, 0.3793530655132743, 0.39404055914896874, 0.40960457464744937, 0.4157154859762159, 0.41870619427713973, 0.4218053699579598, 0.4258835104095832, 0.43476182516063017, 0.4440338252839598, 0.453728497962529, 0.45411403013578727, 0.4469808275076322, 0.43980396196776317, 0.4325819393582788, 0.42045962461476527, 0.4082495740307261, 0.3961233724474999, 0.3943631551660014, 0.41103574614622995, 0.42884897199882216, 0.44794059373982753, 0.4249756272601481, 0.3909625413993561, 0.3586987573477003, 0.3284363733676303, 0.30166360729313274, 0.27594451540608794, 0.25117512039482465, 0.23389122887124383, 0.22169775881291762, 0.20952791502980017, 0.19737754868412105, 0.1891637996614425, 0.18098640907035374, 0.1727528880332392, 0.16741208053340115, 0.16685723288734378, 0.16629203228837086, 0.16571618563446414};

        // data from device
        double[] reflectance = getReflectance();

        if (reflectance != null)
        {
            dataRaw.setValue(reflectance);
        }
        else {
            Toast.makeText(getActivity(), "Sorry can't connect to device", Toast.LENGTH_LONG).show();
            return;
        }
        /*
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            for (int i = 0; i < reflectance.length; i++) {
                jsonArray.put(reflectance[i]);
            }
            jsonObject.put("reflectance", jsonArray);
        } catch (JSONException e) {
            e.getStackTrace();
            Toast.makeText(getActivity(), "Sorry we got some error : " + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
        final String mRequestBody = jsonObject.toString();
        String URL = ConfigurableProperties.apiService;
        */
        Wave rotatingCircle = new Wave();
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminateDrawable(rotatingCircle);
        lUtama.setVisibility(View.GONE);
        lProgress.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        try {
            ResultPrediction[] predicts = PredictionEngine.DoInference(reflectance, this.getActivity());
            // set static DataElements
            setDataElement(predicts);
            saveDatatoSQLITE();
            Calculator();
            Toast.makeText(getActivity(), "Process success", Toast.LENGTH_LONG).show();


            lUtama.setVisibility(View.VISIBLE);
            lProgress.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Sorry we got some error :" + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        /*
        queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            // manipulate JSONObject to JAVAObject
                            Gson gson = new Gson();
                            OutputData outputData = gson.fromJson(jsonObject.toString(), OutputData.class);

                            if (!outputData.isSucceed) {
                                Toast.makeText(getActivity(), outputData.errorMessage, Toast.LENGTH_LONG).show();
                            } else {
                                // set static DataElements
                                setDataElement(outputData.data);
                                saveDatatoSQLITE();
                                Calculator();
                                Toast.makeText(getActivity(), "Process success", Toast.LENGTH_LONG).show();
                            }

                            lUtama.setVisibility(View.VISIBLE);
                            lProgress.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Sorry we got some error :" + e.getMessage(), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                lUtama.setVisibility(View.VISIBLE);
                lProgress.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(mContext, "Periksa jaringan dan ulang proses", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(600000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
        */
    }

    public void setDataElement(ResultPrediction[] datas) {
        // reset value of data element
        DataElements.ResetData();
        for (int i = 0; i < datas.length; i++) {
            ResultPrediction rp = datas[i];
            if (rp.elementName.equals("PH_H2O")) {
                DataElements.setPhH2o(rp.elementValue);
                myObj.setPhh2o(String.valueOf(rp.elementValue));
                phh2o = (Float.toString(DataElements.getPhH2o()));
            }
            if (rp.elementName.equals("PH_KCL")) {
                DataElements.setPhKcl(rp.elementValue);
                phkcl = (Float.toString(DataElements.getPhKcl()));
                myObj.setPhkcl(String.valueOf(rp.elementValue));

            }
            if (rp.elementName.equals("C_N")) {
                DataElements.setCN(rp.elementValue);
                cn = (Float.toString(DataElements.getCN()));
                myObj.setCn(String.valueOf(rp.elementValue));

            }
            if (rp.elementName.equals("KJELDAHL_N")) {
                DataElements.setKjeldahlN(rp.elementValue);
                kjelhal = (Float.toString(DataElements.getKjeldahlN()));
                myObj.setKjelhal(String.valueOf(rp.elementValue));
            }
            if (rp.elementName.equals("HCl25_P2O5")) {
                DataElements.setHCl25P2O5(rp.elementValue);
                hclp2o5 = (Float.toString(DataElements.getHCl25P2O5()));
                myObj.setHclp2o5(String.valueOf(rp.elementValue));
            }
            if (rp.elementName.equals("HCl25_K2O")) {
                DataElements.setHCl25K2O(rp.elementValue);
                hclk2o = (Float.toString(DataElements.getHCl25K2O()));
                myObj.setHclk2o(String.valueOf(rp.elementValue));
            }
            if (rp.elementName.equals("Bray1_P2O5")) {
                DataElements.setBray1P2O5(rp.elementValue);
                bray = (Float.toString(DataElements.getBray1P2O5()));
                myObj.setBray(String.valueOf(rp.elementValue));
            }
            if (rp.elementName.equals("Olsen_P2O5")) {
                DataElements.setOlsenP2O5(rp.elementValue);
                olsen = (Float.toString(DataElements.getOlsenP2O5()));
                myObj.setOlsen(String.valueOf(rp.elementValue));
            }
            if (rp.elementName.equals("Ca")) {
                DataElements.setCa(rp.elementValue);
                ca = (Float.toString(DataElements.getCa()));
                myObj.setCa(String.valueOf(rp.elementValue));
            }
            if (rp.elementName.equals("Mg")) {
                DataElements.setMg(rp.elementValue);
                mg = (Float.toString(DataElements.getMg()));
                myObj.setMg(String.valueOf(rp.elementValue));
            }
            if (rp.elementName.equals("K")) {
                DataElements.setK(rp.elementValue);
                k = (Float.toString(DataElements.getK()));
                myObj.setK(String.valueOf(rp.elementValue));
            }
            if (rp.elementName.equals("Na")) {
                DataElements.setNa(rp.elementValue);
                na = (Float.toString(DataElements.getNa()));
                myObj.setNa(String.valueOf(rp.elementValue));
            }
            if (rp.elementName.equals("KB_adjusted")) {
                DataElements.setKBAdjusted(rp.elementValue);
                kbadj = (Float.toString(DataElements.getKBAdjusted()));
                myObj.setKbadj(String.valueOf(rp.elementValue));
            }
            if (rp.elementName.equals("KTK")) {
                DataElements.setKTK(rp.elementValue);
                ktk = (Float.toString(DataElements.getKTK()));
                myObj.setKtk(String.valueOf(rp.elementValue));
            }
            if (rp.elementName.equals("SAND")) {
                DataElements.setSAND(rp.elementValue);
                sand = (Float.toString(DataElements.getSAND()));
                myObj.setSand(String.valueOf(rp.elementValue));
            }
            if (rp.elementName.equals("SILT")) {
                DataElements.setSILT(rp.elementValue);
                silt = (Float.toString(DataElements.getSILT()));
                myObj.setSilt(String.valueOf(rp.elementValue));
            }
            if (rp.elementName.equals("CLAY")) {
                DataElements.setCLAY(rp.elementValue);
                clay = (Float.toString(DataElements.getCLAY()));
                myObj.setClay(String.valueOf(rp.elementValue));
            }
            if (rp.elementName.equals("Jumlah")) {
                DataElements.setJumlah(rp.elementValue);
                jumlah = (Float.toString(DataElements.getJumlah()));
                myObj.setJumlah(String.valueOf(rp.elementValue));
            }
            if (rp.elementName.equals("Morgan_K2O")) {
                DataElements.setMorganK2O(rp.elementValue);
                morgan = (Float.toString(DataElements.getMorganK2O()));
                myObj.setMorgan(String.valueOf(rp.elementValue));
            }
            if (rp.elementName.equals("RetensiP")) {
                DataElements.setRetensiP(rp.elementValue);
                retensip = (Float.toString(DataElements.getRetensiP()));
                myObj.setRetensip(String.valueOf(rp.elementValue));
            }
            if (rp.elementName.equals("WBC")) {
                DataElements.setWBC(rp.elementValue);
                wbc = (Float.toString(DataElements.getWBC()));
                myObj.setWbc(String.valueOf(rp.elementValue));
            }

        }
    }

    private void saveDatatoSQLITE() {
        //save to db
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int tahun = calendar.get(Calendar.YEAR);
        int bulan = calendar.get(Calendar.MONTH) + 1;
        int tanggal = calendar.get(Calendar.DAY_OF_MONTH);
        String timestamp = "" + tahun + "-" + bulan + "-" + tanggal;

        long id = dbHelper.insertRecord(
                "" + myObj.getBray(),
                "" + myObj.getCa(),
                "" + myObj.getClay(),
                "" + myObj.getCn(),
                "" + myObj.getHclk2o(),
                "" + myObj.getHclp2o5(),
                "" + myObj.getJumlah(),
                "" + myObj.getK(),
                "" + myObj.getKbadj(),
                "" + myObj.getKjelhal(),
                "" + myObj.getKtk(),
                "" + myObj.getMg(),
                "" + myObj.getMorgan(),
                "" + myObj.getNa(),
                "" + myObj.getOlsen(),
                "" + myObj.getPhh2o(),
                "" + myObj.getPhkcl(),
                "" + myObj.getRetensip(),
                "" + myObj.getSand(),
                "" + myObj.getSilt(),
                "" + myObj.getWbc(),
                "" + timestamp
        );

        long id2 = dbHelper.insertRawData(id, dataRaw.getValueString());
    }

    public static String[] getStrings(double[] a) {
        String[] output = new String[a.length];
        int i = 0;
        for (double d : a) {
            output[i++] = Arrays.toString(String.valueOf(d).replace("{", "").replace("}", "").split(","));
        }
        return output;
    }

    public static double[] convertDataToT(double[] data) {
        double[] TArray = new double[data.length];
        for (int i = 0; i < TArray.length; i++) {
            TArray[i] = data[i] * 100;
        }
        return TArray;
    }


    /* modified code */
    /* call this process after doing scan*/
    @RequiresApi(api = Build.VERSION_CODES.N)
    public double[] getReflectance() {
    /*
    for (int i = 0; i < gAllSpectra.size(); i++) {
        dbReading sensorReading = gAllSpectra.get(i);
        if (sensorReading != null) {
            if ((sensorReading.getXReading().length != 0) && (sensorReading.getYReading().length != 0)) {
                double[] xVals = sensorReading.getXReading();
                double[] yVals = sensorReading.getYReading();
                //to do: apply spectral trimming here...
                // wave range: value > 1350 and value < 2510
                return convertDataToT(yVals);
            }
        }
    }*/
        int gsize = gAllSpectra.size();
        for (int i = gsize - 1; i >= 0; i--) {
            dbReading sensorReading = gAllSpectra.get(i);
            ArrayList<DataPoint> dataPoints = new ArrayList<DataPoint>();
            List<Double> dataY = new ArrayList<Double>();
            if (sensorReading != null) {
                if ((sensorReading.getXReading().length != 0) && (sensorReading.getYReading().length != 0)) {
                    double[] xVals = sensorReading.getXReading();
                    double[] yVals = sensorReading.getYReading();
                    //double[] xData = new double[xVals.length];
                    //double[] yData = new double[yVals.length];
                    List<Double> xData = new ArrayList<Double>();
                    List<Double> yData = new ArrayList<Double>();

                    //Map<Double, Double> Waves = new HashMap<>();
                    //create interpolation for current scan


                    //for (int ax = 0; ax < xVals.length; ax++) {
                    for (int ax = xVals.length - 1; ax >= 0; --ax) {

                        double xx = 1e7 / xVals[ax];
                        double yy = yVals[ax] * 100;
                        //if(xx>1350 && xx < 2510){
                        //Waves.put(xx, yy);
                        xData.add(xx);
                        yData.add(yy);
                        //}
                    }
                    //SplineInterpolator scaler = new SplineInterpolator(Waves);
                    SplineInterpolator2 scaler = SplineInterpolator2.createMonotoneCubicSpline(xData,yData);
                    //AkimaInterpolator scaler = new AkimaInterpolator(xData,yData);
                    //adjust to model input
                    //for (int ax = 0; ax < PredictionEngine.InputWaves.length; ax += 1) {
                    for (int ax = 2500; ax > 1350; ax -= 5) {
                        double x = ax;//PredictionEngine.InputWaves[ax];
                        double y = scaler.interpolate(x);
                        System.out.println(x + "," + y);
                        dataPoints.add(new DataPoint(x, y));
                        dataY.add(y);
                    }

                    //DataPoint dataPointsArray[] = dataPoints.toArray(new DataPoint[dataPoints.size()]);
                    //Log.e("debugger", Arrays.toString(dataPointsArray));

                    Double[] dataArrY = dataY.toArray(new Double[dataY.size()]);
                    double[] d = Stream.of(dataArrY).mapToDouble(Double::doubleValue).toArray();

                    return d;

                }
            }
        }

        return null;
    }


    public void Calculator() {

        Ini ini = null;
        try {
            InputStream inputStream = getContext().getAssets().open("config.ini");
            ini = new Ini(inputStream);
            java.util.prefs.Preferences prefs = new IniPreferences(ini);
            //System.out.println("grumpy/homePage: " + prefs.node("grumpy").get("homePage", null));

            String DataRekomendasi = ini.get("Config", "DataRekomendasi");
            try {
                double ureaConst = Double.parseDouble(ini.get("Config", "Urea"));
                double sp36Const = Double.parseDouble(ini.get("Config", "SP36"));
                double kclConst = Double.parseDouble(ini.get("Config", "KCL"));

                double KCLMin = Double.parseDouble(ini.get("Config", "KCLMin"));
                double UreaMin = Double.parseDouble(ini.get("Config", "UreaMin"));
                double SP36Min = Double.parseDouble(ini.get("Config", "SP36Min"));

                FertilizerCalculator calc = new FertilizerCalculator(getContext());
                double ureaVal = 0;
                double sp36Val = 0;
                double kclVal = 0;
                switch (DataElements.getKomoditas()) {
                    case "Padi":
                        ureaVal = calc.GetFertilizerDoze(DataElements.getKjeldahlN(), DataElements.getKomoditas(), "Urea") * ureaConst;
                        sp36Val = calc.GetFertilizerDoze(DataElements.getHCl25P2O5(), DataElements.getKomoditas(), "SP36") * sp36Const;
                        kclVal = calc.GetFertilizerDoze(DataElements.getHCl25K2O(), DataElements.getKomoditas(), "KCL") * kclConst;
                        break;
                    case "Jagung":
                        ureaVal = calc.GetFertilizerDoze(DataElements.getKjeldahlN(), DataElements.getKomoditas(), "Urea") * ureaConst;
                        sp36Val = calc.GetFertilizerDoze(DataElements.getBray1P2O5(), DataElements.getKomoditas(), "SP36") * sp36Const;
                        kclVal = calc.GetFertilizerDoze(DataElements.getHCl25K2O(), DataElements.getKomoditas(), "KCL") * kclConst;
                        break;
                    case "Kedelai":
                        ureaVal = calc.GetFertilizerDoze(DataElements.getKjeldahlN(), DataElements.getKomoditas(), "Urea") * ureaConst;
                        sp36Val = calc.GetFertilizerDoze(DataElements.getBray1P2O5(), DataElements.getKomoditas(), "SP36") * sp36Const;
                        kclVal = calc.GetFertilizerDoze(DataElements.getK(), DataElements.getKomoditas(), "KCL") * kclConst;
                        break;

                }
                ureaVal = ureaVal < UreaMin ? UreaMin : ureaVal;
                sp36Val = sp36Val < SP36Min ? SP36Min : sp36Val;
                kclVal = kclVal < KCLMin ? KCLMin : kclVal;

                String TxtUrea = String.valueOf(ureaVal);
                String TxtSP36 = String.valueOf(sp36Val);
                String TxtKCL = String.valueOf(kclVal);
                System.out.println(String.format("Rekomendasi KCL : %1$s, SP36 : %2$s, Urea : %3$s", TxtKCL, TxtSP36, TxtUrea));

                FertilizerInfo x = calc.GetNPKDoze(DataElements.getHCl25P2O5(), DataElements.getHCl25K2O(), DataElements.getKomoditas());
                String Urea = String.valueOf(x.getUrea());
                String Npk = String.valueOf(x.getNPK());


                System.out.println(String.format("Rekomendasi NPK 15:15:15 = %1$s", Npk));
                System.out.println(String.format("UREA 15:15:15 = %1$s", Urea));

                DataElements.setUrea(TxtUrea);
                DataElements.setSp36(TxtSP36);
                DataElements.setKcl(TxtKCL);
                DataElements.setNpk(Npk);
                DataElements.setUrea15(Urea);

            } catch (RuntimeException ex) {
                System.out.println(ex);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        // old calculator
        /*
        String filename = "SSK.Mobile\\SSK.Desktop\\src\\main\\java\\data\\config.ini";
        Ini ini = null;
        try {

            ini = new Ini(new File(filename));
            Preferences prefs = new IniPreferences(ini);
            //System.out.println("grumpy/homePage: " + prefs.node("grumpy").get("homePage", null));

            String DataRekomendasi = ini.get("Config","DataRekomendasi");
            String WorkingDirectory = ini.get("Config","WorkingDirectory");
            String ModelScript = ini.get("Config","ModelScript");
            String SensorData = ini.get("Config","SensorData");
            String AnacondaFolder = ini.get("Config","AnacondaFolder");
            Resources.PathToData = ini.get("Config","PathToData");
            double ureaConst = Double.parseDouble(ini.get("Config","Urea"));
            double sp36Const = Double.parseDouble(ini.get("Config","SP36"));
            double kclConst = Double.parseDouble(ini.get("Config","KCL"));

            ModelRunner ml = new ModelRunner(WorkingDirectory, ModelScript, SensorData, AnacondaFolder);

            InferenceResult hasil = ml.InferenceModel(false, true);
            if (hasil.getIsSucceed())
            {
                try
                {

                    System.out.println("start recommendation process");
                    FertilizerCalculator calc = new FertilizerCalculator(DataRekomendasi);
                    String TxtUrea = String.valueOf(calc.GetFertilizerDoze(DataElements.getCN(), "Padi", "Urea")*ureaConst);
                    String TxtSP36 = String.valueOf(calc.GetFertilizerDoze(DataElements.getHCl25P2O5(), "Padi", "SP36")*sp36Const);
                    String TxtKCL = String.valueOf(calc.GetFertilizerDoze(DataElements.getHCl25K2O(), "Padi", "KCL")*kclConst);
                    System.out.println(String.format("Rekomendasi KCL : %1$s, SP36 : %2$s, Urea : %3$s", TxtKCL, TxtSP36, TxtUrea));

                    FertilizerInfo x = calc.GetNPKDoze(DataElements.getHCl25P2O5(), DataElements.getHCl25K2O(), "Padi");

                    System.out.println(String.format("Rekomendasi NPK 15:15:15 = %1$s",x.getNPK()));
                    System.out.println(String.format("UREA 15:15:15 = %1$s",x.getUrea()));

                    DataElements.setUrea(String.valueOf(calc.GetFertilizerDoze(DataElements.getCN(), "Padi", "Urea")*ureaConst));
                    DataElements.setSp36(String.valueOf(calc.GetFertilizerDoze(DataElements.getHCl25P2O5(), "Padi", "SP36")*sp36Const));
                    DataElements.setKcl(String.valueOf(calc.GetFertilizerDoze(DataElements.getHCl25K2O(), "Padi", "KCL")*kclConst));
                    DataElements.setNpk(String.valueOf(x.getNPK()));
                    DataElements.setUrea15(String.valueOf(x.getUrea()));

                }
                catch (RuntimeException ex)
                {
                    System.out.println(ex);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}

