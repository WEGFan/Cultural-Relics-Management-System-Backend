package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.SuccessVo;
import cn.wegfan.relicsmanagement.vo.WarehouseVo;

public interface WarehouseService {

    PageResultVo<WarehouseVo> listNotDeletedWarehousesByNameAndPage(String name, long currentPage, long pageSize);

    WarehouseVo createWarehouse(String name);

    WarehouseVo updateWarehouse(Integer warehouseId, String name);
    
    SuccessVo deleteWarehouse(Integer warehouseId);
}
