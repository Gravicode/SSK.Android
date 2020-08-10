package p008rx.internal.operators;

import p008rx.Completable.OnSubscribe;
import p008rx.CompletableSubscriber;
import p008rx.Subscription;
import p008rx.exceptions.AssemblyStackTraceException;

/* renamed from: rx.internal.operators.OnSubscribeOnAssemblyCompletable */
public final class OnSubscribeOnAssemblyCompletable<T> implements OnSubscribe {
    public static volatile boolean fullStackTrace;
    final OnSubscribe source;
    final String stacktrace = OnSubscribeOnAssembly.createStacktrace();

    /* renamed from: rx.internal.operators.OnSubscribeOnAssemblyCompletable$OnAssemblyCompletableSubscriber */
    static final class OnAssemblyCompletableSubscriber implements CompletableSubscriber {
        final CompletableSubscriber actual;
        final String stacktrace;

        public OnAssemblyCompletableSubscriber(CompletableSubscriber actual2, String stacktrace2) {
            this.actual = actual2;
            this.stacktrace = stacktrace2;
        }

        public void onSubscribe(Subscription d) {
            this.actual.onSubscribe(d);
        }

        public void onCompleted() {
            this.actual.onCompleted();
        }

        public void onError(Throwable e) {
            new AssemblyStackTraceException(this.stacktrace).attachTo(e);
            this.actual.onError(e);
        }
    }

    public OnSubscribeOnAssemblyCompletable(OnSubscribe source2) {
        this.source = source2;
    }

    public void call(CompletableSubscriber t) {
        this.source.call(new OnAssemblyCompletableSubscriber(t, this.stacktrace));
    }
}
