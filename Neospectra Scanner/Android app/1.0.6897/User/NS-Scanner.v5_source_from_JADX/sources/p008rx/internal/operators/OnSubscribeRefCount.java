package p008rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.functions.Action0;
import p008rx.functions.Action1;
import p008rx.observables.ConnectableObservable;
import p008rx.subscriptions.CompositeSubscription;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.OnSubscribeRefCount */
public final class OnSubscribeRefCount<T> implements OnSubscribe<T> {
    volatile CompositeSubscription baseSubscription = new CompositeSubscription();
    final ReentrantLock lock = new ReentrantLock();
    private final ConnectableObservable<? extends T> source;
    final AtomicInteger subscriptionCount = new AtomicInteger(0);

    public OnSubscribeRefCount(ConnectableObservable<? extends T> source2) {
        this.source = source2;
    }

    public void call(Subscriber<? super T> subscriber) {
        this.lock.lock();
        if (this.subscriptionCount.incrementAndGet() == 1) {
            AtomicBoolean writeLocked = new AtomicBoolean(true);
            try {
                this.source.connect(onSubscribe(subscriber, writeLocked));
            } finally {
                if (writeLocked.get()) {
                    this.lock.unlock();
                }
            }
        } else {
            try {
                doSubscribe(subscriber, this.baseSubscription);
            } finally {
                this.lock.unlock();
            }
        }
    }

    private Action1<Subscription> onSubscribe(final Subscriber<? super T> subscriber, final AtomicBoolean writeLocked) {
        return new Action1<Subscription>() {
            public void call(Subscription subscription) {
                try {
                    OnSubscribeRefCount.this.baseSubscription.add(subscription);
                    OnSubscribeRefCount.this.doSubscribe(subscriber, OnSubscribeRefCount.this.baseSubscription);
                } finally {
                    OnSubscribeRefCount.this.lock.unlock();
                    writeLocked.set(false);
                }
            }
        };
    }

    /* access modifiers changed from: 0000 */
    public void doSubscribe(final Subscriber<? super T> subscriber, final CompositeSubscription currentBase) {
        subscriber.add(disconnect(currentBase));
        this.source.unsafeSubscribe(new Subscriber<T>(subscriber) {
            public void onError(Throwable e) {
                cleanup();
                subscriber.onError(e);
            }

            public void onNext(T t) {
                subscriber.onNext(t);
            }

            public void onCompleted() {
                cleanup();
                subscriber.onCompleted();
            }

            /* access modifiers changed from: 0000 */
            public void cleanup() {
                OnSubscribeRefCount.this.lock.lock();
                try {
                    if (OnSubscribeRefCount.this.baseSubscription == currentBase) {
                        OnSubscribeRefCount.this.baseSubscription.unsubscribe();
                        OnSubscribeRefCount.this.baseSubscription = new CompositeSubscription();
                        OnSubscribeRefCount.this.subscriptionCount.set(0);
                    }
                } finally {
                    OnSubscribeRefCount.this.lock.unlock();
                }
            }
        });
    }

    private Subscription disconnect(final CompositeSubscription current) {
        return Subscriptions.create(new Action0() {
            public void call() {
                OnSubscribeRefCount.this.lock.lock();
                try {
                    if (OnSubscribeRefCount.this.baseSubscription == current && OnSubscribeRefCount.this.subscriptionCount.decrementAndGet() == 0) {
                        OnSubscribeRefCount.this.baseSubscription.unsubscribe();
                        OnSubscribeRefCount.this.baseSubscription = new CompositeSubscription();
                    }
                } finally {
                    OnSubscribeRefCount.this.lock.unlock();
                }
            }
        });
    }
}
