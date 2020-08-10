package p008rx.internal.operators;

import p008rx.Completable;
import p008rx.Completable.OnSubscribe;
import p008rx.CompletableSubscriber;
import p008rx.Single;
import p008rx.SingleSubscriber;
import p008rx.Subscription;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func1;

/* renamed from: rx.internal.operators.CompletableFlatMapSingleToCompletable */
public final class CompletableFlatMapSingleToCompletable<T> implements OnSubscribe {
    final Func1<? super T, ? extends Completable> mapper;
    final Single<T> source;

    /* renamed from: rx.internal.operators.CompletableFlatMapSingleToCompletable$SourceSubscriber */
    static final class SourceSubscriber<T> extends SingleSubscriber<T> implements CompletableSubscriber {
        final CompletableSubscriber actual;
        final Func1<? super T, ? extends Completable> mapper;

        public SourceSubscriber(CompletableSubscriber actual2, Func1<? super T, ? extends Completable> mapper2) {
            this.actual = actual2;
            this.mapper = mapper2;
        }

        public void onSuccess(T value) {
            try {
                Completable c = (Completable) this.mapper.call(value);
                if (c == null) {
                    onError(new NullPointerException("The mapper returned a null Completable"));
                } else {
                    c.subscribe((CompletableSubscriber) this);
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                onError(ex);
            }
        }

        public void onError(Throwable error) {
            this.actual.onError(error);
        }

        public void onCompleted() {
            this.actual.onCompleted();
        }

        public void onSubscribe(Subscription d) {
            add(d);
        }
    }

    public CompletableFlatMapSingleToCompletable(Single<T> source2, Func1<? super T, ? extends Completable> mapper2) {
        this.source = source2;
        this.mapper = mapper2;
    }

    public void call(CompletableSubscriber t) {
        SourceSubscriber<T> parent = new SourceSubscriber<>(t, this.mapper);
        t.onSubscribe(parent);
        this.source.subscribe((SingleSubscriber<? super T>) parent);
    }
}
