package com.jakewharton.rxbinding.internal;

import p008rx.functions.Func0;
import p008rx.functions.Func1;

public final class Functions {
    private static final Always<Boolean> ALWAYS_TRUE = new Always<>(Boolean.valueOf(true));
    public static final Func0<Boolean> FUNC0_ALWAYS_TRUE = ALWAYS_TRUE;
    public static final Func1<Object, Boolean> FUNC1_ALWAYS_TRUE = ALWAYS_TRUE;

    private static final class Always<T> implements Func1<Object, T>, Func0<T> {
        private final T value;

        Always(T value2) {
            this.value = value2;
        }

        public T call(Object o) {
            return this.value;
        }

        public T call() {
            return this.value;
        }
    }

    private Functions() {
        throw new AssertionError("No instances.");
    }
}
