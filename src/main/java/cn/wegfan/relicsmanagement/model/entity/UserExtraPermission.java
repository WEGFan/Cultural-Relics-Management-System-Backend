package cn.wegfan.relicsmanagement.model.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserExtraPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private Integer userId;

    /**
     * 权限编号
     */
    private Integer permissionId;

    public UserExtraPermission(Integer userId, Integer permissionId) {
        this.userId = userId;
        this.permissionId = permissionId;
    }

}
