package org.apache.commons.math3.stat.interval;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.util.FastMath;

public class NormalApproximationInterval implements BinomialConfidenceInterval {
    public ConfidenceInterval createInterval(int numberOfTrials, int numberOfSuccesses, double confidenceLevel) {
        int i = numberOfTrials;
        IntervalUtils.checkParameters(numberOfTrials, numberOfSuccesses, confidenceLevel);
        double mean = ((double) numberOfSuccesses) / ((double) i);
        double alpha = (1.0d - confidenceLevel) / 2.0d;
        NormalDistribution normalDistribution = new NormalDistribution();
        double difference = FastMath.sqrt((1.0d / ((double) i)) * mean * (1.0d - mean)) * normalDistribution.inverseCumulativeProbability(1.0d - alpha);
        NormalDistribution normalDistribution2 = normalDistribution;
        ConfidenceInterval confidenceInterval = new ConfidenceInterval(mean - difference, mean + difference, confidenceLevel);
        return confidenceInterval;
    }
}
