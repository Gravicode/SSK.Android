package com.jjoe64.graphview.series;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.support.p001v4.view.ViewCompat;
import android.view.animation.AccelerateInterpolator;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPointInterface;
import java.util.Iterator;
import org.apache.poi.p009ss.usermodel.ShapeTypes;

public class LineGraphSeries<E extends DataPointInterface> extends BaseSeries<E> {
    private static final long ANIMATION_DURATION = 333;
    private boolean mAnimated;
    private AccelerateInterpolator mAnimationInterpolator;
    private long mAnimationStart;
    private int mAnimationStartFrameNo;
    private Paint mCustomPaint;
    private boolean mDrawAsPath = false;
    private double mLastAnimatedValue = Double.NaN;
    private Paint mPaint;
    private Paint mPaintBackground;
    private Path mPath;
    private Path mPathBackground;
    private Paint mSelectionPaint;
    private Styles mStyles;

    private final class Styles {
        /* access modifiers changed from: private */
        public int backgroundColor;
        /* access modifiers changed from: private */
        public float dataPointsRadius;
        /* access modifiers changed from: private */
        public boolean drawBackground;
        /* access modifiers changed from: private */
        public boolean drawDataPoints;
        /* access modifiers changed from: private */
        public int thickness;

        private Styles() {
            this.thickness = 5;
            this.drawBackground = false;
            this.drawDataPoints = false;
            this.dataPointsRadius = 10.0f;
            this.backgroundColor = Color.argb(100, ShapeTypes.ACTION_BUTTON_MOVIE, 218, 255);
        }
    }

    public LineGraphSeries() {
        init();
    }

    public LineGraphSeries(E[] data) {
        super(data);
        init();
    }

    /* access modifiers changed from: protected */
    public void init() {
        this.mStyles = new Styles<>();
        this.mPaint = new Paint();
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaintBackground = new Paint();
        this.mSelectionPaint = new Paint();
        this.mSelectionPaint.setColor(Color.argb(80, 0, 0, 0));
        this.mSelectionPaint.setStyle(Style.FILL);
        this.mPathBackground = new Path();
        this.mPath = new Path();
        this.mAnimationInterpolator = new AccelerateInterpolator(2.0f);
    }

