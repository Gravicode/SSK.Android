package p005io.reactivex.internal.operators.parallel;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Scheduler.Worker;
import p005io.reactivex.exceptions.MissingBackpressureException;
import p005io.reactivex.internal.fuseable.ConditionalSubscriber;
import p005io.reactivex.internal.queue.SpscArrayQueue;
import p005io.reactivex.internal.schedulers.SchedulerMultiWorkerSupport;
import p005io.reactivex.internal.schedulers.SchedulerMultiWorkerSupport.WorkerCallback;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.parallel.ParallelFlowable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.parallel.ParallelRunOn */
public final class ParallelRunOn<T> extends ParallelFlowable<T> {
    final int prefetch;
    final Scheduler scheduler;
    final ParallelFlowable<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelRunOn$BaseRunOnSubscriber */
    static abstract class BaseRunOnSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Subscription, Runnable {
        private static final long serialVersionUID = 9222303586456402150L;
        volatile boolean cancelled;
        int consumed;
        volatile boolean done;
        Throwable error;
        final int limit;
        final int prefetch;
        final SpscArrayQueue<T> queue;
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        Subscription f398s;
        final Worker worker;

        BaseRunOnSubscriber(int prefetch2, SpscArrayQueue<T> queue2, Worker worker2) {
            this.prefetch = prefetch2;
            this.queue = queue2;
            this.limit = prefetch2 - (prefetch2 >> 2);
            this.worker = worker2;
        }

        public final void onNext(T t) {
            if (!this.done) {
                if (!this.queue.offer(t)) {
                    this.f398s.cancel();
                    onError(new MissingBackpressureException("Queue is full?!"));
                    return;
                }
                schedule();
            }
        }

        public final void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.error = t;
            this.done = true;
            schedule();
        }

        public final void onComplete() {
            if (!this.done) {
                this.done = true;
                schedule();
            }
        }

