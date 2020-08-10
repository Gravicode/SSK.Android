package com.si_ware.neospectra.ChartView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.si_ware.neospectra.C1284R;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.si_ware.neospectra.ChartView.ChartView */
public class C1236ChartView extends RelativeLayout {
    private LabelAdapter mBottomLabelAdapter;
    private int mBottomLabelHeight;
    private LinearLayout mBottomLabelLayout;
    private Rect mGridBounds;
    private int mGridLineColor;
    private int mGridLineWidth;
    private int mGridLinesHorizontal;
    private int mGridLinesVertical;
    private LabelAdapter mLeftLabelAdapter;
    private LinearLayout mLeftLabelLayout;
    private int mLeftLabelWidth;
    private double mMaxX;
    private double mMaxY;
    private double mMinX;
    private double mMinY;
    private Paint mPaint;
    float mPivotX;
    float mPivotY;
    private LabelAdapter mRightLabelAdapter;
    private LinearLayout mRightLabelLayout;
    private int mRightLabelWidth;
    float mScaleFactor;
    private List<AbstractSeries> mSeries;
    private LabelAdapter mTopLabelAdapter;
    private int mTopLabelHeight;
    private LinearLayout mTopLabelLayout;
    private RectD mValueBounds;

    public C1236ChartView(Context context) {
        this(context, null, 0);
    }

