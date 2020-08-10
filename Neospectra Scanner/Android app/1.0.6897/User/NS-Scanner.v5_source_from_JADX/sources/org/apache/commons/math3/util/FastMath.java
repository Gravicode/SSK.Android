package org.apache.commons.math3.util;

import java.io.PrintStream;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.poi.p009ss.usermodel.ShapeTypes;

public class FastMath {
    private static final double[] CBRTTWO = {0.6299605249474366d, 0.7937005259840998d, 1.0d, 1.2599210498948732d, 1.5874010519681994d};
    private static final double[] COSINE_TABLE_A = {1.0d, 0.9921976327896118d, 0.9689123630523682d, 0.9305076599121094d, 0.8775825500488281d, 0.8109631538391113d, 0.7316888570785522d, 0.6409968137741089d, 0.5403022766113281d, 0.4311765432357788d, 0.3153223395347595d, 0.19454771280288696d, 0.07073719799518585d, -0.05417713522911072d};
    private static final double[] COSINE_TABLE_B = {0.0d, 3.4439717236742845E-8d, 5.865827662008209E-8d, -3.7999795083850525E-8d, 1.184154459111628E-8d, -3.43338934259355E-8d, 1.1795268640216787E-8d, 4.438921624363781E-8d, 2.925681159240093E-8d, -2.6437112632041807E-8d, 2.2860509143963117E-8d, -4.813899778443457E-9d, 3.6725170580355583E-9d, 2.0217439756338078E-10d};

    /* renamed from: E */
    public static final double f806E = 2.718281828459045d;
    private static final double[] EIGHTHS = {0.0d, 0.125d, F_1_4, 0.375d, F_1_2, 0.625d, F_3_4, F_7_8, 1.0d, 1.125d, 1.25d, 1.375d, 1.5d, 1.625d};
    static final int EXP_FRAC_TABLE_LEN = 1025;
    static final int EXP_INT_TABLE_LEN = 1500;
    static final int EXP_INT_TABLE_MAX_INDEX = 750;
    private static final double F_11_12 = 0.9166666666666666d;
    private static final double F_13_14 = 0.9285714285714286d;
    private static final double F_15_16 = 0.9375d;
    private static final double F_1_11 = 0.09090909090909091d;
    private static final double F_1_13 = 0.07692307692307693d;
    private static final double F_1_15 = 0.06666666666666667d;
    private static final double F_1_17 = 0.058823529411764705d;
    private static final double F_1_2 = 0.5d;
    private static final double F_1_3 = 0.3333333333333333d;
    private static final double F_1_4 = 0.25d;
    private static final double F_1_5 = 0.2d;
    private static final double F_1_7 = 0.14285714285714285d;
    private static final double F_1_9 = 0.1111111111111111d;
    private static final double F_3_4 = 0.75d;
    private static final double F_5_6 = 0.8333333333333334d;
    private static final double F_7_8 = 0.875d;
    private static final double F_9_10 = 0.9d;
    private static final long HEX_40000000 = 1073741824;
    private static final long IMPLICIT_HIGH_BIT = 4503599627370496L;
    private static final double LN_2_A = 0.6931470632553101d;
    private static final double LN_2_B = 1.1730463525082348E-7d;
    private static final double[][] LN_HI_PREC_COEF = {new double[]{1.0d, -6.032174644509064E-23d}, new double[]{-0.25d, -0.25d}, new double[]{0.3333333134651184d, 1.9868161777724352E-8d}, new double[]{-0.2499999701976776d, -2.957007209750105E-8d}, new double[]{0.19999954104423523d, 1.5830993332061267E-10d}, new double[]{-0.16624879837036133d, -2.6033824355191673E-8d}};
    static final int LN_MANT_LEN = 1024;
    private static final double[][] LN_QUICK_COEF = {new double[]{1.0d, 5.669184079525E-24d}, new double[]{-0.25d, -0.25d}, new double[]{0.3333333134651184d, 1.986821492305628E-8d}, new double[]{-0.25d, -6.663542893624021E-14d}, new double[]{0.19999998807907104d, 1.1921056801463227E-8d}, new double[]{-0.1666666567325592d, -7.800414592973399E-9d}, new double[]{0.1428571343421936d, 5.650007086920087E-9d}, new double[]{-0.12502530217170715d, -7.44321345601866E-11d}, new double[]{0.11113807559013367d, 9.219544613762692E-9d}};
    private static final double LOG_MAX_VALUE = StrictMath.log(Double.MAX_VALUE);
    private static final long MASK_30BITS = -1073741824;
    private static final long MASK_DOUBLE_EXPONENT = 9218868437227405312L;
    private static final long MASK_DOUBLE_MANTISSA = 4503599627370495L;
    private static final int MASK_NON_SIGN_INT = Integer.MAX_VALUE;
    private static final long MASK_NON_SIGN_LONG = Long.MAX_VALUE;

    /* renamed from: PI */
    public static final double f807PI = 3.141592653589793d;
    private static final long[] PI_O_4_BITS = {-3958705157555305932L, -4267615245585081135L};
    private static final long[] RECIP_2PI = {2935890503282001226L, 9154082963658192752L, 3952090531849364496L, 9193070505571053912L, 7910884519577875640L, 113236205062349959L, 4577762542105553359L, -5034868814120038111L, 4208363204685324176L, 5648769086999809661L, 2819561105158720014L, -4035746434778044925L, -302932621132653753L, -2644281811660520851L, -3183605296591799669L, 6722166367014452318L, -3512299194304650054L, -7278142539171889152L};
    private static final boolean RECOMPUTE_TABLES_AT_RUNTIME = false;
    private static final double[] SINE_TABLE_A = {0.0d, 0.1246747374534607d, 0.24740394949913025d, 0.366272509098053d, 0.4794255495071411d, 0.5850973129272461d, 0.6816387176513672d, 0.7675435543060303d, 0.8414709568023682d, 0.902267575263977d, 0.9489846229553223d, 0.9808930158615112d, 0.9974949359893799d, 0.9985313415527344d};
    private static final double[] SINE_TABLE_B = {0.0d, -4.068233003401932E-9d, 9.755392680573412E-9d, 1.9987994582857286E-8d, -1.0902938113007961E-8d, -3.9986783938944604E-8d, 4.23719669792332E-8d, -5.207000323380292E-8d, 2.800552834259E-8d, 1.883511811213715E-8d, -3.5997360512765566E-9d, 4.116164446561962E-8d, 5.0614674548127384E-8d, -1.0129027912496858E-9d};
    private static final int SINE_TABLE_LEN = 14;
    private static final double[] TANGENT_TABLE_A = {0.0d, 0.1256551444530487d, 0.25534194707870483d, 0.3936265707015991d, 0.5463024377822876d, 0.7214844226837158d, 0.9315965175628662d, 1.1974215507507324d, 1.5574076175689697d, 2.092571258544922d, 3.0095696449279785d, 5.041914939880371d, 14.101419448852539d, -18.430862426757812d};
    private static final double[] TANGENT_TABLE_B = {0.0d, -7.877917738262007E-9d, -2.5857668567479893E-8d, 5.2240336371356666E-9d, 5.206150291559893E-8d, 1.8307188599677033E-8d, -5.7618793749770706E-8d, 7.848361555046424E-8d, 1.0708593250394448E-7d, 1.7827257129423813E-8d, 2.893485277253286E-8d, 3.1660099222737955E-7d, 4.983191803254889E-7d, -3.356118100840571E-7d};
    private static final double TWO_POWER_52 = 4.503599627370496E15d;

    private static class CodyWaite {
        private final int finalK;
        private final double finalRemA;
        private final double finalRemB;

        CodyWaite(double xa) {
            int k = (int) (0.6366197723675814d * xa);
            while (true) {
                double a = ((double) (-k)) * 1.570796251296997d;
                double remA = xa + a;
                double a2 = ((double) (-k)) * 7.549789948768648E-8d;
                double b = remA;
                double remA2 = a2 + b;
                double remB = (-((remA - xa) - a)) + (-((remA2 - b) - a2));
                double a3 = ((double) (-k)) * 6.123233995736766E-17d;
                double b2 = remA2;
                double remA3 = a3 + b2;
                double remB2 = remB + (-((remA3 - b2) - a3));
                if (remA3 > 0.0d) {
                    this.finalK = k;
                    this.finalRemA = remA3;
                    this.finalRemB = remB2;
                    return;
                }
                k--;
            }
        }

        /* access modifiers changed from: 0000 */
        public int getK() {
            return this.finalK;
        }

        /* access modifiers changed from: 0000 */
        public double getRemA() {
            return this.finalRemA;
        }

        /* access modifiers changed from: 0000 */
        public double getRemB() {
            return this.finalRemB;
        }
    }

    private static class ExpFracTable {
        /* access modifiers changed from: private */
        public static final double[] EXP_FRAC_TABLE_A = FastMathLiteralArrays.loadExpFracA();
        /* access modifiers changed from: private */
        public static final double[] EXP_FRAC_TABLE_B = FastMathLiteralArrays.loadExpFracB();

        private ExpFracTable() {
        }
    }

    private static class ExpIntTable {
        /* access modifiers changed from: private */
        public static final double[] EXP_INT_TABLE_A = FastMathLiteralArrays.loadExpIntA();
        /* access modifiers changed from: private */
        public static final double[] EXP_INT_TABLE_B = FastMathLiteralArrays.loadExpIntB();

        private ExpIntTable() {
        }
    }

    private static class Split {
        public static final Split NAN = new Split(Double.NaN, 0.0d);
        public static final Split NEGATIVE_INFINITY = new Split(Double.NEGATIVE_INFINITY, 0.0d);
        public static final Split POSITIVE_INFINITY = new Split(Double.POSITIVE_INFINITY, 0.0d);
        /* access modifiers changed from: private */
        public final double full;
        private final double high;
        private final double low;

        Split(double x) {
            this.full = x;
            this.high = Double.longBitsToDouble(Double.doubleToRawLongBits(x) & -134217728);
            this.low = x - this.high;
        }

        Split(double high2, double low2) {
            double d;
            double d2;
            if (high2 != 0.0d) {
                d2 = high2 + low2;
            } else if (low2 == 0.0d && Double.doubleToRawLongBits(high2) == Long.MIN_VALUE) {
                d2 = -0.0d;
            } else {
                d = low2;
                this(d, high2, low2);
            }
            d = d2;
            this(d, high2, low2);
        }

        Split(double full2, double high2, double low2) {
            this.full = full2;
            this.high = high2;
            this.low = low2;
        }

        public Split multiply(Split b) {
            Split mulBasic = new Split(this.full * b.full);
            return new Split(mulBasic.high, mulBasic.low + ((this.low * b.low) - (((mulBasic.full - (this.high * b.high)) - (this.low * b.high)) - (this.high * b.low))));
        }

        public Split reciprocal() {
            Split splitInv = new Split(1.0d / this.full);
            Split product = multiply(splitInv);
            double error = (product.high - 1.0d) + product.low;
            return Double.isNaN(error) ? splitInv : new Split(splitInv.high, splitInv.low - (error / this.full));
        }

