package com.si_ware.neospectra.Global;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.p001v4.content.LocalBroadcastManager;
import android.support.p004v7.app.AlertDialog.Builder;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import com.si_ware.neospectra.C1284R;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.MessageFormat;

public class MethodsFactory {
    public static void showAlertMessage(@NonNull Context mContext, String title, String message) {
        Builder myAlert = new Builder(mContext);
        myAlert.setTitle((CharSequence) title);
        myAlert.setMessage((CharSequence) message);
        myAlert.setPositiveButton((CharSequence) "OK", (OnClickListener) new OnClickListener() {
            public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        myAlert.show();
    }

    public static void logMessage(String tag, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(tag);
        sb.append(": ");
        sb.append(message);
        Log.e("***Debugging", sb.toString());
    }

    public static void sendBroadCast(Context mContext, @NonNull Intent intent) {
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        StringBuilder sb = new StringBuilder();
        sb.append("sendBroadcast ");
        sb.append(LocalBroadcastManager.getInstance(mContext));
        Log.e("SEND BROADCAST ", sb.toString());
    }

    public static void rotateProgressBar(Context mContext, ProgressBar progressBar) {
        Animation rotation = AnimationUtils.loadAnimation(mContext, C1284R.anim.clockwise_rotation);
        rotation.setRepeatCount(-1);
        progressBar.startAnimation(rotation);
    }

    public static File getFile(String fileDir, String fileName) {
        File file = null;
        try {
            file = new File(Environment.getExternalStoragePublicDirectory(fileDir), fileName);
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append("************************* Path : ");
            sb.append(fileDir);
            printStream.println(sb.toString());
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static boolean writeGraphFile(double[] x, double[] y, String filePath, String fileName, String header) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(getFile(filePath, fileName).getAbsoluteFile()));
            writer.write(header);
            writer.write("\n");
            int length = x.length < y.length ? x.length : y.length;
            for (int i = 0; i < length; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append(Double.toString(x[i]));
                sb.append("\t");
                sb.append(Double.toString(y[i]));
                writer.write(sb.toString());
                writer.write("\n");
            }
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return true;
        } catch (IOException ex2) {
            ex2.printStackTrace();
            try {
            } catch (Exception ex3) {
                ex3.printStackTrace();
            }
            return false;
        } finally {
            try {
                writer.close();
            } catch (Exception ex4) {
                ex4.printStackTrace();
            }
        }
    }

    public static double[] convertDataToT(double[] data) {
        double[] TArray = new double[data.length];
        for (int i = 0; i < TArray.length; i++) {
            TArray[i] = data[i] * 100.0d;
        }
        return TArray;
    }

    public static double[] switch_NM_CM(double[] data) {
        double[] xAxis = new double[data.length];
        for (int i = 0; i < xAxis.length; i++) {
            xAxis[i] = 1.0E7d / data[i];
        }
        return xAxis;
    }

    public static String formatString(String template, Object... params) {
        return MessageFormat.format(template, params);
    }
}
