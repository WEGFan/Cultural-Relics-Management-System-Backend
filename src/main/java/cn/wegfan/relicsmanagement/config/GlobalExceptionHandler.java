package cn.wegfan.relicsmanagement.config;

import cn.wegfan.relicsmanagement.util.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.util.BusinessException;
import cn.wegfan.relicsmanagement.vo.DataReturnVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.StringJoiner;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    public DataReturnVo handleUnauthorizedException(Exception e) {
        // throw new BusinessException(BusinessErrorEnum.Unauthorized);
        log.warn("", e);
        return DataReturnVo.businessError(new BusinessException(BusinessErrorEnum.Unauthorized));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public DataReturnVo handleInvalidArgumentException(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        StringJoiner stringJoiner = new StringJoiner("；");
        errors.forEach(error -> stringJoiner.add(error.getDefaultMessage()));
        return DataReturnVo.error(400, stringJoiner.toString());
    }

    @ExceptionHandler(BusinessException.class)
    public DataReturnVo handleBusinessException(BusinessException e) {
        log.warn("", e);
        return DataReturnVo.businessError(e);
    }

    @ExceptionHandler(Exception.class)
    public DataReturnVo handleException(Exception e) {
        log.error("", e);
        // TODO: 删除返回错误信息
        return DataReturnVo.error(500, e.toString());
    }

}
