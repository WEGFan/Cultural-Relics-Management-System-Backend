package cn.wegfan.relicsmanagement.constant;

import lombok.Getter;

@Getter
public enum OperationItemTypeEnum {
    User("user", "用户"),
    Relic("relic", "文物"),
    Warehouse("warehouse", "仓库"),
    Shelf("shelf", "货架"),
    Check("check", "盘点");

    private String code;

    private String name;

    OperationItemTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
