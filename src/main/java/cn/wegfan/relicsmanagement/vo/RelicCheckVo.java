package cn.wegfan.relicsmanagement.vo;

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
     * 盘点文物个数
     */
    private Integer checkCount;

    /**
     * 未盘点文物个数（盘点异常）
     */
    private Integer notCheckCount;

    /**
     * 盘点开始时间
     */
    private Date startTime;

    /**
     * 盘点结束时间
     */
    private Date endTime;

}