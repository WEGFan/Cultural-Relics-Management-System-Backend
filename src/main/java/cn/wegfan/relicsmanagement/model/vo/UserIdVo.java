package cn.wegfan.relicsmanagement.model.vo;

import lombok.Data;

@Data
public class UserIdVo {

    /**
     * 用户编号
     */
    private Integer id;

    public UserIdVo(Integer id) {
        this.id = id;
    }

}
