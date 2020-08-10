package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;

public abstract class EmbeddedRungeKuttaFieldIntegrator<T extends RealFieldElement<T>> extends AdaptiveStepsizeFieldIntegrator<T> implements FieldButcherArrayProvider<T> {

    /* renamed from: a */
    private final T[][] f685a = getA();

    /* renamed from: b */
    private final T[] f686b = getB();

    /* renamed from: c */
    private final T[] f687c = getC();
    private final T exp;
    private final int fsal;
    private T maxGrowth;
    private T minReduction;
    private T safety;

    /* access modifiers changed from: protected */
    public abstract RungeKuttaFieldStepInterpolator<T> createInterpolator(boolean z, T[][] tArr, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative2, FieldEquationsMapper<T> fieldEquationsMapper);

    /* access modifiers changed from: protected */
    public abstract T estimateError(T[][] tArr, T[] tArr2, T[] tArr3, T t);

    public abstract int getOrder();

    protected EmbeddedRungeKuttaFieldIntegrator(Field<T> field, String name, int fsal2, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(field, name, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        this.fsal = fsal2;
        this.exp = (RealFieldElement) ((RealFieldElement) field.getOne()).divide((double) (-getOrder()));
        setSafety((RealFieldElement) ((RealFieldElement) field.getZero()).add(0.9d));
        setMinReduction((RealFieldElement) ((RealFieldElement) field.getZero()).add(0.2d));
        setMaxGrowth((RealFieldElement) ((RealFieldElement) field.getZero()).add(10.0d));
    }

    protected EmbeddedRungeKuttaFieldIntegrator(Field<T> field, String name, int fsal2, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(field, name, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        this.fsal = fsal2;
        this.exp = (RealFieldElement) ((RealFieldElement) field.getOne()).divide((double) (-getOrder()));
        setSafety((RealFieldElement) ((RealFieldElement) field.getZero()).add(0.9d));
        setMinReduction((RealFieldElement) ((RealFieldElement) field.getZero()).add(0.2d));
        setMaxGrowth((RealFieldElement) ((RealFieldElement) field.getZero()).add(10.0d));
    }

    /* access modifiers changed from: protected */
    public T fraction(int p, int q) {
        return (RealFieldElement) ((RealFieldElement) ((RealFieldElement) getField().getOne()).multiply(p)).divide((double) q);
    }

    /* access modifiers changed from: protected */
    public T fraction(double p, double q) {
        return (RealFieldElement) ((RealFieldElement) ((RealFieldElement) getField().getOne()).multiply(p)).divide(q);
    }

    public T getSafety() {
        return this.safety;
    }

    public void setSafety(T safety2) {
        this.safety = safety2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:70:0x039f, code lost:
        if (((org.apache.commons.math3.RealFieldElement) r1.subtract(r8)).getReal() >= 0.0d) goto L_0x03a1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x03a3, code lost:
        r0 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x03b5, code lost:
        if (((org.apache.commons.math3.RealFieldElement) r1.subtract(r8)).getReal() <= 0.0d) goto L_0x03a1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.commons.math3.ode.FieldODEStateAndDerivative<T> integrate(org.apache.commons.math3.ode.FieldExpandableODE<T> r34, org.apache.commons.math3.ode.FieldODEState<T> r35, T r36) throws org.apache.commons.math3.exception.NumberIsTooSmallException, org.apache.commons.math3.exception.DimensionMismatchException, org.apache.commons.math3.exception.MaxCountExceededException, org.apache.commons.math3.exception.NoBracketingException {
        /*
            r33 = this;
            r6 = r33
            r7 = r35
            r8 = r36
            r6.sanityChecks(r7, r8)
            org.apache.commons.math3.RealFieldElement r9 = r35.getTime()
            org.apache.commons.math3.ode.FieldEquationsMapper r0 = r34.getMapper()
            org.apache.commons.math3.RealFieldElement[] r10 = r0.mapState(r7)
            r11 = r34
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r0 = r6.initIntegration(r11, r9, r10, r8)
            r6.setStepStart(r0)
            org.apache.commons.math3.RealFieldElement r0 = r35.getTime()
            java.lang.Object r0 = r8.subtract(r0)
            org.apache.commons.math3.RealFieldElement r0 = (org.apache.commons.math3.RealFieldElement) r0
            double r0 = r0.getReal()
            r12 = 0
            int r0 = (r0 > r12 ? 1 : (r0 == r12 ? 0 : -1))
            r14 = 0
            r15 = 1
            if (r0 <= 0) goto L_0x0036
            r0 = 1
            goto L_0x0037
        L_0x0036:
            r0 = 0
        L_0x0037:
            r5 = r0
            T[] r0 = r6.f687c
            int r0 = r0.length
            int r4 = r0 + 1
            r0 = r10
            org.apache.commons.math3.Field r1 = r33.getField()
            r2 = -1
            java.lang.Object[][] r1 = org.apache.commons.math3.util.MathArrays.buildArray(r1, r4, r2)
            r3 = r1
            org.apache.commons.math3.RealFieldElement[][] r3 = (org.apache.commons.math3.RealFieldElement[][]) r3
            org.apache.commons.math3.Field r1 = r33.getField()
            int r2 = r10.length
            java.lang.Object[] r1 = org.apache.commons.math3.util.MathArrays.buildArray(r1, r2)
            r2 = r1
            org.apache.commons.math3.RealFieldElement[] r2 = (org.apache.commons.math3.RealFieldElement[]) r2
            org.apache.commons.math3.Field r1 = r33.getField()
            java.lang.Object r1 = r1.getZero()
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            r16 = 1
            r6.setIsLastStep(r14)
        L_0x0065:
            org.apache.commons.math3.Field r15 = r33.getField()
            java.lang.Object r15 = r15.getZero()
            org.apache.commons.math3.RealFieldElement r15 = (org.apache.commons.math3.RealFieldElement) r15
            r12 = 4621819117588971520(0x4024000000000000, double:10.0)
            java.lang.Object r12 = r15.add(r12)
            org.apache.commons.math3.RealFieldElement r12 = (org.apache.commons.math3.RealFieldElement) r12
            r13 = r0
            r15 = r1
        L_0x0079:
            r0 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            java.lang.Object r19 = r12.subtract(r0)
            r0 = r19
            org.apache.commons.math3.RealFieldElement r0 = (org.apache.commons.math3.RealFieldElement) r0
            double r0 = r0.getReal()
            r17 = 0
            int r0 = (r0 > r17 ? 1 : (r0 == r17 ? 0 : -1))
            if (r0 < 0) goto L_0x02c5
            org.apache.commons.math3.ode.FieldEquationsMapper r0 = r34.getMapper()
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r1 = r33.getStepStart()
            org.apache.commons.math3.RealFieldElement[] r13 = r0.mapState(r1)
            org.apache.commons.math3.ode.FieldEquationsMapper r0 = r34.getMapper()
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r1 = r33.getStepStart()
            org.apache.commons.math3.RealFieldElement[] r0 = r0.mapDerivative(r1)
            r3[r14] = r0
            if (r16 == 0) goto L_0x0149
            org.apache.commons.math3.Field r0 = r33.getField()
            int r1 = r6.mainSetDimension
            java.lang.Object[] r0 = org.apache.commons.math3.util.MathArrays.buildArray(r0, r1)
            r1 = r0
            org.apache.commons.math3.RealFieldElement[] r1 = (org.apache.commons.math3.RealFieldElement[]) r1
            double[] r0 = r6.vecAbsoluteTolerance
            if (r0 != 0) goto L_0x00f1
            r0 = 0
        L_0x00bb:
            int r14 = r1.length
            if (r0 >= r14) goto L_0x00e8
            r14 = r13[r0]
            java.lang.Object r14 = r14.abs()
            org.apache.commons.math3.RealFieldElement r14 = (org.apache.commons.math3.RealFieldElement) r14
            r24 = r2
            r23 = r3
            double r2 = r6.scalRelativeTolerance
            java.lang.Object r2 = r14.multiply(r2)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            r25 = r4
            double r3 = r6.scalAbsoluteTolerance
            java.lang.Object r2 = r2.add(r3)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            r1[r0] = r2
            int r0 = r0 + 1
            r3 = r23
            r2 = r24
            r4 = r25
            r14 = 0
            goto L_0x00bb
        L_0x00e8:
            r24 = r2
            r23 = r3
            r25 = r4
            r26 = r15
            goto L_0x0122
        L_0x00f1:
            r24 = r2
            r23 = r3
            r25 = r4
            r0 = 0
        L_0x00f8:
            int r2 = r1.length
            if (r0 >= r2) goto L_0x0120
            r2 = r13[r0]
            java.lang.Object r2 = r2.abs()
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            double[] r3 = r6.vecRelativeTolerance
            r26 = r15
            r14 = r3[r0]
            java.lang.Object r2 = r2.multiply(r14)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            double[] r3 = r6.vecAbsoluteTolerance
            r14 = r3[r0]
            java.lang.Object r2 = r2.add(r14)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            r1[r0] = r2
            int r0 = r0 + 1
            r15 = r26
            goto L_0x00f8
        L_0x0120:
            r26 = r15
        L_0x0122:
            int r2 = r33.getOrder()
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r4 = r33.getStepStart()
            org.apache.commons.math3.ode.FieldEquationsMapper r14 = r34.getMapper()
            r27 = r12
            r11 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r0 = r6
            r15 = r1
            r1 = r5
            r3 = r24
            r12 = r3
            r11 = r23
            r3 = r15
            r7 = r25
            r28 = r9
            r9 = r5
            r5 = r14
            org.apache.commons.math3.RealFieldElement r0 = r0.initializeStep(r1, r2, r3, r4, r5)
            r16 = 0
            r15 = r0
            goto L_0x0153
        L_0x0149:
            r11 = r3
            r7 = r4
            r28 = r9
            r27 = r12
            r26 = r15
            r12 = r2
            r9 = r5
        L_0x0153:
            r6.setStepSize(r15)
            if (r9 == 0) goto L_0x018c
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r0 = r33.getStepStart()
            org.apache.commons.math3.RealFieldElement r0 = r0.getTime()
            org.apache.commons.math3.RealFieldElement r1 = r33.getStepSize()
            java.lang.Object r0 = r0.add(r1)
            org.apache.commons.math3.RealFieldElement r0 = (org.apache.commons.math3.RealFieldElement) r0
            java.lang.Object r0 = r0.subtract(r8)
            org.apache.commons.math3.RealFieldElement r0 = (org.apache.commons.math3.RealFieldElement) r0
            double r0 = r0.getReal()
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 < 0) goto L_0x01bf
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r0 = r33.getStepStart()
            org.apache.commons.math3.RealFieldElement r0 = r0.getTime()
            java.lang.Object r0 = r8.subtract(r0)
            org.apache.commons.math3.RealFieldElement r0 = (org.apache.commons.math3.RealFieldElement) r0
            r6.setStepSize(r0)
            goto L_0x01bf
        L_0x018c:
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r0 = r33.getStepStart()
            org.apache.commons.math3.RealFieldElement r0 = r0.getTime()
            org.apache.commons.math3.RealFieldElement r1 = r33.getStepSize()
            java.lang.Object r0 = r0.add(r1)
            org.apache.commons.math3.RealFieldElement r0 = (org.apache.commons.math3.RealFieldElement) r0
            java.lang.Object r0 = r0.subtract(r8)
            org.apache.commons.math3.RealFieldElement r0 = (org.apache.commons.math3.RealFieldElement) r0
            double r0 = r0.getReal()
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 > 0) goto L_0x01bf
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r0 = r33.getStepStart()
            org.apache.commons.math3.RealFieldElement r0 = r0.getTime()
            java.lang.Object r0 = r8.subtract(r0)
            org.apache.commons.math3.RealFieldElement r0 = (org.apache.commons.math3.RealFieldElement) r0
            r6.setStepSize(r0)
        L_0x01bf:
            r0 = 1
        L_0x01c0:
            if (r0 >= r7) goto L_0x0231
            r1 = 0
        L_0x01c3:
            int r2 = r10.length
            if (r1 >= r2) goto L_0x020c
            r2 = 0
            r3 = r11[r2]
            r3 = r3[r1]
            T[][] r4 = r6.f685a
            int r5 = r0 + -1
            r4 = r4[r5]
            r4 = r4[r2]
            java.lang.Object r2 = r3.multiply(r4)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            r3 = r2
            r2 = 1
        L_0x01db:
            if (r2 >= r0) goto L_0x01f7
            r4 = r11[r2]
            r4 = r4[r1]
            T[][] r5 = r6.f685a
            int r14 = r0 + -1
            r5 = r5[r14]
            r5 = r5[r2]
            java.lang.Object r4 = r4.multiply(r5)
            java.lang.Object r4 = r3.add(r4)
            r3 = r4
            org.apache.commons.math3.RealFieldElement r3 = (org.apache.commons.math3.RealFieldElement) r3
            int r2 = r2 + 1
            goto L_0x01db
        L_0x01f7:
            r2 = r13[r1]
            org.apache.commons.math3.RealFieldElement r4 = r33.getStepSize()
            java.lang.Object r4 = r4.multiply(r3)
            java.lang.Object r2 = r2.add(r4)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            r12[r1] = r2
            int r1 = r1 + 1
            goto L_0x01c3
        L_0x020c:
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r1 = r33.getStepStart()
            org.apache.commons.math3.RealFieldElement r1 = r1.getTime()
            org.apache.commons.math3.RealFieldElement r2 = r33.getStepSize()
            T[] r3 = r6.f687c
            int r4 = r0 + -1
            r3 = r3[r4]
            java.lang.Object r2 = r2.multiply(r3)
            java.lang.Object r1 = r1.add(r2)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            org.apache.commons.math3.RealFieldElement[] r1 = r6.computeDerivatives(r1, r12)
            r11[r0] = r1
            int r0 = r0 + 1
            goto L_0x01c0
        L_0x0231:
            r0 = 0
        L_0x0232:
            int r1 = r10.length
            if (r0 >= r1) goto L_0x0273
            r1 = 0
            r2 = r11[r1]
            r2 = r2[r0]
            T[] r3 = r6.f686b
            r3 = r3[r1]
            java.lang.Object r1 = r2.multiply(r3)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            r2 = r1
            r1 = 1
        L_0x0246:
            if (r1 >= r7) goto L_0x025e
            r3 = r11[r1]
            r3 = r3[r0]
            T[] r4 = r6.f686b
            r4 = r4[r1]
            java.lang.Object r3 = r3.multiply(r4)
            java.lang.Object r3 = r2.add(r3)
            r2 = r3
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            int r1 = r1 + 1
            goto L_0x0246
        L_0x025e:
            r1 = r13[r0]
            org.apache.commons.math3.RealFieldElement r3 = r33.getStepSize()
            java.lang.Object r3 = r3.multiply(r2)
            java.lang.Object r1 = r1.add(r3)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            r12[r0] = r1
            int r0 = r0 + 1
            goto L_0x0232
        L_0x0273:
            org.apache.commons.math3.RealFieldElement r0 = r33.getStepSize()
            org.apache.commons.math3.RealFieldElement r0 = r6.estimateError(r11, r13, r12, r0)
            r1 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            java.lang.Object r1 = r0.subtract(r1)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            double r1 = r1.getReal()
            r3 = 0
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 < 0) goto L_0x02b7
            T r1 = r6.maxGrowth
            T r2 = r6.minReduction
            T r3 = r6.safety
            T r4 = r6.exp
            java.lang.Object r4 = r0.pow(r4)
            java.lang.Object r3 = r3.multiply(r4)
            org.apache.commons.math3.RealFieldElement r3 = (org.apache.commons.math3.RealFieldElement) r3
            org.apache.commons.math3.RealFieldElement r2 = org.apache.commons.math3.util.MathUtils.max(r2, r3)
            org.apache.commons.math3.RealFieldElement r1 = org.apache.commons.math3.util.MathUtils.min(r1, r2)
            org.apache.commons.math3.RealFieldElement r2 = r33.getStepSize()
            java.lang.Object r2 = r2.multiply(r1)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            r3 = 0
            org.apache.commons.math3.RealFieldElement r15 = r6.filterStep(r2, r9, r3)
        L_0x02b7:
            r4 = r7
            r5 = r9
            r3 = r11
            r2 = r12
            r9 = r28
            r7 = r35
            r11 = r34
            r14 = 0
            r12 = r0
            goto L_0x0079
        L_0x02c5:
            r11 = r3
            r7 = r4
            r28 = r9
            r27 = r12
            r26 = r15
            r12 = r2
            r9 = r5
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r0 = r33.getStepStart()
            org.apache.commons.math3.RealFieldElement r0 = r0.getTime()
            org.apache.commons.math3.RealFieldElement r1 = r33.getStepSize()
            java.lang.Object r0 = r0.add(r1)
            r14 = r0
            org.apache.commons.math3.RealFieldElement r14 = (org.apache.commons.math3.RealFieldElement) r14
            int r0 = r6.fsal
            if (r0 < 0) goto L_0x02eb
            int r0 = r6.fsal
            r0 = r11[r0]
            goto L_0x02ef
        L_0x02eb:
            org.apache.commons.math3.RealFieldElement[] r0 = r6.computeDerivatives(r14, r12)
        L_0x02ef:
            r15 = r0
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r4 = new org.apache.commons.math3.ode.FieldODEStateAndDerivative
            r4.<init>(r14, r12, r15)
            int r0 = r10.length
            r5 = 0
            java.lang.System.arraycopy(r12, r5, r13, r5, r0)
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r3 = r33.getStepStart()
            org.apache.commons.math3.ode.FieldEquationsMapper r19 = r34.getMapper()
            r0 = r6
            r1 = r9
            r2 = r11
            r20 = 0
            r5 = r19
            org.apache.commons.math3.ode.nonstiff.RungeKuttaFieldStepInterpolator r0 = r0.createInterpolator(r1, r2, r3, r4, r5)
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r0 = r6.acceptStep(r0, r8)
            r6.setStepStart(r0)
            boolean r0 = r33.isLastStep()
            if (r0 != 0) goto L_0x03cf
            T r0 = r6.maxGrowth
            T r1 = r6.minReduction
            T r2 = r6.safety
            T r3 = r6.exp
            r5 = r27
            java.lang.Object r3 = r5.pow(r3)
            java.lang.Object r2 = r2.multiply(r3)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            org.apache.commons.math3.RealFieldElement r1 = org.apache.commons.math3.util.MathUtils.max(r1, r2)
            org.apache.commons.math3.RealFieldElement r0 = org.apache.commons.math3.util.MathUtils.min(r0, r1)
            org.apache.commons.math3.RealFieldElement r1 = r33.getStepSize()
            java.lang.Object r1 = r1.multiply(r0)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r2 = r33.getStepStart()
            org.apache.commons.math3.RealFieldElement r2 = r2.getTime()
            java.lang.Object r2 = r2.add(r1)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            if (r9 == 0) goto L_0x0364
            java.lang.Object r3 = r2.subtract(r8)
            org.apache.commons.math3.RealFieldElement r3 = (org.apache.commons.math3.RealFieldElement) r3
            double r21 = r3.getReal()
            r17 = 0
            int r3 = (r21 > r17 ? 1 : (r21 == r17 ? 0 : -1))
            if (r3 < 0) goto L_0x0362
        L_0x0360:
            r3 = 1
            goto L_0x0375
        L_0x0362:
            r3 = 0
            goto L_0x0375
        L_0x0364:
            r17 = 0
            java.lang.Object r3 = r2.subtract(r8)
            org.apache.commons.math3.RealFieldElement r3 = (org.apache.commons.math3.RealFieldElement) r3
            double r21 = r3.getReal()
            int r3 = (r21 > r17 ? 1 : (r21 == r17 ? 0 : -1))
            if (r3 > 0) goto L_0x0362
            goto L_0x0360
        L_0x0375:
            r29 = r0
            org.apache.commons.math3.RealFieldElement r0 = r6.filterStep(r1, r9, r3)
            r30 = r1
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r1 = r33.getStepStart()
            org.apache.commons.math3.RealFieldElement r1 = r1.getTime()
            java.lang.Object r1 = r1.add(r0)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            if (r9 == 0) goto L_0x03a5
            java.lang.Object r19 = r1.subtract(r8)
            r31 = r0
            r0 = r19
            org.apache.commons.math3.RealFieldElement r0 = (org.apache.commons.math3.RealFieldElement) r0
            double r21 = r0.getReal()
            r17 = 0
            int r0 = (r21 > r17 ? 1 : (r21 == r17 ? 0 : -1))
            if (r0 < 0) goto L_0x03a3
        L_0x03a1:
            r0 = 1
            goto L_0x03b8
        L_0x03a3:
            r0 = 0
            goto L_0x03b8
        L_0x03a5:
            r31 = r0
            r17 = 0
            java.lang.Object r0 = r1.subtract(r8)
            org.apache.commons.math3.RealFieldElement r0 = (org.apache.commons.math3.RealFieldElement) r0
            double r21 = r0.getReal()
            int r0 = (r21 > r17 ? 1 : (r21 == r17 ? 0 : -1))
            if (r0 > 0) goto L_0x03a3
            goto L_0x03a1
        L_0x03b8:
            if (r0 == 0) goto L_0x03cc
            r32 = r0
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r0 = r33.getStepStart()
            org.apache.commons.math3.RealFieldElement r0 = r0.getTime()
            java.lang.Object r0 = r8.subtract(r0)
            org.apache.commons.math3.RealFieldElement r0 = (org.apache.commons.math3.RealFieldElement) r0
            r1 = r0
            goto L_0x03d3
        L_0x03cc:
            r1 = r31
            goto L_0x03d3
        L_0x03cf:
            r17 = 0
            r1 = r26
        L_0x03d3:
            boolean r0 = r33.isLastStep()
            if (r0 == 0) goto L_0x03e1
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r0 = r33.getStepStart()
            r33.resetInternalState()
            return r0
        L_0x03e1:
            r4 = r7
            r5 = r9
            r3 = r11
            r2 = r12
            r0 = r13
            r12 = r17
            r9 = r28
            r7 = r35
            r14 = 0
            r15 = 1
            r11 = r34
            goto L_0x0065
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaFieldIntegrator.integrate(org.apache.commons.math3.ode.FieldExpandableODE, org.apache.commons.math3.ode.FieldODEState, org.apache.commons.math3.RealFieldElement):org.apache.commons.math3.ode.FieldODEStateAndDerivative");
    }

    public T getMinReduction() {
        return this.minReduction;
    }

    public void setMinReduction(T minReduction2) {
        this.minReduction = minReduction2;
    }

    public T getMaxGrowth() {
        return this.maxGrowth;
    }

    public void setMaxGrowth(T maxGrowth2) {
        this.maxGrowth = maxGrowth2;
    }
}
