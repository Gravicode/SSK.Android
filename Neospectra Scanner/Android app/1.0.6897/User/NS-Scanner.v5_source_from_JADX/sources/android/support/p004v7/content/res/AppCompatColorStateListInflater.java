package android.support.p004v7.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p001v4.graphics.ColorUtils;
import android.support.p004v7.appcompat.C1123R;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* renamed from: android.support.v7.content.res.AppCompatColorStateListInflater */
final class AppCompatColorStateListInflater {
    private static final int DEFAULT_COLOR = -65536;

    private AppCompatColorStateListInflater() {
    }

    @NonNull
    public static ColorStateList createFromXml(@NonNull Resources r, @NonNull XmlPullParser parser, @Nullable Theme theme) throws XmlPullParserException, IOException {
        int type;
        AttributeSet attrs = Xml.asAttributeSet(parser);
        do {
            int next = parser.next();
            type = next;
            if (next == 2) {
                break;
            }
        } while (type != 1);
        if (type == 2) {
            return createFromXmlInner(r, parser, attrs, theme);
        }
        throw new XmlPullParserException("No start tag found");
    }

    @NonNull
    private static ColorStateList createFromXmlInner(@NonNull Resources r, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Theme theme) throws XmlPullParserException, IOException {
        String name = parser.getName();
        if (name.equals("selector")) {
            return inflate(r, parser, attrs, theme);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(parser.getPositionDescription());
        sb.append(": invalid color state list tag ");
        sb.append(name);
        throw new XmlPullParserException(sb.toString());
    }

    private static ColorStateList inflate(@NonNull Resources r, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Theme theme) throws XmlPullParserException, IOException {
        int defaultColor;
        int innerDepth;
        AttributeSet attributeSet = attrs;
        int i = 1;
        int innerDepth2 = parser.getDepth() + 1;
        int[][] stateSpecList = new int[20][];
        int[] colorList = new int[stateSpecList.length];
        int defaultColor2 = -65536;
        int listSize = 0;
        while (true) {
            int next = parser.next();
            int type = next;
            if (next == i) {
                Resources resources = r;
                Theme theme2 = theme;
                int i2 = innerDepth2;
                int i3 = defaultColor2;
                break;
            }
            int depth = parser.getDepth();
            int depth2 = depth;
            if (depth < innerDepth2 && type == 3) {
                Resources resources2 = r;
                Theme theme3 = theme;
                int i4 = innerDepth2;
                int i5 = defaultColor2;
                break;
            }
            if (type != 2 || depth2 > innerDepth2) {
                Resources resources3 = r;
                Theme theme4 = theme;
                innerDepth = innerDepth2;
                defaultColor = defaultColor2;
            } else if (!parser.getName().equals("item")) {
                Resources resources4 = r;
                Theme theme5 = theme;
                innerDepth = innerDepth2;
                defaultColor = defaultColor2;
            } else {
                TypedArray a = obtainAttributes(r, theme, attributeSet, C1123R.styleable.ColorStateListItem);
                int baseColor = a.getColor(C1123R.styleable.ColorStateListItem_android_color, -65281);
                float alphaMod = 1.0f;
                if (a.hasValue(C1123R.styleable.ColorStateListItem_android_alpha)) {
                    alphaMod = a.getFloat(C1123R.styleable.ColorStateListItem_android_alpha, 1.0f);
                } else if (a.hasValue(C1123R.styleable.ColorStateListItem_alpha)) {
                    alphaMod = a.getFloat(C1123R.styleable.ColorStateListItem_alpha, 1.0f);
                }
                a.recycle();
                int numAttrs = attrs.getAttributeCount();
                int[] stateSpec = new int[numAttrs];
                int innerDepth3 = innerDepth2;
                int j = 0;
                int i6 = 0;
                while (i6 < numAttrs) {
                    int numAttrs2 = numAttrs;
                    int stateResId = attributeSet.getAttributeNameResource(i6);
                    int defaultColor3 = defaultColor2;
                    if (!(stateResId == 16843173 || stateResId == 16843551 || stateResId == C1123R.attr.alpha)) {
                        int j2 = j + 1;
                        stateSpec[j] = attributeSet.getAttributeBooleanValue(i6, false) ? stateResId : -stateResId;
                        j = j2;
                    }
                    i6++;
                    numAttrs = numAttrs2;
                    defaultColor2 = defaultColor3;
                }
                int defaultColor4 = defaultColor2;
                int[] stateSpec2 = StateSet.trimStateSet(stateSpec, j);
                int color = modulateColorAlpha(baseColor, alphaMod);
                if (listSize == 0 || stateSpec2.length == 0) {
                    defaultColor2 = color;
                } else {
                    defaultColor2 = defaultColor4;
                }
                colorList = GrowingArrayUtils.append(colorList, listSize, color);
                stateSpecList = (int[][]) GrowingArrayUtils.append((T[]) stateSpecList, listSize, stateSpec2);
                listSize++;
                innerDepth2 = innerDepth3;
                i = 1;
            }
            innerDepth2 = innerDepth;
            defaultColor2 = defaultColor;
            i = 1;
        }
        int[] colors = new int[listSize];
        int[][] stateSpecs = new int[listSize][];
        System.arraycopy(colorList, 0, colors, 0, listSize);
        System.arraycopy(stateSpecList, 0, stateSpecs, 0, listSize);
        return new ColorStateList(stateSpecs, colors);
    }

    private static TypedArray obtainAttributes(Resources res, Theme theme, AttributeSet set, int[] attrs) {
        if (theme == null) {
            return res.obtainAttributes(set, attrs);
        }
        return theme.obtainStyledAttributes(set, attrs, 0, 0);
    }

    private static int modulateColorAlpha(int color, float alphaMod) {
        return ColorUtils.setAlphaComponent(color, Math.round(((float) Color.alpha(color)) * alphaMod));
    }
}
