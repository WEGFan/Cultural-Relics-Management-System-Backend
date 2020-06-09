package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.mapper.PermissionDao;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.model.entity.Permission;
import cn.wegfan.relicsmanagement.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private UserDao userDao;

    @Override
    public List<Permission> listAllPermissions() {
        return permissionDao.selectList(null);
    }

    @Override
    public Set<String> listAllPermissionCodeByUserId(Integer userId) {
        User user = userDao.selectByUserId(userId);
        if (user == null) {
            return new HashSet<>();
        }
        // 获取职位基础权限
        Set<String> permissionCodeSet = permissionDao.selectListByJobId(user.getJobId())
                .stream()
                .map(Permission::getCode)
                .collect(Collectors.toSet());
        // 获取用户额外权限
        Set<String> extraPermissionCodeSet = user.getExtraPermissions()
                .stream()
                .map(Permission::getCode)
                .collect(Collectors.toSet());
        permissionCodeSet.addAll(extraPermissionCodeSet);
        return permissionCodeSet;
    }

    @Override
    public Set<String> listAllPermissionCodeByCurrentLoginUser() {
        // 获取当前登录的用户编号
        Integer currentLoginUserId = (Integer)SecurityUtils.getSubject().getPrincipal();
        Set<String> permissionCodeSet = listAllPermissionCodeByUserId(currentLoginUserId);
        return permissionCodeSet;
    }

}
