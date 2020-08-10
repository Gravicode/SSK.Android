package android.support.constraint.solver;

import java.util.Arrays;

public class SolverVariable {
    private static final boolean INTERNAL_DEBUG = false;
    static final int MAX_STRENGTH = 6;
    public static final int STRENGTH_EQUALITY = 5;
    public static final int STRENGTH_HIGH = 3;
    public static final int STRENGTH_HIGHEST = 4;
    public static final int STRENGTH_LOW = 1;
    public static final int STRENGTH_MEDIUM = 2;
    public static final int STRENGTH_NONE = 0;
    private static int uniqueId = 1;
    public float computedValue;
    int definitionId = -1;

    /* renamed from: id */
    public int f24id = -1;
    ArrayRow[] mClientEquations = new ArrayRow[8];
    int mClientEquationsCount = 0;
    private String mName;
    Type mType;
    public int strength = 0;
    float[] strengthVector = new float[6];

    public enum Type {
        UNRESTRICTED,
        CONSTANT,
        SLACK,
        ERROR,
        UNKNOWN
    }

    private static String getUniqueName(Type type) {
        uniqueId++;
        switch (type) {
            case UNRESTRICTED:
                StringBuilder sb = new StringBuilder();
                sb.append("U");
                sb.append(uniqueId);
                return sb.toString();
            case CONSTANT:
                StringBuilder sb2 = new StringBuilder();
                sb2.append("C");
                sb2.append(uniqueId);
                return sb2.toString();
            case SLACK:
                StringBuilder sb3 = new StringBuilder();
                sb3.append("S");
                sb3.append(uniqueId);
                return sb3.toString();
            case ERROR:
                StringBuilder sb4 = new StringBuilder();
                sb4.append("e");
                sb4.append(uniqueId);
                return sb4.toString();
            default:
                StringBuilder sb5 = new StringBuilder();
                sb5.append("V");
                sb5.append(uniqueId);
                return sb5.toString();
        }
    }

    public SolverVariable(String name, Type type) {
        this.mName = name;
        this.mType = type;
    }

    public SolverVariable(Type type) {
        this.mType = type;
    }

    /* access modifiers changed from: 0000 */
    public void clearStrengths() {
        for (int i = 0; i < 6; i++) {
            this.strengthVector[i] = 0.0f;
        }
    }

    /* access modifiers changed from: 0000 */
    public String strengthsToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this);
        sb.append("[");
        String representation = sb.toString();
        for (int j = 0; j < this.strengthVector.length; j++) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(representation);
            sb2.append(this.strengthVector[j]);
            String representation2 = sb2.toString();
            if (j < this.strengthVector.length - 1) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(representation2);
                sb3.append(", ");
                representation = sb3.toString();
            } else {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(representation2);
                sb4.append("] ");
                representation = sb4.toString();
            }
        }
        return representation;
    }

    /* access modifiers changed from: 0000 */
    public void addClientEquation(ArrayRow equation) {
        int i = 0;
        while (i < this.mClientEquationsCount) {
            if (this.mClientEquations[i] != equation) {
                i++;
            } else {
                return;
            }
        }
        if (this.mClientEquationsCount >= this.mClientEquations.length) {
            this.mClientEquations = (ArrayRow[]) Arrays.copyOf(this.mClientEquations, this.mClientEquations.length * 2);
        }
        this.mClientEquations[this.mClientEquationsCount] = equation;
        this.mClientEquationsCount++;
    }

    /* access modifiers changed from: 0000 */
    public void removeClientEquation(ArrayRow equation) {
        for (int i = 0; i < this.mClientEquationsCount; i++) {
            if (this.mClientEquations[i] == equation) {
                for (int j = 0; j < (this.mClientEquationsCount - i) - 1; j++) {
                    this.mClientEquations[i + j] = this.mClientEquations[i + j + 1];
                }
                this.mClientEquationsCount--;
                return;
            }
        }
    }

    public void reset() {
        this.mName = null;
        this.mType = Type.UNKNOWN;
        this.strength = 0;
        this.f24id = -1;
        this.definitionId = -1;
        this.computedValue = 0.0f;
        this.mClientEquationsCount = 0;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setType(Type type) {
        this.mType = type;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(this.mName);
        return sb.toString();
    }
}
