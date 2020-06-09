package cn.wegfan.relicsmanagement.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class RelicCheckDetailVo {

    /**
     * 文物编号
     */
    private Integer relicId;

    /**
     * 文物名称
     */
    private String name;

    /**
     * 文物照片地址
     */
    private String picturePath;

    /**
     * 盘点前仓库id
     */
    private Integer oldWarehouseId;

    /**
     * 盘点前仓库名
     */
    private String oldWarehouseName;

    /**
     * 盘点前货架id
     */
    private Integer oldShelfId;

    /**
     * 盘点前货架名
     */
    private String oldShelfName;

    /**
     * 盘点后仓库id
     */
    private Integer newWarehouseId;

    /**
     * 盘点后仓库名
     */
    private String newWarehouseName;

    /**
     * 盘点后货架id
     */
    private Integer newShelfId;

    /**
     * 盘点后货架名
     */
    private String newShelfName;

    /**
     * 盘点人姓名
     */
    private String operatorName;

    /**
     * 盘点时间
     */
    private Date checkTime;

}