        /* access modifiers changed from: private */
        public Split pow(long e) {
            Split result = new Split(1.0d);
            Split d2p = new Split(this.full, this.high, this.low);
            Split d2p2 = d2p;
            for (long p = e; p != 0; p >>>= 1) {
                if ((p & 1) != 0) {
                    result = result.multiply(d2p2);
                }
                d2p2 = d2p2.multiply(d2p2);
            }
            if (!Double.isNaN(result.full)) {
                return result;
            }
            if (Double.isNaN(this.full)) {
                return NAN;
            }
            if (FastMath.abs(this.full) < 1.0d) {
                return new Split(FastMath.copySign(0.0d, this.full), 0.0d);
            }
            if (this.full >= 0.0d || (e & 1) != 1) {
                return POSITIVE_INFINITY;
            }
            return NEGATIVE_INFINITY;
        }
    }

    private static class lnMant {
        /* access modifiers changed from: private */
        public static final double[][] LN_MANT = FastMathLiteralArrays.loadLnMant();

        private lnMant() {
        }
    }

    private FastMath() {
    }

    private static double doubleHighPart(double d) {
        if (d <= (-Precision.SAFE_MIN) || d >= Precision.SAFE_MIN) {
            return Double.longBitsToDouble(Double.doubleToRawLongBits(d) & MASK_30BITS);
        }
        return d;
    }

    public static double sqrt(double a) {
        return Math.sqrt(a);
    }

    public static double cosh(double x) {
        double x2 = x;
        if (x2 != x2) {
            return x2;
        }
        if (x2 > 20.0d) {
            if (x2 < LOG_MAX_VALUE) {
                return exp(x) * F_1_2;
            }
            double t = exp(x2 * F_1_2);
            return F_1_2 * t * t;
        } else if (x2 >= -20.0d) {
            double[] hiPrec = new double[2];
            if (x2 < 0.0d) {
                x2 = -x2;
            }
            exp(x2, 0.0d, hiPrec);
            double ya = hiPrec[0] + hiPrec[1];
            double yb = -((ya - hiPrec[0]) - hiPrec[1]);
            double temp = ya * 1.073741824E9d;
            double yaa = (ya + temp) - temp;
            double yab = ya - yaa;
            double recip = 1.0d / ya;
            double temp2 = 1.073741824E9d * recip;
            double recipa = (recip + temp2) - temp2;
            double recipb = recip - recipa;
            double recipb2 = recipb + (((((1.0d - (yaa * recipa)) - (yaa * recipb)) - (yab * recipa)) - (yab * recipb)) * recip) + ((-yb) * recip * recip);
            double temp3 = ya + recipa;
            double ya2 = temp3;
            double temp4 = ya2 + recipb2;
            return (temp4 + yb + (-((temp3 - ya) - recipa)) + (-((temp4 - ya2) - recipb2))) * F_1_2;
        } else if (x2 > (-LOG_MAX_VALUE)) {
            return exp(-x2) * F_1_2;
        } else {
            double t2 = exp(-0.5d * x2);
            return F_1_2 * t2 * t2;
        }
    }

    public static double sinh(double x) {
        double result;
        double x2 = x;
        boolean negate = false;
        if (x2 != x2) {
            return x2;
        }
        if (x2 > 20.0d) {
            if (x2 < LOG_MAX_VALUE) {
                return exp(x) * F_1_2;
            }
            double t = exp(x2 * F_1_2);
            return F_1_2 * t * t;
        } else if (x2 < -20.0d) {
            if (x2 > (-LOG_MAX_VALUE)) {
                return exp(-x2) * -0.5d;
            }
            double t2 = exp(x2 * -0.5d);
            return -0.5d * t2 * t2;
        } else if (x2 == 0.0d) {
            return x2;
        } else {
            if (x2 < 0.0d) {
                x2 = -x2;
                negate = true;
            }
            if (x2 > F_1_4) {
                double[] hiPrec = new double[2];
                exp(x2, 0.0d, hiPrec);
                double ya = hiPrec[0] + hiPrec[1];
                double yb = -((ya - hiPrec[0]) - hiPrec[1]);
                double temp = ya * 1.073741824E9d;
                double yaa = (ya + temp) - temp;
                double yab = ya - yaa;
                double recip = 1.0d / ya;
                double temp2 = 1.073741824E9d * recip;
                double recipa = (recip + temp2) - temp2;
                double recipb = recip - recipa;
                double recipb2 = recipb + (((((1.0d - (yaa * recipa)) - (yaa * recipb)) - (yab * recipa)) - (yab * recipb)) * recip);
                double recipa2 = -recipa;
                double recipb3 = -(((-yb) * recip * recip) + recipb2);
                double temp3 = ya + recipa2;
                double d = ya;
                double yb2 = yb + (-((temp3 - ya) - recipa2));
                double ya2 = temp3;
                double temp4 = ya2 + recipb3;
                double[] dArr = hiPrec;
                double d2 = recipa2;
                result = (temp4 + yb2 + (-((temp4 - ya2) - recipb3))) * F_1_2;
                double d3 = x2;
            } else {
                double[] hiPrec2 = new double[2];
                expm1(x2, hiPrec2);
                double ya3 = hiPrec2[0] + hiPrec2[1];
                double yb3 = -((ya3 - hiPrec2[0]) - hiPrec2[1]);
                double denom = ya3 + 1.0d;
                double denomr = 1.0d / denom;
                double ratio = ya3 * denomr;
                double temp5 = ratio * 1.073741824E9d;
                double ra = (ratio + temp5) - temp5;
                double rb = ratio - ra;
                double temp6 = 1.073741824E9d * denom;
                double za = (denom + temp6) - temp6;
                double zb = denom - za;
                double d4 = x2;
                double rb2 = rb + (((((ya3 - (za * ra)) - (za * rb)) - (zb * ra)) - (zb * rb)) * denomr) + (yb3 * denomr) + ((-ya3) * ((-((denom - 1.0d) - ya3)) + yb3) * denomr * denomr);
                double temp7 = ya3 + ra;
                double ya4 = temp7;
                double temp8 = ya4 + rb2;
                result = (temp8 + yb3 + (-((temp7 - ya3) - ra)) + (-((temp8 - ya4) - rb2))) * F_1_2;
            }
            if (negate) {
                result = -result;
            }
            return result;
        }
    }

    public static double tanh(double x) {
        double ratioa;
        double x2 = x;
        boolean negate = false;
        if (x2 != x2) {
            return x2;
        }
        if (x2 > 20.0d) {
            return 1.0d;
        }
        if (x2 < -20.0d) {
            return -1.0d;
        }
        if (x2 == 0.0d) {
            return x2;
        }
        if (x2 < 0.0d) {
            x2 = -x2;
            negate = true;
        }
        if (x2 >= F_1_2) {
            double[] hiPrec = new double[2];
            exp(x2 * 2.0d, 0.0d, hiPrec);
            double ya = hiPrec[0] + hiPrec[1];
            double yb = -((ya - hiPrec[0]) - hiPrec[1]);
            double na = -1.0d + ya;
            double temp = na + yb;
            double nb = (-((na + 1.0d) - ya)) + (-((temp - na) - yb));
            double na2 = temp;
            double da = ya + 1.0d;
            double temp2 = da + yb;
            double d = ya;
            double db = (-((da - 1.0d) - ya)) + (-((temp2 - da) - yb));
            double da2 = temp2;
            double temp3 = da2 * 1.073741824E9d;
            double daa = (da2 + temp3) - temp3;
            double dab = da2 - daa;
            double ratio = na2 / da2;
            double temp4 = ratio * 1.073741824E9d;
            double ratioa2 = (ratio + temp4) - temp4;
            double ratiob = ratio - ratioa2;
            double d2 = yb;
            ratioa = ratioa2 + ratiob + (((((na2 - (daa * ratioa2)) - (daa * ratiob)) - (dab * ratioa2)) - (dab * ratiob)) / da2) + (nb / da2) + ((((-db) * na2) / da2) / da2);
            double d3 = x2;
        } else {
            double[] hiPrec2 = new double[2];
            expm1(x2 * 2.0d, hiPrec2);
            double ya2 = hiPrec2[0] + hiPrec2[1];
            double yb2 = -((ya2 - hiPrec2[0]) - hiPrec2[1]);
            double na3 = ya2;
            double da3 = ya2 + 2.0d;
            double temp5 = da3 + yb2;
            double d4 = x2;
            double db2 = (-((da3 - 2.0d) - ya2)) + (-((temp5 - da3) - yb2));
            double da4 = temp5;
            double temp6 = da4 * 1.073741824E9d;
            double daa2 = (da4 + temp6) - temp6;
            double dab2 = da4 - daa2;
            double ratio2 = na3 / da4;
            double temp7 = 1.073741824E9d * ratio2;
            double ratioa3 = (ratio2 + temp7) - temp7;
            double ratiob2 = ratio2 - ratioa3;
            double[] dArr = hiPrec2;
            double d5 = ya2;
            ratioa = ratioa3 + ratiob2 + (((((na3 - (daa2 * ratioa3)) - (daa2 * ratiob2)) - (dab2 * ratioa3)) - (dab2 * ratiob2)) / da4) + (yb2 / da4) + ((((-db2) * na3) / da4) / da4);
        }
        double result = ratioa;
        if (negate) {
            result = -result;
        }
        return result;
    }

    public static double acosh(double a) {
        return log(sqrt((a * a) - 1.0d) + a);
    }

    public static double asinh(double a) {
        double absAsinh;
        double a2 = a;
        boolean negative = false;
        if (a2 < 0.0d) {
            negative = true;
            a2 = -a2;
        }
        if (a2 > 0.167d) {
            absAsinh = log(sqrt((a2 * a2) + 1.0d) + a2);
        } else {
            double a22 = a2 * a2;
            if (a2 > 0.097d) {
                absAsinh = a2 * (1.0d - (((F_1_3 - (((F_1_5 - (((F_1_7 - (((F_1_9 - (((F_1_11 - (((F_1_13 - (((F_1_15 - ((F_1_17 * a22) * F_15_16)) * a22) * F_13_14)) * a22) * F_11_12)) * a22) * F_9_10)) * a22) * F_7_8)) * a22) * F_5_6)) * a22) * F_3_4)) * a22) * F_1_2));
            } else if (a2 > 0.036d) {
                absAsinh = a2 * (1.0d - (((F_1_3 - (((F_1_5 - (((F_1_7 - (((F_1_9 - (((F_1_11 - ((F_1_13 * a22) * F_11_12)) * a22) * F_9_10)) * a22) * F_7_8)) * a22) * F_5_6)) * a22) * F_3_4)) * a22) * F_1_2));
            } else if (a2 > 0.0036d) {
                absAsinh = a2 * (1.0d - (((F_1_3 - (((F_1_5 - (((F_1_7 - ((F_1_9 * a22) * F_7_8)) * a22) * F_5_6)) * a22) * F_3_4)) * a22) * F_1_2));
            } else {
                absAsinh = a2 * (1.0d - (((F_1_3 - ((F_1_5 * a22) * F_3_4)) * a22) * F_1_2));
            }
        }
        return negative ? -absAsinh : absAsinh;
    }

