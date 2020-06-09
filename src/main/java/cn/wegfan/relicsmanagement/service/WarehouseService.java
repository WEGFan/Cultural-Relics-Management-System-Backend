package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.model.vo.PageResultVo;
import cn.wegfan.relicsmanagement.model.vo.SuccessVo;
import cn.wegfan.relicsmanagement.model.vo.WarehouseVo;

import java.util.List;

public interface WarehouseService {

    PageResultVo<WarehouseVo> listNotDeletedWarehousesByNameAndPage(String name, long pageIndex, long pageSize);

    List<WarehouseVo> listNotDeletedWarehouses();

    WarehouseVo createWarehouse(String name);

    WarehouseVo updateWarehouse(Integer warehouseId, String name);

    SuccessVo deleteWarehouse(Integer warehouseId);

}
