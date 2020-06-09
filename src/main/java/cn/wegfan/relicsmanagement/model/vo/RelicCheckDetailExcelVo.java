package cn.wegfan.relicsmanagement.model.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

@Data
@ExcelIgnoreUnannotated
public class RelicCheckDetailExcelVo {

    /**
     * 文物编号
     */
    @ExcelProperty("文物编号")
    private Integer relicId;

    /**
     * 文物名称
     */
    @ExcelProperty("文物名称")
    private String name;

    /**
     * 盘点状态
     */
    @ExcelProperty("盘点状态")
    private String status;

    /**
     * 盘点前仓库id
     */
    private Integer oldWarehouseId;

    /**
     * 盘点前仓库名
     */
    @ExcelProperty("盘点前仓库名")
    private String oldWarehouseName;

    /**
     * 盘点前货架id
     */
    private Integer oldShelfId;

    /**
     * 盘点前货架名
     */
    @ExcelProperty("盘点前货架名")
    private String oldShelfName;

    /**
     * 盘点后仓库id
     */
    private Integer newWarehouseId;

    /**
     * 盘点后仓库名
     */
    @ExcelProperty("盘点后仓库名")
    private String newWarehouseName;

    /**
     * 盘点后货架id
     */
    private Integer newShelfId;

    /**
     * 盘点后货架名
     */
    @ExcelProperty("盘点后货架名")
    private String newShelfName;

    /**
     * 盘点人姓名
     */
    @ExcelProperty("盘点人姓名")
    private String operatorName;

    /**
     * 盘点时间
     */
    @ExcelProperty("盘点时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date checkTime;

}