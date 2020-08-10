package p008rx.internal.operators;

import p008rx.Single;
import p008rx.Single.OnSubscribe;
import p008rx.SingleSubscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action0;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.SingleDoAfterTerminate */
public final class SingleDoAfterTerminate<T> implements OnSubscribe<T> {
    final Action0 action;
    final Single<T> source;

    /* renamed from: rx.internal.operators.SingleDoAfterTerminate$SingleDoAfterTerminateSubscriber */
    static final class SingleDoAfterTerminateSubscriber<T> extends SingleSubscriber<T> {
        final Action0 action;
        final SingleSubscriber<? super T> actual;

        public SingleDoAfterTerminateSubscriber(SingleSubscriber<? super T> actual2, Action0 action2) {
            this.actual = actual2;
            this.action = action2;
        }

        public void onSuccess(T value) {
            try {
                this.actual.onSuccess(value);
            } finally {
                doAction();
            }
        }

        public void onError(Throwable error) {
            try {
                this.actual.onError(error);
            } finally {
                doAction();
            }
        }

        /* access modifiers changed from: 0000 */
        public void doAction() {
            try {
                this.action.call();
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                RxJavaHooks.onError(ex);
            }
        }
    }

    public SingleDoAfterTerminate(Single<T> source2, Action0 action2) {
        this.source = source2;
        this.action = action2;
    }

    public void call(SingleSubscriber<? super T> t) {
        SingleDoAfterTerminateSubscriber<T> parent = new SingleDoAfterTerminateSubscriber<>(t, this.action);
        t.add(parent);
        this.source.subscribe((SingleSubscriber<? super T>) parent);
    }
}
