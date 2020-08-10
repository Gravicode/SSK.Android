package p005io.reactivex.exceptions;

import p005io.reactivex.annotations.Beta;

@Beta
/* renamed from: io.reactivex.exceptions.UndeliverableException */
public final class UndeliverableException extends IllegalStateException {
    private static final long serialVersionUID = 1644750035281290266L;

    public UndeliverableException(Throwable cause) {
        super(cause);
    }
}
