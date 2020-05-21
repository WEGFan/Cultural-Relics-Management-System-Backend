package cn.wegfan.relicsmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Date;

@Data
public class RelicCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 盘点编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

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
     * 盘点仓库id
     */
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

}