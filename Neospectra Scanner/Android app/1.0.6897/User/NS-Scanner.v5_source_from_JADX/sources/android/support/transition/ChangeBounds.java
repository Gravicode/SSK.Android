package android.support.transition;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p001v4.content.res.TypedArrayUtils;
import android.support.p001v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;

public class ChangeBounds extends Transition {
    private static final Property<View, PointF> BOTTOM_RIGHT_ONLY_PROPERTY = new Property<View, PointF>(PointF.class, "bottomRight") {
        public void set(View view, PointF bottomRight) {
            ViewUtils.setLeftTopRightBottom(view, view.getLeft(), view.getTop(), Math.round(bottomRight.x), Math.round(bottomRight.y));
        }

        public PointF get(View view) {
            return null;
        }
    };
    private static final Property<ViewBounds, PointF> BOTTOM_RIGHT_PROPERTY = new Property<ViewBounds, PointF>(PointF.class, "bottomRight") {
        public void set(ViewBounds viewBounds, PointF bottomRight) {
            viewBounds.setBottomRight(bottomRight);
        }

        public PointF get(ViewBounds viewBounds) {
            return null;
        }
    };
    private static final Property<Drawable, PointF> DRAWABLE_ORIGIN_PROPERTY = new Property<Drawable, PointF>(PointF.class, "boundsOrigin") {
        private Rect mBounds = new Rect();

        public void set(Drawable object, PointF value) {
            object.copyBounds(this.mBounds);
            this.mBounds.offsetTo(Math.round(value.x), Math.round(value.y));
            object.setBounds(this.mBounds);
        }

        public PointF get(Drawable object) {
            object.copyBounds(this.mBounds);
            return new PointF((float) this.mBounds.left, (float) this.mBounds.top);
        }
    };
    private static final Property<View, PointF> POSITION_PROPERTY = new Property<View, PointF>(PointF.class, "position") {
        public void set(View view, PointF topLeft) {
            int left = Math.round(topLeft.x);
            int top = Math.round(topLeft.y);
            ViewUtils.setLeftTopRightBottom(view, left, top, view.getWidth() + left, view.getHeight() + top);
        }

        public PointF get(View view) {
            return null;
        }
    };
    private static final String PROPNAME_BOUNDS = "android:changeBounds:bounds";
    private static final String PROPNAME_CLIP = "android:changeBounds:clip";
    private static final String PROPNAME_PARENT = "android:changeBounds:parent";
    private static final String PROPNAME_WINDOW_X = "android:changeBounds:windowX";
    private static final String PROPNAME_WINDOW_Y = "android:changeBounds:windowY";
    private static final Property<View, PointF> TOP_LEFT_ONLY_PROPERTY = new Property<View, PointF>(PointF.class, "topLeft") {
        public void set(View view, PointF topLeft) {
            ViewUtils.setLeftTopRightBottom(view, Math.round(topLeft.x), Math.round(topLeft.y), view.getRight(), view.getBottom());
        }

        public PointF get(View view) {
            return null;
        }
    };
    private static final Property<ViewBounds, PointF> TOP_LEFT_PROPERTY = new Property<ViewBounds, PointF>(PointF.class, "topLeft") {
        public void set(ViewBounds viewBounds, PointF topLeft) {
            viewBounds.setTopLeft(topLeft);
        }

        public PointF get(ViewBounds viewBounds) {
            return null;
        }
    };
    private static RectEvaluator sRectEvaluator = new RectEvaluator();
    private static final String[] sTransitionProperties = {PROPNAME_BOUNDS, PROPNAME_CLIP, PROPNAME_PARENT, PROPNAME_WINDOW_X, PROPNAME_WINDOW_Y};
    private boolean mReparent = false;
    private boolean mResizeClip = false;
    private int[] mTempLocation = new int[2];

    private static class ViewBounds {
        private int mBottom;
        private int mBottomRightCalls;
        private int mLeft;
        private int mRight;
        private int mTop;
        private int mTopLeftCalls;
        private View mView;

        ViewBounds(View view) {
            this.mView = view;
        }

        /* access modifiers changed from: 0000 */
        public void setTopLeft(PointF topLeft) {
            this.mLeft = Math.round(topLeft.x);
            this.mTop = Math.round(topLeft.y);
            this.mTopLeftCalls++;
            if (this.mTopLeftCalls == this.mBottomRightCalls) {
                setLeftTopRightBottom();
            }
        }

