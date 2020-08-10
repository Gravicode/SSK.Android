package org.apache.commons.math3.geometry.euclidean.oned;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;
import org.apache.commons.math3.exception.MathParseException;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.VectorFormat;
import org.apache.commons.math3.util.CompositeFormat;

public class Vector1DFormat extends VectorFormat<Euclidean1D> {
    public Vector1DFormat() {
        super(VectorFormat.DEFAULT_PREFIX, VectorFormat.DEFAULT_SUFFIX, VectorFormat.DEFAULT_SEPARATOR, CompositeFormat.getDefaultNumberFormat());
    }

    public Vector1DFormat(NumberFormat format) {
        super(VectorFormat.DEFAULT_PREFIX, VectorFormat.DEFAULT_SUFFIX, VectorFormat.DEFAULT_SEPARATOR, format);
    }

    public Vector1DFormat(String prefix, String suffix) {
        super(prefix, suffix, VectorFormat.DEFAULT_SEPARATOR, CompositeFormat.getDefaultNumberFormat());
    }

    public Vector1DFormat(String prefix, String suffix, NumberFormat format) {
        super(prefix, suffix, VectorFormat.DEFAULT_SEPARATOR, format);
    }

    public static Vector1DFormat getInstance() {
        return getInstance(Locale.getDefault());
    }

    public static Vector1DFormat getInstance(Locale locale) {
        return new Vector1DFormat(CompositeFormat.getDefaultNumberFormat(locale));
    }

    public StringBuffer format(Vector<Euclidean1D> vector, StringBuffer toAppendTo, FieldPosition pos) {
        return format(toAppendTo, pos, ((Vector1D) vector).getX());
    }

    public Vector1D parse(String source) throws MathParseException {
        ParsePosition parsePosition = new ParsePosition(0);
        Vector1D result = parse(source, parsePosition);
        if (parsePosition.getIndex() != 0) {
            return result;
        }
        throw new MathParseException(source, parsePosition.getErrorIndex(), Vector1D.class);
    }

    public Vector1D parse(String source, ParsePosition pos) {
        double[] coordinates = parseCoordinates(1, source, pos);
        if (coordinates == null) {
            return null;
        }
        return new Vector1D(coordinates[0]);
    }
}
