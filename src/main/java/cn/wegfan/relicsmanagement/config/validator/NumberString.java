package cn.wegfan.relicsmanagement.config.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示被注解的元素必须是一个数字字符串
 */
@Constraint(validatedBy = {NumberStringValidator.class})
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NumberString {

    /**
     * @return 是否允许在开头出现减号
     */
    boolean allowMinusSign() default false;

    /**
     * @return 是否允许在开头出现加号
     */
    boolean allowPlusSign() default false;

    /**
     * @return 是否允许小数点
     */
    boolean allowDecimal() default false;

    /**
     * @return 是否允许科学计数法形式（1.23e4）
     */
    boolean allowExponent() default false;

    /**
     * @return 校验失败时的错误信息
     */
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}