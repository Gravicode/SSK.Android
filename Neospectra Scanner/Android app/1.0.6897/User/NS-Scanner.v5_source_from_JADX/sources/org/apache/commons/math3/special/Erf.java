package org.apache.commons.math3.special;

import org.apache.commons.math3.util.FastMath;

public class Erf {
    private static final double X_CRIT = 0.4769362762044697d;

    private Erf() {
    }

    public static double erf(double x) {
        if (FastMath.abs(x) > 40.0d) {
            return x > 0.0d ? 1.0d : -1.0d;
        }
        double ret = Gamma.regularizedGammaP(0.5d, x * x, 1.0E-15d, 10000);
        return x < 0.0d ? -ret : ret;
    }

    public static double erfc(double x) {
        double d = 2.0d;
        if (FastMath.abs(x) > 40.0d) {
            if (x > 0.0d) {
                d = 0.0d;
            }
            return d;
        }
        double ret = Gamma.regularizedGammaQ(0.5d, x * x, 1.0E-15d, 10000);
        return x < 0.0d ? 2.0d - ret : ret;
    }

    public static double erf(double x1, double x2) {
        double erfc;
        double erfc2;
        if (x1 > x2) {
            return -erf(x2, x1);
        }
        if (x1 < -0.4769362762044697d) {
            if (x2 < 0.0d) {
                erfc = erfc(-x2);
                erfc2 = erfc(-x1);
            }
            erfc = erf(x2);
            erfc2 = erf(x1);
        } else {
            if (x2 > X_CRIT && x1 > 0.0d) {
                erfc = erfc(x1);
                erfc2 = erfc(x2);
            }
            erfc = erf(x2);
            erfc2 = erf(x1);
        }
        return erfc - erfc2;
    }

    public static double erfInv(double x) {
        double p;
        double w = -FastMath.log((1.0d - x) * (1.0d + x));
        if (w < 6.25d) {
            double w2 = w - 3.125d;
            p = (((((((((((((((((((((((((((((((((((((((((((-3.64441206401782E-21d * w2) - 2.667043662401199E19d) * w2) + 1.28584807152564E-18d) * w2) + 1.1157877678025181E-17d) * w2) - 3.2411248538046888E16d) * w2) + 2.0972767875968562E-17d) * w2) + 6.637638134358324E-15d) * w2) - 1.1072003390794989E14d) * w2) - 5.5095087562318234E13d) * w2) + 2.6335093153082323E-12d) * w2) - 3.344470457369893E11d) * w2) - 7.829062832735399E10d) * w2) + 1.0512122733215323E-9d) * w2) - 1.0179197164351387E9d) * w2) - 1.4081056938032028E8d) * w2) + 4.2347877827932404E-7d) * w2) - 3288757.474276467d) * w2) - 309432.1293670094d) * w2) + 1.8673420803405714E-4d) * w2) - 6074.5367962725795d) * w2) - 745.1546915399075d) * w2) + 0.24015818242558962d) * w2) + 1.6536545626831027d;
        } else if (w < 16.0d) {
            double w3 = FastMath.sqrt(w) - 3.25d;
            p = (((((((((((((((((((((((((((((((((((2.2137376921775787E-9d * w3) + 9.075656193888539E-8d) * w3) - 1.5483997379245123E7d) * w3) + 1.8239629214389228E-8d) * w3) + 1.5027403968909828E-6d) * w3) - 1021202.2477164116d) * w3) + 2.9234449089955446E-6d) * w3) + 1.2475304481671779E-5d) * w3) - 94992.87695073357d) * w3) + 6.828485145957318E-5d) * w3) + 2.4031110387097894E-5d) * w3) - 12662.917665536219d) * w3) + 9.532893797373805E-4d) * w3) - 2603.429541134195d) * w3) + 0.002491442096107851d) * w3) - 1105.2863939835377d) * w3) + 0.005370914553590064d) * w3) + 1.0052589676941592d) * w3) + 3.0838856104922208d;
        } else if (!Double.isInfinite(w)) {
            double w4 = FastMath.sqrt(w) - 5.0d;
            p = (((((((((((((((((((((((((((((((-2.7109920616438573E-11d * w4) - 1.6341149667559008E10d) * w4) + 1.5076572693500548E-9d) * w4) - 1.0644932113891793E9d) * w4) + 7.61570120807834E-9d) * w4) - 2.6790524331303596E8d) * w4) + 2.914795345090108E-8d) * w4) - 6.254483001581527E7d) * w4) + 2.2900482228026655E-7d) * w4) - 4107863.88563695d) * w4) + 4.526062597223154E-6d) * w4) - 224150.81341922528d) * w4) + 7.599527703001776E-5d) * w4) - 20291.145935925353d) * w4) - 30533.408263202306d) * w4) + 1.0103004648645344d) * w4) + 4.849906401408584d;
        } else {
            p = Double.POSITIVE_INFINITY;
        }
        return p * x;
    }

    public static double erfcInv(double x) {
        return erfInv(1.0d - x);
    }
}
