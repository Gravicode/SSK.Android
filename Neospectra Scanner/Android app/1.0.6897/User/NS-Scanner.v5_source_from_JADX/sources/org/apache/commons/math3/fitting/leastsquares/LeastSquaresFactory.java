package org.apache.commons.math3.fitting.leastsquares;

import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem.Evaluation;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DiagonalMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.AbstractOptimizationProblem;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.PointVectorValuePair;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Incrementor;
import org.apache.commons.math3.util.Pair;

public class LeastSquaresFactory {

    private static class LocalLeastSquaresProblem extends AbstractOptimizationProblem<Evaluation> implements LeastSquaresProblem {
        private final boolean lazyEvaluation;
        private final MultivariateJacobianFunction model;
        private final ParameterValidator paramValidator;
        private final RealVector start;
        private final RealVector target;

        private static class LazyUnweightedEvaluation extends AbstractEvaluation {
            private final ValueAndJacobianFunction model;
            private final RealVector point;
            private final RealVector target;

            private LazyUnweightedEvaluation(ValueAndJacobianFunction model2, RealVector target2, RealVector point2) {
                super(target2.getDimension());
                this.model = model2;
                this.point = point2;
                this.target = target2;
            }

            public RealMatrix getJacobian() {
                return this.model.computeJacobian(this.point.toArray());
            }

            public RealVector getPoint() {
                return this.point;
            }

            public RealVector getResiduals() {
                return this.target.subtract(this.model.computeValue(this.point.toArray()));
            }
        }

        private static class UnweightedEvaluation extends AbstractEvaluation {
            private final RealMatrix jacobian;
            private final RealVector point;
            private final RealVector residuals;

            private UnweightedEvaluation(RealVector values, RealMatrix jacobian2, RealVector target, RealVector point2) {
                super(target.getDimension());
                this.jacobian = jacobian2;
                this.point = point2;
                this.residuals = target.subtract(values);
            }

            public RealMatrix getJacobian() {
                return this.jacobian;
            }

            public RealVector getPoint() {
                return this.point;
            }

            public RealVector getResiduals() {
                return this.residuals;
            }
        }

        LocalLeastSquaresProblem(MultivariateJacobianFunction model2, RealVector target2, RealVector start2, ConvergenceChecker<Evaluation> checker, int maxEvaluations, int maxIterations, boolean lazyEvaluation2, ParameterValidator paramValidator2) {
            super(maxEvaluations, maxIterations, checker);
            this.target = target2;
            this.model = model2;
            this.start = start2;
            this.lazyEvaluation = lazyEvaluation2;
            this.paramValidator = paramValidator2;
            if (lazyEvaluation2 && !(model2 instanceof ValueAndJacobianFunction)) {
                throw new MathIllegalStateException(LocalizedFormats.INVALID_IMPLEMENTATION, model2.getClass().getName());
            }
        }

        public int getObservationSize() {
            return this.target.getDimension();
        }

        public int getParameterSize() {
            return this.start.getDimension();
        }

        public RealVector getStart() {
            if (this.start == null) {
                return null;
            }
            return this.start.copy();
        }

        public Evaluation evaluate(RealVector point) {
            RealVector p = this.paramValidator == null ? point.copy() : this.paramValidator.validate(point.copy());
            if (this.lazyEvaluation) {
                return new LazyUnweightedEvaluation((ValueAndJacobianFunction) this.model, this.target, p);
            }
            Pair<RealVector, RealMatrix> value = this.model.value(p);
            UnweightedEvaluation unweightedEvaluation = new UnweightedEvaluation((RealVector) value.getFirst(), (RealMatrix) value.getSecond(), this.target, p);
            return unweightedEvaluation;
        }
    }

    private static class LocalValueAndJacobianFunction implements ValueAndJacobianFunction {
        private final MultivariateMatrixFunction jacobian;
        private final MultivariateVectorFunction value;

        LocalValueAndJacobianFunction(MultivariateVectorFunction value2, MultivariateMatrixFunction jacobian2) {
            this.value = value2;
            this.jacobian = jacobian2;
        }

        public Pair<RealVector, RealMatrix> value(RealVector point) {
            double[] p = point.toArray();
            return new Pair<>(computeValue(p), computeJacobian(p));
        }

