package p005io.reactivex.internal.operators.parallel;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.ConditionalSubscriber;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.parallel.ParallelFlowable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.parallel.ParallelMap */
public final class ParallelMap<T, R> extends ParallelFlowable<R> {
    final Function<? super T, ? extends R> mapper;
    final ParallelFlowable<T> source;

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelMap$ParallelMapConditionalSubscriber */
    static final class ParallelMapConditionalSubscriber<T, R> implements ConditionalSubscriber<T>, Subscription {
        final ConditionalSubscriber<? super R> actual;
        boolean done;
        final Function<? super T, ? extends R> mapper;

        /* renamed from: s */
        Subscription f393s;

        ParallelMapConditionalSubscriber(ConditionalSubscriber<? super R> actual2, Function<? super T, ? extends R> mapper2) {
            this.actual = actual2;
            this.mapper = mapper2;
        }

        public void request(long n) {
            this.f393s.request(n);
        }

        public void cancel() {
            this.f393s.cancel();
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f393s, s)) {
                this.f393s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    this.actual.onNext(ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null value"));
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    cancel();
                    onError(ex);
                }
            }
        }

        public boolean tryOnNext(T t) {
            if (this.done) {
                return false;
            }
            try {
                return this.actual.tryOnNext(ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null value"));
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                cancel();
                onError(ex);
                return false;
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
                this.actual.onComplete();
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelMap$ParallelMapSubscriber */
    static final class ParallelMapSubscriber<T, R> implements FlowableSubscriber<T>, Subscription {
        final Subscriber<? super R> actual;
        boolean done;
        final Function<? super T, ? extends R> mapper;

        /* renamed from: s */
        Subscription f394s;

        ParallelMapSubscriber(Subscriber<? super R> actual2, Function<? super T, ? extends R> mapper2) {
            this.actual = actual2;
            this.mapper = mapper2;
        }

        public void request(long n) {
            this.f394s.request(n);
        }

        public void cancel() {
            this.f394s.cancel();
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f394s, s)) {
                this.f394s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    this.actual.onNext(ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null value"));
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    cancel();
                    onError(ex);
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
                this.actual.onComplete();
            }
        }
    }

    public ParallelMap(ParallelFlowable<T> source2, Function<? super T, ? extends R> mapper2) {
        this.source = source2;
        this.mapper = mapper2;
    }

    public void subscribe(Subscriber<? super R>[] subscribers) {
        if (validate(subscribers)) {
            int n = subscribers.length;
            Subscriber<? super T>[] parents = new Subscriber[n];
            for (int i = 0; i < n; i++) {
                Subscriber<? super R> a = subscribers[i];
                if (a instanceof ConditionalSubscriber) {
                    parents[i] = new ParallelMapConditionalSubscriber<>((ConditionalSubscriber) a, this.mapper);
                } else {
                    parents[i] = new ParallelMapSubscriber<>(a, this.mapper);
                }
            }
            this.source.subscribe(parents);
        }
    }

    public int parallelism() {
        return this.source.parallelism();
    }
}
