package android.support.constraint.solver;

import android.support.constraint.solver.SolverVariable.Type;

public class ArrayRow {
    private static final boolean DEBUG = false;
    float constantValue = 0.0f;
    boolean isSimpleDefinition = false;
    boolean used = false;
    SolverVariable variable = null;
    final ArrayLinkedVariables variables;

    public ArrayRow(Cache cache) {
        this.variables = new ArrayLinkedVariables(this, cache);
    }

    /* access modifiers changed from: 0000 */
    public void updateClientEquations() {
        this.variables.updateClientEquations(this);
    }

    /* access modifiers changed from: 0000 */
    public boolean hasAtLeastOnePositiveVariable() {
        return this.variables.hasAtLeastOnePositiveVariable();
    }

    /* access modifiers changed from: 0000 */
    public boolean hasKeyVariable() {
        return this.variable != null && (this.variable.mType == Type.UNRESTRICTED || this.constantValue >= 0.0f);
    }

    public String toString() {
        return toReadableString();
    }

    /* access modifiers changed from: 0000 */
    public String toReadableString() {
        String s;
        String s2;
        String s3 = "";
        if (this.variable == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(s3);
            sb.append("0");
            s = sb.toString();
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(s3);
            sb2.append(this.variable);
            s = sb2.toString();
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append(s);
        sb3.append(" = ");
        String s4 = sb3.toString();
        boolean addedVariable = false;
        if (this.constantValue != 0.0f) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(s4);
            sb4.append(this.constantValue);
            s4 = sb4.toString();
            addedVariable = true;
        }
        int count = this.variables.currentSize;
        for (int i = 0; i < count; i++) {
            SolverVariable v = this.variables.getVariable(i);
            if (v != null) {
                float amount = this.variables.getVariableValue(i);
                String name = v.toString();
                if (!addedVariable) {
                    if (amount < 0.0f) {
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(s2);
                        sb5.append("- ");
                        s2 = sb5.toString();
                        amount *= -1.0f;
                    }
                } else if (amount > 0.0f) {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(s2);
                    sb6.append(" + ");
                    s2 = sb6.toString();
                } else {
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(s2);
                    sb7.append(" - ");
                    s2 = sb7.toString();
                    amount *= -1.0f;
                }
                if (amount == 1.0f) {
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append(s2);
                    sb8.append(name);
                    s2 = sb8.toString();
                } else {
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append(s2);
                    sb9.append(amount);
                    sb9.append(" ");
                    sb9.append(name);
                    s2 = sb9.toString();
                }
                addedVariable = true;
            }
        }
        if (addedVariable) {
            return s2;
        }
        StringBuilder sb10 = new StringBuilder();
        sb10.append(s2);
        sb10.append("0.0");
        return sb10.toString();
    }

    public void reset() {
        this.variable = null;
        this.variables.clear();
        this.constantValue = 0.0f;
        this.isSimpleDefinition = false;
    }

    /* access modifiers changed from: 0000 */
    public boolean hasVariable(SolverVariable v) {
        return this.variables.containsKey(v);
    }

    /* access modifiers changed from: 0000 */
    public ArrayRow createRowDefinition(SolverVariable variable2, int value) {
        this.variable = variable2;
        variable2.computedValue = (float) value;
        this.constantValue = (float) value;
        this.isSimpleDefinition = true;
        return this;
    }

    public ArrayRow createRowEquals(SolverVariable variable2, int value) {
        if (value < 0) {
            this.constantValue = (float) (value * -1);
            this.variables.put(variable2, 1.0f);
        } else {
            this.constantValue = (float) value;
            this.variables.put(variable2, -1.0f);
        }
        return this;
    }

    public ArrayRow createRowEquals(SolverVariable variableA, SolverVariable variableB, int margin) {
        boolean inverse = false;
        if (margin != 0) {
            int m = margin;
            if (m < 0) {
                m *= -1;
                inverse = true;
            }
            this.constantValue = (float) m;
        }
        if (!inverse) {
            this.variables.put(variableA, -1.0f);
            this.variables.put(variableB, 1.0f);
        } else {
            this.variables.put(variableA, 1.0f);
            this.variables.put(variableB, -1.0f);
        }
        return this;
    }

    /* access modifiers changed from: 0000 */
    public ArrayRow addSingleError(SolverVariable error, int sign) {
        this.variables.put(error, (float) sign);
        return this;
    }

    public ArrayRow createRowGreaterThan(SolverVariable variableA, SolverVariable variableB, SolverVariable slack, int margin) {
        boolean inverse = false;
        if (margin != 0) {
            int m = margin;
            if (m < 0) {
                m *= -1;
                inverse = true;
            }
            this.constantValue = (float) m;
        }
        if (!inverse) {
            this.variables.put(variableA, -1.0f);
            this.variables.put(variableB, 1.0f);
            this.variables.put(slack, 1.0f);
        } else {
            this.variables.put(variableA, 1.0f);
            this.variables.put(variableB, -1.0f);
            this.variables.put(slack, -1.0f);
        }
        return this;
    }

