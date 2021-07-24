package com.si_ware.neospectra.Data;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

public class DataRaw {
    private double[] data;
    private String dataStr;

    public double[] getValue() {
        return data;
    }
    public String getValueString(){
        return dataStr;
    }

    public void setValue(double[] value) {

        data = value;
        dataStr = "";
        if (data.length > 0) {
            dataStr += "[";
            int count = 0;
            for (double item : data) {
                if (count > 0) dataStr += ",";
                dataStr += Double.toString(item);
                count++;
            }
            dataStr += "]";
        }
    }
}

