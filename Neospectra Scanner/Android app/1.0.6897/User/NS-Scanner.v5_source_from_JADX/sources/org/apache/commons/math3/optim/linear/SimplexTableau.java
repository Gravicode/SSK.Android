package org.apache.commons.math3.optim.linear;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.Precision;

class SimplexTableau implements Serializable {
    private static final String NEGATIVE_VAR_COLUMN_LABEL = "x-";
    private static final long serialVersionUID = -1369660067587938365L;
    private int[] basicRows;
    private int[] basicVariables;
    private final List<String> columnLabels;
    private final List<LinearConstraint> constraints;
    private final double epsilon;

    /* renamed from: f */
    private final LinearObjectiveFunction f705f;
    private final int maxUlps;
    private int numArtificialVariables;
    private final int numDecisionVariables;
    private final int numSlackVariables;
    private final boolean restrictToNonNegative;
    private transient Array2DRowRealMatrix tableau;

    SimplexTableau(LinearObjectiveFunction f, Collection<LinearConstraint> constraints2, GoalType goalType, boolean restrictToNonNegative2, double epsilon2) {
        this(f, constraints2, goalType, restrictToNonNegative2, epsilon2, 10);
    }

    SimplexTableau(LinearObjectiveFunction f, Collection<LinearConstraint> constraints2, GoalType goalType, boolean restrictToNonNegative2, double epsilon2, int maxUlps2) {
        this.columnLabels = new ArrayList();
        this.f705f = f;
        this.constraints = normalizeConstraints(constraints2);
        this.restrictToNonNegative = restrictToNonNegative2;
        this.epsilon = epsilon2;
        this.maxUlps = maxUlps2;
        this.numDecisionVariables = f.getCoefficients().getDimension() + (restrictToNonNegative2 ^ true ? 1 : 0);
        this.numSlackVariables = getConstraintTypeCounts(Relationship.LEQ) + getConstraintTypeCounts(Relationship.GEQ);
        this.numArtificialVariables = getConstraintTypeCounts(Relationship.EQ) + getConstraintTypeCounts(Relationship.GEQ);
        this.tableau = createTableau(goalType == GoalType.MAXIMIZE);
        initializeBasicVariables(getSlackVariableOffset());
        initializeColumnLabels();
    }

    /* access modifiers changed from: protected */
    public void initializeColumnLabels() {
        if (getNumObjectiveFunctions() == 2) {
            this.columnLabels.add("W");
        }
        this.columnLabels.add("Z");
        for (int i = 0; i < getOriginalNumDecisionVariables(); i++) {
            List<String> list = this.columnLabels;
            StringBuilder sb = new StringBuilder();
            sb.append("x");
            sb.append(i);
            list.add(sb.toString());
        }
        if (this.restrictToNonNegative == 0) {
            this.columnLabels.add(NEGATIVE_VAR_COLUMN_LABEL);
        }
        for (int i2 = 0; i2 < getNumSlackVariables(); i2++) {
            List<String> list2 = this.columnLabels;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("s");
            sb2.append(i2);
            list2.add(sb2.toString());
        }
        for (int i3 = 0; i3 < getNumArtificialVariables(); i3++) {
            List<String> list3 = this.columnLabels;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("a");
            sb3.append(i3);
            list3.add(sb3.toString());
        }
        this.columnLabels.add("RHS");
    }

