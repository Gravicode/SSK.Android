package libsvm;

import java.io.Serializable;

public class svm_model implements Serializable {

    /* renamed from: SV */
    public svm_node[][] f493SV;

    /* renamed from: l */
    public int f494l;
    public int[] label;
    public int[] nSV;
    public int nr_class;
    public svm_parameter param;
    public double[] probA;
    public double[] probB;
    public double[] rho;
    public double[][] sv_coef;
}
