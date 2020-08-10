package p008rx.internal.operators;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import p008rx.Completable;
import p008rx.Completable.OnSubscribe;
import p008rx.CompletableSubscriber;
import p008rx.Subscription;
import p008rx.subscriptions.CompositeSubscription;

/* renamed from: rx.internal.operators.CompletableOnSubscribeMergeDelayErrorArray */
public final class CompletableOnSubscribeMergeDelayErrorArray implements OnSubscribe {
    final Completable[] sources;

    public CompletableOnSubscribeMergeDelayErrorArray(Completable[] sources2) {
        this.sources = sources2;
    }

    public void call(CompletableSubscriber s) {
        CompositeSubscription set = new CompositeSubscription();
        AtomicInteger wip = new AtomicInteger(this.sources.length + 1);
        ConcurrentLinkedQueue concurrentLinkedQueue = new ConcurrentLinkedQueue();
        s.onSubscribe(set);
        Completable[] arr$ = this.sources;
        int len$ = arr$.length;
        int i$ = 0;
        while (true) {
            int i$2 = i$;
            if (i$2 < len$) {
                Completable c = arr$[i$2];
                if (!set.isUnsubscribed()) {
                    if (c == null) {
                        concurrentLinkedQueue.offer(new NullPointerException("A completable source is null"));
                        wip.decrementAndGet();
                    } else {
                        final CompositeSubscription compositeSubscription = set;
                        final ConcurrentLinkedQueue concurrentLinkedQueue2 = concurrentLinkedQueue;
                        final AtomicInteger atomicInteger = wip;
                        final CompletableSubscriber completableSubscriber = s;
                        C15431 r1 = new CompletableSubscriber() {
                            public void onSubscribe(Subscription d) {
                                compositeSubscription.add(d);
                            }

                            public void onError(Throwable e) {
                                concurrentLinkedQueue2.offer(e);
                                tryTerminate();
                            }

                            public void onCompleted() {
                                tryTerminate();
                            }

                            /* access modifiers changed from: 0000 */
                            public void tryTerminate() {
                                if (atomicInteger.decrementAndGet() != 0) {
                                    return;
                                }
                                if (concurrentLinkedQueue2.isEmpty()) {
                                    completableSubscriber.onCompleted();
                                } else {
                                    completableSubscriber.onError(CompletableOnSubscribeMerge.collectErrors(concurrentLinkedQueue2));
                                }
                            }
                        };
                        c.unsafeSubscribe((CompletableSubscriber) r1);
                    }
                    i$ = i$2 + 1;
                } else {
                    return;
                }
            } else {
                if (wip.decrementAndGet() == 0) {
                    if (concurrentLinkedQueue.isEmpty()) {
                        s.onCompleted();
                    } else {
                        s.onError(CompletableOnSubscribeMerge.collectErrors(concurrentLinkedQueue));
                    }
                }
                return;
            }
        }
    }
}
