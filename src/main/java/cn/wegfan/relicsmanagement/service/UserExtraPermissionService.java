package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.model.entity.UserExtraPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

public interface UserExtraPermissionService extends IService<UserExtraPermission> {

    Set<Integer> updateUserExtraPermissions(Integer userId, Integer jobId, Set<Integer> permissionId);

}
