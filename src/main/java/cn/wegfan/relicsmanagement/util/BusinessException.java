package cn.wegfan.relicsmanagement.util;

import cn.wegfan.relicsmanagement.constant.BusinessErrorEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 业务异常
 */
@Getter
@Setter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误代码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String message;

    public BusinessException(BusinessErrorEnum errorEnum) {
        code = errorEnum.getErrorCode();
        message = errorEnum.getErrorMessage();
    }

    public BusinessException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}