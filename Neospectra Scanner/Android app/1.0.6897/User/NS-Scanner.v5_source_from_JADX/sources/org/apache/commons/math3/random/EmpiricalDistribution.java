package org.apache.commons.math3.random;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.ConstantRealDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

public class EmpiricalDistribution extends AbstractRealDistribution {
    public static final int DEFAULT_BIN_COUNT = 1000;
    private static final String FILE_CHARSET = "US-ASCII";
    private static final long serialVersionUID = 5729073523949762654L;
    private final int binCount;
    /* access modifiers changed from: private */
    public final List<SummaryStatistics> binStats;
    private double delta;
    private boolean loaded;
    private double max;
    private double min;
    protected final RandomDataGenerator randomData;
    /* access modifiers changed from: private */
    public SummaryStatistics sampleStats;
    private double[] upperBounds;

    private class ArrayDataAdapter extends DataAdapter {
        private double[] inputArray;

        ArrayDataAdapter(double[] in) throws NullArgumentException {
            super();
            MathUtils.checkNotNull(in);
            this.inputArray = in;
        }

        public void computeStats() throws IOException {
            EmpiricalDistribution.this.sampleStats = new SummaryStatistics();
            for (double addValue : this.inputArray) {
                EmpiricalDistribution.this.sampleStats.addValue(addValue);
            }
        }

        public void computeBinStats() throws IOException {
            for (int i = 0; i < this.inputArray.length; i++) {
                ((SummaryStatistics) EmpiricalDistribution.this.binStats.get(EmpiricalDistribution.this.findBin(this.inputArray[i]))).addValue(this.inputArray[i]);
            }
        }
    }

    private abstract class DataAdapter {
        public abstract void computeBinStats() throws IOException;

        public abstract void computeStats() throws IOException;

        private DataAdapter() {
        }
    }

    private class StreamDataAdapter extends DataAdapter {
        private BufferedReader inputStream;

        StreamDataAdapter(BufferedReader in) {
            super();
            this.inputStream = in;
        }

        public void computeBinStats() throws IOException {
            while (true) {
                String readLine = this.inputStream.readLine();
                String str = readLine;
                if (readLine != null) {
                    double val = Double.parseDouble(str);
                    ((SummaryStatistics) EmpiricalDistribution.this.binStats.get(EmpiricalDistribution.this.findBin(val))).addValue(val);
                } else {
                    this.inputStream.close();
                    this.inputStream = null;
                    return;
                }
            }
        }

        public void computeStats() throws IOException {
            EmpiricalDistribution.this.sampleStats = new SummaryStatistics();
            while (true) {
                String readLine = this.inputStream.readLine();
                String str = readLine;
                if (readLine != null) {
                    EmpiricalDistribution.this.sampleStats.addValue(Double.parseDouble(str));
                } else {
                    this.inputStream.close();
                    this.inputStream = null;
                    return;
                }
            }
        }
    }

    public EmpiricalDistribution() {
        this(1000);
    }

    public EmpiricalDistribution(int binCount2) {
        this(binCount2, new RandomDataGenerator());
    }

    public EmpiricalDistribution(int binCount2, RandomGenerator generator) {
        this(binCount2, new RandomDataGenerator(generator));
    }

    public EmpiricalDistribution(RandomGenerator generator) {
        this(1000, generator);
    }

    @Deprecated
    public EmpiricalDistribution(int binCount2, RandomDataImpl randomData2) {
        this(binCount2, randomData2.getDelegate());
    }

    @Deprecated
    public EmpiricalDistribution(RandomDataImpl randomData2) {
        this(1000, randomData2);
    }

