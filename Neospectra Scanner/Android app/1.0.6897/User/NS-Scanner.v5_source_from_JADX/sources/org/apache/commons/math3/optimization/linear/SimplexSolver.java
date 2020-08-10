package org.apache.commons.math3.optimization.linear;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.util.Precision;

@Deprecated
public class SimplexSolver extends AbstractLinearOptimizer {
    private static final double DEFAULT_EPSILON = 1.0E-6d;
    private static final int DEFAULT_ULPS = 10;
    private final double epsilon;
    private final int maxUlps;

    public SimplexSolver() {
        this(1.0E-6d, 10);
    }

    public SimplexSolver(double epsilon2, int maxUlps2) {
        this.epsilon = epsilon2;
        this.maxUlps = maxUlps2;
    }

    private Integer getPivotColumn(SimplexTableau tableau) {
        double minValue = 0.0d;
        Integer minPos = null;
        for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getWidth() - 1; i++) {
            double entry = tableau.getEntry(0, i);
            if (entry < minValue) {
                minValue = entry;
                minPos = Integer.valueOf(i);
            }
        }
        return minPos;
    }

    private Integer getPivotRow(SimplexTableau tableau, int col) {
        SimplexTableau simplexTableau = tableau;
        List<Integer> minRatioPositions = new ArrayList<>();
        double minRatio = Double.MAX_VALUE;
        for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
            double rhs = simplexTableau.getEntry(i, tableau.getWidth() - 1);
            double entry = simplexTableau.getEntry(i, col);
            if (Precision.compareTo(entry, 0.0d, this.maxUlps) > 0) {
                double ratio = rhs / entry;
                int cmp = Double.compare(ratio, minRatio);
                if (cmp == 0) {
                    minRatioPositions.add(Integer.valueOf(i));
                } else if (cmp < 0) {
                    minRatio = ratio;
                    minRatioPositions = new ArrayList<>();
                    minRatioPositions.add(Integer.valueOf(i));
                }
            }
        }
        int i2 = col;
        if (minRatioPositions.size() == 0) {
            return null;
        }
        if (minRatioPositions.size() > 1) {
            if (tableau.getNumArtificialVariables() > 0) {
                for (Integer row : minRatioPositions) {
                    int i3 = 0;
                    while (true) {
                        if (i3 < tableau.getNumArtificialVariables()) {
                            int column = tableau.getArtificialVariableOffset() + i3;
                            if (Precision.equals(simplexTableau.getEntry(row.intValue(), column), 1.0d, this.maxUlps) && row.equals(simplexTableau.getBasicRow(column))) {
                                return row;
                            }
                            i3++;
                        }
                    }
                }
            }
            if (getIterations() < getMaxIterations() / 2) {
                Integer minRow = null;
                int minIndex = tableau.getWidth();
                int varStart = tableau.getNumObjectiveFunctions();
                int varEnd = tableau.getWidth() - 1;
                for (Integer row2 : minRatioPositions) {
                    int minIndex2 = minIndex;
                    Integer minRow2 = minRow;
                    for (int i4 = varStart; i4 < varEnd && !row2.equals(minRow2); i4++) {
                        Integer basicRow = simplexTableau.getBasicRow(i4);
                        if (basicRow != null && basicRow.equals(row2) && i4 < minIndex2) {
                            minIndex2 = i4;
                            minRow2 = row2;
                        }
                    }
                    minRow = minRow2;
                    minIndex = minIndex2;
                }
                return minRow;
            }
        }
        return (Integer) minRatioPositions.get(0);
    }

    /* access modifiers changed from: protected */
    public void doIteration(SimplexTableau tableau) throws MaxCountExceededException, UnboundedSolutionException {
        incrementIterationsCounter();
        Integer pivotCol = getPivotColumn(tableau);
        Integer pivotRow = getPivotRow(tableau, pivotCol.intValue());
        if (pivotRow == null) {
            throw new UnboundedSolutionException();
        }
        tableau.divideRow(pivotRow.intValue(), tableau.getEntry(pivotRow.intValue(), pivotCol.intValue()));
        for (int i = 0; i < tableau.getHeight(); i++) {
            if (i != pivotRow.intValue()) {
                tableau.subtractRow(i, pivotRow.intValue(), tableau.getEntry(i, pivotCol.intValue()));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void solvePhase1(SimplexTableau tableau) throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        if (tableau.getNumArtificialVariables() != 0) {
            while (!tableau.isOptimal()) {
                doIteration(tableau);
            }
            if (!Precision.equals(tableau.getEntry(0, tableau.getRhsOffset()), 0.0d, this.epsilon)) {
                throw new NoFeasibleSolutionException();
            }
        }
    }

    public PointValuePair doOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(getFunction(), getConstraints(), getGoalType(), restrictToNonNegative(), this.epsilon, this.maxUlps);
        solvePhase1(tableau);
        tableau.dropPhase1Objective();
        while (!tableau.isOptimal()) {
            doIteration(tableau);
        }
        return tableau.getSolution();
    }
}
