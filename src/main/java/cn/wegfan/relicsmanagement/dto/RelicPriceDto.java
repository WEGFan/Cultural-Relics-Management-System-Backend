package cn.wegfan.relicsmanagement.dto;

import cn.wegfan.relicsmanagement.config.jackson.BigDecimalDeserializer;
import cn.wegfan.relicsmanagement.config.jackson.BigDecimalSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class RelicPriceDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 入馆价值
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal enterPrice;

    /**
     * 离馆价值
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal leavePrice;

}