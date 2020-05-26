package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.entity.Relic;
import cn.wegfan.relicsmanagement.entity.RelicCheck;
import cn.wegfan.relicsmanagement.entity.RelicCheckDetail;
import cn.wegfan.relicsmanagement.mapper.RelicCheckDao;
import cn.wegfan.relicsmanagement.mapper.RelicDao;
import cn.wegfan.relicsmanagement.mapper.WarehouseDao;
import cn.wegfan.relicsmanagement.util.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.util.BusinessException;
import cn.wegfan.relicsmanagement.vo.CheckIdVo;
import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.RelicCheckVo;
import cn.wegfan.relicsmanagement.vo.SuccessVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Override
    public PageResultVo<RelicCheckVo> listByWarehouseIdAndPage(Integer warehouseId, long pageIndex, long pageSize) {
        Page<RelicCheck> page = new Page<>(pageIndex, pageSize);

        Page<RelicCheck> pageResult = relicCheckDao.selectPageByWarehouseId(page, warehouseId);

        List<RelicCheck> relicCheckList = pageResult.getRecords();
        log.debug(relicCheckList.toString());
        // TODO: checkCount
        List<RelicCheckVo> relicCheckVoList = mapperFacade.mapAsList(relicCheckList, RelicCheckVo.class);
        return new PageResultVo<RelicCheckVo>(relicCheckVoList, pageResult);
    }

    @Override
    public CheckIdVo startRelicCheck(Integer warehouseId) {
        // 获取当前登录的用户编号
        Integer currentLoginUserId = (Integer)SecurityUtils.getSubject().getPrincipal();

        // 检查仓库是否存在
        if (warehouseDao.selectNotDeletedById(warehouseId) == null) {
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

        return new CheckIdVo(relicCheck.getId());
    }

    @Override
    public SuccessVo endRelicCheck(Integer checkId) {
        int result = relicCheckDao.updateEndTimeByCheckId(checkId);
        return new SuccessVo(result > 0);
    }

}
