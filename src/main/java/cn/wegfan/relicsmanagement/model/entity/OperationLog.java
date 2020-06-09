package cn.wegfan.relicsmanagement.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 操作人id
     */
    private Integer operatorId;

    /**
     * 被操作物类型 relic/user/warehouse/shelf/check
     */
    private String itemType;

    /**
     * 被操作物id
     */
    private Integer itemId;

    /**
     * 操作类型
     */
    private String type;

    /**
     * 详细信息
     */
    private String detail;

    /**
     * 操作时间
     */
    private Date createTime;

    /**
     * 操作人实体
     */
    @TableField(exist = false)
    private User operator;

}