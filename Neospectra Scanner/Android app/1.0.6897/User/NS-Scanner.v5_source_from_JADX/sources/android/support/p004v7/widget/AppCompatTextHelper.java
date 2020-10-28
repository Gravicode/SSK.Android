package android.support.p004v7.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p001v4.content.res.ResourcesCompat.FontCallback;
import android.support.p001v4.widget.AutoSizeableTextView;
import android.support.p004v7.appcompat.C1123R;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;
import java.lang.ref.WeakReference;

@RequiresApi(9)
/* renamed from: android.support.v7.widget.AppCompatTextHelper */
class AppCompatTextHelper {
    private static final int MONOSPACE = 3;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private boolean mAsyncFontPending;
    @NonNull
    private final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
    private TintInfo mDrawableBottomTint;
    private TintInfo mDrawableLeftTint;
    private TintInfo mDrawableRightTint;
    private TintInfo mDrawableTopTint;
    private Typeface mFontTypeface;
    private int mStyle = 0;
    final TextView mView;

    static AppCompatTextHelper create(TextView textView) {
        if (VERSION.SDK_INT >= 17) {
            return new AppCompatTextHelperV17(textView);
        }
        return new AppCompatTextHelper(textView);
    }

    AppCompatTextHelper(TextView view) {
        this.mView = view;
        this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(this.mView);
    }

