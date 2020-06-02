package cn.wegfan.relicsmanagement.entity;

import cn.wegfan.relicsmanagement.util.OperationLogProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Date;

@Data
public class Warehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 仓库编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 仓库名
     */
    @OperationLogProperty(name = "仓库名")
    private String name;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    @Nullable
    private Date updateTime;

    /**
     * 删除时间
     */
    @Nullable
    private Date deleteTime;

}
