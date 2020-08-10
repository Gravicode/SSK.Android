package org.apache.commons.math3.random;

public class Well1024a extends AbstractWell {

    /* renamed from: K */
    private static final int f747K = 1024;

    /* renamed from: M1 */
    private static final int f748M1 = 3;

    /* renamed from: M2 */
    private static final int f749M2 = 24;

    /* renamed from: M3 */
    private static final int f750M3 = 10;
    private static final long serialVersionUID = 5680173464174485492L;

    public Well1024a() {
        super(1024, 3, 24, 10);
    }

    public Well1024a(int seed) {
        super(1024, 3, 24, 10, seed);
    }

    public Well1024a(int[] seed) {
        super(1024, 3, 24, 10, seed);
    }

    public Well1024a(long seed) {
        super(1024, 3, 24, 10, seed);
    }

    /* access modifiers changed from: protected */
    public int next(int bits) {
        int indexRm1 = this.iRm1[this.index];
        int v0 = this.f741v[this.index];
        int vM1 = this.f741v[this.f738i1[this.index]];
        int vM2 = this.f741v[this.f739i2[this.index]];
        int vM3 = this.f741v[this.f740i3[this.index]];
        int z0 = this.f741v[indexRm1];
        int z1 = ((vM1 >>> 8) ^ vM1) ^ v0;
        int z2 = ((vM2 << 19) ^ vM2) ^ ((vM3 << 14) ^ vM3);
        int z4 = (((z0 << 11) ^ z0) ^ ((z1 << 7) ^ z1)) ^ ((z2 << 13) ^ z2);
        this.f741v[this.index] = z1 ^ z2;
        this.f741v[indexRm1] = z4;
        this.index = indexRm1;
        return z4 >>> (32 - bits);
    }
}
