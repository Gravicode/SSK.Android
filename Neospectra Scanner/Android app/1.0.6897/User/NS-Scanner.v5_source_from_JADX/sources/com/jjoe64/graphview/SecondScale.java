package com.jjoe64.graphview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import com.jjoe64.graphview.series.Series;
import java.util.ArrayList;
import java.util.List;

public class SecondScale {
    protected RectD mCompleteRange = new RectD();
    protected RectD mCurrentViewport = new RectD();
    protected final GraphView mGraph;
    protected LabelFormatter mLabelFormatter;
    private Paint mPaintAxisTitle;
    protected double mReferenceY = Double.NaN;
    protected List<Series> mSeries;
    private String mVerticalAxisTitle;
    public int mVerticalAxisTitleColor;
    public float mVerticalAxisTitleTextSize;
    private boolean mYAxisBoundsManual = true;

    SecondScale(GraphView graph) {
        this.mGraph = graph;
        this.mSeries = new ArrayList();
        this.mLabelFormatter = new DefaultLabelFormatter();
        this.mLabelFormatter.setViewport(this.mGraph.getViewport());
    }

    public void addSeries(Series s) {
        s.onGraphViewAttached(this.mGraph);
        this.mSeries.add(s);
        this.mGraph.onDataChanged(false, false);
    }

    public void setMinY(double d) {
        this.mReferenceY = d;
        this.mCurrentViewport.bottom = d;
    }

    public void setMaxY(double d) {
        this.mCurrentViewport.top = d;
    }

    public List<Series> getSeries() {
        return this.mSeries;
    }

    public double getMinY(boolean completeRange) {
        return (completeRange ? this.mCompleteRange : this.mCurrentViewport).bottom;
    }

    public double getMaxY(boolean completeRange) {
        return (completeRange ? this.mCompleteRange : this.mCurrentViewport).top;
    }

    public boolean isYAxisBoundsManual() {
        return this.mYAxisBoundsManual;
    }

    public LabelFormatter getLabelFormatter() {
        return this.mLabelFormatter;
    }

    public void setLabelFormatter(LabelFormatter formatter) {
        this.mLabelFormatter = formatter;
        this.mLabelFormatter.setViewport(this.mGraph.getViewport());
    }

    public void removeAllSeries() {
        this.mSeries.clear();
        this.mGraph.onDataChanged(false, false);
    }

    public void removeSeries(Series series) {
        this.mSeries.remove(series);
        this.mGraph.onDataChanged(false, false);
    }

    public void calcCompleteRange() {
        List<Series> series = getSeries();
        this.mCompleteRange.set(0.0d, 0.0d, 0.0d, 0.0d);
        if (!series.isEmpty() && !((Series) series.get(0)).isEmpty()) {
            double d = ((Series) series.get(0)).getLowestValueX();
            for (Series s : series) {
                if (!s.isEmpty() && d > s.getLowestValueX()) {
                    d = s.getLowestValueX();
                }
            }
            this.mCompleteRange.left = d;
            double d2 = ((Series) series.get(0)).getHighestValueX();
            for (Series s2 : series) {
                if (!s2.isEmpty() && d2 < s2.getHighestValueX()) {
                    d2 = s2.getHighestValueX();
                }
            }
            this.mCompleteRange.right = d2;
            if (!series.isEmpty() && !((Series) series.get(0)).isEmpty()) {
                double d3 = ((Series) series.get(0)).getLowestValueY();
                for (Series s3 : series) {
                    if (!s3.isEmpty() && d3 > s3.getLowestValueY()) {
                        d3 = s3.getLowestValueY();
                    }
                }
                this.mCompleteRange.bottom = d3;
                double d4 = ((Series) series.get(0)).getHighestValueY();
                for (Series s4 : series) {
                    if (!s4.isEmpty() && d4 < s4.getHighestValueY()) {
                        d4 = s4.getHighestValueY();
                    }
                }
                this.mCompleteRange.top = d4;
            }
        }
    }

    public String getVerticalAxisTitle() {
        return this.mVerticalAxisTitle;
    }

    public void setVerticalAxisTitle(String mVerticalAxisTitle2) {
        if (this.mPaintAxisTitle == null) {
            this.mPaintAxisTitle = new Paint();
            this.mPaintAxisTitle.setTextSize(getVerticalAxisTitleTextSize());
            this.mPaintAxisTitle.setTextAlign(Align.CENTER);
        }
        this.mVerticalAxisTitle = mVerticalAxisTitle2;
    }

    public float getVerticalAxisTitleTextSize() {
        if (getVerticalAxisTitle() == null || getVerticalAxisTitle().length() == 0) {
            return 0.0f;
        }
        return this.mVerticalAxisTitleTextSize;
    }

    public void setVerticalAxisTitleTextSize(float verticalAxisTitleTextSize) {
        this.mVerticalAxisTitleTextSize = verticalAxisTitleTextSize;
    }

    public int getVerticalAxisTitleColor() {
        return this.mVerticalAxisTitleColor;
    }

    public void setVerticalAxisTitleColor(int verticalAxisTitleColor) {
        this.mVerticalAxisTitleColor = verticalAxisTitleColor;
    }

    /* access modifiers changed from: protected */
    public void drawVerticalAxisTitle(Canvas canvas) {
        if (this.mVerticalAxisTitle != null && this.mVerticalAxisTitle.length() > 0) {
            this.mPaintAxisTitle.setColor(getVerticalAxisTitleColor());
            this.mPaintAxisTitle.setTextSize(getVerticalAxisTitleTextSize());
            float x = ((float) canvas.getWidth()) - (getVerticalAxisTitleTextSize() / 2.0f);
            float y = (float) (canvas.getHeight() / 2);
            canvas.save();
            canvas.rotate(-90.0f, x, y);
            canvas.drawText(this.mVerticalAxisTitle, x, y, this.mPaintAxisTitle);
            canvas.restore();
        }
    }
}
