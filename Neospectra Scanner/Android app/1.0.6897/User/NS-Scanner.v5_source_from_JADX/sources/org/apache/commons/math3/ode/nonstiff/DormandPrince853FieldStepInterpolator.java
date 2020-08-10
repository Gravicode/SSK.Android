package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.MathArrays;

class DormandPrince853FieldStepInterpolator<T extends RealFieldElement<T>> extends RungeKuttaFieldStepInterpolator<T> {

    /* renamed from: d */
    private final T[][] f682d;

    DormandPrince853FieldStepInterpolator(Field<T> field, boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldODEStateAndDerivative<T> softPreviousState, FieldODEStateAndDerivative<T> softCurrentState, FieldEquationsMapper<T> mapper) {
        super(field, forward, yDotK, globalPreviousState, globalCurrentState, softPreviousState, softCurrentState, mapper);
        Field<T> field2 = field;
        this.f682d = (RealFieldElement[][]) MathArrays.buildArray(field2, 7, 16);
        this.f682d[0][0] = fraction(field2, 104257.0d, 1920240.0d);
        this.f682d[0][1] = (RealFieldElement) field.getZero();
        this.f682d[0][2] = (RealFieldElement) field.getZero();
        this.f682d[0][3] = (RealFieldElement) field.getZero();
        this.f682d[0][4] = (RealFieldElement) field.getZero();
        Field<T> field3 = field2;
        this.f682d[0][5] = fraction(field3, 3399327.0d, 763840.0d);
        this.f682d[0][6] = fraction(field3, 6.6578432E7d, 3.5198415E7d);
        this.f682d[0][7] = fraction(field3, -1.674902723E9d, 2.887164E8d);
        this.f682d[0][8] = fraction(field3, 5.4980371265625E13d, 1.76692375811392E14d);
        this.f682d[0][9] = fraction(field3, -734375.0d, 4826304.0d);
        this.f682d[0][10] = fraction(field3, 1.71414593E8d, 8.512614E8d);
        this.f682d[0][11] = fraction(field3, 137909.0d, 3084480.0d);
        this.f682d[0][12] = (RealFieldElement) field.getZero();
        this.f682d[0][13] = (RealFieldElement) field.getZero();
        this.f682d[0][14] = (RealFieldElement) field.getZero();
        this.f682d[0][15] = (RealFieldElement) field.getZero();
        this.f682d[1][0] = (RealFieldElement) ((RealFieldElement) this.f682d[0][0].negate()).add(1.0d);
        this.f682d[1][1] = (RealFieldElement) this.f682d[0][1].negate();
        this.f682d[1][2] = (RealFieldElement) this.f682d[0][2].negate();
        this.f682d[1][3] = (RealFieldElement) this.f682d[0][3].negate();
        this.f682d[1][4] = (RealFieldElement) this.f682d[0][4].negate();
        this.f682d[1][5] = (RealFieldElement) this.f682d[0][5].negate();
        this.f682d[1][6] = (RealFieldElement) this.f682d[0][6].negate();
        this.f682d[1][7] = (RealFieldElement) this.f682d[0][7].negate();
        this.f682d[1][8] = (RealFieldElement) this.f682d[0][8].negate();
        this.f682d[1][9] = (RealFieldElement) this.f682d[0][9].negate();
        this.f682d[1][10] = (RealFieldElement) this.f682d[0][10].negate();
        this.f682d[1][11] = (RealFieldElement) this.f682d[0][11].negate();
        this.f682d[1][12] = (RealFieldElement) this.f682d[0][12].negate();
        this.f682d[1][13] = (RealFieldElement) this.f682d[0][13].negate();
        this.f682d[1][14] = (RealFieldElement) this.f682d[0][14].negate();
        this.f682d[1][15] = (RealFieldElement) this.f682d[0][15].negate();
        this.f682d[2][0] = (RealFieldElement) ((RealFieldElement) this.f682d[0][0].multiply(2)).subtract(1.0d);
        this.f682d[2][1] = (RealFieldElement) this.f682d[0][1].multiply(2);
        this.f682d[2][2] = (RealFieldElement) this.f682d[0][2].multiply(2);
        this.f682d[2][3] = (RealFieldElement) this.f682d[0][3].multiply(2);
        this.f682d[2][4] = (RealFieldElement) this.f682d[0][4].multiply(2);
        this.f682d[2][5] = (RealFieldElement) this.f682d[0][5].multiply(2);
        this.f682d[2][6] = (RealFieldElement) this.f682d[0][6].multiply(2);
        this.f682d[2][7] = (RealFieldElement) this.f682d[0][7].multiply(2);
        this.f682d[2][8] = (RealFieldElement) this.f682d[0][8].multiply(2);
        this.f682d[2][9] = (RealFieldElement) this.f682d[0][9].multiply(2);
        this.f682d[2][10] = (RealFieldElement) this.f682d[0][10].multiply(2);
        this.f682d[2][11] = (RealFieldElement) this.f682d[0][11].multiply(2);
        this.f682d[2][12] = (RealFieldElement) ((RealFieldElement) this.f682d[0][12].multiply(2)).subtract(1.0d);
        this.f682d[2][13] = (RealFieldElement) this.f682d[0][13].multiply(2);
        this.f682d[2][14] = (RealFieldElement) this.f682d[0][14].multiply(2);
        this.f682d[2][15] = (RealFieldElement) this.f682d[0][15].multiply(2);
        this.f682d[3][0] = fraction(field2, -1.7751989329E10d, 2.10607656E9d);
        this.f682d[3][1] = (RealFieldElement) field.getZero();
        this.f682d[3][2] = (RealFieldElement) field.getZero();
        this.f682d[3][3] = (RealFieldElement) field.getZero();
        this.f682d[3][4] = (RealFieldElement) field.getZero();
        Field<T> field4 = field2;
        this.f682d[3][5] = fraction(field4, 4.272954039E9d, 7.53986464E9d);
        this.f682d[3][6] = fraction(field4, -1.18476319744E11d, 3.8604839385E10d);
        this.f682d[3][7] = fraction(field4, 7.55123450731E11d, 3.166577316E11d);
        this.f682d[3][8] = fraction(field4, 3.6923844612348283E18d, 1.7441304416342505E18d);
        this.f682d[3][9] = fraction(field4, -4.612609375E9d, 5.293382976E9d);
        this.f682d[3][10] = fraction(field4, 2.091772278379E12d, 9.336445866E11d);
        this.f682d[3][11] = fraction(field4, 2.136624137E9d, 3.38298912E9d);
        this.f682d[3][12] = fraction(field4, -126493.0d, 1421424.0d);
        this.f682d[3][13] = fraction(field4, 9.835E7d, 5419179.0d);
        this.f682d[3][14] = fraction(field4, -1.8878125E7d, 2053168.0d);
        this.f682d[3][15] = fraction(field4, -1.944542619E9d, 4.38351368E8d);
        this.f682d[4][0] = fraction(field2, 3.2941697297E10d, 3.15911484E9d);
        this.f682d[4][1] = (RealFieldElement) field.getZero();
        this.f682d[4][2] = (RealFieldElement) field.getZero();
        this.f682d[4][3] = (RealFieldElement) field.getZero();
        this.f682d[4][4] = (RealFieldElement) field.getZero();
        Field<T> field5 = field2;
        this.f682d[4][5] = fraction(field5, 4.56696183123E11d, 1.88496616E9d);
        this.f682d[4][6] = fraction(field5, 1.9132610714624E13d, 1.15814518155E11d);
        this.f682d[4][7] = fraction(field5, -1.77904688592943E14d, 4.749865974E11d);
        this.f682d[4][8] = fraction(field5, -4.8211399418367652E18d, 2.18016305204281312E17d);
        this.f682d[4][9] = fraction(field5, 3.0702015625E10d, 3.970037232E9d);
        this.f682d[4][10] = fraction(field5, -8.5916079474274E13d, 2.8009337598E12d);
        this.f682d[4][11] = fraction(field5, -5.919468007E9d, 6.3431046E8d);
        this.f682d[4][12] = fraction(field5, 2479159.0d, 157936.0d);
        this.f682d[4][13] = fraction(field5, -1.875E7d, 602131.0d);
        this.f682d[4][14] = fraction(field5, -1.9203125E7d, 2053168.0d);
        this.f682d[4][15] = fraction(field5, 1.5700361463E10d, 4.38351368E8d);
        this.f682d[5][0] = fraction(field2, 1.2627015655E10d, 6.31822968E8d);
        this.f682d[5][1] = (RealFieldElement) field.getZero();
        this.f682d[5][2] = (RealFieldElement) field.getZero();
        this.f682d[5][3] = (RealFieldElement) field.getZero();
        this.f682d[5][4] = (RealFieldElement) field.getZero();
        Field<T> field6 = field2;
        this.f682d[5][5] = fraction(field6, -7.2955222965E10d, 1.88496616E8d);
        this.f682d[5][6] = fraction(field6, -1.314574495232E13d, 6.9488710893E10d);
        this.f682d[5][7] = fraction(field6, 3.0084216194513E13d, 5.6998391688E10d);
        this.f682d[5][8] = fraction(field6, -2.9685876100664064E17d, 2.5648977082856624E16d);
        this.f682d[5][9] = fraction(field6, 5.69140625E8d, 8.2709109E7d);
        this.f682d[5][10] = fraction(field6, -1.8684190637E10d, 1.8672891732E10d);
        this.f682d[5][11] = fraction(field6, 6.9644045E7d, 8.9549712E7d);
        this.f682d[5][12] = fraction(field6, -1.1847025E7d, 4264272.0d);
        this.f682d[5][13] = fraction(field6, -9.7865E8d, 1.6257537E7d);
        this.f682d[5][14] = fraction(field6, 5.19371875E8d, 6159504.0d);
        this.f682d[5][15] = fraction(field6, 5.256837225E9d, 4.38351368E8d);
        this.f682d[6][0] = fraction(field2, -4.50944925E8d, 1.7550638E7d);
        this.f682d[6][1] = (RealFieldElement) field.getZero();
        this.f682d[6][2] = (RealFieldElement) field.getZero();
        this.f682d[6][3] = (RealFieldElement) field.getZero();
        this.f682d[6][4] = (RealFieldElement) field.getZero();
        Field<T> field7 = field2;
        this.f682d[6][5] = fraction(field7, -1.4532122925E10d, 9.4248308E7d);
        this.f682d[6][6] = fraction(field7, -5.958769664E11d, 2.573655959E9d);
        this.f682d[6][7] = fraction(field7, 1.88748653015E11d, 5.27762886E8d);
        this.f682d[6][8] = fraction(field7, 2.5454854581152343E18d, 2.7252038150535164E16d);
        this.f682d[6][9] = fraction(field7, -1.376953125E9d, 3.6759604E7d);
        this.f682d[6][10] = fraction(field7, 5.3995596795E10d, 5.18691437E8d);
        this.f682d[6][11] = fraction(field7, 2.10311225E8d, 7047894.0d);
        this.f682d[6][12] = fraction(field7, -1718875.0d, 39484.0d);
        this.f682d[6][13] = fraction(field7, 5.8E7d, 602131.0d);
        this.f682d[6][14] = fraction(field7, -1546875.0d, 39484.0d);
        this.f682d[6][15] = fraction(field7, -1.262172375E9d, 8429834.0d);
    }

