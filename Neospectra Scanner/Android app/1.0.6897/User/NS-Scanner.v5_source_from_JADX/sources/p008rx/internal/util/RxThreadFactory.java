package p008rx.internal.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/* renamed from: rx.internal.util.RxThreadFactory */
public final class RxThreadFactory extends AtomicLong implements ThreadFactory {
    public static final ThreadFactory NONE = new ThreadFactory() {
        public Thread newThread(Runnable r) {
            throw new AssertionError("No threads allowed.");
        }
    };
    private static final long serialVersionUID = -8841098858898482335L;
    final String prefix;

    public RxThreadFactory(String prefix2) {
        this.prefix = prefix2;
    }

    public Thread newThread(Runnable r) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.prefix);
        sb.append(incrementAndGet());
        Thread t = new Thread(r, sb.toString());
        t.setDaemon(true);
        return t;
    }
}
