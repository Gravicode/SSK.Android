package p005io.reactivex.internal.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.exceptions.CompositeException;

/* renamed from: io.reactivex.internal.util.ExceptionHelper */
public final class ExceptionHelper {
    public static final Throwable TERMINATED = new Termination();

    /* renamed from: io.reactivex.internal.util.ExceptionHelper$Termination */
    static final class Termination extends Throwable {
        private static final long serialVersionUID = -4649703670690200604L;

        Termination() {
            super("No further exceptions");
        }

        public Throwable fillInStackTrace() {
            return this;
        }
    }

    private ExceptionHelper() {
        throw new IllegalStateException("No instances!");
    }

    public static RuntimeException wrapOrThrow(Throwable error) {
        if (error instanceof Error) {
            throw ((Error) error);
        } else if (error instanceof RuntimeException) {
            return (RuntimeException) error;
        } else {
            return new RuntimeException(error);
        }
    }

    public static <T> boolean addThrowable(AtomicReference<Throwable> field, Throwable exception) {
        Throwable current;
        Throwable update;
        do {
            current = (Throwable) field.get();
            if (current == TERMINATED) {
                return false;
            }
            if (current == null) {
                update = exception;
            } else {
                update = new CompositeException(current, exception);
            }
        } while (!field.compareAndSet(current, update));
        return true;
    }

    public static <T> Throwable terminate(AtomicReference<Throwable> field) {
        Throwable current = (Throwable) field.get();
        if (current != TERMINATED) {
            return (Throwable) field.getAndSet(TERMINATED);
        }
        return current;
    }

    public static List<Throwable> flatten(Throwable t) {
        List<Throwable> list = new ArrayList<>();
        ArrayDeque<Throwable> deque = new ArrayDeque<>();
        deque.offer(t);
        while (!deque.isEmpty()) {
            Throwable e = (Throwable) deque.removeFirst();
            if (e instanceof CompositeException) {
                List<Throwable> exceptions = ((CompositeException) e).getExceptions();
                for (int i = exceptions.size() - 1; i >= 0; i--) {
                    deque.offerFirst(exceptions.get(i));
                }
            } else {
                list.add(e);
            }
        }
        return list;
    }

    public static <E extends Throwable> Exception throwIfThrowable(Throwable e) throws Throwable {
        if (e instanceof Exception) {
            return (Exception) e;
        }
        throw e;
    }
}
