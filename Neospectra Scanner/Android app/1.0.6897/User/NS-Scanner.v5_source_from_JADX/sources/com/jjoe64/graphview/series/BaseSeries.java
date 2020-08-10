package com.jjoe64.graphview.series;

import android.graphics.Canvas;
import android.graphics.PointF;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPointInterface;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

public abstract class BaseSeries<E extends DataPointInterface> implements Series<E> {
    private int mColor = -16746548;
    /* access modifiers changed from: private */
    public final List<E> mData = new ArrayList();
    private Map<PointF, E> mDataPoints = new HashMap();
    private List<WeakReference<GraphView>> mGraphViews = new ArrayList();
    private double mHighestYCache = Double.NaN;
    private Boolean mIsCursorModeCache;
    private double mLowestYCache = Double.NaN;
    protected OnDataPointTapListener mOnDataPointTapListener;
    private String mTitle;

    public abstract void drawSelection(GraphView graphView, Canvas canvas, boolean z, DataPointInterface dataPointInterface);

    public BaseSeries() {
    }

    public BaseSeries(E[] data) {
        for (E d : data) {
            this.mData.add(d);
        }
        checkValueOrder(null);
    }

    public double getLowestValueX() {
        if (this.mData.isEmpty()) {
            return 0.0d;
        }
        return ((DataPointInterface) this.mData.get(0)).getX();
    }

    public double getHighestValueX() {
        if (this.mData.isEmpty()) {
            return 0.0d;
        }
        return ((DataPointInterface) this.mData.get(this.mData.size() - 1)).getX();
    }

    public double getLowestValueY() {
        if (this.mData.isEmpty()) {
            return 0.0d;
        }
        if (!Double.isNaN(this.mLowestYCache)) {
            return this.mLowestYCache;
        }
        double l = ((DataPointInterface) this.mData.get(0)).getY();
        for (int i = 1; i < this.mData.size(); i++) {
            double c = ((DataPointInterface) this.mData.get(i)).getY();
            if (l > c) {
                l = c;
            }
        }
        this.mLowestYCache = l;
        return l;
    }

    public double getHighestValueY() {
        if (this.mData.isEmpty()) {
            return 0.0d;
        }
        if (!Double.isNaN(this.mHighestYCache)) {
            return this.mHighestYCache;
        }
        double h = ((DataPointInterface) this.mData.get(0)).getY();
        for (int i = 1; i < this.mData.size(); i++) {
            double c = ((DataPointInterface) this.mData.get(i)).getY();
            if (h < c) {
                h = c;
            }
        }
        this.mHighestYCache = h;
        return h;
    }

