package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.WarehouseVo;

public interface WarehouseService {

    PageResultVo<WarehouseVo> listWarehousesByNameAndPage(String name, long currentPage, long pageSize);

}
