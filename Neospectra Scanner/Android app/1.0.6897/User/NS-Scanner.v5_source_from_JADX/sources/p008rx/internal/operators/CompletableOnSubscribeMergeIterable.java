package p008rx.internal.operators;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import p008rx.Completable;
import p008rx.Completable.OnSubscribe;
import p008rx.CompletableSubscriber;
import p008rx.Subscription;
import p008rx.plugins.RxJavaHooks;
import p008rx.subscriptions.CompositeSubscription;

/* renamed from: rx.internal.operators.CompletableOnSubscribeMergeIterable */
public final class CompletableOnSubscribeMergeIterable implements OnSubscribe {
    final Iterable<? extends Completable> sources;

    public CompletableOnSubscribeMergeIterable(Iterable<? extends Completable> sources2) {
        this.sources = sources2;
    }

    public void call(CompletableSubscriber s) {
        CompletableSubscriber completableSubscriber = s;
        CompositeSubscription set = new CompositeSubscription();
        completableSubscriber.onSubscribe(set);
        try {
            Iterator it = this.sources.iterator();
            if (it == null) {
                completableSubscriber.onError(new NullPointerException("The source iterator returned is null"));
                return;
            }
            boolean z = true;
            AtomicInteger wip = new AtomicInteger(1);
            AtomicBoolean once = new AtomicBoolean();
            boolean z2 = false;
            while (true) {
                AtomicBoolean once2 = once;
                if (!set.isUnsubscribed()) {
                    try {
                        boolean b = it.hasNext();
                        if (!b) {
                            if (wip.decrementAndGet() == 0 && once2.compareAndSet(z2, z)) {
                                s.onCompleted();
                            }
                            return;
                        } else if (!set.isUnsubscribed()) {
                            try {
                                Completable c = (Completable) it.next();
                                if (!set.isUnsubscribed()) {
                                    if (c == null) {
                                        set.unsubscribe();
                                        NullPointerException npe = new NullPointerException("A completable source is null");
                                        if (once2.compareAndSet(z2, z)) {
                                            completableSubscriber.onError(npe);
                                        } else {
                                            RxJavaHooks.onError(npe);
                                        }
                                        return;
                                    }
                                    wip.getAndIncrement();
                                    final CompositeSubscription compositeSubscription = set;
                                    final AtomicBoolean atomicBoolean = once2;
                                    C15451 r11 = r1;
                                    final CompletableSubscriber completableSubscriber2 = completableSubscriber;
                                    Completable c2 = c;
                                    final AtomicInteger atomicInteger = wip;
                                    C15451 r1 = new CompletableSubscriber() {
                                        public void onSubscribe(Subscription d) {
                                            compositeSubscription.add(d);
                                        }

                                        public void onError(Throwable e) {
                                            compositeSubscription.unsubscribe();
                                            if (atomicBoolean.compareAndSet(false, true)) {
                                                completableSubscriber2.onError(e);
                                            } else {
                                                RxJavaHooks.onError(e);
                                            }
                                        }

                                        public void onCompleted() {
                                            if (atomicInteger.decrementAndGet() == 0 && atomicBoolean.compareAndSet(false, true)) {
                                                completableSubscriber2.onCompleted();
                                            }
                                        }
                                    };
                                    c2.unsafeSubscribe((CompletableSubscriber) r11);
                                    Completable completable = c2;
                                    once = once2;
                                    boolean z3 = b;
                                    z = true;
                                    z2 = false;
                                } else {
                                    return;
                                }
                            } catch (Throwable th) {
                                Throwable e = th;
                                set.unsubscribe();
                                if (once2.compareAndSet(false, true)) {
                                    completableSubscriber.onError(e);
                                } else {
                                    RxJavaHooks.onError(e);
                                }
                                return;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable th2) {
                        Throwable e2 = th2;
                        set.unsubscribe();
                        if (once2.compareAndSet(false, true)) {
                            completableSubscriber.onError(e2);
                        } else {
                            RxJavaHooks.onError(e2);
                        }
                        return;
                    }
                } else {
                    return;
                }
            }
        } catch (Throwable th3) {
            completableSubscriber.onError(th3);
        }
    }
}