    public ArrayRow createRowLowerThan(SolverVariable variableA, SolverVariable variableB, SolverVariable slack, int margin) {
        boolean inverse = false;
        if (margin != 0) {
            int m = margin;
            if (m < 0) {
                m *= -1;
                inverse = true;
            }
            this.constantValue = (float) m;
        }
        if (!inverse) {
            this.variables.put(variableA, -1.0f);
            this.variables.put(variableB, 1.0f);
            this.variables.put(slack, -1.0f);
        } else {
            this.variables.put(variableA, 1.0f);
            this.variables.put(variableB, -1.0f);
            this.variables.put(slack, 1.0f);
        }
        return this;
    }

    public ArrayRow createRowEqualDimension(float currentWeight, float totalWeights, float nextWeight, SolverVariable variableStartA, int marginStartA, SolverVariable variableEndA, int marginEndA, SolverVariable variableStartB, int marginStartB, SolverVariable variableEndB, int marginEndB) {
        SolverVariable solverVariable = variableStartA;
        int i = marginStartA;
        SolverVariable solverVariable2 = variableEndA;
        SolverVariable solverVariable3 = variableStartB;
        int i2 = marginStartB;
        SolverVariable solverVariable4 = variableEndB;
        int i3 = marginEndB;
        if (totalWeights == 0.0f || currentWeight == nextWeight) {
            this.constantValue = (float) (((-i) - marginEndA) + i2 + i3);
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable4, 1.0f);
            this.variables.put(solverVariable3, -1.0f);
        } else {
            float w = (currentWeight / totalWeights) / (nextWeight / totalWeights);
            this.constantValue = ((float) ((-i) - marginEndA)) + (((float) i2) * w) + (((float) i3) * w);
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable4, w);
            this.variables.put(solverVariable3, -w);
        }
        return this;
    }

    /* access modifiers changed from: 0000 */
    public ArrayRow createRowCentering(SolverVariable variableA, SolverVariable variableB, int marginA, float bias, SolverVariable variableC, SolverVariable variableD, int marginB) {
        if (variableB == variableC) {
            this.variables.put(variableA, 1.0f);
            this.variables.put(variableD, 1.0f);
            this.variables.put(variableB, -2.0f);
            return this;
        }
        if (bias == 0.5f) {
            this.variables.put(variableA, 1.0f);
            this.variables.put(variableB, -1.0f);
            this.variables.put(variableC, -1.0f);
            this.variables.put(variableD, 1.0f);
            if (marginA > 0 || marginB > 0) {
                this.constantValue = (float) ((-marginA) + marginB);
            }
        } else if (bias <= 0.0f) {
            this.variables.put(variableA, -1.0f);
            this.variables.put(variableB, 1.0f);
            this.constantValue = (float) marginA;
        } else if (bias >= 1.0f) {
            this.variables.put(variableC, -1.0f);
            this.variables.put(variableD, 1.0f);
            this.constantValue = (float) marginB;
        } else {
            this.variables.put(variableA, (1.0f - bias) * 1.0f);
            this.variables.put(variableB, (1.0f - bias) * -1.0f);
            this.variables.put(variableC, -1.0f * bias);
            this.variables.put(variableD, bias * 1.0f);
            if (marginA > 0 || marginB > 0) {
                this.constantValue = (((float) (-marginA)) * (1.0f - bias)) + (((float) marginB) * bias);
            }
        }
        return this;
    }

    public ArrayRow addError(SolverVariable error1, SolverVariable error2) {
        this.variables.put(error1, 1.0f);
        this.variables.put(error2, -1.0f);
        return this;
    }

    /* access modifiers changed from: 0000 */
    public ArrayRow createRowDimensionPercent(SolverVariable variableA, SolverVariable variableB, SolverVariable variableC, float percent) {
        this.variables.put(variableA, -1.0f);
        this.variables.put(variableB, 1.0f - percent);
        this.variables.put(variableC, percent);
        return this;
    }

    public ArrayRow createRowDimensionRatio(SolverVariable variableA, SolverVariable variableB, SolverVariable variableC, SolverVariable variableD, float ratio) {
        this.variables.put(variableA, -1.0f);
        this.variables.put(variableB, 1.0f);
        this.variables.put(variableC, ratio);
        this.variables.put(variableD, -ratio);
        return this;
    }

    /* access modifiers changed from: 0000 */
    public int sizeInBytes() {
        int size = 0;
        if (this.variable != null) {
            size = 0 + 4;
        }
        return size + 4 + 4 + this.variables.sizeInBytes();
    }

    /* access modifiers changed from: 0000 */
    public boolean updateRowWithEquation(ArrayRow definition) {
        this.variables.updateFromRow(this, definition);
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void ensurePositiveConstant() {
        if (this.constantValue < 0.0f) {
            this.constantValue *= -1.0f;
            this.variables.invert();
        }
    }

    /* access modifiers changed from: 0000 */
    public void pickRowVariable() {
        SolverVariable pivotCandidate = this.variables.pickPivotCandidate();
        if (pivotCandidate != null) {
            pivot(pivotCandidate);
        }
        if (this.variables.currentSize == 0) {
            this.isSimpleDefinition = true;
        }
    }

    /* access modifiers changed from: 0000 */
    public void pivot(SolverVariable v) {
        if (this.variable != null) {
            this.variables.put(this.variable, -1.0f);
            this.variable = null;
        }
        float amount = this.variables.remove(v) * -1.0f;
        this.variable = v;
        if (amount != 1.0f) {
            this.constantValue /= amount;
            this.variables.divideByAmount(amount);
        }
    }
}
