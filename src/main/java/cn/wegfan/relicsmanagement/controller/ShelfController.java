package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.dto.ShelfDto;
import cn.wegfan.relicsmanagement.dto.ShelfNameDto;
import cn.wegfan.relicsmanagement.service.ShelfService;
import cn.wegfan.relicsmanagement.util.PermissionCodeEnum;
import cn.wegfan.relicsmanagement.vo.DataReturnVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/shelves")
public class ShelfController {

    @Autowired
    private ShelfService shelfService;

    /**
     * 获取某仓库的所有货架【仓库管理员】
     *
     * @param warehouseId 按仓库编号筛选
     * @param name        按名称筛选
     * @param page        页码
     * @param count       获取个数
     */
    @GetMapping("")
    @RequiresUser
    public DataReturnVo listShelvesByWarehouse(@RequestParam Integer warehouseId,
                                               @RequestParam(required = false) String name,
                                               @RequestParam(required = false) Integer page,
                                               @RequestParam(required = false) Integer count) {
        if (page != null && count != null) {
            return DataReturnVo.success(shelfService.listNotDeletedShelvesByWarehouseIdAndNameAndPage(warehouseId, name, page, count));
        }
        return DataReturnVo.success(shelfService.listNotDeletedShelves());
    }

    /**
     * 创建货架【仓库管理员】
     */
    @PostMapping("")
    @RequiresPermissions(PermissionCodeEnum.WAREHOUSE)
    public DataReturnVo addShelf(@RequestBody @Valid ShelfDto dto) {
        return DataReturnVo.success(shelfService.createShelf(dto));
    }

    /**
     * 修改货架信息【仓库管理员】
     *
     * @param shelfId 货架编号
     */
    @PutMapping("{shelfId}")
    @RequiresPermissions(PermissionCodeEnum.WAREHOUSE)
    public DataReturnVo updateShelfInfo(@PathVariable Integer shelfId,
                                        @RequestBody @Valid ShelfNameDto dto) {
        return DataReturnVo.success(shelfService.updateShelf(shelfId, dto.getName()));
    }

    /**
     * 删除货架【仓库管理员】
     *
     * @param shelfId 货架编号
     */
    @DeleteMapping("{shelfId}")
    @RequiresPermissions(PermissionCodeEnum.WAREHOUSE)
    public DataReturnVo deleteShelf(@PathVariable Integer shelfId) {
       return DataReturnVo.success(shelfService.deleteShelf(shelfId)); 
    }

}