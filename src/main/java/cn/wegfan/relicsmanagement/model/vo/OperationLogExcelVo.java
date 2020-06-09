package cn.wegfan.relicsmanagement.model.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ExcelIgnoreUnannotated
public class OperationLogExcelVo {

    /**
     * 操作编号
     */
    @ExcelProperty("操作编号")
    private Integer id;

    /**
     * 操作人
     */
    @ExcelProperty("操作人")
    private String operatorName;

    /**
     * 操作对象类型
     */
    @ExcelProperty("操作对象类型")
    private String itemType;

    /**
     * 操作对象编号
     */
    @ExcelProperty("操作对象编号")
    private Integer itemId;

    /**
     * 操作类型
     */
    @ExcelProperty("操作类型")
    private String type;

    /**
     * 详细信息
     */
    @ExcelProperty("详细信息")
    private String detail;

    /**
     * 操作时间
     */
    @ExcelProperty("操作时间")
    private Date createTime;

}
