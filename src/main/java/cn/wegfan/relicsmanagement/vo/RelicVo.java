package cn.wegfan.relicsmanagement.vo;

import cn.wegfan.relicsmanagement.util.PermissionCodeEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Data
public class RelicVo {

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
     * 入馆价值【资产科】
     */
    private BigDecimal enterPrice;

    /**
     * 离馆价值【资产科】
     */
    private BigDecimal leavePrice;

    /**
     * 状态id
     */
    private Integer statusId;

    /**
     * 最后盘点时间【仓库管理员】
     */
    private Date lastCheckTime;

    /**
     * 入馆时间【仓库管理员】
     */
    private Date enterTime;

    /**
     * 离馆时间【仓库管理员】
     */
    private Date leaveTime;

    /**
     * 移入仓库时间【仓库管理员】
     */
    private Date moveTime;

    /**
     * 出借时间【仓库管理员】
     */
    private Date lendTime;

    /**
     * 送修时间【仓库管理员】
     */
    private Date fixTime;

    /**
     * 备注
     */
    private String comment;

    /**
     * 录入时间
     */
    private Date updateTime;

    /**
     * 根据权限代码清除字段
     *
     * @param permissionCodeSet 权限代码集合
     */
    public void clearFieldsByPermissionCode(Set<String> permissionCodeSet) {
        if (!permissionCodeSet.contains(PermissionCodeEnum.VIEW_EDIT_RELIC_PRICE)) {
            enterPrice = null;
            leavePrice = null;
        }
        if (!permissionCodeSet.contains(PermissionCodeEnum.WAREHOUSE)) {
            lastCheckTime = null;
            enterTime = null;
            leaveTime = null;
            moveTime = null;
            lendTime = null;
            fixTime = null;
        }
    }

}
