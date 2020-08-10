package org.apache.commons.math3.p006ml.clustering;

import org.apache.commons.math3.p006ml.clustering.Clusterable;

/* renamed from: org.apache.commons.math3.ml.clustering.CentroidCluster */
public class CentroidCluster<T extends Clusterable> extends Cluster<T> {
    private static final long serialVersionUID = -3075288519071812288L;
    private final Clusterable center;

    public CentroidCluster(Clusterable center2) {
        this.center = center2;
    }

    public Clusterable getCenter() {
        return this.center;
    }
}
