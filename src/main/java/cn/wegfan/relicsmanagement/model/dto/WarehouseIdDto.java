package cn.wegfan.relicsmanagement.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 前端传的仓库编号对象
 */
@Data
public class WarehouseIdDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 仓库编号
     */
    private Integer warehouseId;

}