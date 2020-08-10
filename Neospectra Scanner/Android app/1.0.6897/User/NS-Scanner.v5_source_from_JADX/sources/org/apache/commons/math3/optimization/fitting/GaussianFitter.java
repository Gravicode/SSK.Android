package org.apache.commons.math3.optimization.fitting;

import java.util.Arrays;
import java.util.Comparator;
import org.apache.commons.math3.analysis.function.Gaussian.Parametric;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.DifferentiableMultivariateVectorOptimizer;
import org.apache.commons.math3.util.FastMath;

@Deprecated
public class GaussianFitter extends CurveFitter<Parametric> {

    public static class ParameterGuesser {
        private final double mean;
        private final double norm;
        private final double sigma;

        public ParameterGuesser(WeightedObservedPoint[] observations) {
            if (observations == null) {
                throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
            } else if (observations.length < 3) {
                throw new NumberIsTooSmallException(Integer.valueOf(observations.length), Integer.valueOf(3), true);
            } else {
                double[] params = basicGuess(sortObservations(observations));
                this.norm = params[0];
                this.mean = params[1];
                this.sigma = params[2];
            }
        }

        public double[] guess() {
            return new double[]{this.norm, this.mean, this.sigma};
        }

        private WeightedObservedPoint[] sortObservations(WeightedObservedPoint[] unsorted) {
            WeightedObservedPoint[] observations = (WeightedObservedPoint[]) unsorted.clone();
            Arrays.sort(observations, new Comparator<WeightedObservedPoint>() {
                public int compare(WeightedObservedPoint p1, WeightedObservedPoint p2) {
                    if (p1 == null && p2 == null) {
                        return 0;
                    }
                    if (p1 == null) {
                        return -1;
                    }
                    if (p2 == null) {
                        return 1;
                    }
                    int cmpX = Double.compare(p1.getX(), p2.getX());
                    if (cmpX < 0) {
                        return -1;
                    }
                    if (cmpX > 0) {
                        return 1;
                    }
                    int cmpY = Double.compare(p1.getY(), p2.getY());
                    if (cmpY < 0) {
                        return -1;
                    }
                    if (cmpY > 0) {
                        return 1;
                    }
                    int cmpW = Double.compare(p1.getWeight(), p2.getWeight());
                    if (cmpW < 0) {
                        return -1;
                    }
                    if (cmpW > 0) {
                        return 1;
                    }
                    return 0;
                }
            });
            return observations;
        }

        private double[] basicGuess(WeightedObservedPoint[] points) {
            double fwhmX2;
            WeightedObservedPoint[] weightedObservedPointArr = points;
            int maxYIdx = findMaxY(points);
            double n = weightedObservedPointArr[maxYIdx].getY();
            double m = weightedObservedPointArr[maxYIdx].getX();
            double halfY = n + ((m - n) / 2.0d);
            try {
                fwhmX2 = interpolateXAtY(weightedObservedPointArr, maxYIdx, 1, halfY) - interpolateXAtY(weightedObservedPointArr, maxYIdx, -1, halfY);
            } catch (OutOfRangeException e) {
                OutOfRangeException outOfRangeException = e;
                fwhmX2 = weightedObservedPointArr[weightedObservedPointArr.length - 1].getX() - weightedObservedPointArr[0].getX();
            }
            return new double[]{n, m, fwhmX2 / (FastMath.sqrt(FastMath.log(2.0d) * 2.0d) * 2.0d)};
        }

        private int findMaxY(WeightedObservedPoint[] points) {
            int maxYIdx = 0;
            for (int i = 1; i < points.length; i++) {
                if (points[i].getY() > points[maxYIdx].getY()) {
                    maxYIdx = i;
                }
            }
            return maxYIdx;
        }

        private double interpolateXAtY(WeightedObservedPoint[] points, int startIdx, int idxStep, double y) throws OutOfRangeException {
            if (idxStep == 0) {
                throw new ZeroException();
            }
            WeightedObservedPoint[] twoPoints = getInterpolationPointsForY(points, startIdx, idxStep, y);
            WeightedObservedPoint p1 = twoPoints[0];
            WeightedObservedPoint p2 = twoPoints[1];
            if (p1.getY() == y) {
                return p1.getX();
            }
            if (p2.getY() == y) {
                return p2.getX();
            }
            return p1.getX() + (((y - p1.getY()) * (p2.getX() - p1.getX())) / (p2.getY() - p1.getY()));
        }

        private WeightedObservedPoint[] getInterpolationPointsForY(WeightedObservedPoint[] points, int startIdx, int idxStep, double y) throws OutOfRangeException {
            if (idxStep == 0) {
                throw new ZeroException();
            }
            int i = startIdx;
            while (true) {
                if (idxStep >= 0) {
                    if (i + idxStep >= points.length) {
                        break;
                    }
                } else if (i + idxStep < 0) {
                    break;
                }
                WeightedObservedPoint p1 = points[i];
                WeightedObservedPoint p2 = points[i + idxStep];
                if (!isBetween(y, p1.getY(), p2.getY())) {
                    i += idxStep;
                } else if (idxStep < 0) {
                    return new WeightedObservedPoint[]{p2, p1};
                } else {
                    return new WeightedObservedPoint[]{p1, p2};
                }
            }
            throw new OutOfRangeException(Double.valueOf(y), Double.valueOf(Double.NEGATIVE_INFINITY), Double.valueOf(Double.POSITIVE_INFINITY));
        }

        private boolean isBetween(double value, double boundary1, double boundary2) {
            return (value >= boundary1 && value <= boundary2) || (value >= boundary2 && value <= boundary1);
        }
    }

    public GaussianFitter(DifferentiableMultivariateVectorOptimizer optimizer) {
        super(optimizer);
    }

    public double[] fit(double[] initialGuess) {
        return fit(new Parametric() {
            public double value(double x, double... p) {
                try {
                    return super.value(x, p);
                } catch (NotStrictlyPositiveException e) {
                    return Double.POSITIVE_INFINITY;
                }
            }

            public double[] gradient(double x, double... p) {
                try {
                    return super.gradient(x, p);
                } catch (NotStrictlyPositiveException e) {
                    return new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
                }
            }
        }, initialGuess);
    }

    public double[] fit() {
        return fit(new ParameterGuesser(getObservations()).guess());
    }
}
