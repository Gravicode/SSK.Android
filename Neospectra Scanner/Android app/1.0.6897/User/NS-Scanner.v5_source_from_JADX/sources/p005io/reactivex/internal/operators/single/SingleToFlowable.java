package p005io.reactivex.internal.operators.single;

import org.reactivestreams.Subscriber;
import p005io.reactivex.Flowable;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.subscriptions.DeferredScalarSubscription;

/* renamed from: io.reactivex.internal.operators.single.SingleToFlowable */
public final class SingleToFlowable<T> extends Flowable<T> {
    final SingleSource<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleToFlowable$SingleToFlowableObserver */
    static final class SingleToFlowableObserver<T> extends DeferredScalarSubscription<T> implements SingleObserver<T> {
        private static final long serialVersionUID = 187782011903685568L;

        /* renamed from: d */
        Disposable f421d;

        SingleToFlowableObserver(Subscriber<? super T> actual) {
            super(actual);
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f421d, d)) {
                this.f421d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            complete(value);
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void cancel() {
            super.cancel();
            this.f421d.dispose();
        }
    }

    public SingleToFlowable(SingleSource<? extends T> source2) {
        this.source = source2;
    }

    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe(new SingleToFlowableObserver(s));
    }
}