    /* access modifiers changed from: protected */
    public Array2DRowRealMatrix createTableau(boolean maximize) {
        int height;
        int i = 1;
        int width = this.numDecisionVariables + this.numSlackVariables + this.numArtificialVariables + getNumObjectiveFunctions() + 1;
        int height2 = this.constraints.size() + getNumObjectiveFunctions();
        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(height2, width);
        if (getNumObjectiveFunctions() == 2) {
            matrix.setEntry(0, 0, -1.0d);
        }
        int zIndex = getNumObjectiveFunctions() == 1 ? 0 : 1;
        matrix.setEntry(zIndex, zIndex, maximize ? 1.0d : -1.0d);
        RealVector objectiveCoefficients = maximize ? this.f705f.getCoefficients().mapMultiply(-1.0d) : this.f705f.getCoefficients();
        copyArray(objectiveCoefficients.toArray(), matrix.getDataRef()[zIndex]);
        matrix.setEntry(zIndex, width - 1, maximize ? this.f705f.getConstantTerm() : this.f705f.getConstantTerm() * -1.0d);
        if (!this.restrictToNonNegative) {
            matrix.setEntry(zIndex, getSlackVariableOffset() - 1, getInvertedCoefficientSum(objectiveCoefficients));
        }
        int artificialVar = 0;
        int slackVar = 0;
        int i2 = 0;
        while (i2 < this.constraints.size()) {
            LinearConstraint constraint = (LinearConstraint) this.constraints.get(i2);
            int row = getNumObjectiveFunctions() + i2;
            copyArray(constraint.getCoefficients().toArray(), matrix.getDataRef()[row]);
            if (!this.restrictToNonNegative) {
                matrix.setEntry(row, getSlackVariableOffset() - i, getInvertedCoefficientSum(constraint.getCoefficients()));
            }
            matrix.setEntry(row, width - 1, constraint.getValue());
            if (constraint.getRelationship() == Relationship.LEQ) {
                int slackVar2 = slackVar + 1;
                height = height2;
                matrix.setEntry(row, getSlackVariableOffset() + slackVar, 1.0d);
                slackVar = slackVar2;
            } else {
                height = height2;
                if (constraint.getRelationship() == Relationship.GEQ) {
                    int slackVar3 = slackVar + 1;
                    matrix.setEntry(row, getSlackVariableOffset() + slackVar, -1.0d);
                    slackVar = slackVar3;
                    if (constraint.getRelationship() != Relationship.EQ || constraint.getRelationship() == Relationship.GEQ) {
                        matrix.setEntry(0, getArtificialVariableOffset() + artificialVar, 1.0d);
                        int artificialVar2 = artificialVar + 1;
                        matrix.setEntry(row, getArtificialVariableOffset() + artificialVar, 1.0d);
                        matrix.setRowVector(0, matrix.getRowVector(0).subtract(matrix.getRowVector(row)));
                        artificialVar = artificialVar2;
                    }
                    i2++;
                    height2 = height;
                    i = 1;
                }
            }
            if (constraint.getRelationship() != Relationship.EQ) {
            }
            matrix.setEntry(0, getArtificialVariableOffset() + artificialVar, 1.0d);
            int artificialVar22 = artificialVar + 1;
            matrix.setEntry(row, getArtificialVariableOffset() + artificialVar, 1.0d);
            matrix.setRowVector(0, matrix.getRowVector(0).subtract(matrix.getRowVector(row)));
            artificialVar = artificialVar22;
            i2++;
            height2 = height;
            i = 1;
        }
        return matrix;
    }

    public List<LinearConstraint> normalizeConstraints(Collection<LinearConstraint> originalConstraints) {
        List<LinearConstraint> normalized = new ArrayList<>(originalConstraints.size());
        for (LinearConstraint constraint : originalConstraints) {
            normalized.add(normalize(constraint));
        }
        return normalized;
    }

    private LinearConstraint normalize(LinearConstraint constraint) {
        if (constraint.getValue() < 0.0d) {
            return new LinearConstraint(constraint.getCoefficients().mapMultiply(-1.0d), constraint.getRelationship().oppositeRelationship(), constraint.getValue() * -1.0d);
        }
        return new LinearConstraint(constraint.getCoefficients(), constraint.getRelationship(), constraint.getValue());
    }

    /* access modifiers changed from: protected */
    public final int getNumObjectiveFunctions() {
        return this.numArtificialVariables > 0 ? 2 : 1;
    }

    private int getConstraintTypeCounts(Relationship relationship) {
        int count = 0;
        for (LinearConstraint constraint : this.constraints) {
            if (constraint.getRelationship() == relationship) {
                count++;
            }
        }
        return count;
    }

    protected static double getInvertedCoefficientSum(RealVector coefficients) {
        double sum = 0.0d;
        for (double coefficient : coefficients.toArray()) {
            sum -= coefficient;
        }
        return sum;
    }

    /* access modifiers changed from: protected */
    public Integer getBasicRow(int col) {
        int row = this.basicVariables[col];
        if (row == -1) {
            return null;
        }
        return Integer.valueOf(row);
    }

    /* access modifiers changed from: protected */
    public int getBasicVariable(int row) {
        return this.basicRows[row];
    }

    private void initializeBasicVariables(int startColumn) {
        this.basicVariables = new int[(getWidth() - 1)];
        this.basicRows = new int[getHeight()];
        Arrays.fill(this.basicVariables, -1);
        for (int i = startColumn; i < getWidth() - 1; i++) {
            Integer row = findBasicRow(i);
            if (row != null) {
                this.basicVariables[i] = row.intValue();
                this.basicRows[row.intValue()] = i;
            }
        }
    }

