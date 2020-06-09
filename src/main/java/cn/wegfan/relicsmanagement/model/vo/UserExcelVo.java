package cn.wegfan.relicsmanagement.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UserExcelVo {

    /**
     * 用户编号
     */
    @ExcelProperty("用户编号")
    private Integer id;

    /**
     * 工号
     */
    @ExcelProperty("工号")
    private Integer workId;

    /**
     * 姓名
     */
    @ExcelProperty("姓名")
    private String name;

    /**
     * 职务
     */
    @ExcelProperty("职务")
    private String job;

    /**
     * 手机号
     */
    @ExcelProperty("手机号")
    private String telephone;

    /**
     * 额外权限
     */
    @ExcelProperty("额外权限")
    private String extraPermissions;

    /**
     * 入职时间
     */
    @ExcelProperty("入职时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 离职时间
     */
    @ExcelProperty("离职时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date deleteTime;

}