package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.internal.util.HalfSerializer;

/* renamed from: io.reactivex.internal.operators.observable.ObservableMergeWithCompletable */
public final class ObservableMergeWithCompletable<T> extends AbstractObservableWithUpstream<T, T> {
    final CompletableSource other;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableMergeWithCompletable$MergeWithObserver */
    static final class MergeWithObserver<T> extends AtomicInteger implements Observer<T>, Disposable {
        private static final long serialVersionUID = -4592979584110982903L;
        final Observer<? super T> actual;
        final AtomicThrowable error = new AtomicThrowable();
        final AtomicReference<Disposable> mainDisposable = new AtomicReference<>();
        volatile boolean mainDone;
        volatile boolean otherDone;
        final OtherObserver otherObserver = new OtherObserver(this);

        /* renamed from: io.reactivex.internal.operators.observable.ObservableMergeWithCompletable$MergeWithObserver$OtherObserver */
        static final class OtherObserver extends AtomicReference<Disposable> implements CompletableObserver {
            private static final long serialVersionUID = -2935427570954647017L;
            final MergeWithObserver<?> parent;

            OtherObserver(MergeWithObserver<?> parent2) {
                this.parent = parent2;
            }

            public void onSubscribe(Disposable d) {
                DisposableHelper.setOnce(this, d);
            }

            public void onError(Throwable e) {
                this.parent.otherError(e);
            }

            public void onComplete() {
                this.parent.otherComplete();
            }
        }

        MergeWithObserver(Observer<? super T> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Disposable d) {
            DisposableHelper.setOnce(this.mainDisposable, d);
        }

        public void onNext(T t) {
            HalfSerializer.onNext(this.actual, t, (AtomicInteger) this, this.error);
        }

        public void onError(Throwable ex) {
            DisposableHelper.dispose(this.mainDisposable);
            HalfSerializer.onError(this.actual, ex, (AtomicInteger) this, this.error);
        }

        public void onComplete() {
            this.mainDone = true;
            if (this.otherDone) {
                HalfSerializer.onComplete(this.actual, (AtomicInteger) this, this.error);
            }
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) this.mainDisposable.get());
        }

        public void dispose() {
            DisposableHelper.dispose(this.mainDisposable);
            DisposableHelper.dispose(this.otherObserver);
        }

        /* access modifiers changed from: 0000 */
        public void otherError(Throwable ex) {
            DisposableHelper.dispose(this.mainDisposable);
            HalfSerializer.onError(this.actual, ex, (AtomicInteger) this, this.error);
        }

        /* access modifiers changed from: 0000 */
        public void otherComplete() {
            this.otherDone = true;
            if (this.mainDone) {
                HalfSerializer.onComplete(this.actual, (AtomicInteger) this, this.error);
            }
        }
    }

    public ObservableMergeWithCompletable(Observable<T> source, CompletableSource other2) {
        super(source);
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        MergeWithObserver<T> parent = new MergeWithObserver<>(observer);
        observer.onSubscribe(parent);
        this.source.subscribe(parent);
        this.other.subscribe(parent.otherObserver);
    }
}
