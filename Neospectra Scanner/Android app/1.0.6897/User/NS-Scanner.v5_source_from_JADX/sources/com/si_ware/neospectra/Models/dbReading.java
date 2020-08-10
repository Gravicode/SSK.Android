package com.si_ware.neospectra.Models;

import java.io.Serializable;
import java.util.Arrays;

public class dbReading implements Serializable {
    private double[] xReading;
    private double[] yReading;

    public double[] getXReading() {
        return this.xReading;
    }

    public double[] getYReading() {
        return this.yReading;
    }

    public void setReading(double[] yReading2, double[] xReading2) {
        this.yReading = yReading2;
        this.xReading = xReading2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("dbReading{yReading=");
        sb.append(Arrays.toString(this.yReading));
        sb.append(", xReading=");
        sb.append(Arrays.toString(this.xReading));
        sb.append('}');
        return sb.toString();
    }
}
