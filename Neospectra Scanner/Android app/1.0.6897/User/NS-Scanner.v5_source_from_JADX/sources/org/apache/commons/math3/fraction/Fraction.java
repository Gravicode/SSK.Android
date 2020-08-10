package org.apache.commons.math3.fraction;

import java.io.Serializable;
import java.math.BigInteger;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.FastMath;

public class Fraction extends Number implements FieldElement<Fraction>, Comparable<Fraction>, Serializable {
    private static final double DEFAULT_EPSILON = 1.0E-5d;
    public static final Fraction FOUR_FIFTHS = new Fraction(4, 5);
    public static final Fraction MINUS_ONE = new Fraction(-1, 1);
    public static final Fraction ONE = new Fraction(1, 1);
    public static final Fraction ONE_FIFTH = new Fraction(1, 5);
    public static final Fraction ONE_HALF = new Fraction(1, 2);
    public static final Fraction ONE_QUARTER = new Fraction(1, 4);
    public static final Fraction ONE_THIRD = new Fraction(1, 3);
    public static final Fraction THREE_FIFTHS = new Fraction(3, 5);
    public static final Fraction THREE_QUARTERS = new Fraction(3, 4);
    public static final Fraction TWO = new Fraction(2, 1);
    public static final Fraction TWO_FIFTHS = new Fraction(2, 5);
    public static final Fraction TWO_QUARTERS = new Fraction(2, 4);
    public static final Fraction TWO_THIRDS = new Fraction(2, 3);
    public static final Fraction ZERO = new Fraction(0, 1);
    private static final long serialVersionUID = 3698073679419233275L;
    private final int denominator;
    private final int numerator;

    public Fraction(double value) throws FractionConversionException {
        this(value, 1.0E-5d, 100);
    }

    public Fraction(double value, double epsilon, int maxIterations) throws FractionConversionException {
        this(value, epsilon, Integer.MAX_VALUE, maxIterations);
    }

    public Fraction(double value, int maxDenominator) throws FractionConversionException {
        this(value, 0.0d, maxDenominator, 100);
    }

    private Fraction(double value, double epsilon, int maxDenominator, int maxIterations) throws FractionConversionException {
        boolean stop;
        int n;
        long a0;
        double r1;
        long p2;
        long a1;
        long q2;
        long q1;
        long q12;
        long q13;
        boolean stop2;
        long q14;
        long a02;
        double d = value;
        int i = maxDenominator;
        int i2 = maxIterations;
        long overflow = 2147483647L;
        double r0 = d;
        long a03 = (long) FastMath.floor(r0);
        if (FastMath.abs(a03) > 2147483647L) {
            long j = a03;
            double d2 = r0;
            FractionConversionException fractionConversionException = new FractionConversionException(d, a03, 1);
            throw fractionConversionException;
        }
        double r02 = r0;
        long a04 = a03;
        if (FastMath.abs(((double) a04) - d) < epsilon) {
            this.numerator = (int) a04;
            this.denominator = 1;
            return;
        }
        long q0 = a04;
        int n2 = 0;
        boolean stop3 = false;
        long j2 = a04;
        long q15 = 1;
        long q16 = 0;
        long p1 = 1;
        long q22 = j2;
        while (true) {
            stop = stop3;
            n = n2 + 1;
            double r12 = 1.0d / (r02 - ((double) q22));
            a0 = q22;
            long a12 = (long) FastMath.floor(r12);
            r1 = r12;
            p2 = (a12 * q0) + p1;
            a1 = a12;
            q2 = (a12 * q15) + q16;
            if (FastMath.abs(p2) > overflow) {
                q1 = q15;
                long j3 = overflow;
                break;
            } else if (FastMath.abs(q2) > overflow) {
                q1 = q15;
                long j4 = overflow;
                break;
            } else {
                long overflow2 = overflow;
                long q17 = q15;
                double convergent = ((double) p2) / ((double) q2);
                if (n >= i2 || FastMath.abs(convergent - d) <= epsilon || q2 >= ((long) i)) {
                    stop2 = true;
                    a02 = a0;
                    q15 = q17;
                } else {
                    a02 = a1;
                    r02 = r1;
                    long j5 = q0;
                    stop2 = stop;
                    q15 = q2;
                    q16 = q17;
                    q0 = p2;
                    p1 = j5;
                }
                if (stop2) {
                    q12 = q15;
                    q13 = q0;
                    break;
                }
                long j6 = q2;
                q22 = a02;
                overflow = overflow2;
                long a05 = p2;
                n2 = n;
                stop3 = stop2;
            }
        }
        if (epsilon == 0.0d) {
            long q18 = q1;
            q14 = q18;
            if (FastMath.abs(q18) < ((long) i)) {
                q13 = q0;
                stop2 = stop;
                long j7 = a0;
                q12 = q14;
                if (n >= i2) {
                    boolean z = stop2;
                    throw new FractionConversionException(d, i2);
                }
                if (q2 < ((long) i)) {
                    this.numerator = (int) p2;
                    this.denominator = (int) q2;
                } else {
                    this.numerator = (int) q13;
                    this.denominator = (int) q12;
                }
                return;
            }
        } else {
            q14 = q1;
        }
        long j8 = q14;
        double d3 = r1;
        long j9 = a0;
        long j10 = a1;
        int i3 = n;
        FractionConversionException fractionConversionException2 = new FractionConversionException(d, p2, q2);
        throw fractionConversionException2;
    }

