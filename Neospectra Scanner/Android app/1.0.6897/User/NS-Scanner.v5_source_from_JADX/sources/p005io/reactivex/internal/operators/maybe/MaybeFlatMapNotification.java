package p005io.reactivex.internal.operators.maybe;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeFlatMapNotification */
public final class MaybeFlatMapNotification<T, R> extends AbstractMaybeWithUpstream<T, R> {
    final Callable<? extends MaybeSource<? extends R>> onCompleteSupplier;
    final Function<? super Throwable, ? extends MaybeSource<? extends R>> onErrorMapper;
    final Function<? super T, ? extends MaybeSource<? extends R>> onSuccessMapper;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeFlatMapNotification$FlatMapMaybeObserver */
    static final class FlatMapMaybeObserver<T, R> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
        private static final long serialVersionUID = 4375739915521278546L;
        final MaybeObserver<? super R> actual;

        /* renamed from: d */
        Disposable f253d;
        final Callable<? extends MaybeSource<? extends R>> onCompleteSupplier;
        final Function<? super Throwable, ? extends MaybeSource<? extends R>> onErrorMapper;
        final Function<? super T, ? extends MaybeSource<? extends R>> onSuccessMapper;

        /* renamed from: io.reactivex.internal.operators.maybe.MaybeFlatMapNotification$FlatMapMaybeObserver$InnerObserver */
        final class InnerObserver implements MaybeObserver<R> {
            InnerObserver() {
            }

            public void onSubscribe(Disposable d) {
                DisposableHelper.setOnce(FlatMapMaybeObserver.this, d);
            }

            public void onSuccess(R value) {
                FlatMapMaybeObserver.this.actual.onSuccess(value);
            }

            public void onError(Throwable e) {
                FlatMapMaybeObserver.this.actual.onError(e);
            }

            public void onComplete() {
                FlatMapMaybeObserver.this.actual.onComplete();
            }
        }

        FlatMapMaybeObserver(MaybeObserver<? super R> actual2, Function<? super T, ? extends MaybeSource<? extends R>> onSuccessMapper2, Function<? super Throwable, ? extends MaybeSource<? extends R>> onErrorMapper2, Callable<? extends MaybeSource<? extends R>> onCompleteSupplier2) {
            this.actual = actual2;
            this.onSuccessMapper = onSuccessMapper2;
            this.onErrorMapper = onErrorMapper2;
            this.onCompleteSupplier = onCompleteSupplier2;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
            this.f253d.dispose();
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f253d, d)) {
                this.f253d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            try {
                ((MaybeSource) ObjectHelper.requireNonNull(this.onSuccessMapper.apply(value), "The onSuccessMapper returned a null MaybeSource")).subscribe(new InnerObserver());
            } catch (Exception ex) {
                Exceptions.throwIfFatal(ex);
                this.actual.onError(ex);
            }
        }

        public void onError(Throwable e) {
            try {
                ((MaybeSource) ObjectHelper.requireNonNull(this.onErrorMapper.apply(e), "The onErrorMapper returned a null MaybeSource")).subscribe(new InnerObserver());
            } catch (Exception ex) {
                Exceptions.throwIfFatal(ex);
                this.actual.onError(new CompositeException(e, ex));
            }
        }

        public void onComplete() {
            try {
                ((MaybeSource) ObjectHelper.requireNonNull(this.onCompleteSupplier.call(), "The onCompleteSupplier returned a null MaybeSource")).subscribe(new InnerObserver());
            } catch (Exception ex) {
                Exceptions.throwIfFatal(ex);
                this.actual.onError(ex);
            }
        }
    }

    public MaybeFlatMapNotification(MaybeSource<T> source, Function<? super T, ? extends MaybeSource<? extends R>> onSuccessMapper2, Function<? super Throwable, ? extends MaybeSource<? extends R>> onErrorMapper2, Callable<? extends MaybeSource<? extends R>> onCompleteSupplier2) {
        super(source);
        this.onSuccessMapper = onSuccessMapper2;
        this.onErrorMapper = onErrorMapper2;
        this.onCompleteSupplier = onCompleteSupplier2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super R> observer) {
        this.source.subscribe(new FlatMapMaybeObserver(observer, this.onSuccessMapper, this.onErrorMapper, this.onCompleteSupplier));
    }
}