    public static double atanh(double a) {
        double absAtanh;
        double a2 = a;
        boolean negative = false;
        if (a2 < 0.0d) {
            negative = true;
            a2 = -a2;
        }
        if (a2 > 0.15d) {
            absAtanh = log((a2 + 1.0d) / (1.0d - a2)) * F_1_2;
        } else {
            double a22 = a2 * a2;
            if (a2 > 0.087d) {
                absAtanh = a2 * ((((((((((((((((F_1_17 * a22) + F_1_15) * a22) + F_1_13) * a22) + F_1_11) * a22) + F_1_9) * a22) + F_1_7) * a22) + F_1_5) * a22) + F_1_3) * a22) + 1.0d);
            } else if (a2 > 0.031d) {
                absAtanh = a2 * ((((((((((((F_1_13 * a22) + F_1_11) * a22) + F_1_9) * a22) + F_1_7) * a22) + F_1_5) * a22) + F_1_3) * a22) + 1.0d);
            } else if (a2 > 0.003d) {
                absAtanh = a2 * ((((((((F_1_9 * a22) + F_1_7) * a22) + F_1_5) * a22) + F_1_3) * a22) + 1.0d);
            } else {
                absAtanh = a2 * ((((F_1_5 * a22) + F_1_3) * a22) + 1.0d);
            }
        }
        return negative ? -absAtanh : absAtanh;
    }

    public static double signum(double a) {
        if (a < 0.0d) {
            return -1.0d;
        }
        if (a > 0.0d) {
            return 1.0d;
        }
        return a;
    }

    public static float signum(float a) {
        if (a < 0.0f) {
            return -1.0f;
        }
        if (a > 0.0f) {
            return 1.0f;
        }
        return a;
    }

    public static double nextUp(double a) {
        return nextAfter(a, Double.POSITIVE_INFINITY);
    }

    public static float nextUp(float a) {
        return nextAfter(a, Double.POSITIVE_INFINITY);
    }

    public static double nextDown(double a) {
        return nextAfter(a, Double.NEGATIVE_INFINITY);
    }

    public static float nextDown(float a) {
        return nextAfter(a, Double.NEGATIVE_INFINITY);
    }

    public static double random() {
        return Math.random();
    }

    public static double exp(double x) {
        return exp(x, 0.0d, null);
    }

    private static double exp(double x, double extra, double[] hiPrec) {
        double result;
        double d = x;
        double d2 = extra;
        double[] dArr = hiPrec;
        int intVal = (int) d;
        if (d < 0.0d) {
            if (d < -746.0d) {
                if (dArr != null) {
                    dArr[0] = 0.0d;
                    dArr[1] = 0.0d;
                }
                return 0.0d;
            } else if (intVal < -709) {
                double result2 = exp(40.19140625d + d, d2, dArr) / 2.85040095144011776E17d;
                if (dArr != null) {
                    dArr[0] = dArr[0] / 2.85040095144011776E17d;
                    dArr[1] = dArr[1] / 2.85040095144011776E17d;
                }
                return result2;
            } else if (intVal == -709) {
                double result3 = exp(1.494140625d + d, d2, dArr) / 4.455505956692757d;
                if (dArr != null) {
                    dArr[0] = dArr[0] / 4.455505956692757d;
                    dArr[1] = dArr[1] / 4.455505956692757d;
                }
                return result3;
            } else {
                intVal--;
            }
        } else if (intVal > 709) {
            if (dArr != null) {
                dArr[0] = Double.POSITIVE_INFINITY;
                dArr[1] = 0.0d;
            }
            return Double.POSITIVE_INFINITY;
        }
        double intPartA = ExpIntTable.EXP_INT_TABLE_A[intVal + EXP_INT_TABLE_MAX_INDEX];
        double intPartB = ExpIntTable.EXP_INT_TABLE_B[intVal + EXP_INT_TABLE_MAX_INDEX];
        int intFrac = (int) ((d - ((double) intVal)) * 1024.0d);
        double fracPartA = ExpFracTable.EXP_FRAC_TABLE_A[intFrac];
        double fracPartB = ExpFracTable.EXP_FRAC_TABLE_B[intFrac];
        double epsilon = d - (((double) intVal) + (((double) intFrac) / 1024.0d));
        double z = (((((((0.04168701738764507d * epsilon) + 0.1666666505023083d) * epsilon) + 0.5000000000042687d) * epsilon) + 1.0d) * epsilon) - 1.1409003175371524E20d;
        double tempA = intPartA * fracPartA;
        double tempB = (intPartA * fracPartB) + (intPartB * fracPartA) + (intPartB * fracPartB);
        double tempC = tempB + tempA;
        if (tempC == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }
        if (d2 != 0.0d) {
            result = (tempC * d2 * z) + (tempC * d2) + (tempC * z) + tempB + tempA;
        } else {
            result = (tempC * z) + tempB + tempA;
        }
        if (dArr != null) {
            dArr[0] = tempA;
            dArr[1] = (tempC * d2 * z) + (tempC * d2) + (tempC * z) + tempB;
        }
        return result;
    }

    public static double expm1(double x) {
        return expm1(x, null);
    }

    private static double expm1(double x, double[] hiPrecOut) {
        double x2 = x;
        if (x2 != x2 || x2 == 0.0d) {
            return x2;
        }
        if (x2 <= -1.0d || x2 >= 1.0d) {
            double[] hiPrec = new double[2];
            exp(x2, 0.0d, hiPrec);
            if (x2 > 0.0d) {
                return (hiPrec[0] - 4.0d) + hiPrec[1];
            }
            double ra = hiPrec[0] - 4.0d;
            return ra + (-((ra + 1.0d) - hiPrec[0])) + hiPrec[1];
        }
        boolean negative = false;
        if (x2 < 0.0d) {
            x2 = -x2;
            negative = true;
        }
        int intFrac = (int) (x2 * 1024.0d);
        double tempA = ExpFracTable.EXP_FRAC_TABLE_A[intFrac] - 1.0d;
        double tempB = ExpFracTable.EXP_FRAC_TABLE_B[intFrac];
        double temp = tempA + tempB;
        double tempA2 = temp;
        double temp2 = tempA2 * 1.073741824E9d;
        double baseA = (tempA2 + temp2) - temp2;
        double baseB = (-((temp - tempA) - tempB)) + (tempA2 - baseA);
        double epsilon = x2 - (((double) intFrac) / 1024.0d);
        double zb = ((((((0.008336750013465571d * epsilon) + 0.041666663879186654d) * epsilon) + 0.16666666666745392d) * epsilon) + 0.49999999999999994d) * epsilon * epsilon;
        double za = epsilon;
        double temp3 = za + zb;
        double zb2 = -((temp3 - za) - zb);
        double za2 = temp3;
        double temp4 = za2 * 1.073741824E9d;
        double temp5 = (za2 + temp4) - temp4;
        double zb3 = zb2 + (za2 - temp5);
        double za3 = temp5;
        double ya = za3 * baseA;
        double temp6 = (za3 * baseB) + ya;
        double ya2 = temp6;
        double temp7 = ya2 + (zb3 * baseA);
        double yb = (-((temp6 - ya) - (za3 * baseB))) + (-((temp7 - ya2) - (zb3 * baseA)));
        double ya3 = temp7;
        double temp8 = (zb3 * baseB) + ya3;
        double d = x2;
        double yb2 = yb + (-((temp8 - ya3) - (zb3 * baseB)));
        double ya4 = temp8;
        double temp9 = ya4 + baseA;
        double ya5 = temp9;
        double temp10 = ya5 + za3;
        double ya6 = temp10;
        double temp11 = ya6 + baseB;
        double ya7 = temp11;
        double temp12 = ya7 + zb3;
        double yb3 = yb2 + (-((temp9 - baseA) - ya4)) + (-((temp10 - ya5) - za3)) + (-((temp11 - ya6) - baseB)) + (-((temp12 - ya7) - zb3));
        double ya8 = temp12;
        if (negative) {
            double denom = ya8 + 1.0d;
            double denomr = 1.0d / denom;
            double ratio = ya8 * denomr;
            double temp13 = ratio * 1.073741824E9d;
            double d2 = epsilon;
            double ra2 = (ratio + temp13) - temp13;
            double rb = ratio - ra2;
            double temp14 = denom * 1.073741824E9d;
            double za4 = (denom + temp14) - temp14;
            double zb4 = denom - za4;
            boolean z = negative;
            double d3 = -ya8;
            ya8 = -ra2;
            yb3 = -(rb + (((((ya8 - (za4 * ra2)) - (za4 * rb)) - (zb4 * ra2)) - (zb4 * rb)) * denomr) + (yb3 * denomr) + (d3 * ((-((denom - 1.0d) - ya8)) + yb3) * denomr * denomr));
            double d4 = za4;
        } else {
            boolean z2 = negative;
        }
        if (hiPrecOut != null) {
            hiPrecOut[0] = ya8;
            hiPrecOut[1] = yb3;
        }
        return ya8 + yb3;
    }

    public static double log(double x) {
        return log(x, (double[]) null);
    }

