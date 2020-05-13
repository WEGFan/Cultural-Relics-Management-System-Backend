package cn.wegfan.relicsmanagement.util;

import lombok.Getter;

@Getter
public enum BusinessErrorEnum {

    Unauthorized(401, "没有权限"),
    UserNotLogin(403, "用户未登录"),
    WrongAccountOrPassword(10000, "工号或密码错误"),
    DuplicateWorkId(10001, "工号重复"),
    WarehouseNotEmpty(10002, "仓库内还有文物"),
    DuplicateWarehouseName(10003, "仓库名重复"),
    UserNotExists(10004, "用户不存在"),
    WarehouseNotExists(10005, "仓库不存在"),
    RelicNotExists(10006, "文物不存在"),
    FileNotJpgOrPng(10007, "图片格式不是jpg/png"),
    FileNotFound(10008, "找不到文件");

    private final Integer errorCode;

    private final String errorMessage;

    BusinessErrorEnum(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
