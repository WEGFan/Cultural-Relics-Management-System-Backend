package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.dto.WarehouseNameDto;
import cn.wegfan.relicsmanagement.entity.Permission;
import cn.wegfan.relicsmanagement.service.WarehouseService;
import cn.wegfan.relicsmanagement.util.PermissionCodeEnum;
import cn.wegfan.relicsmanagement.vo.DataReturnVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/warehouses")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    /**
     * 获取所有仓库信息【仓库管理员】
     *
     * @param name  按名称筛选
     * @param page  页码
    * @param count 获取个数
     */
    @GetMapping("")
    @RequiresUser
    public DataReturnVo listWarehouses(@RequestParam(required = false) String name,
                                       @RequestParam Integer page,
                                       @RequestParam Integer count) {
        return DataReturnVo.success(warehouseService.listNotDeletedWarehousesByNameAndPage(name, page, count));
    }

    /**
     * 创建仓库【仓库管理员】
     */
    @PostMapping("")
    @RequiresPermissions(PermissionCodeEnum.WAREHOUSE)
    public DataReturnVo addWarehouse(@RequestBody WarehouseNameDto dto) {
        return DataReturnVo.success(warehouseService.createWarehouse(dto.getName()));
    }

    /**
     * 修改仓库信息【仓库管理员】
     *
     * @param warehouseId 仓库编号
     */
    @PutMapping("{warehouseId}")
    @RequiresPermissions(PermissionCodeEnum.WAREHOUSE)
    public DataReturnVo updateWarehouseInfo(@PathVariable Integer warehouseId,
                                            @RequestBody WarehouseNameDto dto) {
        return DataReturnVo.success(warehouseService.updateWarehouse(warehouseId, dto.getName()));
    }

    /**
     * 删除仓库【仓库管理员】
     *
     * @param warehouseId 仓库编号
     */
    @DeleteMapping("{warehouseId}")
    @RequiresPermissions(PermissionCodeEnum.WAREHOUSE)
    public DataReturnVo deleteWarehouse(@PathVariable Integer warehouseId) {
        return DataReturnVo.success(warehouseService.deleteWarehouse(warehouseId));
    }

}
