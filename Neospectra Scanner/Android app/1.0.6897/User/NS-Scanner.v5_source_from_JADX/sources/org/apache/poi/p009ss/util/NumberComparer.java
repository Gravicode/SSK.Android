package org.apache.poi.p009ss.util;

/* renamed from: org.apache.poi.ss.util.NumberComparer */
public final class NumberComparer {
    public static int compare(double a, double b) {
        long rawBitsA = Double.doubleToLongBits(a);
        long rawBitsB = Double.doubleToLongBits(b);
        int biasedExponentA = IEEEDouble.getBiasedExponent(rawBitsA);
        int biasedExponentB = IEEEDouble.getBiasedExponent(rawBitsB);
        if (biasedExponentA == 2047) {
            StringBuilder sb = new StringBuilder();
            sb.append("Special double values are not allowed: ");
            sb.append(toHex(a));
            throw new IllegalArgumentException(sb.toString());
        } else if (biasedExponentB == 2047) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Special double values are not allowed: ");
            sb2.append(toHex(a));
            throw new IllegalArgumentException(sb2.toString());
        } else {
            int i = 1;
            boolean aIsNegative = rawBitsA < 0;
            if (aIsNegative != (rawBitsB < 0)) {
                if (aIsNegative) {
                    i = -1;
                }
                return i;
            }
            int cmp = biasedExponentA - biasedExponentB;
            int absExpDiff = Math.abs(cmp);
            if (absExpDiff > 1) {
                return aIsNegative ? -cmp : cmp;
            } else if (absExpDiff != 1 && rawBitsA == rawBitsB) {
                return 0;
            } else {
                if (biasedExponentA == 0) {
                    if (biasedExponentB == 0) {
                        return compareSubnormalNumbers(rawBitsA & IEEEDouble.FRAC_MASK, IEEEDouble.FRAC_MASK & rawBitsB, aIsNegative);
                    }
                    return -compareAcrossSubnormalThreshold(rawBitsB, rawBitsA, aIsNegative);
                } else if (biasedExponentB == 0) {
                    return compareAcrossSubnormalThreshold(rawBitsA, rawBitsB, aIsNegative);
                } else {
                    int cmp2 = ExpandedDouble.fromRawBitsAndExponent(rawBitsA, biasedExponentA - 1023).normaliseBaseTen().roundUnits().compareNormalised(ExpandedDouble.fromRawBitsAndExponent(rawBitsB, biasedExponentB - 1023).normaliseBaseTen().roundUnits());
                    if (aIsNegative) {
                        return -cmp2;
                    }
                    return cmp2;
                }
            }
        }
    }

    private static int compareSubnormalNumbers(long fracA, long fracB, boolean isNegative) {
        int cmp = fracA > fracB ? 1 : fracA < fracB ? -1 : 0;
        return isNegative ? -cmp : cmp;
    }

    private static int compareAcrossSubnormalThreshold(long normalRawBitsA, long subnormalRawBitsB, boolean isNegative) {
        long fracB = subnormalRawBitsB & IEEEDouble.FRAC_MASK;
        int i = 1;
        if (fracB == 0) {
            if (isNegative) {
                i = -1;
            }
            return i;
        }
        long fracA = IEEEDouble.FRAC_MASK & normalRawBitsA;
        if (fracA > 7 || fracB < 4503599627370490L) {
            if (isNegative) {
                i = -1;
            }
            return i;
        } else if (fracA == 7 && fracB == 4503599627370490L) {
            return 0;
        } else {
            if (!isNegative) {
                i = -1;
            }
            return i;
        }
    }

    private static String toHex(double a) {
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        sb.append(Long.toHexString(Double.doubleToLongBits(a)).toUpperCase());
        return sb.toString();
    }
}
