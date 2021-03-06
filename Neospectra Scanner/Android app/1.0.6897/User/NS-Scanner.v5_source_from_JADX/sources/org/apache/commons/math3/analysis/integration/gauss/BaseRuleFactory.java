package org.apache.commons.math3.analysis.integration.gauss;

import java.lang.Number;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Pair;

public abstract class BaseRuleFactory<T extends Number> {
    private final Map<Integer, Pair<T[], T[]>> pointsAndWeights = new TreeMap();
    private final Map<Integer, Pair<double[], double[]>> pointsAndWeightsDouble = new TreeMap();

    /* access modifiers changed from: protected */
    public abstract Pair<T[], T[]> computeRule(int i) throws DimensionMismatchException;

    public Pair<double[], double[]> getRule(int numberOfPoints) throws NotStrictlyPositiveException, DimensionMismatchException {
        if (numberOfPoints <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_POINTS, Integer.valueOf(numberOfPoints));
        }
        Pair<double[], double[]> cached = (Pair) this.pointsAndWeightsDouble.get(Integer.valueOf(numberOfPoints));
        if (cached == null) {
            cached = convertToDouble(getRuleInternal(numberOfPoints));
            this.pointsAndWeightsDouble.put(Integer.valueOf(numberOfPoints), cached);
        }
        return new Pair<>(((double[]) cached.getFirst()).clone(), ((double[]) cached.getSecond()).clone());
    }

    /* access modifiers changed from: protected */
    public synchronized Pair<T[], T[]> getRuleInternal(int numberOfPoints) throws DimensionMismatchException {
        Pair<T[], T[]> rule = (Pair) this.pointsAndWeights.get(Integer.valueOf(numberOfPoints));
        if (rule != null) {
            return rule;
        }
        addRule(computeRule(numberOfPoints));
        return getRuleInternal(numberOfPoints);
    }

    /* access modifiers changed from: protected */
    public void addRule(Pair<T[], T[]> rule) throws DimensionMismatchException {
        if (((Number[]) rule.getFirst()).length != ((Number[]) rule.getSecond()).length) {
            throw new DimensionMismatchException(((Number[]) rule.getFirst()).length, ((Number[]) rule.getSecond()).length);
        }
        this.pointsAndWeights.put(Integer.valueOf(((Number[]) rule.getFirst()).length), rule);
    }

    private static <T extends Number> Pair<double[], double[]> convertToDouble(Pair<T[], T[]> rule) {
        T[] pT = (Number[]) rule.getFirst();
        T[] wT = (Number[]) rule.getSecond();
        int len = pT.length;
        double[] pD = new double[len];
        double[] wD = new double[len];
        for (int i = 0; i < len; i++) {
            pD[i] = pT[i].doubleValue();
            wD[i] = wT[i].doubleValue();
        }
        return new Pair<>(pD, wD);
    }
}
