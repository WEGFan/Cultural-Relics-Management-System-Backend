package cn.wegfan.relicsmanagement.model.stringdto;

import cn.wegfan.relicsmanagement.config.validator.NumberString;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 前端传的货架对象，所有成员变量均为字符串类型
 */
@Data
public class ShelfStringDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 货架名称
     */
    @NotBlank(message = "货架名不能为空")
    private String name;

    /**
     * 所属仓库编号
     */
    @NumberString(message = "仓库 ID 不合法")
    @Range(min = 0, max = 999_999_999, message = "仓库 ID 需为 9 位以内数字")
    private String warehouseId;

}