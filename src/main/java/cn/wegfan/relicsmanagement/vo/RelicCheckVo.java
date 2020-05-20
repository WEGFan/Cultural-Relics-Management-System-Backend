package cn.wegfan.relicsmanagement.vo;

import cn.wegfan.relicsmanagement.entity.User;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Date;

@Data
public class RelicCheckVo {

    /**
     * 盘点编号
     */
    private Integer id;

    /**
     * 盘点仓库id
     */
    private Integer warehouseId;

    /**
     * 盘点人姓名
     */
    private String operatorName;

    /**
     * 盘点文物个数
     */
    private Integer checkCount;

    /**
     * 未盘点文物个数（盘点异常）
     */
    private Integer notCheckCount;

    /**
     * 盘点开始时间
     */
    private Date startTime;

    /**
     * 盘点结束时间
     */
    private Date endTime;

}