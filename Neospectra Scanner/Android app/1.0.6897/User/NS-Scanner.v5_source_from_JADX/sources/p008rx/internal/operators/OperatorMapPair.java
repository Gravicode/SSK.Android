package p008rx.internal.operators;

import p008rx.Observable;
import p008rx.Observable.Operator;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.exceptions.OnErrorThrowable;
import p008rx.functions.Func1;
import p008rx.functions.Func2;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OperatorMapPair */
public final class OperatorMapPair<T, U, R> implements Operator<Observable<? extends R>, T> {
    final Func1<? super T, ? extends Observable<? extends U>> collectionSelector;
    final Func2<? super T, ? super U, ? extends R> resultSelector;

    /* renamed from: rx.internal.operators.OperatorMapPair$MapPairSubscriber */
    static final class MapPairSubscriber<T, U, R> extends Subscriber<T> {
        final Subscriber<? super Observable<? extends R>> actual;
        final Func1<? super T, ? extends Observable<? extends U>> collectionSelector;
        boolean done;
        final Func2<? super T, ? super U, ? extends R> resultSelector;

        public MapPairSubscriber(Subscriber<? super Observable<? extends R>> actual2, Func1<? super T, ? extends Observable<? extends U>> collectionSelector2, Func2<? super T, ? super U, ? extends R> resultSelector2) {
            this.actual = actual2;
            this.collectionSelector = collectionSelector2;
            this.resultSelector = resultSelector2;
        }

        public void onNext(T outer) {
            try {
                this.actual.onNext(((Observable) this.collectionSelector.call(outer)).map(new OuterInnerMapper(outer, this.resultSelector)));
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                unsubscribe();
                onError(OnErrorThrowable.addValueAsLastCause(ex, outer));
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

    /* renamed from: rx.internal.operators.OperatorMapPair$OuterInnerMapper */
    static final class OuterInnerMapper<T, U, R> implements Func1<U, R> {
        final T outer;
        final Func2<? super T, ? super U, ? extends R> resultSelector;

        public OuterInnerMapper(T outer2, Func2<? super T, ? super U, ? extends R> resultSelector2) {
            this.outer = outer2;
            this.resultSelector = resultSelector2;
        }

        public R call(U inner) {
            return this.resultSelector.call(this.outer, inner);
        }
    }

    public static <T, U> Func1<T, Observable<U>> convertSelector(final Func1<? super T, ? extends Iterable<? extends U>> selector) {
        return new Func1<T, Observable<U>>() {
            public Observable<U> call(T t1) {
                return Observable.from((Iterable) selector.call(t1));
            }
        };
    }

    public OperatorMapPair(Func1<? super T, ? extends Observable<? extends U>> collectionSelector2, Func2<? super T, ? super U, ? extends R> resultSelector2) {
        this.collectionSelector = collectionSelector2;
        this.resultSelector = resultSelector2;
    }

    public Subscriber<? super T> call(Subscriber<? super Observable<? extends R>> o) {
        MapPairSubscriber<T, U, R> parent = new MapPairSubscriber<>(o, this.collectionSelector, this.resultSelector);
        o.add(parent);
        return parent;
    }
}
