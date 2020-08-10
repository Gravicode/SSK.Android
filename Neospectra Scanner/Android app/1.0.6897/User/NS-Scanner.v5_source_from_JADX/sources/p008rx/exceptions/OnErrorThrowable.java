package p008rx.exceptions;

import java.util.HashSet;
import java.util.Set;
import p008rx.plugins.RxJavaPlugins;

/* renamed from: rx.exceptions.OnErrorThrowable */
public final class OnErrorThrowable extends RuntimeException {
    private static final long serialVersionUID = -569558213262703934L;
    private final boolean hasValue;
    private final Object value;

    /* renamed from: rx.exceptions.OnErrorThrowable$OnNextValue */
    public static class OnNextValue extends RuntimeException {
        private static final long serialVersionUID = -3454462756050397899L;
        private final Object value;

        /* renamed from: rx.exceptions.OnErrorThrowable$OnNextValue$Primitives */
        static final class Primitives {
            static final Set<Class<?>> INSTANCE = create();

            Primitives() {
            }

            private static Set<Class<?>> create() {
                Set<Class<?>> set = new HashSet<>();
                set.add(Boolean.class);
                set.add(Character.class);
                set.add(Byte.class);
                set.add(Short.class);
                set.add(Integer.class);
                set.add(Long.class);
                set.add(Float.class);
                set.add(Double.class);
                return set;
            }
        }

        public OnNextValue(Object value2) {
            StringBuilder sb = new StringBuilder();
            sb.append("OnError while emitting onNext value: ");
            sb.append(renderValue(value2));
            super(sb.toString());
            this.value = value2;
        }

        public Object getValue() {
            return this.value;
        }

        static String renderValue(Object value2) {
            if (value2 == null) {
                return "null";
            }
            if (Primitives.INSTANCE.contains(value2.getClass())) {
                return value2.toString();
            }
            if (value2 instanceof String) {
                return (String) value2;
            }
            if (value2 instanceof Enum) {
                return ((Enum) value2).name();
            }
            String pluggedRendering = RxJavaPlugins.getInstance().getErrorHandler().handleOnNextValueRendering(value2);
            if (pluggedRendering != null) {
                return pluggedRendering;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(value2.getClass().getName());
            sb.append(".class");
            return sb.toString();
        }
    }

    private OnErrorThrowable(Throwable exception) {
        super(exception);
        this.hasValue = false;
        this.value = null;
    }

    private OnErrorThrowable(Throwable exception, Object value2) {
        super(exception);
        this.hasValue = true;
        this.value = value2;
    }

    public Object getValue() {
        return this.value;
    }

    public boolean isValueNull() {
        return this.hasValue;
    }

    public static OnErrorThrowable from(Throwable t) {
        if (t == null) {
            t = new NullPointerException();
        }
        Throwable cause = Exceptions.getFinalCause(t);
        if (cause instanceof OnNextValue) {
            return new OnErrorThrowable(t, ((OnNextValue) cause).getValue());
        }
        return new OnErrorThrowable(t);
    }

    public static Throwable addValueAsLastCause(Throwable e, Object value2) {
        if (e == null) {
            e = new NullPointerException();
        }
        Throwable lastCause = Exceptions.getFinalCause(e);
        if ((lastCause instanceof OnNextValue) && ((OnNextValue) lastCause).getValue() == value2) {
            return e;
        }
        Exceptions.addCause(e, new OnNextValue(value2));
        return e;
    }
}
