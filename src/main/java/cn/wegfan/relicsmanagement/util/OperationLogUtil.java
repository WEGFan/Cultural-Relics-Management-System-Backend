package cn.wegfan.relicsmanagement.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.wegfan.relicsmanagement.constant.OperationItemTypeEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 操作记录工具类
 */
@Slf4j
public class OperationLogUtil {

    /**
     * 获取两个对象的不同成员变量值映射
     *
     * @param before 修改前的对象
     * @param after  修改后的对象
     * @param cls    对象类型
     *
     * @return 不同成员变量值的映射，key 为成员变量名，value 为包含描述、修改前后的值的 {@link FieldDifference FieldDifference}
     */
    public static <T> Map<String, FieldDifference> getDifferenceFieldMap(T before, T after, Class<?> cls)
            throws IllegalAccessException, InstantiationException {
        Map<String, FieldDifference> resultFieldMap = new LinkedHashMap<>();
        // 如果前后都是null则直接返回空map
        if (before == null && after == null) {
            return resultFieldMap;
        }
        // 如果有其中一个是null，则使用无参构造方法初始化
        if (before == null) {
            before = (T)cls.newInstance();
        }
        if (after == null) {
            after = (T)cls.newInstance();
        }
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            // 获取注解
            OperationLogProperty annotation = field.getAnnotation(OperationLogProperty.class);
            if (annotation == null) {
                continue;
            }
            String name = annotation.name();
            try {
                Object oldValue = field.get(before),
                        newValue = field.get(after);
                if (Objects.deepEquals(oldValue, newValue)) {
                    continue;
                }
                FieldDifference fieldDifference = new FieldDifference(name, oldValue, newValue);
                resultFieldMap.put(field.getName(), fieldDifference);
                log.info("{} {}", name, fieldDifference);
            } catch (IllegalAccessException e) {
                log.error("", e);
            }
        }
        return resultFieldMap;
    }

    /**
     * 获取创建项目的详细日志
     *
     * @param itemType           操作对象类型枚举
     * @param itemId             操作对象编号
     * @param fieldDifferenceMap 对象修改信息映射
     *
     * @return 详细日志
     */
    public static String getCreateItemDetailLog(OperationItemTypeEnum itemType, Integer itemId,
                                                Map<String, FieldDifference> fieldDifferenceMap) {
        String resultString = StrUtil.format("创建了编号为{}的{}", itemId, itemType.getName());
        if (fieldDifferenceMap == null || fieldDifferenceMap.isEmpty()) {
            return resultString;
        }
        resultString += "，";
        StringJoiner stringJoiner = new StringJoiner("，");
        fieldDifferenceMap.forEach((k, v) -> {
            stringJoiner.add(StrUtil.format("【{}】为【{}】", v.description,
                    // 使用空字符串代替null
                    ObjectUtil.defaultIfNull(v.newValue, "")));
        });
        resultString += stringJoiner.toString();
        return resultString;
    }

    /**
     * 获取更新项目的详细日志
     *
     * @param itemType           操作对象类型枚举
     * @param itemId             操作对象编号
     * @param fieldDifferenceMap 对象修改信息映射
     *
     * @return 详细日志
     */
    public static String getUpdateItemDetailLog(OperationItemTypeEnum itemType, Integer itemId, String itemName,
                                                Map<String, FieldDifference> fieldDifferenceMap) {
        String resultString = StrUtil.format("更改了编号为{}的{}（{}）", itemId, itemType.getName(), itemName);
        if (fieldDifferenceMap == null || fieldDifferenceMap.isEmpty()) {
            return resultString;
        }
        resultString += "，";
        StringJoiner stringJoiner = new StringJoiner("，");
        fieldDifferenceMap.forEach((k, v) -> {
            stringJoiner.add(StrUtil.format("【{}】从【{}】改为【{}】", v.description,
                    // 使用空字符串代替null
                    ObjectUtil.defaultIfNull(v.oldValue, ""),
                    ObjectUtil.defaultIfNull(v.newValue, "")));
        });
        resultString += stringJoiner.toString();
        return resultString;
    }

    /**
     * 获取删除项目的详细日志
     *
     * @param itemType 操作对象类型枚举
     * @param itemId   操作对象编号
     * @param itemName 操作对象名称
     *
     * @return 详细日志
     */
    public static String getDeleteItemDetailLog(OperationItemTypeEnum itemType, Integer itemId, String itemName) {
        String resultString = StrUtil.format("删除了编号为{}的{}（{}）", itemId, itemType.getName(), itemName);
        return resultString;
    }

    /**
     * 不同成员变量对象
     */
    @Data
    public static class FieldDifference {

        /**
         * 成员变量含义
         */
        private String description;

        /**
         * 修改前的值
         */
        private Object oldValue;

        /**
         * 修改后的值
         */
        private Object newValue;

        public FieldDifference(String description, Object oldValue, Object newValue) {
            this.description = description;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

    }

}
