package org.apache.poi.hssf.record.formula.functions;

final class MathX {
    private MathX() {
    }

    public static double round(double n, int p) {
        double retval;
        if (Double.isNaN(n) || Double.isInfinite(n)) {
            retval = Double.NaN;
        } else if (p != 0) {
            double temp = Math.pow(10.0d, (double) p);
            retval = ((double) Math.round(n * temp)) / temp;
        } else {
            retval = (double) Math.round(n);
        }
        return retval;
    }

    public static double roundUp(double n, int p) {
        double retval;
        if (Double.isNaN(n) || Double.isInfinite(n)) {
            retval = Double.NaN;
        } else if (p != 0) {
            double temp = Math.pow(10.0d, (double) p);
            double nat = Math.abs(n * temp);
            retval = ((double) sign(n)) * (nat == ((double) ((long) nat)) ? nat / temp : ((double) Math.round(0.5d + nat)) / temp);
        } else {
            double na = Math.abs(n);
            retval = (na == ((double) ((long) na)) ? na : (double) (((long) na) + 1)) * ((double) sign(n));
        }
        return retval;
    }

    public static double roundDown(double n, int p) {
        double retval;
        if (Double.isNaN(n) || Double.isInfinite(n)) {
            retval = Double.NaN;
        } else if (p != 0) {
            double temp = Math.pow(10.0d, (double) p);
            retval = ((double) (((long) sign(n)) * Math.round((Math.abs(n) * temp) - 0.5d))) / temp;
        } else {
            retval = (double) ((long) n);
        }
        return retval;
    }

    public static short sign(double d) {
        int i = d == 0.0d ? 0 : d < 0.0d ? -1 : 1;
        return (short) i;
    }

    public static double average(double[] values) {
        double sum = 0.0d;
        for (double d : values) {
            sum += d;
        }
        return sum / ((double) values.length);
    }

    public static double sum(double[] values) {
        double sum = 0.0d;
        for (double d : values) {
            sum += d;
        }
        return sum;
    }

    public static double sumsq(double[] values) {
        double sumsq = 0.0d;
        for (int i = 0; i < values.length; i++) {
            sumsq += values[i] * values[i];
        }
        return sumsq;
    }

    public static double product(double[] values) {
        double product = 0.0d;
        if (values != null && values.length > 0) {
            product = 1.0d;
            for (double d : values) {
                product *= d;
            }
        }
        return product;
    }

    public static double min(double[] values) {
        double min = Double.POSITIVE_INFINITY;
        for (double min2 : values) {
            min = Math.min(min, min2);
        }
        return min;
    }

    public static double max(double[] values) {
        double max = Double.NEGATIVE_INFINITY;
        for (double max2 : values) {
            max = Math.max(max, max2);
        }
        return max;
    }

    public static double floor(double n, double s) {
        if ((n < 0.0d && s > 0.0d) || ((n > 0.0d && s < 0.0d) || (s == 0.0d && n != 0.0d))) {
            return Double.NaN;
        }
        if (n == 0.0d || s == 0.0d) {
            return 0.0d;
        }
        return Math.floor(n / s) * s;
    }

    public static double ceiling(double n, double s) {
        if ((n < 0.0d && s > 0.0d) || (n > 0.0d && s < 0.0d)) {
            return Double.NaN;
        }
        if (n == 0.0d || s == 0.0d) {
            return 0.0d;
        }
        return Math.ceil(n / s) * s;
    }

    public static double factorial(int n) {
        double d = 1.0d;
        if (n < 0) {
            return Double.NaN;
        }
        if (n > 170) {
            return Double.POSITIVE_INFINITY;
        }
        for (int i = 1; i <= n; i++) {
            d *= (double) i;
        }
        return d;
    }

    public static double mod(double n, double d) {
        if (d == 0.0d) {
            return Double.NaN;
        }
        if (sign(n) == sign(d)) {
            return n % d;
        }
        return ((n % d) + d) % d;
    }

    public static double acosh(double d) {
        return Math.log(Math.sqrt(Math.pow(d, 2.0d) - 1.0d) + d);
    }

    public static double asinh(double d) {
        return Math.log(Math.sqrt((d * d) + 1.0d) + d);
    }

    public static double atanh(double d) {
        return Math.log((d + 1.0d) / (1.0d - d)) / 2.0d;
    }

    public static double cosh(double d) {
        return (Math.pow(2.718281828459045d, d) + Math.pow(2.718281828459045d, -d)) / 2.0d;
    }

    public static double sinh(double d) {
        return (Math.pow(2.718281828459045d, d) - Math.pow(2.718281828459045d, -d)) / 2.0d;
    }

    public static double tanh(double d) {
        double ePowX = Math.pow(2.718281828459045d, d);
        double ePowNegX = Math.pow(2.718281828459045d, -d);
        return (ePowX - ePowNegX) / (ePowX + ePowNegX);
    }

    public static double nChooseK(int n, int k) {
        if (n < 0 || k < 0 || n < k) {
            return Double.NaN;
        }
        int minnk = Math.min(n - k, k);
        double d = 1.0d;
        for (int i = Math.max(n - k, k); i < n; i++) {
            d *= (double) (i + 1);
        }
        return d / factorial(minnk);
    }
}
