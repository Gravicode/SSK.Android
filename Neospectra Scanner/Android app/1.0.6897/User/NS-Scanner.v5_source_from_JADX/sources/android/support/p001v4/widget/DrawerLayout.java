package android.support.p001v4.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p001v4.content.ContextCompat;
import android.support.p001v4.graphics.drawable.DrawableCompat;
import android.support.p001v4.view.AbsSavedState;
import android.support.p001v4.view.AccessibilityDelegateCompat;
import android.support.p001v4.view.GravityCompat;
import android.support.p001v4.view.ViewCompat;
import android.support.p001v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.p001v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat;
import android.support.p001v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnApplyWindowInsetsListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/* renamed from: android.support.v4.widget.DrawerLayout */
public class DrawerLayout extends ViewGroup {
    private static final boolean ALLOW_EDGE_LOCK = false;
    static final boolean CAN_HIDE_DESCENDANTS = (VERSION.SDK_INT >= 19);
    private static final boolean CHILDREN_DISALLOW_INTERCEPT = true;
    private static final int DEFAULT_SCRIM_COLOR = -1728053248;
    private static final int DRAWER_ELEVATION = 10;
    static final int[] LAYOUT_ATTRS = {16842931};
    public static final int LOCK_MODE_LOCKED_CLOSED = 1;
    public static final int LOCK_MODE_LOCKED_OPEN = 2;
    public static final int LOCK_MODE_UNDEFINED = 3;
    public static final int LOCK_MODE_UNLOCKED = 0;
    private static final int MIN_DRAWER_MARGIN = 64;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final int PEEK_DELAY = 160;
    private static final boolean SET_DRAWER_SHADOW_FROM_ELEVATION;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "DrawerLayout";
    private static final int[] THEME_ATTRS = {16843828};
    private static final float TOUCH_SLOP_SENSITIVITY = 1.0f;
    private final ChildAccessibilityDelegate mChildAccessibilityDelegate;
    private boolean mChildrenCanceledTouch;
    private boolean mDisallowInterceptRequested;
    private boolean mDrawStatusBarBackground;
    private float mDrawerElevation;
    private int mDrawerState;
    private boolean mFirstLayout;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private Object mLastInsets;
    private final ViewDragCallback mLeftCallback;
    private final ViewDragHelper mLeftDragger;
    @Nullable
    private DrawerListener mListener;
    private List<DrawerListener> mListeners;
    private int mLockModeEnd;
    private int mLockModeLeft;
    private int mLockModeRight;
    private int mLockModeStart;
    private int mMinDrawerMargin;
    private final ArrayList<View> mNonDrawerViews;
    private final ViewDragCallback mRightCallback;
    private final ViewDragHelper mRightDragger;
    private int mScrimColor;
    private float mScrimOpacity;
    private Paint mScrimPaint;
    private Drawable mShadowEnd;
    private Drawable mShadowLeft;
    private Drawable mShadowLeftResolved;
    private Drawable mShadowRight;
    private Drawable mShadowRightResolved;
    private Drawable mShadowStart;
    private Drawable mStatusBarBackground;
    private CharSequence mTitleLeft;
    private CharSequence mTitleRight;

    /* renamed from: android.support.v4.widget.DrawerLayout$AccessibilityDelegate */
    class AccessibilityDelegate extends AccessibilityDelegateCompat {
        private final Rect mTmpRect = new Rect();

        AccessibilityDelegate() {
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            if (DrawerLayout.CAN_HIDE_DESCENDANTS) {
                super.onInitializeAccessibilityNodeInfo(host, info);
            } else {
                AccessibilityNodeInfoCompat superNode = AccessibilityNodeInfoCompat.obtain(info);
                super.onInitializeAccessibilityNodeInfo(host, superNode);
                info.setSource(host);
                ViewParent parent = ViewCompat.getParentForAccessibility(host);
                if (parent instanceof View) {
                    info.setParent((View) parent);
                }
                copyNodeInfoNoChildren(info, superNode);
                superNode.recycle();
                addChildrenForAccessibility(info, (ViewGroup) host);
            }
            info.setClassName(DrawerLayout.class.getName());
            info.setFocusable(false);
            info.setFocused(false);
            info.removeAction(AccessibilityActionCompat.ACTION_FOCUS);
            info.removeAction(AccessibilityActionCompat.ACTION_CLEAR_FOCUS);
        }

        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(DrawerLayout.class.getName());
        }

