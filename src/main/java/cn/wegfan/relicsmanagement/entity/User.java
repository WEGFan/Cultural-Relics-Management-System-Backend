package cn.wegfan.relicsmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号     primary key
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 工号
     */
    private Integer workId;

    /**
     * 姓名
     */
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
    private Integer jobId;

    /**
     * 手机号
     */
    private String telephone;

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

    /**
     * 权限列表
     */
    @TableField(exist = false)
    private List<Permission> permissions;
    
    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("workId=" + workId)
                .add("name='" + name + "'")
                .add("password='" + password + "'")
                .add("salt='" + salt + "'")
                .add("jobId=" + jobId)
                .add("telephone='" + telephone + "'")
                .add("createTime=" + createTime)
                .add("updateTime=" + updateTime)
                .add("deleteTime=" + deleteTime)
                .add("permissions=" + permissions)
                .toString();
    }

}
