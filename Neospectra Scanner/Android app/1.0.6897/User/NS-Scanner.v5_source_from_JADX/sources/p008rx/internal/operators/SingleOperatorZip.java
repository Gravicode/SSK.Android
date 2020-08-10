package p008rx.internal.operators;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import p008rx.Single;
import p008rx.Single.OnSubscribe;
import p008rx.SingleSubscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.FuncN;
import p008rx.plugins.RxJavaHooks;
import p008rx.subscriptions.CompositeSubscription;

/* renamed from: rx.internal.operators.SingleOperatorZip */
public final class SingleOperatorZip {
    private SingleOperatorZip() {
        throw new IllegalStateException("No instances!");
    }

    public static <T, R> Single<R> zip(final Single<? extends T>[] singles, final FuncN<? extends R> zipper) {
        return Single.create(new OnSubscribe<R>() {
            public void call(SingleSubscriber<? super R> subscriber) {
                if (singles.length == 0) {
                    subscriber.onError(new NoSuchElementException("Can't zip 0 Singles."));
                    return;
                }
                final AtomicInteger wip = new AtomicInteger(singles.length);
                AtomicBoolean once = new AtomicBoolean();
                Object[] values = new Object[singles.length];
                CompositeSubscription compositeSubscription = new CompositeSubscription();
                subscriber.add(compositeSubscription);
                int i = 0;
                while (true) {
                    int i2 = i;
                    if (i2 >= singles.length || compositeSubscription.isUnsubscribed() || once.get()) {
                        break;
                    }
                    final int j = i2;
                    final Object[] objArr = values;
                    final SingleSubscriber<? super R> singleSubscriber = subscriber;
                    final AtomicBoolean atomicBoolean = once;
                    C17031 r2 = new SingleSubscriber<T>() {
                        public void onSuccess(T value) {
                            objArr[j] = value;
                            if (wip.decrementAndGet() == 0) {
                                try {
                                    singleSubscriber.onSuccess(zipper.call(objArr));
                                } catch (Throwable e) {
                                    Exceptions.throwIfFatal(e);
                                    onError(e);
                                }
                            }
                        }

                        public void onError(Throwable error) {
                            if (atomicBoolean.compareAndSet(false, true)) {
                                singleSubscriber.onError(error);
                            } else {
                                RxJavaHooks.onError(error);
                            }
                        }
                    };
                    compositeSubscription.add(r2);
                    if (compositeSubscription.isUnsubscribed() || once.get()) {
                        break;
                    }
                    singles[i2].subscribe((SingleSubscriber<? super T>) r2);
                    i = i2 + 1;
                }
            }
        });
    }
}
