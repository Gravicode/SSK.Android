package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.analysis.RealFieldUnivariateFunction;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.IntegerSequence.Incrementor;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

public class FieldBracketingNthOrderBrentSolver<T extends RealFieldElement<T>> implements BracketedRealFieldUnivariateSolver<T> {
    private static final int MAXIMAL_AGING = 2;
    private final T absoluteAccuracy;
    private Incrementor evaluations;
    private final Field<T> field;
    private final T functionValueAccuracy;
    private final int maximalOrder;
    private final T relativeAccuracy;

    public FieldBracketingNthOrderBrentSolver(T relativeAccuracy2, T absoluteAccuracy2, T functionValueAccuracy2, int maximalOrder2) throws NumberIsTooSmallException {
        if (maximalOrder2 < 2) {
            throw new NumberIsTooSmallException(Integer.valueOf(maximalOrder2), Integer.valueOf(2), true);
        }
        this.field = relativeAccuracy2.getField();
        this.maximalOrder = maximalOrder2;
        this.absoluteAccuracy = absoluteAccuracy2;
        this.relativeAccuracy = relativeAccuracy2;
        this.functionValueAccuracy = functionValueAccuracy2;
        this.evaluations = Incrementor.create();
    }

    public int getMaximalOrder() {
        return this.maximalOrder;
    }

    public int getMaxEvaluations() {
        return this.evaluations.getMaximalCount();
    }

    public int getEvaluations() {
        return this.evaluations.getCount();
    }

    public T getAbsoluteAccuracy() {
        return this.absoluteAccuracy;
    }

    public T getRelativeAccuracy() {
        return this.relativeAccuracy;
    }

    public T getFunctionValueAccuracy() {
        return this.functionValueAccuracy;
    }

    public T solve(int maxEval, RealFieldUnivariateFunction<T> f, T min, T max, AllowedSolution allowedSolution) throws NullArgumentException, NoBracketingException {
        return solve(maxEval, f, min, max, (RealFieldElement) ((RealFieldElement) min.add(max)).divide(2.0d), allowedSolution);
    }

