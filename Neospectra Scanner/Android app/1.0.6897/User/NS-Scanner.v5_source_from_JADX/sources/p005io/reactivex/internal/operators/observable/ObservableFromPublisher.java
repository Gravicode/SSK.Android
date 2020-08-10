package p005io.reactivex.internal.operators.observable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableFromPublisher */
public final class ObservableFromPublisher<T> extends Observable<T> {
    final Publisher<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableFromPublisher$PublisherSubscriber */
    static final class PublisherSubscriber<T> implements FlowableSubscriber<T>, Disposable {
        final Observer<? super T> actual;

        /* renamed from: s */
        Subscription f316s;

        PublisherSubscriber(Observer<? super T> o) {
            this.actual = o;
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f316s, s)) {
                this.f316s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void dispose() {
            this.f316s.cancel();
            this.f316s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f316s == SubscriptionHelper.CANCELLED;
        }
    }

    public ObservableFromPublisher(Publisher<? extends T> publisher) {
        this.source = publisher;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> o) {
        this.source.subscribe(new PublisherSubscriber(o));
    }
}
