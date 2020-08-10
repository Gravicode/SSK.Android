package bleshadow.dagger.releasablereferences;

import bleshadow.dagger.internal.GwtIncompatible;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.ANNOTATION_TYPE})
@GwtIncompatible
public @interface CanReleaseReferences {
}
