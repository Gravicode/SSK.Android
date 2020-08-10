package p008rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import p008rx.Notification;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Observable.Operator;
import p008rx.Producer;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Subscriber;
import p008rx.functions.Action0;
import p008rx.functions.Func1;
import p008rx.functions.Func2;
import p008rx.internal.producers.ProducerArbiter;
import p008rx.observers.Subscribers;
import p008rx.schedulers.Schedulers;
import p008rx.subjects.BehaviorSubject;
import p008rx.subjects.SerializedSubject;
import p008rx.subscriptions.SerialSubscription;

/* renamed from: rx.internal.operators.OnSubscribeRedo */
public final class OnSubscribeRedo<T> implements OnSubscribe<T> {
    static final Func1<Observable<? extends Notification<?>>, Observable<?>> REDO_INFINITE = new Func1<Observable<? extends Notification<?>>, Observable<?>>() {
        public Observable<?> call(Observable<? extends Notification<?>> ts) {
            return ts.map(new Func1<Notification<?>, Notification<?>>() {
                public Notification<?> call(Notification<?> notification) {
                    return Notification.createOnNext(null);
                }
            });
        }
    };
    private final Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> controlHandlerFunction;
    private final Scheduler scheduler;
    final Observable<T> source;
    final boolean stopOnComplete;
    final boolean stopOnError;

    /* renamed from: rx.internal.operators.OnSubscribeRedo$RedoFinite */
    public static final class RedoFinite implements Func1<Observable<? extends Notification<?>>, Observable<?>> {
        final long count;

        public RedoFinite(long count2) {
            this.count = count2;
        }

