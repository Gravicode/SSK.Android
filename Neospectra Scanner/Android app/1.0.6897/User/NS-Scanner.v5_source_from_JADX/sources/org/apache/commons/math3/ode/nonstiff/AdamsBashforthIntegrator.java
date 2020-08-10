package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.ode.EquationsMapper;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.sampling.NordsieckStepInterpolator;
import org.apache.commons.math3.util.FastMath;

public class AdamsBashforthIntegrator extends AdamsIntegrator {
    private static final String METHOD_NAME = "Adams-Bashforth";

    public AdamsBashforthIntegrator(int nSteps, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) throws NumberIsTooSmallException {
        super(METHOD_NAME, nSteps, nSteps, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    public AdamsBashforthIntegrator(int nSteps, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) throws IllegalArgumentException {
        super(METHOD_NAME, nSteps, nSteps, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    }

    private double errorEstimation(double[] previousState, double[] predictedState, double[] predictedScaled, RealMatrix predictedNordsieck) {
        double error = 0.0d;
        for (int i = 0; i < this.mainSetDimension; i++) {
            double yScale = FastMath.abs(predictedState[i]);
            double tol = this.vecAbsoluteTolerance == null ? this.scalAbsoluteTolerance + (this.scalRelativeTolerance * yScale) : this.vecAbsoluteTolerance[i] + (this.vecRelativeTolerance[i] * yScale);
            double variation = 0.0d;
            int sign = predictedNordsieck.getRowDimension() % 2 == 0 ? -1 : 1;
            int k = predictedNordsieck.getRowDimension() - 1;
            while (true) {
                int k2 = k;
                if (k2 < 0) {
                    break;
                }
                variation += ((double) sign) * predictedNordsieck.getEntry(k2, i);
                sign = -sign;
                k = k2 - 1;
            }
            RealMatrix realMatrix = predictedNordsieck;
            double ratio = ((predictedState[i] - previousState[i]) + (variation - predictedScaled[i])) / tol;
            error += ratio * ratio;
        }
        RealMatrix realMatrix2 = predictedNordsieck;
        return FastMath.sqrt(error / ((double) this.mainSetDimension));
    }

    public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        double error;
        NordsieckStepInterpolator interpolator;
        NordsieckStepInterpolator interpolator2;
        double error2;
        ExpandableStatefulODE expandableStatefulODE = equations;
        sanityChecks(equations, t);
        setEquations(equations);
        boolean forward = t > equations.getTime();
        double[] y = equations.getCompleteState();
        double[] yDot = new double[y.length];
        NordsieckStepInterpolator interpolator3 = new NordsieckStepInterpolator();
        interpolator3.reinitialize(y, forward, equations.getPrimaryMapper(), equations.getSecondaryMappers());
        double[] dArr = y;
        double d = t;
        initIntegration(equations.getTime(), dArr, d);
        start(equations.getTime(), dArr, d);
        NordsieckStepInterpolator interpolator4 = interpolator3;
        interpolator3.reinitialize(this.stepStart, this.stepSize, this.scaled, this.nordsieck);
        NordsieckStepInterpolator interpolator5 = interpolator4;
        interpolator5.storeTime(this.stepStart);
        double filteredNextT = this.stepSize;
        interpolator5.rescale(filteredNextT);
        this.isLastStep = false;
        while (true) {
            interpolator5.shift();
            double[] predictedY = new double[y.length];
            double[] predictedScaled = new double[y.length];
            double error3 = 10.0d;
            double d2 = filteredNextT;
            Array2DRowRealMatrix predictedNordsieck = null;
            double hNew = d2;
            while (true) {
                error = error3;
                if (error < 1.0d) {
                    break;
                }
                double d3 = error;
                double stepEnd = this.stepStart + hNew;
                interpolator5.storeTime(stepEnd);
                ExpandableStatefulODE expandable = getExpandable();
                EquationsMapper primary = expandable.getPrimaryMapper();
                primary.insertEquationData(interpolator5.getInterpolatedState(), predictedY);
                EquationsMapper[] arr$ = expandable.getSecondaryMappers();
                EquationsMapper equationsMapper = primary;
                int len$ = arr$.length;
                ExpandableStatefulODE expandableStatefulODE2 = expandable;
                int index = 0;
                int i$ = 0;
                while (i$ < len$) {
                    int len$2 = len$;
                    EquationsMapper[] arr$2 = arr$;
                    arr$[i$].insertEquationData(interpolator5.getInterpolatedSecondaryState(index), predictedY);
                    index++;
                    i$++;
                    len$ = len$2;
                    arr$ = arr$2;
                }
                computeDerivatives(stepEnd, predictedY, yDot);
                for (int j = 0; j < predictedScaled.length; j++) {
                    predictedScaled[j] = yDot[j] * hNew;
                }
                predictedNordsieck = updateHighOrderDerivativesPhase1(this.nordsieck);
                updateHighOrderDerivativesPhase2(this.scaled, predictedScaled, predictedNordsieck);
                double error4 = errorEstimation(y, predictedY, predictedScaled, predictedNordsieck);
                if (error4 >= 1.0d) {
                    error2 = error4;
                    hNew = filterStep(hNew * computeStepGrowShrinkFactor(error4), forward, false);
                    interpolator5.rescale(hNew);
                } else {
                    error2 = error4;
                }
                error3 = error2;
                ExpandableStatefulODE expandableStatefulODE3 = equations;
            }
            double error5 = error;
            this.stepSize = hNew;
            double stepEnd2 = this.stepStart + this.stepSize;
            NordsieckStepInterpolator interpolator6 = interpolator5;
            double d4 = stepEnd2;
            interpolator6.reinitialize(d4, this.stepSize, predictedScaled, predictedNordsieck);
            interpolator5.storeTime(stepEnd2);
            System.arraycopy(predictedY, 0, y, 0, y.length);
            Array2DRowRealMatrix predictedNordsieck2 = predictedNordsieck;
            double hNew2 = hNew;
            double[] predictedScaled2 = predictedScaled;
            double[] dArr2 = predictedY;
            this.stepStart = acceptStep(interpolator5, y, yDot, t);
            this.scaled = predictedScaled2;
            this.nordsieck = predictedNordsieck2;
            NordsieckStepInterpolator interpolator7 = interpolator6;
            interpolator6.reinitialize(d4, this.stepSize, this.scaled, this.nordsieck);
            if (!this.isLastStep) {
                interpolator7.storeTime(this.stepStart);
                if (this.resetOccurred) {
                    NordsieckStepInterpolator interpolator8 = interpolator7;
                    start(this.stepStart, y, t);
                    interpolator2 = interpolator8;
                    interpolator8.reinitialize(this.stepStart, this.stepSize, this.scaled, this.nordsieck);
                } else {
                    interpolator2 = interpolator7;
                }
                double error6 = error5;
                double factor = computeStepGrowShrinkFactor(error6);
                double scaledH = this.stepSize * factor;
                double[] dArr3 = predictedScaled2;
                double nextT = this.stepStart + scaledH;
                double hNew3 = filterStep(scaledH, forward, !forward ? nextT <= t : nextT >= t);
                double d5 = error6;
                double filteredNextT2 = this.stepStart + hNew3;
                if (!forward ? filteredNextT2 <= t : filteredNextT2 >= t) {
                    double d6 = filteredNextT2;
                    filteredNextT = t - this.stepStart;
                } else {
                    filteredNextT = hNew3;
                }
                double d7 = factor;
                interpolator = interpolator2;
                interpolator.rescale(filteredNextT);
            } else {
                interpolator = interpolator7;
                filteredNextT = hNew2;
            }
            if (this.isLastStep) {
                ExpandableStatefulODE expandableStatefulODE4 = equations;
                expandableStatefulODE4.setTime(this.stepStart);
                expandableStatefulODE4.setCompleteState(y);
                resetInternalState();
                return;
            }
            interpolator5 = interpolator;
            ExpandableStatefulODE expandableStatefulODE5 = equations;
        }
    }
}
