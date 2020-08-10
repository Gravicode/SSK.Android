package org.apache.commons.math3.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class BigReal implements FieldElement<BigReal>, Comparable<BigReal>, Serializable {
    public static final BigReal ONE = new BigReal(BigDecimal.ONE);
    public static final BigReal ZERO = new BigReal(BigDecimal.ZERO);
    private static final long serialVersionUID = 4984534880991310382L;

    /* renamed from: d */
    private final BigDecimal f797d;
    private RoundingMode roundingMode = RoundingMode.HALF_UP;
    private int scale = 64;

    public BigReal(BigDecimal val) {
        this.f797d = val;
    }

    public BigReal(BigInteger val) {
        this.f797d = new BigDecimal(val);
    }

    public BigReal(BigInteger unscaledVal, int scale2) {
        this.f797d = new BigDecimal(unscaledVal, scale2);
    }

    public BigReal(BigInteger unscaledVal, int scale2, MathContext mc) {
        this.f797d = new BigDecimal(unscaledVal, scale2, mc);
    }

    public BigReal(BigInteger val, MathContext mc) {
        this.f797d = new BigDecimal(val, mc);
    }

    public BigReal(char[] in) {
        this.f797d = new BigDecimal(in);
    }

    public BigReal(char[] in, int offset, int len) {
        this.f797d = new BigDecimal(in, offset, len);
    }

    public BigReal(char[] in, int offset, int len, MathContext mc) {
        this.f797d = new BigDecimal(in, offset, len, mc);
    }

    public BigReal(char[] in, MathContext mc) {
        this.f797d = new BigDecimal(in, mc);
    }

    public BigReal(double val) {
        this.f797d = new BigDecimal(val);
    }

    public BigReal(double val, MathContext mc) {
        this.f797d = new BigDecimal(val, mc);
    }

    public BigReal(int val) {
        this.f797d = new BigDecimal(val);
    }

    public BigReal(int val, MathContext mc) {
        this.f797d = new BigDecimal(val, mc);
    }

    public BigReal(long val) {
        this.f797d = new BigDecimal(val);
    }

    public BigReal(long val, MathContext mc) {
        this.f797d = new BigDecimal(val, mc);
    }

    public BigReal(String val) {
        this.f797d = new BigDecimal(val);
    }

    public BigReal(String val, MathContext mc) {
        this.f797d = new BigDecimal(val, mc);
    }

    public RoundingMode getRoundingMode() {
        return this.roundingMode;
    }

    public void setRoundingMode(RoundingMode roundingMode2) {
        this.roundingMode = roundingMode2;
    }

    public int getScale() {
        return this.scale;
    }

    public void setScale(int scale2) {
        this.scale = scale2;
    }

    public BigReal add(BigReal a) {
        return new BigReal(this.f797d.add(a.f797d));
    }

    public BigReal subtract(BigReal a) {
        return new BigReal(this.f797d.subtract(a.f797d));
    }

    public BigReal negate() {
        return new BigReal(this.f797d.negate());
    }

    public BigReal divide(BigReal a) throws MathArithmeticException {
        try {
            return new BigReal(this.f797d.divide(a.f797d, this.scale, this.roundingMode));
        } catch (ArithmeticException e) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NOT_ALLOWED, new Object[0]);
        }
    }

    public BigReal reciprocal() throws MathArithmeticException {
        try {
            return new BigReal(BigDecimal.ONE.divide(this.f797d, this.scale, this.roundingMode));
        } catch (ArithmeticException e) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NOT_ALLOWED, new Object[0]);
        }
    }

    public BigReal multiply(BigReal a) {
        return new BigReal(this.f797d.multiply(a.f797d));
    }

    public BigReal multiply(int n) {
        return new BigReal(this.f797d.multiply(new BigDecimal(n)));
    }

    public int compareTo(BigReal a) {
        return this.f797d.compareTo(a.f797d);
    }

    public double doubleValue() {
        return this.f797d.doubleValue();
    }

    public BigDecimal bigDecimalValue() {
        return this.f797d;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof BigReal) {
            return this.f797d.equals(((BigReal) other).f797d);
        }
        return false;
    }

    public int hashCode() {
        return this.f797d.hashCode();
    }

    public Field<BigReal> getField() {
        return BigRealField.getInstance();
    }
}
