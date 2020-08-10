package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.fuseable.FuseToFlowable;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableSingleMaybe */
public final class FlowableSingleMaybe<T> extends Maybe<T> implements FuseToFlowable<T> {
    final Flowable<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSingleMaybe$SingleElementSubscriber */
    static final class SingleElementSubscriber<T> implements FlowableSubscriber<T>, Disposable {
        final MaybeObserver<? super T> actual;
        boolean done;

        /* renamed from: s */
        Subscription f201s;
        T value;

        SingleElementSubscriber(MaybeObserver<? super T> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f201s, s)) {
                this.f201s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.value != null) {
                    this.done = true;
                    this.f201s.cancel();
                    this.f201s = SubscriptionHelper.CANCELLED;
                    this.actual.onError(new IllegalArgumentException("Sequence contains more than one element!"));
                    return;
                }
                this.value = t;
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.f201s = SubscriptionHelper.CANCELLED;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.f201s = SubscriptionHelper.CANCELLED;
                T v = this.value;
                this.value = null;
                if (v == null) {
                    this.actual.onComplete();
                } else {
                    this.actual.onSuccess(v);
                }
            }
        }

        public void dispose() {
            this.f201s.cancel();
            this.f201s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f201s == SubscriptionHelper.CANCELLED;
        }
    }

    public FlowableSingleMaybe(Flowable<T> source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new SingleElementSubscriber<Object>(s));
    }

    public Flowable<T> fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSingle<T>(this.source, null, false));
    }
}
