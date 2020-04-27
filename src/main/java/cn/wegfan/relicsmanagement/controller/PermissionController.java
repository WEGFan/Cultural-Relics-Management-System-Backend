package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.service.JobService;
import cn.wegfan.relicsmanagement.service.PermissionService;
import cn.wegfan.relicsmanagement.util.DataReturn;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 获取所有权限
     */
    @GetMapping("")
    public DataReturn listAllPermissions() {
        return DataReturn.success(permissionService.listAllPermissions());
    }

}