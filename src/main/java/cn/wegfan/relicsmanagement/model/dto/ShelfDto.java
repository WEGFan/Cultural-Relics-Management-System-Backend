package cn.wegfan.relicsmanagement.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 前端传的货架对象
 */
@Data
public class ShelfDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 货架名称
     */
    private String name;

    /**
     * 所属仓库编号
     */
    private Integer warehouseId;

}