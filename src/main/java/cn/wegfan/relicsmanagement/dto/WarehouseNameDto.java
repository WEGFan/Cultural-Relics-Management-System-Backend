package cn.wegfan.relicsmanagement.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class WarehouseNameDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 仓库名称
     */
    @NotBlank(message = "仓库名不能为空")
    private String name;

}