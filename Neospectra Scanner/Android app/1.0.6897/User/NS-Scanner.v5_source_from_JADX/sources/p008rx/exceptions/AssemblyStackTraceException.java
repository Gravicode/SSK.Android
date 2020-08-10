package p008rx.exceptions;

import java.util.HashSet;
import java.util.Set;
import p008rx.annotations.Experimental;
import p008rx.plugins.RxJavaHooks;

@Experimental
/* renamed from: rx.exceptions.AssemblyStackTraceException */
public final class AssemblyStackTraceException extends RuntimeException {
    private static final long serialVersionUID = 2038859767182585852L;

    public AssemblyStackTraceException(String message) {
        super(message);
    }

    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public void attachTo(Throwable exception) {
        Set<Throwable> memory = new HashSet<>();
        while (exception.getCause() != null) {
            exception = exception.getCause();
            if (!memory.add(exception)) {
                RxJavaHooks.onError(this);
                return;
            }
        }
        exception.initCause(this);
    }

    public static AssemblyStackTraceException find(Throwable e) {
        Set<Throwable> memory = new HashSet<>();
        while (!(e instanceof AssemblyStackTraceException)) {
            if (e == null || e.getCause() == null) {
                return null;
            }
            e = e.getCause();
            if (!memory.add(e)) {
                return null;
            }
        }
        return (AssemblyStackTraceException) e;
    }
}
