package org.apache.commons.math3.analysis.interpolation;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.random.UnitSphereRandomVectorGenerator;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

public class InterpolatingMicrosphere {
    private final double background;
    private final double darkThreshold;
    private final int dimension;
    private final double maxDarkFraction;
    private final List<Facet> microsphere;
    private final List<FacetData> microsphereData;
    private final int size;

    private static class Facet {
        private final double[] normal;

        Facet(double[] n) {
            this.normal = n;
        }

        public double[] getNormal() {
            return this.normal;
        }
    }

    private static class FacetData {
        private final double illumination;
        private final double sample;

        FacetData(double illumination2, double sample2) {
            this.illumination = illumination2;
            this.sample = sample2;
        }

        public double illumination() {
            return this.illumination;
        }

        public double sample() {
            return this.sample;
        }
    }

    protected InterpolatingMicrosphere(int dimension2, int size2, double maxDarkFraction2, double darkThreshold2, double background2) {
        if (dimension2 <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(dimension2));
        } else if (size2 <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(size2));
        } else if (maxDarkFraction2 < 0.0d || maxDarkFraction2 > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(maxDarkFraction2), Integer.valueOf(0), Integer.valueOf(1));
        } else if (darkThreshold2 < 0.0d) {
            throw new NotPositiveException(Double.valueOf(darkThreshold2));
        } else {
            this.dimension = dimension2;
            this.size = size2;
            this.maxDarkFraction = maxDarkFraction2;
            this.darkThreshold = darkThreshold2;
            this.background = background2;
            this.microsphere = new ArrayList(size2);
            this.microsphereData = new ArrayList(size2);
        }
    }

    public InterpolatingMicrosphere(int dimension2, int size2, double maxDarkFraction2, double darkThreshold2, double background2, UnitSphereRandomVectorGenerator rand) {
        this(dimension2, size2, maxDarkFraction2, darkThreshold2, background2);
        for (int i = 0; i < size2; i++) {
            add(rand.nextVector(), false);
        }
    }

    protected InterpolatingMicrosphere(InterpolatingMicrosphere other) {
        this.dimension = other.dimension;
        this.size = other.size;
        this.maxDarkFraction = other.maxDarkFraction;
        this.darkThreshold = other.darkThreshold;
        this.background = other.background;
        this.microsphere = other.microsphere;
        this.microsphereData = new ArrayList(this.size);
        for (FacetData fd : other.microsphereData) {
            this.microsphereData.add(new FacetData(fd.illumination(), fd.sample()));
        }
    }

    public InterpolatingMicrosphere copy() {
        return new InterpolatingMicrosphere(this);
    }

    public int getDimension() {
        return this.dimension;
    }

    public int getSize() {
        return this.size;
    }

    public double value(double[] point, double[][] samplePoints, double[] sampleValues, double exponent, double noInterpolationTolerance) {
        double[][] dArr = samplePoints;
        double d = exponent;
        if (d < 0.0d) {
            throw new NotPositiveException(Double.valueOf(exponent));
        }
        clear();
        int numSamples = dArr.length;
        for (int i = 0; i < numSamples; i++) {
            double[] diff = MathArrays.ebeSubtract(dArr[i], point);
            double diffNorm = MathArrays.safeNorm(diff);
            if (FastMath.abs(diffNorm) < noInterpolationTolerance) {
                return sampleValues[i];
            }
            double[] dArr2 = diff;
            illuminate(dArr2, sampleValues[i], FastMath.pow(diffNorm, -d));
        }
        double[] dArr3 = point;
        return interpolate();
    }

    /* access modifiers changed from: protected */
    public void add(double[] normal, boolean copy) {
        if (this.microsphere.size() >= this.size) {
            throw new MaxCountExceededException(Integer.valueOf(this.size));
        } else if (normal.length > this.dimension) {
            throw new DimensionMismatchException(normal.length, this.dimension);
        } else {
            this.microsphere.add(new Facet(copy ? (double[]) normal.clone() : normal));
            this.microsphereData.add(new FacetData(0.0d, 0.0d));
        }
    }

    private double interpolate() {
        int darkCount = 0;
        double value = 0.0d;
        double totalWeight = 0.0d;
        for (FacetData fd : this.microsphereData) {
            double iV = fd.illumination();
            if (iV != 0.0d) {
                value += fd.sample() * iV;
                totalWeight += iV;
            } else {
                darkCount++;
            }
        }
        return ((double) darkCount) / ((double) this.size) <= this.maxDarkFraction ? value / totalWeight : this.background;
    }

    private void illuminate(double[] sampleDirection, double sampleValue, double weight) {
        for (int i = 0; i < this.size; i++) {
            double cos = MathArrays.cosAngle(((Facet) this.microsphere.get(i)).getNormal(), sampleDirection);
            if (cos > 0.0d) {
                double illumination = cos * weight;
                if (illumination > this.darkThreshold && illumination > ((FacetData) this.microsphereData.get(i)).illumination()) {
                    this.microsphereData.set(i, new FacetData(illumination, sampleValue));
                }
            }
        }
    }

    private void clear() {
        for (int i = 0; i < this.size; i++) {
            this.microsphereData.set(i, new FacetData(0.0d, 0.0d));
        }
    }
}
