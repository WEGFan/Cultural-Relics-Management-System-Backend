package cn.wegfan.relicsmanagement.vo;

import lombok.Data;

import java.util.Date;

@Data
public class OperationLogVo {

    /**
     * 操作编号
     */
    private Integer id;

    /**
     * 操作人
     */
    private String operatorName;

    /**
     * 操作对象类型
     */
    private String itemType;

    /**
     * 操作对象编号
     */
    private Integer itemId;

    /**
     * 操作类型
     */
    private String type;

    /**
     * 详细信息
     */
    private String detail;

    /**
     * 操作时间
     */
    private Date createTime;

}
