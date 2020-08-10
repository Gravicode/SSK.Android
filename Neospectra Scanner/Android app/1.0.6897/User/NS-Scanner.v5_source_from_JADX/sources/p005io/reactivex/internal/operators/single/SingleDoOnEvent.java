package p005io.reactivex.internal.operators.single;

import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiConsumer;

/* renamed from: io.reactivex.internal.operators.single.SingleDoOnEvent */
public final class SingleDoOnEvent<T> extends Single<T> {
    final BiConsumer<? super T, ? super Throwable> onEvent;
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleDoOnEvent$DoOnEvent */
    final class DoOnEvent implements SingleObserver<T> {

        /* renamed from: s */
        private final SingleObserver<? super T> f411s;

        DoOnEvent(SingleObserver<? super T> s) {
            this.f411s = s;
        }

        public void onSubscribe(Disposable d) {
            this.f411s.onSubscribe(d);
        }

        public void onSuccess(T value) {
            try {
                SingleDoOnEvent.this.onEvent.accept(value, null);
                this.f411s.onSuccess(value);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.f411s.onError(ex);
            }
        }

        public void onError(Throwable e) {
            try {
                SingleDoOnEvent.this.onEvent.accept(null, e);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                e = new CompositeException(e, ex);
            }
            this.f411s.onError(e);
        }
    }

    public SingleDoOnEvent(SingleSource<T> source2, BiConsumer<? super T, ? super Throwable> onEvent2) {
        this.source = source2;
        this.onEvent = onEvent2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        this.source.subscribe(new DoOnEvent(s));
    }
}
