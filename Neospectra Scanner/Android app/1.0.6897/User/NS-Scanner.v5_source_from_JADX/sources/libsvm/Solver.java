package libsvm;

/* compiled from: svm */
class Solver {
    static final byte FREE = 2;
    static final double INF = Double.POSITIVE_INFINITY;
    static final byte LOWER_BOUND = 0;
    static final byte UPPER_BOUND = 1;

    /* renamed from: Cn */
    double f483Cn;

    /* renamed from: Cp */
    double f484Cp;

    /* renamed from: G */
    double[] f485G;
    double[] G_bar;

    /* renamed from: Q */
    QMatrix f486Q;

    /* renamed from: QD */
    double[] f487QD;
    int[] active_set;
    int active_size;
    double[] alpha;
    byte[] alpha_status;
    double eps;

    /* renamed from: l */
    int f488l;

    /* renamed from: p */
    double[] f489p;
    boolean unshrink;

    /* renamed from: y */
    byte[] f490y;

    /* compiled from: svm */
    static class SolutionInfo {
        double obj;

        /* renamed from: r */
        double f491r;
        double rho;
        double upper_bound_n;
        double upper_bound_p;

        SolutionInfo() {
        }
    }

    Solver() {
    }

    /* access modifiers changed from: 0000 */
    public double get_C(int i) {
        return this.f490y[i] > 0 ? this.f484Cp : this.f483Cn;
    }

