package cn.wegfan.relicsmanagement.entity;

import cn.wegfan.relicsmanagement.util.OperationLogProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Date;

@Data
public class Shelf implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 货架编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 货架名
     */
    @OperationLogProperty(name = "货架名")
    private String name;

    /**
     * 所属仓库编号
     */
    @OperationLogProperty(name = "所属仓库")
    private Integer warehouseId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    @Nullable
    private Date updateTime;

    /**
     * 删除时间
     */
    @Nullable
    private Date deleteTime;

    /**
     * 所属仓库实体
     */
    @TableField(exist = false)
    private Warehouse warehouse;

}
