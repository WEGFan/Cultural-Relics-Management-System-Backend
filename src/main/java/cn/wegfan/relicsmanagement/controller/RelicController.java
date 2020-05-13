package cn.wegfan.relicsmanagement.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.wegfan.relicsmanagement.service.RelicService;
import cn.wegfan.relicsmanagement.util.PermissionCodeEnum;
import cn.wegfan.relicsmanagement.vo.DataReturnVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/v1/relics")
public class RelicController {

    @Autowired
    private RelicService relicService;

    /**
     * 获取所有文物状态
     */
    @GetMapping("status")
    @RequiresUser
    public DataReturnVo listAllRelicStatuses() {
        return DataReturnVo.success(relicService.listAllRelicStatus());
    }

    /**
     * 获取所有文物信息
     *
     * @param name   按名称筛选
     * @param status 按状态筛选
     * @param page   页码
     * @param count  获取个数
     * @param from   开始时间
     * @param to     结束时间
     * @param type   时间类型 enter入馆时间 leave离馆时间 lend外借时间 fix送修时间
     */
    @GetMapping("")
    @RequiresPermissions(PermissionCodeEnum.VIEW_RELIC_INFO)
    public DataReturnVo listRelics(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) Integer status,
                                   @RequestParam Integer page,
                                   @RequestParam Integer count,
                                   @RequestParam(required = false) Date from,
                                   @RequestParam(required = false) Date to,
                                   @RequestParam(required = false) String type) {
        log.debug("{} {} {} {}", name, status, page, count);
        log.debug("{} {} {}", from, to, type);
        throw new NotImplementedException();
    }

    /**
     * 获取文物详细信息
     *
     * @param relicId 文物编号
     */
    @GetMapping("{relicId}")
    @RequiresPermissions(PermissionCodeEnum.VIEW_RELIC_INFO)
    public DataReturnVo getRelicInfo(@PathVariable Integer relicId) {
        return DataReturnVo.success(relicService.getRelicById(relicId));
    }

    /**
     * 拍照上传文物【拍照人员】
     */
    @PostMapping("")
    @RequiresPermissions(PermissionCodeEnum.ADD_RELIC)
    public DataReturnVo addRelic(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String dir = Paths.get("data", "images")
                .toAbsolutePath()
                .toString();
        log.debug(dir);
        File file = FileUtil.touch(dir, "tmp/" + UUID.fastUUID().toString(true));
        multipartFile.transferTo(file);
        String tempPath = file.getAbsolutePath();
        log.debug(tempPath);
        return DataReturnVo.success(relicService.addRelicByPicturePath(tempPath));
    }

    /**
     * 修改文物详细信息【文职员工】
     * 入馆后修改文物状态信息/盘点/移动【仓库管理员】
     *
     * @param relicId 文物编号
     */
    @PutMapping("{relicId}")
    @RequiresPermissions(
            value = {PermissionCodeEnum.EDIT_RELIC_INFO, PermissionCodeEnum.EDIT_RELIC_STATUS},
            logical = Logical.OR
    )
    public DataReturnVo updateRelicInfo(@PathVariable Integer relicId) {
        throw new NotImplementedException();
    }

    /**
     * 修改文物价值【资产科】
     *
     * @param relicId 文物编号
     */
    @PutMapping("{relicId}/price")
    @RequiresPermissions(PermissionCodeEnum.VIEW_EDIT_RELIC_PRICE)
    public DataReturnVo updateRelicPrice(@PathVariable Integer relicId) {
        throw new NotImplementedException();
    }

    /**
     * 导出文物一览 Excel 表【文职人员（无价值）、管理员】
     *
     * @param excel 是否导出成 Excel
     */
    @GetMapping(value = "", params = "excel=true")
    @RequiresPermissions(PermissionCodeEnum.EXPORT_RELIC)
    public DataReturnVo exportAllRelicsToExcel(@RequestParam Boolean excel,
                                               @RequestParam(required = false) Integer warehouseId) {
        if (excel.equals(Boolean.FALSE)) {
            throw new NotImplementedException();
        }
        throw new NotImplementedException();
    }

    /**
     * 导出某仓库文物一览 Excel 表【仓库管理员、管理员】 <3.2.3 (3)>
     *
     * @param excel       是否导出成 Excel
     * @param warehouseId 根据仓库编号筛选
     */
    @GetMapping(value = "", params = {"excel=true", "warehouseId"})
    @RequiresPermissions(PermissionCodeEnum.EXPORT_WAREHOUSE_RELIC)
    public DataReturnVo exportWarehouseRelicsToExcel(@RequestParam Boolean excel,
                                                     @RequestParam(required = false) Integer warehouseId) {
        if (excel.equals(Boolean.FALSE)) {
            throw new NotImplementedException();
        }
        throw new NotImplementedException();
    }

    /**
     * 导出文物流水 Excel 表【管理员】
     *
     * @param excel 是否导出成 Excel
     * @param from  起始时间
     * @param to    结束时间
     */
    @GetMapping(value = "changes", params = "excel=true")
    @RequiresPermissions(PermissionCodeEnum.EXPORT_RELIC_CHANGE_LOG)
    public DataReturnVo exportRelicChangesToExcel(@RequestParam Boolean excel,
                                                  @RequestParam(required = false) Date from,
                                                  @RequestParam(required = false) Date to) {
        if (excel.equals(Boolean.FALSE)) {
            throw new NotImplementedException();
        }
        throw new NotImplementedException();
    }

    /**
     * 删除文物
     *
     * @param relicId 文物编号
     */
    @DeleteMapping("{relicId}")
    @RequiresPermissions(PermissionCodeEnum.ADMIN)
    public DataReturnVo deleteRelic(@PathVariable Integer relicId) {
        throw new NotImplementedException();
    }

}
