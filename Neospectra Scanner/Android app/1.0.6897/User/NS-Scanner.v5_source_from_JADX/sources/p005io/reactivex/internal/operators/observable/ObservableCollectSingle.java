package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.Callable;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.functions.BiConsumer;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.FuseToObservable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableCollectSingle */
public final class ObservableCollectSingle<T, U> extends Single<U> implements FuseToObservable<U> {
    final BiConsumer<? super U, ? super T> collector;
    final Callable<? extends U> initialSupplier;
    final ObservableSource<T> source;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableCollectSingle$CollectObserver */
    static final class CollectObserver<T, U> implements Observer<T>, Disposable {
        final SingleObserver<? super U> actual;
        final BiConsumer<? super U, ? super T> collector;
        boolean done;

        /* renamed from: s */
        Disposable f288s;

        /* renamed from: u */
        final U f289u;

        CollectObserver(SingleObserver<? super U> actual2, U u, BiConsumer<? super U, ? super T> collector2) {
            this.actual = actual2;
            this.collector = collector2;
            this.f289u = u;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f288s, s)) {
                this.f288s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f288s.dispose();
        }

        public boolean isDisposed() {
            return this.f288s.isDisposed();
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    this.collector.accept(this.f289u, t);
                } catch (Throwable e) {
                    this.f288s.dispose();
                    onError(e);
                }
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onSuccess(this.f289u);
            }
        }
    }

    public ObservableCollectSingle(ObservableSource<T> source2, Callable<? extends U> initialSupplier2, BiConsumer<? super U, ? super T> collector2) {
        this.source = source2;
        this.initialSupplier = initialSupplier2;
        this.collector = collector2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super U> t) {
        try {
            this.source.subscribe(new CollectObserver(t, ObjectHelper.requireNonNull(this.initialSupplier.call(), "The initialSupplier returned a null value"), this.collector));
        } catch (Throwable e) {
            EmptyDisposable.error(e, t);
        }
    }

    public Observable<U> fuseToObservable() {
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableCollect<T>(this.source, this.initialSupplier, this.collector));
    }
}
