package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.SuccessVo;
import cn.wegfan.relicsmanagement.vo.WarehouseVo;

import java.util.List;

public interface WarehouseService {

    PageResultVo<WarehouseVo> listNotDeletedWarehousesByNameAndPage(String name, long pageIndex, long pageSize);

    List<WarehouseVo> listNotDeletedWarehouses();

    WarehouseVo createWarehouse(String name);

    WarehouseVo updateWarehouse(Integer warehouseId, String name);

    SuccessVo deleteWarehouse(Integer warehouseId);

}
