package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.vo.CheckIdVo;
import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.RelicCheckVo;
import cn.wegfan.relicsmanagement.vo.SuccessVo;

public interface RelicCheckService {

    PageResultVo<RelicCheckVo> listByWarehouseIdAndPage(Integer warehouseId, long pageIndex, long pageSize);

    CheckIdVo startRelicCheck(Integer warehouseId);

    SuccessVo endRelicCheck(Integer checkId);

}
