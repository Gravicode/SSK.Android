package org.apache.commons.math3.dfp;

import java.util.Arrays;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.dfp.DfpField.RoundingMode;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.FastMath;
import org.apache.poi.hssf.record.formula.UnionPtg;

public class Dfp implements RealFieldElement<Dfp> {
    private static final String ADD_TRAP = "add";
    private static final String ALIGN_TRAP = "align";
    private static final String DIVIDE_TRAP = "divide";
    public static final int ERR_SCALE = 32760;
    public static final byte FINITE = 0;
    private static final String GREATER_THAN_TRAP = "greaterThan";
    public static final byte INFINITE = 1;
    private static final String LESS_THAN_TRAP = "lessThan";
    public static final int MAX_EXP = 32768;
    public static final int MIN_EXP = -32767;
    private static final String MULTIPLY_TRAP = "multiply";
    private static final String NAN_STRING = "NaN";
    private static final String NEG_INFINITY_STRING = "-Infinity";
    private static final String NEW_INSTANCE_TRAP = "newInstance";
    private static final String NEXT_AFTER_TRAP = "nextAfter";
    private static final String POS_INFINITY_STRING = "Infinity";
    public static final byte QNAN = 3;
    public static final int RADIX = 10000;
    public static final byte SNAN = 2;
    private static final String SQRT_TRAP = "sqrt";
    private static final String TRUNC_TRAP = "trunc";
    protected int exp;
    private final DfpField field;
    protected int[] mant;
    protected byte nans;
    protected byte sign;

    protected Dfp(DfpField field2) {
        this.mant = new int[field2.getRadixDigits()];
        this.sign = 1;
        this.exp = 0;
        this.nans = 0;
        this.field = field2;
    }

    protected Dfp(DfpField field2, byte x) {
        this(field2, (long) x);
    }

    protected Dfp(DfpField field2, int x) {
        this(field2, (long) x);
    }

    protected Dfp(DfpField field2, long x) {
        this.mant = new int[field2.getRadixDigits()];
        this.nans = 0;
        this.field = field2;
        boolean isLongMin = false;
        if (x == Long.MIN_VALUE) {
            isLongMin = true;
            x++;
        }
        if (x < 0) {
            this.sign = -1;
            x = -x;
        } else {
            this.sign = 1;
        }
        this.exp = 0;
        while (x != 0) {
            System.arraycopy(this.mant, this.mant.length - this.exp, this.mant, (this.mant.length - 1) - this.exp, this.exp);
            this.mant[this.mant.length - 1] = (int) (x % 10000);
            x /= 10000;
            this.exp++;
        }
        if (isLongMin) {
            for (int i = 0; i < this.mant.length - 1; i++) {
                if (this.mant[i] != 0) {
                    int[] iArr = this.mant;
                    iArr[i] = iArr[i] + 1;
                    return;
                }
            }
        }
    }

    protected Dfp(DfpField field2, double x) {
        DfpField dfpField = field2;
        this.mant = new int[field2.getRadixDigits()];
        this.sign = 1;
        this.exp = 0;
        this.nans = 0;
        this.field = dfpField;
        long bits = Double.doubleToLongBits(x);
        long mantissa = bits & IEEEDouble.FRAC_MASK;
        int exponent = ((int) ((9218868437227405312L & bits) >> 52)) - 1023;
        if (exponent == -1023) {
            if (x == 0.0d) {
                if ((bits & Long.MIN_VALUE) != 0) {
                    this.sign = -1;
                }
                return;
            }
            exponent++;
            while ((mantissa & IEEEDouble.FRAC_ASSUMED_HIGH_BIT) == 0) {
                exponent--;
                mantissa <<= 1;
            }
            mantissa &= IEEEDouble.FRAC_MASK;
        }
        if (exponent == 1024) {
            if (x != x) {
                this.sign = 1;
                this.nans = 3;
            } else if (x < 0.0d) {
                this.sign = -1;
                this.nans = 1;
            } else {
                this.sign = 1;
                this.nans = 1;
            }
            return;
        }
        Dfp xdfp = new Dfp(dfpField, mantissa).divide(new Dfp(dfpField, (long) IEEEDouble.FRAC_ASSUMED_HIGH_BIT)).add(field2.getOne()).multiply(DfpMath.pow(field2.getTwo(), exponent));
        if ((bits & Long.MIN_VALUE) != 0) {
            xdfp = xdfp.negate();
        }
        System.arraycopy(xdfp.mant, 0, this.mant, 0, this.mant.length);
        this.sign = xdfp.sign;
        this.exp = xdfp.exp;
        this.nans = xdfp.nans;
    }

    public Dfp(Dfp d) {
        this.mant = (int[]) d.mant.clone();
        this.sign = d.sign;
        this.exp = d.exp;
        this.nans = d.nans;
        this.field = d.field;
    }

    protected Dfp(DfpField field2, String s) {
        String fpdecimal;
        int p;
        int i;
        String str = s;
        this.mant = new int[field2.getRadixDigits()];
        this.sign = 1;
        this.exp = 0;
        this.nans = 0;
        this.field = field2;
        char[] striped = new char[((getRadixDigits() * 4) + 8)];
        if (str.equals(POS_INFINITY_STRING)) {
            this.sign = 1;
            this.nans = 1;
        } else if (str.equals(NEG_INFINITY_STRING)) {
            this.sign = -1;
            this.nans = 1;
        } else if (str.equals(NAN_STRING)) {
            this.sign = 1;
            this.nans = 3;
        } else {
            int p2 = str.indexOf("e");
            if (p2 == -1) {
                p2 = str.indexOf("E");
            }
            int sciexp = 0;
            char c = '9';
            char c2 = '0';
            if (p2 != -1) {
                fpdecimal = str.substring(0, p2);
                String fpexp = str.substring(p2 + 1);
                boolean negative = false;
                int sciexp2 = 0;
                for (int i2 = 0; i2 < fpexp.length(); i2++) {
                    if (fpexp.charAt(i2) == '-') {
                        negative = true;
                    } else if (fpexp.charAt(i2) >= '0' && fpexp.charAt(i2) <= '9') {
                        sciexp2 = ((sciexp2 * 10) + fpexp.charAt(i2)) - 48;
                    }
                }
                if (negative) {
                    sciexp = -sciexp2;
                } else {
                    sciexp = sciexp2;
                }
            } else {
                fpdecimal = str;
            }
            String fpdecimal2 = fpdecimal;
            if (fpdecimal2.indexOf("-") != -1) {
                this.sign = -1;
            }
            boolean decimalFound = false;
            int p3 = 0;
            int decimalPos = 0;
            while (true) {
                if (fpdecimal2.charAt(p3) >= '1' && fpdecimal2.charAt(p3) <= c) {
                    break;
                }
                if (decimalFound && fpdecimal2.charAt(p3) == '0') {
                    decimalPos--;
                }
                if (fpdecimal2.charAt(p3) == '.') {
                    decimalFound = true;
                }
                p3++;
                if (p3 == fpdecimal2.length()) {
                    break;
                }
                c = '9';
            }
            int q = 4;
            int significantDigits = 0;
            striped[0] = '0';
            striped[1] = '0';
            striped[2] = '0';
            striped[3] = '0';
            while (true) {
                if (p == fpdecimal2.length()) {
                    i = 4;
                    break;
                }
                i = 4;
                if (q == (this.mant.length * 4) + 4 + 1) {
                    break;
                }
                if (fpdecimal2.charAt(p) == '.') {
                    decimalFound = true;
                    decimalPos = significantDigits;
                    p++;
                    String str2 = s;
                } else {
                    if (fpdecimal2.charAt(p) >= '0') {
                        if (fpdecimal2.charAt(p) <= '9') {
                            striped[q] = fpdecimal2.charAt(p);
                            q++;
                            p++;
                            significantDigits++;
                            String str3 = s;
                        }
                    }
                    p++;
                    String str32 = s;
                }
                c2 = '0';
            }
            if (decimalFound && q != i) {
                while (true) {
                    q--;
                    if (q == i || striped[q] != c2) {
                        break;
                    }
                    significantDigits--;
                    i = 4;
                }
            }
            if (decimalFound && significantDigits == 0) {
                decimalPos = 0;
            }
            if (!decimalFound) {
                decimalPos = q - 4;
            }
            int p4 = (significantDigits - 1) + 4;
            while (p4 > 4 && striped[p4] == c2) {
                p4--;
            }
            int i3 = 4;
            int i4 = ((400 - decimalPos) - (sciexp % 4)) % 4;
            int q2 = 4 - i4;
            int decimalPos2 = decimalPos + i4;
            while (p4 - q2 < this.mant.length * 4) {
                int i5 = 0;
                while (i5 < i3) {
                    p4++;
                    striped[p4] = '0';
                    i5++;
                    i3 = 4;
                }
                String str4 = s;
            }
            for (int i6 = this.mant.length - 1; i6 >= 0; i6--) {
                this.mant[i6] = ((striped[q2] - '0') * 1000) + ((striped[q2 + 1] - '0') * 100) + ((striped[q2 + 2] - '0') * 10) + (striped[q2 + 3] - '0');
                q2 += 4;
            }
            this.exp = (decimalPos2 + sciexp) / 4;
            if (q2 < striped.length) {
                round((striped[q2] - '0') * 1000);
            }
        }
    }

