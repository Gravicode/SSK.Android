package org.apache.commons.math3.geometry.euclidean.twod;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;
import org.apache.commons.math3.exception.MathParseException;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.VectorFormat;
import org.apache.commons.math3.util.CompositeFormat;

public class Vector2DFormat extends VectorFormat<Euclidean2D> {
    public Vector2DFormat() {
        super(VectorFormat.DEFAULT_PREFIX, VectorFormat.DEFAULT_SUFFIX, VectorFormat.DEFAULT_SEPARATOR, CompositeFormat.getDefaultNumberFormat());
    }

    public Vector2DFormat(NumberFormat format) {
        super(VectorFormat.DEFAULT_PREFIX, VectorFormat.DEFAULT_SUFFIX, VectorFormat.DEFAULT_SEPARATOR, format);
    }

    public Vector2DFormat(String prefix, String suffix, String separator) {
        super(prefix, suffix, separator, CompositeFormat.getDefaultNumberFormat());
    }

    public Vector2DFormat(String prefix, String suffix, String separator, NumberFormat format) {
        super(prefix, suffix, separator, format);
    }

    public static Vector2DFormat getInstance() {
        return getInstance(Locale.getDefault());
    }

    public static Vector2DFormat getInstance(Locale locale) {
        return new Vector2DFormat(CompositeFormat.getDefaultNumberFormat(locale));
    }

    public StringBuffer format(Vector<Euclidean2D> vector, StringBuffer toAppendTo, FieldPosition pos) {
        Vector2D p2 = (Vector2D) vector;
        return format(toAppendTo, pos, p2.getX(), p2.getY());
    }

    public Vector2D parse(String source) throws MathParseException {
        ParsePosition parsePosition = new ParsePosition(0);
        Vector2D result = parse(source, parsePosition);
        if (parsePosition.getIndex() != 0) {
            return result;
        }
        throw new MathParseException(source, parsePosition.getErrorIndex(), Vector2D.class);
    }

    public Vector2D parse(String source, ParsePosition pos) {
        double[] coordinates = parseCoordinates(2, source, pos);
        if (coordinates == null) {
            return null;
        }
        return new Vector2D(coordinates[0], coordinates[1]);
    }
}
