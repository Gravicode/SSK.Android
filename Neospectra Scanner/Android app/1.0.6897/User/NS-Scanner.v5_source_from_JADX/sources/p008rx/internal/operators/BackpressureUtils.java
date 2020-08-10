package p008rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import p008rx.Subscriber;
import p008rx.functions.Func1;
import p008rx.internal.util.UtilityFunctions;

/* renamed from: rx.internal.operators.BackpressureUtils */
public final class BackpressureUtils {
    static final long COMPLETED_MASK = Long.MIN_VALUE;
    static final long REQUESTED_MASK = Long.MAX_VALUE;

    private BackpressureUtils() {
        throw new IllegalStateException("No instances!");
    }

    public static long getAndAddRequest(AtomicLong requested, long n) {
        long current;
        do {
            current = requested.get();
        } while (!requested.compareAndSet(current, addCap(current, n)));
        return current;
    }

    public static long multiplyCap(long a, long b) {
        long u = a * b;
        if (((a | b) >>> 31) == 0 || b == 0 || u / b == a) {
            return u;
        }
        return REQUESTED_MASK;
    }

    public static long addCap(long a, long b) {
        long u = a + b;
        if (u < 0) {
            return REQUESTED_MASK;
        }
        return u;
    }

    public static <T> void postCompleteDone(AtomicLong requested, Queue<T> queue, Subscriber<? super T> actual) {
        postCompleteDone(requested, queue, actual, UtilityFunctions.identity());
    }

    public static <T> boolean postCompleteRequest(AtomicLong requested, long n, Queue<T> queue, Subscriber<? super T> actual) {
        return postCompleteRequest(requested, n, queue, actual, UtilityFunctions.identity());
    }

    public static <T, R> void postCompleteDone(AtomicLong requested, Queue<T> queue, Subscriber<? super R> actual, Func1<? super T, ? extends R> exitTransform) {
        long r;
        do {
            r = requested.get();
            if ((r & COMPLETED_MASK) != 0) {
                return;
            }
        } while (!requested.compareAndSet(r, COMPLETED_MASK | r));
        if (r != 0) {
            postCompleteDrain(requested, queue, actual, exitTransform);
        }
    }

    public static <T, R> boolean postCompleteRequest(AtomicLong requested, long n, Queue<T> queue, Subscriber<? super R> actual, Func1<? super T, ? extends R> exitTransform) {
        long r;
        long c;
        AtomicLong atomicLong = requested;
        long j = n;
        if (j < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("n >= 0 required but it was ");
            sb.append(j);
            throw new IllegalArgumentException(sb.toString());
        }
        boolean z = true;
        if (j == 0) {
            if ((COMPLETED_MASK & requested.get()) != 0) {
                z = false;
            }
            return z;
        }
        while (true) {
            r = requested.get();
            c = r & COMPLETED_MASK;
            if (atomicLong.compareAndSet(r, addCap(REQUESTED_MASK & r, j) | c)) {
                break;
            }
            Queue<T> queue2 = queue;
        }
        if (r == COMPLETED_MASK) {
            postCompleteDrain(atomicLong, queue, actual, exitTransform);
            return false;
        }
        Queue<T> queue3 = queue;
        Subscriber<? super R> subscriber = actual;
        Func1<? super T, ? extends R> func1 = exitTransform;
        if (c != 0) {
            z = false;
        }
        return z;
    }

    static <T, R> void postCompleteDrain(AtomicLong requested, Queue<T> queue, Subscriber<? super R> subscriber, Func1<? super T, ? extends R> exitTransform) {
        long r = requested.get();
        if (r == REQUESTED_MASK) {
            while (!subscriber.isUnsubscribed()) {
                T v = queue.poll();
                if (v == null) {
                    subscriber.onCompleted();
                    return;
                }
                subscriber.onNext(exitTransform.call(v));
            }
            return;
        }
        long r2 = r;
        long e = Long.MIN_VALUE;
        while (true) {
            if (e == r2) {
                if (e == r2) {
                    if (!subscriber.isUnsubscribed()) {
                        if (queue.isEmpty()) {
                            subscriber.onCompleted();
                            return;
                        }
                    } else {
                        return;
                    }
                }
                r2 = requested.get();
                if (r2 == e) {
                    r2 = requested.addAndGet(-(e & REQUESTED_MASK));
                    if (r2 != COMPLETED_MASK) {
                        e = COMPLETED_MASK;
                    } else {
                        return;
                    }
                } else {
                    continue;
                }
            } else if (!subscriber.isUnsubscribed()) {
                T v2 = queue.poll();
                if (v2 == null) {
                    subscriber.onCompleted();
                    return;
                } else {
                    subscriber.onNext(exitTransform.call(v2));
                    e++;
                }
            } else {
                return;
            }
        }
    }

    public static long produced(AtomicLong requested, long n) {
        long current;
        long next;
        do {
            current = requested.get();
            if (current == REQUESTED_MASK) {
                return REQUESTED_MASK;
            }
            next = current - n;
            if (next < 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("More produced than requested: ");
                sb.append(next);
                throw new IllegalStateException(sb.toString());
            }
        } while (!requested.compareAndSet(current, next));
        return next;
    }

    public static boolean validate(long n) {
        if (n >= 0) {
            return n != 0;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("n >= 0 required but it was ");
        sb.append(n);
        throw new IllegalArgumentException(sb.toString());
    }
}
