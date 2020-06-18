package cn.wegfan.relicsmanagement.model.stringdto;

import cn.wegfan.relicsmanagement.config.validator.NumberString;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * 前端传的仓库编号对象，所有成员变量均为字符串类型
 */
@Data
public class WarehouseIdStringDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 仓库编号
     */
    @NumberString(message = "仓库 ID 不合法")
    @Range(min = 0, max = 999_999_999, message = "仓库 ID 不合法")
    private String warehouseId;

}