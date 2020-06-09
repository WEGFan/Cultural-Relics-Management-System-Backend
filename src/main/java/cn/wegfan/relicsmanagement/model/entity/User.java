package cn.wegfan.relicsmanagement.model.entity;

import cn.wegfan.relicsmanagement.util.OperationLogProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 工号
     */
    @OperationLogProperty(name = "工号")
    private Integer workId;

    /**
     * 姓名
     */
    @OperationLogProperty(name = "姓名")
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码 盐
     */
    private String salt;

    /**
     * 职务
     */
    @OperationLogProperty(name = "职务")
    private Integer jobId;

    /**
     * 手机号
     */
    @OperationLogProperty(name = "手机号")
    private String telephone;

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
     * 额外权限列表
     */
    @TableField(exist = false)
    @OperationLogProperty(name = "额外权限")
    private Set<Permission> extraPermissions;

}
