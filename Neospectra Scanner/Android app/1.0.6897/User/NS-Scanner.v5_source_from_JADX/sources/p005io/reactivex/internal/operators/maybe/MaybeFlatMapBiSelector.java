package p005io.reactivex.internal.operators.maybe;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeFlatMapBiSelector */
public final class MaybeFlatMapBiSelector<T, U, R> extends AbstractMaybeWithUpstream<T, R> {
    final Function<? super T, ? extends MaybeSource<? extends U>> mapper;
    final BiFunction<? super T, ? super U, ? extends R> resultSelector;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeFlatMapBiSelector$FlatMapBiMainObserver */
    static final class FlatMapBiMainObserver<T, U, R> implements MaybeObserver<T>, Disposable {
        final InnerObserver<T, U, R> inner;
        final Function<? super T, ? extends MaybeSource<? extends U>> mapper;

        /* renamed from: io.reactivex.internal.operators.maybe.MaybeFlatMapBiSelector$FlatMapBiMainObserver$InnerObserver */
        static final class InnerObserver<T, U, R> extends AtomicReference<Disposable> implements MaybeObserver<U> {
            private static final long serialVersionUID = -2897979525538174559L;
            final MaybeObserver<? super R> actual;
            final BiFunction<? super T, ? super U, ? extends R> resultSelector;
            T value;

            InnerObserver(MaybeObserver<? super R> actual2, BiFunction<? super T, ? super U, ? extends R> resultSelector2) {
                this.actual = actual2;
                this.resultSelector = resultSelector2;
            }

            public void onSubscribe(Disposable d) {
                DisposableHelper.setOnce(this, d);
            }

            public void onSuccess(U value2) {
                T t = this.value;
                this.value = null;
                try {
                    this.actual.onSuccess(ObjectHelper.requireNonNull(this.resultSelector.apply(t, value2), "The resultSelector returned a null value"));
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    this.actual.onError(ex);
                }
            }

            public void onError(Throwable e) {
                this.actual.onError(e);
            }

            public void onComplete() {
                this.actual.onComplete();
            }
        }

        FlatMapBiMainObserver(MaybeObserver<? super R> actual, Function<? super T, ? extends MaybeSource<? extends U>> mapper2, BiFunction<? super T, ? super U, ? extends R> resultSelector) {
            this.inner = new InnerObserver<>(actual, resultSelector);
            this.mapper = mapper2;
        }

        public void dispose() {
            DisposableHelper.dispose(this.inner);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) this.inner.get());
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.setOnce(this.inner, d)) {
                this.inner.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            try {
                MaybeSource<? extends U> next = (MaybeSource) ObjectHelper.requireNonNull(this.mapper.apply(value), "The mapper returned a null MaybeSource");
                if (DisposableHelper.replace(this.inner, null)) {
                    this.inner.value = value;
                    next.subscribe(this.inner);
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.inner.actual.onError(ex);
            }
        }

        public void onError(Throwable e) {
            this.inner.actual.onError(e);
        }

        public void onComplete() {
            this.inner.actual.onComplete();
        }
    }

    public MaybeFlatMapBiSelector(MaybeSource<T> source, Function<? super T, ? extends MaybeSource<? extends U>> mapper2, BiFunction<? super T, ? super U, ? extends R> resultSelector2) {
        super(source);
        this.mapper = mapper2;
        this.resultSelector = resultSelector2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super R> observer) {
        this.source.subscribe(new FlatMapBiMainObserver(observer, this.mapper, this.resultSelector));
    }
}
