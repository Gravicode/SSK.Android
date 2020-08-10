package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.FastMath;

public class RRQRDecomposition extends QRDecomposition {
    private RealMatrix cachedP;

    /* renamed from: p */
    private int[] f614p;

    private static class Solver implements DecompositionSolver {

        /* renamed from: p */
        private RealMatrix f615p;
        private final DecompositionSolver upper;

        private Solver(DecompositionSolver upper2, RealMatrix p) {
            this.upper = upper2;
            this.f615p = p;
        }

        public boolean isNonSingular() {
            return this.upper.isNonSingular();
        }

        public RealVector solve(RealVector b) {
            return this.f615p.operate(this.upper.solve(b));
        }

        public RealMatrix solve(RealMatrix b) {
            return this.f615p.multiply(this.upper.solve(b));
        }

        public RealMatrix getInverse() {
            return solve(MatrixUtils.createRealIdentityMatrix(this.f615p.getRowDimension()));
        }
    }

    public RRQRDecomposition(RealMatrix matrix) {
        this(matrix, 0.0d);
    }

    public RRQRDecomposition(RealMatrix matrix, double threshold) {
        super(matrix, threshold);
    }

    /* access modifiers changed from: protected */
    public void decompose(double[][] qrt) {
        this.f614p = new int[qrt.length];
        for (int i = 0; i < this.f614p.length; i++) {
            this.f614p[i] = i;
        }
        super.decompose(qrt);
    }

    /* access modifiers changed from: protected */
    public void performHouseholderReflection(int minor, double[][] qrt) {
        int l2NormSquaredMaxIndex = minor;
        double l2NormSquaredMax = 0.0d;
        for (int i = minor; i < qrt.length; i++) {
            double l2NormSquared = 0.0d;
            for (int j = 0; j < qrt[i].length; j++) {
                l2NormSquared += qrt[i][j] * qrt[i][j];
            }
            if (l2NormSquared > l2NormSquaredMax) {
                l2NormSquaredMax = l2NormSquared;
                l2NormSquaredMaxIndex = i;
            }
        }
        if (l2NormSquaredMaxIndex != minor) {
            double[] tmp1 = qrt[minor];
            qrt[minor] = qrt[l2NormSquaredMaxIndex];
            qrt[l2NormSquaredMaxIndex] = tmp1;
            int tmp2 = this.f614p[minor];
            this.f614p[minor] = this.f614p[l2NormSquaredMaxIndex];
            this.f614p[l2NormSquaredMaxIndex] = tmp2;
        }
        super.performHouseholderReflection(minor, qrt);
    }

    public RealMatrix getP() {
        if (this.cachedP == null) {
            int n = this.f614p.length;
            this.cachedP = MatrixUtils.createRealMatrix(n, n);
            for (int i = 0; i < n; i++) {
                this.cachedP.setEntry(this.f614p[i], i, 1.0d);
            }
        }
        return this.cachedP;
    }

    public int getRank(double dropThreshold) {
        RealMatrix r = getR();
        int rows = r.getRowDimension();
        int columns = r.getColumnDimension();
        int rank = 1;
        double lastNorm = r.getFrobeniusNorm();
        double lastNorm2 = lastNorm;
        while (rank < FastMath.min(rows, columns)) {
            double thisNorm = r.getSubMatrix(rank, rows - 1, rank, columns - 1).getFrobeniusNorm();
            if (thisNorm == 0.0d || (thisNorm / lastNorm2) * lastNorm < dropThreshold) {
                break;
            }
            lastNorm2 = thisNorm;
            rank++;
        }
        return rank;
    }

    public DecompositionSolver getSolver() {
        return new Solver(super.getSolver(), getP());
    }
}
