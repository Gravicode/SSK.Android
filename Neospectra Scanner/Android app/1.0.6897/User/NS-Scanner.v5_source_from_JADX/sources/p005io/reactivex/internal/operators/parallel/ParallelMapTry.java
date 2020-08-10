package p005io.reactivex.internal.operators.parallel;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.fuseable.ConditionalSubscriber;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.parallel.ParallelFailureHandling;
import p005io.reactivex.parallel.ParallelFlowable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.parallel.ParallelMapTry */
public final class ParallelMapTry<T, R> extends ParallelFlowable<R> {
    final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
    final Function<? super T, ? extends R> mapper;
    final ParallelFlowable<T> source;

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelMapTry$ParallelMapTryConditionalSubscriber */
    static final class ParallelMapTryConditionalSubscriber<T, R> implements ConditionalSubscriber<T>, Subscription {
        final ConditionalSubscriber<? super R> actual;
        boolean done;
        final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
        final Function<? super T, ? extends R> mapper;

        /* renamed from: s */
        Subscription f395s;

        ParallelMapTryConditionalSubscriber(ConditionalSubscriber<? super R> actual2, Function<? super T, ? extends R> mapper2, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler2) {
            this.actual = actual2;
            this.mapper = mapper2;
            this.errorHandler = errorHandler2;
        }

        public void request(long n) {
            this.f395s.request(n);
        }

