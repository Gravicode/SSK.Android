package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.internal.observers.BasicFuseableObserver;

@Experimental
/* renamed from: io.reactivex.internal.operators.observable.ObservableDoAfterNext */
public final class ObservableDoAfterNext<T> extends AbstractObservableWithUpstream<T, T> {
    final Consumer<? super T> onAfterNext;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableDoAfterNext$DoAfterObserver */
    static final class DoAfterObserver<T> extends BasicFuseableObserver<T, T> {
        final Consumer<? super T> onAfterNext;

        DoAfterObserver(Observer<? super T> actual, Consumer<? super T> onAfterNext2) {
            super(actual);
            this.onAfterNext = onAfterNext2;
        }

        public void onNext(T t) {
            this.actual.onNext(t);
            if (this.sourceMode == 0) {
                try {
                    this.onAfterNext.accept(t);
                } catch (Throwable ex) {
                    fail(ex);
                }
            }
        }

        public int requestFusion(int mode) {
            return transitiveBoundaryFusion(mode);
        }

        @Nullable
        public T poll() throws Exception {
            T v = this.f77qs.poll();
            if (v != null) {
                this.onAfterNext.accept(v);
            }
            return v;
        }
    }

    public ObservableDoAfterNext(ObservableSource<T> source, Consumer<? super T> onAfterNext2) {
        super(source);
        this.onAfterNext = onAfterNext2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> s) {
        this.source.subscribe(new DoAfterObserver(s, this.onAfterNext));
    }
}