    public C1236ChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public C1236ChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mScaleFactor = 1.0f;
        this.mPaint = new Paint();
        this.mSeries = new ArrayList();
        this.mValueBounds = new RectD();
        this.mMinX = Double.MAX_VALUE;
        this.mMaxX = Double.MIN_VALUE;
        this.mMinY = Double.MAX_VALUE;
        this.mMaxY = Double.MIN_VALUE;
        this.mGridBounds = new Rect();
        setWillNotDraw(false);
        setBackgroundColor(0);
        TypedArray attributes = context.obtainStyledAttributes(attrs, C1284R.styleable.ChartView);
        this.mGridLineColor = attributes.getInt(1, -16777216);
        this.mGridLineWidth = attributes.getDimensionPixelSize(2, 1);
        this.mGridLinesHorizontal = attributes.getInt(3, 5);
        this.mGridLinesVertical = attributes.getInt(4, 5);
        this.mLeftLabelWidth = attributes.getDimensionPixelSize(5, 0);
        this.mTopLabelHeight = attributes.getDimensionPixelSize(7, 0);
        this.mRightLabelWidth = attributes.getDimensionPixelSize(6, 0);
        this.mBottomLabelHeight = attributes.getDimensionPixelSize(0, 0);
        this.mLeftLabelLayout = new LinearLayout(context);
        this.mLeftLabelLayout.setLayoutParams(new LayoutParams(this.mLeftLabelWidth, -1));
        this.mLeftLabelLayout.setOrientation(1);
        this.mTopLabelLayout = new LinearLayout(context);
        this.mTopLabelLayout.setLayoutParams(new LayoutParams(-1, this.mTopLabelHeight));
        this.mTopLabelLayout.setOrientation(0);
        LayoutParams rightLabelParams = new LayoutParams(this.mRightLabelWidth, -1);
        rightLabelParams.addRule(11);
        this.mRightLabelLayout = new LinearLayout(context);
        this.mRightLabelLayout.setLayoutParams(rightLabelParams);
        this.mRightLabelLayout.setOrientation(1);
        LayoutParams bottomLabelParams = new LayoutParams(-1, this.mBottomLabelHeight);
        bottomLabelParams.addRule(12);
        this.mBottomLabelLayout = new LinearLayout(context);
        this.mBottomLabelLayout.setLayoutParams(bottomLabelParams);
        this.mBottomLabelLayout.setOrientation(0);
        addView(this.mLeftLabelLayout);
        addView(this.mTopLabelLayout);
        addView(this.mRightLabelLayout);
        addView(this.mBottomLabelLayout);
    }

    public void clearSeries() {
        this.mSeries = new ArrayList();
        resetRange();
        invalidate();
    }

    public void addSeries(AbstractSeries series) {
        if (this.mSeries == null) {
            this.mSeries = new ArrayList();
        }
        extendRange(series.getMinX(), series.getMinY());
        extendRange(series.getMaxX(), series.getMaxY());
        this.mSeries.add(series);
        invalidate();
    }

    public void setLeftLabelAdapter(LabelAdapter adapter) {
        this.mLeftLabelAdapter = adapter;
        double[] values = new double[(this.mGridLinesVertical + 2)];
        double step = this.mValueBounds.height() / ((double) (this.mGridLinesVertical + 1));
        for (int i = 0; i < values.length; i++) {
            values[i] = this.mValueBounds.top + (((double) i) * step);
        }
        this.mLeftLabelAdapter.setValues(values);
    }

    public void setTopLabelAdapter(LabelAdapter adapter) {
        this.mTopLabelAdapter = adapter;
        double[] values = new double[(this.mGridLinesHorizontal + 2)];
        double step = this.mValueBounds.width() / ((double) (this.mGridLinesHorizontal + 1));
        for (int i = 0; i < values.length; i++) {
            values[i] = this.mValueBounds.left + (((double) i) * step);
        }
        this.mTopLabelAdapter.setValues(values);
    }

    public void setRightLabelAdapter(LabelAdapter adapter) {
        this.mRightLabelAdapter = adapter;
        double[] values = new double[(this.mGridLinesVertical + 2)];
        double step = this.mValueBounds.height() / ((double) (this.mGridLinesVertical + 1));
        for (int i = 0; i < values.length; i++) {
            values[i] = this.mValueBounds.top + (((double) i) * step);
        }
        this.mRightLabelAdapter.setValues(values);
    }

    public void setBottomLabelAdapter(LabelAdapter adapter) {
        this.mBottomLabelAdapter = adapter;
        double[] values = new double[(this.mGridLinesHorizontal + 2)];
        double step = this.mValueBounds.width() / ((double) (this.mGridLinesHorizontal + 1));
        for (int i = 0; i < values.length; i++) {
            values[i] = this.mValueBounds.left + (((double) i) * step);
        }
        this.mBottomLabelAdapter.setValues(values);
    }

    public void setGridLineColor(int color) {
        this.mGridLineColor = color;
    }

    public void setGridLineWidth(int width) {
        this.mGridLineWidth = width;
    }

    public void setGridLinesHorizontal(int count) {
        this.mGridLinesHorizontal = count;
    }

    public void setGridLinesVertical(int count) {
        this.mGridLinesVertical = count;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int gridLeft = (this.mLeftLabelWidth + this.mGridLineWidth) - 1;
        int gridTop = (this.mTopLabelHeight + this.mGridLineWidth) - 1;
        int gridRight = (getWidth() - this.mRightLabelWidth) - this.mGridLineWidth;
        int gridBottom = (getHeight() - this.mBottomLabelHeight) - this.mGridLineWidth;
        this.mGridBounds.set(gridLeft, gridTop, gridRight, gridBottom);
        LayoutParams leftParams = (LayoutParams) this.mLeftLabelLayout.getLayoutParams();
        leftParams.height = this.mGridBounds.height();
        this.mLeftLabelLayout.setLayoutParams(leftParams);
        LayoutParams topParams = (LayoutParams) this.mTopLabelLayout.getLayoutParams();
        topParams.width = this.mGridBounds.width();
        this.mTopLabelLayout.setLayoutParams(topParams);
        LayoutParams rightParams = (LayoutParams) this.mRightLabelLayout.getLayoutParams();
        rightParams.height = this.mGridBounds.height();
        this.mRightLabelLayout.setLayoutParams(rightParams);
        LayoutParams bottomParams = (LayoutParams) this.mBottomLabelLayout.getLayoutParams();
        bottomParams.width = this.mGridBounds.width();
        this.mBottomLabelLayout.setLayoutParams(bottomParams);
        this.mLeftLabelLayout.layout(0, gridTop, gridLeft, gridBottom);
        this.mTopLabelLayout.layout(gridLeft, 0, gridRight, gridTop);
        this.mRightLabelLayout.layout(gridRight, gridTop, getWidth(), gridBottom);
        this.mBottomLabelLayout.layout(gridLeft, gridBottom, gridRight, getHeight());
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGrid(canvas);
        drawLabels(canvas);
        for (AbstractSeries series : this.mSeries) {
            series.draw(canvas, this.mGridBounds, this.mValueBounds);
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        canvas.save(31);
        canvas.scale(this.mScaleFactor, this.mScaleFactor, this.mPivotX, this.mPivotY);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    public void scale(float scaleFactor, float pivotX, float pivotY) {
        this.mScaleFactor = scaleFactor;
        this.mPivotX = pivotX;
        this.mPivotY = pivotY;
        invalidate();
    }

    public void restore() {
        this.mScaleFactor = 1.0f;
        invalidate();
    }

    private void resetRange() {
        this.mValueBounds.set(1300.0d, 0.0d, 2600.0d, 100.0d);
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
        this.mValueBounds.set(this.mMinX, this.mMinY, this.mMaxX, this.mMaxY);
    }

    private void drawGrid(Canvas canvas) {
        this.mPaint.setColor(this.mGridLineColor);
        this.mPaint.setStrokeWidth((float) this.mGridLineWidth);
        float stepX = ((float) this.mGridBounds.width()) / ((float) (this.mGridLinesHorizontal + 1));
        float stepY = ((float) this.mGridBounds.height()) / ((float) (this.mGridLinesVertical + 1));
        float left = (float) this.mGridBounds.left;
        float top = (float) this.mGridBounds.top;
        float bottom = (float) this.mGridBounds.bottom;
        float right = (float) this.mGridBounds.right;
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= this.mGridLinesHorizontal + 2) {
                break;
            }
            canvas.drawLine(left + (((float) i3) * stepX), top, left + (((float) i3) * stepX), bottom, this.mPaint);
            i2 = i3 + 1;
        }
        while (true) {
            int i4 = i;
            if (i4 < this.mGridLinesVertical + 2) {
                canvas.drawLine(left, top + (((float) i4) * stepY), right, top + (((float) i4) * stepY), this.mPaint);
                i = i4 + 1;
            } else {
                return;
            }
        }
    }

    private void drawLabels(Canvas canvas) {
        if (this.mLeftLabelAdapter != null) {
            drawLeftLabels(canvas);
        }
        if (this.mTopLabelAdapter != null) {
            drawTopLabels(canvas);
        }
        if (this.mRightLabelAdapter != null) {
            drawRightLabels(canvas);
        }
        if (this.mBottomLabelAdapter != null) {
            drawBottomLabels(canvas);
        }
    }

    private void drawLeftLabels(Canvas canvas) {
        int labelCount = this.mLeftLabelAdapter.getCount();
        for (int i = 0; i < labelCount; i++) {
            View view = this.mLeftLabelLayout.getChildAt(i);
            if (view == null) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, 0);
                if (i == 0 || i == labelCount - 1) {
                    params.weight = 0.5f;
                } else {
                    params.weight = 1.0f;
                }
                View view2 = this.mLeftLabelAdapter.getView((labelCount - 1) - i, view, this.mLeftLabelLayout);
                view2.setLayoutParams(params);
                this.mLeftLabelLayout.addView(view2);
            } else {
                this.mLeftLabelAdapter.getView((labelCount - 1) - i, view, this.mLeftLabelLayout);
            }
        }
        int childCount = this.mLeftLabelLayout.getChildCount();
        for (int i2 = labelCount; i2 < childCount; i2++) {
            this.mLeftLabelLayout.removeViewAt(i2);
        }
    }

    private void drawTopLabels(Canvas canvas) {
        int labelCount = this.mTopLabelAdapter.getCount();
        for (int i = 0; i < labelCount; i++) {
            View view = this.mTopLabelLayout.getChildAt(i);
            if (view == null) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, -1);
                if (i == 0 || i == labelCount - 1) {
                    params.weight = 0.5f;
                } else {
                    params.weight = 1.0f;
                }
                View view2 = this.mTopLabelAdapter.getView(i, view, this.mTopLabelLayout);
                view2.setLayoutParams(params);
                this.mTopLabelLayout.addView(view2);
            } else {
                this.mTopLabelAdapter.getView(i, view, this.mTopLabelLayout);
            }
        }
        int childCount = this.mTopLabelLayout.getChildCount();
        for (int i2 = labelCount; i2 < childCount; i2++) {
            this.mTopLabelLayout.removeViewAt(i2);
        }
    }

    private void drawRightLabels(Canvas canvas) {
        int labelCount = this.mRightLabelAdapter.getCount();
        for (int i = 0; i < labelCount; i++) {
            View view = this.mRightLabelLayout.getChildAt(i);
            if (view == null) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, 0);
                if (i == 0 || i == labelCount - 1) {
                    params.weight = 0.5f;
                } else {
                    params.weight = 1.0f;
                }
                View view2 = this.mRightLabelAdapter.getView((labelCount - 1) - i, view, this.mRightLabelLayout);
                view2.setLayoutParams(params);
                this.mRightLabelLayout.addView(view2);
            } else {
                this.mRightLabelAdapter.getView((labelCount - 1) - i, view, this.mRightLabelLayout);
            }
        }
        int childCount = this.mRightLabelLayout.getChildCount();
        for (int i2 = labelCount; i2 < childCount; i2++) {
            this.mRightLabelLayout.removeViewAt(i2);
        }
    }

    private void drawBottomLabels(Canvas canvas) {
        int labelCount = this.mBottomLabelAdapter.getCount();
        for (int i = 0; i < labelCount; i++) {
            View view = this.mBottomLabelLayout.getChildAt(i);
            if (view == null) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, -1);
                if (i == 0 || i == labelCount - 1) {
                    params.weight = 0.5f;
                } else {
                    params.weight = 1.0f;
                }
                View view2 = this.mBottomLabelAdapter.getView(i, view, this.mBottomLabelLayout);
                view2.setLayoutParams(params);
                this.mBottomLabelLayout.addView(view2);
            } else {
                this.mBottomLabelAdapter.getView(i, view, this.mBottomLabelLayout);
            }
        }
        int childCount = this.mBottomLabelLayout.getChildCount();
        for (int i2 = labelCount; i2 < childCount; i2++) {
            this.mBottomLabelLayout.removeViewAt(i2);
        }
    }
}
