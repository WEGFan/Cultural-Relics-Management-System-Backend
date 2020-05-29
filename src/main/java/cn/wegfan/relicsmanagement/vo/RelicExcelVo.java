package cn.wegfan.relicsmanagement.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RelicExcelVo {

    /**
     * 文物编号
     */
    @ExcelProperty("文物编号")
    private Integer id;

    /**
     * 名称
     */
    @ExcelProperty("名称")
    private String name;

    /**
     * 数量
     */
    @ExcelProperty("数量")
    private Integer quantity;

    // /**
    //  * 照片地址
    //  */
    // @ExcelProperty("")
    // private String picturePath;

    /**
     * 年代
     */
    @ExcelProperty("年代")
    private String year;

    /**
     * 年号
     */
    @ExcelProperty("年号")
    private String reign;

    /**
     * 器型
     */
    @ExcelProperty("器型")
    private String type;

    /**
     * 来源
     */
    @ExcelProperty("来源")
    private String source;

    /**
     * 尺寸
     */
    @ExcelProperty("尺寸")
    private String size;

    /**
     * 重量 kg
     */
    @ExcelProperty("重量")
    private Double weight;

    /**
     * 仓库名称
     */
    @ExcelProperty("仓库名称")
    private String warehouseName;

    /**
     * 货架名称
     */
    @ExcelProperty("货架名称")
    private String shelfName;

    /**
     * 入馆价值【资产科】
     */
    @ExcelProperty("入馆价值")
    private BigDecimal enterPrice;

    /**
     * 离馆价值【资产科】
     */
    @ExcelProperty("离馆价值")
    private BigDecimal leavePrice;

    /**
     * 状态
     */
    @ExcelProperty("状态")
    private String status;

    /**
     * 最后盘点时间【仓库管理员】
     */
    @ExcelProperty("最后盘点时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date lastCheckTime;

    /**
     * 入馆时间【仓库管理员】
     */
    @ExcelProperty("入馆时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date enterTime;

    /**
     * 离馆时间【仓库管理员】
     */
    @ExcelProperty("离馆时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date leaveTime;

    /**
     * 移入仓库时间【仓库管理员】
     */
    @ExcelProperty("移入仓库时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date moveTime;

    /**
     * 出借时间【仓库管理员】
     */
    @ExcelProperty("出借时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date lendTime;

    /**
     * 送修时间【仓库管理员】
     */
    @ExcelProperty("送修时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date fixTime;

    /**
     * 备注1
     */
    @ExcelProperty("备注1")
    private String comment1;

    /**
     * 备注2
     */
    @ExcelProperty("备注2")
    private String comment2;

    /**
     * 录入时间
     */
    @ExcelProperty("录入时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