    private EmpiricalDistribution(int binCount2, RandomDataGenerator randomData2) {
        super(randomData2.getRandomGenerator());
        this.sampleStats = null;
        this.max = Double.NEGATIVE_INFINITY;
        this.min = Double.POSITIVE_INFINITY;
        this.delta = 0.0d;
        this.loaded = false;
        this.upperBounds = null;
        if (binCount2 <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(binCount2));
        }
        this.binCount = binCount2;
        this.randomData = randomData2;
        this.binStats = new ArrayList();
    }

    public void load(double[] in) throws NullArgumentException {
        try {
            new ArrayDataAdapter(in).computeStats();
            fillBinStats(new ArrayDataAdapter(in));
            this.loaded = true;
        } catch (IOException e) {
            throw new MathInternalError();
        }
    }

    public void load(URL url) throws IOException, NullArgumentException, ZeroException {
        MathUtils.checkNotNull(url);
        Charset charset = Charset.forName(FILE_CHARSET);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), charset));
        try {
            new StreamDataAdapter(in).computeStats();
            if (this.sampleStats.getN() == 0) {
                throw new ZeroException(LocalizedFormats.URL_CONTAINS_NO_DATA, url);
            }
            in = new BufferedReader(new InputStreamReader(url.openStream(), charset));
            fillBinStats(new StreamDataAdapter(in));
            this.loaded = true;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }

    public void load(File file) throws IOException, NullArgumentException {
        MathUtils.checkNotNull(file);
        Charset charset = Charset.forName(FILE_CHARSET);
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
        try {
            new StreamDataAdapter(in).computeStats();
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
            fillBinStats(new StreamDataAdapter(in));
            this.loaded = true;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }

    private void fillBinStats(DataAdapter da) throws IOException {
        this.min = this.sampleStats.getMin();
        this.max = this.sampleStats.getMax();
        this.delta = (this.max - this.min) / ((double) this.binCount);
        if (!this.binStats.isEmpty()) {
            this.binStats.clear();
        }
        for (int i = 0; i < this.binCount; i++) {
            this.binStats.add(i, new SummaryStatistics());
        }
        da.computeBinStats();
        this.upperBounds = new double[this.binCount];
        this.upperBounds[0] = ((double) ((SummaryStatistics) this.binStats.get(0)).getN()) / ((double) this.sampleStats.getN());
        for (int i2 = 1; i2 < this.binCount - 1; i2++) {
            this.upperBounds[i2] = this.upperBounds[i2 - 1] + (((double) ((SummaryStatistics) this.binStats.get(i2)).getN()) / ((double) this.sampleStats.getN()));
        }
        this.upperBounds[this.binCount - 1] = 1.0d;
    }

    /* access modifiers changed from: private */
    public int findBin(double value) {
        return FastMath.min(FastMath.max(((int) FastMath.ceil((value - this.min) / this.delta)) - 1, 0), this.binCount - 1);
    }

    public double getNextValue() throws MathIllegalStateException {
        if (this.loaded) {
            return sample();
        }
        throw new MathIllegalStateException(LocalizedFormats.DISTRIBUTION_NOT_LOADED, new Object[0]);
    }

    public StatisticalSummary getSampleStats() {
        return this.sampleStats;
    }

    public int getBinCount() {
        return this.binCount;
    }

    public List<SummaryStatistics> getBinStats() {
        return this.binStats;
    }

    public double[] getUpperBounds() {
        double[] binUpperBounds = new double[this.binCount];
        for (int i = 0; i < this.binCount - 1; i++) {
            binUpperBounds[i] = this.min + (this.delta * ((double) (i + 1)));
        }
        binUpperBounds[this.binCount - 1] = this.max;
        return binUpperBounds;
    }

    public double[] getGeneratorUpperBounds() {
        int len = this.upperBounds.length;
        double[] out = new double[len];
        System.arraycopy(this.upperBounds, 0, out, 0, len);
        return out;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void reSeed(long seed) {
        this.randomData.reSeed(seed);
    }

    public double probability(double x) {
        return 0.0d;
    }

    public double density(double x) {
        if (x < this.min || x > this.max) {
            return 0.0d;
        }
        int binIndex = findBin(x);
        return (getKernel((SummaryStatistics) this.binStats.get(binIndex)).density(x) * m47pB(binIndex)) / m46kB(binIndex);
    }

    public double cumulativeProbability(double x) {
        double d = x;
        if (d < this.min) {
            return 0.0d;
        }
        if (d >= this.max) {
            return 1.0d;
        }
        int binIndex = findBin(x);
        double pBminus = pBminus(binIndex);
        double pB = m47pB(binIndex);
        RealDistribution kernel = m45k(x);
        if (!(kernel instanceof ConstantRealDistribution)) {
            return pBminus + (pB * ((kernel.cumulativeProbability(d) - kernel.cumulativeProbability(binIndex == 0 ? this.min : getUpperBounds()[binIndex - 1])) / m46kB(binIndex)));
        } else if (d < kernel.getNumericalMean()) {
            return pBminus;
        } else {
            return pBminus + pB;
        }
    }

    public double inverseCumulativeProbability(double p) throws OutOfRangeException {
        int i;
        int i2 = 0;
        if (p < 0.0d || p > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(1));
        } else if (p == 0.0d) {
            return getSupportLowerBound();
        } else {
            if (p == 1.0d) {
                return getSupportUpperBound();
            }
            while (true) {
                i = i2;
                if (cumBinP(i) >= p) {
                    break;
                }
                i2 = i + 1;
            }
            RealDistribution kernel = getKernel((SummaryStatistics) this.binStats.get(i));
            double kB = m46kB(i);
            double lower = i == 0 ? this.min : getUpperBounds()[i - 1];
            double kBminus = kernel.cumulativeProbability(lower);
            double pB = m47pB(i);
            double pCrit = p - pBminus(i);
            if (pCrit <= 0.0d) {
                return lower;
            }
            return kernel.inverseCumulativeProbability(((pCrit * kB) / pB) + kBminus);
        }
    }

    public double getNumericalMean() {
        return this.sampleStats.getMean();
    }

    public double getNumericalVariance() {
        return this.sampleStats.getVariance();
    }

    public double getSupportLowerBound() {
        return this.min;
    }

    public double getSupportUpperBound() {
        return this.max;
    }

    public boolean isSupportLowerBoundInclusive() {
        return true;
    }

    public boolean isSupportUpperBoundInclusive() {
        return true;
    }

    public boolean isSupportConnected() {
        return true;
    }

    public void reseedRandomGenerator(long seed) {
        this.randomData.reSeed(seed);
    }

    /* renamed from: pB */
    private double m47pB(int i) {
        return i == 0 ? this.upperBounds[0] : this.upperBounds[i] - this.upperBounds[i - 1];
    }

    private double pBminus(int i) {
        if (i == 0) {
            return 0.0d;
        }
        return this.upperBounds[i - 1];
    }

    /* renamed from: kB */
    private double m46kB(int i) {
        double d;
        double d2;
        double[] binBounds = getUpperBounds();
        RealDistribution kernel = getKernel((SummaryStatistics) this.binStats.get(i));
        if (i == 0) {
            d = this.min;
            d2 = binBounds[0];
        } else {
            d = binBounds[i - 1];
            d2 = binBounds[i];
        }
        return kernel.cumulativeProbability(d, d2);
    }

    /* renamed from: k */
    private RealDistribution m45k(double x) {
        return getKernel((SummaryStatistics) this.binStats.get(findBin(x)));
    }

    private double cumBinP(int binIndex) {
        return this.upperBounds[binIndex];
    }

    /* access modifiers changed from: protected */
    public RealDistribution getKernel(SummaryStatistics bStats) {
        if (bStats.getN() == 1 || bStats.getVariance() == 0.0d) {
            return new ConstantRealDistribution(bStats.getMean());
        }
        NormalDistribution normalDistribution = new NormalDistribution(this.randomData.getRandomGenerator(), bStats.getMean(), bStats.getStandardDeviation(), 1.0E-9d);
        return normalDistribution;
    }
}
