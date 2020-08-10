package com.jjoe64.graphview.series;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.animation.AccelerateInterpolator;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.RectD;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.DataPointInterface;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BarGraphSeries<E extends DataPointInterface> extends BaseSeries<E> {
    private static final long ANIMATION_DURATION = 333;
    private boolean mAnimated;
    private AccelerateInterpolator mAnimationInterpolator;
    private long mAnimationStart;
    private int mAnimationStartFrameNo;
    private Paint mCustomPaint;
    private Map<RectD, E> mDataPoints;
    private double mDataWidth;
    private boolean mDrawValuesOnTop;
    private double mLastAnimatedValue;
    private Paint mPaint;
    private int mSpacing;
    private ValueDependentColor<E> mValueDependentColor;
    private int mValuesOnTopColor;
    private float mValuesOnTopSize;

    public BarGraphSeries() {
        this.mDataPoints = new HashMap();
        this.mLastAnimatedValue = Double.NaN;
        this.mPaint = new Paint();
    }

    public BarGraphSeries(E[] data) {
        super(data);
        this.mDataPoints = new HashMap();
        this.mLastAnimatedValue = Double.NaN;
        this.mPaint = new Paint();
        this.mAnimationInterpolator = new AccelerateInterpolator(2.0f);
    }

    /* JADX WARNING: Removed duplicated region for block: B:91:0x0280  */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x0290  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void draw(com.jjoe64.graphview.GraphView r97, android.graphics.Canvas r98, boolean r99) {
        /*
            r96 = this;
            r0 = r96
            android.graphics.Paint r1 = r0.mPaint
            android.graphics.Paint$Align r2 = android.graphics.Paint.Align.CENTER
            r1.setTextAlign(r2)
            float r1 = r0.mValuesOnTopSize
            r2 = 0
            int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            if (r1 != 0) goto L_0x001a
            com.jjoe64.graphview.GridLabelRenderer r1 = r97.getGridLabelRenderer()
            float r1 = r1.getTextSize()
            r0.mValuesOnTopSize = r1
        L_0x001a:
            android.graphics.Paint r1 = r0.mPaint
            float r2 = r0.mValuesOnTopSize
            r1.setTextSize(r2)
            r96.resetDataPoints()
            com.jjoe64.graphview.Viewport r1 = r97.getViewport()
            r2 = 0
            double r3 = r1.getMaxX(r2)
            com.jjoe64.graphview.Viewport r1 = r97.getViewport()
            double r5 = r1.getMinX(r2)
            if (r99 == 0) goto L_0x0048
            com.jjoe64.graphview.SecondScale r7 = r97.getSecondScale()
            double r7 = r7.getMaxY(r2)
            com.jjoe64.graphview.SecondScale r9 = r97.getSecondScale()
            double r9 = r9.getMinY(r2)
            goto L_0x0058
        L_0x0048:
            com.jjoe64.graphview.Viewport r7 = r97.getViewport()
            double r7 = r7.getMaxY(r2)
            com.jjoe64.graphview.Viewport r9 = r97.getViewport()
            double r9 = r9.getMinY(r2)
        L_0x0058:
            r11 = 0
            r12 = 0
            r13 = 0
            java.util.TreeSet r14 = new java.util.TreeSet
            r14.<init>()
            java.util.List r15 = r97.getSeries()
            java.util.Iterator r15 = r15.iterator()
        L_0x0068:
            boolean r16 = r15.hasNext()
            if (r16 == 0) goto L_0x00d9
            java.lang.Object r16 = r15.next()
            r2 = r16
            com.jjoe64.graphview.series.Series r2 = (com.jjoe64.graphview.series.Series) r2
            boolean r1 = r2 instanceof com.jjoe64.graphview.series.BarGraphSeries
            if (r1 == 0) goto L_0x00d3
            if (r2 != r0) goto L_0x007f
            r17 = 1
            goto L_0x0081
        L_0x007f:
            r17 = 0
        L_0x0081:
            r1 = r17
            if (r1 == 0) goto L_0x0086
            r12 = r11
        L_0x0086:
            int r11 = r11 + 1
            r18 = r11
            java.util.Iterator r11 = r2.getValues(r5, r3)
            boolean r16 = r11.hasNext()
            if (r16 == 0) goto L_0x00ce
            java.lang.Object r16 = r11.next()
            r19 = r2
            r2 = r16
            com.jjoe64.graphview.series.DataPointInterface r2 = (com.jjoe64.graphview.series.DataPointInterface) r2
            r20 = r7
            double r7 = r2.getX()
            java.lang.Double r2 = java.lang.Double.valueOf(r7)
            r14.add(r2)
            if (r1 == 0) goto L_0x00af
            int r13 = r13 + 1
        L_0x00af:
            boolean r2 = r11.hasNext()
            if (r2 == 0) goto L_0x00cb
            java.lang.Object r2 = r11.next()
            com.jjoe64.graphview.series.DataPointInterface r2 = (com.jjoe64.graphview.series.DataPointInterface) r2
            double r7 = r2.getX()
            java.lang.Double r2 = java.lang.Double.valueOf(r7)
            r14.add(r2)
            if (r1 == 0) goto L_0x00af
            int r13 = r13 + 1
            goto L_0x00af
        L_0x00cb:
            r11 = r18
            goto L_0x00d5
        L_0x00ce:
            r20 = r7
            r11 = r18
            goto L_0x00d5
        L_0x00d3:
            r20 = r7
        L_0x00d5:
            r7 = r20
            r2 = 0
            goto L_0x0068
        L_0x00d9:
            r20 = r7
            if (r13 != 0) goto L_0x00de
            return
        L_0x00de:
            r1 = 0
            double r7 = r0.mDataWidth
            r15 = 0
            int r7 = (r7 > r15 ? 1 : (r7 == r15 ? 0 : -1))
            if (r7 <= 0) goto L_0x00eb
            double r1 = r0.mDataWidth
            goto L_0x012d
        L_0x00eb:
            r7 = 0
            java.util.Iterator r8 = r14.iterator()
        L_0x00f0:
            boolean r18 = r8.hasNext()
            if (r18 == 0) goto L_0x012d
            java.lang.Object r18 = r8.next()
            r15 = r18
            java.lang.Double r15 = (java.lang.Double) r15
            if (r7 == 0) goto L_0x0122
            double r18 = r15.doubleValue()
            double r24 = r7.doubleValue()
            r27 = r7
            r26 = r8
            double r7 = r18 - r24
            double r7 = java.lang.Math.abs(r7)
            r18 = 0
            int r16 = (r1 > r18 ? 1 : (r1 == r18 ? 0 : -1))
            if (r16 == 0) goto L_0x0120
            int r16 = (r7 > r18 ? 1 : (r7 == r18 ? 0 : -1))
            if (r16 <= 0) goto L_0x0126
            int r16 = (r7 > r1 ? 1 : (r7 == r1 ? 0 : -1))
            if (r16 >= 0) goto L_0x0126
        L_0x0120:
            r1 = r7
            goto L_0x0126
        L_0x0122:
            r27 = r7
            r26 = r8
        L_0x0126:
            r7 = r15
            r8 = r26
            r15 = 0
            goto L_0x00f0
        L_0x012d:
            r7 = 0
            int r15 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r15 != 0) goto L_0x0137
            r8 = 1
            r17 = 1
            goto L_0x0144
        L_0x0137:
            r7 = 0
            double r7 = r3 - r5
            double r7 = r7 / r1
            long r7 = java.lang.Math.round(r7)
            int r7 = (int) r7
            r8 = 1
            int r7 = r7 + r8
            r17 = r7
        L_0x0144:
            r7 = r17
            java.util.Iterator r15 = r0.getValues(r5, r3)
            if (r7 != r8) goto L_0x0151
            int r8 = r97.getGraphContentWidth()
            goto L_0x0159
        L_0x0151:
            int r8 = r97.getGraphContentWidth()
            int r16 = r7 + -1
            int r8 = r8 / r16
        L_0x0159:
            r28 = r1
            int r1 = r0.mSpacing
            int r1 = r1 * r8
            int r1 = r1 / 100
            float r1 = (float) r1
            float r2 = (float) r8
            r16 = 1065017672(0x3f7ae148, float:0.98)
            float r2 = r2 * r16
            float r1 = java.lang.Math.min(r1, r2)
            double r1 = (double) r1
            r31 = r13
            r30 = r14
            double r13 = (double) r8
            double r13 = r13 - r1
            r32 = r1
            double r1 = (double) r11
            double r13 = r13 / r1
            int r1 = r8 / 2
            double r1 = (double) r1
            double r18 = r20 - r9
            double r24 = r3 - r5
            r34 = r3
            int r3 = r97.getGraphContentHeight()
            double r3 = (double) r3
            r36 = r7
            int r7 = r97.getGraphContentWidth()
            r37 = r8
            double r7 = (double) r7
            r38 = r11
            int r11 = r97.getGraphContentLeft()
            r39 = r13
            double r13 = (double) r11
            int r11 = r97.getGraphContentTop()
            r41 = r12
            double r11 = (double) r11
            r16 = 0
        L_0x01a0:
            boolean r26 = r15.hasNext()
            if (r26 == 0) goto L_0x036e
            java.lang.Object r26 = r15.next()
            r42 = r15
            r15 = r26
            com.jjoe64.graphview.series.DataPointInterface r15 = (com.jjoe64.graphview.series.DataPointInterface) r15
            double r26 = r15.getY()
            double r26 = r26 - r9
            double r43 = r26 / r18
            double r45 = r3 * r43
            r22 = 0
            double r47 = r22 - r9
            double r49 = r47 / r18
            double r51 = r3 * r49
            r53 = r9
            double r9 = r15.getX()
            double r55 = r9 - r5
            double r57 = r55 / r24
            double r59 = r7 * r57
            com.jjoe64.graphview.ValueDependentColor r61 = r96.getValueDependentColor()
            if (r61 == 0) goto L_0x01e4
            r62 = r5
            android.graphics.Paint r5 = r0.mPaint
            com.jjoe64.graphview.ValueDependentColor r6 = r96.getValueDependentColor()
            int r6 = r6.get(r15)
            r5.setColor(r6)
            goto L_0x01ef
        L_0x01e4:
            r62 = r5
            android.graphics.Paint r5 = r0.mPaint
            int r6 = r96.getColor()
            r5.setColor(r6)
        L_0x01ef:
            r5 = 0
            double r5 = r59 + r13
            double r5 = r5 - r1
            r64 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r64 = r32 / r64
            double r5 = r5 + r64
            r66 = r1
            r68 = r7
            r1 = r41
            double r7 = (double) r1
            double r7 = r7 * r39
            double r5 = r5 + r7
            double r7 = r11 - r45
            double r7 = r7 + r3
            r70 = r1
            double r1 = r5 + r39
            double r64 = r11 - r51
            double r64 = r64 + r3
            r71 = r15
            com.jjoe64.graphview.GridLabelRenderer r15 = r97.getGridLabelRenderer()
            boolean r15 = r15.isHighlightZeroLines()
            r41 = 4
            if (r15 == 0) goto L_0x021e
            r15 = 4
            goto L_0x021f
        L_0x021e:
            r15 = 1
        L_0x021f:
            r72 = r3
            double r3 = (double) r15
            double r64 = r64 - r3
            int r3 = (r7 > r64 ? 1 : (r7 == r64 ? 0 : -1))
            if (r3 <= 0) goto L_0x022a
            r3 = 1
            goto L_0x022b
        L_0x022a:
            r3 = 0
        L_0x022b:
            boolean r4 = r0.mAnimated
            if (r4 == 0) goto L_0x0295
            r74 = r11
            double r11 = r0.mLastAnimatedValue
            boolean r4 = java.lang.Double.isNaN(r11)
            if (r4 != 0) goto L_0x0245
            double r11 = r0.mLastAnimatedValue
            int r4 = (r11 > r9 ? 1 : (r11 == r9 ? 0 : -1))
            if (r4 >= 0) goto L_0x0240
            goto L_0x0245
        L_0x0240:
            r76 = r1
            r80 = r3
            goto L_0x028d
        L_0x0245:
            long r11 = java.lang.System.currentTimeMillis()
            r76 = r1
            long r1 = r0.mAnimationStart
            r78 = 0
            int r1 = (r1 > r78 ? 1 : (r1 == r78 ? 0 : -1))
            if (r1 != 0) goto L_0x025a
            r0.mAnimationStart = r11
            r1 = 0
            r0.mAnimationStartFrameNo = r1
        L_0x0258:
            r2 = 1
            goto L_0x0268
        L_0x025a:
            int r1 = r0.mAnimationStartFrameNo
            r2 = 15
            if (r1 >= r2) goto L_0x0258
            r0.mAnimationStart = r11
            int r1 = r0.mAnimationStartFrameNo
            r2 = 1
            int r1 = r1 + r2
            r0.mAnimationStartFrameNo = r1
        L_0x0268:
            r80 = r3
            long r2 = r0.mAnimationStart
            long r1 = r11 - r2
            float r1 = (float) r1
            r2 = 1134985216(0x43a68000, float:333.0)
            float r1 = r1 / r2
            android.view.animation.AccelerateInterpolator r2 = r0.mAnimationInterpolator
            float r2 = r2.getInterpolation(r1)
            double r3 = (double) r1
            r78 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r3 = (r3 > r78 ? 1 : (r3 == r78 ? 0 : -1))
            if (r3 > 0) goto L_0x0290
            double r3 = r64 - r7
            r81 = r7
            double r7 = (double) r2
            double r3 = r3 * r7
            double r7 = r64 - r3
            android.support.p001v4.view.ViewCompat.postInvalidateOnAnimation(r97)
        L_0x028d:
            r81 = r7
            goto L_0x029d
        L_0x0290:
            r81 = r7
            r0.mLastAnimatedValue = r9
            goto L_0x029d
        L_0x0295:
            r76 = r1
            r80 = r3
            r81 = r7
            r74 = r11
        L_0x029d:
            if (r80 == 0) goto L_0x02b4
            r1 = r81
            com.jjoe64.graphview.GridLabelRenderer r3 = r97.getGridLabelRenderer()
            boolean r3 = r3.isHighlightZeroLines()
            if (r3 == 0) goto L_0x02ad
            r3 = 4
            goto L_0x02ae
        L_0x02ad:
            r3 = 1
        L_0x02ae:
            double r3 = (double) r3
            double r81 = r64 + r3
            r64 = r1
            goto L_0x02b6
        L_0x02b4:
            r1 = r64
        L_0x02b6:
            r3 = r81
            double r5 = java.lang.Math.max(r5, r13)
            double r7 = r13 + r68
            r11 = r76
            double r7 = java.lang.Math.min(r11, r7)
            double r11 = r74 + r72
            double r1 = java.lang.Math.min(r1, r11)
            r11 = r74
            double r3 = java.lang.Math.max(r3, r11)
            java.util.Map<com.jjoe64.graphview.RectD, E> r15 = r0.mDataPoints
            r90 = r9
            com.jjoe64.graphview.RectD r9 = new com.jjoe64.graphview.RectD
            r81 = r9
            r82 = r5
            r84 = r3
            r86 = r7
            r88 = r1
            r81.<init>(r82, r84, r86, r88)
            r10 = r71
            r15.put(r9, r10)
            android.graphics.Paint r9 = r0.mCustomPaint
            if (r9 == 0) goto L_0x02f1
            android.graphics.Paint r9 = r0.mCustomPaint
        L_0x02ee:
            r79 = r9
            goto L_0x02f4
        L_0x02f1:
            android.graphics.Paint r9 = r0.mPaint
            goto L_0x02ee
        L_0x02f4:
            float r9 = (float) r5
            float r15 = (float) r3
            r92 = r13
            float r13 = (float) r7
            float r14 = (float) r1
            r74 = r98
            r75 = r9
            r76 = r15
            r77 = r13
            r78 = r14
            r74.drawRect(r75, r76, r77, r78, r79)
            boolean r9 = r0.mDrawValuesOnTop
            if (r9 == 0) goto L_0x0354
            r13 = 4616189618054758400(0x4010000000000000, double:4.0)
            if (r80 == 0) goto L_0x0321
            float r9 = r0.mValuesOnTopSize
            r94 = r5
            double r5 = (double) r9
            double r5 = r5 + r1
            double r5 = r5 + r13
            double r3 = r11 + r72
            int r3 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r3 <= 0) goto L_0x031f
            double r3 = r11 + r72
            goto L_0x032c
        L_0x031f:
            r3 = r5
            goto L_0x032c
        L_0x0321:
            r94 = r5
            r5 = 0
            double r3 = r3 - r13
            int r5 = (r3 > r11 ? 1 : (r3 == r11 ? 0 : -1))
            if (r5 > 0) goto L_0x032c
            double r5 = r11 + r13
            double r3 = r3 + r5
        L_0x032c:
            android.graphics.Paint r5 = r0.mPaint
            int r6 = r0.mValuesOnTopColor
            r5.setColor(r6)
            com.jjoe64.graphview.GridLabelRenderer r5 = r97.getGridLabelRenderer()
            com.jjoe64.graphview.LabelFormatter r5 = r5.getLabelFormatter()
            double r13 = r10.getY()
            r6 = 0
            java.lang.String r5 = r5.formatLabel(r13, r6)
            double r13 = r94 + r7
            float r9 = (float) r13
            r13 = 1073741824(0x40000000, float:2.0)
            float r9 = r9 / r13
            float r13 = (float) r3
            android.graphics.Paint r14 = r0.mPaint
            r15 = r98
            r15.drawText(r5, r9, r13, r14)
            goto L_0x0359
        L_0x0354:
            r15 = r98
            r94 = r5
            r6 = 0
        L_0x0359:
            int r16 = r16 + 1
            r15 = r42
            r9 = r53
            r5 = r62
            r1 = r66
            r7 = r68
            r41 = r70
            r3 = r72
            r13 = r92
            goto L_0x01a0
        L_0x036e:
            r66 = r1
            r72 = r3
            r62 = r5
            r68 = r7
            r53 = r9
            r92 = r13
            r42 = r15
            r70 = r41
            r15 = r98
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jjoe64.graphview.series.BarGraphSeries.draw(com.jjoe64.graphview.GraphView, android.graphics.Canvas, boolean):void");
    }

    public ValueDependentColor<E> getValueDependentColor() {
        return this.mValueDependentColor;
    }

    public void setValueDependentColor(ValueDependentColor<E> mValueDependentColor2) {
        this.mValueDependentColor = mValueDependentColor2;
    }

    public int getSpacing() {
        return this.mSpacing;
    }

    public void setSpacing(int mSpacing2) {
        this.mSpacing = mSpacing2;
    }

    public double getDataWidth() {
        return this.mDataWidth;
    }

    public void setDataWidth(double mDataWidth2) {
        this.mDataWidth = mDataWidth2;
    }

    public boolean isDrawValuesOnTop() {
        return this.mDrawValuesOnTop;
    }

    public void setDrawValuesOnTop(boolean mDrawValuesOnTop2) {
        this.mDrawValuesOnTop = mDrawValuesOnTop2;
    }

    public int getValuesOnTopColor() {
        return this.mValuesOnTopColor;
    }

    public void setValuesOnTopColor(int mValuesOnTopColor2) {
        this.mValuesOnTopColor = mValuesOnTopColor2;
    }

    public float getValuesOnTopSize() {
        return this.mValuesOnTopSize;
    }

    public void setValuesOnTopSize(float mValuesOnTopSize2) {
        this.mValuesOnTopSize = mValuesOnTopSize2;
    }

    /* access modifiers changed from: protected */
    public void resetDataPoints() {
        this.mDataPoints.clear();
    }

    /* access modifiers changed from: protected */
    public E findDataPoint(float x, float y) {
        for (Entry<RectD, E> entry : this.mDataPoints.entrySet()) {
            if (((double) x) >= ((RectD) entry.getKey()).left && ((double) x) <= ((RectD) entry.getKey()).right && ((double) y) >= ((RectD) entry.getKey()).top && ((double) y) <= ((RectD) entry.getKey()).bottom) {
                return (DataPointInterface) entry.getValue();
            }
        }
        return null;
    }

    public Paint getCustomPaint() {
        return this.mCustomPaint;
    }

    public void setCustomPaint(Paint mCustomPaint2) {
        this.mCustomPaint = mCustomPaint2;
    }

    public void setAnimated(boolean animated) {
        this.mAnimated = animated;
    }

    public boolean isAnimated() {
        return this.mAnimated;
    }

    public void drawSelection(GraphView mGraphView, Canvas canvas, boolean b, DataPointInterface value) {
    }
}
