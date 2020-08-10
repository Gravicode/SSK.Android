package p005io.reactivex.internal.operators.maybe;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.Scheduler;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.SequentialDisposable;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeSubscribeOn */
public final class MaybeSubscribeOn<T> extends AbstractMaybeWithUpstream<T, T> {
    final Scheduler scheduler;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeSubscribeOn$SubscribeOnMaybeObserver */
    static final class SubscribeOnMaybeObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
        private static final long serialVersionUID = 8571289934935992137L;
        final MaybeObserver<? super T> actual;
        final SequentialDisposable task = new SequentialDisposable();

        SubscribeOnMaybeObserver(MaybeObserver<? super T> actual2) {
            this.actual = actual2;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
            this.task.dispose();
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
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

        public void onComplete() {
            this.actual.onComplete();
        }
    }

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeSubscribeOn$SubscribeTask */
    static final class SubscribeTask<T> implements Runnable {
        final MaybeObserver<? super T> observer;
        final MaybeSource<T> source;

        SubscribeTask(MaybeObserver<? super T> observer2, MaybeSource<T> source2) {
            this.observer = observer2;
            this.source = source2;
        }

        public void run() {
            this.source.subscribe(this.observer);
        }
    }

    public MaybeSubscribeOn(MaybeSource<T> source, Scheduler scheduler2) {
        super(source);
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        SubscribeOnMaybeObserver<T> parent = new SubscribeOnMaybeObserver<>(observer);
        observer.onSubscribe(parent);
        parent.task.replace(this.scheduler.scheduleDirect(new SubscribeTask(parent, this.source)));
    }
}