        public RealVector computeValue(double[] params) {
            return new ArrayRealVector(this.value.value(params), false);
        }

        public RealMatrix computeJacobian(double[] params) {
            return new Array2DRowRealMatrix(this.jacobian.value(params), false);
        }
    }

    private LeastSquaresFactory() {
    }

    public static LeastSquaresProblem create(MultivariateJacobianFunction model, RealVector observed, RealVector start, RealMatrix weight, ConvergenceChecker<Evaluation> checker, int maxEvaluations, int maxIterations, boolean lazyEvaluation, ParameterValidator paramValidator) {
        RealMatrix realMatrix = weight;
        LocalLeastSquaresProblem localLeastSquaresProblem = new LocalLeastSquaresProblem(model, observed, start, checker, maxEvaluations, maxIterations, lazyEvaluation, paramValidator);
        if (realMatrix != null) {
            return weightMatrix(localLeastSquaresProblem, realMatrix);
        }
        return localLeastSquaresProblem;
    }

    public static LeastSquaresProblem create(MultivariateJacobianFunction model, RealVector observed, RealVector start, ConvergenceChecker<Evaluation> checker, int maxEvaluations, int maxIterations) {
        return create(model, observed, start, null, checker, maxEvaluations, maxIterations, false, null);
    }

    public static LeastSquaresProblem create(MultivariateJacobianFunction model, RealVector observed, RealVector start, RealMatrix weight, ConvergenceChecker<Evaluation> checker, int maxEvaluations, int maxIterations) {
        return weightMatrix(create(model, observed, start, checker, maxEvaluations, maxIterations), weight);
    }

    public static LeastSquaresProblem create(MultivariateVectorFunction model, MultivariateMatrixFunction jacobian, double[] observed, double[] start, RealMatrix weight, ConvergenceChecker<Evaluation> checker, int maxEvaluations, int maxIterations) {
        return create(model(model, jacobian), new ArrayRealVector(observed, false), new ArrayRealVector(start, false), weight, checker, maxEvaluations, maxIterations);
    }

    public static LeastSquaresProblem weightMatrix(LeastSquaresProblem problem, RealMatrix weights) {
        final RealMatrix weightSquareRoot = squareRoot(weights);
        return new LeastSquaresAdapter(problem) {
            public Evaluation evaluate(RealVector point) {
                return new DenseWeightedEvaluation(super.evaluate(point), weightSquareRoot);
            }
        };
    }

    public static LeastSquaresProblem weightDiagonal(LeastSquaresProblem problem, RealVector weights) {
        return weightMatrix(problem, new DiagonalMatrix(weights.toArray()));
    }

    public static LeastSquaresProblem countEvaluations(LeastSquaresProblem problem, final Incrementor counter) {
        return new LeastSquaresAdapter(problem) {
            public Evaluation evaluate(RealVector point) {
                counter.incrementCount();
                return super.evaluate(point);
            }
        };
    }

    public static ConvergenceChecker<Evaluation> evaluationChecker(final ConvergenceChecker<PointVectorValuePair> checker) {
        return new ConvergenceChecker<Evaluation>() {
            public boolean converged(int iteration, Evaluation previous, Evaluation current) {
                return checker.converged(iteration, new PointVectorValuePair(previous.getPoint().toArray(), previous.getResiduals().toArray(), false), new PointVectorValuePair(current.getPoint().toArray(), current.getResiduals().toArray(), false));
            }
        };
    }

    private static RealMatrix squareRoot(RealMatrix m) {
        if (!(m instanceof DiagonalMatrix)) {
            return new EigenDecomposition(m).getSquareRoot();
        }
        int dim = m.getRowDimension();
        RealMatrix sqrtM = new DiagonalMatrix(dim);
        for (int i = 0; i < dim; i++) {
            sqrtM.setEntry(i, i, FastMath.sqrt(m.getEntry(i, i)));
        }
        return sqrtM;
    }

    public static MultivariateJacobianFunction model(MultivariateVectorFunction value, MultivariateMatrixFunction jacobian) {
        return new LocalValueAndJacobianFunction(value, jacobian);
    }
}
