package com.jjoe64.graphview.series;

import java.io.Serializable;
import java.util.Date;

public class DataPoint implements DataPointInterface, Serializable {
    private static final long serialVersionUID = 1428263322645L;

    /* renamed from: x */
    private double f63x;

    /* renamed from: y */
    private double f64y;

    public DataPoint(double x, double y) {
        this.f63x = x;
        this.f64y = y;
    }

    public DataPoint(Date x, double y) {
        this.f63x = (double) x.getTime();
        this.f64y = y;
    }

    public double getX() {
        return this.f63x;
    }

    public double getY() {
        return this.f64y;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(this.f63x);
        sb.append("/");
        sb.append(this.f64y);
        sb.append("]");
        return sb.toString();
    }
}
