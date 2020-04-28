package cn.wegfan.relicsmanagement.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限编号
     */
    private Integer id;

    /**
     * 权限名称
     */
    private String name;

}
