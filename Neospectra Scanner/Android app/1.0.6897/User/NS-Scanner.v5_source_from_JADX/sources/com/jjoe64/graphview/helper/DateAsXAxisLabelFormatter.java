package com.jjoe64.graphview.helper;

import android.content.Context;
import com.jjoe64.graphview.DefaultLabelFormatter;
import java.text.DateFormat;
import java.util.Calendar;

public class DateAsXAxisLabelFormatter extends DefaultLabelFormatter {
    protected final Calendar mCalendar = Calendar.getInstance();
    protected final DateFormat mDateFormat;

    public DateAsXAxisLabelFormatter(Context context) {
        this.mDateFormat = android.text.format.DateFormat.getDateFormat(context);
    }

    public DateAsXAxisLabelFormatter(Context context, DateFormat dateFormat) {
        this.mDateFormat = dateFormat;
    }

    public String formatLabel(double value, boolean isValueX) {
        if (!isValueX) {
            return super.formatLabel(value, isValueX);
        }
        this.mCalendar.setTimeInMillis((long) value);
        return this.mDateFormat.format(Long.valueOf(this.mCalendar.getTimeInMillis()));
    }
}
