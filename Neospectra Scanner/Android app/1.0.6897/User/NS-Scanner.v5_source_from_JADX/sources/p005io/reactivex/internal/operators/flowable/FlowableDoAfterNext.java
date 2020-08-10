package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.internal.fuseable.ConditionalSubscriber;
import p005io.reactivex.internal.subscribers.BasicFuseableConditionalSubscriber;
import p005io.reactivex.internal.subscribers.BasicFuseableSubscriber;

@Experimental
/* renamed from: io.reactivex.internal.operators.flowable.FlowableDoAfterNext */
public final class FlowableDoAfterNext<T> extends AbstractFlowableWithUpstream<T, T> {
    final Consumer<? super T> onAfterNext;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDoAfterNext$DoAfterConditionalSubscriber */
    static final class DoAfterConditionalSubscriber<T> extends BasicFuseableConditionalSubscriber<T, T> {
        final Consumer<? super T> onAfterNext;

        DoAfterConditionalSubscriber(ConditionalSubscriber<? super T> actual, Consumer<? super T> onAfterNext2) {
            super(actual);
            this.onAfterNext = onAfterNext2;
        }

        public void onNext(T t) {
            this.actual.onNext(t);
            if (this.sourceMode == 0) {
                try {
                    this.onAfterNext.accept(t);
                } catch (Throwable ex) {
                    fail(ex);
                }
            }
        }

        public boolean tryOnNext(T t) {
            boolean b = this.actual.tryOnNext(t);
            try {
                this.onAfterNext.accept(t);
            } catch (Throwable ex) {
                fail(ex);
            }
            return b;
        }

        public int requestFusion(int mode) {
            return transitiveBoundaryFusion(mode);
        }

        @Nullable
        public T poll() throws Exception {
            T v = this.f428qs.poll();
            if (v != null) {
                this.onAfterNext.accept(v);
            }
            return v;
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDoAfterNext$DoAfterSubscriber */
    static final class DoAfterSubscriber<T> extends BasicFuseableSubscriber<T, T> {
        final Consumer<? super T> onAfterNext;

        DoAfterSubscriber(Subscriber<? super T> actual, Consumer<? super T> onAfterNext2) {
            super(actual);
            this.onAfterNext = onAfterNext2;
        }

        public void onNext(T t) {
            if (!this.done) {
                this.actual.onNext(t);
                if (this.sourceMode == 0) {
                    try {
                        this.onAfterNext.accept(t);
                    } catch (Throwable ex) {
                        fail(ex);
                    }
                }
            }
        }

        public int requestFusion(int mode) {
            return transitiveBoundaryFusion(mode);
        }

        @Nullable
        public T poll() throws Exception {
            T v = this.f430qs.poll();
            if (v != null) {
                this.onAfterNext.accept(v);
            }
            return v;
        }
    }

    public FlowableDoAfterNext(Flowable<T> source, Consumer<? super T> onAfterNext2) {
        super(source);
        this.onAfterNext = onAfterNext2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        if (s instanceof ConditionalSubscriber) {
            this.source.subscribe((FlowableSubscriber<? super T>) new DoAfterConditionalSubscriber<Object>((ConditionalSubscriber) s, this.onAfterNext));
        } else {
            this.source.subscribe((FlowableSubscriber<? super T>) new DoAfterSubscriber<Object>(s, this.onAfterNext));
        }
    }
}
