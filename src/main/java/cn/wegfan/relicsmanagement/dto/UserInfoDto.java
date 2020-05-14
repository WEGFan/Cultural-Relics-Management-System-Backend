package cn.wegfan.relicsmanagement.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Data
public class UserInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工号
     */
    @Pattern(regexp = "^\\d{1,10}$", message = "工号需为 10 位以内数字")
    private String workId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 密码
     */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 8, max = 24, message = "密码长度需在 8 到 24 个字符之间")
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
