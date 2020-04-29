package cn.wegfan.relicsmanagement.util;

import lombok.Getter;

@Getter
public enum BusinessErrorEnum {

    WrongAccountOrPassword(10000, "工号或密码错误"),
    DuplicateWorkId(10001, "工号重复");

    private Integer errorCode;
    private String errorMessage;

    BusinessErrorEnum(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
