package p005io.reactivex.internal.operators.maybe;

import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeFilter */
public final class MaybeFilter<T> extends AbstractMaybeWithUpstream<T, T> {
    final Predicate<? super T> predicate;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeFilter$FilterMaybeObserver */
    static final class FilterMaybeObserver<T> implements MaybeObserver<T>, Disposable {
        final MaybeObserver<? super T> actual;

        /* renamed from: d */
        Disposable f247d;
        final Predicate<? super T> predicate;

        FilterMaybeObserver(MaybeObserver<? super T> actual2, Predicate<? super T> predicate2) {
            this.actual = actual2;
            this.predicate = predicate2;
        }

        public void dispose() {
            Disposable d = this.f247d;
            this.f247d = DisposableHelper.DISPOSED;
            d.dispose();
        }

        public boolean isDisposed() {
            return this.f247d.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f247d, d)) {
                this.f247d = d;
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

        public void onComplete() {
            this.actual.onComplete();
        }
    }

    public MaybeFilter(MaybeSource<T> source, Predicate<? super T> predicate2) {
        super(source);
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        this.source.subscribe(new FilterMaybeObserver(observer, this.predicate));
    }
}
