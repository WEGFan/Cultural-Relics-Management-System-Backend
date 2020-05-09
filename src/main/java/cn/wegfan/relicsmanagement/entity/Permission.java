package cn.wegfan.relicsmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 权限代码
     */
    private String code;

    /**
     * 权限名称
     */
    private String name;

    // @Override
    // public String toString() {
    //     return new StringJoiner(", ", Permission.class.getSimpleName() + "[", "]")
    //             .add("id=" + id)
    //             .add("name='" + name + "'")
    //             .toString();
    // }

}
