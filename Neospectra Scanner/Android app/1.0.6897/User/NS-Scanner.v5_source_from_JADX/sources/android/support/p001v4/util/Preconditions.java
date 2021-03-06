package android.support.p001v4.util;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.text.TextUtils;
import java.util.Collection;
import java.util.Locale;

@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v4.util.Preconditions */
public class Preconditions {
    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkArgument(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    @NonNull
    public static <T extends CharSequence> T checkStringNotEmpty(T string) {
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        throw new IllegalArgumentException();
    }

    @NonNull
    public static <T extends CharSequence> T checkStringNotEmpty(T string, Object errorMessage) {
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        throw new IllegalArgumentException(String.valueOf(errorMessage));
    }

    @NonNull
    public static <T> T checkNotNull(T reference) {
        if (reference != null) {
            return reference;
        }
        throw new NullPointerException();
    }

    @NonNull
    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference != null) {
            return reference;
        }
        throw new NullPointerException(String.valueOf(errorMessage));
    }

    public static void checkState(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    public static void checkState(boolean expression) {
        checkState(expression, null);
    }

    public static int checkFlagsArgument(int requestedFlags, int allowedFlags) {
        if ((requestedFlags & allowedFlags) == requestedFlags) {
            return requestedFlags;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Requested flags 0x");
        sb.append(Integer.toHexString(requestedFlags));
        sb.append(", but only 0x");
        sb.append(Integer.toHexString(allowedFlags));
        sb.append(" are allowed");
        throw new IllegalArgumentException(sb.toString());
    }

    @IntRange(from = 0)
    public static int checkArgumentNonnegative(int value, String errorMessage) {
        if (value >= 0) {
            return value;
        }
        throw new IllegalArgumentException(errorMessage);
    }

    @IntRange(from = 0)
    public static int checkArgumentNonnegative(int value) {
        if (value >= 0) {
            return value;
        }
        throw new IllegalArgumentException();
    }

    public static long checkArgumentNonnegative(long value) {
        if (value >= 0) {
            return value;
        }
        throw new IllegalArgumentException();
    }

    public static long checkArgumentNonnegative(long value, String errorMessage) {
        if (value >= 0) {
            return value;
        }
        throw new IllegalArgumentException(errorMessage);
    }

    public static int checkArgumentPositive(int value, String errorMessage) {
        if (value > 0) {
            return value;
        }
        throw new IllegalArgumentException(errorMessage);
    }

    public static float checkArgumentFinite(float value, String valueName) {
        if (Float.isNaN(value)) {
            StringBuilder sb = new StringBuilder();
            sb.append(valueName);
            sb.append(" must not be NaN");
            throw new IllegalArgumentException(sb.toString());
        } else if (!Float.isInfinite(value)) {
            return value;
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(valueName);
            sb2.append(" must not be infinite");
            throw new IllegalArgumentException(sb2.toString());
        }
    }

    public static float checkArgumentInRange(float value, float lower, float upper, String valueName) {
        if (Float.isNaN(value)) {
            StringBuilder sb = new StringBuilder();
            sb.append(valueName);
            sb.append(" must not be NaN");
            throw new IllegalArgumentException(sb.toString());
        } else if (value < lower) {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%f, %f] (too low)", new Object[]{valueName, Float.valueOf(lower), Float.valueOf(upper)}));
        } else if (value <= upper) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%f, %f] (too high)", new Object[]{valueName, Float.valueOf(lower), Float.valueOf(upper)}));
        }
    }

    public static int checkArgumentInRange(int value, int lower, int upper, String valueName) {
        if (value < lower) {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", new Object[]{valueName, Integer.valueOf(lower), Integer.valueOf(upper)}));
        } else if (value <= upper) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", new Object[]{valueName, Integer.valueOf(lower), Integer.valueOf(upper)}));
        }
    }

    public static long checkArgumentInRange(long value, long lower, long upper, String valueName) {
        if (value < lower) {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", new Object[]{valueName, Long.valueOf(lower), Long.valueOf(upper)}));
        } else if (value <= upper) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", new Object[]{valueName, Long.valueOf(lower), Long.valueOf(upper)}));
        }
    }

    public static <T> T[] checkArrayElementsNotNull(T[] value, String valueName) {
        if (value == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(valueName);
            sb.append(" must not be null");
            throw new NullPointerException(sb.toString());
        }
        for (int i = 0; i < value.length; i++) {
            if (value[i] == null) {
                throw new NullPointerException(String.format(Locale.US, "%s[%d] must not be null", new Object[]{valueName, Integer.valueOf(i)}));
            }
        }
        return value;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=C, code=C<T>, for r9v0, types: [C<T>, C, java.util.Collection] */
    @android.support.annotation.NonNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <C extends java.util.Collection<T>, T> C checkCollectionElementsNotNull(C<T> r9, java.lang.String r10) {
        /*
            if (r9 != 0) goto L_0x0019
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r10)
            java.lang.String r2 = " must not be null"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x0019:
            r0 = 0
            java.util.Iterator r2 = r9.iterator()
        L_0x001f:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x004a
            java.lang.Object r3 = r2.next()
            if (r3 != 0) goto L_0x0046
            java.lang.NullPointerException r2 = new java.lang.NullPointerException
            java.util.Locale r4 = java.util.Locale.US
            java.lang.String r5 = "%s[%d] must not be null"
            r6 = 2
            java.lang.Object[] r6 = new java.lang.Object[r6]
            r7 = 0
            r6[r7] = r10
            r7 = 1
            java.lang.Long r8 = java.lang.Long.valueOf(r0)
            r6[r7] = r8
            java.lang.String r4 = java.lang.String.format(r4, r5, r6)
            r2.<init>(r4)
            throw r2
        L_0x0046:
            r4 = 1
            long r0 = r0 + r4
            goto L_0x001f
        L_0x004a:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p001v4.util.Preconditions.checkCollectionElementsNotNull(java.util.Collection, java.lang.String):java.util.Collection");
    }

    public static <T> Collection<T> checkCollectionNotEmpty(Collection<T> value, String valueName) {
        if (value == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(valueName);
            sb.append(" must not be null");
            throw new NullPointerException(sb.toString());
        } else if (!value.isEmpty()) {
            return value;
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(valueName);
            sb2.append(" is empty");
            throw new IllegalArgumentException(sb2.toString());
        }
    }

    public static float[] checkArrayElementsInRange(float[] value, float lower, float upper, String valueName) {
        StringBuilder sb = new StringBuilder();
        sb.append(valueName);
        sb.append(" must not be null");
        checkNotNull(value, sb.toString());
        int i = 0;
        while (i < value.length) {
            float v = value[i];
            if (Float.isNaN(v)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(valueName);
                sb2.append("[");
                sb2.append(i);
                sb2.append("] must not be NaN");
                throw new IllegalArgumentException(sb2.toString());
            } else if (v < lower) {
                throw new IllegalArgumentException(String.format(Locale.US, "%s[%d] is out of range of [%f, %f] (too low)", new Object[]{valueName, Integer.valueOf(i), Float.valueOf(lower), Float.valueOf(upper)}));
            } else if (v > upper) {
                throw new IllegalArgumentException(String.format(Locale.US, "%s[%d] is out of range of [%f, %f] (too high)", new Object[]{valueName, Integer.valueOf(i), Float.valueOf(lower), Float.valueOf(upper)}));
            } else {
                i++;
            }
        }
        return value;
    }
}
