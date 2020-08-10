package org.apache.commons.math3.fraction;

import java.io.Serializable;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;

public class BigFractionField implements Field<BigFraction>, Serializable {
    private static final long serialVersionUID = -1699294557189741703L;

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static final BigFractionField INSTANCE = new BigFractionField();

        private LazyHolder() {
        }
    }

    private BigFractionField() {
    }

    public static BigFractionField getInstance() {
        return LazyHolder.INSTANCE;
    }

    public BigFraction getOne() {
        return BigFraction.ONE;
    }

    public BigFraction getZero() {
        return BigFraction.ZERO;
    }

    public Class<? extends FieldElement<BigFraction>> getRuntimeClass() {
        return BigFraction.class;
    }

    private Object readResolve() {
        return LazyHolder.INSTANCE;
    }
}
