package com.jjoe64.graphview.series;

import android.graphics.Canvas;
import android.graphics.Canvas.VertexMode;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Path;
import android.graphics.Point;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPointInterface;
import java.util.Iterator;

public class PointsGraphSeries<E extends DataPointInterface> extends BaseSeries<E> {
    private CustomShape mCustomShape;
    private Paint mPaint;
    private Styles mStyles;

    public interface CustomShape {
        void draw(Canvas canvas, Paint paint, float f, float f2, DataPointInterface dataPointInterface);
    }

    public enum Shape {
        POINT,
        TRIANGLE,
        RECTANGLE
    }

    private final class Styles {
        Shape shape;
        float size;

        private Styles() {
        }
    }

    public PointsGraphSeries() {
        init();
    }

    public PointsGraphSeries(E[] data) {
        super(data);
        init();
    }

    /* access modifiers changed from: protected */
    public void init() {
        this.mStyles = new Styles<>();
        this.mStyles.size = 20.0f;
        this.mPaint = new Paint();
        this.mPaint.setStrokeCap(Cap.ROUND);
        setShape(Shape.POINT);
    }

    public void draw(GraphView graphView, Canvas canvas, boolean isSecondScale) {
        double maxY;
        double minY;
        float graphHeight;
        Iterator it;
        float graphWidth;
        float graphLeft;
        float graphTop;
        Canvas canvas2 = canvas;
        resetDataPoints();
        double maxX = graphView.getViewport().getMaxX(false);
        double minX = graphView.getViewport().getMinX(false);
        if (isSecondScale) {
            maxY = graphView.getSecondScale().getMaxY(false);
            minY = graphView.getSecondScale().getMinY(false);
        } else {
            maxY = graphView.getViewport().getMaxY(false);
            minY = graphView.getViewport().getMinY(false);
        }
        double maxY2 = maxY;
        double minY2 = minY;
        Iterator values = getValues(minX, maxX);
        this.mPaint.setColor(getColor());
        double diffY = maxY2 - minY2;
        double diffX = maxX - minX;
        float graphHeight2 = (float) graphView.getGraphContentHeight();
        float graphWidth2 = (float) graphView.getGraphContentWidth();
        float graphLeft2 = (float) graphView.getGraphContentLeft();
        float graphTop2 = (float) graphView.getGraphContentTop();
        int i = 0;
        while (true) {
            int i2 = i;
            if (values.hasNext() != 0) {
                E value = (DataPointInterface) values.next();
                double maxX2 = maxX;
                double maxX3 = ((double) graphHeight2) * ((value.getY() - minY2) / diffY);
                double minX2 = minX;
                double x = ((double) graphWidth2) * ((value.getX() - minX) / diffX);
                double d = x;
                double d2 = maxX3;
                boolean overdraw = false;
                double maxY3 = maxY2;
                if (x > ((double) graphWidth2)) {
                    overdraw = true;
                }
                if (maxX3 < 0.0d) {
                    overdraw = true;
                }
                if (maxX3 > ((double) graphHeight2)) {
                    overdraw = true;
                }
                if (x < 0.0d) {
                    overdraw = true;
                }
                boolean overdraw2 = overdraw;
                float endX = 1.0f + graphLeft2 + ((float) x);
                float graphLeft3 = graphLeft2;
                float endY = ((float) (((double) graphTop2) - maxX3)) + graphHeight2;
                registerDataPoint(endX, endY, value);
                if (!overdraw2) {
                    if (this.mCustomShape != null) {
                        graphTop = graphTop2;
                        graphLeft = graphLeft3;
                        graphWidth = graphWidth2;
                        it = values;
                        this.mCustomShape.draw(canvas2, this.mPaint, endX, endY, value);
                    } else {
                        graphTop = graphTop2;
                        graphWidth = graphWidth2;
                        it = values;
                        graphLeft = graphLeft3;
                        DataPointInterface dataPointInterface = value;
                        if (this.mStyles.shape == Shape.POINT) {
                            canvas2.drawCircle(endX, endY, this.mStyles.size, this.mPaint);
                        } else if (this.mStyles.shape == Shape.RECTANGLE) {
                            canvas2.drawRect(endX - this.mStyles.size, endY - this.mStyles.size, endX + this.mStyles.size, endY + this.mStyles.size, this.mPaint);
                        } else if (this.mStyles.shape == Shape.TRIANGLE) {
                            graphHeight = graphHeight2;
                            double d3 = maxX3;
                            drawArrows(new Point[]{new Point((int) endX, (int) (endY - getSize())), new Point((int) (getSize() + endX), (int) (((double) endY) + (((double) getSize()) * 0.67d))), new Point((int) (endX - getSize()), (int) (((double) endY) + (((double) getSize()) * 0.67d)))}, canvas2, this.mPaint);
                        } else {
                            graphHeight = graphHeight2;
                            double d4 = maxX3;
                        }
                    }
                    graphHeight = graphHeight2;
                    double y = maxX3;
                } else {
                    graphTop = graphTop2;
                    graphWidth = graphWidth2;
                    it = values;
                    graphHeight = graphHeight2;
                    double d5 = maxX3;
                    graphLeft = graphLeft3;
                    DataPointInterface dataPointInterface2 = value;
                }
                i = i2 + 1;
                maxX = maxX2;
                minX = minX2;
                maxY2 = maxY3;
                graphTop2 = graphTop;
                graphLeft2 = graphLeft;
                graphWidth2 = graphWidth;
                values = it;
                graphHeight2 = graphHeight;
            } else {
                float f = graphLeft2;
                float f2 = graphWidth2;
                Iterator it2 = values;
                float f3 = graphHeight2;
                double d6 = maxX;
                double d7 = minX;
                double d8 = maxY2;
                return;
            }
        }
    }

    private void drawArrows(Point[] point, Canvas canvas, Paint paint) {
        Canvas canvas2 = canvas;
        canvas2.drawVertices(VertexMode.TRIANGLES, 8, new float[]{(float) point[0].x, (float) point[0].y, (float) point[1].x, (float) point[1].y, (float) point[2].x, (float) point[2].y, (float) point[0].x, (float) point[0].y}, 0, null, 0, null, 0, null, 0, 0, paint);
        Path path = new Path();
        path.moveTo((float) point[0].x, (float) point[0].y);
        path.lineTo((float) point[1].x, (float) point[1].y);
        path.lineTo((float) point[2].x, (float) point[2].y);
        canvas.drawPath(path, paint);
    }

    public float getSize() {
        return this.mStyles.size;
    }

    public void setSize(float radius) {
        this.mStyles.size = radius;
    }

    public Shape getShape() {
        return this.mStyles.shape;
    }

    public void setShape(Shape s) {
        this.mStyles.shape = s;
    }

    public void setCustomShape(CustomShape shape) {
        this.mCustomShape = shape;
    }

    public void drawSelection(GraphView mGraphView, Canvas canvas, boolean b, DataPointInterface value) {
    }
}
