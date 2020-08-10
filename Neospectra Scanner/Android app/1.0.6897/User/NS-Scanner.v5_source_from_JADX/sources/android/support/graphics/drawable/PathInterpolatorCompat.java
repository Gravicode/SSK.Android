package android.support.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p001v4.content.res.TypedArrayUtils;
import android.support.p001v4.graphics.PathParser;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.animation.Interpolator;
import org.xmlpull.v1.XmlPullParser;

@RestrictTo({Scope.LIBRARY_GROUP})
public class PathInterpolatorCompat implements Interpolator {
    public static final double EPSILON = 1.0E-5d;
    public static final int MAX_NUM_POINTS = 3000;
    private static final float PRECISION = 0.002f;

    /* renamed from: mX */
    private float[] f35mX;

    /* renamed from: mY */
    private float[] f36mY;

    public PathInterpolatorCompat(Context context, AttributeSet attrs, XmlPullParser parser) {
        this(context.getResources(), context.getTheme(), attrs, parser);
    }

    public PathInterpolatorCompat(Resources res, Theme theme, AttributeSet attrs, XmlPullParser parser) {
        TypedArray a = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_PATH_INTERPOLATOR);
        parseInterpolatorFromTypeArray(a, parser);
        a.recycle();
    }

    private void parseInterpolatorFromTypeArray(TypedArray a, XmlPullParser parser) {
        if (TypedArrayUtils.hasAttribute(parser, "pathData")) {
            String pathData = TypedArrayUtils.getNamedString(a, parser, "pathData", 4);
            Path path = PathParser.createPathFromPathData(pathData);
            if (path == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("The path is null, which is created from ");
                sb.append(pathData);
                throw new InflateException(sb.toString());
            }
            initPath(path);
        } else if (!TypedArrayUtils.hasAttribute(parser, "controlX1")) {
            throw new InflateException("pathInterpolator requires the controlX1 attribute");
        } else if (!TypedArrayUtils.hasAttribute(parser, "controlY1")) {
            throw new InflateException("pathInterpolator requires the controlY1 attribute");
        } else {
            float x1 = TypedArrayUtils.getNamedFloat(a, parser, "controlX1", 0, 0.0f);
            float y1 = TypedArrayUtils.getNamedFloat(a, parser, "controlY1", 1, 0.0f);
            boolean hasX2 = TypedArrayUtils.hasAttribute(parser, "controlX2");
            if (hasX2 != TypedArrayUtils.hasAttribute(parser, "controlY2")) {
                throw new InflateException("pathInterpolator requires both controlX2 and controlY2 for cubic Beziers.");
            } else if (!hasX2) {
                initQuad(x1, y1);
            } else {
                initCubic(x1, y1, TypedArrayUtils.getNamedFloat(a, parser, "controlX2", 2, 0.0f), TypedArrayUtils.getNamedFloat(a, parser, "controlY2", 3, 0.0f));
            }
        }
    }

    private void initQuad(float controlX, float controlY) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.quadTo(controlX, controlY, 1.0f, 1.0f);
        initPath(path);
    }

    private void initCubic(float x1, float y1, float x2, float y2) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.cubicTo(x1, y1, x2, y2, 1.0f, 1.0f);
        initPath(path);
    }

    private void initPath(Path path) {
        int i = 0;
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float pathLength = pathMeasure.getLength();
        int numPoints = Math.min(3000, ((int) (pathLength / PRECISION)) + 1);
        if (numPoints <= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("The Path has a invalid length ");
            sb.append(pathLength);
            throw new IllegalArgumentException(sb.toString());
        }
        this.f35mX = new float[numPoints];
        this.f36mY = new float[numPoints];
        float[] position = new float[2];
        for (int i2 = 0; i2 < numPoints; i2++) {
            pathMeasure.getPosTan((((float) i2) * pathLength) / ((float) (numPoints - 1)), position, null);
            this.f35mX[i2] = position[0];
            this.f36mY[i2] = position[1];
        }
        if (((double) Math.abs(this.f35mX[0])) > 1.0E-5d || ((double) Math.abs(this.f36mY[0])) > 1.0E-5d || ((double) Math.abs(this.f35mX[numPoints - 1] - 1.0f)) > 1.0E-5d || ((double) Math.abs(this.f36mY[numPoints - 1] - 1.0f)) > 1.0E-5d) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("The Path must start at (0,0) and end at (1,1) start: ");
            sb2.append(this.f35mX[0]);
            sb2.append(",");
            sb2.append(this.f36mY[0]);
            sb2.append(" end:");
            sb2.append(this.f35mX[numPoints - 1]);
            sb2.append(",");
            sb2.append(this.f36mY[numPoints - 1]);
            throw new IllegalArgumentException(sb2.toString());
        }
        float prevX = 0.0f;
        int componentIndex = 0;
        while (i < numPoints) {
            int componentIndex2 = componentIndex + 1;
            float x = this.f35mX[componentIndex];
            if (x < prevX) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("The Path cannot loop back on itself, x :");
                sb3.append(x);
                throw new IllegalArgumentException(sb3.toString());
            }
            this.f35mX[i] = x;
            prevX = x;
            i++;
            componentIndex = componentIndex2;
        }
        if (pathMeasure.nextContour() != 0) {
            throw new IllegalArgumentException("The Path should be continuous, can't have 2+ contours");
        }
    }

    public float getInterpolation(float t) {
        if (t <= 0.0f) {
            return 0.0f;
        }
        if (t >= 1.0f) {
            return 1.0f;
        }
        int startIndex = 0;
        int endIndex = this.f35mX.length - 1;
        while (endIndex - startIndex > 1) {
            int midIndex = (startIndex + endIndex) / 2;
            if (t < this.f35mX[midIndex]) {
                endIndex = midIndex;
            } else {
                startIndex = midIndex;
            }
        }
        float xRange = this.f35mX[endIndex] - this.f35mX[startIndex];
        if (xRange == 0.0f) {
            return this.f36mY[startIndex];
        }
        float fraction = (t - this.f35mX[startIndex]) / xRange;
        float startY = this.f36mY[startIndex];
        return ((this.f36mY[endIndex] - startY) * fraction) + startY;
    }
}
