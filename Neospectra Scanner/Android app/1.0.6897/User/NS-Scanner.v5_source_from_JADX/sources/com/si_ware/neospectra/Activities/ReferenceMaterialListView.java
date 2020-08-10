package com.si_ware.neospectra.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.si_ware.neospectra.C1284R;
import com.si_ware.neospectra.Global.MethodsFactory;
import java.util.ArrayList;
import java.util.List;

public class ReferenceMaterialListView extends Activity {
    Button Addbutton;
    EditText GetValue;
    Double[] ListElements = new Double[5];
    ListView listview;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C1284R.layout.listview);
        this.listview = (ListView) findViewById(C1284R.C1286id.listView1);
        this.Addbutton = (Button) findViewById(C1284R.C1286id.button1);
        this.GetValue = (EditText) findViewById(C1284R.C1286id.editText1);
        final List<Double> ListElementsArrayList = new ArrayList<>();
        final ArrayAdapter<Double> adapter = new ArrayAdapter<>(this, 17367043, ListElementsArrayList);
        this.listview.setEmptyView(findViewById(C1284R.C1286id.emptyElement));
        this.listview.setAdapter(adapter);
        this.Addbutton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ListElementsArrayList.size() == 5) {
                    MethodsFactory.showAlertMessage(ReferenceMaterialListView.this, "Warning", "You reach the maximum number of wavelength values: 5");
                    return;
                }
                ListElementsArrayList.add(Double.valueOf(Double.parseDouble(ReferenceMaterialListView.this.GetValue.getText().toString())));
                ReferenceMaterialListView.this.GetValue.setText("");
                adapter.notifyDataSetChanged();
            }
        });
    }
}
