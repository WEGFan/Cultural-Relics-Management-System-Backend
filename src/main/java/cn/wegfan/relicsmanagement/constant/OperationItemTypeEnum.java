package cn.wegfan.relicsmanagement.constant;

import lombok.Getter;

/**
 * 操作对象类型枚举
 */
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

    public static OperationItemTypeEnum getFromCode(String code) {
        for (OperationItemTypeEnum element : values()) {
            if (code.equals(element.code)) {
                return element;
            }
        }
        throw new IllegalArgumentException("No enum constant which code is " + code);
    }

}