    protected Dfp(DfpField field2, byte sign2, byte nans2) {
        this.field = field2;
        this.mant = new int[field2.getRadixDigits()];
        this.sign = sign2;
        this.exp = 0;
        this.nans = nans2;
    }

    public Dfp newInstance() {
        return new Dfp(getField());
    }

    public Dfp newInstance(byte x) {
        return new Dfp(getField(), x);
    }

    public Dfp newInstance(int x) {
        return new Dfp(getField(), x);
    }

    public Dfp newInstance(long x) {
        return new Dfp(getField(), x);
    }

    public Dfp newInstance(double x) {
        return new Dfp(getField(), x);
    }

    public Dfp newInstance(Dfp d) {
        if (this.field.getRadixDigits() == d.field.getRadixDigits()) {
            return new Dfp(d);
        }
        this.field.setIEEEFlagsBits(1);
        Dfp result = newInstance(getZero());
        result.nans = 3;
        return dotrap(1, NEW_INSTANCE_TRAP, d, result);
    }

    public Dfp newInstance(String s) {
        return new Dfp(this.field, s);
    }

    public Dfp newInstance(byte sig, byte code) {
        return this.field.newDfp(sig, code);
    }

    public DfpField getField() {
        return this.field;
    }

    public int getRadixDigits() {
        return this.field.getRadixDigits();
    }

    public Dfp getZero() {
        return this.field.getZero();
    }

    public Dfp getOne() {
        return this.field.getOne();
    }

    public Dfp getTwo() {
        return this.field.getTwo();
    }

    /* access modifiers changed from: protected */
    public void shiftLeft() {
        for (int i = this.mant.length - 1; i > 0; i--) {
            this.mant[i] = this.mant[i - 1];
        }
        this.mant[0] = 0;
        this.exp--;
    }

    /* access modifiers changed from: protected */
    public void shiftRight() {
        for (int i = 0; i < this.mant.length - 1; i++) {
            this.mant[i] = this.mant[i + 1];
        }
        this.mant[this.mant.length - 1] = 0;
        this.exp++;
    }

    /* access modifiers changed from: protected */
    public int align(int e) {
        boolean inexact = false;
        int diff = this.exp - e;
        int adiff = diff;
        if (adiff < 0) {
            adiff = -adiff;
        }
        if (diff == 0) {
            return 0;
        }
        if (adiff > this.mant.length + 1) {
            Arrays.fill(this.mant, 0);
            this.exp = e;
            this.field.setIEEEFlagsBits(16);
            dotrap(16, ALIGN_TRAP, this, this);
            return 0;
        }
        int lostdigit = 0;
        for (int i = 0; i < adiff; i++) {
            if (diff < 0) {
                if (lostdigit != 0) {
                    inexact = true;
                }
                lostdigit = this.mant[0];
                shiftRight();
            } else {
                shiftLeft();
            }
        }
        if (inexact) {
            this.field.setIEEEFlagsBits(16);
            dotrap(16, ALIGN_TRAP, this, this);
        }
        return lostdigit;
    }

    public boolean lessThan(Dfp x) {
        boolean z = false;
        if (this.field.getRadixDigits() != x.field.getRadixDigits()) {
            this.field.setIEEEFlagsBits(1);
            Dfp result = newInstance(getZero());
            result.nans = 3;
            dotrap(1, LESS_THAN_TRAP, x, result);
            return false;
        } else if (isNaN() || x.isNaN()) {
            this.field.setIEEEFlagsBits(1);
            dotrap(1, LESS_THAN_TRAP, x, newInstance(getZero()));
            return false;
        } else {
            if (compare(this, x) < 0) {
                z = true;
            }
            return z;
        }
    }

    public boolean greaterThan(Dfp x) {
        boolean z = false;
        if (this.field.getRadixDigits() != x.field.getRadixDigits()) {
            this.field.setIEEEFlagsBits(1);
            Dfp result = newInstance(getZero());
            result.nans = 3;
            dotrap(1, GREATER_THAN_TRAP, x, result);
            return false;
        } else if (isNaN() || x.isNaN()) {
            this.field.setIEEEFlagsBits(1);
            dotrap(1, GREATER_THAN_TRAP, x, newInstance(getZero()));
            return false;
        } else {
            if (compare(this, x) > 0) {
                z = true;
            }
            return z;
        }
    }

    public boolean negativeOrNull() {
        boolean z = false;
        if (isNaN()) {
            this.field.setIEEEFlagsBits(1);
            dotrap(1, LESS_THAN_TRAP, this, newInstance(getZero()));
            return false;
        }
        if (this.sign < 0 || (this.mant[this.mant.length - 1] == 0 && !isInfinite())) {
            z = true;
        }
        return z;
    }

    public boolean strictlyNegative() {
        boolean z = false;
        if (isNaN()) {
            this.field.setIEEEFlagsBits(1);
            dotrap(1, LESS_THAN_TRAP, this, newInstance(getZero()));
            return false;
        }
        if (this.sign < 0 && (this.mant[this.mant.length - 1] != 0 || isInfinite())) {
            z = true;
        }
        return z;
    }

    public boolean positiveOrNull() {
        boolean z = false;
        if (isNaN()) {
            this.field.setIEEEFlagsBits(1);
            dotrap(1, LESS_THAN_TRAP, this, newInstance(getZero()));
            return false;
        }
        if (this.sign > 0 || (this.mant[this.mant.length - 1] == 0 && !isInfinite())) {
            z = true;
        }
        return z;
    }

