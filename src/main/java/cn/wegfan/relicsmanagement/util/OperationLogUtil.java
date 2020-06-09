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

@Slf4j
public class OperationLogUtil {

    public static <T> Map<String, FieldDifference> getDifferenceFieldMap(T before, T after, Class<?> cls) throws IllegalAccessException, InstantiationException {
        Map<String, FieldDifference> resultFieldMap = new LinkedHashMap<>();
        if (before == null && after == null) {
            return resultFieldMap;
        }
        if (before == null) {
            before = (T)cls.newInstance();
        }
        if (after == null) {
            after = (T)cls.newInstance();
        }
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
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
                    ObjectUtil.defaultIfNull(v.newValue, "")));
        });
        resultString += stringJoiner.toString();
        return resultString;
    }

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
                    ObjectUtil.defaultIfNull(v.oldValue, ""),
                    ObjectUtil.defaultIfNull(v.newValue, "")));
        });
        resultString += stringJoiner.toString();
        return resultString;
    }

    public static String getDeleteItemDetailLog(OperationItemTypeEnum itemType, Integer itemId, String itemName) {
        String resultString = StrUtil.format("删除了编号为{}的{}（{}）", itemId, itemType.getName(), itemName);
        return resultString;
    }

    @Data
    public static class FieldDifference {

        private String description;

        private Object oldValue;

        private Object newValue;

        public FieldDifference(String description, Object oldValue, Object newValue) {
            this.description = description;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

    }

}
