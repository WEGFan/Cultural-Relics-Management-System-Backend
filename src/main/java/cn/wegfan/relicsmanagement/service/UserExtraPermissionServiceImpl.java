package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.mapper.PermissionDao;
import cn.wegfan.relicsmanagement.mapper.UserExtraPermissionDao;
import cn.wegfan.relicsmanagement.model.entity.Permission;
import cn.wegfan.relicsmanagement.model.entity.UserExtraPermission;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserExtraPermissionServiceImpl extends ServiceImpl<UserExtraPermissionDao, UserExtraPermission> implements UserExtraPermissionService {

    @Autowired
    private UserExtraPermissionDao userExtraPermissionDao;

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 管理员职位编号
     */
    private final Integer ADMIN_PERMISSION_ID = 1;

    @Override
    public Set<Integer> updateUserExtraPermissions(Integer userId, Integer jobId, Set<Integer> permissionId) {
        // 删除已有记录
        userExtraPermissionDao.deleteByUserId(userId);
        // 获取职位的默认权限
        Set<Integer> jobPermission = permissionDao.selectListByJobId(jobId).stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());
        Set<Integer> realPermissionId = new TreeSet<>(permissionId);
        // 把职位的默认权限从权限编号集合中移除
        realPermissionId.removeAll(jobPermission);
        // 移除管理员权限
        realPermissionId.remove(ADMIN_PERMISSION_ID);
        Set<UserExtraPermission> userExtraPermissionList = realPermissionId.stream()
                .map(x -> new UserExtraPermission(userId, x))
                .collect(Collectors.toSet());
        saveBatch(userExtraPermissionList);
        return realPermissionId;
    }

}
