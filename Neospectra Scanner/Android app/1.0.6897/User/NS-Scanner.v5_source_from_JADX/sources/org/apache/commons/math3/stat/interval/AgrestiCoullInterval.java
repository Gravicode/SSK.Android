package org.apache.commons.math3.stat.interval;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.util.FastMath;

public class AgrestiCoullInterval implements BinomialConfidenceInterval {
    public ConfidenceInterval createInterval(int numberOfTrials, int numberOfSuccesses, double confidenceLevel) {
        IntervalUtils.checkParameters(numberOfTrials, numberOfSuccesses, confidenceLevel);
        double z = new NormalDistribution().inverseCumulativeProbability(1.0d - ((1.0d - confidenceLevel) / 2.0d));
        double zSquared = FastMath.pow(z, 2);
        double modifiedNumberOfTrials = ((double) numberOfTrials) + zSquared;
        double modifiedSuccessesRatio = (1.0d / modifiedNumberOfTrials) * (((double) numberOfSuccesses) + (0.5d * zSquared));
        double difference = FastMath.sqrt((1.0d / modifiedNumberOfTrials) * modifiedSuccessesRatio * (1.0d - modifiedSuccessesRatio)) * z;
        ConfidenceInterval confidenceInterval = new ConfidenceInterval(modifiedSuccessesRatio - difference, modifiedSuccessesRatio + difference, confidenceLevel);
        return confidenceInterval;
    }
}
