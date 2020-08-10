package p005io.reactivex.internal.util;

import java.util.concurrent.CountDownLatch;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.schedulers.NonBlockingThread;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.util.BlockingHelper */
public final class BlockingHelper {
    private BlockingHelper() {
        throw new IllegalStateException("No instances!");
    }

    public static void awaitForComplete(CountDownLatch latch, Disposable subscription) {
        if (latch.getCount() != 0) {
            try {
                verifyNonBlocking();
                latch.await();
            } catch (InterruptedException e) {
                subscription.dispose();
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while waiting for subscription to complete.", e);
            }
        }
    }

    public static void verifyNonBlocking() {
        if (!RxJavaPlugins.isFailOnNonBlockingScheduler()) {
            return;
        }
        if ((Thread.currentThread() instanceof NonBlockingThread) || RxJavaPlugins.onBeforeBlocking()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Attempt to block on a Scheduler ");
            sb.append(Thread.currentThread().getName());
            sb.append(" that doesn't support blocking operators as they may lead to deadlock");
            throw new IllegalStateException(sb.toString());
        }
    }
}
