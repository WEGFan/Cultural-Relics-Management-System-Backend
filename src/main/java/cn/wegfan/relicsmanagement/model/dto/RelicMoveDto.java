package cn.wegfan.relicsmanagement.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 前端传的文物移动对象
 */
@Data
public class RelicMoveDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 仓库编号
     */
    private Integer warehouseId;

    /**
     * 货架编号
     */
    private Integer shelfId;

    public RelicMoveDto(Integer warehouseId, Integer shelfId) {
        this.warehouseId = warehouseId;
        this.shelfId = shelfId;
    }

}