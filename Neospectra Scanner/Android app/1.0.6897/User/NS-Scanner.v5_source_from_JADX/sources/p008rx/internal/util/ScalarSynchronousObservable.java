package p008rx.internal.util;

import java.util.concurrent.atomic.AtomicBoolean;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Producer;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action0;
import p008rx.functions.Func1;
import p008rx.internal.producers.SingleProducer;
import p008rx.internal.schedulers.EventLoopsScheduler;
import p008rx.observers.Subscribers;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.util.ScalarSynchronousObservable */
public final class ScalarSynchronousObservable<T> extends Observable<T> {
    static final boolean STRONG_MODE = Boolean.valueOf(System.getProperty("rx.just.strong-mode", "false")).booleanValue();

    /* renamed from: t */
    final T f917t;

    /* renamed from: rx.internal.util.ScalarSynchronousObservable$JustOnSubscribe */
    static final class JustOnSubscribe<T> implements OnSubscribe<T> {
        final T value;

        JustOnSubscribe(T value2) {
            this.value = value2;
        }

        public void call(Subscriber<? super T> s) {
            s.setProducer(ScalarSynchronousObservable.createProducer(s, this.value));
        }
    }

    /* renamed from: rx.internal.util.ScalarSynchronousObservable$ScalarAsyncOnSubscribe */
    static final class ScalarAsyncOnSubscribe<T> implements OnSubscribe<T> {
        final Func1<Action0, Subscription> onSchedule;
        final T value;

        ScalarAsyncOnSubscribe(T value2, Func1<Action0, Subscription> onSchedule2) {
            this.value = value2;
            this.onSchedule = onSchedule2;
        }

        public void call(Subscriber<? super T> s) {
            s.setProducer(new ScalarAsyncProducer(s, this.value, this.onSchedule));
        }
    }

    /* renamed from: rx.internal.util.ScalarSynchronousObservable$ScalarAsyncProducer */
    static final class ScalarAsyncProducer<T> extends AtomicBoolean implements Producer, Action0 {
        private static final long serialVersionUID = -2466317989629281651L;
        final Subscriber<? super T> actual;
        final Func1<Action0, Subscription> onSchedule;
        final T value;

        public ScalarAsyncProducer(Subscriber<? super T> actual2, T value2, Func1<Action0, Subscription> onSchedule2) {
            this.actual = actual2;
            this.value = value2;
            this.onSchedule = onSchedule2;
        }

        public void request(long n) {
            if (n < 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("n >= 0 required but it was ");
                sb.append(n);
                throw new IllegalArgumentException(sb.toString());
            } else if (n != 0 && compareAndSet(false, true)) {
                this.actual.add((Subscription) this.onSchedule.call(this));
            }
        }

        public void call() {
            Subscriber<? super T> a = this.actual;
            if (!a.isUnsubscribed()) {
                T v = this.value;
                try {
                    a.onNext(v);
                    if (!a.isUnsubscribed()) {
                        a.onCompleted();
                    }
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, (Observer<?>) a, (Object) v);
                }
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ScalarAsyncProducer[");
            sb.append(this.value);
            sb.append(", ");
            sb.append(get());
            sb.append("]");
            return sb.toString();
        }
    }

    /* renamed from: rx.internal.util.ScalarSynchronousObservable$WeakSingleProducer */
    static final class WeakSingleProducer<T> implements Producer {
        final Subscriber<? super T> actual;
        boolean once;
        final T value;

        public WeakSingleProducer(Subscriber<? super T> actual2, T value2) {
            this.actual = actual2;
            this.value = value2;
        }

        public void request(long n) {
            if (!this.once) {
                if (n < 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("n >= required but it was ");
                    sb.append(n);
                    throw new IllegalStateException(sb.toString());
                } else if (n != 0) {
                    this.once = true;
                    Subscriber<? super T> a = this.actual;
                    if (!a.isUnsubscribed()) {
                        T v = this.value;
                        try {
                            a.onNext(v);
                            if (!a.isUnsubscribed()) {
                                a.onCompleted();
                            }
                        } catch (Throwable e) {
                            Exceptions.throwOrReport(e, (Observer<?>) a, (Object) v);
                        }
                    }
                }
            }
        }
    }

    static <T> Producer createProducer(Subscriber<? super T> s, T v) {
        if (STRONG_MODE) {
            return new SingleProducer(s, v);
        }
        return new WeakSingleProducer(s, v);
    }

    public static <T> ScalarSynchronousObservable<T> create(T t) {
        return new ScalarSynchronousObservable<>(t);
    }

    protected ScalarSynchronousObservable(T t) {
        super(RxJavaHooks.onCreate((OnSubscribe<T>) new JustOnSubscribe<T>(t)));
        this.f917t = t;
    }

    public T get() {
        return this.f917t;
    }

    public Observable<T> scalarScheduleOn(final Scheduler scheduler) {
        Func1 func1;
        if (scheduler instanceof EventLoopsScheduler) {
            final EventLoopsScheduler els = (EventLoopsScheduler) scheduler;
            func1 = new Func1<Action0, Subscription>() {
                public Subscription call(Action0 a) {
                    return els.scheduleDirect(a);
                }
            };
        } else {
            func1 = new Func1<Action0, Subscription>() {
                public Subscription call(final Action0 a) {
                    final Worker w = scheduler.createWorker();
                    w.schedule(new Action0() {
                        public void call() {
                            try {
                                a.call();
                            } finally {
                                w.unsubscribe();
                            }
                        }
                    });
                    return w;
                }
            };
        }
        return create((OnSubscribe<T>) new ScalarAsyncOnSubscribe<T>(this.f917t, func1));
    }

    public <R> Observable<R> scalarFlatMap(final Func1<? super T, ? extends Observable<? extends R>> func) {
        return create((OnSubscribe<T>) new OnSubscribe<R>() {
            public void call(Subscriber<? super R> child) {
                Observable<? extends R> o = (Observable) func.call(ScalarSynchronousObservable.this.f917t);
                if (o instanceof ScalarSynchronousObservable) {
                    child.setProducer(ScalarSynchronousObservable.createProducer(child, ((ScalarSynchronousObservable) o).f917t));
                } else {
                    o.unsafeSubscribe(Subscribers.wrap(child));
                }
            }
        });
    }
}
