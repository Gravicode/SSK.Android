package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.internal.fuseable.FuseToFlowable;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableAllSingle */
public final class FlowableAllSingle<T> extends Single<Boolean> implements FuseToFlowable<Boolean> {
    final Predicate<? super T> predicate;
    final Flowable<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableAllSingle$AllSubscriber */
    static final class AllSubscriber<T> implements FlowableSubscriber<T>, Disposable {
        final SingleObserver<? super Boolean> actual;
        boolean done;
        final Predicate<? super T> predicate;

        /* renamed from: s */
        Subscription f119s;

        AllSubscriber(SingleObserver<? super Boolean> actual2, Predicate<? super T> predicate2) {
            this.actual = actual2;
            this.predicate = predicate2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f119s, s)) {
                this.f119s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    if (!this.predicate.test(t)) {
                        this.done = true;
                        this.f119s.cancel();
                        this.f119s = SubscriptionHelper.CANCELLED;
                        this.actual.onSuccess(Boolean.valueOf(false));
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.f119s.cancel();
                    this.f119s = SubscriptionHelper.CANCELLED;
                    onError(e);
                }
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.f119s = SubscriptionHelper.CANCELLED;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.f119s = SubscriptionHelper.CANCELLED;
                this.actual.onSuccess(Boolean.valueOf(true));
            }
        }

        public void dispose() {
            this.f119s.cancel();
            this.f119s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f119s == SubscriptionHelper.CANCELLED;
        }
    }

    public FlowableAllSingle(Flowable<T> source2, Predicate<? super T> predicate2) {
        this.source = source2;
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super Boolean> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new AllSubscriber<Object>(s, this.predicate));
    }

    public Flowable<Boolean> fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableAll<T>(this.source, this.predicate));
    }
}
