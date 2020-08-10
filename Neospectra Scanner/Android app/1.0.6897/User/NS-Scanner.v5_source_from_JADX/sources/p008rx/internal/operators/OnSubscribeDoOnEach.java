package p008rx.internal.operators;

import java.util.Arrays;
import java.util.Collection;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.exceptions.CompositeException;
import p008rx.exceptions.Exceptions;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OnSubscribeDoOnEach */
public class OnSubscribeDoOnEach<T> implements OnSubscribe<T> {
    private final Observer<? super T> doOnEachObserver;
    private final Observable<T> source;

    /* renamed from: rx.internal.operators.OnSubscribeDoOnEach$DoOnEachSubscriber */
    private static final class DoOnEachSubscriber<T> extends Subscriber<T> {
        private final Observer<? super T> doOnEachObserver;
        private boolean done;
        private final Subscriber<? super T> subscriber;

        DoOnEachSubscriber(Subscriber<? super T> subscriber2, Observer<? super T> doOnEachObserver2) {
            super(subscriber2);
            this.subscriber = subscriber2;
            this.doOnEachObserver = doOnEachObserver2;
        }

        public void onCompleted() {
            if (!this.done) {
                try {
                    this.doOnEachObserver.onCompleted();
                    this.done = true;
                    this.subscriber.onCompleted();
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, (Observer<?>) this);
                }
            }
        }

        public void onError(Throwable e) {
            if (this.done) {
                RxJavaHooks.onError(e);
                return;
            }
            this.done = true;
            try {
                this.doOnEachObserver.onError(e);
                this.subscriber.onError(e);
            } catch (Throwable e2) {
                Exceptions.throwIfFatal(e2);
                this.subscriber.onError(new CompositeException((Collection<? extends Throwable>) Arrays.asList(new Throwable[]{e, e2})));
            }
        }

        public void onNext(T value) {
            if (!this.done) {
                try {
                    this.doOnEachObserver.onNext(value);
                    this.subscriber.onNext(value);
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, (Observer<?>) this, (Object) value);
                }
            }
        }
    }

    public OnSubscribeDoOnEach(Observable<T> source2, Observer<? super T> doOnEachObserver2) {
        this.source = source2;
        this.doOnEachObserver = doOnEachObserver2;
    }

    public void call(Subscriber<? super T> subscriber) {
        this.source.unsafeSubscribe(new DoOnEachSubscriber(subscriber, this.doOnEachObserver));
    }
}
