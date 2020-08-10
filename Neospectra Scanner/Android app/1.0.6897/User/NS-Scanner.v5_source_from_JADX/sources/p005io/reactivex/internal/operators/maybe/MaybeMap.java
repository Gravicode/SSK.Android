package p005io.reactivex.internal.operators.maybe;

import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeMap */
public final class MaybeMap<T, R> extends AbstractMaybeWithUpstream<T, R> {
    final Function<? super T, ? extends R> mapper;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeMap$MapMaybeObserver */
    static final class MapMaybeObserver<T, R> implements MaybeObserver<T>, Disposable {
        final MaybeObserver<? super R> actual;

        /* renamed from: d */
        Disposable f262d;
        final Function<? super T, ? extends R> mapper;

        MapMaybeObserver(MaybeObserver<? super R> actual2, Function<? super T, ? extends R> mapper2) {
            this.actual = actual2;
            this.mapper = mapper2;
        }

        public void dispose() {
            Disposable d = this.f262d;
            this.f262d = DisposableHelper.DISPOSED;
            d.dispose();
        }

        public boolean isDisposed() {
            return this.f262d.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f262d, d)) {
                this.f262d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            try {
                this.actual.onSuccess(ObjectHelper.requireNonNull(this.mapper.apply(value), "The mapper returned a null item"));
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

    public MaybeMap(MaybeSource<T> source, Function<? super T, ? extends R> mapper2) {
        super(source);
        this.mapper = mapper2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super R> observer) {
        this.source.subscribe(new MapMaybeObserver(observer, this.mapper));
    }
}