    private static double log(double x, double[] hiPrec) {
        double lnza;
        double denom;
        if (x == 0.0d) {
            return Double.NEGATIVE_INFINITY;
        }
        long bits = Double.doubleToRawLongBits(x);
        if (((Long.MIN_VALUE & bits) != 0 || x != x) && x != 0.0d) {
            if (hiPrec != null) {
                hiPrec[0] = Double.NaN;
            }
            return Double.NaN;
        } else if (x == Double.POSITIVE_INFINITY) {
            if (hiPrec != null) {
                hiPrec[0] = Double.POSITIVE_INFINITY;
            }
            return Double.POSITIVE_INFINITY;
        } else {
            int exp = ((int) (bits >> 52)) - 1023;
            if ((MASK_DOUBLE_EXPONENT & bits) == 0) {
                if (x == 0.0d) {
                    if (hiPrec != null) {
                        hiPrec[0] = Double.NEGATIVE_INFINITY;
                    }
                    return Double.NEGATIVE_INFINITY;
                }
                bits <<= 1;
                while ((4503599627370496L & bits) == 0) {
                    exp--;
                    bits <<= 1;
                }
            }
            if ((exp == -1 || exp == 0) && x < 1.01d && x > 0.99d && hiPrec == null) {
                double xa = x - 1.0d;
                double d = (xa - x) + 1.0d;
                double tmp = xa * 1.073741824E9d;
                double aa = (xa + tmp) - tmp;
                double ab = xa - aa;
                double xa2 = aa;
                double xb = ab;
                double[] lnCoef_last = LN_QUICK_COEF[LN_QUICK_COEF.length - 1];
                double ya = lnCoef_last[0];
                double yb = lnCoef_last[1];
                int i = LN_QUICK_COEF.length - 2;
                while (i >= 0) {
                    double aa2 = ya * xa2;
                    double tmp2 = aa2 * 1.073741824E9d;
                    double ya2 = (aa2 + tmp2) - tmp2;
                    double yb2 = (aa2 - ya2) + (ya * xb) + (yb * xa2) + (yb * xb);
                    double[] lnCoef_i = LN_QUICK_COEF[i];
                    double aa3 = ya2 + lnCoef_i[0];
                    double ab2 = yb2 + lnCoef_i[1];
                    double tmp3 = aa3 * 1.073741824E9d;
                    double ya3 = (aa3 + tmp3) - tmp3;
                    yb = (aa3 - ya3) + ab2;
                    i--;
                    ya = ya3;
                    double ya4 = ab2;
                }
                double aa4 = ya * xa2;
                double tmp4 = aa4 * 1.073741824E9d;
                double ya5 = (aa4 + tmp4) - tmp4;
                return ya5 + (aa4 - ya5) + (ya * xb) + (yb * xa2) + (yb * xb);
            }
            double[] lnm = lnMant.LN_MANT[(int) ((bits & 4499201580859392L) >> 42)];
            double epsilon = ((double) (bits & 4398046511103L)) / (((double) (bits & 4499201580859392L)) + TWO_POWER_52);
            if (hiPrec != null) {
                double tmp5 = epsilon * 1.073741824E9d;
                double aa5 = (epsilon + tmp5) - tmp5;
                double xa3 = aa5;
                double xb2 = epsilon - aa5;
                double denom2 = ((double) (bits & 4499201580859392L)) + TWO_POWER_52;
                double xb3 = xb2 + (((((double) (bits & 4398046511103L)) - (xa3 * denom2)) - (xb2 * denom2)) / denom2);
                double[] lnCoef_last2 = LN_HI_PREC_COEF[LN_HI_PREC_COEF.length - 1];
                double ya6 = lnCoef_last2[0];
                double yb3 = lnCoef_last2[1];
                int i2 = LN_HI_PREC_COEF.length - 2;
                while (i2 >= 0) {
                    double aa6 = ya6 * xa3;
                    double tmp6 = aa6 * 1.073741824E9d;
                    double ya7 = (aa6 + tmp6) - tmp6;
                    double yb4 = (aa6 - ya7) + (ya6 * xb3) + (yb3 * xa3) + (yb3 * xb3);
                    double[] lnCoef_i2 = LN_HI_PREC_COEF[i2];
                    double aa7 = ya7 + lnCoef_i2[0];
                    double ab3 = yb4 + lnCoef_i2[1];
                    double tmp7 = aa7 * 1.073741824E9d;
                    double ya8 = (aa7 + tmp7) - tmp7;
                    yb3 = (aa7 - ya8) + ab3;
                    i2--;
                    ya6 = ya8;
                    double ya9 = ab3;
                }
                double aa8 = ya6 * xa3;
                double ab4 = (ya6 * xb3) + (yb3 * xa3) + (yb3 * xb3);
                lnza = aa8 + ab4;
                double d2 = denom2;
                denom = -((lnza - aa8) - ab4);
            } else {
                lnza = ((((((((((-0.16624882440418567d * epsilon) + 0.19999954120254515d) * epsilon) - 16.00000002972804d) * epsilon) + 0.3333333333332802d) * epsilon) - 8.0d) * epsilon) + 1.0d) * epsilon;
                denom = 0.0d;
            }
            double a = ((double) exp) * LN_2_A;
            double c = lnm[0] + a;
            double d3 = epsilon;
            double d4 = -((c - a) - lnm[0]);
            double a2 = c;
            double b = 0.0d + d4;
            double c2 = a2 + lnza;
            double d5 = d4;
            double d6 = -((c2 - a2) - lnza);
            double a3 = c2;
            double b2 = b + d6;
            double d7 = d6;
            double c3 = (((double) exp) * LN_2_B) + a3;
            long j = bits;
            double a4 = c3;
            double b3 = b2 + (-((c3 - a3) - (((double) exp) * LN_2_B)));
            double c4 = lnm[1] + a4;
            double d8 = -((c4 - a4) - lnm[1]);
            double a5 = c4;
            double c5 = a5 + denom;
            double d9 = d8;
            double a6 = c5;
            double b4 = b3 + d8 + (-((c5 - a5) - denom));
            if (hiPrec != null) {
                hiPrec[0] = a6;
                hiPrec[1] = b4;
            }
            return a6 + b4;
        }
    }

    public static double log1p(double x) {
        if (x == -1.0d) {
            return Double.NEGATIVE_INFINITY;
        }
        if (x == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }
        if (x <= 1.0E-6d && x >= -1.0E-6d) {
            return ((((F_1_3 * x) - F_1_2) * x) + 1.0d) * x;
        }
        double xpa = x + 1.0d;
        double xpb = -((xpa - 1.0d) - x);
        double[] hiPrec = new double[2];
        double lores = log(xpa, hiPrec);
        if (Double.isInfinite(lores)) {
            return lores;
        }
        double fx1 = xpb / xpa;
        return (((F_1_2 * fx1) + 1.0d) * fx1) + hiPrec[1] + hiPrec[0];
    }

    public static double log10(double x) {
        double[] hiPrec = new double[2];
        double lores = log(x, hiPrec);
        if (Double.isInfinite(lores)) {
            return lores;
        }
        double tmp = hiPrec[0] * 1.073741824E9d;
        double lna = (hiPrec[0] + tmp) - tmp;
        double lnb = (hiPrec[0] - lna) + hiPrec[1];
        return (lnb * 1.9699272335463627E-8d) + (1.9699272335463627E-8d * lna) + (lnb * 0.4342944622039795d) + (0.4342944622039795d * lna);
    }

    public static double log(double base, double x) {
        return log(x) / log(base);
    }

    public static double pow(double x, double y) {
        long integralMask;
        double d = x;
        if (y == 0.0d) {
            return 1.0d;
        }
        long yBits = Double.doubleToRawLongBits(y);
        int yRawExp = (int) ((yBits & MASK_DOUBLE_EXPONENT) >> 52);
        long yRawMantissa = yBits & 4503599627370495L;
        long xBits = Double.doubleToRawLongBits(x);
        int xRawExp = (int) ((xBits & MASK_DOUBLE_EXPONENT) >> 52);
        long xRawMantissa = xBits & 4503599627370495L;
        boolean z = true;
        if (yRawExp <= 1085) {
            if (yRawExp >= 1023) {
                long yFullMantissa = yRawMantissa | 4503599627370496L;
                if (yRawExp < 1075) {
                    long integralMask2 = -1 << (1075 - yRawExp);
                    if ((yFullMantissa & integralMask2) == yFullMantissa) {
                        long l = yFullMantissa >> (1075 - yRawExp);
                        if (y < 0.0d) {
                            long j = integralMask2;
                            integralMask = -l;
                        } else {
                            integralMask = l;
                        }
                        return pow(d, integralMask);
                    }
                } else {
                    long l2 = yFullMantissa << (yRawExp - 1075);
                    return pow(d, y < 0.0d ? -l2 : l2);
                }
            }
            if (d == 0.0d) {
                return y < 0.0d ? Double.POSITIVE_INFINITY : 0.0d;
            } else if (xRawExp == 2047) {
                if (xRawMantissa != 0) {
                    return Double.NaN;
                }
                double d2 = 0.0d;
                if (y >= 0.0d) {
                    d2 = Double.POSITIVE_INFINITY;
                }
                return d2;
            } else if (d < 0.0d) {
                return Double.NaN;
            } else {
                double tmp = y * 1.073741824E9d;
                double ya = (y + tmp) - tmp;
                double yb = y - ya;
                double[] lns = new double[2];
                double lores = log(d, lns);
                if (Double.isInfinite(lores)) {
                    return lores;
                }
                double lna = lns[0];
                double tmp1 = 1.073741824E9d * lna;
                double tmp2 = (lna + tmp1) - tmp1;
                double lnb = lns[1] + (lna - tmp2);
                double lna2 = tmp2;
                double aa = lna2 * ya;
                double ab = (lna2 * yb) + (lnb * ya) + (lnb * yb);
                double lna3 = aa + ab;
                double d3 = lores;
                double lores2 = -((lna3 - aa) - ab);
                double[] dArr = lns;
                double d4 = lores2;
                return exp(lna3, ((((((((0.008333333333333333d * lores2) + 0.041666666666666664d) * lores2) + 0.16666666666666666d) * lores2) + F_1_2) * lores2) + 1.0d) * lores2, null);
            }
        } else if ((yRawExp == 2047 && yRawMantissa != 0) || (xRawExp == 2047 && xRawMantissa != 0)) {
            return Double.NaN;
        } else {
            if (xRawExp != 1023 || xRawMantissa != 0) {
                boolean z2 = y > 0.0d;
                if (xRawExp >= 1023) {
                    z = false;
                }
                if (z2 ^ z) {
                    return Double.POSITIVE_INFINITY;
                }
                return 0.0d;
            } else if (yRawExp == 2047) {
                return Double.NaN;
            } else {
                return 1.0d;
            }
        }
    }

    public static double pow(double d, int e) {
        return pow(d, (long) e);
    }

    public static double pow(double d, long e) {
        if (e == 0) {
            return 1.0d;
        }
        if (e > 0) {
            return new Split(d).pow(e).full;
        }
        return new Split(d).reciprocal().pow(-e).full;
    }

    private static double polySine(double x) {
        double x2 = x * x;
        return ((((((2.7553817452272217E-6d * x2) - 22521.49865654966d) * x2) + 0.008333333333329196d) * x2) - 26.666666666666668d) * x2 * x;
    }

    private static double polyCosine(double x) {
        double x2 = x * x;
        return ((((((2.479773539153719E-5d * x2) - 3231.288930800263d) * x2) + 0.041666666666621166d) * x2) - 8.000000000000002d) * x2;
    }

    private static double sinQ(double xa, double xb) {
        int idx = (int) ((8.0d * xa) + F_1_2);
        double epsilon = xa - EIGHTHS[idx];
        double sintA = SINE_TABLE_A[idx];
        double sintB = SINE_TABLE_B[idx];
        double costA = COSINE_TABLE_A[idx];
        double costB = COSINE_TABLE_B[idx];
        double sinEpsA = epsilon;
        double sinEpsB = polySine(epsilon);
        double cosEpsB = polyCosine(epsilon);
        double temp = 1.073741824E9d * sinEpsA;
        double temp2 = (sinEpsA + temp) - temp;
        double sinEpsB2 = sinEpsB + (sinEpsA - temp2);
        double sinEpsA2 = temp2;
        double t = sintA;
        double c = 0.0d + t;
        double d = -((c - 0.0d) - t);
        double a = c;
        double b = 0.0d + d;
        double t2 = costA * sinEpsA2;
        double c2 = a + t2;
        double d2 = d;
        double d3 = -((c2 - a) - t2);
        double a2 = c2;
        double b2 = b + d3 + (sintA * cosEpsB) + (costA * sinEpsB2) + sintB + (costB * sinEpsA2) + (sintB * cosEpsB) + (costB * sinEpsB2);
        if (xb != 0.0d) {
            double t3 = (((costA + costB) * (cosEpsB + 1.0d)) - ((sintA + sintB) * (sinEpsA2 + sinEpsB2))) * xb;
            double c3 = a2 + t3;
            double d4 = d3;
            double d5 = -((c3 - a2) - t3);
            a2 = c3;
            b2 += d5;
            double d6 = d5;
        } else {
            double d7 = d3;
        }
        return a2 + b2;
    }

