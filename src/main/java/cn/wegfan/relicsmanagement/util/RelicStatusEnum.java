package cn.wegfan.relicsmanagement.util;

import lombok.Getter;

@Getter
public enum RelicStatusEnum {
    /**
     * 待评估
     */
    ToBeAssessed(1),
    /**
     * 在馆
     */
    InMuseum(2),
    /**
     * 外借
     */
    Lend(3),
    /**
     * 修理
     */
    Fix(4),
    /**
     * 离馆
     */
    LeaveMuseum(5);

    private Integer statusId;

    RelicStatusEnum(Integer statusId) {
        this.statusId = statusId;
    }

}
