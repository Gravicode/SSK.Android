package org.apache.commons.math3.p006ml.neuralnet;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.Constant;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;

/* renamed from: org.apache.commons.math3.ml.neuralnet.FeatureInitializerFactory */
public class FeatureInitializerFactory {
    private FeatureInitializerFactory() {
    }

    public static FeatureInitializer uniform(RandomGenerator rng, double min, double max) {
        UniformRealDistribution uniformRealDistribution = new UniformRealDistribution(rng, min, max);
        return randomize(uniformRealDistribution, function(new Constant(0.0d), 0.0d, 0.0d));
    }

    public static FeatureInitializer uniform(double min, double max) {
        return randomize(new UniformRealDistribution(min, max), function(new Constant(0.0d), 0.0d, 0.0d));
    }

    public static FeatureInitializer function(UnivariateFunction f, double init, double inc) {
        final double d = init;
        final UnivariateFunction univariateFunction = f;
        final double d2 = inc;
        C09881 r0 = new FeatureInitializer() {
            private double arg = d;

            public double value() {
                double result = univariateFunction.value(this.arg);
                this.arg += d2;
                return result;
            }
        };
        return r0;
    }

    public static FeatureInitializer randomize(final RealDistribution random, final FeatureInitializer orig) {
        return new FeatureInitializer() {
            public double value() {
                return orig.value() + random.sample();
            }
        };
    }
}
