package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import p005io.reactivex.Flowable;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.internal.fuseable.ConditionalSubscriber;
import p005io.reactivex.internal.subscriptions.BasicQueueSubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableRangeLong */
public final class FlowableRangeLong extends Flowable<Long> {
    final long end;
    final long start;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableRangeLong$BaseRangeSubscription */
    static abstract class BaseRangeSubscription extends BasicQueueSubscription<Long> {
        private static final long serialVersionUID = -2252972430506210021L;
        volatile boolean cancelled;
        final long end;
        long index;

        /* access modifiers changed from: 0000 */
        public abstract void fastPath();

        /* access modifiers changed from: 0000 */
        public abstract void slowPath(long j);

        BaseRangeSubscription(long index2, long end2) {
            this.index = index2;
            this.end = end2;
        }

        public final int requestFusion(int mode) {
            return mode & 1;
        }

        @Nullable
        public final Long poll() {
            long i = this.index;
            if (i == this.end) {
                return null;
            }
            this.index = 1 + i;
            return Long.valueOf(i);
        }

        public final boolean isEmpty() {
            return this.index == this.end;
        }

        public final void clear() {
            this.index = this.end;
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

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableRangeLong$RangeConditionalSubscription */
    static final class RangeConditionalSubscription extends BaseRangeSubscription {
        private static final long serialVersionUID = 2587302975077663557L;
        final ConditionalSubscriber<? super Long> actual;

        RangeConditionalSubscription(ConditionalSubscriber<? super Long> actual2, long index, long end) {
            super(index, end);
            this.actual = actual2;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            long f = this.end;
            ConditionalSubscriber<? super Long> a = this.actual;
            long i = this.index;
            while (i != f) {
                if (!this.cancelled) {
                    a.tryOnNext(Long.valueOf(i));
                    i++;
                } else {
                    return;
                }
            }
            if (!this.cancelled) {
                a.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long r) {
            long e = 0;
            long f = this.end;
            long i = this.index;
            ConditionalSubscriber<? super Long> a = this.actual;
            while (true) {
                if (e == r || i == f) {
                    if (i == f) {
                        if (!this.cancelled) {
                            a.onComplete();
                        }
                        return;
                    }
                    r = get();
                    if (e == r) {
                        this.index = i;
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
                    if (a.tryOnNext(Long.valueOf(i))) {
                        e++;
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableRangeLong$RangeSubscription */
    static final class RangeSubscription extends BaseRangeSubscription {
        private static final long serialVersionUID = 2587302975077663557L;
        final Subscriber<? super Long> actual;

        RangeSubscription(Subscriber<? super Long> actual2, long index, long end) {
            super(index, end);
            this.actual = actual2;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            long f = this.end;
            Subscriber<? super Long> a = this.actual;
            long i = this.index;
            while (i != f) {
                if (!this.cancelled) {
                    a.onNext(Long.valueOf(i));
                    i++;
                } else {
                    return;
                }
            }
            if (!this.cancelled) {
                a.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long r) {
            long e = 0;
            long f = this.end;
            long i = this.index;
            Subscriber<? super Long> a = this.actual;
            while (true) {
                if (e == r || i == f) {
                    if (i == f) {
                        if (!this.cancelled) {
                            a.onComplete();
                        }
                        return;
                    }
                    r = get();
                    if (e == r) {
                        this.index = i;
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
                    a.onNext(Long.valueOf(i));
                    e++;
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public FlowableRangeLong(long start2, long count) {
        this.start = start2;
        this.end = start2 + count;
    }

    public void subscribeActual(Subscriber<? super Long> s) {
        if (s instanceof ConditionalSubscriber) {
            RangeConditionalSubscription rangeConditionalSubscription = new RangeConditionalSubscription((ConditionalSubscriber) s, this.start, this.end);
            s.onSubscribe(rangeConditionalSubscription);
            return;
        }
        RangeSubscription rangeSubscription = new RangeSubscription(s, this.start, this.end);
        s.onSubscribe(rangeSubscription);
    }
}
