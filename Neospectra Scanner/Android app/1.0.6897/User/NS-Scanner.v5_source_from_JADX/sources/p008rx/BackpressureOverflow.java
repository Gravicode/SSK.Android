package p008rx;

import p008rx.annotations.Beta;
import p008rx.exceptions.MissingBackpressureException;

@Beta
/* renamed from: rx.BackpressureOverflow */
public final class BackpressureOverflow {
    public static final Strategy ON_OVERFLOW_DEFAULT = ON_OVERFLOW_ERROR;
    public static final Strategy ON_OVERFLOW_DROP_LATEST = DropLatest.INSTANCE;
    public static final Strategy ON_OVERFLOW_DROP_OLDEST = DropOldest.INSTANCE;
    public static final Strategy ON_OVERFLOW_ERROR = Error.INSTANCE;

    /* renamed from: rx.BackpressureOverflow$DropLatest */
    static class DropLatest implements Strategy {
        static final DropLatest INSTANCE = new DropLatest();

        private DropLatest() {
        }

        public boolean mayAttemptDrop() {
            return false;
        }
    }

    /* renamed from: rx.BackpressureOverflow$DropOldest */
    static class DropOldest implements Strategy {
        static final DropOldest INSTANCE = new DropOldest();

        private DropOldest() {
        }

        public boolean mayAttemptDrop() {
            return true;
        }
    }

    /* renamed from: rx.BackpressureOverflow$Error */
    static class Error implements Strategy {
        static final Error INSTANCE = new Error();

        private Error() {
        }

        public boolean mayAttemptDrop() throws MissingBackpressureException {
            throw new MissingBackpressureException("Overflowed buffer");
        }
    }

    /* renamed from: rx.BackpressureOverflow$Strategy */
    public interface Strategy {
        boolean mayAttemptDrop() throws MissingBackpressureException;
    }
}
