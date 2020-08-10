package p005io.reactivex.internal.operators.observable;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableBuffer */
public final class ObservableBuffer<T, U extends Collection<? super T>> extends AbstractObservableWithUpstream<T, U> {
    final Callable<U> bufferSupplier;
    final int count;
    final int skip;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableBuffer$BufferExactObserver */
    static final class BufferExactObserver<T, U extends Collection<? super T>> implements Observer<T>, Disposable {
        final Observer<? super U> actual;
        U buffer;
        final Callable<U> bufferSupplier;
        final int count;

        /* renamed from: s */
        Disposable f276s;
        int size;

        BufferExactObserver(Observer<? super U> actual2, int count2, Callable<U> bufferSupplier2) {
            this.actual = actual2;
            this.count = count2;
            this.bufferSupplier = bufferSupplier2;
        }

        /* access modifiers changed from: 0000 */
        public boolean createBuffer() {
            try {
                this.buffer = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "Empty buffer supplied");
                return true;
            } catch (Throwable t) {
                Exceptions.throwIfFatal(t);
                this.buffer = null;
                if (this.f276s == null) {
                    EmptyDisposable.error(t, this.actual);
                } else {
                    this.f276s.dispose();
                    this.actual.onError(t);
                }
                return false;
            }
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f276s, s)) {
                this.f276s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f276s.dispose();
        }

        public boolean isDisposed() {
            return this.f276s.isDisposed();
        }

        public void onNext(T t) {
            U b = this.buffer;
            if (b != null) {
                b.add(t);
                int i = this.size + 1;
                this.size = i;
                if (i >= this.count) {
                    this.actual.onNext(b);
                    this.size = 0;
                    createBuffer();
                }
            }
        }

        public void onError(Throwable t) {
            this.buffer = null;
            this.actual.onError(t);
        }

        public void onComplete() {
            U b = this.buffer;
            if (b != null) {
                this.buffer = null;
                if (!b.isEmpty()) {
                    this.actual.onNext(b);
                }
                this.actual.onComplete();
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableBuffer$BufferSkipObserver */
    static final class BufferSkipObserver<T, U extends Collection<? super T>> extends AtomicBoolean implements Observer<T>, Disposable {
        private static final long serialVersionUID = -8223395059921494546L;
        final Observer<? super U> actual;
        final Callable<U> bufferSupplier;
        final ArrayDeque<U> buffers = new ArrayDeque<>();
        final int count;
        long index;

        /* renamed from: s */
        Disposable f277s;
        final int skip;

        BufferSkipObserver(Observer<? super U> actual2, int count2, int skip2, Callable<U> bufferSupplier2) {
            this.actual = actual2;
            this.count = count2;
            this.skip = skip2;
            this.bufferSupplier = bufferSupplier2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f277s, s)) {
                this.f277s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f277s.dispose();
        }

        public boolean isDisposed() {
            return this.f277s.isDisposed();
        }

        public void onNext(T t) {
            long j = this.index;
            this.index = 1 + j;
            if (j % ((long) this.skip) == 0) {
                try {
                    this.buffers.offer((Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources."));
                } catch (Throwable e) {
                    this.buffers.clear();
                    this.f277s.dispose();
                    this.actual.onError(e);
                    return;
                }
            }
            Iterator<U> it = this.buffers.iterator();
            while (it.hasNext()) {
                U b = (Collection) it.next();
                b.add(t);
                if (this.count <= b.size()) {
                    it.remove();
                    this.actual.onNext(b);
                }
            }
        }

        public void onError(Throwable t) {
            this.buffers.clear();
            this.actual.onError(t);
        }

        public void onComplete() {
            while (!this.buffers.isEmpty()) {
                this.actual.onNext(this.buffers.poll());
            }
            this.actual.onComplete();
        }
    }

    public ObservableBuffer(ObservableSource<T> source, int count2, int skip2, Callable<U> bufferSupplier2) {
        super(source);
        this.count = count2;
        this.skip = skip2;
        this.bufferSupplier = bufferSupplier2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super U> t) {
        if (this.skip == this.count) {
            BufferExactObserver<T, U> bes = new BufferExactObserver<>(t, this.count, this.bufferSupplier);
            if (bes.createBuffer()) {
                this.source.subscribe(bes);
                return;
            }
            return;
        }
        this.source.subscribe(new BufferSkipObserver(t, this.count, this.skip, this.bufferSupplier));
    }
}
