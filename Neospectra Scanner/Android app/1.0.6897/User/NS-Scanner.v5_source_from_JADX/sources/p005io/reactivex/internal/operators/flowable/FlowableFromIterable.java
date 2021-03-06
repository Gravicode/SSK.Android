package p005io.reactivex.internal.operators.flowable;

import java.util.Iterator;
import org.reactivestreams.Subscriber;
import p005io.reactivex.Flowable;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.ConditionalSubscriber;
import p005io.reactivex.internal.subscriptions.BasicQueueSubscription;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableFromIterable */
public final class FlowableFromIterable<T> extends Flowable<T> {
    final Iterable<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFromIterable$BaseRangeSubscription */
    static abstract class BaseRangeSubscription<T> extends BasicQueueSubscription<T> {
        private static final long serialVersionUID = -2252972430506210021L;
        volatile boolean cancelled;

        /* renamed from: it */
        Iterator<? extends T> f163it;
        boolean once;

        /* access modifiers changed from: 0000 */
        public abstract void fastPath();

        /* access modifiers changed from: 0000 */
        public abstract void slowPath(long j);

        BaseRangeSubscription(Iterator<? extends T> it) {
            this.f163it = it;
        }

        public final int requestFusion(int mode) {
            return mode & 1;
        }

        @Nullable
        public final T poll() {
            if (this.f163it == null) {
                return null;
            }
            if (!this.once) {
                this.once = true;
            } else if (!this.f163it.hasNext()) {
                return null;
            }
            return ObjectHelper.requireNonNull(this.f163it.next(), "Iterator.next() returned a null value");
        }

        public final boolean isEmpty() {
            return this.f163it == null || !this.f163it.hasNext();
        }

        public final void clear() {
            this.f163it = null;
        }

        public final void request(long n) {
            if (SubscriptionHelper.validate(n) && BackpressureHelper.add(this, n) == 0) {
                if (n == Long.MAX_VALUE) {
                    fastPath();
                } else {
                    slowPath(n);
                }
            }
        }

        public final void cancel() {
            this.cancelled = true;
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFromIterable$IteratorConditionalSubscription */
    static final class IteratorConditionalSubscription<T> extends BaseRangeSubscription<T> {
        private static final long serialVersionUID = -6022804456014692607L;
        final ConditionalSubscriber<? super T> actual;

        IteratorConditionalSubscription(ConditionalSubscriber<? super T> actual2, Iterator<? extends T> it) {
            super(it);
            this.actual = actual2;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            Iterator<? extends T> it = this.f163it;
            ConditionalSubscriber<? super T> a = this.actual;
            while (!this.cancelled) {
                try {
                    T t = it.next();
                    if (!this.cancelled) {
                        if (t == null) {
                            a.onError(new NullPointerException("Iterator.next() returned a null value"));
                            return;
                        }
                        a.tryOnNext(t);
                        if (!this.cancelled) {
                            try {
                                if (!it.hasNext()) {
                                    if (!this.cancelled) {
                                        a.onComplete();
                                    }
                                    return;
                                }
                            } catch (Throwable ex) {
                                Exceptions.throwIfFatal(ex);
                                a.onError(ex);
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (Throwable ex2) {
                    Exceptions.throwIfFatal(ex2);
                    a.onError(ex2);
                    return;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long r) {
            long e = 0;
            Iterator<? extends T> it = this.f163it;
            ConditionalSubscriber<? super T> a = this.actual;
            while (true) {
                if (e == r) {
                    r = get();
                    if (e == r) {
                        r = addAndGet(-e);
                        if (r != 0) {
                            e = 0;
                        } else {
                            return;
                        }
                    } else {
                        continue;
                    }
                } else if (!this.cancelled) {
                    try {
                        T t = it.next();
                        if (!this.cancelled) {
                            if (t == null) {
                                a.onError(new NullPointerException("Iterator.next() returned a null value"));
                                return;
                            }
                            boolean b = a.tryOnNext(t);
                            if (!this.cancelled) {
                                try {
                                    if (!it.hasNext()) {
                                        if (!this.cancelled) {
                                            a.onComplete();
                                        }
                                        return;
                                    } else if (b) {
                                        e++;
                                    }
                                } catch (Throwable ex) {
                                    Exceptions.throwIfFatal(ex);
                                    a.onError(ex);
                                    return;
                                }
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable ex2) {
                        Exceptions.throwIfFatal(ex2);
                        a.onError(ex2);
                        return;
                    }
                } else {
                    return;
                }
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFromIterable$IteratorSubscription */
    static final class IteratorSubscription<T> extends BaseRangeSubscription<T> {
        private static final long serialVersionUID = -6022804456014692607L;
        final Subscriber<? super T> actual;

        IteratorSubscription(Subscriber<? super T> actual2, Iterator<? extends T> it) {
            super(it);
            this.actual = actual2;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            Iterator<? extends T> it = this.f163it;
            Subscriber<? super T> a = this.actual;
            while (!this.cancelled) {
                try {
                    T t = it.next();
                    if (!this.cancelled) {
                        if (t == null) {
                            a.onError(new NullPointerException("Iterator.next() returned a null value"));
                            return;
                        }
                        a.onNext(t);
                        if (!this.cancelled) {
                            try {
                                if (!it.hasNext()) {
                                    if (!this.cancelled) {
                                        a.onComplete();
                                    }
                                    return;
                                }
                            } catch (Throwable ex) {
                                Exceptions.throwIfFatal(ex);
                                a.onError(ex);
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (Throwable ex2) {
                    Exceptions.throwIfFatal(ex2);
                    a.onError(ex2);
                    return;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long r) {
            long e = 0;
            Iterator<? extends T> it = this.f163it;
            Subscriber<? super T> a = this.actual;
            while (true) {
                if (e == r) {
                    r = get();
                    if (e == r) {
                        r = addAndGet(-e);
                        if (r != 0) {
                            e = 0;
                        } else {
                            return;
                        }
                    } else {
                        continue;
                    }
                } else if (!this.cancelled) {
                    try {
                        T t = it.next();
                        if (!this.cancelled) {
                            if (t == null) {
                                a.onError(new NullPointerException("Iterator.next() returned a null value"));
                                return;
                            }
                            a.onNext(t);
                            if (!this.cancelled) {
                                try {
                                    if (!it.hasNext()) {
                                        if (!this.cancelled) {
                                            a.onComplete();
                                        }
                                        return;
                                    }
                                    e++;
                                } catch (Throwable ex) {
                                    Exceptions.throwIfFatal(ex);
                                    a.onError(ex);
                                    return;
                                }
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable ex2) {
                        Exceptions.throwIfFatal(ex2);
                        a.onError(ex2);
                        return;
                    }
                } else {
                    return;
                }
            }
        }
    }

    public FlowableFromIterable(Iterable<? extends T> source2) {
        this.source = source2;
    }

    public void subscribeActual(Subscriber<? super T> s) {
        try {
            subscribe(s, this.source.iterator());
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            EmptySubscription.error(e, s);
        }
    }

    public static <T> void subscribe(Subscriber<? super T> s, Iterator<? extends T> it) {
        try {
            if (!it.hasNext()) {
                EmptySubscription.complete(s);
                return;
            }
            if (s instanceof ConditionalSubscriber) {
                s.onSubscribe(new IteratorConditionalSubscription((ConditionalSubscriber) s, it));
            } else {
                s.onSubscribe(new IteratorSubscription(s, it));
            }
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            EmptySubscription.error(e, s);
        }
    }
}
