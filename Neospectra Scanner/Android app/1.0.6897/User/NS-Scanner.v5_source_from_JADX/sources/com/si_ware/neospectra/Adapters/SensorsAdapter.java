package com.si_ware.neospectra.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.p004v7.widget.RecyclerView.Adapter;
import android.support.p004v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.si_ware.neospectra.Activities.HomeActivity;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3BLEDevice;
import com.si_ware.neospectra.C1284R;
import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.Global.MethodsFactory;
import java.util.List;

public class SensorsAdapter extends Adapter<SensorViewHolder> {
    @NonNull
    private String TAG = "SensorAdapter";
    private LayoutInflater inflater;
    private Context mContext;
    private List<SWS_P3BLEDevice> sensors;

    public class SensorViewHolder extends ViewHolder {
        Button btnConnect;
        ProgressBar progressBar;
        TextView tvMac;
        TextView tvName;

        public SensorViewHolder(@NonNull View itemView) {
            super(itemView);
            this.btnConnect = (Button) itemView.findViewById(C1284R.C1286id.btn_connect_disconnect);
            this.tvName = (TextView) itemView.findViewById(C1284R.C1286id.tv_sensor_name);
            this.tvMac = (TextView) itemView.findViewById(C1284R.C1286id.tv_mac);
            this.progressBar = (ProgressBar) itemView.findViewById(C1284R.C1286id.pb_connecting);
            MethodsFactory.logMessage("bluetooth .. adapter", "Sensor view holder");
        }
    }

    public SensorsAdapter(Context context, List<SWS_P3BLEDevice> devices) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(this.mContext);
        this.sensors = devices;
        StringBuilder sb = new StringBuilder();
        sb.append("Devices in constructor length: ");
        sb.append(devices.size());
        sb.append(", Sensors: ");
        sb.append(this.sensors.size());
        MethodsFactory.logMessage("blutooth", sb.toString());
    }

    @NonNull
    public SensorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MethodsFactory.logMessage("bluetooth .. adapter", "Creating view holder 1");
        View view = this.inflater.inflate(C1284R.layout.row_bluetooth_device, parent, false);
        MethodsFactory.logMessage("bluetooth .. adapter", "Creating view holder 2");
        return new SensorViewHolder(view);
    }

    public void onBindViewHolder(@NonNull SensorViewHolder currentViewHolder, int position) {
        String str = "bluetooth .. adapter";
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Binding in adapter, the list length equals: ");
            sb.append(this.sensors.size());
            MethodsFactory.logMessage(str, sb.toString());
            SWS_P3BLEDevice currentDevice = (SWS_P3BLEDevice) this.sensors.get(position);
            String name = currentDevice.getDeviceName();
            if (name != null) {
                currentViewHolder.tvName.setText(name);
            }
            String mac = currentDevice.getDeviceMacAddress();
            if (mac != null) {
                currentViewHolder.tvMac.setText(mac);
            }
            ProgressBar mScanProgress = currentViewHolder.progressBar;
            Button btnConnect = currentViewHolder.btnConnect;
            if (!GlobalVariables.bluetoothAPI.isDeviceConnected()) {
                btnConnect.setText(this.mContext.getResources().getString(C1284R.string.connect));
                currentViewHolder.tvName.setTypeface(Typeface.DEFAULT);
            }
            SensorsAdapter$$Lambda$0 sensorsAdapter$$Lambda$0 = new SensorsAdapter$$Lambda$0(this, mScanProgress, position, btnConnect, currentViewHolder);
            btnConnect.setOnClickListener(sensorsAdapter$$Lambda$0);
        } catch (Exception e) {
            MethodsFactory.logMessage(this.TAG, e.getMessage());
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onBindViewHolder$0$SensorsAdapter(ProgressBar mScanProgress, int position, Button btnConnect, @NonNull SensorViewHolder currentViewHolder, View v) {
        mScanProgress.setVisibility(0);
        GlobalVariables.bluetoothAPI.connectToDevice((SWS_P3BLEDevice) this.sensors.get(position), mScanProgress, btnConnect, currentViewHolder.tvName);
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Intent iConnected = new Intent(this.mContext, HomeActivity.class);
        GlobalVariables.gDeviceName = ((SWS_P3BLEDevice) this.sensors.get(position)).getDeviceName();
        this.mContext.startActivity(iConnected);
    }

    public int getItemCount() {
        StringBuilder sb = new StringBuilder();
        sb.append("getItemCount() has: ");
        sb.append(this.sensors.size());
        sb.append(" items");
        MethodsFactory.logMessage("bluetooth .. adapter", sb.toString());
        return this.sensors.size();
    }
}
