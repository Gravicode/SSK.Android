package libsvm;

/* compiled from: svm */
class ONE_CLASS_Q extends Kernel {

    /* renamed from: QD */
    private final double[] f478QD;
    private final Cache cache;

    ONE_CLASS_Q(svm_problem prob, svm_parameter param) {
        super(prob.f498l, prob.f499x, param);
        this.cache = new Cache(prob.f498l, (long) (param.cache_size * 1048576.0d));
        this.f478QD = new double[prob.f498l];
        for (int i = 0; i < prob.f498l; i++) {
            this.f478QD[i] = kernel_function(i, i);
        }
    }

    /* access modifiers changed from: 0000 */
    public float[] get_Q(int i, int len) {
        float[][] data = new float[1][];
        int i2 = this.cache.get_data(i, data, len);
        int start = i2;
        if (i2 < len) {
            for (int j = start; j < len; j++) {
                data[0][j] = (float) kernel_function(i, j);
            }
        }
        return data[0];
    }

    /* access modifiers changed from: 0000 */
    public double[] get_QD() {
        return this.f478QD;
    }

    /* access modifiers changed from: 0000 */
    public void swap_index(int i, int j) {
        this.cache.swap_index(i, j);
        super.swap_index(i, j);
        double _ = this.f478QD[i];
        this.f478QD[i] = this.f478QD[j];
        this.f478QD[j] = _;
    }
}
