package p005io.reactivex.internal.operators.parallel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.parallel.ParallelFlowable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.parallel.ParallelSortedJoin */
public final class ParallelSortedJoin<T> extends Flowable<T> {
    final Comparator<? super T> comparator;
    final ParallelFlowable<List<T>> source;

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelSortedJoin$SortedJoinInnerSubscriber */
    static final class SortedJoinInnerSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<List<T>> {
        private static final long serialVersionUID = 6751017204873808094L;
        final int index;
        final SortedJoinSubscription<T> parent;

        SortedJoinInnerSubscriber(SortedJoinSubscription<T> parent2, int index2) {
            this.parent = parent2;
            this.index = index2;
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
        }

        public void onNext(List<T> t) {
            this.parent.innerNext(t, this.index);
        }

        public void onError(Throwable t) {
            this.parent.innerError(t);
        }

        public void onComplete() {
        }

        /* access modifiers changed from: 0000 */
        public void cancel() {
            SubscriptionHelper.cancel(this);
        }
    }

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelSortedJoin$SortedJoinSubscription */
    static final class SortedJoinSubscription<T> extends AtomicInteger implements Subscription {
        private static final long serialVersionUID = 3481980673745556697L;
        final Subscriber<? super T> actual;
        volatile boolean cancelled;
        final Comparator<? super T> comparator;
        final AtomicReference<Throwable> error = new AtomicReference<>();
        final int[] indexes;
        final List<T>[] lists;
        final AtomicInteger remaining = new AtomicInteger();
        final AtomicLong requested = new AtomicLong();
        final SortedJoinInnerSubscriber<T>[] subscribers;

        SortedJoinSubscription(Subscriber<? super T> actual2, int n, Comparator<? super T> comparator2) {
            this.actual = actual2;
            this.comparator = comparator2;
            SortedJoinInnerSubscriber<T>[] s = new SortedJoinInnerSubscriber[n];
            for (int i = 0; i < n; i++) {
                s[i] = new SortedJoinInnerSubscriber<>(this, i);
            }
            this.subscribers = s;
            this.lists = new List[n];
            this.indexes = new int[n];
            this.remaining.lazySet(n);
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
                if (this.remaining.get() == 0) {
                    drain();
                }
            }
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                cancelAll();
                if (getAndIncrement() == 0) {
                    Arrays.fill(this.lists, null);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void cancelAll() {
            for (SortedJoinInnerSubscriber<T> s : this.subscribers) {
                s.cancel();
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerNext(List<T> value, int index) {
            this.lists[index] = value;
            if (this.remaining.decrementAndGet() == 0) {
                drain();
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerError(Throwable e) {
            if (this.error.compareAndSet(null, e)) {
                drain();
            } else if (e != this.error.get()) {
                RxJavaPlugins.onError(e);
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                Subscriber<? super T> a = this.actual;
                List<T>[] lists2 = this.lists;
                int[] indexes2 = this.indexes;
                int n = indexes2.length;
                while (true) {
                    long r = this.requested.get();
                    long e = 0;
                    while (e != r) {
                        if (this.cancelled) {
                            Arrays.fill(lists2, null);
                            return;
                        }
                        Throwable ex = (Throwable) this.error.get();
                        if (ex != null) {
                            cancelAll();
                            Arrays.fill(lists2, null);
                            a.onError(ex);
                            return;
                        }
                        int minIndex = -1;
                        Object obj = null;
                        int i = 0;
                        while (true) {
                            int i2 = i;
                            boolean z = true;
                            int i3 = i2;
                            if (i3 >= n) {
                                break;
                            }
                            List<T> list = lists2[i3];
                            int index = indexes2[i3];
                            Throwable ex2 = ex;
                            if (list.size() != index) {
                                if (obj == null) {
                                    minIndex = i3;
                                    obj = list.get(index);
                                } else {
                                    T b = list.get(index);
                                    List<T> list2 = list;
                                    try {
                                        if (this.comparator.compare(obj, b) <= 0) {
                                            z = false;
                                        }
                                        if (z) {
                                            obj = b;
                                            minIndex = i3;
                                        }
                                    } catch (Throwable th) {
                                        Throwable exc = th;
                                        Exceptions.throwIfFatal(exc);
                                        cancelAll();
                                        Object obj2 = b;
                                        Arrays.fill(lists2, null);
                                        int i4 = index;
                                        if (!this.error.compareAndSet(null, exc)) {
                                            RxJavaPlugins.onError(exc);
                                        }
                                        a.onError((Throwable) this.error.get());
                                        return;
                                    }
                                }
                            }
                            i = i3 + 1;
                            ex = ex2;
                        }
                        if (obj == null) {
                            Arrays.fill(lists2, null);
                            a.onComplete();
                            return;
                        }
                        a.onNext(obj);
                        indexes2[minIndex] = indexes2[minIndex] + 1;
                        e++;
                    }
                    if (e == r) {
                        if (this.cancelled) {
                            Arrays.fill(lists2, null);
                            return;
                        }
                        Throwable ex3 = (Throwable) this.error.get();
                        if (ex3 != null) {
                            cancelAll();
                            Arrays.fill(lists2, null);
                            a.onError(ex3);
                            return;
                        }
                        boolean empty = true;
                        int i5 = 0;
                        while (true) {
                            int i6 = i5;
                            if (i6 >= n) {
                                break;
                            } else if (indexes2[i6] != lists2[i6].size()) {
                                empty = false;
                                break;
                            } else {
                                i5 = i6 + 1;
                            }
                        }
                        if (empty) {
                            Arrays.fill(lists2, null);
                            a.onComplete();
                            return;
                        }
                    }
                    if (!(e == 0 || r == Long.MAX_VALUE)) {
                        this.requested.addAndGet(-e);
                    }
                    int w = get();
                    if (w == missed) {
                        missed = addAndGet(-missed);
                        if (missed == 0) {
                            return;
                        }
                    } else {
                        missed = w;
                    }
                }
            }
        }
    }

    public ParallelSortedJoin(ParallelFlowable<List<T>> source2, Comparator<? super T> comparator2) {
        this.source = source2;
        this.comparator = comparator2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        SortedJoinSubscription<T> parent = new SortedJoinSubscription<>(s, this.source.parallelism(), this.comparator);
        s.onSubscribe(parent);
        this.source.subscribe(parent.subscribers);
    }
}
