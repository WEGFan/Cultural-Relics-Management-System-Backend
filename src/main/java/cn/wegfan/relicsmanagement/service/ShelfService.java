package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.model.dto.ShelfDto;
import cn.wegfan.relicsmanagement.model.vo.PageResultVo;
import cn.wegfan.relicsmanagement.model.vo.ShelfVo;
import cn.wegfan.relicsmanagement.model.vo.SuccessVo;

import java.util.List;

public interface ShelfService {

    /**
     * 分页筛选或获取所有未删除的货架
     *
     * @param warehouseId 根据仓库编号筛选
     * @param name        根据名称筛选
     * @param pageIndex   当前页码
     * @param pageSize    每页个数
     *
     * @return 货架分页对象
     */
    PageResultVo<ShelfVo> listNotDeletedShelvesByWarehouseIdAndNameAndPage(Integer warehouseId, String name,
                                                                           long pageIndex, long pageSize);

    /**
     * 获取所有未删除的货架
     *
     * @return 货架对象列表
     */
    List<ShelfVo> listNotDeletedShelves();

    /**
     * 根据仓库编号获取未删除的货架列表
     *
     * @param warehouseId 仓库编号
     *
     * @return 货架对象列表
     */
    List<ShelfVo> listNotDeletedShelvesByWarehouseId(Integer warehouseId);

    /**
     * 创建货架
     *
     * @param dto 货架对象
     *
     * @return 货架对象
     */
    ShelfVo createShelf(ShelfDto dto);

    /**
     * 根据货架编号更新货架
     *
     * @param shelfId 货架编号
     * @param dto     货架对象
     *
     * @return 货架对象
     */
    ShelfVo updateShelf(Integer shelfId, ShelfDto dto);

    /**
     * 根据货架编号删除货架
     *
     * @param shelfId 货架编号
     *
     * @return 成功对象
     */
    SuccessVo deleteShelf(Integer shelfId);

}
