package android.support.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build.VERSION;
import android.support.annotation.AnimatorRes;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p001v4.content.res.TypedArrayUtils;
import android.support.p001v4.graphics.PathParser;
import android.support.p001v4.graphics.PathParser.PathDataNode;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.InflateException;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({Scope.LIBRARY_GROUP})
public class AnimatorInflaterCompat {
    private static final boolean DBG_ANIMATOR_INFLATER = false;
    private static final int MAX_NUM_POINTS = 100;
    private static final String TAG = "AnimatorInflater";
    private static final int TOGETHER = 0;
    private static final int VALUE_TYPE_COLOR = 3;
    private static final int VALUE_TYPE_FLOAT = 0;
    private static final int VALUE_TYPE_INT = 1;
    private static final int VALUE_TYPE_PATH = 2;
    private static final int VALUE_TYPE_UNDEFINED = 4;

    private static class PathDataEvaluator implements TypeEvaluator<PathDataNode[]> {
        private PathDataNode[] mNodeArray;

        private PathDataEvaluator() {
        }

        PathDataEvaluator(PathDataNode[] nodeArray) {
            this.mNodeArray = nodeArray;
        }

        public PathDataNode[] evaluate(float fraction, PathDataNode[] startPathData, PathDataNode[] endPathData) {
            if (!PathParser.canMorph(startPathData, endPathData)) {
                throw new IllegalArgumentException("Can't interpolate between two incompatible pathData");
            }
            if (this.mNodeArray == null || !PathParser.canMorph(this.mNodeArray, startPathData)) {
                this.mNodeArray = PathParser.deepCopyNodes(startPathData);
            }
            for (int i = 0; i < startPathData.length; i++) {
                this.mNodeArray[i].interpolatePathDataNode(startPathData[i], endPathData[i], fraction);
            }
            return this.mNodeArray;
        }
    }

    public static Animator loadAnimator(Context context, @AnimatorRes int id) throws NotFoundException {
        if (VERSION.SDK_INT >= 24) {
            return AnimatorInflater.loadAnimator(context, id);
        }
        return loadAnimator(context, context.getResources(), context.getTheme(), id);
    }

    public static Animator loadAnimator(Context context, Resources resources, Theme theme, @AnimatorRes int id) throws NotFoundException {
        return loadAnimator(context, resources, theme, id, 1.0f);
    }

    public static Animator loadAnimator(Context context, Resources resources, Theme theme, @AnimatorRes int id, float pathErrorScale) throws NotFoundException {
        XmlResourceParser parser = null;
        try {
            XmlResourceParser parser2 = resources.getAnimation(id);
            Animator animator = createAnimatorFromXml(context, resources, theme, parser2, pathErrorScale);
            if (parser2 != null) {
                parser2.close();
            }
            return animator;
        } catch (XmlPullParserException ex) {
            StringBuilder sb = new StringBuilder();
            sb.append("Can't load animation resource ID #0x");
            sb.append(Integer.toHexString(id));
            NotFoundException rnf = new NotFoundException(sb.toString());
            rnf.initCause(ex);
            throw rnf;
        } catch (IOException ex2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Can't load animation resource ID #0x");
            sb2.append(Integer.toHexString(id));
            NotFoundException rnf2 = new NotFoundException(sb2.toString());
            rnf2.initCause(ex2);
            throw rnf2;
        } catch (Throwable th) {
            if (parser != null) {
                parser.close();
            }
            throw th;
        }
    }

