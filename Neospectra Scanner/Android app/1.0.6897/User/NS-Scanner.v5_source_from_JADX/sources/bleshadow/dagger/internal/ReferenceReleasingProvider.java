package bleshadow.dagger.internal;

import bleshadow.javax.inject.Provider;
import java.lang.ref.WeakReference;

@GwtIncompatible
public final class ReferenceReleasingProvider<T> implements Provider<T> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final Object NULL = new Object();
    private final Provider<T> provider;
    private volatile Object strongReference;
    private volatile WeakReference<T> weakReference;

    private ReferenceReleasingProvider(Provider<T> provider2) {
        this.provider = provider2;
    }

    public void releaseStrongReference() {
        Object value = this.strongReference;
        if (value != null && value != NULL) {
            synchronized (this) {
                this.weakReference = new WeakReference<>(value);
                this.strongReference = null;
            }
        }
    }

    public void restoreStrongReference() {
        Object value = this.strongReference;
        if (this.weakReference != null && value == null) {
            synchronized (this) {
                Object value2 = this.strongReference;
                if (this.weakReference != null && value2 == null) {
                    Object value3 = this.weakReference.get();
                    if (value3 != null) {
                        this.strongReference = value3;
                        this.weakReference = null;
                    }
                }
            }
        }
    }

    public T get() {
        Object value = currentValue();
        if (value == null) {
            synchronized (this) {
                value = currentValue();
                if (value == null) {
                    value = this.provider.get();
                    if (value == null) {
                        value = NULL;
                    }
                    this.strongReference = value;
                }
            }
        }
        if (value == NULL) {
            return null;
        }
        return value;
    }

    private Object currentValue() {
        Object value = this.strongReference;
        if (value != null) {
            return value;
        }
        if (this.weakReference != null) {
            return this.weakReference.get();
        }
        return null;
    }

    public static <T> ReferenceReleasingProvider<T> create(Provider<T> delegate, ReferenceReleasingProviderManager references) {
        ReferenceReleasingProvider<T> provider2 = new ReferenceReleasingProvider<>((Provider) Preconditions.checkNotNull(delegate));
        references.addProvider(provider2);
        return provider2;
    }
}
