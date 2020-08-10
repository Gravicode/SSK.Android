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

/* renamed from: io.reactivex.internal.operators.flowable.FlowableElementAtMaybe */
public final class FlowableElementAtMaybe<T> extends Maybe<T> implements FuseToFlowable<T> {
    final long index;
    final Flowable<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableElementAtMaybe$ElementAtSubscriber */
    static final class ElementAtSubscriber<T> implements FlowableSubscriber<T>, Disposable {
        final MaybeObserver<? super T> actual;
        long count;
        boolean done;
        final long index;

        /* renamed from: s */
        Subscription f155s;

        ElementAtSubscriber(MaybeObserver<? super T> actual2, long index2) {
            this.actual = actual2;
            this.index = index2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f155s, s)) {
                this.f155s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long c = this.count;
                if (c == this.index) {
                    this.done = true;
                    this.f155s.cancel();
                    this.f155s = SubscriptionHelper.CANCELLED;
                    this.actual.onSuccess(t);
                    return;
                }
                this.count = 1 + c;
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.f155s = SubscriptionHelper.CANCELLED;
            this.actual.onError(t);
        }

        public void onComplete() {
            this.f155s = SubscriptionHelper.CANCELLED;
            if (!this.done) {
                this.done = true;
                this.actual.onComplete();
            }
        }

        public void dispose() {
            this.f155s.cancel();
            this.f155s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f155s == SubscriptionHelper.CANCELLED;
        }
    }

    public FlowableElementAtMaybe(Flowable<T> source2, long index2) {
        this.source = source2;
        this.index = index2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new ElementAtSubscriber<Object>(s, this.index));
    }

    public Flowable<T> fuseToFlowable() {
        FlowableElementAt flowableElementAt = new FlowableElementAt(this.source, this.index, null, false);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableElementAt);
    }
}
