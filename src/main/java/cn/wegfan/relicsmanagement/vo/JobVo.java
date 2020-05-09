package cn.wegfan.relicsmanagement.vo;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class JobVo {

    /**
     * 职务编号
     */
    private Integer id;

    /**
     * 职务名称
     */
    private String name;

    /**
     * 职务权限id列表
     */
    private Set<Integer> permissionsId;

}
