package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Action;
import p005io.reactivex.internal.fuseable.ConditionalSubscriber;
import p005io.reactivex.internal.fuseable.QueueSubscription;
import p005io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

@Experimental
/* renamed from: io.reactivex.internal.operators.flowable.FlowableDoFinally */
public final class FlowableDoFinally<T> extends AbstractFlowableWithUpstream<T, T> {
    final Action onFinally;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDoFinally$DoFinallyConditionalSubscriber */
    static final class DoFinallyConditionalSubscriber<T> extends BasicIntQueueSubscription<T> implements ConditionalSubscriber<T> {
        private static final long serialVersionUID = 4109457741734051389L;
        final ConditionalSubscriber<? super T> actual;
        final Action onFinally;

        /* renamed from: qs */
        QueueSubscription<T> f149qs;

        /* renamed from: s */
        Subscription f150s;
        boolean syncFused;

        DoFinallyConditionalSubscriber(ConditionalSubscriber<? super T> actual2, Action onFinally2) {
            this.actual = actual2;
            this.onFinally = onFinally2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f150s, s)) {
                this.f150s = s;
                if (s instanceof QueueSubscription) {
                    this.f149qs = (QueueSubscription) s;
                }
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public boolean tryOnNext(T t) {
            return this.actual.tryOnNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
            runFinally();
        }

        public void onComplete() {
            this.actual.onComplete();
            runFinally();
        }

        public void cancel() {
            this.f150s.cancel();
            runFinally();
        }

        public void request(long n) {
            this.f150s.request(n);
        }

        public int requestFusion(int mode) {
            QueueSubscription<T> qs = this.f149qs;
            boolean z = false;
            if (qs == null || (mode & 4) != 0) {
                return 0;
            }
            int m = qs.requestFusion(mode);
            if (m != 0) {
                if (m == 1) {
                    z = true;
                }
                this.syncFused = z;
            }
            return m;
        }

        public void clear() {
            this.f149qs.clear();
        }

        public boolean isEmpty() {
            return this.f149qs.isEmpty();
        }

        @Nullable
        public T poll() throws Exception {
            T v = this.f149qs.poll();
            if (v == null && this.syncFused) {
                runFinally();
            }
            return v;
        }

        /* access modifiers changed from: 0000 */
        public void runFinally() {
            if (compareAndSet(0, 1)) {
                try {
                    this.onFinally.run();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    RxJavaPlugins.onError(ex);
                }
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDoFinally$DoFinallySubscriber */
    static final class DoFinallySubscriber<T> extends BasicIntQueueSubscription<T> implements FlowableSubscriber<T> {
        private static final long serialVersionUID = 4109457741734051389L;
        final Subscriber<? super T> actual;
        final Action onFinally;

        /* renamed from: qs */
        QueueSubscription<T> f151qs;

        /* renamed from: s */
        Subscription f152s;
        boolean syncFused;

        DoFinallySubscriber(Subscriber<? super T> actual2, Action onFinally2) {
            this.actual = actual2;
            this.onFinally = onFinally2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f152s, s)) {
                this.f152s = s;
                if (s instanceof QueueSubscription) {
                    this.f151qs = (QueueSubscription) s;
                }
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
            runFinally();
        }

        public void onComplete() {
            this.actual.onComplete();
            runFinally();
        }

        public void cancel() {
            this.f152s.cancel();
            runFinally();
        }

        public void request(long n) {
            this.f152s.request(n);
        }

        public int requestFusion(int mode) {
            QueueSubscription<T> qs = this.f151qs;
            boolean z = false;
            if (qs == null || (mode & 4) != 0) {
                return 0;
            }
            int m = qs.requestFusion(mode);
            if (m != 0) {
                if (m == 1) {
                    z = true;
                }
                this.syncFused = z;
            }
            return m;
        }

        public void clear() {
            this.f151qs.clear();
        }

        public boolean isEmpty() {
            return this.f151qs.isEmpty();
        }

        @Nullable
        public T poll() throws Exception {
            T v = this.f151qs.poll();
            if (v == null && this.syncFused) {
                runFinally();
            }
            return v;
        }

        /* access modifiers changed from: 0000 */
        public void runFinally() {
            if (compareAndSet(0, 1)) {
                try {
                    this.onFinally.run();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    RxJavaPlugins.onError(ex);
                }
            }
        }
    }

    public FlowableDoFinally(Flowable<T> source, Action onFinally2) {
        super(source);
        this.onFinally = onFinally2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        if (s instanceof ConditionalSubscriber) {
            this.source.subscribe((FlowableSubscriber<? super T>) new DoFinallyConditionalSubscriber<Object>((ConditionalSubscriber) s, this.onFinally));
        } else {
            this.source.subscribe((FlowableSubscriber<? super T>) new DoFinallySubscriber<Object>(s, this.onFinally));
        }
    }
}
