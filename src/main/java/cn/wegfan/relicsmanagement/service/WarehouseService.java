package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.WarehouseVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface WarehouseService {

    PageResultVo<WarehouseVo> listWarehousesByNameAndPage(String name, long currentPage, long pageSize);

}
