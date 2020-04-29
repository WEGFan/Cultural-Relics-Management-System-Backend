package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.entity.UserPermission;
import cn.wegfan.relicsmanagement.mapper.PermissionDao;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.mapper.UserPermissionDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserPermissionServiceImpl extends ServiceImpl<UserPermissionDao, UserPermission> implements UserPermissionService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserPermissionDao userPermissionDao;

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public void updateUserPermissions(Integer userId, List<Integer> permissionId) {
        userPermissionDao.deleteByUserId(userId);
        List<UserPermission> userPermissionList = permissionId.stream()
                .map(x -> new UserPermission(userId, x))
                .collect(Collectors.toList());
        saveBatch(userPermissionList);
    }

}
