package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.entity.JobPermission;
import cn.wegfan.relicsmanagement.entity.Permission;
import cn.wegfan.relicsmanagement.entity.UserExtraPermission;
import cn.wegfan.relicsmanagement.mapper.JobDao;
import cn.wegfan.relicsmanagement.mapper.PermissionDao;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.mapper.UserExtraPermissionDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserExtraPermissionServiceImpl extends ServiceImpl<UserExtraPermissionDao, UserExtraPermission> implements UserExtraPermissionService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserExtraPermissionDao userExtraPermissionDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private JobDao jobDao;

    /**
     * 管理员职位编号
     */
    private final Integer ADMIN_PERMISSION_ID = 1;

    @Override
    public void updateUserExtraPermissions(Integer userId, Integer jobId, Set<Integer> permissionId) {
        // 删除已有记录
        userExtraPermissionDao.deleteByUserId(userId);
        // 获取职位的默认权限
        Set<Integer> jobPermission = permissionDao.selectListByJobId(jobId).stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());
        // 把职位的默认权限从权限编号集合中移除
        permissionId.removeAll(jobPermission);
        // 移除管理员权限
        permissionId.remove(ADMIN_PERMISSION_ID);
        Set<UserExtraPermission> userExtraPermissionList = permissionId.stream()
                .map(x -> new UserExtraPermission(userId, x))
                .collect(Collectors.toSet());
        saveBatch(userExtraPermissionList);
    }

}
