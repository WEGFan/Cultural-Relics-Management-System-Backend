package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.model.vo.CheckIdVo;
import cn.wegfan.relicsmanagement.model.vo.PageResultVo;
import cn.wegfan.relicsmanagement.model.vo.RelicCheckVo;
import cn.wegfan.relicsmanagement.model.vo.SuccessVo;

public interface RelicCheckService {

    PageResultVo<RelicCheckVo> listByWarehouseIdAndPage(Integer warehouseId, long pageIndex, long pageSize);

    CheckIdVo startRelicCheck(Integer warehouseId);

    SuccessVo endRelicCheck(Integer checkId);

}
