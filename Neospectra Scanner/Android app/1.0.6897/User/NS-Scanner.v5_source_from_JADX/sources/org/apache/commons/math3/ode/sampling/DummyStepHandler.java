package org.apache.commons.math3.ode.sampling;

public class DummyStepHandler implements StepHandler {

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static final DummyStepHandler INSTANCE = new DummyStepHandler();

        private LazyHolder() {
        }
    }

    private DummyStepHandler() {
    }

    public static DummyStepHandler getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void init(double t0, double[] y0, double t) {
    }

    public void handleStep(StepInterpolator interpolator, boolean isLast) {
    }

    private Object readResolve() {
        return LazyHolder.INSTANCE;
    }
}
