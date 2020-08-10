package com.jjoe64.graphview;

public interface LabelFormatter {
    String formatLabel(double d, boolean z);

    void setViewport(Viewport viewport);
}
