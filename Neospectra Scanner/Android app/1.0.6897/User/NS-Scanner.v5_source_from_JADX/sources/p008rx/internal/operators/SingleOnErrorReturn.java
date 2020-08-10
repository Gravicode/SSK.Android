package p008rx.internal.operators;

import p008rx.Single.OnSubscribe;
import p008rx.SingleSubscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func1;

/* renamed from: rx.internal.operators.SingleOnErrorReturn */
public final class SingleOnErrorReturn<T> implements OnSubscribe<T> {
    final Func1<Throwable, ? extends T> resumeFunction;
    final OnSubscribe<T> source;

    /* renamed from: rx.internal.operators.SingleOnErrorReturn$OnErrorReturnsSingleSubscriber */
    static final class OnErrorReturnsSingleSubscriber<T> extends SingleSubscriber<T> {
        final SingleSubscriber<? super T> actual;
        final Func1<Throwable, ? extends T> resumeFunction;

        public OnErrorReturnsSingleSubscriber(SingleSubscriber<? super T> actual2, Func1<Throwable, ? extends T> resumeFunction2) {
            this.actual = actual2;
            this.resumeFunction = resumeFunction2;
        }

        public void onSuccess(T value) {
            this.actual.onSuccess(value);
        }

        public void onError(Throwable error) {
            try {
                this.actual.onSuccess(this.resumeFunction.call(error));
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.actual.onError(ex);
            }
        }
    }

    public SingleOnErrorReturn(OnSubscribe<T> source2, Func1<Throwable, ? extends T> resumeFunction2) {
        this.source = source2;
        this.resumeFunction = resumeFunction2;
    }

    public void call(SingleSubscriber<? super T> t) {
        OnErrorReturnsSingleSubscriber<T> parent = new OnErrorReturnsSingleSubscriber<>(t, this.resumeFunction);
        t.add(parent);
        this.source.call(parent);
    }
}
