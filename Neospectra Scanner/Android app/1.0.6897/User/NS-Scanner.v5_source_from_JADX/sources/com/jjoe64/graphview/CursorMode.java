package com.jjoe64.graphview;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.TypedValue;
import android.view.MotionEvent;
import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.Series;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.poi.p009ss.usermodel.ShapeTypes;

public class CursorMode {
    protected int cachedLegendWidth;
    protected final Map<BaseSeries, DataPointInterface> mCurrentSelection;
    protected double mCurrentSelectionX;
    protected boolean mCursorVisible;
    protected final GraphView mGraphView;
    protected final Paint mPaintLine;
    protected float mPosX;
    protected float mPosY;
    protected final Paint mRectPaint;
    protected Styles mStyles = new Styles();
    protected final Paint mTextPaint;

    private static final class Styles {
        public int backgroundColor;
        public int margin;
        public int padding;
        public int spacing;
        public int textColor;
        public float textSize;
        public int width;

        private Styles() {
        }
    }

    public CursorMode(GraphView graphView) {
        this.mGraphView = graphView;
        this.mPaintLine = new Paint();
        this.mPaintLine.setColor(Color.argb(128, ShapeTypes.MATH_EQUAL, ShapeTypes.MATH_EQUAL, ShapeTypes.MATH_EQUAL));
        this.mPaintLine.setStrokeWidth(10.0f);
        this.mCurrentSelection = new HashMap();
        this.mRectPaint = new Paint();
        this.mTextPaint = new Paint();
        resetStyles();
    }

    public void resetStyles() {
        this.mStyles.textSize = this.mGraphView.getGridLabelRenderer().getTextSize();
        this.mStyles.spacing = (int) (this.mStyles.textSize / 5.0f);
        this.mStyles.padding = (int) (this.mStyles.textSize / 2.0f);
        this.mStyles.width = 0;
        this.mStyles.backgroundColor = Color.argb(ShapeTypes.MATH_EQUAL, 100, 100, 100);
        this.mStyles.margin = (int) this.mStyles.textSize;
        TypedValue typedValue = new TypedValue();
        this.mGraphView.getContext().getTheme().resolveAttribute(16842818, typedValue, true);
        int color1 = -16777216;
        try {
            TypedArray array = this.mGraphView.getContext().obtainStyledAttributes(typedValue.data, new int[]{16842806});
            int color12 = array.getColor(0, -16777216);
            array.recycle();
            color1 = color12;
        } catch (Exception e) {
        }
        this.mStyles.textColor = color1;
        this.cachedLegendWidth = 0;
    }

    public void onDown(MotionEvent e) {
        this.mPosX = Math.max(e.getX(), (float) this.mGraphView.getGraphContentLeft());
        this.mPosX = Math.min(this.mPosX, (float) (this.mGraphView.getGraphContentLeft() + this.mGraphView.getGraphContentWidth()));
        this.mPosY = e.getY();
        this.mCursorVisible = true;
        findCurrentDataPoint();
        this.mGraphView.invalidate();
    }

    public void onMove(MotionEvent e) {
        if (this.mCursorVisible) {
            this.mPosX = Math.max(e.getX(), (float) this.mGraphView.getGraphContentLeft());
            this.mPosX = Math.min(this.mPosX, (float) (this.mGraphView.getGraphContentLeft() + this.mGraphView.getGraphContentWidth()));
            this.mPosY = e.getY();
            findCurrentDataPoint();
            this.mGraphView.invalidate();
        }
    }

    public void draw(Canvas canvas) {
        if (this.mCursorVisible) {
            canvas.drawLine(this.mPosX, 0.0f, this.mPosX, (float) canvas.getHeight(), this.mPaintLine);
        }
        for (Entry<BaseSeries, DataPointInterface> entry : this.mCurrentSelection.entrySet()) {
            ((BaseSeries) entry.getKey()).drawSelection(this.mGraphView, canvas, false, (DataPointInterface) entry.getValue());
        }
        if (!this.mCurrentSelection.isEmpty()) {
            drawLegend(canvas);
        }
    }

    /* access modifiers changed from: protected */
    public String getTextForSeries(Series s, DataPointInterface value) {
        StringBuffer txt = new StringBuffer();
        if (s.getTitle() != null) {
            txt.append(s.getTitle());
            txt.append(": ");
        }
        txt.append(this.mGraphView.getGridLabelRenderer().getLabelFormatter().formatLabel(value.getY(), false));
        return txt.toString();
    }

