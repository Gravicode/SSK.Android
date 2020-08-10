package p005io.reactivex.internal.operators.completable;

import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.Scheduler;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.completable.CompletableDisposeOn */
public final class CompletableDisposeOn extends Completable {
    final Scheduler scheduler;
    final CompletableSource source;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableDisposeOn$CompletableObserverImplementation */
    static final class CompletableObserverImplementation implements CompletableObserver, Disposable, Runnable {

        /* renamed from: d */
        Disposable f102d;
        volatile boolean disposed;

        /* renamed from: s */
        final CompletableObserver f103s;
        final Scheduler scheduler;

        CompletableObserverImplementation(CompletableObserver s, Scheduler scheduler2) {
            this.f103s = s;
            this.scheduler = scheduler2;
        }

        public void onComplete() {
            if (!this.disposed) {
                this.f103s.onComplete();
            }
        }

        public void onError(Throwable e) {
            if (this.disposed) {
                RxJavaPlugins.onError(e);
            } else {
                this.f103s.onError(e);
            }
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f102d, d)) {
                this.f102d = d;
                this.f103s.onSubscribe(this);
            }
        }

        public void dispose() {
            this.disposed = true;
            this.scheduler.scheduleDirect(this);
        }

        public boolean isDisposed() {
            return this.disposed;
        }

        public void run() {
            this.f102d.dispose();
            this.f102d = DisposableHelper.DISPOSED;
        }
    }

    public CompletableDisposeOn(CompletableSource source2, Scheduler scheduler2) {
        this.source = source2;
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        this.source.subscribe(new CompletableObserverImplementation(s, this.scheduler));
    }
}
