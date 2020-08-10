package org.apache.commons.math3.fitting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.math3.analysis.function.HarmonicOscillator.Parametric;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.linear.DiagonalMatrix;
import org.apache.commons.math3.util.FastMath;

public class HarmonicCurveFitter extends AbstractCurveFitter {
    private static final Parametric FUNCTION = new Parametric();
    private final double[] initialGuess;
    private final int maxIter;

    public static class ParameterGuesser {

        /* renamed from: a */
        private final double f566a;
        private final double omega;
        private final double phi;

        public ParameterGuesser(Collection<WeightedObservedPoint> observations) {
            if (observations.size() < 4) {
                throw new NumberIsTooSmallException(LocalizedFormats.INSUFFICIENT_OBSERVED_POINTS_IN_SAMPLE, Integer.valueOf(observations.size()), Integer.valueOf(4), true);
            }
            WeightedObservedPoint[] sorted = (WeightedObservedPoint[]) sortObservations(observations).toArray(new WeightedObservedPoint[0]);
            double[] aOmega = guessAOmega(sorted);
            this.f566a = aOmega[0];
            this.omega = aOmega[1];
            this.phi = guessPhi(sorted);
        }

        public double[] guess() {
            return new double[]{this.f566a, this.omega, this.phi};
        }

        private List<WeightedObservedPoint> sortObservations(Collection<WeightedObservedPoint> unsorted) {
            List<WeightedObservedPoint> observations = new ArrayList<>(unsorted);
            WeightedObservedPoint curr = (WeightedObservedPoint) observations.get(0);
            int len = observations.size();
            for (int j = 1; j < len; j++) {
                WeightedObservedPoint prec = curr;
                curr = (WeightedObservedPoint) observations.get(j);
                if (curr.getX() < prec.getX()) {
                    int i = j - 1;
                    WeightedObservedPoint mI = (WeightedObservedPoint) observations.get(i);
                    while (i >= 0 && curr.getX() < mI.getX()) {
                        observations.set(i + 1, mI);
                        int i2 = i - 1;
                        if (i != 0) {
                            mI = (WeightedObservedPoint) observations.get(i2);
                        }
                        i = i2;
                    }
                    observations.set(i + 1, curr);
                    curr = (WeightedObservedPoint) observations.get(j);
                }
            }
            return observations;
        }

        private double[] guessAOmega(WeightedObservedPoint[] observations) {
            WeightedObservedPoint[] weightedObservedPointArr = observations;
            double[] aOmega = new double[2];
            double currentX = weightedObservedPointArr[0].getX();
            double currentY = weightedObservedPointArr[0].getY();
            double f2Integral = 0.0d;
            double fPrime2Integral = 0.0d;
            double startX = currentX;
            int i = 1;
            double syz = 0.0d;
            double sxz = 0.0d;
            double sxy = 0.0d;
            double sy2 = 0.0d;
            double sx2 = 0.0d;
            for (int i2 = 1; i2 < weightedObservedPointArr.length; i2++) {
                double previousX = currentX;
                double previousY = currentY;
                currentX = weightedObservedPointArr[i2].getX();
                currentY = weightedObservedPointArr[i2].getY();
                double dx = currentX - previousX;
                double dy = currentY - previousY;
                double x = currentX - startX;
                f2Integral += ((((previousY * previousY) + (previousY * currentY)) + (currentY * currentY)) * dx) / 3.0d;
                fPrime2Integral += (dy * dy) / dx;
                sx2 += x * x;
                sy2 += f2Integral * f2Integral;
                sxy += x * f2Integral;
                sxz += x * fPrime2Integral;
                syz += f2Integral * fPrime2Integral;
            }
            double c1 = (sy2 * sxz) - (sxy * syz);
            double c2 = (sxy * sxz) - (sx2 * syz);
            double c3 = (sx2 * sy2) - (sxy * sxy);
            if (c1 / c2 < 0.0d) {
            } else if (c2 / c3 < 0.0d) {
                double d = sx2;
            } else if (c2 == 0.0d) {
                throw new MathIllegalStateException(LocalizedFormats.ZERO_DENOMINATOR, new Object[0]);
            } else {
                double d2 = sx2;
                aOmega[0] = FastMath.sqrt(c1 / c2);
                aOmega[1] = FastMath.sqrt(c2 / c3);
                return aOmega;
            }
            double xRange = weightedObservedPointArr[weightedObservedPointArr.length - 1].getX() - weightedObservedPointArr[0].getX();
            if (xRange == 0.0d) {
                throw new ZeroException();
            }
            aOmega[1] = 6.283185307179586d / xRange;
            double yMin = Double.POSITIVE_INFINITY;
            double yMax = Double.NEGATIVE_INFINITY;
            while (true) {
                int i3 = i;
                if (i3 >= weightedObservedPointArr.length) {
                    break;
                }
                double y = weightedObservedPointArr[i3].getY();
                if (y < yMin) {
                    yMin = y;
                }
                if (y > yMax) {
                    yMax = y;
                }
                i = i3 + 1;
            }
            aOmega[0] = (yMax - yMin) * 0.5d;
            return aOmega;
        }

