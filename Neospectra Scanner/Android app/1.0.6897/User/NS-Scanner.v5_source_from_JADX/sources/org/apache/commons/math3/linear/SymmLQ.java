package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.ExceptionContext;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.IterationManager;
import org.apache.commons.math3.util.MathUtils;

public class SymmLQ extends PreconditionedIterativeLinearSolver {
    private static final String OPERATOR = "operator";
    private static final String THRESHOLD = "threshold";
    private static final String VECTOR = "vector";
    private static final String VECTOR1 = "vector1";
    private static final String VECTOR2 = "vector2";
    private final boolean check;
    private final double delta;

    private static class State {
        static final double CBRT_MACH_PREC = FastMath.cbrt(MACH_PREC);
        static final double MACH_PREC = FastMath.ulp(1.0d);

        /* renamed from: a */
        private final RealLinearOperator f625a;

        /* renamed from: b */
        private final RealVector f626b;
        private boolean bIsNull;
        private double beta;
        private double beta1;
        private double bstep;
        private double cgnorm;
        private final boolean check;
        private double dbar;
        private final double delta;
        private double gammaZeta;
        private double gbar;
        private double gmax;
        private double gmin;
        private final boolean goodb;
        private boolean hasConverged;
        private double lqnorm;

        /* renamed from: m */
        private final RealLinearOperator f627m;

        /* renamed from: mb */
        private final RealVector f628mb;
        private double minusEpsZeta;
        private double oldb;

        /* renamed from: r1 */
        private RealVector f629r1;

        /* renamed from: r2 */
        private RealVector f630r2;
        private double rnorm;
        private final double shift;
        private double snprod;
        private double tnorm;
        private RealVector wbar;

        /* renamed from: xL */
        private final RealVector f631xL;

        /* renamed from: y */
        private RealVector f632y;
        private double ynorm2;

        State(RealLinearOperator a, RealLinearOperator m, RealVector b, boolean goodb2, double shift2, double delta2, boolean check2) {
            this.f625a = a;
            this.f627m = m;
            this.f626b = b;
            this.f631xL = new ArrayRealVector(b.getDimension());
            this.goodb = goodb2;
            this.shift = shift2;
            this.f628mb = m == null ? b : m.operate(b);
            this.hasConverged = false;
            this.check = check2;
            this.delta = delta2;
        }

        private static void checkSymmetry(RealLinearOperator l, RealVector x, RealVector y, RealVector z) throws NonSelfAdjointOperatorException {
            double s = y.dotProduct(y);
            double epsa = (MACH_PREC + s) * CBRT_MACH_PREC;
            if (FastMath.abs(s - x.dotProduct(z)) > epsa) {
                NonSelfAdjointOperatorException e = new NonSelfAdjointOperatorException();
                ExceptionContext context = e.getContext();
                context.setValue("operator", l);
                context.setValue(SymmLQ.VECTOR1, x);
                context.setValue(SymmLQ.VECTOR2, y);
                context.setValue(SymmLQ.THRESHOLD, Double.valueOf(epsa));
                throw e;
            }
        }

        private static void throwNPDLOException(RealLinearOperator l, RealVector v) throws NonPositiveDefiniteOperatorException {
            NonPositiveDefiniteOperatorException e = new NonPositiveDefiniteOperatorException();
            ExceptionContext context = e.getContext();
            context.setValue("operator", l);
            context.setValue("vector", v);
            throw e;
        }

        private static void daxpy(double a, RealVector x, RealVector y) {
            int n = x.getDimension();
            for (int i = 0; i < n; i++) {
                y.setEntry(i, (x.getEntry(i) * a) + y.getEntry(i));
            }
        }

        private static void daxpbypz(double a, RealVector x, double b, RealVector y, RealVector z) {
            int n = z.getDimension();
            for (int i = 0; i < n; i++) {
                z.setEntry(i, (x.getEntry(i) * a) + (y.getEntry(i) * b) + z.getEntry(i));
            }
        }

