package cn.wegfan.relicsmanagement.dto;

import lombok.Data;

import java.io.Serializable;

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