        private double guessPhi(WeightedObservedPoint[] observations) {
            WeightedObservedPoint[] weightedObservedPointArr = observations;
            double fcMean = 0.0d;
            double fsMean = 0.0d;
            double currentX = weightedObservedPointArr[0].getX();
            double currentY = weightedObservedPointArr[0].getY();
            int i = 1;
            while (i < weightedObservedPointArr.length) {
                double previousX = currentX;
                double previousY = currentY;
                double currentX2 = weightedObservedPointArr[i].getX();
                currentY = weightedObservedPointArr[i].getY();
                double currentYPrime = (currentY - previousY) / (currentX2 - previousX);
                double d = previousX;
                double omegaX = this.omega * currentX2;
                double cosine = FastMath.cos(omegaX);
                double sine = FastMath.sin(omegaX);
                fcMean += ((this.omega * currentY) * cosine) - (currentYPrime * sine);
                fsMean += (this.omega * currentY * sine) + (currentYPrime * cosine);
                i++;
                currentX = currentX2;
            }
            return FastMath.atan2(-fsMean, fcMean);
        }
    }

    private HarmonicCurveFitter(double[] initialGuess2, int maxIter2) {
        this.initialGuess = initialGuess2;
        this.maxIter = maxIter2;
    }

    public static HarmonicCurveFitter create() {
        return new HarmonicCurveFitter(null, Integer.MAX_VALUE);
    }

    public HarmonicCurveFitter withStartPoint(double[] newStart) {
        return new HarmonicCurveFitter((double[]) newStart.clone(), this.maxIter);
    }

    public HarmonicCurveFitter withMaxIterations(int newMaxIter) {
        return new HarmonicCurveFitter(this.initialGuess, newMaxIter);
    }

    /* access modifiers changed from: protected */
    public LeastSquaresProblem getProblem(Collection<WeightedObservedPoint> observations) {
        int len = observations.size();
        double[] target = new double[len];
        double[] weights = new double[len];
        int i = 0;
        for (WeightedObservedPoint obs : observations) {
            target[i] = obs.getY();
            weights[i] = obs.getWeight();
            i++;
        }
        TheoreticalValuesFunction model = new TheoreticalValuesFunction(FUNCTION, observations);
        return new LeastSquaresBuilder().maxEvaluations(Integer.MAX_VALUE).maxIterations(this.maxIter).start(this.initialGuess != null ? this.initialGuess : new ParameterGuesser(observations).guess()).target(target).weight(new DiagonalMatrix(weights)).model(model.getModelFunction(), model.getModelFunctionJacobian()).build();
    }
}