    public T solve(int maxEval, RealFieldUnivariateFunction<T> f, T min, T max, T startValue, AllowedSolution allowedSolution) throws NullArgumentException, NoBracketingException {
        int signChangeIndex;
        int nbPoints;
        RealFieldElement realFieldElement;
        RealFieldElement realFieldElement2;
        RealFieldElement realFieldElement3;
        RealFieldElement realFieldElement4;
        RealFieldElement realFieldElement5;
        T targetY;
        int agingB;
        RealFieldElement realFieldElement6;
        RealFieldElement[] realFieldElementArr;
        RealFieldElement realFieldElement7;
        RealFieldElement realFieldElement8;
        RealFieldElement realFieldElement9;
        RealFieldElement realFieldElement10;
        RealFieldElement guessX;
        RealFieldElement realFieldElement11;
        RealFieldUnivariateFunction<T> realFieldUnivariateFunction = f;
        MathUtils.checkNotNull(f);
        this.evaluations = this.evaluations.withMaximalCount(maxEval).withStart(0);
        T zero = (RealFieldElement) this.field.getZero();
        RealFieldElement realFieldElement12 = (RealFieldElement) zero.add(Double.NaN);
        RealFieldElement[] realFieldElementArr2 = (RealFieldElement[]) MathArrays.buildArray(this.field, this.maximalOrder + 1);
        T[] y = (RealFieldElement[]) MathArrays.buildArray(this.field, this.maximalOrder + 1);
        realFieldElementArr2[0] = min;
        realFieldElementArr2[1] = startValue;
        realFieldElementArr2[2] = max;
        this.evaluations.increment();
        y[1] = realFieldUnivariateFunction.value(realFieldElementArr2[1]);
        if (Precision.equals(y[1].getReal(), 0.0d, 1)) {
            return realFieldElementArr2[1];
        }
        this.evaluations.increment();
        y[0] = realFieldUnivariateFunction.value(realFieldElementArr2[0]);
        if (Precision.equals(y[0].getReal(), 0.0d, 1)) {
            return realFieldElementArr2[0];
        }
        if (((RealFieldElement) y[0].multiply(y[1])).getReal() < 0.0d) {
            nbPoints = 2;
            signChangeIndex = 1;
        } else {
            this.evaluations.increment();
            y[2] = realFieldUnivariateFunction.value(realFieldElementArr2[2]);
            if (Precision.equals(y[2].getReal(), 0.0d, 1)) {
                return realFieldElementArr2[2];
            }
            if (((RealFieldElement) y[1].multiply(y[2])).getReal() < 0.0d) {
                nbPoints = 3;
                signChangeIndex = 2;
            } else {
                RealFieldUnivariateFunction<T> realFieldUnivariateFunction2 = realFieldUnivariateFunction;
                RealFieldElement realFieldElement13 = zero;
                RealFieldElement realFieldElement14 = realFieldElement12;
                NoBracketingException noBracketingException = new NoBracketingException(realFieldElementArr2[0].getReal(), realFieldElementArr2[2].getReal(), y[0].getReal(), y[2].getReal());
                throw noBracketingException;
            }
        }
        RealFieldElement[] realFieldElementArr3 = (RealFieldElement[]) MathArrays.buildArray(this.field, realFieldElementArr2.length);
        RealFieldElement realFieldElement15 = realFieldElementArr2[signChangeIndex - 1];
        T yA = y[signChangeIndex - 1];
        RealFieldElement realFieldElement16 = (RealFieldElement) realFieldElement15.abs();
        T absYA = (RealFieldElement) yA.abs();
        RealFieldElement realFieldElement17 = realFieldElementArr2[signChangeIndex];
        T yB = y[signChangeIndex];
        RealFieldElement realFieldElement18 = (RealFieldElement) realFieldElement17.abs();
        int nbPoints2 = nbPoints;
        int signChangeIndex2 = signChangeIndex;
        RealFieldElement realFieldElement19 = yB;
        T zero2 = zero;
        RealFieldElement realFieldElement20 = yA;
        RealFieldElement realFieldElement21 = absYA;
        int signChangeIndex3 = 0;
        RealFieldElement realFieldElement22 = (RealFieldElement) yB.abs();
        int agingB2 = 0;
        while (true) {
            RealFieldElement realFieldElement23 = realFieldElement16;
            RealFieldElement realFieldElement24 = ((RealFieldElement) realFieldElement16.subtract(realFieldElement18)).getReal() < 0.0d ? realFieldElement18 : realFieldElement23;
            RealFieldElement realFieldElement25 = realFieldElement22;
            RealFieldElement realFieldElement26 = ((RealFieldElement) realFieldElement21.subtract(realFieldElement22)).getReal() < 0.0d ? realFieldElement25 : realFieldElement21;
            RealFieldElement realFieldElement27 = realFieldElement18;
            realFieldElement = realFieldElement21;
            RealFieldElement realFieldElement28 = (RealFieldElement) this.absoluteAccuracy.add(this.relativeAccuracy.multiply(realFieldElement24));
            double d = 0.0d;
            if (((RealFieldElement) ((RealFieldElement) realFieldElement17.subtract(realFieldElement15)).subtract(realFieldElement28)).getReal() <= 0.0d) {
                int i = agingB2;
                int i2 = signChangeIndex3;
                realFieldElement2 = realFieldElement15;
                RealFieldElement realFieldElement29 = realFieldElement26;
                RealFieldElement realFieldElement30 = realFieldElement28;
                RealFieldElement[] realFieldElementArr4 = realFieldElementArr3;
                realFieldElement3 = realFieldElement20;
                RealFieldElement realFieldElement31 = realFieldElement12;
                int agingA = nbPoints2;
                RealFieldElement realFieldElement32 = realFieldElement23;
                realFieldElement4 = realFieldElement25;
                RealFieldUnivariateFunction<T> realFieldUnivariateFunction3 = f;
                realFieldElement5 = realFieldElement17;
                RealFieldElement realFieldElement33 = realFieldElement24;
                break;
            } else if (((RealFieldElement) realFieldElement26.subtract(this.functionValueAccuracy)).getReal() < 0.0d) {
                int i3 = agingB2;
                int i4 = signChangeIndex3;
                realFieldElement2 = realFieldElement15;
                RealFieldElement realFieldElement34 = realFieldElement26;
                RealFieldElement realFieldElement35 = realFieldElement28;
                RealFieldElement[] realFieldElementArr5 = realFieldElementArr3;
                realFieldElement3 = realFieldElement20;
                RealFieldElement realFieldElement36 = realFieldElement12;
                int agingA2 = nbPoints2;
                RealFieldElement realFieldElement37 = realFieldElement23;
                realFieldElement4 = realFieldElement25;
                RealFieldUnivariateFunction<T> realFieldUnivariateFunction4 = f;
                realFieldElement5 = realFieldElement17;
                RealFieldElement realFieldElement38 = realFieldElement24;
                break;
            } else {
                RealFieldElement realFieldElement39 = realFieldElement15;
                RealFieldElement realFieldElement40 = realFieldElement17;
                if (signChangeIndex3 >= 2) {
                    targetY = (RealFieldElement) ((RealFieldElement) realFieldElement19.divide(16.0d)).negate();
                } else if (agingB2 >= 2) {
                    targetY = (RealFieldElement) ((RealFieldElement) realFieldElement20.divide(16.0d)).negate();
                } else {
                    targetY = zero2;
                }
                int agingA3 = signChangeIndex3;
                T targetY2 = targetY;
                int start = 0;
                int end = nbPoints2;
                while (true) {
                    int end2 = end;
                    System.arraycopy(realFieldElementArr2, start, realFieldElementArr3, start, end2 - start);
                    agingB = agingB2;
                    realFieldElement6 = realFieldElement39;
                    RealFieldElement[] realFieldElementArr6 = realFieldElementArr3;
                    RealFieldElement realFieldElement41 = realFieldElement28;
                    realFieldElementArr = realFieldElementArr3;
                    realFieldElement7 = realFieldElement20;
                    double d2 = d;
                    realFieldElement8 = realFieldElement40;
                    int start2 = start;
                    RealFieldElement realFieldElement42 = realFieldElement26;
                    realFieldElement9 = realFieldElement25;
                    realFieldElement10 = realFieldElement23;
                    RealFieldElement realFieldElement43 = realFieldElement24;
                    guessX = guessX(targetY2, realFieldElementArr6, y, start2, end2);
                    if (((RealFieldElement) guessX.subtract(realFieldElement6)).getReal() <= d2 || ((RealFieldElement) guessX.subtract(realFieldElement8)).getReal() >= d2) {
                        if (signChangeIndex2 - start2 >= end2 - signChangeIndex2) {
                            start = start2 + 1;
                        } else {
                            end2--;
                            start = start2;
                        }
                        guessX = realFieldElement12;
                        end = end2;
                    } else {
                        end = end2;
                        start = start2;
                    }
                    if (Double.isNaN(guessX.getReal()) && end - start > 1) {
                        realFieldElement40 = realFieldElement8;
                        realFieldElement39 = realFieldElement6;
                        realFieldElement26 = realFieldElement42;
                        agingB2 = agingB;
                        realFieldElement24 = realFieldElement43;
                        realFieldElement28 = realFieldElement41;
                        realFieldElement25 = realFieldElement9;
                        d = d2;
                        realFieldElement23 = realFieldElement10;
                        realFieldElementArr3 = realFieldElementArr;
                        realFieldElement20 = realFieldElement7;
                    }
                }
                if (Double.isNaN(guessX.getReal())) {
                    guessX = (RealFieldElement) realFieldElement6.add(((RealFieldElement) realFieldElement8.subtract(realFieldElement6)).divide(2.0d));
                    start = signChangeIndex2 - 1;
                    end = signChangeIndex2;
                }
                this.evaluations.increment();
                T nextY = f.value(guessX);
                RealFieldElement realFieldElement44 = targetY2;
                RealFieldElement realFieldElement45 = realFieldElement12;
                RealFieldElement realFieldElement46 = realFieldElement6;
                if (Precision.equals(nextY.getReal(), 0.0d, 1)) {
                    return guessX;
                }
                int nbPoints3 = nbPoints2;
                if (nbPoints3 > 2 && end - start != nbPoints3) {
                    nbPoints3 = end - start;
                    System.arraycopy(realFieldElementArr2, start, realFieldElementArr2, 0, nbPoints3);
                    System.arraycopy(y, start, y, 0, nbPoints3);
                    signChangeIndex2 -= start;
                } else if (nbPoints3 == realFieldElementArr2.length) {
                    nbPoints3--;
                    if (signChangeIndex2 >= (realFieldElementArr2.length + 1) / 2) {
                        System.arraycopy(realFieldElementArr2, 1, realFieldElementArr2, 0, nbPoints3);
                        System.arraycopy(y, 1, y, 0, nbPoints3);
                        signChangeIndex2--;
                    }
                }
                System.arraycopy(realFieldElementArr2, signChangeIndex2, realFieldElementArr2, signChangeIndex2 + 1, nbPoints3 - signChangeIndex2);
                realFieldElementArr2[signChangeIndex2] = guessX;
                System.arraycopy(y, signChangeIndex2, y, signChangeIndex2 + 1, nbPoints3 - signChangeIndex2);
                y[signChangeIndex2] = nextY;
                nbPoints2 = nbPoints3 + 1;
                RealFieldElement realFieldElement47 = realFieldElement7;
                if (((RealFieldElement) nextY.multiply(realFieldElement47)).getReal() <= 0.0d) {
                    T xB = nextY;
                    realFieldElement17 = guessX;
                    realFieldElement9 = (RealFieldElement) xB.abs();
                    realFieldElement11 = realFieldElement47;
                    signChangeIndex3 = agingA3 + 1;
                    agingB2 = 0;
                    realFieldElement21 = realFieldElement;
                    realFieldElement15 = realFieldElement46;
                    realFieldElement19 = xB;
                } else {
                    realFieldElement11 = nextY;
                    realFieldElement21 = (RealFieldElement) realFieldElement11.abs();
                    signChangeIndex2++;
                    realFieldElement15 = guessX;
                    realFieldElement17 = realFieldElement8;
                    signChangeIndex3 = 0;
                    agingB2 = agingB + 1;
                }
                realFieldElement20 = realFieldElement11;
                realFieldElement16 = realFieldElement10;
                realFieldElement18 = realFieldElement27;
                realFieldElementArr3 = realFieldElementArr;
                realFieldElement22 = realFieldElement9;
                realFieldElement12 = realFieldElement45;
            }
        }
        switch (allowedSolution) {
            case ANY_SIDE:
                return ((RealFieldElement) realFieldElement.subtract(realFieldElement4)).getReal() < 0.0d ? realFieldElement2 : realFieldElement5;
            case LEFT_SIDE:
                return realFieldElement2;
            case RIGHT_SIDE:
                return realFieldElement5;
            case BELOW_SIDE:
                return realFieldElement3.getReal() <= 0.0d ? realFieldElement2 : realFieldElement5;
            case ABOVE_SIDE:
                return realFieldElement3.getReal() < 0.0d ? realFieldElement5 : realFieldElement2;
            default:
                RealFieldElement realFieldElement48 = realFieldElement;
                RealFieldElement realFieldElement49 = realFieldElement4;
                throw new MathInternalError(null);
        }
    }

    private T guessX(T targetY, T[] x, T[] y, int start, int end) {
        for (int i = start; i < end - 1; i++) {
            int delta = (i + 1) - start;
            for (int j = end - 1; j > i; j--) {
                x[j] = (RealFieldElement) ((RealFieldElement) x[j].subtract(x[j - 1])).divide(y[j].subtract(y[j - delta]));
            }
        }
        T x0 = (RealFieldElement) this.field.getZero();
        for (int j2 = end - 1; j2 >= start; j2--) {
            x0 = (RealFieldElement) x[j2].add(x0.multiply(targetY.subtract(y[j2])));
        }
        return x0;
    }
}
