package p005io.reactivex.functions;

import p005io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.functions.Predicate */
public interface Predicate<T> {
    boolean test(@NonNull T t) throws Exception;
}
