package bleshadow.dagger.internal;

import bleshadow.dagger.releasablereferences.ReleasableReferenceManager;
import bleshadow.dagger.releasablereferences.TypedReleasableReferenceManager;
import java.lang.annotation.Annotation;

@GwtIncompatible
public final class TypedReleasableReferenceManagerDecorator<M extends Annotation> implements TypedReleasableReferenceManager<M> {
    private final ReleasableReferenceManager delegate;
    private final M metadata;

    public TypedReleasableReferenceManagerDecorator(ReleasableReferenceManager delegate2, M metadata2) {
        this.delegate = (ReleasableReferenceManager) Preconditions.checkNotNull(delegate2);
        this.metadata = (Annotation) Preconditions.checkNotNull(metadata2);
    }

    public Class<? extends Annotation> scope() {
        return this.delegate.scope();
    }

    public M metadata() {
        return this.metadata;
    }

    public void releaseStrongReferences() {
        this.delegate.releaseStrongReferences();
    }

    public void restoreStrongReferences() {
        this.delegate.restoreStrongReferences();
    }
}
