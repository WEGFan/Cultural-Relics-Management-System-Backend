package cn.wegfan.relicsmanagement.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String decimalString = jsonParser.getValueAsString();
        if (decimalString == null) {
            return null;
        }
        return new BigDecimal(decimalString).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}