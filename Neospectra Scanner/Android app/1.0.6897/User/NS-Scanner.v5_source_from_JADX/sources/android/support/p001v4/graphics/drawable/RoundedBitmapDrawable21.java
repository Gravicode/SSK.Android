package android.support.p001v4.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.graphics.Rect;
import android.support.annotation.RequiresApi;
import android.view.Gravity;

@RequiresApi(21)
/* renamed from: android.support.v4.graphics.drawable.RoundedBitmapDrawable21 */
class RoundedBitmapDrawable21 extends RoundedBitmapDrawable {
    protected RoundedBitmapDrawable21(Resources res, Bitmap bitmap) {
        super(res, bitmap);
    }

    public void getOutline(Outline outline) {
        updateDstRect();
        outline.setRoundRect(this.mDstRect, getCornerRadius());
    }

    public void setMipMap(boolean mipMap) {
        if (this.mBitmap != null) {
            this.mBitmap.setHasMipMap(mipMap);
            invalidateSelf();
        }
    }

    public boolean hasMipMap() {
        return this.mBitmap != null && this.mBitmap.hasMipMap();
    }

    /* access modifiers changed from: 0000 */
    public void gravityCompatApply(int gravity, int bitmapWidth, int bitmapHeight, Rect bounds, Rect outRect) {
        Gravity.apply(gravity, bitmapWidth, bitmapHeight, bounds, outRect, 0);
    }
}