        public final void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
                schedule();
            }
        }

        public final void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.f398s.cancel();
                this.worker.dispose();
                if (getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public final void schedule() {
            if (getAndIncrement() == 0) {
                this.worker.schedule(this);
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelRunOn$MultiWorkerCallback */
    final class MultiWorkerCallback implements WorkerCallback {
        final Subscriber<T>[] parents;
        final Subscriber<? super T>[] subscribers;

        MultiWorkerCallback(Subscriber<? super T>[] subscribers2, Subscriber<T>[] parents2) {
            this.subscribers = subscribers2;
            this.parents = parents2;
        }

        public void onWorker(int i, Worker w) {
            ParallelRunOn.this.createSubscriber(i, this.subscribers, this.parents, w);
        }
    }

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelRunOn$RunOnConditionalSubscriber */
    static final class RunOnConditionalSubscriber<T> extends BaseRunOnSubscriber<T> {
        private static final long serialVersionUID = 1075119423897941642L;
        final ConditionalSubscriber<? super T> actual;

        RunOnConditionalSubscriber(ConditionalSubscriber<? super T> actual2, int prefetch, SpscArrayQueue<T> queue, Worker worker) {
            super(prefetch, queue, worker);
            this.actual = actual2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f398s, s)) {
                this.f398s = s;
                this.actual.onSubscribe(this);
                s.request((long) this.prefetch);
            }
        }

        public void run() {
            long e;
            int missed = 1;
            int c = this.consumed;
            SpscArrayQueue<T> q = this.queue;
            ConditionalSubscriber<? super T> a = this.actual;
            int lim = this.limit;
            while (true) {
                long r = this.requested.get();
                long e2 = 0;
                while (e2 != r) {
                    if (this.cancelled) {
                        q.clear();
                        return;
                    }
                    boolean d = this.done;
                    if (d) {
                        Throwable ex = this.error;
                        if (ex != null) {
                            q.clear();
                            a.onError(ex);
                            this.worker.dispose();
                            return;
                        }
                    }
                    T v = q.poll();
                    boolean empty = v == null;
                    if (d && empty) {
                        a.onComplete();
                        this.worker.dispose();
                        return;
                    } else if (empty) {
                        break;
                    } else {
                        if (a.tryOnNext(v)) {
                            e2++;
                        }
                        c++;
                        int p = c;
                        if (p == lim) {
                            c = 0;
                            e = e2;
                            this.f398s.request((long) p);
                        } else {
                            e = e2;
                        }
                        e2 = e;
                    }
                }
                if (e2 == r) {
                    if (this.cancelled) {
                        q.clear();
                        return;
                    } else if (this.done) {
                        Throwable ex2 = this.error;
                        if (ex2 != null) {
                            q.clear();
                            a.onError(ex2);
                            this.worker.dispose();
                            return;
                        } else if (q.isEmpty()) {
                            a.onComplete();
                            this.worker.dispose();
                            return;
                        }
                    }
                }
                if (!(e2 == 0 || r == Long.MAX_VALUE)) {
                    this.requested.addAndGet(-e2);
                }
                int w = get();
                if (w == missed) {
                    this.consumed = c;
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

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelRunOn$RunOnSubscriber */
    static final class RunOnSubscriber<T> extends BaseRunOnSubscriber<T> {
        private static final long serialVersionUID = 1075119423897941642L;
        final Subscriber<? super T> actual;

        RunOnSubscriber(Subscriber<? super T> actual2, int prefetch, SpscArrayQueue<T> queue, Worker worker) {
            super(prefetch, queue, worker);
            this.actual = actual2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f398s, s)) {
                this.f398s = s;
                this.actual.onSubscribe(this);
                s.request((long) this.prefetch);
            }
        }

        public void run() {
            long e;
            int missed = 1;
            int c = this.consumed;
            SpscArrayQueue<T> q = this.queue;
            Subscriber<? super T> a = this.actual;
            int lim = this.limit;
            while (true) {
                long r = this.requested.get();
                long e2 = 0;
                while (e2 != r) {
                    if (this.cancelled) {
                        q.clear();
                        return;
                    }
                    boolean d = this.done;
                    if (d) {
                        Throwable ex = this.error;
                        if (ex != null) {
                            q.clear();
                            a.onError(ex);
                            this.worker.dispose();
                            return;
                        }
                    }
                    T v = q.poll();
                    boolean empty = v == null;
                    if (d && empty) {
                        a.onComplete();
                        this.worker.dispose();
                        return;
                    } else if (empty) {
                        break;
                    } else {
                        a.onNext(v);
                        long e3 = e2 + 1;
                        c++;
                        int p = c;
                        if (p == lim) {
                            c = 0;
                            e = e3;
                            this.f398s.request((long) p);
                        } else {
                            e = e3;
                        }
                        e2 = e;
                    }
                }
                if (e2 == r) {
                    if (this.cancelled) {
                        q.clear();
                        return;
                    } else if (this.done) {
                        Throwable ex2 = this.error;
                        if (ex2 != null) {
                            q.clear();
                            a.onError(ex2);
                            this.worker.dispose();
                            return;
                        } else if (q.isEmpty()) {
                            a.onComplete();
                            this.worker.dispose();
                            return;
                        }
                    }
                }
                if (!(e2 == 0 || r == Long.MAX_VALUE)) {
                    this.requested.addAndGet(-e2);
                }
                int w = get();
                if (w == missed) {
                    this.consumed = c;
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

    public ParallelRunOn(ParallelFlowable<? extends T> parent, Scheduler scheduler2, int prefetch2) {
        this.source = parent;
        this.scheduler = scheduler2;
        this.prefetch = prefetch2;
    }

    public void subscribe(Subscriber<? super T>[] subscribers) {
        if (validate(subscribers)) {
            int n = subscribers.length;
            Subscriber<T>[] parents = new Subscriber[n];
            if (this.scheduler instanceof SchedulerMultiWorkerSupport) {
                ((SchedulerMultiWorkerSupport) this.scheduler).createWorkers(n, new MultiWorkerCallback(subscribers, parents));
            } else {
                for (int i = 0; i < n; i++) {
                    createSubscriber(i, subscribers, parents, this.scheduler.createWorker());
                }
            }
            this.source.subscribe(parents);
        }
    }

    /* access modifiers changed from: 0000 */
    public void createSubscriber(int i, Subscriber<? super T>[] subscribers, Subscriber<T>[] parents, Worker worker) {
        Subscriber<? super T> a = subscribers[i];
        SpscArrayQueue<T> q = new SpscArrayQueue<>(this.prefetch);
        if (a instanceof ConditionalSubscriber) {
            parents[i] = new RunOnConditionalSubscriber((ConditionalSubscriber) a, this.prefetch, q, worker);
        } else {
            parents[i] = new RunOnSubscriber(a, this.prefetch, q, worker);
        }
    }

    public int parallelism() {
        return this.source.parallelism();
    }
}
