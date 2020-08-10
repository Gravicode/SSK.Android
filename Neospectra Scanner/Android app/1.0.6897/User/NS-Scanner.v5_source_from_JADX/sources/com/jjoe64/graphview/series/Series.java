package com.jjoe64.graphview.series;

import android.graphics.Canvas;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPointInterface;
import java.util.Iterator;

public interface Series<E extends DataPointInterface> {
    void clearReference(GraphView graphView);

    void draw(GraphView graphView, Canvas canvas, boolean z);

    int getColor();

    double getHighestValueX();

    double getHighestValueY();

    double getLowestValueX();

    double getLowestValueY();

    String getTitle();

    Iterator<E> getValues(double d, double d2);

    boolean isEmpty();

    void onGraphViewAttached(GraphView graphView);

    void onTap(float f, float f2);

    void setOnDataPointTapListener(OnDataPointTapListener onDataPointTapListener);
}
