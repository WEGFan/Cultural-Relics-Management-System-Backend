package cn.wegfan.relicsmanagement.dto.stringdto;

import cn.wegfan.relicsmanagement.config.validator.NumberString;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class UserLoginStringDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工号
     */
    @NumberString(message = "工号不合法")
    @Range(min = 0, max = 999_999_999, message = "工号需为 9 位以内数字")
    private String workId;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

}

