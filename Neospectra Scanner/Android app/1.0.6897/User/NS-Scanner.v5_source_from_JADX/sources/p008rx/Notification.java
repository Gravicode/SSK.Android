package p008rx;

/* renamed from: rx.Notification */
public final class Notification<T> {
    private static final Notification<Void> ON_COMPLETED = new Notification<>(Kind.OnCompleted, null, null);
    private final Kind kind;
    private final Throwable throwable;
    private final T value;

    /* renamed from: rx.Notification$Kind */
    public enum Kind {
        OnNext,
        OnError,
        OnCompleted
    }

    public static <T> Notification<T> createOnNext(T t) {
        return new Notification<>(Kind.OnNext, t, null);
    }

    public static <T> Notification<T> createOnError(Throwable e) {
        return new Notification<>(Kind.OnError, null, e);
    }

    public static <T> Notification<T> createOnCompleted() {
        return ON_COMPLETED;
    }

    @Deprecated
    public static <T> Notification<T> createOnCompleted(Class<T> cls) {
        return ON_COMPLETED;
    }

    private Notification(Kind kind2, T value2, Throwable e) {
        this.value = value2;
        this.throwable = e;
        this.kind = kind2;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public T getValue() {
        return this.value;
    }

    public boolean hasValue() {
        return isOnNext() && this.value != null;
    }

    public boolean hasThrowable() {
        return isOnError() && this.throwable != null;
    }

    public Kind getKind() {
        return this.kind;
    }

    public boolean isOnError() {
        return getKind() == Kind.OnError;
    }

    public boolean isOnCompleted() {
        return getKind() == Kind.OnCompleted;
    }

    public boolean isOnNext() {
        return getKind() == Kind.OnNext;
    }

    public void accept(Observer<? super T> observer) {
        if (this.kind == Kind.OnNext) {
            observer.onNext(getValue());
        } else if (this.kind == Kind.OnCompleted) {
            observer.onCompleted();
        } else {
            observer.onError(getThrowable());
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append('[');
        sb.append(super.toString());
        sb.append(' ');
        StringBuilder str = sb.append(getKind());
        if (hasValue()) {
            str.append(' ');
            str.append(getValue());
        }
        if (hasThrowable()) {
            str.append(' ');
            str.append(getThrowable().getMessage());
        }
        str.append(']');
        return str.toString();
    }

    public int hashCode() {
        int hash = getKind().hashCode();
        if (hasValue()) {
            hash = (hash * 31) + getValue().hashCode();
        }
        if (hasThrowable()) {
            return (hash * 31) + getThrowable().hashCode();
        }
        return hash;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Notification<?> notification = (Notification) obj;
        if (notification.getKind() == getKind() && ((this.value == notification.value || (this.value != null && this.value.equals(notification.value))) && (this.throwable == notification.throwable || (this.throwable != null && this.throwable.equals(notification.throwable))))) {
            z = true;
        }
        return z;
    }
}
