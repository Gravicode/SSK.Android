package p008rx.observables;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Observable;
import p008rx.Observer;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.annotations.Beta;
import p008rx.exceptions.Exceptions;
import p008rx.exceptions.OnErrorNotImplementedException;
import p008rx.functions.Action0;
import p008rx.functions.Action1;
import p008rx.functions.Actions;
import p008rx.functions.Func1;
import p008rx.internal.operators.BlockingOperatorLatest;
import p008rx.internal.operators.BlockingOperatorMostRecent;
import p008rx.internal.operators.BlockingOperatorNext;
import p008rx.internal.operators.BlockingOperatorToFuture;
import p008rx.internal.operators.BlockingOperatorToIterator;
import p008rx.internal.operators.NotificationLite;
import p008rx.internal.util.BlockingUtils;
import p008rx.internal.util.UtilityFunctions;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.observables.BlockingObservable */
public final class BlockingObservable<T> {
    static final Object ON_START = new Object();
    static final Object SET_PRODUCER = new Object();
    static final Object UNSUBSCRIBE = new Object();

    /* renamed from: o */
    private final Observable<? extends T> f929o;

    private BlockingObservable(Observable<? extends T> o) {
        this.f929o = o;
    }

    public static <T> BlockingObservable<T> from(Observable<? extends T> o) {
        return new BlockingObservable<>(o);
    }

    public void forEach(final Action1<? super T> onNext) {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Throwable> exceptionFromOnError = new AtomicReference<>();
        BlockingUtils.awaitForComplete(latch, this.f929o.subscribe((Subscriber<? super T>) new Subscriber<T>() {
            public void onCompleted() {
                latch.countDown();
            }

            public void onError(Throwable e) {
                exceptionFromOnError.set(e);
                latch.countDown();
            }

            public void onNext(T args) {
                onNext.call(args);
            }
        }));
        if (exceptionFromOnError.get() != null) {
            Exceptions.propagate((Throwable) exceptionFromOnError.get());
        }
    }

    public Iterator<T> getIterator() {
        return BlockingOperatorToIterator.toIterator(this.f929o);
    }

    public T first() {
        return blockForSingle(this.f929o.first());
    }

