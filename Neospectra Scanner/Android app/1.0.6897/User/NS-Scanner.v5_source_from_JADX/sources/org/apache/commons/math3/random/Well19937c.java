package org.apache.commons.math3.random;

public class Well19937c extends AbstractWell {

    /* renamed from: K */
    private static final int f755K = 19937;

    /* renamed from: M1 */
    private static final int f756M1 = 70;

    /* renamed from: M2 */
    private static final int f757M2 = 179;

    /* renamed from: M3 */
    private static final int f758M3 = 449;
    private static final long serialVersionUID = -7203498180754925124L;

    public Well19937c() {
        super(f755K, 70, 179, f758M3);
    }

    public Well19937c(int seed) {
        super((int) f755K, 70, 179, (int) f758M3, seed);
    }

    public Well19937c(int[] seed) {
        super((int) f755K, 70, 179, (int) f758M3, seed);
    }

    public Well19937c(long seed) {
        super((int) f755K, 70, 179, (int) f758M3, seed);
    }

    /* access modifiers changed from: protected */
    public int next(int bits) {
        int indexRm1 = this.iRm1[this.index];
        int indexRm2 = this.iRm2[this.index];
        int v0 = this.f741v[this.index];
        int vM1 = this.f741v[this.f738i1[this.index]];
        int vM2 = this.f741v[this.f739i2[this.index]];
        int vM3 = this.f741v[this.f740i3[this.index]];
        int z1 = ((v0 << 25) ^ v0) ^ ((vM1 >>> 27) ^ vM1);
        int z2 = (vM2 >>> 9) ^ ((vM3 >>> 1) ^ vM3);
        int z3 = z1 ^ z2;
        int z4 = ((((z1 << 9) ^ z1) ^ ((this.f741v[indexRm1] & Integer.MIN_VALUE) ^ (this.f741v[indexRm2] & Integer.MAX_VALUE))) ^ ((z2 << 21) ^ z2)) ^ ((z3 >>> 21) ^ z3);
        this.f741v[this.index] = z3;
        this.f741v[indexRm1] = z4;
        int[] iArr = this.f741v;
        iArr[indexRm2] = Integer.MIN_VALUE & iArr[indexRm2];
        this.index = indexRm1;
        int z42 = ((z4 << 7) & -462547200) ^ z4;
        return (z42 ^ ((z42 << 15) & -1685684224)) >>> (32 - bits);
    }
}
