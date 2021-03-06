package cn.wegfan.relicsmanagement.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class RelicCheckVo {

    /**
     * 盘点编号
     */
    private Integer id;

    /**
     * 盘点仓库id
     */
    private Integer warehouseId;

    /**
     * 盘点仓库名
     */
    private String warehouseName;

    /**
     * 盘点文物个数
     */
    private Integer checkCount;

    /**
     * 盘点异常个数（移动文物个数 + 未盘点文物个数）
     */
    private Integer abnormalCount;

    /**
     * 盘点开始时间
     */
    private Date startTime;

    /**
     * 盘点结束时间
     */
    private Date endTime;

}