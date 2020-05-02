package cn.wegfan.relicsmanagement.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    public BusinessException(BusinessErrorEnum errorEnum) {
        code = errorEnum.getErrorCode();
        message = errorEnum.getErrorMessage();
    }

}