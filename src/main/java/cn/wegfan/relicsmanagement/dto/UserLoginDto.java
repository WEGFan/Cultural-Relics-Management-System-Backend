package cn.wegfan.relicsmanagement.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class UserLoginDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工号
     */
    @Pattern(regexp = "^\\d{1,10}$", message="工号需为 10 位以内数字")
    private String workId;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

}

