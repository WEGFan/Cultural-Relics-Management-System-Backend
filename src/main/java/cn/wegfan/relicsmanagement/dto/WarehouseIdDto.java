package cn.wegfan.relicsmanagement.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class WarehouseIdDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 仓库编号
     */
    private Integer warehouseId;

}