        /* access modifiers changed from: 0000 */
        public void setBottomRight(PointF bottomRight) {
            this.mRight = Math.round(bottomRight.x);
            this.mBottom = Math.round(bottomRight.y);
            this.mBottomRightCalls++;
            if (this.mTopLeftCalls == this.mBottomRightCalls) {
                setLeftTopRightBottom();
            }
        }

        private void setLeftTopRightBottom() {
            ViewUtils.setLeftTopRightBottom(this.mView, this.mLeft, this.mTop, this.mRight, this.mBottom);
            this.mTopLeftCalls = 0;
            this.mBottomRightCalls = 0;
        }
    }

    public ChangeBounds() {
    }

    public ChangeBounds(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, Styleable.CHANGE_BOUNDS);
        boolean resizeClip = TypedArrayUtils.getNamedBoolean(a, (XmlResourceParser) attrs, "resizeClip", 0, false);
        a.recycle();
        setResizeClip(resizeClip);
    }

    @Nullable
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    public void setResizeClip(boolean resizeClip) {
        this.mResizeClip = resizeClip;
    }

    public boolean getResizeClip() {
        return this.mResizeClip;
    }

    private void captureValues(TransitionValues values) {
        View view = values.view;
        if (ViewCompat.isLaidOut(view) || view.getWidth() != 0 || view.getHeight() != 0) {
            values.values.put(PROPNAME_BOUNDS, new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
            values.values.put(PROPNAME_PARENT, values.view.getParent());
            if (this.mReparent) {
                values.view.getLocationInWindow(this.mTempLocation);
                values.values.put(PROPNAME_WINDOW_X, Integer.valueOf(this.mTempLocation[0]));
                values.values.put(PROPNAME_WINDOW_Y, Integer.valueOf(this.mTempLocation[1]));
            }
            if (this.mResizeClip) {
                values.values.put(PROPNAME_CLIP, ViewCompat.getClipBounds(view));
            }
        }
    }

    public void captureStartValues(@NonNull TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public void captureEndValues(@NonNull TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private boolean parentMatches(View startParent, View endParent) {
        if (!this.mReparent) {
            return true;
        }
        boolean z = true;
        TransitionValues endValues = getMatchedTransitionValues(startParent, true);
        if (endValues == null) {
            if (startParent != endParent) {
                z = false;
            }
            return z;
        }
        if (endParent != endValues.view) {
            z = false;
        }
        return z;
    }

    /* JADX WARNING: type inference failed for: r0v26, types: [android.animation.Animator] */
    /* JADX WARNING: type inference failed for: r0v27, types: [android.animation.Animator] */
    /* JADX WARNING: type inference failed for: r0v30 */
    /* JADX WARNING: type inference failed for: r0v33, types: [android.animation.ObjectAnimator] */
    /* JADX WARNING: type inference failed for: r0v36, types: [android.animation.ObjectAnimator] */
    /* JADX WARNING: type inference failed for: r0v37 */
    /* JADX WARNING: type inference failed for: r10v12, types: [android.animation.AnimatorSet] */
    /* JADX WARNING: type inference failed for: r14v13 */
    /* JADX WARNING: type inference failed for: r0v39 */
    /* JADX WARNING: type inference failed for: r0v42, types: [android.animation.ObjectAnimator] */
    /* JADX WARNING: type inference failed for: r0v43 */
    /* JADX WARNING: type inference failed for: r0v44 */
    /* JADX WARNING: type inference failed for: r0v45 */
    /* JADX WARNING: type inference failed for: r0v46 */
    /* JADX WARNING: type inference failed for: r0v47 */
    /* JADX WARNING: type inference failed for: r0v48 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v30
      assigns: []
      uses: []
      mth insns count: 398
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 8 */
    @android.support.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.animation.Animator createAnimator(@android.support.annotation.NonNull android.view.ViewGroup r53, @android.support.annotation.Nullable android.support.transition.TransitionValues r54, @android.support.annotation.Nullable android.support.transition.TransitionValues r55) {
        /*
            r52 = this;
            r8 = r52
            r9 = r54
            r10 = r55
            if (r9 == 0) goto L_0x0388
            if (r10 != 0) goto L_0x000c
            goto L_0x0388
        L_0x000c:
            java.util.Map<java.lang.String, java.lang.Object> r11 = r9.values
            java.util.Map<java.lang.String, java.lang.Object> r12 = r10.values
            java.lang.String r1 = "android:changeBounds:parent"
            java.lang.Object r1 = r11.get(r1)
            r13 = r1
            android.view.ViewGroup r13 = (android.view.ViewGroup) r13
            java.lang.String r1 = "android:changeBounds:parent"
            java.lang.Object r1 = r12.get(r1)
            r14 = r1
            android.view.ViewGroup r14 = (android.view.ViewGroup) r14
            if (r13 == 0) goto L_0x037b
            if (r14 != 0) goto L_0x0033
            r15 = r53
            r18 = r11
            r19 = r12
            r20 = r13
            r21 = r14
            r12 = r10
            goto L_0x0386
        L_0x0033:
            android.view.View r15 = r10.view
            boolean r1 = r8.parentMatches(r13, r14)
            if (r1 == 0) goto L_0x02a7
            java.util.Map<java.lang.String, java.lang.Object> r1 = r9.values
            java.lang.String r3 = "android:changeBounds:bounds"
            java.lang.Object r1 = r1.get(r3)
            r6 = r1
            android.graphics.Rect r6 = (android.graphics.Rect) r6
            java.util.Map<java.lang.String, java.lang.Object> r1 = r10.values
            java.lang.String r3 = "android:changeBounds:bounds"
            java.lang.Object r1 = r1.get(r3)
            r5 = r1
            android.graphics.Rect r5 = (android.graphics.Rect) r5
            int r4 = r6.left
            int r1 = r5.left
            int r3 = r6.top
            int r7 = r5.top
            int r2 = r6.right
            r18 = r11
            int r11 = r5.right
            r19 = r12
            int r12 = r6.bottom
            r20 = r13
            int r13 = r5.bottom
            r21 = r14
            int r14 = r2 - r4
            r22 = r6
            int r6 = r12 - r3
            r23 = r5
            int r5 = r11 - r1
            int r0 = r13 - r7
            r25 = r15
            java.util.Map<java.lang.String, java.lang.Object> r15 = r9.values
            java.lang.String r9 = "android:changeBounds:clip"
            java.lang.Object r9 = r15.get(r9)
            android.graphics.Rect r9 = (android.graphics.Rect) r9
            java.util.Map<java.lang.String, java.lang.Object> r15 = r10.values
            java.lang.String r10 = "android:changeBounds:clip"
            java.lang.Object r10 = r15.get(r10)
            android.graphics.Rect r10 = (android.graphics.Rect) r10
            r15 = 0
            if (r14 == 0) goto L_0x0090
            if (r6 != 0) goto L_0x0094
        L_0x0090:
            if (r5 == 0) goto L_0x00a0
            if (r0 == 0) goto L_0x00a0
        L_0x0094:
            if (r4 != r1) goto L_0x0098
            if (r3 == r7) goto L_0x009a
        L_0x0098:
            int r15 = r15 + 1
        L_0x009a:
            if (r2 != r11) goto L_0x009e
            if (r12 == r13) goto L_0x00a0
        L_0x009e:
            int r15 = r15 + 1
        L_0x00a0:
            if (r9 == 0) goto L_0x00a8
            boolean r26 = r9.equals(r10)
            if (r26 == 0) goto L_0x00ac
        L_0x00a8:
            if (r9 != 0) goto L_0x00ae
            if (r10 == 0) goto L_0x00ae
        L_0x00ac:
            int r15 = r15 + 1
        L_0x00ae:
            if (r15 <= 0) goto L_0x02a0
            r27 = r9
            boolean r9 = r8.mResizeClip
            r28 = r10
            r10 = 2
            if (r9 != 0) goto L_0x01b0
            r9 = r25
            android.support.transition.ViewUtils.setLeftTopRightBottom(r9, r4, r3, r2, r12)
            if (r15 != r10) goto L_0x0162
            if (r14 != r5) goto L_0x00f0
            if (r6 != r0) goto L_0x00f0
            android.support.transition.PathMotion r10 = r52.getPathMotion()
            r29 = r15
            float r15 = (float) r4
            r30 = r0
            float r0 = (float) r3
            r31 = r6
            float r6 = (float) r1
            r32 = r5
            float r5 = (float) r7
            android.graphics.Path r0 = r10.getPath(r15, r0, r6, r5)
            android.util.Property<android.view.View, android.graphics.PointF> r5 = POSITION_PROPERTY
            android.animation.ObjectAnimator r0 = android.support.transition.ObjectAnimatorUtils.ofPointF(r9, r5, r0)
            r25 = r1
            r26 = r2
            r24 = r3
            r16 = r7
            r47 = r11
            r37 = r12
            r45 = r14
            r17 = r30
            goto L_0x0155
        L_0x00f0:
            r30 = r0
            r32 = r5
            r31 = r6
            r29 = r15
            android.support.transition.ChangeBounds$ViewBounds r0 = new android.support.transition.ChangeBounds$ViewBounds
            r0.<init>(r9)
            android.support.transition.PathMotion r5 = r52.getPathMotion()
            float r6 = (float) r4
            float r15 = (float) r3
            float r10 = (float) r1
            r33 = r14
            float r14 = (float) r7
            android.graphics.Path r5 = r5.getPath(r6, r15, r10, r14)
            android.util.Property<android.support.transition.ChangeBounds$ViewBounds, android.graphics.PointF> r6 = TOP_LEFT_PROPERTY
            android.animation.ObjectAnimator r6 = android.support.transition.ObjectAnimatorUtils.ofPointF(r0, r6, r5)
            android.support.transition.PathMotion r10 = r52.getPathMotion()
            float r14 = (float) r2
            float r15 = (float) r12
            r34 = r5
            float r5 = (float) r11
            r35 = r9
            float r9 = (float) r13
            android.graphics.Path r5 = r10.getPath(r14, r15, r5, r9)
            android.util.Property<android.support.transition.ChangeBounds$ViewBounds, android.graphics.PointF> r9 = BOTTOM_RIGHT_PROPERTY
            android.animation.ObjectAnimator r9 = android.support.transition.ObjectAnimatorUtils.ofPointF(r0, r9, r5)
            android.animation.AnimatorSet r10 = new android.animation.AnimatorSet
            r10.<init>()
            r14 = 2
            android.animation.Animator[] r14 = new android.animation.Animator[r14]
            r15 = 0
            r14[r15] = r6
            r15 = 1
            r14[r15] = r9
            r10.playTogether(r14)
            r14 = r10
            android.support.transition.ChangeBounds$7 r15 = new android.support.transition.ChangeBounds$7
            r15.<init>(r0)
            r10.addListener(r15)
            r25 = r1
            r26 = r2
            r24 = r3
            r16 = r7
            r47 = r11
            r37 = r12
            r0 = r14
            r17 = r30
            r45 = r33
            r9 = r35
        L_0x0155:
            r11 = 1
            r30 = r27
            r27 = r4
            r51 = r32
            r32 = r31
            r31 = r51
            goto L_0x0286
        L_0x0162:
            r30 = r0
            r32 = r5
            r31 = r6
            r35 = r9
            r33 = r14
            r29 = r15
            if (r4 != r1) goto L_0x018a
            if (r3 == r7) goto L_0x0175
            r9 = r35
            goto L_0x018c
        L_0x0175:
            android.support.transition.PathMotion r0 = r52.getPathMotion()
            float r5 = (float) r2
            float r6 = (float) r12
            float r9 = (float) r11
            float r10 = (float) r13
            android.graphics.Path r0 = r0.getPath(r5, r6, r9, r10)
            android.util.Property<android.view.View, android.graphics.PointF> r5 = BOTTOM_RIGHT_ONLY_PROPERTY
            r9 = r35
            android.animation.ObjectAnimator r0 = android.support.transition.ObjectAnimatorUtils.ofPointF(r9, r5, r0)
            goto L_0x019f
        L_0x018a:
            r9 = r35
        L_0x018c:
            android.support.transition.PathMotion r0 = r52.getPathMotion()
            float r5 = (float) r4
            float r6 = (float) r3
            float r10 = (float) r1
            float r14 = (float) r7
            android.graphics.Path r0 = r0.getPath(r5, r6, r10, r14)
            android.util.Property<android.view.View, android.graphics.PointF> r5 = TOP_LEFT_ONLY_PROPERTY
            android.animation.ObjectAnimator r0 = android.support.transition.ObjectAnimatorUtils.ofPointF(r9, r5, r0)
        L_0x019f:
            r25 = r1
            r26 = r2
            r24 = r3
            r16 = r7
            r47 = r11
            r37 = r12
            r17 = r30
            r45 = r33
            goto L_0x0155
        L_0x01b0:
            r30 = r0
            r32 = r5
            r31 = r6
            r33 = r14
            r29 = r15
            r9 = r25
            r10 = r33
            int r14 = java.lang.Math.max(r10, r5)
            int r15 = java.lang.Math.max(r6, r0)
            r36 = r2
            int r2 = r4 + r14
            r37 = r12
            int r12 = r3 + r15
            android.support.transition.ViewUtils.setLeftTopRightBottom(r9, r4, r3, r2, r12)
            r2 = 0
            if (r4 != r1) goto L_0x01df
            if (r3 == r7) goto L_0x01d7
            goto L_0x01df
        L_0x01d7:
            r41 = r1
            r12 = r2
            r40 = r3
            r39 = r4
            goto L_0x01fa
        L_0x01df:
            android.support.transition.PathMotion r12 = r52.getPathMotion()
            r38 = r2
            float r2 = (float) r4
            r39 = r4
            float r4 = (float) r3
            r40 = r3
            float r3 = (float) r1
            r41 = r1
            float r1 = (float) r7
            android.graphics.Path r1 = r12.getPath(r2, r4, r3, r1)
            android.util.Property<android.view.View, android.graphics.PointF> r2 = POSITION_PROPERTY
            android.animation.ObjectAnimator r2 = android.support.transition.ObjectAnimatorUtils.ofPointF(r9, r2, r1)
            r12 = r2
        L_0x01fa:
            r24 = r40
            r3 = r28
            if (r27 != 0) goto L_0x0208
            android.graphics.Rect r1 = new android.graphics.Rect
            r2 = 0
            r1.<init>(r2, r2, r10, r6)
            r4 = r1
            goto L_0x020b
        L_0x0208:
            r2 = 0
            r4 = r27
        L_0x020b:
            if (r28 != 0) goto L_0x0214
            android.graphics.Rect r1 = new android.graphics.Rect
            r1.<init>(r2, r2, r5, r0)
            r2 = r1
            goto L_0x0216
        L_0x0214:
            r2 = r28
        L_0x0216:
            r1 = 0
            boolean r25 = r4.equals(r2)
            if (r25 != 0) goto L_0x0265
            android.support.p001v4.view.ViewCompat.setClipBounds(r9, r4)
            r42 = r0
            java.lang.String r0 = "clipBounds"
            r43 = r1
            android.support.transition.RectEvaluator r1 = sRectEvaluator
            r44 = r5
            r5 = 2
            java.lang.Object[] r5 = new java.lang.Object[r5]
            r17 = 0
            r5[r17] = r4
            r16 = 1
            r5[r16] = r2
            android.animation.ObjectAnimator r5 = android.animation.ObjectAnimator.ofObject(r9, r0, r1, r5)
            android.support.transition.ChangeBounds$8 r1 = new android.support.transition.ChangeBounds$8
            r17 = r42
            r0 = r1
            r45 = r10
            r25 = r41
            r10 = r1
            r1 = r8
            r28 = r2
            r26 = r36
            r2 = r9
            r30 = r4
            r27 = r39
            r4 = r25
            r46 = r14
            r31 = r44
            r14 = r5
            r5 = r7
            r32 = r6
            r6 = r11
            r16 = r7
            r47 = r11
            r11 = 1
            r7 = r13
            r0.<init>(r2, r3, r4, r5, r6, r7)
            r14.addListener(r10)
            goto L_0x0282
        L_0x0265:
            r17 = r0
            r43 = r1
            r28 = r2
            r30 = r4
            r31 = r5
            r32 = r6
            r16 = r7
            r45 = r10
            r47 = r11
            r46 = r14
            r26 = r36
            r27 = r39
            r25 = r41
            r11 = 1
            r14 = r43
        L_0x0282:
            android.animation.Animator r0 = android.support.transition.TransitionUtils.mergeAnimators(r12, r14)
        L_0x0286:
            android.view.ViewParent r1 = r9.getParent()
            boolean r1 = r1 instanceof android.view.ViewGroup
            if (r1 == 0) goto L_0x029f
            android.view.ViewParent r1 = r9.getParent()
            android.view.ViewGroup r1 = (android.view.ViewGroup) r1
            android.support.transition.ViewGroupUtils.suppressLayout(r1, r11)
            android.support.transition.ChangeBounds$9 r2 = new android.support.transition.ChangeBounds$9
            r2.<init>(r1)
            r8.addListener(r2)
        L_0x029f:
            return r0
        L_0x02a0:
            r9 = r25
            r6 = r54
            r12 = r55
            goto L_0x02f2
        L_0x02a7:
            r18 = r11
            r19 = r12
            r20 = r13
            r21 = r14
            r9 = r15
            r11 = 1
            r6 = r54
            java.util.Map<java.lang.String, java.lang.Object> r0 = r6.values
            java.lang.String r1 = "android:changeBounds:windowX"
            java.lang.Object r0 = r0.get(r1)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r7 = r0.intValue()
            java.util.Map<java.lang.String, java.lang.Object> r0 = r6.values
            java.lang.String r1 = "android:changeBounds:windowY"
            java.lang.Object r0 = r0.get(r1)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r10 = r0.intValue()
            r12 = r55
            java.util.Map<java.lang.String, java.lang.Object> r0 = r12.values
            java.lang.String r1 = "android:changeBounds:windowX"
            java.lang.Object r0 = r0.get(r1)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r13 = r0.intValue()
            java.util.Map<java.lang.String, java.lang.Object> r0 = r12.values
            java.lang.String r1 = "android:changeBounds:windowY"
            java.lang.Object r0 = r0.get(r1)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r14 = r0.intValue()
            if (r7 != r13) goto L_0x02f4
            if (r10 == r14) goto L_0x02f2
            goto L_0x02f4
        L_0x02f2:
            r0 = 0
            return r0
        L_0x02f4:
            int[] r0 = r8.mTempLocation
            r15 = r53
            r15.getLocationInWindow(r0)
            int r0 = r9.getWidth()
            int r1 = r9.getHeight()
            android.graphics.Bitmap$Config r2 = android.graphics.Bitmap.Config.ARGB_8888
            android.graphics.Bitmap r5 = android.graphics.Bitmap.createBitmap(r0, r1, r2)
            android.graphics.Canvas r0 = new android.graphics.Canvas
            r0.<init>(r5)
            r4 = r0
            r9.draw(r4)
            android.graphics.drawable.BitmapDrawable r0 = new android.graphics.drawable.BitmapDrawable
            r0.<init>(r5)
            r3 = r0
            float r16 = android.support.transition.ViewUtils.getTransitionAlpha(r9)
            r0 = 0
            android.support.transition.ViewUtils.setTransitionAlpha(r9, r0)
            android.support.transition.ViewOverlayImpl r0 = android.support.transition.ViewUtils.getOverlay(r53)
            r0.add(r3)
            android.support.transition.PathMotion r0 = r52.getPathMotion()
            int[] r1 = r8.mTempLocation
            r2 = 0
            r1 = r1[r2]
            int r1 = r7 - r1
            float r1 = (float) r1
            int[] r2 = r8.mTempLocation
            r2 = r2[r11]
            int r2 = r10 - r2
            float r2 = (float) r2
            int[] r11 = r8.mTempLocation
            r17 = 0
            r11 = r11[r17]
            int r11 = r13 - r11
            float r11 = (float) r11
            r48 = r4
            int[] r4 = r8.mTempLocation
            r49 = r5
            r5 = 1
            r4 = r4[r5]
            int r4 = r14 - r4
            float r4 = (float) r4
            android.graphics.Path r11 = r0.getPath(r1, r2, r11, r4)
            android.util.Property<android.graphics.drawable.Drawable, android.graphics.PointF> r0 = DRAWABLE_ORIGIN_PROPERTY
            android.animation.PropertyValuesHolder r22 = android.support.transition.PropertyValuesHolderUtils.ofPointF(r0, r11)
            android.animation.PropertyValuesHolder[] r0 = new android.animation.PropertyValuesHolder[r5]
            r1 = 0
            r0[r1] = r22
            android.animation.ObjectAnimator r5 = android.animation.ObjectAnimator.ofPropertyValuesHolder(r3, r0)
            android.support.transition.ChangeBounds$10 r4 = new android.support.transition.ChangeBounds$10
            r0 = r4
            r1 = r8
            r2 = r15
            r17 = r3
            r6 = r4
            r23 = r48
            r4 = r9
            r50 = r7
            r24 = r49
            r7 = r5
            r5 = r16
            r0.<init>(r2, r3, r4, r5)
            r7.addListener(r6)
            return r7
        L_0x037b:
            r15 = r53
            r18 = r11
            r19 = r12
            r20 = r13
            r21 = r14
            r12 = r10
        L_0x0386:
            r0 = 0
            return r0
        L_0x0388:
            r15 = r53
            r12 = r10
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.transition.ChangeBounds.createAnimator(android.view.ViewGroup, android.support.transition.TransitionValues, android.support.transition.TransitionValues):android.animation.Animator");
    }
}
