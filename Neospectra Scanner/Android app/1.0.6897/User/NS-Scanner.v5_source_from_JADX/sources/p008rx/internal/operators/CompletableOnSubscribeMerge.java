package p008rx.internal.operators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Completable;
import p008rx.Completable.OnSubscribe;
import p008rx.CompletableSubscriber;
import p008rx.Observable;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.exceptions.CompositeException;
import p008rx.plugins.RxJavaHooks;
import p008rx.subscriptions.CompositeSubscription;

/* renamed from: rx.internal.operators.CompletableOnSubscribeMerge */
public final class CompletableOnSubscribeMerge implements OnSubscribe {
    final boolean delayErrors;
    final int maxConcurrency;
    final Observable<Completable> source;

    /* renamed from: rx.internal.operators.CompletableOnSubscribeMerge$CompletableMergeSubscriber */
    static final class CompletableMergeSubscriber extends Subscriber<Completable> {
        final CompletableSubscriber actual;
        final boolean delayErrors;
        volatile boolean done;
        final AtomicReference<Queue<Throwable>> errors = new AtomicReference<>();
        final AtomicBoolean once = new AtomicBoolean();
        final CompositeSubscription set = new CompositeSubscription();
        final AtomicInteger wip = new AtomicInteger(1);

        public CompletableMergeSubscriber(CompletableSubscriber actual2, int maxConcurrency, boolean delayErrors2) {
            this.actual = actual2;
            this.delayErrors = delayErrors2;
            if (maxConcurrency == Integer.MAX_VALUE) {
                request(Long.MAX_VALUE);
            } else {
                request((long) maxConcurrency);
            }
        }

        /* access modifiers changed from: 0000 */
        public Queue<Throwable> getOrCreateErrors() {
            Queue<Throwable> q = (Queue) this.errors.get();
            if (q != null) {
                return q;
            }
            Queue<Throwable> q2 = new ConcurrentLinkedQueue<>();
            if (this.errors.compareAndSet(null, q2)) {
                return q2;
            }
            return (Queue) this.errors.get();
        }

        public void onNext(Completable t) {
            if (!this.done) {
                this.wip.getAndIncrement();
                t.unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {

                    /* renamed from: d */
                    Subscription f898d;
                    boolean innerDone;

                    public void onSubscribe(Subscription d) {
                        this.f898d = d;
                        CompletableMergeSubscriber.this.set.add(d);
                    }

                    public void onError(Throwable e) {
                        if (this.innerDone) {
                            RxJavaHooks.onError(e);
                            return;
                        }
                        this.innerDone = true;
                        CompletableMergeSubscriber.this.set.remove(this.f898d);
                        CompletableMergeSubscriber.this.getOrCreateErrors().offer(e);
                        CompletableMergeSubscriber.this.terminate();
                        if (CompletableMergeSubscriber.this.delayErrors && !CompletableMergeSubscriber.this.done) {
                            CompletableMergeSubscriber.this.request(1);
                        }
                    }

                    public void onCompleted() {
                        if (!this.innerDone) {
                            this.innerDone = true;
                            CompletableMergeSubscriber.this.set.remove(this.f898d);
                            CompletableMergeSubscriber.this.terminate();
                            if (!CompletableMergeSubscriber.this.done) {
                                CompletableMergeSubscriber.this.request(1);
                            }
                        }
                    }
                });
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaHooks.onError(t);
                return;
            }
            getOrCreateErrors().offer(t);
            this.done = true;
            terminate();
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                terminate();
            }
        }

        /* access modifiers changed from: 0000 */
        public void terminate() {
            if (this.wip.decrementAndGet() == 0) {
                Queue<Throwable> q = (Queue) this.errors.get();
                if (q == null || q.isEmpty()) {
                    this.actual.onCompleted();
                    return;
                }
                Throwable e = CompletableOnSubscribeMerge.collectErrors(q);
                if (this.once.compareAndSet(false, true)) {
                    this.actual.onError(e);
                } else {
                    RxJavaHooks.onError(e);
                }
            } else if (!this.delayErrors) {
                Queue<Throwable> q2 = (Queue) this.errors.get();
                if (q2 != null && !q2.isEmpty()) {
                    Throwable e2 = CompletableOnSubscribeMerge.collectErrors(q2);
                    if (this.once.compareAndSet(false, true)) {
                        this.actual.onError(e2);
                    } else {
                        RxJavaHooks.onError(e2);
                    }
                }
            }
        }
    }

    public CompletableOnSubscribeMerge(Observable<? extends Completable> source2, int maxConcurrency2, boolean delayErrors2) {
        this.source = source2;
        this.maxConcurrency = maxConcurrency2;
        this.delayErrors = delayErrors2;
    }

    public void call(CompletableSubscriber s) {
        CompletableMergeSubscriber parent = new CompletableMergeSubscriber(s, this.maxConcurrency, this.delayErrors);
        s.onSubscribe(parent);
        this.source.subscribe((Subscriber<? super T>) parent);
    }

    public static Throwable collectErrors(Queue<Throwable> q) {
        List<Throwable> list = new ArrayList<>();
        while (true) {
            Throwable th = (Throwable) q.poll();
            Throwable t = th;
            if (th == null) {
                break;
            }
            list.add(t);
        }
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() == 1) {
            return (Throwable) list.get(0);
        }
        return new CompositeException((Collection<? extends Throwable>) list);
    }
}
