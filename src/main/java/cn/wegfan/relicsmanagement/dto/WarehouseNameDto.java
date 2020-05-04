package cn.wegfan.relicsmanagement.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class WarehouseNameDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 仓库名称
     */
    private String name;

}