package com.si_ware.neospectra.dbtable;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.TimeZone;

public class addUpdateRecord extends AppCompatActivity {

    private EditText edtBray, edtCa, edtClay, edtCn;
    private FloatingActionButton saveBtn;

    private String bray, ca, clay, cn;

    //dbHelper
    private DbHelper dbHelper;

    //action bar

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_record);


        //init
        actionBar = getSupportActionBar();
        //title actionn bar
        actionBar.setTitle("Add Record");
        //back button
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        edtBray = findViewById(R.id.edtBray);
        edtCa = findViewById(R.id.edtCa);
        edtClay = findViewById(R.id.edtClay);
        edtCn = findViewById(R.id.edtCn);
        saveBtn = findViewById(R.id.saveBtn);
        //init db helper
        dbHelper = new DbHelper(this);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inputData();

            }
        });


    }

    private void inputData() {
        // get data
        bray = "" + edtBray.getText().toString().trim();
        ca = "" + edtCa.getText().toString().trim();
        clay = "" + edtClay.getText().toString().trim();
        cn = "" + edtCn.getText().toString().trim();

        //save to db
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int tahun = calendar.get(Calendar.YEAR);
        int bulan = calendar.get(Calendar.MONTH) + 1;
        int tanggal = calendar.get(Calendar.DAY_OF_MONTH);
        String timestamp = "" + tahun + "-" + bulan + "-" + tanggal;


        long id = dbHelper.insertRecord(
                "" + bray,
                "" + ca,
                "" + clay,
                "" + cn,
                "" + timestamp


        );

        Toast.makeText(this, "Record Added againts ID:" + id, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}