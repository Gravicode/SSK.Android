package p008rx.internal.operators;

import p008rx.Single;
import p008rx.Single.OnSubscribe;
import p008rx.SingleSubscriber;
import p008rx.exceptions.CompositeException;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action1;

/* renamed from: rx.internal.operators.SingleDoOnEvent */
public final class SingleDoOnEvent<T> implements OnSubscribe<T> {
    final Action1<Throwable> onError;
    final Action1<? super T> onSuccess;
    final Single<T> source;

    /* renamed from: rx.internal.operators.SingleDoOnEvent$SingleDoOnEventSubscriber */
    static final class SingleDoOnEventSubscriber<T> extends SingleSubscriber<T> {
        final SingleSubscriber<? super T> actual;
        final Action1<Throwable> onError;
        final Action1<? super T> onSuccess;

        SingleDoOnEventSubscriber(SingleSubscriber<? super T> actual2, Action1<? super T> onSuccess2, Action1<Throwable> onError2) {
            this.actual = actual2;
            this.onSuccess = onSuccess2;
            this.onError = onError2;
        }

        public void onSuccess(T value) {
            try {
                this.onSuccess.call(value);
                this.actual.onSuccess(value);
            } catch (Throwable e) {
                Exceptions.throwOrReport(e, (SingleSubscriber<?>) this, (Object) value);
            }
        }

        public void onError(Throwable error) {
            try {
                this.onError.call(error);
                this.actual.onError(error);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                this.actual.onError(new CompositeException(error, e));
            }
        }
    }

    public SingleDoOnEvent(Single<T> source2, Action1<? super T> onSuccess2, Action1<Throwable> onError2) {
        this.source = source2;
        this.onSuccess = onSuccess2;
        this.onError = onError2;
    }

    public void call(SingleSubscriber<? super T> actual) {
        SingleDoOnEventSubscriber<T> parent = new SingleDoOnEventSubscriber<>(actual, this.onSuccess, this.onError);
        actual.add(parent);
        this.source.subscribe((SingleSubscriber<? super T>) parent);
    }
}
