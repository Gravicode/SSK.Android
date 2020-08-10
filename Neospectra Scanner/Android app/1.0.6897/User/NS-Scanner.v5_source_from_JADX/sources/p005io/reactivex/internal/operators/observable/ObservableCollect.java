package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.Callable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.functions.BiConsumer;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableCollect */
public final class ObservableCollect<T, U> extends AbstractObservableWithUpstream<T, U> {
    final BiConsumer<? super U, ? super T> collector;
    final Callable<? extends U> initialSupplier;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableCollect$CollectObserver */
    static final class CollectObserver<T, U> implements Observer<T>, Disposable {
        final Observer<? super U> actual;
        final BiConsumer<? super U, ? super T> collector;
        boolean done;

        /* renamed from: s */
        Disposable f286s;

        /* renamed from: u */
        final U f287u;

        CollectObserver(Observer<? super U> actual2, U u, BiConsumer<? super U, ? super T> collector2) {
            this.actual = actual2;
            this.collector = collector2;
            this.f287u = u;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f286s, s)) {
                this.f286s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f286s.dispose();
        }

        public boolean isDisposed() {
            return this.f286s.isDisposed();
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    this.collector.accept(this.f287u, t);
                } catch (Throwable e) {
                    this.f286s.dispose();
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
                this.actual.onNext(this.f287u);
                this.actual.onComplete();
            }
        }
    }

    public ObservableCollect(ObservableSource<T> source, Callable<? extends U> initialSupplier2, BiConsumer<? super U, ? super T> collector2) {
        super(source);
        this.initialSupplier = initialSupplier2;
        this.collector = collector2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super U> t) {
        try {
            this.source.subscribe(new CollectObserver(t, ObjectHelper.requireNonNull(this.initialSupplier.call(), "The initialSupplier returned a null value"), this.collector));
        } catch (Throwable e) {
            EmptyDisposable.error(e, t);
        }
    }
}
