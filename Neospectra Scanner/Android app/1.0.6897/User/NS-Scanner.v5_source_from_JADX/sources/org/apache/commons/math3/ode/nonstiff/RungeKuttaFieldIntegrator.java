package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.AbstractFieldIntegrator;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.ode.FirstOrderFieldDifferentialEquations;
import org.apache.commons.math3.util.MathArrays;

public abstract class RungeKuttaFieldIntegrator<T extends RealFieldElement<T>> extends AbstractFieldIntegrator<T> implements FieldButcherArrayProvider<T> {

    /* renamed from: a */
    private final T[][] f695a = getA();

    /* renamed from: b */
    private final T[] f696b = getB();

    /* renamed from: c */
    private final T[] f697c = getC();
    private final T step;

    /* access modifiers changed from: protected */
    public abstract RungeKuttaFieldStepInterpolator<T> createInterpolator(boolean z, T[][] tArr, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative2, FieldEquationsMapper<T> fieldEquationsMapper);

    protected RungeKuttaFieldIntegrator(Field<T> field, String name, T step2) {
        super(field, name);
        this.step = (RealFieldElement) step2.abs();
    }

    /* access modifiers changed from: protected */
    public T fraction(int p, int q) {
        return (RealFieldElement) ((RealFieldElement) ((RealFieldElement) getField().getZero()).add((double) p)).divide((double) q);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:37:0x020a, code lost:
        if (((org.apache.commons.math3.RealFieldElement) r0.subtract(r8)).getReal() >= 0.0d) goto L_0x020c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x020e, code lost:
        r1 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x021e, code lost:
        if (((org.apache.commons.math3.RealFieldElement) r0.subtract(r8)).getReal() <= 0.0d) goto L_0x020c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.commons.math3.ode.FieldODEStateAndDerivative<T> integrate(org.apache.commons.math3.ode.FieldExpandableODE<T> r23, org.apache.commons.math3.ode.FieldODEState<T> r24, T r25) throws org.apache.commons.math3.exception.NumberIsTooSmallException, org.apache.commons.math3.exception.DimensionMismatchException, org.apache.commons.math3.exception.MaxCountExceededException, org.apache.commons.math3.exception.NoBracketingException {
        /*
            r22 = this;
            r6 = r22
            r7 = r24
            r8 = r25
            r6.sanityChecks(r7, r8)
            org.apache.commons.math3.RealFieldElement r9 = r24.getTime()
            org.apache.commons.math3.ode.FieldEquationsMapper r0 = r23.getMapper()
            org.apache.commons.math3.RealFieldElement[] r10 = r0.mapState(r7)
            r11 = r23
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r0 = r6.initIntegration(r11, r9, r10, r8)
            r6.setStepStart(r0)
            org.apache.commons.math3.RealFieldElement r0 = r24.getTime()
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
            r16 = r0
            T[] r0 = r6.f697c
            int r0 = r0.length
            int r5 = r0 + 1
            r0 = r10
            org.apache.commons.math3.Field r1 = r22.getField()
            r2 = -1
            java.lang.Object[][] r1 = org.apache.commons.math3.util.MathArrays.buildArray(r1, r5, r2)
            r17 = r1
            org.apache.commons.math3.RealFieldElement[][] r17 = (org.apache.commons.math3.RealFieldElement[][]) r17
            org.apache.commons.math3.Field r1 = r22.getField()
            int r2 = r10.length
            java.lang.Object[] r1 = org.apache.commons.math3.util.MathArrays.buildArray(r1, r2)
            r3 = r1
            org.apache.commons.math3.RealFieldElement[] r3 = (org.apache.commons.math3.RealFieldElement[]) r3
            if (r16 == 0) goto L_0x0090
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r1 = r22.getStepStart()
            org.apache.commons.math3.RealFieldElement r1 = r1.getTime()
            T r2 = r6.step
            java.lang.Object r1 = r1.add(r2)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            java.lang.Object r1 = r1.subtract(r8)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            double r1 = r1.getReal()
            int r1 = (r1 > r12 ? 1 : (r1 == r12 ? 0 : -1))
            if (r1 < 0) goto L_0x008a
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r1 = r22.getStepStart()
            org.apache.commons.math3.RealFieldElement r1 = r1.getTime()
            java.lang.Object r1 = r8.subtract(r1)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            r6.setStepSize(r1)
            goto L_0x00cb
        L_0x008a:
            T r1 = r6.step
            r6.setStepSize(r1)
            goto L_0x00cb
        L_0x0090:
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r1 = r22.getStepStart()
            org.apache.commons.math3.RealFieldElement r1 = r1.getTime()
            T r2 = r6.step
            java.lang.Object r1 = r1.subtract(r2)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            java.lang.Object r1 = r1.subtract(r8)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            double r1 = r1.getReal()
            int r1 = (r1 > r12 ? 1 : (r1 == r12 ? 0 : -1))
            if (r1 > 0) goto L_0x00c0
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r1 = r22.getStepStart()
            org.apache.commons.math3.RealFieldElement r1 = r1.getTime()
            java.lang.Object r1 = r8.subtract(r1)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            r6.setStepSize(r1)
            goto L_0x00cb
        L_0x00c0:
            T r1 = r6.step
            java.lang.Object r1 = r1.negate()
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            r6.setStepSize(r1)
        L_0x00cb:
            r6.setIsLastStep(r14)
        L_0x00ce:
            org.apache.commons.math3.ode.FieldEquationsMapper r1 = r23.getMapper()
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r2 = r22.getStepStart()
            org.apache.commons.math3.RealFieldElement[] r2 = r1.mapState(r2)
            org.apache.commons.math3.ode.FieldEquationsMapper r0 = r23.getMapper()
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r1 = r22.getStepStart()
            org.apache.commons.math3.RealFieldElement[] r0 = r0.mapDerivative(r1)
            r17[r14] = r0
            r0 = 1
        L_0x00e9:
            if (r0 >= r5) goto L_0x0161
            r1 = 0
        L_0x00ec:
            int r4 = r10.length
            if (r1 >= r4) goto L_0x0139
            r4 = r17[r14]
            r4 = r4[r1]
            T[][] r15 = r6.f695a
            int r18 = r0 + -1
            r15 = r15[r18]
            r15 = r15[r14]
            java.lang.Object r4 = r4.multiply(r15)
            org.apache.commons.math3.RealFieldElement r4 = (org.apache.commons.math3.RealFieldElement) r4
            r15 = r4
            r4 = 1
        L_0x0103:
            if (r4 >= r0) goto L_0x0121
            r18 = r17[r4]
            r12 = r18[r1]
            T[][] r13 = r6.f695a
            int r18 = r0 + -1
            r13 = r13[r18]
            r13 = r13[r4]
            java.lang.Object r12 = r12.multiply(r13)
            java.lang.Object r12 = r15.add(r12)
            r15 = r12
            org.apache.commons.math3.RealFieldElement r15 = (org.apache.commons.math3.RealFieldElement) r15
            int r4 = r4 + 1
            r12 = 0
            goto L_0x0103
        L_0x0121:
            r4 = r2[r1]
            org.apache.commons.math3.RealFieldElement r12 = r22.getStepSize()
            java.lang.Object r12 = r12.multiply(r15)
            java.lang.Object r4 = r4.add(r12)
            org.apache.commons.math3.RealFieldElement r4 = (org.apache.commons.math3.RealFieldElement) r4
            r3[r1] = r4
            int r1 = r1 + 1
            r12 = 0
            r15 = 1
            goto L_0x00ec
        L_0x0139:
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r1 = r22.getStepStart()
            org.apache.commons.math3.RealFieldElement r1 = r1.getTime()
            org.apache.commons.math3.RealFieldElement r4 = r22.getStepSize()
            T[] r12 = r6.f697c
            int r13 = r0 + -1
            r12 = r12[r13]
            java.lang.Object r4 = r4.multiply(r12)
            java.lang.Object r1 = r1.add(r4)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            org.apache.commons.math3.RealFieldElement[] r1 = r6.computeDerivatives(r1, r3)
            r17[r0] = r1
            int r0 = r0 + 1
            r12 = 0
            r15 = 1
            goto L_0x00e9
        L_0x0161:
            r0 = 0
        L_0x0162:
            int r1 = r10.length
            if (r0 >= r1) goto L_0x01a2
            r1 = r17[r14]
            r1 = r1[r0]
            T[] r4 = r6.f696b
            r4 = r4[r14]
            java.lang.Object r1 = r1.multiply(r4)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            r4 = r1
            r1 = 1
        L_0x0175:
            if (r1 >= r5) goto L_0x018d
            r12 = r17[r1]
            r12 = r12[r0]
            T[] r13 = r6.f696b
            r13 = r13[r1]
            java.lang.Object r12 = r12.multiply(r13)
            java.lang.Object r12 = r4.add(r12)
            r4 = r12
            org.apache.commons.math3.RealFieldElement r4 = (org.apache.commons.math3.RealFieldElement) r4
            int r1 = r1 + 1
            goto L_0x0175
        L_0x018d:
            r1 = r2[r0]
            org.apache.commons.math3.RealFieldElement r12 = r22.getStepSize()
            java.lang.Object r12 = r12.multiply(r4)
            java.lang.Object r1 = r1.add(r12)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            r3[r0] = r1
            int r0 = r0 + 1
            goto L_0x0162
        L_0x01a2:
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r0 = r22.getStepStart()
            org.apache.commons.math3.RealFieldElement r0 = r0.getTime()
            org.apache.commons.math3.RealFieldElement r1 = r22.getStepSize()
            java.lang.Object r0 = r0.add(r1)
            r12 = r0
            org.apache.commons.math3.RealFieldElement r12 = (org.apache.commons.math3.RealFieldElement) r12
            org.apache.commons.math3.RealFieldElement[] r13 = r6.computeDerivatives(r12, r3)
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r4 = new org.apache.commons.math3.ode.FieldODEStateAndDerivative
            r4.<init>(r12, r3, r13)
            int r0 = r10.length
            java.lang.System.arraycopy(r3, r14, r2, r14, r0)
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r15 = r22.getStepStart()
            org.apache.commons.math3.ode.FieldEquationsMapper r18 = r23.getMapper()
            r0 = r6
            r1 = r16
            r20 = r2
            r2 = r17
            r21 = r3
            r3 = r15
            r15 = r5
            r5 = r18
            org.apache.commons.math3.ode.nonstiff.RungeKuttaFieldStepInterpolator r0 = r0.createInterpolator(r1, r2, r3, r4, r5)
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r0 = r6.acceptStep(r0, r8)
            r6.setStepStart(r0)
            boolean r0 = r22.isLastStep()
            if (r0 != 0) goto L_0x0235
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r0 = r22.getStepStart()
            org.apache.commons.math3.RealFieldElement r0 = r0.getTime()
            org.apache.commons.math3.RealFieldElement r1 = r22.getStepSize()
            java.lang.Object r0 = r0.add(r1)
            org.apache.commons.math3.RealFieldElement r0 = (org.apache.commons.math3.RealFieldElement) r0
            if (r16 == 0) goto L_0x0210
            java.lang.Object r1 = r0.subtract(r8)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            double r1 = r1.getReal()
            r18 = 0
            int r1 = (r1 > r18 ? 1 : (r1 == r18 ? 0 : -1))
            if (r1 < 0) goto L_0x020e
        L_0x020c:
            r1 = 1
            goto L_0x0221
        L_0x020e:
            r1 = 0
            goto L_0x0221
        L_0x0210:
            r18 = 0
            java.lang.Object r1 = r0.subtract(r8)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            double r1 = r1.getReal()
            int r1 = (r1 > r18 ? 1 : (r1 == r18 ? 0 : -1))
            if (r1 > 0) goto L_0x020e
            goto L_0x020c
        L_0x0221:
            if (r1 == 0) goto L_0x0237
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r2 = r22.getStepStart()
            org.apache.commons.math3.RealFieldElement r2 = r2.getTime()
            java.lang.Object r2 = r8.subtract(r2)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            r6.setStepSize(r2)
            goto L_0x0237
        L_0x0235:
            r18 = 0
        L_0x0237:
            boolean r0 = r22.isLastStep()
            if (r0 == 0) goto L_0x0249
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r0 = r22.getStepStart()
            r1 = 0
            r6.setStepStart(r1)
            r6.setStepSize(r1)
            return r0
        L_0x0249:
            r5 = r15
            r12 = r18
            r0 = r20
            r3 = r21
            r15 = 1
            goto L_0x00ce
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.ode.nonstiff.RungeKuttaFieldIntegrator.integrate(org.apache.commons.math3.ode.FieldExpandableODE, org.apache.commons.math3.ode.FieldODEState, org.apache.commons.math3.RealFieldElement):org.apache.commons.math3.ode.FieldODEStateAndDerivative");
    }

    public T[] singleStep(FirstOrderFieldDifferentialEquations<T> equations, T t0, T[] y0, T t) {
        FirstOrderFieldDifferentialEquations<T> firstOrderFieldDifferentialEquations = equations;
        T t2 = t0;
        T[] tArr = y0;
        T[] y = (RealFieldElement[]) y0.clone();
        int stages = this.f697c.length + 1;
        T[][] yDotK = (RealFieldElement[][]) MathArrays.buildArray(getField(), stages, -1);
        T[] yTmp = (RealFieldElement[]) y0.clone();
        T h = (RealFieldElement) t.subtract(t2);
        char c = 0;
        yDotK[0] = firstOrderFieldDifferentialEquations.computeDerivatives(t2, y);
        int k = 1;
        while (k < stages) {
            int j = 0;
            while (j < tArr.length) {
                T sum = (RealFieldElement) yDotK[c][j].multiply(this.f695a[k - 1][c]);
                for (int l = 1; l < k; l++) {
                    sum = (RealFieldElement) sum.add(yDotK[l][j].multiply(this.f695a[k - 1][l]));
                }
                yTmp[j] = (RealFieldElement) y[j].add(h.multiply(sum));
                j++;
                c = 0;
            }
            yDotK[k] = firstOrderFieldDifferentialEquations.computeDerivatives((RealFieldElement) t2.add(h.multiply(this.f697c[k - 1])), yTmp);
            k++;
            c = 0;
        }
        for (int j2 = 0; j2 < tArr.length; j2++) {
            T sum2 = (RealFieldElement) yDotK[0][j2].multiply(this.f696b[0]);
            for (int l2 = 1; l2 < stages; l2++) {
                sum2 = (RealFieldElement) sum2.add(yDotK[l2][j2].multiply(this.f696b[l2]));
            }
            y[j2] = (RealFieldElement) y[j2].add(h.multiply(sum2));
        }
        return y;
    }
}
