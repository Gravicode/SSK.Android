package p005io.reactivex.internal.operators.completable;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.Scheduler;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.completable.CompletableObserveOn */
public final class CompletableObserveOn extends Completable {
    final Scheduler scheduler;
    final CompletableSource source;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableObserveOn$ObserveOnCompletableObserver */
    static final class ObserveOnCompletableObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable, Runnable {
        private static final long serialVersionUID = 8571289934935992137L;
        final CompletableObserver actual;
        Throwable error;
        final Scheduler scheduler;

        ObserveOnCompletableObserver(CompletableObserver actual2, Scheduler scheduler2) {
            this.actual = actual2;
            this.scheduler = scheduler2;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.setOnce(this, d)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onError(Throwable e) {
            this.error = e;
            DisposableHelper.replace(this, this.scheduler.scheduleDirect(this));
        }

        public void onComplete() {
            DisposableHelper.replace(this, this.scheduler.scheduleDirect(this));
        }

        public void run() {
            Throwable ex = this.error;
            if (ex != null) {
                this.error = null;
                this.actual.onError(ex);
                return;
            }
            this.actual.onComplete();
        }
    }

    public CompletableObserveOn(CompletableSource source2, Scheduler scheduler2) {
        this.source = source2;
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        this.source.subscribe(new ObserveOnCompletableObserver(s, this.scheduler));
    }
}
