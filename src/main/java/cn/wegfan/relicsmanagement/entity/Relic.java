package cn.wegfan.relicsmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

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
    private String name;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 照片地址
     */
    private String picturePath;

    /**
     * 年代
     */
    private String year;

    /**
     * 年号
     */
    private String reign;

    /**
     * 器型
     */
    private String type;

    /**
     * 来源
     */
    private String source;

    /**
     * 尺寸
     */
    private String size;

    /**
     * 重量 kg
     */
    private Double weight;

    /**
     * 收储仓库id
     */
    private Integer warehouseId;

    /**
     * 收储地点
     */
    private String place;

    /**
     * 入馆价值
     */
    private BigDecimal enterPrice;

    /**
     * 离馆价值
     */
    private BigDecimal leavePrice;

    /**
     * 状态id
     */
    private Integer statusId;

    /**
     * 最后盘点时间
     */
    private Date lastCheckTime;

    /**
     * 入馆时间
     */
    private Date enterTime;

    /**
     * 离馆时间
     */
    private Date leaveTime;

    /**
     * 移入仓库时间
     */
    private Date moveTime;

    /**
     * 出借时间
     */
    private Date lendTime;

    /**
     * 送修时间
     */
    private Date fixTime;

    /**
     * 备注
     */
    private String comment;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除时间
     */
    private Date deleteTime;

}