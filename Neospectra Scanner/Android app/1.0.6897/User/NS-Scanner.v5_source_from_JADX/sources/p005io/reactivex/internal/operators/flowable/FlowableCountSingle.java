package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.fuseable.FuseToFlowable;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableCountSingle */
public final class FlowableCountSingle<T> extends Single<Long> implements FuseToFlowable<Long> {
    final Flowable<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableCountSingle$CountSubscriber */
    static final class CountSubscriber implements FlowableSubscriber<Object>, Disposable {
        final SingleObserver<? super Long> actual;
        long count;

        /* renamed from: s */
        Subscription f139s;

        CountSubscriber(SingleObserver<? super Long> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f139s, s)) {
                this.f139s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(Object t) {
            this.count++;
        }

        public void onError(Throwable t) {
            this.f139s = SubscriptionHelper.CANCELLED;
            this.actual.onError(t);
        }

        public void onComplete() {
            this.f139s = SubscriptionHelper.CANCELLED;
            this.actual.onSuccess(Long.valueOf(this.count));
        }

        public void dispose() {
            this.f139s.cancel();
            this.f139s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f139s == SubscriptionHelper.CANCELLED;
        }
    }

    public FlowableCountSingle(Flowable<T> source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super Long> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new CountSubscriber<Object>(s));
    }

    public Flowable<Long> fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableCount<T>(this.source));
    }
}