    public void draw(GraphView graphView, Canvas canvas, boolean isSecondScale) {
        double minY;
        double maxY;
        Paint paint;
        float maxYOnSameX;
        float lastRenderedX;
        float graphWidth;
        float maxYOnSameX2;
        float lastAnimationReferenceX;
        float graphTop;
        Canvas canvas2;
        Paint paint2;
        float minYOnSameX;
        double y;
        double x;
        double valueX;
        E value;
        double valueX2;
        double lastEndY;
        Paint paint3;
        float startXAnimated;
        float lastRenderedX2;
        float lastAnimationReferenceX2;
        double valueX3;
        float minYOnSameX2;
        char c;
        char c2;
        float maxYOnSameX3;
        float startXAnimated2;
        Canvas canvas3 = canvas;
        resetDataPoints();
        double maxX = graphView.getViewport().getMaxX(false);
        double lastEndY2 = graphView.getViewport().getMinX(false);
        if (isSecondScale) {
            maxY = graphView.getSecondScale().getMaxY(false);
            minY = graphView.getSecondScale().getMinY(false);
        } else {
            maxY = graphView.getViewport().getMaxY(false);
            minY = graphView.getViewport().getMinY(false);
        }
        Iterator values = getValues(lastEndY2, maxX);
        this.mPaint.setStrokeWidth((float) this.mStyles.thickness);
        this.mPaint.setColor(getColor());
        this.mPaintBackground.setColor(this.mStyles.backgroundColor);
        if (this.mCustomPaint != null) {
            paint = this.mCustomPaint;
        } else {
            paint = this.mPaint;
        }
        this.mPath.reset();
        if (this.mStyles.drawBackground) {
            this.mPathBackground.reset();
        }
        double diffY = maxY - minY;
        double diffX = maxX - lastEndY2;
        float graphHeight = (float) graphView.getGraphContentHeight();
        double d = maxX;
        float graphWidth2 = (float) graphView.getGraphContentWidth();
        float graphLeft = (float) graphView.getGraphContentLeft();
        double d2 = maxY;
        float graphTop2 = (float) graphView.getGraphContentTop();
        double lastEndY3 = 0.0d;
        double lastEndX = 0.0d;
        double lastUsedEndY = 0.0d;
        int i = 0;
        boolean sameXSkip = false;
        float maxYOnSameX4 = 0.0f;
        float firstX = -1.0f;
        double lastUsedEndX = 0.0d;
        float firstY = -1.0f;
        float maxYOnSameX5 = Float.NaN;
        float graphTop3 = graphLeft;
        float minYOnSameX3 = 0.0f;
        while (true) {
            maxYOnSameX = maxYOnSameX4;
            if (!values.hasNext()) {
                break;
            }
            Iterator it = values;
            E value2 = (DataPointInterface) values.next();
            double minY2 = minY;
            double y2 = ((double) graphHeight) * ((value2.getY() - minY) / diffY);
            Paint paint4 = paint;
            double valueX4 = value2.getX();
            double minX = lastEndY2;
            double x2 = ((double) graphWidth2) * ((valueX4 - lastEndY2) / diffX);
            double orgX = x2;
            double orgY = y2;
            if (i > 0) {
                boolean isOverdrawY = false;
                boolean isOverdrawEndPoint = false;
                boolean skipDraw = false;
                double valueX5 = valueX4;
                if (x2 > ((double) graphWidth2)) {
                    y2 = lastEndY3 + (((((double) graphWidth2) - lastEndX) * (y2 - lastEndY3)) / (x2 - lastEndX));
                    x2 = (double) graphWidth2;
                    isOverdrawEndPoint = true;
                }
                if (y2 < 0.0d) {
                    if (lastEndY3 < 0.0d) {
                        skipDraw = true;
                    } else {
                        x2 = lastEndX + (((0.0d - lastEndY3) * (x2 - lastEndX)) / (y2 - lastEndY3));
                    }
                    y2 = 0.0d;
                    isOverdrawEndPoint = true;
                    isOverdrawY = true;
                }
                if (y2 > ((double) graphHeight)) {
                    if (lastEndY3 > ((double) graphHeight)) {
                        skipDraw = true;
                    } else {
                        x2 = lastEndX + (((((double) graphHeight) - lastEndY3) * (x2 - lastEndX)) / (y2 - lastEndY3));
                    }
                    isOverdrawEndPoint = true;
                    isOverdrawY = true;
                    y2 = (double) graphHeight;
                }
                if (lastEndX < 0.0d) {
                    lastEndY3 = y2 - (((0.0d - x2) * (y2 - lastEndY3)) / (lastEndX - x2));
                    lastEndX = 0.0d;
                }
                double lastEndX2 = lastEndX;
                float orgStartX = ((float) lastEndX2) + graphLeft + 1.0f;
                if (lastEndY3 < 0.0d) {
                    if (!skipDraw) {
                        lastEndX2 = x2 - (((0.0d - y2) * (x2 - lastEndX2)) / (lastEndY3 - y2));
                    }
                    lastEndY3 = 0.0d;
                    isOverdrawY = true;
                }
                float graphTop4 = graphTop2;
                float lastAnimationReferenceX3 = graphTop3;
                if (lastEndY3 > ((double) graphHeight)) {
                    if (!skipDraw) {
                        lastEndX2 = x2 - (((((double) graphHeight) - y2) * (x2 - lastEndX2)) / (lastEndY3 - y2));
                    }
                    isOverdrawY = true;
                    lastEndY3 = (double) graphHeight;
                }
                float startX = ((float) lastEndX2) + graphLeft + 1.0f;
                double lastEndX3 = lastEndX2;
                graphTop = graphTop4;
                float startY = ((float) (((double) graphTop) - lastEndY3)) + graphHeight;
                float endX = ((float) x2) + graphLeft + 1.0f;
                double d3 = x2;
                float endY = ((float) (((double) graphTop) - y2)) + graphHeight;
                float startXAnimated3 = startX;
                float endXAnimated = endX;
                if (endX < startX) {
                    skipDraw = true;
                }
                if (skipDraw || Float.isNaN(startY) || Float.isNaN(endY)) {
                    graphWidth = graphWidth2;
                    float startXAnimated4 = startXAnimated3;
                    float f = startX;
                    double d4 = y2;
                    value = value2;
                    lastEndY = lastEndY3;
                    lastRenderedX2 = maxYOnSameX5;
                    maxYOnSameX2 = maxYOnSameX;
                    paint3 = paint4;
                    valueX2 = valueX5;
                    lastAnimationReferenceX = lastAnimationReferenceX3;
                    canvas2 = canvas;
                    minYOnSameX3 = minYOnSameX3;
                    lastAnimationReferenceX2 = endXAnimated;
                    startXAnimated = startXAnimated4;
                } else {
                    graphWidth = graphWidth2;
                    if (this.mAnimated) {
                        double d5 = y2;
                        if (Double.isNaN(this.mLastAnimatedValue) || this.mLastAnimatedValue < valueX5) {
                            long currentTime = System.currentTimeMillis();
                            lastEndY = lastEndY3;
                            if (this.mAnimationStart == 0) {
                                this.mAnimationStart = currentTime;
                                this.mAnimationStartFrameNo = 0;
                            } else if (this.mAnimationStartFrameNo < 15) {
                                this.mAnimationStart = currentTime;
                                this.mAnimationStartFrameNo++;
                            }
                            float timeFactor = ((float) (currentTime - this.mAnimationStart)) / 333.0f;
                            float factor = this.mAnimationInterpolator.getInterpolation(timeFactor);
                            long j = currentTime;
                            if (((double) timeFactor) <= 1.0d) {
                                lastAnimationReferenceX = lastAnimationReferenceX3;
                                float startXAnimated5 = Math.max(((startX - lastAnimationReferenceX3) * factor) + lastAnimationReferenceX3, lastAnimationReferenceX);
                                float endXAnimated2 = ((endX - lastAnimationReferenceX) * factor) + lastAnimationReferenceX;
                                ViewCompat.postInvalidateOnAnimation(graphView);
                                startXAnimated2 = startXAnimated5;
                                float f2 = startX;
                                endXAnimated = endXAnimated2;
                                valueX3 = valueX5;
                            } else {
                                lastAnimationReferenceX = lastAnimationReferenceX3;
                                startXAnimated2 = startXAnimated3;
                                float f3 = startX;
                                valueX3 = valueX5;
                                this.mLastAnimatedValue = valueX3;
                            }
                            lastAnimationReferenceX2 = endXAnimated;
                            startXAnimated = startXAnimated2;
                        } else {
                            lastAnimationReferenceX = endX;
                            startXAnimated = startXAnimated3;
                            float f4 = startX;
                            lastEndY = lastEndY3;
                            lastAnimationReferenceX2 = endXAnimated;
                            valueX3 = valueX5;
                        }
                    } else {
                        float startXAnimated6 = startXAnimated3;
                        float f5 = startX;
                        double d6 = y2;
                        lastEndY = lastEndY3;
                        valueX3 = valueX5;
                        lastAnimationReferenceX = lastAnimationReferenceX3;
                        lastAnimationReferenceX2 = endXAnimated;
                        startXAnimated = startXAnimated6;
                    }
                    if (!isOverdrawEndPoint) {
                        if (this.mStyles.drawDataPoints) {
                            paint3 = paint4;
                            Style prevStyle = paint3.getStyle();
                            double valueX6 = valueX3;
                            paint3.setStyle(Style.FILL);
                            valueX2 = valueX6;
                            canvas2 = canvas;
                            canvas2.drawCircle(lastAnimationReferenceX2, endY, this.mStyles.dataPointsRadius, paint3);
                            paint3.setStyle(prevStyle);
                        } else {
                            valueX2 = valueX3;
                            paint3 = paint4;
                            canvas2 = canvas;
                        }
                        registerDataPoint(endX, endY, value2);
                    } else {
                        valueX2 = valueX3;
                        paint3 = paint4;
                        canvas2 = canvas;
                    }
                    if (this.mDrawAsPath) {
                        this.mPath.moveTo(startXAnimated, startY);
                    }
                    lastRenderedX2 = maxYOnSameX5;
                    if (Float.isNaN(lastRenderedX2)) {
                        minYOnSameX2 = minYOnSameX3;
                        maxYOnSameX2 = maxYOnSameX;
                    } else if (Math.abs(endX - lastRenderedX2) > 0.3f) {
                        minYOnSameX2 = minYOnSameX3;
                        maxYOnSameX2 = maxYOnSameX;
                    } else {
                        if (sameXSkip) {
                            minYOnSameX3 = Math.min(minYOnSameX3, endY);
                            maxYOnSameX3 = Math.max(maxYOnSameX, endY);
                        } else {
                            float f6 = minYOnSameX3;
                            float f7 = maxYOnSameX;
                            sameXSkip = true;
                            minYOnSameX3 = Math.min(startY, endY);
                            maxYOnSameX3 = Math.max(startY, endY);
                        }
                        value = value2;
                        maxYOnSameX2 = maxYOnSameX3;
                    }
                    float lastAnimationReferenceX4 = lastAnimationReferenceX;
                    if (this.mDrawAsPath) {
                        this.mPath.lineTo(lastAnimationReferenceX2, endY);
                        value = value2;
                    } else {
                        if (sameXSkip) {
                            sameXSkip = false;
                            value = value2;
                            c2 = 0;
                            c = 3;
                            renderLine(canvas2, new float[]{lastRenderedX2, minYOnSameX2, lastRenderedX2, maxYOnSameX2}, paint3);
                        } else {
                            value = value2;
                            c2 = 0;
                            c = 3;
                        }
                        float[] fArr = new float[4];
                        fArr[c2] = startXAnimated;
                        fArr[1] = startY;
                        fArr[2] = lastAnimationReferenceX2;
                        fArr[c] = endY;
                        renderLine(canvas2, fArr, paint3);
                    }
                    lastRenderedX2 = endX;
                    minYOnSameX3 = minYOnSameX2;
                    lastAnimationReferenceX = lastAnimationReferenceX4;
                }
                if (this.mStyles.drawBackground) {
                    if (isOverdrawY) {
                        if (firstX == -1.0f) {
                            float firstX2 = orgStartX;
                            float firstY2 = startY;
                            float f8 = endX;
                            this.mPathBackground.moveTo(orgStartX, startY);
                            firstX = firstX2;
                            firstY = firstY2;
                        }
                        this.mPathBackground.lineTo(startXAnimated, startY);
                    }
                    if (firstX == -1.0f) {
                        float firstX3 = startXAnimated;
                        float firstY3 = startY;
                        this.mPathBackground.moveTo(startXAnimated, startY);
                        firstX = firstX3;
                        firstY = firstY3;
                    }
                    this.mPathBackground.lineTo(startXAnimated, startY);
                    this.mPathBackground.lineTo(lastAnimationReferenceX2, endY);
                }
                float f9 = startY;
                lastUsedEndX = (double) lastAnimationReferenceX2;
                lastRenderedX = lastRenderedX2;
                paint2 = paint3;
                lastUsedEndY = (double) endY;
                double d7 = lastEndX3;
                double d8 = lastEndY;
                double d9 = valueX2;
                DataPointInterface dataPointInterface = value;
            } else {
                double valueX7 = valueX4;
                graphWidth = graphWidth2;
                float lastAnimationReferenceX5 = graphTop3;
                E value3 = value2;
                float lastRenderedX3 = maxYOnSameX5;
                float minYOnSameX4 = minYOnSameX3;
                maxYOnSameX2 = maxYOnSameX;
                paint2 = paint4;
                graphTop = graphTop2;
                canvas2 = canvas;
                lastRenderedX = lastRenderedX3;
                if (this.mStyles.drawDataPoints) {
                    float first_X = ((float) x2) + graphLeft + 1.0f;
                    x = x2;
                    float first_Y = ((float) (((double) graphTop) - y2)) + graphHeight;
                    if (first_X < graphLeft || first_Y > graphTop + graphHeight) {
                        y = y2;
                        minYOnSameX = minYOnSameX4;
                        double d10 = lastEndY3;
                        double d11 = lastEndX;
                        double d12 = valueX7;
                        DataPointInterface dataPointInterface2 = value3;
                    } else {
                        if (this.mAnimated) {
                            y = y2;
                            if (!Double.isNaN(this.mLastAnimatedValue)) {
                                minYOnSameX = minYOnSameX4;
                                double d13 = lastEndY3;
                                valueX = valueX7;
                                if (this.mLastAnimatedValue >= valueX) {
                                    double d14 = lastEndX;
                                }
                            } else {
                                minYOnSameX = minYOnSameX4;
                                double d15 = lastEndY3;
                                valueX = valueX7;
                            }
                            long currentTime2 = System.currentTimeMillis();
                            double d16 = lastEndX;
                            if (this.mAnimationStart == 0) {
                                this.mAnimationStart = currentTime2;
                            }
                            float timeFactor2 = ((float) (currentTime2 - this.mAnimationStart)) / 333.0f;
                            float factor2 = this.mAnimationInterpolator.getInterpolation(timeFactor2);
                            long j2 = currentTime2;
                            if (((double) timeFactor2) <= 1.0d) {
                                first_X = ((first_X - lastAnimationReferenceX5) * factor2) + lastAnimationReferenceX5;
                                ViewCompat.postInvalidateOnAnimation(graphView);
                            } else {
                                this.mLastAnimatedValue = valueX;
                            }
                        } else {
                            y = y2;
                            minYOnSameX = minYOnSameX4;
                            double d17 = lastEndY3;
                            double d18 = lastEndX;
                            double d19 = valueX7;
                        }
                        Style prevStyle2 = paint2.getStyle();
                        paint2.setStyle(Style.FILL);
                        canvas2.drawCircle(first_X, first_Y, this.mStyles.dataPointsRadius, paint2);
                        paint2.setStyle(prevStyle2);
                        registerDataPoint(first_X, first_Y, value3);
                    }
                } else {
                    x = x2;
                    y = y2;
                    minYOnSameX = minYOnSameX4;
                    double d20 = lastEndY3;
                    double d21 = lastEndX;
                    double d22 = valueX7;
                    DataPointInterface dataPointInterface3 = value3;
                }
                lastAnimationReferenceX = lastAnimationReferenceX5;
                double d23 = x;
                double d24 = y;
                minYOnSameX3 = minYOnSameX;
            }
            lastEndX = orgX;
            i++;
            paint = paint2;
            lastEndY3 = orgY;
            canvas3 = canvas2;
            graphTop2 = graphTop;
            graphTop3 = lastAnimationReferenceX;
            maxYOnSameX4 = maxYOnSameX2;
            values = it;
            minY = minY2;
            lastEndY2 = minX;
            graphWidth2 = graphWidth;
            maxYOnSameX5 = lastRenderedX;
        }
        double d25 = lastEndY2;
        float graphWidth3 = graphTop3;
        double d26 = minY;
        Iterator it2 = values;
        double d27 = lastEndY3;
        double d28 = lastEndX;
        float f10 = maxYOnSameX5;
        float f11 = minYOnSameX3;
        float lastRenderedX4 = maxYOnSameX;
        float graphTop5 = graphTop2;
        Canvas canvas4 = canvas3;
        Paint paint5 = paint;
        if (this.mDrawAsPath) {
            canvas4.drawPath(this.mPath, paint5);
        }
        if (!this.mStyles.drawBackground || firstX == -1.0f) {
            float f12 = firstY;
            float f13 = firstX;
            return;
        }
        if (lastUsedEndY != ((double) (graphHeight + graphTop5))) {
            this.mPathBackground.lineTo((float) lastUsedEndX, graphHeight + graphTop5);
        }
        float firstX4 = firstX;
        this.mPathBackground.lineTo(firstX4, graphHeight + graphTop5);
        float firstY4 = firstY;
        if (firstY4 != graphHeight + graphTop5) {
            this.mPathBackground.lineTo(firstX4, firstY4);
        }
        canvas4.drawPath(this.mPathBackground, this.mPaintBackground);
    }