    private static double cosQ(double xa, double xb) {
        double a = 1.5707963267948966d - xa;
        return sinQ(a, (-((a - 1.5707963267948966d) + xa)) + (6.123233995736766E-17d - xb));
    }

    private static double tanQ(double xa, double xb, boolean cotanFlag) {
        int idx = (int) ((8.0d * xa) + F_1_2);
        double epsilon = xa - EIGHTHS[idx];
        double sintA = SINE_TABLE_A[idx];
        double sintB = SINE_TABLE_B[idx];
        double costA = COSINE_TABLE_A[idx];
        double costB = COSINE_TABLE_B[idx];
        double sinEpsA = epsilon;
        double sinEpsB = polySine(epsilon);
        double cosEpsB = polyCosine(epsilon);
        double temp = sinEpsA * 1.073741824E9d;
        double temp2 = (sinEpsA + temp) - temp;
        double sinEpsB2 = sinEpsB + (sinEpsA - temp2);
        double sinEpsA2 = temp2;
        double t = sintA;
        double c = 0.0d + t;
        double d = -((c - 0.0d) - t);
        double a = c;
        double b = 0.0d + d;
        double t2 = costA * sinEpsA2;
        double c2 = a + t2;
        double d2 = d;
        double d3 = -((c2 - a) - t2);
        double a2 = c2;
        double b2 = b + d3 + (sintA * cosEpsB) + (costA * sinEpsB2) + sintB + (costB * sinEpsA2) + (sintB * cosEpsB) + (costB * sinEpsB2);
        double sina = a2 + b2;
        double d4 = d3;
        double sinb = -((sina - a2) - b2);
        double t3 = costA * 1.0d;
        double c3 = 0.0d + t3;
        int i = idx;
        double d5 = epsilon;
        double d6 = -((c3 - 0.0d) - t3);
        double a3 = c3;
        double b3 = 0.0d + d6;
        double d7 = d6;
        double t4 = (-sintA) * sinEpsA2;
        double c4 = a3 + t4;
        double d8 = a3;
        double a4 = c4;
        double b4 = ((b3 + (-((c4 - a3) - t4))) + (((1.0d * costB) + (costA * cosEpsB)) + (costB * cosEpsB))) - (((sintB * sinEpsA2) + (sintA * sinEpsB2)) + (sintB * sinEpsB2));
        double cosa = a4 + b4;
        double d9 = t4;
        double cosb = -((cosa - a4) - b4);
        if (cotanFlag) {
            double tmp = cosa;
            cosa = sina;
            sina = tmp;
            double tmp2 = cosb;
            cosb = sinb;
            sinb = tmp2;
        }
        double d10 = sinb;
        double sina2 = sina;
        double cosb2 = cosb;
        double sinb2 = d10;
        double est = sina2 / cosa;
        double temp3 = est * 1.073741824E9d;
        double esta = (est + temp3) - temp3;
        double estb = est - esta;
        double temp4 = 1.073741824E9d * cosa;
        double cosaa = (cosa + temp4) - temp4;
        double cosab = cosa - cosaa;
        double d11 = sinb2;
        double err = (((((sina2 - (esta * cosaa)) - (esta * cosab)) - (estb * cosaa)) - (estb * cosab)) / cosa) + (sinb2 / cosa) + ((((-sina2) * cosb2) / cosa) / cosa);
        if (xb != 0.0d) {
            double xbadj = xb + (est * est * xb);
            if (cotanFlag) {
                xbadj = -xbadj;
            }
            err += xbadj;
        }
        return est + err;
    }

    private static void reducePayneHanek(double x, double[] result) {
        long shpiB;
        long shpiA;
        long shpi0;
        long inbits = Double.doubleToRawLongBits(x);
        int exponent = (((int) ((inbits >> 52) & 2047)) - 1023) + 1;
        long inbits2 = ((inbits & 4503599627370495L) | 4503599627370496L) << 11;
        int idx = exponent >> 6;
        int shift = exponent - (idx << 6);
        if (shift != 0) {
            shpi0 = (idx == 0 ? 0 : RECIP_2PI[idx - 1] << shift) | (RECIP_2PI[idx] >>> (64 - shift));
            shpiA = (RECIP_2PI[idx] << shift) | (RECIP_2PI[idx + 1] >>> (64 - shift));
            shpiB = (RECIP_2PI[idx + 1] << shift) | (RECIP_2PI[idx + 2] >>> (64 - shift));
        } else {
            shpi0 = idx == 0 ? 0 : RECIP_2PI[idx - 1];
            shpiA = RECIP_2PI[idx];
            shpiB = RECIP_2PI[idx + 1];
        }
        long a = inbits2 >>> 32;
        long b = inbits2 & 4294967295L;
        long c = shpiA >>> 32;
        long d = shpiA & 4294967295L;
        long bd = b * d;
        long bc = b * c;
        long ad = a * d;
        long prodB = bd + (ad << 32);
        long prodA = (a * c) + (ad >>> 32);
        boolean bita = (bd & Long.MIN_VALUE) != 0;
        boolean bitb = (ad & 2147483648L) != 0;
        boolean bitsum = (prodB & Long.MIN_VALUE) != 0;
        if ((bita && bitb) || ((bita || bitb) && !bitsum)) {
            prodA++;
        }
        boolean bita2 = (prodB & Long.MIN_VALUE) != 0;
        boolean bitb2 = (bc & 2147483648L) != 0;
        long prodB2 = prodB + (bc << 32);
        long prodA2 = prodA + (bc >>> 32);
        boolean bitsum2 = (prodB2 & Long.MIN_VALUE) != 0;
        if ((bita2 && bitb2) || ((bita2 || bitb2) && !bitsum2)) {
            prodA2++;
        }
        long c2 = shpiB >>> 32;
        long ac = (a * c2) + (((b * c2) + (a * (shpiB & 4294967295L))) >>> 32);
        boolean bita3 = (prodB2 & Long.MIN_VALUE) != 0;
        boolean bitb3 = (ac & Long.MIN_VALUE) != 0;
        long prodB3 = prodB2 + ac;
        boolean bitsum3 = (prodB3 & Long.MIN_VALUE) != 0;
        if ((bita3 && bitb3) || ((bita3 || bitb3) && !bitsum3)) {
            prodA2++;
        }
        long d2 = shpi0 & 4294967295L;
        long prodA3 = prodA2 + (b * d2) + (((b * (shpi0 >>> 32)) + (a * d2)) << 32);
        int i = idx;
        int intPart = (int) (prodA3 >>> 62);
        long prodA4 = (prodA3 << 2) | (prodB3 >>> 62);
        long prodB4 = prodB3 << 2;
        long a2 = prodA4 >>> 32;
        long b2 = prodA4 & 4294967295L;
        long c3 = PI_O_4_BITS[0] >>> 32;
        long d3 = PI_O_4_BITS[0] & 4294967295L;
        long bd2 = b2 * d3;
        long bc2 = b2 * c3;
        long ad2 = a2 * d3;
        long prod2B = bd2 + (ad2 << 32);
        long prod2A = (a2 * c3) + (ad2 >>> 32);
        boolean bita4 = (bd2 & Long.MIN_VALUE) != 0;
        boolean bitb4 = (ad2 & 2147483648L) != 0;
        boolean bitsum4 = (prod2B & Long.MIN_VALUE) != 0;
        if ((bita4 && bitb4) || ((bita4 || bitb4) && !bitsum4)) {
            prod2A++;
        }
        boolean bita5 = (prod2B & Long.MIN_VALUE) != 0;
        boolean bitb5 = (bc2 & 2147483648L) != 0;
        long prod2B2 = prod2B + (bc2 << 32);
        long prod2A2 = prod2A + (bc2 >>> 32);
        boolean bitsum5 = (prod2B2 & Long.MIN_VALUE) != 0;
        if ((bita5 && bitb5) || ((bita5 || bitb5) && !bitsum5)) {
            prod2A2++;
        }
        long c4 = PI_O_4_BITS[1] >>> 32;
        long ac2 = (a2 * c4) + (((b2 * c4) + (a2 * (PI_O_4_BITS[1] & 4294967295L))) >>> 32);
        boolean bita6 = (prod2B2 & Long.MIN_VALUE) != 0;
        boolean bitb6 = (ac2 & Long.MIN_VALUE) != 0;
        long prod2B3 = prod2B2 + ac2;
        boolean bitsum6 = (prod2B3 & Long.MIN_VALUE) != 0;
        if ((bita6 && bitb6) || ((bita6 || bitb6) && !bitsum6)) {
            prod2A2++;
        }
        long a3 = prodB4 >>> 32;
        long c5 = PI_O_4_BITS[0] >>> 32;
        long ac3 = (a3 * c5) + ((((prodB4 & 4294967295L) * c5) + (a3 * (PI_O_4_BITS[0] & 4294967295L))) >>> 32);
        boolean bita7 = (prod2B3 & Long.MIN_VALUE) != 0;
        boolean bitb7 = (ac3 & Long.MIN_VALUE) != 0;
        long prod2B4 = prod2B3 + ac3;
        boolean bitsum7 = (prod2B4 & Long.MIN_VALUE) != 0;
        if ((bita7 && bitb7) || ((bita7 || bitb7) && !bitsum7)) {
            prod2A2++;
        }
        int i2 = shift;
        double tmpA = ((double) (prod2A2 >>> 12)) / TWO_POWER_52;
        long j = inbits2;
        double tmpB = (((double) (((prod2A2 & 4095) << 40) + (prod2B4 >>> 24))) / TWO_POWER_52) / TWO_POWER_52;
        double sumA = tmpA + tmpB;
        double d4 = tmpA;
        double sumB = -((sumA - tmpA) - tmpB);
        double d5 = tmpB;
        result[0] = (double) intPart;
        result[1] = sumA * 2.0d;
        result[2] = 2.0d * sumB;
    }

    public static double sin(double x) {
        boolean negative = false;
        int quadrant = 0;
        double xb = 0.0d;
        double xa = x;
        if (x < 0.0d) {
            negative = true;
            xa = -xa;
        }
        if (xa == 0.0d) {
            if (Double.doubleToRawLongBits(x) < 0) {
                return -0.0d;
            }
            return 0.0d;
        } else if (xa != xa || xa == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        } else {
            if (xa > 3294198.0d) {
                double[] reduceResults = new double[3];
                reducePayneHanek(xa, reduceResults);
                quadrant = ((int) reduceResults[0]) & 3;
                xa = reduceResults[1];
                xb = reduceResults[2];
            } else if (xa > 1.5707963267948966d) {
                CodyWaite cw = new CodyWaite(xa);
                quadrant = cw.getK() & 3;
                xa = cw.getRemA();
                xb = cw.getRemB();
            }
            if (negative) {
                quadrant ^= 2;
            }
            switch (quadrant) {
                case 0:
                    return sinQ(xa, xb);
                case 1:
                    return cosQ(xa, xb);
                case 2:
                    return -sinQ(xa, xb);
                case 3:
                    return -cosQ(xa, xb);
                default:
                    return Double.NaN;
            }
        }
    }

