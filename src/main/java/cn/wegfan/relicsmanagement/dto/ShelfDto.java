package cn.wegfan.relicsmanagement.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class ShelfDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 货架名称
     */
    @NotBlank(message = "货架名不能为空")
    private String name;

    /**
     * 所属仓库编号
     */
    private Integer warehouseId;

}