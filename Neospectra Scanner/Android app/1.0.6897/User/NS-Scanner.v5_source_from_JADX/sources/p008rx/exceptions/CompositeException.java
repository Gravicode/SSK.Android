package p008rx.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import p008rx.annotations.Beta;

/* renamed from: rx.exceptions.CompositeException */
public final class CompositeException extends RuntimeException {
    private static final long serialVersionUID = 3026362227162912146L;
    private Throwable cause;
    private final List<Throwable> exceptions;
    private final String message;

    /* renamed from: rx.exceptions.CompositeException$CompositeExceptionCausalChain */
    static final class CompositeExceptionCausalChain extends RuntimeException {
        static final String MESSAGE = "Chain of Causes for CompositeException In Order Received =>";
        private static final long serialVersionUID = 3875212506787802066L;

        CompositeExceptionCausalChain() {
        }

        public String getMessage() {
            return MESSAGE;
        }
    }

    /* renamed from: rx.exceptions.CompositeException$PrintStreamOrWriter */
    static abstract class PrintStreamOrWriter {
        /* access modifiers changed from: 0000 */
        public abstract Object lock();

        /* access modifiers changed from: 0000 */
        public abstract void println(Object obj);

        PrintStreamOrWriter() {
        }
    }

    /* renamed from: rx.exceptions.CompositeException$WrappedPrintStream */
    static final class WrappedPrintStream extends PrintStreamOrWriter {
        private final PrintStream printStream;

        WrappedPrintStream(PrintStream printStream2) {
            this.printStream = printStream2;
        }

        /* access modifiers changed from: 0000 */
        public Object lock() {
            return this.printStream;
        }

        /* access modifiers changed from: 0000 */
        public void println(Object o) {
            this.printStream.println(o);
        }
    }

    /* renamed from: rx.exceptions.CompositeException$WrappedPrintWriter */
    static final class WrappedPrintWriter extends PrintStreamOrWriter {
        private final PrintWriter printWriter;

        WrappedPrintWriter(PrintWriter printWriter2) {
            this.printWriter = printWriter2;
        }

        /* access modifiers changed from: 0000 */
        public Object lock() {
            return this.printWriter;
        }

        /* access modifiers changed from: 0000 */
        public void println(Object o) {
            this.printWriter.println(o);
        }
    }

    @Deprecated
    public CompositeException(String messagePrefix, Collection<? extends Throwable> errors) {
        Set<Throwable> deDupedExceptions = new LinkedHashSet<>();
        List<Throwable> localExceptions = new ArrayList<>();
        if (errors != null) {
            for (Throwable ex : errors) {
                if (ex instanceof CompositeException) {
                    deDupedExceptions.addAll(((CompositeException) ex).getExceptions());
                } else if (ex != null) {
                    deDupedExceptions.add(ex);
                } else {
                    deDupedExceptions.add(new NullPointerException());
                }
            }
        } else {
            deDupedExceptions.add(new NullPointerException());
        }
        localExceptions.addAll(deDupedExceptions);
        this.exceptions = Collections.unmodifiableList(localExceptions);
        StringBuilder sb = new StringBuilder();
        sb.append(this.exceptions.size());
        sb.append(" exceptions occurred. ");
        this.message = sb.toString();
    }

    public CompositeException(Collection<? extends Throwable> errors) {
        this(null, errors);
    }

    @Beta
    public CompositeException(Throwable... errors) {
        Throwable[] arr$;
        Set<Throwable> deDupedExceptions = new LinkedHashSet<>();
        List<Throwable> localExceptions = new ArrayList<>();
        if (errors != null) {
            for (Throwable ex : errors) {
                if (ex instanceof CompositeException) {
                    deDupedExceptions.addAll(((CompositeException) ex).getExceptions());
                } else if (ex != null) {
                    deDupedExceptions.add(ex);
                } else {
                    deDupedExceptions.add(new NullPointerException());
                }
            }
        } else {
            deDupedExceptions.add(new NullPointerException());
        }
        localExceptions.addAll(deDupedExceptions);
        this.exceptions = Collections.unmodifiableList(localExceptions);
        StringBuilder sb = new StringBuilder();
        sb.append(this.exceptions.size());
        sb.append(" exceptions occurred. ");
        this.message = sb.toString();
    }

    public List<Throwable> getExceptions() {
        return this.exceptions;
    }

    public String getMessage() {
        return this.message;
    }

    public synchronized Throwable getCause() {
        if (this.cause == null) {
            CompositeExceptionCausalChain localCause = new CompositeExceptionCausalChain();
            Set<Throwable> seenCauses = new HashSet<>();
            Throwable chain = localCause;
            for (Throwable e : this.exceptions) {
                if (!seenCauses.contains(e)) {
                    seenCauses.add(e);
                    for (Throwable child : getListOfCauses(e)) {
                        if (seenCauses.contains(child)) {
                            e = new RuntimeException("Duplicate found in causal chain so cropping to prevent loop ...");
                        } else {
                            seenCauses.add(child);
                        }
                    }
                    try {
                        chain.initCause(e);
                    } catch (Throwable th) {
                    }
                    chain = getRootCause(chain);
                }
            }
            this.cause = localCause;
        }
        return this.cause;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream s) {
        printStackTrace((PrintStreamOrWriter) new WrappedPrintStream(s));
    }

    public void printStackTrace(PrintWriter s) {
        printStackTrace((PrintStreamOrWriter) new WrappedPrintWriter(s));
    }

    private void printStackTrace(PrintStreamOrWriter s) {
        StackTraceElement[] arr$;
        StringBuilder b = new StringBuilder(128);
        b.append(this);
        b.append(10);
        for (StackTraceElement myStackElement : getStackTrace()) {
            b.append("\tat ");
            b.append(myStackElement);
            b.append(10);
        }
        int i = 1;
        for (Throwable ex : this.exceptions) {
            b.append("  ComposedException ");
            b.append(i);
            b.append(" :\n");
            appendStackTrace(b, ex, "\t");
            i++;
        }
        synchronized (s.lock()) {
            s.println(b.toString());
        }
    }

    private void appendStackTrace(StringBuilder b, Throwable ex, String prefix) {
        StackTraceElement[] arr$;
        b.append(prefix);
        b.append(ex);
        b.append(10);
        for (StackTraceElement stackElement : ex.getStackTrace()) {
            b.append("\t\tat ");
            b.append(stackElement);
            b.append(10);
        }
        if (ex.getCause() != null) {
            b.append("\tCaused by: ");
            appendStackTrace(b, ex.getCause(), "");
        }
    }

    private List<Throwable> getListOfCauses(Throwable ex) {
        List<Throwable> list = new ArrayList<>();
        Throwable root = ex.getCause();
        if (root == null || root == ex) {
            return list;
        }
        while (true) {
            list.add(root);
            Throwable cause2 = root.getCause();
            if (cause2 == null || cause2 == root) {
                return list;
            }
            root = root.getCause();
        }
        return list;
    }

    private Throwable getRootCause(Throwable e) {
        Throwable root = e.getCause();
        if (root == null || root == e) {
            return e;
        }
        while (true) {
            Throwable cause2 = root.getCause();
            if (cause2 == null || cause2 == root) {
                return root;
            }
            root = root.getCause();
        }
        return root;
    }
}
