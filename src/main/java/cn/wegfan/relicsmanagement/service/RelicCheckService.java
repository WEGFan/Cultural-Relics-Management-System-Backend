package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.model.vo.CheckIdVo;
import cn.wegfan.relicsmanagement.model.vo.PageResultVo;
import cn.wegfan.relicsmanagement.model.vo.RelicCheckVo;
import cn.wegfan.relicsmanagement.model.vo.SuccessVo;

public interface RelicCheckService {

    /**
     * 分页筛选或获取所有盘点记录
     *
     * @param warehouseId 根据仓库编号筛选
     * @param pageIndex   当前页码
     * @param pageSize    每页个数
     *
     * @return 盘点记录分页对象
     */
    PageResultVo<RelicCheckVo> listByWarehouseIdAndPage(Integer warehouseId, long pageIndex, long pageSize);

    /**
     * 开始一次盘点
     *
     * @param warehouseId 仓库编号
     *
     * @return 盘点编号对象
     */
    CheckIdVo startRelicCheck(Integer warehouseId);

    /**
     * 根据盘点编号结束盘点
     *
     * @param checkId 盘点编号
     *
     * @return 成功对象
     */
    SuccessVo endRelicCheck(Integer checkId);

}
