package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.service.PermissionService;
import cn.wegfan.relicsmanagement.vo.DataReturnVo;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private MapperFacade mapperFacade;

    /**
     * 获取所有权限
     */
    @GetMapping("")
    @RequiresUser
    public DataReturnVo listAllPermissions() {
        return DataReturnVo.success(permissionService.listAllPermissions());
    }

}