    public static double cos(double x) {
        int quadrant = 0;
        double xa = x;
        if (x < 0.0d) {
            xa = -xa;
        }
        if (xa != xa || xa == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }
        double xb = 0.0d;
        if (xa > 3294198.0d) {
            double[] reduceResults = new double[3];
            reducePayneHanek(xa, reduceResults);
            quadrant = ((int) reduceResults[0]) & 3;
            xa = reduceResults[1];
            xb = reduceResults[2];
        } else if (xa > 1.5707963267948966d) {
            CodyWaite cw = new CodyWaite(xa);
            quadrant = cw.getK() & 3;
            xa = cw.getRemA();
            xb = cw.getRemB();
        }
        switch (quadrant) {
            case 0:
                return cosQ(xa, xb);
            case 1:
                return -sinQ(xa, xb);
            case 2:
                return -cosQ(xa, xb);
            case 3:
                return sinQ(xa, xb);
            default:
                return Double.NaN;
        }
    }

    public static double tan(double x) {
        double result;
        boolean negative = false;
        int quadrant = 0;
        double xa = x;
        if (x < 0.0d) {
            negative = true;
            xa = -xa;
        }
        if (xa == 0.0d) {
            if (Double.doubleToRawLongBits(x) < 0) {
                return -0.0d;
            }
            return 0.0d;
        } else if (xa != xa || xa == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        } else {
            double xb = 0.0d;
            if (xa > 3294198.0d) {
                double[] reduceResults = new double[3];
                reducePayneHanek(xa, reduceResults);
                quadrant = ((int) reduceResults[0]) & 3;
                xa = reduceResults[1];
                xb = reduceResults[2];
            } else if (xa > 1.5707963267948966d) {
                CodyWaite cw = new CodyWaite(xa);
                quadrant = cw.getK() & 3;
                xa = cw.getRemA();
                xb = cw.getRemB();
            }
            if (xa > 1.5d) {
                double a = 1.5707963267948966d - xa;
                double b = (-((a - 1.5707963267948966d) + xa)) + (6.123233995736766E-17d - xb);
                xa = a + b;
                xb = -((xa - a) - b);
                quadrant ^= 1;
                negative = !negative;
            }
            if ((quadrant & 1) == 0) {
                result = tanQ(xa, xb, false);
            } else {
                result = -tanQ(xa, xb, true);
            }
            if (negative) {
                result = -result;
            }
            return result;
        }
    }

    public static double atan(double x) {
        return atan(x, 0.0d, false);
    }

    private static double atan(double xa, double xb, boolean leftPlane) {
        boolean negate;
        double xb2;
        int idx;
        int idx2;
        double ya;
        double denom;
        double xa2 = xa;
        if (xa2 == 0.0d) {
            return leftPlane ? copySign(3.141592653589793d, xa2) : xa2;
        }
        if (xa2 < 0.0d) {
            xa2 = -xa2;
            xb2 = -xb;
            negate = true;
        } else {
            xb2 = xb;
            negate = false;
        }
        if (xa2 > 1.633123935319537E16d) {
            return negate ^ leftPlane ? -1.5707963267948966d : 1.5707963267948966d;
        }
        if (xa2 < 1.0d) {
            idx = (int) ((((-1.7168146928204135d * xa2 * xa2) + 8.0d) * xa2) + F_1_2);
        } else {
            double oneOverXa = 1.0d / xa2;
            idx = (int) ((-(((-1.7168146928204135d * oneOverXa * oneOverXa) + 8.0d) * oneOverXa)) + 13.07d);
        }
        double ttA = TANGENT_TABLE_A[idx];
        double ttB = TANGENT_TABLE_B[idx];
        double epsA = xa2 - ttA;
        double epsB = (-((epsA - xa2) + ttA)) + (xb2 - ttB);
        double temp = epsA + epsB;
        double epsB2 = -((temp - epsA) - epsB);
        double epsA2 = temp;
        double temp2 = xa2 * 1.073741824E9d;
        double ya2 = (xa2 + temp2) - temp2;
        double xa3 = ya2;
        double xb3 = xb2 + ((xb2 + xa2) - ya2);
        if (idx == 0) {
            double denom2 = 1.0d / (((xa3 + xb3) * (ttA + ttB)) + 1.0d);
            ya = epsA2 * denom2;
            denom = denom2 * epsB2;
            double d = xa3;
            double d2 = xb3;
            idx2 = idx;
        } else {
            double temp22 = xa3 * ttA;
            double za = temp22 + 1.0d;
            idx2 = idx;
            double temp23 = (xb3 * ttA) + (xa3 * ttB);
            double temp3 = za + temp23;
            double d3 = xa3;
            double za2 = temp3;
            ya = epsA2 / za2;
            double temp4 = ya * 1.073741824E9d;
            double yaa = (ya + temp4) - temp4;
            double yab = ya - yaa;
            double temp5 = za2 * 1.073741824E9d;
            double zaa = (za2 + temp5) - temp5;
            double zab = za2 - zaa;
            double d4 = xb3;
            denom = (((((epsA2 - (yaa * zaa)) - (yaa * zab)) - (yab * zaa)) - (yab * zab)) / za2) + ((((-epsA2) * (((-((za - 1.0d) - temp22)) + (-((temp3 - za) - temp23))) + (xb3 * ttB))) / za2) / za2) + (epsB2 / za2);
        }
        double za3 = ya;
        double epsB3 = denom;
        double epsB4 = za3 * za3;
        double yb = ((((((((((0.07490822288864472d * epsB4) - 0.09088450866185192d) * epsB4) + 0.11111095942313305d) * epsB4) - 0.1428571423679182d) * epsB4) + 0.19999999999923582d) * epsB4) - 0.33333333333333287d) * epsB4 * za3;
        double yb2 = za3;
        double temp6 = yb2 + yb;
        double d5 = epsB4;
        double yb3 = temp6;
        double yb4 = (-((temp6 - yb2) - yb)) + (epsB3 / ((za3 * za3) + 1.0d));
        double eighths = EIGHTHS[idx2];
        double za4 = eighths + yb3;
        double d6 = za3;
        double temp7 = za4 + yb4;
        double d7 = epsB3;
        double zb = (-((za4 - eighths) - yb3)) + (-((temp7 - za4) - yb4));
        double za5 = temp7;
        double result = za5 + zb;
        if (leftPlane) {
            double d8 = za5;
            double za6 = 3.141592653589793d - result;
            double d9 = zb;
            result = za6 + (-((za6 - 3.141592653589793d) + result)) + (1.2246467991473532E-16d - (-((result - za5) - zb)));
        } else {
            double d10 = zb;
            double d11 = za5;
        }
        if (negate ^ leftPlane) {
            result = -result;
        }
        return result;
    }

    public static double atan2(double y, double x) {
        double d = y;
        if (x != x || d != d) {
            return Double.NaN;
        }
        if (d == 0.0d) {
            double result = x * d;
            double invx = 1.0d / x;
            double invy = 1.0d / d;
            if (invx == 0.0d) {
                if (x > 0.0d) {
                    return d;
                }
                return copySign(3.141592653589793d, d);
            } else if (x < 0.0d || invx < 0.0d) {
                return (d < 0.0d || invy < 0.0d) ? -3.141592653589793d : 3.141592653589793d;
            } else {
                return result;
            }
        } else if (d == Double.POSITIVE_INFINITY) {
            if (x == Double.POSITIVE_INFINITY) {
                return 0.7853981633974483d;
            }
            if (x == Double.NEGATIVE_INFINITY) {
                return 2.356194490192345d;
            }
            return 1.5707963267948966d;
        } else if (d != Double.NEGATIVE_INFINITY) {
            if (x == Double.POSITIVE_INFINITY) {
                if (d > 0.0d || 1.0d / d > 0.0d) {
                    return 0.0d;
                }
                if (d < 0.0d || 1.0d / d < 0.0d) {
                    return -0.0d;
                }
            }
            if (x == Double.NEGATIVE_INFINITY) {
                if (d > 0.0d || 1.0d / d > 0.0d) {
                    return 3.141592653589793d;
                }
                if (d < 0.0d || 1.0d / d < 0.0d) {
                    return -3.141592653589793d;
                }
            }
            if (x == 0.0d) {
                if (d > 0.0d || 1.0d / d > 0.0d) {
                    return 1.5707963267948966d;
                }
                if (d < 0.0d || 1.0d / d < 0.0d) {
                    return -1.5707963267948966d;
                }
            }
            double r = d / x;
            if (Double.isInfinite(r)) {
                return atan(r, 0.0d, x < 0.0d);
            }
            double ra = doubleHighPart(r);
            double rb = r - ra;
            double xa = doubleHighPart(x);
            double xb = x - xa;
            double rb2 = rb + (((((d - (ra * xa)) - (ra * xb)) - (rb * xa)) - (rb * xb)) / x);
            double temp = ra + rb2;
            double rb3 = -((temp - ra) - rb2);
            double ra2 = temp;
            if (ra2 == 0.0d) {
                ra2 = copySign(0.0d, d);
            }
            return atan(ra2, rb3, x < 0.0d);
        } else if (x == Double.POSITIVE_INFINITY) {
            return -0.7853981633974483d;
        } else {
            if (x == Double.NEGATIVE_INFINITY) {
                return -2.356194490192345d;
            }
            return -1.5707963267948966d;
        }
    }

    public static double asin(double x) {
        double d = x;
        if (d != d || d > 1.0d || d < -1.0d) {
            return Double.NaN;
        }
        if (d == 1.0d) {
            return 1.5707963267948966d;
        }
        if (d == -1.0d) {
            return -1.5707963267948966d;
        }
        if (d == 0.0d) {
            return d;
        }
        double temp = d * 1.073741824E9d;
        double xa = (d + temp) - temp;
        double xb = d - xa;
        double ya = -(xa * xa);
        double yb = -((xa * xb * 2.0d) + (xb * xb));
        double za = ya + 1.0d;
        double temp2 = za + yb;
        double d2 = xa;
        double zb = (-((za - 1.0d) - ya)) + (-((temp2 - za) - yb));
        double za2 = temp2;
        double za3 = sqrt(za2);
        double temp3 = za3 * 1.073741824E9d;
        double ya2 = (za3 + temp3) - temp3;
        double yb2 = za3 - ya2;
        double yb3 = yb2 + ((((za2 - (ya2 * ya2)) - ((ya2 * 2.0d) * yb2)) - (yb2 * yb2)) / (za3 * 2.0d));
        double r = d / za3;
        double temp4 = r * 1.073741824E9d;
        double ra = (r + temp4) - temp4;
        double rb = r - ra;
        double d3 = yb3;
        double rb2 = rb + (((((d - (ra * ya2)) - (ra * yb3)) - (rb * ya2)) - (rb * yb3)) / za3) + ((((-d) * (zb / (2.0d * za3))) / za3) / za3);
        double temp5 = ra + rb2;
        return atan(temp5, -((temp5 - ra) - rb2), false);
    }

