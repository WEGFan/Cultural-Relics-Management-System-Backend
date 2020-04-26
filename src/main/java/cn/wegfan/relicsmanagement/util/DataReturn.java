package cn.wegfan.relicsmanagement.util;

import cn.wegfan.relicsmanagement.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DataReturn {

    private Object data;
    private Integer code;
    private String msg;

    public DataReturn(Object data, Integer code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public static DataReturn success(Object data) {
        return new DataReturn(data, 200, "success");
    }

}
