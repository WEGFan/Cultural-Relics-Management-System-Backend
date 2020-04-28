package cn.wegfan.relicsmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sun.org.apache.xml.internal.serializer.Serializer;
import com.sun.org.apache.xml.internal.serializer.utils.SerializerMessages_zh_CN;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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

    public Warehouse() {
    }

}
