package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.constant.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.constant.OperationItemTypeEnum;
import cn.wegfan.relicsmanagement.mapper.RelicCheckDao;
import cn.wegfan.relicsmanagement.mapper.RelicDao;
import cn.wegfan.relicsmanagement.mapper.WarehouseDao;
import cn.wegfan.relicsmanagement.model.entity.Relic;
import cn.wegfan.relicsmanagement.model.entity.RelicCheck;
import cn.wegfan.relicsmanagement.model.entity.RelicCheckDetail;
import cn.wegfan.relicsmanagement.model.entity.Warehouse;
import cn.wegfan.relicsmanagement.model.vo.CheckIdVo;
import cn.wegfan.relicsmanagement.model.vo.PageResultVo;
import cn.wegfan.relicsmanagement.model.vo.RelicCheckVo;
import cn.wegfan.relicsmanagement.model.vo.SuccessVo;
import cn.wegfan.relicsmanagement.util.BusinessException;
import cn.wegfan.relicsmanagement.util.OperationLogUtil;
import cn.wegfan.relicsmanagement.util.OperationLogUtil.FieldDifference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RelicCheckServiceImpl implements RelicCheckService {

    @Autowired
    private RelicDao relicDao;

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private RelicCheckDao relicCheckDao;

    @Autowired
    private RelicCheckDetailService relicCheckDetailService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private OperationLogService operationLogService;

    @Override
    public PageResultVo<RelicCheckVo> listByWarehouseIdAndPage(Integer warehouseId, long pageIndex, long pageSize) {
        Page<RelicCheck> page = new Page<>(pageIndex, pageSize);
        Page<RelicCheck> pageResult = relicCheckDao.selectPageByWarehouseId(page, warehouseId);

        List<RelicCheck> relicCheckList = pageResult.getRecords();
        List<RelicCheckVo> relicCheckVoList = mapperFacade.mapAsList(relicCheckList, RelicCheckVo.class);
        return new PageResultVo<RelicCheckVo>(relicCheckVoList, pageResult);
    }

    @Override
    public CheckIdVo startRelicCheck(Integer warehouseId) {
        Warehouse warehouse = warehouseDao.selectNotDeletedById(warehouseId);
        // 检查仓库是否存在
        if (warehouse == null) {
            throw new BusinessException(BusinessErrorEnum.WarehouseNotExists);
        }

        // 检查该仓库是否在被盘点
        if (relicCheckDao.selectNotEndByWarehouseId(warehouseId) != null) {
            throw new BusinessException(BusinessErrorEnum.WarehouseHasBeenChecking);
        }

        RelicCheck relicCheck = new RelicCheck();
        relicCheck.setWarehouseId(warehouseId);
        relicCheck.setStartTime(new Date());

        relicCheckDao.insert(relicCheck);

        // 获取盘点仓库内的所有文物并批量插入到盘点详情表里
        List<Relic> warehouseRelicList = relicDao.selectNotDeletedByWarehouseId(warehouseId);
        List<RelicCheckDetail> relicCheckDetailList = new ArrayList<>();
        for (Relic relic : warehouseRelicList) {
            RelicCheckDetail relicCheckDetail = new RelicCheckDetail();
            relicCheckDetail.setCheckId(relicCheck.getId());
            relicCheckDetail.setRelicId(relic.getId());
            relicCheckDetail.setCreateTime(new Date());
            relicCheckDetail.setOldWarehouseId(relic.getWarehouseId());
            relicCheckDetail.setOldShelfId(relic.getShelfId());
            relicCheckDetailList.add(relicCheckDetail);
        }
        relicCheckDetailService.saveBatch(relicCheckDetailList);

        try {
            Map<String, FieldDifference> fieldDifferenceMap = OperationLogUtil.getDifferenceFieldMap(null, relicCheck, RelicCheck.class);
            // 把所属仓库变成名称
            String warehouseName = warehouse.getName();
            fieldDifferenceMap.get("warehouseId").setNewValue(warehouseName);

            // 添加操作记录
            OperationItemTypeEnum itemType = OperationItemTypeEnum.Check;
            Integer itemId = relicCheck.getId();
            String detail = OperationLogUtil.getCreateItemDetailLog(itemType, itemId, fieldDifferenceMap);
            detail = detail.replaceFirst("创建", "开始");
            operationLogService.addOperationLog(itemType, itemId,
                    "开始盘点", detail);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("获取不同成员变量错误", e);
            throw new BusinessException(BusinessErrorEnum.InternalServerError);
        }

        return new CheckIdVo(relicCheck.getId());
    }

    @Override
    public SuccessVo endRelicCheck(Integer checkId) {
        RelicCheck relicCheck = relicCheckDao.selectNotEndByCheckId(checkId);
        // 如果盘点已经结束
        if (relicCheck == null) {
            throw new BusinessException(BusinessErrorEnum.RelicCheckEnded);
        }

        // 添加操作记录
        String detail = OperationLogUtil.getDeleteItemDetailLog(OperationItemTypeEnum.Check, checkId, relicCheck.getWarehouse().getName());
        detail = detail.replaceFirst("删除", "结束");
        operationLogService.addOperationLog(OperationItemTypeEnum.Check, checkId,
                "结束盘点", detail);

        int result = relicCheckDao.updateEndTimeByCheckId(checkId);
        return new SuccessVo(result > 0);
    }

}