    public Iterator<E> getValues(double from, double until) {
        if (from <= getLowestValueX() && until >= getHighestValueX()) {
            return this.mData.iterator();
        }
        final double d = from;
        final double d2 = until;
        C06851 r1 = new Iterator<E>() {
            E nextNextValue = null;
            E nextValue = null;

            /* renamed from: org reason: collision with root package name */
            Iterator<E> f934org = BaseSeries.this.mData.iterator();
            boolean plusOne = true;

            {
                boolean found = false;
                E prevValue = null;
                if (this.f934org.hasNext()) {
                    prevValue = (DataPointInterface) this.f934org.next();
                }
                if (prevValue != null) {
                    if (prevValue.getX() < d) {
                        while (true) {
                            if (!this.f934org.hasNext()) {
                                break;
                            }
                            this.nextValue = (DataPointInterface) this.f934org.next();
                            if (this.nextValue.getX() >= d) {
                                found = true;
                                this.nextNextValue = this.nextValue;
                                this.nextValue = prevValue;
                                break;
                            }
                            prevValue = this.nextValue;
                        }
                    } else {
                        this.nextValue = prevValue;
                        found = true;
                    }
                }
                if (!found) {
                    this.nextValue = null;
                }
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

            public E next() {
                if (hasNext()) {
                    E r = this.nextValue;
                    if (r.getX() > d2) {
                        this.plusOne = false;
                    }
                    if (this.nextNextValue != null) {
                        this.nextValue = this.nextNextValue;
                        this.nextNextValue = null;
                    } else if (this.f934org.hasNext()) {
                        this.nextValue = (DataPointInterface) this.f934org.next();
                    } else {
                        this.nextValue = null;
                    }
                    return r;
                }
                throw new NoSuchElementException();
            }

            public boolean hasNext() {
                return this.nextValue != null && (this.nextValue.getX() <= d2 || this.plusOne);
            }
        };
        return r1;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String mTitle2) {
        this.mTitle = mTitle2;
    }

    public int getColor() {
        return this.mColor;
    }

    public void setColor(int mColor2) {
        this.mColor = mColor2;
    }

    public void setOnDataPointTapListener(OnDataPointTapListener l) {
        this.mOnDataPointTapListener = l;
    }

    public void onTap(float x, float y) {
        if (this.mOnDataPointTapListener != null) {
            E p = findDataPoint(x, y);
            if (p != null) {
                this.mOnDataPointTapListener.onTap(this, p);
            }
        }
    }

    /* access modifiers changed from: protected */
    public E findDataPoint(float x, float y) {
        float shortestDistance = Float.NaN;
        E shortest = null;
        for (Entry<PointF, E> entry : this.mDataPoints.entrySet()) {
            float x1 = ((PointF) entry.getKey()).x;
            float y1 = ((PointF) entry.getKey()).y;
            float x2 = x;
            float y2 = y;
            float distance = (float) Math.sqrt((double) (((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2))));
            if (shortest == null || distance < shortestDistance) {
                shortestDistance = distance;
                shortest = (DataPointInterface) entry.getValue();
            }
        }
        if (shortest == null || shortestDistance >= 120.0f) {
            return null;
        }
        return shortest;
    }

    public E findDataPointAtX(float x) {
        float shortestDistance = Float.NaN;
        E shortest = null;
        for (Entry<PointF, E> entry : this.mDataPoints.entrySet()) {
            float distance = Math.abs(((PointF) entry.getKey()).x - x);
            if (shortest == null || distance < shortestDistance) {
                shortestDistance = distance;
                shortest = (DataPointInterface) entry.getValue();
            }
        }
        if (shortest == null || shortestDistance >= 200.0f) {
            return null;
        }
        return shortest;
    }

    /* access modifiers changed from: protected */
    public void registerDataPoint(float x, float y, E dp) {
        if (this.mOnDataPointTapListener != null || isCursorMode()) {
            this.mDataPoints.put(new PointF(x, y), dp);
        }
    }

    private boolean isCursorMode() {
        if (this.mIsCursorModeCache != null) {
            return this.mIsCursorModeCache.booleanValue();
        }
        for (WeakReference<GraphView> graphView : this.mGraphViews) {
            if (graphView != null && graphView.get() != null && ((GraphView) graphView.get()).isCursorMode()) {
                Boolean valueOf = Boolean.valueOf(true);
                this.mIsCursorModeCache = valueOf;
                return valueOf.booleanValue();
            }
        }
        Boolean valueOf2 = Boolean.valueOf(false);
        this.mIsCursorModeCache = valueOf2;
        return valueOf2.booleanValue();
    }

    /* access modifiers changed from: protected */
    public void resetDataPoints() {
        this.mDataPoints.clear();
    }

    public void resetData(E[] data) {
        this.mData.clear();
        for (E d : data) {
            this.mData.add(d);
        }
        checkValueOrder(null);
        this.mLowestYCache = Double.NaN;
        this.mHighestYCache = Double.NaN;
        for (WeakReference<GraphView> gv : this.mGraphViews) {
            if (!(gv == null || gv.get() == null)) {
                ((GraphView) gv.get()).onDataChanged(true, false);
            }
        }
    }

    public void onGraphViewAttached(GraphView graphView) {
        this.mGraphViews.add(new WeakReference(graphView));
    }

    public void appendData(E dataPoint, boolean scrollToEnd, int maxDataPoints, boolean silent) {
        checkValueOrder(dataPoint);
        boolean z = true;
        if (this.mData.isEmpty() || dataPoint.getX() >= ((DataPointInterface) this.mData.get(this.mData.size() - 1)).getX()) {
            synchronized (this.mData) {
                if (this.mData.size() < maxDataPoints) {
                    this.mData.add(dataPoint);
                } else {
                    this.mData.remove(0);
                    this.mData.add(dataPoint);
                }
                double dataPointY = dataPoint.getY();
                if (!Double.isNaN(this.mHighestYCache) && dataPointY > this.mHighestYCache) {
                    this.mHighestYCache = dataPointY;
                }
                if (!Double.isNaN(this.mLowestYCache) && dataPointY < this.mLowestYCache) {
                    this.mLowestYCache = dataPointY;
                }
            }
            if (!silent) {
                if (this.mData.size() == 1) {
                    z = false;
                }
                boolean keepLabels = z;
                for (WeakReference<GraphView> gv : this.mGraphViews) {
                    if (!(gv == null || gv.get() == null)) {
                        if (scrollToEnd) {
                            ((GraphView) gv.get()).getViewport().scrollToEnd();
                        } else {
                            ((GraphView) gv.get()).onDataChanged(keepLabels, scrollToEnd);
                        }
                    }
                }
                return;
            }
            return;
        }
        throw new IllegalArgumentException("new x-value must be greater then the last value. x-values has to be ordered in ASC.");
    }

    public void appendData(E dataPoint, boolean scrollToEnd, int maxDataPoints) {
        appendData(dataPoint, scrollToEnd, maxDataPoints, false);
    }

    public boolean isEmpty() {
        return this.mData.isEmpty();
    }

    /* access modifiers changed from: protected */
    public void checkValueOrder(DataPointInterface onlyLast) {
        int i = 1;
        if (this.mData.size() <= 1) {
            return;
        }
        if (onlyLast == null) {
            double lx = ((DataPointInterface) this.mData.get(0)).getX();
            while (true) {
                int i2 = i;
                if (i2 < this.mData.size()) {
                    if (((DataPointInterface) this.mData.get(i2)).getX() != Double.NaN) {
                        if (lx > ((DataPointInterface) this.mData.get(i2)).getX()) {
                            throw new IllegalArgumentException("The order of the values is not correct. X-Values have to be ordered ASC. First the lowest x value and at least the highest x value.");
                        }
                        lx = ((DataPointInterface) this.mData.get(i2)).getX();
                    }
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        } else if (onlyLast.getX() < ((DataPointInterface) this.mData.get(this.mData.size() - 1)).getX()) {
            throw new IllegalArgumentException("new x-value must be greater then the last value. x-values has to be ordered in ASC.");
        }
    }

    public void clearCursorModeCache() {
        this.mIsCursorModeCache = null;
    }

    public void clearReference(GraphView graphView) {
        for (WeakReference<GraphView> view : this.mGraphViews) {
            if (view != null && view.get() != null && view.get() == graphView) {
                this.mGraphViews.remove(view);
                return;
            }
        }
    }
}
