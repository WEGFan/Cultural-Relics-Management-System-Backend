package cn.wegfan.relicsmanagement.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工号
     */
    private Integer workId;

    /**
     * 密码
     */
    private String password;

}

