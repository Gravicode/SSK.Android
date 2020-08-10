package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;
import org.apache.poi.p009ss.usermodel.ShapeTypes;

public class DormandPrince853FieldIntegrator<T extends RealFieldElement<T>> extends EmbeddedRungeKuttaFieldIntegrator<T> {
    private static final String METHOD_NAME = "Dormand-Prince 8 (5, 3)";
    private final T e1_01 = fraction(1.16092271E8d, 8.84846592E9d);
    private final T e1_06 = fraction(-1871647.0d, 1527680.0d);
    private final T e1_07 = fraction(-6.9799717E7d, 1.4079366E8d);
    private final T e1_08 = fraction(1.230164450203E12d, 7.39113984E11d);
    private final T e1_09 = fraction(-1.980813971228885E15d, 5.654156025964544E15d);
    private final T e1_10 = fraction(4.64500805E8d, 1.389975552E9d);
    private final T e1_11 = fraction(1.606764981773E12d, 1.9613062656E13d);
    private final T e1_12 = fraction(-137909.0d, 6168960.0d);
    private final T e2_01 = fraction(-364463.0d, 1920240.0d);
    private final T e2_06 = fraction(3399327.0d, 763840.0d);
    private final T e2_07 = fraction(6.6578432E7d, 3.5198415E7d);
    private final T e2_08 = fraction(-1.674902723E9d, 2.887164E8d);
    private final T e2_09 = fraction(-7.4684743568175E13d, 1.76692375811392E14d);
    private final T e2_10 = fraction(-734375.0d, 4826304.0d);
    private final T e2_11 = fraction(1.71414593E8d, 8.512614E8d);
    private final T e2_12 = fraction(69869.0d, 3084480.0d);

    public DormandPrince853FieldIntegrator(Field<T> field, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(field, METHOD_NAME, 12, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    public DormandPrince853FieldIntegrator(Field<T> field, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(field, METHOD_NAME, 12, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    }

    public T[] getC() {
        T sqrt6 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) getField().getOne()).multiply(6)).sqrt();
        T[] c = (RealFieldElement[]) MathArrays.buildArray(getField(), 15);
        c[0] = (RealFieldElement) ((RealFieldElement) sqrt6.add(-6.0d)).divide(-67.5d);
        c[1] = (RealFieldElement) ((RealFieldElement) sqrt6.add(-6.0d)).divide(-45.0d);
        c[2] = (RealFieldElement) ((RealFieldElement) sqrt6.add(-6.0d)).divide(-30.0d);
        c[3] = (RealFieldElement) ((RealFieldElement) sqrt6.add(6.0d)).divide(30.0d);
        c[4] = fraction(1, 3);
        c[5] = fraction(1, 4);
        c[6] = fraction(4, 13);
        c[7] = fraction((int) ShapeTypes.VERTICAL_SCROLL, 195);
        c[8] = fraction(3, 5);
        c[9] = fraction(6, 7);
        c[10] = (RealFieldElement) getField().getOne();
        c[11] = (RealFieldElement) getField().getOne();
        c[12] = fraction(1.0d, 10.0d);
        c[13] = fraction(1.0d, 5.0d);
        c[14] = fraction(7.0d, 9.0d);
        return c;
    }

