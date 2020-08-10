package com.si_ware.neospectra.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Paint.Align;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.p001v4.app.ActivityCompat;
import android.support.p001v4.internal.view.SupportMenu;
import android.support.p001v4.view.InputDeviceCompat;
import android.support.p004v7.app.AlertDialog.Builder;
import android.support.p004v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer.LegendAlign;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.si_ware.neospectra.C1284R;
import com.si_ware.neospectra.ChartView.C1236ChartView;
import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.Global.MethodsFactory;
import com.si_ware.neospectra.Models.dbReading;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ResultsActivity extends AppCompatActivity implements Comparator<File> {
    private static final String TAG = "Results Activity";
    private static int[] colors = {SupportMenu.CATEGORY_MASK, -16776961, -16711936, -16711681, -65409, -8453889, -65281, InputDeviceCompat.SOURCE_ANY, -16744449, -33024, -16711809, -8388864};
    private static int measurementCount_Spectroscopy = 0;
    public static boolean rdbtn_nm_Spec = true;
    public static boolean rdbtn_ref_Spec = true;
    private File[] files = null;
    /* access modifiers changed from: private */
    public String filesName = "";
    private String from;
    private Intent intent;
    private ImageView mBackButton;
    private C1236ChartView mChartView;
    private Button mClearButton;
    /* access modifiers changed from: private */
    public Context mContext;
    private GraphView mGraphView;
    private Button mLoadButton;
    /* access modifiers changed from: private */
    public Button mSaveButton;
    private double maxValue = 0.0d;
    private Switch simpleSwitch;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C1284R.layout.activity_results);
        setRequestedOrientation(4);
        this.mContext = this;
        isWriteStoragePermissionGranted();
        this.mGraphView = (GraphView) findViewById(C1284R.C1286id.chart_view);
        this.mClearButton = (Button) findViewById(C1284R.C1286id.btn_clear);
        this.mClearButton.setOnClickListener(new ResultsActivity$$Lambda$0(this));
        this.mSaveButton = (Button) findViewById(C1284R.C1286id.btn_SaveResults);
        this.mSaveButton.setOnClickListener(new ResultsActivity$$Lambda$1(this));
        displayGraph();
        this.mLoadButton = (Button) findViewById(C1284R.C1286id.btn_LoadResults);
        this.mLoadButton.setOnClickListener(new ResultsActivity$$Lambda$2(this));
        this.mGraphView.getLegendRenderer().setVisible(true);
        this.mGraphView.getLegendRenderer().setAlign(LegendAlign.TOP);
        if (this.maxValue == 0.0d) {
            this.mGraphView.getViewport().setMinY(90.0d);
            this.mGraphView.getViewport().setMaxY(110.0d);
            this.mGraphView.getViewport().setMinX(1100.0d);
            this.mGraphView.getViewport().setMaxX(2650.0d);
        } else {
            this.mGraphView.getViewport().setMaxY(this.maxValue);
            this.mGraphView.getViewport().setScalable(true);
            this.mGraphView.getViewport().setScrollable(true);
            this.mGraphView.getViewport().setScalableY(true);
            this.mGraphView.getViewport().setScrollableY(true);
        }
        this.mGraphView.getViewport().setYAxisBoundsManual(true);
        this.mGraphView.getViewport().setXAxisBoundsManual(true);
        this.mGraphView.getGridLabelRenderer().setHorizontalLabelsAngle(45);
        this.mGraphView.getGridLabelRenderer().setHorizontalAxisTitle("nm");
        this.mGraphView.getGridLabelRenderer().setVerticalAxisTitle("%Refl.");
        this.mGraphView.getGridLabelRenderer().setVerticalLabelsAlign(Align.RIGHT);
        this.mGraphView.getViewport().setDrawBorder(true);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$0$ResultsActivity(View v) {
        this.maxValue = 0.0d;
        if (!this.mGraphView.getSeries().isEmpty()) {
            this.mGraphView.removeAllSeries();
        }
        this.mGraphView.getViewport().setMinY(90.0d);
        this.mGraphView.getViewport().setMaxY(110.0d);
        this.mGraphView.getViewport().setMinX(1100.0d);
        this.mGraphView.getViewport().setMaxX(2650.0d);
        this.mGraphView.getViewport().setScalable(false);
        this.mGraphView.getViewport().setScrollable(false);
        this.mGraphView.getViewport().setScalableY(false);
        this.mGraphView.getViewport().setScrollableY(false);
        GlobalVariables.gAllSpectra.clear();
        measurementCount_Spectroscopy = 0;
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$1$ResultsActivity(View v) {
        Builder myAlert = new Builder(this.mContext);
        myAlert.setTitle((CharSequence) "Add Files name");
        final EditText input = new EditText(this);
        input.setInputType(1);
        input.setText("");
        myAlert.setView((View) input);
        myAlert.setPositiveButton((CharSequence) "OK", (OnClickListener) new OnClickListener() {
            public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                int j = 0;
                Boolean isRepeated = Boolean.valueOf(false);
                ResultsActivity.this.filesName = input.getText().toString();
                String[] filesInDirectory = Environment.getExternalStoragePublicDirectory(GlobalVariables.OutputDirectory).list();
                while (true) {
                    if (j >= filesInDirectory.length) {
                        break;
                    } else if (filesInDirectory[j].contains(ResultsActivity.this.filesName)) {
                        isRepeated = Boolean.valueOf(true);
                        break;
                    } else {
                        j++;
                    }
                }
                if (isRepeated.booleanValue() == 0) {
                    ResultsActivity.this.saveResults();
                    dialogInterface.dismiss();
                    MethodsFactory.showAlertMessage(ResultsActivity.this.mContext, "Success in saving files", "Data are saved successfully in DCIM/NeoSpectra folder");
                    return;
                }
                dialogInterface.dismiss();
                Builder myAlertOk = new Builder(ResultsActivity.this.mContext);
                myAlertOk.setTitle((CharSequence) "Warning");
                new EditText(ResultsActivity.this.mContext);
                myAlertOk.setMessage((CharSequence) "There a file/files saved with the same name");
                myAlertOk.setPositiveButton((CharSequence) "Continue", (OnClickListener) new OnClickListener() {
                    public void onClick(@NonNull DialogInterface dialogInterfaceOk, int i) {
                        ResultsActivity.this.saveResults();
                        dialogInterfaceOk.dismiss();
                        MethodsFactory.showAlertMessage(ResultsActivity.this.mContext, "Success in saving files", "Data are saved successfully in DCIM/NeoSpectra folder");
                    }
                });
                myAlertOk.setNegativeButton((CharSequence) "Cancel", (OnClickListener) new OnClickListener() {
                    public void onClick(@NonNull DialogInterface dialogInterfaceOk, int i) {
                        dialogInterfaceOk.dismiss();
                        ResultsActivity.this.mSaveButton.callOnClick();
                    }
                });
                myAlertOk.show();
            }
        });
        myAlert.setNegativeButton((CharSequence) "Cancel", (OnClickListener) new OnClickListener() {
            public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        myAlert.show();
        this.filesName = "";
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreate$2$ResultsActivity(View v) {
        this.intent = new Intent("android.intent.action.GET_CONTENT");
        this.intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        this.intent.setAction("android.intent.action.OPEN_DOCUMENT");
        this.intent.setDataAndType(Uri.parse("content://com.android.externalstorage.documents/document/primary%3ADCIM%2FNeoSpectra"), "*/*");
        startActivityForResult(this.intent, 7);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 7 && resultCode == -1 && data != null) {
            int index = 0;
            if (data.getClipData() != null) {
                this.files = new File[data.getClipData().getItemCount()];
                while (true) {
                    int index2 = index;
                    if (index2 >= data.getClipData().getItemCount()) {
                        break;
                    }
                    File file = new File(data.getClipData().getItemAt(index2).getUri().getPath());
                    isReadStoragePermissionGranted();
                    File directory = Environment.getExternalStoragePublicDirectory(GlobalVariables.OutputDirectory);
                    if (directory == null) {
                        MethodsFactory.showAlertMessage(this, "Error in loading files", "No data to be loaded!");
                        return;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(directory.getAbsolutePath());
                    sb.append(File.separator);
                    sb.append(file.getName());
                    this.files[index2] = new File(sb.toString());
                    index = index2 + 1;
                }
            } else {
                File file2 = new File(data.getData().getPath());
                isReadStoragePermissionGranted();
                File directory2 = Environment.getExternalStoragePublicDirectory(GlobalVariables.OutputDirectory);
                if (directory2 == null) {
                    MethodsFactory.showAlertMessage(this, "Error in loading files", "No data to be loaded!");
                    return;
                }
                this.files = new File[1];
                StringBuilder sb2 = new StringBuilder();
                sb2.append(directory2.getAbsolutePath());
                sb2.append(File.separator);
                sb2.append(file2.getName());
                this.files[0] = new File(sb2.toString());
            }
            loadResults();
        }
    }

    public void displayGraph() {
        for (int i = 0; i < GlobalVariables.gAllSpectra.size(); i++) {
            dbReading sensorReading = (dbReading) GlobalVariables.gAllSpectra.get(i);
            ArrayList<DataPoint> dataPoints = new ArrayList<>();
            if (!(sensorReading == null || sensorReading.getXReading().length == 0 || sensorReading.getYReading().length == 0)) {
                double[] xVals = sensorReading.getXReading();
                double[] yVals = sensorReading.getYReading();
                for (int j = xVals.length - 1; j >= 0; j--) {
                    dataPoints.add(new DataPoint(1.0E7d / xVals[j], yVals[j] * 100.0d));
                    if (this.maxValue < yVals[j] * 100.0d) {
                        this.maxValue = yVals[j] * 100.0d;
                    }
                }
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>((DataPoint[]) dataPoints.toArray(new DataPoint[dataPoints.size()]));
                series.setThickness(4);
                series.setColor(colors[i % 12]);
                StringBuilder sb = new StringBuilder();
                sb.append("Meas. ");
                sb.append(String.valueOf(i + 1));
                series.setTitle(sb.toString());
                this.mGraphView.addSeries(series);
            }
        }
    }

    public void onBackPressed() {
        goBack();
    }

    public void back(View view) {
        goBack();
    }

    private void goBack() {
        Intent i = new Intent(this, GlobalVariables.measurmentsViewCaller);
        i.putExtra("goto", 0);
        startActivity(i);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    public static double[] switch_NM_CM(double[] data) {
        double[] xAxis = new double[data.length];
        for (int i = 0; i < xAxis.length; i++) {
            xAxis[i] = 1.0E7d / data[i];
        }
        return xAxis;
    }

    public static double[] convertRefl(double[] data) {
        double[] xAxis = new double[data.length];
        for (int i = 0; i < xAxis.length; i++) {
            xAxis[i] = 1.0E7d / data[i];
        }
        return xAxis;
    }

    public static double[] convertRefltoAbs(double[] data) {
        double[] xInverse = new double[data.length];
        for (int i = 0; i < xInverse.length; i++) {
            xInverse[i] = -Math.log10(data[i] / 100.0d);
        }
        return xInverse;
    }

    public static double[] convertAbstoRefl(double[] data) {
        double[] xInverse = new double[data.length];
        for (int i = 0; i < xInverse.length; i++) {
            xInverse[i] = Math.pow(10.0d, -data[i]) * 100.0d;
        }
        return xInverse;
    }

    public static double[] convertDataToT(double[] data) {
        double[] TArray = new double[data.length];
        for (int i = 0; i < TArray.length; i++) {
            TArray[i] = data[i] * 100.0d;
        }
        return TArray;
    }

    public static double[] convertTToData(double[] data) {
        double[] TArray = new double[data.length];
        for (int i = 0; i < TArray.length; i++) {
            TArray[i] = data[i] / 100.0d;
        }
        return TArray;
    }

    public void createOutDir(String dir) {
        File file = new File(Environment.getExternalStoragePublicDirectory(dir).toString());
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("************************* Path : ");
        sb.append(dir);
        printStream.println(sb.toString());
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public boolean isReadStoragePermissionGranted() {
        if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 3);
        return false;
    }

    public boolean isWriteStoragePermissionGranted() {
        if (VERSION.SDK_INT < 23) {
            createOutDir(GlobalVariables.OutputDirectory);
            return true;
        } else if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            createOutDir(GlobalVariables.OutputDirectory);
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 2);
            return false;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                if (grantResults[0] == 0) {
                    createOutDir(GlobalVariables.OutputDirectory);
                    return;
                }
                return;
            case 3:
                int i = grantResults[0];
                return;
            default:
                return;
        }
    }

    public void writeRunDataFile(double[] x, double[] y, int graphCount) {
        double[] xInverse = switch_NM_CM(x);
        double[] TArray = convertDataToT(y);
        double[] Absorbance = convertRefltoAbs(TArray);
        for (int i = 0; i < TArray.length; i++) {
            TArray[i] = TArray[i] / 1.0d;
        }
        if (this.filesName.isEmpty() || this.filesName == null) {
            this.filesName = GlobalVariables.SPECTRUM_DEFAULT_PATH_TEMPLATE;
        }
        if (rdbtn_nm_Spec) {
            if (rdbtn_ref_Spec) {
                String str = GlobalVariables.OutputDirectory;
                StringBuilder sb = new StringBuilder();
                sb.append(this.filesName);
                sb.append(GlobalVariables.SPECTRUM_REFL_PATH_TEMPLATE);
                sb.append(String.format("%03d", new Object[]{Integer.valueOf(graphCount)}));
                sb.append(GlobalVariables.SPECTRUM);
                if (!MethodsFactory.writeGraphFile(xInverse, TArray, str, sb.toString(), "x_Axis:Wavelength (nm)\ty_Axis:%Reflectance")) {
                    MethodsFactory.showAlertMessage(this, "Error in saving files", "Sorry, don't have enough memory to store data!");
                }
            } else {
                String str2 = GlobalVariables.OutputDirectory;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(this.filesName);
                sb2.append(MethodsFactory.formatString(GlobalVariables.SPECTRUM_ABS_PATH_TEMPLATE, Integer.valueOf(graphCount)));
                if (!MethodsFactory.writeGraphFile(xInverse, Absorbance, str2, sb2.toString(), "x_Axis:Wavelength (nm)\ty_Axis:Absorbance")) {
                    MethodsFactory.showAlertMessage(this, "Error in saving files", "Sorry, don't have enough memory to store data!");
                }
            }
        } else if (rdbtn_ref_Spec) {
            String str3 = GlobalVariables.OutputDirectory;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(this.filesName);
            sb3.append(MethodsFactory.formatString(GlobalVariables.SPECTRUM_REFL_PATH_TEMPLATE, Integer.valueOf(graphCount)));
            if (!MethodsFactory.writeGraphFile(x, TArray, str3, sb3.toString(), "x_Axis:Wavenumber (cm-1)\ty_Axis:%Reflectance")) {
                MethodsFactory.showAlertMessage(this, "Error in saving files", "Sorry, don't have enough memory to store data!");
            }
        } else {
            String str4 = GlobalVariables.OutputDirectory;
            StringBuilder sb4 = new StringBuilder();
            sb4.append(this.filesName);
            sb4.append(MethodsFactory.formatString(GlobalVariables.SPECTRUM_ABS_PATH_TEMPLATE, Integer.valueOf(graphCount)));
            if (!MethodsFactory.writeGraphFile(x, Absorbance, str4, sb4.toString(), "x_Axis:Wavenumber (cm-1)\ty_Axis:Absorbance")) {
                MethodsFactory.showAlertMessage(this, "Error in saving files", "Sorry, don't have enough memory to store data!");
            }
        }
    }

    public int compare(File f1, File f2) {
        return f1.getName().compareTo(f2.getName());
    }

    public void loadResults() {
        if (this.files == null) {
            MethodsFactory.showAlertMessage(this, "Error in loading files", "No data to be loaded!");
            return;
        }
        Arrays.sort(this.files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return f1.getName().compareTo(f2.getName());
            }
        });
        measurementCount_Spectroscopy = GlobalVariables.gAllSpectra.size();
        for (File absolutePath : this.files) {
            ArrayList<DataPoint> dataPoints = new ArrayList<>();
            double[][] arraysToPlot = loadGraphDataFromFile(absolutePath.getAbsolutePath());
            if (!rdbtn_nm_Spec) {
                arraysToPlot[0] = switch_NM_CM(arraysToPlot[0]);
            }
            for (int j = arraysToPlot[0].length - 1; j >= 0; j--) {
                dataPoints.add(new DataPoint(arraysToPlot[0][j], arraysToPlot[1][j]));
                if (this.maxValue < arraysToPlot[1][j]) {
                    this.maxValue = arraysToPlot[1][j];
                }
            }
            dbReading newReading = new dbReading();
            newReading.setReading(convertTToData(arraysToPlot[1]), switch_NM_CM(arraysToPlot[0]));
            GlobalVariables.gAllSpectra.add(newReading);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>((DataPoint[]) dataPoints.toArray(new DataPoint[dataPoints.size()]));
            series.setThickness(4);
            series.setColor(colors[measurementCount_Spectroscopy % 12]);
            StringBuilder sb = new StringBuilder();
            sb.append("Meas. ");
            sb.append(String.valueOf(measurementCount_Spectroscopy + 1));
            series.setTitle(sb.toString());
            this.mGraphView.addSeries(series);
            measurementCount_Spectroscopy++;
        }
        this.mGraphView.getLegendRenderer().setVisible(true);
        this.mGraphView.getLegendRenderer().setAlign(LegendAlign.TOP);
        if (this.maxValue == 0.0d) {
            this.mGraphView.getViewport().setMinY(90.0d);
            this.mGraphView.getViewport().setMaxY(110.0d);
            this.mGraphView.getViewport().setMinX(1100.0d);
            this.mGraphView.getViewport().setMaxX(2650.0d);
        } else {
            this.mGraphView.getViewport().setMaxY(this.maxValue);
            this.mGraphView.getViewport().setScalable(true);
            this.mGraphView.getViewport().setScrollable(true);
            this.mGraphView.getViewport().setScalableY(true);
            this.mGraphView.getViewport().setScrollableY(true);
        }
        this.mGraphView.getViewport().setYAxisBoundsManual(false);
        this.mGraphView.getViewport().setXAxisBoundsManual(false);
    }

    static double[][] loadGraphDataFromFile(String strFilePath) {
        ArrayList<Double> xList = new ArrayList<>();
        ArrayList<Double> yList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(strFilePath));
            String line = reader.readLine();
            int y = 0;
            if (line.contains(GlobalVariables.SPECTRUMFILE_Y_AXIS_A)) {
                rdbtn_ref_Spec = false;
            } else if (line.contains("y_Axis:%Transmittance") || line.contains(GlobalVariables.SPECTRUMFILE_Y_AXIS_R_T)) {
                rdbtn_ref_Spec = true;
            }
            if (line.contains(GlobalVariables.SPECTRUM_FILE_X_AXIS_CM)) {
                rdbtn_nm_Spec = false;
            } else if (line.contains(GlobalVariables.SPECTRUM_FILE_X_AXIS_NM)) {
                rdbtn_nm_Spec = true;
            }
            while (true) {
                String readLine = reader.readLine();
                String line2 = readLine;
                if (readLine != null) {
                    String[] strLineTokens = line2.split("\t");
                    if (strLineTokens.length == 2) {
                        xList.add(Double.valueOf(Double.parseDouble(strLineTokens[0])));
                        yList.add(Double.valueOf(Double.parseDouble(strLineTokens[1])));
                    }
                } else {
                    try {
                        break;
                    } catch (Exception e) {
                    }
                }
            }
            double[][] arrayToReturn = {new double[xList.size()], new double[yList.size()]};
            for (int y2 = 0; y2 < arrayToReturn[0].length; y2++) {
                arrayToReturn[0][y2] = ((Double) xList.get(y2)).doubleValue();
            }
            while (true) {
                int y3 = y;
                if (y3 >= arrayToReturn[1].length) {
                    return arrayToReturn;
                }
                arrayToReturn[1][y3] = ((Double) yList.get(y3)).doubleValue();
                y = y3 + 1;
            }
        } catch (Exception e2) {
            return null;
        } finally {
            try {
                null.close();
            } catch (Exception e3) {
            }
        }
    }

    public void saveResults() {
        if (GlobalVariables.gAllSpectra.size() == 0 || GlobalVariables.gAllSpectra == null) {
            MethodsFactory.showAlertMessage(this, "Error in Saving files", "No data to be saved!");
            return;
        }
        for (int i = 0; i < GlobalVariables.gAllSpectra.size(); i++) {
            dbReading sensorReading = (dbReading) GlobalVariables.gAllSpectra.get(i);
            if (!(sensorReading == null || sensorReading.getXReading().length == 0 || sensorReading.getYReading().length == 0)) {
                writeRunDataFile(sensorReading.getXReading(), sensorReading.getYReading(), i + 1);
            }
        }
    }
}