    private static PropertyValuesHolder getPVH(TypedArray styledAttributes, int valueType, int valueFromId, int valueToId, String propertyName) {
        int valueType2;
        PropertyValuesHolder returnValue;
        PropertyValuesHolder returnValue2;
        int valueTo;
        int valueTo2;
        int valueFrom;
        int valueTo3;
        int valueTo4;
        PropertyValuesHolder propertyValuesHolder;
        float valueTo5;
        float valueFrom2;
        float valueTo6;
        int toType;
        PropertyValuesHolder propertyValuesHolder2;
        PropertyValuesHolder propertyValuesHolder3;
        TypedArray typedArray = styledAttributes;
        int i = valueFromId;
        int i2 = valueToId;
        String str = propertyName;
        TypedValue tvFrom = typedArray.peekValue(i);
        boolean hasFrom = tvFrom != null;
        int fromType = hasFrom ? tvFrom.type : 0;
        TypedValue tvTo = typedArray.peekValue(i2);
        boolean hasTo = tvTo != null;
        int toType2 = hasTo ? tvTo.type : 0;
        int i3 = valueType;
        if (i3 != 4) {
            valueType2 = i3;
        } else if ((!hasFrom || !isColorType(fromType)) && (!hasTo || !isColorType(toType2))) {
            valueType2 = 0;
        } else {
            valueType2 = 3;
        }
        boolean getFloats = valueType2 == 0;
        if (valueType2 == 2) {
            String fromString = typedArray.getString(i);
            String toString = typedArray.getString(i2);
            PathDataNode[] nodesFrom = PathParser.createNodesFromPathData(fromString);
            TypedValue typedValue = tvFrom;
            PathDataNode[] nodesTo = PathParser.createNodesFromPathData(toString);
            if (nodesFrom == null && nodesTo == null) {
                TypedValue typedValue2 = tvTo;
                toType = toType2;
                propertyValuesHolder2 = null;
            } else {
                TypedValue typedValue3 = tvTo;
                if (nodesFrom != null) {
                    TypeEvaluator evaluator = new PathDataEvaluator();
                    if (nodesTo == null) {
                        toType = toType2;
                        propertyValuesHolder3 = PropertyValuesHolder.ofObject(str, evaluator, new Object[]{nodesFrom});
                    } else if (!PathParser.canMorph(nodesFrom, nodesTo)) {
                        StringBuilder sb = new StringBuilder();
                        int i4 = toType2;
                        sb.append(" Can't morph from ");
                        sb.append(fromString);
                        sb.append(" to ");
                        sb.append(toString);
                        throw new InflateException(sb.toString());
                    } else {
                        toType = toType2;
                        propertyValuesHolder3 = PropertyValuesHolder.ofObject(str, evaluator, new Object[]{nodesFrom, nodesTo});
                    }
                    returnValue = propertyValuesHolder3;
                } else {
                    toType = toType2;
                    propertyValuesHolder2 = null;
                    if (nodesTo != null) {
                        returnValue = PropertyValuesHolder.ofObject(str, new PathDataEvaluator(), new Object[]{nodesTo});
                    }
                }
                int i5 = toType;
                int i6 = valueToId;
            }
            returnValue = propertyValuesHolder2;
            int i52 = toType;
            int i62 = valueToId;
        } else {
            TypedValue typedValue4 = tvTo;
            int toType3 = toType2;
            TypeEvaluator evaluator2 = null;
            if (valueType2 == 3) {
                evaluator2 = ArgbEvaluator.getInstance();
            }
            if (getFloats) {
                if (hasFrom) {
                    if (fromType == 5) {
                        valueFrom2 = typedArray.getDimension(i, 0.0f);
                    } else {
                        valueFrom2 = typedArray.getFloat(i, 0.0f);
                    }
                    if (hasTo) {
                        if (toType3 == 5) {
                            valueTo6 = typedArray.getDimension(valueToId, 0.0f);
                        } else {
                            valueTo6 = typedArray.getFloat(valueToId, 0.0f);
                        }
                        returnValue2 = PropertyValuesHolder.ofFloat(str, new float[]{valueFrom2, valueTo6});
                    } else {
                        int i7 = valueToId;
                        propertyValuesHolder = PropertyValuesHolder.ofFloat(str, new float[]{valueFrom2});
                    }
                } else {
                    int i8 = valueToId;
                    if (toType3 == 5) {
                        valueTo5 = typedArray.getDimension(i8, 0.0f);
                    } else {
                        valueTo5 = typedArray.getFloat(i8, 0.0f);
                    }
                    propertyValuesHolder = PropertyValuesHolder.ofFloat(str, new float[]{valueTo5});
                }
                returnValue2 = propertyValuesHolder;
            } else {
                int toType4 = toType3;
                int i9 = valueToId;
                if (hasFrom) {
                    if (fromType == 5) {
                        valueFrom = (int) typedArray.getDimension(i, 0.0f);
                    } else if (isColorType(fromType) != 0) {
                        valueFrom = typedArray.getColor(i, 0);
                    } else {
                        valueFrom = typedArray.getInt(i, 0);
                    }
                    int valueFrom3 = valueFrom;
                    if (hasTo) {
                        if (toType4 == 5) {
                            valueTo3 = (int) typedArray.getDimension(i9, 0.0f);
                            valueTo4 = 0;
                        } else if (isColorType(toType4) != 0) {
                            valueTo4 = 0;
                            valueTo3 = typedArray.getColor(i9, 0);
                        } else {
                            valueTo4 = 0;
                            valueTo3 = typedArray.getInt(i9, 0);
                        }
                        int[] iArr = new int[2];
                        iArr[valueTo4] = valueFrom3;
                        iArr[1] = valueTo3;
                        returnValue2 = PropertyValuesHolder.ofInt(str, iArr);
                    } else {
                        returnValue2 = PropertyValuesHolder.ofInt(str, new int[]{valueFrom3});
                    }
                } else if (hasTo) {
                    if (toType4 == 5) {
                        valueTo = (int) typedArray.getDimension(i9, 0.0f);
                        valueTo2 = 0;
                    } else if (isColorType(toType4) != 0) {
                        valueTo2 = 0;
                        valueTo = typedArray.getColor(i9, 0);
                    } else {
                        valueTo2 = 0;
                        valueTo = typedArray.getInt(i9, 0);
                    }
                    int[] iArr2 = new int[1];
                    iArr2[valueTo2] = valueTo;
                    returnValue2 = PropertyValuesHolder.ofInt(str, iArr2);
                } else {
                    returnValue2 = null;
                }
            }
            if (!(returnValue == null || evaluator2 == null)) {
                returnValue.setEvaluator(evaluator2);
            }
        }
        return returnValue;
    }

