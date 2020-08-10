package p005io.reactivex.exceptions;

import p005io.reactivex.annotations.Beta;
import p005io.reactivex.annotations.NonNull;

@Beta
/* renamed from: io.reactivex.exceptions.OnErrorNotImplementedException */
public final class OnErrorNotImplementedException extends RuntimeException {
    private static final long serialVersionUID = -6298857009889503852L;

    public OnErrorNotImplementedException(String message, @NonNull Throwable e) {
        super(message, e != null ? e : new NullPointerException());
    }

    public OnErrorNotImplementedException(@NonNull Throwable e) {
        super(e != null ? e.getMessage() : null, e != null ? e : new NullPointerException());
    }
}
