package p008rx.internal.operators;

import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;

/* renamed from: rx.internal.operators.OnSubscribeTakeLastOne */
public final class OnSubscribeTakeLastOne<T> implements OnSubscribe<T> {
    final Observable<T> source;

    /* renamed from: rx.internal.operators.OnSubscribeTakeLastOne$TakeLastOneSubscriber */
    static final class TakeLastOneSubscriber<T> extends DeferredScalarSubscriber<T, T> {
        static final Object EMPTY = new Object();

        public TakeLastOneSubscriber(Subscriber<? super T> actual) {
            super(actual);
            this.value = EMPTY;
        }

        public void onNext(T t) {
            this.value = t;
        }

        public void onCompleted() {
            Object o = this.value;
            if (o == EMPTY) {
                complete();
            } else {
                complete(o);
            }
        }
    }

    public OnSubscribeTakeLastOne(Observable<T> source2) {
        this.source = source2;
    }

    public void call(Subscriber<? super T> t) {
        new TakeLastOneSubscriber(t).subscribeTo(this.source);
    }
}
