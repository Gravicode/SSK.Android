package com.si_ware.neospectra.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.p004v7.app.AppCompatActivity;
import com.si_ware.neospectra.C1284R;
import com.si_ware.neospectra.Global.GlobalVariables;

public class SplashActivity extends AppCompatActivity {
    private Context mContext;

    private class ReadModules extends AsyncTask<Void, Void, String> {
        private ReadModules() {
        }

        /* access modifiers changed from: protected */
        public String doInBackground(Void... Voids) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "";
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String s) {
            AsyncTask.execute(new Runnable() {
                public void run() {
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, ConnectActivity.class));
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1284R.layout.activity_splash);
        this.mContext = getApplicationContext();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        new ReadModules().execute(new Void[0]);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (GlobalVariables.bluetoothAPI != null) {
            GlobalVariables.bluetoothAPI.disconnectFromDevice();
        }
    }
}
