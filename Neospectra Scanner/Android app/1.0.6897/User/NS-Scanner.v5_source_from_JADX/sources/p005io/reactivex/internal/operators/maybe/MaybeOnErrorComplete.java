package p005io.reactivex.internal.operators.maybe;

import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeOnErrorComplete */
public final class MaybeOnErrorComplete<T> extends AbstractMaybeWithUpstream<T, T> {
    final Predicate<? super Throwable> predicate;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeOnErrorComplete$OnErrorCompleteMaybeObserver */
    static final class OnErrorCompleteMaybeObserver<T> implements MaybeObserver<T>, Disposable {
        final MaybeObserver<? super T> actual;

        /* renamed from: d */
        Disposable f263d;
        final Predicate<? super Throwable> predicate;

        OnErrorCompleteMaybeObserver(MaybeObserver<? super T> actual2, Predicate<? super Throwable> predicate2) {
            this.actual = actual2;
            this.predicate = predicate2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f263d, d)) {
                this.f263d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            this.actual.onSuccess(value);
        }

        public void onError(Throwable e) {
            try {
                if (this.predicate.test(e)) {
                    this.actual.onComplete();
                } else {
                    this.actual.onError(e);
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.actual.onError(new CompositeException(e, ex));
            }
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void dispose() {
            this.f263d.dispose();
        }

        public boolean isDisposed() {
            return this.f263d.isDisposed();
        }
    }

    public MaybeOnErrorComplete(MaybeSource<T> source, Predicate<? super Throwable> predicate2) {
        super(source);
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        this.source.subscribe(new OnErrorCompleteMaybeObserver(observer, this.predicate));
    }
}
