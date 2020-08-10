package p008rx.internal.operators;

import p008rx.Single;
import p008rx.Single.OnSubscribe;
import p008rx.SingleSubscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func1;

/* renamed from: rx.internal.operators.SingleOperatorOnErrorResumeNext */
public final class SingleOperatorOnErrorResumeNext<T> implements OnSubscribe<T> {
    private final Single<? extends T> originalSingle;
    final Func1<Throwable, ? extends Single<? extends T>> resumeFunctionInCaseOfError;

    private SingleOperatorOnErrorResumeNext(Single<? extends T> originalSingle2, Func1<Throwable, ? extends Single<? extends T>> resumeFunctionInCaseOfError2) {
        if (originalSingle2 == null) {
            throw new NullPointerException("originalSingle must not be null");
        } else if (resumeFunctionInCaseOfError2 == null) {
            throw new NullPointerException("resumeFunctionInCaseOfError must not be null");
        } else {
            this.originalSingle = originalSingle2;
            this.resumeFunctionInCaseOfError = resumeFunctionInCaseOfError2;
        }
    }

    public static <T> SingleOperatorOnErrorResumeNext<T> withFunction(Single<? extends T> originalSingle2, Func1<Throwable, ? extends Single<? extends T>> resumeFunctionInCaseOfError2) {
        return new SingleOperatorOnErrorResumeNext<>(originalSingle2, resumeFunctionInCaseOfError2);
    }

    public static <T> SingleOperatorOnErrorResumeNext<T> withOther(Single<? extends T> originalSingle2, final Single<? extends T> resumeSingleInCaseOfError) {
        if (resumeSingleInCaseOfError != null) {
            return new SingleOperatorOnErrorResumeNext<>(originalSingle2, new Func1<Throwable, Single<? extends T>>() {
                public Single<? extends T> call(Throwable throwable) {
                    return resumeSingleInCaseOfError;
                }
            });
        }
        throw new NullPointerException("resumeSingleInCaseOfError must not be null");
    }

    public void call(final SingleSubscriber<? super T> child) {
        SingleSubscriber<? super T> parent = new SingleSubscriber<T>() {
            public void onSuccess(T value) {
                child.onSuccess(value);
            }

            public void onError(Throwable error) {
                try {
                    ((Single) SingleOperatorOnErrorResumeNext.this.resumeFunctionInCaseOfError.call(error)).subscribe(child);
                } catch (Throwable innerError) {
                    Exceptions.throwOrReport(innerError, child);
                }
            }
        };
        child.add(parent);
        this.originalSingle.subscribe(parent);
    }
}
