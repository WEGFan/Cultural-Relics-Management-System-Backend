package cn.wegfan.relicsmanagement.config.validator;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

@Constraint(validatedBy = {})
@Documented
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@EmptyOrMin(0)
@EmptyOrMax(Long.MAX_VALUE)
@ReportAsSingleViolation
public @interface EmptyOrRange {

    @OverridesAttribute(constraint = EmptyOrMin.class, name = "value") long min() default 0;

    @OverridesAttribute(constraint = EmptyOrMax.class, name = "value") long max() default Long.MAX_VALUE;

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}