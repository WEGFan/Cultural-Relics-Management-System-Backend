package cn.wegfan.relicsmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
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

    // @Override
    // public String toString() {
    //     return new StringJoiner(", ", UserPermission.class.getSimpleName() + "[", "]")
    //             .add("userId=" + userId)
    //             .add("permissionId=" + permissionId)
    //             .toString();
    // }

}