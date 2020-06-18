package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.model.vo.PageResultVo;
import cn.wegfan.relicsmanagement.model.vo.SuccessVo;
import cn.wegfan.relicsmanagement.model.vo.WarehouseVo;

import java.util.List;

public interface WarehouseService {

    /**
     * 分页筛选或获取所有未删除的仓库
     *
     * @param name      按名称筛选
     * @param pageIndex 当前页码
     * @param pageSize  每页个数
     *
     * @return 仓库分页对象
     */
    PageResultVo<WarehouseVo> listNotDeletedWarehousesByNameAndPage(String name, long pageIndex, long pageSize);

    /**
     * 获取所有未删除的仓库
     *
     * @return 仓库对象列表
     */
    List<WarehouseVo> listNotDeletedWarehouses();

    /**
     * 创建仓库
     *
     * @param name 仓库名称
     *
     * @return 仓库对象
     */
    WarehouseVo createWarehouse(String name);

    /**
     * 根据仓库编号更新仓库
     *
     * @param warehouseId 仓库编号
     * @param name        名称
     *
     * @return 仓库对象
     */
    WarehouseVo updateWarehouse(Integer warehouseId, String name);

    /**
     * 根据仓库编号删除仓库
     *
     * @param warehouseId 仓库编号
     *
     * @return 成功对象
     */
    SuccessVo deleteWarehouse(Integer warehouseId);

}
