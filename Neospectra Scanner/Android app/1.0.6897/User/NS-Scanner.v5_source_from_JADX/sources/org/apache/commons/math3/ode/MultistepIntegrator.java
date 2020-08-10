package org.apache.commons.math3.ode;

import java.lang.reflect.Array;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.ode.nonstiff.AdaptiveStepsizeIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.FastMath;

public abstract class MultistepIntegrator extends AdaptiveStepsizeIntegrator {
    private double exp;
    private double maxGrowth;
    private double minReduction;
    private final int nSteps;
    protected Array2DRowRealMatrix nordsieck;
    private double safety;
    protected double[] scaled;
    private FirstOrderIntegrator starter;

    private static class InitializationCompletedMarkerException extends RuntimeException {
        private static final long serialVersionUID = -1914085471038046418L;

        InitializationCompletedMarkerException() {
            super(null);
        }
    }

    private class NordsieckInitializer implements StepHandler {
        private int count = 0;

        /* renamed from: t */
        private final double[] f645t;

        /* renamed from: y */
        private final double[][] f646y;
        private final double[][] yDot;

        NordsieckInitializer(int nbStartPoints, int n) {
            this.f645t = new double[nbStartPoints];
            this.f646y = (double[][]) Array.newInstance(double.class, new int[]{nbStartPoints, n});
            this.yDot = (double[][]) Array.newInstance(double.class, new int[]{nbStartPoints, n});
        }

        public void handleStep(StepInterpolator interpolator, boolean isLast) throws MaxCountExceededException {
            EquationsMapper[] arr$;
            EquationsMapper[] arr$2;
            StepInterpolator stepInterpolator = interpolator;
            double prev = interpolator.getPreviousTime();
            double curr = interpolator.getCurrentTime();
            if (this.count == 0) {
                stepInterpolator.setInterpolatedTime(prev);
                this.f645t[0] = prev;
                ExpandableStatefulODE expandable = MultistepIntegrator.this.getExpandable();
                EquationsMapper primary = expandable.getPrimaryMapper();
                primary.insertEquationData(interpolator.getInterpolatedState(), this.f646y[this.count]);
                primary.insertEquationData(interpolator.getInterpolatedDerivatives(), this.yDot[this.count]);
                int index = 0;
                for (EquationsMapper secondary : expandable.getSecondaryMappers()) {
                    secondary.insertEquationData(stepInterpolator.getInterpolatedSecondaryState(index), this.f646y[this.count]);
                    secondary.insertEquationData(stepInterpolator.getInterpolatedSecondaryDerivatives(index), this.yDot[this.count]);
                    index++;
                }
            }
            this.count++;
            stepInterpolator.setInterpolatedTime(curr);
            this.f645t[this.count] = curr;
            ExpandableStatefulODE expandable2 = MultistepIntegrator.this.getExpandable();
            EquationsMapper primary2 = expandable2.getPrimaryMapper();
            primary2.insertEquationData(interpolator.getInterpolatedState(), this.f646y[this.count]);
            primary2.insertEquationData(interpolator.getInterpolatedDerivatives(), this.yDot[this.count]);
            int index2 = 0;
            for (EquationsMapper secondary2 : expandable2.getSecondaryMappers()) {
                secondary2.insertEquationData(stepInterpolator.getInterpolatedSecondaryState(index2), this.f646y[this.count]);
                secondary2.insertEquationData(stepInterpolator.getInterpolatedSecondaryDerivatives(index2), this.yDot[this.count]);
                index2++;
            }
            if (this.count == this.f645t.length - 1) {
                int j = 0;
                MultistepIntegrator.this.stepStart = this.f645t[0];
                MultistepIntegrator.this.stepSize = (this.f645t[this.f645t.length - 1] - this.f645t[0]) / ((double) (this.f645t.length - 1));
                MultistepIntegrator.this.scaled = (double[]) this.yDot[0].clone();
                while (true) {
                    int j2 = j;
                    if (j2 < MultistepIntegrator.this.scaled.length) {
                        double[] dArr = MultistepIntegrator.this.scaled;
                        dArr[j2] = dArr[j2] * MultistepIntegrator.this.stepSize;
                        j = j2 + 1;
                    } else {
                        MultistepIntegrator.this.nordsieck = MultistepIntegrator.this.initializeHighOrderDerivatives(MultistepIntegrator.this.stepSize, this.f645t, this.f646y, this.yDot);
                        throw new InitializationCompletedMarkerException();
                    }
                }
            }
        }

