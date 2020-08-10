package p005io.reactivex.internal.operators.maybe;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeFlatMapSingle */
public final class MaybeFlatMapSingle<T, R> extends Single<R> {
    final Function<? super T, ? extends SingleSource<? extends R>> mapper;
    final MaybeSource<T> source;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeFlatMapSingle$FlatMapMaybeObserver */
    static final class FlatMapMaybeObserver<T, R> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
        private static final long serialVersionUID = 4827726964688405508L;
        final SingleObserver<? super R> actual;
        final Function<? super T, ? extends SingleSource<? extends R>> mapper;

        FlatMapMaybeObserver(SingleObserver<? super R> actual2, Function<? super T, ? extends SingleSource<? extends R>> mapper2) {
            this.actual = actual2;
            this.mapper = mapper2;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.setOnce(this, d)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            try {
                SingleSource<? extends R> ss = (SingleSource) ObjectHelper.requireNonNull(this.mapper.apply(value), "The mapper returned a null SingleSource");
                if (!isDisposed()) {
                    ss.subscribe(new FlatMapSingleObserver(this, this.actual));
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                onError(ex);
            }
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void onComplete() {
            this.actual.onError(new NoSuchElementException());
        }
    }

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeFlatMapSingle$FlatMapSingleObserver */
    static final class FlatMapSingleObserver<R> implements SingleObserver<R> {
        final SingleObserver<? super R> actual;
        final AtomicReference<Disposable> parent;

        FlatMapSingleObserver(AtomicReference<Disposable> parent2, SingleObserver<? super R> actual2) {
            this.parent = parent2;
            this.actual = actual2;
        }

        public void onSubscribe(Disposable d) {
            DisposableHelper.replace(this.parent, d);
        }

        public void onSuccess(R value) {
            this.actual.onSuccess(value);
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }
    }

    public MaybeFlatMapSingle(MaybeSource<T> source2, Function<? super T, ? extends SingleSource<? extends R>> mapper2) {
        this.source = source2;
        this.mapper = mapper2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super R> actual) {
        this.source.subscribe(new FlatMapMaybeObserver(actual, this.mapper));
    }
}