    private static void parseAnimatorFromTypeArray(ValueAnimator anim, TypedArray arrayAnimator, TypedArray arrayObjectAnimator, float pixelSize, XmlPullParser parser) {
        long duration = (long) TypedArrayUtils.getNamedInt(arrayAnimator, parser, "duration", 1, 300);
        long startDelay = (long) TypedArrayUtils.getNamedInt(arrayAnimator, parser, "startOffset", 2, 0);
        int valueType = TypedArrayUtils.getNamedInt(arrayAnimator, parser, "valueType", 7, 4);
        if (TypedArrayUtils.hasAttribute(parser, "valueFrom") && TypedArrayUtils.hasAttribute(parser, "valueTo")) {
            if (valueType == 4) {
                valueType = inferValueTypeFromValues(arrayAnimator, 5, 6);
            }
            PropertyValuesHolder pvh = getPVH(arrayAnimator, valueType, 5, 6, "");
            if (pvh != null) {
                anim.setValues(new PropertyValuesHolder[]{pvh});
            }
        }
        anim.setDuration(duration);
        anim.setStartDelay(startDelay);
        anim.setRepeatCount(TypedArrayUtils.getNamedInt(arrayAnimator, parser, "repeatCount", 3, 0));
        anim.setRepeatMode(TypedArrayUtils.getNamedInt(arrayAnimator, parser, "repeatMode", 4, 1));
        if (arrayObjectAnimator != null) {
            setupObjectAnimator(anim, arrayObjectAnimator, valueType, pixelSize, parser);
        }
    }

    private static void setupObjectAnimator(ValueAnimator anim, TypedArray arrayObjectAnimator, int valueType, float pixelSize, XmlPullParser parser) {
        ObjectAnimator oa = (ObjectAnimator) anim;
        String pathData = TypedArrayUtils.getNamedString(arrayObjectAnimator, parser, "pathData", 1);
        if (pathData != null) {
            String propertyXName = TypedArrayUtils.getNamedString(arrayObjectAnimator, parser, "propertyXName", 2);
            String propertyYName = TypedArrayUtils.getNamedString(arrayObjectAnimator, parser, "propertyYName", 3);
            if (valueType == 2 || valueType == 4) {
            }
            if (propertyXName == null && propertyYName == null) {
                StringBuilder sb = new StringBuilder();
                sb.append(arrayObjectAnimator.getPositionDescription());
                sb.append(" propertyXName or propertyYName is needed for PathData");
                throw new InflateException(sb.toString());
            }
            setupPathMotion(PathParser.createPathFromPathData(pathData), oa, 0.5f * pixelSize, propertyXName, propertyYName);
            return;
        }
        oa.setPropertyName(TypedArrayUtils.getNamedString(arrayObjectAnimator, parser, "propertyName", 0));
    }

