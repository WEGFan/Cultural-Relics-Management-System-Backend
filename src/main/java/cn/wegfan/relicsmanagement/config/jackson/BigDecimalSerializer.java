package cn.wegfan.relicsmanagement.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * 自定义 BigDecimal 序列化器
 */
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(BigDecimal value, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        if (value == null) {
            generator.writeNull();
        } else {
            // 保留两位小数，四舍五入
            generator.writeString(value.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        }
    }

}
