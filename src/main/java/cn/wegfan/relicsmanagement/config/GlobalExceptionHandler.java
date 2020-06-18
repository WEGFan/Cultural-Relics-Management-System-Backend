package cn.wegfan.relicsmanagement.config;

import cn.wegfan.relicsmanagement.constant.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.model.vo.DataReturnVo;
import cn.wegfan.relicsmanagement.util.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.Serializable;
import java.util.*;

/**
 * 全局异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 当前生效的项目配置名称
     */
    @Value("${spring.profiles.active}")
    private String activeProfile;

    /**
     * 处理上传文件大小超过限制
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public DataReturnVo handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.warn("{}", e.getMessage());
        return DataReturnVo.businessError(new BusinessException(BusinessErrorEnum.UploadFileTooLarge));
    }

    /**
     * 处理没有权限的请求
     */
    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    public DataReturnVo handleUnauthorizedException(Exception e) {
        log.warn("{}", e.getMessage());
        return DataReturnVo.businessError(new BusinessException(BusinessErrorEnum.Unauthorized));
    }

    /**
     * 处理请求参数校验失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public DataReturnVo handleInvalidArgumentException(MethodArgumentNotValidException e) throws JsonProcessingException {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        Map<String, BindingErrorDetail> errorLog = new HashMap<>();
        for (ObjectError error : errors) {
            FieldError fieldError = (FieldError)error;
            BindingErrorDetail errorDetail = errorLog.compute(fieldError.getField(),
                    (k, v) -> (v == null ? new BindingErrorDetail() : v));
            errorDetail.setValue(String.valueOf(fieldError.getRejectedValue()));
            errorDetail.getMsg().add(error.getDefaultMessage());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String errorLogJsonString = objectMapper.writeValueAsString(errorLog);
        log.warn("参数校验失败：{}", errorLogJsonString);

        StringJoiner stringJoiner = new StringJoiner("；");
        errors.forEach(error -> stringJoiner.add(error.getDefaultMessage()));
        return DataReturnVo.error(400, stringJoiner.toString());
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public DataReturnVo handleBusinessException(BusinessException e) {
        log.warn("{}", e.getMessage());
        return DataReturnVo.businessError(e);
    }

    /**
     * 处理 GET 参数校验失败
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public DataReturnVo handleNumberFormatException(MethodArgumentTypeMismatchException e) {
        log.warn("{}", e.getMessage());
        return DataReturnVo.error(400, "搜索条件不合法");
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public DataReturnVo handleException(Exception e) {
        log.error("", e);
        // 如果是生产环境则不显示错误信息
        if ("prod".equals(activeProfile)) {
            return DataReturnVo.error(500, "内部服务器错误，请联系管理员");
        }
        return DataReturnVo.error(500, String.format("内部服务器错误，错误信息：%s", e.toString()));
    }

    /**
     * 参数校验失败信息对象
     */
    @Data
    private static class BindingErrorDetail implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 参数名称
         */
        private String value;

        /**
         * 提示信息列表
         */
        private List<String> msg = new ArrayList<>();

    }

}
