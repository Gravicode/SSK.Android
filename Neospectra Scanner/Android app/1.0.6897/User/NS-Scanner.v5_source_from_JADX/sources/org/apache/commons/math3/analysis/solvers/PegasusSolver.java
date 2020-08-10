package org.apache.commons.math3.analysis.solvers;

public class PegasusSolver extends BaseSecantSolver {
    public PegasusSolver() {
        super(1.0E-6d, Method.PEGASUS);
    }

    public PegasusSolver(double absoluteAccuracy) {
        super(absoluteAccuracy, Method.PEGASUS);
    }

    public PegasusSolver(double relativeAccuracy, double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, Method.PEGASUS);
    }

    public PegasusSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, Method.PEGASUS);
    }
}
