package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import p005io.reactivex.Flowable;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.internal.fuseable.ConditionalSubscriber;
import p005io.reactivex.internal.subscriptions.BasicQueueSubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableRange */
public final class FlowableRange extends Flowable<Integer> {
    final int end;
    final int start;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableRange$BaseRangeSubscription */
    static abstract class BaseRangeSubscription extends BasicQueueSubscription<Integer> {
        private static final long serialVersionUID = -2252972430506210021L;
        volatile boolean cancelled;
        final int end;
        int index;

        /* access modifiers changed from: 0000 */
        public abstract void fastPath();

        /* access modifiers changed from: 0000 */
        public abstract void slowPath(long j);

        BaseRangeSubscription(int index2, int end2) {
            this.index = index2;
            this.end = end2;
        }

        public final int requestFusion(int mode) {
            return mode & 1;
        }

        @Nullable
        public final Integer poll() {
            int i = this.index;
            if (i == this.end) {
                return null;
            }
            this.index = i + 1;
            return Integer.valueOf(i);
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

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableRange$RangeConditionalSubscription */
    static final class RangeConditionalSubscription extends BaseRangeSubscription {
        private static final long serialVersionUID = 2587302975077663557L;
        final ConditionalSubscriber<? super Integer> actual;

        RangeConditionalSubscription(ConditionalSubscriber<? super Integer> actual2, int index, int end) {
            super(index, end);
            this.actual = actual2;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            int f = this.end;
            ConditionalSubscriber<? super Integer> a = this.actual;
            int i = this.index;
            while (i != f) {
                if (!this.cancelled) {
                    a.tryOnNext(Integer.valueOf(i));
                    i++;
                } else {
                    return;
                }
            }
            if (this.cancelled == 0) {
                a.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long r) {
            long e = 0;
            int f = this.end;
            int i = this.index;
            ConditionalSubscriber<? super Integer> a = this.actual;
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
                    if (a.tryOnNext(Integer.valueOf(i))) {
                        e++;
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableRange$RangeSubscription */
    static final class RangeSubscription extends BaseRangeSubscription {
        private static final long serialVersionUID = 2587302975077663557L;
        final Subscriber<? super Integer> actual;

        RangeSubscription(Subscriber<? super Integer> actual2, int index, int end) {
            super(index, end);
            this.actual = actual2;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            int f = this.end;
            Subscriber<? super Integer> a = this.actual;
            int i = this.index;
            while (i != f) {
                if (!this.cancelled) {
                    a.onNext(Integer.valueOf(i));
                    i++;
                } else {
                    return;
                }
            }
            if (this.cancelled == 0) {
                a.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long r) {
            long e = 0;
            int f = this.end;
            int i = this.index;
            Subscriber<? super Integer> a = this.actual;
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
                    a.onNext(Integer.valueOf(i));
                    e++;
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public FlowableRange(int start2, int count) {
        this.start = start2;
        this.end = start2 + count;
    }

    public void subscribeActual(Subscriber<? super Integer> s) {
        if (s instanceof ConditionalSubscriber) {
            s.onSubscribe(new RangeConditionalSubscription((ConditionalSubscriber) s, this.start, this.end));
        } else {
            s.onSubscribe(new RangeSubscription(s, this.start, this.end));
        }
    }
}
