package org.apache.commons.math3.exception;

import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class NoBracketingException extends MathIllegalArgumentException {
    private static final long serialVersionUID = -3629324471511904459L;
    private final double fHi;
    private final double fLo;

    /* renamed from: hi */
    private final double f560hi;

    /* renamed from: lo */
    private final double f561lo;

    public NoBracketingException(double lo, double hi, double fLo2, double fHi2) {
        this(LocalizedFormats.SAME_SIGN_AT_ENDPOINTS, lo, hi, fLo2, fHi2, new Object[0]);
    }

    public NoBracketingException(Localizable specific, double lo, double hi, double fLo2, double fHi2, Object... args) {
        super(specific, Double.valueOf(lo), Double.valueOf(hi), Double.valueOf(fLo2), Double.valueOf(fHi2), args);
        this.f561lo = lo;
        this.f560hi = hi;
        this.fLo = fLo2;
        this.fHi = fHi2;
    }

    public double getLo() {
        return this.f561lo;
    }

    public double getHi() {
        return this.f560hi;
    }

    public double getFLo() {
        return this.fLo;
    }

    public double getFHi() {
        return this.fHi;
    }
}
