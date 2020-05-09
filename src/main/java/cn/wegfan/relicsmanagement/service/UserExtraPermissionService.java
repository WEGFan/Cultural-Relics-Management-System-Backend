package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.entity.UserExtraPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

public interface UserExtraPermissionService extends IService<UserExtraPermission> {

    void updateUserExtraPermissions(Integer userId, Integer jobId, Set<Integer> permissionId);

}
