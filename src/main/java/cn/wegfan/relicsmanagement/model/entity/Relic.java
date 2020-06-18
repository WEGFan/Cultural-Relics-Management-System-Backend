package cn.wegfan.relicsmanagement.model.entity;

import cn.wegfan.relicsmanagement.util.OperationLogProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 文物
 */
@Data
public class Relic implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文物编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    @Nullable
    @OperationLogProperty(name = "文物名")
    private String name;

    /**
     * 数量
     */
    @Nullable
    @OperationLogProperty(name = "数量")
    private Integer quantity;

    /**
     * 照片地址
     */
    @Nullable
    private String picturePath;

    /**
     * 年代
     */
    @Nullable
    @OperationLogProperty(name = "年代")
    private String year;

    /**
     * 年号
     */
    @Nullable
    @OperationLogProperty(name = "年号")
    private String reign;

    /**
     * 器型
     */
    @Nullable
    @OperationLogProperty(name = "器型")
    private String type;

    /**
     * 来源
     */
    @Nullable
    @OperationLogProperty(name = "来源")
    private String source;

    /**
     * 尺寸
     */
    @Nullable
    @OperationLogProperty(name = "尺寸")
    private String size;

    /**
     * 重量 kg
     */
    @Nullable
    @OperationLogProperty(name = "重量")
    private Double weight;

    /**
     * 收储仓库id
     */
    @Nullable
    @OperationLogProperty(name = "收储仓库")
    private Integer warehouseId;

    /**
     * 货架id
     */
    @Nullable
    @OperationLogProperty(name = "所在货架")
    private Integer shelfId;

    /**
     * 入馆价值
     */
    @Nullable
    @OperationLogProperty(name = "入馆价值")
    private BigDecimal enterPrice;

    /**
     * 离馆价值
     */
    @Nullable
    @OperationLogProperty(name = "离馆价值")
    private BigDecimal leavePrice;

    /**
     * 状态id
     */
    @OperationLogProperty(name = "状态")
    private Integer statusId;

    /**
     * 最后盘点时间
     */
    @Nullable
    private Date lastCheckTime;

    /**
     * 入馆时间
     */
    @Nullable
    private Date enterTime;

    /**
     * 离馆时间
     */
    @Nullable
    private Date leaveTime;

    /**
     * 移入仓库时间
     */
    @Nullable
    private Date moveTime;

    /**
     * 出借时间
     */
    @Nullable
    private Date lendTime;

    /**
     * 送修时间
     */
    @Nullable
    private Date fixTime;

    /**
     * 备注1
     */
    @Nullable
    @OperationLogProperty(name = "备注1")
    private String comment1;

    /**
     * 备注2
     */
    @Nullable
    @OperationLogProperty(name = "备注2")
    private String comment2;

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
     * 仓库实体
     */
    @TableField(exist = false)
    private Warehouse warehouse;

    /**
     * 货架实体
     */
    @TableField(exist = false)
    private Shelf shelf;

}