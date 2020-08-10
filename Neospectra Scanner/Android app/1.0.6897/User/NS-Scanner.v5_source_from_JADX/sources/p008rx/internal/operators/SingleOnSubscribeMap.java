package p008rx.internal.operators;

import p008rx.Single;
import p008rx.Single.OnSubscribe;
import p008rx.SingleSubscriber;
import p008rx.exceptions.Exceptions;
import p008rx.exceptions.OnErrorThrowable;
import p008rx.functions.Func1;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.SingleOnSubscribeMap */
public final class SingleOnSubscribeMap<T, R> implements OnSubscribe<R> {
    final Single<T> source;
    final Func1<? super T, ? extends R> transformer;

    /* renamed from: rx.internal.operators.SingleOnSubscribeMap$MapSubscriber */
    static final class MapSubscriber<T, R> extends SingleSubscriber<T> {
        final SingleSubscriber<? super R> actual;
        boolean done;
        final Func1<? super T, ? extends R> mapper;

        public MapSubscriber(SingleSubscriber<? super R> actual2, Func1<? super T, ? extends R> mapper2) {
            this.actual = actual2;
            this.mapper = mapper2;
        }

        public void onSuccess(T t) {
            try {
                this.actual.onSuccess(this.mapper.call(t));
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
    }

    public SingleOnSubscribeMap(Single<T> source2, Func1<? super T, ? extends R> transformer2) {
        this.source = source2;
        this.transformer = transformer2;
    }

    public void call(SingleSubscriber<? super R> o) {
        MapSubscriber<T, R> parent = new MapSubscriber<>(o, this.transformer);
        o.add(parent);
        this.source.subscribe((SingleSubscriber<? super T>) parent);
    }
}
