package cn.wegfan.relicsmanagement.model.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.converters.string.StringImageConverter;
import lombok.Data;

import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@ContentRowHeight(117)
public class RelicCheckDetailExcelVo {

    /**
     * 文物编号
     */
    @ExcelProperty("文物编号")
    @ColumnWidth(11)
    private Integer relicId;

    /**
     * 文物名称
     */
    @ExcelProperty("文物名称")
    @ColumnWidth(13)
    private String name;

    /**
     * 照片
     */
    @ExcelProperty(value = "照片", converter = StringImageConverter.class)
    @ColumnWidth(19)
    private String picturePath;

    /**
     * 盘点状态
     */
    @ExcelProperty("盘点状态")
    @ColumnWidth(12)
    private String status;

    /**
     * 盘点前仓库id
     */
    private Integer oldWarehouseId;

    /**
     * 盘点前仓库名
     */
    @ExcelProperty("盘点前仓库名")
    @ColumnWidth(17)
    private String oldWarehouseName;

    /**
     * 盘点前货架id
     */
    private Integer oldShelfId;

    /**
     * 盘点前货架名
     */
    @ExcelProperty("盘点前货架名")
    @ColumnWidth(17)
    private String oldShelfName;

    /**
     * 盘点后仓库id
     */
    private Integer newWarehouseId;

    /**
     * 盘点后仓库名
     */
    @ExcelProperty("盘点后仓库名")
    @ColumnWidth(17)
    private String newWarehouseName;

    /**
     * 盘点后货架id
     */
    private Integer newShelfId;

    /**
     * 盘点后货架名
     */
    @ExcelProperty("盘点后货架名")
    @ColumnWidth(17)
    private String newShelfName;

    /**
     * 盘点人姓名
     */
    @ExcelProperty("盘点人姓名")
    @ColumnWidth(15)
    private String operatorName;

    /**
     * 盘点时间
     */
    @ExcelProperty("盘点时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(19)
    private Date checkTime;

}