        public void cancel() {
            this.f395s.cancel();
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f395s, s)) {
                this.f395s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!tryOnNext(t) && !this.done) {
                this.f395s.request(1);
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:4:0x0008 A[LOOP:0: B:4:0x0008->B:13:0x0041, LOOP_START, PHI: r2 
          PHI: (r2v1 'retries' long) = (r2v0 'retries' long), (r2v2 'retries' long) binds: [B:3:0x0006, B:13:0x0041] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC, Splitter:B:4:0x0008] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean tryOnNext(T r9) {
            /*
                r8 = this;
                boolean r0 = r8.done
                r1 = 0
                if (r0 == 0) goto L_0x0006
                return r1
            L_0x0006:
                r2 = 0
            L_0x0008:
                io.reactivex.functions.Function<? super T, ? extends R> r0 = r8.mapper     // Catch:{ Throwable -> 0x001d }
                java.lang.Object r0 = r0.apply(r9)     // Catch:{ Throwable -> 0x001d }
                java.lang.String r4 = "The mapper returned a null value"
                java.lang.Object r0 = p005io.reactivex.internal.functions.ObjectHelper.requireNonNull(r0, r4)     // Catch:{ Throwable -> 0x001d }
                io.reactivex.internal.fuseable.ConditionalSubscriber<? super R> r1 = r8.actual
                boolean r1 = r1.tryOnNext(r0)
                return r1
            L_0x001d:
                r0 = move-exception
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r0)
                io.reactivex.functions.BiFunction<? super java.lang.Long, ? super java.lang.Throwable, io.reactivex.parallel.ParallelFailureHandling> r4 = r8.errorHandler     // Catch:{ Throwable -> 0x0054 }
                r5 = 1
                long r5 = r5 + r2
                r2 = r5
                java.lang.Long r5 = java.lang.Long.valueOf(r5)     // Catch:{ Throwable -> 0x0054 }
                java.lang.Object r4 = r4.apply(r5, r0)     // Catch:{ Throwable -> 0x0054 }
                java.lang.String r5 = "The errorHandler returned a null item"
                java.lang.Object r4 = p005io.reactivex.internal.functions.ObjectHelper.requireNonNull(r4, r5)     // Catch:{ Throwable -> 0x0054 }
                io.reactivex.parallel.ParallelFailureHandling r4 = (p005io.reactivex.parallel.ParallelFailureHandling) r4     // Catch:{ Throwable -> 0x0054 }
                int[] r5 = p005io.reactivex.internal.operators.parallel.ParallelMapTry.C08631.$SwitchMap$io$reactivex$parallel$ParallelFailureHandling
                int r6 = r4.ordinal()
                r5 = r5[r6]
                switch(r5) {
                    case 1: goto L_0x0053;
                    case 2: goto L_0x0052;
                    case 3: goto L_0x004b;
                    default: goto L_0x0044;
                }
            L_0x0044:
                r8.cancel()
                r8.onError(r0)
                return r1
            L_0x004b:
                r8.cancel()
                r8.onComplete()
                return r1
            L_0x0052:
                return r1
            L_0x0053:
                goto L_0x0008
            L_0x0054:
                r4 = move-exception
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r4)
                r8.cancel()
                io.reactivex.exceptions.CompositeException r5 = new io.reactivex.exceptions.CompositeException
                r6 = 2
                java.lang.Throwable[] r6 = new java.lang.Throwable[r6]
                r6[r1] = r0
                r7 = 1
                r6[r7] = r4
                r5.<init>(r6)
                r8.onError(r5)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.parallel.ParallelMapTry.ParallelMapTryConditionalSubscriber.tryOnNext(java.lang.Object):boolean");
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

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelMapTry$ParallelMapTrySubscriber */
    static final class ParallelMapTrySubscriber<T, R> implements ConditionalSubscriber<T>, Subscription {
        final Subscriber<? super R> actual;
        boolean done;
        final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
        final Function<? super T, ? extends R> mapper;

        /* renamed from: s */
        Subscription f396s;

        ParallelMapTrySubscriber(Subscriber<? super R> actual2, Function<? super T, ? extends R> mapper2, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler2) {
            this.actual = actual2;
            this.mapper = mapper2;
            this.errorHandler = errorHandler2;
        }

        public void request(long n) {
            this.f396s.request(n);
        }

        public void cancel() {
            this.f396s.cancel();
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f396s, s)) {
                this.f396s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!tryOnNext(t) && !this.done) {
                this.f396s.request(1);
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:4:0x0008 A[LOOP:0: B:4:0x0008->B:14:0x0043, LOOP_START, PHI: r2 
          PHI: (r2v1 'retries' long) = (r2v0 'retries' long), (r2v2 'retries' long) binds: [B:3:0x0006, B:14:0x0043] A[DONT_GENERATE, DONT_INLINE]] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean tryOnNext(T r9) {
            /*
                r8 = this;
                boolean r0 = r8.done
                r1 = 0
                if (r0 == 0) goto L_0x0006
                return r1
            L_0x0006:
                r2 = 0
            L_0x0008:
                r0 = 1
                io.reactivex.functions.Function<? super T, ? extends R> r4 = r8.mapper     // Catch:{ Throwable -> 0x001e }
                java.lang.Object r4 = r4.apply(r9)     // Catch:{ Throwable -> 0x001e }
                java.lang.String r5 = "The mapper returned a null value"
                java.lang.Object r4 = p005io.reactivex.internal.functions.ObjectHelper.requireNonNull(r4, r5)     // Catch:{ Throwable -> 0x001e }
                r1 = r4
                org.reactivestreams.Subscriber<? super R> r4 = r8.actual
                r4.onNext(r1)
                return r0
            L_0x001e:
                r4 = move-exception
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r4)
                io.reactivex.functions.BiFunction<? super java.lang.Long, ? super java.lang.Throwable, io.reactivex.parallel.ParallelFailureHandling> r5 = r8.errorHandler     // Catch:{ Throwable -> 0x0056 }
                r6 = 1
                long r6 = r6 + r2
                r2 = r6
                java.lang.Long r6 = java.lang.Long.valueOf(r6)     // Catch:{ Throwable -> 0x0056 }
                java.lang.Object r5 = r5.apply(r6, r4)     // Catch:{ Throwable -> 0x0056 }
                java.lang.String r6 = "The errorHandler returned a null item"
                java.lang.Object r5 = p005io.reactivex.internal.functions.ObjectHelper.requireNonNull(r5, r6)     // Catch:{ Throwable -> 0x0056 }
                io.reactivex.parallel.ParallelFailureHandling r5 = (p005io.reactivex.parallel.ParallelFailureHandling) r5     // Catch:{ Throwable -> 0x0056 }
                r0 = r5
                int[] r5 = p005io.reactivex.internal.operators.parallel.ParallelMapTry.C08631.$SwitchMap$io$reactivex$parallel$ParallelFailureHandling
                int r6 = r0.ordinal()
                r5 = r5[r6]
                switch(r5) {
                    case 1: goto L_0x0055;
                    case 2: goto L_0x0054;
                    case 3: goto L_0x004d;
                    default: goto L_0x0046;
                }
            L_0x0046:
                r8.cancel()
                r8.onError(r4)
                return r1
            L_0x004d:
                r8.cancel()
                r8.onComplete()
                return r1
            L_0x0054:
                return r1
            L_0x0055:
                goto L_0x0008
            L_0x0056:
                r5 = move-exception
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r5)
                r8.cancel()
                io.reactivex.exceptions.CompositeException r6 = new io.reactivex.exceptions.CompositeException
                r7 = 2
                java.lang.Throwable[] r7 = new java.lang.Throwable[r7]
                r7[r1] = r4
                r7[r0] = r5
                r6.<init>(r7)
                r8.onError(r6)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.parallel.ParallelMapTry.ParallelMapTrySubscriber.tryOnNext(java.lang.Object):boolean");
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

    public ParallelMapTry(ParallelFlowable<T> source2, Function<? super T, ? extends R> mapper2, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler2) {
        this.source = source2;
        this.mapper = mapper2;
        this.errorHandler = errorHandler2;
    }

    public void subscribe(Subscriber<? super R>[] subscribers) {
        if (validate(subscribers)) {
            int n = subscribers.length;
            Subscriber<? super T>[] parents = new Subscriber[n];
            for (int i = 0; i < n; i++) {
                Subscriber<? super R> a = subscribers[i];
                if (a instanceof ConditionalSubscriber) {
                    parents[i] = new ParallelMapTryConditionalSubscriber<>((ConditionalSubscriber) a, this.mapper, this.errorHandler);
                } else {
                    parents[i] = new ParallelMapTrySubscriber<>(a, this.mapper, this.errorHandler);
                }
            }
            this.source.subscribe(parents);
        }
    }

    public int parallelism() {
        return this.source.parallelism();
    }
}
