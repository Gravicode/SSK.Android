package libsvm;

/* compiled from: svm */
abstract class Kernel extends QMatrix {
    private final double coef0;
    private final int degree;
    private final double gamma;
    private final int kernel_type;

    /* renamed from: x */
    private svm_node[][] f477x;
    private final double[] x_square;

    /* access modifiers changed from: 0000 */
    public abstract float[] get_Q(int i, int i2);

    /* access modifiers changed from: 0000 */
    public abstract double[] get_QD();

    /* access modifiers changed from: 0000 */
    public void swap_index(int i, int j) {
        svm_node[] _ = this.f477x[i];
        this.f477x[i] = this.f477x[j];
        this.f477x[j] = _;
        if (this.x_square != null) {
            double _2 = this.x_square[i];
            this.x_square[i] = this.x_square[j];
            this.x_square[j] = _2;
        }
    }

    private static double powi(double base, int times) {
        double ret = 1.0d;
        double tmp = base;
        for (int t = times; t > 0; t /= 2) {
            if (t % 2 == 1) {
                ret *= tmp;
            }
            tmp *= tmp;
        }
        return ret;
    }

    /* access modifiers changed from: 0000 */
    public double kernel_function(int i, int j) {
        switch (this.kernel_type) {
            case 0:
                return dot(this.f477x[i], this.f477x[j]);
            case 1:
                return powi((this.gamma * dot(this.f477x[i], this.f477x[j])) + this.coef0, this.degree);
            case 2:
                return Math.exp((-this.gamma) * ((this.x_square[i] + this.x_square[j]) - (dot(this.f477x[i], this.f477x[j]) * 2.0d)));
            case 3:
                return Math.tanh((this.gamma * dot(this.f477x[i], this.f477x[j])) + this.coef0);
            case 4:
                return this.f477x[i][(int) this.f477x[j][0].value].value;
            default:
                return 0.0d;
        }
    }

    Kernel(int l, svm_node[][] x_, svm_parameter param) {
        this.kernel_type = param.kernel_type;
        this.degree = param.degree;
        this.gamma = param.gamma;
        this.coef0 = param.coef0;
        this.f477x = (svm_node[][]) x_.clone();
        if (this.kernel_type == 2) {
            this.x_square = new double[l];
            for (int i = 0; i < l; i++) {
                this.x_square[i] = dot(this.f477x[i], this.f477x[i]);
            }
            return;
        }
        this.x_square = null;
    }

    static double dot(svm_node[] x, svm_node[] y) {
        double sum = 0.0d;
        int xlen = x.length;
        int ylen = y.length;
        int j = 0;
        int j2 = 0;
        while (j < xlen && j2 < ylen) {
            if (x[j].index == y[j2].index) {
                int i = j + 1;
                double d = x[j].value;
                sum += d * y[j2].value;
                j2++;
                j = i;
            } else if (x[j].index > y[j2].index) {
                j2++;
            } else {
                j++;
            }
        }
        return sum;
    }

    static double k_function(svm_node[] x, svm_node[] y, svm_parameter param) {
        int j = 0;
        switch (param.kernel_type) {
            case 0:
                return dot(x, y);
            case 1:
                return powi((param.gamma * dot(x, y)) + param.coef0, param.degree);
            case 2:
                double sum = 0.0d;
                int xlen = x.length;
                int ylen = y.length;
                int j2 = 0;
                while (j2 < xlen && j < ylen) {
                    if (x[j2].index == y[j].index) {
                        int i = j2 + 1;
                        double d = x[j2].value;
                        double d2 = d - y[j].value;
                        sum += d2 * d2;
                        j++;
                        j2 = i;
                    } else if (x[j2].index > y[j].index) {
                        sum += y[j].value * y[j].value;
                        j++;
                    } else {
                        sum += x[j2].value * x[j2].value;
                        j2++;
                    }
                }
                while (j2 < xlen) {
                    sum += x[j2].value * x[j2].value;
                    j2++;
                }
                while (j < ylen) {
                    sum += y[j].value * y[j].value;
                    j++;
                }
                return Math.exp((-param.gamma) * sum);
            case 3:
                return Math.tanh((param.gamma * dot(x, y)) + param.coef0);
            case 4:
                return x[(int) y[0].value].value;
            default:
                return 0.0d;
        }
    }
}
