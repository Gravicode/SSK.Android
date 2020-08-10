package org.apache.commons.math3.exception;

import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class OutOfRangeException extends MathIllegalNumberException {
    private static final long serialVersionUID = 111601815794403609L;

    /* renamed from: hi */
    private final Number f562hi;

    /* renamed from: lo */
    private final Number f563lo;

    public OutOfRangeException(Number wrong, Number lo, Number hi) {
        this(LocalizedFormats.OUT_OF_RANGE_SIMPLE, wrong, lo, hi);
    }

    public OutOfRangeException(Localizable specific, Number wrong, Number lo, Number hi) {
        super(specific, wrong, lo, hi);
        this.f563lo = lo;
        this.f562hi = hi;
    }

    public Number getLo() {
        return this.f563lo;
    }

    public Number getHi() {
        return this.f562hi;
    }
}
