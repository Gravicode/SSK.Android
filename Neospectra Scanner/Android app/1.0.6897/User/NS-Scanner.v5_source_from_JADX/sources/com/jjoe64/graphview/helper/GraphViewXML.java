package com.jjoe64.graphview.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import com.jjoe64.graphview.C1164R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

public class GraphViewXML extends GraphView {
    public GraphViewXML(Context context, AttributeSet attrs) {
        Exception e;
        BaseSeries<DataPoint> series;
        String str;
        TypedArray a;
        String dd;
        String title;
        String[] d;
        super(context, attrs);
        TypedArray a2 = context.obtainStyledAttributes(attrs, C1164R.styleable.GraphViewXML);
        String dataStr = a2.getString(C1164R.styleable.GraphViewXML_seriesData);
        int color = a2.getColor(C1164R.styleable.GraphViewXML_seriesColor, 0);
        String type = a2.getString(C1164R.styleable.GraphViewXML_seriesType);
        String seriesTitle = a2.getString(C1164R.styleable.GraphViewXML_seriesTitle);
        String title2 = a2.getString(C1164R.styleable.GraphViewXML_android_title);
        a2.recycle();
        if (dataStr == null) {
            String str2 = dataStr;
            String str3 = title2;
        } else if (dataStr.isEmpty()) {
            TypedArray typedArray = a2;
            String str4 = dataStr;
            String str5 = title2;
        } else {
            String[] d2 = dataStr.split(";");
            try {
                DataPoint[] data = new DataPoint[d2.length];
                int length = d2.length;
                int i = 0;
                int i2 = 0;
                while (i2 < length) {
                    try {
                        str = "=";
                        a = a2;
                        dd = d2[i2];
                    } catch (Exception e2) {
                        TypedArray typedArray2 = a2;
                        String str6 = dataStr;
                        String[] strArr = d2;
                        e = e2;
                        String str7 = title2;
                        Log.e("GraphViewXML", e.toString());
                        throw new IllegalArgumentException("Attribute seriesData is broken. Use this format: 0=5.0;1=5;2=4;3=9");
                    }
                    try {
                        String[] xy = dd.split(str);
                        String str8 = dd;
                        String dataStr2 = dataStr;
                        try {
                            title = title2;
                            d = d2;
                        } catch (Exception e3) {
                            String[] strArr2 = d2;
                            e = e3;
                            String str9 = title2;
                            Log.e("GraphViewXML", e.toString());
                            throw new IllegalArgumentException("Attribute seriesData is broken. Use this format: 0=5.0;1=5;2=4;3=9");
                        }
                        try {
                            data[i] = new DataPoint(Double.parseDouble(xy[0]), Double.parseDouble(xy[1]));
                            i++;
                            i2++;
                            a2 = a;
                            dataStr = dataStr2;
                            title2 = title;
                            d2 = d;
                            Context context2 = context;
                            AttributeSet attributeSet = attrs;
                        } catch (Exception e4) {
                            e = e4;
                            String str10 = title;
                            Log.e("GraphViewXML", e.toString());
                            throw new IllegalArgumentException("Attribute seriesData is broken. Use this format: 0=5.0;1=5;2=4;3=9");
                        }
                    } catch (Exception e5) {
                        String str11 = dataStr;
                        String[] strArr3 = d2;
                        e = e5;
                        String str12 = title2;
                        Log.e("GraphViewXML", e.toString());
                        throw new IllegalArgumentException("Attribute seriesData is broken. Use this format: 0=5.0;1=5;2=4;3=9");
                    }
                }
                String str13 = dataStr;
                String title3 = title2;
                String[] strArr4 = d2;
                DataPoint[] data2 = data;
                if (type == null || type.isEmpty()) {
                    type = "line";
                }
                if (type.equals("line")) {
                    series = new LineGraphSeries<>(data2);
                } else if (type.equals("bar")) {
                    series = new BarGraphSeries<>(data2);
                } else if (type.equals("points")) {
                    series = new PointsGraphSeries<>(data2);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("unknown graph type: ");
                    sb.append(type);
                    sb.append(". Possible is line|bar|points");
                    throw new IllegalArgumentException(sb.toString());
                }
                if (color != 0) {
                    series.setColor(color);
                }
                addSeries(series);
                if (seriesTitle != null && !seriesTitle.isEmpty()) {
                    series.setTitle(seriesTitle);
                    getLegendRenderer().setVisible(true);
                }
                if (title3 != null) {
                    String title4 = title3;
                    if (!title4.isEmpty()) {
                        setTitle(title4);
                        return;
                    }
                    return;
                }
                return;
            } catch (Exception e6) {
                TypedArray typedArray3 = a2;
                String str14 = dataStr;
                String str15 = title2;
                String[] strArr5 = d2;
                e = e6;
                Log.e("GraphViewXML", e.toString());
                throw new IllegalArgumentException("Attribute seriesData is broken. Use this format: 0=5.0;1=5;2=4;3=9");
            }
        }
        throw new IllegalArgumentException("Attribute seriesData is required in the format: 0=5.0;1=5;2=4;3=9");
    }
}
