package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.model.entity.UserExtraPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

public interface UserExtraPermissionService extends IService<UserExtraPermission> {

    /**
     * 更新用户的额外权限
     *
     * @param userId       用户编号
     * @param jobId        职务编号
     * @param permissionId 权限编号集合
     *
     * @return 更新后用户实际的权限编号集合
     */
    Set<Integer> updateUserExtraPermissions(Integer userId, Integer jobId, Set<Integer> permissionId);

}
