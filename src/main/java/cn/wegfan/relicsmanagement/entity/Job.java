package cn.wegfan.relicsmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.StringJoiner;

@Data
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 职务编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 职务名称
     */
    private String name;

    // @Override
    // public String toString() {
    //     return new StringJoiner(", ", Job.class.getSimpleName() + "[", "]")
    //             .add("id=" + id)
    //             .add("name='" + name + "'")
    //             .toString();
    // }

}