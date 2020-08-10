package libsvm;

/* compiled from: svm */
abstract class QMatrix {
    /* access modifiers changed from: 0000 */
    public abstract float[] get_Q(int i, int i2);

    /* access modifiers changed from: 0000 */
    public abstract double[] get_QD();

    /* access modifiers changed from: 0000 */
    public abstract void swap_index(int i, int i2);

    QMatrix() {
    }
}
