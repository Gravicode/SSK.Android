package p005io.reactivex.internal.operators.maybe;

import java.util.Iterator;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.observers.BasicQueueDisposable;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeFlatMapIterableObservable */
public final class MaybeFlatMapIterableObservable<T, R> extends Observable<R> {
    final Function<? super T, ? extends Iterable<? extends R>> mapper;
    final MaybeSource<T> source;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeFlatMapIterableObservable$FlatMapIterableObserver */
    static final class FlatMapIterableObserver<T, R> extends BasicQueueDisposable<R> implements MaybeObserver<T> {
        final Observer<? super R> actual;
        volatile boolean cancelled;

        /* renamed from: d */
        Disposable f251d;

        /* renamed from: it */
        volatile Iterator<? extends R> f252it;
        final Function<? super T, ? extends Iterable<? extends R>> mapper;
        boolean outputFused;

        FlatMapIterableObserver(Observer<? super R> actual2, Function<? super T, ? extends Iterable<? extends R>> mapper2) {
            this.actual = actual2;
            this.mapper = mapper2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f251d, d)) {
                this.f251d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            Observer<? super R> a = this.actual;
            try {
                Iterator<? extends R> iterator = ((Iterable) this.mapper.apply(value)).iterator();
                if (!iterator.hasNext()) {
                    a.onComplete();
                    return;
                }
                this.f252it = iterator;
                if (this.outputFused) {
                    a.onNext(null);
                    a.onComplete();
                    return;
                }
                while (!this.cancelled) {
                    try {
                        a.onNext(iterator.next());
                        if (!this.cancelled) {
                            try {
                                if (!iterator.hasNext()) {
                                    a.onComplete();
                                    return;
                                }
                            } catch (Throwable ex) {
                                Exceptions.throwIfFatal(ex);
                                a.onError(ex);
                                return;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable ex2) {
                        Exceptions.throwIfFatal(ex2);
                        a.onError(ex2);
                        return;
                    }
                }
            } catch (Throwable ex3) {
                Exceptions.throwIfFatal(ex3);
                a.onError(ex3);
            }
        }

        public void onError(Throwable e) {
            this.f251d = DisposableHelper.DISPOSED;
            this.actual.onError(e);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void dispose() {
            this.cancelled = true;
            this.f251d.dispose();
            this.f251d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public int requestFusion(int mode) {
            if ((mode & 2) == 0) {
                return 0;
            }
            this.outputFused = true;
            return 2;
        }

        public void clear() {
            this.f252it = null;
        }

        public boolean isEmpty() {
            return this.f252it == null;
        }

        @Nullable
        public R poll() throws Exception {
            Iterator<? extends R> iterator = this.f252it;
            if (iterator == null) {
                return null;
            }
            R v = ObjectHelper.requireNonNull(iterator.next(), "The iterator returned a null value");
            if (!iterator.hasNext()) {
                this.f252it = null;
            }
            return v;
        }
    }

    public MaybeFlatMapIterableObservable(MaybeSource<T> source2, Function<? super T, ? extends Iterable<? extends R>> mapper2) {
        this.source = source2;
        this.mapper = mapper2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super R> s) {
        this.source.subscribe(new FlatMapIterableObserver(s, this.mapper));
    }
}
