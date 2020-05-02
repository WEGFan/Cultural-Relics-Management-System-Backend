package cn.wegfan.relicsmanagement.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessVo {

    /**
     * 是否成功
     */
    private Boolean success;

    // @Override
    // public String toString() {
    //     return new StringJoiner(", ", SuccessVo.class.getSimpleName() + "[", "]")
    //             .add("success=" + success)
    //             .toString();
    // }

}
