package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscription;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.fuseable.FuseToFlowable;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableIgnoreElementsCompletable */
public final class FlowableIgnoreElementsCompletable<T> extends Completable implements FuseToFlowable<T> {
    final Flowable<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableIgnoreElementsCompletable$IgnoreElementsSubscriber */
    static final class IgnoreElementsSubscriber<T> implements FlowableSubscriber<T>, Disposable {
        final CompletableObserver actual;

        /* renamed from: s */
        Subscription f169s;

        IgnoreElementsSubscriber(CompletableObserver actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f169s, s)) {
                this.f169s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
        }

        public void onError(Throwable t) {
            this.f169s = SubscriptionHelper.CANCELLED;
            this.actual.onError(t);
        }

        public void onComplete() {
            this.f169s = SubscriptionHelper.CANCELLED;
            this.actual.onComplete();
        }

        public void dispose() {
            this.f169s.cancel();
            this.f169s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f169s == SubscriptionHelper.CANCELLED;
        }
    }

    public FlowableIgnoreElementsCompletable(Flowable<T> source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver t) {
        this.source.subscribe((FlowableSubscriber<? super T>) new IgnoreElementsSubscriber<Object>(t));
    }

    public Flowable<T> fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableIgnoreElements<T>(this.source));
    }
}
