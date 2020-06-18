package cn.wegfan.relicsmanagement.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 前端传的用户修改密码对象
 */
@Data
public class UserChangePasswordDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 旧密码
     */
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 8, max = 24, message = "密码长度需在 8 到 24 个字符之间")
    private String newPassword;

}