    public boolean strictlyPositive() {
        boolean z = false;
        if (isNaN()) {
            this.field.setIEEEFlagsBits(1);
            dotrap(1, LESS_THAN_TRAP, this, newInstance(getZero()));
            return false;
        }
        if (this.sign > 0 && (this.mant[this.mant.length - 1] != 0 || isInfinite())) {
            z = true;
        }
        return z;
    }

    public Dfp abs() {
        Dfp result = newInstance(this);
        result.sign = 1;
        return result;
    }

    public boolean isInfinite() {
        return this.nans == 1;
    }

    public boolean isNaN() {
        return this.nans == 3 || this.nans == 2;
    }

    public boolean isZero() {
        boolean z = false;
        if (isNaN()) {
            this.field.setIEEEFlagsBits(1);
            dotrap(1, LESS_THAN_TRAP, this, newInstance(getZero()));
            return false;
        }
        if (this.mant[this.mant.length - 1] == 0 && !isInfinite()) {
            z = true;
        }
        return z;
    }

    public boolean equals(Object other) {
        boolean z = false;
        if (!(other instanceof Dfp)) {
            return false;
        }
        Dfp x = (Dfp) other;
        if (isNaN() || x.isNaN() || this.field.getRadixDigits() != x.field.getRadixDigits()) {
            return false;
        }
        if (compare(this, x) == 0) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return (isZero() ? 0 : this.sign << 8) + 17 + (this.nans << UnionPtg.sid) + this.exp + Arrays.hashCode(this.mant);
    }

    public boolean unequal(Dfp x) {
        boolean z = false;
        if (isNaN() || x.isNaN() || this.field.getRadixDigits() != x.field.getRadixDigits()) {
            return false;
        }
        if (greaterThan(x) || lessThan(x)) {
            z = true;
        }
        return z;
    }

    private static int compare(Dfp a, Dfp b) {
        if (a.mant[a.mant.length - 1] == 0 && b.mant[b.mant.length - 1] == 0 && a.nans == 0 && b.nans == 0) {
            return 0;
        }
        if (a.sign != b.sign) {
            if (a.sign == -1) {
                return -1;
            }
            return 1;
        } else if (a.nans == 1 && b.nans == 0) {
            return a.sign;
        } else {
            if (a.nans == 0 && b.nans == 1) {
                return -b.sign;
            }
            if (a.nans == 1 && b.nans == 1) {
                return 0;
            }
            if (!(b.mant[b.mant.length - 1] == 0 || a.mant[b.mant.length - 1] == 0)) {
                if (a.exp < b.exp) {
                    return -a.sign;
                }
                if (a.exp > b.exp) {
                    return a.sign;
                }
            }
            for (int i = a.mant.length - 1; i >= 0; i--) {
                if (a.mant[i] > b.mant[i]) {
                    return a.sign;
                }
                if (a.mant[i] < b.mant[i]) {
                    return -a.sign;
                }
            }
            return 0;
        }
    }

    public Dfp rint() {
        return trunc(RoundingMode.ROUND_HALF_EVEN);
    }

    public Dfp floor() {
        return trunc(RoundingMode.ROUND_FLOOR);
    }

    public Dfp ceil() {
        return trunc(RoundingMode.ROUND_CEIL);
    }

