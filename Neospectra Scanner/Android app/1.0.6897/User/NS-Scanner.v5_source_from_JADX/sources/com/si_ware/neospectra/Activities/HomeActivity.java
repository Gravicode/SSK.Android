package com.si_ware.neospectra.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.p001v4.content.LocalBroadcastManager;
import android.support.p001v4.widget.DrawerLayout;
import android.support.p004v7.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.si_ware.neospectra.BuildConfig;
import com.si_ware.neospectra.C1284R;
import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.Global.GlobalVariables.OTA_Functions;

public class HomeActivity extends NavDrawerActivity implements OnClickListener {
    DrawerLayout drawer;
    Button mConfigureDevice;
    Context mContext;
    Button mDeviceStatusButton;
    Button mDisconnectDevice;
    @NonNull
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r10, android.content.Intent r11) {
            /*
                r9 = this;
                java.lang.String r0 = "iName"
                java.lang.String r0 = r11.getStringExtra(r0)
                java.io.PrintStream r1 = java.lang.System.out
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = "Action: "
                r2.append(r3)
                java.lang.String r3 = r11.getAction()
                r2.append(r3)
                java.lang.String r2 = r2.toString()
                r1.println(r2)
                int r1 = r0.hashCode()
                r2 = 2
                r3 = 0
                switch(r1) {
                    case -1993889503: goto L_0x005c;
                    case -1962934264: goto L_0x0052;
                    case -831571810: goto L_0x0048;
                    case -744386077: goto L_0x003e;
                    case 563853263: goto L_0x0034;
                    case 1349144263: goto L_0x002a;
                    default: goto L_0x0029;
                }
            L_0x0029:
                goto L_0x0066
            L_0x002a:
                java.lang.String r1 = "FWVersion"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L_0x0066
                r1 = 2
                goto L_0x0067
            L_0x0034:
                java.lang.String r1 = "BatCapacity"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L_0x0066
                r1 = 3
                goto L_0x0067
            L_0x003e:
                java.lang.String r1 = "ChargingStatus"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L_0x0066
                r1 = 4
                goto L_0x0067
            L_0x0048:
                java.lang.String r1 = "Disconnection_Notification"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L_0x0066
                r1 = 5
                goto L_0x0067
            L_0x0052:
                java.lang.String r1 = "MemoryScanData"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L_0x0066
                r1 = 0
                goto L_0x0067
            L_0x005c:
                java.lang.String r1 = "Memory"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L_0x0066
                r1 = 1
                goto L_0x0067
            L_0x0066:
                r1 = -1
            L_0x0067:
                r4 = 1097859072(0x41700000, float:15.0)
                r5 = 0
                switch(r1) {
                    case 0: goto L_0x0299;
                    case 1: goto L_0x0210;
                    case 2: goto L_0x0197;
                    case 3: goto L_0x0119;
                    case 4: goto L_0x0077;
                    case 5: goto L_0x0070;
                    default: goto L_0x006e;
                }
            L_0x006e:
                goto L_0x02d3
            L_0x0070:
                com.si_ware.neospectra.Activities.HomeActivity r1 = com.si_ware.neospectra.Activities.HomeActivity.this
                r1.endActivity()
                goto L_0x02d3
            L_0x0077:
                java.lang.String r1 = "HomeActivity"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r7 = "Intent Received:\nName: "
                r2.append(r7)
                java.lang.String r7 = "iName"
                java.lang.String r7 = r11.getStringExtra(r7)
                r2.append(r7)
                java.lang.String r7 = "\nSuccess: "
                r2.append(r7)
                java.lang.String r7 = "isNotificationSuccess"
                boolean r3 = r11.getBooleanExtra(r7, r3)
                r2.append(r3)
                java.lang.String r3 = "\nReason: "
                r2.append(r3)
                java.lang.String r3 = "reason"
                java.lang.String r3 = r11.getStringExtra(r3)
                r2.append(r3)
                java.lang.String r3 = "\nError: "
                r2.append(r3)
                java.lang.String r3 = "err"
                java.lang.String r3 = r11.getStringExtra(r3)
                r2.append(r3)
                java.lang.String r3 = "\ndata : "
                r2.append(r3)
                java.lang.String r3 = "data"
                long r7 = r11.getLongExtra(r3, r5)
                r2.append(r7)
                java.lang.String r3 = "\n"
                r2.append(r3)
                java.lang.String r2 = r2.toString()
                com.si_ware.neospectra.Global.MethodsFactory.logMessage(r1, r2)
                java.lang.String r1 = "data"
                long r1 = r11.getLongExtra(r1, r5)
                int r3 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
                if (r3 != 0) goto L_0x00e5
                com.si_ware.neospectra.Activities.HomeActivity r3 = com.si_ware.neospectra.Activities.HomeActivity.this
                android.widget.TextView r3 = r3.tvBatteryStatus
                java.lang.String r4 = ""
                r3.setText(r4)
                goto L_0x02d3
            L_0x00e5:
                r5 = 1
                int r3 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
                if (r3 != 0) goto L_0x00f6
                com.si_ware.neospectra.Activities.HomeActivity r3 = com.si_ware.neospectra.Activities.HomeActivity.this
                android.widget.TextView r3 = r3.tvBatteryStatus
                java.lang.String r4 = "Charging"
                r3.setText(r4)
                goto L_0x02d3
            L_0x00f6:
                r5 = 2
                int r3 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
                if (r3 != 0) goto L_0x010e
                com.si_ware.neospectra.Activities.HomeActivity r3 = com.si_ware.neospectra.Activities.HomeActivity.this
                android.widget.TextView r3 = r3.tvBatteryStatus
                java.lang.String r5 = "Fast Charging"
                r3.setText(r5)
                com.si_ware.neospectra.Activities.HomeActivity r3 = com.si_ware.neospectra.Activities.HomeActivity.this
                android.widget.TextView r3 = r3.tvBatteryStatus
                r3.setTextSize(r4)
                goto L_0x02d3
            L_0x010e:
                com.si_ware.neospectra.Activities.HomeActivity r3 = com.si_ware.neospectra.Activities.HomeActivity.this
                android.widget.TextView r3 = r3.tvBatteryStatus
                java.lang.String r4 = ""
                r3.setText(r4)
                goto L_0x02d3
            L_0x0119:
                java.lang.String r1 = "HomeActivity"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r4 = "Intent Received:\nName: "
                r2.append(r4)
                java.lang.String r4 = "iName"
                java.lang.String r4 = r11.getStringExtra(r4)
                r2.append(r4)
                java.lang.String r4 = "\nSuccess: "
                r2.append(r4)
                java.lang.String r4 = "isNotificationSuccess"
                boolean r3 = r11.getBooleanExtra(r4, r3)
                r2.append(r3)
                java.lang.String r3 = "\nReason: "
                r2.append(r3)
                java.lang.String r3 = "reason"
                java.lang.String r3 = r11.getStringExtra(r3)
                r2.append(r3)
                java.lang.String r3 = "\nError: "
                r2.append(r3)
                java.lang.String r3 = "err"
                java.lang.String r3 = r11.getStringExtra(r3)
                r2.append(r3)
                java.lang.String r3 = "\ndata : "
                r2.append(r3)
                java.lang.String r3 = "data"
                long r3 = r11.getLongExtra(r3, r5)
                r2.append(r3)
                java.lang.String r3 = "\n"
                r2.append(r3)
                java.lang.String r2 = r2.toString()
                com.si_ware.neospectra.Global.MethodsFactory.logMessage(r1, r2)
                com.si_ware.neospectra.Activities.HomeActivity r1 = com.si_ware.neospectra.Activities.HomeActivity.this
                android.widget.TextView r1 = r1.tvBattery
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = ""
                r2.append(r3)
                java.lang.String r3 = "data"
                long r3 = r11.getLongExtra(r3, r5)
                r2.append(r3)
                java.lang.String r3 = "%"
                r2.append(r3)
                java.lang.String r2 = r2.toString()
                r1.setText(r2)
                goto L_0x02d3
            L_0x0197:
                java.lang.String r1 = "HomeActivity"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r4 = "Intent Received:\nName: "
                r2.append(r4)
                java.lang.String r4 = "iName"
                java.lang.String r4 = r11.getStringExtra(r4)
                r2.append(r4)
                java.lang.String r4 = "\nSuccess: "
                r2.append(r4)
                java.lang.String r4 = "isNotificationSuccess"
                boolean r3 = r11.getBooleanExtra(r4, r3)
                r2.append(r3)
                java.lang.String r3 = "\nReason: "
                r2.append(r3)
                java.lang.String r3 = "reason"
                java.lang.String r3 = r11.getStringExtra(r3)
                r2.append(r3)
                java.lang.String r3 = "\nError: "
                r2.append(r3)
                java.lang.String r3 = "err"
                java.lang.String r3 = r11.getStringExtra(r3)
                r2.append(r3)
                java.lang.String r3 = "\ndata : "
                r2.append(r3)
                java.lang.String r3 = "data"
                long r3 = r11.getLongExtra(r3, r5)
                r2.append(r3)
                java.lang.String r3 = "\n"
                r2.append(r3)
                java.lang.String r2 = r2.toString()
                com.si_ware.neospectra.Global.MethodsFactory.logMessage(r1, r2)
                com.si_ware.neospectra.Activities.HomeActivity r1 = com.si_ware.neospectra.Activities.HomeActivity.this
                android.widget.TextView r1 = r1.tv_firmware_version
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = ""
                r2.append(r3)
                java.lang.String r3 = "data"
                long r3 = r11.getLongExtra(r3, r5)
                r2.append(r3)
                java.lang.String r2 = r2.toString()
                r1.setText(r2)
                goto L_0x02d3
            L_0x0210:
                java.lang.String r1 = "HomeActivity"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r7 = "Intent Received:\nName: "
                r2.append(r7)
                java.lang.String r7 = "iName"
                java.lang.String r7 = r11.getStringExtra(r7)
                r2.append(r7)
                java.lang.String r7 = "\nSuccess: "
                r2.append(r7)
                java.lang.String r7 = "isNotificationSuccess"
                boolean r3 = r11.getBooleanExtra(r7, r3)
                r2.append(r3)
                java.lang.String r3 = "\nReason: "
                r2.append(r3)
                java.lang.String r3 = "reason"
                java.lang.String r3 = r11.getStringExtra(r3)
                r2.append(r3)
                java.lang.String r3 = "\nError: "
                r2.append(r3)
                java.lang.String r3 = "err"
                java.lang.String r3 = r11.getStringExtra(r3)
                r2.append(r3)
                java.lang.String r3 = "\ndata : "
                r2.append(r3)
                java.lang.String r3 = "data"
                long r7 = r11.getLongExtra(r3, r5)
                r2.append(r7)
                java.lang.String r3 = "\n"
                r2.append(r3)
                java.lang.String r2 = r2.toString()
                com.si_ware.neospectra.Global.MethodsFactory.logMessage(r1, r2)
                com.si_ware.neospectra.Activities.HomeActivity r1 = com.si_ware.neospectra.Activities.HomeActivity.this
                android.widget.TextView r1 = r1.tv_memory
                r1.setTextSize(r4)
                com.si_ware.neospectra.Activities.HomeActivity r1 = com.si_ware.neospectra.Activities.HomeActivity.this
                android.widget.TextView r1 = r1.tv_memory
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = ""
                r2.append(r3)
                java.lang.String r3 = "data"
                long r3 = r11.getLongExtra(r3, r5)
                r2.append(r3)
                java.lang.String r3 = "/"
                r2.append(r3)
                r3 = 150(0x96, float:2.1E-43)
                r2.append(r3)
                java.lang.String r2 = r2.toString()
                r1.setText(r2)
                goto L_0x02d3
            L_0x0299:
                java.lang.String r1 = "data"
                double[] r1 = r11.getDoubleArrayExtra(r1)
                if (r1 != 0) goto L_0x02a9
                java.lang.String r2 = "HomeView"
                java.lang.String r3 = "Reading is NULL."
                com.si_ware.neospectra.Global.MethodsFactory.logMessage(r2, r3)
                return
            L_0x02a9:
                int r4 = r1.length
                int r4 = r4 / r2
                double[] r2 = new double[r4]
                double[] r5 = new double[r4]
            L_0x02b0:
                if (r3 >= r4) goto L_0x02bf
                r6 = r1[r3]
                r5[r3] = r6
                int r6 = r4 + r3
                r6 = r1[r6]
                r2[r3] = r6
                int r3 = r3 + 1
                goto L_0x02b0
            L_0x02bf:
                int r3 = r5.length
                if (r3 <= 0) goto L_0x02d3
                int r3 = r2.length
                if (r3 <= 0) goto L_0x02d3
                com.si_ware.neospectra.Models.dbReading r3 = new com.si_ware.neospectra.Models.dbReading
                r3.<init>()
                r3.setReading(r5, r2)
                java.util.ArrayList<com.si_ware.neospectra.Models.dbReading> r6 = com.si_ware.neospectra.Global.GlobalVariables.gAllSpectra
                r6.add(r3)
            L_0x02d3:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.si_ware.neospectra.Activities.HomeActivity.C11778.onReceive(android.content.Context, android.content.Intent):void");
        }
    };
    Button mOTASettingsButton;
    Button mScanButton;
    Button mScanHistoryButton;
    Button mSuperuserBtn;
    Button mViewMeasurements;
    ImageButton menuButton;
    int superUserVisibility = 4;
    TextView tvBattery;
    TextView tvBatteryStatus;
    TextView tv_device_name;
    TextView tv_firmware_version;
    TextView tv_memory;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate = ((LayoutInflater) getSystemService("layout_inflater")).inflate(C1284R.layout.activity_home2, null, false);
        setRequestedOrientation(14);
        if (GlobalVariables.gDeviceName.indexOf("_") != -1) {
            GlobalVariables.gDeviceName = GlobalVariables.gDeviceName.substring(GlobalVariables.gDeviceName.indexOf("_") + 1);
        }
        this.mContext = this;
        setContentView((int) C1284R.layout.activity_home2);
        this.mScanButton = (Button) findViewById(C1284R.C1286id.button_scan);
        this.mSuperuserBtn = (Button) findViewById(C1284R.C1286id.superuser_btn);
        this.mDisconnectDevice = (Button) findViewById(C1284R.C1286id.btn_disconnect);
        this.mConfigureDevice = (Button) findViewById(C1284R.C1286id.button_configure);
        this.tv_device_name = (TextView) findViewById(C1284R.C1286id.button_device_name);
        this.tv_firmware_version = (TextView) findViewById(C1284R.C1286id.button_firmware_version);
        this.tv_memory = (TextView) findViewById(C1284R.C1286id.tv_memory_percentage);
        this.tvBattery = (TextView) findViewById(C1284R.C1286id.tv_battery_percentage);
        this.tvBatteryStatus = (TextView) findViewById(C1284R.C1286id.tv_battery_status);
        this.mOTASettingsButton = (Button) findViewById(C1284R.C1286id.button_ota_configure);
        this.menuButton = (ImageButton) findViewById(C1284R.C1286id.home_menu_button);
        this.mViewMeasurements = (Button) findViewById(C1284R.C1286id.btn_view_measurements);
        this.tv_device_name.setText(GlobalVariables.gDeviceName);
        if (GlobalVariables.bluetoothAPI != null) {
            GlobalVariables.bluetoothAPI.setHomeContext(this.mContext);
            GlobalVariables.bluetoothAPI.sendMemoryRequest();
            GlobalVariables.bluetoothAPI.sendBatRequest();
        }
        this.mSuperuserBtn.setVisibility(this.superUserVisibility);
        this.mSuperuserBtn.setOnClickListener(new HomeActivity$$Lambda$0(this));
        this.mScanButton.setOnClickListener(new HomeActivity$$Lambda$1(this));
        this.mViewMeasurements.setOnClickListener(new HomeActivity$$Lambda$2(this));
        this.mOTASettingsButton.setOnClickListener(new HomeActivity$$Lambda$3(this));
        this.mConfigureDevice.setOnClickListener(new HomeActivity$$Lambda$4(this));
        this.mDisconnectDevice.setOnClickListener(new HomeActivity$$Lambda$5(this));
        this.menuButton.setOnClickListener(new HomeActivity$$Lambda$6(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$0$HomeActivity(View v) {
        startActivity(new Intent(this, SuperUserActivity.class));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$1$HomeActivity(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$2$HomeActivity(View v) {
        GlobalVariables.measurmentsViewCaller = HomeActivity.class;
        startActivity(new Intent(this, ResultsActivity.class));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$3$HomeActivity(View v) {
        startActivity(new Intent(this, OTAActivity.class));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$4$HomeActivity(View v) {
        startActivity(new Intent(this, ConfigureActivity.class));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$5$HomeActivity(View v) {
        Builder myAlert = new Builder(this.mContext);
        myAlert.setTitle((CharSequence) "Disconnect");
        myAlert.setMessage((CharSequence) "Are you sure you want to disconnect the device?");
        myAlert.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (GlobalVariables.bluetoothAPI != null) {
                    GlobalVariables.bluetoothAPI.disconnectFromDevice();
                }
                Intent iMain = new Intent(HomeActivity.this.mContext, ConnectActivity.class);
                iMain.setFlags(268468224);
                HomeActivity.this.startActivity(iMain);
            }
        });
        myAlert.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        myAlert.show();
    }

    /* renamed from: showPopup */
    public void lambda$onCreate$6$HomeActivity(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(C1284R.C1287menu.home_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new HomeActivity$$Lambda$7(this));
        popup.show();
    }

    /* renamed from: onMenuItemClick */
    public boolean lambda$showPopup$7$HomeActivity(MenuItem item) {
        switch (item.getItemId()) {
            case C1284R.C1286id.SWabout /*2131361809*/:
                Builder myAlert = new Builder(this.mContext);
                String virsion = "";
                try {
                    ApplicationInfo ai = getPackageManager().getApplicationInfo(BuildConfig.APPLICATION_ID, 128);
                    StringBuilder sb = new StringBuilder();
                    sb.append(ai.metaData.getString("Specification-Version"));
                    sb.append(".");
                    sb.append(ai.metaData.getString("Implementation-Version"));
                    virsion = sb.toString();
                } catch (NameNotFoundException e) {
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Software : NeoSpectra-Scanner ™ © 2019 SWS.\r\nVersion : ");
                sb2.append(virsion);
                sb2.append("\r\nProvided by : Si-Ware Systems.\r\nWebsite: www.si-ware.com\r\nFor assistance please contact neospectra.support@si-ware.com");
                myAlert.setMessage((CharSequence) sb2.toString());
                myAlert.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                myAlert.show();
                return true;
            case C1284R.C1286id.clear_hist /*2131361898*/:
                if (GlobalVariables.bluetoothAPI != null) {
                    GlobalVariables.bluetoothAPI.sendClearMemoryRequest();
                }
                return true;
            case C1284R.C1286id.get_hist /*2131361953*/:
                if (GlobalVariables.bluetoothAPI != null) {
                    final String numberOfSavedSpectra1 = this.tv_memory.getText().toString().split("/")[0];
                    final Intent popupIntent = new Intent(this, PopupActivity.class);
                    Builder myAlert2 = new Builder(this.mContext);
                    myAlert2.setTitle((CharSequence) "Add Files name");
                    final EditText input = new EditText(this);
                    input.setInputType(1);
                    input.setText("");
                    myAlert2.setView((View) input);
                    myAlert2.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                            popupIntent.putExtra("FilesName", input.getText().toString());
                            dialogInterface.dismiss();
                            popupIntent.putExtra("NumberOfSavedSpectra", numberOfSavedSpectra1);
                            HomeActivity.this.startActivity(popupIntent);
                        }
                    });
                    myAlert2.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    myAlert2.show();
                }
                return true;
            case C1284R.C1286id.menuItemOTAReset /*2131362014*/:
                Intent popupIntent2 = new Intent(this, OTA_popup.class);
                popupIntent2.putExtra("Action", OTA_Functions.RESET_FIRMWARE.getValue());
                startActivity(popupIntent2);
                return true;
            case C1284R.C1286id.menuItemUpdateFirmware /*2131362015*/:
                Intent popupIntent3 = new Intent(this, OTA_popup.class);
                popupIntent3.putExtra("Action", OTA_Functions.UPDATE_FIRMWARE.getValue());
                startActivity(popupIntent3);
                return true;
            default:
                return false;
        }
    }

    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mMessageReceiver, new IntentFilter(GlobalVariables.HOME_INTENT_ACTION));
        if (GlobalVariables.bluetoothAPI != null && !GlobalVariables.bluetoothAPI.isDeviceConnected()) {
            endActivity();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mMessageReceiver);
    }

    public void onBackPressed() {
        Builder myAlert = new Builder(this.mContext);
        myAlert.setTitle((CharSequence) "Disconnect");
        myAlert.setMessage((CharSequence) "Are you sure you want to disconnect the device?");
        myAlert.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (GlobalVariables.bluetoothAPI != null) {
                    GlobalVariables.bluetoothAPI.disconnectFromDevice();
                }
                Intent iMain = new Intent(HomeActivity.this.mContext, ConnectActivity.class);
                iMain.setFlags(268468224);
                HomeActivity.this.startActivity(iMain);
            }
        });
        myAlert.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        myAlert.show();
    }

    public void onClick(View v) {
    }

    /* access modifiers changed from: private */
    public void endActivity() {
        GlobalVariables.bluetoothAPI = null;
        startActivity(new Intent(this, ConnectActivity.class));
    }
}
