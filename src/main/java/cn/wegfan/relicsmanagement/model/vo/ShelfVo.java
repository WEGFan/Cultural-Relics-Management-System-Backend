package cn.wegfan.relicsmanagement.model.vo;

import lombok.Data;

@Data
public class ShelfVo {

    /**
     * 货架编号
     */
    private Integer id;

    /**
     * 货架名
     */
    private String name;

    /**
     * 所属仓库编号
     */
    private Integer warehouseId;

    /**
     * 所属仓库名
     */
    private String warehouseName;

}
