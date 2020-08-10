package org.apache.commons.math3.stat.interval;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.util.FastMath;

public class WilsonScoreInterval implements BinomialConfidenceInterval {
    public ConfidenceInterval createInterval(int numberOfTrials, int numberOfSuccesses, double confidenceLevel) {
        int i = numberOfTrials;
        IntervalUtils.checkParameters(numberOfTrials, numberOfSuccesses, confidenceLevel);
        double z = new NormalDistribution().inverseCumulativeProbability(1.0d - ((1.0d - confidenceLevel) / 2.0d));
        double zSquared = FastMath.pow(z, 2);
        double mean = ((double) numberOfSuccesses) / ((double) i);
        double factor = 1.0d / (((1.0d / ((double) i)) * zSquared) + 1.0d);
        double modifiedSuccessRatio = mean + ((1.0d / ((double) (i * 2))) * zSquared);
        double d = mean;
        double difference = FastMath.sqrt(((1.0d / ((double) i)) * mean * (1.0d - mean)) + ((1.0d / (FastMath.pow((double) i, 2) * 4.0d)) * zSquared)) * z;
        double d2 = z;
        ConfidenceInterval confidenceInterval = new ConfidenceInterval(factor * (modifiedSuccessRatio - difference), factor * (modifiedSuccessRatio + difference), confidenceLevel);
        return confidenceInterval;
    }
}
