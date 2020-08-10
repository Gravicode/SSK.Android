package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;
import org.apache.poi.p009ss.usermodel.ShapeTypes;

public class HighamHall54FieldIntegrator<T extends RealFieldElement<T>> extends EmbeddedRungeKuttaFieldIntegrator<T> {
    private static final String METHOD_NAME = "Higham-Hall 5(4)";

    /* renamed from: e */
    private final T[] f692e;

    public HighamHall54FieldIntegrator(Field<T> field, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        Field<T> field2 = field;
        super(field2, METHOD_NAME, -1, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        this.f692e = (RealFieldElement[]) MathArrays.buildArray(field2, 7);
        this.f692e[0] = fraction(-1, 20);
        this.f692e[1] = (RealFieldElement) field2.getZero();
        this.f692e[2] = fraction(81, 160);
        this.f692e[3] = fraction(-6, 5);
        this.f692e[4] = fraction(25, 32);
        this.f692e[5] = fraction(1, 16);
        this.f692e[6] = fraction(-1, 10);
    }

    public HighamHall54FieldIntegrator(Field<T> field, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        Field<T> field2 = field;
        super(field2, METHOD_NAME, -1, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        this.f692e = (RealFieldElement[]) MathArrays.buildArray(field2, 7);
        this.f692e[0] = fraction(-1, 20);
        this.f692e[1] = (RealFieldElement) field2.getZero();
        this.f692e[2] = fraction(81, 160);
        this.f692e[3] = fraction(-6, 5);
        this.f692e[4] = fraction(25, 32);
        this.f692e[5] = fraction(1, 16);
        this.f692e[6] = fraction(-1, 10);
    }

    public T[] getC() {
        T[] c = (RealFieldElement[]) MathArrays.buildArray(getField(), 6);
        c[0] = fraction(2, 9);
        c[1] = fraction(1, 3);
        c[2] = fraction(1, 2);
        c[3] = fraction(3, 5);
        c[4] = (RealFieldElement) getField().getOne();
        c[5] = (RealFieldElement) getField().getOne();
        return c;
    }

    public T[][] getA() {
        T[][] a = (RealFieldElement[][]) MathArrays.buildArray(getField(), 6, -1);
        for (int i = 0; i < a.length; i++) {
            a[i] = (RealFieldElement[]) MathArrays.buildArray(getField(), i + 1);
        }
        a[0][0] = fraction(2, 9);
        a[1][0] = fraction(1, 12);
        a[1][1] = fraction(1, 4);
        a[2][0] = fraction(1, 8);
        a[2][1] = (RealFieldElement) getField().getZero();
        a[2][2] = fraction(3, 8);
        a[3][0] = fraction(91, 500);
        a[3][1] = fraction(-27, 100);
        a[3][2] = fraction(78, (int) ShapeTypes.ELLIPSE_RIBBON_2);
        a[3][3] = fraction(8, (int) ShapeTypes.ELLIPSE_RIBBON_2);
        a[4][0] = fraction(-11, 20);
        a[4][1] = fraction(27, 20);
        a[4][2] = fraction(12, 5);
        a[4][3] = fraction(-36, 5);
        a[4][4] = fraction(5, 1);
        a[5][0] = fraction(1, 12);
        a[5][1] = (RealFieldElement) getField().getZero();
        a[5][2] = fraction(27, 32);
        a[5][3] = fraction(-4, 3);
        a[5][4] = fraction((int) ShapeTypes.ELLIPSE_RIBBON_2, 96);
        a[5][5] = fraction(5, 48);
        return a;
    }

    public T[] getB() {
        T[] b = (RealFieldElement[]) MathArrays.buildArray(getField(), 7);
        b[0] = fraction(1, 12);
        b[1] = (RealFieldElement) getField().getZero();
        b[2] = fraction(27, 32);
        b[3] = fraction(-4, 3);
        b[4] = fraction((int) ShapeTypes.ELLIPSE_RIBBON_2, 96);
        b[5] = fraction(5, 48);
        b[6] = (RealFieldElement) getField().getZero();
        return b;
    }

    /* access modifiers changed from: protected */
    public HighamHall54FieldStepInterpolator<T> createInterpolator(boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldEquationsMapper<T> mapper) {
        HighamHall54FieldStepInterpolator highamHall54FieldStepInterpolator = new HighamHall54FieldStepInterpolator(getField(), forward, yDotK, globalPreviousState, globalCurrentState, globalPreviousState, globalCurrentState, mapper);
        return highamHall54FieldStepInterpolator;
    }

    public int getOrder() {
        return 5;
    }

    /* access modifiers changed from: protected */
    public T estimateError(T[][] yDotK, T[] y0, T[] y1, T h) {
        T error = (RealFieldElement) getField().getZero();
        for (int j = 0; j < this.mainSetDimension; j++) {
            T errSum = (RealFieldElement) yDotK[0][j].multiply(this.f692e[0]);
            for (int l = 1; l < this.f692e.length; l++) {
                errSum = (RealFieldElement) errSum.add(yDotK[l][j].multiply(this.f692e[l]));
            }
            T yScale = MathUtils.max((RealFieldElement) y0[j].abs(), (RealFieldElement) y1[j].abs());
            T ratio = (RealFieldElement) ((RealFieldElement) h.multiply(errSum)).divide((RealFieldElement) (this.vecAbsoluteTolerance == null ? ((RealFieldElement) yScale.multiply(this.scalRelativeTolerance)).add(this.scalAbsoluteTolerance) : ((RealFieldElement) yScale.multiply(this.vecRelativeTolerance[j])).add(this.vecAbsoluteTolerance[j])));
            error = (RealFieldElement) error.add(ratio.multiply(ratio));
        }
        return (RealFieldElement) ((RealFieldElement) error.divide((double) this.mainSetDimension)).sqrt();
    }
}