        public boolean dispatchPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
            if (event.getEventType() != 32) {
                return super.dispatchPopulateAccessibilityEvent(host, event);
            }
            List<CharSequence> eventText = event.getText();
            View visibleDrawer = DrawerLayout.this.findVisibleDrawer();
            if (visibleDrawer != null) {
                CharSequence title = DrawerLayout.this.getDrawerTitle(DrawerLayout.this.getDrawerViewAbsoluteGravity(visibleDrawer));
                if (title != null) {
                    eventText.add(title);
                }
            }
            return true;
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
            if (DrawerLayout.CAN_HIDE_DESCENDANTS || DrawerLayout.includeChildForAccessibility(child)) {
                return super.onRequestSendAccessibilityEvent(host, child, event);
            }
            return false;
        }

        private void addChildrenForAccessibility(AccessibilityNodeInfoCompat info, ViewGroup v) {
            int childCount = v.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = v.getChildAt(i);
                if (DrawerLayout.includeChildForAccessibility(child)) {
                    info.addChild(child);
                }
            }
        }

        private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat dest, AccessibilityNodeInfoCompat src) {
            Rect rect = this.mTmpRect;
            src.getBoundsInParent(rect);
            dest.setBoundsInParent(rect);
            src.getBoundsInScreen(rect);
            dest.setBoundsInScreen(rect);
            dest.setVisibleToUser(src.isVisibleToUser());
            dest.setPackageName(src.getPackageName());
            dest.setClassName(src.getClassName());
            dest.setContentDescription(src.getContentDescription());
            dest.setEnabled(src.isEnabled());
            dest.setClickable(src.isClickable());
            dest.setFocusable(src.isFocusable());
            dest.setFocused(src.isFocused());
            dest.setAccessibilityFocused(src.isAccessibilityFocused());
            dest.setSelected(src.isSelected());
            dest.setLongClickable(src.isLongClickable());
            dest.addAction(src.getActions());
        }
    }

    /* renamed from: android.support.v4.widget.DrawerLayout$ChildAccessibilityDelegate */
    static final class ChildAccessibilityDelegate extends AccessibilityDelegateCompat {
        ChildAccessibilityDelegate() {
        }

        public void onInitializeAccessibilityNodeInfo(View child, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(child, info);
            if (!DrawerLayout.includeChildForAccessibility(child)) {
                info.setParent(null);
            }
        }
    }

    /* renamed from: android.support.v4.widget.DrawerLayout$DrawerListener */
    public interface DrawerListener {
        void onDrawerClosed(@NonNull View view);

        void onDrawerOpened(@NonNull View view);

        void onDrawerSlide(@NonNull View view, float f);

        void onDrawerStateChanged(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* renamed from: android.support.v4.widget.DrawerLayout$EdgeGravity */
    private @interface EdgeGravity {
    }

    /* renamed from: android.support.v4.widget.DrawerLayout$LayoutParams */
    public static class LayoutParams extends MarginLayoutParams {
        private static final int FLAG_IS_CLOSING = 4;
        private static final int FLAG_IS_OPENED = 1;
        private static final int FLAG_IS_OPENING = 2;
        public int gravity;
        boolean isPeeking;
        float onScreen;
        int openState;

        public LayoutParams(@NonNull Context c, @Nullable AttributeSet attrs) {
            super(c, attrs);
            this.gravity = 0;
            TypedArray a = c.obtainStyledAttributes(attrs, DrawerLayout.LAYOUT_ATTRS);
            this.gravity = a.getInt(0, 0);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.gravity = 0;
        }

        public LayoutParams(int width, int height, int gravity2) {
            this(width, height);
            this.gravity = gravity2;
        }

        public LayoutParams(@NonNull LayoutParams source) {
            super(source);
            this.gravity = 0;
            this.gravity = source.gravity;
        }

        public LayoutParams(@NonNull android.view.ViewGroup.LayoutParams source) {
            super(source);
            this.gravity = 0;
        }

        public LayoutParams(@NonNull MarginLayoutParams source) {
            super(source);
            this.gravity = 0;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    /* renamed from: android.support.v4.widget.DrawerLayout$LockMode */
    private @interface LockMode {
    }

    /* renamed from: android.support.v4.widget.DrawerLayout$SavedState */
    protected static class SavedState extends AbsSavedState {
        public static final Creator<SavedState> CREATOR = new ClassLoaderCreator<SavedState>() {
            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int lockModeEnd;
        int lockModeLeft;
        int lockModeRight;
        int lockModeStart;
        int openDrawerGravity = 0;

        public SavedState(@NonNull Parcel in, @Nullable ClassLoader loader) {
            super(in, loader);
            this.openDrawerGravity = in.readInt();
            this.lockModeLeft = in.readInt();
            this.lockModeRight = in.readInt();
            this.lockModeStart = in.readInt();
            this.lockModeEnd = in.readInt();
        }

        public SavedState(@NonNull Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.openDrawerGravity);
            dest.writeInt(this.lockModeLeft);
            dest.writeInt(this.lockModeRight);
            dest.writeInt(this.lockModeStart);
            dest.writeInt(this.lockModeEnd);
        }
    }

    /* renamed from: android.support.v4.widget.DrawerLayout$SimpleDrawerListener */
    public static abstract class SimpleDrawerListener implements DrawerListener {
        public void onDrawerSlide(View drawerView, float slideOffset) {
        }

        public void onDrawerOpened(View drawerView) {
        }

        public void onDrawerClosed(View drawerView) {
        }

        public void onDrawerStateChanged(int newState) {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    /* renamed from: android.support.v4.widget.DrawerLayout$State */
    private @interface State {
    }

    /* renamed from: android.support.v4.widget.DrawerLayout$ViewDragCallback */
    private class ViewDragCallback extends Callback {
        private final int mAbsGravity;
        private ViewDragHelper mDragger;
        private final Runnable mPeekRunnable = new Runnable() {
            public void run() {
                ViewDragCallback.this.peekDrawer();
            }
        };

        ViewDragCallback(int gravity) {
            this.mAbsGravity = gravity;
        }

        public void setDragger(ViewDragHelper dragger) {
            this.mDragger = dragger;
        }

        public void removeCallbacks() {
            DrawerLayout.this.removeCallbacks(this.mPeekRunnable);
        }

        public boolean tryCaptureView(View child, int pointerId) {
            return DrawerLayout.this.isDrawerView(child) && DrawerLayout.this.checkDrawerViewAbsoluteGravity(child, this.mAbsGravity) && DrawerLayout.this.getDrawerLockMode(child) == 0;
        }

        public void onViewDragStateChanged(int state) {
            DrawerLayout.this.updateDrawerState(this.mAbsGravity, state, this.mDragger.getCapturedView());
        }

        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            float offset;
            int childWidth = changedView.getWidth();
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(changedView, 3)) {
                offset = ((float) (childWidth + left)) / ((float) childWidth);
            } else {
                offset = ((float) (DrawerLayout.this.getWidth() - left)) / ((float) childWidth);
            }
            DrawerLayout.this.setDrawerViewOffset(changedView, offset);
            changedView.setVisibility(offset == 0.0f ? 4 : 0);
            DrawerLayout.this.invalidate();
        }

        public void onViewCaptured(View capturedChild, int activePointerId) {
            ((LayoutParams) capturedChild.getLayoutParams()).isPeeking = false;
            closeOtherDrawer();
        }

        private void closeOtherDrawer() {
            int i = 3;
            if (this.mAbsGravity == 3) {
                i = 5;
            }
            View toClose = DrawerLayout.this.findDrawerWithGravity(i);
            if (toClose != null) {
                DrawerLayout.this.closeDrawer(toClose);
            }
        }

        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int left;
            float offset = DrawerLayout.this.getDrawerViewOffset(releasedChild);
            int childWidth = releasedChild.getWidth();
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(releasedChild, 3)) {
                left = (xvel > 0.0f || (xvel == 0.0f && offset > 0.5f)) ? 0 : -childWidth;
            } else {
                int width = DrawerLayout.this.getWidth();
                left = (xvel < 0.0f || (xvel == 0.0f && offset > 0.5f)) ? width - childWidth : width;
            }
            this.mDragger.settleCapturedViewAt(left, releasedChild.getTop());
            DrawerLayout.this.invalidate();
        }

        public void onEdgeTouched(int edgeFlags, int pointerId) {
            DrawerLayout.this.postDelayed(this.mPeekRunnable, 160);
        }

        /* access modifiers changed from: 0000 */
        public void peekDrawer() {
            View toCapture;
            int childLeft;
            int peekDistance = this.mDragger.getEdgeSize();
            int i = 0;
            boolean leftEdge = this.mAbsGravity == 3;
            if (leftEdge) {
                toCapture = DrawerLayout.this.findDrawerWithGravity(3);
                if (toCapture != null) {
                    i = -toCapture.getWidth();
                }
                childLeft = i + peekDistance;
            } else {
                toCapture = DrawerLayout.this.findDrawerWithGravity(5);
                childLeft = DrawerLayout.this.getWidth() - peekDistance;
            }
            if (toCapture == null) {
                return;
            }
            if (((leftEdge && toCapture.getLeft() < childLeft) || (!leftEdge && toCapture.getLeft() > childLeft)) && DrawerLayout.this.getDrawerLockMode(toCapture) == 0) {
                LayoutParams lp = (LayoutParams) toCapture.getLayoutParams();
                this.mDragger.smoothSlideViewTo(toCapture, childLeft, toCapture.getTop());
                lp.isPeeking = true;
                DrawerLayout.this.invalidate();
                closeOtherDrawer();
                DrawerLayout.this.cancelChildViewTouch();
            }
        }

        public boolean onEdgeLock(int edgeFlags) {
            return false;
        }

        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            View toCapture;
            if ((edgeFlags & 1) == 1) {
                toCapture = DrawerLayout.this.findDrawerWithGravity(3);
            } else {
                toCapture = DrawerLayout.this.findDrawerWithGravity(5);
            }
            if (toCapture != null && DrawerLayout.this.getDrawerLockMode(toCapture) == 0) {
                this.mDragger.captureChildView(toCapture, pointerId);
            }
        }

        public int getViewHorizontalDragRange(View child) {
            if (DrawerLayout.this.isDrawerView(child)) {
                return child.getWidth();
            }
            return 0;
        }

        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(child, 3)) {
                return Math.max(-child.getWidth(), Math.min(left, 0));
            }
            int width = DrawerLayout.this.getWidth();
            return Math.max(width - child.getWidth(), Math.min(left, width));
        }

        public int clampViewPositionVertical(View child, int top, int dy) {
            return child.getTop();
        }
    }

    static {
        boolean z = true;
        if (VERSION.SDK_INT < 21) {
            z = false;
        }
        SET_DRAWER_SHADOW_FROM_ELEVATION = z;
    }

    public DrawerLayout(@NonNull Context context) {
        this(context, null);
    }

    public DrawerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mChildAccessibilityDelegate = new ChildAccessibilityDelegate();
        this.mScrimColor = DEFAULT_SCRIM_COLOR;
        this.mScrimPaint = new Paint();
        this.mFirstLayout = true;
        this.mLockModeLeft = 3;
        this.mLockModeRight = 3;
        this.mLockModeStart = 3;
        this.mLockModeEnd = 3;
        this.mShadowStart = null;
        this.mShadowEnd = null;
        this.mShadowLeft = null;
        this.mShadowRight = null;
        setDescendantFocusability(262144);
        float density = getResources().getDisplayMetrics().density;
        this.mMinDrawerMargin = (int) ((64.0f * density) + 0.5f);
        float minVel = 400.0f * density;
        this.mLeftCallback = new ViewDragCallback(3);
        this.mRightCallback = new ViewDragCallback(5);
        this.mLeftDragger = ViewDragHelper.create(this, TOUCH_SLOP_SENSITIVITY, this.mLeftCallback);
        this.mLeftDragger.setEdgeTrackingEnabled(1);
        this.mLeftDragger.setMinVelocity(minVel);
        this.mLeftCallback.setDragger(this.mLeftDragger);
        this.mRightDragger = ViewDragHelper.create(this, TOUCH_SLOP_SENSITIVITY, this.mRightCallback);
        this.mRightDragger.setEdgeTrackingEnabled(2);
        this.mRightDragger.setMinVelocity(minVel);
        this.mRightCallback.setDragger(this.mRightDragger);
        setFocusableInTouchMode(true);
        ViewCompat.setImportantForAccessibility(this, 1);
        ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegate());
        setMotionEventSplittingEnabled(false);
        if (ViewCompat.getFitsSystemWindows(this)) {
            if (VERSION.SDK_INT >= 21) {
                setOnApplyWindowInsetsListener(new OnApplyWindowInsetsListener() {
                    @TargetApi(21)
                    public WindowInsets onApplyWindowInsets(View view, WindowInsets insets) {
                        ((DrawerLayout) view).setChildInsets(insets, insets.getSystemWindowInsetTop() > 0);
                        return insets.consumeSystemWindowInsets();
                    }
                });
                setSystemUiVisibility(1280);
                TypedArray a = context.obtainStyledAttributes(THEME_ATTRS);
                try {
                    this.mStatusBarBackground = a.getDrawable(0);
                } finally {
                    a.recycle();
                }
            } else {
                this.mStatusBarBackground = null;
            }
        }
        this.mDrawerElevation = 10.0f * density;
        this.mNonDrawerViews = new ArrayList<>();
    }

    public void setDrawerElevation(float elevation) {
        this.mDrawerElevation = elevation;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (isDrawerView(child)) {
                ViewCompat.setElevation(child, this.mDrawerElevation);
            }
        }
    }

    public float getDrawerElevation() {
        if (SET_DRAWER_SHADOW_FROM_ELEVATION) {
            return this.mDrawerElevation;
        }
        return 0.0f;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setChildInsets(Object insets, boolean draw) {
        this.mLastInsets = insets;
        this.mDrawStatusBarBackground = draw;
        setWillNotDraw(!draw && getBackground() == null);
        requestLayout();
    }

    public void setDrawerShadow(Drawable shadowDrawable, int gravity) {
        if (!SET_DRAWER_SHADOW_FROM_ELEVATION) {
            if ((gravity & GravityCompat.START) == 8388611) {
                this.mShadowStart = shadowDrawable;
            } else if ((gravity & GravityCompat.END) == 8388613) {
                this.mShadowEnd = shadowDrawable;
            } else if ((gravity & 3) == 3) {
                this.mShadowLeft = shadowDrawable;
            } else if ((gravity & 5) == 5) {
                this.mShadowRight = shadowDrawable;
            } else {
                return;
            }
            resolveShadowDrawables();
            invalidate();
        }
    }

    public void setDrawerShadow(@DrawableRes int resId, int gravity) {
        setDrawerShadow(ContextCompat.getDrawable(getContext(), resId), gravity);
    }

    public void setScrimColor(@ColorInt int color) {
        this.mScrimColor = color;
        invalidate();
    }

    @Deprecated
    public void setDrawerListener(DrawerListener listener) {
        if (this.mListener != null) {
            removeDrawerListener(this.mListener);
        }
        if (listener != null) {
            addDrawerListener(listener);
        }
        this.mListener = listener;
    }

    public void addDrawerListener(@NonNull DrawerListener listener) {
        if (listener != null) {
            if (this.mListeners == null) {
                this.mListeners = new ArrayList();
            }
            this.mListeners.add(listener);
        }
    }

    public void removeDrawerListener(@NonNull DrawerListener listener) {
        if (listener != null && this.mListeners != null) {
            this.mListeners.remove(listener);
        }
    }

    public void setDrawerLockMode(int lockMode) {
        setDrawerLockMode(lockMode, 3);
        setDrawerLockMode(lockMode, 5);
    }

    public void setDrawerLockMode(int lockMode, int edgeGravity) {
        int absGravity = GravityCompat.getAbsoluteGravity(edgeGravity, ViewCompat.getLayoutDirection(this));
        if (edgeGravity == 3) {
            this.mLockModeLeft = lockMode;
        } else if (edgeGravity == 5) {
            this.mLockModeRight = lockMode;
        } else if (edgeGravity == 8388611) {
            this.mLockModeStart = lockMode;
        } else if (edgeGravity == 8388613) {
            this.mLockModeEnd = lockMode;
        }
        if (lockMode != 0) {
            (absGravity == 3 ? this.mLeftDragger : this.mRightDragger).cancel();
        }
        switch (lockMode) {
            case 1:
                View toClose = findDrawerWithGravity(absGravity);
                if (toClose != null) {
                    closeDrawer(toClose);
                    return;
                }
                return;
            case 2:
                View toOpen = findDrawerWithGravity(absGravity);
                if (toOpen != null) {
                    openDrawer(toOpen);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void setDrawerLockMode(int lockMode, @NonNull View drawerView) {
        if (!isDrawerView(drawerView)) {
            StringBuilder sb = new StringBuilder();
            sb.append("View ");
            sb.append(drawerView);
            sb.append(" is not a ");
            sb.append("drawer with appropriate layout_gravity");
            throw new IllegalArgumentException(sb.toString());
        }
        setDrawerLockMode(lockMode, ((LayoutParams) drawerView.getLayoutParams()).gravity);
    }

    public int getDrawerLockMode(int edgeGravity) {
        int layoutDirection = ViewCompat.getLayoutDirection(this);
        if (edgeGravity != 3) {
            if (edgeGravity != 5) {
                if (edgeGravity != 8388611) {
                    if (edgeGravity == 8388613) {
                        if (this.mLockModeEnd != 3) {
                            return this.mLockModeEnd;
                        }
                        int endLockMode = layoutDirection == 0 ? this.mLockModeRight : this.mLockModeLeft;
                        if (endLockMode != 3) {
                            return endLockMode;
                        }
                    }
                } else if (this.mLockModeStart != 3) {
                    return this.mLockModeStart;
                } else {
                    int startLockMode = layoutDirection == 0 ? this.mLockModeLeft : this.mLockModeRight;
                    if (startLockMode != 3) {
                        return startLockMode;
                    }
                }
            } else if (this.mLockModeRight != 3) {
                return this.mLockModeRight;
            } else {
                int rightLockMode = layoutDirection == 0 ? this.mLockModeEnd : this.mLockModeStart;
                if (rightLockMode != 3) {
                    return rightLockMode;
                }
            }
        } else if (this.mLockModeLeft != 3) {
            return this.mLockModeLeft;
        } else {
            int leftLockMode = layoutDirection == 0 ? this.mLockModeStart : this.mLockModeEnd;
            if (leftLockMode != 3) {
                return leftLockMode;
            }
        }
        return 0;
    }

    public int getDrawerLockMode(@NonNull View drawerView) {
        if (isDrawerView(drawerView)) {
            return getDrawerLockMode(((LayoutParams) drawerView.getLayoutParams()).gravity);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("View ");
        sb.append(drawerView);
        sb.append(" is not a drawer");
        throw new IllegalArgumentException(sb.toString());
    }

    public void setDrawerTitle(int edgeGravity, @Nullable CharSequence title) {
        int absGravity = GravityCompat.getAbsoluteGravity(edgeGravity, ViewCompat.getLayoutDirection(this));
        if (absGravity == 3) {
            this.mTitleLeft = title;
        } else if (absGravity == 5) {
            this.mTitleRight = title;
        }
    }

    @Nullable
    public CharSequence getDrawerTitle(int edgeGravity) {
        int absGravity = GravityCompat.getAbsoluteGravity(edgeGravity, ViewCompat.getLayoutDirection(this));
        if (absGravity == 3) {
            return this.mTitleLeft;
        }
        if (absGravity == 5) {
            return this.mTitleRight;
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void updateDrawerState(int forGravity, int activeState, View activeDrawer) {
        int state;
        int leftState = this.mLeftDragger.getViewDragState();
        int rightState = this.mRightDragger.getViewDragState();
        if (leftState == 1 || rightState == 1) {
            state = 1;
        } else if (leftState == 2 || rightState == 2) {
            state = 2;
        } else {
            state = 0;
        }
        if (activeDrawer != null && activeState == 0) {
            LayoutParams lp = (LayoutParams) activeDrawer.getLayoutParams();
            if (lp.onScreen == 0.0f) {
                dispatchOnDrawerClosed(activeDrawer);
            } else if (lp.onScreen == TOUCH_SLOP_SENSITIVITY) {
                dispatchOnDrawerOpened(activeDrawer);
            }
        }
        if (state != this.mDrawerState) {
            this.mDrawerState = state;
            if (this.mListeners != null) {
                for (int i = this.mListeners.size() - 1; i >= 0; i--) {
                    ((DrawerListener) this.mListeners.get(i)).onDrawerStateChanged(state);
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnDrawerClosed(View drawerView) {
        LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
        if ((lp.openState & 1) == 1) {
            lp.openState = 0;
            if (this.mListeners != null) {
                for (int i = this.mListeners.size() - 1; i >= 0; i--) {
                    ((DrawerListener) this.mListeners.get(i)).onDrawerClosed(drawerView);
                }
            }
            updateChildrenImportantForAccessibility(drawerView, false);
            if (hasWindowFocus()) {
                View rootView = getRootView();
                if (rootView != null) {
                    rootView.sendAccessibilityEvent(32);
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnDrawerOpened(View drawerView) {
        LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
        if ((lp.openState & 1) == 0) {
            lp.openState = 1;
            if (this.mListeners != null) {
                for (int i = this.mListeners.size() - 1; i >= 0; i--) {
                    ((DrawerListener) this.mListeners.get(i)).onDrawerOpened(drawerView);
                }
            }
            updateChildrenImportantForAccessibility(drawerView, true);
            if (hasWindowFocus()) {
                sendAccessibilityEvent(32);
            }
        }
    }

    private void updateChildrenImportantForAccessibility(View drawerView, boolean isDrawerOpen) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if ((isDrawerOpen || isDrawerView(child)) && (!isDrawerOpen || child != drawerView)) {
                ViewCompat.setImportantForAccessibility(child, 4);
            } else {
                ViewCompat.setImportantForAccessibility(child, 1);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnDrawerSlide(View drawerView, float slideOffset) {
        if (this.mListeners != null) {
            for (int i = this.mListeners.size() - 1; i >= 0; i--) {
                ((DrawerListener) this.mListeners.get(i)).onDrawerSlide(drawerView, slideOffset);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void setDrawerViewOffset(View drawerView, float slideOffset) {
        LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
        if (slideOffset != lp.onScreen) {
            lp.onScreen = slideOffset;
            dispatchOnDrawerSlide(drawerView, slideOffset);
        }
    }

    /* access modifiers changed from: 0000 */
    public float getDrawerViewOffset(View drawerView) {
        return ((LayoutParams) drawerView.getLayoutParams()).onScreen;
    }

    /* access modifiers changed from: 0000 */
    public int getDrawerViewAbsoluteGravity(View drawerView) {
        return GravityCompat.getAbsoluteGravity(((LayoutParams) drawerView.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(this));
    }

    /* access modifiers changed from: 0000 */
    public boolean checkDrawerViewAbsoluteGravity(View drawerView, int checkFor) {
        return (getDrawerViewAbsoluteGravity(drawerView) & checkFor) == checkFor;
    }

    /* access modifiers changed from: 0000 */
    public View findOpenDrawer() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if ((((LayoutParams) child.getLayoutParams()).openState & 1) == 1) {
                return child;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void moveDrawerToOffset(View drawerView, float slideOffset) {
        int i;
        float oldOffset = getDrawerViewOffset(drawerView);
        int width = drawerView.getWidth();
        int dx = ((int) (((float) width) * slideOffset)) - ((int) (((float) width) * oldOffset));
        if (checkDrawerViewAbsoluteGravity(drawerView, 3)) {
            i = dx;
        } else {
            i = -dx;
        }
        drawerView.offsetLeftAndRight(i);
        setDrawerViewOffset(drawerView, slideOffset);
    }

    /* access modifiers changed from: 0000 */
    public View findDrawerWithGravity(int gravity) {
        int absHorizGravity = GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(this)) & 7;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if ((getDrawerViewAbsoluteGravity(child) & 7) == absHorizGravity) {
                return child;
            }
        }
        return null;
    }

    static String gravityToString(int gravity) {
        if ((gravity & 3) == 3) {
            return "LEFT";
        }
        if ((gravity & 5) == 5) {
            return "RIGHT";
        }
        return Integer.toHexString(gravity);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstLayout = true;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0123  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0145  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onMeasure(int r22, int r23) {
        /*
            r21 = this;
            r0 = r21
            int r1 = android.view.View.MeasureSpec.getMode(r22)
            int r2 = android.view.View.MeasureSpec.getMode(r23)
            int r3 = android.view.View.MeasureSpec.getSize(r22)
            int r4 = android.view.View.MeasureSpec.getSize(r23)
            r5 = 1073741824(0x40000000, float:2.0)
            if (r1 != r5) goto L_0x0018
            if (r2 == r5) goto L_0x0036
        L_0x0018:
            boolean r6 = r21.isInEditMode()
            if (r6 == 0) goto L_0x0210
            r6 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r1 != r6) goto L_0x0025
            r1 = 1073741824(0x40000000, float:2.0)
            goto L_0x002b
        L_0x0025:
            if (r1 != 0) goto L_0x002b
            r1 = 1073741824(0x40000000, float:2.0)
            r3 = 300(0x12c, float:4.2E-43)
        L_0x002b:
            if (r2 != r6) goto L_0x0030
            r2 = 1073741824(0x40000000, float:2.0)
            goto L_0x0036
        L_0x0030:
            if (r2 != 0) goto L_0x0036
            r2 = 1073741824(0x40000000, float:2.0)
            r4 = 300(0x12c, float:4.2E-43)
        L_0x0036:
            r0.setMeasuredDimension(r3, r4)
            java.lang.Object r6 = r0.mLastInsets
            if (r6 == 0) goto L_0x0045
            boolean r6 = android.support.p001v4.view.ViewCompat.getFitsSystemWindows(r21)
            if (r6 == 0) goto L_0x0045
            r6 = 1
            goto L_0x0046
        L_0x0045:
            r6 = 0
        L_0x0046:
            int r9 = android.support.p001v4.view.ViewCompat.getLayoutDirection(r21)
            r10 = 0
            r11 = 0
            int r12 = r21.getChildCount()
            r13 = r11
            r11 = r10
            r10 = 0
        L_0x0053:
            if (r10 >= r12) goto L_0x0205
            android.view.View r14 = r0.getChildAt(r10)
            int r15 = r14.getVisibility()
            r7 = 8
            if (r15 != r7) goto L_0x006d
            r17 = r1
            r18 = r2
            r19 = r6
            r2 = 1073741824(0x40000000, float:2.0)
            r15 = 0
            goto L_0x013f
        L_0x006d:
            android.view.ViewGroup$LayoutParams r7 = r14.getLayoutParams()
            android.support.v4.widget.DrawerLayout$LayoutParams r7 = (android.support.p001v4.widget.DrawerLayout.LayoutParams) r7
            if (r6 == 0) goto L_0x0116
            int r5 = r7.gravity
            int r5 = android.support.p001v4.view.GravityCompat.getAbsoluteGravity(r5, r9)
            boolean r16 = android.support.p001v4.view.ViewCompat.getFitsSystemWindows(r14)
            r8 = 21
            if (r16 == 0) goto L_0x00c4
            int r15 = android.os.Build.VERSION.SDK_INT
            if (r15 < r8) goto L_0x0116
            java.lang.Object r8 = r0.mLastInsets
            android.view.WindowInsets r8 = (android.view.WindowInsets) r8
            r15 = 3
            if (r5 != r15) goto L_0x00a6
            int r15 = r8.getSystemWindowInsetLeft()
            r17 = r1
            int r1 = r8.getSystemWindowInsetTop()
            r18 = r2
            int r2 = r8.getSystemWindowInsetBottom()
            r19 = r6
            r6 = 0
            android.view.WindowInsets r8 = r8.replaceSystemWindowInsets(r15, r1, r6, r2)
            goto L_0x00c0
        L_0x00a6:
            r17 = r1
            r18 = r2
            r19 = r6
            r6 = 0
            r1 = 5
            if (r5 != r1) goto L_0x00c0
            int r1 = r8.getSystemWindowInsetTop()
            int r2 = r8.getSystemWindowInsetRight()
            int r15 = r8.getSystemWindowInsetBottom()
            android.view.WindowInsets r8 = r8.replaceSystemWindowInsets(r6, r1, r2, r15)
        L_0x00c0:
            r14.dispatchApplyWindowInsets(r8)
            goto L_0x0114
        L_0x00c4:
            r17 = r1
            r18 = r2
            r19 = r6
            int r1 = android.os.Build.VERSION.SDK_INT
            if (r1 < r8) goto L_0x0114
            java.lang.Object r1 = r0.mLastInsets
            android.view.WindowInsets r1 = (android.view.WindowInsets) r1
            r2 = 3
            if (r5 != r2) goto L_0x00e7
            int r2 = r1.getSystemWindowInsetLeft()
            int r6 = r1.getSystemWindowInsetTop()
            int r8 = r1.getSystemWindowInsetBottom()
            r15 = 0
            android.view.WindowInsets r1 = r1.replaceSystemWindowInsets(r2, r6, r15, r8)
            goto L_0x00fb
        L_0x00e7:
            r15 = 0
            r2 = 5
            if (r5 != r2) goto L_0x00fb
            int r2 = r1.getSystemWindowInsetTop()
            int r6 = r1.getSystemWindowInsetRight()
            int r8 = r1.getSystemWindowInsetBottom()
            android.view.WindowInsets r1 = r1.replaceSystemWindowInsets(r15, r2, r6, r8)
        L_0x00fb:
            int r2 = r1.getSystemWindowInsetLeft()
            r7.leftMargin = r2
            int r2 = r1.getSystemWindowInsetTop()
            r7.topMargin = r2
            int r2 = r1.getSystemWindowInsetRight()
            r7.rightMargin = r2
            int r2 = r1.getSystemWindowInsetBottom()
            r7.bottomMargin = r2
            goto L_0x011d
        L_0x0114:
            r15 = 0
            goto L_0x011d
        L_0x0116:
            r17 = r1
            r18 = r2
            r19 = r6
            r15 = 0
        L_0x011d:
            boolean r1 = r0.isContentView(r14)
            if (r1 == 0) goto L_0x0145
            int r1 = r7.leftMargin
            int r1 = r3 - r1
            int r2 = r7.rightMargin
            int r1 = r1 - r2
            r2 = 1073741824(0x40000000, float:2.0)
            int r1 = android.view.View.MeasureSpec.makeMeasureSpec(r1, r2)
            int r5 = r7.topMargin
            int r5 = r4 - r5
            int r6 = r7.bottomMargin
            int r5 = r5 - r6
            int r5 = android.view.View.MeasureSpec.makeMeasureSpec(r5, r2)
            r14.measure(r1, r5)
        L_0x013f:
            r2 = r22
            r0 = r23
            goto L_0x01ca
        L_0x0145:
            r2 = 1073741824(0x40000000, float:2.0)
            boolean r1 = r0.isDrawerView(r14)
            if (r1 == 0) goto L_0x01d8
            boolean r1 = SET_DRAWER_SHADOW_FROM_ELEVATION
            if (r1 == 0) goto L_0x0160
            float r1 = android.support.p001v4.view.ViewCompat.getElevation(r14)
            float r5 = r0.mDrawerElevation
            int r1 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r1 == 0) goto L_0x0160
            float r1 = r0.mDrawerElevation
            android.support.p001v4.view.ViewCompat.setElevation(r14, r1)
        L_0x0160:
            int r1 = r0.getDrawerViewAbsoluteGravity(r14)
            r1 = r1 & 7
            r5 = 3
            if (r1 != r5) goto L_0x016c
            r5 = 1
            goto L_0x016d
        L_0x016c:
            r5 = 0
        L_0x016d:
            if (r5 == 0) goto L_0x0171
            if (r11 != 0) goto L_0x0175
        L_0x0171:
            if (r5 != 0) goto L_0x01a4
            if (r13 == 0) goto L_0x01a4
        L_0x0175:
            java.lang.IllegalStateException r2 = new java.lang.IllegalStateException
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r8 = "Child drawer has absolute gravity "
            r6.append(r8)
            java.lang.String r8 = gravityToString(r1)
            r6.append(r8)
            java.lang.String r8 = " but this "
            r6.append(r8)
            java.lang.String r8 = "DrawerLayout"
            r6.append(r8)
            java.lang.String r8 = " already has a "
            r6.append(r8)
            java.lang.String r8 = "drawer view along that edge"
            r6.append(r8)
            java.lang.String r6 = r6.toString()
            r2.<init>(r6)
            throw r2
        L_0x01a4:
            if (r5 == 0) goto L_0x01a8
            r11 = 1
            goto L_0x01a9
        L_0x01a8:
            r13 = 1
        L_0x01a9:
            int r6 = r0.mMinDrawerMargin
            int r8 = r7.leftMargin
            int r6 = r6 + r8
            int r8 = r7.rightMargin
            int r6 = r6 + r8
            int r8 = r7.width
            r2 = r22
            int r6 = getChildMeasureSpec(r2, r6, r8)
            int r8 = r7.topMargin
            int r15 = r7.bottomMargin
            int r8 = r8 + r15
            int r15 = r7.height
            r0 = r23
            int r8 = getChildMeasureSpec(r0, r8, r15)
            r14.measure(r6, r8)
        L_0x01ca:
            int r10 = r10 + 1
            r1 = r17
            r2 = r18
            r6 = r19
            r0 = r21
            r5 = 1073741824(0x40000000, float:2.0)
            goto L_0x0053
        L_0x01d8:
            r2 = r22
            r0 = r23
            java.lang.IllegalStateException r1 = new java.lang.IllegalStateException
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Child "
            r5.append(r6)
            r5.append(r14)
            java.lang.String r6 = " at index "
            r5.append(r6)
            r5.append(r10)
            java.lang.String r6 = " does not have a valid layout_gravity - must be Gravity.LEFT, "
            r5.append(r6)
            java.lang.String r6 = "Gravity.RIGHT or Gravity.NO_GRAVITY"
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            r1.<init>(r5)
            throw r1
        L_0x0205:
            r0 = r23
            r17 = r1
            r18 = r2
            r19 = r6
            r2 = r22
            return
        L_0x0210:
            r0 = r23
            r5 = r2
            r2 = r22
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.String r7 = "DrawerLayout must be measured with MeasureSpec.EXACTLY."
            r6.<init>(r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p001v4.widget.DrawerLayout.onMeasure(int, int):void");
    }

    private void resolveShadowDrawables() {
        if (!SET_DRAWER_SHADOW_FROM_ELEVATION) {
            this.mShadowLeftResolved = resolveLeftShadow();
            this.mShadowRightResolved = resolveRightShadow();
        }
    }

    private Drawable resolveLeftShadow() {
        int layoutDirection = ViewCompat.getLayoutDirection(this);
        if (layoutDirection == 0) {
            if (this.mShadowStart != null) {
                mirror(this.mShadowStart, layoutDirection);
                return this.mShadowStart;
            }
        } else if (this.mShadowEnd != null) {
            mirror(this.mShadowEnd, layoutDirection);
            return this.mShadowEnd;
        }
        return this.mShadowLeft;
    }

    private Drawable resolveRightShadow() {
        int layoutDirection = ViewCompat.getLayoutDirection(this);
        if (layoutDirection == 0) {
            if (this.mShadowEnd != null) {
                mirror(this.mShadowEnd, layoutDirection);
                return this.mShadowEnd;
            }
        } else if (this.mShadowStart != null) {
            mirror(this.mShadowStart, layoutDirection);
            return this.mShadowStart;
        }
        return this.mShadowRight;
    }

    private boolean mirror(Drawable drawable, int layoutDirection) {
        if (drawable == null || !DrawableCompat.isAutoMirrored(drawable)) {
            return false;
        }
        DrawableCompat.setLayoutDirection(drawable, layoutDirection);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount;
        int width;
        int childLeft;
        float newOffset;
        this.mInLayout = true;
        int width2 = r - l;
        int childCount2 = getChildCount();
        int i = 0;
        while (i < childCount2) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (isContentView(child)) {
                    child.layout(lp.leftMargin, lp.topMargin, lp.leftMargin + child.getMeasuredWidth(), lp.topMargin + child.getMeasuredHeight());
                } else {
                    int childWidth = child.getMeasuredWidth();
                    int childHeight = child.getMeasuredHeight();
                    if (checkDrawerViewAbsoluteGravity(child, 3)) {
                        childLeft = (-childWidth) + ((int) (((float) childWidth) * lp.onScreen));
                        newOffset = ((float) (childWidth + childLeft)) / ((float) childWidth);
                    } else {
                        childLeft = width2 - ((int) (((float) childWidth) * lp.onScreen));
                        newOffset = ((float) (width2 - childLeft)) / ((float) childWidth);
                    }
                    boolean changeOffset = newOffset != lp.onScreen;
                    int vgrav = lp.gravity & 112;
                    if (vgrav == 16) {
                        width = width2;
                        childCount = childCount2;
                        int height = b - t;
                        int childTop = (height - childHeight) / 2;
                        if (childTop < lp.topMargin) {
                            childTop = lp.topMargin;
                        } else if (childTop + childHeight > height - lp.bottomMargin) {
                            childTop = (height - lp.bottomMargin) - childHeight;
                        }
                        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
                    } else if (vgrav != 80) {
                        width = width2;
                        child.layout(childLeft, lp.topMargin, childLeft + childWidth, lp.topMargin + childHeight);
                        childCount = childCount2;
                    } else {
                        width = width2;
                        int height2 = b - t;
                        childCount = childCount2;
                        child.layout(childLeft, (height2 - lp.bottomMargin) - child.getMeasuredHeight(), childLeft + childWidth, height2 - lp.bottomMargin);
                    }
                    if (changeOffset) {
                        setDrawerViewOffset(child, newOffset);
                    }
                    int newVisibility = lp.onScreen > 0.0f ? 0 : 4;
                    if (child.getVisibility() != newVisibility) {
                        child.setVisibility(newVisibility);
                    }
                    i++;
                    width2 = width;
                    childCount2 = childCount;
                }
            }
            width = width2;
            childCount = childCount2;
            i++;
            width2 = width;
            childCount2 = childCount;
        }
        int i2 = childCount2;
        this.mInLayout = false;
        this.mFirstLayout = false;
    }

    public void requestLayout() {
        if (!this.mInLayout) {
            super.requestLayout();
        }
    }

    public void computeScroll() {
        int childCount = getChildCount();
        float scrimOpacity = 0.0f;
        for (int i = 0; i < childCount; i++) {
            scrimOpacity = Math.max(scrimOpacity, ((LayoutParams) getChildAt(i).getLayoutParams()).onScreen);
        }
        this.mScrimOpacity = scrimOpacity;
        boolean leftDraggerSettling = this.mLeftDragger.continueSettling(true);
        boolean rightDraggerSettling = this.mRightDragger.continueSettling(true);
        if (leftDraggerSettling || rightDraggerSettling) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private static boolean hasOpaqueBackground(View v) {
        Drawable bg = v.getBackground();
        boolean z = false;
        if (bg == null) {
            return false;
        }
        if (bg.getOpacity() == -1) {
            z = true;
        }
        return z;
    }

    public void setStatusBarBackground(@Nullable Drawable bg) {
        this.mStatusBarBackground = bg;
        invalidate();
    }

    @Nullable
    public Drawable getStatusBarBackgroundDrawable() {
        return this.mStatusBarBackground;
    }

    public void setStatusBarBackground(int resId) {
        this.mStatusBarBackground = resId != 0 ? ContextCompat.getDrawable(getContext(), resId) : null;
        invalidate();
    }

    public void setStatusBarBackgroundColor(@ColorInt int color) {
        this.mStatusBarBackground = new ColorDrawable(color);
        invalidate();
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        resolveShadowDrawables();
    }

    public void onDraw(Canvas c) {
        super.onDraw(c);
        if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
            int inset = VERSION.SDK_INT >= 21 ? this.mLastInsets != null ? ((WindowInsets) this.mLastInsets).getSystemWindowInsetTop() : 0 : 0;
            if (inset > 0) {
                this.mStatusBarBackground.setBounds(0, 0, getWidth(), inset);
                this.mStatusBarBackground.draw(c);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        int clipRight;
        int clipLeft;
        Canvas canvas2 = canvas;
        View view = child;
        int height = getHeight();
        boolean drawingContent = isContentView(view);
        int clipRight2 = getWidth();
        int restoreCount = canvas.save();
        if (drawingContent) {
            int childCount = getChildCount();
            int clipRight3 = clipRight2;
            int clipLeft2 = 0;
            for (int i = 0; i < childCount; i++) {
                View v = getChildAt(i);
                if (v != view && v.getVisibility() == 0 && hasOpaqueBackground(v) && isDrawerView(v) && v.getHeight() >= height) {
                    if (checkDrawerViewAbsoluteGravity(v, 3)) {
                        int vright = v.getRight();
                        if (vright > clipLeft2) {
                            clipLeft2 = vright;
                        }
                    } else {
                        int vleft = v.getLeft();
                        if (vleft < clipRight3) {
                            clipRight3 = vleft;
                        }
                    }
                }
            }
            canvas2.clipRect(clipLeft2, 0, clipRight3, getHeight());
            clipLeft = clipLeft2;
            clipRight = clipRight3;
        } else {
            clipLeft = 0;
            clipRight = clipRight2;
        }
        boolean result = super.drawChild(canvas, child, drawingTime);
        canvas2.restoreToCount(restoreCount);
        if (this.mScrimOpacity > 0.0f && drawingContent) {
            int imag = (int) (((float) ((this.mScrimColor & -16777216) >>> 24)) * this.mScrimOpacity);
            int color = (imag << 24) | (this.mScrimColor & ViewCompat.MEASURED_SIZE_MASK);
            this.mScrimPaint.setColor(color);
            int i2 = color;
            float height2 = (float) getHeight();
            int i3 = imag;
            canvas2.drawRect((float) clipLeft, 0.0f, (float) clipRight, height2, this.mScrimPaint);
        } else if (this.mShadowLeftResolved != null && checkDrawerViewAbsoluteGravity(view, 3)) {
            int shadowWidth = this.mShadowLeftResolved.getIntrinsicWidth();
            int childRight = child.getRight();
            float alpha = Math.max(0.0f, Math.min(((float) childRight) / ((float) this.mLeftDragger.getEdgeSize()), TOUCH_SLOP_SENSITIVITY));
            int i4 = shadowWidth;
            this.mShadowLeftResolved.setBounds(childRight, child.getTop(), childRight + shadowWidth, child.getBottom());
            this.mShadowLeftResolved.setAlpha((int) (255.0f * alpha));
            this.mShadowLeftResolved.draw(canvas2);
        } else if (this.mShadowRightResolved != null && checkDrawerViewAbsoluteGravity(view, 5)) {
            int shadowWidth2 = this.mShadowRightResolved.getIntrinsicWidth();
            int childLeft = child.getLeft();
            int showing = getWidth() - childLeft;
            float alpha2 = Math.max(0.0f, Math.min(((float) showing) / ((float) this.mRightDragger.getEdgeSize()), TOUCH_SLOP_SENSITIVITY));
            int i5 = shadowWidth2;
            int i6 = showing;
            this.mShadowRightResolved.setBounds(childLeft - shadowWidth2, child.getTop(), childLeft, child.getBottom());
            this.mShadowRightResolved.setAlpha((int) (255.0f * alpha2));
            this.mShadowRightResolved.draw(canvas2);
        }
        return result;
    }

    /* access modifiers changed from: 0000 */
    public boolean isContentView(View child) {
        return ((LayoutParams) child.getLayoutParams()).gravity == 0;
    }

    /* access modifiers changed from: 0000 */
    public boolean isDrawerView(View child) {
        int absGravity = GravityCompat.getAbsoluteGravity(((LayoutParams) child.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(child));
        if ((absGravity & 3) == 0 && (absGravity & 5) == 0) {
            return false;
        }
        return true;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean interceptForDrag = this.mLeftDragger.shouldInterceptTouchEvent(ev) | this.mRightDragger.shouldInterceptTouchEvent(ev);
        boolean interceptForTap = false;
        switch (ev.getActionMasked()) {
            case 0:
                float x = ev.getX();
                float y = ev.getY();
                this.mInitialMotionX = x;
                this.mInitialMotionY = y;
                if (this.mScrimOpacity > 0.0f) {
                    View child = this.mLeftDragger.findTopChildUnder((int) x, (int) y);
                    if (child != null && isContentView(child)) {
                        interceptForTap = true;
                    }
                }
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                break;
            case 1:
            case 3:
                closeDrawers(true);
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                break;
            case 2:
                if (this.mLeftDragger.checkTouchSlop(3)) {
                    this.mLeftCallback.removeCallbacks();
                    this.mRightCallback.removeCallbacks();
                    break;
                }
                break;
        }
        if (interceptForDrag || interceptForTap || hasPeekingDrawer() || this.mChildrenCanceledTouch) {
            return true;
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        this.mLeftDragger.processTouchEvent(ev);
        this.mRightDragger.processTouchEvent(ev);
        int action = ev.getAction() & 255;
        boolean z = true;
        if (action != 3) {
            switch (action) {
                case 0:
                    float x = ev.getX();
                    float y = ev.getY();
                    this.mInitialMotionX = x;
                    this.mInitialMotionY = y;
                    this.mDisallowInterceptRequested = false;
                    this.mChildrenCanceledTouch = false;
                    break;
                case 1:
                    float x2 = ev.getX();
                    float y2 = ev.getY();
                    boolean peekingOnly = true;
                    View touchedView = this.mLeftDragger.findTopChildUnder((int) x2, (int) y2);
                    if (touchedView != null && isContentView(touchedView)) {
                        float dx = x2 - this.mInitialMotionX;
                        float dy = y2 - this.mInitialMotionY;
                        int slop = this.mLeftDragger.getTouchSlop();
                        if ((dx * dx) + (dy * dy) < ((float) (slop * slop))) {
                            View openDrawer = findOpenDrawer();
                            if (openDrawer != null) {
                                if (getDrawerLockMode(openDrawer) != 2) {
                                    z = false;
                                }
                                peekingOnly = z;
                            }
                        }
                    }
                    closeDrawers(peekingOnly);
                    this.mDisallowInterceptRequested = false;
                    break;
            }
        } else {
            closeDrawers(true);
            this.mDisallowInterceptRequested = false;
            this.mChildrenCanceledTouch = false;
        }
        return true;
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
        this.mDisallowInterceptRequested = disallowIntercept;
        if (disallowIntercept) {
            closeDrawers(true);
        }
    }

    public void closeDrawers() {
        closeDrawers(false);
    }

    /* access modifiers changed from: 0000 */
    public void closeDrawers(boolean peekingOnly) {
        int childCount = getChildCount();
        boolean needsInvalidate = false;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (isDrawerView(child) && (!peekingOnly || lp.isPeeking)) {
                int childWidth = child.getWidth();
                if (checkDrawerViewAbsoluteGravity(child, 3)) {
                    needsInvalidate |= this.mLeftDragger.smoothSlideViewTo(child, -childWidth, child.getTop());
                } else {
                    needsInvalidate |= this.mRightDragger.smoothSlideViewTo(child, getWidth(), child.getTop());
                }
                lp.isPeeking = false;
            }
        }
        this.mLeftCallback.removeCallbacks();
        this.mRightCallback.removeCallbacks();
        if (needsInvalidate) {
            invalidate();
        }
    }

    public void openDrawer(@NonNull View drawerView) {
        openDrawer(drawerView, true);
    }

    public void openDrawer(@NonNull View drawerView, boolean animate) {
        if (!isDrawerView(drawerView)) {
            StringBuilder sb = new StringBuilder();
            sb.append("View ");
            sb.append(drawerView);
            sb.append(" is not a sliding drawer");
            throw new IllegalArgumentException(sb.toString());
        }
        LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
        if (this.mFirstLayout) {
            lp.onScreen = TOUCH_SLOP_SENSITIVITY;
            lp.openState = 1;
            updateChildrenImportantForAccessibility(drawerView, true);
        } else if (animate) {
            lp.openState |= 2;
            if (checkDrawerViewAbsoluteGravity(drawerView, 3)) {
                this.mLeftDragger.smoothSlideViewTo(drawerView, 0, drawerView.getTop());
            } else {
                this.mRightDragger.smoothSlideViewTo(drawerView, getWidth() - drawerView.getWidth(), drawerView.getTop());
            }
        } else {
            moveDrawerToOffset(drawerView, TOUCH_SLOP_SENSITIVITY);
            updateDrawerState(lp.gravity, 0, drawerView);
            drawerView.setVisibility(0);
        }
        invalidate();
    }

    public void openDrawer(int gravity) {
        openDrawer(gravity, true);
    }

    public void openDrawer(int gravity, boolean animate) {
        View drawerView = findDrawerWithGravity(gravity);
        if (drawerView == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("No drawer view found with gravity ");
            sb.append(gravityToString(gravity));
            throw new IllegalArgumentException(sb.toString());
        }
        openDrawer(drawerView, animate);
    }

    public void closeDrawer(@NonNull View drawerView) {
        closeDrawer(drawerView, true);
    }

    public void closeDrawer(@NonNull View drawerView, boolean animate) {
        if (!isDrawerView(drawerView)) {
            StringBuilder sb = new StringBuilder();
            sb.append("View ");
            sb.append(drawerView);
            sb.append(" is not a sliding drawer");
            throw new IllegalArgumentException(sb.toString());
        }
        LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
        if (this.mFirstLayout) {
            lp.onScreen = 0.0f;
            lp.openState = 0;
        } else if (animate) {
            lp.openState = 4 | lp.openState;
            if (checkDrawerViewAbsoluteGravity(drawerView, 3)) {
                this.mLeftDragger.smoothSlideViewTo(drawerView, -drawerView.getWidth(), drawerView.getTop());
            } else {
                this.mRightDragger.smoothSlideViewTo(drawerView, getWidth(), drawerView.getTop());
            }
        } else {
            moveDrawerToOffset(drawerView, 0.0f);
            updateDrawerState(lp.gravity, 0, drawerView);
            drawerView.setVisibility(4);
        }
        invalidate();
    }

    public void closeDrawer(int gravity) {
        closeDrawer(gravity, true);
    }

    public void closeDrawer(int gravity, boolean animate) {
        View drawerView = findDrawerWithGravity(gravity);
        if (drawerView == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("No drawer view found with gravity ");
            sb.append(gravityToString(gravity));
            throw new IllegalArgumentException(sb.toString());
        }
        closeDrawer(drawerView, animate);
    }

    public boolean isDrawerOpen(@NonNull View drawer) {
        if (isDrawerView(drawer)) {
            return (((LayoutParams) drawer.getLayoutParams()).openState & 1) == 1;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("View ");
        sb.append(drawer);
        sb.append(" is not a drawer");
        throw new IllegalArgumentException(sb.toString());
    }

    public boolean isDrawerOpen(int drawerGravity) {
        View drawerView = findDrawerWithGravity(drawerGravity);
        if (drawerView != null) {
            return isDrawerOpen(drawerView);
        }
        return false;
    }

    public boolean isDrawerVisible(@NonNull View drawer) {
        if (isDrawerView(drawer)) {
            return ((LayoutParams) drawer.getLayoutParams()).onScreen > 0.0f;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("View ");
        sb.append(drawer);
        sb.append(" is not a drawer");
        throw new IllegalArgumentException(sb.toString());
    }

    public boolean isDrawerVisible(int drawerGravity) {
        View drawerView = findDrawerWithGravity(drawerGravity);
        if (drawerView != null) {
            return isDrawerVisible(drawerView);
        }
        return false;
    }

    private boolean hasPeekingDrawer() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (((LayoutParams) getChildAt(i).getLayoutParams()).isPeeking) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    /* access modifiers changed from: protected */
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        if (p instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) p);
        }
        return p instanceof MarginLayoutParams ? new LayoutParams((MarginLayoutParams) p) : new LayoutParams(p);
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return (p instanceof LayoutParams) && super.checkLayoutParams(p);
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (getDescendantFocusability() != 393216) {
            int childCount = getChildCount();
            boolean isDrawerOpen = false;
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (!isDrawerView(child)) {
                    this.mNonDrawerViews.add(child);
                } else if (isDrawerOpen(child)) {
                    isDrawerOpen = true;
                    child.addFocusables(views, direction, focusableMode);
                }
            }
            if (!isDrawerOpen) {
                int nonDrawerViewsCount = this.mNonDrawerViews.size();
                for (int i2 = 0; i2 < nonDrawerViewsCount; i2++) {
                    View child2 = (View) this.mNonDrawerViews.get(i2);
                    if (child2.getVisibility() == 0) {
                        child2.addFocusables(views, direction, focusableMode);
                    }
                }
            }
            this.mNonDrawerViews.clear();
        }
    }

    private boolean hasVisibleDrawer() {
        return findVisibleDrawer() != null;
    }

    /* access modifiers changed from: 0000 */
    public View findVisibleDrawer() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (isDrawerView(child) && isDrawerVisible(child)) {
                return child;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public void cancelChildViewTouch() {
        if (!this.mChildrenCanceledTouch) {
            long now = SystemClock.uptimeMillis();
            MotionEvent cancelEvent = MotionEvent.obtain(now, now, 3, 0.0f, 0.0f, 0);
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).dispatchTouchEvent(cancelEvent);
            }
            cancelEvent.recycle();
            this.mChildrenCanceledTouch = true;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || !hasVisibleDrawer()) {
            return super.onKeyDown(keyCode, event);
        }
        event.startTracking();
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyUp(keyCode, event);
        }
        View visibleDrawer = findVisibleDrawer();
        if (visibleDrawer != null && getDrawerLockMode(visibleDrawer) == 0) {
            closeDrawers();
        }
        return visibleDrawer != null;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        if (ss.openDrawerGravity != 0) {
            View toOpen = findDrawerWithGravity(ss.openDrawerGravity);
            if (toOpen != null) {
                openDrawer(toOpen);
            }
        }
        if (ss.lockModeLeft != 3) {
            setDrawerLockMode(ss.lockModeLeft, 3);
        }
        if (ss.lockModeRight != 3) {
            setDrawerLockMode(ss.lockModeRight, 5);
        }
        if (ss.lockModeStart != 3) {
            setDrawerLockMode(ss.lockModeStart, (int) GravityCompat.START);
        }
        if (ss.lockModeEnd != 3) {
            setDrawerLockMode(ss.lockModeEnd, (int) GravityCompat.END);
        }
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        int childCount = getChildCount();
        int i = 0;
        while (true) {
            if (i >= childCount) {
                break;
            }
            LayoutParams lp = (LayoutParams) getChildAt(i).getLayoutParams();
            boolean isClosedAndOpening = true;
            boolean isOpenedAndNotClosing = lp.openState == 1;
            if (lp.openState != 2) {
                isClosedAndOpening = false;
            }
            if (isOpenedAndNotClosing || isClosedAndOpening) {
                ss.openDrawerGravity = lp.gravity;
            } else {
                i++;
            }
        }
        ss.lockModeLeft = this.mLockModeLeft;
        ss.lockModeRight = this.mLockModeRight;
        ss.lockModeStart = this.mLockModeStart;
        ss.lockModeEnd = this.mLockModeEnd;
        return ss;
    }

    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (findOpenDrawer() != null || isDrawerView(child)) {
            ViewCompat.setImportantForAccessibility(child, 4);
        } else {
            ViewCompat.setImportantForAccessibility(child, 1);
        }
        if (!CAN_HIDE_DESCENDANTS) {
            ViewCompat.setAccessibilityDelegate(child, this.mChildAccessibilityDelegate);
        }
    }

    static boolean includeChildForAccessibility(View child) {
        return (ViewCompat.getImportantForAccessibility(child) == 4 || ViewCompat.getImportantForAccessibility(child) == 2) ? false : true;
    }
}
