package org.apache.commons.math3.linear;

import java.lang.reflect.Array;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class SingularValueDecomposition {
    private static final double EPS = 2.220446049250313E-16d;
    private static final double TINY = 1.6033346880071782E-291d;
    private RealMatrix cachedS;
    private final RealMatrix cachedU;
    private RealMatrix cachedUt;
    private final RealMatrix cachedV;
    private RealMatrix cachedVt;

    /* renamed from: m */
    private final int f623m;

    /* renamed from: n */
    private final int f624n;
    /* access modifiers changed from: private */
    public final double[] singularValues;
    private final double tol;
    private final boolean transposed;

    private static class Solver implements DecompositionSolver {
        private boolean nonSingular;
        private final RealMatrix pseudoInverse;

        private Solver(double[] singularValues, RealMatrix uT, RealMatrix v, boolean nonSingular2, double tol) {
            double a;
            double[][] suT = uT.getData();
            for (int i = 0; i < singularValues.length; i++) {
                if (singularValues[i] > tol) {
                    a = 1.0d / singularValues[i];
                } else {
                    a = 0.0d;
                }
                double[] suTi = suT[i];
                for (int j = 0; j < suTi.length; j++) {
                    suTi[j] = suTi[j] * a;
                }
            }
            this.pseudoInverse = v.multiply(new Array2DRowRealMatrix(suT, false));
            this.nonSingular = nonSingular2;
        }

        public RealVector solve(RealVector b) {
            return this.pseudoInverse.operate(b);
        }

        public RealMatrix solve(RealMatrix b) {
            return this.pseudoInverse.multiply(b);
        }

        public boolean isNonSingular() {
            return this.nonSingular;
        }

        public RealMatrix getInverse() {
            return this.pseudoInverse;
        }
    }

    /* JADX WARNING: type inference failed for: r9v10 */
    /* JADX WARNING: type inference failed for: r3v15 */
    /* JADX WARNING: type inference failed for: r9v15 */
    /* JADX WARNING: type inference failed for: r3v27 */
    /* JADX WARNING: type inference failed for: r3v45 */
    /* JADX WARNING: type inference failed for: r15v15 */
    /* JADX WARNING: type inference failed for: r15v16 */
    /* JADX WARNING: type inference failed for: r3v46 */
    /* JADX WARNING: type inference failed for: r15v19 */
    /* JADX WARNING: type inference failed for: r3v50 */
    /* JADX WARNING: type inference failed for: r13v24 */
    /* JADX WARNING: type inference failed for: r13v25 */
    /* JADX WARNING: type inference failed for: r4v31 */
    /* JADX WARNING: type inference failed for: r4v32 */
    /* JADX WARNING: type inference failed for: r4v33 */
    /* JADX WARNING: type inference failed for: r10v48 */
    /* JADX WARNING: type inference failed for: r11v7 */
    /* JADX WARNING: type inference failed for: r10v59 */
    /* JADX WARNING: type inference failed for: r11v18 */
    /* JADX WARNING: type inference failed for: r11v23 */
    /* JADX WARNING: type inference failed for: r11v24 */
    /* JADX WARNING: type inference failed for: r11v35 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r51v2
      assigns: []
      uses: []
      mth insns count: 900
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 22 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public SingularValueDecomposition(org.apache.commons.math3.linear.RealMatrix r67) {
        /*
            r66 = this;
            r0 = r66
            r66.<init>()
            int r1 = r67.getRowDimension()
            int r2 = r67.getColumnDimension()
            r3 = 0
            r4 = 1
            if (r1 >= r2) goto L_0x0028
            r0.transposed = r4
            org.apache.commons.math3.linear.RealMatrix r1 = r67.transpose()
            double[][] r1 = r1.getData()
            int r2 = r67.getColumnDimension()
            r0.f623m = r2
            int r2 = r67.getRowDimension()
            r0.f624n = r2
            goto L_0x003a
        L_0x0028:
            r0.transposed = r3
            double[][] r1 = r67.getData()
            int r2 = r67.getRowDimension()
            r0.f623m = r2
            int r2 = r67.getColumnDimension()
            r0.f624n = r2
        L_0x003a:
            int r2 = r0.f624n
            double[] r2 = new double[r2]
            r0.singularValues = r2
            int r2 = r0.f623m
            int r5 = r0.f624n
            int[] r2 = new int[]{r2, r5}
            java.lang.Class<double> r5 = double.class
            java.lang.Object r2 = java.lang.reflect.Array.newInstance(r5, r2)
            double[][] r2 = (double[][]) r2
            int r5 = r0.f624n
            int r6 = r0.f624n
            int[] r5 = new int[]{r5, r6}
            java.lang.Class<double> r6 = double.class
            java.lang.Object r5 = java.lang.reflect.Array.newInstance(r6, r5)
            double[][] r5 = (double[][]) r5
            int r6 = r0.f624n
            double[] r6 = new double[r6]
            int r7 = r0.f623m
            double[] r7 = new double[r7]
            int r8 = r0.f623m
            int r8 = r8 - r4
            int r9 = r0.f624n
            int r8 = org.apache.commons.math3.util.FastMath.min(r8, r9)
            int r9 = r0.f624n
            int r9 = r9 + -2
            int r9 = org.apache.commons.math3.util.FastMath.max(r3, r9)
            r10 = 0
        L_0x007a:
            int r11 = org.apache.commons.math3.util.FastMath.max(r8, r9)
            r14 = 0
            if (r10 >= r11) goto L_0x0204
            if (r10 >= r8) goto L_0x00e4
            double[] r11 = r0.singularValues
            r11[r10] = r14
            r11 = r10
        L_0x0089:
            int r3 = r0.f623m
            if (r11 >= r3) goto L_0x00a4
            double[] r3 = r0.singularValues
            double[] r4 = r0.singularValues
            r12 = r4[r10]
            r4 = r1[r11]
            r14 = r4[r10]
            double r12 = org.apache.commons.math3.util.FastMath.hypot(r12, r14)
            r3[r10] = r12
            int r11 = r11 + 1
            r3 = 0
            r4 = 1
            r14 = 0
            goto L_0x0089
        L_0x00a4:
            double[] r3 = r0.singularValues
            r11 = r3[r10]
            r3 = 0
            int r11 = (r11 > r3 ? 1 : (r11 == r3 ? 0 : -1))
            if (r11 == 0) goto L_0x00db
            r11 = r1[r10]
            r12 = r11[r10]
            int r11 = (r12 > r3 ? 1 : (r12 == r3 ? 0 : -1))
            if (r11 >= 0) goto L_0x00bf
            double[] r3 = r0.singularValues
            double[] r4 = r0.singularValues
            r11 = r4[r10]
            double r11 = -r11
            r3[r10] = r11
        L_0x00bf:
            r3 = r10
        L_0x00c0:
            int r4 = r0.f623m
            if (r3 >= r4) goto L_0x00d2
            r4 = r1[r3]
            r11 = r4[r10]
            double[] r13 = r0.singularValues
            r14 = r13[r10]
            double r11 = r11 / r14
            r4[r10] = r11
            int r3 = r3 + 1
            goto L_0x00c0
        L_0x00d2:
            r3 = r1[r10]
            r11 = r3[r10]
            r13 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r11 = r11 + r13
            r3[r10] = r11
        L_0x00db:
            double[] r3 = r0.singularValues
            double[] r4 = r0.singularValues
            r11 = r4[r10]
            double r11 = -r11
            r3[r10] = r11
        L_0x00e4:
            int r3 = r10 + 1
        L_0x00e6:
            int r4 = r0.f624n
            if (r3 >= r4) goto L_0x0131
            if (r10 >= r8) goto L_0x0128
            double[] r4 = r0.singularValues
            r11 = r4[r10]
            r13 = 0
            int r4 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
            if (r4 == 0) goto L_0x0128
            r11 = 0
            r4 = r10
        L_0x00f9:
            int r13 = r0.f623m
            if (r4 >= r13) goto L_0x010b
            r13 = r1[r4]
            r14 = r13[r10]
            r13 = r1[r4]
            r21 = r13[r3]
            double r14 = r14 * r21
            double r11 = r11 + r14
            int r4 = r4 + 1
            goto L_0x00f9
        L_0x010b:
            double r13 = -r11
            r4 = r1[r10]
            r21 = r4[r10]
            double r13 = r13 / r21
            r4 = r10
        L_0x0113:
            int r11 = r0.f623m
            if (r4 >= r11) goto L_0x0128
            r11 = r1[r4]
            r21 = r11[r3]
            r12 = r1[r4]
            r23 = r12[r10]
            double r23 = r23 * r13
            double r21 = r21 + r23
            r11[r3] = r21
            int r4 = r4 + 1
            goto L_0x0113
        L_0x0128:
            r4 = r1[r10]
            r11 = r4[r3]
            r6[r3] = r11
            int r3 = r3 + 1
            goto L_0x00e6
        L_0x0131:
            if (r10 >= r8) goto L_0x0143
            r3 = r10
        L_0x0134:
            int r4 = r0.f623m
            if (r3 >= r4) goto L_0x0143
            r4 = r2[r3]
            r11 = r1[r3]
            r12 = r11[r10]
            r4[r10] = r12
            int r3 = r3 + 1
            goto L_0x0134
        L_0x0143:
            if (r10 >= r9) goto L_0x01fe
            r3 = 0
            r6[r10] = r3
            int r3 = r10 + 1
        L_0x014b:
            int r4 = r0.f624n
            if (r3 >= r4) goto L_0x015c
            r11 = r6[r10]
            r13 = r6[r3]
            double r11 = org.apache.commons.math3.util.FastMath.hypot(r11, r13)
            r6[r10] = r11
            int r3 = r3 + 1
            goto L_0x014b
        L_0x015c:
            r3 = r6[r10]
            r11 = 0
            int r3 = (r3 > r11 ? 1 : (r3 == r11 ? 0 : -1))
            if (r3 == 0) goto L_0x018a
            int r3 = r10 + 1
            r3 = r6[r3]
            int r3 = (r3 > r11 ? 1 : (r3 == r11 ? 0 : -1))
            if (r3 >= 0) goto L_0x0171
            r3 = r6[r10]
            double r3 = -r3
            r6[r10] = r3
        L_0x0171:
            int r3 = r10 + 1
        L_0x0173:
            int r4 = r0.f624n
            if (r3 >= r4) goto L_0x0181
            r11 = r6[r3]
            r13 = r6[r10]
            double r11 = r11 / r13
            r6[r3] = r11
            int r3 = r3 + 1
            goto L_0x0173
        L_0x0181:
            int r3 = r10 + 1
            r11 = r6[r3]
            r13 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r11 = r11 + r13
            r6[r3] = r11
        L_0x018a:
            r3 = r6[r10]
            double r3 = -r3
            r6[r10] = r3
            int r3 = r10 + 1
            int r4 = r0.f623m
            if (r3 >= r4) goto L_0x01ef
            r3 = r6[r10]
            r11 = 0
            int r3 = (r3 > r11 ? 1 : (r3 == r11 ? 0 : -1))
            if (r3 == 0) goto L_0x01ef
            int r3 = r10 + 1
        L_0x019f:
            int r4 = r0.f623m
            if (r3 >= r4) goto L_0x01aa
            r7[r3] = r11
            int r3 = r3 + 1
            r11 = 0
            goto L_0x019f
        L_0x01aa:
            int r3 = r10 + 1
        L_0x01ac:
            int r4 = r0.f624n
            if (r3 >= r4) goto L_0x01c9
            int r4 = r10 + 1
        L_0x01b2:
            int r11 = r0.f623m
            if (r4 >= r11) goto L_0x01c6
            r11 = r7[r4]
            r13 = r6[r3]
            r15 = r1[r4]
            r17 = r15[r3]
            double r13 = r13 * r17
            double r11 = r11 + r13
            r7[r4] = r11
            int r4 = r4 + 1
            goto L_0x01b2
        L_0x01c6:
            int r3 = r3 + 1
            goto L_0x01ac
        L_0x01c9:
            int r3 = r10 + 1
        L_0x01cb:
            int r4 = r0.f624n
            if (r3 >= r4) goto L_0x01ef
            r11 = r6[r3]
            double r11 = -r11
            int r4 = r10 + 1
            r13 = r6[r4]
            double r11 = r11 / r13
            int r4 = r10 + 1
        L_0x01d9:
            int r13 = r0.f623m
            if (r4 >= r13) goto L_0x01ec
            r13 = r1[r4]
            r14 = r13[r3]
            r17 = r7[r4]
            double r17 = r17 * r11
            double r14 = r14 + r17
            r13[r3] = r14
            int r4 = r4 + 1
            goto L_0x01d9
        L_0x01ec:
            int r3 = r3 + 1
            goto L_0x01cb
        L_0x01ef:
            int r3 = r10 + 1
        L_0x01f1:
            int r4 = r0.f624n
            if (r3 >= r4) goto L_0x01fe
            r4 = r5[r3]
            r11 = r6[r3]
            r4[r10] = r11
            int r3 = r3 + 1
            goto L_0x01f1
        L_0x01fe:
            int r10 = r10 + 1
            r3 = 0
            r4 = 1
            goto L_0x007a
        L_0x0204:
            int r3 = r0.f624n
            int r4 = r0.f624n
            if (r8 >= r4) goto L_0x0212
            double[] r4 = r0.singularValues
            r10 = r1[r8]
            r11 = r10[r8]
            r4[r8] = r11
        L_0x0212:
            int r4 = r0.f623m
            if (r4 >= r3) goto L_0x021e
            double[] r4 = r0.singularValues
            int r10 = r3 + -1
            r11 = 0
            r4[r10] = r11
        L_0x021e:
            int r4 = r9 + 1
            if (r4 >= r3) goto L_0x022a
            r4 = r1[r9]
            int r10 = r3 + -1
            r10 = r4[r10]
            r6[r9] = r10
        L_0x022a:
            int r4 = r3 + -1
            r10 = 0
            r6[r4] = r10
            r4 = r8
        L_0x0231:
            int r10 = r0.f624n
            if (r4 >= r10) goto L_0x024c
            r10 = 0
        L_0x0236:
            int r11 = r0.f623m
            if (r10 >= r11) goto L_0x0243
            r11 = r2[r10]
            r12 = 0
            r11[r4] = r12
            int r10 = r10 + 1
            goto L_0x0236
        L_0x0243:
            r10 = r2[r4]
            r11 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r10[r4] = r11
            int r4 = r4 + 1
            goto L_0x0231
        L_0x024c:
            int r4 = r8 + -1
        L_0x024e:
            if (r4 < 0) goto L_0x02d9
            double[] r10 = r0.singularValues
            r11 = r10[r4]
            r13 = 0
            int r10 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
            if (r10 == 0) goto L_0x02c1
            int r10 = r4 + 1
        L_0x025c:
            int r11 = r0.f624n
            if (r10 >= r11) goto L_0x0297
            r11 = 0
            r12 = r11
            r11 = r4
        L_0x0264:
            int r14 = r0.f623m
            if (r11 >= r14) goto L_0x0277
            r14 = r2[r11]
            r21 = r14[r4]
            r14 = r2[r11]
            r23 = r14[r10]
            double r21 = r21 * r23
            double r12 = r12 + r21
            int r11 = r11 + 1
            goto L_0x0264
        L_0x0277:
            double r14 = -r12
            r11 = r2[r4]
            r21 = r11[r4]
            double r14 = r14 / r21
            r11 = r4
        L_0x027f:
            int r12 = r0.f623m
            if (r11 >= r12) goto L_0x0294
            r12 = r2[r11]
            r21 = r12[r10]
            r13 = r2[r11]
            r23 = r13[r4]
            double r23 = r23 * r14
            double r21 = r21 + r23
            r12[r10] = r21
            int r11 = r11 + 1
            goto L_0x027f
        L_0x0294:
            int r10 = r10 + 1
            goto L_0x025c
        L_0x0297:
            r10 = r4
        L_0x0298:
            int r11 = r0.f623m
            if (r10 >= r11) goto L_0x02a8
            r11 = r2[r10]
            r12 = r2[r10]
            r13 = r12[r4]
            double r12 = -r13
            r11[r4] = r12
            int r10 = r10 + 1
            goto L_0x0298
        L_0x02a8:
            r10 = r2[r4]
            r11 = r2[r4]
            r12 = r11[r4]
            r14 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r12 = r12 + r14
            r10[r4] = r12
            r10 = 0
        L_0x02b4:
            int r11 = r4 + -1
            if (r10 >= r11) goto L_0x02d5
            r11 = r2[r10]
            r12 = 0
            r11[r4] = r12
            int r10 = r10 + 1
            goto L_0x02b4
        L_0x02c1:
            r10 = 0
        L_0x02c2:
            int r11 = r0.f623m
            if (r10 >= r11) goto L_0x02cf
            r11 = r2[r10]
            r12 = 0
            r11[r4] = r12
            int r10 = r10 + 1
            goto L_0x02c2
        L_0x02cf:
            r10 = r2[r4]
            r11 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r10[r4] = r11
        L_0x02d5:
            int r4 = r4 + -1
            goto L_0x024e
        L_0x02d9:
            int r4 = r0.f624n
            r10 = 1
            int r4 = r4 - r10
        L_0x02dd:
            if (r4 < 0) goto L_0x0340
            if (r4 >= r9) goto L_0x0329
            r10 = r6[r4]
            r12 = 0
            int r10 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r10 == 0) goto L_0x0329
            int r10 = r4 + 1
        L_0x02eb:
            int r11 = r0.f624n
            if (r10 >= r11) goto L_0x0329
            r11 = 0
            int r13 = r4 + 1
        L_0x02f3:
            int r14 = r0.f624n
            if (r13 >= r14) goto L_0x0306
            r14 = r5[r13]
            r21 = r14[r4]
            r14 = r5[r13]
            r23 = r14[r10]
            double r21 = r21 * r23
            double r11 = r11 + r21
            int r13 = r13 + 1
            goto L_0x02f3
        L_0x0306:
            double r13 = -r11
            int r15 = r4 + 1
            r15 = r5[r15]
            r21 = r15[r4]
            double r13 = r13 / r21
            int r11 = r4 + 1
        L_0x0311:
            int r12 = r0.f624n
            if (r11 >= r12) goto L_0x0326
            r12 = r5[r11]
            r21 = r12[r10]
            r15 = r5[r11]
            r23 = r15[r4]
            double r23 = r23 * r13
            double r21 = r21 + r23
            r12[r10] = r21
            int r11 = r11 + 1
            goto L_0x0311
        L_0x0326:
            int r10 = r10 + 1
            goto L_0x02eb
        L_0x0329:
            r10 = 0
        L_0x032a:
            int r11 = r0.f624n
            if (r10 >= r11) goto L_0x0337
            r11 = r5[r10]
            r12 = 0
            r11[r4] = r12
            int r10 = r10 + 1
            goto L_0x032a
        L_0x0337:
            r10 = r5[r4]
            r11 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r10[r4] = r11
            int r4 = r4 + -1
            goto L_0x02dd
        L_0x0340:
            int r4 = r3 + -1
        L_0x0342:
            if (r3 <= 0) goto L_0x0770
            int r12 = r3 + -2
        L_0x0346:
            if (r12 < 0) goto L_0x0374
            double[] r15 = r0.singularValues
            r13 = r15[r12]
            double r13 = org.apache.commons.math3.util.FastMath.abs(r13)
            double[] r15 = r0.singularValues
            int r17 = r12 + 1
            r10 = r15[r17]
            double r10 = org.apache.commons.math3.util.FastMath.abs(r10)
            double r13 = r13 + r10
            r10 = 4372995238176751616(0x3cb0000000000000, double:2.220446049250313E-16)
            double r13 = r13 * r10
            r10 = 256705178760118272(0x390000000000000, double:1.6033346880071782E-291)
            double r13 = r13 + r10
            r10 = r6[r12]
            double r10 = org.apache.commons.math3.util.FastMath.abs(r10)
            int r10 = (r10 > r13 ? 1 : (r10 == r13 ? 0 : -1))
            if (r10 > 0) goto L_0x0371
            r10 = 0
            r6[r12] = r10
            goto L_0x0374
        L_0x0371:
            int r12 = r12 + -1
            goto L_0x0346
        L_0x0374:
            int r10 = r3 + -2
            if (r12 != r10) goto L_0x0382
            r10 = 4
            r29 = r7
            r30 = r8
            r31 = r9
            r7 = r10
            goto L_0x03ec
        L_0x0382:
            int r10 = r3 + -1
        L_0x0384:
            if (r10 < r12) goto L_0x03da
            if (r10 != r12) goto L_0x0390
            r29 = r7
            r30 = r8
            r31 = r9
            goto L_0x03e0
        L_0x0390:
            if (r10 == r3) goto L_0x0399
            r13 = r6[r10]
            double r14 = org.apache.commons.math3.util.FastMath.abs(r13)
            goto L_0x039b
        L_0x0399:
            r14 = 0
        L_0x039b:
            int r11 = r12 + 1
            if (r10 == r11) goto L_0x03ac
            int r11 = r10 + -1
            r29 = r7
            r30 = r8
            r7 = r6[r11]
            double r7 = org.apache.commons.math3.util.FastMath.abs(r7)
            goto L_0x03b2
        L_0x03ac:
            r29 = r7
            r30 = r8
            r7 = 0
        L_0x03b2:
            r11 = 0
            double r14 = r14 + r7
            double[] r7 = r0.singularValues
            r31 = r9
            r8 = r7[r10]
            double r7 = org.apache.commons.math3.util.FastMath.abs(r8)
            r17 = 4372995238176751616(0x3cb0000000000000, double:2.220446049250313E-16)
            double r21 = r14 * r17
            r17 = 256705178760118272(0x390000000000000, double:1.6033346880071782E-291)
            double r21 = r21 + r17
            int r7 = (r7 > r21 ? 1 : (r7 == r21 ? 0 : -1))
            if (r7 > 0) goto L_0x03d1
            double[] r7 = r0.singularValues
            r8 = 0
            r7[r10] = r8
            goto L_0x03e0
        L_0x03d1:
            int r10 = r10 + -1
            r7 = r29
            r8 = r30
            r9 = r31
            goto L_0x0384
        L_0x03da:
            r29 = r7
            r30 = r8
            r31 = r9
        L_0x03e0:
            if (r10 != r12) goto L_0x03e4
            r7 = 3
        L_0x03e3:
            goto L_0x03ec
        L_0x03e4:
            int r7 = r3 + -1
            if (r10 != r7) goto L_0x03ea
            r7 = 1
            goto L_0x03e3
        L_0x03ea:
            r7 = 2
            r12 = r10
        L_0x03ec:
            r8 = 1
            int r12 = r12 + r8
            switch(r7) {
                case 1: goto L_0x0655;
                case 2: goto L_0x05dc;
                case 3: goto L_0x0413;
                default: goto L_0x03f1;
            }
        L_0x03f1:
            r51 = r1
            r53 = r4
            r38 = r7
            r7 = r3
            double[] r1 = r0.singularValues
            r3 = r1[r12]
            r8 = 0
            int r1 = (r3 > r8 ? 1 : (r3 == r8 ? 0 : -1))
            if (r1 > 0) goto L_0x06f2
            double[] r1 = r0.singularValues
            double[] r3 = r0.singularValues
            r10 = r3[r12]
            int r3 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1))
            if (r3 >= 0) goto L_0x06dc
            double[] r3 = r0.singularValues
            r10 = r3[r12]
            double r14 = -r10
            goto L_0x06dd
        L_0x0413:
            double[] r8 = r0.singularValues
            int r9 = r3 + -1
            r9 = r8[r9]
            double r8 = org.apache.commons.math3.util.FastMath.abs(r9)
            double[] r10 = r0.singularValues
            int r11 = r3 + -2
            r13 = r10[r11]
            double r10 = org.apache.commons.math3.util.FastMath.abs(r13)
            double r8 = org.apache.commons.math3.util.FastMath.max(r8, r10)
            int r10 = r3 + -2
            r10 = r6[r10]
            double r10 = org.apache.commons.math3.util.FastMath.abs(r10)
            double r10 = org.apache.commons.math3.util.FastMath.max(r8, r10)
            double[] r13 = r0.singularValues
            r14 = r13[r12]
            double r13 = org.apache.commons.math3.util.FastMath.abs(r14)
            double r10 = org.apache.commons.math3.util.FastMath.max(r10, r13)
            r13 = r6[r12]
            double r13 = org.apache.commons.math3.util.FastMath.abs(r13)
            double r10 = org.apache.commons.math3.util.FastMath.max(r10, r13)
            double[] r13 = r0.singularValues
            int r14 = r3 + -1
            r14 = r13[r14]
            double r14 = r14 / r10
            double[] r13 = r0.singularValues
            int r17 = r3 + -2
            r17 = r13[r17]
            double r17 = r17 / r10
            int r13 = r3 + -2
            r21 = r6[r13]
            double r21 = r21 / r10
            double[] r13 = r0.singularValues
            r23 = r13[r12]
            double r23 = r23 / r10
            r25 = r6[r12]
            double r25 = r25 / r10
            double r27 = r17 + r14
            double r32 = r17 - r14
            double r27 = r27 * r32
            double r32 = r21 * r21
            double r27 = r27 + r32
            r32 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r27 = r27 / r32
            double r32 = r14 * r21
            double r34 = r14 * r21
            double r32 = r32 * r34
            r34 = 0
            r19 = 0
            int r13 = (r27 > r19 ? 1 : (r27 == r19 ? 0 : -1))
            if (r13 != 0) goto L_0x0492
            int r13 = (r32 > r19 ? 1 : (r32 == r19 ? 0 : -1))
            if (r13 == 0) goto L_0x048d
            goto L_0x0492
        L_0x048d:
            r38 = r7
            r39 = r8
            goto L_0x04ab
        L_0x0492:
            double r36 = r27 * r27
            r13 = 0
            r38 = r7
            r39 = r8
            double r7 = r36 + r32
            double r7 = org.apache.commons.math3.util.FastMath.sqrt(r7)
            r19 = 0
            int r9 = (r27 > r19 ? 1 : (r27 == r19 ? 0 : -1))
            if (r9 >= 0) goto L_0x04a6
            double r7 = -r7
        L_0x04a6:
            r9 = 0
            double r34 = r27 + r7
            double r34 = r32 / r34
        L_0x04ab:
            r7 = 0
            double r7 = r23 + r14
            double r36 = r23 - r14
            double r7 = r7 * r36
            double r7 = r7 + r34
            double r36 = r23 * r25
            r8 = r7
            r41 = r10
            r7 = r12
            r10 = r36
        L_0x04bc:
            int r13 = r3 + -1
            if (r7 >= r13) goto L_0x05cb
            double r36 = org.apache.commons.math3.util.FastMath.hypot(r8, r10)
            double r43 = r8 / r36
            r45 = r14
            double r13 = r10 / r36
            if (r7 == r12) goto L_0x04d0
            int r15 = r7 + -1
            r6[r15] = r36
        L_0x04d0:
            double[] r15 = r0.singularValues
            r47 = r15[r7]
            double r47 = r47 * r43
            r49 = r6[r7]
            double r49 = r49 * r13
            double r8 = r47 + r49
            r47 = r6[r7]
            double r47 = r47 * r43
            double[] r15 = r0.singularValues
            r49 = r15[r7]
            double r49 = r49 * r13
            double r47 = r47 - r49
            r6[r7] = r47
            double[] r15 = r0.singularValues
            int r47 = r7 + 1
            r47 = r15[r47]
            double r10 = r13 * r47
            double[] r15 = r0.singularValues
            int r47 = r7 + 1
            r51 = r1
            double[] r1 = r0.singularValues
            int r48 = r7 + 1
            r48 = r1[r48]
            double r48 = r48 * r43
            r15[r47] = r48
            r1 = 0
        L_0x0503:
            int r15 = r0.f624n
            if (r1 >= r15) goto L_0x053d
            r15 = r5[r1]
            r47 = r15[r7]
            double r47 = r47 * r43
            r15 = r5[r1]
            int r49 = r7 + 1
            r49 = r15[r49]
            double r49 = r49 * r13
            double r36 = r47 + r49
            r15 = r5[r1]
            int r47 = r7 + 1
            r52 = r3
            r53 = r4
            double r3 = -r13
            r48 = r5[r1]
            r49 = r48[r7]
            double r3 = r3 * r49
            r48 = r5[r1]
            int r49 = r7 + 1
            r49 = r48[r49]
            double r49 = r49 * r43
            double r3 = r3 + r49
            r15[r47] = r3
            r3 = r5[r1]
            r3[r7] = r36
            int r1 = r1 + 1
            r3 = r52
            r4 = r53
            goto L_0x0503
        L_0x053d:
            r52 = r3
            r53 = r4
            double r3 = org.apache.commons.math3.util.FastMath.hypot(r8, r10)
            double r36 = r8 / r3
            double r13 = r10 / r3
            double[] r1 = r0.singularValues
            r1[r7] = r3
            r43 = r6[r7]
            double r43 = r43 * r36
            double[] r1 = r0.singularValues
            int r15 = r7 + 1
            r47 = r1[r15]
            double r47 = r47 * r13
            double r8 = r43 + r47
            double[] r1 = r0.singularValues
            int r15 = r7 + 1
            r54 = r3
            double r3 = -r13
            r43 = r6[r7]
            double r3 = r3 * r43
            r56 = r8
            double[] r8 = r0.singularValues
            int r9 = r7 + 1
            r43 = r8[r9]
            double r43 = r43 * r36
            double r3 = r3 + r43
            r1[r15] = r3
            int r1 = r7 + 1
            r3 = r6[r1]
            double r10 = r13 * r3
            int r1 = r7 + 1
            int r3 = r7 + 1
            r3 = r6[r3]
            double r3 = r3 * r36
            r6[r1] = r3
            int r1 = r0.f623m
            r3 = 1
            int r1 = r1 - r3
            if (r7 >= r1) goto L_0x05bd
            r1 = 0
        L_0x058b:
            int r3 = r0.f623m
            if (r1 >= r3) goto L_0x05bd
            r3 = r2[r1]
            r8 = r3[r7]
            double r8 = r8 * r36
            r3 = r2[r1]
            int r4 = r7 + 1
            r43 = r3[r4]
            double r43 = r43 * r13
            double r54 = r8 + r43
            r3 = r2[r1]
            int r4 = r7 + 1
            double r8 = -r13
            r15 = r2[r1]
            r43 = r15[r7]
            double r8 = r8 * r43
            r15 = r2[r1]
            int r43 = r7 + 1
            r43 = r15[r43]
            double r43 = r43 * r36
            double r8 = r8 + r43
            r3[r4] = r8
            r3 = r2[r1]
            r3[r7] = r54
            int r1 = r1 + 1
            goto L_0x058b
        L_0x05bd:
            int r7 = r7 + 1
            r14 = r45
            r1 = r51
            r3 = r52
            r4 = r53
            r8 = r56
            goto L_0x04bc
        L_0x05cb:
            r51 = r1
            r52 = r3
            r53 = r4
            r45 = r14
            int r3 = r52 + -2
            r6[r3] = r8
            r7 = r52
            goto L_0x06d4
        L_0x05dc:
            r51 = r1
            r52 = r3
            r53 = r4
            r38 = r7
            int r1 = r12 + -1
            r3 = r6[r1]
            int r1 = r12 + -1
            r7 = 0
            r6[r1] = r7
            r1 = r12
        L_0x05ef:
            r7 = r52
            if (r1 >= r7) goto L_0x0653
            double[] r8 = r0.singularValues
            r9 = r8[r1]
            double r8 = org.apache.commons.math3.util.FastMath.hypot(r9, r3)
            double[] r10 = r0.singularValues
            r13 = r10[r1]
            double r13 = r13 / r8
            double r10 = r3 / r8
            double[] r15 = r0.singularValues
            r15[r1] = r8
            r58 = r3
            double r3 = -r10
            r17 = r6[r1]
            double r3 = r3 * r17
            r17 = r6[r1]
            double r17 = r17 * r13
            r6[r1] = r17
            r17 = r8
            r8 = 0
        L_0x0616:
            int r9 = r0.f623m
            if (r8 >= r9) goto L_0x064c
            r9 = r2[r8]
            r21 = r9[r1]
            double r21 = r21 * r13
            r9 = r2[r8]
            int r15 = r12 + -1
            r23 = r9[r15]
            double r23 = r23 * r10
            double r17 = r21 + r23
            r9 = r2[r8]
            int r15 = r12 + -1
            r60 = r3
            double r3 = -r10
            r21 = r2[r8]
            r22 = r21[r1]
            double r3 = r3 * r22
            r21 = r2[r8]
            int r22 = r12 + -1
            r22 = r21[r22]
            double r22 = r22 * r13
            double r3 = r3 + r22
            r9[r15] = r3
            r3 = r2[r8]
            r3[r1] = r17
            int r8 = r8 + 1
            r3 = r60
            goto L_0x0616
        L_0x064c:
            r60 = r3
            int r1 = r1 + 1
            r52 = r7
            goto L_0x05ef
        L_0x0653:
            goto L_0x06d4
        L_0x0655:
            r51 = r1
            r53 = r4
            r38 = r7
            r7 = r3
            int r3 = r7 + -2
            r3 = r6[r3]
            int r1 = r7 + -2
            r8 = 0
            r6[r1] = r8
            int r1 = r7 + -2
        L_0x0668:
            if (r1 < r12) goto L_0x06d3
            double[] r8 = r0.singularValues
            r9 = r8[r1]
            double r8 = org.apache.commons.math3.util.FastMath.hypot(r9, r3)
            double[] r10 = r0.singularValues
            r13 = r10[r1]
            double r13 = r13 / r8
            double r10 = r3 / r8
            double[] r15 = r0.singularValues
            r15[r1] = r8
            if (r1 == r12) goto L_0x0693
            r62 = r3
            double r3 = -r10
            int r15 = r1 + -1
            r17 = r6[r15]
            double r3 = r3 * r17
            int r15 = r1 + -1
            int r17 = r1 + -1
            r17 = r6[r17]
            double r17 = r17 * r13
            r6[r15] = r17
            goto L_0x0695
        L_0x0693:
            r62 = r3
        L_0x0695:
            r17 = r8
            r8 = 0
        L_0x0698:
            int r9 = r0.f624n
            if (r8 >= r9) goto L_0x06ce
            r9 = r5[r8]
            r21 = r9[r1]
            double r21 = r21 * r13
            r9 = r5[r8]
            int r15 = r7 + -1
            r23 = r9[r15]
            double r23 = r23 * r10
            double r17 = r21 + r23
            r9 = r5[r8]
            int r15 = r7 + -1
            r64 = r3
            double r3 = -r10
            r21 = r5[r8]
            r22 = r21[r1]
            double r3 = r3 * r22
            r21 = r5[r8]
            int r22 = r7 + -1
            r22 = r21[r22]
            double r22 = r22 * r13
            double r3 = r3 + r22
            r9[r15] = r3
            r3 = r5[r8]
            r3[r1] = r17
            int r8 = r8 + 1
            r3 = r64
            goto L_0x0698
        L_0x06ce:
            r64 = r3
            int r1 = r1 + -1
            goto L_0x0668
        L_0x06d3:
        L_0x06d4:
            r1 = r7
            r3 = r53
            r4 = 1
            r8 = 0
            goto L_0x0763
        L_0x06dc:
            r14 = r8
        L_0x06dd:
            r1[r12] = r14
            r1 = 0
        L_0x06e0:
            r3 = r53
            if (r1 > r3) goto L_0x06f4
            r4 = r5[r1]
            r10 = r5[r1]
            r13 = r10[r12]
            double r10 = -r13
            r4[r12] = r10
            int r1 = r1 + 1
            r53 = r3
            goto L_0x06e0
        L_0x06f2:
            r3 = r53
        L_0x06f4:
            if (r12 >= r3) goto L_0x0760
            double[] r1 = r0.singularValues
            r10 = r1[r12]
            double[] r1 = r0.singularValues
            int r4 = r12 + 1
            r13 = r1[r4]
            int r1 = (r10 > r13 ? 1 : (r10 == r13 ? 0 : -1))
            if (r1 < 0) goto L_0x0705
            goto L_0x0760
        L_0x0705:
            double[] r1 = r0.singularValues
            r10 = r1[r12]
            double[] r1 = r0.singularValues
            double[] r4 = r0.singularValues
            int r13 = r12 + 1
            r13 = r4[r13]
            r1[r12] = r13
            double[] r1 = r0.singularValues
            int r4 = r12 + 1
            r1[r4] = r10
            int r1 = r0.f624n
            r4 = 1
            int r1 = r1 - r4
            if (r12 >= r1) goto L_0x073b
            r1 = 0
        L_0x0720:
            int r4 = r0.f624n
            if (r1 >= r4) goto L_0x073b
            r4 = r5[r1]
            int r13 = r12 + 1
            r10 = r4[r13]
            r4 = r5[r1]
            int r13 = r12 + 1
            r14 = r5[r1]
            r17 = r14[r12]
            r4[r13] = r17
            r4 = r5[r1]
            r4[r12] = r10
            int r1 = r1 + 1
            goto L_0x0720
        L_0x073b:
            int r1 = r0.f623m
            r4 = 1
            int r1 = r1 - r4
            if (r12 >= r1) goto L_0x075d
            r1 = 0
        L_0x0742:
            int r13 = r0.f623m
            if (r1 >= r13) goto L_0x075d
            r13 = r2[r1]
            int r14 = r12 + 1
            r10 = r13[r14]
            r13 = r2[r1]
            int r14 = r12 + 1
            r15 = r2[r1]
            r16 = r15[r12]
            r13[r14] = r16
            r13 = r2[r1]
            r13[r12] = r10
            int r1 = r1 + 1
            goto L_0x0742
        L_0x075d:
            int r12 = r12 + 1
            goto L_0x06f4
        L_0x0760:
            r4 = 1
            int r1 = r7 + -1
        L_0x0763:
            r4 = r3
            r7 = r29
            r8 = r30
            r9 = r31
            r3 = r1
            r1 = r51
            goto L_0x0342
        L_0x0770:
            r51 = r1
            r29 = r7
            r30 = r8
            r31 = r9
            r7 = r3
            r3 = r4
            int r1 = r0.f623m
            double r8 = (double) r1
            double[] r1 = r0.singularValues
            r4 = 0
            r10 = r1[r4]
            double r8 = r8 * r10
            r10 = 4372995238176751616(0x3cb0000000000000, double:2.220446049250313E-16)
            double r8 = r8 * r10
            double r10 = org.apache.commons.math3.util.Precision.SAFE_MIN
            double r10 = org.apache.commons.math3.util.FastMath.sqrt(r10)
            double r8 = org.apache.commons.math3.util.FastMath.max(r8, r10)
            r0.tol = r8
            boolean r1 = r0.transposed
            if (r1 != 0) goto L_0x07a5
            org.apache.commons.math3.linear.RealMatrix r1 = org.apache.commons.math3.linear.MatrixUtils.createRealMatrix(r2)
            r0.cachedU = r1
            org.apache.commons.math3.linear.RealMatrix r1 = org.apache.commons.math3.linear.MatrixUtils.createRealMatrix(r5)
            r0.cachedV = r1
            goto L_0x07b1
        L_0x07a5:
            org.apache.commons.math3.linear.RealMatrix r1 = org.apache.commons.math3.linear.MatrixUtils.createRealMatrix(r5)
            r0.cachedU = r1
            org.apache.commons.math3.linear.RealMatrix r1 = org.apache.commons.math3.linear.MatrixUtils.createRealMatrix(r2)
            r0.cachedV = r1
        L_0x07b1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.linear.SingularValueDecomposition.<init>(org.apache.commons.math3.linear.RealMatrix):void");
    }

    public RealMatrix getU() {
        return this.cachedU;
    }

    public RealMatrix getUT() {
        if (this.cachedUt == null) {
            this.cachedUt = getU().transpose();
        }
        return this.cachedUt;
    }

    public RealMatrix getS() {
        if (this.cachedS == null) {
            this.cachedS = MatrixUtils.createRealDiagonalMatrix(this.singularValues);
        }
        return this.cachedS;
    }

    public double[] getSingularValues() {
        return (double[]) this.singularValues.clone();
    }

    public RealMatrix getV() {
        return this.cachedV;
    }

    public RealMatrix getVT() {
        if (this.cachedVt == null) {
            this.cachedVt = getV().transpose();
        }
        return this.cachedVt;
    }

    public RealMatrix getCovariance(double minSingularValue) {
        int p = this.singularValues.length;
        int dimension = 0;
        while (dimension < p && this.singularValues[dimension] >= minSingularValue) {
            dimension++;
        }
        if (dimension == 0) {
            throw new NumberIsTooLargeException(LocalizedFormats.TOO_LARGE_CUTOFF_SINGULAR_VALUE, Double.valueOf(minSingularValue), Double.valueOf(this.singularValues[0]), true);
        }
        final double[][] data = (double[][]) Array.newInstance(double.class, new int[]{dimension, p});
        getVT().walkInOptimizedOrder((RealMatrixPreservingVisitor) new DefaultRealMatrixPreservingVisitor() {
            public void visit(int row, int column, double value) {
                data[row][column] = value / SingularValueDecomposition.this.singularValues[row];
            }
        }, 0, dimension - 1, 0, p - 1);
        RealMatrix jv = new Array2DRowRealMatrix(data, false);
        return jv.transpose().multiply(jv);
    }

    public double getNorm() {
        return this.singularValues[0];
    }

    public double getConditionNumber() {
        return this.singularValues[0] / this.singularValues[this.f624n - 1];
    }

    public double getInverseConditionNumber() {
        return this.singularValues[this.f624n - 1] / this.singularValues[0];
    }

    public int getRank() {
        int r = 0;
        for (int i = 0; i < this.singularValues.length; i++) {
            if (this.singularValues[i] > this.tol) {
                r++;
            }
        }
        return r;
    }

    public DecompositionSolver getSolver() {
        Solver solver = new Solver(this.singularValues, getUT(), getV(), getRank() == this.f623m, this.tol);
        return solver;
    }
}
