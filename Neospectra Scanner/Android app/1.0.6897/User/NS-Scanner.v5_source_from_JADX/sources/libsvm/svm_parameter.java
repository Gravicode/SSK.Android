package libsvm;

import java.io.Serializable;

public class svm_parameter implements Cloneable, Serializable {
    public static final int C_SVC = 0;
    public static final int EPSILON_SVR = 3;
    public static final int LINEAR = 0;
    public static final int NU_SVC = 1;
    public static final int NU_SVR = 4;
    public static final int ONE_CLASS = 2;
    public static final int POLY = 1;
    public static final int PRECOMPUTED = 4;
    public static final int RBF = 2;
    public static final int SIGMOID = 3;

    /* renamed from: C */
    public double f495C;
    public double cache_size;
    public double coef0;
    public int degree;
    public double eps;
    public double gamma;
    public int kernel_type;
    public int nr_weight;

    /* renamed from: nu */
    public double f496nu;

    /* renamed from: p */
    public double f497p;
    public int probability;
    public int shrinking;
    public int svm_type;
    public double[] weight;
    public int[] weight_label;

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
