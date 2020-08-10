package org.apache.commons.math3.ode.nonstiff;

import java.util.Arrays;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrixPreservingVisitor;
import org.apache.commons.math3.ode.EquationsMapper;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.sampling.NordsieckStepInterpolator;
import org.apache.commons.math3.util.FastMath;

public class AdamsMoultonIntegrator extends AdamsIntegrator {
    private static final String METHOD_NAME = "Adams-Moulton";

    private class Corrector implements RealMatrixPreservingVisitor {
        private final double[] after;
        private final double[] before;
        private final double[] previous;
        private final double[] scaled;

        Corrector(double[] previous2, double[] scaled2, double[] state) {
            this.previous = previous2;
            this.scaled = scaled2;
            this.after = state;
            this.before = (double[]) state.clone();
        }

        public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
            Arrays.fill(this.after, 0.0d);
        }

        public void visit(int row, int column, double value) {
            if ((row & 1) == 0) {
                double[] dArr = this.after;
                dArr[column] = dArr[column] - value;
                return;
            }
            double[] dArr2 = this.after;
            dArr2[column] = dArr2[column] + value;
        }

        public double end() {
            double error = 0.0d;
            for (int i = 0; i < this.after.length; i++) {
                double[] dArr = this.after;
                dArr[i] = dArr[i] + this.previous[i] + this.scaled[i];
                if (i < AdamsMoultonIntegrator.this.mainSetDimension) {
                    double yScale = FastMath.max(FastMath.abs(this.previous[i]), FastMath.abs(this.after[i]));
                    double ratio = (this.after[i] - this.before[i]) / (AdamsMoultonIntegrator.this.vecAbsoluteTolerance == null ? AdamsMoultonIntegrator.this.scalAbsoluteTolerance + (AdamsMoultonIntegrator.this.scalRelativeTolerance * yScale) : AdamsMoultonIntegrator.this.vecAbsoluteTolerance[i] + (AdamsMoultonIntegrator.this.vecRelativeTolerance[i] * yScale));
                    error += ratio * ratio;
                }
            }
            return FastMath.sqrt(error / ((double) AdamsMoultonIntegrator.this.mainSetDimension));
        }
    }

    public AdamsMoultonIntegrator(int nSteps, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) throws NumberIsTooSmallException {
        super(METHOD_NAME, nSteps, nSteps + 1, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    public AdamsMoultonIntegrator(int nSteps, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) throws IllegalArgumentException {
        super(METHOD_NAME, nSteps, nSteps + 1, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    }

    public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        boolean forward;
        double hNew;
        boolean forward2;
        double error;
        ExpandableStatefulODE expandableStatefulODE = equations;
        sanityChecks(equations, t);
        setEquations(equations);
        boolean forward3 = t > equations.getTime();
        double[] y0 = equations.getCompleteState();
        double[] y = (double[]) y0.clone();
        double[] yDot = new double[y.length];
        double[] yTmp = new double[y.length];
        double[] predictedScaled = new double[y.length];
        Array2DRowRealMatrix nordsieckTmp = null;
        NordsieckStepInterpolator interpolator = new NordsieckStepInterpolator();
        interpolator.reinitialize(y, forward3, equations.getPrimaryMapper(), equations.getSecondaryMappers());
        NordsieckStepInterpolator interpolator2 = interpolator;
        double[] yTmp2 = yTmp;
        double[] predictedScaled2 = predictedScaled;
        double d = t;
        initIntegration(equations.getTime(), y0, d);
        start(equations.getTime(), y, d);
        interpolator2.reinitialize(this.stepStart, this.stepSize, this.scaled, this.nordsieck);
        NordsieckStepInterpolator interpolator3 = interpolator2;
        interpolator3.storeTime(this.stepStart);
        double hNew2 = this.stepSize;
        interpolator3.rescale(hNew2);
        this.isLastStep = false;
        while (true) {
            double error2 = 10.0d;
            Array2DRowRealMatrix nordsieckTmp2 = nordsieckTmp;
            while (error2 >= 1.0d) {
                this.stepSize = hNew;
                boolean forward4 = forward;
                double hNew3 = hNew;
                double stepEnd = this.stepStart + this.stepSize;
                interpolator3.setInterpolatedTime(stepEnd);
                ExpandableStatefulODE expandable = getExpandable();
                EquationsMapper primary = expandable.getPrimaryMapper();
                double d2 = error2;
                primary.insertEquationData(interpolator3.getInterpolatedState(), yTmp2);
                EquationsMapper[] arr$ = expandable.getSecondaryMappers();
                ExpandableStatefulODE expandableStatefulODE2 = expandable;
                int len$ = arr$.length;
                EquationsMapper equationsMapper = primary;
                int index = 0;
                int i$ = 0;
                while (i$ < len$) {
                    int len$2 = len$;
                    EquationsMapper[] arr$2 = arr$;
                    arr$[i$].insertEquationData(interpolator3.getInterpolatedSecondaryState(index), yTmp2);
                    index++;
                    i$++;
                    len$ = len$2;
                    arr$ = arr$2;
                }
                computeDerivatives(stepEnd, yTmp2, yDot);
                int j = 0;
                while (j < y0.length) {
                    double stepEnd2 = stepEnd;
                    predictedScaled2[j] = this.stepSize * yDot[j];
                    j++;
                    stepEnd = stepEnd2;
                }
                double[] predictedScaled3 = predictedScaled2;
                nordsieckTmp2 = updateHighOrderDerivativesPhase1(this.nordsieck);
                updateHighOrderDerivativesPhase2(this.scaled, predictedScaled3, nordsieckTmp2);
                double error3 = nordsieckTmp2.walkInOptimizedOrder((RealMatrixPreservingVisitor) new Corrector(y, predictedScaled3, yTmp2));
                if (error3 >= 1.0d) {
                    int i = index;
                    error = error3;
                    forward = forward4;
                    hNew = filterStep(this.stepSize * computeStepGrowShrinkFactor(error3), forward, false);
                    interpolator3.rescale(hNew);
                } else {
                    error = error3;
                    forward = forward4;
                    hNew = hNew3;
                }
                predictedScaled2 = predictedScaled3;
                error2 = error;
            }
            double hNew4 = hNew;
            double error4 = error2;
            double[] predictedScaled4 = predictedScaled2;
            double stepEnd3 = this.stepSize + this.stepStart;
            computeDerivatives(stepEnd3, yTmp2, yDot);
            double[] correctedScaled = new double[y0.length];
            int j2 = 0;
            while (j2 < y0.length) {
                boolean forward5 = forward;
                double[] y02 = y0;
                correctedScaled[j2] = this.stepSize * yDot[j2];
                j2++;
                y0 = y02;
                forward = forward5;
                ExpandableStatefulODE expandableStatefulODE3 = equations;
            }
            boolean forward6 = forward;
            double[] y03 = y0;
            updateHighOrderDerivativesPhase2(predictedScaled4, correctedScaled, nordsieckTmp2);
            System.arraycopy(yTmp2, 0, y, 0, y.length);
            interpolator3.reinitialize(stepEnd3, this.stepSize, correctedScaled, nordsieckTmp2);
            interpolator3.storeTime(this.stepStart);
            interpolator3.shift();
            interpolator3.storeTime(stepEnd3);
            double[] correctedScaled2 = correctedScaled;
            double d3 = stepEnd3;
            double[] yTmp3 = yTmp2;
            double[] predictedScaled5 = predictedScaled4;
            double error5 = error4;
            double hNew5 = hNew4;
            NordsieckStepInterpolator interpolator4 = interpolator3;
            Array2DRowRealMatrix nordsieckTmp3 = nordsieckTmp2;
            this.stepStart = acceptStep(interpolator3, y, yDot, t);
            this.scaled = correctedScaled2;
            this.nordsieck = nordsieckTmp3;
            if (!this.isLastStep) {
                interpolator4.storeTime(this.stepStart);
                if (this.resetOccurred) {
                    start(this.stepStart, y, t);
                    interpolator4.reinitialize(this.stepStart, this.stepSize, this.scaled, this.nordsieck);
                }
                double factor = computeStepGrowShrinkFactor(error5);
                double scaledH = this.stepSize * factor;
                double nextT = this.stepStart + scaledH;
                double d4 = factor;
                boolean forward7 = forward6;
                boolean nextIsLast = !forward6 ? nextT <= t : nextT >= t;
                double hNew6 = filterStep(scaledH, forward7, nextIsLast);
                boolean z = nextIsLast;
                double d5 = scaledH;
                double filteredNextT = this.stepStart + hNew6;
                if (!forward7 ? filteredNextT <= t : filteredNextT >= t) {
                    forward2 = forward7;
                    double d6 = filteredNextT;
                    hNew2 = t - this.stepStart;
                } else {
                    forward2 = forward7;
                    double d7 = filteredNextT;
                    hNew2 = hNew6;
                }
                interpolator4.rescale(hNew2);
            } else {
                forward2 = forward6;
                hNew2 = hNew5;
            }
            if (this.isLastStep) {
                ExpandableStatefulODE expandableStatefulODE4 = equations;
                expandableStatefulODE4.setTime(this.stepStart);
                expandableStatefulODE4.setCompleteState(y);
                resetInternalState();
                return;
            }
            interpolator3 = interpolator4;
            nordsieckTmp = nordsieckTmp3;
            y0 = y03;
            yTmp2 = yTmp3;
            predictedScaled2 = predictedScaled5;
            forward3 = forward2;
            ExpandableStatefulODE expandableStatefulODE5 = equations;
        }
    }
}
