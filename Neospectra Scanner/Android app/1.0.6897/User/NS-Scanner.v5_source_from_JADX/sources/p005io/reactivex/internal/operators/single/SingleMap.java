package p005io.reactivex.internal.operators.single;

import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.single.SingleMap */
public final class SingleMap<T, R> extends Single<R> {
    final Function<? super T, ? extends R> mapper;
    final SingleSource<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleMap$MapSingleObserver */
    static final class MapSingleObserver<T, R> implements SingleObserver<T> {
        final Function<? super T, ? extends R> mapper;

        /* renamed from: t */
        final SingleObserver<? super R> f420t;

        MapSingleObserver(SingleObserver<? super R> t, Function<? super T, ? extends R> mapper2) {
            this.f420t = t;
            this.mapper = mapper2;
        }

        public void onSubscribe(Disposable d) {
            this.f420t.onSubscribe(d);
        }

        public void onSuccess(T value) {
            try {
                this.f420t.onSuccess(ObjectHelper.requireNonNull(this.mapper.apply(value), "The mapper function returned a null value."));
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                onError(e);
            }
        }

        public void onError(Throwable e) {
            this.f420t.onError(e);
        }
    }

    public SingleMap(SingleSource<? extends T> source2, Function<? super T, ? extends R> mapper2) {
        this.source = source2;
        this.mapper = mapper2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super R> t) {
        this.source.subscribe(new MapSingleObserver(t, this.mapper));
    }
}
