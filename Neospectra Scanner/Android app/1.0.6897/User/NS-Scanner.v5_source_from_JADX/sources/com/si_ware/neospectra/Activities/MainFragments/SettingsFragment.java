package com.si_ware.neospectra.Activities.MainFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.p001v4.app.NotificationCompat;
import android.support.p001v4.content.LocalBroadcastManager;
import android.support.p004v7.app.AlertDialog.Builder;
import android.support.p004v7.preference.EditTextPreference;
import android.support.p004v7.preference.ListPreference;
import android.support.p004v7.preference.Preference;
import android.support.p004v7.preference.Preference.OnPreferenceClickListener;
import android.support.p004v7.preference.PreferenceCategory;
import android.support.p004v7.preference.PreferenceFragmentCompat;
import android.support.p004v7.preference.PreferenceManager;
import android.util.Log;
import com.si_ware.neospectra.Activities.ConnectActivity;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3API;
import com.si_ware.neospectra.C1284R;
import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.Global.GlobalVariables.apodization;
import com.si_ware.neospectra.Global.GlobalVariables.pointsCount;
import com.si_ware.neospectra.Global.GlobalVariables.zeroPadding;
import com.si_ware.neospectra.Global.MethodsFactory;
import com.si_ware.neospectra.Scan.Presenter.ScanPresenter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SettingsFragment extends PreferenceFragmentCompat implements OnSharedPreferenceChangeListener {
    private static final String TAG = "Settings Fragment";
    public boolean isWaitingForGainSettings = false;
    public boolean isWaitingForRestoreToDefault = false;
    public boolean isWaitingForSelfCorrection = false;
    public boolean isWaitingForStoringAllSettings = false;
    private ListPreference mApodizationList;
    /* access modifiers changed from: private */
    public Context mContext;
    private ListPreference mInterpolationPoints;
    @NonNull
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        /* JADX WARNING: Removed duplicated region for block: B:17:0x003d  */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x0048  */
        /* JADX WARNING: Removed duplicated region for block: B:19:0x00f2  */
        /* JADX WARNING: Removed duplicated region for block: B:23:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r7, android.content.Intent r8) {
            /*
                r6 = this;
                java.lang.String r0 = "iName"
                java.lang.String r0 = r8.getStringExtra(r0)
                int r1 = r0.hashCode()
                r2 = 559518180(0x215991e4, float:7.3715544E-19)
                r3 = 0
                if (r1 == r2) goto L_0x002f
                r2 = 871069634(0x33eb77c2, float:1.0964824E-7)
                if (r1 == r2) goto L_0x0025
                r2 = 1603317008(0x5f90ad10, float:2.0850013E19)
                if (r1 == r2) goto L_0x001b
                goto L_0x0039
            L_0x001b:
                java.lang.String r1 = "sensorNotification_failure"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L_0x0039
                r1 = 1
                goto L_0x003a
            L_0x0025:
                java.lang.String r1 = "sensorWriting"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L_0x0039
                r1 = 2
                goto L_0x003a
            L_0x002f:
                java.lang.String r1 = "sensorNotification_data"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L_0x0039
                r1 = 0
                goto L_0x003a
            L_0x0039:
                r1 = -1
            L_0x003a:
                switch(r1) {
                    case 0: goto L_0x00f2;
                    case 1: goto L_0x0048;
                    case 2: goto L_0x0046;
                    default: goto L_0x003d;
                }
            L_0x003d:
                java.lang.String r1 = "Settings Fragmentintent"
                java.lang.String r2 = "Got unknown broadcast intent"
                android.util.Log.v(r1, r2)
                goto L_0x00f8
            L_0x0046:
                goto L_0x00f8
            L_0x0048:
                com.si_ware.neospectra.Activities.MainFragments.SettingsFragment r1 = com.si_ware.neospectra.Activities.MainFragments.SettingsFragment.this
                r1.gotSensorReading(r8)
                java.lang.String r1 = "Settings Fragment"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r4 = "Intent Received:\nName: "
                r2.append(r4)
                java.lang.String r4 = "iName"
                java.lang.String r4 = r8.getStringExtra(r4)
                r2.append(r4)
                java.lang.String r4 = "\nSuccess: "
                r2.append(r4)
                java.lang.String r4 = "isNotificationSuccess"
                boolean r4 = r8.getBooleanExtra(r4, r3)
                r2.append(r4)
                java.lang.String r4 = "\nReason: "
                r2.append(r4)
                java.lang.String r4 = "reason"
                java.lang.String r4 = r8.getStringExtra(r4)
                r2.append(r4)
                java.lang.String r4 = "\nError: "
                r2.append(r4)
                java.lang.String r4 = "err"
                java.lang.String r4 = r8.getStringExtra(r4)
                r2.append(r4)
                java.lang.String r4 = "\ndata : "
                r2.append(r4)
                java.lang.String r4 = "data"
                int r4 = r8.getIntExtra(r4, r3)
                java.lang.String r4 = java.lang.String.valueOf(r4)
                r2.append(r4)
                java.lang.String r4 = "\n"
                r2.append(r4)
                java.lang.String r2 = r2.toString()
                com.si_ware.neospectra.Global.MethodsFactory.logMessage(r1, r2)
                java.lang.String r1 = "data"
                int r1 = r8.getIntExtra(r1, r3)
                com.si_ware.neospectra.Activities.MainFragments.SettingsFragment r2 = com.si_ware.neospectra.Activities.MainFragments.SettingsFragment.this
                r2.notifications_count = r3
                com.si_ware.neospectra.Activities.MainFragments.SettingsFragment r2 = com.si_ware.neospectra.Activities.MainFragments.SettingsFragment.this
                r2.isWaitingForGainSettings = r3
                com.si_ware.neospectra.Activities.MainFragments.SettingsFragment r2 = com.si_ware.neospectra.Activities.MainFragments.SettingsFragment.this
                r2.isWaitingForSelfCorrection = r3
                com.si_ware.neospectra.Activities.MainFragments.SettingsFragment r2 = com.si_ware.neospectra.Activities.MainFragments.SettingsFragment.this
                r2.isWaitingForRestoreToDefault = r3
                com.si_ware.neospectra.Activities.MainFragments.SettingsFragment r2 = com.si_ware.neospectra.Activities.MainFragments.SettingsFragment.this
                r2.isWaitingForStoringAllSettings = r3
                com.si_ware.neospectra.Activities.MainFragments.SettingsFragment r2 = com.si_ware.neospectra.Activities.MainFragments.SettingsFragment.this
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
                com.si_ware.neospectra.Activities.MainFragments.SettingsFragment r2 = com.si_ware.neospectra.Activities.MainFragments.SettingsFragment.this
                r2.enableView()
                goto L_0x00f8
            L_0x00f2:
                com.si_ware.neospectra.Activities.MainFragments.SettingsFragment r1 = com.si_ware.neospectra.Activities.MainFragments.SettingsFragment.this
                r1.gotSensorReading(r8)
            L_0x00f8:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.si_ware.neospectra.Activities.MainFragments.SettingsFragment.C118310.onReceive(android.content.Context, android.content.Intent):void");
        }
    };
    @NonNull
    private BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
        /* JADX WARNING: Removed duplicated region for block: B:12:0x002e  */
        /* JADX WARNING: Removed duplicated region for block: B:13:0x0036  */
        /* JADX WARNING: Removed duplicated region for block: B:14:0x0069  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r7, android.content.Intent r8) {
            /*
                r6 = this;
                java.lang.String r0 = "iName"
                java.lang.String r0 = r8.getStringExtra(r0)
                int r1 = r0.hashCode()
                r2 = 67232232(0x401e1e8, float:1.5267608E-36)
                r3 = 0
                if (r1 == r2) goto L_0x0020
                r2 = 890474857(0x35139169, float:5.4973367E-7)
                if (r1 == r2) goto L_0x0016
                goto L_0x002a
            L_0x0016:
                java.lang.String r1 = "OperationDone"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L_0x002a
                r1 = 0
                goto L_0x002b
            L_0x0020:
                java.lang.String r1 = "Error"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L_0x002a
                r1 = 1
                goto L_0x002b
            L_0x002a:
                r1 = -1
            L_0x002b:
                switch(r1) {
                    case 0: goto L_0x0069;
                    case 1: goto L_0x0036;
                    default: goto L_0x002e;
                }
            L_0x002e:
                java.lang.String r1 = "Settings Fragmentintent"
                java.lang.String r2 = "Got unknown broadcast intent"
                android.util.Log.v(r1, r2)
                goto L_0x007c
            L_0x0036:
                java.lang.String r1 = "data"
                int r1 = r8.getIntExtra(r1, r3)
                com.si_ware.neospectra.Activities.MainFragments.SettingsFragment r2 = com.si_ware.neospectra.Activities.MainFragments.SettingsFragment.this
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
                com.si_ware.neospectra.Activities.MainFragments.SettingsFragment r2 = com.si_ware.neospectra.Activities.MainFragments.SettingsFragment.this
                r2.enableView()
                goto L_0x007c
            L_0x0069:
                com.si_ware.neospectra.Activities.MainFragments.SettingsFragment r1 = com.si_ware.neospectra.Activities.MainFragments.SettingsFragment.this
                android.content.Context r1 = r1.mContext
                java.lang.String r2 = "Save Preferences"
                java.lang.String r3 = "Preferences has been saved on the scanner successfully."
                com.si_ware.neospectra.Global.MethodsFactory.showAlertMessage(r1, r2, r3)
                com.si_ware.neospectra.Activities.MainFragments.SettingsFragment r1 = com.si_ware.neospectra.Activities.MainFragments.SettingsFragment.this
                r1.enableView()
            L_0x007c:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.si_ware.neospectra.Activities.MainFragments.SettingsFragment.C11989.onReceive(android.content.Context, android.content.Intent):void");
        }
    };
    /* access modifiers changed from: private */
    public ListPreference mOpticalGainList;
    private ScanPresenter mScanPresenter;
    private ListPreference mZeroPaddingList;
    /* access modifiers changed from: private */
    public int notifications_count = 0;
    public String opticalGainName;
    SharedPreferences preferences;

    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(C1284R.xml.pref_general, rootKey);
        this.mContext = getContext();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        this.mOpticalGainList = (ListPreference) findPreference("optical_gain_settings");
        this.mInterpolationPoints = (ListPreference) findPreference("data_points");
        this.mApodizationList = (ListPreference) findPreference("apodization_function");
        this.mZeroPaddingList = (ListPreference) findPreference("fft_points");
        if (this.mInterpolationPoints.getValue() == null) {
            this.mInterpolationPoints.setValueIndex(2);
        }
        if (this.mApodizationList.getValue() == null) {
            this.mApodizationList.setValueIndex(0);
        }
        if (this.mZeroPaddingList.getValue() == null) {
            this.mZeroPaddingList.setValueIndex(0);
        }
        final EditTextPreference editTextPreference = (EditTextPreference) findPreference("add_new_optical_gain_settings");
        findPreference("clear_optical_gain_settings").setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Builder myAlert = new Builder(SettingsFragment.this.mContext);
                myAlert.setTitle((CharSequence) "Clear");
                myAlert.setMessage((CharSequence) "Are you sure you want to clear all optical gains?");
                myAlert.setPositiveButton((CharSequence) "OK", (OnClickListener) new OnClickListener() {
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        SettingsFragment.this.clearFile();
                        SettingsFragment.this.setListPreferenceData(SettingsFragment.this.mOpticalGainList);
                    }
                });
                myAlert.setNegativeButton((CharSequence) "Cancel", (OnClickListener) new OnClickListener() {
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                myAlert.show();
                return true;
            }
        });
        findPreference("store_all_Settings").setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Builder myAlert = new Builder(SettingsFragment.this.mContext);
                myAlert.setTitle((CharSequence) "Confirm");
                myAlert.setMessage((CharSequence) "Start storing all settings?");
                myAlert.setPositiveButton((CharSequence) "Proceed", (OnClickListener) new OnClickListener() {
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                        SettingsFragment.this.askForStoringAllSettings();
                        dialogInterface.dismiss();
                    }
                });
                myAlert.setNegativeButton((CharSequence) "Cancel", (OnClickListener) new OnClickListener() {
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                myAlert.show();
                return true;
            }
        });
        findPreference("restore_default").setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Builder myAlert = new Builder(SettingsFragment.this.mContext);
                myAlert.setTitle((CharSequence) "Warning!");
                myAlert.setMessage((CharSequence) "By restoring default settings, all current settings will be cleared.Are you sure you would like to proceed?");
                myAlert.setPositiveButton((CharSequence) "Proceed", (OnClickListener) new OnClickListener() {
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                        SettingsFragment.this.askForRestoreToDefaultSettings();
                        dialogInterface.dismiss();
                    }
                });
                myAlert.setNegativeButton((CharSequence) "Cancel", (OnClickListener) new OnClickListener() {
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                myAlert.show();
                return true;
            }
        });
        findPreference("scanTime_preferences").setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Builder myAlert = new Builder(SettingsFragment.this.mContext);
                myAlert.setTitle((CharSequence) "ScanTime");
                StringBuilder sb = new StringBuilder();
                sb.append("Scan time will be saved in Scanner = ");
                sb.append(Integer.toString(GlobalVariables.scanTime));
                sb.append("S");
                myAlert.setMessage((CharSequence) sb.toString());
                myAlert.setPositiveButton((CharSequence) "Ok", (OnClickListener) new OnClickListener() {
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                myAlert.show();
                return true;
            }
        });
        editTextPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                editTextPreference.setText("");
                return true;
            }
        });
        this.mOpticalGainList.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                SettingsFragment.this.setListPreferenceData(SettingsFragment.this.mOpticalGainList);
                return false;
            }
        });
        Preference mSelfCorrectionPreferences = findPreference("wavelength_correction");
        findPreference("wavelength_correction").setEnabled(false);
        mSelfCorrectionPreferences.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Builder myAlert = new Builder(SettingsFragment.this.mContext);
                myAlert.setTitle((CharSequence) "Run Self Calibration");
                myAlert.setMessage((CharSequence) "Run a smart routine to automatically recalibrate wavelengths shifts with samples that have flat spectral response.  ");
                myAlert.setPositiveButton((CharSequence) "Proceed", (OnClickListener) new OnClickListener() {
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                        SettingsFragment.this.sendSelfCorrectionCommand();
                        dialogInterface.dismiss();
                    }
                });
                myAlert.setNegativeButton((CharSequence) "Cancel", (OnClickListener) new OnClickListener() {
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                myAlert.show();
                return true;
            }
        });
        findPreference("save_preferences").setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                SettingsFragment.this.savePreferences();
                return false;
            }
        });
        findPreference("load_preferences").setEnabled(false);
    }

    /* access modifiers changed from: private */
    public void sendSelfCorrectionCommand() {
        System.out.println("inside sendSelfCorrectionCommand");
        if (this.isWaitingForSelfCorrection) {
            MethodsFactory.logMessage(TAG, "Still waiting for sensor reading ... ");
            return;
        }
        this.isWaitingForSelfCorrection = true;
        askForSelfCorrection();
        disableView();
    }

    private void askForSelfCorrection() {
        if (this.mScanPresenter == null) {
            this.mScanPresenter = new ScanPresenter();
        }
        if (GlobalVariables.bluetoothAPI == null || !GlobalVariables.bluetoothAPI.isDeviceConnected()) {
            MethodsFactory.showAlertMessage(this.mContext, "Device not connected", "Please! Ensure that you have a connected device firstly");
            Intent iMain = new Intent(this.mContext, ConnectActivity.class);
            iMain.setFlags(268468224);
            startActivity(iMain);
            return;
        }
        this.mScanPresenter.requestSelfCalibration(GlobalVariables.scanTime);
    }

    /* access modifiers changed from: private */
    public void askForRestoreToDefaultSettings() {
        if (this.mScanPresenter == null) {
            this.mScanPresenter = new ScanPresenter();
        }
        if (GlobalVariables.bluetoothAPI == null || !GlobalVariables.bluetoothAPI.isDeviceConnected()) {
            MethodsFactory.showAlertMessage(this.mContext, "Device not connected", "Please! Ensure that you have a connected device firstly");
            Intent iMain = new Intent(this.mContext, ConnectActivity.class);
            iMain.setFlags(268468224);
            startActivity(iMain);
            return;
        }
        this.isWaitingForRestoreToDefault = true;
        this.mScanPresenter.restoreToDefaultSettings();
        disableView();
    }

    /* access modifiers changed from: private */
    public void askForStoringAllSettings() {
        if (this.mScanPresenter == null) {
            this.mScanPresenter = new ScanPresenter();
        }
        if (GlobalVariables.bluetoothAPI == null || !GlobalVariables.bluetoothAPI.isDeviceConnected()) {
            MethodsFactory.showAlertMessage(this.mContext, "Device not connected", "Please! Ensure that you have a connected device firstly");
            Intent iMain = new Intent(this.mContext, ConnectActivity.class);
            iMain.setFlags(268468224);
            startActivity(iMain);
            return;
        }
        this.isWaitingForStoringAllSettings = true;
        this.mScanPresenter.storingSettings();
        disableView();
    }

    /* access modifiers changed from: protected */
    public void setListPreferenceData(ListPreference lp) {
        List<CharSequence> charSequences = new ArrayList<>();
        ArrayList<String> opticalGains = readFromFile();
        ArrayList<String> newList = new ArrayList<>();
        Iterator it = opticalGains.iterator();
        while (it.hasNext()) {
            String element = (String) it.next();
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }
        charSequences.add("Default");
        for (int i = 0; i < newList.size(); i++) {
            charSequences.add(newList.get(i));
        }
        CharSequence[] charSequenceArray = (CharSequence[]) charSequences.toArray(new CharSequence[charSequences.size()]);
        lp.setEntries(charSequenceArray);
        lp.setEntryValues(charSequenceArray);
        if (lp.getValue() == null) {
            lp.setValueIndex(charSequenceArray.length - 1);
        }
        if (lp.getEntries().length == 1) {
            lp.setValueIndex(0);
        }
    }

    private ArrayList<String> readFromFile() {
        ArrayList<String> arr = new ArrayList<>();
        try {
            InputStream inputStream = this.mContext.openFileInput("configurations.txt");
            if (inputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String str = "";
                while (true) {
                    String readLine = bufferedReader.readLine();
                    String receiveString = readLine;
                    if (readLine == null) {
                        break;
                    }
                    arr.add(receiveString);
                }
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("File not found: ");
            sb.append(e.toString());
            Log.e("login activity", sb.toString());
        } catch (IOException e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Can not read file: ");
            sb2.append(e2.toString());
            Log.e("login activity", sb2.toString());
        }
        return arr;
    }

    /* access modifiers changed from: private */
    public void gotSensorReading(Intent intent) {
        boolean isNotificationSuccessful = intent.getBooleanExtra("isNotificationSuccess", false);
        String notificationReason = intent.getStringExtra("reason");
        String errorMessage = intent.getStringExtra(NotificationCompat.CATEGORY_ERROR);
        if (!isNotificationSuccessful) {
            MethodsFactory.logMessage(notificationReason, errorMessage);
            return;
        }
        this.notifications_count++;
        if (notificationReason.equals("gotData")) {
            double[] gain = intent.getDoubleArrayExtra("data");
            if (this.isWaitingForGainSettings) {
                Editor editor = this.preferences.edit();
                editor.putInt(this.opticalGainName, (int) gain[0]);
                editor.commit();
                this.preferences.edit().putString(this.mOpticalGainList.getKey(), this.mOpticalGainList.getValue());
                writeToFile();
                setListPreferenceData(this.mOpticalGainList);
                this.mOpticalGainList.setValue(this.opticalGainName);
                this.isWaitingForGainSettings = false;
                enableView();
                return;
            }
            if (this.isWaitingForRestoreToDefault) {
                MethodsFactory.logMessage("RestoreToDefault", "Settings has been restored successfully.");
                MethodsFactory.showAlertMessage(this.mContext, "Restore To Default", "Settings has been restored successfully.");
                enableView();
                this.isWaitingForRestoreToDefault = false;
            } else if (this.isWaitingForStoringAllSettings && this.notifications_count % 2 == 0) {
                MethodsFactory.logMessage("StoringAllSettings", "Settings has been stored successfully.");
                MethodsFactory.showAlertMessage(this.mContext, "Storing All Settings", "Settings has been stored successfully.");
                enableView();
                this.isWaitingForStoringAllSettings = false;
            } else if (this.isWaitingForSelfCorrection && this.notifications_count % 3 == 0) {
                MethodsFactory.logMessage("SelfCalibration", "Self Calibration has been finished successfully.");
                MethodsFactory.showAlertMessage(this.mContext, "Run Self Calibration", "Self Calibration has been finished successfully.");
                this.isWaitingForSelfCorrection = false;
                enableView();
            }
        }
    }

    private void writeToFile() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.mContext.openFileOutput("configurations.txt", 32768));
            outputStreamWriter.write(this.opticalGainName);
            outputStreamWriter.write("\n");
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("File write failed: ");
            sb.append(e.toString());
            Log.e("Exception", sb.toString());
        } catch (IOException e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("File write failed: ");
            sb2.append(e2.toString());
            Log.e("Exception", sb2.toString());
        }
    }

    /* access modifiers changed from: private */
    public void clearFile() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.mContext.openFileOutput("configurations.txt", 0));
            outputStreamWriter.write("");
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("File write failed: ");
            sb.append(e.toString());
            Log.e("Exception", sb.toString());
        } catch (IOException e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("File write failed: ");
            sb2.append(e2.toString());
            Log.e("Exception", sb2.toString());
        }
    }

    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this.mMessageReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this.mMessageReceiver2);
    }

    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.mMessageReceiver, new IntentFilter(GlobalVariables.INTENT_ACTION));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.mMessageReceiver2, new IntentFilter(GlobalVariables.HOME_INTENT_ACTION));
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("add_new_optical_gain_settings")) {
            this.opticalGainName = String.valueOf(((EditTextPreference) findPreference(key)).getText().toString());
            if (!this.opticalGainName.equals("")) {
                if (GlobalVariables.bluetoothAPI == null) {
                    GlobalVariables.bluetoothAPI = new SWS_P3API(getActivity(), getContext());
                }
                this.mScanPresenter = new ScanPresenter();
                this.isWaitingForGainSettings = true;
                this.mScanPresenter.requestGainReading();
                disableView();
            }
        }
    }

    private void disableView() {
        ((PreferenceCategory) findPreference("category_measurement_parameters")).setEnabled(false);
        ((PreferenceCategory) findPreference("category_data_display")).setEnabled(false);
        ((PreferenceCategory) findPreference("category_fft_settings")).setEnabled(false);
        ((PreferenceCategory) findPreference("category_advanced_settings")).setEnabled(false);
        ((PreferenceCategory) findPreference("category_save_restore")).setEnabled(false);
    }

    /* access modifiers changed from: private */
    public void enableView() {
        ((PreferenceCategory) findPreference("category_measurement_parameters")).setEnabled(true);
        ((PreferenceCategory) findPreference("category_data_display")).setEnabled(true);
        ((PreferenceCategory) findPreference("category_fft_settings")).setEnabled(true);
        ((PreferenceCategory) findPreference("category_advanced_settings")).setEnabled(true);
        ((PreferenceCategory) findPreference("category_save_restore")).setEnabled(true);
    }

    /* access modifiers changed from: private */
    public void savePreferences() {
        byte[] memPreferencePacket = new byte[11];
        memPreferencePacket[0] = 3;
        System.arraycopy(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(GlobalVariables.scanTime * 1000).array(), 0, memPreferencePacket, 1, 3);
        String optical_gain = this.preferences.getString("optical_gain_settings", "Default");
        System.arraycopy(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(this.preferences.getInt(optical_gain, 0)).array(), 0, memPreferencePacket, 4, 2);
        if (!this.preferences.getBoolean("linear_interpolation_switch", false)) {
            memPreferencePacket[6] = 0;
        } else {
            String interpolationPoints = this.preferences.getString("data_points", pointsCount.points_257.toString());
            if (interpolationPoints.equals(pointsCount.points_65.toString())) {
                memPreferencePacket[6] = 1;
            } else if (interpolationPoints.equals(pointsCount.points_129.toString())) {
                memPreferencePacket[6] = 2;
            } else if (interpolationPoints.equals(pointsCount.points_257.toString())) {
                memPreferencePacket[6] = 3;
            } else {
                memPreferencePacket[6] = 0;
            }
        }
        if (optical_gain.equals("Default")) {
            memPreferencePacket[7] = 0;
        } else {
            memPreferencePacket[7] = 2;
        }
        String apodizationSel = this.preferences.getString("apodization_function", apodization.Boxcar.toString());
        if (apodizationSel.equals(apodization.Boxcar.toString())) {
            memPreferencePacket[8] = 0;
        } else if (apodizationSel.equals(apodization.Gaussian.toString())) {
            memPreferencePacket[8] = 1;
        } else if (apodizationSel.equals(apodization.HappGenzel.toString())) {
            memPreferencePacket[8] = 2;
        } else if (apodizationSel.equals(apodization.Lorenz.toString())) {
            memPreferencePacket[8] = 3;
        } else {
            memPreferencePacket[8] = 0;
        }
        String FftPoints = this.preferences.getString("fft_points", zeroPadding.points_8k.toString());
        if (FftPoints.equals(zeroPadding.points_8k.toString())) {
            memPreferencePacket[9] = 1;
        } else if (FftPoints.equals(zeroPadding.points_16k.toString())) {
            memPreferencePacket[9] = 2;
        } else if (FftPoints.equals(zeroPadding.points_32k.toString())) {
            memPreferencePacket[9] = 3;
        } else {
            memPreferencePacket[9] = 1;
        }
        memPreferencePacket[10] = 0;
        disableView();
        if (GlobalVariables.bluetoothAPI != null) {
            GlobalVariables.bluetoothAPI.sendPacket(memPreferencePacket, false, "Memory Service");
        }
    }
}