    public Dfp remainder(Dfp d) {
        Dfp result = subtract(divide(d).rint().multiply(d));
        if (result.mant[this.mant.length - 1] == 0) {
            result.sign = this.sign;
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public Dfp trunc(RoundingMode rmode) {
        if (isNaN()) {
            return newInstance(this);
        }
        if (this.nans == 1) {
            return newInstance(this);
        }
        if (this.mant[this.mant.length - 1] == 0) {
            return newInstance(this);
        }
        if (this.exp < 0) {
            this.field.setIEEEFlagsBits(16);
            return dotrap(16, TRUNC_TRAP, this, newInstance(getZero()));
        } else if (this.exp >= this.mant.length) {
            return newInstance(this);
        } else {
            Dfp result = newInstance(this);
            boolean changed = false;
            for (int i = 0; i < this.mant.length - result.exp; i++) {
                changed |= result.mant[i] != 0;
                result.mant[i] = 0;
            }
            if (!changed) {
                return result;
            }
            switch (rmode) {
                case ROUND_FLOOR:
                    if (result.sign == -1) {
                        result = result.add(newInstance(-1));
                        break;
                    }
                    break;
                case ROUND_CEIL:
                    if (result.sign == 1) {
                        result = result.add(getOne());
                        break;
                    }
                    break;
                default:
                    Dfp half = newInstance("0.5");
                    Dfp a = subtract(result);
                    a.sign = 1;
                    if (a.greaterThan(half)) {
                        a = newInstance(getOne());
                        a.sign = this.sign;
                        result = result.add(a);
                    }
                    if (a.equals(half) && result.exp > 0 && (1 & result.mant[this.mant.length - result.exp]) != 0) {
                        Dfp a2 = newInstance(getOne());
                        a2.sign = this.sign;
                        result = result.add(a2);
                        break;
                    }
            }
            this.field.setIEEEFlagsBits(16);
            return dotrap(16, TRUNC_TRAP, this, result);
        }
    }

    public int intValue() {
        int result = 0;
        Dfp rounded = rint();
        if (rounded.greaterThan(newInstance(Integer.MAX_VALUE))) {
            return Integer.MAX_VALUE;
        }
        if (rounded.lessThan(newInstance(Integer.MIN_VALUE))) {
            return Integer.MIN_VALUE;
        }
        int i = this.mant.length;
        while (true) {
            i--;
            if (i < this.mant.length - rounded.exp) {
                break;
            }
            result = (result * 10000) + rounded.mant[i];
        }
        if (rounded.sign == -1) {
            result = -result;
        }
        return result;
    }

    public int log10K() {
        return this.exp - 1;
    }

    public Dfp power10K(int e) {
        Dfp d = newInstance(getOne());
        d.exp = e + 1;
        return d;
    }

    public int intLog10() {
        if (this.mant[this.mant.length - 1] > 1000) {
            return (this.exp * 4) - 1;
        }
        if (this.mant[this.mant.length - 1] > 100) {
            return (this.exp * 4) - 2;
        }
        if (this.mant[this.mant.length - 1] > 10) {
            return (this.exp * 4) - 3;
        }
        return (this.exp * 4) - 4;
    }

    public Dfp power10(int e) {
        Dfp d = newInstance(getOne());
        if (e >= 0) {
            d.exp = (e / 4) + 1;
        } else {
            d.exp = (e + 1) / 4;
        }
        switch (((e % 4) + 4) % 4) {
            case 0:
                return d;
            case 1:
                return d.multiply(10);
            case 2:
                return d.multiply(100);
            default:
                return d.multiply(1000);
        }
    }

    /* access modifiers changed from: protected */
    public int complement(int extra) {
        int extra2 = 10000 - extra;
        for (int i = 0; i < this.mant.length; i++) {
            this.mant[i] = (10000 - this.mant[i]) - 1;
        }
        int rh = extra2 / 10000;
        int extra3 = extra2 - (rh * 10000);
        for (int i2 = 0; i2 < this.mant.length; i2++) {
            int r = this.mant[i2] + rh;
            rh = r / 10000;
            this.mant[i2] = r - (rh * 10000);
        }
        return extra3;
    }

    public Dfp add(Dfp x) {
        Dfp dfp = x;
        if (this.field.getRadixDigits() != dfp.field.getRadixDigits()) {
            this.field.setIEEEFlagsBits(1);
            Dfp result = newInstance(getZero());
            result.nans = 3;
            return dotrap(1, ADD_TRAP, dfp, result);
        }
        if (!(this.nans == 0 && dfp.nans == 0)) {
            if (isNaN()) {
                return this;
            }
            if (x.isNaN()) {
                return dfp;
            }
            if (this.nans == 1 && dfp.nans == 0) {
                return this;
            }
            if (dfp.nans == 1 && this.nans == 0) {
                return dfp;
            }
            if (dfp.nans == 1 && this.nans == 1 && this.sign == dfp.sign) {
                return dfp;
            }
            if (dfp.nans == 1 && this.nans == 1 && this.sign != dfp.sign) {
                this.field.setIEEEFlagsBits(1);
                Dfp result2 = newInstance(getZero());
                result2.nans = 3;
                return dotrap(1, ADD_TRAP, dfp, result2);
            }
        }
        Dfp a = newInstance(this);
        Dfp b = newInstance(x);
        Dfp result3 = newInstance(getZero());
        byte asign = a.sign;
        byte bsign = b.sign;
        a.sign = 1;
        b.sign = 1;
        byte rsign = bsign;
        if (compare(a, b) > 0) {
            rsign = asign;
        }
        if (b.mant[this.mant.length - 1] == 0) {
            b.exp = a.exp;
        }
        if (a.mant[this.mant.length - 1] == 0) {
            a.exp = b.exp;
        }
        int aextradigit = 0;
        int bextradigit = 0;
        if (a.exp < b.exp) {
            aextradigit = a.align(b.exp);
        } else {
            bextradigit = b.align(a.exp);
        }
        if (asign != bsign) {
            if (asign == rsign) {
                bextradigit = b.complement(bextradigit);
            } else {
                aextradigit = a.complement(aextradigit);
            }
        }
        int rh = 0;
        for (int i = 0; i < this.mant.length; i++) {
            int r = a.mant[i] + b.mant[i] + rh;
            rh = r / 10000;
            result3.mant[i] = r - (rh * 10000);
        }
        result3.exp = a.exp;
        result3.sign = rsign;
        if (rh != 0 && asign == bsign) {
            int lostdigit = result3.mant[0];
            result3.shiftRight();
            result3.mant[this.mant.length - 1] = rh;
            int excp = result3.round(lostdigit);
            if (excp != 0) {
                result3 = dotrap(excp, ADD_TRAP, dfp, result3);
            }
        }
        for (int i2 = 0; i2 < this.mant.length && result3.mant[this.mant.length - 1] == 0; i2++) {
            result3.shiftLeft();
            if (i2 == 0) {
                result3.mant[0] = aextradigit + bextradigit;
                aextradigit = 0;
                bextradigit = 0;
            }
        }
        if (result3.mant[this.mant.length - 1] == 0) {
            result3.exp = 0;
            if (asign != bsign) {
                result3.sign = 1;
            }
        }
        int excp2 = result3.round(aextradigit + bextradigit);
        if (excp2 != 0) {
            result3 = dotrap(excp2, ADD_TRAP, dfp, result3);
        }
        return result3;
    }

    public Dfp negate() {
        Dfp result = newInstance(this);
        result.sign = (byte) (-result.sign);
        return result;
    }

    public Dfp subtract(Dfp x) {
        return add(x.negate());
    }

    /* access modifiers changed from: protected */
    public int round(int n) {
        boolean inc;
        boolean z;
        switch (this.field.getRoundingMode()) {
            case ROUND_CEIL:
                inc = this.sign == 1 && n != 0;
                break;
            case ROUND_HALF_EVEN:
                inc = n > 5000 || (n == 5000 && (this.mant[0] & 1) == 1);
                break;
            case ROUND_DOWN:
                inc = false;
                break;
            case ROUND_UP:
                inc = n != 0;
                break;
            case ROUND_HALF_UP:
                inc = n >= 5000;
                break;
            case ROUND_HALF_DOWN:
                inc = n > 5000;
                break;
            case ROUND_HALF_ODD:
                inc = n > 5000 || (n == 5000 && (this.mant[0] & 1) == 0);
                break;
            default:
                if (this.sign != -1 || n == 0) {
                    z = false;
                } else {
                    z = true;
                }
                inc = z;
                break;
        }
        if (inc) {
            int rh = 1;
            for (int i = 0; i < this.mant.length; i++) {
                int r = this.mant[i] + rh;
                rh = r / 10000;
                this.mant[i] = r - (rh * 10000);
            }
            if (rh != 0) {
                shiftRight();
                this.mant[this.mant.length - 1] = rh;
            }
        }
        if (this.exp < -32767) {
            this.field.setIEEEFlagsBits(8);
            return 8;
        } else if (this.exp > 32768) {
            this.field.setIEEEFlagsBits(4);
            return 4;
        } else if (n == 0) {
            return 0;
        } else {
            this.field.setIEEEFlagsBits(16);
            return 16;
        }
    }

    public Dfp multiply(Dfp x) {
        int excp;
        if (this.field.getRadixDigits() != x.field.getRadixDigits()) {
            this.field.setIEEEFlagsBits(1);
            Dfp result = newInstance(getZero());
            result.nans = 3;
            return dotrap(1, MULTIPLY_TRAP, x, result);
        }
        Dfp result2 = newInstance(getZero());
        if (!(this.nans == 0 && x.nans == 0)) {
            if (isNaN()) {
                return this;
            }
            if (x.isNaN()) {
                return x;
            }
            if (this.nans == 1 && x.nans == 0 && x.mant[this.mant.length - 1] != 0) {
                Dfp result3 = newInstance(this);
                result3.sign = (byte) (this.sign * x.sign);
                return result3;
            } else if (x.nans == 1 && this.nans == 0 && this.mant[this.mant.length - 1] != 0) {
                Dfp result4 = newInstance(x);
                result4.sign = (byte) (this.sign * x.sign);
                return result4;
            } else if (x.nans == 1 && this.nans == 1) {
                Dfp result5 = newInstance(this);
                result5.sign = (byte) (this.sign * x.sign);
                return result5;
            } else if ((x.nans == 1 && this.nans == 0 && this.mant[this.mant.length - 1] == 0) || (this.nans == 1 && x.nans == 0 && x.mant[this.mant.length - 1] == 0)) {
                this.field.setIEEEFlagsBits(1);
                Dfp result6 = newInstance(getZero());
                result6.nans = 3;
                return dotrap(1, MULTIPLY_TRAP, x, result6);
            }
        }
        int[] product = new int[(this.mant.length * 2)];
        for (int i = 0; i < this.mant.length; i++) {
            int rh = 0;
            for (int j = 0; j < this.mant.length; j++) {
                int r = (this.mant[i] * x.mant[j]) + product[i + j] + rh;
                rh = r / 10000;
                product[i + j] = r - (rh * 10000);
            }
            product[this.mant.length + i] = rh;
        }
        int md = (this.mant.length * 2) - 1;
        int i2 = (this.mant.length * 2) - 1;
        while (true) {
            if (i2 < 0) {
                break;
            } else if (product[i2] != 0) {
                md = i2;
                break;
            } else {
                i2--;
            }
        }
        for (int i3 = 0; i3 < this.mant.length; i3++) {
            result2.mant[(this.mant.length - i3) - 1] = product[md - i3];
        }
        result2.exp = (((this.exp + x.exp) + md) - (this.mant.length * 2)) + 1;
        result2.sign = (byte) (this.sign == x.sign ? 1 : -1);
        if (result2.mant[this.mant.length - 1] == 0) {
            result2.exp = 0;
        }
        if (md > this.mant.length - 1) {
            excp = result2.round(product[md - this.mant.length]);
        } else {
            excp = result2.round(0);
        }
        if (excp != 0) {
            result2 = dotrap(excp, MULTIPLY_TRAP, x, result2);
        }
        return result2;
    }

    public Dfp multiply(int x) {
        if (x < 0 || x >= 10000) {
            return multiply(newInstance(x));
        }
        return multiplyFast(x);
    }

    private Dfp multiplyFast(int x) {
        Dfp result = newInstance(this);
        if (this.nans != 0) {
            if (isNaN()) {
                return this;
            }
            if (this.nans == 1 && x != 0) {
                return newInstance(this);
            }
            if (this.nans == 1 && x == 0) {
                this.field.setIEEEFlagsBits(1);
                Dfp result2 = newInstance(getZero());
                result2.nans = 3;
                return dotrap(1, MULTIPLY_TRAP, newInstance(getZero()), result2);
            }
        }
        if (x < 0 || x >= 10000) {
            this.field.setIEEEFlagsBits(1);
            Dfp result3 = newInstance(getZero());
            result3.nans = 3;
            return dotrap(1, MULTIPLY_TRAP, result3, result3);
        }
        int rh = 0;
        for (int i = 0; i < this.mant.length; i++) {
            int r = (this.mant[i] * x) + rh;
            rh = r / 10000;
            result.mant[i] = r - (rh * 10000);
        }
        int lostdigit = 0;
        if (rh != 0) {
            lostdigit = result.mant[0];
            result.shiftRight();
            result.mant[this.mant.length - 1] = rh;
        }
        if (result.mant[this.mant.length - 1] == 0) {
            result.exp = 0;
        }
        int excp = result.round(lostdigit);
        if (excp != 0) {
            result = dotrap(excp, MULTIPLY_TRAP, result, result);
        }
        return result;
    }

    public Dfp divide(Dfp divisor) {
        int excp;
        int trial;
        boolean trialgood;
        Dfp dfp = divisor;
        int trial2 = 0;
        boolean z = false;
        int i = 1;
        if (this.field.getRadixDigits() != dfp.field.getRadixDigits()) {
            this.field.setIEEEFlagsBits(1);
            Dfp result = newInstance(getZero());
            result.nans = 3;
            return dotrap(1, DIVIDE_TRAP, dfp, result);
        }
        Dfp result2 = newInstance(getZero());
        if (!(this.nans == 0 && dfp.nans == 0)) {
            if (isNaN()) {
                return this;
            }
            if (divisor.isNaN()) {
                return dfp;
            }
            if (this.nans == 1 && dfp.nans == 0) {
                Dfp result3 = newInstance(this);
                result3.sign = (byte) (this.sign * dfp.sign);
                return result3;
            } else if (dfp.nans == 1 && this.nans == 0) {
                Dfp result4 = newInstance(getZero());
                result4.sign = (byte) (this.sign * dfp.sign);
                return result4;
            } else if (dfp.nans == 1 && this.nans == 1) {
                this.field.setIEEEFlagsBits(1);
                Dfp result5 = newInstance(getZero());
                result5.nans = 3;
                return dotrap(1, DIVIDE_TRAP, dfp, result5);
            }
        }
        if (dfp.mant[this.mant.length - 1] == 0) {
            this.field.setIEEEFlagsBits(2);
            Dfp result6 = newInstance(getZero());
            result6.sign = (byte) (this.sign * dfp.sign);
            result6.nans = 1;
            return dotrap(2, DIVIDE_TRAP, dfp, result6);
        }
        int[] dividend = new int[(this.mant.length + 1)];
        int[] quotient = new int[(this.mant.length + 2)];
        int[] remainder = new int[(this.mant.length + 1)];
        dividend[this.mant.length] = 0;
        quotient[this.mant.length] = 0;
        quotient[this.mant.length + 1] = 0;
        remainder[this.mant.length] = 0;
        for (int i2 = 0; i2 < this.mant.length; i2++) {
            dividend[i2] = this.mant[i2];
            quotient[i2] = 0;
            remainder[i2] = 0;
        }
        int nsqd = 0;
        int qd = this.mant.length + 1;
        while (true) {
            if (qd < 0) {
                int md = z;
                break;
            }
            int divMsb = (dividend[this.mant.length] * 10000) + dividend[this.mant.length - i];
            int min = divMsb / (dfp.mant[this.mant.length - i] + i);
            int max = (divMsb + 1) / dfp.mant[this.mant.length - i];
            trial = trial2;
            boolean trialgood2 = false;
            while (!trialgood2) {
                trial = (min + max) / 2;
                int rh = 0;
                int i3 = 0;
                while (true) {
                    trialgood = trialgood2;
                    if (i3 >= this.mant.length + i) {
                        break;
                    }
                    int dm = i3 < this.mant.length ? dfp.mant[i3] : 0;
                    int r = (dm * trial) + rh;
                    int i4 = dm;
                    int rh2 = r / 10000;
                    boolean z2 = z;
                    remainder[i3] = r - (rh2 * 10000);
                    i3++;
                    rh = rh2;
                    trialgood2 = trialgood;
                    z = z2;
                    i = 1;
                }
                boolean z3 = z;
                int rh3 = 1;
                for (int i5 = 0; i5 < this.mant.length + 1; i5++) {
                    int r2 = (9999 - remainder[i5]) + dividend[i5] + rh3;
                    rh3 = r2 / 10000;
                    remainder[i5] = r2 - (rh3 * 10000);
                }
                if (rh3 == 0) {
                    max = trial - 1;
                } else {
                    int i6 = rh3;
                    int minadj = ((remainder[this.mant.length] * 10000) + remainder[this.mant.length - 1]) / (dfp.mant[this.mant.length - 1] + 1);
                    if (minadj >= 2) {
                        min = trial + minadj;
                    } else {
                        boolean trialgood3 = false;
                        int i7 = this.mant.length - 1;
                        while (true) {
                            if (i7 < 0) {
                                break;
                            }
                            int minadj2 = minadj;
                            if (dfp.mant[i7] > remainder[i7]) {
                                trialgood3 = true;
                            }
                            if (dfp.mant[i7] < remainder[i7]) {
                                break;
                            }
                            i7--;
                            minadj = minadj2;
                        }
                        if (remainder[this.mant.length] != 0) {
                            trialgood2 = false;
                        } else {
                            trialgood2 = trialgood3;
                        }
                        if (!trialgood2) {
                            min = trial + 1;
                        }
                        z = z3;
                        i = 1;
                    }
                }
                trialgood2 = trialgood;
                z = z3;
                i = 1;
            }
            boolean z4 = trialgood2;
            boolean z5 = z;
            quotient[qd] = trial;
            if (!(trial == 0 && nsqd == 0)) {
                nsqd++;
            }
            if (!(this.field.getRoundingMode() == RoundingMode.ROUND_DOWN && nsqd == this.mant.length) && nsqd <= this.mant.length) {
                dividend[0] = 0;
                for (int i8 = 0; i8 < this.mant.length; i8++) {
                    dividend[i8 + 1] = remainder[i8];
                }
                qd--;
                trial2 = trial;
                z = z5;
                i = 1;
            }
        }
        int i9 = trial;
        int md2 = this.mant.length;
        int i10 = this.mant.length + 1;
        while (true) {
            if (i10 < 0) {
                break;
            } else if (quotient[i10] != 0) {
                md2 = i10;
                break;
            } else {
                i10--;
            }
        }
        for (int i11 = 0; i11 < this.mant.length; i11++) {
            result2.mant[(this.mant.length - i11) - 1] = quotient[md2 - i11];
        }
        result2.exp = ((this.exp - dfp.exp) + md2) - this.mant.length;
        result2.sign = (byte) (this.sign == dfp.sign ? 1 : -1);
        if (result2.mant[this.mant.length - 1] == 0) {
            result2.exp = 0;
        }
        if (md2 > this.mant.length - 1) {
            excp = result2.round(quotient[md2 - this.mant.length]);
        } else {
            excp = result2.round(0);
        }
        if (excp != 0) {
            result2 = dotrap(excp, DIVIDE_TRAP, dfp, result2);
        }
        return result2;
    }

    public Dfp divide(int divisor) {
        if (this.nans != 0) {
            if (isNaN()) {
                return this;
            }
            if (this.nans == 1) {
                return newInstance(this);
            }
        }
        if (divisor == 0) {
            this.field.setIEEEFlagsBits(2);
            Dfp result = newInstance(getZero());
            result.sign = this.sign;
            result.nans = 1;
            return dotrap(2, DIVIDE_TRAP, getZero(), result);
        } else if (divisor < 0 || divisor >= 10000) {
            this.field.setIEEEFlagsBits(1);
            Dfp result2 = newInstance(getZero());
            result2.nans = 3;
            return dotrap(1, DIVIDE_TRAP, result2, result2);
        } else {
            Dfp result3 = newInstance(this);
            int rl = 0;
            for (int i = this.mant.length - 1; i >= 0; i--) {
                int r = (rl * 10000) + result3.mant[i];
                int rh = r / divisor;
                rl = r - (rh * divisor);
                result3.mant[i] = rh;
            }
            if (result3.mant[this.mant.length - 1] == 0) {
                result3.shiftLeft();
                int r2 = rl * 10000;
                int rh2 = r2 / divisor;
                rl = r2 - (rh2 * divisor);
                result3.mant[0] = rh2;
            }
            int excp = result3.round((rl * 10000) / divisor);
            if (excp != 0) {
                result3 = dotrap(excp, DIVIDE_TRAP, result3, result3);
            }
            return result3;
        }
    }

    public Dfp reciprocal() {
        return this.field.getOne().divide(this);
    }

    public Dfp sqrt() {
        if (this.nans == 0 && this.mant[this.mant.length - 1] == 0) {
            return newInstance(this);
        }
        if (this.nans != 0) {
            if (this.nans == 1 && this.sign == 1) {
                return newInstance(this);
            }
            if (this.nans == 3) {
                return newInstance(this);
            }
            if (this.nans == 2) {
                this.field.setIEEEFlagsBits(1);
                return dotrap(1, SQRT_TRAP, null, newInstance(this));
            }
        }
        if (this.sign == -1) {
            this.field.setIEEEFlagsBits(1);
            Dfp result = newInstance(this);
            result.nans = 3;
            return dotrap(1, SQRT_TRAP, null, result);
        }
        Dfp x = newInstance(this);
        if (x.exp < -1 || x.exp > 1) {
            x.exp = this.exp / 2;
        }
        int i = x.mant[this.mant.length - 1] / 2000;
        if (i != 0) {
            switch (i) {
                case 2:
                    x.mant[this.mant.length - 1] = 1500;
                    break;
                case 3:
                    x.mant[this.mant.length - 1] = 2200;
                    break;
                default:
                    x.mant[this.mant.length - 1] = 3000;
                    break;
            }
        } else {
            x.mant[this.mant.length - 1] = (x.mant[this.mant.length - 1] / 2) + 1;
        }
        Dfp newInstance = newInstance(x);
        Dfp px = getZero();
        Dfp zero = getZero();
        while (x.unequal(px)) {
            Dfp dx = newInstance(x);
            dx.sign = -1;
            Dfp dx2 = dx.add(divide(x)).divide(2);
            Dfp ppx = px;
            px = x;
            x = x.add(dx2);
            if (!x.equals(ppx)) {
                if (dx2.mant[this.mant.length - 1] == 0) {
                }
            }
            return x;
        }
        return x;
    }

    public String toString() {
        if (this.nans != 0) {
            if (this.nans != 1) {
                return NAN_STRING;
            }
            return this.sign < 0 ? NEG_INFINITY_STRING : POS_INFINITY_STRING;
        } else if (this.exp > this.mant.length || this.exp < -1) {
            return dfp2sci();
        } else {
            return dfp2string();
        }
    }

    /* access modifiers changed from: protected */
    public String dfp2sci() {
        char[] rawdigits = new char[(this.mant.length * 4)];
        char[] outputbuffer = new char[((this.mant.length * 4) + 20)];
        int p = 0;
        int i = this.mant.length;
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            int p2 = p + 1;
            rawdigits[p] = (char) ((this.mant[i] / 1000) + 48);
            int p3 = p2 + 1;
            rawdigits[p2] = (char) (((this.mant[i] / 100) % 10) + 48);
            int p4 = p3 + 1;
            rawdigits[p3] = (char) (((this.mant[i] / 10) % 10) + 48);
            p = p4 + 1;
            rawdigits[p4] = (char) ((this.mant[i] % 10) + 48);
        }
        int p5 = 0;
        while (p5 < rawdigits.length && rawdigits[p5] == '0') {
            p5++;
        }
        int shf = p5;
        int q = 0;
        if (this.sign == -1) {
            int q2 = 0 + 1;
            outputbuffer[0] = '-';
            q = q2;
        }
        if (p5 != rawdigits.length) {
            int q3 = q + 1;
            int p6 = p5 + 1;
            outputbuffer[q] = rawdigits[p5];
            int q4 = q3 + 1;
            outputbuffer[q3] = '.';
            while (p6 < rawdigits.length) {
                int q5 = q4 + 1;
                int p7 = p6 + 1;
                outputbuffer[q4] = rawdigits[p6];
                q4 = q5;
                p6 = p7;
            }
            int q6 = q4 + 1;
            outputbuffer[q4] = 'e';
            int e = ((this.exp * 4) - shf) - 1;
            int ae = e;
            if (e < 0) {
                ae = -e;
            }
            int p8 = 1000000000;
            while (p8 > ae) {
                p8 /= 10;
            }
            if (e < 0) {
                int q7 = q6 + 1;
                outputbuffer[q6] = '-';
                q6 = q7;
            }
            while (p8 > 0) {
                int q8 = q6 + 1;
                outputbuffer[q6] = (char) ((ae / p8) + 48);
                ae %= p8;
                p8 /= 10;
                q6 = q8;
            }
            return new String(outputbuffer, 0, q6);
        }
        int ae2 = q + 1;
        outputbuffer[q] = '0';
        int q9 = ae2 + 1;
        outputbuffer[ae2] = '.';
        int q10 = q9 + 1;
        outputbuffer[q9] = '0';
        int q11 = q10 + 1;
        outputbuffer[q10] = 'e';
        int q12 = q11 + 1;
        outputbuffer[q11] = '0';
        return new String(outputbuffer, 0, 5);
    }

    /* access modifiers changed from: protected */
    public String dfp2string() {
        int p;
        int q;
        char[] buffer = new char[((this.mant.length * 4) + 20)];
        int p2 = 1;
        int e = this.exp;
        boolean pointInserted = false;
        buffer[0] = ' ';
        if (e <= 0) {
            int p3 = 1 + 1;
            buffer[1] = '0';
            p2 = p3 + 1;
            buffer[p3] = '.';
            pointInserted = true;
        }
        while (e < 0) {
            int p4 = p + 1;
            buffer[p] = '0';
            int p5 = p4 + 1;
            buffer[p4] = '0';
            int p6 = p5 + 1;
            buffer[p5] = '0';
            p2 = p6 + 1;
            buffer[p6] = '0';
            e++;
        }
        int q2 = 1;
        for (int i = this.mant.length - 1; i >= 0; i--) {
            int p7 = p + 1;
            buffer[p] = (char) ((this.mant[i] / 1000) + 48);
            int p8 = p7 + 1;
            buffer[p7] = (char) (((this.mant[i] / 100) % 10) + 48);
            int p9 = p8 + 1;
            buffer[p8] = (char) (((this.mant[i] / 10) % 10) + 48);
            p = p9 + 1;
            buffer[p9] = (char) ((this.mant[i] % 10) + 48);
            e--;
            if (e == 0) {
                int p10 = p + 1;
                buffer[p] = '.';
                pointInserted = true;
                p = p10;
            }
        }
        while (e > 0) {
            int p11 = p + 1;
            buffer[p] = '0';
            int p12 = p11 + 1;
            buffer[p11] = '0';
            int p13 = p12 + 1;
            buffer[p12] = '0';
            p = p13 + 1;
            buffer[p13] = '0';
            e--;
        }
        if (!pointInserted) {
            int p14 = p + 1;
            buffer[p] = '.';
            p = p14;
        }
        while (true) {
            q = q2;
            if (buffer[q] != '0') {
                break;
            }
            q2 = q + 1;
        }
        if (buffer[q] == '.') {
            q--;
        }
        while (buffer[p - 1] == '0') {
            p--;
        }
        if (this.sign < 0) {
            q--;
            buffer[q] = '-';
        }
        return new String(buffer, q, p - q);
    }

    public Dfp dotrap(int type, String what, Dfp oper, Dfp result) {
        Dfp def;
        Dfp def2 = result;
        if (type == 4) {
            result.exp -= 32760;
            def = newInstance(getZero());
            def.sign = result.sign;
            def.nans = 1;
        } else if (type != 8) {
            switch (type) {
                case 1:
                    def = newInstance(getZero());
                    def.sign = result.sign;
                    def.nans = 3;
                    break;
                case 2:
                    if (this.nans == 0 && this.mant[this.mant.length - 1] != 0) {
                        def2 = newInstance(getZero());
                        def2.sign = (byte) (this.sign * oper.sign);
                        def2.nans = 1;
                    }
                    if (this.nans == 0 && this.mant[this.mant.length - 1] == 0) {
                        def2 = newInstance(getZero());
                        def2.nans = 3;
                    }
                    if (this.nans == 1 || this.nans == 3) {
                        def2 = newInstance(getZero());
                        def2.nans = 3;
                    }
                    if (this.nans == 1 || this.nans == 2) {
                        def = newInstance(getZero());
                        def.nans = 3;
                        break;
                    }
                default:
                    def = result;
                    break;
            }
        } else {
            if (result.exp + this.mant.length < -32767) {
                def = newInstance(getZero());
                def.sign = result.sign;
            } else {
                def = newInstance(result);
            }
            result.exp += ERR_SCALE;
        }
        return trap(type, what, oper, def, result);
    }

    /* access modifiers changed from: protected */
    public Dfp trap(int type, String what, Dfp oper, Dfp def, Dfp result) {
        return def;
    }

    public int classify() {
        return this.nans;
    }

    public static Dfp copysign(Dfp x, Dfp y) {
        Dfp result = x.newInstance(x);
        result.sign = y.sign;
        return result;
    }

    public Dfp nextAfter(Dfp x) {
        Dfp result;
        if (this.field.getRadixDigits() != x.field.getRadixDigits()) {
            this.field.setIEEEFlagsBits(1);
            Dfp result2 = newInstance(getZero());
            result2.nans = 3;
            return dotrap(1, NEXT_AFTER_TRAP, x, result2);
        }
        boolean up = false;
        if (lessThan(x)) {
            up = true;
        }
        if (compare(this, x) == 0) {
            return newInstance(x);
        }
        if (lessThan(getZero())) {
            up = !up;
        }
        if (up) {
            Dfp inc = newInstance(getOne());
            inc.exp = (this.exp - this.mant.length) + 1;
            inc.sign = this.sign;
            if (equals(getZero())) {
                inc.exp = -32767 - this.mant.length;
            }
            result = add(inc);
        } else {
            Dfp inc2 = newInstance(getOne());
            inc2.exp = this.exp;
            inc2.sign = this.sign;
            if (equals(inc2)) {
                inc2.exp = this.exp - this.mant.length;
            } else {
                inc2.exp = (this.exp - this.mant.length) + 1;
            }
            if (equals(getZero())) {
                inc2.exp = -32767 - this.mant.length;
            }
            result = subtract(inc2);
        }
        if (result.classify() == 1 && classify() != 1) {
            this.field.setIEEEFlagsBits(16);
            result = dotrap(16, NEXT_AFTER_TRAP, x, result);
        }
        if (result.equals(getZero()) && !equals(getZero())) {
            this.field.setIEEEFlagsBits(16);
            result = dotrap(16, NEXT_AFTER_TRAP, x, result);
        }
        return result;
    }

    public double toDouble() {
        double d = Double.POSITIVE_INFINITY;
        if (isInfinite()) {
            return lessThan(getZero()) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        }
        if (isNaN()) {
            return Double.NaN;
        }
        Dfp y = this;
        boolean negate = false;
        int cmp0 = compare(this, getZero());
        double d2 = 0.0d;
        if (cmp0 == 0) {
            if (this.sign < 0) {
                d2 = -0.0d;
            }
            return d2;
        }
        if (cmp0 < 0) {
            y = negate();
            negate = true;
        }
        int exponent = (int) (((double) y.intLog10()) * 3.32d);
        if (exponent < 0) {
            exponent--;
        }
        Dfp tempDfp = DfpMath.pow(getTwo(), exponent);
        while (true) {
            if (!tempDfp.lessThan(y) && !tempDfp.equals(y)) {
                break;
            }
            tempDfp = tempDfp.multiply(2);
            exponent++;
        }
        int exponent2 = exponent - 1;
        Dfp y2 = y.divide(DfpMath.pow(getTwo(), exponent2));
        if (exponent2 > -1023) {
            y2 = y2.subtract(getOne());
        }
        if (exponent2 < -1074) {
            return 0.0d;
        }
        if (exponent2 > 1023) {
            if (negate) {
                d = Double.NEGATIVE_INFINITY;
            }
            return d;
        }
        String str = y2.multiply(newInstance((long) IEEEDouble.FRAC_ASSUMED_HIGH_BIT)).rint().toString();
        long mantissa = Long.parseLong(str.substring(0, str.length() - 1));
        if (mantissa == IEEEDouble.FRAC_ASSUMED_HIGH_BIT) {
            mantissa = 0;
            exponent2++;
        }
        if (exponent2 <= -1023) {
            exponent2--;
        }
        while (exponent2 < -1023) {
            exponent2++;
            mantissa >>>= 1;
        }
        double x = Double.longBitsToDouble(((((long) exponent2) + 1023) << 52) | mantissa);
        if (negate) {
            x = -x;
        }
        return x;
    }

    public double[] toSplitDouble() {
        double[] split = new double[2];
        split[0] = Double.longBitsToDouble(Double.doubleToLongBits(toDouble()) & -1073741824);
        split[1] = subtract(newInstance(split[0])).toDouble();
        return split;
    }

    public double getReal() {
        return toDouble();
    }

    public Dfp add(double a) {
        return add(newInstance(a));
    }

    public Dfp subtract(double a) {
        return subtract(newInstance(a));
    }

    public Dfp multiply(double a) {
        return multiply(newInstance(a));
    }

    public Dfp divide(double a) {
        return divide(newInstance(a));
    }

    public Dfp remainder(double a) {
        return remainder(newInstance(a));
    }

    public long round() {
        return FastMath.round(toDouble());
    }

    public Dfp signum() {
        if (isNaN() || isZero()) {
            return this;
        }
        return newInstance(this.sign > 0 ? 1 : -1);
    }

    public Dfp copySign(Dfp s) {
        if ((this.sign < 0 || s.sign < 0) && (this.sign >= 0 || s.sign >= 0)) {
            return negate();
        }
        return this;
    }

    public Dfp copySign(double s) {
        long sb = Double.doubleToLongBits(s);
        if ((this.sign < 0 || sb < 0) && (this.sign >= 0 || sb >= 0)) {
            return negate();
        }
        return this;
    }

    public Dfp scalb(int n) {
        return multiply(DfpMath.pow(getTwo(), n));
    }

    public Dfp hypot(Dfp y) {
        return multiply(this).add(y.multiply(y)).sqrt();
    }

    public Dfp cbrt() {
        return rootN(3);
    }

    public Dfp rootN(int n) {
        return this.sign >= 0 ? DfpMath.pow(this, getOne().divide(n)) : DfpMath.pow(negate(), getOne().divide(n)).negate();
    }

    public Dfp pow(double p) {
        return DfpMath.pow(this, newInstance(p));
    }

    public Dfp pow(int n) {
        return DfpMath.pow(this, n);
    }

    public Dfp pow(Dfp e) {
        return DfpMath.pow(this, e);
    }

    public Dfp exp() {
        return DfpMath.exp(this);
    }

    public Dfp expm1() {
        return DfpMath.exp(this).subtract(getOne());
    }

    public Dfp log() {
        return DfpMath.log(this);
    }

    public Dfp log1p() {
        return DfpMath.log(add(getOne()));
    }

    @Deprecated
    public int log10() {
        return intLog10();
    }

    public Dfp cos() {
        return DfpMath.cos(this);
    }

    public Dfp sin() {
        return DfpMath.sin(this);
    }

    public Dfp tan() {
        return DfpMath.tan(this);
    }

    public Dfp acos() {
        return DfpMath.acos(this);
    }

    public Dfp asin() {
        return DfpMath.asin(this);
    }

    public Dfp atan() {
        return DfpMath.atan(this);
    }

    public Dfp atan2(Dfp x) throws DimensionMismatchException {
        Dfp r = x.multiply(x).add(multiply(this)).sqrt();
        if (x.sign >= 0) {
            return getTwo().multiply(divide(r.add(x)).atan());
        }
        Dfp tmp = getTwo().multiply(divide(r.subtract(x)).atan());
        return newInstance(tmp.sign <= 0 ? -3.141592653589793d : 3.141592653589793d).subtract(tmp);
    }

    public Dfp cosh() {
        return DfpMath.exp(this).add(DfpMath.exp(negate())).divide(2);
    }

    public Dfp sinh() {
        return DfpMath.exp(this).subtract(DfpMath.exp(negate())).divide(2);
    }

    public Dfp tanh() {
        Dfp ePlus = DfpMath.exp(this);
        Dfp eMinus = DfpMath.exp(negate());
        return ePlus.subtract(eMinus).divide(ePlus.add(eMinus));
    }

    public Dfp acosh() {
        return multiply(this).subtract(getOne()).sqrt().add(this).log();
    }

    public Dfp asinh() {
        return multiply(this).add(getOne()).sqrt().add(this).log();
    }

    public Dfp atanh() {
        return getOne().add(this).divide(getOne().subtract(this)).log().divide(2);
    }

    public Dfp linearCombination(Dfp[] a, Dfp[] b) throws DimensionMismatchException {
        if (a.length != b.length) {
            throw new DimensionMismatchException(a.length, b.length);
        }
        Dfp r = getZero();
        for (int i = 0; i < a.length; i++) {
            r = r.add(a[i].multiply(b[i]));
        }
        return r;
    }

    public Dfp linearCombination(double[] a, Dfp[] b) throws DimensionMismatchException {
        if (a.length != b.length) {
            throw new DimensionMismatchException(a.length, b.length);
        }
        Dfp r = getZero();
        for (int i = 0; i < a.length; i++) {
            r = r.add(b[i].multiply(a[i]));
        }
        return r;
    }

    public Dfp linearCombination(Dfp a1, Dfp b1, Dfp a2, Dfp b2) {
        return a1.multiply(b1).add(a2.multiply(b2));
    }

    public Dfp linearCombination(double a1, Dfp b1, double a2, Dfp b2) {
        return b1.multiply(a1).add(b2.multiply(a2));
    }

    public Dfp linearCombination(Dfp a1, Dfp b1, Dfp a2, Dfp b2, Dfp a3, Dfp b3) {
        return a1.multiply(b1).add(a2.multiply(b2)).add(a3.multiply(b3));
    }

    public Dfp linearCombination(double a1, Dfp b1, double a2, Dfp b2, double a3, Dfp b3) {
        return b1.multiply(a1).add(b2.multiply(a2)).add(b3.multiply(a3));
    }

    public Dfp linearCombination(Dfp a1, Dfp b1, Dfp a2, Dfp b2, Dfp a3, Dfp b3, Dfp a4, Dfp b4) {
        return a1.multiply(b1).add(a2.multiply(b2)).add(a3.multiply(b3)).add(a4.multiply(b4));
    }

    public Dfp linearCombination(double a1, Dfp b1, double a2, Dfp b2, double a3, Dfp b3, double a4, Dfp b4) {
        return b1.multiply(a1).add(b2.multiply(a2)).add(b3.multiply(a3)).add(b4.multiply(a4));
    }
}
