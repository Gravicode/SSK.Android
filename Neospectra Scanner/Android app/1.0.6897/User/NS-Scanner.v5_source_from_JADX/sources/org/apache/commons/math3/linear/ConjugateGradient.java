package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.ExceptionContext;
import org.apache.commons.math3.util.IterationManager;

public class ConjugateGradient extends PreconditionedIterativeLinearSolver {
    public static final String OPERATOR = "operator";
    public static final String VECTOR = "vector";
    private boolean check;
    private final double delta;

    public ConjugateGradient(int maxIterations, double delta2, boolean check2) {
        super(maxIterations);
        this.delta = delta2;
        this.check = check2;
    }

    public ConjugateGradient(IterationManager manager, double delta2, boolean check2) throws NullArgumentException {
        super(manager);
        this.delta = delta2;
        this.check = check2;
    }

    public final boolean getCheck() {
        return this.check;
    }

    public RealVector solveInPlace(RealLinearOperator a, RealLinearOperator m, RealVector b, RealVector x0) throws NullArgumentException, NonPositiveDefiniteOperatorException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException {
        RealVector z;
        RealVector z2;
        RealVector p;
        RealLinearOperator realLinearOperator = a;
        RealLinearOperator realLinearOperator2 = m;
        checkParameters(a, m, b, x0);
        IterationManager manager = getIterationManager();
        manager.resetIterationCount();
        double rmax = this.delta * b.getNorm();
        RealVector bro = RealVector.unmodifiableRealVector(b);
        manager.incrementIterationCount();
        RealVector x = x0;
        RealVector xro = RealVector.unmodifiableRealVector(x);
        RealVector p2 = x.copy();
        RealVector q = realLinearOperator.operate(p2);
        RealVector r = b.combine(1.0d, -1.0d, q);
        RealVector rro = RealVector.unmodifiableRealVector(r);
        double rnorm = r.getNorm();
        if (realLinearOperator2 == null) {
            z = r;
        } else {
            z = null;
        }
        RealVector z3 = z;
        RealVector p3 = p2;
        RealVector r2 = r;
        DefaultIterativeLinearSolverEvent defaultIterativeLinearSolverEvent = new DefaultIterativeLinearSolverEvent(this, manager.getIterations(), xro, bro, rro, rnorm);
        manager.fireInitializationEvent(defaultIterativeLinearSolverEvent);
        if (rnorm <= rmax) {
            manager.fireTerminationEvent(defaultIterativeLinearSolverEvent);
            return x;
        }
        double rnorm2 = rnorm;
        RealVector realVector = q;
        DefaultIterativeLinearSolverEvent defaultIterativeLinearSolverEvent2 = defaultIterativeLinearSolverEvent;
        double rhoPrev = 0.0d;
        while (true) {
            double rhoPrev2 = rhoPrev;
            manager.incrementIterationCount();
            DefaultIterativeLinearSolverEvent defaultIterativeLinearSolverEvent3 = new DefaultIterativeLinearSolverEvent(this, manager.getIterations(), xro, bro, rro, rnorm2);
            DefaultIterativeLinearSolverEvent defaultIterativeLinearSolverEvent4 = defaultIterativeLinearSolverEvent3;
            manager.fireIterationStartedEvent(defaultIterativeLinearSolverEvent4);
            if (realLinearOperator2 != null) {
                z2 = realLinearOperator2.operate(r2);
            } else {
                z2 = z3;
            }
            double rhoNext = r2.dotProduct(z2);
            if (!this.check || rhoNext > 0.0d) {
                if (manager.getIterations() == 2) {
                    RealVector p4 = p3;
                    p4.setSubVector(0, z2);
                    p = p4;
                } else {
                    RealVector p5 = p3;
                    p = p5;
                    p5.combineToSelf(rhoNext / rhoPrev2, 1.0d, z2);
                }
                RealVector p6 = p;
                RealLinearOperator realLinearOperator3 = a;
                RealVector q2 = realLinearOperator3.operate(p6);
                double pq = p6.dotProduct(q2);
                if (!this.check || pq > 0.0d) {
                    double alpha = rhoNext / pq;
                    RealVector p7 = p6;
                    RealVector q3 = q2;
                    x.combineToSelf(1.0d, alpha, p7);
                    double alpha2 = alpha;
                    r2.combineToSelf(1.0d, -alpha2, q3);
                    double rhoPrev3 = rhoNext;
                    rnorm2 = r2.getNorm();
                    double d = alpha2;
                    DefaultIterativeLinearSolverEvent defaultIterativeLinearSolverEvent5 = defaultIterativeLinearSolverEvent4;
                    RealVector z4 = z2;
                    DefaultIterativeLinearSolverEvent defaultIterativeLinearSolverEvent6 = new DefaultIterativeLinearSolverEvent(this, manager.getIterations(), xro, bro, rro, rnorm2);
                    manager.fireIterationPerformedEvent(defaultIterativeLinearSolverEvent6);
                    if (rnorm2 <= rmax) {
                        manager.fireTerminationEvent(defaultIterativeLinearSolverEvent6);
                        return x;
                    }
                    p3 = p7;
                    z3 = z4;
                    DefaultIterativeLinearSolverEvent defaultIterativeLinearSolverEvent7 = defaultIterativeLinearSolverEvent6;
                    rhoPrev = rhoPrev3;
                    RealVector realVector2 = q3;
                } else {
                    NonPositiveDefiniteOperatorException e = new NonPositiveDefiniteOperatorException();
                    ExceptionContext context = e.getContext();
                    context.setValue(OPERATOR, realLinearOperator3);
                    context.setValue(VECTOR, p6);
                    throw e;
                }
            } else {
                NonPositiveDefiniteOperatorException e2 = new NonPositiveDefiniteOperatorException();
                ExceptionContext context2 = e2.getContext();
                context2.setValue(OPERATOR, realLinearOperator2);
                context2.setValue(VECTOR, r2);
                throw e2;
            }
        }
    }
}