    public T first(Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.f929o.first(predicate));
    }

    public T firstOrDefault(T defaultValue) {
        return blockForSingle(this.f929o.map(UtilityFunctions.identity()).firstOrDefault(defaultValue));
    }

    public T firstOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.f929o.filter(predicate).map(UtilityFunctions.identity()).firstOrDefault(defaultValue));
    }

    public T last() {
        return blockForSingle(this.f929o.last());
    }

    public T last(Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.f929o.last(predicate));
    }

    public T lastOrDefault(T defaultValue) {
        return blockForSingle(this.f929o.map(UtilityFunctions.identity()).lastOrDefault(defaultValue));
    }

    public T lastOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.f929o.filter(predicate).map(UtilityFunctions.identity()).lastOrDefault(defaultValue));
    }

    public Iterable<T> mostRecent(T initialValue) {
        return BlockingOperatorMostRecent.mostRecent(this.f929o, initialValue);
    }

    public Iterable<T> next() {
        return BlockingOperatorNext.next(this.f929o);
    }

    public Iterable<T> latest() {
        return BlockingOperatorLatest.latest(this.f929o);
    }

    public T single() {
        return blockForSingle(this.f929o.single());
    }

    public T single(Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.f929o.single(predicate));
    }

    public T singleOrDefault(T defaultValue) {
        return blockForSingle(this.f929o.map(UtilityFunctions.identity()).singleOrDefault(defaultValue));
    }

    public T singleOrDefault(T defaultValue, Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.f929o.filter(predicate).map(UtilityFunctions.identity()).singleOrDefault(defaultValue));
    }

    public Future<T> toFuture() {
        return BlockingOperatorToFuture.toFuture(this.f929o);
    }

    public Iterable<T> toIterable() {
        return new Iterable<T>() {
            public Iterator<T> iterator() {
                return BlockingObservable.this.getIterator();
            }
        };
    }

    private T blockForSingle(Observable<? extends T> observable) {
        final AtomicReference<T> returnItem = new AtomicReference<>();
        final AtomicReference<Throwable> returnException = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        BlockingUtils.awaitForComplete(latch, observable.subscribe((Subscriber<? super T>) new Subscriber<T>() {
            public void onCompleted() {
                latch.countDown();
            }

            public void onError(Throwable e) {
                returnException.set(e);
                latch.countDown();
            }

            public void onNext(T item) {
                returnItem.set(item);
            }
        }));
        if (returnException.get() != null) {
            Exceptions.propagate((Throwable) returnException.get());
        }
        return returnItem.get();
    }

    @Beta
    public void subscribe() {
        final CountDownLatch cdl = new CountDownLatch(1);
        final Throwable[] error = {null};
        BlockingUtils.awaitForComplete(cdl, this.f929o.subscribe((Subscriber<? super T>) new Subscriber<T>() {
            public void onNext(T t) {
            }

            public void onError(Throwable e) {
                error[0] = e;
                cdl.countDown();
            }

            public void onCompleted() {
                cdl.countDown();
            }
        }));
        Throwable e = error[0];
        if (e != null) {
            Exceptions.propagate(e);
        }
    }

    @Beta
    public void subscribe(Observer<? super T> observer) {
        Object o;
        final BlockingQueue<Object> queue = new LinkedBlockingQueue<>();
        Subscription s = this.f929o.subscribe((Subscriber<? super T>) new Subscriber<T>() {
            public void onNext(T t) {
                queue.offer(NotificationLite.next(t));
            }

            public void onError(Throwable e) {
                queue.offer(NotificationLite.error(e));
            }

            public void onCompleted() {
                queue.offer(NotificationLite.completed());
            }
        });
        do {
            try {
                o = queue.poll();
                if (o == null) {
                    o = queue.take();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                observer.onError(e);
                return;
            } finally {
                s.unsubscribe();
            }
        } while (!NotificationLite.accept(observer, o));
    }

    @Beta
    public void subscribe(Subscriber<? super T> subscriber) {
        final BlockingQueue<Object> queue = new LinkedBlockingQueue<>();
        final Producer[] theProducer = {null};
        Subscriber<T> s = new Subscriber<T>() {
            public void onNext(T t) {
                queue.offer(NotificationLite.next(t));
            }

            public void onError(Throwable e) {
                queue.offer(NotificationLite.error(e));
            }

            public void onCompleted() {
                queue.offer(NotificationLite.completed());
            }

            public void setProducer(Producer p) {
                theProducer[0] = p;
                queue.offer(BlockingObservable.SET_PRODUCER);
            }

            public void onStart() {
                queue.offer(BlockingObservable.ON_START);
            }
        };
        subscriber.add(s);
        subscriber.add(Subscriptions.create(new Action0() {
            public void call() {
                queue.offer(BlockingObservable.UNSUBSCRIBE);
            }
        }));
        this.f929o.subscribe(s);
        while (true) {
            try {
                if (subscriber.isUnsubscribed()) {
                    break;
                }
                Object o = queue.poll();
                if (o == null) {
                    o = queue.take();
                }
                if (subscriber.isUnsubscribed()) {
                    break;
                } else if (o == UNSUBSCRIBE) {
                    break;
                } else if (o == ON_START) {
                    subscriber.onStart();
                } else if (o == SET_PRODUCER) {
                    subscriber.setProducer(theProducer[0]);
                } else if (NotificationLite.accept(subscriber, o)) {
                    s.unsubscribe();
                    return;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                subscriber.onError(e);
            } catch (Throwable th) {
                s.unsubscribe();
                throw th;
            }
        }
        s.unsubscribe();
    }

    @Beta
    public void subscribe(Action1<? super T> onNext) {
        subscribe(onNext, new Action1<Throwable>() {
            public void call(Throwable t) {
                throw new OnErrorNotImplementedException(t);
            }
        }, Actions.empty());
    }

    @Beta
    public void subscribe(Action1<? super T> onNext, Action1<? super Throwable> onError) {
        subscribe(onNext, onError, Actions.empty());
    }

    @Beta
    public void subscribe(final Action1<? super T> onNext, final Action1<? super Throwable> onError, final Action0 onCompleted) {
        subscribe((Observer<? super T>) new Observer<T>() {
            public void onNext(T t) {
                onNext.call(t);
            }

            public void onError(Throwable e) {
                onError.call(e);
            }

            public void onCompleted() {
                onCompleted.call();
            }
        });
    }
}
