package cn.wegfan.relicsmanagement.config.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class EmptyOrMaxValidatorForCharSequence implements ConstraintValidator<EmptyOrMax, CharSequence> {

    private BigDecimal maxValue;

    @Override
    public void initialize(EmptyOrMax constraintAnnotation) {
        maxValue = BigDecimal.valueOf(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null || value.length() == 0) {
            return true;
        }
        try {
            BigDecimal bigDecimal = new BigDecimal(value.toString());
            return bigDecimal.compareTo(maxValue) <= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}