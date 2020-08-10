package p005io.reactivex.internal.operators.parallel;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.internal.fuseable.ConditionalSubscriber;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.parallel.ParallelFailureHandling;
import p005io.reactivex.parallel.ParallelFlowable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.parallel.ParallelDoOnNextTry */
public final class ParallelDoOnNextTry<T> extends ParallelFlowable<T> {
    final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
    final Consumer<? super T> onNext;
    final ParallelFlowable<T> source;

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelDoOnNextTry$ParallelDoOnNextConditionalSubscriber */
    static final class ParallelDoOnNextConditionalSubscriber<T> implements ConditionalSubscriber<T>, Subscription {
        final ConditionalSubscriber<? super T> actual;
        boolean done;
        final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
        final Consumer<? super T> onNext;

        /* renamed from: s */
        Subscription f386s;

        ParallelDoOnNextConditionalSubscriber(ConditionalSubscriber<? super T> actual2, Consumer<? super T> onNext2, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler2) {
            this.actual = actual2;
            this.onNext = onNext2;
            this.errorHandler = errorHandler2;
        }

        public void request(long n) {
            this.f386s.request(n);
        }

        public void cancel() {
            this.f386s.cancel();
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f386s, s)) {
                this.f386s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!tryOnNext(t) && !this.done) {
                this.f386s.request(1);
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:4:0x0008 A[LOOP:0: B:4:0x0008->B:13:0x0039, LOOP_START, PHI: r2 
          PHI: (r2v1 'retries' long) = (r2v0 'retries' long), (r2v2 'retries' long) binds: [B:3:0x0006, B:13:0x0039] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC, Splitter:B:4:0x0008] */
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
                io.reactivex.functions.Consumer<? super T> r0 = r8.onNext     // Catch:{ Throwable -> 0x0015 }
                r0.accept(r9)     // Catch:{ Throwable -> 0x0015 }
                io.reactivex.internal.fuseable.ConditionalSubscriber<? super T> r0 = r8.actual
                boolean r0 = r0.tryOnNext(r9)
                return r0
            L_0x0015:
                r0 = move-exception
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r0)
                io.reactivex.functions.BiFunction<? super java.lang.Long, ? super java.lang.Throwable, io.reactivex.parallel.ParallelFailureHandling> r4 = r8.errorHandler     // Catch:{ Throwable -> 0x004c }
                r5 = 1
                long r5 = r5 + r2
                r2 = r5
                java.lang.Long r5 = java.lang.Long.valueOf(r5)     // Catch:{ Throwable -> 0x004c }
                java.lang.Object r4 = r4.apply(r5, r0)     // Catch:{ Throwable -> 0x004c }
                java.lang.String r5 = "The errorHandler returned a null item"
                java.lang.Object r4 = p005io.reactivex.internal.functions.ObjectHelper.requireNonNull(r4, r5)     // Catch:{ Throwable -> 0x004c }
                io.reactivex.parallel.ParallelFailureHandling r4 = (p005io.reactivex.parallel.ParallelFailureHandling) r4     // Catch:{ Throwable -> 0x004c }
                int[] r5 = p005io.reactivex.internal.operators.parallel.ParallelDoOnNextTry.C08611.$SwitchMap$io$reactivex$parallel$ParallelFailureHandling
                int r6 = r4.ordinal()
                r5 = r5[r6]
                switch(r5) {
                    case 1: goto L_0x004b;
                    case 2: goto L_0x004a;
                    case 3: goto L_0x0043;
                    default: goto L_0x003c;
                }
            L_0x003c:
                r8.cancel()
                r8.onError(r0)
                return r1
            L_0x0043:
                r8.cancel()
                r8.onComplete()
                return r1
            L_0x004a:
                return r1
            L_0x004b:
                goto L_0x0008
            L_0x004c:
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
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.parallel.ParallelDoOnNextTry.ParallelDoOnNextConditionalSubscriber.tryOnNext(java.lang.Object):boolean");
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

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelDoOnNextTry$ParallelDoOnNextSubscriber */
    static final class ParallelDoOnNextSubscriber<T> implements ConditionalSubscriber<T>, Subscription {
        final Subscriber<? super T> actual;
        boolean done;
        final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
        final Consumer<? super T> onNext;

