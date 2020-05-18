package cn.wegfan.relicsmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RelicStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文物状态编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 状态名称
     */
    private String name;

}
