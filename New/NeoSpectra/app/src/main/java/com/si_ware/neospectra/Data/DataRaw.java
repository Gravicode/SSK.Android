package com.si_ware.neospectra.Data;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

public class DataRaw {
    private static double[] value;

    public static double[] getValue() {
        return value;
    }

    public static void setValue(double[] value) {
        DataRaw.value = value;
    }
}
