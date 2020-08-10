package p008rx.internal.operators;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import p008rx.Completable;
import p008rx.Completable.OnSubscribe;
import p008rx.CompletableSubscriber;
import p008rx.Subscription;
import p008rx.subscriptions.SerialSubscription;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.CompletableOnSubscribeConcatIterable */
public final class CompletableOnSubscribeConcatIterable implements OnSubscribe {
    final Iterable<? extends Completable> sources;

    /* renamed from: rx.internal.operators.CompletableOnSubscribeConcatIterable$ConcatInnerSubscriber */
    static final class ConcatInnerSubscriber extends AtomicInteger implements CompletableSubscriber {
        private static final long serialVersionUID = -7965400327305809232L;
        final CompletableSubscriber actual;

        /* renamed from: sd */
        final SerialSubscription f897sd = new SerialSubscription();
        final Iterator<? extends Completable> sources;

        public ConcatInnerSubscriber(CompletableSubscriber actual2, Iterator<? extends Completable> sources2) {
            this.actual = actual2;
            this.sources = sources2;
        }

        public void onSubscribe(Subscription d) {
            this.f897sd.set(d);
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void onCompleted() {
            next();
        }

        /* access modifiers changed from: 0000 */
        public void next() {
            if (!this.f897sd.isUnsubscribed() && getAndIncrement() == 0) {
                Iterator<? extends Completable> a = this.sources;
                while (!this.f897sd.isUnsubscribed()) {
                    try {
                        if (!a.hasNext()) {
                            this.actual.onCompleted();
                            return;
                        }
                        try {
                            Completable c = (Completable) a.next();
                            if (c == null) {
                                this.actual.onError(new NullPointerException("The completable returned is null"));
                                return;
                            }
                            c.unsafeSubscribe((CompletableSubscriber) this);
                            if (decrementAndGet() == 0) {
                                return;
                            }
                        } catch (Throwable ex) {
                            this.actual.onError(ex);
                            return;
                        }
                    } catch (Throwable ex2) {
                        this.actual.onError(ex2);
                        return;
                    }
                }
            }
        }
    }

    public CompletableOnSubscribeConcatIterable(Iterable<? extends Completable> sources2) {
        this.sources = sources2;
    }

    public void call(CompletableSubscriber s) {
        try {
            Iterator<? extends Completable> it = this.sources.iterator();
            if (it == null) {
                s.onSubscribe(Subscriptions.unsubscribed());
                s.onError(new NullPointerException("The iterator returned is null"));
                return;
            }
            ConcatInnerSubscriber inner = new ConcatInnerSubscriber(s, it);
            s.onSubscribe(inner.f897sd);
            inner.next();
        } catch (Throwable e) {
            s.onSubscribe(Subscriptions.unsubscribed());
            s.onError(e);
        }
    }
}
