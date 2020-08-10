package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicBoolean;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.Scheduler;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableUnsubscribeOn */
public final class ObservableUnsubscribeOn<T> extends AbstractObservableWithUpstream<T, T> {
    final Scheduler scheduler;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableUnsubscribeOn$UnsubscribeObserver */
    static final class UnsubscribeObserver<T> extends AtomicBoolean implements Observer<T>, Disposable {
        private static final long serialVersionUID = 1015244841293359600L;
        final Observer<? super T> actual;

        /* renamed from: s */
        Disposable f369s;
        final Scheduler scheduler;

        /* renamed from: io.reactivex.internal.operators.observable.ObservableUnsubscribeOn$UnsubscribeObserver$DisposeTask */
        final class DisposeTask implements Runnable {
            DisposeTask() {
            }

            public void run() {
                UnsubscribeObserver.this.f369s.dispose();
            }
        }

        UnsubscribeObserver(Observer<? super T> actual2, Scheduler scheduler2) {
            this.actual = actual2;
            this.scheduler = scheduler2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f369s, s)) {
                this.f369s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!get()) {
                this.actual.onNext(t);
            }
        }

        public void onError(Throwable t) {
            if (get()) {
                RxJavaPlugins.onError(t);
            } else {
                this.actual.onError(t);
            }
        }

        public void onComplete() {
            if (!get()) {
                this.actual.onComplete();
            }
        }

        public void dispose() {
            if (compareAndSet(false, true)) {
                this.scheduler.scheduleDirect(new DisposeTask());
            }
        }

        public boolean isDisposed() {
            return get();
        }
    }

    public ObservableUnsubscribeOn(ObservableSource<T> source, Scheduler scheduler2) {
        super(source);
        this.scheduler = scheduler2;
    }

    public void subscribeActual(Observer<? super T> t) {
        this.source.subscribe(new UnsubscribeObserver(t, this.scheduler));
    }
}
