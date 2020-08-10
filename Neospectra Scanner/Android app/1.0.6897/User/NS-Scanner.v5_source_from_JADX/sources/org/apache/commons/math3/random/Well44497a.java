package org.apache.commons.math3.random;

public class Well44497a extends AbstractWell {

    /* renamed from: K */
    private static final int f759K = 44497;

    /* renamed from: M1 */
    private static final int f760M1 = 23;

    /* renamed from: M2 */
    private static final int f761M2 = 481;

    /* renamed from: M3 */
    private static final int f762M3 = 229;
    private static final long serialVersionUID = -3859207588353972099L;

    public Well44497a() {
        super(f759K, 23, f761M2, f762M3);
    }

    public Well44497a(int seed) {
        super((int) f759K, 23, (int) f761M2, (int) f762M3, seed);
    }

    public Well44497a(int[] seed) {
        super((int) f759K, 23, (int) f761M2, (int) f762M3, seed);
    }

    public Well44497a(long seed) {
        super((int) f759K, 23, (int) f761M2, (int) f762M3, seed);
    }

    /* access modifiers changed from: protected */
    public int next(int bits) {
        int indexRm1 = this.iRm1[this.index];
        int indexRm2 = this.iRm2[this.index];
        int v0 = this.f741v[this.index];
        int vM1 = this.f741v[this.f738i1[this.index]];
        int vM2 = this.f741v[this.f739i2[this.index]];
        int z1 = ((v0 << 24) ^ v0) ^ ((vM1 >>> 30) ^ vM1);
        int z2 = ((vM2 << 10) ^ vM2) ^ (this.f741v[this.f740i3[this.index]] << 26);
        int z3 = z1 ^ z2;
        int z2Prime = ((z2 << 9) ^ (z2 >>> 23)) & -67108865;
        int z4 = ((((z1 >>> 20) ^ z1) ^ ((this.f741v[indexRm1] & -32768) ^ (this.f741v[indexRm2] & 32767))) ^ ((131072 & z2) != 0 ? -1221985044 ^ z2Prime : z2Prime)) ^ z3;
        this.f741v[this.index] = z3;
        this.f741v[indexRm1] = z4;
        int[] iArr = this.f741v;
        iArr[indexRm2] = iArr[indexRm2] & -32768;
        this.index = indexRm1;
        return z4 >>> (32 - bits);
    }
}
