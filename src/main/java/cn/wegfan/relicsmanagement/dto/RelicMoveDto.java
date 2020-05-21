package cn.wegfan.relicsmanagement.dto;

import lombok.Data;

import java.io.Serializable;

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

}