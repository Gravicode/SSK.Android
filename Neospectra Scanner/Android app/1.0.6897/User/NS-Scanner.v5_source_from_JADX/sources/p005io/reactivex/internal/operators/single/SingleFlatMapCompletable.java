package p005io.reactivex.internal.operators.single;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.single.SingleFlatMapCompletable */
public final class SingleFlatMapCompletable<T> extends Completable {
    final Function<? super T, ? extends CompletableSource> mapper;
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleFlatMapCompletable$FlatMapCompletableObserver */
    static final class FlatMapCompletableObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T>, CompletableObserver, Disposable {
        private static final long serialVersionUID = -2177128922851101253L;
        final CompletableObserver actual;
        final Function<? super T, ? extends CompletableSource> mapper;

        FlatMapCompletableObserver(CompletableObserver actual2, Function<? super T, ? extends CompletableSource> mapper2) {
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
            DisposableHelper.replace(this, d);
        }

        public void onSuccess(T value) {
            try {
                CompletableSource cs = (CompletableSource) ObjectHelper.requireNonNull(this.mapper.apply(value), "The mapper returned a null CompletableSource");
                if (!isDisposed()) {
                    cs.subscribe(this);
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
            this.actual.onComplete();
        }
    }

    public SingleFlatMapCompletable(SingleSource<T> source2, Function<? super T, ? extends CompletableSource> mapper2) {
        this.source = source2;
        this.mapper = mapper2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        FlatMapCompletableObserver<T> parent = new FlatMapCompletableObserver<>(s, this.mapper);
        s.onSubscribe(parent);
        this.source.subscribe(parent);
    }
}
