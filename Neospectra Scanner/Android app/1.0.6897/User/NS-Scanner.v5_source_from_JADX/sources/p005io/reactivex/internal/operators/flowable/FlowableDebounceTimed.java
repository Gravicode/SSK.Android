package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Scheduler.Worker;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.MissingBackpressureException;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.plugins.RxJavaPlugins;
import p005io.reactivex.subscribers.SerializedSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableDebounceTimed */
public final class FlowableDebounceTimed<T> extends AbstractFlowableWithUpstream<T, T> {
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDebounceTimed$DebounceEmitter */
    static final class DebounceEmitter<T> extends AtomicReference<Disposable> implements Runnable, Disposable {
        private static final long serialVersionUID = 6812032969491025141L;
        final long idx;
        final AtomicBoolean once = new AtomicBoolean();
        final DebounceTimedSubscriber<T> parent;
        final T value;

        DebounceEmitter(T value2, long idx2, DebounceTimedSubscriber<T> parent2) {
            this.value = value2;
            this.idx = idx2;
            this.parent = parent2;
        }

        public void run() {
            emit();
        }

        /* access modifiers changed from: 0000 */
        public void emit() {
            if (this.once.compareAndSet(false, true)) {
                this.parent.emit(this.idx, this.value, this);
            }
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return get() == DisposableHelper.DISPOSED;
        }

        public void setResource(Disposable d) {
            DisposableHelper.replace(this, d);
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDebounceTimed$DebounceTimedSubscriber */
    static final class DebounceTimedSubscriber<T> extends AtomicLong implements FlowableSubscriber<T>, Subscription {
        private static final long serialVersionUID = -9102637559663639004L;
        final Subscriber<? super T> actual;
        boolean done;
        volatile long index;

        /* renamed from: s */
        Subscription f141s;
        final long timeout;
        Disposable timer;
        final TimeUnit unit;
        final Worker worker;

        DebounceTimedSubscriber(Subscriber<? super T> actual2, long timeout2, TimeUnit unit2, Worker worker2) {
            this.actual = actual2;
            this.timeout = timeout2;
            this.unit = unit2;
            this.worker = worker2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f141s, s)) {
                this.f141s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long idx = this.index + 1;
                this.index = idx;
                Disposable d = this.timer;
                if (d != null) {
                    d.dispose();
                }
                DebounceEmitter<T> de = new DebounceEmitter<>(t, idx, this);
                this.timer = de;
                de.setResource(this.worker.schedule(de, this.timeout, this.unit));
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            Disposable d = this.timer;
            if (d != null) {
                d.dispose();
            }
            this.actual.onError(t);
            this.worker.dispose();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                Disposable d = this.timer;
                if (d != null) {
                    d.dispose();
                }
                DebounceEmitter<T> de = (DebounceEmitter) d;
                if (de != null) {
                    de.emit();
                }
                this.actual.onComplete();
                this.worker.dispose();
            }
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this, n);
            }
        }

        public void cancel() {
            this.f141s.cancel();
            this.worker.dispose();
        }

        /* access modifiers changed from: 0000 */
        public void emit(long idx, T t, DebounceEmitter<T> emitter) {
            if (idx != this.index) {
                return;
            }
            if (get() != 0) {
                this.actual.onNext(t);
                BackpressureHelper.produced(this, 1);
                emitter.dispose();
                return;
            }
            cancel();
            this.actual.onError(new MissingBackpressureException("Could not deliver value due to lack of requests"));
        }
    }

    public FlowableDebounceTimed(Flowable<T> source, long timeout2, TimeUnit unit2, Scheduler scheduler2) {
        super(source);
        this.timeout = timeout2;
        this.unit = unit2;
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        Flowable flowable = this.source;
        DebounceTimedSubscriber debounceTimedSubscriber = new DebounceTimedSubscriber(new SerializedSubscriber(s), this.timeout, this.unit, this.scheduler.createWorker());
        flowable.subscribe((FlowableSubscriber<? super T>) debounceTimedSubscriber);
    }
}