    /* access modifiers changed from: 0000 */
    @SuppressLint({"NewApi"})
    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        AttributeSet attributeSet = attrs;
        int i = defStyleAttr;
        Context context = this.mView.getContext();
        AppCompatDrawableManager drawableManager = AppCompatDrawableManager.get();
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attributeSet, C1123R.styleable.AppCompatTextHelper, i, 0);
        int ap = a.getResourceId(C1123R.styleable.AppCompatTextHelper_android_textAppearance, -1);
        if (a.hasValue(C1123R.styleable.AppCompatTextHelper_android_drawableLeft)) {
            this.mDrawableLeftTint = createTintInfo(context, drawableManager, a.getResourceId(C1123R.styleable.AppCompatTextHelper_android_drawableLeft, 0));
        }
        if (a.hasValue(C1123R.styleable.AppCompatTextHelper_android_drawableTop)) {
            this.mDrawableTopTint = createTintInfo(context, drawableManager, a.getResourceId(C1123R.styleable.AppCompatTextHelper_android_drawableTop, 0));
        }
        if (a.hasValue(C1123R.styleable.AppCompatTextHelper_android_drawableRight)) {
            this.mDrawableRightTint = createTintInfo(context, drawableManager, a.getResourceId(C1123R.styleable.AppCompatTextHelper_android_drawableRight, 0));
        }
        if (a.hasValue(C1123R.styleable.AppCompatTextHelper_android_drawableBottom)) {
            this.mDrawableBottomTint = createTintInfo(context, drawableManager, a.getResourceId(C1123R.styleable.AppCompatTextHelper_android_drawableBottom, 0));
        }
        a.recycle();
        boolean hasPwdTm = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
        boolean allCaps = false;
        boolean allCapsSet = false;
        ColorStateList textColor = null;
        ColorStateList textColorHint = null;
        ColorStateList textColorLink = null;
        if (ap != -1) {
            TintTypedArray a2 = TintTypedArray.obtainStyledAttributes(context, ap, C1123R.styleable.TextAppearance);
            if (!hasPwdTm && a2.hasValue(C1123R.styleable.TextAppearance_textAllCaps)) {
                allCaps = a2.getBoolean(C1123R.styleable.TextAppearance_textAllCaps, false);
                allCapsSet = true;
            }
            updateTypefaceAndStyle(context, a2);
            if (VERSION.SDK_INT < 23) {
                if (a2.hasValue(C1123R.styleable.TextAppearance_android_textColor)) {
                    textColor = a2.getColorStateList(C1123R.styleable.TextAppearance_android_textColor);
                }
                if (a2.hasValue(C1123R.styleable.TextAppearance_android_textColorHint)) {
                    textColorHint = a2.getColorStateList(C1123R.styleable.TextAppearance_android_textColorHint);
                }
                if (a2.hasValue(C1123R.styleable.TextAppearance_android_textColorLink)) {
                    textColorLink = a2.getColorStateList(C1123R.styleable.TextAppearance_android_textColorLink);
                }
            }
            a2.recycle();
        }
        TintTypedArray a3 = TintTypedArray.obtainStyledAttributes(context, attributeSet, C1123R.styleable.TextAppearance, i, 0);
        if (!hasPwdTm && a3.hasValue(C1123R.styleable.TextAppearance_textAllCaps)) {
            allCapsSet = true;
            allCaps = a3.getBoolean(C1123R.styleable.TextAppearance_textAllCaps, false);
        }
        if (VERSION.SDK_INT < 23) {
            if (a3.hasValue(C1123R.styleable.TextAppearance_android_textColor)) {
                textColor = a3.getColorStateList(C1123R.styleable.TextAppearance_android_textColor);
            }
            if (a3.hasValue(C1123R.styleable.TextAppearance_android_textColorHint)) {
                textColorHint = a3.getColorStateList(C1123R.styleable.TextAppearance_android_textColorHint);
            }
            if (a3.hasValue(C1123R.styleable.TextAppearance_android_textColorLink)) {
                textColorLink = a3.getColorStateList(C1123R.styleable.TextAppearance_android_textColorLink);
            }
        }
        updateTypefaceAndStyle(context, a3);
        a3.recycle();
        if (textColor != null) {
            this.mView.setTextColor(textColor);
        }
        if (textColorHint != null) {
            this.mView.setHintTextColor(textColorHint);
        }
        if (textColorLink != null) {
            this.mView.setLinkTextColor(textColorLink);
        }
        if (!hasPwdTm && allCapsSet) {
            setAllCaps(allCaps);
        }
        if (this.mFontTypeface != null) {
            this.mView.setTypeface(this.mFontTypeface, this.mStyle);
        }
        this.mAutoSizeTextHelper.loadFromAttributes(attributeSet, i);
        if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && this.mAutoSizeTextHelper.getAutoSizeTextType() != 0) {
            int[] autoSizeTextSizesInPx = this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
            if (autoSizeTextSizesInPx.length > 0) {
                if (((float) this.mView.getAutoSizeStepGranularity()) != -1.0f) {
                    Context context2 = context;
                    this.mView.setAutoSizeTextTypeUniformWithConfiguration(this.mAutoSizeTextHelper.getAutoSizeMinTextSize(), this.mAutoSizeTextHelper.getAutoSizeMaxTextSize(), this.mAutoSizeTextHelper.getAutoSizeStepGranularity(), 0);
                    return;
                }
                Context context3 = context;
                this.mView.setAutoSizeTextTypeUniformWithPresetSizes(autoSizeTextSizesInPx, 0);
                return;
            }
        }
    }

    private void updateTypefaceAndStyle(Context context, TintTypedArray a) {
        this.mStyle = a.getInt(C1123R.styleable.TextAppearance_android_textStyle, this.mStyle);
        boolean z = true;
        if (a.hasValue(C1123R.styleable.TextAppearance_android_fontFamily) || a.hasValue(C1123R.styleable.TextAppearance_fontFamily)) {
            this.mFontTypeface = null;
            int fontFamilyId = a.hasValue(C1123R.styleable.TextAppearance_fontFamily) ? C1123R.styleable.TextAppearance_fontFamily : C1123R.styleable.TextAppearance_android_fontFamily;
            if (!context.isRestricted()) {
                final WeakReference<TextView> textViewWeak = new WeakReference<>(this.mView);
                try {
                    this.mFontTypeface = a.getFont(fontFamilyId, this.mStyle, new FontCallback() {
                        public void onFontRetrieved(@NonNull Typeface typeface) {
                            AppCompatTextHelper.this.onAsyncTypefaceReceived(textViewWeak, typeface);
                        }

                        public void onFontRetrievalFailed(int reason) {
                        }
                    });
                    if (this.mFontTypeface != null) {
                        z = false;
                    }
                    this.mAsyncFontPending = z;
                } catch (NotFoundException | UnsupportedOperationException e) {
                }
            }
            if (this.mFontTypeface == null) {
                String fontFamilyName = a.getString(fontFamilyId);
                if (fontFamilyName != null) {
                    this.mFontTypeface = Typeface.create(fontFamilyName, this.mStyle);
                }
            }
            return;
        }
        if (a.hasValue(C1123R.styleable.TextAppearance_android_typeface)) {
            this.mAsyncFontPending = false;
            switch (a.getInt(C1123R.styleable.TextAppearance_android_typeface, 1)) {
                case 1:
                    this.mFontTypeface = Typeface.SANS_SERIF;
                    break;
                case 2:
                    this.mFontTypeface = Typeface.SERIF;
                    break;
                case 3:
                    this.mFontTypeface = Typeface.MONOSPACE;
                    break;
            }
        }
    }

    /* access modifiers changed from: private */
    public void onAsyncTypefaceReceived(WeakReference<TextView> textViewWeak, Typeface typeface) {
        if (this.mAsyncFontPending) {
            this.mFontTypeface = typeface;
            TextView textView = (TextView) textViewWeak.get();
            if (textView != null) {
                textView.setTypeface(typeface, this.mStyle);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void onSetTextAppearance(Context context, int resId) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, resId, C1123R.styleable.TextAppearance);
        if (a.hasValue(C1123R.styleable.TextAppearance_textAllCaps)) {
            setAllCaps(a.getBoolean(C1123R.styleable.TextAppearance_textAllCaps, false));
        }
        if (VERSION.SDK_INT < 23 && a.hasValue(C1123R.styleable.TextAppearance_android_textColor)) {
            ColorStateList textColor = a.getColorStateList(C1123R.styleable.TextAppearance_android_textColor);
            if (textColor != null) {
                this.mView.setTextColor(textColor);
            }
        }
        updateTypefaceAndStyle(context, a);
        a.recycle();
        if (this.mFontTypeface != null) {
            this.mView.setTypeface(this.mFontTypeface, this.mStyle);
        }
    }

    /* access modifiers changed from: 0000 */
    public void setAllCaps(boolean allCaps) {
        this.mView.setAllCaps(allCaps);
    }

    /* access modifiers changed from: 0000 */
    public void applyCompoundDrawablesTints() {
        if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
            Drawable[] compoundDrawables = this.mView.getCompoundDrawables();
            applyCompoundDrawableTint(compoundDrawables[0], this.mDrawableLeftTint);
            applyCompoundDrawableTint(compoundDrawables[1], this.mDrawableTopTint);
            applyCompoundDrawableTint(compoundDrawables[2], this.mDrawableRightTint);
            applyCompoundDrawableTint(compoundDrawables[3], this.mDrawableBottomTint);
        }
    }

    /* access modifiers changed from: 0000 */
    public final void applyCompoundDrawableTint(Drawable drawable, TintInfo info) {
        if (drawable != null && info != null) {
            AppCompatDrawableManager.tintDrawable(drawable, info, this.mView.getDrawableState());
        }
    }

    protected static TintInfo createTintInfo(Context context, AppCompatDrawableManager drawableManager, int drawableId) {
        ColorStateList tintList = drawableManager.getTintList(context, drawableId);
        if (tintList == null) {
            return null;
        }
        TintInfo tintInfo = new TintInfo();
        tintInfo.mHasTintList = true;
        tintInfo.mTintList = tintList;
        return tintInfo;
    }

    /* access modifiers changed from: 0000 */
    @RestrictTo({Scope.LIBRARY_GROUP})
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            autoSizeText();
        }
    }

    /* access modifiers changed from: 0000 */
    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setTextSize(int unit, float size) {
        if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && !isAutoSizeEnabled()) {
            setTextSizeInternal(unit, size);
        }
    }

    /* access modifiers changed from: 0000 */
    @RestrictTo({Scope.LIBRARY_GROUP})
    public void autoSizeText() {
        this.mAutoSizeTextHelper.autoSizeText();
    }

    /* access modifiers changed from: 0000 */
    @RestrictTo({Scope.LIBRARY_GROUP})
    public boolean isAutoSizeEnabled() {
        return this.mAutoSizeTextHelper.isAutoSizeEnabled();
    }

    private void setTextSizeInternal(int unit, float size) {
        this.mAutoSizeTextHelper.setTextSizeInternal(unit, size);
    }

    /* access modifiers changed from: 0000 */
    public void setAutoSizeTextTypeWithDefaults(int autoSizeTextType) {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeWithDefaults(autoSizeTextType);
    }

    /* access modifiers changed from: 0000 */
    public void setAutoSizeTextTypeUniformWithConfiguration(int autoSizeMinTextSize, int autoSizeMaxTextSize, int autoSizeStepGranularity, int unit) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithConfiguration(autoSizeMinTextSize, autoSizeMaxTextSize, autoSizeStepGranularity, unit);
    }

    /* access modifiers changed from: 0000 */
    public void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull int[] presetSizes, int unit) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(presetSizes, unit);
    }

    /* access modifiers changed from: 0000 */
    public int getAutoSizeTextType() {
        return this.mAutoSizeTextHelper.getAutoSizeTextType();
    }

    /* access modifiers changed from: 0000 */
    public int getAutoSizeStepGranularity() {
        return this.mAutoSizeTextHelper.getAutoSizeStepGranularity();
    }

    /* access modifiers changed from: 0000 */
    public int getAutoSizeMinTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMinTextSize();
    }

    /* access modifiers changed from: 0000 */
    public int getAutoSizeMaxTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMaxTextSize();
    }

    /* access modifiers changed from: 0000 */
    public int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
    }
}