package cn.wegfan.relicsmanagement.config.orika;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.builtin.FromStringConverter;
import ma.glasnost.orika.metadata.Type;

/**
 * 可空字符串转换器
 */
public class FromEmptiableStringConverter extends FromStringConverter {

    @Override
    public Object convert(Object source, Type<? extends Object> destinationType, MappingContext context) {
        if (((String)source).length() == 0) {
            return null;
        }
        return super.convert(source, destinationType, context);
    }

}
