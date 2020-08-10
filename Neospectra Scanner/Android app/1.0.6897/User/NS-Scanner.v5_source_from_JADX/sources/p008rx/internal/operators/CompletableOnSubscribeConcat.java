package p008rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import p008rx.Completable;
import p008rx.Completable.OnSubscribe;
import p008rx.CompletableSubscriber;
import p008rx.Observable;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.exceptions.MissingBackpressureException;
import p008rx.internal.util.unsafe.SpscArrayQueue;
import p008rx.plugins.RxJavaHooks;
import p008rx.subscriptions.SerialSubscription;

/* renamed from: rx.internal.operators.CompletableOnSubscribeConcat */
public final class CompletableOnSubscribeConcat implements OnSubscribe {
    final int prefetch;
    final Observable<Completable> sources;

    /* renamed from: rx.internal.operators.CompletableOnSubscribeConcat$CompletableConcatSubscriber */
    static final class CompletableConcatSubscriber extends Subscriber<Completable> {
        final CompletableSubscriber actual;
        volatile boolean done;
        final ConcatInnerSubscriber inner = new ConcatInnerSubscriber();
        final AtomicBoolean once = new AtomicBoolean();
        final SpscArrayQueue<Completable> queue;

        /* renamed from: sr */
        final SerialSubscription f895sr = new SerialSubscription();
        final AtomicInteger wip = new AtomicInteger();

        /* renamed from: rx.internal.operators.CompletableOnSubscribeConcat$CompletableConcatSubscriber$ConcatInnerSubscriber */
        final class ConcatInnerSubscriber implements CompletableSubscriber {
            ConcatInnerSubscriber() {
            }

            public void onSubscribe(Subscription d) {
                CompletableConcatSubscriber.this.f895sr.set(d);
            }

            public void onError(Throwable e) {
                CompletableConcatSubscriber.this.innerError(e);
            }

            public void onCompleted() {
                CompletableConcatSubscriber.this.innerComplete();
            }
        }

        public CompletableConcatSubscriber(CompletableSubscriber actual2, int prefetch) {
            this.actual = actual2;
            this.queue = new SpscArrayQueue<>(prefetch);
            add(this.f895sr);
            request((long) prefetch);
        }

        public void onNext(Completable t) {
            if (!this.queue.offer(t)) {
                onError(new MissingBackpressureException());
                return;
            }
            if (this.wip.getAndIncrement() == 0) {
                next();
            }
        }

        public void onError(Throwable t) {
            if (this.once.compareAndSet(false, true)) {
                this.actual.onError(t);
            } else {
                RxJavaHooks.onError(t);
            }
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                if (this.wip.getAndIncrement() == 0) {
                    next();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerError(Throwable e) {
            unsubscribe();
            onError(e);
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete() {
            if (this.wip.decrementAndGet() != 0) {
                next();
            }
            if (!this.done) {
                request(1);
            }
        }

        /* access modifiers changed from: 0000 */
        public void next() {
            boolean d = this.done;
            Completable c = (Completable) this.queue.poll();
            if (c != null) {
                c.unsafeSubscribe((CompletableSubscriber) this.inner);
            } else if (d) {
                if (this.once.compareAndSet(false, true)) {
                    this.actual.onCompleted();
                }
            } else {
                RxJavaHooks.onError(new IllegalStateException("Queue is empty?!"));
            }
        }
    }

    public CompletableOnSubscribeConcat(Observable<? extends Completable> sources2, int prefetch2) {
        this.sources = sources2;
        this.prefetch = prefetch2;
    }

    public void call(CompletableSubscriber s) {
        CompletableConcatSubscriber parent = new CompletableConcatSubscriber(s, this.prefetch);
        s.onSubscribe(parent);
        this.sources.subscribe((Subscriber<? super T>) parent);
    }
}
