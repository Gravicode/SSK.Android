package org.apache.commons.math3.stat.inference;

import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class BinomialTest {
    public boolean binomialTest(int numberOfTrials, int numberOfSuccesses, double probability, AlternativeHypothesis alternativeHypothesis, double alpha) {
        return binomialTest(numberOfTrials, numberOfSuccesses, probability, alternativeHypothesis) < alpha;
    }

    public double binomialTest(int numberOfTrials, int numberOfSuccesses, double probability, AlternativeHypothesis alternativeHypothesis) {
        int i = numberOfTrials;
        int i2 = numberOfSuccesses;
        double d = probability;
        if (i < 0) {
            throw new NotPositiveException(Integer.valueOf(numberOfTrials));
        } else if (i2 < 0) {
            throw new NotPositiveException(Integer.valueOf(numberOfSuccesses));
        } else {
            double pTotal = 0.0d;
            if (d < 0.0d || d > 1.0d) {
                throw new OutOfRangeException(Double.valueOf(probability), Integer.valueOf(0), Integer.valueOf(1));
            } else if (i < i2) {
                throw new MathIllegalArgumentException(LocalizedFormats.BINOMIAL_INVALID_PARAMETERS_ORDER, Integer.valueOf(numberOfTrials), Integer.valueOf(numberOfSuccesses));
            } else if (alternativeHypothesis == null) {
                throw new NullArgumentException();
            } else {
                BinomialDistribution distribution = new BinomialDistribution(null, i, d);
                switch (alternativeHypothesis) {
                    case GREATER_THAN:
                        return 1.0d - distribution.cumulativeProbability(i2 - 1);
                    case LESS_THAN:
                        return distribution.cumulativeProbability(i2);
                    case TWO_SIDED:
                        int criticalValueLow = 0;
                        int criticalValueHigh = i;
                        do {
                            double pLow = distribution.probability(criticalValueLow);
                            double pHigh = distribution.probability(criticalValueHigh);
                            if (pLow == pHigh) {
                                pTotal += 2.0d * pLow;
                                criticalValueLow++;
                                criticalValueHigh--;
                            } else if (pLow < pHigh) {
                                pTotal += pLow;
                                criticalValueLow++;
                            } else {
                                pTotal += pHigh;
                                criticalValueHigh--;
                            }
                            if (criticalValueLow <= i2) {
                            }
                            return pTotal;
                        } while (criticalValueHigh >= i2);
                        return pTotal;
                    default:
                        throw new MathInternalError(LocalizedFormats.OUT_OF_RANGE_SIMPLE, alternativeHypothesis, AlternativeHypothesis.TWO_SIDED, AlternativeHypothesis.LESS_THAN);
                }
            }
        }
    }
}
