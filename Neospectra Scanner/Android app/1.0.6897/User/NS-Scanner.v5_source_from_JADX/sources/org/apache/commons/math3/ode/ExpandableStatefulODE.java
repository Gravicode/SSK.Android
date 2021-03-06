package org.apache.commons.math3.ode;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;

public class ExpandableStatefulODE {
    private List<SecondaryComponent> components;
    private final FirstOrderDifferentialEquations primary;
    private final EquationsMapper primaryMapper;
    private final double[] primaryState;
    private final double[] primaryStateDot;
    private double time = Double.NaN;

    private static class SecondaryComponent {
        /* access modifiers changed from: private */
        public final SecondaryEquations equation;
        /* access modifiers changed from: private */
        public final EquationsMapper mapper;
        /* access modifiers changed from: private */
        public final double[] state;
        /* access modifiers changed from: private */
        public final double[] stateDot;

        SecondaryComponent(SecondaryEquations equation2, int firstIndex) {
            int n = equation2.getDimension();
            this.equation = equation2;
            this.mapper = new EquationsMapper(firstIndex, n);
            this.state = new double[n];
            this.stateDot = new double[n];
        }
    }

    public ExpandableStatefulODE(FirstOrderDifferentialEquations primary2) {
        int n = primary2.getDimension();
        this.primary = primary2;
        this.primaryMapper = new EquationsMapper(0, n);
        this.primaryState = new double[n];
        this.primaryStateDot = new double[n];
        this.components = new ArrayList();
    }

    public FirstOrderDifferentialEquations getPrimary() {
        return this.primary;
    }

    public int getTotalDimension() {
        if (this.components.isEmpty()) {
            return this.primaryMapper.getDimension();
        }
        EquationsMapper lastMapper = ((SecondaryComponent) this.components.get(this.components.size() - 1)).mapper;
        return lastMapper.getFirstIndex() + lastMapper.getDimension();
    }

    public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
        this.primaryMapper.extractEquationData(y, this.primaryState);
        this.primary.computeDerivatives(t, this.primaryState, this.primaryStateDot);
        for (SecondaryComponent component : this.components) {
            component.mapper.extractEquationData(y, component.state);
            component.equation.computeDerivatives(t, this.primaryState, this.primaryStateDot, component.state, component.stateDot);
            component.mapper.insertEquationData(component.stateDot, yDot);
        }
        this.primaryMapper.insertEquationData(this.primaryStateDot, yDot);
    }

    public int addSecondaryEquations(SecondaryEquations secondary) {
        int firstIndex;
        if (this.components.isEmpty()) {
            this.components = new ArrayList();
            firstIndex = this.primary.getDimension();
        } else {
            SecondaryComponent last = (SecondaryComponent) this.components.get(this.components.size() - 1);
            firstIndex = last.mapper.getFirstIndex() + last.mapper.getDimension();
        }
        this.components.add(new SecondaryComponent(secondary, firstIndex));
        return this.components.size() - 1;
    }

    public EquationsMapper getPrimaryMapper() {
        return this.primaryMapper;
    }

    public EquationsMapper[] getSecondaryMappers() {
        EquationsMapper[] mappers = new EquationsMapper[this.components.size()];
        for (int i = 0; i < mappers.length; i++) {
            mappers[i] = ((SecondaryComponent) this.components.get(i)).mapper;
        }
        return mappers;
    }

    public void setTime(double time2) {
        this.time = time2;
    }

    public double getTime() {
        return this.time;
    }

    public void setPrimaryState(double[] primaryState2) throws DimensionMismatchException {
        if (primaryState2.length != this.primaryState.length) {
            throw new DimensionMismatchException(primaryState2.length, this.primaryState.length);
        }
        System.arraycopy(primaryState2, 0, this.primaryState, 0, primaryState2.length);
    }

    public double[] getPrimaryState() {
        return (double[]) this.primaryState.clone();
    }

    public double[] getPrimaryStateDot() {
        return (double[]) this.primaryStateDot.clone();
    }

    public void setSecondaryState(int index, double[] secondaryState) throws DimensionMismatchException {
        double[] localArray = ((SecondaryComponent) this.components.get(index)).state;
        if (secondaryState.length != localArray.length) {
            throw new DimensionMismatchException(secondaryState.length, localArray.length);
        }
        System.arraycopy(secondaryState, 0, localArray, 0, secondaryState.length);
    }

    public double[] getSecondaryState(int index) {
        return (double[]) ((SecondaryComponent) this.components.get(index)).state.clone();
    }

    public double[] getSecondaryStateDot(int index) {
        return (double[]) ((SecondaryComponent) this.components.get(index)).stateDot.clone();
    }

    public void setCompleteState(double[] completeState) throws DimensionMismatchException {
        if (completeState.length != getTotalDimension()) {
            throw new DimensionMismatchException(completeState.length, getTotalDimension());
        }
        this.primaryMapper.extractEquationData(completeState, this.primaryState);
        for (SecondaryComponent component : this.components) {
            component.mapper.extractEquationData(completeState, component.state);
        }
    }

    public double[] getCompleteState() throws DimensionMismatchException {
        double[] completeState = new double[getTotalDimension()];
        this.primaryMapper.insertEquationData(this.primaryState, completeState);
        for (SecondaryComponent component : this.components) {
            component.mapper.insertEquationData(component.state, completeState);
        }
        return completeState;
    }
}
