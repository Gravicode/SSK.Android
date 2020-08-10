package p008rx.internal.operators;

import p008rx.Single.OnSubscribe;
import p008rx.SingleSubscriber;
import p008rx.exceptions.AssemblyStackTraceException;

/* renamed from: rx.internal.operators.OnSubscribeOnAssemblySingle */
public final class OnSubscribeOnAssemblySingle<T> implements OnSubscribe<T> {
    public static volatile boolean fullStackTrace;
    final OnSubscribe<T> source;
    final String stacktrace = OnSubscribeOnAssembly.createStacktrace();

    /* renamed from: rx.internal.operators.OnSubscribeOnAssemblySingle$OnAssemblySingleSubscriber */
    static final class OnAssemblySingleSubscriber<T> extends SingleSubscriber<T> {
        final SingleSubscriber<? super T> actual;
        final String stacktrace;

        public OnAssemblySingleSubscriber(SingleSubscriber<? super T> actual2, String stacktrace2) {
            this.actual = actual2;
            this.stacktrace = stacktrace2;
            actual2.add(this);
        }

        public void onError(Throwable e) {
            new AssemblyStackTraceException(this.stacktrace).attachTo(e);
            this.actual.onError(e);
        }

        public void onSuccess(T t) {
            this.actual.onSuccess(t);
        }
    }

    public OnSubscribeOnAssemblySingle(OnSubscribe<T> source2) {
        this.source = source2;
    }

    public void call(SingleSubscriber<? super T> t) {
        this.source.call(new OnAssemblySingleSubscriber(t, this.stacktrace));
    }
}
