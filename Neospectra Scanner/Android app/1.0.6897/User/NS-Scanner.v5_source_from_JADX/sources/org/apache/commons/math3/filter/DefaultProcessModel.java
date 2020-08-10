package org.apache.commons.math3.filter;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class DefaultProcessModel implements ProcessModel {
    private RealMatrix controlMatrix;
    private RealMatrix initialErrorCovMatrix;
    private RealVector initialStateEstimateVector;
    private RealMatrix processNoiseCovMatrix;
    private RealMatrix stateTransitionMatrix;

    public DefaultProcessModel(double[][] stateTransition, double[][] control, double[][] processNoise, double[] initialStateEstimate, double[][] initialErrorCovariance) throws NullArgumentException, NoDataException, DimensionMismatchException {
        this((RealMatrix) new Array2DRowRealMatrix(stateTransition), (RealMatrix) new Array2DRowRealMatrix(control), (RealMatrix) new Array2DRowRealMatrix(processNoise), (RealVector) new ArrayRealVector(initialStateEstimate), (RealMatrix) new Array2DRowRealMatrix(initialErrorCovariance));
    }

    public DefaultProcessModel(double[][] stateTransition, double[][] control, double[][] processNoise) throws NullArgumentException, NoDataException, DimensionMismatchException {
        this((RealMatrix) new Array2DRowRealMatrix(stateTransition), (RealMatrix) new Array2DRowRealMatrix(control), (RealMatrix) new Array2DRowRealMatrix(processNoise), (RealVector) null, (RealMatrix) null);
    }

    public DefaultProcessModel(RealMatrix stateTransition, RealMatrix control, RealMatrix processNoise, RealVector initialStateEstimate, RealMatrix initialErrorCovariance) {
        this.stateTransitionMatrix = stateTransition;
        this.controlMatrix = control;
        this.processNoiseCovMatrix = processNoise;
        this.initialStateEstimateVector = initialStateEstimate;
        this.initialErrorCovMatrix = initialErrorCovariance;
    }

    public RealMatrix getStateTransitionMatrix() {
        return this.stateTransitionMatrix;
    }

    public RealMatrix getControlMatrix() {
        return this.controlMatrix;
    }

    public RealMatrix getProcessNoise() {
        return this.processNoiseCovMatrix;
    }

    public RealVector getInitialStateEstimate() {
        return this.initialStateEstimateVector;
    }

    public RealMatrix getInitialErrorCovariance() {
        return this.initialErrorCovMatrix;
    }
}