    public static double acos(double x) {
        if (x != x || x > 1.0d || x < -1.0d) {
            return Double.NaN;
        }
        if (x == -1.0d) {
            return 3.141592653589793d;
        }
        if (x == 1.0d) {
            return 0.0d;
        }
        if (x == 0.0d) {
            return 1.5707963267948966d;
        }
        double temp = x * 1.073741824E9d;
        double xa = (x + temp) - temp;
        double xb = x - xa;
        double ya = -(xa * xa);
        double yb = -((xa * xb * 2.0d) + (xb * xb));
        double za = ya + 1.0d;
        double temp2 = za + yb;
        double zb = (-((za - 1.0d) - ya)) + (-((temp2 - za) - yb));
        double za2 = temp2;
        double za3 = sqrt(za2);
        double temp3 = za3 * 1.073741824E9d;
        double ya2 = (za3 + temp3) - temp3;
        double yb2 = za3 - ya2;
        double yb3 = yb2 + ((((za2 - (ya2 * ya2)) - ((ya2 * 2.0d) * yb2)) - (yb2 * yb2)) / (za3 * 2.0d)) + (zb / (2.0d * za3));
        double y = ya2 + yb3;
        double d = zb;
        double yb4 = -((y - ya2) - yb3);
        double r = y / x;
        if (Double.isInfinite(r)) {
            return 1.5707963267948966d;
        }
        double ra = doubleHighPart(r);
        double rb = r - ra;
        double rb2 = rb + (((((y - (ra * xa)) - (ra * xb)) - (rb * xa)) - (rb * xb)) / x) + (yb4 / x);
        double temp4 = ra + rb2;
        double d2 = yb4;
        return atan(temp4, -((temp4 - ra) - rb2), x < 0.0d);
    }

    public static double cbrt(double x) {
        double x2;
        long inbits = Double.doubleToRawLongBits(x);
        int exponent = ((int) ((inbits >> 52) & 2047)) - 1023;
        boolean subnormal = false;
        if (exponent != -1023) {
            x2 = x;
        } else if (x == 0.0d) {
            return x;
        } else {
            subnormal = true;
            x2 = x * 1.8014398509481984E16d;
            inbits = Double.doubleToRawLongBits(x2);
            exponent = ((int) (2047 & (inbits >> 52))) - 1023;
        }
        if (exponent == 1024) {
            return x2;
        }
        double p2 = Double.longBitsToDouble((Long.MIN_VALUE & inbits) | (((long) (((exponent / 3) + IEEEDouble.EXPONENT_BIAS) & IEEEDouble.BIASED_EXPONENT_SPECIAL_VALUE)) << 52));
        double mant = Double.longBitsToDouble((4503599627370495L & inbits) | 4607182418800017408L);
        double est = ((((((((-0.010714690733195933d * mant) + 0.0875862700108075d) * mant) - 14.214349574856733d) * mant) + 0.7249995199969751d) * mant) + 0.5039018405998233d) * CBRTTWO[(exponent % 3) + 2];
        double xs = x2 / ((p2 * p2) * p2);
        double est2 = est + ((xs - ((est * est) * est)) / ((est * 3.0d) * est));
        double est3 = est2 + ((xs - ((est2 * est2) * est2)) / ((est2 * 3.0d) * est2));
        double temp = est3 * 1.073741824E9d;
        double ya = (est3 + temp) - temp;
        double yb = est3 - ya;
        double za = ya * ya;
        double temp2 = 1.073741824E9d * za;
        double temp22 = (za + temp2) - temp2;
        double zb = (ya * yb * 2.0d) + (yb * yb) + (za - temp22);
        double za2 = temp22;
        double zb2 = (za2 * yb) + (ya * zb) + (zb * yb);
        double za3 = za2 * ya;
        double na = xs - za3;
        double d = x2;
        double est4 = (est3 + ((na + ((-((na - xs) + za3)) - zb2)) / ((3.0d * est3) * est3))) * p2;
        if (subnormal) {
            est4 *= 3.814697265625E-6d;
        }
        return est4;
    }

    public static double toRadians(double x) {
        if (Double.isInfinite(x) || x == 0.0d) {
            return x;
        }
        double xa = doubleHighPart(x);
        double xb = x - xa;
        double result = (xb * 1.997844754509471E-9d) + (xb * 0.01745329052209854d) + (1.997844754509471E-9d * xa) + (0.01745329052209854d * xa);
        if (result == 0.0d) {
            result *= x;
        }
        return result;
    }

    public static double toDegrees(double x) {
        if (Double.isInfinite(x) || x == 0.0d) {
            return x;
        }
        double xa = doubleHighPart(x);
        double xb = x - xa;
        return (xb * 3.145894820876798E-6d) + (xb * 57.2957763671875d) + (3.145894820876798E-6d * xa) + (57.2957763671875d * xa);
    }

    public static int abs(int x) {
        int i = x >>> 31;
        return (((~i) + 1) ^ x) + i;
    }

    public static long abs(long x) {
        long l = x >>> 63;
        return (((~l) + 1) ^ x) + l;
    }

    public static float abs(float x) {
        return Float.intBitsToFloat(Float.floatToRawIntBits(x) & Integer.MAX_VALUE);
    }

    public static double abs(double x) {
        return Double.longBitsToDouble(Double.doubleToRawLongBits(x) & MASK_NON_SIGN_LONG);
    }

    public static double ulp(double x) {
        if (Double.isInfinite(x)) {
            return Double.POSITIVE_INFINITY;
        }
        return abs(x - Double.longBitsToDouble(Double.doubleToRawLongBits(x) ^ 1));
    }

    public static float ulp(float x) {
        if (Float.isInfinite(x)) {
            return Float.POSITIVE_INFINITY;
        }
        return abs(x - Float.intBitsToFloat(Float.floatToIntBits(x) ^ 1));
    }

    public static double scalb(double d, int n) {
        int i = n;
        if (i > -1023 && i < 1024) {
            return Double.longBitsToDouble(((long) (i + IEEEDouble.EXPONENT_BIAS)) << 52) * d;
        }
        if (!Double.isNaN(d) && !Double.isInfinite(d)) {
            double d2 = 0.0d;
            if (d != 0.0d) {
                if (i < -2098) {
                    if (d <= 0.0d) {
                        d2 = -0.0d;
                    }
                    return d2;
                }
                double d3 = Double.POSITIVE_INFINITY;
                if (i > 2097) {
                    if (d <= 0.0d) {
                        d3 = Double.NEGATIVE_INFINITY;
                    }
                    return d3;
                }
                long bits = Double.doubleToRawLongBits(d);
                long sign = Long.MIN_VALUE & bits;
                int exponent = ((int) (bits >>> 52)) & IEEEDouble.BIASED_EXPONENT_SPECIAL_VALUE;
                long mantissa = bits & 4503599627370495L;
                int scaledExponent = exponent + i;
                if (i < 0) {
                    if (scaledExponent > 0) {
                        return Double.longBitsToDouble((((long) scaledExponent) << 52) | sign | mantissa);
                    }
                    if (scaledExponent > -53) {
                        long mantissa2 = mantissa | 4503599627370496L;
                        long mostSignificantLostBit = (1 << (-scaledExponent)) & mantissa2;
                        long mantissa3 = mantissa2 >>> (1 - scaledExponent);
                        if (mostSignificantLostBit != 0) {
                            mantissa3++;
                        }
                        return Double.longBitsToDouble(sign | mantissa3);
                    }
                    return sign == 0 ? 0.0d : -0.0d;
                } else if (exponent == 0) {
                    while ((mantissa >>> 52) != 1) {
                        mantissa <<= 1;
                        scaledExponent--;
                    }
                    int scaledExponent2 = scaledExponent + 1;
                    long mantissa4 = mantissa & 4503599627370495L;
                    if (scaledExponent2 < 2047) {
                        return Double.longBitsToDouble((((long) scaledExponent2) << 52) | sign | mantissa4);
                    }
                    if (sign != 0) {
                        d3 = Double.NEGATIVE_INFINITY;
                    }
                    return d3;
                } else if (scaledExponent < 2047) {
                    return Double.longBitsToDouble((((long) scaledExponent) << 52) | sign | mantissa);
                } else {
                    if (sign != 0) {
                        d3 = Double.NEGATIVE_INFINITY;
                    }
                    return d3;
                }
            }
        }
        return d;
    }

    public static float scalb(float f, int n) {
        if (n > -127 && n < 128) {
            return Float.intBitsToFloat((n + ShapeTypes.VERTICAL_SCROLL) << 23) * f;
        }
        if (!Float.isNaN(f) && !Float.isInfinite(f)) {
            float f2 = 0.0f;
            if (f != 0.0f) {
                if (n < -277) {
                    if (f <= 0.0f) {
                        f2 = -0.0f;
                    }
                    return f2;
                }
                float f3 = Float.NEGATIVE_INFINITY;
                if (n > 276) {
                    if (f > 0.0f) {
                        f3 = Float.POSITIVE_INFINITY;
                    }
                    return f3;
                }
                int bits = Float.floatToIntBits(f);
                int sign = Integer.MIN_VALUE & bits;
                int exponent = (bits >>> 23) & 255;
                int mantissa = bits & 8388607;
                int scaledExponent = exponent + n;
                if (n < 0) {
                    if (scaledExponent > 0) {
                        return Float.intBitsToFloat((scaledExponent << 23) | sign | mantissa);
                    }
                    if (scaledExponent > -24) {
                        int mantissa2 = 8388608 | mantissa;
                        int mostSignificantLostBit = (1 << (-scaledExponent)) & mantissa2;
                        int mantissa3 = mantissa2 >>> (1 - scaledExponent);
                        if (mostSignificantLostBit != 0) {
                            mantissa3++;
                        }
                        return Float.intBitsToFloat(sign | mantissa3);
                    }
                    if (sign != 0) {
                        f2 = -0.0f;
                    }
                    return f2;
                } else if (exponent == 0) {
                    while ((mantissa >>> 23) != 1) {
                        mantissa <<= 1;
                        scaledExponent--;
                    }
                    int scaledExponent2 = scaledExponent + 1;
                    int mantissa4 = mantissa & 8388607;
                    if (scaledExponent2 < 255) {
                        return Float.intBitsToFloat((scaledExponent2 << 23) | sign | mantissa4);
                    }
                    if (sign == 0) {
                        f3 = Float.POSITIVE_INFINITY;
                    }
                    return f3;
                } else if (scaledExponent < 255) {
                    return Float.intBitsToFloat((scaledExponent << 23) | sign | mantissa);
                } else {
                    if (sign == 0) {
                        f3 = Float.POSITIVE_INFINITY;
                    }
                    return f3;
                }
            }
        }
        return f;
    }

