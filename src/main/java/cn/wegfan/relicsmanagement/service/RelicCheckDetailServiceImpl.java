package cn.wegfan.relicsmanagement.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.wegfan.relicsmanagement.constant.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.constant.OperationItemTypeEnum;
import cn.wegfan.relicsmanagement.constant.RelicStatusEnum;
import cn.wegfan.relicsmanagement.mapper.*;
import cn.wegfan.relicsmanagement.model.dto.RelicMoveDto;
import cn.wegfan.relicsmanagement.model.entity.*;
import cn.wegfan.relicsmanagement.model.vo.*;
import cn.wegfan.relicsmanagement.util.BusinessException;
import cn.wegfan.relicsmanagement.util.OperationLogUtil;
import cn.wegfan.relicsmanagement.util.OperationLogUtil.FieldDifference;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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
    private OperationLogService operationLogService;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public PageResultVo<RelicCheckDetailVo> listRelicCheckDetailByCheckIdAndStatusAndPage(Integer checkId, Boolean checked, long pageIndex, long pageSize) {
        Page<RelicCheckDetail> page = new Page<>(pageIndex, pageSize);
        Page<RelicCheckDetail> pageResult = relicCheckDetailDao.selectPageByCheckId(page, checkId, checked);

        List<RelicCheckDetail> relicCheckDetailList = pageResult.getRecords();
        List<RelicCheckDetailVo> relicCheckDetailVoList = mapperFacade.mapAsList(relicCheckDetailList, RelicCheckDetailVo.class);
        return new PageResultVo<RelicCheckDetailVo>(relicCheckDetailVoList, pageResult);
    }

    @Override
    public FilePathVo exportRelicCheckDetailByCheckIdToExcel(Integer checkId) {
        long pageSize = 500;

        Path dir = Paths.get("data", "exports", "checks")
                .toAbsolutePath();
        FileUtil.mkdir(dir.toFile());
        String fileName = "第" + checkId + "次文物盘点列表_" +
                DateUtil.format(new Date(), "yyyy-MM-dd_HH-mm-ss") + ".xlsx";
        File file = dir.resolve(fileName)
                .toFile();
        ExcelWriter excelWriter = EasyExcel.write(file, RelicCheckDetailExcelVo.class)
                .build();

        WriteSheet writeSheet = EasyExcel.writerSheet("文物盘点列表").build();

        Path imageDirectory = Paths.get("data", "images")
                .toAbsolutePath();

        int pageIndex = 1;
        PageResultVo<RelicCheckDetailVo> pageResult;
        do {
            pageResult = listRelicCheckDetailByCheckIdAndStatusAndPage(checkId, null,
                    pageIndex, pageSize);
            List<RelicCheckDetailVo> relicCheckDetailVoList = pageResult.getContent();
            List<RelicCheckDetailExcelVo> data = mapperFacade.mapAsList(relicCheckDetailVoList, RelicCheckDetailExcelVo.class);
            data.forEach(i -> {
                // 设置盘点状态
                if (i.getCheckTime() == null) {
                    i.setStatus("未盘点");
                } else if (i.getOldWarehouseId().equals(i.getNewWarehouseId()) &&
                        i.getOldShelfId().equals(i.getNewShelfId())) {
                    i.setStatus("盘点正常");
                } else {
                    i.setStatus("盘点异常");
                }
                // 设置照片地址
                i.setPicturePath(imageDirectory.resolve(FileUtil.getName(i.getPicturePath())).toString());
            });
            excelWriter.write(data, writeSheet);
            pageIndex++;
        } while (pageResult.getHasNext());
        excelWriter.finish();

        return new FilePathVo("/api/files/exports/checks/" + fileName);
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
        if (!relic.getStatusId().equals(RelicStatusEnum.InMuseum.getStatusId())) {
            throw new BusinessException(BusinessErrorEnum.RelicNotInMuseum);
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

        Relic oldRelic = SerializationUtils.clone(relic);

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

        try {
            Map<String, FieldDifference> fieldDifferenceMap = OperationLogUtil.getDifferenceFieldMap(oldRelic, relic, Relic.class);
            // 把仓库变成字符串  
            if (fieldDifferenceMap.containsKey("warehouseId")) {
                String oldWarehouseString = Optional.ofNullable(oldRelic.getWarehouse())
                        .map(Warehouse::getName)
                        .orElse(null);
                fieldDifferenceMap.get("warehouseId").setOldValue(oldWarehouseString);
                String newWarehouseString = Optional.ofNullable(relic.getWarehouseId())
                        .map(i -> warehouseDao.selectNotDeletedById(i))
                        .map(Warehouse::getName)
                        .orElse(null);
                fieldDifferenceMap.get("warehouseId").setNewValue(newWarehouseString);
            }
            // 把货架变成字符串  
            if (fieldDifferenceMap.containsKey("shelfId")) {
                String oldShelfString = Optional.ofNullable(oldRelic.getShelf())
                        .map(Shelf::getName)
                        .orElse(null);
                fieldDifferenceMap.get("shelfId").setOldValue(oldShelfString);
                String newShelfString = Optional.ofNullable(relic.getShelfId())
                        .map(i -> shelfDao.selectNotDeletedById(i))
                        .map(Shelf::getName)
                        .orElse(null);
                fieldDifferenceMap.get("shelfId").setNewValue(newShelfString);
            }

            // 添加操作记录
            OperationItemTypeEnum itemType = OperationItemTypeEnum.Relic;
            Integer itemId = relic.getId();
            String detail = OperationLogUtil.getUpdateItemDetailLog(itemType, itemId, relic.getName(), fieldDifferenceMap);
            // 把“更改”替换成“盘点”
            detail = detail.replaceFirst("更改", "盘点");
            operationLogService.addOperationLog(itemType, itemId,
                    "盘点文物", detail);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("获取不同成员变量错误", e);
            throw new BusinessException(BusinessErrorEnum.InternalServerError);
        }

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
