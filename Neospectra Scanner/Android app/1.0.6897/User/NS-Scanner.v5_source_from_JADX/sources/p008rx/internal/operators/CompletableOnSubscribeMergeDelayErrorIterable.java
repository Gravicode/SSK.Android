package p008rx.internal.operators;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import p008rx.Completable;
import p008rx.Completable.OnSubscribe;
import p008rx.CompletableSubscriber;
import p008rx.Subscription;
import p008rx.internal.util.unsafe.MpscLinkedQueue;
import p008rx.subscriptions.CompositeSubscription;

/* renamed from: rx.internal.operators.CompletableOnSubscribeMergeDelayErrorIterable */
public final class CompletableOnSubscribeMergeDelayErrorIterable implements OnSubscribe {
    final Iterable<? extends Completable> sources;

    public CompletableOnSubscribeMergeDelayErrorIterable(Iterable<? extends Completable> sources2) {
        this.sources = sources2;
    }

    public void call(CompletableSubscriber s) {
        CompositeSubscription set = new CompositeSubscription();
        s.onSubscribe(set);
        try {
            Iterator it = this.sources.iterator();
            if (it == null) {
                s.onError(new NullPointerException("The source iterator returned is null"));
                return;
            }
            AtomicInteger wip = new AtomicInteger(1);
            MpscLinkedQueue mpscLinkedQueue = new MpscLinkedQueue();
            while (true) {
                MpscLinkedQueue mpscLinkedQueue2 = mpscLinkedQueue;
                if (!set.isUnsubscribed()) {
                    try {
                        boolean b = it.hasNext();
                        if (!b) {
                            if (wip.decrementAndGet() == 0) {
                                if (mpscLinkedQueue2.isEmpty()) {
                                    s.onCompleted();
                                } else {
                                    s.onError(CompletableOnSubscribeMerge.collectErrors(mpscLinkedQueue2));
                                }
                            }
                            return;
                        } else if (!set.isUnsubscribed()) {
                            try {
                                Completable c = (Completable) it.next();
                                if (!set.isUnsubscribed()) {
                                    if (c == null) {
                                        mpscLinkedQueue2.offer(new NullPointerException("A completable source is null"));
                                        if (wip.decrementAndGet() == 0) {
                                            if (mpscLinkedQueue2.isEmpty()) {
                                                s.onCompleted();
                                            } else {
                                                s.onError(CompletableOnSubscribeMerge.collectErrors(mpscLinkedQueue2));
                                            }
                                        }
                                        return;
                                    }
                                    wip.getAndIncrement();
                                    final CompositeSubscription compositeSubscription = set;
                                    final MpscLinkedQueue mpscLinkedQueue3 = mpscLinkedQueue2;
                                    final AtomicInteger atomicInteger = wip;
                                    final CompletableSubscriber completableSubscriber = s;
                                    C15441 r1 = new CompletableSubscriber() {
                                        public void onSubscribe(Subscription d) {
                                            compositeSubscription.add(d);
                                        }

                                        public void onError(Throwable e) {
                                            mpscLinkedQueue3.offer(e);
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
                                            if (mpscLinkedQueue3.isEmpty()) {
                                                completableSubscriber.onCompleted();
                                            } else {
                                                completableSubscriber.onError(CompletableOnSubscribeMerge.collectErrors(mpscLinkedQueue3));
                                            }
                                        }
                                    };
                                    c.unsafeSubscribe((CompletableSubscriber) r1);
                                    mpscLinkedQueue = mpscLinkedQueue2;
                                    boolean z = b;
                                    Completable completable = c;
                                } else {
                                    return;
                                }
                            } catch (Throwable e) {
                                mpscLinkedQueue2.offer(e);
                                if (wip.decrementAndGet() == 0) {
                                    if (mpscLinkedQueue2.isEmpty()) {
                                        s.onCompleted();
                                    } else {
                                        s.onError(CompletableOnSubscribeMerge.collectErrors(mpscLinkedQueue2));
                                    }
                                }
                                return;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable e2) {
                        mpscLinkedQueue2.offer(e2);
                        if (wip.decrementAndGet() == 0) {
                            if (mpscLinkedQueue2.isEmpty()) {
                                s.onCompleted();
                            } else {
                                s.onError(CompletableOnSubscribeMerge.collectErrors(mpscLinkedQueue2));
                            }
                        }
                        return;
                    }
                } else {
                    return;
                }
            }
        } catch (Throwable e3) {
            s.onError(e3);
        }
    }
}
