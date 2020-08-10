package p008rx.internal.util.unsafe;

import java.lang.reflect.Field;
import p008rx.internal.util.SuppressAnimalSniffer;
import sun.misc.Unsafe;

@SuppressAnimalSniffer
/* renamed from: rx.internal.util.unsafe.UnsafeAccess */
public final class UnsafeAccess {
    private static final boolean DISABLED_BY_USER = (System.getProperty("rx.unsafe-disable") != null);
    public static final Unsafe UNSAFE;

    static {
        Unsafe u = null;
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            u = (Unsafe) field.get(null);
        } catch (Throwable th) {
        }
        UNSAFE = u;
    }

    private UnsafeAccess() {
        throw new IllegalStateException("No instances!");
    }

    public static boolean isUnsafeAvailable() {
        return UNSAFE != null && !DISABLED_BY_USER;
    }

    public static int getAndIncrementInt(Object obj, long offset) {
        int current;
        do {
            current = UNSAFE.getIntVolatile(obj, offset);
        } while (!UNSAFE.compareAndSwapInt(obj, offset, current, current + 1));
        return current;
    }

    public static int getAndAddInt(Object obj, long offset, int n) {
        int current;
        do {
            current = UNSAFE.getIntVolatile(obj, offset);
        } while (!UNSAFE.compareAndSwapInt(obj, offset, current, current + n));
        return current;
    }

    public static int getAndSetInt(Object obj, long offset, int newValue) {
        int current;
        do {
            current = UNSAFE.getIntVolatile(obj, offset);
        } while (!UNSAFE.compareAndSwapInt(obj, offset, current, newValue));
        return current;
    }

    public static boolean compareAndSwapInt(Object obj, long offset, int expected, int newValue) {
        return UNSAFE.compareAndSwapInt(obj, offset, expected, newValue);
    }

    public static long addressOf(Class<?> clazz, String fieldName) {
        try {
            return UNSAFE.objectFieldOffset(clazz.getDeclaredField(fieldName));
        } catch (NoSuchFieldException ex) {
            InternalError ie = new InternalError();
            ie.initCause(ex);
            throw ie;
        }
    }
}
