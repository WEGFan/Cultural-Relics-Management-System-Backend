package cn.wegfan.relicsmanagement.dto;

import cn.wegfan.relicsmanagement.util.PermissionCodeEnum;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Data

public class RelicInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */

    private String name;

    /**
     * 数量
     */
    private Integer quantity;

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
    private Double weight;

    /**
     * 收储仓库id
     */
    @Nullable
    private Integer warehouseId;

    /**
     * 货架id
     */
    @Nullable
    private Integer shelfId;

    /**
     * 入馆价值
     */
    @Nullable
    private BigDecimal enterPrice;

    /**
     * 离馆价值
     */
    @Nullable
    private BigDecimal leavePrice;

    /**
     * 状态id
     */
    private Integer statusId;

    /**
     * 备注1
     */
    private String comment1;

    /**
     * 备注2
     */
    private String comment2;

    /**
     * 根据权限代码检查并清除字段
     *
     * @param permissionCodeSet 权限代码集合
     *
     * @return 字段是否都合法
     */
    public boolean checkAndClearFieldsByPermissionCode(Set<String> permissionCodeSet) {
        // 要检查的字段名列表
        List<String> checkFields = new ArrayList<>();

        // 根据权限增加字段名
        if (!permissionCodeSet.contains(PermissionCodeEnum.VIEW_EDIT_RELIC_PRICE)) {
            checkFields.addAll(Lists.newArrayList("enterPrice", "leavePrice"));
        }
        if (!permissionCodeSet.contains(PermissionCodeEnum.MOVE_RELIC)) {
            checkFields.addAll(Lists.newArrayList("warehouseId", "shelfId"));
        }
        if (!permissionCodeSet.contains(PermissionCodeEnum.EDIT_RELIC_INFO)) {
            checkFields.addAll(Lists.newArrayList("name", "quantity", "year", "reign",
                    "type", "source", "size", "weight", "comment1", "comment2"));
        }
        if (!permissionCodeSet.contains(PermissionCodeEnum.EDIT_RELIC_STATUS) &&
                !permissionCodeSet.contains(PermissionCodeEnum.RELIC_ENTER_MUSEUM)) {
            checkFields.addAll(Lists.newArrayList("statusId"));
        }

        for (String fieldName : checkFields) {
            try {
                Field field = getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                if (field.get(this) != null) {
                    return false;
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("", e);
            }
        }
        return true;
    }

}
