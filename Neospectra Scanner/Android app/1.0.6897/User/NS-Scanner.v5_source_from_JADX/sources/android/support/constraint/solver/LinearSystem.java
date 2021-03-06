package android.support.constraint.solver;

import android.support.constraint.solver.SolverVariable.Type;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;

public class LinearSystem {
    private static final boolean DEBUG = false;
    private static int POOL_SIZE = 1000;
    private int TABLE_SIZE;
    private boolean[] mAlreadyTestedCandidates;
    final Cache mCache;
    private Goal mGoal;
    private int mMaxColumns;
    private int mMaxRows;
    int mNumColumns;
    private int mNumRows;
    private SolverVariable[] mPoolVariables;
    private int mPoolVariablesCount;
    private ArrayRow[] mRows;
    private HashMap<String, SolverVariable> mVariables;
    int mVariablesID;
    private ArrayRow[] tempClientsCopy;

    public LinearSystem() {
        this.mVariablesID = 0;
        this.mVariables = null;
        this.mGoal = new Goal();
        this.TABLE_SIZE = 32;
        this.mMaxColumns = this.TABLE_SIZE;
        this.mRows = null;
        this.mAlreadyTestedCandidates = new boolean[this.TABLE_SIZE];
        this.mNumColumns = 1;
        this.mNumRows = 0;
        this.mMaxRows = this.TABLE_SIZE;
        this.mPoolVariables = new SolverVariable[POOL_SIZE];
        this.mPoolVariablesCount = 0;
        this.tempClientsCopy = new ArrayRow[this.TABLE_SIZE];
        this.mRows = new ArrayRow[this.TABLE_SIZE];
        releaseRows();
        this.mCache = new Cache();
    }

    private void increaseTableSize() {
        this.TABLE_SIZE *= 2;
        this.mRows = (ArrayRow[]) Arrays.copyOf(this.mRows, this.TABLE_SIZE);
        this.mCache.mIndexedVariables = (SolverVariable[]) Arrays.copyOf(this.mCache.mIndexedVariables, this.TABLE_SIZE);
        this.mAlreadyTestedCandidates = new boolean[this.TABLE_SIZE];
        this.mMaxColumns = this.TABLE_SIZE;
        this.mMaxRows = this.TABLE_SIZE;
        this.mGoal.variables.clear();
    }

    private void releaseRows() {
        for (int i = 0; i < this.mRows.length; i++) {
            ArrayRow row = this.mRows[i];
            if (row != null) {
                this.mCache.arrayRowPool.release(row);
            }
            this.mRows[i] = null;
        }
    }

    public void reset() {
        for (SolverVariable variable : this.mCache.mIndexedVariables) {
            if (variable != null) {
                variable.reset();
            }
        }
        this.mCache.solverVariablePool.releaseAll(this.mPoolVariables, this.mPoolVariablesCount);
        this.mPoolVariablesCount = 0;
        Arrays.fill(this.mCache.mIndexedVariables, null);
        if (this.mVariables != null) {
            this.mVariables.clear();
        }
        this.mVariablesID = 0;
        this.mGoal.variables.clear();
        this.mNumColumns = 1;
        for (int i = 0; i < this.mNumRows; i++) {
            this.mRows[i].used = false;
        }
        releaseRows();
        this.mNumRows = 0;
    }