        /* renamed from: s */
        Subscription f387s;

        ParallelDoOnNextSubscriber(Subscriber<? super T> actual2, Consumer<? super T> onNext2, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler2) {
            this.actual = actual2;
            this.onNext = onNext2;
            this.errorHandler = errorHandler2;
        }

        public void request(long n) {
            this.f387s.request(n);
        }

        public void cancel() {
            this.f387s.cancel();
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f387s, s)) {
                this.f387s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!tryOnNext(t)) {
                this.f387s.request(1);
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:4:0x0008 A[LOOP:0: B:4:0x0008->B:14:0x003a, LOOP_START, PHI: r2 
          PHI: (r2v1 'retries' long) = (r2v0 'retries' long), (r2v2 'retries' long) binds: [B:3:0x0006, B:14:0x003a] A[DONT_GENERATE, DONT_INLINE]] */
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
                io.reactivex.functions.Consumer<? super T> r4 = r8.onNext     // Catch:{ Throwable -> 0x0015 }
                r4.accept(r9)     // Catch:{ Throwable -> 0x0015 }
                org.reactivestreams.Subscriber<? super T> r1 = r8.actual
                r1.onNext(r9)
                return r0
            L_0x0015:
                r4 = move-exception
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r4)
                io.reactivex.functions.BiFunction<? super java.lang.Long, ? super java.lang.Throwable, io.reactivex.parallel.ParallelFailureHandling> r5 = r8.errorHandler     // Catch:{ Throwable -> 0x004d }
                r6 = 1
                long r6 = r6 + r2
                r2 = r6
                java.lang.Long r6 = java.lang.Long.valueOf(r6)     // Catch:{ Throwable -> 0x004d }
                java.lang.Object r5 = r5.apply(r6, r4)     // Catch:{ Throwable -> 0x004d }
                java.lang.String r6 = "The errorHandler returned a null item"
                java.lang.Object r5 = p005io.reactivex.internal.functions.ObjectHelper.requireNonNull(r5, r6)     // Catch:{ Throwable -> 0x004d }
                io.reactivex.parallel.ParallelFailureHandling r5 = (p005io.reactivex.parallel.ParallelFailureHandling) r5     // Catch:{ Throwable -> 0x004d }
                r0 = r5
                int[] r5 = p005io.reactivex.internal.operators.parallel.ParallelDoOnNextTry.C08611.$SwitchMap$io$reactivex$parallel$ParallelFailureHandling
                int r6 = r0.ordinal()
                r5 = r5[r6]
                switch(r5) {
                    case 1: goto L_0x004c;
                    case 2: goto L_0x004b;
                    case 3: goto L_0x0044;
                    default: goto L_0x003d;
                }
            L_0x003d:
                r8.cancel()
                r8.onError(r4)
                return r1
            L_0x0044:
                r8.cancel()
                r8.onComplete()
                return r1
            L_0x004b:
                return r1
            L_0x004c:
                goto L_0x0008
            L_0x004d:
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
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.parallel.ParallelDoOnNextTry.ParallelDoOnNextSubscriber.tryOnNext(java.lang.Object):boolean");
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

    public ParallelDoOnNextTry(ParallelFlowable<T> source2, Consumer<? super T> onNext2, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler2) {
        this.source = source2;
        this.onNext = onNext2;
        this.errorHandler = errorHandler2;
    }

    public void subscribe(Subscriber<? super T>[] subscribers) {
        if (validate(subscribers)) {
            int n = subscribers.length;
            Subscriber<? super T>[] parents = new Subscriber[n];
            for (int i = 0; i < n; i++) {
                Subscriber<? super T> a = subscribers[i];
                if (a instanceof ConditionalSubscriber) {
                    parents[i] = new ParallelDoOnNextConditionalSubscriber<>((ConditionalSubscriber) a, this.onNext, this.errorHandler);
                } else {
                    parents[i] = new ParallelDoOnNextSubscriber<>(a, this.onNext, this.errorHandler);
                }
            }
            this.source.subscribe(parents);
        }
    }

    public int parallelism() {
        return this.source.parallelism();
    }
}
