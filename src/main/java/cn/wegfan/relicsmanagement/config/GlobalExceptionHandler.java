package cn.wegfan.relicsmanagement.config;

import cn.wegfan.relicsmanagement.util.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.util.BusinessException;
import cn.wegfan.relicsmanagement.vo.DataReturnVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    public DataReturnVo UnauthorizedException(Exception e) {
        // throw new BusinessException(BusinessErrorEnum.Unauthorized);
        log.warn("", e);
        return DataReturnVo.businessError(new BusinessException(BusinessErrorEnum.Unauthorized));
    }

    @ExceptionHandler(BusinessException.class)
    public DataReturnVo handleBusinessException(BusinessException e) {
        log.warn("", e);
        return DataReturnVo.businessError(e);
    }

    @ExceptionHandler(Exception.class)
    public DataReturnVo handleException(Exception e) {
        log.error("", e);
        return DataReturnVo.error(500, e.getMessage());
    }

}
