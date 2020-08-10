package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Notification;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableDematerialize */
public final class FlowableDematerialize<T> extends AbstractFlowableWithUpstream<Notification<T>, T> {

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDematerialize$DematerializeSubscriber */
    static final class DematerializeSubscriber<T> implements FlowableSubscriber<Notification<T>>, Subscription {
        final Subscriber<? super T> actual;
        boolean done;

        /* renamed from: s */
        Subscription f147s;

        DematerializeSubscriber(Subscriber<? super T> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f147s, s)) {
                this.f147s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(Notification<T> t) {
            if (this.done) {
                if (t.isOnError()) {
                    RxJavaPlugins.onError(t.getError());
                }
                return;
            }
            if (t.isOnError()) {
                this.f147s.cancel();
                onError(t.getError());
            } else if (t.isOnComplete()) {
                this.f147s.cancel();
                onComplete();
            } else {
                this.actual.onNext(t.getValue());
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onComplete();
            }
        }

        public void request(long n) {
            this.f147s.request(n);
        }

        public void cancel() {
            this.f147s.cancel();
        }
    }

    public FlowableDematerialize(Flowable<Notification<T>> source) {
        super(source);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new DematerializeSubscriber<Object>(s));
    }
}
