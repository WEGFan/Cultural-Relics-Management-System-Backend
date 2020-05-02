package cn.wegfan.relicsmanagement.vo;

import lombok.Data;

@Data
public class WarehouseVo {

    /**
     * 仓库编号
     */
    private Integer id;

    /**
     * 仓库名
     */
    private String name;

    // @Override
    // public String toString() {
    //     return new StringJoiner(", ", WarehouseVo.class.getSimpleName() + "[", "]")
    //             .add("id=" + id)
    //             .add("name='" + name + "'")
    //             .toString();
    // }

}
