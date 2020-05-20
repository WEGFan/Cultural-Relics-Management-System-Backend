package cn.wegfan.relicsmanagement.vo;

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
     * 盘点后仓库id
     */
    private Integer warehouseId;

    /**
     * 盘点后货架id
     */
    private Integer shelfId;

    /**
     * 盘点时间
     */
    private Date checkTime;

}