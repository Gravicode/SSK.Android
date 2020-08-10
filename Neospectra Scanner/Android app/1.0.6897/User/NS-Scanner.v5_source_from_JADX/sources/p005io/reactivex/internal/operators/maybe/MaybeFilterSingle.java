package p005io.reactivex.internal.operators.maybe;

import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeFilterSingle */
public final class MaybeFilterSingle<T> extends Maybe<T> {
    final Predicate<? super T> predicate;
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeFilterSingle$FilterMaybeObserver */
    static final class FilterMaybeObserver<T> implements SingleObserver<T>, Disposable {
        final MaybeObserver<? super T> actual;

        /* renamed from: d */
        Disposable f248d;
        final Predicate<? super T> predicate;

        FilterMaybeObserver(MaybeObserver<? super T> actual2, Predicate<? super T> predicate2) {
            this.actual = actual2;
            this.predicate = predicate2;
        }

        public void dispose() {
            Disposable d = this.f248d;
            this.f248d = DisposableHelper.DISPOSED;
            d.dispose();
        }

        public boolean isDisposed() {
            return this.f248d.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f248d, d)) {
                this.f248d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            try {
                if (this.predicate.test(value)) {
                    this.actual.onSuccess(value);
                } else {
                    this.actual.onComplete();
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.actual.onError(ex);
            }
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }
    }

    public MaybeFilterSingle(SingleSource<T> source2, Predicate<? super T> predicate2) {
        this.source = source2;
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        this.source.subscribe(new FilterMaybeObserver(observer, this.predicate));
    }
}
