package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.entity.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionService {

    List<Permission> listAllPermissions();
    
    Set<String> listAllPermissionCodeByUserId(Integer userId);

}
