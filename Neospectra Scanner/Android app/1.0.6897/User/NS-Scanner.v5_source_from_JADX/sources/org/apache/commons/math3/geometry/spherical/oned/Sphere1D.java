package org.apache.commons.math3.geometry.spherical.oned;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Space;

public class Sphere1D implements Serializable, Space {
    private static final long serialVersionUID = 20131218;

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static final Sphere1D INSTANCE = new Sphere1D();

        private LazyHolder() {
        }
    }

    public static class NoSubSpaceException extends MathUnsupportedOperationException {
        private static final long serialVersionUID = 20140225;

        public NoSubSpaceException() {
            super(LocalizedFormats.NOT_SUPPORTED_IN_DIMENSION_N, Integer.valueOf(1));
        }
    }

    private Sphere1D() {
    }

    public static Sphere1D getInstance() {
        return LazyHolder.INSTANCE;
    }

    public int getDimension() {
        return 1;
    }

    public Space getSubSpace() throws NoSubSpaceException {
        throw new NoSubSpaceException();
    }

    private Object readResolve() {
        return LazyHolder.INSTANCE;
    }
}
