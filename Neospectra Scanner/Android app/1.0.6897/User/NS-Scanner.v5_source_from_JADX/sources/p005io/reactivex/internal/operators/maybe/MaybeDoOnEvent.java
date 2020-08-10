package p005io.reactivex.internal.operators.maybe;

import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiConsumer;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeDoOnEvent */
public final class MaybeDoOnEvent<T> extends AbstractMaybeWithUpstream<T, T> {
    final BiConsumer<? super T, ? super Throwable> onEvent;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeDoOnEvent$DoOnEventMaybeObserver */
    static final class DoOnEventMaybeObserver<T> implements MaybeObserver<T>, Disposable {
        final MaybeObserver<? super T> actual;

        /* renamed from: d */
        Disposable f246d;
        final BiConsumer<? super T, ? super Throwable> onEvent;

        DoOnEventMaybeObserver(MaybeObserver<? super T> actual2, BiConsumer<? super T, ? super Throwable> onEvent2) {
            this.actual = actual2;
            this.onEvent = onEvent2;
        }

        public void dispose() {
            this.f246d.dispose();
            this.f246d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.f246d.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f246d, d)) {
                this.f246d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            this.f246d = DisposableHelper.DISPOSED;
            try {
                this.onEvent.accept(value, null);
                this.actual.onSuccess(value);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.actual.onError(ex);
            }
        }

        public void onError(Throwable e) {
            this.f246d = DisposableHelper.DISPOSED;
            try {
                this.onEvent.accept(null, e);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                e = new CompositeException(e, ex);
            }
            this.actual.onError(e);
        }

        public void onComplete() {
            this.f246d = DisposableHelper.DISPOSED;
            try {
                this.onEvent.accept(null, null);
                this.actual.onComplete();
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.actual.onError(ex);
            }
        }
    }

    public MaybeDoOnEvent(MaybeSource<T> source, BiConsumer<? super T, ? super Throwable> onEvent2) {
        super(source);
        this.onEvent = onEvent2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        this.source.subscribe(new DoOnEventMaybeObserver(observer, this.onEvent));
    }
}
