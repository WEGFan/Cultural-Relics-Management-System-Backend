package cn.wegfan.relicsmanagement.vo;

import lombok.Getter;
import lombok.Setter;

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

}
