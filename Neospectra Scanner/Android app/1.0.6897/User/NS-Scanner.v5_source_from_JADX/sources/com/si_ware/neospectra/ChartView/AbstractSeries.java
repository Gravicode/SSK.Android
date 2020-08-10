package com.si_ware.neospectra.ChartView;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractSeries {
    private double mMaxX = Double.MIN_VALUE;
    private double mMaxY = Double.MIN_VALUE;
    private double mMinX = Double.MAX_VALUE;
    private double mMinY = Double.MAX_VALUE;
    protected Paint mPaint = new Paint();
    private List<AbstractPoint> mPoints;
    private boolean mPointsSorted = false;
    private double mRangeX = 0.0d;
    private double mRangeY = 0.0d;
    protected float mScaleX = 1.0f;
    protected float mScaleY = 1.0f;

    public static abstract class AbstractPoint implements Comparable<AbstractPoint> {

        /* renamed from: mX */
        private double f829mX;

        /* renamed from: mY */
        private double f830mY;

        public AbstractPoint() {
        }

        public AbstractPoint(double x, double y) {
            this.f829mX = x;
            this.f830mY = y;
        }

        public double getX() {
            return this.f829mX;
        }

        public double getY() {
            return this.f830mY;
        }

        public void set(double x, double y) {
            this.f829mX = x;
            this.f830mY = y;
        }

        public int compareTo(AbstractPoint another) {
            return Double.compare(this.f829mX, another.f829mX);
        }
    }

    /* access modifiers changed from: protected */
    public abstract void drawPoint(Canvas canvas, AbstractPoint abstractPoint, float f, float f2, Rect rect);

    public AbstractSeries() {
        this.mPaint.setAntiAlias(true);
    }

    public List<AbstractPoint> getPoints() {
        Collections.sort(this.mPoints);
        return this.mPoints;
    }

    public void setPoints(List<? extends AbstractPoint> points) {
        this.mPoints = new ArrayList();
        this.mPoints.addAll(points);
        sortPoints();
        resetRange();
        for (AbstractPoint point : this.mPoints) {
            extendRange(point.getX(), point.getY());
        }
    }

    public void addPoint(AbstractPoint point) {
        if (this.mPoints == null) {
            this.mPoints = new ArrayList();
        }
        extendRange(point.getX(), point.getY());
        this.mPoints.add(point);
        this.mPointsSorted = false;
    }

    public void setLineColor(int color) {
        this.mPaint.setColor(color);
    }

    public void setLineWidth(float width) {
        this.mPaint.setStrokeWidth(width);
    }

    private void sortPoints() {
        if (!this.mPointsSorted) {
            Collections.sort(this.mPoints);
            this.mPointsSorted = true;
        }
    }

    private void resetRange() {
        this.mMinX = Double.MAX_VALUE;
        this.mMaxX = Double.MIN_VALUE;
        this.mMinY = Double.MAX_VALUE;
        this.mMaxY = Double.MIN_VALUE;
        this.mRangeX = 0.0d;
        this.mRangeY = 0.0d;
    }

    private void extendRange(double x, double y) {
        if (x < this.mMinX) {
            this.mMinX = x;
        }
        if (x > this.mMaxX) {
            this.mMaxX = x;
        }
        if (y < this.mMinY) {
            this.mMinY = y;
        }
        if (y > this.mMaxY) {
            this.mMaxY = y;
        }
        this.mRangeX = this.mMaxX - this.mMinX;
        this.mRangeY = this.mMaxY - this.mMinY;
    }

    /* access modifiers changed from: 0000 */
    public double getMinX() {
        return this.mMinX;
    }

    /* access modifiers changed from: 0000 */
    public double getMaxX() {
        return this.mMaxX;
    }

    /* access modifiers changed from: 0000 */
    public double getMinY() {
        return this.mMinY;
    }

    /* access modifiers changed from: 0000 */
    public double getMaxY() {
        return this.mMaxY;
    }

    /* access modifiers changed from: 0000 */
    public double getRangeX() {
        return this.mRangeX;
    }

    /* access modifiers changed from: 0000 */
    public double getRangeY() {
        return this.mRangeY;
    }

    /* access modifiers changed from: 0000 */
    public void draw(Canvas canvas, Rect gridBounds, RectD valueBounds) {
        sortPoints();
        float scaleX = ((float) gridBounds.width()) / ((float) valueBounds.width());
        float scaleY = ((float) gridBounds.height()) / ((float) valueBounds.height());
        for (AbstractPoint point : this.mPoints) {
            drawPoint(canvas, point, scaleX, scaleY, gridBounds);
        }
        onDrawingComplete();
    }

    /* access modifiers changed from: protected */
    public void onDrawingComplete() {
    }
}
