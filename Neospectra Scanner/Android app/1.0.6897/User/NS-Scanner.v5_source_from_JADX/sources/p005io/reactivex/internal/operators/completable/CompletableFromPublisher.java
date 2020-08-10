package p005io.reactivex.internal.operators.completable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.completable.CompletableFromPublisher */
public final class CompletableFromPublisher<T> extends Completable {
    final Publisher<T> flowable;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableFromPublisher$FromPublisherSubscriber */
    static final class FromPublisherSubscriber<T> implements FlowableSubscriber<T>, Disposable {

        /* renamed from: cs */
        final CompletableObserver f106cs;

        /* renamed from: s */
        Subscription f107s;

        FromPublisherSubscriber(CompletableObserver actual) {
            this.f106cs = actual;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f107s, s)) {
                this.f107s = s;
                this.f106cs.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
        }

        public void onError(Throwable t) {
            this.f106cs.onError(t);
        }

        public void onComplete() {
            this.f106cs.onComplete();
        }

        public void dispose() {
            this.f107s.cancel();
            this.f107s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f107s == SubscriptionHelper.CANCELLED;
        }
    }

    public CompletableFromPublisher(Publisher<T> flowable2) {
        this.flowable = flowable2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver cs) {
        this.flowable.subscribe(new FromPublisherSubscriber(cs));
    }
}
