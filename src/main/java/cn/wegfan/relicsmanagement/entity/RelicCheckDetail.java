package cn.wegfan.relicsmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Date;

@Data
public class RelicCheckDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 盘点编号
     */
    private Integer checkId;

    /**
     * 文物编号
     */
    private Integer relicId;

    /**
     * 盘点前文物所在仓库编号
     */
    private Integer oldWarehouseId;

    /**
     * 盘点前文物所在货架编号
     */
    private Integer oldShelfId;

    /**
     * 盘点后文物所在仓库编号
     */
    @Nullable
    private Integer newWarehouseId;

    /**
     * 盘点后文物所在货架编号
     */
    @Nullable
    private Integer newShelfId;

    /**
     * 盘点人id
     */
    private Integer operatorId;

    /**
     * 盘点人实体
     */
    @TableField(exist = false)
    private User operator;

    /**
     * 盘点时间
     */
    @Nullable
    private Date checkTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 盘点文物实体
     */
    @TableField(exist = false)
    private Relic relic;

    /**
     * 盘点前仓库实体
     */
    @TableField(exist = false)
    private Warehouse oldWarehouse;

    /**
     * 盘点前货架实体
     */
    @TableField(exist = false)
    private Shelf oldShelf;

    /**
     * 盘点后仓库实体
     */
    @TableField(exist = false)
    private Warehouse newWarehouse;

    /**
     * 盘点后货架实体
     */
    @TableField(exist = false)
    private Shelf newShelf;

}