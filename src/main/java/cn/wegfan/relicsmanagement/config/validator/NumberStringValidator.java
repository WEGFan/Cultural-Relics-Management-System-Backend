package cn.wegfan.relicsmanagement.config.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class NumberStringValidator implements ConstraintValidator<NumberString, CharSequence> {

    private boolean allowMinusSign;

    private boolean allowPlusSign;

    private boolean allowDecimal;

    private boolean allowExponent;

    @Override
    public void initialize(NumberString constraintAnnotation) {
        allowMinusSign = constraintAnnotation.allowMinusSign();
        allowPlusSign = constraintAnnotation.allowPlusSign();
        allowDecimal = constraintAnnotation.allowDecimal();
        allowExponent = constraintAnnotation.allowExponent();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        // 空字符串和null当成校验通过
        if (value == null || value.length() == 0) {
            return true;
        }
        String valueString = value.toString();
        if ((!allowMinusSign && valueString.charAt(0) == '-') || (!allowPlusSign && valueString.charAt(0) == '+')) {
            return false;
        }
        if ((!allowDecimal && valueString.contains(".")) ||
                (!allowExponent && (valueString.contains("e") || valueString.contains("E")))) {
            return false;
        }
        try {
            BigDecimal bigDecimal = new BigDecimal(value.toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}