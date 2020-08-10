package org.apache.commons.math3.stat.inference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.util.MathUtils;

public class OneWayAnova {

    private static class AnovaStats {
        /* access modifiers changed from: private */

        /* renamed from: F */
        public final double f792F;
        /* access modifiers changed from: private */
        public final int dfbg;
        /* access modifiers changed from: private */
        public final int dfwg;

        private AnovaStats(int dfbg2, int dfwg2, double F) {
            this.dfbg = dfbg2;
            this.dfwg = dfwg2;
            this.f792F = F;
        }
    }

    public double anovaFValue(Collection<double[]> categoryData) throws NullArgumentException, DimensionMismatchException {
        return anovaStats(categoryData).f792F;
    }

    public double anovaPValue(Collection<double[]> categoryData) throws NullArgumentException, DimensionMismatchException, ConvergenceException, MaxCountExceededException {
        AnovaStats a = anovaStats(categoryData);
        FDistribution fdist = new FDistribution((RandomGenerator) null, (double) a.dfbg, (double) a.dfwg);
        return 1.0d - fdist.cumulativeProbability(a.f792F);
    }

    public double anovaPValue(Collection<SummaryStatistics> categoryData, boolean allowOneElementData) throws NullArgumentException, DimensionMismatchException, ConvergenceException, MaxCountExceededException {
        AnovaStats a = anovaStats(categoryData, allowOneElementData);
        FDistribution fdist = new FDistribution((RandomGenerator) null, (double) a.dfbg, (double) a.dfwg);
        return 1.0d - fdist.cumulativeProbability(a.f792F);
    }

    private AnovaStats anovaStats(Collection<double[]> categoryData) throws NullArgumentException, DimensionMismatchException {
        MathUtils.checkNotNull(categoryData);
        Collection<SummaryStatistics> categoryDataSummaryStatistics = new ArrayList<>(categoryData.size());
        Iterator i$ = categoryData.iterator();
        while (true) {
            if (!i$.hasNext()) {
                return anovaStats(categoryDataSummaryStatistics, false);
            }
            double[] data = (double[]) i$.next();
            SummaryStatistics dataSummaryStatistics = new SummaryStatistics();
            categoryDataSummaryStatistics.add(dataSummaryStatistics);
            for (double val : data) {
                dataSummaryStatistics.addValue(val);
            }
        }
    }

    public boolean anovaTest(Collection<double[]> categoryData, double alpha) throws NullArgumentException, DimensionMismatchException, OutOfRangeException, ConvergenceException, MaxCountExceededException {
        if (alpha <= 0.0d || alpha > 0.5d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_BOUND_SIGNIFICANCE_LEVEL, Double.valueOf(alpha), Integer.valueOf(0), Double.valueOf(0.5d));
        } else if (anovaPValue(categoryData) < alpha) {
            return true;
        } else {
            return false;
        }
    }

    private AnovaStats anovaStats(Collection<SummaryStatistics> categoryData, boolean allowOneElementData) throws NullArgumentException, DimensionMismatchException {
        MathUtils.checkNotNull(categoryData);
        if (!allowOneElementData) {
            if (categoryData.size() < 2) {
                throw new DimensionMismatchException(LocalizedFormats.TWO_OR_MORE_CATEGORIES_REQUIRED, categoryData.size(), 2);
            }
            for (SummaryStatistics array : categoryData) {
                if (array.getN() <= 1) {
                    throw new DimensionMismatchException(LocalizedFormats.TWO_OR_MORE_VALUES_IN_CATEGORY_REQUIRED, (int) array.getN(), 2);
                }
            }
        }
        int dfwg = 0;
        double sswg = 0.0d;
        double totsum = 0.0d;
        double totsumsq = 0.0d;
        int totnum = 0;
        for (SummaryStatistics data : categoryData) {
            double sum = data.getSum();
            double sumsq = data.getSumsq();
            double sswg2 = sswg;
            int num = (int) data.getN();
            totnum += num;
            totsum += sum;
            totsumsq += sumsq;
            sswg = sswg2 + (sumsq - ((sum * sum) / ((double) num)));
            dfwg += num - 1;
        }
        double sswg3 = sswg;
        int dfbg = categoryData.size() - 1;
        AnovaStats anovaStats = new AnovaStats(dfbg, dfwg, (((totsumsq - ((totsum * totsum) / ((double) totnum))) - sswg3) / ((double) dfbg)) / (sswg3 / ((double) dfwg)));
        return anovaStats;
    }
}
