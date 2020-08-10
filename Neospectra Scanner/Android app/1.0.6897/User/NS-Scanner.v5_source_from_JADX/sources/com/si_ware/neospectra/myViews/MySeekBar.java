package com.si_ware.neospectra.myViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.p004v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class MySeekBar extends AppCompatSeekBar {
    private List<ProgressItem> mProgressItemsList;
    View thumbView;

    public MySeekBar(Context context) {
        super(context);
    }

    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
    }

    public void initData(ArrayList<ProgressItem> progressItemsList) {
        this.mProgressItemsList = progressItemsList;
    }

    /* access modifiers changed from: protected */
    public synchronized void onDraw(@NonNull Canvas canvas) {
        int progressBarWidth = getWidth();
        int progressBarHeight = getHeight();
        int thumboffset = getThumbOffset();
        int lastProgressX = 0;
        for (int i = 0; i < this.mProgressItemsList.size(); i++) {
            ProgressItem progressItem = (ProgressItem) this.mProgressItemsList.get(i);
            Paint progressPaint = new Paint();
            progressPaint.setColor(getResources().getColor(progressItem.color));
            int progressItemRight = lastProgressX + ((int) ((progressItem.progressItemPercentage * ((float) progressBarWidth)) / 100.0f));
            if (i == this.mProgressItemsList.size() - 1 && progressItemRight != progressBarWidth) {
                progressItemRight = progressBarWidth;
            }
            Rect progressRect = new Rect();
            progressRect.set(lastProgressX, thumboffset / 2, progressItemRight, progressBarHeight - (thumboffset / 2));
            canvas.drawRect(progressRect, progressPaint);
            lastProgressX = progressItemRight;
        }
        super.onDraw(canvas);
    }
}