    private void renderLine(Canvas canvas, float[] pts, Paint paint) {
        if (pts.length != 4 || pts[0] != pts[2] || pts[1] != pts[3]) {
            canvas.drawLines(pts, paint);
        }
    }

    public int getThickness() {
        return this.mStyles.thickness;
    }

    public void setThickness(int thickness) {
        this.mStyles.thickness = thickness;
    }

    public boolean isDrawBackground() {
        return this.mStyles.drawBackground;
    }

    public void setDrawBackground(boolean drawBackground) {
        this.mStyles.drawBackground = drawBackground;
    }

    public boolean isDrawDataPoints() {
        return this.mStyles.drawDataPoints;
    }

    public void setDrawDataPoints(boolean drawDataPoints) {
        this.mStyles.drawDataPoints = drawDataPoints;
    }

    public float getDataPointsRadius() {
        return this.mStyles.dataPointsRadius;
    }

    public void setDataPointsRadius(float dataPointsRadius) {
        this.mStyles.dataPointsRadius = dataPointsRadius;
    }

    public int getBackgroundColor() {
        return this.mStyles.backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.mStyles.backgroundColor = backgroundColor;
    }

    public void setCustomPaint(Paint customPaint) {
        this.mCustomPaint = customPaint;
    }

    public void setAnimated(boolean animated) {
        this.mAnimated = animated;
    }