    private static void setupPathMotion(Path path, ObjectAnimator oa, float precision, String propertyXName, String propertyYName) {
        Path path2 = path;
        ObjectAnimator objectAnimator = oa;
        String str = propertyXName;
        String str2 = propertyYName;
        PathMeasure measureForTotalLength = new PathMeasure(path2, false);
        float totalLength = 0.0f;
        ArrayList<Float> contourLengths = new ArrayList<>();
        contourLengths.add(Float.valueOf(0.0f));
        do {
            totalLength += measureForTotalLength.getLength();
            contourLengths.add(Float.valueOf(totalLength));
        } while (measureForTotalLength.nextContour());
        PathMeasure pathMeasure = new PathMeasure(path2, false);
        int numPoints = Math.min(100, ((int) (totalLength / precision)) + 1);
        float[] mX = new float[numPoints];
        float[] mY = new float[numPoints];
        float[] position = new float[2];
        float step = totalLength / ((float) (numPoints - 1));
        float currentDistance = 0.0f;
        int contourIndex = 0;
        int contourIndex2 = 0;
        while (true) {
            int i = contourIndex2;
            if (i >= numPoints) {
                break;
            }
            pathMeasure.getPosTan(currentDistance, position, null);
            mX[i] = position[0];
            mY[i] = position[1];
            currentDistance += step;
            PathMeasure measureForTotalLength2 = measureForTotalLength;
            if (contourIndex + 1 < contourLengths.size() && currentDistance > ((Float) contourLengths.get(contourIndex + 1)).floatValue()) {
                currentDistance -= ((Float) contourLengths.get(contourIndex + 1)).floatValue();
                contourIndex++;
                pathMeasure.nextContour();
            }
            contourIndex2 = i + 1;
            measureForTotalLength = measureForTotalLength2;
            Path path3 = path;
        }
        PropertyValuesHolder x = null;
        PropertyValuesHolder y = null;
        if (str != null) {
            x = PropertyValuesHolder.ofFloat(str, mX);
        }
        if (str2 != null) {
            y = PropertyValuesHolder.ofFloat(str2, mY);
        }
        if (x == null) {
            objectAnimator.setValues(new PropertyValuesHolder[]{y});
        } else if (y == null) {
            objectAnimator.setValues(new PropertyValuesHolder[]{x});
        } else {
            objectAnimator.setValues(new PropertyValuesHolder[]{x, y});
        }
    }

