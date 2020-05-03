package cn.wegfan.relicsmanagement.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserChangePasswordDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;

}