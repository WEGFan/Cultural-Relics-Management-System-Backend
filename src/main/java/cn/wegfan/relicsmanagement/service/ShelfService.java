package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.dto.ShelfDto;
import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.ShelfVo;
import cn.wegfan.relicsmanagement.vo.SuccessVo;

import java.util.List;

public interface ShelfService {

    PageResultVo<ShelfVo> listNotDeletedShelvesByWarehouseIdAndNameAndPage(Integer warehouseId, String name, long pageIndex, long pageSize);

    List<ShelfVo> listNotDeletedShelves();

    List<ShelfVo> listNotDeletedShelvesByWarehouseId(Integer warehouseId);

    ShelfVo createShelf(ShelfDto dto);

    ShelfVo updateShelf(Integer shelfId, ShelfDto dto);

    SuccessVo deleteShelf(Integer shelfId);

}
