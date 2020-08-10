package p008rx.internal.operators;

import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action2;
import p008rx.functions.Func0;

/* renamed from: rx.internal.operators.OnSubscribeCollect */
public final class OnSubscribeCollect<T, R> implements OnSubscribe<R> {
    final Func0<R> collectionFactory;
    final Action2<R, ? super T> collector;
    final Observable<T> source;

    /* renamed from: rx.internal.operators.OnSubscribeCollect$CollectSubscriber */
    static final class CollectSubscriber<T, R> extends DeferredScalarSubscriberSafe<T, R> {
        final Action2<R, ? super T> collector;

        public CollectSubscriber(Subscriber<? super R> actual, R initialValue, Action2<R, ? super T> collector2) {
            super(actual);
            this.value = initialValue;
            this.hasValue = true;
            this.collector = collector2;
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    this.collector.call(this.value, t);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    unsubscribe();
                    onError(ex);
                }
            }
        }
    }

    public OnSubscribeCollect(Observable<T> source2, Func0<R> collectionFactory2, Action2<R, ? super T> collector2) {
        this.source = source2;
        this.collectionFactory = collectionFactory2;
        this.collector = collector2;
    }

    public void call(Subscriber<? super R> t) {
        try {
            new CollectSubscriber(t, this.collectionFactory.call(), this.collector).subscribeTo(this.source);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            t.onError(ex);
        }
    }
}
