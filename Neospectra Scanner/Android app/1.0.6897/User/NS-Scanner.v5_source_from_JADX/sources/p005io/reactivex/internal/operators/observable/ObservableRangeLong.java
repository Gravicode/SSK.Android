package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.internal.observers.BasicIntQueueDisposable;

/* renamed from: io.reactivex.internal.operators.observable.ObservableRangeLong */
public final class ObservableRangeLong extends Observable<Long> {
    private final long count;
    private final long start;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableRangeLong$RangeDisposable */
    static final class RangeDisposable extends BasicIntQueueDisposable<Long> {
        private static final long serialVersionUID = 396518478098735504L;
        final Observer<? super Long> actual;
        final long end;
        boolean fused;
        long index;

        RangeDisposable(Observer<? super Long> actual2, long start, long end2) {
            this.actual = actual2;
            this.index = start;
            this.end = end2;
        }

        /* access modifiers changed from: 0000 */
        public void run() {
            if (!this.fused) {
                Observer<? super Long> actual2 = this.actual;
                long e = this.end;
                for (long i = this.index; i != e && get() == 0; i++) {
                    actual2.onNext(Long.valueOf(i));
                }
                if (get() == 0) {
                    lazySet(1);
                    actual2.onComplete();
                }
            }
        }

        @Nullable
        public Long poll() throws Exception {
            long i = this.index;
            if (i != this.end) {
                this.index = 1 + i;
                return Long.valueOf(i);
            }
            lazySet(1);
            return null;
        }

        public boolean isEmpty() {
            return this.index == this.end;
        }

        public void clear() {
            this.index = this.end;
            lazySet(1);
        }

        public void dispose() {
            set(1);
        }

        public boolean isDisposed() {
            return get() != 0;
        }

        public int requestFusion(int mode) {
            if ((mode & 1) == 0) {
                return 0;
            }
            this.fused = true;
            return 1;
        }
    }

    public ObservableRangeLong(long start2, long count2) {
        this.start = start2;
        this.count = count2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super Long> o) {
        Observer<? super Long> observer = o;
        RangeDisposable parent = new RangeDisposable(observer, this.start, this.count + this.start);
        o.onSubscribe(parent);
        parent.run();
    }
}
