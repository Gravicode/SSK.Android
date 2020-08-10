package com.si_ware.neospectra.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.p001v4.app.NotificationCompat;
import android.support.p001v4.content.LocalBroadcastManager;
import android.support.p004v7.app.AlertDialog.Builder;
import android.support.p004v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.si_ware.neospectra.C1284R;
import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.Global.GlobalVariables.wavelengthCorrection;
import com.si_ware.neospectra.Global.MethodsFactory;
import com.si_ware.neospectra.Scan.Presenter.ScanPresenter;

public class ReferenceMaterialCorrection extends AppCompatActivity implements OnItemSelectedListener {
    private static final String TAG = "Wavelength Correction";
    EditText GetValue;
    Double[] ListElements = {Double.valueOf(0.0d), Double.valueOf(0.0d), Double.valueOf(0.0d), Double.valueOf(0.0d), Double.valueOf(0.0d)};
    EditText input;
    public boolean isWaitingForSelfCorrection = false;
    ListView listview;
    Button mAddNewMaterial;
    /* access modifiers changed from: private */
    public Context mContext;
    @NonNull
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        /* JADX WARNING: Removed duplicated region for block: B:17:0x003d  */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x0048  */
        /* JADX WARNING: Removed duplicated region for block: B:19:0x00e1  */
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
                    case 0: goto L_0x00e1;
                    case 1: goto L_0x0048;
                    case 2: goto L_0x0046;
                    default: goto L_0x003d;
                }
            L_0x003d:
                java.lang.String r1 = "Wavelength Correction"
                java.lang.String r2 = "Got unknown broadcast intent"
                android.util.Log.v(r1, r2)
                goto L_0x00e7
            L_0x0046:
                goto L_0x00e7
            L_0x0048:
                com.si_ware.neospectra.Activities.ReferenceMaterialCorrection r1 = com.si_ware.neospectra.Activities.ReferenceMaterialCorrection.this
                r1.gotSensorReading(r8)
                java.lang.String r1 = "Wavelength Correction"
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
                com.si_ware.neospectra.Activities.ReferenceMaterialCorrection r2 = com.si_ware.neospectra.Activities.ReferenceMaterialCorrection.this
                r2.notifications_count = r3
                com.si_ware.neospectra.Activities.ReferenceMaterialCorrection r2 = com.si_ware.neospectra.Activities.ReferenceMaterialCorrection.this
                r2.isWaitingForSelfCorrection = r3
                com.si_ware.neospectra.Activities.ReferenceMaterialCorrection r2 = com.si_ware.neospectra.Activities.ReferenceMaterialCorrection.this
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
                goto L_0x00e7
            L_0x00e1:
                com.si_ware.neospectra.Activities.ReferenceMaterialCorrection r1 = com.si_ware.neospectra.Activities.ReferenceMaterialCorrection.this
                r1.gotSensorReading(r8)
            L_0x00e7:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.si_ware.neospectra.Activities.ReferenceMaterialCorrection.C12125.onReceive(android.content.Context, android.content.Intent):void");
        }
    };
    Button mProceed;
    private ScanPresenter mScanPresenter;
    /* access modifiers changed from: private */
    public String m_Text = "";
    /* access modifiers changed from: private */
    public int notifications_count = 0;
    String[] users = {"TS5"};

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1284R.layout.activity_referencematerial_correction);
        this.mContext = this;
        RadioButton rdbutton = (RadioButton) findViewById(C1284R.C1286id.rb_selfCorrection);
        this.mProceed = (Button) findViewById(C1284R.C1286id.btn_proceed);
        this.mAddNewMaterial = (Button) findViewById(C1284R.C1286id.btn_add_new_material);
        rdbutton.setSelected(true);
        Spinner spin = (Spinner) findViewById(C1284R.C1286id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 17367048, this.users);
        adapter.setDropDownViewResource(17367049);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);
        this.mProceed.setOnClickListener(new ReferenceMaterialCorrection$$Lambda$0(this));
        this.mAddNewMaterial.setOnClickListener(new ReferenceMaterialCorrection$$Lambda$1(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$0$ReferenceMaterialCorrection(View v) {
        if (GlobalVariables.gCorrectionMode == wavelengthCorrection.Self_Calibration.toString()) {
            Builder myAlert = new Builder(this.mContext);
            myAlert.setTitle((CharSequence) "Run Self Calibration");
            myAlert.setMessage((CharSequence) "Run a smart routine to automatically recalibrate wavelengths shifts with samples that have flat spectral response.  ");
            myAlert.setPositiveButton((CharSequence) "Proceed", (OnClickListener) new OnClickListener() {
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    ReferenceMaterialCorrection.this.sendSelfCorrectionCommand();
                    dialogInterface.dismiss();
                }
            });
            myAlert.setNegativeButton((CharSequence) "Cancel", (OnClickListener) new OnClickListener() {
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            myAlert.show();
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$1$ReferenceMaterialCorrection(View v) {
        Builder builder = new Builder(this);
        builder.setTitle((CharSequence) "Title");
        this.input = new EditText(this);
        this.input.setInputType(1);
        builder.setView((View) this.input);
        builder.setPositiveButton((CharSequence) "OK", (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ReferenceMaterialCorrection.this.m_Text = ReferenceMaterialCorrection.this.input.getText().toString();
                ReferenceMaterialCorrection.this.startActivity(new Intent(ReferenceMaterialCorrection.this.mContext, ReferenceMaterialListView.class));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton((CharSequence) "Cancel", (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void onItemSelected(AdapterView<?> adapterView, View arg1, int position, long id) {
        Context applicationContext = getApplicationContext();
        StringBuilder sb = new StringBuilder();
        sb.append("Selected User: ");
        sb.append(this.users[position]);
        Toast.makeText(applicationContext, sb.toString(), 0).show();
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case C1284R.C1286id.rb_referenceMaterial /*2131362065*/:
                if (checked) {
                    GlobalVariables.gCorrectionMode = wavelengthCorrection.Correction_Using_Reference_Calibrator.toString();
                    return;
                }
                return;
            case C1284R.C1286id.rb_selfCorrection /*2131362066*/:
                if (checked) {
                    GlobalVariables.gCorrectionMode = wavelengthCorrection.Self_Calibration.toString();
                    return;
                }
                return;
            default:
                return;
        }
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

    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mMessageReceiver, new IntentFilter(GlobalVariables.INTENT_ACTION));
    }

    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mMessageReceiver);
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
            double[] doubleArrayExtra = intent.getDoubleArrayExtra("data");
            if (this.isWaitingForSelfCorrection && this.notifications_count % 3 == 0) {
                MethodsFactory.logMessage("SelfCalibration", "Self Calibration has been finished successfully.");
                MethodsFactory.showAlertMessage(this.mContext, "Run Self Calibration", "Self Calibration has been finished successfully.");
                this.isWaitingForSelfCorrection = false;
            }
        }
    }
}
