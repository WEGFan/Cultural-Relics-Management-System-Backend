package cn.wegfan.relicsmanagement.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class UserInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

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
