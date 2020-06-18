package cn.wegfan.relicsmanagement.model.vo;

import lombok.Data;

@Data
public class CheckIdVo {

    /**
     * 盘点编号
     */
    private Integer id;

    public CheckIdVo(Integer id) {
        this.id = id;
    }

}
