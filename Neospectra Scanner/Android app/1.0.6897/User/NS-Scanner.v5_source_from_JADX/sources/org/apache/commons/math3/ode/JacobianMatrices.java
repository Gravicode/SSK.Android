package org.apache.commons.math3.ode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class JacobianMatrices {
    /* access modifiers changed from: private */
    public boolean dirtyParameter;
    private ExpandableStatefulODE efode;
    private int index;
    /* access modifiers changed from: private */
    public List<ParameterJacobianProvider> jacobianProviders;
    /* access modifiers changed from: private */
    public MainStateJacobianProvider jode;
    private double[] matricesData;
    /* access modifiers changed from: private */
    public int paramDim;
    /* access modifiers changed from: private */
    public ParameterizedODE pode;
    /* access modifiers changed from: private */
    public ParameterConfiguration[] selectedParameters;
    /* access modifiers changed from: private */
    public int stateDim;

    private class JacobiansSecondaryEquations implements SecondaryEquations {
        private JacobiansSecondaryEquations() {
        }

        public int getDimension() {
            return JacobianMatrices.this.stateDim * (JacobianMatrices.this.stateDim + JacobianMatrices.this.paramDim);
        }

        public void computeDerivatives(double t, double[] y, double[] yDot, double[] z, double[] zDot) throws MaxCountExceededException, DimensionMismatchException {
            ParameterConfiguration param;
            int k;
            int i$;
            double[] dArr = zDot;
            if (JacobianMatrices.this.dirtyParameter && JacobianMatrices.this.paramDim != 0) {
                JacobianMatrices.this.jacobianProviders.add(new ParameterJacobianWrapper(JacobianMatrices.this.jode, JacobianMatrices.this.pode, JacobianMatrices.this.selectedParameters));
                JacobianMatrices.this.dirtyParameter = false;
            }
            double[][] dFdY = (double[][]) Array.newInstance(double.class, new int[]{JacobianMatrices.this.stateDim, JacobianMatrices.this.stateDim});
            JacobianMatrices.this.jode.computeMainStateJacobian(t, y, yDot, dFdY);
            for (int i = 0; i < JacobianMatrices.this.stateDim; i++) {
                double[] dFdYi = dFdY[i];
                for (int j = 0; j < JacobianMatrices.this.stateDim; j++) {
                    int startIndex = j;
                    int zIndex = startIndex;
                    double s = 0.0d;
                    for (int l = 0; l < JacobianMatrices.this.stateDim; l++) {
                        s += dFdYi[l] * z[zIndex];
                        zIndex += JacobianMatrices.this.stateDim;
                    }
                    dArr[(JacobianMatrices.this.stateDim * i) + startIndex] = s;
                }
            }
            if (JacobianMatrices.this.paramDim != 0) {
                double[] dFdP = new double[JacobianMatrices.this.stateDim];
                int startIndex2 = JacobianMatrices.this.stateDim * JacobianMatrices.this.stateDim;
                ParameterConfiguration[] arr$ = JacobianMatrices.this.selectedParameters;
                int len$ = arr$.length;
                int startIndex3 = startIndex2;
                int startIndex4 = 0;
                while (true) {
                    int i$2 = startIndex4;
                    if (i$2 < len$) {
                        ParameterConfiguration param2 = arr$[i$2];
                        boolean found = false;
                        int k2 = 0;
                        while (true) {
                            int k3 = k2;
                            if (found || k3 >= JacobianMatrices.this.jacobianProviders.size()) {
                                int i$3 = i$2;
                            } else {
                                ParameterJacobianProvider provider = (ParameterJacobianProvider) JacobianMatrices.this.jacobianProviders.get(k3);
                                if (provider.isSupported(param2.getParameterName())) {
                                    ParameterJacobianProvider parameterJacobianProvider = provider;
                                    k = k3;
                                    param = param2;
                                    i$ = i$2;
                                    provider.computeParameterJacobian(t, y, yDot, param2.getParameterName(), dFdP);
                                    for (int i2 = 0; i2 < JacobianMatrices.this.stateDim; i2++) {
                                        double[] dFdYi2 = dFdY[i2];
                                        int zIndex2 = startIndex3;
                                        double s2 = dFdP[i2];
                                        int zIndex3 = zIndex2;
                                        for (int l2 = 0; l2 < JacobianMatrices.this.stateDim; l2++) {
                                            s2 += dFdYi2[l2] * z[zIndex3];
                                            zIndex3++;
                                        }
                                        dArr[startIndex3 + i2] = s2;
                                    }
                                    found = true;
                                } else {
                                    k = k3;
                                    param = param2;
                                    i$ = i$2;
                                }
                                k2 = k + 1;
                                i$2 = i$;
                                param2 = param;
                            }
                        }
                        int i$32 = i$2;
                        if (!found) {
                            Arrays.fill(dArr, startIndex3, JacobianMatrices.this.stateDim + startIndex3, 0.0d);
                        }
                        startIndex3 += JacobianMatrices.this.stateDim;
                        startIndex4 = i$32 + 1;
                    } else {
                        return;
                    }
                }
            }
        }
    }

    private static class MainStateJacobianWrapper implements MainStateJacobianProvider {

        /* renamed from: hY */
        private final double[] f642hY;
        /* access modifiers changed from: private */
        public final FirstOrderDifferentialEquations ode;

        MainStateJacobianWrapper(FirstOrderDifferentialEquations ode2, double[] hY) throws DimensionMismatchException {
            this.ode = ode2;
            this.f642hY = (double[]) hY.clone();
            if (hY.length != ode2.getDimension()) {
                throw new DimensionMismatchException(ode2.getDimension(), hY.length);
            }
        }

        public int getDimension() {
            return this.ode.getDimension();
        }

        public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
            this.ode.computeDerivatives(t, y, yDot);
        }

        public void computeMainStateJacobian(double t, double[] y, double[] yDot, double[][] dFdY) throws MaxCountExceededException, DimensionMismatchException {
            double[] dArr = y;
            int n = this.ode.getDimension();
            double[] tmpDot = new double[n];
            for (int j = 0; j < n; j++) {
                double savedYj = dArr[j];
                dArr[j] = dArr[j] + this.f642hY[j];
                this.ode.computeDerivatives(t, dArr, tmpDot);
                for (int i = 0; i < n; i++) {
                    dFdY[i][j] = (tmpDot[i] - yDot[i]) / this.f642hY[j];
                }
                dArr[j] = savedYj;
            }
            double d = t;
        }
    }

    public static class MismatchedEquations extends MathIllegalArgumentException {
        private static final long serialVersionUID = 20120902;

        public MismatchedEquations() {
            super(LocalizedFormats.UNMATCHED_ODE_IN_EXPANDED_SET, new Object[0]);
        }
    }

    public JacobianMatrices(FirstOrderDifferentialEquations fode, double[] hY, String... parameters) throws DimensionMismatchException {
        this(new MainStateJacobianWrapper(fode, hY), parameters);
    }

    public JacobianMatrices(MainStateJacobianProvider jode2, String... parameters) {
        this.efode = null;
        this.index = -1;
        this.jode = jode2;
        this.pode = null;
        this.stateDim = jode2.getDimension();
        int i = 0;
        if (parameters == null) {
            this.selectedParameters = null;
            this.paramDim = 0;
        } else {
            this.selectedParameters = new ParameterConfiguration[parameters.length];
            for (int i2 = 0; i2 < parameters.length; i2++) {
                this.selectedParameters[i2] = new ParameterConfiguration(parameters[i2], Double.NaN);
            }
            this.paramDim = parameters.length;
        }
        this.dirtyParameter = false;
        this.jacobianProviders = new ArrayList();
        this.matricesData = new double[((this.stateDim + this.paramDim) * this.stateDim)];
        while (true) {
            int i3 = i;
            if (i3 < this.stateDim) {
                this.matricesData[(this.stateDim + 1) * i3] = 1.0d;
                i = i3 + 1;
            } else {
                return;
            }
        }
    }

    public void registerVariationalEquations(ExpandableStatefulODE expandable) throws DimensionMismatchException, MismatchedEquations {
        if (expandable.getPrimary() != (this.jode instanceof MainStateJacobianWrapper ? ((MainStateJacobianWrapper) this.jode).ode : this.jode)) {
            throw new MismatchedEquations();
        }
        this.efode = expandable;
        this.index = this.efode.addSecondaryEquations(new JacobiansSecondaryEquations());
        this.efode.setSecondaryState(this.index, this.matricesData);
    }

    public void addParameterJacobianProvider(ParameterJacobianProvider provider) {
        this.jacobianProviders.add(provider);
    }

    public void setParameterizedODE(ParameterizedODE parameterizedOde) {
        this.pode = parameterizedOde;
        this.dirtyParameter = true;
    }

    public void setParameterStep(String parameter, double hP) throws UnknownParameterException {
        ParameterConfiguration[] arr$;
        for (ParameterConfiguration param : this.selectedParameters) {
            if (parameter.equals(param.getParameterName())) {
                param.setHP(hP);
                this.dirtyParameter = true;
                return;
            }
        }
        throw new UnknownParameterException(parameter);
    }

    public void setInitialMainStateJacobian(double[][] dYdY0) throws DimensionMismatchException {
        checkDimension(this.stateDim, dYdY0);
        checkDimension(this.stateDim, dYdY0[0]);
        int i = 0;
        for (double[] row : dYdY0) {
            System.arraycopy(row, 0, this.matricesData, i, this.stateDim);
            i += this.stateDim;
        }
        if (this.efode != null) {
            this.efode.setSecondaryState(this.index, this.matricesData);
        }
    }

    public void setInitialParameterJacobian(String pName, double[] dYdP) throws UnknownParameterException, DimensionMismatchException {
        checkDimension(this.stateDim, dYdP);
        int i = this.stateDim * this.stateDim;
        int i2 = i;
        for (ParameterConfiguration param : this.selectedParameters) {
            if (pName.equals(param.getParameterName())) {
                System.arraycopy(dYdP, 0, this.matricesData, i2, this.stateDim);
                if (this.efode != null) {
                    this.efode.setSecondaryState(this.index, this.matricesData);
                    return;
                }
                return;
            }
            i2 += this.stateDim;
        }
        throw new UnknownParameterException(pName);
    }

    public void getCurrentMainSetJacobian(double[][] dYdY0) {
        double[] p = this.efode.getSecondaryState(this.index);
        int j = 0;
        for (int i = 0; i < this.stateDim; i++) {
            System.arraycopy(p, j, dYdY0[i], 0, this.stateDim);
            j += this.stateDim;
        }
    }

    public void getCurrentParameterJacobian(String pName, double[] dYdP) {
        double[] p = this.efode.getSecondaryState(this.index);
        int i = this.stateDim * this.stateDim;
        ParameterConfiguration[] arr$ = this.selectedParameters;
        int len$ = arr$.length;
        int i2 = i;
        int i$ = 0;
        while (i$ < len$) {
            if (arr$[i$].getParameterName().equals(pName)) {
                System.arraycopy(p, i2, dYdP, 0, this.stateDim);
                return;
            } else {
                i2 += this.stateDim;
                i$++;
            }
        }
    }

    private void checkDimension(int expected, Object array) throws DimensionMismatchException {
        int arrayDimension = array == null ? 0 : Array.getLength(array);
        if (arrayDimension != expected) {
            throw new DimensionMismatchException(arrayDimension, expected);
        }
    }
}
