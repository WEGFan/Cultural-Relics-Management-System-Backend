package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.service.BackupService;
import cn.wegfan.relicsmanagement.vo.DataReturnVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Slf4j
@RestController
@RequestMapping("/api/v1/backups")
public class BackupController {

    @Autowired
    private BackupService backupService;

    /**
     * 获取所有数据库备份信息【管理员】
     */
    @GetMapping("")
    public DataReturnVo listAllDatabaseBackups() {
        throw new NotImplementedException();
    }

    /**
     * 数据库备份【管理员】
     */
    @PostMapping("")
    public DataReturnVo createDatabaseBackup() {
        throw new NotImplementedException();
    }

    /**
     * 数据库恢复【管理员】
     *
     * @param backupId 备份编号
     */
    @GetMapping("{backupId}")
    public DataReturnVo restoreDatabase(@PathVariable Integer backupId) {
        throw new NotImplementedException();
    }

}
