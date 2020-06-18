package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.model.entity.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionService {

    /**
     * 获取所有权限
     *
     * @return 权限对象列表
     */
    List<Permission> listAllPermissions();

    /**
     * 根据用户编号获取用户权限代码集合
     *
     * @param userId 用户编号
     *
     * @return 权限代码集合
     */
    Set<String> listAllPermissionCodeByUserId(Integer userId);

    /**
     * 获取当前登录用户的权限代码集合
     *
     * @return 权限代码集合
     */
    Set<String> listAllPermissionCodeByCurrentLoginUser();

}
