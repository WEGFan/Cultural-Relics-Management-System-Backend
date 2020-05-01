package cn.wegfan.relicsmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.StringJoiner;

@Data
public class Warehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 仓库编号     primary key
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 仓库名
     */
    private String name;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除时间
     */
    private Date deleteTime;

    // @Override
    // public String toString() {
    //     return new StringJoiner(", ", Warehouse.class.getSimpleName() + "[", "]")
    //             .add("id=" + id)
    //             .add("name='" + name + "'")
    //             .add("createTime=" + createTime)
    //             .add("updateTime=" + updateTime)
    //             .add("deleteTime=" + deleteTime)
    //             .toString();
    // }

}
