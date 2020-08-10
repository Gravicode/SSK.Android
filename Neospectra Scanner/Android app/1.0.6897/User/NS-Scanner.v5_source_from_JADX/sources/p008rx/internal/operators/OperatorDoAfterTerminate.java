package p008rx.internal.operators;

import p008rx.Observable.Operator;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action0;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OperatorDoAfterTerminate */
public final class OperatorDoAfterTerminate<T> implements Operator<T, T> {
    final Action0 action;

    public OperatorDoAfterTerminate(Action0 action2) {
        if (action2 == null) {
            throw new NullPointerException("Action can not be null");
        }
        this.action = action2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        return new Subscriber<T>(child) {
            public void onNext(T t) {
                child.onNext(t);
            }

            public void onError(Throwable e) {
                try {
                    child.onError(e);
                } finally {
                    callAction();
                }
            }

            public void onCompleted() {
                try {
                    child.onCompleted();
                } finally {
                    callAction();
                }
            }

            /* access modifiers changed from: 0000 */
            public void callAction() {
                try {
                    OperatorDoAfterTerminate.this.action.call();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    RxJavaHooks.onError(ex);
                }
            }
        };
    }
}
