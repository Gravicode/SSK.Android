package p008rx.exceptions;

/* renamed from: rx.exceptions.OnErrorNotImplementedException */
public class OnErrorNotImplementedException extends RuntimeException {
    private static final long serialVersionUID = -6298857009889503852L;

    public OnErrorNotImplementedException(String message, Throwable e) {
        super(message, e != null ? e : new NullPointerException());
    }

    public OnErrorNotImplementedException(Throwable e) {
        super(e != null ? e.getMessage() : null, e != null ? e : new NullPointerException());
    }
}
