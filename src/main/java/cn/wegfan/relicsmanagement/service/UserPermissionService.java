package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.entity.UserPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserPermissionService extends IService<UserPermission> {

    void updateUserPermissions(Integer userId, List<Integer> permissionId);

}