    /* access modifiers changed from: protected */
    public DormandPrince853FieldStepInterpolator<T> create(Field<T> newField, boolean newForward, T[][] newYDotK, FieldODEStateAndDerivative<T> newGlobalPreviousState, FieldODEStateAndDerivative<T> newGlobalCurrentState, FieldODEStateAndDerivative<T> newSoftPreviousState, FieldODEStateAndDerivative<T> newSoftCurrentState, FieldEquationsMapper<T> newMapper) {
        DormandPrince853FieldStepInterpolator dormandPrince853FieldStepInterpolator = new DormandPrince853FieldStepInterpolator(newField, newForward, newYDotK, newGlobalPreviousState, newGlobalCurrentState, newSoftPreviousState, newSoftCurrentState, newMapper);
        return dormandPrince853FieldStepInterpolator;
    }

    private T fraction(Field<T> field, double p, double q) {
        return (RealFieldElement) ((RealFieldElement) ((RealFieldElement) field.getZero()).add(p)).divide(q);
    }

    /* access modifiers changed from: protected */
    public FieldODEStateAndDerivative<T> computeInterpolatedStateAndDerivatives(FieldEquationsMapper<T> fieldEquationsMapper, T time, T theta, T thetaH, T oneMinusThetaH) throws MaxCountExceededException {
        T[] p;
        T[] interpolatedState;
        RealFieldElement[] realFieldElementArr;
        RealFieldElement[] realFieldElementArr2;
        T t = theta;
        T one = (RealFieldElement) time.getField().getOne();
        T eta = (RealFieldElement) one.subtract(t);
        T twoTheta = (RealFieldElement) t.multiply(2);
        T theta2 = (RealFieldElement) t.multiply(t);
        T dot1 = (RealFieldElement) one.subtract(twoTheta);
        T dot2 = (RealFieldElement) t.multiply(((RealFieldElement) t.multiply(-3)).add(2.0d));
        T dot3 = (RealFieldElement) twoTheta.multiply(((RealFieldElement) t.multiply(twoTheta.subtract(3.0d))).add(1.0d));
        T dot4 = (RealFieldElement) theta2.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(5)).subtract(8.0d))).add(3.0d));
        T dot5 = (RealFieldElement) theta2.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(-6)).add(15.0d))).subtract(12.0d))).add(3.0d));
        T dot6 = (RealFieldElement) theta2.multiply(t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(-7)).add(18.0d))).subtract(15.0d))).add(4.0d)));
        if (getGlobalPreviousState() == null || theta.getReal() > 0.5d) {
            T eta2 = eta;
            RealFieldElement realFieldElement = twoTheta;
            RealFieldElement realFieldElement2 = theta2;
            T one2 = dot6;
            RealFieldElement realFieldElement3 = (RealFieldElement) oneMinusThetaH.negate();
            T t2 = theta;
            RealFieldElement realFieldElement4 = (RealFieldElement) ((RealFieldElement) realFieldElement3.multiply(t2)).negate();
            T f2 = (RealFieldElement) realFieldElement4.multiply(t2);
            T eta3 = eta2;
            T f3 = (RealFieldElement) f2.multiply(eta3);
            T f4 = (RealFieldElement) f3.multiply(t2);
            T f5 = (RealFieldElement) f4.multiply(eta3);
            T f6 = (RealFieldElement) f5.multiply(t2);
            RealFieldElement realFieldElement5 = eta3;
            T[] p2 = (RealFieldElement[]) MathArrays.buildArray(time.getField(), 16);
            T dot62 = one2;
            RealFieldElement[] realFieldElementArr3 = (RealFieldElement[]) MathArrays.buildArray(time.getField(), 16);
            int i = 0;
            while (true) {
                realFieldElementArr = realFieldElementArr3;
                if (i >= p2.length) {
                    break;
                }
                RealFieldElement realFieldElement6 = realFieldElement3;
                p2[i] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement3.multiply(this.f682d[0][i])).add(realFieldElement4.multiply(this.f682d[1][i]))).add(f2.multiply(this.f682d[2][i]))).add(f3.multiply(this.f682d[3][i]))).add(f4.multiply(this.f682d[4][i]))).add(f5.multiply(this.f682d[5][i]))).add(f6.multiply(this.f682d[6][i]));
                RealFieldElement realFieldElement7 = realFieldElement4;
                realFieldElementArr[i] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f682d[0][i].add(dot1.multiply(this.f682d[1][i]))).add(dot2.multiply(this.f682d[2][i]))).add(dot3.multiply(this.f682d[3][i]))).add(dot4.multiply(this.f682d[4][i]))).add(dot5.multiply(this.f682d[5][i]))).add(dot62.multiply(this.f682d[6][i]));
                i++;
                realFieldElementArr3 = realFieldElementArr;
                realFieldElement3 = realFieldElement6;
                realFieldElement4 = realFieldElement7;
            }
            RealFieldElement realFieldElement8 = realFieldElement3;
            RealFieldElement realFieldElement9 = realFieldElement4;
            T f1 = dot62;
            interpolatedState = currentStateLinearCombination(p2[0], p2[1], p2[2], p2[3], p2[4], p2[5], p2[6], p2[7], p2[8], p2[9], p2[10], p2[11], p2[12], p2[13], p2[14], p2[15]);
            p = derivativeLinearCombination(realFieldElementArr[0], realFieldElementArr[1], realFieldElementArr[2], realFieldElementArr[3], realFieldElementArr[4], realFieldElementArr[5], realFieldElementArr[6], realFieldElementArr[7], realFieldElementArr[8], realFieldElementArr[9], realFieldElementArr[10], realFieldElementArr[11], realFieldElementArr[12], realFieldElementArr[13], realFieldElementArr[14], realFieldElementArr[15]);
        } else {
            T f0 = thetaH;
            T f12 = (RealFieldElement) f0.multiply(eta);
            T f22 = (RealFieldElement) f12.multiply(t);
            T f32 = (RealFieldElement) f22.multiply(eta);
            RealFieldElement realFieldElement10 = one;
            RealFieldElement realFieldElement11 = (RealFieldElement) f32.multiply(t);
            RealFieldElement realFieldElement12 = twoTheta;
            T twoTheta2 = (RealFieldElement) realFieldElement11.multiply(eta);
            RealFieldElement realFieldElement13 = theta2;
            T theta22 = (RealFieldElement) twoTheta2.multiply(t);
            T eta4 = eta;
            T[] p3 = (RealFieldElement[]) MathArrays.buildArray(time.getField(), 16);
            T dot63 = dot6;
            RealFieldElement[] realFieldElementArr4 = (RealFieldElement[]) MathArrays.buildArray(time.getField(), 16);
            int i2 = 0;
            while (true) {
                realFieldElementArr2 = realFieldElementArr4;
                if (i2 >= p3.length) {
                    break;
                }
                T f02 = f0;
                p3[i2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) f0.multiply(this.f682d[0][i2])).add(f12.multiply(this.f682d[1][i2]))).add(f22.multiply(this.f682d[2][i2]))).add(f32.multiply(this.f682d[3][i2]))).add(realFieldElement11.multiply(this.f682d[4][i2]))).add(twoTheta2.multiply(this.f682d[5][i2]))).add(theta22.multiply(this.f682d[6][i2]));
                RealFieldElement realFieldElement14 = realFieldElement11;
                realFieldElementArr2[i2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f682d[0][i2].add(dot1.multiply(this.f682d[1][i2]))).add(dot2.multiply(this.f682d[2][i2]))).add(dot3.multiply(this.f682d[3][i2]))).add(dot4.multiply(this.f682d[4][i2]))).add(dot5.multiply(this.f682d[5][i2]))).add(dot63.multiply(this.f682d[6][i2]));
                i2++;
                realFieldElementArr4 = realFieldElementArr2;
                f0 = f02;
                realFieldElement11 = realFieldElement14;
            }
            RealFieldElement realFieldElement15 = realFieldElement11;
            T t3 = f0;
            T f42 = dot63;
            interpolatedState = previousStateLinearCombination(p3[0], p3[1], p3[2], p3[3], p3[4], p3[5], p3[6], p3[7], p3[8], p3[9], p3[10], p3[11], p3[12], p3[13], p3[14], p3[15]);
            p = derivativeLinearCombination(realFieldElementArr2[0], realFieldElementArr2[1], realFieldElementArr2[2], realFieldElementArr2[3], realFieldElementArr2[4], realFieldElementArr2[5], realFieldElementArr2[6], realFieldElementArr2[7], realFieldElementArr2[8], realFieldElementArr2[9], realFieldElementArr2[10], realFieldElementArr2[11], realFieldElementArr2[12], realFieldElementArr2[13], realFieldElementArr2[14], realFieldElementArr2[15]);
            RealFieldElement realFieldElement16 = f42;
            RealFieldElement realFieldElement17 = eta4;
        }
        return new FieldODEStateAndDerivative<>(time, interpolatedState, p);
    }
}
