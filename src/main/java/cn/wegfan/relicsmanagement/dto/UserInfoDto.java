package cn.wegfan.relicsmanagement.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Set;

@Data
public class UserInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工号
     */
    @NotNull(message = "工号不能为 null")
    @Pattern(regexp = "^\\d{1,10}$", message = "工号需为 10 位以内数字")
    private String workId;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String name;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为 null")
    @Pattern(regexp = "^(.{8,24}|\\s{0})$", message = "密码长度需在 8 到 24 个字符之间")
    private String password;

    /**
     * 职务
     */
    @NotNull(message = "职务不能为 null")
    private Integer jobId;

    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为 null")
    @Pattern(regexp = "^1[3456789]\\d{9}$", message = "手机号格式不正确")
    private String telephone;

    /**
     * 权限id列表
     */
    @NotNull(message = "额外权限不能为 null")
    private Set<Integer> extraPermissionsId;

}
