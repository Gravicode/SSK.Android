package p008rx.internal.operators;

import p008rx.Observable;
import p008rx.Observer;
import p008rx.Scheduler.Worker;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func0;
import p008rx.functions.Func1;
import p008rx.schedulers.Schedulers;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.OperatorTimeoutWithSelector */
public class OperatorTimeoutWithSelector<T, U, V> extends OperatorTimeoutBase<T> {
    public /* bridge */ /* synthetic */ Subscriber call(Subscriber x0) {
        return super.call(x0);
    }

    public OperatorTimeoutWithSelector(final Func0<? extends Observable<U>> firstTimeoutSelector, final Func1<? super T, ? extends Observable<V>> timeoutSelector, Observable<? extends T> other) {
        super(new FirstTimeoutStub<T>() {
            public Subscription call(final TimeoutSubscriber<T> timeoutSubscriber, final Long seqId, Worker inner) {
                if (Func0.this == null) {
                    return Subscriptions.unsubscribed();
                }
                try {
                    return ((Observable) Func0.this.call()).unsafeSubscribe(new Subscriber<U>() {
                        public void onCompleted() {
                            timeoutSubscriber.onTimeout(seqId.longValue());
                        }

                        public void onError(Throwable e) {
                            timeoutSubscriber.onError(e);
                        }

                        public void onNext(U u) {
                            timeoutSubscriber.onTimeout(seqId.longValue());
                        }
                    });
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, (Observer<?>) timeoutSubscriber);
                    return Subscriptions.unsubscribed();
                }
            }
        }, new TimeoutStub<T>() {
            public Subscription call(final TimeoutSubscriber<T> timeoutSubscriber, final Long seqId, T value, Worker inner) {
                try {
                    return ((Observable) Func1.this.call(value)).unsafeSubscribe(new Subscriber<V>() {
                        public void onCompleted() {
                            timeoutSubscriber.onTimeout(seqId.longValue());
                        }

                        public void onError(Throwable e) {
                            timeoutSubscriber.onError(e);
                        }

                        public void onNext(V v) {
                            timeoutSubscriber.onTimeout(seqId.longValue());
                        }
                    });
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, (Observer<?>) timeoutSubscriber);
                    return Subscriptions.unsubscribed();
                }
            }
        }, other, Schedulers.immediate());
    }
}