        public void init(double t0, double[] y0, double time) {
        }
    }

    @Deprecated
    public interface NordsieckTransformer {
        Array2DRowRealMatrix initializeHighOrderDerivatives(double d, double[] dArr, double[][] dArr2, double[][] dArr3);
    }

    /* access modifiers changed from: protected */
    public abstract Array2DRowRealMatrix initializeHighOrderDerivatives(double d, double[] dArr, double[][] dArr2, double[][] dArr3);

    protected MultistepIntegrator(String name, int nSteps2, int order, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) throws NumberIsTooSmallException {
        int i = nSteps2;
        super(name, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        if (i < 2) {
            throw new NumberIsTooSmallException(LocalizedFormats.INTEGRATION_METHOD_NEEDS_AT_LEAST_TWO_PREVIOUS_POINTS, Integer.valueOf(nSteps2), Integer.valueOf(2), true);
        }
        DormandPrince853Integrator dormandPrince853Integrator = new DormandPrince853Integrator(minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        this.starter = dormandPrince853Integrator;
        this.nSteps = i;
        this.exp = -1.0d / ((double) order);
        setSafety(0.9d);
        setMinReduction(0.2d);
        setMaxGrowth(FastMath.pow(2.0d, -this.exp));
    }

    protected MultistepIntegrator(String name, int nSteps2, int order, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(name, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        DormandPrince853Integrator dormandPrince853Integrator = new DormandPrince853Integrator(minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        this.starter = dormandPrince853Integrator;
        this.nSteps = nSteps2;
        this.exp = -1.0d / ((double) order);
        setSafety(0.9d);
        setMinReduction(0.2d);
        setMaxGrowth(FastMath.pow(2.0d, -this.exp));
    }

    public ODEIntegrator getStarterIntegrator() {
        return this.starter;
    }

    public void setStarterIntegrator(FirstOrderIntegrator starterIntegrator) {
        this.starter = starterIntegrator;
    }

    /* access modifiers changed from: protected */
    public void start(double t0, double[] y0, double t) throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        this.starter.clearEventHandlers();
        this.starter.clearStepHandlers();
        this.starter.addStepHandler(new NordsieckInitializer((this.nSteps + 3) / 2, y0.length));
        try {
            if (this.starter instanceof AbstractIntegrator) {
                ((AbstractIntegrator) this.starter).integrate(getExpandable(), t);
            } else {
                this.starter.integrate(new FirstOrderDifferentialEquations() {
                    public int getDimension() {
                        return MultistepIntegrator.this.getExpandable().getTotalDimension();
                    }

                    public void computeDerivatives(double t, double[] y, double[] yDot) {
                        MultistepIntegrator.this.getExpandable().computeDerivatives(t, y, yDot);
                    }
                }, t0, y0, t, new double[y0.length]);
            }
            throw new MathIllegalStateException(LocalizedFormats.MULTISTEP_STARTER_STOPPED_EARLY, new Object[0]);
        } catch (InitializationCompletedMarkerException e) {
            getCounter().increment(this.starter.getEvaluations());
            this.starter.clearStepHandlers();
        }
    }

    public double getMinReduction() {
        return this.minReduction;
    }

    public void setMinReduction(double minReduction2) {
        this.minReduction = minReduction2;
    }

    public double getMaxGrowth() {
        return this.maxGrowth;
    }

    public void setMaxGrowth(double maxGrowth2) {
        this.maxGrowth = maxGrowth2;
    }

    public double getSafety() {
        return this.safety;
    }

    public void setSafety(double safety2) {
        this.safety = safety2;
    }

    public int getNSteps() {
        return this.nSteps;
    }

    /* access modifiers changed from: protected */
    public double computeStepGrowShrinkFactor(double error) {
        return FastMath.min(this.maxGrowth, FastMath.max(this.minReduction, this.safety * FastMath.pow(error, this.exp)));
    }
}