        public Observable<?> call(Observable<? extends Notification<?>> ts) {
            return ts.map(new Func1<Notification<?>, Notification<?>>() {
                int num;

                public Notification<?> call(Notification<?> terminalNotification) {
                    if (RedoFinite.this.count == 0) {
                        return terminalNotification;
                    }
                    this.num++;
                    if (((long) this.num) <= RedoFinite.this.count) {
                        return Notification.createOnNext(Integer.valueOf(this.num));
                    }
                    return terminalNotification;
                }
            }).dematerialize();
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeRedo$RetryWithPredicate */
    public static final class RetryWithPredicate implements Func1<Observable<? extends Notification<?>>, Observable<? extends Notification<?>>> {
        final Func2<Integer, Throwable, Boolean> predicate;

        public RetryWithPredicate(Func2<Integer, Throwable, Boolean> predicate2) {
            this.predicate = predicate2;
        }

        public Observable<? extends Notification<?>> call(Observable<? extends Notification<?>> ts) {
            return ts.scan(Notification.createOnNext(Integer.valueOf(0)), new Func2<Notification<Integer>, Notification<?>, Notification<Integer>>() {
                public Notification<Integer> call(Notification<Integer> n, Notification<?> term) {
                    int value = ((Integer) n.getValue()).intValue();
                    if (((Boolean) RetryWithPredicate.this.predicate.call(Integer.valueOf(value), term.getThrowable())).booleanValue()) {
                        return Notification.createOnNext(Integer.valueOf(value + 1));
                    }
                    return term;
                }
            });
        }
    }

    public static <T> Observable<T> retry(Observable<T> source2) {
        return retry(source2, REDO_INFINITE);
    }

    public static <T> Observable<T> retry(Observable<T> source2, long count) {
        if (count < 0) {
            throw new IllegalArgumentException("count >= 0 expected");
        } else if (count == 0) {
            return source2;
        } else {
            return retry(source2, (Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>>) new RedoFinite<Object,Object>(count));
        }
    }

    public static <T> Observable<T> retry(Observable<T> source2, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler) {
        OnSubscribeRedo onSubscribeRedo = new OnSubscribeRedo(source2, notificationHandler, true, false, Schedulers.trampoline());
        return Observable.create((OnSubscribe<T>) onSubscribeRedo);
    }

    public static <T> Observable<T> retry(Observable<T> source2, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler, Scheduler scheduler2) {
        OnSubscribeRedo onSubscribeRedo = new OnSubscribeRedo(source2, notificationHandler, true, false, scheduler2);
        return Observable.create((OnSubscribe<T>) onSubscribeRedo);
    }

    public static <T> Observable<T> repeat(Observable<T> source2) {
        return repeat(source2, Schedulers.trampoline());
    }

    public static <T> Observable<T> repeat(Observable<T> source2, Scheduler scheduler2) {
        return repeat(source2, REDO_INFINITE, scheduler2);
    }

    public static <T> Observable<T> repeat(Observable<T> source2, long count) {
        return repeat(source2, count, Schedulers.trampoline());
    }

    public static <T> Observable<T> repeat(Observable<T> source2, long count, Scheduler scheduler2) {
        if (count == 0) {
            return Observable.empty();
        }
        if (count >= 0) {
            return repeat(source2, (Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>>) new RedoFinite<Object,Object>(count - 1), scheduler2);
        }
        throw new IllegalArgumentException("count >= 0 expected");
    }

    public static <T> Observable<T> repeat(Observable<T> source2, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler) {
        OnSubscribeRedo onSubscribeRedo = new OnSubscribeRedo(source2, notificationHandler, false, true, Schedulers.trampoline());
        return Observable.create((OnSubscribe<T>) onSubscribeRedo);
    }

    public static <T> Observable<T> repeat(Observable<T> source2, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler, Scheduler scheduler2) {
        OnSubscribeRedo onSubscribeRedo = new OnSubscribeRedo(source2, notificationHandler, false, true, scheduler2);
        return Observable.create((OnSubscribe<T>) onSubscribeRedo);
    }

    public static <T> Observable<T> redo(Observable<T> source2, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler, Scheduler scheduler2) {
        OnSubscribeRedo onSubscribeRedo = new OnSubscribeRedo(source2, notificationHandler, false, false, scheduler2);
        return Observable.create((OnSubscribe<T>) onSubscribeRedo);
    }

    private OnSubscribeRedo(Observable<T> source2, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> f, boolean stopOnComplete2, boolean stopOnError2, Scheduler scheduler2) {
        this.source = source2;
        this.controlHandlerFunction = f;
        this.stopOnComplete = stopOnComplete2;
        this.stopOnError = stopOnError2;
        this.scheduler = scheduler2;
    }

    public void call(Subscriber<? super T> child) {
        Subscriber<? super T> subscriber = child;
        final AtomicBoolean resumeBoundary = new AtomicBoolean(true);
        final AtomicLong consumerCapacity = new AtomicLong();
        Worker worker = this.scheduler.createWorker();
        subscriber.add(worker);
        SerialSubscription sourceSubscriptions = new SerialSubscription();
        subscriber.add(sourceSubscriptions);
        SerializedSubject serialized = BehaviorSubject.create().toSerialized();
        Subscriber<Notification<?>> dummySubscriber = Subscribers.empty();
        serialized.subscribe(dummySubscriber);
        final ProducerArbiter arbiter = new ProducerArbiter();
        final Subscriber<? super T> subscriber2 = subscriber;
        final SerializedSubject serializedSubject = serialized;
        final SerialSubscription serialSubscription = sourceSubscriptions;
        final C15622 r0 = new Action0() {
            public void call() {
                if (!subscriber2.isUnsubscribed()) {
                    Subscriber<T> terminalDelegatingSubscriber = new Subscriber<T>() {
                        boolean done;

                        public void onCompleted() {
                            if (!this.done) {
                                this.done = true;
                                unsubscribe();
                                serializedSubject.onNext(Notification.createOnCompleted());
                            }
                        }

                        public void onError(Throwable e) {
                            if (!this.done) {
                                this.done = true;
                                unsubscribe();
                                serializedSubject.onNext(Notification.createOnError(e));
                            }
                        }

                        public void onNext(T v) {
                            if (!this.done) {
                                subscriber2.onNext(v);
                                decrementConsumerCapacity();
                                arbiter.produced(1);
                            }
                        }

                        private void decrementConsumerCapacity() {
                            long cc;
                            do {
                                cc = consumerCapacity.get();
                                if (cc == Long.MAX_VALUE) {
                                    return;
                                }
                            } while (!consumerCapacity.compareAndSet(cc, cc - 1));
                        }

                        public void setProducer(Producer producer) {
                            arbiter.setProducer(producer);
                        }
                    };
                    serialSubscription.set(terminalDelegatingSubscriber);
                    OnSubscribeRedo.this.source.unsafeSubscribe(terminalDelegatingSubscriber);
                }
            }
        };
        Observable<?> restarts = (Observable) this.controlHandlerFunction.call(serialized.lift(new Operator<Notification<?>, Notification<?>>() {
            public Subscriber<? super Notification<?>> call(final Subscriber<? super Notification<?>> filteredTerminals) {
                return new Subscriber<Notification<?>>(filteredTerminals) {
                    public void onCompleted() {
                        filteredTerminals.onCompleted();
                    }

                    public void onError(Throwable e) {
                        filteredTerminals.onError(e);
                    }

                    public void onNext(Notification<?> t) {
                        if (t.isOnCompleted() && OnSubscribeRedo.this.stopOnComplete) {
                            filteredTerminals.onCompleted();
                        } else if (!t.isOnError() || !OnSubscribeRedo.this.stopOnError) {
                            filteredTerminals.onNext(t);
                        } else {
                            filteredTerminals.onError(t.getThrowable());
                        }
                    }

                    public void setProducer(Producer producer) {
                        producer.request(Long.MAX_VALUE);
                    }
                };
            }
        }));
        Subscriber subscriber3 = dummySubscriber;
        final Observable observable = restarts;
        SerialSubscription serialSubscription2 = sourceSubscriptions;
        final Subscriber<? super T> subscriber4 = subscriber;
        Worker worker2 = worker;
        final AtomicLong atomicLong = consumerCapacity;
        Observable observable2 = restarts;
        Subscriber<? super T> subscriber5 = subscriber;
        final Worker worker3 = worker2;
        C15664 r7 = new Action0() {
            public void call() {
                observable.unsafeSubscribe(new Subscriber<Object>(subscriber4) {
                    public void onCompleted() {
                        subscriber4.onCompleted();
                    }

                    public void onError(Throwable e) {
                        subscriber4.onError(e);
                    }

                    public void onNext(Object t) {
                        if (subscriber4.isUnsubscribed()) {
                            return;
                        }
                        if (atomicLong.get() > 0) {
                            worker3.schedule(r0);
                        } else {
                            resumeBoundary.compareAndSet(false, true);
                        }
                    }

                    public void setProducer(Producer producer) {
                        producer.request(Long.MAX_VALUE);
                    }
                });
            }
        };
        worker2.schedule(r7);
        final AtomicLong atomicLong2 = consumerCapacity;
        final ProducerArbiter producerArbiter = arbiter;
        final AtomicBoolean atomicBoolean = resumeBoundary;
        final Worker worker4 = worker2;
        final C15622 r12 = r0;
        C15685 r6 = new Producer() {
            public void request(long n) {
                if (n > 0) {
                    BackpressureUtils.getAndAddRequest(atomicLong2, n);
                    producerArbiter.request(n);
                    if (atomicBoolean.compareAndSet(true, false)) {
                        worker4.schedule(r12);
                    }
                }
            }
        };
        subscriber5.setProducer(r6);
    }
}
