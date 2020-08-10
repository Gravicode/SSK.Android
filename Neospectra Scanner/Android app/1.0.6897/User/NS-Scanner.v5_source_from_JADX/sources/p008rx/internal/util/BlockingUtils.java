package p008rx.internal.util;

import java.util.concurrent.CountDownLatch;
import p008rx.Subscription;
import p008rx.annotations.Experimental;

@Experimental
/* renamed from: rx.internal.util.BlockingUtils */
public final class BlockingUtils {
    private BlockingUtils() {
    }

    @Experimental
    public static void awaitForComplete(CountDownLatch latch, Subscription subscription) {
        if (latch.getCount() != 0) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                subscription.unsubscribe();
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while waiting for subscription to complete.", e);
            }
        }
    }
}
