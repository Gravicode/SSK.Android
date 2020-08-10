package com.google.android.gms.common.api;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.internal.zzbm;

public class GoogleApiActivity extends Activity implements OnCancelListener {
    private int zzfmm = 0;

    public static PendingIntent zza(Context context, PendingIntent pendingIntent, int i) {
        return PendingIntent.getActivity(context, 0, zza(context, pendingIntent, i, true), 134217728);
    }

    public static Intent zza(Context context, PendingIntent pendingIntent, int i, boolean z) {
        Intent intent = new Intent(context, GoogleApiActivity.class);
        intent.putExtra("pending_intent", pendingIntent);
        intent.putExtra("failing_client_id", i);
        intent.putExtra("notify_manager", z);
        return intent;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1) {
            boolean booleanExtra = getIntent().getBooleanExtra("notify_manager", true);
            this.zzfmm = 0;
            setResult(i2, intent);
            if (booleanExtra) {
                zzbm zzcj = zzbm.zzcj(this);
                switch (i2) {
                    case -1:
                        zzcj.zzagz();
                        break;
                    case 0:
                        zzcj.zza(new ConnectionResult(13, null), getIntent().getIntExtra("failing_client_id", -1));
                        break;
                }
            }
        } else if (i == 2) {
            this.zzfmm = 0;
            setResult(i2, intent);
        }
        finish();
    }

    public void onCancel(DialogInterface dialogInterface) {
        this.zzfmm = 0;
        setResult(0);
        finish();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        String str;
        String str2;
        super.onCreate(bundle);
        if (bundle != null) {
            this.zzfmm = bundle.getInt("resolution");
        }
        if (this.zzfmm != 1) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                str = "GoogleApiActivity";
                str2 = "Activity started without extras";
            } else {
                PendingIntent pendingIntent = (PendingIntent) extras.get("pending_intent");
                Integer num = (Integer) extras.get("error_code");
                if (pendingIntent == null && num == null) {
                    str = "GoogleApiActivity";
                    str2 = "Activity started without resolution";
                } else if (pendingIntent != null) {
                    try {
                        startIntentSenderForResult(pendingIntent.getIntentSender(), 1, null, 0, 0, 0);
                        this.zzfmm = 1;
                        return;
                    } catch (SendIntentException e) {
                        Log.e("GoogleApiActivity", "Failed to launch pendingIntent", e);
                    }
                } else {
                    GoogleApiAvailability.getInstance().showErrorDialogFragment(this, num.intValue(), 2, this);
                    this.zzfmm = 1;
                }
            }
            Log.e(str, str2);
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt("resolution", this.zzfmm);
        super.onSaveInstanceState(bundle);
    }
}
