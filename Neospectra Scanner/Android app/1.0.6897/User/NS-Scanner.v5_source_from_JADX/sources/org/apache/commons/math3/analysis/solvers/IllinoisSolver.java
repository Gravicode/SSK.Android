package org.apache.commons.math3.analysis.solvers;

public class IllinoisSolver extends BaseSecantSolver {
    public IllinoisSolver() {
        super(1.0E-6d, Method.ILLINOIS);
    }

    public IllinoisSolver(double absoluteAccuracy) {
        super(absoluteAccuracy, Method.ILLINOIS);
    }

    public IllinoisSolver(double relativeAccuracy, double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, Method.ILLINOIS);
    }

    public IllinoisSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, Method.PEGASUS);
    }
}
