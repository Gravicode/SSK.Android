package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiPredicate;
import p005io.reactivex.internal.fuseable.FuseToFlowable;
import p005io.reactivex.internal.fuseable.SimpleQueue;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableSequenceEqualSingle */
public final class FlowableSequenceEqualSingle<T> extends Single<Boolean> implements FuseToFlowable<Boolean> {
    final BiPredicate<? super T, ? super T> comparer;
    final Publisher<? extends T> first;
    final int prefetch;
    final Publisher<? extends T> second;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSequenceEqualSingle$EqualCoordinator */
    static final class EqualCoordinator<T> extends AtomicInteger implements Disposable, EqualCoordinatorHelper {
        private static final long serialVersionUID = -6178010334400373240L;
        final SingleObserver<? super Boolean> actual;
        final BiPredicate<? super T, ? super T> comparer;
        final AtomicThrowable error = new AtomicThrowable();
        final EqualSubscriber<T> first;
        final EqualSubscriber<T> second;

        /* renamed from: v1 */
        T f198v1;

        /* renamed from: v2 */
        T f199v2;

        EqualCoordinator(SingleObserver<? super Boolean> actual2, int prefetch, BiPredicate<? super T, ? super T> comparer2) {
            this.actual = actual2;
            this.comparer = comparer2;
            this.first = new EqualSubscriber<>(this, prefetch);
            this.second = new EqualSubscriber<>(this, prefetch);
        }

        /* access modifiers changed from: 0000 */
        public void subscribe(Publisher<? extends T> source1, Publisher<? extends T> source2) {
            source1.subscribe(this.first);
            source2.subscribe(this.second);
        }

        public void dispose() {
            this.first.cancel();
            this.second.cancel();
            if (getAndIncrement() == 0) {
                this.first.clear();
                this.second.clear();
            }
        }

        public boolean isDisposed() {
            return SubscriptionHelper.isCancelled((Subscription) this.first.get());
        }

        /* access modifiers changed from: 0000 */
        public void cancelAndClear() {
            this.first.cancel();
            this.first.clear();
            this.second.cancel();
            this.second.clear();
        }

        public void drain() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                do {
                    SimpleQueue<T> q1 = this.first.queue;
                    SimpleQueue<T> q2 = this.second.queue;
                    if (q1 != null && q2 != null) {
                        while (!isDisposed()) {
                            if (((Throwable) this.error.get()) != null) {
                                cancelAndClear();
                                this.actual.onError(this.error.terminate());
                                return;
                            }
                            boolean d1 = this.first.done;
                            T a = this.f198v1;
                            if (a == null) {
                                try {
                                    a = q1.poll();
                                    this.f198v1 = a;
                                } catch (Throwable exc) {
                                    Exceptions.throwIfFatal(exc);
                                    cancelAndClear();
                                    this.error.addThrowable(exc);
                                    this.actual.onError(this.error.terminate());
                                    return;
                                }
                            }
                            boolean e1 = a == null;
                            boolean d2 = this.second.done;
                            T b = this.f199v2;
                            if (b == null) {
                                try {
                                    b = q2.poll();
                                    this.f199v2 = b;
                                } catch (Throwable exc2) {
                                    Exceptions.throwIfFatal(exc2);
                                    cancelAndClear();
                                    this.error.addThrowable(exc2);
                                    this.actual.onError(this.error.terminate());
                                    return;
                                }
                            }
                            boolean e2 = b == null;
                            if (d1 && d2 && e1 && e2) {
                                this.actual.onSuccess(Boolean.valueOf(true));
                                return;
                            } else if (d1 && d2 && e1 != e2) {
                                cancelAndClear();
                                this.actual.onSuccess(Boolean.valueOf(false));
                                return;
                            } else if (!e1 && !e2) {
                                try {
                                    if (!this.comparer.test(a, b)) {
                                        cancelAndClear();
                                        this.actual.onSuccess(Boolean.valueOf(false));
                                        return;
                                    }
                                    this.f198v1 = null;
                                    this.f199v2 = null;
                                    this.first.request();
                                    this.second.request();
                                } catch (Throwable exc3) {
                                    Exceptions.throwIfFatal(exc3);
                                    cancelAndClear();
                                    this.error.addThrowable(exc3);
                                    this.actual.onError(this.error.terminate());
                                    return;
                                }
                            }
                        }
                        this.first.clear();
                        this.second.clear();
                        return;
                    } else if (isDisposed()) {
                        this.first.clear();
                        this.second.clear();
                        return;
                    } else if (((Throwable) this.error.get()) != null) {
                        cancelAndClear();
                        this.actual.onError(this.error.terminate());
                        return;
                    }
                    missed = addAndGet(-missed);
                } while (missed != 0);
            }
        }

        public void innerError(Throwable t) {
            if (this.error.addThrowable(t)) {
                drain();
            } else {
                RxJavaPlugins.onError(t);
            }
        }
    }

    public FlowableSequenceEqualSingle(Publisher<? extends T> first2, Publisher<? extends T> second2, BiPredicate<? super T, ? super T> comparer2, int prefetch2) {
        this.first = first2;
        this.second = second2;
        this.comparer = comparer2;
        this.prefetch = prefetch2;
    }

    public void subscribeActual(SingleObserver<? super Boolean> s) {
        EqualCoordinator<T> parent = new EqualCoordinator<>(s, this.prefetch, this.comparer);
        s.onSubscribe(parent);
        parent.subscribe(this.first, this.second);
    }

    public Flowable<Boolean> fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSequenceEqual<T>(this.first, this.second, this.comparer, this.prefetch));
    }
}
