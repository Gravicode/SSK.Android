package p008rx.internal.operators;

import java.util.concurrent.atomic.AtomicInteger;
import p008rx.Completable;
import p008rx.Completable.OnSubscribe;
import p008rx.CompletableSubscriber;
import p008rx.Subscription;
import p008rx.subscriptions.SerialSubscription;

/* renamed from: rx.internal.operators.CompletableOnSubscribeConcatArray */
public final class CompletableOnSubscribeConcatArray implements OnSubscribe {
    final Completable[] sources;

    /* renamed from: rx.internal.operators.CompletableOnSubscribeConcatArray$ConcatInnerSubscriber */
    static final class ConcatInnerSubscriber extends AtomicInteger implements CompletableSubscriber {
        private static final long serialVersionUID = -7965400327305809232L;
        final CompletableSubscriber actual;
        int index;

        /* renamed from: sd */
        final SerialSubscription f896sd = new SerialSubscription();
        final Completable[] sources;

        public ConcatInnerSubscriber(CompletableSubscriber actual2, Completable[] sources2) {
            this.actual = actual2;
            this.sources = sources2;
        }

        public void onSubscribe(Subscription d) {
            this.f896sd.set(d);
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void onCompleted() {
            next();
        }

        /* access modifiers changed from: 0000 */
        public void next() {
            if (!this.f896sd.isUnsubscribed() && getAndIncrement() == 0) {
                Completable[] a = this.sources;
                while (!this.f896sd.isUnsubscribed()) {
                    int idx = this.index;
                    this.index = idx + 1;
                    if (idx == a.length) {
                        this.actual.onCompleted();
                        return;
                    }
                    a[idx].unsafeSubscribe((CompletableSubscriber) this);
                    if (decrementAndGet() == 0) {
                        return;
                    }
                }
            }
        }
    }

    public CompletableOnSubscribeConcatArray(Completable[] sources2) {
        this.sources = sources2;
    }

    public void call(CompletableSubscriber s) {
        ConcatInnerSubscriber inner = new ConcatInnerSubscriber(s, this.sources);
        s.onSubscribe(inner.f896sd);
        inner.next();
    }
}
