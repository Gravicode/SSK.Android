package org.apache.commons.math3.random;

public class Well512a extends AbstractWell {

    /* renamed from: K */
    private static final int f767K = 512;

    /* renamed from: M1 */
    private static final int f768M1 = 13;

    /* renamed from: M2 */
    private static final int f769M2 = 9;

    /* renamed from: M3 */
    private static final int f770M3 = 5;
    private static final long serialVersionUID = -6104179812103820574L;

    public Well512a() {
        super(512, 13, 9, 5);
    }

    public Well512a(int seed) {
        super(512, 13, 9, 5, seed);
    }

    public Well512a(int[] seed) {
        super(512, 13, 9, 5, seed);
    }

    public Well512a(long seed) {
        super(512, 13, 9, 5, seed);
    }

    /* access modifiers changed from: protected */
    public int next(int bits) {
        int indexRm1 = this.iRm1[this.index];
        int vi = this.f741v[this.index];
        int vi1 = this.f741v[this.f738i1[this.index]];
        int vi2 = this.f741v[this.f739i2[this.index]];
        int z0 = this.f741v[indexRm1];
        int z1 = ((vi << 16) ^ vi) ^ ((vi1 << 15) ^ vi1);
        int z2 = (vi2 >>> 11) ^ vi2;
        int z3 = z1 ^ z2;
        int z4 = ((((z0 << 2) ^ z0) ^ ((z1 << 18) ^ z1)) ^ (z2 << 28)) ^ (((z3 << 5) & -633066204) ^ z3);
        this.f741v[this.index] = z3;
        this.f741v[indexRm1] = z4;
        this.index = indexRm1;
        return z4 >>> (32 - bits);
    }
}
