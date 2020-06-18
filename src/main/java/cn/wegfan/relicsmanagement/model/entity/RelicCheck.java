package cn.wegfan.relicsmanagement.model.entity;

import cn.wegfan.relicsmanagement.util.OperationLogProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Date;

/**
 * 盘点
 */
@Data
public class RelicCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 盘点编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 盘点仓库id
     */
    @OperationLogProperty(name = "盘点仓库")
    private Integer warehouseId;

    /**
     * 盘点开始时间
     */
    private Date startTime;

    /**
     * 盘点结束时间
     */
    @Nullable
    private Date endTime;

    /**
     * 盘点文物个数
     */
    @TableField(exist = false)
    private Integer checkCount;

    /**
     * 盘点异常个数（移动文物个数 + 未盘点文物个数）
     */
    @TableField(exist = false)
    private Integer abnormalCount;

    /**
     * 仓库实体
     */
    @TableField(exist = false)
    private Warehouse warehouse;

}