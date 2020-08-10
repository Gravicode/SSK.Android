package p005io.reactivex.internal.operators.single;

import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiPredicate;

/* renamed from: io.reactivex.internal.operators.single.SingleContains */
public final class SingleContains<T> extends p005io.reactivex.Single<Boolean> {
    final BiPredicate<Object, Object> comparer;
    final SingleSource<T> source;
    final Object value;

    /* renamed from: io.reactivex.internal.operators.single.SingleContains$Single */
    final class Single implements SingleObserver<T> {

        /* renamed from: s */
        private final SingleObserver<? super Boolean> f400s;

        Single(SingleObserver<? super Boolean> s) {
            this.f400s = s;
        }

        public void onSubscribe(Disposable d) {
            this.f400s.onSubscribe(d);
        }

        public void onSuccess(T v) {
            try {
                this.f400s.onSuccess(Boolean.valueOf(SingleContains.this.comparer.test(v, SingleContains.this.value)));
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.f400s.onError(ex);
            }
        }

        public void onError(Throwable e) {
            this.f400s.onError(e);
        }
    }

    public SingleContains(SingleSource<T> source2, Object value2, BiPredicate<Object, Object> comparer2) {
        this.source = source2;
        this.value = value2;
        this.comparer = comparer2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super Boolean> s) {
        this.source.subscribe(new Single(s));
    }
}
