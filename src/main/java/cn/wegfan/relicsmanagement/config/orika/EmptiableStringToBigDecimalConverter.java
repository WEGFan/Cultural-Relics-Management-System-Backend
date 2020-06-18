package cn.wegfan.relicsmanagement.config.orika;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

import java.math.BigDecimal;

/**
 * 可空字符串到 BigDecimal 转换器
 */
public class EmptiableStringToBigDecimalConverter extends CustomConverter<String, BigDecimal> {

    @Override
    public BigDecimal convert(String source, Type<? extends BigDecimal> destinationType, MappingContext mappingContext) {
        if (source.length() == 0) {
            return null;
        }
        return new BigDecimal(source);
    }

}
