package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;

public class StableRandomGenerator implements NormalizedRandomGenerator {
    private final double alpha;
    private final double beta;
    private final RandomGenerator generator;
    private final double zeta;

    public StableRandomGenerator(RandomGenerator generator2, double alpha2, double beta2) throws NullArgumentException, OutOfRangeException {
        if (generator2 == null) {
            throw new NullArgumentException();
        } else if (alpha2 <= 0.0d || alpha2 > 2.0d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_RANGE_LEFT, Double.valueOf(alpha2), Integer.valueOf(0), Integer.valueOf(2));
        } else if (beta2 < -1.0d || beta2 > 1.0d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_RANGE_SIMPLE, Double.valueOf(beta2), Integer.valueOf(-1), Integer.valueOf(1));
        } else {
            this.generator = generator2;
            this.alpha = alpha2;
            this.beta = beta2;
            if (alpha2 >= 2.0d || beta2 == 0.0d) {
                this.zeta = 0.0d;
            } else {
                this.zeta = FastMath.tan((3.141592653589793d * alpha2) / 2.0d) * beta2;
            }
        }
    }

    public double nextNormalizedDouble() {
        double x;
        double omega = -FastMath.log(this.generator.nextDouble());
        double phi = (this.generator.nextDouble() - 0.5d) * 3.141592653589793d;
        if (this.alpha == 2.0d) {
            return FastMath.sqrt(2.0d * omega) * FastMath.sin(phi);
        }
        if (this.beta != 0.0d) {
            double cosPhi = FastMath.cos(phi);
            if (FastMath.abs(this.alpha - 1.0d) > 1.0E-8d) {
                double alphaPhi = this.alpha * phi;
                double invAlphaPhi = phi - alphaPhi;
                double d = alphaPhi;
                double d2 = invAlphaPhi;
                x = (((FastMath.sin(alphaPhi) + (this.zeta * FastMath.cos(alphaPhi))) / cosPhi) * (FastMath.cos(invAlphaPhi) + (this.zeta * FastMath.sin(invAlphaPhi)))) / FastMath.pow(omega * cosPhi, (1.0d - this.alpha) / this.alpha);
            } else {
                double betaPhi = (this.beta * phi) + 1.5707963267948966d;
                double x2 = ((FastMath.tan(phi) * betaPhi) - (this.beta * FastMath.log(((1.5707963267948966d * omega) * cosPhi) / betaPhi))) * 0.6366197723675814d;
                if (this.alpha != 1.0d) {
                    double d3 = omega;
                    x = x2 + (this.beta * FastMath.tan((this.alpha * 3.141592653589793d) / 2.0d));
                } else {
                    x = x2;
                }
                return x;
            }
        } else if (this.alpha == 1.0d) {
            x = FastMath.tan(phi);
        } else {
            x = (FastMath.pow(FastMath.cos((1.0d - this.alpha) * phi) * omega, (1.0d / this.alpha) - 1.0d) * FastMath.sin(this.alpha * phi)) / FastMath.pow(FastMath.cos(phi), 1.0d / this.alpha);
        }
        return x;
    }
}
