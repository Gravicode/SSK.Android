package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.Scheduler;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSubscribeOn */
public final class ObservableSubscribeOn<T> extends AbstractObservableWithUpstream<T, T> {
    final Scheduler scheduler;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSubscribeOn$SubscribeOnObserver */
    static final class SubscribeOnObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable {
        private static final long serialVersionUID = 8094547886072529208L;
        final Observer<? super T> actual;

        /* renamed from: s */
        final AtomicReference<Disposable> f357s = new AtomicReference<>();

        SubscribeOnObserver(Observer<? super T> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Disposable s) {
            DisposableHelper.setOnce(this.f357s, s);
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void dispose() {
            DisposableHelper.dispose(this.f357s);
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        /* access modifiers changed from: 0000 */
        public void setDisposable(Disposable d) {
            DisposableHelper.setOnce(this, d);
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSubscribeOn$SubscribeTask */
    final class SubscribeTask implements Runnable {
        private final SubscribeOnObserver<T> parent;

        SubscribeTask(SubscribeOnObserver<T> parent2) {
            this.parent = parent2;
        }

        public void run() {
            ObservableSubscribeOn.this.source.subscribe(this.parent);
        }
    }

    public ObservableSubscribeOn(ObservableSource<T> source, Scheduler scheduler2) {
        super(source);
        this.scheduler = scheduler2;
    }

    public void subscribeActual(Observer<? super T> s) {
        SubscribeOnObserver<T> parent = new SubscribeOnObserver<>(s);
        s.onSubscribe(parent);
        parent.setDisposable(this.scheduler.scheduleDirect(new SubscribeTask(parent)));
    }
}
