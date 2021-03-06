package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Action;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.internal.fuseable.ConditionalSubscriber;
import p005io.reactivex.internal.subscribers.BasicFuseableConditionalSubscriber;
import p005io.reactivex.internal.subscribers.BasicFuseableSubscriber;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableDoOnEach */
public final class FlowableDoOnEach<T> extends AbstractFlowableWithUpstream<T, T> {
    final Action onAfterTerminate;
    final Action onComplete;
    final Consumer<? super Throwable> onError;
    final Consumer<? super T> onNext;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDoOnEach$DoOnEachConditionalSubscriber */
    static final class DoOnEachConditionalSubscriber<T> extends BasicFuseableConditionalSubscriber<T, T> {
        final Action onAfterTerminate;
        final Action onComplete;
        final Consumer<? super Throwable> onError;
        final Consumer<? super T> onNext;

        DoOnEachConditionalSubscriber(ConditionalSubscriber<? super T> actual, Consumer<? super T> onNext2, Consumer<? super Throwable> onError2, Action onComplete2, Action onAfterTerminate2) {
            super(actual);
            this.onNext = onNext2;
            this.onError = onError2;
            this.onComplete = onComplete2;
            this.onAfterTerminate = onAfterTerminate2;
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.sourceMode != 0) {
                    this.actual.onNext(null);
                    return;
                }
                try {
                    this.onNext.accept(t);
                    this.actual.onNext(t);
                } catch (Throwable e) {
                    fail(e);
                }
            }
        }

        public boolean tryOnNext(T t) {
            if (this.done) {
                return false;
            }
            try {
                this.onNext.accept(t);
                return this.actual.tryOnNext(t);
            } catch (Throwable e) {
                fail(e);
                return false;
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            boolean relay = true;
            try {
                this.onError.accept(t);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                this.actual.onError(new CompositeException(t, e));
                relay = false;
            }
            if (relay) {
                this.actual.onError(t);
            }
            try {
                this.onAfterTerminate.run();
            } catch (Throwable e2) {
                Exceptions.throwIfFatal(e2);
                RxJavaPlugins.onError(e2);
            }
        }

        public void onComplete() {
            if (!this.done) {
                try {
                    this.onComplete.run();
                    this.done = true;
                    this.actual.onComplete();
                    try {
                        this.onAfterTerminate.run();
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        RxJavaPlugins.onError(e);
                    }
                } catch (Throwable e2) {
                    fail(e2);
                }
            }
        }

        public int requestFusion(int mode) {
            return transitiveBoundaryFusion(mode);
        }

        @Nullable
        public T poll() throws Exception {
            try {
                T v = this.f428qs.poll();
                if (v != null) {
                    try {
                        this.onNext.accept(v);
                        this.onAfterTerminate.run();
                    } catch (Throwable exc) {
                        throw new CompositeException(ex, exc);
                    }
                } else if (this.sourceMode == 1) {
                    this.onComplete.run();
                    this.onAfterTerminate.run();
                }
                return v;
            } catch (Throwable exc2) {
                throw new CompositeException(ex, exc2);
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDoOnEach$DoOnEachSubscriber */
    static final class DoOnEachSubscriber<T> extends BasicFuseableSubscriber<T, T> {
        final Action onAfterTerminate;
        final Action onComplete;
        final Consumer<? super Throwable> onError;
        final Consumer<? super T> onNext;

        DoOnEachSubscriber(Subscriber<? super T> actual, Consumer<? super T> onNext2, Consumer<? super Throwable> onError2, Action onComplete2, Action onAfterTerminate2) {
            super(actual);
            this.onNext = onNext2;
            this.onError = onError2;
            this.onComplete = onComplete2;
            this.onAfterTerminate = onAfterTerminate2;
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.sourceMode != 0) {
                    this.actual.onNext(null);
                    return;
                }
                try {
                    this.onNext.accept(t);
                    this.actual.onNext(t);
                } catch (Throwable e) {
                    fail(e);
                }
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            boolean relay = true;
            try {
                this.onError.accept(t);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                this.actual.onError(new CompositeException(t, e));
                relay = false;
            }
            if (relay) {
                this.actual.onError(t);
            }
            try {
                this.onAfterTerminate.run();
            } catch (Throwable e2) {
                Exceptions.throwIfFatal(e2);
                RxJavaPlugins.onError(e2);
            }
        }

        public void onComplete() {
            if (!this.done) {
                try {
                    this.onComplete.run();
                    this.done = true;
                    this.actual.onComplete();
                    try {
                        this.onAfterTerminate.run();
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        RxJavaPlugins.onError(e);
                    }
                } catch (Throwable e2) {
                    fail(e2);
                }
            }
        }

        public int requestFusion(int mode) {
            return transitiveBoundaryFusion(mode);
        }

        @Nullable
        public T poll() throws Exception {
            try {
                T v = this.f430qs.poll();
                if (v != null) {
                    try {
                        this.onNext.accept(v);
                        this.onAfterTerminate.run();
                    } catch (Throwable exc) {
                        throw new CompositeException(ex, exc);
                    }
                } else if (this.sourceMode == 1) {
                    this.onComplete.run();
                    this.onAfterTerminate.run();
                }
                return v;
            } catch (Throwable exc2) {
                throw new CompositeException(ex, exc2);
            }
        }
    }

    public FlowableDoOnEach(Flowable<T> source, Consumer<? super T> onNext2, Consumer<? super Throwable> onError2, Action onComplete2, Action onAfterTerminate2) {
        super(source);
        this.onNext = onNext2;
        this.onError = onError2;
        this.onComplete = onComplete2;
        this.onAfterTerminate = onAfterTerminate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        if (s instanceof ConditionalSubscriber) {
            Flowable flowable = this.source;
            DoOnEachConditionalSubscriber doOnEachConditionalSubscriber = new DoOnEachConditionalSubscriber((ConditionalSubscriber) s, this.onNext, this.onError, this.onComplete, this.onAfterTerminate);
            flowable.subscribe((FlowableSubscriber<? super T>) doOnEachConditionalSubscriber);
            return;
        }
        Flowable flowable2 = this.source;
        DoOnEachSubscriber doOnEachSubscriber = new DoOnEachSubscriber(s, this.onNext, this.onError, this.onComplete, this.onAfterTerminate);
        flowable2.subscribe((FlowableSubscriber<? super T>) doOnEachSubscriber);
    }
}
