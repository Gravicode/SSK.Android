package p008rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import p008rx.Completable;
import p008rx.Completable.OnSubscribe;
import p008rx.CompletableSubscriber;
import p008rx.Subscription;
import p008rx.plugins.RxJavaHooks;
import p008rx.subscriptions.CompositeSubscription;

/* renamed from: rx.internal.operators.CompletableOnSubscribeMergeArray */
public final class CompletableOnSubscribeMergeArray implements OnSubscribe {
    final Completable[] sources;

    public CompletableOnSubscribeMergeArray(Completable[] sources2) {
        this.sources = sources2;
    }

    public void call(CompletableSubscriber s) {
        CompletableSubscriber completableSubscriber = s;
        CompositeSubscription set = new CompositeSubscription();
        boolean z = true;
        AtomicInteger wip = new AtomicInteger(this.sources.length + 1);
        AtomicBoolean once = new AtomicBoolean();
        completableSubscriber.onSubscribe(set);
        Completable[] arr$ = this.sources;
        int len$ = arr$.length;
        boolean z2 = false;
        int i$ = 0;
        while (true) {
            int i$2 = i$;
            if (i$2 < len$) {
                Completable c = arr$[i$2];
                if (!set.isUnsubscribed()) {
                    if (c == null) {
                        set.unsubscribe();
                        NullPointerException npe = new NullPointerException("A completable source is null");
                        if (once.compareAndSet(z2, z)) {
                            completableSubscriber.onError(npe);
                            return;
                        }
                        RxJavaHooks.onError(npe);
                    }
                    final CompositeSubscription compositeSubscription = set;
                    final AtomicBoolean atomicBoolean = once;
                    C15421 r9 = r0;
                    final CompletableSubscriber completableSubscriber2 = completableSubscriber;
                    Completable c2 = c;
                    final AtomicInteger atomicInteger = wip;
                    C15421 r0 = new CompletableSubscriber() {
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
                    c2.unsafeSubscribe((CompletableSubscriber) r9);
                    i$ = i$2 + 1;
                    z = true;
                    z2 = false;
                } else {
                    return;
                }
            } else {
                if (wip.decrementAndGet() == 0 && once.compareAndSet(false, true)) {
                    s.onCompleted();
                }
                return;
            }
        }
    }
}
