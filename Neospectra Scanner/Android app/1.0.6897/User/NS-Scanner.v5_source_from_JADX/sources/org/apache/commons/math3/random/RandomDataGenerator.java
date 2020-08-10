package org.apache.commons.math3.random;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Collection;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.CauchyDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.PascalDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.distribution.WeibullDistribution;
import org.apache.commons.math3.distribution.ZipfDistribution;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NotANumberException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;

public class RandomDataGenerator implements RandomData, Serializable {
    private static final long serialVersionUID = -626730818244969716L;
    private RandomGenerator rand = null;
    private RandomGenerator secRand = null;

    public RandomDataGenerator() {
    }

    public RandomDataGenerator(RandomGenerator rand2) {
        this.rand = rand2;
    }

    public String nextHexString(int len) throws NotStrictlyPositiveException {
        if (len <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.LENGTH, Integer.valueOf(len));
        }
        RandomGenerator ran = getRandomGenerator();
        StringBuilder outBuffer = new StringBuilder();
        byte[] randomBytes = new byte[((len / 2) + 1)];
        ran.nextBytes(randomBytes);
        for (byte valueOf : randomBytes) {
            String hex = Integer.toHexString(Integer.valueOf(valueOf).intValue() + 128);
            if (hex.length() == 1) {
                StringBuilder sb = new StringBuilder();
                sb.append("0");
                sb.append(hex);
                hex = sb.toString();
            }
            outBuffer.append(hex);
        }
        return outBuffer.toString().substring(0, len);
    }

    public int nextInt(int lower, int upper) throws NumberIsTooLargeException {
        return new UniformIntegerDistribution(getRandomGenerator(), lower, upper).sample();
    }

    public long nextLong(long lower, long upper) throws NumberIsTooLargeException {
        if (lower >= upper) {
            throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, Long.valueOf(lower), Long.valueOf(upper), false);
        }
        long max = (upper - lower) + 1;
        if (max <= 0) {
            RandomGenerator rng = getRandomGenerator();
            while (true) {
                long r = rng.nextLong();
                if (r >= lower && r <= upper) {
                    return r;
                }
            }
        } else if (max < 2147483647L) {
            return ((long) getRandomGenerator().nextInt((int) max)) + lower;
        } else {
            return nextLong(getRandomGenerator(), max) + lower;
        }
    }

    private static long nextLong(RandomGenerator rng, long n) throws IllegalArgumentException {
        if (n > 0) {
            char c = 8;
            byte[] byteArray = new byte[8];
            while (true) {
                rng.nextBytes(byteArray);
                long bits = 0;
                byte[] arr$ = byteArray;
                int i$ = 0;
                while (i$ < arr$.length) {
                    bits = (bits << c) | (((long) arr$[i$]) & 255);
                    i$++;
                    byteArray = byteArray;
                    c = 8;
                }
                byte[] byteArray2 = byteArray;
                long bits2 = Long.MAX_VALUE & bits;
                long val = bits2 % n;
                if ((bits2 - val) + (n - 1) >= 0) {
                    return val;
                }
                byteArray = byteArray2;
                c = 8;
            }
        } else {
            RandomGenerator randomGenerator = rng;
            throw new NotStrictlyPositiveException(Long.valueOf(n));
        }
    }

    public String nextSecureHexString(int len) throws NotStrictlyPositiveException {
        if (len <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.LENGTH, Integer.valueOf(len));
        }
        RandomGenerator secRan = getSecRan();
        try {
            MessageDigest alg = MessageDigest.getInstance("SHA-1");
            alg.reset();
            int numIter = (len / 40) + 1;
            StringBuilder outBuffer = new StringBuilder();
            int iter = 1;
            while (true) {
                if (iter >= numIter + 1) {
                    return outBuffer.toString().substring(0, len);
                }
                byte[] randomBytes = new byte[40];
                secRan.nextBytes(randomBytes);
                alg.update(randomBytes);
                byte[] hash = alg.digest();
                for (byte valueOf : hash) {
                    String hex = Integer.toHexString(Integer.valueOf(valueOf).intValue() + 128);
                    if (hex.length() == 1) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("0");
                        sb.append(hex);
                        hex = sb.toString();
                    }
                    outBuffer.append(hex);
                }
                iter++;
            }
        } catch (NoSuchAlgorithmException ex) {
            throw new MathInternalError(ex);
        }
    }

    public int nextSecureInt(int lower, int upper) throws NumberIsTooLargeException {
        return new UniformIntegerDistribution(getSecRan(), lower, upper).sample();
    }

    public long nextSecureLong(long lower, long upper) throws NumberIsTooLargeException {
        if (lower >= upper) {
            throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, Long.valueOf(lower), Long.valueOf(upper), false);
        }
        RandomGenerator rng = getSecRan();
        long max = (upper - lower) + 1;
        if (max <= 0) {
            while (true) {
                long r = rng.nextLong();
                if (r >= lower && r <= upper) {
                    return r;
                }
            }
        } else if (max < 2147483647L) {
            return ((long) rng.nextInt((int) max)) + lower;
        } else {
            return nextLong(rng, max) + lower;
        }
    }

    public long nextPoisson(double mean) throws NotStrictlyPositiveException {
        PoissonDistribution poissonDistribution = new PoissonDistribution(getRandomGenerator(), mean, 1.0E-12d, PoissonDistribution.DEFAULT_MAX_ITERATIONS);
        return (long) poissonDistribution.sample();
    }

    public double nextGaussian(double mu, double sigma) throws NotStrictlyPositiveException {
        if (sigma > 0.0d) {
            return (getRandomGenerator().nextGaussian() * sigma) + mu;
        }
        throw new NotStrictlyPositiveException(LocalizedFormats.STANDARD_DEVIATION, Double.valueOf(sigma));
    }

    public double nextExponential(double mean) throws NotStrictlyPositiveException {
        ExponentialDistribution exponentialDistribution = new ExponentialDistribution(getRandomGenerator(), mean, 1.0E-9d);
        return exponentialDistribution.sample();
    }

    public double nextGamma(double shape, double scale) throws NotStrictlyPositiveException {
        GammaDistribution gammaDistribution = new GammaDistribution(getRandomGenerator(), shape, scale, 1.0E-9d);
        return gammaDistribution.sample();
    }

    public int nextHypergeometric(int populationSize, int numberOfSuccesses, int sampleSize) throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        return new HypergeometricDistribution(getRandomGenerator(), populationSize, numberOfSuccesses, sampleSize).sample();
    }

    public int nextPascal(int r, double p) throws NotStrictlyPositiveException, OutOfRangeException {
        return new PascalDistribution(getRandomGenerator(), r, p).sample();
    }

    public double nextT(double df) throws NotStrictlyPositiveException {
        TDistribution tDistribution = new TDistribution(getRandomGenerator(), df, 1.0E-9d);
        return tDistribution.sample();
    }

    public double nextWeibull(double shape, double scale) throws NotStrictlyPositiveException {
        WeibullDistribution weibullDistribution = new WeibullDistribution(getRandomGenerator(), shape, scale, 1.0E-9d);
        return weibullDistribution.sample();
    }

    public int nextZipf(int numberOfElements, double exponent) throws NotStrictlyPositiveException {
        return new ZipfDistribution(getRandomGenerator(), numberOfElements, exponent).sample();
    }

    public double nextBeta(double alpha, double beta) {
        BetaDistribution betaDistribution = new BetaDistribution(getRandomGenerator(), alpha, beta, 1.0E-9d);
        return betaDistribution.sample();
    }

    public int nextBinomial(int numberOfTrials, double probabilityOfSuccess) {
        return new BinomialDistribution(getRandomGenerator(), numberOfTrials, probabilityOfSuccess).sample();
    }

    public double nextCauchy(double median, double scale) {
        CauchyDistribution cauchyDistribution = new CauchyDistribution(getRandomGenerator(), median, scale, 1.0E-9d);
        return cauchyDistribution.sample();
    }

    public double nextChiSquare(double df) {
        ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistribution(getRandomGenerator(), df, 1.0E-9d);
        return chiSquaredDistribution.sample();
    }

    public double nextF(double numeratorDf, double denominatorDf) throws NotStrictlyPositiveException {
        FDistribution fDistribution = new FDistribution(getRandomGenerator(), numeratorDf, denominatorDf, 1.0E-9d);
        return fDistribution.sample();
    }

    public double nextUniform(double lower, double upper) throws NumberIsTooLargeException, NotFiniteNumberException, NotANumberException {
        return nextUniform(lower, upper, false);
    }

    public double nextUniform(double lower, double upper, boolean lowerInclusive) throws NumberIsTooLargeException, NotFiniteNumberException, NotANumberException {
        if (lower >= upper) {
            throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, Double.valueOf(lower), Double.valueOf(upper), false);
        } else if (Double.isInfinite(lower)) {
            throw new NotFiniteNumberException(LocalizedFormats.INFINITE_BOUND, Double.valueOf(lower), new Object[0]);
        } else if (Double.isInfinite(upper)) {
            throw new NotFiniteNumberException(LocalizedFormats.INFINITE_BOUND, Double.valueOf(upper), new Object[0]);
        } else if (Double.isNaN(lower) || Double.isNaN(upper)) {
            throw new NotANumberException();
        } else {
            RandomGenerator generator = getRandomGenerator();
            double u = generator.nextDouble();
            while (!lowerInclusive && u <= 0.0d) {
                u = generator.nextDouble();
            }
            return (u * upper) + ((1.0d - u) * lower);
        }
    }

    public int[] nextPermutation(int n, int k) throws NumberIsTooLargeException, NotStrictlyPositiveException {
        if (k > n) {
            throw new NumberIsTooLargeException(LocalizedFormats.PERMUTATION_EXCEEDS_N, Integer.valueOf(k), Integer.valueOf(n), true);
        } else if (k <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.PERMUTATION_SIZE, Integer.valueOf(k));
        } else {
            int[] index = MathArrays.natural(n);
            MathArrays.shuffle(index, getRandomGenerator());
            return MathArrays.copyOf(index, k);
        }
    }

    public Object[] nextSample(Collection<?> c, int k) throws NumberIsTooLargeException, NotStrictlyPositiveException {
        int len = c.size();
        if (k > len) {
            throw new NumberIsTooLargeException(LocalizedFormats.SAMPLE_SIZE_EXCEEDS_COLLECTION_SIZE, Integer.valueOf(k), Integer.valueOf(len), true);
        } else if (k <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, Integer.valueOf(k));
        } else {
            Object[] objects = c.toArray();
            int[] index = nextPermutation(len, k);
            Object[] result = new Object[k];
            for (int i = 0; i < k; i++) {
                result[i] = objects[index[i]];
            }
            return result;
        }
    }

    public void reSeed(long seed) {
        getRandomGenerator().setSeed(seed);
    }

    public void reSeedSecure() {
        getSecRan().setSeed(System.currentTimeMillis());
    }

    public void reSeedSecure(long seed) {
        getSecRan().setSeed(seed);
    }

    public void reSeed() {
        getRandomGenerator().setSeed(System.currentTimeMillis() + ((long) System.identityHashCode(this)));
    }

    public void setSecureAlgorithm(String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
        this.secRand = RandomGeneratorFactory.createRandomGenerator(SecureRandom.getInstance(algorithm, provider));
    }

    public RandomGenerator getRandomGenerator() {
        if (this.rand == null) {
            initRan();
        }
        return this.rand;
    }

    private void initRan() {
        this.rand = new Well19937c(System.currentTimeMillis() + ((long) System.identityHashCode(this)));
    }

    private RandomGenerator getSecRan() {
        if (this.secRand == null) {
            this.secRand = RandomGeneratorFactory.createRandomGenerator(new SecureRandom());
            this.secRand.setSeed(System.currentTimeMillis() + ((long) System.identityHashCode(this)));
        }
        return this.secRand;
    }
}
