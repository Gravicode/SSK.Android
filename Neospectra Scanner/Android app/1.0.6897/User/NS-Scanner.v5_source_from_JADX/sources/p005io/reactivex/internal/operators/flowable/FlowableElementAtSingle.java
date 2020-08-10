package p005io.reactivex.internal.operators.flowable;

import java.util.NoSuchElementException;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.fuseable.FuseToFlowable;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableElementAtSingle */
public final class FlowableElementAtSingle<T> extends Single<T> implements FuseToFlowable<T> {
    final T defaultValue;
    final long index;
    final Flowable<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableElementAtSingle$ElementAtSubscriber */
    static final class ElementAtSubscriber<T> implements FlowableSubscriber<T>, Disposable {
        final SingleObserver<? super T> actual;
        long count;
        final T defaultValue;
        boolean done;
        final long index;

        /* renamed from: s */
        Subscription f156s;

        ElementAtSubscriber(SingleObserver<? super T> actual2, long index2, T defaultValue2) {
            this.actual = actual2;
            this.index = index2;
            this.defaultValue = defaultValue2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f156s, s)) {
                this.f156s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long c = this.count;
                if (c == this.index) {
                    this.done = true;
                    this.f156s.cancel();
                    this.f156s = SubscriptionHelper.CANCELLED;
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
            this.f156s = SubscriptionHelper.CANCELLED;
            this.actual.onError(t);
        }

        public void onComplete() {
            this.f156s = SubscriptionHelper.CANCELLED;
            if (!this.done) {
                this.done = true;
                T v = this.defaultValue;
                if (v != null) {
                    this.actual.onSuccess(v);
                } else {
                    this.actual.onError(new NoSuchElementException());
                }
            }
        }

        public void dispose() {
            this.f156s.cancel();
            this.f156s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f156s == SubscriptionHelper.CANCELLED;
        }
    }

    public FlowableElementAtSingle(Flowable<T> source2, long index2, T defaultValue2) {
        this.source = source2;
        this.index = index2;
        this.defaultValue = defaultValue2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new ElementAtSubscriber<Object>(s, this.index, this.defaultValue));
    }

    public Flowable<T> fuseToFlowable() {
        FlowableElementAt flowableElementAt = new FlowableElementAt(this.source, this.index, this.defaultValue, true);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableElementAt);
    }
}