    public boolean isDrawAsPath() {
        return this.mDrawAsPath;
    }

    public void setDrawAsPath(boolean mDrawAsPath2) {
        this.mDrawAsPath = mDrawAsPath2;
    }

    public void appendData(E dataPoint, boolean scrollToEnd, int maxDataPoints, boolean silent) {
        if (!isAnimationActive()) {
            this.mAnimationStart = 0;
        }
        super.appendData(dataPoint, scrollToEnd, maxDataPoints, silent);
    }

    private boolean isAnimationActive() {
        boolean z = false;
        if (!this.mAnimated) {
            return false;
        }
        if (System.currentTimeMillis() - this.mAnimationStart <= ANIMATION_DURATION) {
            z = true;
        }
        return z;
    }

    public void drawSelection(GraphView graphView, Canvas canvas, boolean b, DataPointInterface value) {
        Canvas canvas2 = canvas;
        double spanX = graphView.getViewport().getMaxX(false) - graphView.getViewport().getMinX(false);
        double spanYPixel = (double) graphView.getGraphContentHeight();
        double pointX = (((value.getX() - graphView.getViewport().getMinX(false)) * ((double) graphView.getGraphContentWidth())) / spanX) + ((double) graphView.getGraphContentLeft());
        double pointY = (((double) graphView.getGraphContentTop()) + spanYPixel) - (((value.getY() - graphView.getViewport().getMinY(false)) * spanYPixel) / (graphView.getViewport().getMaxY(false) - graphView.getViewport().getMinY(false)));
        double d = spanX;
        canvas2.drawCircle((float) pointX, (float) pointY, 30.0f, this.mSelectionPaint);
        Style prevStyle = this.mPaint.getStyle();
        this.mPaint.setStyle(Style.FILL);
        double d2 = pointY;
        canvas2.drawCircle((float) pointX, (float) pointY, 23.0f, this.mPaint);
        this.mPaint.setStyle(prevStyle);
    }
}
