package libsvm;

/* compiled from: svm */
class SVC_Q extends Kernel {

    /* renamed from: QD */
    private final double[] f479QD;
    private final Cache cache;

    /* renamed from: y */
    private final byte[] f480y;

    SVC_Q(svm_problem prob, svm_parameter param, byte[] y_) {
        super(prob.f498l, prob.f499x, param);
        this.f480y = (byte[]) y_.clone();
        this.cache = new Cache(prob.f498l, (long) (param.cache_size * 1048576.0d));
        this.f479QD = new double[prob.f498l];
        for (int i = 0; i < prob.f498l; i++) {
            this.f479QD[i] = kernel_function(i, i);
        }
    }

    /* access modifiers changed from: 0000 */
    public float[] get_Q(int i, int len) {
        float[][] data = new float[1][];
        int i2 = this.cache.get_data(i, data, len);
        int start = i2;
        if (i2 < len) {
            for (int j = start; j < len; j++) {
                data[0][j] = (float) (((double) (this.f480y[i] * this.f480y[j])) * kernel_function(i, j));
            }
        }
        return data[0];
    }

    /* access modifiers changed from: 0000 */
    public double[] get_QD() {
        return this.f479QD;
    }

    /* access modifiers changed from: 0000 */
    public void swap_index(int i, int j) {
        this.cache.swap_index(i, j);
        super.swap_index(i, j);
        byte _ = this.f480y[i];
        this.f480y[i] = this.f480y[j];
        this.f480y[j] = _;
        double _2 = this.f479QD[i];
        this.f479QD[i] = this.f479QD[j];
        this.f479QD[j] = _2;
    }
}
