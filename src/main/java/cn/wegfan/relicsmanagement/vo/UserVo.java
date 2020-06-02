package cn.wegfan.relicsmanagement.vo;

import lombok.Data;

import java.util.Set;

@Data
public class UserVo {

    /**
     * 用户编号
     */
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
     * 职务
     */
    private Integer jobId;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 权限id列表
     */
    private Set<Integer> extraPermissionsId;

}