    /* access modifiers changed from: protected */
    public void drawLegend(Canvas canvas) {
        Canvas canvas2 = canvas;
        this.mTextPaint.setTextSize(this.mStyles.textSize);
        this.mTextPaint.setColor(this.mStyles.textColor);
        int shapeSize = (int) (((double) this.mStyles.textSize) * 0.8d);
        int legendWidth = this.mStyles.width;
        if (legendWidth == 0) {
            legendWidth = this.cachedLegendWidth;
            if (legendWidth == 0) {
                Rect textBounds = new Rect();
                for (Entry<BaseSeries, DataPointInterface> entry : this.mCurrentSelection.entrySet()) {
                    String txt = getTextForSeries((Series) entry.getKey(), (DataPointInterface) entry.getValue());
                    this.mTextPaint.getTextBounds(txt, 0, txt.length(), textBounds);
                    legendWidth = Math.max(legendWidth, textBounds.width());
                }
                if (legendWidth == 0) {
                    legendWidth = 1;
                }
                legendWidth += (this.mStyles.padding * 2) + shapeSize + this.mStyles.spacing;
                this.cachedLegendWidth = legendWidth;
            }
        }
        float legendPosX = (this.mPosX - ((float) this.mStyles.margin)) - ((float) legendWidth);
        if (legendPosX < 0.0f) {
            legendPosX = 0.0f;
        }
        float legendHeight = ((this.mStyles.textSize + ((float) this.mStyles.spacing)) * ((float) (this.mCurrentSelection.size() + 1))) - ((float) this.mStyles.spacing);
        float legendPosY = (this.mPosY - legendHeight) - (this.mStyles.textSize * 4.5f);
        if (legendPosY < 0.0f) {
            legendPosY = 0.0f;
        }
        float lLeft = legendPosX;
        float lTop = legendPosY;
        float lRight = ((float) legendWidth) + lLeft;
        float lBottom = lTop + legendHeight + ((float) (this.mStyles.padding * 2));
        this.mRectPaint.setColor(this.mStyles.backgroundColor);
        canvas2.drawRoundRect(new RectF(lLeft, lTop, lRight, lBottom), 8.0f, 8.0f, this.mRectPaint);
        this.mTextPaint.setFakeBoldText(true);
        canvas2.drawText(this.mGraphView.getGridLabelRenderer().getLabelFormatter().formatLabel(this.mCurrentSelectionX, true), ((float) this.mStyles.padding) + lLeft, ((float) (this.mStyles.padding / 2)) + lTop + this.mStyles.textSize, this.mTextPaint);
        this.mTextPaint.setFakeBoldText(false);
        int i = 1;
        Iterator it = this.mCurrentSelection.entrySet().iterator();
        while (it.hasNext()) {
            Entry<BaseSeries, DataPointInterface> entry2 = (Entry) it.next();
            this.mRectPaint.setColor(((BaseSeries) entry2.getKey()).getColor());
            int legendWidth2 = legendWidth;
            float legendPosX2 = legendPosX;
            float legendHeight2 = legendHeight;
            float legendPosY2 = legendPosY;
            Iterator it2 = it;
            float lRight2 = lRight;
            canvas2.drawRect(new RectF(((float) this.mStyles.padding) + lLeft, ((float) this.mStyles.padding) + lTop + (((float) i) * (this.mStyles.textSize + ((float) this.mStyles.spacing))), ((float) this.mStyles.padding) + lLeft + ((float) shapeSize), ((float) this.mStyles.padding) + lTop + (((float) i) * (this.mStyles.textSize + ((float) this.mStyles.spacing))) + ((float) shapeSize)), this.mRectPaint);
            canvas2.drawText(getTextForSeries((Series) entry2.getKey(), (DataPointInterface) entry2.getValue()), ((float) this.mStyles.padding) + lLeft + ((float) shapeSize) + ((float) this.mStyles.spacing), ((float) (this.mStyles.padding / 2)) + lTop + this.mStyles.textSize + (((float) i) * (this.mStyles.textSize + ((float) this.mStyles.spacing))), this.mTextPaint);
            i++;
            legendWidth = legendWidth2;
            legendPosX = legendPosX2;
            legendHeight = legendHeight2;
            legendPosY = legendPosY2;
            it = it2;
            lRight = lRight2;
        }
        float f = legendPosX;
        float f2 = legendHeight;
        float f3 = legendPosY;
        float f4 = lRight;
    }

    public boolean onUp(MotionEvent event) {
        this.mCursorVisible = false;
        findCurrentDataPoint();
        this.mGraphView.invalidate();
        return true;
    }

    private void findCurrentDataPoint() {
        double selX = 0.0d;
        this.mCurrentSelection.clear();
        for (Series series : this.mGraphView.getSeries()) {
            if (series instanceof BaseSeries) {
                DataPointInterface p = ((BaseSeries) series).findDataPointAtX(this.mPosX);
                if (p != null) {
                    selX = p.getX();
                    this.mCurrentSelection.put((BaseSeries) series, p);
                }
            }
        }
        if (!this.mCurrentSelection.isEmpty()) {
            this.mCurrentSelectionX = selX;
        }
    }

    public void setTextSize(float t) {
        this.mStyles.textSize = t;
    }

    public void setTextColor(int color) {
        this.mStyles.textColor = color;
    }

    public void setBackgroundColor(int color) {
        this.mStyles.backgroundColor = color;
    }

    public void setSpacing(int s) {
        this.mStyles.spacing = s;
    }

    public void setPadding(int s) {
        this.mStyles.padding = s;
    }

    public void setMargin(int s) {
        this.mStyles.margin = s;
    }

    public void setWidth(int s) {
        this.mStyles.width = s;
    }
}
