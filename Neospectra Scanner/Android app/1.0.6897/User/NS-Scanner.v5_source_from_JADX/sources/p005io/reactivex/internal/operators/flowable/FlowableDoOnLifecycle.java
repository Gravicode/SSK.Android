package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Action;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.functions.LongConsumer;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableDoOnLifecycle */
public final class FlowableDoOnLifecycle<T> extends AbstractFlowableWithUpstream<T, T> {
    private final Action onCancel;
    private final LongConsumer onRequest;
    private final Consumer<? super Subscription> onSubscribe;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDoOnLifecycle$SubscriptionLambdaSubscriber */
    static final class SubscriptionLambdaSubscriber<T> implements FlowableSubscriber<T>, Subscription {
        final Subscriber<? super T> actual;
        final Action onCancel;
        final LongConsumer onRequest;
        final Consumer<? super Subscription> onSubscribe;

        /* renamed from: s */
        Subscription f153s;

        SubscriptionLambdaSubscriber(Subscriber<? super T> actual2, Consumer<? super Subscription> onSubscribe2, LongConsumer onRequest2, Action onCancel2) {
            this.actual = actual2;
            this.onSubscribe = onSubscribe2;
            this.onCancel = onCancel2;
            this.onRequest = onRequest2;
        }

        public void onSubscribe(Subscription s) {
            try {
                this.onSubscribe.accept(s);
                if (SubscriptionHelper.validate(this.f153s, s)) {
                    this.f153s = s;
                    this.actual.onSubscribe(this);
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                s.cancel();
                this.f153s = SubscriptionHelper.CANCELLED;
                EmptySubscription.error(e, this.actual);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            if (this.f153s != SubscriptionHelper.CANCELLED) {
                this.actual.onError(t);
            } else {
                RxJavaPlugins.onError(t);
            }
        }

        public void onComplete() {
            if (this.f153s != SubscriptionHelper.CANCELLED) {
                this.actual.onComplete();
            }
        }

        public void request(long n) {
            try {
                this.onRequest.accept(n);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(e);
            }
            this.f153s.request(n);
        }

        public void cancel() {
            try {
                this.onCancel.run();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(e);
            }
            this.f153s.cancel();
        }
    }

    public FlowableDoOnLifecycle(Flowable<T> source, Consumer<? super Subscription> onSubscribe2, LongConsumer onRequest2, Action onCancel2) {
        super(source);
        this.onSubscribe = onSubscribe2;
        this.onRequest = onRequest2;
        this.onCancel = onCancel2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new SubscriptionLambdaSubscriber<Object>(s, this.onSubscribe, this.onRequest, this.onCancel));
    }
}
