package android.support.constraint.solver.widgets;

import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.Cache;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintAnchor.ConnectionType;
import android.support.constraint.solver.widgets.ConstraintAnchor.Strength;
import android.support.constraint.solver.widgets.ConstraintAnchor.Type;
import java.util.ArrayList;

public class ConstraintWidget {
    private static final boolean AUTOTAG_CENTER = false;
    public static final int CHAIN_PACKED = 2;
    public static final int CHAIN_SPREAD = 0;
    public static final int CHAIN_SPREAD_INSIDE = 1;
    public static float DEFAULT_BIAS = 0.5f;
    protected static final int DIRECT = 2;
    public static final int GONE = 8;
    public static final int HORIZONTAL = 0;
    public static final int INVISIBLE = 4;
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    protected static final int SOLVER = 1;
    public static final int UNKNOWN = -1;
    public static final int VERTICAL = 1;
    public static final int VISIBLE = 0;
    protected ArrayList<ConstraintAnchor> mAnchors;
    ConstraintAnchor mBaseline;
    int mBaselineDistance;
    ConstraintAnchor mBottom;
    boolean mBottomHasCentered;
    ConstraintAnchor mCenter;
    ConstraintAnchor mCenterX;
    ConstraintAnchor mCenterY;
    private Object mCompanionWidget;
    private int mContainerItemSkip;
    private String mDebugName;
    protected float mDimensionRatio;
    protected int mDimensionRatioSide;
    int mDistToBottom;
    int mDistToLeft;
    int mDistToRight;
    int mDistToTop;
    private int mDrawHeight;
    private int mDrawWidth;
    private int mDrawX;
    private int mDrawY;
    int mHeight;
    float mHorizontalBiasPercent;
    boolean mHorizontalChainFixedPosition;
    int mHorizontalChainStyle;
    DimensionBehaviour mHorizontalDimensionBehaviour;
    ConstraintWidget mHorizontalNextWidget;
    public int mHorizontalResolution;
    float mHorizontalWeight;
    boolean mHorizontalWrapVisited;
    ConstraintAnchor mLeft;
    boolean mLeftHasCentered;
    int mMatchConstraintDefaultHeight;
    int mMatchConstraintDefaultWidth;
    int mMatchConstraintMaxHeight;
    int mMatchConstraintMaxWidth;
    int mMatchConstraintMinHeight;
    int mMatchConstraintMinWidth;
    protected int mMinHeight;
    protected int mMinWidth;
    protected int mOffsetX;
    protected int mOffsetY;
    ConstraintWidget mParent;
    ConstraintAnchor mRight;
    boolean mRightHasCentered;
    private int mSolverBottom;
    private int mSolverLeft;
    private int mSolverRight;
    private int mSolverTop;
    ConstraintAnchor mTop;
    boolean mTopHasCentered;
    private String mType;
    float mVerticalBiasPercent;
    boolean mVerticalChainFixedPosition;
    int mVerticalChainStyle;
    DimensionBehaviour mVerticalDimensionBehaviour;
    ConstraintWidget mVerticalNextWidget;
    public int mVerticalResolution;
    float mVerticalWeight;
    boolean mVerticalWrapVisited;
    private int mVisibility;
    int mWidth;
    private int mWrapHeight;
    private int mWrapWidth;

    /* renamed from: mX */
    protected int f26mX;

    /* renamed from: mY */
    protected int f27mY;

    public enum ContentAlignment {
        BEGIN,
        MIDDLE,
        END,
        TOP,
        VERTICAL_MIDDLE,
        BOTTOM,
        LEFT,
        RIGHT
    }

    public enum DimensionBehaviour {
        FIXED,
        WRAP_CONTENT,
        MATCH_CONSTRAINT,
        MATCH_PARENT
    }

    public void reset() {
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.f26mX = 0;
        this.f27mY = 0;
        this.mDrawX = 0;
        this.mDrawY = 0;
        this.mDrawWidth = 0;
        this.mDrawHeight = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        this.mWrapWidth = 0;
        this.mWrapHeight = 0;
        this.mHorizontalBiasPercent = DEFAULT_BIAS;
        this.mVerticalBiasPercent = DEFAULT_BIAS;
        this.mHorizontalDimensionBehaviour = DimensionBehaviour.FIXED;
        this.mVerticalDimensionBehaviour = DimensionBehaviour.FIXED;
        this.mCompanionWidget = null;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mHorizontalWrapVisited = false;
        this.mVerticalWrapVisited = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalChainFixedPosition = false;
        this.mVerticalChainFixedPosition = false;
        this.mHorizontalWeight = 0.0f;
        this.mVerticalWeight = 0.0f;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
    }

    public ConstraintWidget() {
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mLeft = new ConstraintAnchor(this, Type.LEFT);
        this.mTop = new ConstraintAnchor(this, Type.TOP);
        this.mRight = new ConstraintAnchor(this, Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, Type.CENTER_Y);
        this.mCenter = new ConstraintAnchor(this, Type.CENTER);
        this.mAnchors = new ArrayList<>();
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mSolverLeft = 0;
        this.mSolverTop = 0;
        this.mSolverRight = 0;
        this.mSolverBottom = 0;
        this.f26mX = 0;
        this.f27mY = 0;
        this.mDrawX = 0;
        this.mDrawY = 0;
        this.mDrawWidth = 0;
        this.mDrawHeight = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mHorizontalBiasPercent = DEFAULT_BIAS;
        this.mVerticalBiasPercent = DEFAULT_BIAS;
        this.mHorizontalDimensionBehaviour = DimensionBehaviour.FIXED;
        this.mVerticalDimensionBehaviour = DimensionBehaviour.FIXED;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalWeight = 0.0f;
        this.mVerticalWeight = 0.0f;
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        addAnchors();
    }

    public ConstraintWidget(int x, int y, int width, int height) {
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mLeft = new ConstraintAnchor(this, Type.LEFT);
        this.mTop = new ConstraintAnchor(this, Type.TOP);
        this.mRight = new ConstraintAnchor(this, Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, Type.CENTER_Y);
        this.mCenter = new ConstraintAnchor(this, Type.CENTER);
        this.mAnchors = new ArrayList<>();
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mSolverLeft = 0;
        this.mSolverTop = 0;
        this.mSolverRight = 0;
        this.mSolverBottom = 0;
        this.f26mX = 0;
        this.f27mY = 0;
        this.mDrawX = 0;
        this.mDrawY = 0;
        this.mDrawWidth = 0;
        this.mDrawHeight = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mHorizontalBiasPercent = DEFAULT_BIAS;
        this.mVerticalBiasPercent = DEFAULT_BIAS;
        this.mHorizontalDimensionBehaviour = DimensionBehaviour.FIXED;
        this.mVerticalDimensionBehaviour = DimensionBehaviour.FIXED;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalWeight = 0.0f;
        this.mVerticalWeight = 0.0f;
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.f26mX = x;
        this.f27mY = y;
        this.mWidth = width;
        this.mHeight = height;
        addAnchors();
        forceUpdateDrawPosition();
    }

    public ConstraintWidget(int width, int height) {
        this(0, 0, width, height);
    }

    public void resetSolverVariables(Cache cache) {
        this.mLeft.resetSolverVariable(cache);
        this.mTop.resetSolverVariable(cache);
        this.mRight.resetSolverVariable(cache);
        this.mBottom.resetSolverVariable(cache);
        this.mBaseline.resetSolverVariable(cache);
        this.mCenter.resetSolverVariable(cache);
        this.mCenterX.resetSolverVariable(cache);
        this.mCenterY.resetSolverVariable(cache);
    }

    public void resetGroups() {
        int numAnchors = this.mAnchors.size();
        for (int i = 0; i < numAnchors; i++) {
            ((ConstraintAnchor) this.mAnchors.get(i)).mGroup = Integer.MAX_VALUE;
        }
    }

    private void addAnchors() {
        this.mAnchors.add(this.mLeft);
        this.mAnchors.add(this.mTop);
        this.mAnchors.add(this.mRight);
        this.mAnchors.add(this.mBottom);
        this.mAnchors.add(this.mCenterX);
        this.mAnchors.add(this.mCenterY);
        this.mAnchors.add(this.mBaseline);
    }

    public boolean isRoot() {
        return this.mParent == null;
    }

    public boolean isRootContainer() {
        return (this instanceof ConstraintWidgetContainer) && (this.mParent == null || !(this.mParent instanceof ConstraintWidgetContainer));
    }

    public boolean isInsideConstraintLayout() {
        ConstraintWidget widget = getParent();
        if (widget == null) {
            return false;
        }
        while (widget != null) {
            if (widget instanceof ConstraintWidgetContainer) {
                return true;
            }
            widget = widget.getParent();
        }
        return false;
    }

