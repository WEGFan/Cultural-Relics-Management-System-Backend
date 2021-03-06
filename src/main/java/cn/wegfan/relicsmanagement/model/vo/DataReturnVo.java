package cn.wegfan.relicsmanagement.model.vo;

import cn.wegfan.relicsmanagement.util.BusinessException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * 接口返回对象
 */
@Slf4j
@Getter
@Setter
@ToString
public class DataReturnVo {

    /**
     * 数据
     */
    private Object data;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    public DataReturnVo(Object data, Integer code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
        log.info(String.valueOf(this));
    }

    public static DataReturnVo success(Object data) {
        return new DataReturnVo(data, 200, "success");
    }

    public static DataReturnVo error(Integer code, String msg) {
        return new DataReturnVo(null, code, msg);
    }

    public static DataReturnVo businessError(BusinessException exception) {
        return new DataReturnVo(null, exception.getCode(), exception.getMessage());
    }

}
