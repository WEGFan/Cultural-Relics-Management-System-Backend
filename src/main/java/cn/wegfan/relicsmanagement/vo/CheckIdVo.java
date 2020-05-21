package cn.wegfan.relicsmanagement.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckIdVo {

    /**
     * 盘点编号
     */
    private Integer id;

    // @Override
    // public String toString() {
    //     return new StringJoiner(", ", UserIdVo.class.getSimpleName() + "[", "]")
    //             .add("id=" + id)
    //             .toString();
    // }

}
