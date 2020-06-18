package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.constant.PermissionCodeEnum;
import cn.wegfan.relicsmanagement.model.dto.ShelfDto;
import cn.wegfan.relicsmanagement.model.stringdto.ShelfStringDto;
import cn.wegfan.relicsmanagement.model.vo.DataReturnVo;
import cn.wegfan.relicsmanagement.service.ShelfService;
import cn.wegfan.relicsmanagement.util.PaginationUtil;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/shelves")
public class ShelfController {

    @Autowired
    private ShelfService shelfService;

    @Autowired
    private MapperFacade mapperFacade;

    /**
     * 获取某仓库的所有货架
     *
     * @param warehouseId 按仓库编号筛选
     * @param name        按名称筛选
     * @param page        页码
     * @param count       每页个数
     */
    @GetMapping("")
    @RequiresUser
    public DataReturnVo listShelvesByWarehouse(@RequestParam(required = false) Integer warehouseId,
                                               @RequestParam(required = false) String name,
                                               @RequestParam(required = false) Integer page,
                                               @RequestParam(required = false) Integer count) {
        if (page != null && count != null) {
            return DataReturnVo.success(shelfService.listNotDeletedShelvesByWarehouseIdAndNameAndPage(warehouseId, name,
                    page, PaginationUtil.clampPageCount(count)));
        }
        if (warehouseId != null) {
            return DataReturnVo.success(shelfService.listNotDeletedShelvesByWarehouseId(warehouseId));
        }
        return DataReturnVo.success(shelfService.listNotDeletedShelves());
    }

    /**
     * 创建货架
     *
     * @param stringDto 货架对象
     */
    @PostMapping("")
    @RequiresPermissions(PermissionCodeEnum.WAREHOUSE)
    public DataReturnVo addShelf(@RequestBody @Valid ShelfStringDto stringDto) {
        ShelfDto dto = mapperFacade.map(stringDto, ShelfDto.class);
        return DataReturnVo.success(shelfService.createShelf(dto));
    }

    /**
     * 修改货架信息
     *
     * @param shelfId   货架编号
     * @param stringDto 货架对象
     */
    @PutMapping("{shelfId}")
    @RequiresPermissions(PermissionCodeEnum.WAREHOUSE)
    public DataReturnVo updateShelfInfo(@PathVariable Integer shelfId,
                                        @RequestBody @Valid ShelfStringDto stringDto) {
        ShelfDto dto = mapperFacade.map(stringDto, ShelfDto.class);
        return DataReturnVo.success(shelfService.updateShelf(shelfId, dto));
    }

    /**
     * 删除货架
     *
     * @param shelfId 货架编号
     */
    @DeleteMapping("{shelfId}")
    @RequiresPermissions(PermissionCodeEnum.WAREHOUSE)
    public DataReturnVo deleteShelf(@PathVariable Integer shelfId) {
        return DataReturnVo.success(shelfService.deleteShelf(shelfId));
    }

}
