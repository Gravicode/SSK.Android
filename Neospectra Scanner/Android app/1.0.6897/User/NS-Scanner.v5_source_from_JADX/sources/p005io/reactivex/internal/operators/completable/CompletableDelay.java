package p005io.reactivex.internal.operators.completable;

import java.util.concurrent.TimeUnit;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.Scheduler;
import p005io.reactivex.disposables.CompositeDisposable;
import p005io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.internal.operators.completable.CompletableDelay */
public final class CompletableDelay extends Completable {
    final long delay;
    final boolean delayError;
    final Scheduler scheduler;
    final CompletableSource source;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableDelay$Delay */
    final class Delay implements CompletableObserver {

        /* renamed from: s */
        final CompletableObserver f99s;
        private final CompositeDisposable set;

        /* renamed from: io.reactivex.internal.operators.completable.CompletableDelay$Delay$OnComplete */
        final class OnComplete implements Runnable {
            OnComplete() {
            }

            public void run() {
                Delay.this.f99s.onComplete();
            }
        }

        /* renamed from: io.reactivex.internal.operators.completable.CompletableDelay$Delay$OnError */
        final class OnError implements Runnable {

            /* renamed from: e */
            private final Throwable f100e;

            OnError(Throwable e) {
                this.f100e = e;
            }

            public void run() {
                Delay.this.f99s.onError(this.f100e);
            }
        }

        Delay(CompositeDisposable set2, CompletableObserver s) {
            this.set = set2;
            this.f99s = s;
        }

        public void onComplete() {
            this.set.add(CompletableDelay.this.scheduler.scheduleDirect(new OnComplete(), CompletableDelay.this.delay, CompletableDelay.this.unit));
        }

        public void onError(Throwable e) {
            this.set.add(CompletableDelay.this.scheduler.scheduleDirect(new OnError(e), CompletableDelay.this.delayError ? CompletableDelay.this.delay : 0, CompletableDelay.this.unit));
        }

        public void onSubscribe(Disposable d) {
            this.set.add(d);
            this.f99s.onSubscribe(this.set);
        }
    }

    public CompletableDelay(CompletableSource source2, long delay2, TimeUnit unit2, Scheduler scheduler2, boolean delayError2) {
        this.source = source2;
        this.delay = delay2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.delayError = delayError2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        this.source.subscribe(new Delay(new CompositeDisposable(), s));
    }
}
