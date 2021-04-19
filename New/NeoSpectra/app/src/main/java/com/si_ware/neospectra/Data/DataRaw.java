package com.si_ware.neospectra.Data;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

public class DataRaw {
    private static ArrayList<DataPoint> value;

    public static ArrayList<DataPoint> getValue() {
        return value;
    }

    public static void setValue(ArrayList<DataPoint> value) {
        DataRaw.value = value;
    }
}