    private Integer findBasicRow(int col) {
        Integer row = null;
        for (int i = 0; i < getHeight(); i++) {
            double entry = getEntry(i, col);
            if (Precision.equals(entry, 1.0d, this.maxUlps) && row == null) {
                row = Integer.valueOf(i);
            } else if (!Precision.equals(entry, 0.0d, this.maxUlps)) {
                return null;
            }
        }
        return row;
    }

    /* access modifiers changed from: protected */
    public void dropPhase1Objective() {
        if (getNumObjectiveFunctions() != 1) {
            Set<Integer> columnsToDrop = new TreeSet<>();
            columnsToDrop.add(Integer.valueOf(0));
            for (int i = getNumObjectiveFunctions(); i < getArtificialVariableOffset(); i++) {
                if (Precision.compareTo(getEntry(0, i), 0.0d, this.epsilon) > 0) {
                    columnsToDrop.add(Integer.valueOf(i));
                }
            }
            for (int i2 = 0; i2 < getNumArtificialVariables(); i2++) {
                int col = getArtificialVariableOffset() + i2;
                if (getBasicRow(col) == null) {
                    columnsToDrop.add(Integer.valueOf(col));
                }
            }
            double[][] matrix = (double[][]) Array.newInstance(double.class, new int[]{getHeight() - 1, getWidth() - columnsToDrop.size()});
            for (int i3 = 1; i3 < getHeight(); i3++) {
                int col2 = 0;
                for (int j = 0; j < getWidth(); j++) {
                    if (!columnsToDrop.contains(Integer.valueOf(j))) {
                        int col3 = col2 + 1;
                        matrix[i3 - 1][col2] = getEntry(i3, j);
                        col2 = col3;
                    }
                }
            }
            Integer[] drop = (Integer[]) columnsToDrop.toArray(new Integer[columnsToDrop.size()]);
            int i4 = drop.length - 1;
            while (true) {
                int i5 = i4;
                if (i5 >= 0) {
                    this.columnLabels.remove(drop[i5].intValue());
                    i4 = i5 - 1;
                } else {
                    this.tableau = new Array2DRowRealMatrix(matrix);
                    this.numArtificialVariables = 0;
                    initializeBasicVariables(getNumObjectiveFunctions());
                    return;
                }
            }
        }
    }

    private void copyArray(double[] src, double[] dest) {
        System.arraycopy(src, 0, dest, getNumObjectiveFunctions(), src.length);
    }

