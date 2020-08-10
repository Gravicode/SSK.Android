package com.si_ware.neospectra.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.p001v4.view.GravityCompat;
import android.support.p001v4.widget.DrawerLayout;
import android.support.p004v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.si_ware.neospectra.C1284R;

public class NavDrawerActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {
    public static final int GOTO_FRAGMENT_ABOUT_US = 3;
    public static final int GOTO_FRAGMENT_CONFIGURE = 1;
    public static final int GOTO_FRAGMENT_HISTORY = 4;
    public static final int GOTO_FRAGMENT_OTA = 6;
    public static final int GOTO_FRAGMENT_POWER = 5;
    public static final int GOTO_FRAGMENT_SCAN = 0;
    public static final int GOTO_FRAGMENT_SHOP = 2;
    public NavigationView navigationView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1284R.layout.activity_nav_drawer);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        handleItemSelection(item);
        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        handleItemSelection(item);
        ((DrawerLayout) findViewById(C1284R.C1286id.drawer_layout)).closeDrawer((int) GravityCompat.START);
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void handleItemSelection(@NonNull MenuItem item) {
        if (item.getItemId() == C1284R.C1286id.nav_disconnect) {
        }
    }
}