    private static Animator createAnimatorFromXml(Context context, Resources res, Theme theme, XmlPullParser parser, float pixelSize) throws XmlPullParserException, IOException {
        return createAnimatorFromXml(context, res, theme, parser, Xml.asAttributeSet(parser), null, 0, pixelSize);
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x00cf  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.animation.Animator createAnimatorFromXml(android.content.Context r20, android.content.res.Resources r21, android.content.res.Resources.Theme r22, org.xmlpull.v1.XmlPullParser r23, android.util.AttributeSet r24, android.animation.AnimatorSet r25, int r26, float r27) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            r8 = r21
            r9 = r22
            r10 = r23
            r11 = r25
            r0 = 0
            r1 = 0
            int r2 = r23.getDepth()
            r7 = r0
            r12 = r1
        L_0x0010:
            r13 = r2
            int r0 = r23.next()
            r14 = r0
            r1 = 3
            if (r0 != r1) goto L_0x0026
            int r0 = r23.getDepth()
            if (r0 <= r13) goto L_0x0020
            goto L_0x0026
        L_0x0020:
            r1 = r20
            r19 = r13
            goto L_0x00fe
        L_0x0026:
            r0 = 1
            if (r14 == r0) goto L_0x00fa
            r0 = 2
            if (r14 == r0) goto L_0x002f
            r2 = r13
            goto L_0x0010
        L_0x002f:
            java.lang.String r15 = r23.getName()
            r16 = 0
            java.lang.String r0 = "objectAnimator"
            boolean r0 = r15.equals(r0)
            if (r0 == 0) goto L_0x0051
            r0 = r20
            r1 = r8
            r2 = r9
            r3 = r24
            r4 = r27
            r5 = r10
            android.animation.ObjectAnimator r0 = loadObjectAnimator(r0, r1, r2, r3, r4, r5)
        L_0x004a:
            r1 = r20
            r7 = r0
            r19 = r13
            goto L_0x00c9
        L_0x0051:
            java.lang.String r0 = "animator"
            boolean r0 = r15.equals(r0)
            if (r0 == 0) goto L_0x0068
            r4 = 0
            r0 = r20
            r1 = r8
            r2 = r9
            r3 = r24
            r5 = r27
            r6 = r10
            android.animation.ValueAnimator r0 = loadAnimator(r0, r1, r2, r3, r4, r5, r6)
            goto L_0x004a
        L_0x0068:
            java.lang.String r0 = "set"
            boolean r0 = r15.equals(r0)
            if (r0 == 0) goto L_0x00a3
            android.animation.AnimatorSet r0 = new android.animation.AnimatorSet
            r0.<init>()
            r17 = r0
            int[] r0 = android.support.graphics.drawable.AndroidResources.STYLEABLE_ANIMATOR_SET
            r7 = r24
            android.content.res.TypedArray r6 = android.support.p001v4.content.res.TypedArrayUtils.obtainAttributes(r8, r9, r7, r0)
            java.lang.String r0 = "ordering"
            r1 = 0
            int r18 = android.support.p001v4.content.res.TypedArrayUtils.getNamedInt(r6, r10, r0, r1, r1)
            r5 = r17
            android.animation.AnimatorSet r5 = (android.animation.AnimatorSet) r5
            r0 = r20
            r1 = r8
            r2 = r9
            r3 = r10
            r4 = r7
            r19 = r13
            r13 = r6
            r6 = r18
            r7 = r27
            createAnimatorFromXml(r0, r1, r2, r3, r4, r5, r6, r7)
            r13.recycle()
            r1 = r20
            r7 = r17
            goto L_0x00c9
        L_0x00a3:
            r19 = r13
            java.lang.String r0 = "propertyValuesHolder"
            boolean r0 = r15.equals(r0)
            if (r0 == 0) goto L_0x00dd
            android.util.AttributeSet r0 = android.util.Xml.asAttributeSet(r23)
            r1 = r20
            android.animation.PropertyValuesHolder[] r0 = loadValues(r1, r8, r9, r10, r0)
            if (r0 == 0) goto L_0x00c6
            if (r7 == 0) goto L_0x00c6
            boolean r2 = r7 instanceof android.animation.ValueAnimator
            if (r2 == 0) goto L_0x00c6
            r2 = r7
            android.animation.ValueAnimator r2 = (android.animation.ValueAnimator) r2
            r2.setValues(r0)
        L_0x00c6:
            r16 = 1
        L_0x00c9:
            if (r11 == 0) goto L_0x00d8
            if (r16 != 0) goto L_0x00d8
            if (r12 != 0) goto L_0x00d5
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r12 = r0
        L_0x00d5:
            r12.add(r7)
        L_0x00d8:
            r2 = r19
            goto L_0x0010
        L_0x00dd:
            r1 = r20
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Unknown animator name: "
            r2.append(r3)
            java.lang.String r3 = r23.getName()
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r0.<init>(r2)
            throw r0
        L_0x00fa:
            r1 = r20
            r19 = r13
        L_0x00fe:
            if (r11 == 0) goto L_0x0129
            if (r12 == 0) goto L_0x0129
            int r0 = r12.size()
            android.animation.Animator[] r0 = new android.animation.Animator[r0]
            r2 = 0
            java.util.Iterator r3 = r12.iterator()
        L_0x010d:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x0120
            java.lang.Object r4 = r3.next()
            android.animation.Animator r4 = (android.animation.Animator) r4
            int r5 = r2 + 1
            r0[r2] = r4
            r2 = r5
            goto L_0x010d
        L_0x0120:
            if (r26 != 0) goto L_0x0126
            r11.playTogether(r0)
            goto L_0x0129
        L_0x0126:
            r11.playSequentially(r0)
        L_0x0129:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.graphics.drawable.AnimatorInflaterCompat.createAnimatorFromXml(android.content.Context, android.content.res.Resources, android.content.res.Resources$Theme, org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.animation.AnimatorSet, int, float):android.animation.Animator");
    }

