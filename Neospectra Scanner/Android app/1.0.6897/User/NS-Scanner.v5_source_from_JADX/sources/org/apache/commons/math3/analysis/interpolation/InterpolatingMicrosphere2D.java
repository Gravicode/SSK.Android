package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.util.FastMath;

public class InterpolatingMicrosphere2D extends InterpolatingMicrosphere {
    private static final int DIMENSION = 2;

    public InterpolatingMicrosphere2D(int size, double maxDarkFraction, double darkThreshold, double background) {
        int i = size;
        super(2, i, maxDarkFraction, darkThreshold, background);
        for (int i2 = 0; i2 < i; i2++) {
            double angle = (((double) i2) * 6.283185307179586d) / ((double) i);
            add(new double[]{FastMath.cos(angle), FastMath.sin(angle)}, false);
        }
    }

    protected InterpolatingMicrosphere2D(InterpolatingMicrosphere2D other) {
        super(other);
    }

    public InterpolatingMicrosphere2D copy() {
        return new InterpolatingMicrosphere2D(this);
    }
}