    /* access modifiers changed from: 0000 */
    public void update_alpha_status(int i) {
        if (this.alpha[i] >= get_C(i)) {
            this.alpha_status[i] = 1;
        } else if (this.alpha[i] <= 0.0d) {
            this.alpha_status[i] = 0;
        } else {
            this.alpha_status[i] = 2;
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean is_upper_bound(int i) {
        return this.alpha_status[i] == 1;
    }

    /* access modifiers changed from: 0000 */
    public boolean is_lower_bound(int i) {
        return this.alpha_status[i] == 0;
    }

    /* access modifiers changed from: 0000 */
    public boolean is_free(int i) {
        return this.alpha_status[i] == 2;
    }

    /* access modifiers changed from: 0000 */
    public void swap_index(int i, int j) {
        this.f486Q.swap_index(i, j);
        byte _ = this.f490y[i];
        this.f490y[i] = this.f490y[j];
        this.f490y[j] = _;
        double _2 = this.f485G[i];
        this.f485G[i] = this.f485G[j];
        this.f485G[j] = _2;
        byte _3 = this.alpha_status[i];
        this.alpha_status[i] = this.alpha_status[j];
        this.alpha_status[j] = _3;
        double _4 = this.alpha[i];
        this.alpha[i] = this.alpha[j];
        this.alpha[j] = _4;
        double _5 = this.f489p[i];
        this.f489p[i] = this.f489p[j];
        this.f489p[j] = _5;
        int _6 = this.active_set[i];
        this.active_set[i] = this.active_set[j];
        this.active_set[j] = _6;
        double _7 = this.G_bar[i];
        this.G_bar[i] = this.G_bar[j];
        this.G_bar[j] = _7;
    }

    /* access modifiers changed from: 0000 */
    public void reconstruct_gradient() {
        if (this.active_size != this.f488l) {
            int nr_free = 0;
            for (int j = this.active_size; j < this.f488l; j++) {
                this.f485G[j] = this.G_bar[j] + this.f489p[j];
            }
            for (int j2 = 0; j2 < this.active_size; j2++) {
                if (is_free(j2)) {
                    nr_free++;
                }
            }
            if (nr_free * 2 < this.active_size) {
                svm.info("\nWarning: using -h 0 may be faster\n");
            }
            if (this.f488l * nr_free > this.active_size * 2 * (this.f488l - this.active_size)) {
                for (int i = this.active_size; i < this.f488l; i++) {
                    float[] Q_i = this.f486Q.get_Q(i, this.active_size);
                    for (int j3 = 0; j3 < this.active_size; j3++) {
                        if (is_free(j3)) {
                            double[] dArr = this.f485G;
                            dArr[i] = dArr[i] + (this.alpha[j3] * ((double) Q_i[j3]));
                        }
                    }
                }
            } else {
                for (int i2 = 0; i2 < this.active_size; i2++) {
                    if (is_free(i2)) {
                        float[] Q_i2 = this.f486Q.get_Q(i2, this.f488l);
                        double alpha_i = this.alpha[i2];
                        for (int j4 = this.active_size; j4 < this.f488l; j4++) {
                            double[] dArr2 = this.f485G;
                            dArr2[j4] = dArr2[j4] + (((double) Q_i2[j4]) * alpha_i);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x034e  */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x0356  */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x0388 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:131:0x02f5 A[EDGE_INSN: B:131:0x02f5->B:92:0x02f5 ?: BREAK  
    EDGE_INSN: B:131:0x02f5->B:92:0x02f5 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x02d1 A[LOOP:9: B:89:0x02cb->B:91:0x02d1, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x030f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void Solve(int r47, libsvm.QMatrix r48, double[] r49, byte[] r50, double[] r51, double r52, double r54, double r56, libsvm.Solver.SolutionInfo r58, int r59) {
        /*
            r46 = this;
            r0 = r46
            r1 = r47
            r2 = r48
            r3 = r52
            r5 = r54
            r7 = r58
            r0.f488l = r1
            r0.f486Q = r2
            double[] r8 = r48.get_QD()
            r0.f487QD = r8
            java.lang.Object r8 = r49.clone()
            double[] r8 = (double[]) r8
            r0.f489p = r8
            java.lang.Object r8 = r50.clone()
            byte[] r8 = (byte[]) r8
            r0.f490y = r8
            java.lang.Object r8 = r51.clone()
            double[] r8 = (double[]) r8
            r0.alpha = r8
            r0.f484Cp = r3
            r0.f483Cn = r5
            r8 = r56
            r0.eps = r8
            r10 = 0
            r0.unshrink = r10
            byte[] r11 = new byte[r1]
            r0.alpha_status = r11
            r11 = 0
        L_0x003e:
            if (r11 >= r1) goto L_0x0046
            r0.update_alpha_status(r11)
            int r11 = r11 + 1
            goto L_0x003e
        L_0x0046:
            int[] r11 = new int[r1]
            r0.active_set = r11
            r11 = 0
        L_0x004b:
            if (r11 >= r1) goto L_0x0054
            int[] r12 = r0.active_set
            r12[r11] = r11
            int r11 = r11 + 1
            goto L_0x004b
        L_0x0054:
            r0.active_size = r1
            double[] r11 = new double[r1]
            r0.f485G = r11
            double[] r11 = new double[r1]
            r0.G_bar = r11
            r11 = 0
        L_0x005f:
            r12 = 0
            if (r11 >= r1) goto L_0x0072
            double[] r14 = r0.f485G
            double[] r15 = r0.f489p
            r16 = r15[r11]
            r14[r11] = r16
            double[] r14 = r0.G_bar
            r14[r11] = r12
            int r11 = r11 + 1
            goto L_0x005f
        L_0x0072:
            r11 = 0
        L_0x0073:
            if (r11 >= r1) goto L_0x00c6
            boolean r14 = r0.is_lower_bound(r11)
            if (r14 != 0) goto L_0x00be
            float[] r14 = r2.get_Q(r11, r1)
            double[] r15 = r0.alpha
            r16 = r15[r11]
            r15 = 0
        L_0x0084:
            if (r15 >= r1) goto L_0x009d
            double[] r12 = r0.f485G
            r20 = r12[r15]
            r13 = r14[r15]
            r23 = r11
            double r10 = (double) r13
            double r10 = r10 * r16
            double r20 = r20 + r10
            r12[r15] = r20
            int r15 = r15 + 1
            r11 = r23
            r10 = 0
            r12 = 0
            goto L_0x0084
        L_0x009d:
            r23 = r11
            boolean r10 = r0.is_upper_bound(r11)
            if (r10 == 0) goto L_0x00be
            r10 = 0
        L_0x00a6:
            if (r10 >= r1) goto L_0x00be
            double[] r12 = r0.G_bar
            r20 = r12[r10]
            double r23 = r0.get_C(r11)
            r13 = r14[r10]
            double r8 = (double) r13
            double r23 = r23 * r8
            double r20 = r20 + r23
            r12[r10] = r20
            int r10 = r10 + 1
            r8 = r56
            goto L_0x00a6
        L_0x00be:
            int r11 = r11 + 1
            r8 = r56
            r10 = 0
            r12 = 0
            goto L_0x0073
        L_0x00c6:
            r8 = 0
            r9 = 1000(0x3e8, float:1.401E-42)
            int r10 = java.lang.Math.min(r1, r9)
            r11 = 1
            int r10 = r10 + r11
            r12 = 2
            int[] r12 = new int[r12]
        L_0x00d2:
            int r10 = r10 + -1
            if (r10 != 0) goto L_0x00e4
            int r10 = java.lang.Math.min(r1, r9)
            if (r59 == 0) goto L_0x00df
            r46.do_shrinking()
        L_0x00df:
            java.lang.String r14 = "."
            libsvm.svm.info(r14)
        L_0x00e4:
            int r14 = r0.select_working_set(r12)
            if (r14 == 0) goto L_0x015a
            r46.reconstruct_gradient()
            r0.active_size = r1
            java.lang.String r14 = "*"
            libsvm.svm.info(r14)
            int r14 = r0.select_working_set(r12)
            if (r14 == 0) goto L_0x0156
            double r14 = r46.calculate_rho()
            r7.rho = r14
            r14 = 0
            r9 = 0
        L_0x0104:
            if (r9 >= r1) goto L_0x011b
            double[] r11 = r0.alpha
            r16 = r11[r9]
            double[] r11 = r0.f485G
            r18 = r11[r9]
            double[] r11 = r0.f489p
            r20 = r11[r9]
            double r18 = r18 + r20
            double r16 = r16 * r18
            double r14 = r14 + r16
            int r9 = r9 + 1
            goto L_0x0104
        L_0x011b:
            r16 = 4611686018427387904(0x4000000000000000, double:2.0)
            r26 = r9
            r25 = r10
            double r9 = r14 / r16
            r7.obj = r9
            r22 = 0
        L_0x0127:
            r9 = r22
            if (r9 >= r1) goto L_0x0138
            int[] r10 = r0.active_set
            r10 = r10[r9]
            double[] r11 = r0.alpha
            r14 = r11[r9]
            r51[r10] = r14
            int r22 = r9 + 1
            goto L_0x0127
        L_0x0138:
            r7.upper_bound_p = r3
            r7.upper_bound_n = r5
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "\noptimization finished, #iter = "
            r9.append(r10)
            r9.append(r8)
            java.lang.String r10 = "\n"
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            libsvm.svm.info(r9)
            return
        L_0x0156:
            r25 = r10
            r10 = 1
            goto L_0x015c
        L_0x015a:
            r25 = r10
        L_0x015c:
            r14 = 0
            r15 = r12[r14]
            r9 = r12[r11]
            int r8 = r8 + 1
            int r11 = r0.active_size
            float[] r11 = r2.get_Q(r15, r11)
            int r14 = r0.active_size
            float[] r14 = r2.get_Q(r9, r14)
            double r20 = r0.get_C(r15)
            double r22 = r0.get_C(r9)
            double[] r3 = r0.alpha
            r25 = r3[r15]
            double[] r3 = r0.alpha
            r27 = r3[r9]
            byte[] r3 = r0.f490y
            byte r3 = r3[r15]
            byte[] r4 = r0.f490y
            byte r4 = r4[r9]
            r17 = 1073741824(0x40000000, float:2.0)
            if (r3 == r4) goto L_0x022b
            double[] r3 = r0.f487QD
            r29 = r3[r15]
            double[] r3 = r0.f487QD
            r31 = r3[r9]
            double r29 = r29 + r31
            r3 = r11[r9]
            float r3 = r3 * r17
            double r3 = (double) r3
            double r29 = r29 + r3
            r3 = 0
            int r17 = (r29 > r3 ? 1 : (r29 == r3 ? 0 : -1))
            if (r17 > 0) goto L_0x01a7
            r29 = 4427486594234968593(0x3d719799812dea11, double:1.0E-12)
        L_0x01a7:
            double[] r3 = r0.f485G
            r4 = r3[r15]
            double r3 = -r4
            double[] r5 = r0.f485G
            r31 = r5[r9]
            double r3 = r3 - r31
            double r3 = r3 / r29
            double[] r5 = r0.alpha
            r31 = r5[r15]
            double[] r5 = r0.alpha
            r33 = r5[r9]
            double r5 = r31 - r33
            double[] r7 = r0.alpha
            r31 = r7[r15]
            double r31 = r31 + r3
            r7[r15] = r31
            double[] r7 = r0.alpha
            r31 = r7[r9]
            double r31 = r31 + r3
            r7[r9] = r31
            r17 = 0
            int r7 = (r5 > r17 ? 1 : (r5 == r17 ? 0 : -1))
            if (r7 <= 0) goto L_0x01e7
            double[] r7 = r0.alpha
            r31 = r7[r9]
            int r7 = (r31 > r17 ? 1 : (r31 == r17 ? 0 : -1))
            if (r7 >= 0) goto L_0x01e4
            double[] r7 = r0.alpha
            r7[r9] = r17
            double[] r7 = r0.alpha
            r7[r15] = r5
        L_0x01e4:
            r35 = r3
            goto L_0x01fd
        L_0x01e7:
            double[] r7 = r0.alpha
            r31 = r7[r15]
            int r7 = (r31 > r17 ? 1 : (r31 == r17 ? 0 : -1))
            if (r7 >= 0) goto L_0x01fb
            double[] r7 = r0.alpha
            r7[r15] = r17
            double[] r7 = r0.alpha
            r35 = r3
            double r3 = -r5
            r7[r9] = r3
            goto L_0x01fd
        L_0x01fb:
            r35 = r3
        L_0x01fd:
            r3 = 0
            double r3 = r20 - r22
            int r3 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r3 <= 0) goto L_0x0217
            double[] r3 = r0.alpha
            r31 = r3[r15]
            int r3 = (r31 > r20 ? 1 : (r31 == r20 ? 0 : -1))
            if (r3 <= 0) goto L_0x0229
            double[] r3 = r0.alpha
            r3[r15] = r20
            double[] r3 = r0.alpha
            double r31 = r20 - r5
            r3[r9] = r31
            goto L_0x0229
        L_0x0217:
            double[] r3 = r0.alpha
            r31 = r3[r9]
            int r3 = (r31 > r22 ? 1 : (r31 == r22 ? 0 : -1))
            if (r3 <= 0) goto L_0x0229
            double[] r3 = r0.alpha
            r3[r9] = r22
            double[] r3 = r0.alpha
            double r31 = r22 + r5
            r3[r15] = r31
        L_0x0229:
            goto L_0x02a9
        L_0x022b:
            double[] r3 = r0.f487QD
            r4 = r3[r15]
            double[] r3 = r0.f487QD
            r6 = r3[r9]
            double r4 = r4 + r6
            r3 = r11[r9]
            float r3 = r3 * r17
            double r6 = (double) r3
            double r4 = r4 - r6
            r6 = 0
            int r3 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r3 > 0) goto L_0x0245
            r4 = 4427486594234968593(0x3d719799812dea11, double:1.0E-12)
        L_0x0245:
            double[] r3 = r0.f485G
            r6 = r3[r15]
            double[] r3 = r0.f485G
            r29 = r3[r9]
            double r6 = r6 - r29
            double r6 = r6 / r4
            double[] r3 = r0.alpha
            r29 = r3[r15]
            double[] r3 = r0.alpha
            r31 = r3[r9]
            double r29 = r29 + r31
            double[] r3 = r0.alpha
            r31 = r3[r15]
            double r31 = r31 - r6
            r3[r15] = r31
            double[] r3 = r0.alpha
            r31 = r3[r9]
            double r31 = r31 + r6
            r3[r9] = r31
            int r3 = (r29 > r20 ? 1 : (r29 == r20 ? 0 : -1))
            if (r3 <= 0) goto L_0x0281
            double[] r3 = r0.alpha
            r31 = r3[r15]
            int r3 = (r31 > r20 ? 1 : (r31 == r20 ? 0 : -1))
            if (r3 <= 0) goto L_0x0293
            double[] r3 = r0.alpha
            r3[r15] = r20
            double[] r3 = r0.alpha
            double r31 = r29 - r20
            r3[r9] = r31
            goto L_0x0293
        L_0x0281:
            double[] r3 = r0.alpha
            r31 = r3[r9]
            r17 = 0
            int r3 = (r31 > r17 ? 1 : (r31 == r17 ? 0 : -1))
            if (r3 >= 0) goto L_0x0293
            double[] r3 = r0.alpha
            r3[r9] = r17
            double[] r3 = r0.alpha
            r3[r15] = r29
        L_0x0293:
            int r3 = (r29 > r22 ? 1 : (r29 == r22 ? 0 : -1))
            if (r3 <= 0) goto L_0x02ac
            double[] r3 = r0.alpha
            r31 = r3[r9]
            int r3 = (r31 > r22 ? 1 : (r31 == r22 ? 0 : -1))
            if (r3 <= 0) goto L_0x02a9
            double[] r3 = r0.alpha
            r3[r9] = r22
            double[] r3 = r0.alpha
            double r31 = r29 - r22
            r3[r15] = r31
        L_0x02a9:
            r17 = 0
            goto L_0x02be
        L_0x02ac:
            double[] r3 = r0.alpha
            r31 = r3[r15]
            r17 = 0
            int r3 = (r31 > r17 ? 1 : (r31 == r17 ? 0 : -1))
            if (r3 >= 0) goto L_0x02be
            double[] r3 = r0.alpha
            r3[r15] = r17
            double[] r3 = r0.alpha
            r3[r9] = r29
        L_0x02be:
            double[] r3 = r0.alpha
            r4 = r3[r15]
            double r4 = r4 - r25
            double[] r3 = r0.alpha
            r6 = r3[r9]
            double r6 = r6 - r27
            r3 = 0
        L_0x02cb:
            r37 = r8
            int r8 = r0.active_size
            if (r3 >= r8) goto L_0x02f5
            double[] r8 = r0.f485G
            r29 = r8[r3]
            r38 = r10
            r10 = r11[r3]
            r39 = r11
            double r10 = (double) r10
            double r10 = r10 * r4
            r40 = r4
            r4 = r14[r3]
            double r4 = (double) r4
            double r4 = r4 * r6
            double r10 = r10 + r4
            double r29 = r29 + r10
            r8[r3] = r29
            int r3 = r3 + 1
            r8 = r37
            r10 = r38
            r11 = r39
            r4 = r40
            goto L_0x02cb
        L_0x02f5:
            r40 = r4
            r38 = r10
            r39 = r11
            boolean r3 = r0.is_upper_bound(r15)
            boolean r4 = r0.is_upper_bound(r9)
            r0.update_alpha_status(r15)
            r0.update_alpha_status(r9)
            boolean r5 = r0.is_upper_bound(r15)
            if (r3 == r5) goto L_0x034e
            float[] r11 = r2.get_Q(r15, r1)
            if (r3 == 0) goto L_0x0331
            r5 = 0
        L_0x0316:
            if (r5 >= r1) goto L_0x032c
            double[] r8 = r0.G_bar
            r29 = r8[r5]
            r10 = r11[r5]
            r42 = r6
            double r6 = (double) r10
            double r6 = r6 * r20
            double r29 = r29 - r6
            r8[r5] = r29
            int r5 = r5 + 1
            r6 = r42
            goto L_0x0316
        L_0x032c:
            r42 = r6
            r44 = r11
            goto L_0x034b
        L_0x0331:
            r42 = r6
            r5 = 0
        L_0x0334:
            if (r5 >= r1) goto L_0x0349
            double[] r6 = r0.G_bar
            r7 = r6[r5]
            r10 = r11[r5]
            r44 = r11
            double r10 = (double) r10
            double r10 = r10 * r20
            double r7 = r7 + r10
            r6[r5] = r7
            int r5 = r5 + 1
            r11 = r44
            goto L_0x0334
        L_0x0349:
            r44 = r11
        L_0x034b:
            r39 = r44
            goto L_0x0350
        L_0x034e:
            r42 = r6
        L_0x0350:
            boolean r5 = r0.is_upper_bound(r9)
            if (r4 == r5) goto L_0x0388
            float[] r5 = r2.get_Q(r9, r1)
            if (r4 == 0) goto L_0x0374
            r6 = 0
        L_0x035d:
            if (r6 >= r1) goto L_0x0388
            double[] r7 = r0.G_bar
            r10 = r7[r6]
            r8 = r5[r6]
            r45 = r3
            double r2 = (double) r8
            double r2 = r2 * r22
            double r10 = r10 - r2
            r7[r6] = r10
            int r6 = r6 + 1
            r3 = r45
            r2 = r48
            goto L_0x035d
        L_0x0374:
            r45 = r3
            r2 = 0
        L_0x0377:
            if (r2 >= r1) goto L_0x0388
            double[] r3 = r0.G_bar
            r6 = r3[r2]
            r8 = r5[r2]
            double r10 = (double) r8
            double r10 = r10 * r22
            double r6 = r6 + r10
            r3[r2] = r6
            int r2 = r2 + 1
            goto L_0x0377
        L_0x0388:
            r8 = r37
            r10 = r38
            r2 = r48
            r3 = r52
            r5 = r54
            r7 = r58
            r9 = 1000(0x3e8, float:1.401E-42)
            r11 = 1
            goto L_0x00d2
        */
        throw new UnsupportedOperationException("Method not decompiled: libsvm.Solver.Solve(int, libsvm.QMatrix, double[], byte[], double[], double, double, double, libsvm.Solver$SolutionInfo, int):void");
    }

    /* access modifiers changed from: 0000 */
    public int select_working_set(int[] working_set) {
        byte b;
        float[] Q_i;
        int i;
        double Gmax;
        double obj_diff_min;
        double obj_diff;
        double obj_diff_min2;
        double obj_diff2;
        int Gmax_idx;
        int Gmax_idx2 = -1;
        double Gmax2 = Double.NEGATIVE_INFINITY;
        int t = 0;
        while (true) {
            b = 1;
            if (t >= this.active_size) {
                break;
            }
            if (this.f490y[t] == 1) {
                if (!is_upper_bound(t) && (-this.f485G[t]) >= Gmax2) {
                    Gmax2 = -this.f485G[t];
                    Gmax_idx = t;
                }
                t++;
            } else {
                if (is_lower_bound(t) == 0 && this.f485G[t] >= Gmax2) {
                    Gmax2 = this.f485G[t];
                    Gmax_idx = t;
                }
                t++;
            }
            Gmax_idx2 = Gmax_idx;
            t++;
        }
        int i2 = Gmax_idx2;
        float[] Q_i2 = null;
        if (i2 != -1) {
            Q_i2 = this.f486Q.get_Q(i2, this.active_size);
        }
        double obj_diff_min3 = Double.POSITIVE_INFINITY;
        int Gmin_idx = -1;
        double Gmax22 = Double.NEGATIVE_INFINITY;
        int j = 0;
        while (j < this.active_size) {
            if (this.f490y[j] != b) {
                Gmax = Gmax2;
                obj_diff_min = obj_diff_min3;
                if (!is_upper_bound(j)) {
                    double grad_diff = Gmax - this.f485G[j];
                    if ((-this.f485G[j]) >= Gmax22) {
                        Gmax22 = -this.f485G[j];
                    }
                    if (grad_diff > 0.0d) {
                        i = i2;
                        Q_i = Q_i2;
                        double quad_coef = this.f487QD[i2] + this.f487QD[j] + (((double) this.f490y[i2]) * 2.0d * ((double) Q_i2[j]));
                        if (quad_coef > 0.0d) {
                            obj_diff = (-(grad_diff * grad_diff)) / quad_coef;
                        } else {
                            obj_diff = (-(grad_diff * grad_diff)) / 1.0E-12d;
                        }
                        if (obj_diff <= obj_diff_min) {
                            Gmin_idx = j;
                            obj_diff_min3 = obj_diff;
                            j++;
                            Gmax2 = Gmax;
                            i2 = i;
                            Q_i2 = Q_i;
                            b = 1;
                        }
                    } else {
                        i = i2;
                        Q_i = Q_i2;
                    }
                    obj_diff_min3 = obj_diff_min;
                    j++;
                    Gmax2 = Gmax;
                    i2 = i;
                    Q_i2 = Q_i;
                    b = 1;
                } else {
                    i = i2;
                    Q_i = Q_i2;
                }
            } else if (!is_lower_bound(j)) {
                double grad_diff2 = Gmax2 + this.f485G[j];
                if (this.f485G[j] >= Gmax22) {
                    Gmax22 = this.f485G[j];
                }
                if (grad_diff2 > 0.0d) {
                    Gmax = Gmax2;
                    obj_diff_min2 = obj_diff_min3;
                    double quad_coef2 = (this.f487QD[i2] + this.f487QD[j]) - ((((double) this.f490y[i2]) * 2.0d) * ((double) Q_i2[j]));
                    if (quad_coef2 > 0.0d) {
                        obj_diff2 = (-(grad_diff2 * grad_diff2)) / quad_coef2;
                    } else {
                        obj_diff2 = (-(grad_diff2 * grad_diff2)) / 1.0E-12d;
                    }
                    if (obj_diff2 <= obj_diff_min2) {
                        Gmin_idx = j;
                        obj_diff_min3 = obj_diff2;
                        i = i2;
                        Q_i = Q_i2;
                        j++;
                        Gmax2 = Gmax;
                        i2 = i;
                        Q_i2 = Q_i;
                        b = 1;
                    }
                } else {
                    Gmax = Gmax2;
                    obj_diff_min2 = obj_diff_min3;
                }
                obj_diff_min3 = obj_diff_min2;
                i = i2;
                Q_i = Q_i2;
                j++;
                Gmax2 = Gmax;
                i2 = i;
                Q_i2 = Q_i;
                b = 1;
            } else {
                Gmax = Gmax2;
                obj_diff_min = obj_diff_min3;
                i = i2;
                Q_i = Q_i2;
            }
            obj_diff_min3 = obj_diff_min;
            j++;
            Gmax2 = Gmax;
            i2 = i;
            Q_i2 = Q_i;
            b = 1;
        }
        float[] fArr = Q_i2;
        double d = obj_diff_min3;
        if (Gmax2 + Gmax22 < this.eps) {
            return 1;
        }
        working_set[0] = Gmax_idx2;
        working_set[1] = Gmin_idx;
        return 0;
    }

    private boolean be_shrunk(int i, double Gmax1, double Gmax2) {
        boolean z = false;
        if (is_upper_bound(i)) {
            if (this.f490y[i] == 1) {
                if ((-this.f485G[i]) > Gmax1) {
                    z = true;
                }
                return z;
            }
            if ((-this.f485G[i]) > Gmax2) {
                z = true;
            }
            return z;
        } else if (!is_lower_bound(i)) {
            return false;
        } else {
            if (this.f490y[i] == 1) {
                if (this.f485G[i] > Gmax2) {
                    z = true;
                }
                return z;
            }
            if (this.f485G[i] > Gmax1) {
                z = true;
            }
            return z;
        }
    }

    /* access modifiers changed from: 0000 */
    public void do_shrinking() {
        double Gmax1 = Double.NEGATIVE_INFINITY;
        double Gmax2 = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < this.active_size; i++) {
            if (this.f490y[i] == 1) {
                if (!is_upper_bound(i) && (-this.f485G[i]) >= Gmax1) {
                    Gmax1 = -this.f485G[i];
                }
                if (!is_lower_bound(i) && this.f485G[i] >= Gmax2) {
                    Gmax2 = this.f485G[i];
                }
            } else {
                if (!is_upper_bound(i) && (-this.f485G[i]) >= Gmax2) {
                    Gmax2 = -this.f485G[i];
                }
                if (!is_lower_bound(i) && this.f485G[i] >= Gmax1) {
                    Gmax1 = this.f485G[i];
                }
            }
        }
        if (!this.unshrink && Gmax1 + Gmax2 <= this.eps * 10.0d) {
            this.unshrink = true;
            reconstruct_gradient();
            this.active_size = this.f488l;
        }
        for (int i2 = 0; i2 < this.active_size; i2++) {
            if (be_shrunk(i2, Gmax1, Gmax2)) {
                this.active_size--;
                while (true) {
                    if (this.active_size <= i2) {
                        break;
                    }
                    if (!be_shrunk(this.active_size, Gmax1, Gmax2)) {
                        swap_index(i2, this.active_size);
                        break;
                    }
                    this.active_size--;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public double calculate_rho() {
        int nr_free = 0;
        double ub = INF;
        double lb = Double.NEGATIVE_INFINITY;
        double sum_free = 0.0d;
        for (int i = 0; i < this.active_size; i++) {
            double yG = ((double) this.f490y[i]) * this.f485G[i];
            if (is_lower_bound(i)) {
                if (this.f490y[i] > 0) {
                    ub = Math.min(ub, yG);
                } else {
                    lb = Math.max(lb, yG);
                }
            } else if (!is_upper_bound(i)) {
                nr_free++;
                sum_free += yG;
            } else if (this.f490y[i] < 0) {
                ub = Math.min(ub, yG);
            } else {
                lb = Math.max(lb, yG);
            }
        }
        if (nr_free > 0) {
            return sum_free / ((double) nr_free);
        }
        return (ub + lb) / 2.0d;
    }
}