    public T[][] getA() {
        T sqrt6 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) getField().getOne()).multiply(6)).sqrt();
        RealFieldElement[][] realFieldElementArr = (RealFieldElement[][]) MathArrays.buildArray(getField(), 15, -1);
        for (int i = 0; i < realFieldElementArr.length; i++) {
            realFieldElementArr[i] = (RealFieldElement[]) MathArrays.buildArray(getField(), i + 1);
        }
        realFieldElementArr[0][0] = (RealFieldElement) ((RealFieldElement) sqrt6.add(-6.0d)).divide(-67.5d);
        realFieldElementArr[1][0] = (RealFieldElement) ((RealFieldElement) sqrt6.add(-6.0d)).divide(-180.0d);
        realFieldElementArr[1][1] = (RealFieldElement) ((RealFieldElement) sqrt6.add(-6.0d)).divide(-60.0d);
        realFieldElementArr[2][0] = (RealFieldElement) ((RealFieldElement) sqrt6.add(-6.0d)).divide(-120.0d);
        realFieldElementArr[2][1] = (RealFieldElement) getField().getZero();
        realFieldElementArr[2][2] = (RealFieldElement) ((RealFieldElement) sqrt6.add(-6.0d)).divide(-40.0d);
        realFieldElementArr[3][0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) sqrt6.multiply(107)).add(462.0d)).divide(3000.0d);
        realFieldElementArr[3][1] = (RealFieldElement) getField().getZero();
        realFieldElementArr[3][2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) sqrt6.multiply(197)).add(402.0d)).divide(-1000.0d);
        realFieldElementArr[3][3] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) sqrt6.multiply(73)).add(168.0d)).divide(375.0d);
        realFieldElementArr[4][0] = fraction(1, 27);
        realFieldElementArr[4][1] = (RealFieldElement) getField().getZero();
        realFieldElementArr[4][2] = (RealFieldElement) getField().getZero();
        realFieldElementArr[4][3] = (RealFieldElement) ((RealFieldElement) sqrt6.add(16.0d)).divide(108.0d);
        realFieldElementArr[4][4] = (RealFieldElement) ((RealFieldElement) sqrt6.add(-16.0d)).divide(-108.0d);
        realFieldElementArr[5][0] = fraction(19, 512);
        realFieldElementArr[5][1] = (RealFieldElement) getField().getZero();
        realFieldElementArr[5][2] = (RealFieldElement) getField().getZero();
        realFieldElementArr[5][3] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) sqrt6.multiply(23)).add(118.0d)).divide(1024.0d);
        realFieldElementArr[5][4] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) sqrt6.multiply(-23)).add(118.0d)).divide(1024.0d);
        realFieldElementArr[5][5] = fraction(-9, 512);
        realFieldElementArr[6][0] = fraction(13772, 371293);
        realFieldElementArr[6][1] = (RealFieldElement) getField().getZero();
        realFieldElementArr[6][2] = (RealFieldElement) getField().getZero();
        realFieldElementArr[6][3] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) sqrt6.multiply(4784)).add(51544.0d)).divide(371293.0d);
        realFieldElementArr[6][4] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) sqrt6.multiply(-4784)).add(51544.0d)).divide(371293.0d);
        realFieldElementArr[6][5] = fraction(-5688, 371293);
        realFieldElementArr[6][6] = fraction(3072, 371293);
        realFieldElementArr[7][0] = fraction(5.8656157643E10d, 9.3983540625E10d);
        realFieldElementArr[7][1] = (RealFieldElement) getField().getZero();
        realFieldElementArr[7][2] = (RealFieldElement) getField().getZero();
        realFieldElementArr[7][3] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) sqrt6.multiply(-3.18801444819E11d)).add(-1.324889724104E12d)).divide(6.265569375E11d);
        realFieldElementArr[7][4] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) sqrt6.multiply(3.18801444819E11d)).add(-1.324889724104E12d)).divide(6.265569375E11d);
        realFieldElementArr[7][5] = fraction(9.6044563816E10d, 3.480871875E9d);
        realFieldElementArr[7][6] = fraction(5.682451879168E12d, 2.81950621875E11d);
        realFieldElementArr[7][7] = fraction(-1.65125654E8d, 3796875.0d);
        realFieldElementArr[8][0] = fraction(8909899.0d, 1.8653125E7d);
        realFieldElementArr[8][1] = (RealFieldElement) getField().getZero();
        realFieldElementArr[8][2] = (RealFieldElement) getField().getZero();
        realFieldElementArr[8][3] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) sqrt6.multiply(-1137963.0d)).add(-4521408.0d)).divide(2937500.0d);
        realFieldElementArr[8][4] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) sqrt6.multiply(1137963.0d)).add(-4521408.0d)).divide(2937500.0d);
        realFieldElementArr[8][5] = fraction(9.6663078E7d, 4553125.0d);
        realFieldElementArr[8][6] = fraction(2.107245056E9d, 1.37915625E8d);
        realFieldElementArr[8][7] = fraction(-4.913652016E9d, 1.47609375E8d);
        realFieldElementArr[8][8] = fraction(-7.889427E7d, 3.880452869E9d);
        realFieldElementArr[9][0] = fraction(-2.0401265806E10d, 2.1769653311E10d);
        realFieldElementArr[9][1] = (RealFieldElement) getField().getZero();
        realFieldElementArr[9][2] = (RealFieldElement) getField().getZero();
        realFieldElementArr[9][3] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) sqrt6.multiply(94326.0d)).add(354216.0d)).divide(112847.0d);
        realFieldElementArr[9][4] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) sqrt6.multiply(-94326.0d)).add(354216.0d)).divide(112847.0d);
        realFieldElementArr[9][5] = fraction(-4.3306765128E10d, 5.313852383E9d);
        realFieldElementArr[9][6] = fraction(-2.0866708358144E13d, 1.126708119789E12d);
        realFieldElementArr[9][7] = fraction(1.488600343802E13d, 6.54632330667E11d);
        realFieldElementArr[9][8] = fraction(3.5290686222309376E16d, 1.4152473387134412E16d);
        realFieldElementArr[9][9] = fraction(-1.477884375E9d, 4.85066827E8d);
        realFieldElementArr[10][0] = fraction(3.9815761E7d, 1.7514443E7d);
        realFieldElementArr[10][1] = (RealFieldElement) getField().getZero();
        realFieldElementArr[10][2] = (RealFieldElement) getField().getZero();
        realFieldElementArr[10][3] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) sqrt6.multiply(-960905.0d)).add(-3457480.0d)).divide(551636.0d);
        realFieldElementArr[10][4] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) sqrt6.multiply(960905.0d)).add(-3457480.0d)).divide(551636.0d);
        realFieldElementArr[10][5] = fraction(-8.44554132E8d, 4.7026969E7d);
        realFieldElementArr[10][6] = fraction(8.444996352E9d, 3.02158619E8d);
        realFieldElementArr[10][7] = fraction(-2.509602342E9d, 8.77790785E8d);
        realFieldElementArr[10][8] = fraction(-2.8388795297996248E16d, 3.199510091356783E15d);
        realFieldElementArr[10][9] = fraction(2.2671625E8d, 1.8341897E7d);
        realFieldElementArr[10][10] = fraction(1.371316744E9d, 2.131383595E9d);
        realFieldElementArr[11][0] = fraction(104257.0d, 1920240.0d);
        realFieldElementArr[11][1] = (RealFieldElement) getField().getZero();
        realFieldElementArr[11][2] = (RealFieldElement) getField().getZero();
        realFieldElementArr[11][3] = (RealFieldElement) getField().getZero();
        realFieldElementArr[11][4] = (RealFieldElement) getField().getZero();
        realFieldElementArr[11][5] = fraction(3399327.0d, 763840.0d);
        realFieldElementArr[11][6] = fraction(6.6578432E7d, 3.5198415E7d);
        realFieldElementArr[11][7] = fraction(-1.674902723E9d, 2.887164E8d);
        realFieldElementArr[11][8] = fraction(5.4980371265625E13d, 1.76692375811392E14d);
        realFieldElementArr[11][9] = fraction(-734375.0d, 4826304.0d);
        realFieldElementArr[11][10] = fraction(1.71414593E8d, 8.512614E8d);
        realFieldElementArr[11][11] = fraction(137909.0d, 3084480.0d);
        realFieldElementArr[12][0] = fraction(1.3481885573E10d, 2.4003E11d);
        realFieldElementArr[12][1] = (RealFieldElement) getField().getZero();
        realFieldElementArr[12][2] = (RealFieldElement) getField().getZero();
        realFieldElementArr[12][3] = (RealFieldElement) getField().getZero();
        realFieldElementArr[12][4] = (RealFieldElement) getField().getZero();
        realFieldElementArr[12][5] = (RealFieldElement) getField().getZero();
        realFieldElementArr[12][6] = fraction(1.39418837528E11d, 5.49975234375E11d);
        realFieldElementArr[12][7] = fraction(-1.1108320068443E13d, 4.51119375E13d);
        realFieldElementArr[12][8] = fraction(-1.769651421925959E15d, 1.424938514608E16d);
        realFieldElementArr[12][9] = fraction(5.7799439E7d, 3.77055E8d);
        realFieldElementArr[12][10] = fraction(7.93322643029E11d, 9.673425E13d);
        realFieldElementArr[12][11] = fraction(1.458939311E9d, 1.9278E11d);
        realFieldElementArr[12][12] = fraction(-4149.0d, 500000.0d);
        realFieldElementArr[13][0] = fraction(1.595561272731E12d, 5.01202735E13d);
        realFieldElementArr[13][1] = (RealFieldElement) getField().getZero();
        realFieldElementArr[13][2] = (RealFieldElement) getField().getZero();
        realFieldElementArr[13][3] = (RealFieldElement) getField().getZero();
        realFieldElementArr[13][4] = (RealFieldElement) getField().getZero();
        realFieldElementArr[13][5] = fraction(9.75183916491E11d, 3.445768803125E13d);
        realFieldElementArr[13][6] = fraction(3.8492013932672E13d, 7.18912673015625E14d);
        realFieldElementArr[13][7] = fraction(-1.114881286517557E15d, 2.02987107675E16d);
        realFieldElementArr[13][8] = (RealFieldElement) getField().getZero();
        realFieldElementArr[13][9] = (RealFieldElement) getField().getZero();
        realFieldElementArr[13][10] = fraction(-2.538710946863E12d, 2.343122786125E16d);
        realFieldElementArr[13][11] = fraction(8.824659001E9d, 2.306671678125E13d);
        realFieldElementArr[13][12] = fraction(-1.1518334563E10d, 3.38311846125E13d);
        realFieldElementArr[13][13] = fraction(1.912306948E9d, 1.3532473845E10d);
        realFieldElementArr[14][0] = fraction(-1.3613986967E10d, 3.1741908048E10d);
        realFieldElementArr[14][1] = (RealFieldElement) getField().getZero();
        realFieldElementArr[14][2] = (RealFieldElement) getField().getZero();
        realFieldElementArr[14][3] = (RealFieldElement) getField().getZero();
        realFieldElementArr[14][4] = (RealFieldElement) getField().getZero();
        realFieldElementArr[14][5] = fraction(-4.755612631E9d, 1.012344804E9d);
        realFieldElementArr[14][6] = fraction(4.2939257944576E13d, 5.588559685701E12d);
        realFieldElementArr[14][7] = fraction(7.7881972900277E13d, 1.9140370552944E13d);
        realFieldElementArr[14][8] = fraction(2.2719829234375E13d, 6.3689648654052E13d);
        realFieldElementArr[14][9] = (RealFieldElement) getField().getZero();
        realFieldElementArr[14][10] = (RealFieldElement) getField().getZero();
        realFieldElementArr[14][11] = (RealFieldElement) getField().getZero();
        realFieldElementArr[14][12] = fraction(-1.199007803E9d, 8.57031517296E11d);
        realFieldElementArr[14][13] = fraction(1.57882067E11d, 5.3564469831E10d);
        realFieldElementArr[14][14] = fraction(-2.90468882375E11d, 3.1741908048E10d);
        return realFieldElementArr;
    }

    public T[] getB() {
        T[] b = (RealFieldElement[]) MathArrays.buildArray(getField(), 16);
        b[0] = fraction(104257, 1920240);
        b[1] = (RealFieldElement) getField().getZero();
        b[2] = (RealFieldElement) getField().getZero();
        b[3] = (RealFieldElement) getField().getZero();
        b[4] = (RealFieldElement) getField().getZero();
        b[5] = fraction(3399327.0d, 763840.0d);
        b[6] = fraction(6.6578432E7d, 3.5198415E7d);
        b[7] = fraction(-1.674902723E9d, 2.887164E8d);
        b[8] = fraction(5.4980371265625E13d, 1.76692375811392E14d);
        b[9] = fraction(-734375.0d, 4826304.0d);
        b[10] = fraction(1.71414593E8d, 8.512614E8d);
        b[11] = fraction(137909.0d, 3084480.0d);
        b[12] = (RealFieldElement) getField().getZero();
        b[13] = (RealFieldElement) getField().getZero();
        b[14] = (RealFieldElement) getField().getZero();
        b[15] = (RealFieldElement) getField().getZero();
        return b;
    }

    /* access modifiers changed from: protected */
    public DormandPrince853FieldStepInterpolator<T> createInterpolator(boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldEquationsMapper<T> mapper) {
        DormandPrince853FieldStepInterpolator dormandPrince853FieldStepInterpolator = new DormandPrince853FieldStepInterpolator(getField(), forward, yDotK, globalPreviousState, globalCurrentState, globalPreviousState, globalCurrentState, mapper);
        return dormandPrince853FieldStepInterpolator;
    }

    public int getOrder() {
        return 8;
    }

    /* access modifiers changed from: protected */
    public T estimateError(T[][] yDotK, T[] y0, T[] y1, T h) {
        T error2 = (RealFieldElement) h.getField().getZero();
        T error1 = (RealFieldElement) h.getField().getZero();
        for (int j = 0; j < this.mainSetDimension; j++) {
            T errSum1 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) yDotK[0][j].multiply(this.e1_01)).add(yDotK[5][j].multiply(this.e1_06))).add(yDotK[6][j].multiply(this.e1_07))).add(yDotK[7][j].multiply(this.e1_08))).add(yDotK[8][j].multiply(this.e1_09))).add(yDotK[9][j].multiply(this.e1_10))).add(yDotK[10][j].multiply(this.e1_11))).add(yDotK[11][j].multiply(this.e1_12));
            T errSum2 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) yDotK[0][j].multiply(this.e2_01)).add(yDotK[5][j].multiply(this.e2_06))).add(yDotK[6][j].multiply(this.e2_07))).add(yDotK[7][j].multiply(this.e2_08))).add(yDotK[8][j].multiply(this.e2_09))).add(yDotK[9][j].multiply(this.e2_10))).add(yDotK[10][j].multiply(this.e2_11))).add(yDotK[11][j].multiply(this.e2_12));
            T yScale = MathUtils.max((RealFieldElement) y0[j].abs(), (RealFieldElement) y1[j].abs());
            T tol = (RealFieldElement) (this.vecAbsoluteTolerance == null ? ((RealFieldElement) yScale.multiply(this.scalRelativeTolerance)).add(this.scalAbsoluteTolerance) : ((RealFieldElement) yScale.multiply(this.vecRelativeTolerance[j])).add(this.vecAbsoluteTolerance[j]));
            T ratio1 = (RealFieldElement) errSum1.divide(tol);
            error1 = (RealFieldElement) error1.add(ratio1.multiply(ratio1));
            T ratio2 = (RealFieldElement) errSum2.divide(tol);
            error2 = (RealFieldElement) error2.add(ratio2.multiply(ratio2));
        }
        T den = (RealFieldElement) error1.add(error2.multiply(0.01d));
        if (den.getReal() <= 0.0d) {
            den = (RealFieldElement) h.getField().getOne();
        }
        return (RealFieldElement) ((RealFieldElement) ((RealFieldElement) h.abs()).multiply(error1)).divide(((RealFieldElement) den.multiply(this.mainSetDimension)).sqrt());
    }
}