    public SolverVariable createObjectVariable(Object anchor) {
        if (anchor == null) {
            return null;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable variable = null;
        if (anchor instanceof ConstraintAnchor) {
            variable = ((ConstraintAnchor) anchor).getSolverVariable();
            if (variable == null) {
                ((ConstraintAnchor) anchor).resetSolverVariable(this.mCache);
                variable = ((ConstraintAnchor) anchor).getSolverVariable();
            }
            if (variable.f24id == -1 || variable.f24id > this.mVariablesID || this.mCache.mIndexedVariables[variable.f24id] == null) {
                if (variable.f24id != -1) {
                    variable.reset();
                }
                this.mVariablesID++;
                this.mNumColumns++;
                variable.f24id = this.mVariablesID;
                variable.mType = Type.UNRESTRICTED;
                this.mCache.mIndexedVariables[this.mVariablesID] = variable;
            }
        }
        return variable;
    }

    public ArrayRow createRow() {
        ArrayRow row = (ArrayRow) this.mCache.arrayRowPool.acquire();
        if (row == null) {
            return new ArrayRow(this.mCache);
        }
        row.reset();
        return row;
    }

    public SolverVariable createSlackVariable() {
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable variable = acquireSolverVariable(Type.SLACK);
        this.mVariablesID++;
        this.mNumColumns++;
        variable.f24id = this.mVariablesID;
        this.mCache.mIndexedVariables[this.mVariablesID] = variable;
        return variable;
    }

    private void addError(ArrayRow row) {
        row.addError(createErrorVariable(), createErrorVariable());
    }

    private void addSingleError(ArrayRow row, int sign) {
        row.addSingleError(createErrorVariable(), sign);
    }

    private SolverVariable createVariable(String name, Type type) {
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable variable = acquireSolverVariable(type);
        variable.setName(name);
        this.mVariablesID++;
        this.mNumColumns++;
        variable.f24id = this.mVariablesID;
        if (this.mVariables == null) {
            this.mVariables = new HashMap<>();
        }
        this.mVariables.put(name, variable);
        this.mCache.mIndexedVariables[this.mVariablesID] = variable;
        return variable;
    }

    public SolverVariable createErrorVariable() {
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable variable = acquireSolverVariable(Type.ERROR);
        this.mVariablesID++;
        this.mNumColumns++;
        variable.f24id = this.mVariablesID;
        this.mCache.mIndexedVariables[this.mVariablesID] = variable;
        return variable;
    }

    private SolverVariable acquireSolverVariable(Type type) {
        SolverVariable variable = (SolverVariable) this.mCache.solverVariablePool.acquire();
        if (variable == null) {
            variable = new SolverVariable(type);
        } else {
            variable.reset();
            variable.setType(type);
        }
        if (this.mPoolVariablesCount >= POOL_SIZE) {
            POOL_SIZE *= 2;
            this.mPoolVariables = (SolverVariable[]) Arrays.copyOf(this.mPoolVariables, POOL_SIZE);
        }
        SolverVariable[] solverVariableArr = this.mPoolVariables;
        int i = this.mPoolVariablesCount;
        this.mPoolVariablesCount = i + 1;
        solverVariableArr[i] = variable;
        return variable;
    }

    /* access modifiers changed from: 0000 */
    public Goal getGoal() {
        return this.mGoal;
    }

    /* access modifiers changed from: 0000 */
    public ArrayRow getRow(int n) {
        return this.mRows[n];
    }

    /* access modifiers changed from: 0000 */
    public float getValueFor(String name) {
        SolverVariable v = getVariable(name, Type.UNRESTRICTED);
        if (v == null) {
            return 0.0f;
        }
        return v.computedValue;
    }

    public int getObjectVariableValue(Object anchor) {
        SolverVariable variable = ((ConstraintAnchor) anchor).getSolverVariable();
        if (variable != null) {
            return (int) (variable.computedValue + 0.5f);
        }
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public SolverVariable getVariable(String name, Type type) {
        if (this.mVariables == null) {
            this.mVariables = new HashMap<>();
        }
        SolverVariable variable = (SolverVariable) this.mVariables.get(name);
        if (variable == null) {
            return createVariable(name, type);
        }
        return variable;
    }

    /* access modifiers changed from: 0000 */
    public void rebuildGoalFromErrors() {
        this.mGoal.updateFromSystem(this);
    }

    public void minimize() throws Exception {
        minimizeGoal(this.mGoal);
    }

    /* access modifiers changed from: 0000 */
    public void minimizeGoal(Goal goal) throws Exception {
        goal.updateFromSystem(this);
        enforceBFS(goal);
        optimize(goal);
        computeValues();
    }

    private void updateRowFromVariables(ArrayRow row) {
        if (this.mNumRows > 0) {
            row.variables.updateFromSystem(row, this.mRows);
            if (row.variables.currentSize == 0) {
                row.isSimpleDefinition = true;
            }
        }
    }

    public void addConstraint(ArrayRow row) {
        if (row != null) {
            if (this.mNumRows + 1 >= this.mMaxRows || this.mNumColumns + 1 >= this.mMaxColumns) {
                increaseTableSize();
            }
            if (!row.isSimpleDefinition) {
                updateRowFromVariables(row);
                row.ensurePositiveConstant();
                row.pickRowVariable();
                if (!row.hasKeyVariable()) {
                    return;
                }
            }
            if (this.mRows[this.mNumRows] != null) {
                this.mCache.arrayRowPool.release(this.mRows[this.mNumRows]);
            }
            if (!row.isSimpleDefinition) {
                row.updateClientEquations();
            }
            this.mRows[this.mNumRows] = row;
            row.variable.definitionId = this.mNumRows;
            this.mNumRows++;
            int count = row.variable.mClientEquationsCount;
            if (count > 0) {
                while (this.tempClientsCopy.length < count) {
                    this.tempClientsCopy = new ArrayRow[(this.tempClientsCopy.length * 2)];
                }
                ArrayRow[] clients = this.tempClientsCopy;
                for (int i = 0; i < count; i++) {
                    clients[i] = row.variable.mClientEquations[i];
                }
                for (int i2 = 0; i2 < count; i2++) {
                    ArrayRow client = clients[i2];
                    if (client != row) {
                        client.variables.updateFromRow(client, row);
                        client.updateClientEquations();
                    }
                }
            }
        }
    }

    private int optimize(Goal goal) {
        for (int i = 0; i < this.mNumColumns; i++) {
            this.mAlreadyTestedCandidates[i] = false;
        }
        int tries = 0;
        boolean done = false;
        int tested = 0;
        while (!done) {
            tries++;
            SolverVariable pivotCandidate = goal.getPivotCandidate();
            if (pivotCandidate != null) {
                if (this.mAlreadyTestedCandidates[pivotCandidate.f24id]) {
                    pivotCandidate = null;
                } else {
                    this.mAlreadyTestedCandidates[pivotCandidate.f24id] = true;
                    tested++;
                    if (tested >= this.mNumColumns) {
                        done = true;
                    }
                }
            }
            if (pivotCandidate != null) {
                int pivotRowIndex = -1;
                float min = Float.MAX_VALUE;
                for (int i2 = 0; i2 < this.mNumRows; i2++) {
                    ArrayRow current = this.mRows[i2];
                    if (current.variable.mType != Type.UNRESTRICTED && current.hasVariable(pivotCandidate)) {
                        float a_j = current.variables.get(pivotCandidate);
                        if (a_j < 0.0f) {
                            float value = (-current.constantValue) / a_j;
                            if (value < min) {
                                min = value;
                                pivotRowIndex = i2;
                            }
                        }
                    }
                }
                if (pivotRowIndex > -1) {
                    ArrayRow pivotEquation = this.mRows[pivotRowIndex];
                    pivotEquation.variable.definitionId = -1;
                    pivotEquation.pivot(pivotCandidate);
                    pivotEquation.variable.definitionId = pivotRowIndex;
                    for (int i3 = 0; i3 < this.mNumRows; i3++) {
                        this.mRows[i3].updateRowWithEquation(pivotEquation);
                    }
                    goal.updateFromSystem(this);
                    try {
                        enforceBFS(goal);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    done = true;
                }
            } else {
                done = true;
            }
        }
        return tries;
    }

    private int enforceBFS(Goal goal) throws Exception {
        int tries = 0;
        boolean infeasibleSystem = false;
        int i = 0;
        while (true) {
            if (i >= this.mNumRows) {
                break;
            } else if (this.mRows[i].variable.mType != Type.UNRESTRICTED && this.mRows[i].constantValue < 0.0f) {
                infeasibleSystem = true;
                break;
            } else {
                i++;
            }
        }
        if (infeasibleSystem) {
            boolean done = false;
            tries = 0;
            while (!done) {
                tries++;
                int pivotRowIndex = -1;
                int pivotRowIndex2 = -1;
                int strength = 0;
                float min = Float.MAX_VALUE;
                for (int i2 = 0; i2 < this.mNumRows; i2++) {
                    ArrayRow current = this.mRows[i2];
                    if (current.variable.mType != Type.UNRESTRICTED && current.constantValue < 0.0f) {
                        for (int j = 1; j < this.mNumColumns; j++) {
                            SolverVariable candidate = this.mCache.mIndexedVariables[j];
                            float a_j = current.variables.get(candidate);
                            if (a_j > 0.0f) {
                                int pivotColumnIndex = pivotRowIndex2;
                                int pivotRowIndex3 = pivotRowIndex;
                                float min2 = min;
                                for (int k = 0; k < 6; k++) {
                                    float value = candidate.strengthVector[k] / a_j;
                                    if ((value < min2 && k == strength) || k > strength) {
                                        min2 = value;
                                        pivotRowIndex3 = i2;
                                        pivotColumnIndex = j;
                                        strength = k;
                                    }
                                }
                                min = min2;
                                pivotRowIndex = pivotRowIndex3;
                                pivotRowIndex2 = pivotColumnIndex;
                            }
                        }
                    }
                }
                if (pivotRowIndex != -1) {
                    ArrayRow pivotEquation = this.mRows[pivotRowIndex];
                    pivotEquation.variable.definitionId = -1;
                    pivotEquation.pivot(this.mCache.mIndexedVariables[pivotRowIndex2]);
                    pivotEquation.variable.definitionId = pivotRowIndex;
                    for (int i3 = 0; i3 < this.mNumRows; i3++) {
                        this.mRows[i3].updateRowWithEquation(pivotEquation);
                    }
                    goal.updateFromSystem(this);
                } else {
                    Goal goal2 = goal;
                    done = true;
                }
            }
        }
        Goal goal3 = goal;
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 >= this.mNumRows) {
                break;
            } else if (this.mRows[i5].variable.mType != Type.UNRESTRICTED && this.mRows[i5].constantValue < 0.0f) {
                break;
            } else {
                i4 = i5 + 1;
            }
        }
        return tries;
    }

    private void computeValues() {
        for (int i = 0; i < this.mNumRows; i++) {
            ArrayRow row = this.mRows[i];
            row.variable.computedValue = row.constantValue;
        }
    }

    private void displayRows() {
        displaySolverVariables();
        String s = "";
        for (int i = 0; i < this.mNumRows; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(this.mRows[i]);
            String s2 = sb.toString();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(s2);
            sb2.append("\n");
            s = sb2.toString();
        }
        if (this.mGoal.variables.size() != 0) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append(this.mGoal);
            sb3.append("\n");
            s = sb3.toString();
        }
        System.out.println(s);
    }

