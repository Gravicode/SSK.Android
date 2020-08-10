package org.apache.commons.math3.fraction;

import java.io.Serializable;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;

public class FractionField implements Field<Fraction>, Serializable {
    private static final long serialVersionUID = -1257768487499119313L;

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static final FractionField INSTANCE = new FractionField();

        private LazyHolder() {
        }
    }

    private FractionField() {
    }

    public static FractionField getInstance() {
        return LazyHolder.INSTANCE;
    }

    public Fraction getOne() {
        return Fraction.ONE;
    }

    public Fraction getZero() {
        return Fraction.ZERO;
    }

    public Class<? extends FieldElement<Fraction>> getRuntimeClass() {
        return Fraction.class;
    }

    private Object readResolve() {
        return LazyHolder.INSTANCE;
    }
}
