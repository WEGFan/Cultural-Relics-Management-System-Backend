package cn.wegfan.relicsmanagement.service;

import cn.wegfan.relicsmanagement.dto.RelicMoveDto;
import cn.wegfan.relicsmanagement.entity.Relic;
import cn.wegfan.relicsmanagement.entity.RelicCheck;
import cn.wegfan.relicsmanagement.entity.RelicCheckDetail;
import cn.wegfan.relicsmanagement.mapper.*;
import cn.wegfan.relicsmanagement.util.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.util.BusinessException;
import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.RelicCheckDetailVo;
import cn.wegfan.relicsmanagement.vo.SuccessVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RelicCheckDetailServiceImpl extends ServiceImpl<RelicCheckDetailDao, RelicCheckDetail> implements RelicCheckDetailService {

    @Autowired
    private RelicCheckDetailDao relicCheckDetailDao;

    @Autowired
    private RelicDao relicDao;

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private RelicCheckDao relicCheckDao;

    @Autowired
    private ShelfDao shelfDao;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public PageResultVo<RelicCheckDetailVo> listRelicCheckDetailByCheckIdAndStatusAndPage(Integer checkId, Boolean checked, long pageIndex, long pageSize) {
        Page<RelicCheckDetail> page = new Page<>(pageIndex, pageSize);

        Page<RelicCheckDetail> pageResult = relicCheckDetailDao.selectPageByCheckId(page, checkId, checked);

        List<RelicCheckDetail> relicCheckDetailList = pageResult.getRecords();
        log.debug(relicCheckDetailList.toString());

        List<RelicCheckDetailVo> relicCheckDetailVoList = mapperFacade.mapAsList(relicCheckDetailList, RelicCheckDetailVo.class);
        return new PageResultVo<RelicCheckDetailVo>(relicCheckDetailVoList, pageResult);
    }

    @Override
    public SuccessVo addRelicCheckDetail(Integer checkId, Integer relicId, RelicMoveDto dto) {
        RelicCheck relicCheck = relicCheckDao.selectNotEndByCheckId(checkId);
        if (relicCheck == null) {
            throw new BusinessException(BusinessErrorEnum.RelicCheckEnded);
        }

        Relic relic = relicDao.selectNotDeletedByRelicId(relicId);
        if (relic == null) {
            throw new BusinessException(BusinessErrorEnum.RelicNotExists);
        }

        Integer warehouseId = dto.getWarehouseId();
        Integer shelfId = dto.getShelfId();
        if (warehouseId == null) {
            warehouseId = relic.getWarehouseId();
        }
        if (shelfId == null) {
            shelfId = relic.getShelfId();
        }

        // 获取当前登录的用户编号
        Integer currentLoginUserId = (Integer)SecurityUtils.getSubject().getPrincipal();

        if (warehouseDao.selectNotDeletedById(warehouseId) == null) {
            throw new BusinessException(BusinessErrorEnum.WarehouseNotExists);
        }
        if (shelfDao.selectNotDeletedByWarehouseIdAndShelfId(warehouseId, shelfId) == null) {
            throw new BusinessException(BusinessErrorEnum.ShelfNotExists);
        }

        RelicCheckDetail relicCheckDetail = relicCheckDetailDao.selectByCheckIdAndRelicId(checkId, relicId);

        // 说明这个文物在盘点前不属于这个仓库
        if (relicCheckDetail == null) {
            relicCheckDetail = new RelicCheckDetail();
            relicCheckDetail.setOldWarehouseId(relic.getWarehouseId());
            relicCheckDetail.setOldShelfId(relic.getShelfId());
            relicCheckDetail.setCreateTime(new Date());
            relicCheckDetail.setCheckId(checkId);
            relicCheckDetail.setRelicId(relicId);
        }

        if (relicCheckDetail.getCheckTime() != null) {
            throw new BusinessException(BusinessErrorEnum.RelicAlreadyChecked);
        }

        relicCheckDetail.setOperatorId(currentLoginUserId);
        relicCheckDetail.setNewWarehouseId(warehouseId);
        relicCheckDetail.setNewShelfId(shelfId);
        relicCheckDetail.setCheckTime(new Date());
        saveOrUpdate(relicCheckDetail);

        relic.setWarehouseId(warehouseId);
        relic.setShelfId(shelfId);
        relic.setLastCheckTime(new Date());
        relic.setUpdateTime(new Date());
        relicDao.updateById(relic);

        return new SuccessVo(true);
    }

    @Override
    public void updateRelicCheckDetailAfterRelicMove(Integer relicId, RelicMoveDto oldPlace, RelicMoveDto newPlace) {
        RelicCheck oldRelicCheck = relicCheckDao.selectNotEndByWarehouseId(oldPlace.getWarehouseId());
        RelicCheck newRelicCheck = relicCheckDao.selectNotEndByWarehouseId(newPlace.getWarehouseId());

        // 如果旧仓库正在被盘点
        if (oldRelicCheck != null) {
            RelicCheckDetail oldRelicCheckDetail = relicCheckDetailDao.selectNotCheckedByCheckIdAndRelicId(oldRelicCheck.getId(), relicId);
            // 如果对应的文物还没有被盘点的话，就删除这个记录
            if (oldRelicCheckDetail != null) {
                relicCheckDetailDao.deleteById(oldRelicCheckDetail);
            }
        }
        // 如果新仓库正在被盘点，就插入新位置信息
        if (newRelicCheck != null) {
            RelicCheckDetail newRelicCheckDetail = new RelicCheckDetail();
            newRelicCheckDetail.setRelicId(relicId);
            newRelicCheckDetail.setOldWarehouseId(newPlace.getWarehouseId());
            newRelicCheckDetail.setOldShelfId(newPlace.getShelfId());
            newRelicCheckDetail.setCheckId(newRelicCheck.getId());
            newRelicCheckDetail.setCreateTime(new Date());
            relicCheckDetailDao.insert(newRelicCheckDetail);
        }
    }

    @Override
    public void updateRelicCheckDetailAfterShelfMove(Integer shelfId, Integer oldWarehouseId, Integer newWarehouseId) {
        RelicCheck oldRelicCheck = relicCheckDao.selectNotEndByWarehouseId(oldWarehouseId);
        RelicCheck newRelicCheck = relicCheckDao.selectNotEndByWarehouseId(newWarehouseId);

        // 如果旧仓库正在被盘点
        if (oldRelicCheck != null) {
            List<RelicCheckDetail> oldRelicCheckDetailList = relicCheckDetailDao.selectNotCheckedListByCheckIdAndOldShelfId(oldRelicCheck.getId(), shelfId);
            log.debug(oldRelicCheckDetailList.toString());
            // 如果存在还没有被盘点的文物，就删除掉
            if (!oldRelicCheckDetailList.isEmpty()) {
                List<Integer> idList = oldRelicCheckDetailList.stream()
                        .map(RelicCheckDetail::getId)
                        .collect(Collectors.toList());
                relicCheckDetailDao.deleteBatchIds(idList);
            }
        }
        // 如果新仓库正在被盘点，就插入新位置信息
        if (newRelicCheck != null) {
            // 获取当前货架的所有文物
            List<Relic> shelfRelicList = relicDao.selectNotDeletedByShelfId(shelfId);
            List<RelicCheckDetail> newRelicCheckDetailList = new ArrayList<>();
            for (Relic relic : shelfRelicList) {
                RelicCheckDetail relicCheckDetail = new RelicCheckDetail();
                relicCheckDetail.setRelicId(relic.getId());
                relicCheckDetail.setOldWarehouseId(newWarehouseId);
                relicCheckDetail.setOldShelfId(shelfId);
                relicCheckDetail.setCheckId(newRelicCheck.getId());
                relicCheckDetail.setCreateTime(new Date());
                newRelicCheckDetailList.add(relicCheckDetail);
            }
            saveBatch(newRelicCheckDetailList);
        }
    }

}
