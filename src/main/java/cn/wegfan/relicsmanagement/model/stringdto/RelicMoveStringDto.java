package cn.wegfan.relicsmanagement.model.stringdto;

import cn.wegfan.relicsmanagement.config.validator.EmptyOrRange;
import cn.wegfan.relicsmanagement.config.validator.NumberString;
import lombok.Data;

import java.io.Serializable;

/**
 * 前端传的文物移动对象，所有成员变量均为字符串类型
 */
@Data
public class RelicMoveStringDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 仓库编号
     */
    @NumberString(message = "仓库 ID 不合法")
    @EmptyOrRange(min = 0, max = 999_999_999, message = "仓库 ID 需为 9 位以内数字")
    private String warehouseId;

    /**
     * 货架编号
     */
    @NumberString(message = "货架 ID 不合法")
    @EmptyOrRange(min = 0, max = 999_999_999, message = "货架 ID 需为 9 位以内数字")
    private String shelfId;

}