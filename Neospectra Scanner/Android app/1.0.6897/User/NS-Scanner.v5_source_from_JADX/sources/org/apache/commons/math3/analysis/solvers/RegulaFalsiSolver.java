package org.apache.commons.math3.analysis.solvers;

public class RegulaFalsiSolver extends BaseSecantSolver {
    public RegulaFalsiSolver() {
        super(1.0E-6d, Method.REGULA_FALSI);
    }

    public RegulaFalsiSolver(double absoluteAccuracy) {
        super(absoluteAccuracy, Method.REGULA_FALSI);
    }

    public RegulaFalsiSolver(double relativeAccuracy, double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, Method.REGULA_FALSI);
    }

    public RegulaFalsiSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, Method.REGULA_FALSI);
    }
}