    public Fraction(int num) {
        this(num, 1);
    }

    public Fraction(int num, int den) {
        if (den == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR_IN_FRACTION, Integer.valueOf(num), Integer.valueOf(den));
        }
        if (den < 0) {
            if (num == Integer.MIN_VALUE || den == Integer.MIN_VALUE) {
                throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_FRACTION, Integer.valueOf(num), Integer.valueOf(den));
            }
            num = -num;
            den = -den;
        }
        int d = ArithmeticUtils.gcd(num, den);
        if (d > 1) {
            num /= d;
            den /= d;
        }
        if (den < 0) {
            num = -num;
            den = -den;
        }
        this.numerator = num;
        this.denominator = den;
    }

    public Fraction abs() {
        if (this.numerator >= 0) {
            return this;
        }
        return negate();
    }

    public int compareTo(Fraction object) {
        long nOd = ((long) this.numerator) * ((long) object.denominator);
        long dOn = ((long) this.denominator) * ((long) object.numerator);
        if (nOd < dOn) {
            return -1;
        }
        return nOd > dOn ? 1 : 0;
    }

    public double doubleValue() {
        return ((double) this.numerator) / ((double) this.denominator);
    }

    public boolean equals(Object other) {
        boolean z = true;
        if (this == other) {
            return true;
        }
        if (!(other instanceof Fraction)) {
            return false;
        }
        Fraction rhs = (Fraction) other;
        if (!(this.numerator == rhs.numerator && this.denominator == rhs.denominator)) {
            z = false;
        }
        return z;
    }

    public float floatValue() {
        return (float) doubleValue();
    }

    public int getDenominator() {
        return this.denominator;
    }

    public int getNumerator() {
        return this.numerator;
    }

    public int hashCode() {
        return ((this.numerator + 629) * 37) + this.denominator;
    }

    public int intValue() {
        return (int) doubleValue();
    }

    public long longValue() {
        return (long) doubleValue();
    }

    public Fraction negate() {
        if (this.numerator != Integer.MIN_VALUE) {
            return new Fraction(-this.numerator, this.denominator);
        }
        throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_FRACTION, Integer.valueOf(this.numerator), Integer.valueOf(this.denominator));
    }

    public Fraction reciprocal() {
        return new Fraction(this.denominator, this.numerator);
    }

    public Fraction add(Fraction fraction) {
        return addSub(fraction, true);
    }

    public Fraction add(int i) {
        return new Fraction(this.numerator + (this.denominator * i), this.denominator);
    }

    public Fraction subtract(Fraction fraction) {
        return addSub(fraction, false);
    }

    public Fraction subtract(int i) {
        return new Fraction(this.numerator - (this.denominator * i), this.denominator);
    }

    private Fraction addSub(Fraction fraction, boolean isAdd) {
        if (fraction == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION, new Object[0]);
        } else if (this.numerator == 0) {
            return isAdd ? fraction : fraction.negate();
        } else if (fraction.numerator == 0) {
            return this;
        } else {
            int d1 = ArithmeticUtils.gcd(this.denominator, fraction.denominator);
            if (d1 == 1) {
                int uvp = ArithmeticUtils.mulAndCheck(this.numerator, fraction.denominator);
                int upv = ArithmeticUtils.mulAndCheck(fraction.numerator, this.denominator);
                return new Fraction(isAdd ? ArithmeticUtils.addAndCheck(uvp, upv) : ArithmeticUtils.subAndCheck(uvp, upv), ArithmeticUtils.mulAndCheck(this.denominator, fraction.denominator));
            }
            BigInteger uvp2 = BigInteger.valueOf((long) this.numerator).multiply(BigInteger.valueOf((long) (fraction.denominator / d1)));
            BigInteger upv2 = BigInteger.valueOf((long) fraction.numerator).multiply(BigInteger.valueOf((long) (this.denominator / d1)));
            BigInteger t = isAdd ? uvp2.add(upv2) : uvp2.subtract(upv2);
            int tmodd1 = t.mod(BigInteger.valueOf((long) d1)).intValue();
            int d2 = tmodd1 == 0 ? d1 : ArithmeticUtils.gcd(tmodd1, d1);
            BigInteger w = t.divide(BigInteger.valueOf((long) d2));
            if (w.bitLength() <= 31) {
                return new Fraction(w.intValue(), ArithmeticUtils.mulAndCheck(this.denominator / d1, fraction.denominator / d2));
            }
            throw new MathArithmeticException(LocalizedFormats.NUMERATOR_OVERFLOW_AFTER_MULTIPLY, w);
        }
    }

    public Fraction multiply(Fraction fraction) {
        if (fraction == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION, new Object[0]);
        } else if (this.numerator == 0 || fraction.numerator == 0) {
            return ZERO;
        } else {
            int d1 = ArithmeticUtils.gcd(this.numerator, fraction.denominator);
            int d2 = ArithmeticUtils.gcd(fraction.numerator, this.denominator);
            return getReducedFraction(ArithmeticUtils.mulAndCheck(this.numerator / d1, fraction.numerator / d2), ArithmeticUtils.mulAndCheck(this.denominator / d2, fraction.denominator / d1));
        }
    }

    public Fraction multiply(int i) {
        return multiply(new Fraction(i));
    }

    public Fraction divide(Fraction fraction) {
        if (fraction == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION, new Object[0]);
        } else if (fraction.numerator != 0) {
            return multiply(fraction.reciprocal());
        } else {
            throw new MathArithmeticException(LocalizedFormats.ZERO_FRACTION_TO_DIVIDE_BY, Integer.valueOf(fraction.numerator), Integer.valueOf(fraction.denominator));
        }
    }

    public Fraction divide(int i) {
        return divide(new Fraction(i));
    }

    public double percentageValue() {
        return doubleValue() * 100.0d;
    }

    public static Fraction getReducedFraction(int numerator2, int denominator2) {
        if (denominator2 == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR_IN_FRACTION, Integer.valueOf(numerator2), Integer.valueOf(denominator2));
        } else if (numerator2 == 0) {
            return ZERO;
        } else {
            if (denominator2 == Integer.MIN_VALUE && (numerator2 & 1) == 0) {
                numerator2 /= 2;
                denominator2 /= 2;
            }
            if (denominator2 < 0) {
                if (numerator2 == Integer.MIN_VALUE || denominator2 == Integer.MIN_VALUE) {
                    throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_FRACTION, Integer.valueOf(numerator2), Integer.valueOf(denominator2));
                }
                numerator2 = -numerator2;
                denominator2 = -denominator2;
            }
            int gcd = ArithmeticUtils.gcd(numerator2, denominator2);
            return new Fraction(numerator2 / gcd, denominator2 / gcd);
        }
    }

    public String toString() {
        if (this.denominator == 1) {
            return Integer.toString(this.numerator);
        }
        if (this.numerator == 0) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.numerator);
        sb.append(" / ");
        sb.append(this.denominator);
        return sb.toString();
    }

    public FractionField getField() {
        return FractionField.getInstance();
    }
}
