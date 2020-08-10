package com.si_ware.neospectra.ChartView;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import com.si_ware.neospectra.ChartView.AbstractSeries.AbstractPoint;

public class LinearSeries extends AbstractSeries {
    private PointF mLastPoint;

    public static class LinearPoint extends AbstractPoint {
        public LinearPoint() {
        }

        public LinearPoint(double x, double y) {
            super(x, y);
        }
    }

    public void drawPoint(Canvas canvas, AbstractPoint point, float scaleX, float scaleY, Rect gridBounds) {
        float x = (float) (((double) gridBounds.left) + (((double) scaleX) * (point.getX() - getMinX())));
        float y = (float) (((double) gridBounds.bottom) - (((double) scaleY) * (point.getY() - getMinY())));
        if (this.mLastPoint != null) {
            canvas.drawLine(this.mLastPoint.x, this.mLastPoint.y, x, y, this.mPaint);
        } else {
            this.mLastPoint = new PointF();
        }
        this.mLastPoint.set(x, y);
    }

    /* access modifiers changed from: protected */
    public void onDrawingComplete() {
        this.mLastPoint = null;
    }
}
