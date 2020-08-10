package org.apache.commons.math3.stat.interval;

import org.apache.commons.math3.distribution.FDistribution;

public class ClopperPearsonInterval implements BinomialConfidenceInterval {
    public ConfidenceInterval createInterval(int numberOfTrials, int numberOfSuccesses, double confidenceLevel) {
        int i = numberOfSuccesses;
        IntervalUtils.checkParameters(numberOfTrials, numberOfSuccesses, confidenceLevel);
        double lowerBound = 0.0d;
        double upperBound = 0.0d;
        double alpha = (1.0d - confidenceLevel) / 2.0d;
        FDistribution distributionLowerBound = new FDistribution((double) (((numberOfTrials - i) + 1) * 2), (double) (i * 2));
        double fValueLowerBound = distributionLowerBound.inverseCumulativeProbability(1.0d - alpha);
        if (i > 0) {
            lowerBound = ((double) i) / (((double) i) + (((double) ((numberOfTrials - i) + 1)) * fValueLowerBound));
        }
        double fValueUpperBound = new FDistribution((double) ((i + 1) * 2), (double) ((numberOfTrials - i) * 2)).inverseCumulativeProbability(1.0d - alpha);
        if (i > 0) {
            upperBound = (((double) (i + 1)) * fValueUpperBound) / (((double) (numberOfTrials - i)) + (((double) (i + 1)) * fValueUpperBound));
        }
        FDistribution fDistribution = distributionLowerBound;
        ConfidenceInterval confidenceInterval = new ConfidenceInterval(lowerBound, upperBound, confidenceLevel);
        return confidenceInterval;
    }
}