    /* access modifiers changed from: 0000 */
    public void displayReadableRows() {
        displaySolverVariables();
        String s = "";
        for (int i = 0; i < this.mNumRows; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(this.mRows[i].toReadableString());
            String s2 = sb.toString();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(s2);
            sb2.append("\n");
            s = sb2.toString();
        }
        if (this.mGoal != null) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append(this.mGoal);
            sb3.append("\n");
            s = sb3.toString();
        }
        System.out.println(s);
    }

    public void displayVariablesReadableRows() {
        displaySolverVariables();
        String s = "";
        for (int i = 0; i < this.mNumRows; i++) {
            if (this.mRows[i].variable.mType == Type.UNRESTRICTED) {
                StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append(this.mRows[i].toReadableString());
                String s2 = sb.toString();
                StringBuilder sb2 = new StringBuilder();
                sb2.append(s2);
                sb2.append("\n");
                s = sb2.toString();
            }
        }
        if (this.mGoal.variables.size() != 0) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append(this.mGoal);
            sb3.append("\n");
            s = sb3.toString();
        }
        System.out.println(s);
    }

    public int getMemoryUsed() {
        int actualRowSize = 0;
        for (int i = 0; i < this.mNumRows; i++) {
            if (this.mRows[i] != null) {
                actualRowSize += this.mRows[i].sizeInBytes();
            }
        }
        return actualRowSize;
    }

    public int getNumEquations() {
        return this.mNumRows;
    }

    public int getNumVariables() {
        return this.mVariablesID;
    }

    /* access modifiers changed from: 0000 */
    public void displaySystemInformations() {
        int rowSize = 0;
        for (int i = 0; i < this.TABLE_SIZE; i++) {
            if (this.mRows[i] != null) {
                rowSize += this.mRows[i].sizeInBytes();
            }
        }
        int actualRowSize = 0;
        for (int i2 = 0; i2 < this.mNumRows; i2++) {
            if (this.mRows[i2] != null) {
                actualRowSize += this.mRows[i2].sizeInBytes();
            }
        }
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("Linear System -> Table size: ");
        sb.append(this.TABLE_SIZE);
        sb.append(" (");
        sb.append(getDisplaySize(this.TABLE_SIZE * this.TABLE_SIZE));
        sb.append(") -- row sizes: ");
        sb.append(getDisplaySize(rowSize));
        sb.append(", actual size: ");
        sb.append(getDisplaySize(actualRowSize));
        sb.append(" rows: ");
        sb.append(this.mNumRows);
        sb.append("/");
        sb.append(this.mMaxRows);
        sb.append(" cols: ");
        sb.append(this.mNumColumns);
        sb.append("/");
        sb.append(this.mMaxColumns);
        sb.append(" ");
        sb.append(0);
        sb.append(" occupied cells, ");
        sb.append(getDisplaySize(0));
        printStream.println(sb.toString());
    }

    private void displaySolverVariables() {
        StringBuilder sb = new StringBuilder();
        sb.append("Display Rows (");
        sb.append(this.mNumRows);
        sb.append("x");
        sb.append(this.mNumColumns);
        sb.append(") :\n\t | C | ");
        String s = sb.toString();
        for (int i = 1; i <= this.mNumColumns; i++) {
            SolverVariable v = this.mCache.mIndexedVariables[i];
            StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(v);
            String s2 = sb2.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append(s2);
            sb3.append(" | ");
            s = sb3.toString();
        }
        StringBuilder sb4 = new StringBuilder();
        sb4.append(s);
        sb4.append("\n");
        System.out.println(sb4.toString());
    }

    private String getDisplaySize(int n) {
        int mb = ((n * 4) / 1024) / 1024;
        if (mb > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(mb);
            sb.append(" Mb");
            return sb.toString();
        }
        int kb = (n * 4) / 1024;
        if (kb > 0) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(kb);
            sb2.append(" Kb");
            return sb2.toString();
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("");
        sb3.append(n * 4);
        sb3.append(" bytes");
        return sb3.toString();
    }

    public Cache getCache() {
        return this.mCache;
    }

    public void addGreaterThan(SolverVariable a, SolverVariable b, int margin, int strength) {
        ArrayRow row = createRow();
        SolverVariable slack = createSlackVariable();
        slack.strength = strength;
        row.createRowGreaterThan(a, b, slack, margin);
        addConstraint(row);
    }

    public void addLowerThan(SolverVariable a, SolverVariable b, int margin, int strength) {
        ArrayRow row = createRow();
        SolverVariable slack = createSlackVariable();
        slack.strength = strength;
        row.createRowLowerThan(a, b, slack, margin);
        addConstraint(row);
    }

    public void addCentering(SolverVariable a, SolverVariable b, int m1, float bias, SolverVariable c, SolverVariable d, int m2, int strength) {
        int i = strength;
        ArrayRow row = createRow();
        row.createRowCentering(a, b, m1, bias, c, d, m2);
        SolverVariable error1 = createErrorVariable();
        SolverVariable error2 = createErrorVariable();
        error1.strength = i;
        error2.strength = i;
        row.addError(error1, error2);
        addConstraint(row);
    }

    public ArrayRow addEquality(SolverVariable a, SolverVariable b, int margin, int strength) {
        ArrayRow row = createRow();
        row.createRowEquals(a, b, margin);
        SolverVariable error1 = createErrorVariable();
        SolverVariable error2 = createErrorVariable();
        error1.strength = strength;
        error2.strength = strength;
        row.addError(error1, error2);
        addConstraint(row);
        return row;
    }

    public void addEquality(SolverVariable a, int value) {
        int idx = a.definitionId;
        if (a.definitionId != -1) {
            ArrayRow row = this.mRows[idx];
            if (row.isSimpleDefinition) {
                row.constantValue = (float) value;
                return;
            }
            ArrayRow newRow = createRow();
            newRow.createRowEquals(a, value);
            addConstraint(newRow);
            return;
        }
        ArrayRow row2 = createRow();
        row2.createRowDefinition(a, value);
        addConstraint(row2);
    }

    public static ArrayRow createRowEquals(LinearSystem linearSystem, SolverVariable variableA, SolverVariable variableB, int margin, boolean withError) {
        ArrayRow row = linearSystem.createRow();
        row.createRowEquals(variableA, variableB, margin);
        if (withError) {
            linearSystem.addSingleError(row, 1);
        }
        return row;
    }

    public static ArrayRow createRowDimensionPercent(LinearSystem linearSystem, SolverVariable variableA, SolverVariable variableB, SolverVariable variableC, float percent, boolean withError) {
        ArrayRow row = linearSystem.createRow();
        if (withError) {
            linearSystem.addError(row);
        }
        return row.createRowDimensionPercent(variableA, variableB, variableC, percent);
    }

    public static ArrayRow createRowGreaterThan(LinearSystem linearSystem, SolverVariable variableA, SolverVariable variableB, int margin, boolean withError) {
        SolverVariable slack = linearSystem.createSlackVariable();
        ArrayRow row = linearSystem.createRow();
        row.createRowGreaterThan(variableA, variableB, slack, margin);
        if (withError) {
            linearSystem.addSingleError(row, (int) (-1.0f * row.variables.get(slack)));
        }
        return row;
    }

    public static ArrayRow createRowLowerThan(LinearSystem linearSystem, SolverVariable variableA, SolverVariable variableB, int margin, boolean withError) {
        SolverVariable slack = linearSystem.createSlackVariable();
        ArrayRow row = linearSystem.createRow();
        row.createRowLowerThan(variableA, variableB, slack, margin);
        if (withError) {
            linearSystem.addSingleError(row, (int) (-1.0f * row.variables.get(slack)));
        }
        return row;
    }

    public static ArrayRow createRowCentering(LinearSystem linearSystem, SolverVariable variableA, SolverVariable variableB, int marginA, float bias, SolverVariable variableC, SolverVariable variableD, int marginB, boolean withError) {
        ArrayRow row = linearSystem.createRow();
        row.createRowCentering(variableA, variableB, marginA, bias, variableC, variableD, marginB);
        if (withError) {
            SolverVariable error1 = linearSystem.createErrorVariable();
            SolverVariable error2 = linearSystem.createErrorVariable();
            error1.strength = 4;
            error2.strength = 4;
            row.addError(error1, error2);
        }
        return row;
    }
}
