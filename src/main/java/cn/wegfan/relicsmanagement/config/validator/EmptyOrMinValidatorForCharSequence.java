package cn.wegfan.relicsmanagement.config.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class EmptyOrMinValidatorForCharSequence implements ConstraintValidator<EmptyOrMin, CharSequence> {

    private BigDecimal minValue;

    @Override
    public void initialize(EmptyOrMin constraintAnnotation) {
        minValue = BigDecimal.valueOf(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null || value.length() == 0) {
            return true;
        }
        try {
            BigDecimal bigDecimal = new BigDecimal(value.toString());
            return bigDecimal.compareTo(minValue) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}