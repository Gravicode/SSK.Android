package com.si_ware.neospectra.Activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.p001v4.content.LocalBroadcastManager;
import android.support.p001v4.widget.DrawerLayout;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.si_ware.neospectra.C1284R;
import com.si_ware.neospectra.Global.GlobalVariables;

public class SuperUserActivity extends NavDrawerActivity {
    DrawerLayout drawer;
    TextView mBatteryLog;
    @NonNull
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        /* JADX WARNING: Removed duplicated region for block: B:18:0x005b  */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x00c9  */
        /* JADX WARNING: Removed duplicated region for block: B:22:0x0134  */
        /* JADX WARNING: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r9, android.content.Intent r10) {
            /*
                r8 = this;
                java.lang.String r0 = "iName"
                java.lang.String r0 = r10.getStringExtra(r0)
                java.io.PrintStream r1 = java.lang.System.out
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = "Action: "
                r2.append(r3)
                java.lang.String r3 = r10.getAction()
                r2.append(r3)
                java.lang.String r2 = r2.toString()
                r1.println(r2)
                int r1 = r0.hashCode()
                r2 = -1526627392(0xffffffffa50183c0, float:-1.1233605E-16)
                r3 = 0
                if (r1 == r2) goto L_0x0049
                r2 = 75494647(0x47ff4f7, float:3.0087588E-36)
                if (r1 == r2) goto L_0x003f
                r2 = 1989569876(0x76966d54, float:1.5255117E33)
                if (r1 == r2) goto L_0x0035
                goto L_0x0053
            L_0x0035:
                java.lang.String r1 = "Temperature"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L_0x0053
                r1 = 0
                goto L_0x0054
            L_0x003f:
                java.lang.String r1 = "P3_ID"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L_0x0053
                r1 = 1
                goto L_0x0054
            L_0x0049:
                java.lang.String r1 = "Battery_info"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L_0x0053
                r1 = 2
                goto L_0x0054
            L_0x0053:
                r1 = -1
            L_0x0054:
                r4 = 0
                switch(r1) {
                    case 0: goto L_0x0134;
                    case 1: goto L_0x00c9;
                    case 2: goto L_0x005b;
                    default: goto L_0x0059;
                }
            L_0x0059:
                goto L_0x019f
            L_0x005b:
                java.lang.String r1 = "SuperUserActivity"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r4 = "Intent Received:\nName: "
                r2.append(r4)
                java.lang.String r4 = "iName"
                java.lang.String r4 = r10.getStringExtra(r4)
                r2.append(r4)
                java.lang.String r4 = "\nSuccess: "
                r2.append(r4)
                java.lang.String r4 = "isNotificationSuccess"
                boolean r3 = r10.getBooleanExtra(r4, r3)
                r2.append(r3)
                java.lang.String r3 = "\nReason: "
                r2.append(r3)
                java.lang.String r3 = "reason"
                java.lang.String r3 = r10.getStringExtra(r3)
                r2.append(r3)
                java.lang.String r3 = "\nError: "
                r2.append(r3)
                java.lang.String r3 = "err"
                java.lang.String r3 = r10.getStringExtra(r3)
                r2.append(r3)
                java.lang.String r3 = "\n"
                r2.append(r3)
                java.lang.String r2 = r2.toString()
                com.si_ware.neospectra.Global.MethodsFactory.logMessage(r1, r2)
                java.lang.String r1 = "data"
                java.lang.String r1 = r10.getStringExtra(r1)
                if (r1 == 0) goto L_0x019f
                com.si_ware.neospectra.Activities.SuperUserActivity r2 = com.si_ware.neospectra.Activities.SuperUserActivity.this
                android.widget.TextView r2 = r2.mBatteryLog
                r2.append(r1)
                com.si_ware.neospectra.Activities.SuperUserActivity r2 = com.si_ware.neospectra.Activities.SuperUserActivity.this
                android.widget.TextView r2 = r2.mBatteryLog
                java.lang.String r3 = "\n"
                r2.append(r3)
                com.si_ware.neospectra.Activities.SuperUserActivity r2 = com.si_ware.neospectra.Activities.SuperUserActivity.this
                android.widget.TextView r2 = r2.mBatteryLog
                java.lang.String r3 = "----------------------\n"
                r2.append(r3)
                goto L_0x019f
            L_0x00c9:
                java.lang.String r1 = "SuperUserActivity"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r6 = "Intent Received:\nName: "
                r2.append(r6)
                java.lang.String r6 = "iName"
                java.lang.String r6 = r10.getStringExtra(r6)
                r2.append(r6)
                java.lang.String r6 = "\nSuccess: "
                r2.append(r6)
                java.lang.String r6 = "isNotificationSuccess"
                boolean r3 = r10.getBooleanExtra(r6, r3)
                r2.append(r3)
                java.lang.String r3 = "\nReason: "
                r2.append(r3)
                java.lang.String r3 = "reason"
                java.lang.String r3 = r10.getStringExtra(r3)
                r2.append(r3)
                java.lang.String r3 = "\nError: "
                r2.append(r3)
                java.lang.String r3 = "err"
                java.lang.String r3 = r10.getStringExtra(r3)
                r2.append(r3)
                java.lang.String r3 = "\ndata : "
                r2.append(r3)
                java.lang.String r3 = "data"
                long r6 = r10.getLongExtra(r3, r4)
                r2.append(r6)
                java.lang.String r3 = "\n"
                r2.append(r3)
                java.lang.String r2 = r2.toString()
                com.si_ware.neospectra.Global.MethodsFactory.logMessage(r1, r2)
                com.si_ware.neospectra.Activities.SuperUserActivity r1 = com.si_ware.neospectra.Activities.SuperUserActivity.this
                android.widget.TextView r1 = r1.mP3_ID_Value
                java.lang.String r2 = "data"
                long r2 = r10.getLongExtra(r2, r4)
                java.lang.String r2 = java.lang.Long.toString(r2)
                r1.setText(r2)
                goto L_0x019f
            L_0x0134:
                java.lang.String r1 = "SuperUserActivity"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r6 = "Intent Received:\nName: "
                r2.append(r6)
                java.lang.String r6 = "iName"
                java.lang.String r6 = r10.getStringExtra(r6)
                r2.append(r6)
                java.lang.String r6 = "\nSuccess: "
                r2.append(r6)
                java.lang.String r6 = "isNotificationSuccess"
                boolean r3 = r10.getBooleanExtra(r6, r3)
                r2.append(r3)
                java.lang.String r3 = "\nReason: "
                r2.append(r3)
                java.lang.String r3 = "reason"
                java.lang.String r3 = r10.getStringExtra(r3)
                r2.append(r3)
                java.lang.String r3 = "\nError: "
                r2.append(r3)
                java.lang.String r3 = "err"
                java.lang.String r3 = r10.getStringExtra(r3)
                r2.append(r3)
                java.lang.String r3 = "\ndata : "
                r2.append(r3)
                java.lang.String r3 = "data"
                long r6 = r10.getLongExtra(r3, r4)
                r2.append(r6)
                java.lang.String r3 = "\n"
                r2.append(r3)
                java.lang.String r2 = r2.toString()
                com.si_ware.neospectra.Global.MethodsFactory.logMessage(r1, r2)
                com.si_ware.neospectra.Activities.SuperUserActivity r1 = com.si_ware.neospectra.Activities.SuperUserActivity.this
                android.widget.TextView r1 = r1.mTemperatureValue
                java.lang.String r2 = "data"
                long r2 = r10.getLongExtra(r2, r4)
                java.lang.String r2 = java.lang.Long.toString(r2)
                r1.setText(r2)
            L_0x019f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.si_ware.neospectra.Activities.SuperUserActivity.C12252.onReceive(android.content.Context, android.content.Intent):void");
        }
    };
    TextView mP3_ID_Value;
    Button mScannerIDButton;
    EditText mScannerIDValue;
    TextView mTemperatureValue;
    int temperaturePeriod = 1000;
    Thread temperatureThread;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = ((LayoutInflater) getSystemService("layout_inflater")).inflate(C1284R.layout.activity_superuser, null, false);
        this.drawer = (DrawerLayout) findViewById(C1284R.C1286id.drawer_layout);
        this.drawer.addView(contentView, 0);
        this.mP3_ID_Value = (TextView) findViewById(C1284R.C1286id.P3_ID_Value);
        this.mTemperatureValue = (TextView) findViewById(C1284R.C1286id.temperatureValue);
        this.mBatteryLog = (TextView) findViewById(C1284R.C1286id.BatteryLog);
        this.mBatteryLog.setMovementMethod(new ScrollingMovementMethod());
        this.mScannerIDValue = (EditText) findViewById(C1284R.C1286id.ScannerIDValue);
        this.mScannerIDButton = (Button) findViewById(C1284R.C1286id.ScannerIDButton);
        this.mScannerIDButton.setOnClickListener(new SuperUserActivity$$Lambda$0(this));
        this.temperatureThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (GlobalVariables.bluetoothAPI != null) {
                        GlobalVariables.bluetoothAPI.sendTemperatureRequest();
                    }
                    try {
                        Thread.sleep((long) SuperUserActivity.this.temperaturePeriod);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        return;
                    }
                }
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$0$SuperUserActivity(View v) {
        if (GlobalVariables.bluetoothAPI != null) {
            GlobalVariables.bluetoothAPI.setScannerID(Long.parseLong(this.mScannerIDValue.getText().toString()));
        }
    }

    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mMessageReceiver, new IntentFilter(GlobalVariables.HOME_INTENT_ACTION));
        if (GlobalVariables.bluetoothAPI != null) {
            GlobalVariables.bluetoothAPI.sendP3_ID_Request();
        }
        this.temperatureThread.start();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mMessageReceiver);
        this.temperatureThread.interrupt();
    }
}
