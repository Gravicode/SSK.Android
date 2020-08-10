package bleshadow.dagger.internal;

import bleshadow.javax.inject.Provider;

public final class DelegateFactory<T> implements Factory<T> {
    private Provider<T> delegate;

    public T get() {
        if (this.delegate != null) {
            return this.delegate.get();
        }
        throw new IllegalStateException();
    }

    public void setDelegatedProvider(Provider<T> delegate2) {
        if (delegate2 == null) {
            throw new IllegalArgumentException();
        } else if (this.delegate != null) {
            throw new IllegalStateException();
        } else {
            this.delegate = delegate2;
        }
    }
}
