package com.jjoe64.graphview.helper;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;

public class StaticLabelsFormatter implements LabelFormatter {
    protected LabelFormatter mDynamicLabelFormatter;
    protected final GraphView mGraphView;
    protected String[] mHorizontalLabels;
    protected String[] mVerticalLabels;
    protected Viewport mViewport;

    public StaticLabelsFormatter(GraphView graphView) {
        this.mGraphView = graphView;
        init(null, null, null);
    }

    public StaticLabelsFormatter(GraphView graphView, LabelFormatter dynamicLabelFormatter) {
        this.mGraphView = graphView;
        init(null, null, dynamicLabelFormatter);
    }

    public StaticLabelsFormatter(GraphView graphView, String[] horizontalLabels, String[] verticalLabels) {
        this.mGraphView = graphView;
        init(horizontalLabels, verticalLabels, null);
    }

    public StaticLabelsFormatter(GraphView graphView, String[] horizontalLabels, String[] verticalLabels, LabelFormatter dynamicLabelFormatter) {
        this.mGraphView = graphView;
        init(horizontalLabels, verticalLabels, dynamicLabelFormatter);
    }

    /* access modifiers changed from: protected */
    public void init(String[] horizontalLabels, String[] verticalLabels, LabelFormatter dynamicLabelFormatter) {
        this.mDynamicLabelFormatter = dynamicLabelFormatter;
        if (this.mDynamicLabelFormatter == null) {
            this.mDynamicLabelFormatter = new DefaultLabelFormatter();
        }
        this.mHorizontalLabels = horizontalLabels;
        this.mVerticalLabels = verticalLabels;
    }

    public void setDynamicLabelFormatter(LabelFormatter dynamicLabelFormatter) {
        this.mDynamicLabelFormatter = dynamicLabelFormatter;
        adjust();
    }

    public void setHorizontalLabels(String[] horizontalLabels) {
        this.mHorizontalLabels = horizontalLabels;
        adjust();
    }

    public void setVerticalLabels(String[] verticalLabels) {
        this.mVerticalLabels = verticalLabels;
        adjust();
    }

    public String formatLabel(double value, boolean isValueX) {
        if (isValueX && this.mHorizontalLabels != null) {
            double minX = this.mViewport.getMinX(false);
            return this.mHorizontalLabels[(int) (((value - minX) / (this.mViewport.getMaxX(false) - minX)) * ((double) (this.mHorizontalLabels.length - 1)))];
        } else if (isValueX || this.mVerticalLabels == null) {
            return this.mDynamicLabelFormatter.formatLabel(value, isValueX);
        } else {
            double minY = this.mViewport.getMinY(false);
            return this.mVerticalLabels[(int) (((value - minY) / (this.mViewport.getMaxY(false) - minY)) * ((double) (this.mVerticalLabels.length - 1)))];
        }
    }

    public void setViewport(Viewport viewport) {
        this.mViewport = viewport;
        adjust();
    }

    /* access modifiers changed from: protected */
    public void adjust() {
        this.mDynamicLabelFormatter.setViewport(this.mViewport);
        if (this.mVerticalLabels != null) {
            if (this.mVerticalLabels.length < 2) {
                throw new IllegalStateException("You need at least 2 vertical labels if you use static label formatter.");
            }
            this.mGraphView.getGridLabelRenderer().setNumVerticalLabels(this.mVerticalLabels.length);
        }
        if (this.mHorizontalLabels == null) {
            return;
        }
        if (this.mHorizontalLabels.length < 2) {
            throw new IllegalStateException("You need at least 2 horizontal labels if you use static label formatter.");
        }
        this.mGraphView.getGridLabelRenderer().setNumHorizontalLabels(this.mHorizontalLabels.length);
    }
}
