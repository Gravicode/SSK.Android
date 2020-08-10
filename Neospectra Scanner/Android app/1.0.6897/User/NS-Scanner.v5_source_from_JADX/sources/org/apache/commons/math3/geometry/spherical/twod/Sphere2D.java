package org.apache.commons.math3.geometry.spherical.twod;

import java.io.Serializable;
import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.spherical.oned.Sphere1D;

public class Sphere2D implements Serializable, Space {
    private static final long serialVersionUID = 20131218;

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static final Sphere2D INSTANCE = new Sphere2D();

        private LazyHolder() {
        }
    }

    private Sphere2D() {
    }

    public static Sphere2D getInstance() {
        return LazyHolder.INSTANCE;
    }

    public int getDimension() {
        return 2;
    }

    public Sphere1D getSubSpace() {
        return Sphere1D.getInstance();
    }

    private Object readResolve() {
        return LazyHolder.INSTANCE;
    }
}
