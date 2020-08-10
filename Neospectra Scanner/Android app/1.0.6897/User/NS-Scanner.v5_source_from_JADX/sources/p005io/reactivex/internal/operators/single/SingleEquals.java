package p005io.reactivex.internal.operators.single;

import java.util.concurrent.atomic.AtomicInteger;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.CompositeDisposable;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.single.SingleEquals */
public final class SingleEquals<T> extends Single<Boolean> {
    final SingleSource<? extends T> first;
    final SingleSource<? extends T> second;

    /* renamed from: io.reactivex.internal.operators.single.SingleEquals$InnerObserver */
    static class InnerObserver<T> implements SingleObserver<T> {
        final AtomicInteger count;
        final int index;

        /* renamed from: s */
        final SingleObserver<? super Boolean> f413s;
        final CompositeDisposable set;
        final Object[] values;

        InnerObserver(int index2, CompositeDisposable set2, Object[] values2, SingleObserver<? super Boolean> s, AtomicInteger count2) {
            this.index = index2;
            this.set = set2;
            this.values = values2;
            this.f413s = s;
            this.count = count2;
        }

        public void onSubscribe(Disposable d) {
            this.set.add(d);
        }

        public void onSuccess(T value) {
            this.values[this.index] = value;
            if (this.count.incrementAndGet() == 2) {
                this.f413s.onSuccess(Boolean.valueOf(ObjectHelper.equals(this.values[0], this.values[1])));
            }
        }

        public void onError(Throwable e) {
            int state;
            do {
                state = this.count.get();
                if (state >= 2) {
                    RxJavaPlugins.onError(e);
                    return;
                }
            } while (!this.count.compareAndSet(state, 2));
            this.set.dispose();
            this.f413s.onError(e);
        }
    }

    public SingleEquals(SingleSource<? extends T> first2, SingleSource<? extends T> second2) {
        this.first = first2;
        this.second = second2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super Boolean> s) {
        AtomicInteger count = new AtomicInteger();
        Object[] values = {null, null};
        CompositeDisposable set = new CompositeDisposable();
        s.onSubscribe(set);
        SingleSource<? extends T> singleSource = this.first;
        InnerObserver innerObserver = new InnerObserver(0, set, values, s, count);
        singleSource.subscribe(innerObserver);
        SingleSource<? extends T> singleSource2 = this.second;
        InnerObserver innerObserver2 = new InnerObserver(1, set, values, s, count);
        singleSource2.subscribe(innerObserver2);
    }
}
