package cn.wegfan.relicsmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
    private String name;

    /**
     * 数量
     */
    @Nullable
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
    private String year;

    /**
     * 年号
     */
    @Nullable
    private String reign;

    /**
     * 器型
     */
    @Nullable
    private String type;

    /**
     * 来源
     */
    @Nullable
    private String source;

    /**
     * 尺寸
     */
    @Nullable
    private String size;

    /**
     * 重量 kg
     */
    @Nullable
    private Double weight;

    /**
     * 收储仓库id
     */
    @Nullable
    private Integer warehouseId;

    /**
     * 货架id
     */
    @Nullable
    private Integer shelfId;

    /**
     * 入馆价值
     */
    @Nullable
    private BigDecimal enterPrice;

    /**
     * 离馆价值
     */
    @Nullable
    private BigDecimal leavePrice;

    /**
     * 状态id
     */
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
    private String comment1;

    /**
     * 备注2
     */
    @Nullable
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

}