    public static double nextAfter(double d, double direction) {
        if (Double.isNaN(d) || Double.isNaN(direction)) {
            return Double.NaN;
        }
        if (d == direction) {
            return direction;
        }
        if (Double.isInfinite(d)) {
            return d < 0.0d ? -1.7976931348623157E308d : Double.MAX_VALUE;
        } else if (d == 0.0d) {
            return direction < 0.0d ? -4.9E-324d : Double.MIN_VALUE;
        } else {
            long bits = Double.doubleToRawLongBits(d);
            long sign = Long.MIN_VALUE & bits;
            boolean z = false;
            boolean z2 = direction < d;
            if (sign == 0) {
                z = true;
            }
            if (z2 ^ z) {
                return Double.longBitsToDouble(sign | ((MASK_NON_SIGN_LONG & bits) + 1));
            }
            return Double.longBitsToDouble(sign | ((MASK_NON_SIGN_LONG & bits) - 1));
        }
    }

    public static float nextAfter(float f, double direction) {
        if (Double.isNaN((double) f) || Double.isNaN(direction)) {
            return Float.NaN;
        }
        if (((double) f) == direction) {
            return (float) direction;
        }
        if (Float.isInfinite(f)) {
            return f < 0.0f ? -3.4028235E38f : Float.MAX_VALUE;
        } else if (f == 0.0f) {
            return direction < 0.0d ? -1.4E-45f : Float.MIN_VALUE;
        } else {
            int bits = Float.floatToIntBits(f);
            int sign = Integer.MIN_VALUE & bits;
            boolean z = false;
            boolean z2 = direction < ((double) f);
            if (sign == 0) {
                z = true;
            }
            if (z2 ^ z) {
                return Float.intBitsToFloat(((bits & Integer.MAX_VALUE) + 1) | sign);
            }
            return Float.intBitsToFloat(((bits & Integer.MAX_VALUE) - 1) | sign);
        }
    }

    public static double floor(double x) {
        if (x != x || x >= TWO_POWER_52 || x <= -4.503599627370496E15d) {
            return x;
        }
        long y = (long) x;
        if (x < 0.0d && ((double) y) != x) {
            y--;
        }
        if (y == 0) {
            return ((double) y) * x;
        }
        return (double) y;
    }

    public static double ceil(double x) {
        if (x != x) {
            return x;
        }
        double y = floor(x);
        if (y == x) {
            return y;
        }
        double y2 = y + 1.0d;
        if (y2 == 0.0d) {
            return x * y2;
        }
        return y2;
    }

    public static double rint(double x) {
        double y = floor(x);
        double d = x - y;
        if (d > F_1_2) {
            if (y == -1.0d) {
                return -0.0d;
            }
            return 1.0d + y;
        } else if (d < F_1_2) {
            return y;
        } else {
            return (1 & ((long) y)) == 0 ? y : y + 1.0d;
        }
    }

    public static long round(double x) {
        return (long) floor(F_1_2 + x);
    }

    public static int round(float x) {
        return (int) floor((double) (0.5f + x));
    }

    public static int min(int a, int b) {
        return a <= b ? a : b;
    }

    public static long min(long a, long b) {
        return a <= b ? a : b;
    }

    public static float min(float a, float b) {
        if (a > b) {
            return b;
        }
        if (a < b) {
            return a;
        }
        if (a != b) {
            return Float.NaN;
        }
        if (Float.floatToRawIntBits(a) == Integer.MIN_VALUE) {
            return a;
        }
        return b;
    }

    public static double min(double a, double b) {
        if (a > b) {
            return b;
        }
        if (a < b) {
            return a;
        }
        if (a != b) {
            return Double.NaN;
        }
        if (Double.doubleToRawLongBits(a) == Long.MIN_VALUE) {
            return a;
        }
        return b;
    }

    public static int max(int a, int b) {
        return a <= b ? b : a;
    }

    public static long max(long a, long b) {
        return a <= b ? b : a;
    }

    public static float max(float a, float b) {
        if (a > b) {
            return a;
        }
        if (a < b) {
            return b;
        }
        if (a != b) {
            return Float.NaN;
        }
        if (Float.floatToRawIntBits(a) == Integer.MIN_VALUE) {
            return b;
        }
        return a;
    }

    public static double max(double a, double b) {
        if (a > b) {
            return a;
        }
        if (a < b) {
            return b;
        }
        if (a != b) {
            return Double.NaN;
        }
        if (Double.doubleToRawLongBits(a) == Long.MIN_VALUE) {
            return b;
        }
        return a;
    }

    public static double hypot(double x, double y) {
        if (Double.isInfinite(x) || Double.isInfinite(y)) {
            return Double.POSITIVE_INFINITY;
        }
        if (Double.isNaN(x) || Double.isNaN(y)) {
            return Double.NaN;
        }
        int expX = getExponent(x);
        int expY = getExponent(y);
        if (expX > expY + 27) {
            return abs(x);
        }
        if (expY > expX + 27) {
            return abs(y);
        }
        int middleExp = (expX + expY) / 2;
        double scaledX = scalb(x, -middleExp);
        double scaledY = scalb(y, -middleExp);
        return scalb(sqrt((scaledX * scaledX) + (scaledY * scaledY)), middleExp);
    }

    public static double IEEEremainder(double dividend, double divisor) {
        return StrictMath.IEEEremainder(dividend, divisor);
    }

    public static int toIntExact(long n) throws MathArithmeticException {
        if (n >= -2147483648L && n <= 2147483647L) {
            return (int) n;
        }
        throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
    }

    public static int incrementExact(int n) throws MathArithmeticException {
        if (n != Integer.MAX_VALUE) {
            return n + 1;
        }
        throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_ADDITION, Integer.valueOf(n), Integer.valueOf(1));
    }

    public static long incrementExact(long n) throws MathArithmeticException {
        if (n != MASK_NON_SIGN_LONG) {
            return 1 + n;
        }
        throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_ADDITION, Long.valueOf(n), Integer.valueOf(1));
    }

    public static int decrementExact(int n) throws MathArithmeticException {
        if (n != Integer.MIN_VALUE) {
            return n - 1;
        }
        throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_SUBTRACTION, Integer.valueOf(n), Integer.valueOf(1));
    }

    public static long decrementExact(long n) throws MathArithmeticException {
        if (n != Long.MIN_VALUE) {
            return n - 1;
        }
        throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_SUBTRACTION, Long.valueOf(n), Integer.valueOf(1));
    }

    public static int addExact(int a, int b) throws MathArithmeticException {
        int sum = a + b;
        if ((a ^ b) < 0 || (sum ^ b) >= 0) {
            return sum;
        }
        throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_ADDITION, Integer.valueOf(a), Integer.valueOf(b));
    }

    public static long addExact(long a, long b) throws MathArithmeticException {
        long sum = a + b;
        if ((a ^ b) < 0 || (sum ^ b) >= 0) {
            return sum;
        }
        throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_ADDITION, Long.valueOf(a), Long.valueOf(b));
    }

    public static int subtractExact(int a, int b) {
        int sub = a - b;
        if ((a ^ b) >= 0 || (sub ^ b) < 0) {
            return sub;
        }
        throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_SUBTRACTION, Integer.valueOf(a), Integer.valueOf(b));
    }

    public static long subtractExact(long a, long b) {
        long sub = a - b;
        if ((a ^ b) >= 0 || (sub ^ b) < 0) {
            return sub;
        }
        throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_SUBTRACTION, Long.valueOf(a), Long.valueOf(b));
    }

    public static int multiplyExact(int a, int b) {
        if ((b <= 0 || (a <= Integer.MAX_VALUE / b && a >= Integer.MIN_VALUE / b)) && ((b >= -1 || (a <= Integer.MIN_VALUE / b && a >= Integer.MAX_VALUE / b)) && (b != -1 || a != Integer.MIN_VALUE))) {
            return a * b;
        }
        throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_MULTIPLICATION, Integer.valueOf(a), Integer.valueOf(b));
    }

    public static long multiplyExact(long a, long b) {
        if ((b <= 0 || (a <= MASK_NON_SIGN_LONG / b && a >= Long.MIN_VALUE / b)) && ((b >= -1 || (a <= Long.MIN_VALUE / b && a >= MASK_NON_SIGN_LONG / b)) && (b != -1 || a != Long.MIN_VALUE))) {
            return a * b;
        }
        throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_MULTIPLICATION, Long.valueOf(a), Long.valueOf(b));
    }

    public static int floorDiv(int a, int b) throws MathArithmeticException {
        if (b == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR, new Object[0]);
        }
        int m = a % b;
        if ((a ^ b) >= 0 || m == 0) {
            return a / b;
        }
        return (a / b) - 1;
    }

    public static long floorDiv(long a, long b) throws MathArithmeticException {
        if (b == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR, new Object[0]);
        }
        long m = a % b;
        if ((a ^ b) >= 0 || m == 0) {
            return a / b;
        }
        return (a / b) - 1;
    }

    public static int floorMod(int a, int b) throws MathArithmeticException {
        if (b == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR, new Object[0]);
        }
        int m = a % b;
        if ((a ^ b) >= 0 || m == 0) {
            return m;
        }
        return b + m;
    }

    public static long floorMod(long a, long b) {
        if (b == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR, new Object[0]);
        }
        long m = a % b;
        if ((a ^ b) >= 0 || m == 0) {
            return m;
        }
        return b + m;
    }

    public static double copySign(double magnitude, double sign) {
        if ((Double.doubleToRawLongBits(magnitude) ^ Double.doubleToRawLongBits(sign)) >= 0) {
            return magnitude;
        }
        return -magnitude;
    }

    public static float copySign(float magnitude, float sign) {
        if ((Float.floatToRawIntBits(magnitude) ^ Float.floatToRawIntBits(sign)) >= 0) {
            return magnitude;
        }
        return -magnitude;
    }

    public static int getExponent(double d) {
        return ((int) ((Double.doubleToRawLongBits(d) >>> 52) & 2047)) - 1023;
    }

    public static int getExponent(float f) {
        return ((Float.floatToRawIntBits(f) >>> 23) & 255) - 127;
    }

    public static void main(String[] a) {
        PrintStream out = System.out;
        FastMathCalc.printarray(out, "EXP_INT_TABLE_A", 1500, ExpIntTable.EXP_INT_TABLE_A);
        FastMathCalc.printarray(out, "EXP_INT_TABLE_B", 1500, ExpIntTable.EXP_INT_TABLE_B);
        FastMathCalc.printarray(out, "EXP_FRAC_TABLE_A", 1025, ExpFracTable.EXP_FRAC_TABLE_A);
        FastMathCalc.printarray(out, "EXP_FRAC_TABLE_B", 1025, ExpFracTable.EXP_FRAC_TABLE_B);
        FastMathCalc.printarray(out, "LN_MANT", 1024, lnMant.LN_MANT);
        FastMathCalc.printarray(out, "SINE_TABLE_A", 14, SINE_TABLE_A);
        FastMathCalc.printarray(out, "SINE_TABLE_B", 14, SINE_TABLE_B);
        FastMathCalc.printarray(out, "COSINE_TABLE_A", 14, COSINE_TABLE_A);
        FastMathCalc.printarray(out, "COSINE_TABLE_B", 14, COSINE_TABLE_B);
        FastMathCalc.printarray(out, "TANGENT_TABLE_A", 14, TANGENT_TABLE_A);
        FastMathCalc.printarray(out, "TANGENT_TABLE_B", 14, TANGENT_TABLE_B);
    }
}
