package com.si_ware.neospectra.Activities;

import android.os.Bundle;
import android.support.p004v7.app.AppCompatActivity;
import android.support.p004v7.preference.PreferenceManager;
import com.si_ware.neospectra.Activities.MainFragments.SettingsFragment;
import com.si_ware.neospectra.C1284R;

public class ConfigureActivity extends AppCompatActivity {
    public static final String KEY_PREF_EXAMPLE_SWITCH = "example_switch";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(14);
        PreferenceManager.setDefaultValues(this, C1284R.xml.pref_general, false);
        getSupportFragmentManager().beginTransaction().replace(16908290, new SettingsFragment()).commit();
    }
}
