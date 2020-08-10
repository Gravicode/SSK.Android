package p005io.reactivex.internal.operators.parallel;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.internal.fuseable.ConditionalSubscriber;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.parallel.ParallelFailureHandling;
import p005io.reactivex.parallel.ParallelFlowable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.parallel.ParallelFilterTry */
public final class ParallelFilterTry<T> extends ParallelFlowable<T> {
    final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
    final Predicate<? super T> predicate;
    final ParallelFlowable<T> source;

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelFilterTry$BaseFilterSubscriber */
    static abstract class BaseFilterSubscriber<T> implements ConditionalSubscriber<T>, Subscription {
        boolean done;
        final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
        final Predicate<? super T> predicate;

        /* renamed from: s */
        Subscription f389s;

        BaseFilterSubscriber(Predicate<? super T> predicate2, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler2) {
            this.predicate = predicate2;
            this.errorHandler = errorHandler2;
        }

        public final void request(long n) {
            this.f389s.request(n);
        }

        public final void cancel() {
            this.f389s.cancel();
        }

        public final void onNext(T t) {
            if (!tryOnNext(t) && !this.done) {
                this.f389s.request(1);
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelFilterTry$ParallelFilterConditionalSubscriber */
    static final class ParallelFilterConditionalSubscriber<T> extends BaseFilterSubscriber<T> {
        final ConditionalSubscriber<? super T> actual;

        ParallelFilterConditionalSubscriber(ConditionalSubscriber<? super T> actual2, Predicate<? super T> predicate, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler) {
            super(predicate, errorHandler);
            this.actual = actual2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f389s, s)) {
                this.f389s = s;
                this.actual.onSubscribe(this);
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:3:0x0007 A[LOOP:0: B:3:0x0007->B:16:0x0042, LOOP_START, PHI: r2 
          PHI: (r2v1 'retries' long) = (r2v0 'retries' long), (r2v2 'retries' long) binds: [B:2:0x0005, B:16:0x0042] A[DONT_GENERATE, DONT_INLINE]] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean tryOnNext(T r9) {
            /*
                r8 = this;
                boolean r0 = r8.done
                r1 = 0
                if (r0 != 0) goto L_0x006c
                r2 = 0
            L_0x0007:
                r0 = 1
                io.reactivex.functions.Predicate r4 = r8.predicate     // Catch:{ Throwable -> 0x001d }
                boolean r4 = r4.test(r9)     // Catch:{ Throwable -> 0x001d }
                if (r4 == 0) goto L_0x001b
                io.reactivex.internal.fuseable.ConditionalSubscriber<? super T> r5 = r8.actual
                boolean r5 = r5.tryOnNext(r9)
                if (r5 == 0) goto L_0x001b
                goto L_0x001c
            L_0x001b:
                r0 = 0
            L_0x001c:
                return r0
            L_0x001d:
                r4 = move-exception
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r4)
                io.reactivex.functions.BiFunction r5 = r8.errorHandler     // Catch:{ Throwable -> 0x0055 }
                r6 = 1
                long r6 = r6 + r2
                r2 = r6
                java.lang.Long r6 = java.lang.Long.valueOf(r6)     // Catch:{ Throwable -> 0x0055 }
                java.lang.Object r5 = r5.apply(r6, r4)     // Catch:{ Throwable -> 0x0055 }
                java.lang.String r6 = "The errorHandler returned a null item"
                java.lang.Object r5 = p005io.reactivex.internal.functions.ObjectHelper.requireNonNull(r5, r6)     // Catch:{ Throwable -> 0x0055 }
                io.reactivex.parallel.ParallelFailureHandling r5 = (p005io.reactivex.parallel.ParallelFailureHandling) r5     // Catch:{ Throwable -> 0x0055 }
                r0 = r5
                int[] r5 = p005io.reactivex.internal.operators.parallel.ParallelFilterTry.C08621.$SwitchMap$io$reactivex$parallel$ParallelFailureHandling
                int r6 = r0.ordinal()
                r5 = r5[r6]
                switch(r5) {
                    case 1: goto L_0x0054;
                    case 2: goto L_0x0053;
                    case 3: goto L_0x004c;
                    default: goto L_0x0045;
                }
            L_0x0045:
                r8.cancel()
                r8.onError(r4)
                return r1
            L_0x004c:
                r8.cancel()
                r8.onComplete()
                return r1
            L_0x0053:
                return r1
            L_0x0054:
                goto L_0x0007
            L_0x0055:
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
            L_0x006c:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.parallel.ParallelFilterTry.ParallelFilterConditionalSubscriber.tryOnNext(java.lang.Object):boolean");
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

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelFilterTry$ParallelFilterSubscriber */
    static final class ParallelFilterSubscriber<T> extends BaseFilterSubscriber<T> {
        final Subscriber<? super T> actual;

        ParallelFilterSubscriber(Subscriber<? super T> actual2, Predicate<? super T> predicate, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler) {
            super(predicate, errorHandler);
            this.actual = actual2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f389s, s)) {
                this.f389s = s;
                this.actual.onSubscribe(this);
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:3:0x0007 A[LOOP:0: B:3:0x0007->B:15:0x003e, LOOP_START, PHI: r2 
          PHI: (r2v1 'retries' long) = (r2v0 'retries' long), (r2v2 'retries' long) binds: [B:2:0x0005, B:15:0x003e] A[DONT_GENERATE, DONT_INLINE]] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean tryOnNext(T r9) {
            /*
                r8 = this;
                boolean r0 = r8.done
                r1 = 0
                if (r0 != 0) goto L_0x0068
                r2 = 0
            L_0x0007:
                r0 = 1
                io.reactivex.functions.Predicate r4 = r8.predicate     // Catch:{ Throwable -> 0x0019 }
                boolean r4 = r4.test(r9)     // Catch:{ Throwable -> 0x0019 }
                if (r4 == 0) goto L_0x0018
                org.reactivestreams.Subscriber<? super T> r1 = r8.actual
                r1.onNext(r9)
                return r0
            L_0x0018:
                return r1
            L_0x0019:
                r4 = move-exception
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r4)
                io.reactivex.functions.BiFunction r5 = r8.errorHandler     // Catch:{ Throwable -> 0x0051 }
                r6 = 1
                long r6 = r6 + r2
                r2 = r6
                java.lang.Long r6 = java.lang.Long.valueOf(r6)     // Catch:{ Throwable -> 0x0051 }
                java.lang.Object r5 = r5.apply(r6, r4)     // Catch:{ Throwable -> 0x0051 }
                java.lang.String r6 = "The errorHandler returned a null item"
                java.lang.Object r5 = p005io.reactivex.internal.functions.ObjectHelper.requireNonNull(r5, r6)     // Catch:{ Throwable -> 0x0051 }
                io.reactivex.parallel.ParallelFailureHandling r5 = (p005io.reactivex.parallel.ParallelFailureHandling) r5     // Catch:{ Throwable -> 0x0051 }
                r0 = r5
                int[] r5 = p005io.reactivex.internal.operators.parallel.ParallelFilterTry.C08621.$SwitchMap$io$reactivex$parallel$ParallelFailureHandling
                int r6 = r0.ordinal()
                r5 = r5[r6]
                switch(r5) {
                    case 1: goto L_0x0050;
                    case 2: goto L_0x004f;
                    case 3: goto L_0x0048;
                    default: goto L_0x0041;
                }
            L_0x0041:
                r8.cancel()
                r8.onError(r4)
                return r1
            L_0x0048:
                r8.cancel()
                r8.onComplete()
                return r1
            L_0x004f:
                return r1
            L_0x0050:
                goto L_0x0007
            L_0x0051:
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
            L_0x0068:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.parallel.ParallelFilterTry.ParallelFilterSubscriber.tryOnNext(java.lang.Object):boolean");
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

    public ParallelFilterTry(ParallelFlowable<T> source2, Predicate<? super T> predicate2, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler2) {
        this.source = source2;
        this.predicate = predicate2;
        this.errorHandler = errorHandler2;
    }

    public void subscribe(Subscriber<? super T>[] subscribers) {
        if (validate(subscribers)) {
            int n = subscribers.length;
            Subscriber<? super T>[] parents = new Subscriber[n];
            for (int i = 0; i < n; i++) {
                Subscriber<? super T> a = subscribers[i];
                if (a instanceof ConditionalSubscriber) {
                    parents[i] = new ParallelFilterConditionalSubscriber<>((ConditionalSubscriber) a, this.predicate, this.errorHandler);
                } else {
                    parents[i] = new ParallelFilterSubscriber<>(a, this.predicate, this.errorHandler);
                }
            }
            this.source.subscribe(parents);
        }
    }

    public int parallelism() {
        return this.source.parallelism();
    }
}
