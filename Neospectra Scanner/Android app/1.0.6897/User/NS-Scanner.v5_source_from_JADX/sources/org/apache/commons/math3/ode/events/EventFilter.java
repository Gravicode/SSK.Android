package org.apache.commons.math3.ode.events;

import java.util.Arrays;
import org.apache.commons.math3.ode.events.EventHandler.Action;

public class EventFilter implements EventHandler {
    private static final int HISTORY_SIZE = 100;
    private double extremeT;
    private final FilterType filter;
    private boolean forward;
    private final EventHandler rawHandler;
    private final Transformer[] transformers = new Transformer[100];
    private final double[] updates = new double[100];

    public EventFilter(EventHandler rawHandler2, FilterType filter2) {
        this.rawHandler = rawHandler2;
        this.filter = filter2;
    }

    public void init(double t0, double[] y0, double t) {
        this.rawHandler.init(t0, y0, t);
        this.forward = t >= t0;
        this.extremeT = this.forward ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        Arrays.fill(this.transformers, Transformer.UNINITIALIZED);
        Arrays.fill(this.updates, this.extremeT);
    }

    /* renamed from: g */
    public double mo18647g(double t, double[] y) {
        double rawG = this.rawHandler.mo18647g(t, y);
        int i = 0;
        if (this.forward) {
            int last = this.transformers.length - 1;
            if (this.extremeT < t) {
                Transformer previous = this.transformers[last];
                Transformer next = this.filter.selectTransformer(previous, rawG, this.forward);
                if (next != previous) {
                    System.arraycopy(this.updates, 1, this.updates, 0, last);
                    System.arraycopy(this.transformers, 1, this.transformers, 0, last);
                    this.updates[last] = this.extremeT;
                    this.transformers[last] = next;
                }
                this.extremeT = t;
                return next.transformed(rawG);
            }
            for (int i2 = last; i2 > 0; i2--) {
                if (this.updates[i2] <= t) {
                    return this.transformers[i2].transformed(rawG);
                }
            }
            return this.transformers[0].transformed(rawG);
        } else if (t < this.extremeT) {
            Transformer previous2 = this.transformers[0];
            Transformer next2 = this.filter.selectTransformer(previous2, rawG, this.forward);
            if (next2 != previous2) {
                System.arraycopy(this.updates, 0, this.updates, 1, this.updates.length - 1);
                System.arraycopy(this.transformers, 0, this.transformers, 1, this.transformers.length - 1);
                this.updates[0] = this.extremeT;
                this.transformers[0] = next2;
            }
            this.extremeT = t;
            return next2.transformed(rawG);
        } else {
            while (true) {
                int i3 = i;
                if (i3 >= this.updates.length - 1) {
                    return this.transformers[this.updates.length - 1].transformed(rawG);
                }
                if (t <= this.updates[i3]) {
                    return this.transformers[i3].transformed(rawG);
                }
                i = i3 + 1;
            }
        }
    }

    public Action eventOccurred(double t, double[] y, boolean increasing) {
        return this.rawHandler.eventOccurred(t, y, this.filter.getTriggeredIncreasing());
    }

    public void resetState(double t, double[] y) {
        this.rawHandler.resetState(t, y);
    }
}
