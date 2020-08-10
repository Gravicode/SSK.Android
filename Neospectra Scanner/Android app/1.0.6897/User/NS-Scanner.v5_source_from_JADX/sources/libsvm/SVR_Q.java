package libsvm;

import java.lang.reflect.Array;

/* compiled from: svm */
class SVR_Q extends Kernel {

    /* renamed from: QD */
    private final double[] f481QD = new double[(this.f482l * 2)];
    private float[][] buffer;
    private final Cache cache;
    private final int[] index = new int[(this.f482l * 2)];

    /* renamed from: l */
    private final int f482l;
    private int next_buffer;
    private final byte[] sign = new byte[(this.f482l * 2)];

    SVR_Q(svm_problem prob, svm_parameter param) {
        super(prob.f498l, prob.f499x, param);
        this.f482l = prob.f498l;
        this.cache = new Cache(this.f482l, (long) (param.cache_size * 1048576.0d));
        for (int k = 0; k < this.f482l; k++) {
            this.sign[k] = 1;
            this.sign[this.f482l + k] = -1;
            this.index[k] = k;
            this.index[this.f482l + k] = k;
            this.f481QD[k] = kernel_function(k, k);
            this.f481QD[this.f482l + k] = this.f481QD[k];
        }
        this.buffer = (float[][]) Array.newInstance(float.class, new int[]{2, this.f482l * 2});
        this.next_buffer = 0;
    }

    /* access modifiers changed from: 0000 */
    public void swap_index(int i, int j) {
        byte _ = this.sign[i];
        this.sign[i] = this.sign[j];
        this.sign[j] = _;
        int _2 = this.index[i];
        this.index[i] = this.index[j];
        this.index[j] = _2;
        double _3 = this.f481QD[i];
        this.f481QD[i] = this.f481QD[j];
        this.f481QD[j] = _3;
    }

    /* access modifiers changed from: 0000 */
    public float[] get_Q(int i, int len) {
        float[][] data = new float[1][];
        int real_i = this.index[i];
        if (this.cache.get_data(real_i, data, this.f482l) < this.f482l) {
            for (int j = 0; j < this.f482l; j++) {
                data[0][j] = (float) kernel_function(real_i, j);
            }
        }
        float[] buf = this.buffer[this.next_buffer];
        this.next_buffer = 1 - this.next_buffer;
        byte si = this.sign[i];
        for (int j2 = 0; j2 < len; j2++) {
            buf[j2] = ((float) si) * ((float) this.sign[j2]) * data[0][this.index[j2]];
        }
        return buf;
    }

    /* access modifiers changed from: 0000 */
    public double[] get_QD() {
        return this.f481QD;
    }
}
