package org.apache.commons.math3.fitting;

import org.apache.commons.math3.analysis.function.HarmonicOscillator.Parametric;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optim.nonlinear.vector.MultivariateVectorOptimizer;
import org.apache.commons.math3.util.FastMath;

@Deprecated
public class HarmonicFitter extends CurveFitter<Parametric> {

    public static class ParameterGuesser {

        /* renamed from: a */
        private final double f567a;
        private final double omega;
        private final double phi;

        public ParameterGuesser(WeightedObservedPoint[] observations) {
            if (observations.length < 4) {
                throw new NumberIsTooSmallException(LocalizedFormats.INSUFFICIENT_OBSERVED_POINTS_IN_SAMPLE, Integer.valueOf(observations.length), Integer.valueOf(4), true);
            }
            WeightedObservedPoint[] sorted = sortObservations(observations);
            double[] aOmega = guessAOmega(sorted);
            this.f567a = aOmega[0];
            this.omega = aOmega[1];
            this.phi = guessPhi(sorted);
        }

        public double[] guess() {
            return new double[]{this.f567a, this.omega, this.phi};
        }

        private WeightedObservedPoint[] sortObservations(WeightedObservedPoint[] unsorted) {
            WeightedObservedPoint[] observations = (WeightedObservedPoint[]) unsorted.clone();
            WeightedObservedPoint curr = observations[0];
            for (int j = 1; j < observations.length; j++) {
                WeightedObservedPoint prec = curr;
                curr = observations[j];
                if (curr.getX() < prec.getX()) {
                    int i = j - 1;
                    WeightedObservedPoint mI = observations[i];
                    while (i >= 0 && curr.getX() < mI.getX()) {
                        observations[i + 1] = mI;
                        int i2 = i - 1;
                        if (i != 0) {
                            mI = observations[i2];
                        }
                        i = i2;
                    }
                    observations[i + 1] = curr;
                    curr = observations[j];
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

    public HarmonicFitter(MultivariateVectorOptimizer optimizer) {
        super(optimizer);
    }

    public double[] fit(double[] initialGuess) {
        return fit(new Parametric(), initialGuess);
    }

    public double[] fit() {
        return fit(new ParameterGuesser(getObservations()).guess());
    }
}
