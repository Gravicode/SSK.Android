package org.apache.commons.math3.optimization.fitting;

import java.io.Serializable;

@Deprecated
public class WeightedObservedPoint implements Serializable {
    private static final long serialVersionUID = 5306874947404636157L;
    private final double weight;

    /* renamed from: x */
    private final double f730x;

    /* renamed from: y */
    private final double f731y;

    public WeightedObservedPoint(double weight2, double x, double y) {
        this.weight = weight2;
        this.f730x = x;
        this.f731y = y;
    }

    public double getWeight() {
        return this.weight;
    }

    public double getX() {
        return this.f730x;
    }

    public double getY() {
        return this.f731y;
    }
}