    private static PropertyValuesHolder[] loadValues(Context context, Resources res, Theme theme, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        ArrayList arrayList;
        XmlPullParser xmlPullParser = parser;
        ArrayList arrayList2 = null;
        while (true) {
            arrayList = arrayList2;
            int eventType = parser.getEventType();
            int type = eventType;
            if (eventType == 3 || type == 1) {
                Resources resources = res;
                Theme theme2 = theme;
                AttributeSet attributeSet = attrs;
                PropertyValuesHolder[] valuesArray = null;
            } else if (type != 2) {
                parser.next();
                arrayList2 = arrayList;
            } else {
                if (parser.getName().equals("propertyValuesHolder")) {
                    Resources resources2 = res;
                    Theme theme3 = theme;
                    TypedArray a = TypedArrayUtils.obtainAttributes(resources2, theme3, attrs, AndroidResources.STYLEABLE_PROPERTY_VALUES_HOLDER);
                    String propertyName = TypedArrayUtils.getNamedString(a, xmlPullParser, "propertyName", 3);
                    int valueType = TypedArrayUtils.getNamedInt(a, xmlPullParser, "valueType", 2, 4);
                    String propertyName2 = propertyName;
                    PropertyValuesHolder pvh = loadPvh(context, resources2, theme3, xmlPullParser, propertyName, valueType);
                    if (pvh == null) {
                        pvh = getPVH(a, valueType, 0, 1, propertyName2);
                    } else {
                        int i = valueType;
                    }
                    if (pvh != null) {
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(pvh);
                    }
                    a.recycle();
                } else {
                    Resources resources3 = res;
                    Theme theme4 = theme;
                    AttributeSet attributeSet2 = attrs;
                }
                arrayList2 = arrayList;
                parser.next();
            }
        }
        Resources resources4 = res;
        Theme theme22 = theme;
        AttributeSet attributeSet3 = attrs;
        PropertyValuesHolder[] valuesArray2 = null;
        if (arrayList != null) {
            int count = arrayList.size();
            valuesArray2 = new PropertyValuesHolder[count];
            for (int i2 = 0; i2 < count; i2++) {
                valuesArray2[i2] = (PropertyValuesHolder) arrayList.get(i2);
            }
        }
        return valuesArray2;
    }

