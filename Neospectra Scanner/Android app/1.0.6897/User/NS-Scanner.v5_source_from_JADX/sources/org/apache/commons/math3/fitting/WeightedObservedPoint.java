package org.apache.commons.math3.fitting;

import java.io.Serializable;

public class WeightedObservedPoint implements Serializable {
    private static final long serialVersionUID = 5306874947404636157L;
    private final double weight;

    /* renamed from: x */
    private final double f568x;

    /* renamed from: y */
    private final double f569y;

    public WeightedObservedPoint(double weight2, double x, double y) {
        this.weight = weight2;
        this.f568x = x;
        this.f569y = y;
    }

    public double getWeight() {
        return this.weight;
    }

    public double getX() {
        return this.f568x;
    }

    public double getY() {
        return this.f569y;
    }
}
