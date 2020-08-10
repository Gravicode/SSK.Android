package p008rx.internal.operators;

import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func2;

/* renamed from: rx.internal.operators.OnSubscribeReduceSeed */
public final class OnSubscribeReduceSeed<T, R> implements OnSubscribe<R> {
    final R initialValue;
    final Func2<R, ? super T, R> reducer;
    final Observable<T> source;

    /* renamed from: rx.internal.operators.OnSubscribeReduceSeed$ReduceSeedSubscriber */
    static final class ReduceSeedSubscriber<T, R> extends DeferredScalarSubscriber<T, R> {
        final Func2<R, ? super T, R> reducer;

        public ReduceSeedSubscriber(Subscriber<? super R> actual, R initialValue, Func2<R, ? super T, R> reducer2) {
            super(actual);
            this.value = initialValue;
            this.hasValue = true;
            this.reducer = reducer2;
        }

        public void onNext(T t) {
            try {
                this.value = this.reducer.call(this.value, t);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                unsubscribe();
                this.actual.onError(ex);
            }
        }
    }

    public OnSubscribeReduceSeed(Observable<T> source2, R initialValue2, Func2<R, ? super T, R> reducer2) {
        this.source = source2;
        this.initialValue = initialValue2;
        this.reducer = reducer2;
    }

    public void call(Subscriber<? super R> t) {
        new ReduceSeedSubscriber(t, this.initialValue, this.reducer).subscribeTo(this.source);
    }
}
