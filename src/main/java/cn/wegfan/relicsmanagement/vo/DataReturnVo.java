package cn.wegfan.relicsmanagement.vo;

import cn.wegfan.relicsmanagement.util.BusinessException;
import lombok.Getter;
import lombok.Setter;

import java.util.StringJoiner;

@Getter
@Setter
public class DataReturnVo {

    private Object data;

    private Integer code;

    private String msg;

    public DataReturnVo(Object data, Integer code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public static DataReturnVo success(Object data) {
        return new DataReturnVo(data, 200, "success");
    }

    public static DataReturnVo error(Integer code, String msg) {
        return new DataReturnVo(null, code, msg);
    }

    public static DataReturnVo error(BusinessException exception) {
        return new DataReturnVo(null, exception.getCode(), exception.getMessage());
    }

    // @Override
    // public String toString() {
    //     return new StringJoiner(", ", DataReturnVo.class.getSimpleName() + "[", "]")
    //             .add("data=" + data)
    //             .add("code=" + code)
    //             .add("msg='" + msg + "'")
    //             .toString();
    // }

}
