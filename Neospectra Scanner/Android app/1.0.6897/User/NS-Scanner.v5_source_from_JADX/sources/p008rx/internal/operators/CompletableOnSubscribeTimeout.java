package p008rx.internal.operators;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import p008rx.Completable;
import p008rx.Completable.OnSubscribe;
import p008rx.CompletableSubscriber;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Subscription;
import p008rx.functions.Action0;
import p008rx.plugins.RxJavaHooks;
import p008rx.subscriptions.CompositeSubscription;

/* renamed from: rx.internal.operators.CompletableOnSubscribeTimeout */
public final class CompletableOnSubscribeTimeout implements OnSubscribe {
    final Completable other;
    final Scheduler scheduler;
    final Completable source;
    final long timeout;
    final TimeUnit unit;

    public CompletableOnSubscribeTimeout(Completable source2, long timeout2, TimeUnit unit2, Scheduler scheduler2, Completable other2) {
        this.source = source2;
        this.timeout = timeout2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.other = other2;
    }

    public void call(final CompletableSubscriber s) {
        final CompositeSubscription set = new CompositeSubscription();
        s.onSubscribe(set);
        final AtomicBoolean once = new AtomicBoolean();
        Worker w = this.scheduler.createWorker();
        set.add(w);
        w.schedule(new Action0() {
            public void call() {
                if (once.compareAndSet(false, true)) {
                    set.clear();
                    if (CompletableOnSubscribeTimeout.this.other == null) {
                        s.onError(new TimeoutException());
                    } else {
                        CompletableOnSubscribeTimeout.this.other.unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {
                            public void onSubscribe(Subscription d) {
                                set.add(d);
                            }

                            public void onError(Throwable e) {
                                set.unsubscribe();
                                s.onError(e);
                            }

                            public void onCompleted() {
                                set.unsubscribe();
                                s.onCompleted();
                            }
                        });
                    }
                }
            }
        }, this.timeout, this.unit);
        this.source.unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {
            public void onSubscribe(Subscription d) {
                set.add(d);
            }

            public void onError(Throwable e) {
                if (once.compareAndSet(false, true)) {
                    set.unsubscribe();
                    s.onError(e);
                    return;
                }
                RxJavaHooks.onError(e);
            }

            public void onCompleted() {
                if (once.compareAndSet(false, true)) {
                    set.unsubscribe();
                    s.onCompleted();
                }
            }
        });
    }
}
