package cn.wegfan.relicsmanagement.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class WarehouseIdDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 仓库编号
     */
    @NotNull(message = "仓库编号不能为空")
    private Integer warehouseId;

}