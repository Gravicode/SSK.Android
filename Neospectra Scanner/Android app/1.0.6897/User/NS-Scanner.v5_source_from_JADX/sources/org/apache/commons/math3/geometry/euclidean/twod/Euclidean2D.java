package org.apache.commons.math3.geometry.euclidean.twod;

import java.io.Serializable;
import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;

public class Euclidean2D implements Serializable, Space {
    private static final long serialVersionUID = 4793432849757649566L;

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static final Euclidean2D INSTANCE = new Euclidean2D();

        private LazyHolder() {
        }
    }

    private Euclidean2D() {
    }

    public static Euclidean2D getInstance() {
        return LazyHolder.INSTANCE;
    }

    public int getDimension() {
        return 2;
    }

    public Euclidean1D getSubSpace() {
        return Euclidean1D.getInstance();
    }

    private Object readResolve() {
        return LazyHolder.INSTANCE;
    }
}