    public boolean hasAncestor(ConstraintWidget widget) {
        ConstraintWidget parent = getParent();
        if (parent == widget) {
            return true;
        }
        if (parent == widget.getParent()) {
            return false;
        }
        while (parent != null) {
            if (parent == widget || parent == widget.getParent()) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }

    public WidgetContainer getRootWidgetContainer() {
        ConstraintWidget root = this;
        while (root.getParent() != null) {
            root = root.getParent();
        }
        if (root instanceof WidgetContainer) {
            return (WidgetContainer) root;
        }
        return null;
    }

    public ConstraintWidget getParent() {
        return this.mParent;
    }

    public void setParent(ConstraintWidget widget) {
        this.mParent = widget;
    }

    public String getType() {
        return this.mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public void setVisibility(int visibility) {
        this.mVisibility = visibility;
    }

    public int getVisibility() {
        return this.mVisibility;
    }

    public String getDebugName() {
        return this.mDebugName;
    }

    public void setDebugName(String name) {
        this.mDebugName = name;
    }

    public void setDebugSolverName(LinearSystem system, String name) {
        this.mDebugName = name;
        SolverVariable left = system.createObjectVariable(this.mLeft);
        SolverVariable top = system.createObjectVariable(this.mTop);
        SolverVariable right = system.createObjectVariable(this.mRight);
        SolverVariable bottom = system.createObjectVariable(this.mBottom);
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(".left");
        left.setName(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(name);
        sb2.append(".top");
        top.setName(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(name);
        sb3.append(".right");
        right.setName(sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append(name);
        sb4.append(".bottom");
        bottom.setName(sb4.toString());
        if (this.mBaselineDistance > 0) {
            SolverVariable baseline = system.createObjectVariable(this.mBaseline);
            StringBuilder sb5 = new StringBuilder();
            sb5.append(name);
            sb5.append(".baseline");
            baseline.setName(sb5.toString());
        }
    }

    public String toString() {
        String str;
        String str2;
        StringBuilder sb = new StringBuilder();
        if (this.mType != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("type: ");
            sb2.append(this.mType);
            sb2.append(" ");
            str = sb2.toString();
        } else {
            str = "";
        }
        sb.append(str);
        if (this.mDebugName != null) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("id: ");
            sb3.append(this.mDebugName);
            sb3.append(" ");
            str2 = sb3.toString();
        } else {
            str2 = "";
        }
        sb.append(str2);
        sb.append("(");
        sb.append(this.f26mX);
        sb.append(", ");
        sb.append(this.f27mY);
        sb.append(") - (");
        sb.append(this.mWidth);
        sb.append(" x ");
        sb.append(this.mHeight);
        sb.append(")");
        sb.append(" wrap: (");
        sb.append(this.mWrapWidth);
        sb.append(" x ");
        sb.append(this.mWrapHeight);
        sb.append(")");
        return sb.toString();
    }

    /* access modifiers changed from: 0000 */
    public int getInternalDrawX() {
        return this.mDrawX;
    }

    /* access modifiers changed from: 0000 */
    public int getInternalDrawY() {
        return this.mDrawY;
    }

    public int getInternalDrawRight() {
        return this.mDrawX + this.mDrawWidth;
    }

    public int getInternalDrawBottom() {
        return this.mDrawY + this.mDrawHeight;
    }

    public int getX() {
        return this.f26mX;
    }

    public int getY() {
        return this.f27mY;
    }

    public int getWidth() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mWidth;
    }

    public int getOptimizerWrapWidth() {
        int w;
        int w2 = this.mWidth;
        if (this.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) {
            return w2;
        }
        if (this.mMatchConstraintDefaultWidth == 1) {
            w = Math.max(this.mMatchConstraintMinWidth, w2);
        } else if (this.mMatchConstraintMinWidth > 0) {
            w = this.mMatchConstraintMinWidth;
            this.mWidth = w;
        } else {
            w = 0;
        }
        if (this.mMatchConstraintMaxWidth <= 0 || this.mMatchConstraintMaxWidth >= w) {
            return w;
        }
        return this.mMatchConstraintMaxWidth;
    }

    public int getOptimizerWrapHeight() {
        int h;
        int h2 = this.mHeight;
        if (this.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) {
            return h2;
        }
        if (this.mMatchConstraintDefaultHeight == 1) {
            h = Math.max(this.mMatchConstraintMinHeight, h2);
        } else if (this.mMatchConstraintMinHeight > 0) {
            h = this.mMatchConstraintMinHeight;
            this.mHeight = h;
        } else {
            h = 0;
        }
        if (this.mMatchConstraintMaxHeight <= 0 || this.mMatchConstraintMaxHeight >= h) {
            return h;
        }
        return this.mMatchConstraintMaxHeight;
    }

    public int getWrapWidth() {
        return this.mWrapWidth;
    }

    public int getHeight() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mHeight;
    }

    public int getWrapHeight() {
        return this.mWrapHeight;
    }

    public int getDrawX() {
        return this.mDrawX + this.mOffsetX;
    }

    public int getDrawY() {
        return this.mDrawY + this.mOffsetY;
    }

    public int getDrawWidth() {
        return this.mDrawWidth;
    }

    public int getDrawHeight() {
        return this.mDrawHeight;
    }

    public int getDrawBottom() {
        return getDrawY() + this.mDrawHeight;
    }

    public int getDrawRight() {
        return getDrawX() + this.mDrawWidth;
    }

    /* access modifiers changed from: protected */
    public int getRootX() {
        return this.f26mX + this.mOffsetX;
    }

    /* access modifiers changed from: protected */
    public int getRootY() {
        return this.f27mY + this.mOffsetY;
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    public int getMinHeight() {
        return this.mMinHeight;
    }

    public int getLeft() {
        return getX();
    }

    public int getTop() {
        return getY();
    }

    public int getRight() {
        return getX() + this.mWidth;
    }

    public int getBottom() {
        return getY() + this.mHeight;
    }

    public float getHorizontalBiasPercent() {
        return this.mHorizontalBiasPercent;
    }

    public float getVerticalBiasPercent() {
        return this.mVerticalBiasPercent;
    }

    public boolean hasBaseline() {
        return this.mBaselineDistance > 0;
    }

    public int getBaselineDistance() {
        return this.mBaselineDistance;
    }

    public Object getCompanionWidget() {
        return this.mCompanionWidget;
    }

    public ArrayList<ConstraintAnchor> getAnchors() {
        return this.mAnchors;
    }

    public void setX(int x) {
        this.f26mX = x;
    }

    public void setY(int y) {
        this.f27mY = y;
    }

    public void setOrigin(int x, int y) {
        this.f26mX = x;
        this.f27mY = y;
    }

    public void setOffset(int x, int y) {
        this.mOffsetX = x;
        this.mOffsetY = y;
    }

    public void setGoneMargin(Type type, int goneMargin) {
        switch (type) {
            case LEFT:
                this.mLeft.mGoneMargin = goneMargin;
                return;
            case TOP:
                this.mTop.mGoneMargin = goneMargin;
                return;
            case RIGHT:
                this.mRight.mGoneMargin = goneMargin;
                return;
            case BOTTOM:
                this.mBottom.mGoneMargin = goneMargin;
                return;
            default:
                return;
        }
    }

    public void updateDrawPosition() {
        int left = this.f26mX;
        int top = this.f27mY;
        int right = this.f26mX + this.mWidth;
        int bottom = this.f27mY + this.mHeight;
        this.mDrawX = left;
        this.mDrawY = top;
        this.mDrawWidth = right - left;
        this.mDrawHeight = bottom - top;
    }

    public void forceUpdateDrawPosition() {
        int left = this.f26mX;
        int top = this.f27mY;
        int right = this.f26mX + this.mWidth;
        int bottom = this.f27mY + this.mHeight;
        this.mDrawX = left;
        this.mDrawY = top;
        this.mDrawWidth = right - left;
        this.mDrawHeight = bottom - top;
    }

    public void setDrawOrigin(int x, int y) {
        this.mDrawX = x - this.mOffsetX;
        this.mDrawY = y - this.mOffsetY;
        this.f26mX = this.mDrawX;
        this.f27mY = this.mDrawY;
    }

    public void setDrawX(int x) {
        this.mDrawX = x - this.mOffsetX;
        this.f26mX = this.mDrawX;
    }

    public void setDrawY(int y) {
        this.mDrawY = y - this.mOffsetY;
        this.f27mY = this.mDrawY;
    }

    public void setDrawWidth(int drawWidth) {
        this.mDrawWidth = drawWidth;
    }

    public void setDrawHeight(int drawHeight) {
        this.mDrawHeight = drawHeight;
    }

    public void setWidth(int w) {
        this.mWidth = w;
        if (this.mWidth < this.mMinWidth) {
            this.mWidth = this.mMinWidth;
        }
    }

    public void setHeight(int h) {
        this.mHeight = h;
        if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
        }
    }

    public void setHorizontalMatchStyle(int horizontalMatchStyle, int min, int max) {
        this.mMatchConstraintDefaultWidth = horizontalMatchStyle;
        this.mMatchConstraintMinWidth = min;
        this.mMatchConstraintMaxWidth = max;
    }

    public void setVerticalMatchStyle(int verticalMatchStyle, int min, int max) {
        this.mMatchConstraintDefaultHeight = verticalMatchStyle;
        this.mMatchConstraintMinHeight = min;
        this.mMatchConstraintMaxHeight = max;
    }

    public void setDimensionRatio(String ratio) {
        int commaIndex;
        if (ratio == null || ratio.length() == 0) {
            this.mDimensionRatio = 0.0f;
            return;
        }
        int dimensionRatioSide = -1;
        float dimensionRatio = 0.0f;
        int len = ratio.length();
        int commaIndex2 = ratio.indexOf(44);
        if (commaIndex2 <= 0 || commaIndex2 >= len - 1) {
            commaIndex = 0;
        } else {
            String dimension = ratio.substring(0, commaIndex2);
            if (dimension.equalsIgnoreCase("W")) {
                dimensionRatioSide = 0;
            } else if (dimension.equalsIgnoreCase("H")) {
                dimensionRatioSide = 1;
            }
            commaIndex = commaIndex2 + 1;
        }
        int colonIndex = ratio.indexOf(58);
        if (colonIndex < 0 || colonIndex >= len - 1) {
            String r = ratio.substring(commaIndex);
            if (r.length() > 0) {
                try {
                    dimensionRatio = Float.parseFloat(r);
                } catch (NumberFormatException e) {
                }
            }
        } else {
            String nominator = ratio.substring(commaIndex, colonIndex);
            String denominator = ratio.substring(colonIndex + 1);
            if (nominator.length() > 0 && denominator.length() > 0) {
                try {
                    float nominatorValue = Float.parseFloat(nominator);
                    float denominatorValue = Float.parseFloat(denominator);
                    if (nominatorValue > 0.0f && denominatorValue > 0.0f) {
                        dimensionRatio = dimensionRatioSide == 1 ? Math.abs(denominatorValue / nominatorValue) : Math.abs(nominatorValue / denominatorValue);
                    }
                } catch (NumberFormatException e2) {
                }
            }
        }
        if (dimensionRatio > 0.0f) {
            this.mDimensionRatio = dimensionRatio;
            this.mDimensionRatioSide = dimensionRatioSide;
        }
    }

    public void setDimensionRatio(float ratio, int dimensionRatioSide) {
        this.mDimensionRatio = ratio;
        this.mDimensionRatioSide = dimensionRatioSide;
    }

    public float getDimensionRatio() {
        return this.mDimensionRatio;
    }

    public int getDimensionRatioSide() {
        return this.mDimensionRatioSide;
    }

    public void setHorizontalBiasPercent(float horizontalBiasPercent) {
        this.mHorizontalBiasPercent = horizontalBiasPercent;
    }

    public void setVerticalBiasPercent(float verticalBiasPercent) {
        this.mVerticalBiasPercent = verticalBiasPercent;
    }

    public void setMinWidth(int w) {
        if (w < 0) {
            this.mMinWidth = 0;
        } else {
            this.mMinWidth = w;
        }
    }

    public void setMinHeight(int h) {
        if (h < 0) {
            this.mMinHeight = 0;
        } else {
            this.mMinHeight = h;
        }
    }

    public void setWrapWidth(int w) {
        this.mWrapWidth = w;
    }

    public void setWrapHeight(int h) {
        this.mWrapHeight = h;
    }

    public void setDimension(int w, int h) {
        this.mWidth = w;
        if (this.mWidth < this.mMinWidth) {
            this.mWidth = this.mMinWidth;
        }
        this.mHeight = h;
        if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
        }
    }

    public void setFrame(int left, int top, int right, int bottom) {
        int w = right - left;
        int h = bottom - top;
        this.f26mX = left;
        this.f27mY = top;
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.FIXED && w < this.mWidth) {
            w = this.mWidth;
        }
        if (this.mVerticalDimensionBehaviour == DimensionBehaviour.FIXED && h < this.mHeight) {
            h = this.mHeight;
        }
        this.mWidth = w;
        this.mHeight = h;
        if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
        }
        if (this.mWidth < this.mMinWidth) {
            this.mWidth = this.mMinWidth;
        }
    }

    public void setHorizontalDimension(int left, int right) {
        this.f26mX = left;
        this.mWidth = right - left;
        if (this.mWidth < this.mMinWidth) {
            this.mWidth = this.mMinWidth;
        }
    }

    public void setVerticalDimension(int top, int bottom) {
        this.f27mY = top;
        this.mHeight = bottom - top;
        if (this.mHeight < this.mMinHeight) {
            this.mHeight = this.mMinHeight;
        }
    }

    public void setBaselineDistance(int baseline) {
        this.mBaselineDistance = baseline;
    }

    public void setCompanionWidget(Object companion) {
        this.mCompanionWidget = companion;
    }

    public void setContainerItemSkip(int skip) {
        if (skip >= 0) {
            this.mContainerItemSkip = skip;
        } else {
            this.mContainerItemSkip = 0;
        }
    }

    public int getContainerItemSkip() {
        return this.mContainerItemSkip;
    }

    public void setHorizontalWeight(float horizontalWeight) {
        this.mHorizontalWeight = horizontalWeight;
    }

    public void setVerticalWeight(float verticalWeight) {
        this.mVerticalWeight = verticalWeight;
    }

    public void setHorizontalChainStyle(int horizontalChainStyle) {
        this.mHorizontalChainStyle = horizontalChainStyle;
    }

    public int getHorizontalChainStyle() {
        return this.mHorizontalChainStyle;
    }

    public void setVerticalChainStyle(int verticalChainStyle) {
        this.mVerticalChainStyle = verticalChainStyle;
    }

    public int getVerticalChainStyle() {
        return this.mVerticalChainStyle;
    }

    public void connectedTo(ConstraintWidget source) {
    }

    public void immediateConnect(Type startType, ConstraintWidget target, Type endType, int margin, int goneMargin) {
        ConstraintAnchor startAnchor = getAnchor(startType);
        startAnchor.connect(target.getAnchor(endType), margin, goneMargin, Strength.STRONG, 0, true);
    }

    public void connect(ConstraintAnchor from, ConstraintAnchor to, int margin, int creator) {
        connect(from, to, margin, Strength.STRONG, creator);
    }

    public void connect(ConstraintAnchor from, ConstraintAnchor to, int margin) {
        connect(from, to, margin, Strength.STRONG, 0);
    }

    public void connect(ConstraintAnchor from, ConstraintAnchor to, int margin, Strength strength, int creator) {
        if (from.getOwner() == this) {
            connect(from.getType(), to.getOwner(), to.getType(), margin, strength, creator);
        }
    }

    public void connect(Type constraintFrom, ConstraintWidget target, Type constraintTo, int margin) {
        connect(constraintFrom, target, constraintTo, margin, Strength.STRONG);
    }

    public void connect(Type constraintFrom, ConstraintWidget target, Type constraintTo) {
        connect(constraintFrom, target, constraintTo, 0, Strength.STRONG);
    }

    public void connect(Type constraintFrom, ConstraintWidget target, Type constraintTo, int margin, Strength strength) {
        connect(constraintFrom, target, constraintTo, margin, strength, 0);
    }

    public void connect(Type constraintFrom, ConstraintWidget target, Type constraintTo, int margin, Strength strength, int creator) {
        int margin2;
        ConstraintAnchor bottom;
        Type type = constraintFrom;
        ConstraintWidget constraintWidget = target;
        Type type2 = constraintTo;
        int i = creator;
        if (type == Type.CENTER) {
            if (type2 == Type.CENTER) {
                ConstraintAnchor left = getAnchor(Type.LEFT);
                ConstraintAnchor right = getAnchor(Type.RIGHT);
                ConstraintAnchor top = getAnchor(Type.TOP);
                ConstraintAnchor bottom2 = getAnchor(Type.BOTTOM);
                boolean centerX = false;
                boolean centerY = false;
                if ((left == null || !left.isConnected()) && (right == null || !right.isConnected())) {
                    ConstraintWidget constraintWidget2 = constraintWidget;
                    Strength strength2 = strength;
                    bottom = bottom2;
                    int i2 = i;
                    connect(Type.LEFT, constraintWidget2, Type.LEFT, 0, strength2, i2);
                    connect(Type.RIGHT, constraintWidget2, Type.RIGHT, 0, strength2, i2);
                    centerX = true;
                } else {
                    bottom = bottom2;
                }
                if ((top == null || !top.isConnected()) && (bottom == null || !bottom.isConnected())) {
                    ConstraintWidget constraintWidget3 = constraintWidget;
                    Strength strength3 = strength;
                    int i3 = i;
                    connect(Type.TOP, constraintWidget3, Type.TOP, 0, strength3, i3);
                    connect(Type.BOTTOM, constraintWidget3, Type.BOTTOM, 0, strength3, i3);
                    centerY = true;
                }
                if (centerX && centerY) {
                    getAnchor(Type.CENTER).connect(constraintWidget.getAnchor(Type.CENTER), 0, i);
                } else if (centerX) {
                    getAnchor(Type.CENTER_X).connect(constraintWidget.getAnchor(Type.CENTER_X), 0, i);
                } else if (centerY) {
                    getAnchor(Type.CENTER_Y).connect(constraintWidget.getAnchor(Type.CENTER_Y), 0, i);
                }
            } else if (type2 == Type.LEFT || type2 == Type.RIGHT) {
                ConstraintWidget constraintWidget4 = constraintWidget;
                Type type3 = type2;
                Strength strength4 = strength;
                int i4 = i;
                connect(Type.LEFT, constraintWidget4, type3, 0, strength4, i4);
                connect(Type.RIGHT, constraintWidget4, type3, 0, strength4, i4);
                getAnchor(Type.CENTER).connect(target.getAnchor(constraintTo), 0, i);
            } else if (type2 == Type.TOP || type2 == Type.BOTTOM) {
                ConstraintWidget constraintWidget5 = constraintWidget;
                Type type4 = type2;
                Strength strength5 = strength;
                int i5 = i;
                connect(Type.TOP, constraintWidget5, type4, 0, strength5, i5);
                connect(Type.BOTTOM, constraintWidget5, type4, 0, strength5, i5);
                getAnchor(Type.CENTER).connect(target.getAnchor(constraintTo), 0, i);
            }
        } else if (type == Type.CENTER_X && (type2 == Type.LEFT || type2 == Type.RIGHT)) {
            ConstraintAnchor left2 = getAnchor(Type.LEFT);
            ConstraintAnchor targetAnchor = target.getAnchor(constraintTo);
            ConstraintAnchor right2 = getAnchor(Type.RIGHT);
            left2.connect(targetAnchor, 0, i);
            right2.connect(targetAnchor, 0, i);
            getAnchor(Type.CENTER_X).connect(targetAnchor, 0, i);
        } else if (type == Type.CENTER_Y && (type2 == Type.TOP || type2 == Type.BOTTOM)) {
            ConstraintAnchor targetAnchor2 = target.getAnchor(constraintTo);
            getAnchor(Type.TOP).connect(targetAnchor2, 0, i);
            getAnchor(Type.BOTTOM).connect(targetAnchor2, 0, i);
            getAnchor(Type.CENTER_Y).connect(targetAnchor2, 0, i);
        } else if (type == Type.CENTER_X && type2 == Type.CENTER_X) {
            getAnchor(Type.LEFT).connect(constraintWidget.getAnchor(Type.LEFT), 0, i);
            getAnchor(Type.RIGHT).connect(constraintWidget.getAnchor(Type.RIGHT), 0, i);
            getAnchor(Type.CENTER_X).connect(target.getAnchor(constraintTo), 0, i);
        } else if (type == Type.CENTER_Y && type2 == Type.CENTER_Y) {
            getAnchor(Type.TOP).connect(constraintWidget.getAnchor(Type.TOP), 0, i);
            getAnchor(Type.BOTTOM).connect(constraintWidget.getAnchor(Type.BOTTOM), 0, i);
            getAnchor(Type.CENTER_Y).connect(target.getAnchor(constraintTo), 0, i);
        } else {
            ConstraintAnchor fromAnchor = getAnchor(constraintFrom);
            ConstraintAnchor toAnchor = target.getAnchor(constraintTo);
            if (fromAnchor.isValidConnection(toAnchor)) {
                if (type == Type.BASELINE) {
                    ConstraintAnchor top2 = getAnchor(Type.TOP);
                    ConstraintAnchor bottom3 = getAnchor(Type.BOTTOM);
                    if (top2 != null) {
                        top2.reset();
                    }
                    if (bottom3 != null) {
                        bottom3.reset();
                    }
                    margin2 = 0;
                } else {
                    if (type == Type.TOP || type == Type.BOTTOM) {
                        ConstraintAnchor baseline = getAnchor(Type.BASELINE);
                        if (baseline != null) {
                            baseline.reset();
                        }
                        ConstraintAnchor center = getAnchor(Type.CENTER);
                        if (center.getTarget() != toAnchor) {
                            center.reset();
                        }
                        ConstraintAnchor opposite = getAnchor(constraintFrom).getOpposite();
                        ConstraintAnchor centerY2 = getAnchor(Type.CENTER_Y);
                        if (centerY2.isConnected()) {
                            opposite.reset();
                            centerY2.reset();
                        }
                    } else if (type == Type.LEFT || type == Type.RIGHT) {
                        ConstraintAnchor center2 = getAnchor(Type.CENTER);
                        if (center2.getTarget() != toAnchor) {
                            center2.reset();
                        }
                        ConstraintAnchor opposite2 = getAnchor(constraintFrom).getOpposite();
                        ConstraintAnchor centerX2 = getAnchor(Type.CENTER_X);
                        if (centerX2.isConnected()) {
                            opposite2.reset();
                            centerX2.reset();
                        }
                    }
                    margin2 = margin;
                }
                fromAnchor.connect(toAnchor, margin2, strength, i);
                toAnchor.getOwner().connectedTo(fromAnchor.getOwner());
                return;
            }
        }
        Strength strength6 = strength;
    }

    public void resetAllConstraints() {
        resetAnchors();
        setVerticalBiasPercent(DEFAULT_BIAS);
        setHorizontalBiasPercent(DEFAULT_BIAS);
        if (!(this instanceof ConstraintWidgetContainer)) {
            if (getHorizontalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
                if (getWidth() == getWrapWidth()) {
                    setHorizontalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
                } else if (getWidth() > getMinWidth()) {
                    setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
            }
            if (getVerticalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
                if (getHeight() == getWrapHeight()) {
                    setVerticalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
                } else if (getHeight() > getMinHeight()) {
                    setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
            }
        }
    }

    public void resetAnchor(ConstraintAnchor anchor) {
        if (getParent() == null || !(getParent() instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer) getParent()).handlesInternalConstraints()) {
            ConstraintAnchor left = getAnchor(Type.LEFT);
            ConstraintAnchor right = getAnchor(Type.RIGHT);
            ConstraintAnchor top = getAnchor(Type.TOP);
            ConstraintAnchor bottom = getAnchor(Type.BOTTOM);
            ConstraintAnchor center = getAnchor(Type.CENTER);
            ConstraintAnchor centerX = getAnchor(Type.CENTER_X);
            ConstraintAnchor centerY = getAnchor(Type.CENTER_Y);
            if (anchor == center) {
                if (left.isConnected() && right.isConnected() && left.getTarget() == right.getTarget()) {
                    left.reset();
                    right.reset();
                }
                if (top.isConnected() && bottom.isConnected() && top.getTarget() == bottom.getTarget()) {
                    top.reset();
                    bottom.reset();
                }
                this.mHorizontalBiasPercent = 0.5f;
                this.mVerticalBiasPercent = 0.5f;
            } else if (anchor == centerX) {
                if (left.isConnected() && right.isConnected() && left.getTarget().getOwner() == right.getTarget().getOwner()) {
                    left.reset();
                    right.reset();
                }
                this.mHorizontalBiasPercent = 0.5f;
            } else if (anchor == centerY) {
                if (top.isConnected() && bottom.isConnected() && top.getTarget().getOwner() == bottom.getTarget().getOwner()) {
                    top.reset();
                    bottom.reset();
                }
                this.mVerticalBiasPercent = 0.5f;
            } else if (anchor == left || anchor == right) {
                if (left.isConnected() && left.getTarget() == right.getTarget()) {
                    center.reset();
                }
            } else if ((anchor == top || anchor == bottom) && top.isConnected() && top.getTarget() == bottom.getTarget()) {
                center.reset();
            }
            anchor.reset();
        }
    }

    public void resetAnchors() {
        ConstraintWidget parent = getParent();
        if (parent == null || !(parent instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer) getParent()).handlesInternalConstraints()) {
            int mAnchorsSize = this.mAnchors.size();
            for (int i = 0; i < mAnchorsSize; i++) {
                ((ConstraintAnchor) this.mAnchors.get(i)).reset();
            }
        }
    }

    public void resetAnchors(int connectionCreator) {
        ConstraintWidget parent = getParent();
        if (parent == null || !(parent instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer) getParent()).handlesInternalConstraints()) {
            int mAnchorsSize = this.mAnchors.size();
            for (int i = 0; i < mAnchorsSize; i++) {
                ConstraintAnchor anchor = (ConstraintAnchor) this.mAnchors.get(i);
                if (connectionCreator == anchor.getConnectionCreator()) {
                    if (anchor.isVerticalAnchor()) {
                        setVerticalBiasPercent(DEFAULT_BIAS);
                    } else {
                        setHorizontalBiasPercent(DEFAULT_BIAS);
                    }
                    anchor.reset();
                }
            }
        }
    }

    public void disconnectWidget(ConstraintWidget widget) {
        ArrayList<ConstraintAnchor> anchors = getAnchors();
        int anchorsSize = anchors.size();
        for (int i = 0; i < anchorsSize; i++) {
            ConstraintAnchor anchor = (ConstraintAnchor) anchors.get(i);
            if (anchor.isConnected() && anchor.getTarget().getOwner() == widget) {
                anchor.reset();
            }
        }
    }

    public void disconnectUnlockedWidget(ConstraintWidget widget) {
        ArrayList<ConstraintAnchor> anchors = getAnchors();
        int anchorsSize = anchors.size();
        for (int i = 0; i < anchorsSize; i++) {
            ConstraintAnchor anchor = (ConstraintAnchor) anchors.get(i);
            if (anchor.isConnected() && anchor.getTarget().getOwner() == widget && anchor.getConnectionCreator() == 2) {
                anchor.reset();
            }
        }
    }

    public ConstraintAnchor getAnchor(Type anchorType) {
        switch (anchorType) {
            case LEFT:
                return this.mLeft;
            case TOP:
                return this.mTop;
            case RIGHT:
                return this.mRight;
            case BOTTOM:
                return this.mBottom;
            case BASELINE:
                return this.mBaseline;
            case CENTER_X:
                return this.mCenterX;
            case CENTER_Y:
                return this.mCenterY;
            case CENTER:
                return this.mCenter;
            default:
                return null;
        }
    }

    public DimensionBehaviour getHorizontalDimensionBehaviour() {
        return this.mHorizontalDimensionBehaviour;
    }

    public DimensionBehaviour getVerticalDimensionBehaviour() {
        return this.mVerticalDimensionBehaviour;
    }

    public void setHorizontalDimensionBehaviour(DimensionBehaviour behaviour) {
        this.mHorizontalDimensionBehaviour = behaviour;
        if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
            setWidth(this.mWrapWidth);
        }
    }

    public void setVerticalDimensionBehaviour(DimensionBehaviour behaviour) {
        this.mVerticalDimensionBehaviour = behaviour;
        if (this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
            setHeight(this.mWrapHeight);
        }
    }

    public boolean isInHorizontalChain() {
        if ((this.mLeft.mTarget == null || this.mLeft.mTarget.mTarget != this.mLeft) && (this.mRight.mTarget == null || this.mRight.mTarget.mTarget != this.mRight)) {
            return false;
        }
        return true;
    }

    public ConstraintWidget getHorizontalChainControlWidget() {
        if (!isInHorizontalChain()) {
            return null;
        }
        ConstraintWidget found = null;
        ConstraintWidget tmp = this;
        while (found == null && tmp != null) {
            ConstraintAnchor anchor = tmp.getAnchor(Type.LEFT);
            ConstraintAnchor targetAnchor = null;
            ConstraintAnchor targetOwner = anchor == null ? null : anchor.getTarget();
            ConstraintWidget target = targetOwner == null ? null : targetOwner.getOwner();
            if (target == getParent()) {
                return tmp;
            }
            if (target != null) {
                targetAnchor = target.getAnchor(Type.RIGHT).getTarget();
            }
            if (targetAnchor == null || targetAnchor.getOwner() == tmp) {
                tmp = target;
            } else {
                found = tmp;
            }
        }
        return found;
    }

    public boolean isInVerticalChain() {
        if ((this.mTop.mTarget == null || this.mTop.mTarget.mTarget != this.mTop) && (this.mBottom.mTarget == null || this.mBottom.mTarget.mTarget != this.mBottom)) {
            return false;
        }
        return true;
    }

    public ConstraintWidget getVerticalChainControlWidget() {
        if (!isInVerticalChain()) {
            return null;
        }
        ConstraintWidget found = null;
        ConstraintWidget tmp = this;
        while (found == null && tmp != null) {
            ConstraintAnchor anchor = tmp.getAnchor(Type.TOP);
            ConstraintAnchor targetAnchor = null;
            ConstraintAnchor targetOwner = anchor == null ? null : anchor.getTarget();
            ConstraintWidget target = targetOwner == null ? null : targetOwner.getOwner();
            if (target == getParent()) {
                return tmp;
            }
            if (target != null) {
                targetAnchor = target.getAnchor(Type.BOTTOM).getTarget();
            }
            if (targetAnchor == null || targetAnchor.getOwner() == tmp) {
                tmp = target;
            } else {
                found = tmp;
            }
        }
        return found;
    }

    public void addToSolver(LinearSystem system) {
        addToSolver(system, Integer.MAX_VALUE);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:206:0x03e5, code lost:
        if (r14 == -1) goto L_0x03e9;
     */
    /* JADX WARNING: Removed duplicated region for block: B:173:0x02cc  */
    /* JADX WARNING: Removed duplicated region for block: B:191:0x03b9  */
    /* JADX WARNING: Removed duplicated region for block: B:194:0x03ce A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:195:0x03cf  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addToSolver(android.support.constraint.solver.LinearSystem r49, int r50) {
        /*
            r48 = this;
            r15 = r48
            r14 = r49
            r13 = r50
            r0 = 0
            r1 = 0
            r2 = 0
            r3 = 0
            r4 = 0
            r12 = 2147483647(0x7fffffff, float:NaN)
            if (r13 == r12) goto L_0x0016
            android.support.constraint.solver.widgets.ConstraintAnchor r5 = r15.mLeft
            int r5 = r5.mGroup
            if (r5 != r13) goto L_0x001c
        L_0x0016:
            android.support.constraint.solver.widgets.ConstraintAnchor r5 = r15.mLeft
            android.support.constraint.solver.SolverVariable r0 = r14.createObjectVariable(r5)
        L_0x001c:
            if (r13 == r12) goto L_0x0024
            android.support.constraint.solver.widgets.ConstraintAnchor r5 = r15.mRight
            int r5 = r5.mGroup
            if (r5 != r13) goto L_0x002a
        L_0x0024:
            android.support.constraint.solver.widgets.ConstraintAnchor r5 = r15.mRight
            android.support.constraint.solver.SolverVariable r1 = r14.createObjectVariable(r5)
        L_0x002a:
            if (r13 == r12) goto L_0x0035
            android.support.constraint.solver.widgets.ConstraintAnchor r5 = r15.mTop
            int r5 = r5.mGroup
            if (r5 != r13) goto L_0x0033
            goto L_0x0035
        L_0x0033:
            r11 = r2
            goto L_0x003c
        L_0x0035:
            android.support.constraint.solver.widgets.ConstraintAnchor r5 = r15.mTop
            android.support.constraint.solver.SolverVariable r2 = r14.createObjectVariable(r5)
            goto L_0x0033
        L_0x003c:
            if (r13 == r12) goto L_0x0047
            android.support.constraint.solver.widgets.ConstraintAnchor r2 = r15.mBottom
            int r2 = r2.mGroup
            if (r2 != r13) goto L_0x0045
            goto L_0x0047
        L_0x0045:
            r10 = r3
            goto L_0x004e
        L_0x0047:
            android.support.constraint.solver.widgets.ConstraintAnchor r2 = r15.mBottom
            android.support.constraint.solver.SolverVariable r3 = r14.createObjectVariable(r2)
            goto L_0x0045
        L_0x004e:
            if (r13 == r12) goto L_0x0059
            android.support.constraint.solver.widgets.ConstraintAnchor r2 = r15.mBaseline
            int r2 = r2.mGroup
            if (r2 != r13) goto L_0x0057
            goto L_0x0059
        L_0x0057:
            r9 = r4
            goto L_0x0060
        L_0x0059:
            android.support.constraint.solver.widgets.ConstraintAnchor r2 = r15.mBaseline
            android.support.constraint.solver.SolverVariable r4 = r14.createObjectVariable(r2)
            goto L_0x0057
        L_0x0060:
            r2 = 0
            r3 = 0
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r15.mParent
            r8 = 1
            r7 = 0
            if (r4 == 0) goto L_0x01d0
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mLeft
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            if (r4 == 0) goto L_0x0078
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mLeft
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            android.support.constraint.solver.widgets.ConstraintAnchor r5 = r15.mLeft
            if (r4 == r5) goto L_0x0088
        L_0x0078:
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mRight
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            if (r4 == 0) goto L_0x0090
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mRight
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            android.support.constraint.solver.widgets.ConstraintAnchor r5 = r15.mRight
            if (r4 != r5) goto L_0x0090
        L_0x0088:
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r15.mParent
            android.support.constraint.solver.widgets.ConstraintWidgetContainer r4 = (android.support.constraint.solver.widgets.ConstraintWidgetContainer) r4
            r4.addChain(r15, r7)
            r2 = 1
        L_0x0090:
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mTop
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            if (r4 == 0) goto L_0x00a0
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mTop
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            android.support.constraint.solver.widgets.ConstraintAnchor r5 = r15.mTop
            if (r4 == r5) goto L_0x00b0
        L_0x00a0:
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mBottom
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            if (r4 == 0) goto L_0x00b8
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mBottom
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            android.support.constraint.solver.widgets.ConstraintAnchor r5 = r15.mBottom
            if (r4 != r5) goto L_0x00b8
        L_0x00b0:
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r15.mParent
            android.support.constraint.solver.widgets.ConstraintWidgetContainer r4 = (android.support.constraint.solver.widgets.ConstraintWidgetContainer) r4
            r4.addChain(r15, r8)
            r3 = 1
        L_0x00b8:
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r15.mParent
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r4 = r4.getHorizontalDimensionBehaviour()
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r5 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r4 != r5) goto L_0x0144
            if (r2 != 0) goto L_0x0144
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mLeft
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            if (r4 == 0) goto L_0x00ed
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mLeft
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r4.mOwner
            android.support.constraint.solver.widgets.ConstraintWidget r5 = r15.mParent
            if (r4 == r5) goto L_0x00d5
            goto L_0x00ed
        L_0x00d5:
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mLeft
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            if (r4 == 0) goto L_0x0104
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mLeft
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r4.mOwner
            android.support.constraint.solver.widgets.ConstraintWidget r5 = r15.mParent
            if (r4 != r5) goto L_0x0104
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mLeft
            android.support.constraint.solver.widgets.ConstraintAnchor$ConnectionType r5 = android.support.constraint.solver.widgets.ConstraintAnchor.ConnectionType.STRICT
            r4.setConnectionType(r5)
            goto L_0x0104
        L_0x00ed:
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r15.mParent
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mLeft
            android.support.constraint.solver.SolverVariable r4 = r14.createObjectVariable(r4)
            android.support.constraint.solver.ArrayRow r5 = r49.createRow()
            android.support.constraint.solver.SolverVariable r6 = r49.createSlackVariable()
            r5.createRowGreaterThan(r0, r4, r6, r7)
            r14.addConstraint(r5)
        L_0x0104:
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mRight
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            if (r4 == 0) goto L_0x012d
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mRight
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r4.mOwner
            android.support.constraint.solver.widgets.ConstraintWidget r5 = r15.mParent
            if (r4 == r5) goto L_0x0115
            goto L_0x012d
        L_0x0115:
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mRight
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            if (r4 == 0) goto L_0x0144
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mRight
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r4.mOwner
            android.support.constraint.solver.widgets.ConstraintWidget r5 = r15.mParent
            if (r4 != r5) goto L_0x0144
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mRight
            android.support.constraint.solver.widgets.ConstraintAnchor$ConnectionType r5 = android.support.constraint.solver.widgets.ConstraintAnchor.ConnectionType.STRICT
            r4.setConnectionType(r5)
            goto L_0x0144
        L_0x012d:
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r15.mParent
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mRight
            android.support.constraint.solver.SolverVariable r4 = r14.createObjectVariable(r4)
            android.support.constraint.solver.ArrayRow r5 = r49.createRow()
            android.support.constraint.solver.SolverVariable r6 = r49.createSlackVariable()
            r5.createRowGreaterThan(r4, r1, r6, r7)
            r14.addConstraint(r5)
        L_0x0144:
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r15.mParent
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r4 = r4.getVerticalDimensionBehaviour()
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r5 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r4 != r5) goto L_0x01d0
            if (r3 != 0) goto L_0x01d0
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mTop
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            if (r4 == 0) goto L_0x0179
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mTop
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r4.mOwner
            android.support.constraint.solver.widgets.ConstraintWidget r5 = r15.mParent
            if (r4 == r5) goto L_0x0161
            goto L_0x0179
        L_0x0161:
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mTop
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            if (r4 == 0) goto L_0x0190
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mTop
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r4.mOwner
            android.support.constraint.solver.widgets.ConstraintWidget r5 = r15.mParent
            if (r4 != r5) goto L_0x0190
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mTop
            android.support.constraint.solver.widgets.ConstraintAnchor$ConnectionType r5 = android.support.constraint.solver.widgets.ConstraintAnchor.ConnectionType.STRICT
            r4.setConnectionType(r5)
            goto L_0x0190
        L_0x0179:
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r15.mParent
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTop
            android.support.constraint.solver.SolverVariable r4 = r14.createObjectVariable(r4)
            android.support.constraint.solver.ArrayRow r5 = r49.createRow()
            android.support.constraint.solver.SolverVariable r6 = r49.createSlackVariable()
            r5.createRowGreaterThan(r11, r4, r6, r7)
            r14.addConstraint(r5)
        L_0x0190:
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mBottom
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            if (r4 == 0) goto L_0x01b9
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mBottom
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r4.mOwner
            android.support.constraint.solver.widgets.ConstraintWidget r5 = r15.mParent
            if (r4 == r5) goto L_0x01a1
            goto L_0x01b9
        L_0x01a1:
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mBottom
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            if (r4 == 0) goto L_0x01d0
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mBottom
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r4.mOwner
            android.support.constraint.solver.widgets.ConstraintWidget r5 = r15.mParent
            if (r4 != r5) goto L_0x01d0
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mBottom
            android.support.constraint.solver.widgets.ConstraintAnchor$ConnectionType r5 = android.support.constraint.solver.widgets.ConstraintAnchor.ConnectionType.STRICT
            r4.setConnectionType(r5)
            goto L_0x01d0
        L_0x01b9:
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r15.mParent
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r4.mBottom
            android.support.constraint.solver.SolverVariable r4 = r14.createObjectVariable(r4)
            android.support.constraint.solver.ArrayRow r5 = r49.createRow()
            android.support.constraint.solver.SolverVariable r6 = r49.createSlackVariable()
            r5.createRowGreaterThan(r4, r10, r6, r7)
            r14.addConstraint(r5)
        L_0x01d0:
            r19 = r2
            r20 = r3
            int r2 = r15.mWidth
            int r3 = r15.mMinWidth
            if (r2 >= r3) goto L_0x01dc
            int r2 = r15.mMinWidth
        L_0x01dc:
            int r3 = r15.mHeight
            int r4 = r15.mMinHeight
            if (r3 >= r4) goto L_0x01e4
            int r3 = r15.mMinHeight
        L_0x01e4:
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r4 = r15.mHorizontalDimensionBehaviour
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r5 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r4 == r5) goto L_0x01ec
            r4 = 1
            goto L_0x01ed
        L_0x01ec:
            r4 = 0
        L_0x01ed:
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r5 = r15.mVerticalDimensionBehaviour
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r6 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r5 == r6) goto L_0x01f5
            r5 = 1
            goto L_0x01f6
        L_0x01f5:
            r5 = 0
        L_0x01f6:
            if (r4 != 0) goto L_0x020d
            android.support.constraint.solver.widgets.ConstraintAnchor r6 = r15.mLeft
            if (r6 == 0) goto L_0x020d
            android.support.constraint.solver.widgets.ConstraintAnchor r6 = r15.mRight
            if (r6 == 0) goto L_0x020d
            android.support.constraint.solver.widgets.ConstraintAnchor r6 = r15.mLeft
            android.support.constraint.solver.widgets.ConstraintAnchor r6 = r6.mTarget
            if (r6 == 0) goto L_0x020c
            android.support.constraint.solver.widgets.ConstraintAnchor r6 = r15.mRight
            android.support.constraint.solver.widgets.ConstraintAnchor r6 = r6.mTarget
            if (r6 != 0) goto L_0x020d
        L_0x020c:
            r4 = 1
        L_0x020d:
            if (r5 != 0) goto L_0x0238
            android.support.constraint.solver.widgets.ConstraintAnchor r6 = r15.mTop
            if (r6 == 0) goto L_0x0238
            android.support.constraint.solver.widgets.ConstraintAnchor r6 = r15.mBottom
            if (r6 == 0) goto L_0x0238
            android.support.constraint.solver.widgets.ConstraintAnchor r6 = r15.mTop
            android.support.constraint.solver.widgets.ConstraintAnchor r6 = r6.mTarget
            if (r6 == 0) goto L_0x0223
            android.support.constraint.solver.widgets.ConstraintAnchor r6 = r15.mBottom
            android.support.constraint.solver.widgets.ConstraintAnchor r6 = r6.mTarget
            if (r6 != 0) goto L_0x0238
        L_0x0223:
            int r6 = r15.mBaselineDistance
            if (r6 == 0) goto L_0x0237
            android.support.constraint.solver.widgets.ConstraintAnchor r6 = r15.mBaseline
            if (r6 == 0) goto L_0x0238
            android.support.constraint.solver.widgets.ConstraintAnchor r6 = r15.mTop
            android.support.constraint.solver.widgets.ConstraintAnchor r6 = r6.mTarget
            if (r6 == 0) goto L_0x0237
            android.support.constraint.solver.widgets.ConstraintAnchor r6 = r15.mBaseline
            android.support.constraint.solver.widgets.ConstraintAnchor r6 = r6.mTarget
            if (r6 != 0) goto L_0x0238
        L_0x0237:
            r5 = 1
        L_0x0238:
            r6 = 0
            int r7 = r15.mDimensionRatioSide
            float r8 = r15.mDimensionRatio
            float r12 = r15.mDimensionRatio
            r16 = 0
            int r12 = (r12 > r16 ? 1 : (r12 == r16 ? 0 : -1))
            r24 = r11
            if (r12 <= 0) goto L_0x02a0
            int r12 = r15.mVisibility
            r11 = 8
            if (r12 == r11) goto L_0x02a0
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r11 = r15.mHorizontalDimensionBehaviour
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r12 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            r16 = 1065353216(0x3f800000, float:1.0)
            if (r11 != r12) goto L_0x027c
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r11 = r15.mVerticalDimensionBehaviour
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r12 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r11 != r12) goto L_0x027c
            r6 = 1
            if (r4 == 0) goto L_0x0262
            if (r5 != 0) goto L_0x0262
            r7 = 0
            goto L_0x02a0
        L_0x0262:
            if (r4 != 0) goto L_0x02a0
            if (r5 == 0) goto L_0x02a0
            r7 = 1
            int r11 = r15.mDimensionRatioSide
            r12 = -1
            if (r11 != r12) goto L_0x02a0
            float r16 = r16 / r8
            r27 = r2
            r29 = r3
            r28 = r4
            r30 = r5
            r26 = r6
            r12 = r7
            r31 = r16
            goto L_0x02ad
        L_0x027c:
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r11 = r15.mHorizontalDimensionBehaviour
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r12 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r11 != r12) goto L_0x028b
            r7 = 0
            int r11 = r15.mHeight
            float r11 = (float) r11
            float r11 = r11 * r8
            int r2 = (int) r11
            r4 = 1
            goto L_0x02a0
        L_0x028b:
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r11 = r15.mVerticalDimensionBehaviour
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r12 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r11 != r12) goto L_0x02a0
            r7 = 1
            int r11 = r15.mDimensionRatioSide
            r12 = -1
            if (r11 != r12) goto L_0x0299
            float r8 = r16 / r8
        L_0x0299:
            int r11 = r15.mWidth
            float r11 = (float) r11
            float r11 = r11 * r8
            int r3 = (int) r11
            r5 = 1
        L_0x02a0:
            r27 = r2
            r29 = r3
            r28 = r4
            r30 = r5
            r26 = r6
            r12 = r7
            r31 = r8
        L_0x02ad:
            if (r26 == 0) goto L_0x02b6
            if (r12 == 0) goto L_0x02b4
            r2 = -1
            if (r12 != r2) goto L_0x02b6
        L_0x02b4:
            r2 = 1
            goto L_0x02b7
        L_0x02b6:
            r2 = 0
        L_0x02b7:
            r32 = r2
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r2 = r15.mHorizontalDimensionBehaviour
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r3 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r2 != r3) goto L_0x02c5
            boolean r2 = r15 instanceof android.support.constraint.solver.widgets.ConstraintWidgetContainer
            if (r2 == 0) goto L_0x02c5
            r2 = 1
            goto L_0x02c6
        L_0x02c5:
            r2 = 0
        L_0x02c6:
            int r3 = r15.mHorizontalResolution
            r11 = 2
            r8 = 3
            if (r3 == r11) goto L_0x03b9
            r7 = 2147483647(0x7fffffff, float:NaN)
            if (r13 == r7) goto L_0x02ee
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r15.mLeft
            int r3 = r3.mGroup
            if (r3 != r13) goto L_0x02de
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r15.mRight
            int r3 = r3.mGroup
            if (r3 != r13) goto L_0x02de
            goto L_0x02ee
        L_0x02de:
            r38 = r0
            r39 = r1
            r35 = r9
            r36 = r10
            r40 = r12
            r37 = r24
            r21 = 0
            goto L_0x03c7
        L_0x02ee:
            if (r32 == 0) goto L_0x037a
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r15.mLeft
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.mTarget
            if (r3 == 0) goto L_0x037a
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r15.mRight
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.mTarget
            if (r3 == 0) goto L_0x037a
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r15.mLeft
            android.support.constraint.solver.SolverVariable r6 = r14.createObjectVariable(r3)
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r15.mRight
            android.support.constraint.solver.SolverVariable r5 = r14.createObjectVariable(r3)
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r15.mLeft
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.getTarget()
            android.support.constraint.solver.SolverVariable r4 = r14.createObjectVariable(r3)
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r15.mRight
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.getTarget()
            android.support.constraint.solver.SolverVariable r3 = r14.createObjectVariable(r3)
            android.support.constraint.solver.widgets.ConstraintAnchor r7 = r15.mLeft
            int r7 = r7.getMargin()
            r14.addGreaterThan(r6, r4, r7, r8)
            android.support.constraint.solver.widgets.ConstraintAnchor r7 = r15.mRight
            int r7 = r7.getMargin()
            r16 = -1
            int r7 = r7 * -1
            r14.addLowerThan(r5, r3, r7, r8)
            if (r19 != 0) goto L_0x0368
            android.support.constraint.solver.widgets.ConstraintAnchor r7 = r15.mLeft
            int r7 = r7.getMargin()
            float r8 = r15.mHorizontalBiasPercent
            android.support.constraint.solver.widgets.ConstraintAnchor r11 = r15.mRight
            int r11 = r11.getMargin()
            r17 = 4
            r18 = r3
            r3 = r14
            r23 = r4
            r4 = r6
            r25 = r5
            r5 = r23
            r34 = r6
            r6 = r7
            r21 = 0
            r33 = 2147483647(0x7fffffff, float:NaN)
            r7 = r8
            r8 = r18
            r35 = r9
            r9 = r25
            r36 = r10
            r10 = r11
            r37 = r24
            r11 = r17
            r3.addCentering(r4, r5, r6, r7, r8, r9, r10, r11)
            goto L_0x0373
        L_0x0368:
            r35 = r9
            r36 = r10
            r37 = r24
            r21 = 0
            r33 = 2147483647(0x7fffffff, float:NaN)
        L_0x0373:
            r38 = r0
            r39 = r1
            r40 = r12
            goto L_0x03c7
        L_0x037a:
            r35 = r9
            r36 = r10
            r37 = r24
            r21 = 0
            r33 = 2147483647(0x7fffffff, float:NaN)
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mLeft
            android.support.constraint.solver.widgets.ConstraintAnchor r5 = r15.mRight
            int r6 = r15.f26mX
            int r3 = r15.f26mX
            int r7 = r3 + r27
            int r9 = r15.mMinWidth
            float r10 = r15.mHorizontalBiasPercent
            int r11 = r15.mMatchConstraintDefaultWidth
            int r8 = r15.mMatchConstraintMinWidth
            int r3 = r15.mMatchConstraintMaxWidth
            r38 = r0
            r0 = r15
            r39 = r1
            r1 = r14
            r16 = r3
            r3 = r28
            r17 = r8
            r8 = r27
            r18 = r11
            r11 = r32
            r40 = r12
            r12 = r19
            r13 = r18
            r14 = r17
            r15 = r16
            r0.applyConstraints(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15)
            goto L_0x03c7
        L_0x03b9:
            r38 = r0
            r39 = r1
            r35 = r9
            r36 = r10
            r40 = r12
            r37 = r24
            r21 = 0
        L_0x03c7:
            r15 = r48
            int r0 = r15.mVerticalResolution
            r1 = 2
            if (r0 != r1) goto L_0x03cf
            return
        L_0x03cf:
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r0 = r15.mVerticalDimensionBehaviour
            android.support.constraint.solver.widgets.ConstraintWidget$DimensionBehaviour r1 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r0 != r1) goto L_0x03db
            boolean r0 = r15 instanceof android.support.constraint.solver.widgets.ConstraintWidgetContainer
            if (r0 == 0) goto L_0x03db
            r0 = 1
            goto L_0x03dc
        L_0x03db:
            r0 = 0
        L_0x03dc:
            r2 = r0
            if (r26 == 0) goto L_0x03ec
            r14 = r40
            r13 = 1
            if (r14 == r13) goto L_0x03e8
            r0 = -1
            if (r14 != r0) goto L_0x03f0
            goto L_0x03e9
        L_0x03e8:
            r0 = -1
        L_0x03e9:
            r21 = 1
            goto L_0x03f0
        L_0x03ec:
            r14 = r40
            r0 = -1
            r13 = 1
        L_0x03f0:
            int r1 = r15.mBaselineDistance
            if (r1 <= 0) goto L_0x0511
            android.support.constraint.solver.widgets.ConstraintAnchor r1 = r15.mBottom
            r12 = 5
            r10 = 2147483647(0x7fffffff, float:NaN)
            r11 = r50
            if (r11 == r10) goto L_0x0412
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r15.mBottom
            int r3 = r3.mGroup
            if (r3 != r11) goto L_0x040b
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r15.mBaseline
            int r3 = r3.mGroup
            if (r3 != r11) goto L_0x040b
            goto L_0x0412
        L_0x040b:
            r7 = r35
            r8 = r37
            r9 = r49
            goto L_0x041f
        L_0x0412:
            int r3 = r48.getBaselineDistance()
            r7 = r35
            r8 = r37
            r9 = r49
            r9.addEquality(r7, r8, r3, r12)
        L_0x041f:
            r6 = r29
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r15.mBaseline
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.mTarget
            if (r3 == 0) goto L_0x042d
            int r3 = r15.mBaselineDistance
            android.support.constraint.solver.widgets.ConstraintAnchor r1 = r15.mBaseline
            r29 = r3
        L_0x042d:
            if (r11 == r10) goto L_0x0444
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r15.mTop
            int r3 = r3.mGroup
            if (r3 != r11) goto L_0x043a
            int r3 = r1.mGroup
            if (r3 != r11) goto L_0x043a
            goto L_0x0444
        L_0x043a:
            r23 = r7
            r15 = r8
            r1 = r9
        L_0x043e:
            r43 = r14
            r14 = r36
            goto L_0x0504
        L_0x0444:
            if (r21 == 0) goto L_0x04c0
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r15.mTop
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.mTarget
            if (r3 == 0) goto L_0x04c0
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r15.mBottom
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.mTarget
            if (r3 == 0) goto L_0x04c0
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r15.mTop
            android.support.constraint.solver.SolverVariable r12 = r9.createObjectVariable(r3)
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r15.mBottom
            android.support.constraint.solver.SolverVariable r5 = r9.createObjectVariable(r3)
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r15.mTop
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.getTarget()
            android.support.constraint.solver.SolverVariable r4 = r9.createObjectVariable(r3)
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r15.mBottom
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.getTarget()
            android.support.constraint.solver.SolverVariable r3 = r9.createObjectVariable(r3)
            android.support.constraint.solver.widgets.ConstraintAnchor r10 = r15.mTop
            int r10 = r10.getMargin()
            r11 = 3
            r9.addGreaterThan(r12, r4, r10, r11)
            android.support.constraint.solver.widgets.ConstraintAnchor r10 = r15.mBottom
            int r10 = r10.getMargin()
            int r10 = r10 * -1
            r9.addLowerThan(r5, r3, r10, r11)
            if (r20 != 0) goto L_0x04b6
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r15.mTop
            int r0 = r0.getMargin()
            float r10 = r15.mVerticalBiasPercent
            android.support.constraint.solver.widgets.ConstraintAnchor r11 = r15.mBottom
            int r11 = r11.getMargin()
            r16 = 4
            r17 = r3
            r3 = r9
            r18 = r4
            r4 = r12
            r22 = r5
            r5 = r18
            r41 = r6
            r6 = r0
            r23 = r7
            r7 = r10
            r0 = r8
            r8 = r17
            r10 = r9
            r9 = r22
            r10 = r11
            r11 = r16
            r3.addCentering(r4, r5, r6, r7, r8, r9, r10, r11)
            goto L_0x04bb
        L_0x04b6:
            r41 = r6
            r23 = r7
            r0 = r8
        L_0x04bb:
            r1 = r49
            r15 = r0
            goto L_0x043e
        L_0x04c0:
            r41 = r6
            r23 = r7
            r0 = r8
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r15.mTop
            int r6 = r15.f27mY
            int r3 = r15.f27mY
            int r7 = r3 + r29
            int r9 = r15.mMinHeight
            float r10 = r15.mVerticalBiasPercent
            int r11 = r15.mMatchConstraintDefaultHeight
            int r8 = r15.mMatchConstraintMinHeight
            int r5 = r15.mMatchConstraintMaxHeight
            r3 = r0
            r0 = r15
            r16 = r1
            r1 = r49
            r42 = r3
            r3 = r30
            r17 = r5
            r5 = r16
            r18 = r8
            r8 = r29
            r22 = r11
            r11 = r21
            r12 = r20
            r13 = r22
            r43 = r14
            r14 = r18
            r15 = r17
            r0.applyConstraints(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15)
            r14 = r36
            r0 = r41
            r15 = r42
            r3 = 5
            r1.addEquality(r14, r15, r0, r3)
        L_0x0504:
            r45 = r2
            r46 = r14
            r47 = r15
            r0 = r48
            r2 = r50
            goto L_0x05ec
        L_0x0511:
            r43 = r14
            r23 = r35
            r14 = r36
            r15 = r37
            r1 = r49
            r12 = 2147483647(0x7fffffff, float:NaN)
            r13 = r50
            if (r13 == r12) goto L_0x053b
            r11 = r48
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r11.mTop
            int r3 = r3.mGroup
            if (r3 != r13) goto L_0x0531
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r11.mBottom
            int r3 = r3.mGroup
            if (r3 != r13) goto L_0x0531
            goto L_0x053d
        L_0x0531:
            r45 = r2
            r0 = r11
        L_0x0534:
            r2 = r13
            r46 = r14
            r47 = r15
            goto L_0x05ec
        L_0x053b:
            r11 = r48
        L_0x053d:
            if (r21 == 0) goto L_0x05b1
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r11.mTop
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.mTarget
            if (r3 == 0) goto L_0x05b1
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r11.mBottom
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.mTarget
            if (r3 == 0) goto L_0x05b1
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r11.mTop
            android.support.constraint.solver.SolverVariable r10 = r1.createObjectVariable(r3)
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r11.mBottom
            android.support.constraint.solver.SolverVariable r9 = r1.createObjectVariable(r3)
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r11.mTop
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.getTarget()
            android.support.constraint.solver.SolverVariable r8 = r1.createObjectVariable(r3)
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r11.mBottom
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.getTarget()
            android.support.constraint.solver.SolverVariable r7 = r1.createObjectVariable(r3)
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r11.mTop
            int r3 = r3.getMargin()
            r6 = 3
            r1.addGreaterThan(r10, r8, r3, r6)
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r11.mBottom
            int r3 = r3.getMargin()
            int r3 = r3 * -1
            r1.addLowerThan(r9, r7, r3, r6)
            if (r20 != 0) goto L_0x05ad
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r11.mTop
            int r0 = r0.getMargin()
            float r5 = r11.mVerticalBiasPercent
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r11.mBottom
            int r16 = r3.getMargin()
            r17 = 4
            r3 = r1
            r4 = r10
            r18 = r5
            r5 = r8
            r6 = r0
            r0 = r7
            r7 = r18
            r18 = r8
            r8 = r0
            r22 = r9
            r24 = r10
            r10 = r16
            r44 = r0
            r0 = r11
            r11 = r17
            r3.addCentering(r4, r5, r6, r7, r8, r9, r10, r11)
            goto L_0x05ae
        L_0x05ad:
            r0 = r11
        L_0x05ae:
            r45 = r2
            goto L_0x0534
        L_0x05b1:
            r0 = r11
            android.support.constraint.solver.widgets.ConstraintAnchor r7 = r0.mTop
            android.support.constraint.solver.widgets.ConstraintAnchor r8 = r0.mBottom
            int r9 = r0.f27mY
            int r3 = r0.f27mY
            int r10 = r3 + r29
            int r11 = r0.mMinHeight
            float r6 = r0.mVerticalBiasPercent
            int r5 = r0.mMatchConstraintDefaultHeight
            int r4 = r0.mMatchConstraintMinHeight
            int r3 = r0.mMatchConstraintMaxHeight
            r18 = r3
            r3 = r0
            r17 = r4
            r4 = r1
            r16 = r5
            r5 = r2
            r22 = r6
            r6 = r30
            r24 = r11
            r11 = r29
            r45 = r2
            r2 = 2147483647(0x7fffffff, float:NaN)
            r12 = r24
            r2 = r13
            r13 = r22
            r46 = r14
            r14 = r21
            r47 = r15
            r15 = r20
            r3.applyConstraints(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18)
        L_0x05ec:
            if (r26 == 0) goto L_0x0685
            android.support.constraint.solver.ArrayRow r3 = r49.createRow()
            r4 = 2147483647(0x7fffffff, float:NaN)
            if (r2 == r4) goto L_0x0610
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r0.mLeft
            int r4 = r4.mGroup
            if (r4 != r2) goto L_0x0604
            android.support.constraint.solver.widgets.ConstraintAnchor r4 = r0.mRight
            int r4 = r4.mGroup
            if (r4 != r2) goto L_0x0604
            goto L_0x0610
        L_0x0604:
            r11 = r38
            r12 = r39
            r4 = r43
        L_0x060a:
            r14 = r46
            r13 = r47
            goto L_0x068f
        L_0x0610:
            r4 = r43
            if (r4 != 0) goto L_0x062b
            r5 = r3
            r6 = r39
            r7 = r38
            r8 = r46
            r9 = r47
            r10 = r31
            android.support.constraint.solver.ArrayRow r5 = r5.createRowDimensionRatio(r6, r7, r8, r9, r10)
            r1.addConstraint(r5)
        L_0x0626:
            r11 = r38
            r12 = r39
            goto L_0x060a
        L_0x062b:
            r5 = 1
            if (r4 != r5) goto L_0x0641
            r5 = r3
            r6 = r46
            r7 = r47
            r8 = r39
            r9 = r38
            r10 = r31
            android.support.constraint.solver.ArrayRow r5 = r5.createRowDimensionRatio(r6, r7, r8, r9, r10)
            r1.addConstraint(r5)
            goto L_0x0626
        L_0x0641:
            int r5 = r0.mMatchConstraintMinWidth
            if (r5 <= 0) goto L_0x0650
            int r5 = r0.mMatchConstraintMinWidth
            r11 = r38
            r12 = r39
            r6 = 3
            r1.addGreaterThan(r12, r11, r5, r6)
            goto L_0x0655
        L_0x0650:
            r11 = r38
            r12 = r39
            r6 = 3
        L_0x0655:
            int r5 = r0.mMatchConstraintMinHeight
            if (r5 <= 0) goto L_0x0663
            int r5 = r0.mMatchConstraintMinHeight
            r14 = r46
            r13 = r47
            r1.addGreaterThan(r14, r13, r5, r6)
            goto L_0x0667
        L_0x0663:
            r14 = r46
            r13 = r47
        L_0x0667:
            r15 = 4
            r5 = r3
            r6 = r12
            r7 = r11
            r8 = r14
            r9 = r13
            r10 = r31
            r5.createRowDimensionRatio(r6, r7, r8, r9, r10)
            android.support.constraint.solver.SolverVariable r5 = r49.createErrorVariable()
            android.support.constraint.solver.SolverVariable r6 = r49.createErrorVariable()
            r5.strength = r15
            r6.strength = r15
            r3.addError(r5, r6)
            r1.addConstraint(r3)
            goto L_0x068f
        L_0x0685:
            r11 = r38
            r12 = r39
            r4 = r43
            r14 = r46
            r13 = r47
        L_0x068f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.ConstraintWidget.addToSolver(android.support.constraint.solver.LinearSystem, int):void");
    }

    private void applyConstraints(LinearSystem system, boolean wrapContent, boolean dimensionFixed, ConstraintAnchor beginAnchor, ConstraintAnchor endAnchor, int beginPosition, int endPosition, int dimension, int minDimension, float bias, boolean useRatio, boolean inChain, int matchConstraintDefault, int matchMinDimension, int matchMaxDimension) {
        boolean dimensionFixed2;
        int dimension2;
        int i;
        SolverVariable endTarget;
        LinearSystem linearSystem = system;
        int i2 = beginPosition;
        int i3 = endPosition;
        int i4 = minDimension;
        int i5 = matchMinDimension;
        int i6 = matchMaxDimension;
        SolverVariable begin = linearSystem.createObjectVariable(beginAnchor);
        SolverVariable end = linearSystem.createObjectVariable(endAnchor);
        SolverVariable beginTarget = linearSystem.createObjectVariable(beginAnchor.getTarget());
        SolverVariable endTarget2 = linearSystem.createObjectVariable(endAnchor.getTarget());
        int beginAnchorMargin = beginAnchor.getMargin();
        int endAnchorMargin = endAnchor.getMargin();
        if (this.mVisibility == 8) {
            dimension2 = 0;
            dimensionFixed2 = true;
        } else {
            dimensionFixed2 = dimensionFixed;
            dimension2 = dimension;
        }
        if (beginTarget == null && endTarget2 == null) {
            linearSystem.addConstraint(system.createRow().createRowEquals(begin, i2));
            if (!useRatio) {
                if (wrapContent) {
                    linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, end, begin, i4, true));
                } else if (dimensionFixed2) {
                    linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, end, begin, dimension2, false));
                } else {
                    linearSystem.addConstraint(system.createRow().createRowEquals(end, i3));
                }
            }
        } else if (beginTarget != null && endTarget2 == null) {
            linearSystem.addConstraint(system.createRow().createRowEquals(begin, beginTarget, beginAnchorMargin));
            if (wrapContent) {
                linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, end, begin, i4, true));
            } else if (!useRatio) {
                if (dimensionFixed2) {
                    linearSystem.addConstraint(system.createRow().createRowEquals(end, begin, dimension2));
                } else {
                    linearSystem.addConstraint(system.createRow().createRowEquals(end, i3));
                }
            }
        } else if (beginTarget == null && endTarget2 != null) {
            linearSystem.addConstraint(system.createRow().createRowEquals(end, endTarget2, endAnchorMargin * -1));
            if (wrapContent) {
                linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, end, begin, i4, true));
            } else if (!useRatio) {
                if (dimensionFixed2) {
                    linearSystem.addConstraint(system.createRow().createRowEquals(end, begin, dimension2));
                } else {
                    linearSystem.addConstraint(system.createRow().createRowEquals(begin, i2));
                }
            }
        } else if (dimensionFixed2) {
            if (wrapContent) {
                linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, end, begin, i4, true));
            } else {
                linearSystem.addConstraint(system.createRow().createRowEquals(end, begin, dimension2));
            }
            if (beginAnchor.getStrength() == endAnchor.getStrength()) {
                int beginAnchorMargin2 = beginAnchorMargin;
                if (beginTarget == endTarget2) {
                    int beginAnchorMargin3 = beginAnchorMargin2;
                    SolverVariable endTarget3 = endTarget2;
                    SolverVariable beginTarget2 = beginTarget;
                    SolverVariable end2 = end;
                    SolverVariable begin2 = begin;
                    int dimension3 = dimension2;
                    int i7 = endAnchorMargin;
                    int i8 = matchMinDimension;
                    linearSystem.addConstraint(LinearSystem.createRowCentering(linearSystem, begin, beginTarget, 0, 0.5f, endTarget3, end2, 0, true));
                    boolean z = dimensionFixed2;
                    int i9 = beginAnchorMargin3;
                    SolverVariable solverVariable = endTarget3;
                    SolverVariable solverVariable2 = beginTarget2;
                    SolverVariable solverVariable3 = end2;
                    SolverVariable solverVariable4 = begin2;
                    int i10 = dimension3;
                    return;
                }
                SolverVariable endTarget4 = endTarget2;
                SolverVariable beginTarget3 = beginTarget;
                SolverVariable end3 = end;
                SolverVariable begin3 = begin;
                int dimension4 = dimension2;
                int endAnchorMargin2 = endAnchorMargin;
                int beginAnchorMargin4 = beginAnchorMargin2;
                int i11 = matchMinDimension;
                if (!inChain) {
                    int beginAnchorMargin5 = beginAnchorMargin4;
                    SolverVariable beginTarget4 = beginTarget3;
                    SolverVariable begin4 = begin3;
                    linearSystem.addConstraint(LinearSystem.createRowGreaterThan(linearSystem, begin4, beginTarget4, beginAnchorMargin5, beginAnchor.getConnectionType() != ConnectionType.STRICT));
                    boolean useBidirectionalError = endAnchor.getConnectionType() != ConnectionType.STRICT;
                    SolverVariable endTarget5 = endTarget4;
                    SolverVariable end4 = end3;
                    linearSystem.addConstraint(LinearSystem.createRowLowerThan(linearSystem, end4, endTarget5, endAnchorMargin2 * -1, useBidirectionalError));
                    SolverVariable endTarget6 = endTarget5;
                    SolverVariable end5 = end4;
                    boolean z2 = useBidirectionalError;
                    int beginAnchorMargin6 = beginAnchorMargin5;
                    SolverVariable solverVariable5 = beginTarget4;
                    SolverVariable solverVariable6 = begin4;
                    linearSystem.addConstraint(LinearSystem.createRowCentering(linearSystem, begin4, beginTarget4, beginAnchorMargin5, bias, endTarget6, end5, endAnchorMargin2, false));
                    boolean z3 = dimensionFixed2;
                    int i12 = dimension4;
                    SolverVariable solverVariable7 = endTarget6;
                    SolverVariable solverVariable8 = end5;
                    int i13 = beginAnchorMargin6;
                    return;
                }
                SolverVariable solverVariable9 = begin3;
                boolean z4 = dimensionFixed2;
                int i14 = beginAnchorMargin4;
                SolverVariable solverVariable10 = endTarget4;
                SolverVariable solverVariable11 = end3;
                int i15 = dimension4;
                return;
            } else if (beginAnchor.getStrength() == Strength.STRONG) {
                linearSystem.addConstraint(system.createRow().createRowEquals(begin, beginTarget, beginAnchorMargin));
                SolverVariable slack = system.createSlackVariable();
                ArrayRow row = system.createRow();
                row.createRowLowerThan(end, endTarget2, slack, endAnchorMargin * -1);
                linearSystem.addConstraint(row);
            } else {
                SolverVariable slack2 = system.createSlackVariable();
                ArrayRow row2 = system.createRow();
                row2.createRowGreaterThan(begin, beginTarget, slack2, beginAnchorMargin);
                linearSystem.addConstraint(row2);
                int beginAnchorMargin7 = beginAnchorMargin;
                linearSystem.addConstraint(system.createRow().createRowEquals(end, endTarget2, endAnchorMargin * -1));
                SolverVariable solverVariable12 = endTarget2;
                SolverVariable solverVariable13 = beginTarget;
                SolverVariable solverVariable14 = end;
                SolverVariable solverVariable15 = begin;
                int i16 = dimension2;
                int i17 = endAnchorMargin;
                boolean z5 = dimensionFixed2;
                int i18 = beginAnchorMargin7;
                int i19 = matchMinDimension;
                return;
            }
        } else {
            int beginAnchorMargin8 = beginAnchorMargin;
            SolverVariable endTarget7 = endTarget2;
            SolverVariable beginTarget5 = beginTarget;
            SolverVariable end6 = end;
            SolverVariable begin5 = begin;
            int dimension5 = dimension2;
            int endAnchorMargin3 = endAnchorMargin;
            int i20 = matchMinDimension;
            if (useRatio) {
                int beginAnchorMargin9 = beginAnchorMargin8;
                linearSystem.addGreaterThan(begin5, beginTarget5, beginAnchorMargin9, 3);
                SolverVariable endTarget8 = endTarget7;
                SolverVariable end7 = end6;
                linearSystem.addLowerThan(end7, endTarget8, endAnchorMargin3 * -1, 3);
                SolverVariable endTarget9 = endTarget8;
                SolverVariable solverVariable16 = end7;
                boolean z6 = dimensionFixed2;
                int i21 = beginAnchorMargin9;
                linearSystem.addConstraint(LinearSystem.createRowCentering(linearSystem, begin5, beginTarget5, beginAnchorMargin9, bias, endTarget8, end7, endAnchorMargin3, true));
                int i22 = dimension5;
                SolverVariable solverVariable17 = endTarget9;
                return;
            }
            SolverVariable endTarget10 = endTarget7;
            SolverVariable end8 = end6;
            int beginAnchorMargin10 = beginAnchorMargin8;
            if (inChain) {
                SolverVariable solverVariable18 = endTarget10;
                return;
            } else if (matchConstraintDefault == 1) {
                int dimension6 = dimension5;
                if (i20 > dimension6) {
                    dimension6 = i20;
                }
                int i23 = matchMaxDimension;
                if (i23 > 0) {
                    if (i23 < dimension6) {
                        dimension6 = i23;
                    } else {
                        linearSystem.addLowerThan(end8, begin5, i23, 3);
                    }
                }
                linearSystem.addEquality(end8, begin5, dimension6, 3);
                linearSystem.addGreaterThan(begin5, beginTarget5, beginAnchorMargin10, 2);
                SolverVariable endTarget11 = endTarget10;
                linearSystem.addLowerThan(end8, endTarget11, -endAnchorMargin3, 2);
                SolverVariable endTarget12 = endTarget11;
                int dimension7 = dimension6;
                linearSystem.addCentering(begin5, beginTarget5, beginAnchorMargin10, bias, endTarget11, end8, endAnchorMargin3, 4);
                int i24 = dimension7;
                SolverVariable solverVariable19 = endTarget12;
                return;
            } else {
                int dimension8 = dimension5;
                SolverVariable endTarget13 = endTarget10;
                if (i20 == 0) {
                    i = matchMaxDimension;
                    if (i == 0) {
                        linearSystem.addConstraint(system.createRow().createRowEquals(begin5, beginTarget5, beginAnchorMargin10));
                        SolverVariable endTarget14 = endTarget13;
                        linearSystem.addConstraint(system.createRow().createRowEquals(end8, endTarget14, endAnchorMargin3 * -1));
                        SolverVariable solverVariable20 = endTarget14;
                        int i25 = dimension8;
                        return;
                    }
                    endTarget = endTarget13;
                } else {
                    endTarget = endTarget13;
                    i = matchMaxDimension;
                }
                if (i > 0) {
                    linearSystem.addLowerThan(end8, begin5, i, 3);
                }
                linearSystem.addGreaterThan(begin5, beginTarget5, beginAnchorMargin10, 2);
                linearSystem.addLowerThan(end8, endTarget, -endAnchorMargin3, 2);
                SolverVariable solverVariable21 = endTarget;
                int i26 = dimension8;
                linearSystem.addCentering(begin5, beginTarget5, beginAnchorMargin10, bias, endTarget, end8, endAnchorMargin3, 4);
                return;
            }
        }
        int i27 = matchMinDimension;
    }

    public void updateFromSolver(LinearSystem system, int group) {
        if (group == Integer.MAX_VALUE) {
            setFrame(system.getObjectVariableValue(this.mLeft), system.getObjectVariableValue(this.mTop), system.getObjectVariableValue(this.mRight), system.getObjectVariableValue(this.mBottom));
        } else if (group == -2) {
            setFrame(this.mSolverLeft, this.mSolverTop, this.mSolverRight, this.mSolverBottom);
        } else {
            if (this.mLeft.mGroup == group) {
                this.mSolverLeft = system.getObjectVariableValue(this.mLeft);
            }
            if (this.mTop.mGroup == group) {
                this.mSolverTop = system.getObjectVariableValue(this.mTop);
            }
            if (this.mRight.mGroup == group) {
                this.mSolverRight = system.getObjectVariableValue(this.mRight);
            }
            if (this.mBottom.mGroup == group) {
                this.mSolverBottom = system.getObjectVariableValue(this.mBottom);
            }
        }
    }

    public void updateFromSolver(LinearSystem system) {
        updateFromSolver(system, Integer.MAX_VALUE);
    }
}