    private static int inferValueTypeOfKeyframe(Resources res, Theme theme, AttributeSet attrs, XmlPullParser parser) {
        TypedArray a = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_KEYFRAME);
        int valueType = 0;
        TypedValue keyframeValue = TypedArrayUtils.peekNamedValue(a, parser, Param.VALUE, 0);
        if ((keyframeValue != null) && isColorType(keyframeValue.type)) {
            valueType = 3;
        }
        a.recycle();
        return valueType;
    }

    private static int inferValueTypeFromValues(TypedArray styledAttributes, int valueFromId, int valueToId) {
        TypedValue tvFrom = styledAttributes.peekValue(valueFromId);
        boolean hasTo = true;
        boolean hasFrom = tvFrom != null;
        int fromType = hasFrom ? tvFrom.type : 0;
        TypedValue tvTo = styledAttributes.peekValue(valueToId);
        if (tvTo == null) {
            hasTo = false;
        }
        int toType = hasTo ? tvTo.type : 0;
        if ((!hasFrom || !isColorType(fromType)) && (!hasTo || !isColorType(toType))) {
            return 0;
        }
        return 3;
    }

    private static void dumpKeyframes(Object[] keyframes, String header) {
        if (keyframes != null && keyframes.length != 0) {
            Log.d(TAG, header);
            int count = keyframes.length;
            for (int i = 0; i < count; i++) {
                Keyframe keyframe = keyframes[i];
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Keyframe ");
                sb.append(i);
                sb.append(": fraction ");
                sb.append(keyframe.getFraction() < 0.0f ? "null" : Float.valueOf(keyframe.getFraction()));
                sb.append(", ");
                sb.append(", value : ");
                sb.append(keyframe.hasValue() ? keyframe.getValue() : "null");
                Log.d(str, sb.toString());
            }
        }
    }

    private static PropertyValuesHolder loadPvh(Context context, Resources res, Theme theme, XmlPullParser parser, String propertyName, int valueType) throws XmlPullParserException, IOException {
        int type;
        int type2;
        ArrayList arrayList;
        Object obj;
        XmlPullParser xmlPullParser;
        Theme theme2;
        Resources resources;
        Object obj2 = null;
        ArrayList arrayList2 = null;
        int valueType2 = valueType;
        while (true) {
            int next = parser.next();
            type = next;
            if (next == 3 || type == 1) {
                Resources resources2 = res;
                Theme theme3 = theme;
                XmlPullParser xmlPullParser2 = parser;
            } else if (parser.getName().equals("keyframe")) {
                if (valueType2 == 4) {
                    resources = res;
                    theme2 = theme;
                    xmlPullParser = parser;
                    valueType2 = inferValueTypeOfKeyframe(resources, theme2, Xml.asAttributeSet(parser), xmlPullParser);
                } else {
                    resources = res;
                    theme2 = theme;
                    xmlPullParser = parser;
                }
                Keyframe keyframe = loadKeyframe(context, resources, theme2, Xml.asAttributeSet(parser), valueType2, xmlPullParser);
                if (keyframe != null) {
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList();
                    }
                    arrayList2.add(keyframe);
                }
                parser.next();
            } else {
                Resources resources3 = res;
                Theme theme4 = theme;
                XmlPullParser xmlPullParser3 = parser;
            }
        }
        Resources resources22 = res;
        Theme theme32 = theme;
        XmlPullParser xmlPullParser22 = parser;
        if (arrayList2 != null) {
            int size = arrayList2.size();
            int count = size;
            if (size > 0) {
                int i = 0;
                Keyframe firstKeyframe = (Keyframe) arrayList2.get(0);
                Keyframe lastKeyframe = (Keyframe) arrayList2.get(count - 1);
                float endFraction = lastKeyframe.getFraction();
                float f = 0.0f;
                if (endFraction < 1.0f) {
                    if (endFraction < 0.0f) {
                        lastKeyframe.setFraction(1.0f);
                    } else {
                        arrayList2.add(arrayList2.size(), createNewKeyframe(lastKeyframe, 1.0f));
                        count++;
                    }
                }
                float startFraction = firstKeyframe.getFraction();
                if (startFraction != 0.0f) {
                    if (startFraction < 0.0f) {
                        firstKeyframe.setFraction(0.0f);
                    } else {
                        arrayList2.add(0, createNewKeyframe(firstKeyframe, 0.0f));
                        count++;
                    }
                }
                Keyframe[] keyframeArray = new Keyframe[count];
                arrayList2.toArray(keyframeArray);
                while (i < count) {
                    Keyframe keyframe2 = keyframeArray[i];
                    if (keyframe2.getFraction() < f) {
                        if (i == 0) {
                            keyframe2.setFraction(f);
                        } else if (i == count - 1) {
                            keyframe2.setFraction(1.0f);
                        } else {
                            int startIndex = i;
                            int j = startIndex + 1;
                            obj = obj2;
                            int endIndex = i;
                            while (true) {
                                arrayList = arrayList2;
                                type2 = type;
                                int j2 = j;
                                if (j2 >= count - 1) {
                                    break;
                                } else if (keyframeArray[j2].getFraction() >= 0.0f) {
                                    break;
                                } else {
                                    endIndex = j2;
                                    j = j2 + 1;
                                    arrayList2 = arrayList;
                                    type = type2;
                                }
                            }
                            distributeKeyframes(keyframeArray, keyframeArray[endIndex + 1].getFraction() - keyframeArray[startIndex - 1].getFraction(), startIndex, endIndex);
                        }
                        obj = obj2;
                        arrayList = arrayList2;
                        type2 = type;
                    } else {
                        obj = obj2;
                        arrayList = arrayList2;
                        type2 = type;
                    }
                    i++;
                    obj2 = obj;
                    arrayList2 = arrayList;
                    type = type2;
                    f = 0.0f;
                }
                Object obj3 = obj2;
                ArrayList arrayList3 = arrayList2;
                int i2 = type;
                PropertyValuesHolder value = PropertyValuesHolder.ofKeyframe(propertyName, keyframeArray);
                if (valueType2 != 3) {
                    return value;
                }
                value.setEvaluator(ArgbEvaluator.getInstance());
                return value;
            }
        }
        ArrayList arrayList4 = arrayList2;
        int i3 = type;
        String str = propertyName;
        return null;
    }

    private static Keyframe createNewKeyframe(Keyframe sampleKeyframe, float fraction) {
        if (sampleKeyframe.getType() == Float.TYPE) {
            return Keyframe.ofFloat(fraction);
        }
        if (sampleKeyframe.getType() == Integer.TYPE) {
            return Keyframe.ofInt(fraction);
        }
        return Keyframe.ofObject(fraction);
    }

    private static void distributeKeyframes(Keyframe[] keyframes, float gap, int startIndex, int endIndex) {
        float increment = gap / ((float) ((endIndex - startIndex) + 2));
        for (int i = startIndex; i <= endIndex; i++) {
            keyframes[i].setFraction(keyframes[i - 1].getFraction() + increment);
        }
    }

    private static Keyframe loadKeyframe(Context context, Resources res, Theme theme, AttributeSet attrs, int valueType, XmlPullParser parser) throws XmlPullParserException, IOException {
        Keyframe keyframe;
        TypedArray a = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_KEYFRAME);
        Keyframe keyframe2 = null;
        float fraction = TypedArrayUtils.getNamedFloat(a, parser, "fraction", 3, -1.0f);
        TypedValue keyframeValue = TypedArrayUtils.peekNamedValue(a, parser, Param.VALUE, 0);
        boolean hasValue = keyframeValue != null;
        if (valueType == 4) {
            if (!hasValue || !isColorType(keyframeValue.type)) {
                valueType = 0;
            } else {
                valueType = 3;
            }
        }
        if (hasValue) {
            if (valueType != 3) {
                switch (valueType) {
                    case 0:
                        keyframe2 = Keyframe.ofFloat(fraction, TypedArrayUtils.getNamedFloat(a, parser, Param.VALUE, 0, 0.0f));
                        break;
                    case 1:
                        break;
                }
            }
            keyframe2 = Keyframe.ofInt(fraction, TypedArrayUtils.getNamedInt(a, parser, Param.VALUE, 0, 0));
        } else {
            if (valueType == 0) {
                keyframe = Keyframe.ofFloat(fraction);
            } else {
                keyframe = Keyframe.ofInt(fraction);
            }
            keyframe2 = keyframe;
        }
        int resID = TypedArrayUtils.getNamedResourceId(a, parser, "interpolator", 1, 0);
        if (resID > 0) {
            keyframe2.setInterpolator(AnimationUtilsCompat.loadInterpolator(context, resID));
        }
        a.recycle();
        return keyframe2;
    }

    private static ObjectAnimator loadObjectAnimator(Context context, Resources res, Theme theme, AttributeSet attrs, float pathErrorScale, XmlPullParser parser) throws NotFoundException {
        ObjectAnimator anim = new ObjectAnimator();
        loadAnimator(context, res, theme, attrs, anim, pathErrorScale, parser);
        return anim;
    }

    private static ValueAnimator loadAnimator(Context context, Resources res, Theme theme, AttributeSet attrs, ValueAnimator anim, float pathErrorScale, XmlPullParser parser) throws NotFoundException {
        TypedArray arrayAnimator = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_ANIMATOR);
        TypedArray arrayObjectAnimator = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_PROPERTY_ANIMATOR);
        if (anim == null) {
            anim = new ValueAnimator();
        }
        parseAnimatorFromTypeArray(anim, arrayAnimator, arrayObjectAnimator, pathErrorScale, parser);
        int resID = TypedArrayUtils.getNamedResourceId(arrayAnimator, parser, "interpolator", 0, 0);
        if (resID > 0) {
            anim.setInterpolator(AnimationUtilsCompat.loadInterpolator(context, resID));
        }
        arrayAnimator.recycle();
        if (arrayObjectAnimator != null) {
            arrayObjectAnimator.recycle();
        }
        return anim;
    }

    private static boolean isColorType(int type) {
        return type >= 28 && type <= 31;
    }
}
