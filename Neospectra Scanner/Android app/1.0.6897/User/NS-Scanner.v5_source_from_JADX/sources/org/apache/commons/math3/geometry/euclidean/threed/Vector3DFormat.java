package org.apache.commons.math3.geometry.euclidean.threed;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;
import org.apache.commons.math3.exception.MathParseException;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.VectorFormat;
import org.apache.commons.math3.util.CompositeFormat;

public class Vector3DFormat extends VectorFormat<Euclidean3D> {
    public Vector3DFormat() {
        super(VectorFormat.DEFAULT_PREFIX, VectorFormat.DEFAULT_SUFFIX, VectorFormat.DEFAULT_SEPARATOR, CompositeFormat.getDefaultNumberFormat());
    }

    public Vector3DFormat(NumberFormat format) {
        super(VectorFormat.DEFAULT_PREFIX, VectorFormat.DEFAULT_SUFFIX, VectorFormat.DEFAULT_SEPARATOR, format);
    }

    public Vector3DFormat(String prefix, String suffix, String separator) {
        super(prefix, suffix, separator, CompositeFormat.getDefaultNumberFormat());
    }

    public Vector3DFormat(String prefix, String suffix, String separator, NumberFormat format) {
        super(prefix, suffix, separator, format);
    }

    public static Vector3DFormat getInstance() {
        return getInstance(Locale.getDefault());
    }

    public static Vector3DFormat getInstance(Locale locale) {
        return new Vector3DFormat(CompositeFormat.getDefaultNumberFormat(locale));
    }

    public StringBuffer format(Vector<Euclidean3D> vector, StringBuffer toAppendTo, FieldPosition pos) {
        Vector3D v3 = (Vector3D) vector;
        return format(toAppendTo, pos, v3.getX(), v3.getY(), v3.getZ());
    }

    public Vector3D parse(String source) throws MathParseException {
        ParsePosition parsePosition = new ParsePosition(0);
        Vector3D result = parse(source, parsePosition);
        if (parsePosition.getIndex() != 0) {
            return result;
        }
        throw new MathParseException(source, parsePosition.getErrorIndex(), Vector3D.class);
    }

    public Vector3D parse(String source, ParsePosition pos) {
        double[] coordinates = parseCoordinates(3, source, pos);
        if (coordinates == null) {
            return null;
        }
        Vector3D vector3D = new Vector3D(coordinates[0], coordinates[1], coordinates[2]);
        return vector3D;
    }
}