    /* access modifiers changed from: 0000 */
    public boolean isOptimal() {
        double[] objectiveFunctionRow = getRow(0);
        int end = getRhsOffset();
        for (int i = getNumObjectiveFunctions(); i < end; i++) {
            if (Precision.compareTo(objectiveFunctionRow[i], 0.0d, this.epsilon) < 0) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public PointValuePair getSolution() {
        int negativeVarColumn = this.columnLabels.indexOf(NEGATIVE_VAR_COLUMN_LABEL);
        Integer negativeVarBasicRow = negativeVarColumn > 0 ? getBasicRow(negativeVarColumn) : null;
        double mostNegative = negativeVarBasicRow == null ? 0.0d : getEntry(negativeVarBasicRow.intValue(), getRhsOffset());
        Set<Integer> usedBasicRows = new HashSet<>();
        double[] coefficients = new double[getOriginalNumDecisionVariables()];
        for (int i = 0; i < coefficients.length; i++) {
            List<String> list = this.columnLabels;
            StringBuilder sb = new StringBuilder();
            sb.append("x");
            sb.append(i);
            int colIndex = list.indexOf(sb.toString());
            if (colIndex < 0) {
                coefficients[i] = 0.0d;
            } else {
                Integer basicRow = getBasicRow(colIndex);
                if (basicRow != null && basicRow.intValue() == 0) {
                    coefficients[i] = 0.0d;
                } else if (usedBasicRows.contains(basicRow)) {
                    coefficients[i] = 0.0d - (this.restrictToNonNegative ? 0.0d : mostNegative);
                } else {
                    usedBasicRows.add(basicRow);
                    coefficients[i] = (basicRow == null ? 0.0d : getEntry(basicRow.intValue(), getRhsOffset())) - (this.restrictToNonNegative ? 0.0d : mostNegative);
                }
            }
        }
        return new PointValuePair(coefficients, this.f705f.value(coefficients));
    }

    /* access modifiers changed from: protected */
    public void performRowOperations(int pivotCol, int pivotRow) {
        divideRow(pivotRow, getEntry(pivotRow, pivotCol));
        for (int i = 0; i < getHeight(); i++) {
            if (i != pivotRow) {
                double multiplier = getEntry(i, pivotCol);
                if (multiplier != 0.0d) {
                    subtractRow(i, pivotRow, multiplier);
                }
            }
        }
        this.basicVariables[getBasicVariable(pivotRow)] = -1;
        this.basicVariables[pivotCol] = pivotRow;
        this.basicRows[pivotRow] = pivotCol;
    }

    /* access modifiers changed from: protected */
    public void divideRow(int dividendRowIndex, double divisor) {
        double[] dividendRow = getRow(dividendRowIndex);
        for (int j = 0; j < getWidth(); j++) {
            dividendRow[j] = dividendRow[j] / divisor;
        }
    }

    /* access modifiers changed from: protected */
    public void subtractRow(int minuendRowIndex, int subtrahendRowIndex, double multiplier) {
        double[] minuendRow = getRow(minuendRowIndex);
        double[] subtrahendRow = getRow(subtrahendRowIndex);
        for (int i = 0; i < getWidth(); i++) {
            minuendRow[i] = minuendRow[i] - (subtrahendRow[i] * multiplier);
        }
    }

    /* access modifiers changed from: protected */
    public final int getWidth() {
        return this.tableau.getColumnDimension();
    }

    /* access modifiers changed from: protected */
    public final int getHeight() {
        return this.tableau.getRowDimension();
    }

    /* access modifiers changed from: protected */
    public final double getEntry(int row, int column) {
        return this.tableau.getEntry(row, column);
    }

    /* access modifiers changed from: protected */
    public final void setEntry(int row, int column, double value) {
        this.tableau.setEntry(row, column, value);
    }

    /* access modifiers changed from: protected */
    public final int getSlackVariableOffset() {
        return getNumObjectiveFunctions() + this.numDecisionVariables;
    }

    /* access modifiers changed from: protected */
    public final int getArtificialVariableOffset() {
        return getNumObjectiveFunctions() + this.numDecisionVariables + this.numSlackVariables;
    }

    /* access modifiers changed from: protected */
    public final int getRhsOffset() {
        return getWidth() - 1;
    }

    /* access modifiers changed from: protected */
    public final int getNumDecisionVariables() {
        return this.numDecisionVariables;
    }

    /* access modifiers changed from: protected */
    public final int getOriginalNumDecisionVariables() {
        return this.f705f.getCoefficients().getDimension();
    }

    /* access modifiers changed from: protected */
    public final int getNumSlackVariables() {
        return this.numSlackVariables;
    }

    /* access modifiers changed from: protected */
    public final int getNumArtificialVariables() {
        return this.numArtificialVariables;
    }

    /* access modifiers changed from: protected */
    public final double[] getRow(int row) {
        return this.tableau.getDataRef()[row];
    }

    /* access modifiers changed from: protected */
    public final double[][] getData() {
        return this.tableau.getData();
    }

    public boolean equals(Object other) {
        boolean z = true;
        if (this == other) {
            return true;
        }
        if (!(other instanceof SimplexTableau)) {
            return false;
        }
        SimplexTableau rhs = (SimplexTableau) other;
        if (!(this.restrictToNonNegative == rhs.restrictToNonNegative && this.numDecisionVariables == rhs.numDecisionVariables && this.numSlackVariables == rhs.numSlackVariables && this.numArtificialVariables == rhs.numArtificialVariables && this.epsilon == rhs.epsilon && this.maxUlps == rhs.maxUlps && this.f705f.equals(rhs.f705f) && this.constraints.equals(rhs.constraints) && this.tableau.equals(rhs.tableau))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((((((Boolean.valueOf(this.restrictToNonNegative).hashCode() ^ this.numDecisionVariables) ^ this.numSlackVariables) ^ this.numArtificialVariables) ^ Double.valueOf(this.epsilon).hashCode()) ^ this.maxUlps) ^ this.f705f.hashCode()) ^ this.constraints.hashCode()) ^ this.tableau.hashCode();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        MatrixUtils.serializeRealMatrix(this.tableau, oos);
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        MatrixUtils.deserializeRealMatrix(this, "tableau", ois);
    }
}
