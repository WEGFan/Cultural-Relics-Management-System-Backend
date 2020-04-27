package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.entity.Permission;
import cn.wegfan.relicsmanagement.mapper.PermissionDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;


    @Override
    public List<Permission> listAllPermissions() {
        return permissionDao.selectList(null);
    }

}
