package com.jjoe64.graphview;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.TypedValue;
import com.jjoe64.graphview.series.Series;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.p009ss.usermodel.ShapeTypes;

public class LegendRenderer {
    private int cachedLegendWidth;
    private final GraphView mGraphView;
    private boolean mIsVisible = false;
    private Paint mPaint = new Paint();
    private Styles mStyles;

    public enum LegendAlign {
        TOP,
        MIDDLE,
        BOTTOM
    }

    private final class Styles {
        LegendAlign align;
        int backgroundColor;
        Point fixedPosition;
        int margin;
        int padding;
        int spacing;
        int textColor;
        float textSize;
        int width;

        private Styles() {
        }
    }

    public LegendRenderer(GraphView graphView) {
        this.mGraphView = graphView;
        this.mPaint.setTextAlign(Align.LEFT);
        this.mStyles = new Styles();
        this.cachedLegendWidth = 0;
        resetStyles();
    }

    public void resetStyles() {
        this.mStyles.align = LegendAlign.MIDDLE;
        this.mStyles.textSize = this.mGraphView.getGridLabelRenderer().getTextSize();
        this.mStyles.spacing = (int) (this.mStyles.textSize / 5.0f);
        this.mStyles.padding = (int) (this.mStyles.textSize / 2.0f);
        this.mStyles.width = 0;
        this.mStyles.backgroundColor = Color.argb(ShapeTypes.MATH_EQUAL, 100, 100, 100);
        this.mStyles.margin = (int) (this.mStyles.textSize / 5.0f);
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

    /* access modifiers changed from: protected */
    public List<Series> getAllSeries() {
        List<Series> allSeries = new ArrayList<>();
        allSeries.addAll(this.mGraphView.getSeries());
        if (this.mGraphView.mSecondScale != null) {
            allSeries.addAll(this.mGraphView.getSecondScale().getSeries());
        }
        return allSeries;
    }

    public void draw(Canvas canvas) {
        float lTop;
        float lLeft;
        Canvas canvas2 = canvas;
        if (this.mIsVisible) {
            this.mPaint.setTextSize(this.mStyles.textSize);
            int shapeSize = (int) (((double) this.mStyles.textSize) * 0.8d);
            List<Series> allSeries = getAllSeries();
            int legendWidth = this.mStyles.width;
            if (legendWidth == 0) {
                legendWidth = this.cachedLegendWidth;
                if (legendWidth == 0) {
                    Rect textBounds = new Rect();
                    for (Series s : allSeries) {
                        if (s.getTitle() != null) {
                            this.mPaint.getTextBounds(s.getTitle(), 0, s.getTitle().length(), textBounds);
                            legendWidth = Math.max(legendWidth, textBounds.width());
                        }
                    }
                    if (legendWidth == 0) {
                        legendWidth = 1;
                    }
                    legendWidth += (this.mStyles.padding * 2) + shapeSize + this.mStyles.spacing;
                    this.cachedLegendWidth = legendWidth;
                }
            }
            float legendHeight = ((this.mStyles.textSize + ((float) this.mStyles.spacing)) * ((float) allSeries.size())) - ((float) this.mStyles.spacing);
            if (this.mStyles.fixedPosition == null) {
                lLeft = (float) (((this.mGraphView.getGraphContentLeft() + this.mGraphView.getGraphContentWidth()) - legendWidth) - this.mStyles.margin);
                switch (this.mStyles.align) {
                    case TOP:
                        lTop = (float) (this.mGraphView.getGraphContentTop() + this.mStyles.margin);
                        break;
                    case MIDDLE:
                        lTop = ((float) (this.mGraphView.getHeight() / 2)) - (legendHeight / 2.0f);
                        break;
                    default:
                        lTop = (((float) ((this.mGraphView.getGraphContentTop() + this.mGraphView.getGraphContentHeight()) - this.mStyles.margin)) - legendHeight) - ((float) (this.mStyles.padding * 2));
                        break;
                }
            } else {
                lLeft = (float) (this.mGraphView.getGraphContentLeft() + this.mStyles.margin + this.mStyles.fixedPosition.x);
                lTop = (float) (this.mGraphView.getGraphContentTop() + this.mStyles.margin + this.mStyles.fixedPosition.y);
            }
            float lRight = ((float) legendWidth) + lLeft;
            float lBottom = lTop + legendHeight + ((float) (this.mStyles.padding * 2));
            this.mPaint.setColor(this.mStyles.backgroundColor);
            canvas2.drawRoundRect(new RectF(lLeft, lTop, lRight, lBottom), 8.0f, 8.0f, this.mPaint);
            int i = 0;
            for (Series series : allSeries) {
                this.mPaint.setColor(series.getColor());
                List list = allSeries;
                int legendWidth2 = legendWidth;
                float legendHeight2 = legendHeight;
                float lRight2 = lRight;
                float lBottom2 = lBottom;
                canvas2.drawRect(new RectF(((float) this.mStyles.padding) + lLeft, ((float) this.mStyles.padding) + lTop + (((float) i) * (this.mStyles.textSize + ((float) this.mStyles.spacing))), ((float) this.mStyles.padding) + lLeft + ((float) shapeSize), ((float) this.mStyles.padding) + lTop + (((float) i) * (this.mStyles.textSize + ((float) this.mStyles.spacing))) + ((float) shapeSize)), this.mPaint);
                if (series.getTitle() != null) {
                    this.mPaint.setColor(this.mStyles.textColor);
                    canvas2.drawText(series.getTitle(), ((float) this.mStyles.padding) + lLeft + ((float) shapeSize) + ((float) this.mStyles.spacing), ((float) this.mStyles.padding) + lTop + this.mStyles.textSize + (((float) i) * (this.mStyles.textSize + ((float) this.mStyles.spacing))), this.mPaint);
                }
                i++;
                allSeries = list;
                legendWidth = legendWidth2;
                legendHeight = legendHeight2;
                lRight = lRight2;
                lBottom = lBottom2;
            }
            List list2 = allSeries;
            int i2 = legendWidth;
            float f = legendHeight;
            float f2 = lRight;
            float f3 = lBottom;
        }
    }

    public boolean isVisible() {
        return this.mIsVisible;
    }

    public void setVisible(boolean mIsVisible2) {
        this.mIsVisible = mIsVisible2;
    }

    public float getTextSize() {
        return this.mStyles.textSize;
    }

    public void setTextSize(float textSize) {
        this.mStyles.textSize = textSize;
        this.cachedLegendWidth = 0;
    }

    public int getSpacing() {
        return this.mStyles.spacing;
    }

    public void setSpacing(int spacing) {
        this.mStyles.spacing = spacing;
    }

    public int getPadding() {
        return this.mStyles.padding;
    }

    public void setPadding(int padding) {
        this.mStyles.padding = padding;
    }

    public int getWidth() {
        return this.mStyles.width;
    }

    public void setWidth(int width) {
        this.mStyles.width = width;
    }

    public int getBackgroundColor() {
        return this.mStyles.backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.mStyles.backgroundColor = backgroundColor;
    }

    public int getMargin() {
        return this.mStyles.margin;
    }

    public void setMargin(int margin) {
        this.mStyles.margin = margin;
    }

    public LegendAlign getAlign() {
        return this.mStyles.align;
    }

    public void setAlign(LegendAlign align) {
        this.mStyles.align = align;
    }

    public int getTextColor() {
        return this.mStyles.textColor;
    }

    public void setTextColor(int textColor) {
        this.mStyles.textColor = textColor;
    }

    public void setFixedPosition(int x, int y) {
        this.mStyles.fixedPosition = new Point(x, y);
    }
}
