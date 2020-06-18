package cn.wegfan.relicsmanagement.model.vo;

import lombok.Data;

@Data
public class SuccessVo {

    /**
     * 是否成功
     */
    private Boolean success;

    public SuccessVo(Boolean success) {
        this.success = success;
    }

}
