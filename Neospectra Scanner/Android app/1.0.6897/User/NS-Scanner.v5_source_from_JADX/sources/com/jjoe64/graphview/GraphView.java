package com.jjoe64.graphview;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.MediaStore.Images.Media;
import android.support.p004v7.widget.helper.ItemTouchHelper.Callback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.Series;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GraphView extends View {
    private CursorMode mCursorMode;
    private GridLabelRenderer mGridLabelRenderer;
    private boolean mIsCursorMode;
    private LegendRenderer mLegendRenderer;
    private Paint mPaintTitle;
    private Paint mPreviewPaint;
    protected SecondScale mSecondScale;
    private List<Series> mSeries;
    private Styles mStyles;
    private TapDetector mTapDetector;
    private String mTitle;
    private Viewport mViewport;

    private static final class Styles {
        int titleColor;
        float titleTextSize;

        private Styles() {
        }
    }

    private class TapDetector {
        private long lastDown;
        private PointF lastPoint;

        private TapDetector() {
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == 0) {
                this.lastDown = System.currentTimeMillis();
                this.lastPoint = new PointF(event.getX(), event.getY());
            } else if (this.lastDown <= 0 || event.getAction() != 2) {
                if (event.getAction() == 1 && System.currentTimeMillis() - this.lastDown < 400) {
                    return true;
                }
            } else if (Math.abs(event.getX() - this.lastPoint.x) > 60.0f || Math.abs(event.getY() - this.lastPoint.y) > 60.0f) {
                this.lastDown = 0;
            }
            return false;
        }
    }

    public GraphView(Context context) {
        super(context);
        init();
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /* access modifiers changed from: protected */
    public void init() {
        this.mPreviewPaint = new Paint();
        this.mPreviewPaint.setTextAlign(Align.CENTER);
        this.mPreviewPaint.setColor(-16777216);
        this.mPreviewPaint.setTextSize(50.0f);
        this.mStyles = new Styles();
        this.mViewport = new Viewport(this);
        this.mGridLabelRenderer = new GridLabelRenderer(this);
        this.mLegendRenderer = new LegendRenderer(this);
        this.mSeries = new ArrayList();
        this.mPaintTitle = new Paint();
        this.mTapDetector = new TapDetector();
        loadStyles();
    }

    /* access modifiers changed from: protected */
    public void loadStyles() {
        this.mStyles.titleColor = this.mGridLabelRenderer.getHorizontalLabelsColor();
        this.mStyles.titleTextSize = this.mGridLabelRenderer.getTextSize();
    }

    public GridLabelRenderer getGridLabelRenderer() {
        return this.mGridLabelRenderer;
    }

    public void addSeries(Series s) {
        s.onGraphViewAttached(this);
        this.mSeries.add(s);
        onDataChanged(false, false);
    }

    public List<Series> getSeries() {
        return this.mSeries;
    }

    public void onDataChanged(boolean keepLabelsSize, boolean keepViewport) {
        this.mViewport.calcCompleteRange();
        if (this.mSecondScale != null) {
            this.mSecondScale.calcCompleteRange();
        }
        this.mGridLabelRenderer.invalidate(keepLabelsSize, keepViewport);
        postInvalidate();
    }

    /* access modifiers changed from: protected */
    public void drawGraphElements(Canvas canvas) {
        if (VERSION.SDK_INT >= 11 && !canvas.isHardwareAccelerated()) {
            Log.w("GraphView", "GraphView should be used in hardware accelerated mode.You can use android:hardwareAccelerated=\"true\" on your activity. Read this for more info:https://developer.android.com/guide/topics/graphics/hardware-accel.html");
        }
        drawTitle(canvas);
        this.mViewport.drawFirst(canvas);
        this.mGridLabelRenderer.draw(canvas);
        for (Series s : this.mSeries) {
            s.draw(this, canvas, false);
        }
        if (this.mSecondScale != null) {
            for (Series s2 : this.mSecondScale.getSeries()) {
                s2.draw(this, canvas, true);
            }
        }
        if (this.mCursorMode != null) {
            this.mCursorMode.draw(canvas);
        }
        this.mViewport.draw(canvas);
        this.mLegendRenderer.draw(canvas);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            canvas.drawColor(Color.rgb(Callback.DEFAULT_DRAG_ANIMATION_DURATION, Callback.DEFAULT_DRAG_ANIMATION_DURATION, Callback.DEFAULT_DRAG_ANIMATION_DURATION));
            canvas.drawText("GraphView: No Preview available", (float) (canvas.getWidth() / 2), (float) (canvas.getHeight() / 2), this.mPreviewPaint);
            return;
        }
        drawGraphElements(canvas);
    }

    /* access modifiers changed from: protected */
    public void drawTitle(Canvas canvas) {
        if (this.mTitle != null && this.mTitle.length() > 0) {
            this.mPaintTitle.setColor(this.mStyles.titleColor);
            this.mPaintTitle.setTextSize(this.mStyles.titleTextSize);
            this.mPaintTitle.setTextAlign(Align.CENTER);
            canvas.drawText(this.mTitle, (float) (canvas.getWidth() / 2), this.mPaintTitle.getTextSize(), this.mPaintTitle);
        }
    }

    /* access modifiers changed from: protected */
    public int getTitleHeight() {
        if (this.mTitle == null || this.mTitle.length() <= 0) {
            return 0;
        }
        return (int) this.mPaintTitle.getTextSize();
    }

    public Viewport getViewport() {
        return this.mViewport;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        onDataChanged(false, false);
    }

    public int getGraphContentLeft() {
        return getGridLabelRenderer().getLabelVerticalWidth() + getGridLabelRenderer().getStyles().padding + getGridLabelRenderer().getVerticalAxisTitleWidth();
    }

    public int getGraphContentTop() {
        return getGridLabelRenderer().getStyles().padding + getTitleHeight();
    }

    public int getGraphContentHeight() {
        return (((getHeight() - (getGridLabelRenderer().getStyles().padding * 2)) - getGridLabelRenderer().getLabelHorizontalHeight()) - getTitleHeight()) - getGridLabelRenderer().getHorizontalAxisTitleHeight();
    }

    public int getGraphContentWidth() {
        int graphwidth = (getWidth() - (getGridLabelRenderer().getStyles().padding * 2)) - getGridLabelRenderer().getLabelVerticalWidth();
        if (this.mSecondScale != null) {
            return (int) (((float) (graphwidth - getGridLabelRenderer().getLabelVerticalSecondScaleWidth())) - this.mSecondScale.getVerticalAxisTitleTextSize());
        }
        return graphwidth;
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean b = this.mViewport.onTouchEvent(event);
        boolean a = super.onTouchEvent(event);
        if (this.mTapDetector.onTouchEvent(event)) {
            for (Series s : this.mSeries) {
                s.onTap(event.getX(), event.getY());
            }
            if (this.mSecondScale != null) {
                for (Series s2 : this.mSecondScale.getSeries()) {
                    s2.onTap(event.getX(), event.getY());
                }
            }
        }
        return b || a;
    }

    public void computeScroll() {
        super.computeScroll();
        this.mViewport.computeScroll();
    }

    public LegendRenderer getLegendRenderer() {
        return this.mLegendRenderer;
    }

    public void setLegendRenderer(LegendRenderer mLegendRenderer2) {
        this.mLegendRenderer = mLegendRenderer2;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String mTitle2) {
        this.mTitle = mTitle2;
    }

    public float getTitleTextSize() {
        return this.mStyles.titleTextSize;
    }

    public void setTitleTextSize(float titleTextSize) {
        this.mStyles.titleTextSize = titleTextSize;
    }

    public int getTitleColor() {
        return this.mStyles.titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.mStyles.titleColor = titleColor;
    }

    public SecondScale getSecondScale() {
        if (this.mSecondScale == null) {
            this.mSecondScale = new SecondScale(this);
            this.mSecondScale.setVerticalAxisTitleTextSize(this.mGridLabelRenderer.mStyles.textSize);
        }
        return this.mSecondScale;
    }

    public void clearSecondScale() {
        if (this.mSecondScale != null) {
            this.mSecondScale.removeAllSeries();
            this.mSecondScale = null;
        }
    }

    public void removeAllSeries() {
        this.mSeries.clear();
        onDataChanged(false, false);
    }

    public void removeSeries(Series<?> series) {
        this.mSeries.remove(series);
        onDataChanged(false, false);
    }

    public Bitmap takeSnapshot() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        draw(new Canvas(bitmap));
        return bitmap;
    }

    public void takeSnapshotAndShare(Context context, String imageName, String title) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Bitmap inImage = takeSnapshot();
        inImage.compress(CompressFormat.JPEG, 100, bytes);
        String path = Media.insertImage(context.getContentResolver(), inImage, imageName, null);
        if (path == null) {
            throw new SecurityException("Could not get path from MediaStore. Please check permissions.");
        }
        Intent i = new Intent("android.intent.action.SEND");
        i.setType("image/*");
        i.putExtra("android.intent.extra.STREAM", Uri.parse(path));
        try {
            context.startActivity(Intent.createChooser(i, title));
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void setCursorMode(boolean b) {
        this.mIsCursorMode = b;
        if (!this.mIsCursorMode) {
            this.mCursorMode = null;
            invalidate();
        } else if (this.mCursorMode == null) {
            this.mCursorMode = new CursorMode(this);
        }
        for (Series series : this.mSeries) {
            if (series instanceof BaseSeries) {
                ((BaseSeries) series).clearCursorModeCache();
            }
        }
    }

    public CursorMode getCursorMode() {
        return this.mCursorMode;
    }

    public boolean isCursorMode() {
        return this.mIsCursorMode;
    }
}
