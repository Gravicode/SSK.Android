package com.jjoe64.graphview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build.VERSION;
import android.support.p001v4.view.ViewCompat;
import android.support.p001v4.widget.EdgeEffectCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.widget.OverScroller;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.Series;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Viewport {
    private int mBackgroundColor;
    private Integer mBorderColor;
    private Paint mBorderPaint;
    protected RectD mCompleteRange;
    protected RectD mCurrentViewport;
    private boolean mDrawBorder;
    /* access modifiers changed from: private */
    public EdgeEffectCompat mEdgeEffectBottom;
    /* access modifiers changed from: private */
    public EdgeEffectCompat mEdgeEffectLeft;
    /* access modifiers changed from: private */
    public EdgeEffectCompat mEdgeEffectRight;
    /* access modifiers changed from: private */
    public EdgeEffectCompat mEdgeEffectTop;
    protected GestureDetector mGestureDetector;
    private final SimpleOnGestureListener mGestureListener;
    /* access modifiers changed from: private */
    public final GraphView mGraphView;
    /* access modifiers changed from: private */
    public boolean mIsScalable;
    /* access modifiers changed from: private */
    public boolean mIsScrollable;
    protected double mMaxXAxisSize;
    protected double mMaxYAxisSize;
    /* access modifiers changed from: private */
    public RectD mMinimalViewport;
    protected OnXAxisBoundsChangedListener mOnXAxisBoundsChangedListener;
    private Paint mPaint;
    protected ScaleGestureDetector mScaleGestureDetector;
    private final OnScaleGestureListener mScaleGestureListener;
    protected boolean mScalingActive;
    protected OverScroller mScroller;
    private boolean mXAxisBoundsManual;
    protected AxisBoundsStatus mXAxisBoundsStatus;
    private boolean mYAxisBoundsManual;
    protected AxisBoundsStatus mYAxisBoundsStatus;
    protected double referenceX = Double.NaN;
    protected double referenceY = Double.NaN;
    protected boolean scalableY;
    /* access modifiers changed from: private */
    public boolean scrollableY;

    public enum AxisBoundsStatus {
        INITIAL,
        AUTO_ADJUSTED,
        FIX
    }

    public interface OnXAxisBoundsChangedListener {

        public enum Reason {
            SCROLL,
            SCALE
        }

        void onXAxisBoundsChanged(double d, double d2, Reason reason);
    }

    /* access modifiers changed from: protected */
    public double getReferenceX() {
        if (!isXAxisBoundsManual() || this.mGraphView.getGridLabelRenderer().isHumanRoundingX()) {
            return 0.0d;
        }
        if (Double.isNaN(this.referenceX)) {
            this.referenceX = getMinX(false);
        }
        return this.referenceX;
    }

    Viewport(GraphView graphView) {
        RectD rectD = new RectD(Double.NaN, Double.NaN, Double.NaN, Double.NaN);
        this.mMinimalViewport = rectD;
        this.mScaleGestureListener = new OnScaleGestureListener() {
            public boolean onScale(ScaleGestureDetector detector) {
                float scaleSpanX;
                double viewportHeight;
                double viewportWidth = Viewport.this.mCurrentViewport.width();
                if (Viewport.this.mMaxXAxisSize != 0.0d && viewportWidth > Viewport.this.mMaxXAxisSize) {
                    viewportWidth = Viewport.this.mMaxXAxisSize;
                }
                double center = Viewport.this.mCurrentViewport.left + (viewportWidth / 2.0d);
                if (VERSION.SDK_INT < 11 || !Viewport.this.scalableY) {
                    scaleSpanX = detector.getScaleFactor();
                } else {
                    scaleSpanX = detector.getCurrentSpanX() / detector.getPreviousSpanX();
                }
                double viewportWidth2 = viewportWidth / ((double) scaleSpanX);
                Viewport.this.mCurrentViewport.left = center - (viewportWidth2 / 2.0d);
                Viewport.this.mCurrentViewport.right = Viewport.this.mCurrentViewport.left + viewportWidth2;
                double minX = Viewport.this.getMinX(true);
                if (!Double.isNaN(Viewport.this.mMinimalViewport.left)) {
                    minX = Math.min(minX, Viewport.this.mMinimalViewport.left);
                }
                if (Viewport.this.mCurrentViewport.left < minX) {
                    Viewport.this.mCurrentViewport.left = minX;
                    Viewport.this.mCurrentViewport.right = Viewport.this.mCurrentViewport.left + viewportWidth2;
                }
                double maxX = Viewport.this.getMaxX(true);
                if (!Double.isNaN(Viewport.this.mMinimalViewport.right)) {
                    maxX = Math.max(maxX, Viewport.this.mMinimalViewport.right);
                }
                if (viewportWidth2 == 0.0d) {
                    Viewport.this.mCurrentViewport.right = maxX;
                }
                double overlap = (Viewport.this.mCurrentViewport.left + viewportWidth2) - maxX;
                if (overlap > 0.0d) {
                    if (Viewport.this.mCurrentViewport.left - overlap > minX) {
                        RectD rectD = Viewport.this.mCurrentViewport;
                        double minX2 = minX;
                        rectD.left -= overlap;
                        Viewport.this.mCurrentViewport.right = Viewport.this.mCurrentViewport.left + viewportWidth2;
                        minX = minX2;
                    } else {
                        Viewport.this.mCurrentViewport.left = minX;
                        Viewport.this.mCurrentViewport.right = maxX;
                    }
                }
                if (!Viewport.this.scalableY || VERSION.SDK_INT < 11 || detector.getCurrentSpanY() == 0.0f || detector.getPreviousSpanY() == 0.0f) {
                    double d = minX;
                } else {
                    boolean hasSecondScale = Viewport.this.mGraphView.mSecondScale != null;
                    double viewportHeight2 = Viewport.this.mCurrentViewport.height() * -1.0d;
                    double d2 = minX;
                    if (Viewport.this.mMaxYAxisSize == 0.0d || viewportHeight2 <= Viewport.this.mMaxYAxisSize) {
                        viewportHeight = viewportHeight2;
                    } else {
                        viewportHeight = Viewport.this.mMaxYAxisSize;
                    }
                    double center2 = Viewport.this.mCurrentViewport.bottom + (viewportHeight / 2.0d);
                    double viewportHeight3 = viewportHeight / ((double) (detector.getCurrentSpanY() / detector.getPreviousSpanY()));
                    double d3 = viewportWidth2;
                    Viewport.this.mCurrentViewport.bottom = center2 - (viewportHeight3 / 2.0d);
                    double center3 = center2;
                    Viewport.this.mCurrentViewport.top = Viewport.this.mCurrentViewport.bottom + viewportHeight3;
                    if (!hasSecondScale) {
                        double minY = Viewport.this.getMinY(true);
                        if (!Double.isNaN(Viewport.this.mMinimalViewport.bottom)) {
                            minY = Math.min(minY, Viewport.this.mMinimalViewport.bottom);
                        }
                        if (Viewport.this.mCurrentViewport.bottom < minY) {
                            Viewport.this.mCurrentViewport.bottom = minY;
                            Viewport.this.mCurrentViewport.top = Viewport.this.mCurrentViewport.bottom + viewportHeight3;
                        }
                        double maxY = Viewport.this.getMaxY(true);
                        if (!Double.isNaN(Viewport.this.mMinimalViewport.top)) {
                            maxY = Math.max(maxY, Viewport.this.mMinimalViewport.top);
                        }
                        if (viewportHeight3 == 0.0d) {
                            Viewport.this.mCurrentViewport.top = maxY;
                        }
                        double overlap2 = (Viewport.this.mCurrentViewport.bottom + viewportHeight3) - maxY;
                        if (overlap2 <= 0.0d) {
                        } else if (Viewport.this.mCurrentViewport.bottom - overlap2 > minY) {
                            RectD rectD2 = Viewport.this.mCurrentViewport;
                            boolean z = hasSecondScale;
                            rectD2.bottom -= overlap2;
                            Viewport.this.mCurrentViewport.top = Viewport.this.mCurrentViewport.bottom + viewportHeight3;
                        } else {
                            Viewport.this.mCurrentViewport.bottom = minY;
                            Viewport.this.mCurrentViewport.top = maxY;
                        }
                        double d4 = center3;
                    } else {
                        double viewportHeight4 = Viewport.this.mGraphView.mSecondScale.mCurrentViewport.height() * -1.0d;
                        double center4 = Viewport.this.mGraphView.mSecondScale.mCurrentViewport.bottom + (viewportHeight4 / 2.0d);
                        double viewportHeight5 = viewportHeight4 / ((double) (detector.getCurrentSpanY() / detector.getPreviousSpanY()));
                        Viewport.this.mGraphView.mSecondScale.mCurrentViewport.bottom = center4 - (viewportHeight5 / 2.0d);
                        Viewport.this.mGraphView.mSecondScale.mCurrentViewport.top = Viewport.this.mGraphView.mSecondScale.mCurrentViewport.bottom + viewportHeight5;
                    }
                }
                Viewport.this.mGraphView.onDataChanged(true, false);
                ViewCompat.postInvalidateOnAnimation(Viewport.this.mGraphView);
                return true;
            }

            public boolean onScaleBegin(ScaleGestureDetector detector) {
                if (Viewport.this.mGraphView.isCursorMode() || !Viewport.this.mIsScalable) {
                    return false;
                }
                Viewport.this.mScalingActive = true;
                return true;
            }

            public void onScaleEnd(ScaleGestureDetector detector) {
                Viewport.this.mScalingActive = false;
                if (Viewport.this.mOnXAxisBoundsChangedListener != null) {
                    Viewport.this.mOnXAxisBoundsChangedListener.onXAxisBoundsChanged(Viewport.this.getMinX(false), Viewport.this.getMaxX(false), Reason.SCALE);
                }
                ViewCompat.postInvalidateOnAnimation(Viewport.this.mGraphView);
            }
        };
        this.mGestureListener = new SimpleOnGestureListener() {
            public boolean onDown(MotionEvent e) {
                if (Viewport.this.mGraphView.isCursorMode()) {
                    return true;
                }
                if (!Viewport.this.mIsScrollable || Viewport.this.mScalingActive) {
                    return false;
                }
                Viewport.this.releaseEdgeEffects();
                Viewport.this.mScroller.forceFinished(true);
                ViewCompat.postInvalidateOnAnimation(Viewport.this.mGraphView);
                return true;
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                int completeHeight;
                int completeWidth;
                double viewportOffsetY2;
                float f = distanceY;
                if (Viewport.this.mGraphView.isCursorMode()) {
                    return true;
                }
                if (!Viewport.this.mIsScrollable || Viewport.this.mScalingActive) {
                    return false;
                }
                double viewportOffsetX = (((double) distanceX) * Viewport.this.mCurrentViewport.width()) / ((double) Viewport.this.mGraphView.getGraphContentWidth());
                double viewportOffsetY = (((double) f) * Viewport.this.mCurrentViewport.height()) / ((double) Viewport.this.mGraphView.getGraphContentHeight());
                double completeRangeLeft = Viewport.this.mCompleteRange.left;
                if (!Double.isNaN(Viewport.this.mMinimalViewport.left)) {
                    completeRangeLeft = Math.min(completeRangeLeft, Viewport.this.mMinimalViewport.left);
                }
                double completeRangeRight = Viewport.this.mCompleteRange.right;
                if (!Double.isNaN(Viewport.this.mMinimalViewport.right)) {
                    completeRangeRight = Math.max(completeRangeRight, Viewport.this.mMinimalViewport.right);
                }
                double completeRangeWidth = completeRangeRight - completeRangeLeft;
                double completeRangeBottom = Viewport.this.mCompleteRange.bottom;
                if (!Double.isNaN(Viewport.this.mMinimalViewport.bottom)) {
                    completeRangeBottom = Math.min(completeRangeBottom, Viewport.this.mMinimalViewport.bottom);
                }
                double completeRangeTop = Viewport.this.mCompleteRange.top;
                double completeRangeRight2 = completeRangeRight;
                if (!Double.isNaN(Viewport.this.mMinimalViewport.top)) {
                    completeRangeTop = Math.max(completeRangeTop, Viewport.this.mMinimalViewport.top);
                }
                double completeRangeHeight = completeRangeTop - completeRangeBottom;
                double completeRangeTop2 = completeRangeTop;
                int completeWidth2 = (int) (((double) Viewport.this.mGraphView.getGraphContentWidth()) * (completeRangeWidth / Viewport.this.mCurrentViewport.width()));
                int completeHeight2 = (int) (((double) Viewport.this.mGraphView.getGraphContentHeight()) * (completeRangeHeight / Viewport.this.mCurrentViewport.height()));
                double completeRangeBottom2 = completeRangeBottom;
                int scrolledX = (int) ((((double) completeWidth2) * ((Viewport.this.mCurrentViewport.left + viewportOffsetX) - completeRangeLeft)) / completeRangeWidth);
                double d = completeRangeWidth;
                int scrolledY = (int) (((((double) completeHeight2) * ((Viewport.this.mCurrentViewport.bottom + viewportOffsetY) - completeRangeBottom2)) / completeRangeHeight) * -1.0d);
                boolean canScrollX = Viewport.this.mCurrentViewport.left > completeRangeLeft || Viewport.this.mCurrentViewport.right < completeRangeRight2;
                boolean canScrollY = Viewport.this.mCurrentViewport.bottom > completeRangeBottom2 || Viewport.this.mCurrentViewport.top < completeRangeTop2;
                boolean hasSecondScale = Viewport.this.mGraphView.mSecondScale != null;
                if (hasSecondScale) {
                    completeWidth = completeWidth2;
                    completeHeight = completeHeight2;
                    canScrollY |= Viewport.this.mGraphView.mSecondScale.mCurrentViewport.bottom > Viewport.this.mGraphView.mSecondScale.mCompleteRange.bottom || Viewport.this.mGraphView.mSecondScale.mCurrentViewport.top < Viewport.this.mGraphView.mSecondScale.mCompleteRange.top;
                    viewportOffsetY2 = (((double) distanceY) * Viewport.this.mGraphView.mSecondScale.mCurrentViewport.height()) / ((double) Viewport.this.mGraphView.getGraphContentHeight());
                } else {
                    completeWidth = completeWidth2;
                    completeHeight = completeHeight2;
                    viewportOffsetY2 = 0.0d;
                }
                boolean canScrollY2 = Viewport.this.scrollableY & canScrollY;
                if (canScrollX) {
                    if (viewportOffsetX < 0.0d) {
                        double tooMuch = (Viewport.this.mCurrentViewport.left + viewportOffsetX) - completeRangeLeft;
                        if (tooMuch < 0.0d) {
                            viewportOffsetX -= tooMuch;
                        }
                    } else {
                        double tooMuch2 = (Viewport.this.mCurrentViewport.right + viewportOffsetX) - completeRangeRight2;
                        if (tooMuch2 > 0.0d) {
                            viewportOffsetX -= tooMuch2;
                        }
                    }
                    Viewport.this.mCurrentViewport.left += viewportOffsetX;
                    Viewport.this.mCurrentViewport.right += viewportOffsetX;
                    if (Viewport.this.mOnXAxisBoundsChangedListener != null) {
                        Viewport.this.mOnXAxisBoundsChangedListener.onXAxisBoundsChanged(Viewport.this.getMinX(false), Viewport.this.getMaxX(false), Reason.SCROLL);
                    }
                }
                if (canScrollY2) {
                    if (!hasSecondScale) {
                        if (viewportOffsetY < 0.0d) {
                            double tooMuch3 = (Viewport.this.mCurrentViewport.bottom + viewportOffsetY) - completeRangeBottom2;
                            if (tooMuch3 < 0.0d) {
                                viewportOffsetY -= tooMuch3;
                            }
                        } else {
                            double tooMuch4 = (Viewport.this.mCurrentViewport.top + viewportOffsetY) - completeRangeTop2;
                            if (tooMuch4 > 0.0d) {
                                viewportOffsetY -= tooMuch4;
                            }
                        }
                    }
                    Viewport.this.mCurrentViewport.top += viewportOffsetY;
                    Viewport.this.mCurrentViewport.bottom += viewportOffsetY;
                    if (hasSecondScale) {
                        Viewport.this.mGraphView.mSecondScale.mCurrentViewport.top += viewportOffsetY2;
                        Viewport.this.mGraphView.mSecondScale.mCurrentViewport.bottom += viewportOffsetY2;
                    }
                }
                if (canScrollX && scrolledX < 0) {
                    Viewport.this.mEdgeEffectLeft.onPull(((float) scrolledX) / ((float) Viewport.this.mGraphView.getGraphContentWidth()));
                }
                if (!hasSecondScale && canScrollY2 && scrolledY < 0) {
                    Viewport.this.mEdgeEffectBottom.onPull(((float) scrolledY) / ((float) Viewport.this.mGraphView.getGraphContentHeight()));
                }
                if (canScrollX && scrolledX > completeWidth - Viewport.this.mGraphView.getGraphContentWidth()) {
                    Viewport.this.mEdgeEffectRight.onPull(((float) ((scrolledX - completeWidth) + Viewport.this.mGraphView.getGraphContentWidth())) / ((float) Viewport.this.mGraphView.getGraphContentWidth()));
                }
                if (!hasSecondScale && canScrollY2 && scrolledY > completeHeight - Viewport.this.mGraphView.getGraphContentHeight()) {
                    Viewport.this.mEdgeEffectTop.onPull(((float) ((scrolledY - completeHeight) + Viewport.this.mGraphView.getGraphContentHeight())) / ((float) Viewport.this.mGraphView.getGraphContentHeight()));
                }
                Viewport.this.mGraphView.onDataChanged(true, false);
                ViewCompat.postInvalidateOnAnimation(Viewport.this.mGraphView);
                return true;
            }

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return true;
            }
        };
        this.mCurrentViewport = new RectD();
        this.mMaxXAxisSize = 0.0d;
        this.mMaxYAxisSize = 0.0d;
        this.mCompleteRange = new RectD();
        this.mScroller = new OverScroller(graphView.getContext());
        this.mEdgeEffectTop = new EdgeEffectCompat(graphView.getContext());
        this.mEdgeEffectBottom = new EdgeEffectCompat(graphView.getContext());
        this.mEdgeEffectLeft = new EdgeEffectCompat(graphView.getContext());
        this.mEdgeEffectRight = new EdgeEffectCompat(graphView.getContext());
        this.mGestureDetector = new GestureDetector(graphView.getContext(), this.mGestureListener);
        this.mScaleGestureDetector = new ScaleGestureDetector(graphView.getContext(), this.mScaleGestureListener);
        this.mGraphView = graphView;
        this.mXAxisBoundsStatus = AxisBoundsStatus.INITIAL;
        this.mYAxisBoundsStatus = AxisBoundsStatus.INITIAL;
        this.mBackgroundColor = 0;
        this.mPaint = new Paint();
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean b = this.mScaleGestureDetector.onTouchEvent(event) | this.mGestureDetector.onTouchEvent(event);
        if (!this.mGraphView.isCursorMode()) {
            return b;
        }
        if (event.getAction() == 0) {
            this.mGraphView.getCursorMode().onDown(event);
            b |= true;
        }
        if (event.getAction() == 2) {
            this.mGraphView.getCursorMode().onMove(event);
            b |= true;
        }
        if (event.getAction() == 1) {
            return b | this.mGraphView.getCursorMode().onUp(event);
        }
        return b;
    }

    public void setXAxisBoundsStatus(AxisBoundsStatus s) {
        this.mXAxisBoundsStatus = s;
    }

    public void setYAxisBoundsStatus(AxisBoundsStatus s) {
        this.mYAxisBoundsStatus = s;
    }

    public boolean isScrollable() {
        return this.mIsScrollable;
    }

    public void setScrollable(boolean mIsScrollable2) {
        this.mIsScrollable = mIsScrollable2;
    }

    public AxisBoundsStatus getXAxisBoundsStatus() {
        return this.mXAxisBoundsStatus;
    }

    public AxisBoundsStatus getYAxisBoundsStatus() {
        return this.mYAxisBoundsStatus;
    }

    public void calcCompleteRange() {
        List<Series> series = this.mGraphView.getSeries();
        List<Series> seriesInclusiveSecondScale = new ArrayList<>(this.mGraphView.getSeries());
        if (this.mGraphView.mSecondScale != null) {
            seriesInclusiveSecondScale.addAll(this.mGraphView.mSecondScale.getSeries());
        }
        this.mCompleteRange.set(0.0d, 0.0d, 0.0d, 0.0d);
        if (!seriesInclusiveSecondScale.isEmpty() && !((Series) seriesInclusiveSecondScale.get(0)).isEmpty()) {
            double d = ((Series) seriesInclusiveSecondScale.get(0)).getLowestValueX();
            for (Series s : seriesInclusiveSecondScale) {
                if (!s.isEmpty() && d > s.getLowestValueX()) {
                    d = s.getLowestValueX();
                }
            }
            this.mCompleteRange.left = d;
            double d2 = ((Series) seriesInclusiveSecondScale.get(0)).getHighestValueX();
            for (Series s2 : seriesInclusiveSecondScale) {
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
        if (this.mYAxisBoundsStatus == AxisBoundsStatus.AUTO_ADJUSTED) {
            this.mYAxisBoundsStatus = AxisBoundsStatus.INITIAL;
        }
        if (this.mYAxisBoundsStatus == AxisBoundsStatus.INITIAL) {
            this.mCurrentViewport.top = this.mCompleteRange.top;
            this.mCurrentViewport.bottom = this.mCompleteRange.bottom;
        }
        if (this.mXAxisBoundsStatus == AxisBoundsStatus.AUTO_ADJUSTED) {
            this.mXAxisBoundsStatus = AxisBoundsStatus.INITIAL;
        }
        if (this.mXAxisBoundsStatus == AxisBoundsStatus.INITIAL) {
            this.mCurrentViewport.left = this.mCompleteRange.left;
            this.mCurrentViewport.right = this.mCompleteRange.right;
        } else if (this.mXAxisBoundsManual && !this.mYAxisBoundsManual && this.mCompleteRange.width() != 0.0d) {
            double d5 = Double.MAX_VALUE;
            for (Series s5 : series) {
                Iterator<DataPointInterface> values = s5.getValues(this.mCurrentViewport.left, this.mCurrentViewport.right);
                while (values.hasNext()) {
                    double v = ((DataPointInterface) values.next()).getY();
                    if (d5 > v) {
                        d5 = v;
                    }
                }
            }
            if (d5 != Double.MAX_VALUE) {
                this.mCurrentViewport.bottom = d5;
            }
            double d6 = Double.MIN_VALUE;
            for (Series s6 : series) {
                Iterator<DataPointInterface> values2 = s6.getValues(this.mCurrentViewport.left, this.mCurrentViewport.right);
                while (values2.hasNext()) {
                    double v2 = ((DataPointInterface) values2.next()).getY();
                    if (d6 < v2) {
                        d6 = v2;
                    }
                }
            }
            if (d6 != Double.MIN_VALUE) {
                this.mCurrentViewport.top = d6;
            }
        }
        if (this.mCurrentViewport.left == this.mCurrentViewport.right) {
            this.mCurrentViewport.right += 1.0d;
        }
        if (this.mCurrentViewport.top == this.mCurrentViewport.bottom) {
            this.mCurrentViewport.top += 1.0d;
        }
    }

    public double getMinX(boolean completeRange) {
        if (completeRange) {
            return this.mCompleteRange.left;
        }
        return this.mCurrentViewport.left;
    }

    public double getMaxX(boolean completeRange) {
        if (completeRange) {
            return this.mCompleteRange.right;
        }
        return this.mCurrentViewport.right;
    }

    public double getMinY(boolean completeRange) {
        if (completeRange) {
            return this.mCompleteRange.bottom;
        }
        return this.mCurrentViewport.bottom;
    }

    public double getMaxY(boolean completeRange) {
        if (completeRange) {
            return this.mCompleteRange.top;
        }
        return this.mCurrentViewport.top;
    }

    public void setMaxY(double y) {
        this.mCurrentViewport.top = y;
    }

    public void setMinY(double y) {
        this.mCurrentViewport.bottom = y;
    }

    public void setMaxX(double x) {
        this.mCurrentViewport.right = x;
    }

    public void setMinX(double x) {
        this.mCurrentViewport.left = x;
    }

    /* access modifiers changed from: private */
    public void releaseEdgeEffects() {
        this.mEdgeEffectLeft.onRelease();
        this.mEdgeEffectRight.onRelease();
        this.mEdgeEffectTop.onRelease();
        this.mEdgeEffectBottom.onRelease();
    }

    private void fling(int velocityX, int velocityY) {
        releaseEdgeEffects();
        int maxX = ((int) ((this.mCurrentViewport.width() / this.mCompleteRange.width()) * ((double) ((float) this.mGraphView.getGraphContentWidth())))) - this.mGraphView.getGraphContentWidth();
        int maxY = ((int) ((this.mCurrentViewport.height() / this.mCompleteRange.height()) * ((double) ((float) this.mGraphView.getGraphContentHeight())))) - this.mGraphView.getGraphContentHeight();
        int startX = ((int) ((this.mCurrentViewport.left - this.mCompleteRange.left) / this.mCompleteRange.width())) * maxX;
        int startY = ((int) ((this.mCurrentViewport.top - this.mCompleteRange.top) / this.mCompleteRange.height())) * maxY;
        this.mScroller.forceFinished(true);
        this.mScroller.fling(startX, startY, velocityX, 0, 0, maxX, 0, maxY, this.mGraphView.getGraphContentWidth() / 2, this.mGraphView.getGraphContentHeight() / 2);
        ViewCompat.postInvalidateOnAnimation(this.mGraphView);
    }

    public void computeScroll() {
    }

    private void drawEdgeEffectsUnclipped(Canvas canvas) {
        boolean needsInvalidate = false;
        if (!this.mEdgeEffectTop.isFinished()) {
            int restoreCount = canvas.save();
            canvas.translate((float) this.mGraphView.getGraphContentLeft(), (float) this.mGraphView.getGraphContentTop());
            this.mEdgeEffectTop.setSize(this.mGraphView.getGraphContentWidth(), this.mGraphView.getGraphContentHeight());
            if (this.mEdgeEffectTop.draw(canvas)) {
                needsInvalidate = true;
            }
            canvas.restoreToCount(restoreCount);
        }
        if (!this.mEdgeEffectBottom.isFinished()) {
            int restoreCount2 = canvas.save();
            canvas.translate((float) this.mGraphView.getGraphContentLeft(), (float) (this.mGraphView.getGraphContentTop() + this.mGraphView.getGraphContentHeight()));
            canvas.rotate(180.0f, (float) (this.mGraphView.getGraphContentWidth() / 2), 0.0f);
            this.mEdgeEffectBottom.setSize(this.mGraphView.getGraphContentWidth(), this.mGraphView.getGraphContentHeight());
            if (this.mEdgeEffectBottom.draw(canvas)) {
                needsInvalidate = true;
            }
            canvas.restoreToCount(restoreCount2);
        }
        if (!this.mEdgeEffectLeft.isFinished()) {
            int restoreCount3 = canvas.save();
            canvas.translate((float) this.mGraphView.getGraphContentLeft(), (float) (this.mGraphView.getGraphContentTop() + this.mGraphView.getGraphContentHeight()));
            canvas.rotate(-90.0f, 0.0f, 0.0f);
            this.mEdgeEffectLeft.setSize(this.mGraphView.getGraphContentHeight(), this.mGraphView.getGraphContentWidth());
            if (this.mEdgeEffectLeft.draw(canvas)) {
                needsInvalidate = true;
            }
            canvas.restoreToCount(restoreCount3);
        }
        if (!this.mEdgeEffectRight.isFinished()) {
            int restoreCount4 = canvas.save();
            canvas.translate((float) (this.mGraphView.getGraphContentLeft() + this.mGraphView.getGraphContentWidth()), (float) this.mGraphView.getGraphContentTop());
            canvas.rotate(90.0f, 0.0f, 0.0f);
            this.mEdgeEffectRight.setSize(this.mGraphView.getGraphContentHeight(), this.mGraphView.getGraphContentWidth());
            if (this.mEdgeEffectRight.draw(canvas)) {
                needsInvalidate = true;
            }
            canvas.restoreToCount(restoreCount4);
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this.mGraphView);
        }
    }

    public void drawFirst(Canvas c) {
        Paint p;
        if (this.mBackgroundColor != 0) {
            this.mPaint.setColor(this.mBackgroundColor);
            c.drawRect((float) this.mGraphView.getGraphContentLeft(), (float) this.mGraphView.getGraphContentTop(), (float) (this.mGraphView.getGraphContentLeft() + this.mGraphView.getGraphContentWidth()), (float) (this.mGraphView.getGraphContentTop() + this.mGraphView.getGraphContentHeight()), this.mPaint);
        }
        if (this.mDrawBorder) {
            if (this.mBorderPaint != null) {
                p = this.mBorderPaint;
            } else {
                p = this.mPaint;
                p.setColor(getBorderColor());
            }
            Paint paint = p;
            c.drawLine((float) this.mGraphView.getGraphContentLeft(), (float) this.mGraphView.getGraphContentTop(), (float) this.mGraphView.getGraphContentLeft(), (float) (this.mGraphView.getGraphContentTop() + this.mGraphView.getGraphContentHeight()), paint);
            c.drawLine((float) this.mGraphView.getGraphContentLeft(), (float) (this.mGraphView.getGraphContentTop() + this.mGraphView.getGraphContentHeight()), (float) (this.mGraphView.getGraphContentLeft() + this.mGraphView.getGraphContentWidth()), (float) (this.mGraphView.getGraphContentTop() + this.mGraphView.getGraphContentHeight()), paint);
            if (this.mGraphView.mSecondScale != null) {
                c.drawLine((float) (this.mGraphView.getGraphContentLeft() + this.mGraphView.getGraphContentWidth()), (float) this.mGraphView.getGraphContentTop(), (float) (this.mGraphView.getGraphContentLeft() + this.mGraphView.getGraphContentWidth()), (float) (this.mGraphView.getGraphContentTop() + this.mGraphView.getGraphContentHeight()), p);
            }
        }
    }

    public void draw(Canvas c) {
        drawEdgeEffectsUnclipped(c);
    }

    public int getBackgroundColor() {
        return this.mBackgroundColor;
    }

    public void setBackgroundColor(int mBackgroundColor2) {
        this.mBackgroundColor = mBackgroundColor2;
    }

    public boolean isScalable() {
        return this.mIsScalable;
    }

    public void setScalable(boolean mIsScalable2) {
        this.mIsScalable = mIsScalable2;
        if (mIsScalable2) {
            this.mIsScrollable = true;
            setXAxisBoundsManual(true);
        }
    }

    public boolean isXAxisBoundsManual() {
        return this.mXAxisBoundsManual;
    }

    public void setXAxisBoundsManual(boolean mXAxisBoundsManual2) {
        this.mXAxisBoundsManual = mXAxisBoundsManual2;
        if (mXAxisBoundsManual2) {
            this.mXAxisBoundsStatus = AxisBoundsStatus.FIX;
        }
    }

    public boolean isYAxisBoundsManual() {
        return this.mYAxisBoundsManual;
    }

    public void setYAxisBoundsManual(boolean mYAxisBoundsManual2) {
        this.mYAxisBoundsManual = mYAxisBoundsManual2;
        if (mYAxisBoundsManual2) {
            this.mYAxisBoundsStatus = AxisBoundsStatus.FIX;
        }
    }

    public void scrollToEnd() {
        if (this.mXAxisBoundsManual) {
            double size = this.mCurrentViewport.width();
            this.mCurrentViewport.right = this.mCompleteRange.right;
            this.mCurrentViewport.left = this.mCompleteRange.right - size;
            this.mGraphView.onDataChanged(true, false);
            return;
        }
        Log.w("GraphView", "scrollToEnd works only with manual x axis bounds");
    }

    public OnXAxisBoundsChangedListener getOnXAxisBoundsChangedListener() {
        return this.mOnXAxisBoundsChangedListener;
    }

    public void setOnXAxisBoundsChangedListener(OnXAxisBoundsChangedListener l) {
        this.mOnXAxisBoundsChangedListener = l;
    }

    public void setDrawBorder(boolean drawBorder) {
        this.mDrawBorder = drawBorder;
    }

    public int getBorderColor() {
        if (this.mBorderColor != null) {
            return this.mBorderColor.intValue();
        }
        return this.mGraphView.getGridLabelRenderer().getGridColor();
    }

    public void setBorderColor(Integer borderColor) {
        this.mBorderColor = borderColor;
    }

    public void setBorderPaint(Paint borderPaint) {
        this.mBorderPaint = borderPaint;
    }

    public void setScrollableY(boolean scrollableY2) {
        this.scrollableY = scrollableY2;
    }

    /* access modifiers changed from: protected */
    public double getReferenceY() {
        if (!isYAxisBoundsManual() || this.mGraphView.getGridLabelRenderer().isHumanRoundingY()) {
            return 0.0d;
        }
        if (Double.isNaN(this.referenceY)) {
            this.referenceY = getMinY(false);
        }
        return this.referenceY;
    }

    public void setScalableY(boolean scalableY2) {
        if (scalableY2) {
            this.scrollableY = true;
            setScalable(true);
            if (VERSION.SDK_INT < 11) {
                Log.w("GraphView", "Vertical scaling requires minimum Android 3.0 (API Level 11)");
            }
        }
        this.scalableY = scalableY2;
    }

    public double getMaxXAxisSize() {
        return this.mMaxXAxisSize;
    }

    public double getMaxYAxisSize() {
        return this.mMaxYAxisSize;
    }

    public void setMaxXAxisSize(double mMaxXAxisViewportSize) {
        this.mMaxXAxisSize = mMaxXAxisViewportSize;
    }

    public void setMaxYAxisSize(double mMaxYAxisViewportSize) {
        this.mMaxYAxisSize = mMaxYAxisViewportSize;
    }

    public void setMinimalViewport(double minX, double maxX, double minY, double maxY) {
        this.mMinimalViewport.set(minX, maxY, maxX, minY);
    }
}
