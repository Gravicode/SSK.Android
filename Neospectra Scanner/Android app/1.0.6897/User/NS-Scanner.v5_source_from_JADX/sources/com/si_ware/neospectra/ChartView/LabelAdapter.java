package com.si_ware.neospectra.ChartView;

import android.widget.BaseAdapter;

public abstract class LabelAdapter extends BaseAdapter {
    private double[] mValues;

    /* access modifiers changed from: 0000 */
    public void setValues(double[] points) {
        this.mValues = points;
    }

    public int getCount() {
        return this.mValues.length;
    }

    public Double getItem(int position) {
        return Double.valueOf(this.mValues[position]);
    }

    public long getItemId(int position) {
        return (long) position;
    }
}
