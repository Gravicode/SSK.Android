package p005io.reactivex.internal.operators.single;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.SequentialDisposable;

/* renamed from: io.reactivex.internal.operators.single.SingleSubscribeOn */
public final class SingleSubscribeOn<T> extends Single<T> {
    final Scheduler scheduler;
    final SingleSource<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleSubscribeOn$SubscribeOnObserver */
    static final class SubscribeOnObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T>, Disposable, Runnable {
        private static final long serialVersionUID = 7000911171163930287L;
        final SingleObserver<? super T> actual;
        final SingleSource<? extends T> source;
        final SequentialDisposable task = new SequentialDisposable();

        SubscribeOnObserver(SingleObserver<? super T> actual2, SingleSource<? extends T> source2) {
            this.actual = actual2;
            this.source = source2;
        }

        public void onSubscribe(Disposable d) {
            DisposableHelper.setOnce(this, d);
        }

        public void onSuccess(T value) {
            this.actual.onSuccess(value);
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void dispose() {
            DisposableHelper.dispose(this);
            this.task.dispose();
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void run() {
            this.source.subscribe(this);
        }
    }

    public SingleSubscribeOn(SingleSource<? extends T> source2, Scheduler scheduler2) {
        this.source = source2;
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        SubscribeOnObserver<T> parent = new SubscribeOnObserver<>(s, this.source);
        s.onSubscribe(parent);
        parent.task.replace(this.scheduler.scheduleDirect(parent));
    }
}
