package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.MathUnsupportedOperationException;

public class DefaultIterativeLinearSolverEvent extends IterativeLinearSolverEvent {
    private static final long serialVersionUID = 20120129;

    /* renamed from: b */
    private final RealVector f607b;

    /* renamed from: r */
    private final RealVector f608r;
    private final double rnorm;

    /* renamed from: x */
    private final RealVector f609x;

    public DefaultIterativeLinearSolverEvent(Object source, int iterations, RealVector x, RealVector b, RealVector r, double rnorm2) {
        super(source, iterations);
        this.f609x = x;
        this.f607b = b;
        this.f608r = r;
        this.rnorm = rnorm2;
    }

    public DefaultIterativeLinearSolverEvent(Object source, int iterations, RealVector x, RealVector b, double rnorm2) {
        super(source, iterations);
        this.f609x = x;
        this.f607b = b;
        this.f608r = null;
        this.rnorm = rnorm2;
    }

    public double getNormOfResidual() {
        return this.rnorm;
    }

    public RealVector getResidual() {
        if (this.f608r != null) {
            return this.f608r;
        }
        throw new MathUnsupportedOperationException();
    }

    public RealVector getRightHandSideVector() {
        return this.f607b;
    }

    public RealVector getSolution() {
        return this.f609x;
    }

    public boolean providesResidual() {
        return this.f608r != null;
    }
}
