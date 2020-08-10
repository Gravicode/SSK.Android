package p008rx.exceptions;

/* renamed from: rx.exceptions.OnErrorFailedException */
public class OnErrorFailedException extends RuntimeException {
    private static final long serialVersionUID = -419289748403337611L;

    public OnErrorFailedException(String message, Throwable e) {
        super(message, e != null ? e : new NullPointerException());
    }

    public OnErrorFailedException(Throwable e) {
        super(e != null ? e.getMessage() : null, e != null ? e : new NullPointerException());
    }
}