        /* access modifiers changed from: 0000 */
        public void refineSolution(RealVector x) {
            RealVector realVector = x;
            int n = this.f631xL.getDimension();
            int i = 0;
            if (this.lqnorm >= this.cgnorm) {
                double anorm = FastMath.sqrt(this.tnorm);
                double zbar = this.gammaZeta / (this.gbar == 0.0d ? MACH_PREC * anorm : this.gbar);
                double step = (this.bstep + (this.snprod * zbar)) / this.beta1;
                if (!this.goodb) {
                    while (true) {
                        int i2 = i;
                        if (i2 < n) {
                            double anorm2 = anorm;
                            realVector.setEntry(i2, this.f631xL.getEntry(i2) + (zbar * this.wbar.getEntry(i2)));
                            i = i2 + 1;
                            anorm = anorm2;
                        } else {
                            return;
                        }
                    }
                } else {
                    while (true) {
                        int i3 = i;
                        if (i3 < n) {
                            double xi = this.f631xL.getEntry(i3);
                            double d = xi;
                            realVector.setEntry(i3, xi + (zbar * this.wbar.getEntry(i3)) + (step * this.f628mb.getEntry(i3)));
                            i = i3 + 1;
                        } else {
                            return;
                        }
                    }
                }
            } else if (!this.goodb) {
                realVector.setSubVector(0, this.f631xL);
            } else {
                double step2 = this.bstep / this.beta1;
                while (true) {
                    int i4 = i;
                    if (i4 < n) {
                        realVector.setEntry(i4, (step2 * this.f628mb.getEntry(i4)) + this.f631xL.getEntry(i4));
                        i = i4 + 1;
                    } else {
                        return;
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void init() {
            this.f631xL.set(0.0d);
            this.f629r1 = this.f626b.copy();
            this.f632y = this.f627m == null ? this.f626b.copy() : this.f627m.operate(this.f629r1);
            if (this.f627m != null && this.check) {
                checkSymmetry(this.f627m, this.f629r1, this.f632y, this.f627m.operate(this.f632y));
            }
            this.beta1 = this.f629r1.dotProduct(this.f632y);
            if (this.beta1 < 0.0d) {
                throwNPDLOException(this.f627m, this.f632y);
            }
            if (this.beta1 == 0.0d) {
                this.bIsNull = true;
                return;
            }
            this.bIsNull = false;
            this.beta1 = FastMath.sqrt(this.beta1);
            RealVector v = this.f632y.mapMultiply(1.0d / this.beta1);
            this.f632y = this.f625a.operate(v);
            if (this.check) {
                checkSymmetry(this.f625a, v, this.f632y, this.f625a.operate(this.f632y));
            }
            daxpy(-this.shift, v, this.f632y);
            double alpha = v.dotProduct(this.f632y);
            daxpy((-alpha) / this.beta1, this.f629r1, this.f632y);
            daxpy((-v.dotProduct(this.f632y)) / v.dotProduct(v), v, this.f632y);
            this.f630r2 = this.f632y.copy();
            if (this.f627m != null) {
                this.f632y = this.f627m.operate(this.f630r2);
            }
            this.oldb = this.beta1;
            this.beta = this.f630r2.dotProduct(this.f632y);
            if (this.beta < 0.0d) {
                throwNPDLOException(this.f627m, this.f632y);
            }
            this.beta = FastMath.sqrt(this.beta);
            this.cgnorm = this.beta1;
            this.gbar = alpha;
            this.dbar = this.beta;
            this.gammaZeta = this.beta1;
            this.minusEpsZeta = 0.0d;
            this.bstep = 0.0d;
            this.snprod = 1.0d;
            this.tnorm = (alpha * alpha) + (this.beta * this.beta);
            this.ynorm2 = 0.0d;
            this.gmax = FastMath.abs(alpha) + MACH_PREC;
            this.gmin = this.gmax;
            if (this.goodb) {
                this.wbar = new ArrayRealVector(this.f625a.getRowDimension());
                this.wbar.set(0.0d);
            } else {
                this.wbar = v;
            }
            updateNorms();
        }

        /* access modifiers changed from: 0000 */
        public void update() {
            RealVector v = this.f632y.mapMultiply(1.0d / this.beta);
            this.f632y = this.f625a.operate(v);
            daxpbypz(-this.shift, v, (-this.beta) / this.oldb, this.f629r1, this.f632y);
            double alpha = v.dotProduct(this.f632y);
            daxpy((-alpha) / this.beta, this.f630r2, this.f632y);
            this.f629r1 = this.f630r2;
            this.f630r2 = this.f632y;
            if (this.f627m != null) {
                this.f632y = this.f627m.operate(this.f630r2);
            }
            this.oldb = this.beta;
            this.beta = this.f630r2.dotProduct(this.f632y);
            if (this.beta < 0.0d) {
                throwNPDLOException(this.f627m, this.f632y);
            }
            this.beta = FastMath.sqrt(this.beta);
            this.tnorm += (alpha * alpha) + (this.oldb * this.oldb) + (this.beta * this.beta);
            double gamma = FastMath.sqrt((this.gbar * this.gbar) + (this.oldb * this.oldb));
            double c = this.gbar / gamma;
            double s = this.oldb / gamma;
            double deltak = (this.dbar * c) + (s * alpha);
            this.gbar = (this.dbar * s) - (c * alpha);
            double eps = this.beta * s;
            double d = alpha;
            this.dbar = (-c) * this.beta;
            double zeta = this.gammaZeta / gamma;
            double zetaC = zeta * c;
            double zetaS = zeta * s;
            double eps2 = eps;
            int n = this.f631xL.getDimension();
            int i = 0;
            while (i < n) {
                int n2 = n;
                double xi = this.f631xL.getEntry(i);
                double vi = v.getEntry(i);
                double wi = this.wbar.getEntry(i);
                double zetaC2 = zetaC;
                this.f631xL.setEntry(i, xi + (wi * zetaC) + (vi * zetaS));
                this.wbar.setEntry(i, (wi * s) - (vi * c));
                i++;
                n = n2;
                zetaC = zetaC2;
            }
            double d2 = zetaC;
            this.bstep += this.snprod * c * zeta;
            this.snprod *= s;
            this.gmax = FastMath.max(this.gmax, gamma);
            this.gmin = FastMath.min(this.gmin, gamma);
            this.ynorm2 += zeta * zeta;
            this.gammaZeta = this.minusEpsZeta - (deltak * zeta);
            this.minusEpsZeta = (-eps2) * zeta;
            updateNorms();
        }

        private void updateNorms() {
            double acond;
            double anorm = FastMath.sqrt(this.tnorm);
            double ynorm = FastMath.sqrt(this.ynorm2);
            double epsx = anorm * ynorm * MACH_PREC;
            double epsr = anorm * ynorm * this.delta;
            double diag = this.gbar == 0.0d ? MACH_PREC * anorm : this.gbar;
            double d = anorm;
            double d2 = ynorm;
            this.lqnorm = FastMath.sqrt((this.gammaZeta * this.gammaZeta) + (this.minusEpsZeta * this.minusEpsZeta));
            double qrnorm = this.snprod * this.beta1;
            this.cgnorm = (this.beta * qrnorm) / FastMath.abs(diag);
            if (this.lqnorm <= this.cgnorm) {
                acond = this.gmax / this.gmin;
                double d3 = qrnorm;
            } else {
                double d4 = qrnorm;
                acond = this.gmax / FastMath.min(this.gmin, FastMath.abs(diag));
            }
            double acond2 = acond;
            if (MACH_PREC * acond2 >= 0.1d) {
                throw new IllConditionedOperatorException(acond2);
            } else if (this.beta1 <= epsx) {
                throw new SingularOperatorException();
            } else {
                this.rnorm = FastMath.min(this.cgnorm, this.lqnorm);
                this.hasConverged = this.cgnorm <= epsx || this.cgnorm <= epsr;
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean hasConverged() {
            return this.hasConverged;
        }

        /* access modifiers changed from: 0000 */
        public boolean bEqualsNullVector() {
            return this.bIsNull;
        }

        /* access modifiers changed from: 0000 */
        public boolean betaEqualsZero() {
            return this.beta < MACH_PREC;
        }

        /* access modifiers changed from: 0000 */
        public double getNormOfResidual() {
            return this.rnorm;
        }
    }

    public SymmLQ(int maxIterations, double delta2, boolean check2) {
        super(maxIterations);
        this.delta = delta2;
        this.check = check2;
    }

    public SymmLQ(IterationManager manager, double delta2, boolean check2) {
        super(manager);
        this.delta = delta2;
        this.check = check2;
    }

    public final boolean getCheck() {
        return this.check;
    }

    public RealVector solve(RealLinearOperator a, RealLinearOperator m, RealVector b) throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException, NonSelfAdjointOperatorException, NonPositiveDefiniteOperatorException, IllConditionedOperatorException {
        MathUtils.checkNotNull(a);
        return solveInPlace(a, m, b, new ArrayRealVector(a.getColumnDimension()), false, 0.0d);
    }

    public RealVector solve(RealLinearOperator a, RealLinearOperator m, RealVector b, boolean goodb, double shift) throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException, NonSelfAdjointOperatorException, NonPositiveDefiniteOperatorException, IllConditionedOperatorException {
        MathUtils.checkNotNull(a);
        return solveInPlace(a, m, b, new ArrayRealVector(a.getColumnDimension()), goodb, shift);
    }

    public RealVector solve(RealLinearOperator a, RealLinearOperator m, RealVector b, RealVector x) throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, NonSelfAdjointOperatorException, NonPositiveDefiniteOperatorException, IllConditionedOperatorException, MaxCountExceededException {
        MathUtils.checkNotNull(x);
        return solveInPlace(a, m, b, x.copy(), false, 0.0d);
    }

    public RealVector solve(RealLinearOperator a, RealVector b) throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, NonSelfAdjointOperatorException, IllConditionedOperatorException, MaxCountExceededException {
        MathUtils.checkNotNull(a);
        ArrayRealVector arrayRealVector = new ArrayRealVector(a.getColumnDimension());
        arrayRealVector.set(0.0d);
        return solveInPlace(a, null, b, arrayRealVector, false, 0.0d);
    }

    public RealVector solve(RealLinearOperator a, RealVector b, boolean goodb, double shift) throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, NonSelfAdjointOperatorException, IllConditionedOperatorException, MaxCountExceededException {
        MathUtils.checkNotNull(a);
        return solveInPlace(a, null, b, new ArrayRealVector(a.getColumnDimension()), goodb, shift);
    }

    public RealVector solve(RealLinearOperator a, RealVector b, RealVector x) throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, NonSelfAdjointOperatorException, IllConditionedOperatorException, MaxCountExceededException {
        MathUtils.checkNotNull(x);
        return solveInPlace(a, null, b, x.copy(), false, 0.0d);
    }

    public RealVector solveInPlace(RealLinearOperator a, RealLinearOperator m, RealVector b, RealVector x) throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, NonSelfAdjointOperatorException, NonPositiveDefiniteOperatorException, IllConditionedOperatorException, MaxCountExceededException {
        return solveInPlace(a, m, b, x, false, 0.0d);
    }

    public RealVector solveInPlace(RealLinearOperator a, RealLinearOperator m, RealVector b, RealVector x, boolean goodb, double shift) throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, NonSelfAdjointOperatorException, NonPositiveDefiniteOperatorException, IllConditionedOperatorException, MaxCountExceededException {
        RealVector realVector = x;
        checkParameters(a, m, b, x);
        IterationManager manager = getIterationManager();
        manager.resetIterationCount();
        manager.incrementIterationCount();
        State state = new State(a, m, b, goodb, shift, this.delta, this.check);
        state.init();
        state.refineSolution(realVector);
        DefaultIterativeLinearSolverEvent defaultIterativeLinearSolverEvent = new DefaultIterativeLinearSolverEvent(this, manager.getIterations(), realVector, b, state.getNormOfResidual());
        if (state.bEqualsNullVector()) {
            manager.fireTerminationEvent(defaultIterativeLinearSolverEvent);
            return realVector;
        }
        boolean earlyStop = state.betaEqualsZero() || state.hasConverged();
        manager.fireInitializationEvent(defaultIterativeLinearSolverEvent);
        if (!earlyStop) {
            DefaultIterativeLinearSolverEvent defaultIterativeLinearSolverEvent2 = defaultIterativeLinearSolverEvent;
            do {
                manager.incrementIterationCount();
                RealVector realVector2 = realVector;
                RealVector realVector3 = b;
                DefaultIterativeLinearSolverEvent defaultIterativeLinearSolverEvent3 = new DefaultIterativeLinearSolverEvent(this, manager.getIterations(), realVector2, realVector3, state.getNormOfResidual());
                manager.fireIterationStartedEvent(defaultIterativeLinearSolverEvent3);
                state.update();
                state.refineSolution(realVector);
                DefaultIterativeLinearSolverEvent defaultIterativeLinearSolverEvent4 = new DefaultIterativeLinearSolverEvent(this, manager.getIterations(), realVector2, realVector3, state.getNormOfResidual());
                manager.fireIterationPerformedEvent(defaultIterativeLinearSolverEvent4);
            } while (!state.hasConverged());
        } else {
            DefaultIterativeLinearSolverEvent defaultIterativeLinearSolverEvent5 = defaultIterativeLinearSolverEvent;
        }
        DefaultIterativeLinearSolverEvent defaultIterativeLinearSolverEvent6 = new DefaultIterativeLinearSolverEvent(this, manager.getIterations(), realVector, b, state.getNormOfResidual());
        manager.fireTerminationEvent(defaultIterativeLinearSolverEvent6);
        return realVector;
    }

    public RealVector solveInPlace(RealLinearOperator a, RealVector b, RealVector x) throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, NonSelfAdjointOperatorException, IllConditionedOperatorException, MaxCountExceededException {
        return solveInPlace(a, null, b, x, false, 0.0d);
    }
}
