package cn.wegfan.relicsmanagement.controller;

import cn.wegfan.relicsmanagement.vo.DataReturnVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Slf4j
@RestController
@RequestMapping("/api/v1/warehouses")
public class WarehouseController {

    /**
     * 获取所有仓库信息【仓库管理员】
     *
     * @param name  按名称筛选
     * @param page  页码
     * @param count 获取个数
     */
    @GetMapping("")
    public DataReturnVo listWarehouses(@RequestParam(required = false) String name,
                                       @RequestParam Integer page,
                                       @RequestParam Integer count) {
        throw new NotImplementedException();
    }

    /**
     * 创建仓库【仓库管理员】
     */
    @PostMapping("")
    public DataReturnVo addWarehouse() {
        throw new NotImplementedException();
    }

    /**
     * 修改仓库信息【仓库管理员】
     *
     * @param warehouseId 仓库编号
     */
    @PutMapping("{warehouseId}")
    public DataReturnVo updateWarehouseInfo(@PathVariable Integer warehouseId) {
        throw new NotImplementedException();
    }

    /**
     * 删除仓库【仓库管理员】
     *
     * @param warehouseId 仓库编号
     */
    @DeleteMapping("{warehouseId}")
    public DataReturnVo deleteWarehouse(@PathVariable Integer warehouseId) {
        throw new NotImplementedException();
    }

}
