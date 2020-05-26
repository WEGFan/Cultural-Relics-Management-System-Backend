package cn.wegfan.relicsmanagement.dto.stringdto;

import cn.wegfan.relicsmanagement.config.validator.EmptyOrMin;
import cn.wegfan.relicsmanagement.config.validator.EmptyOrRange;
import cn.wegfan.relicsmanagement.config.validator.NumberString;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@Data

public class RelicInfoStringDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    private String name;

    /**
     * 数量
     */
    @NumberString(message = "数量不合法")
    @EmptyOrRange(min = 0, max = 999_999_999, message = "数量超出范围")
    private String quantity;

    /**
     * 年代
     */
    private String year;

    /**
     * 年号
     */
    private String reign;

    /**
     * 器型
     */
    private String type;

    /**
     * 来源
     */
    private String source;

    /**
     * 尺寸
     */
    private String size;

    /**
     * 重量 kg
     */
    @NumberString(allowDecimal = true, message = "重量不合法")
    @EmptyOrMin(value = 0, message = "重量不能为负数")
    private String weight;

    /**
     * 收储仓库id
     */
    @NumberString(message = "仓库 ID 不合法")
    @EmptyOrRange(min = 1, max = 999_999_999, message = "仓库 ID 需为 9 位以内数字")
    private String warehouseId;

    /**
     * 货架id
     */
    @NumberString(message = "货架 ID 不合法")
    @EmptyOrRange(min = 1, max = 999_999_999, message = "货架 ID 需为 9 位以内数字")
    private String shelfId;

    /**
     * 入馆价值
     */
    @NumberString(allowDecimal = true, message = "入馆价值不合法")
    @EmptyOrMin(value = 0, message = "入馆价值不能为负数")
    private String enterPrice;

    /**
     * 离馆价值
     */
    @NumberString(allowDecimal = true, message = "离馆价值不合法")
    @EmptyOrMin(value = 0, message = "离馆价值不能为负数")
    private String leavePrice;

    /**
     * 状态id
     */
    @NumberString(message = "状态不合法")
    @EmptyOrRange(min = 1, max = 5, message = "状态不存在")
    private String statusId;

    /**
     * 备注1
     */
    private String comment1;

    /**
     * 备注2
     */
    private String comment2;

}
