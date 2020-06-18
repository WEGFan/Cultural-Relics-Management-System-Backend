package cn.wegfan.relicsmanagement.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示被注解的成员变量会被包含在操作详细日志里
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLogProperty {

    /**
     * 含义
     */
    String name();

}
