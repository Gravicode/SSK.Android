package p008rx.internal.operators;

import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.exceptions.OnErrorThrowable;
import p008rx.functions.Func1;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OnSubscribeMap */
public final class OnSubscribeMap<T, R> implements OnSubscribe<R> {
    final Observable<T> source;
    final Func1<? super T, ? extends R> transformer;

    /* renamed from: rx.internal.operators.OnSubscribeMap$MapSubscriber */
    static final class MapSubscriber<T, R> extends Subscriber<T> {
        final Subscriber<? super R> actual;
        boolean done;
        final Func1<? super T, ? extends R> mapper;

        public MapSubscriber(Subscriber<? super R> actual2, Func1<? super T, ? extends R> mapper2) {
            this.actual = actual2;
            this.mapper = mapper2;
        }

        public void onNext(T t) {
            try {
                this.actual.onNext(this.mapper.call(t));
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                unsubscribe();
                onError(OnErrorThrowable.addValueAsLastCause(ex, t));
            }
        }

        public void onError(Throwable e) {
            if (this.done) {
                RxJavaHooks.onError(e);
                return;
            }
            this.done = true;
            this.actual.onError(e);
        }

        public void onCompleted() {
            if (!this.done) {
                this.actual.onCompleted();
            }
        }

        public void setProducer(Producer p) {
            this.actual.setProducer(p);
        }
    }

    public OnSubscribeMap(Observable<T> source2, Func1<? super T, ? extends R> transformer2) {
        this.source = source2;
        this.transformer = transformer2;
    }

    public void call(Subscriber<? super R> o) {
        MapSubscriber<T, R> parent = new MapSubscriber<>(o, this.transformer);
        o.add(parent);
        this.source.unsafeSubscribe(parent);
    }
}
