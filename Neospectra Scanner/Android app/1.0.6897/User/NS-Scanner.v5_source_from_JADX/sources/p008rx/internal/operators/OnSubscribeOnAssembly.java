package p008rx.internal.operators;

import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.exceptions.AssemblyStackTraceException;

/* renamed from: rx.internal.operators.OnSubscribeOnAssembly */
public final class OnSubscribeOnAssembly<T> implements OnSubscribe<T> {
    public static volatile boolean fullStackTrace;
    final OnSubscribe<T> source;
    final String stacktrace = createStacktrace();

    /* renamed from: rx.internal.operators.OnSubscribeOnAssembly$OnAssemblySubscriber */
    static final class OnAssemblySubscriber<T> extends Subscriber<T> {
        final Subscriber<? super T> actual;
        final String stacktrace;

        public OnAssemblySubscriber(Subscriber<? super T> actual2, String stacktrace2) {
            super(actual2);
            this.actual = actual2;
            this.stacktrace = stacktrace2;
        }

        public void onCompleted() {
            this.actual.onCompleted();
        }

        public void onError(Throwable e) {
            new AssemblyStackTraceException(this.stacktrace).attachTo(e);
            this.actual.onError(e);
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }
    }

    public OnSubscribeOnAssembly(OnSubscribe<T> source2) {
        this.source = source2;
    }

    static String createStacktrace() {
        StackTraceElement[] arr$;
        StackTraceElement[] stacktraceElements = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder("Assembly trace:");
        for (StackTraceElement e : stacktraceElements) {
            String row = e.toString();
            if (fullStackTrace || (e.getLineNumber() > 1 && !row.contains("RxJavaHooks.") && !row.contains("OnSubscribeOnAssembly") && !row.contains(".junit.runner") && !row.contains(".junit4.runner") && !row.contains(".junit.internal") && !row.contains("sun.reflect") && !row.contains("java.lang.Thread.") && !row.contains("ThreadPoolExecutor") && !row.contains("org.apache.catalina.") && !row.contains("org.apache.tomcat."))) {
                sb.append("\n at ");
                sb.append(row);
            }
        }
        sb.append("\nOriginal exception:");
        return sb.toString();
    }

    public void call(Subscriber<? super T> t) {
        this.source.call(new OnAssemblySubscriber(t, this.stacktrace));
    }
}
