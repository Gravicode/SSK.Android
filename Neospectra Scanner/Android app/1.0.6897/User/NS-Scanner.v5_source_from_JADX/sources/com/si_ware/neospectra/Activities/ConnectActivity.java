package com.si_ware.neospectra.Activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.p001v4.app.ActivityCompat;
import android.support.p004v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3API;
import com.si_ware.neospectra.C1284R;
import com.si_ware.neospectra.Global.GlobalVariables;

public class ConnectActivity extends AppCompatActivity {
    private static Context mContext;
    String[] PERMISSIONS = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_EXTERNAL_STORAGE"};
    final int PERMISSION_ALL = 1;
    private boolean isBluetoothEnabled = false;
    private boolean isLocationEnabled = false;

    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1284R.layout.activity_connect);
        mContext = this;
        GlobalVariables.bluetoothAPI = new SWS_P3API(this, mContext);
        setRequestedOrientation(14);
        BluetoothAdapter.getDefaultAdapter().enable();
        Button connectButton = (Button) findViewById(C1284R.C1286id.button_connect);
        if (!hasPermissions(this, this.PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, this.PERMISSIONS, 1);
        }
        connectButton.setOnClickListener(new ConnectActivity$$Lambda$0(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$0$ConnectActivity(View v) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (!(context == null || permissions == null)) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
