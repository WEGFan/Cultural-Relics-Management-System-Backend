package cn.wegfan.relicsmanagement.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 职务编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 职务名称
     */
    private String name;

    /**
     * 职务默认权限
     */
    private Set<Permission> permissions;

}