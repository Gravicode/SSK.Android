package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.meta.TypeQualifier;
import javax.annotation.meta.TypeQualifierValidator;
import javax.annotation.meta.When;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@TypeQualifier(applicableTo = Number.class)
public @interface Nonnegative {

    public static class Checker implements TypeQualifierValidator<Nonnegative> {
        public When forConstantValue(Nonnegative annotation, Object v) {
            boolean isNegative;
            if (!(v instanceof Number)) {
                return When.NEVER;
            }
            Number value = (Number) v;
            boolean z = false;
            if (value instanceof Long) {
                if (value.longValue() < 0) {
                    z = true;
                }
                isNegative = z;
            } else if (value instanceof Double) {
                if (value.doubleValue() < 0.0d) {
                    z = true;
                }
                isNegative = z;
            } else if (value instanceof Float) {
                if (value.floatValue() < 0.0f) {
                    z = true;
                }
                isNegative = z;
            } else {
                isNegative = value.intValue() < 0;
            }
            if (isNegative) {
                return When.NEVER;
            }
            return When.ALWAYS;
        }
    }

    When when() default When.ALWAYS;
}
