package com.si_ware.neospectra.Adapters;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import com.si_ware.neospectra.Adapters.SensorsAdapter.SensorViewHolder;

final /* synthetic */ class SensorsAdapter$$Lambda$0 implements OnClickListener {
    private final SensorsAdapter arg$1;
    private final ProgressBar arg$2;
    private final int arg$3;
    private final Button arg$4;
    private final SensorViewHolder arg$5;

    SensorsAdapter$$Lambda$0(SensorsAdapter sensorsAdapter, ProgressBar progressBar, int i, Button button, SensorViewHolder sensorViewHolder) {
        this.arg$1 = sensorsAdapter;
        this.arg$2 = progressBar;
        this.arg$3 = i;
        this.arg$4 = button;
        this.arg$5 = sensorViewHolder;
    }

    public void onClick(View view) {
        this.arg$1.lambda$onBindViewHolder$0$SensorsAdapter(this.arg$2, this.arg$3, this.arg$4, this.arg$5, view);
    }
}
