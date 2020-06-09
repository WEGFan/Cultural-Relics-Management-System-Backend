package cn.wegfan.relicsmanagement.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.wegfan.relicsmanagement.dto.RelicInfoDto;
import cn.wegfan.relicsmanagement.dto.RelicMoveDto;
import cn.wegfan.relicsmanagement.entity.Relic;
import cn.wegfan.relicsmanagement.entity.RelicStatus;
import cn.wegfan.relicsmanagement.entity.Shelf;
import cn.wegfan.relicsmanagement.entity.Warehouse;
import cn.wegfan.relicsmanagement.mapper.RelicDao;
import cn.wegfan.relicsmanagement.mapper.RelicStatusDao;
import cn.wegfan.relicsmanagement.mapper.ShelfDao;
import cn.wegfan.relicsmanagement.mapper.WarehouseDao;
import cn.wegfan.relicsmanagement.util.*;
import cn.wegfan.relicsmanagement.util.OperationLogUtil.FieldDifference;
import cn.wegfan.relicsmanagement.vo.*;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RelicServiceImpl extends ServiceImpl<RelicDao, Relic> implements RelicService {

    @Autowired
    private RelicStatusDao relicStatusDao;

    @Autowired
    private RelicDao relicDao;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RelicCheckDetailService relicCheckDetailService;

    @Autowired
    private ShelfDao shelfDao;

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public List<RelicStatus> listAllRelicStatus() {
        List<RelicStatus> relicStatusList = relicStatusDao.selectRelicStatusList();
        return relicStatusList;
    }

    @Override
    public PageResultVo<RelicVo> searchNotDeletedRelicsByPage(String name, Integer status,
                                                              Integer warehouseId, Integer shelfId,
                                                              String dateType, Date startTime, Date endTime,
                                                              long pageIndex, long pageSize) {
        Page<Relic> page = new Page<>(pageIndex, pageSize);

        if (StrUtil.isEmpty(name)) {
            name = null;
        }
        if (StrUtil.isEmpty(dateType)) {
            dateType = null;
        }

        // 检测是否填写了开始或结束时间，但是没填写时间类型
        if (dateType == null && (startTime != null || endTime != null)) {
            throw new BusinessException(BusinessErrorEnum.NoDateType);
        }

        // 检测字符串是否符合格式
        if (dateType != null && dateType.matches("^(enter|leave|lend|fix)$")) {
            dateType += "_time";
        } else {
            dateType = null;
        }

        Page<Relic> pageResult = relicDao.selectPageNotDeletedByCondition(page, EscapeUtil.escapeSqlLike(name), status,
                warehouseId, shelfId, dateType, startTime, endTime);

        List<Relic> relicList = pageResult.getRecords();
        List<RelicVo> relicVoList = mapperFacade.mapAsList(relicList, RelicVo.class);

        // 获取当前登录的用户权限
        Set<String> permissionCodeSet = permissionService.listAllPermissionCodeByCurrentLoginUser();

        relicVoList.forEach(i -> i.clearFieldsByPermissionCode(permissionCodeSet));
        return new PageResultVo<RelicVo>(relicVoList, pageResult);
    }

    @Override
    public RelicVo getRelicById(Integer relicId) {
        // 根据文物编号查找未删除的文物中是否存在该文物
        Relic relic = relicDao.selectNotDeletedByRelicId(relicId);
        if (relic == null) {
            throw new BusinessException(BusinessErrorEnum.RelicNotExists);
        }

        RelicVo relicVo = mapperFacade.map(relic, RelicVo.class);

        // 获取当前登录的用户权限
        Set<String> permissionCodeSet = permissionService.listAllPermissionCodeByCurrentLoginUser();

        relicVo.clearFieldsByPermissionCode(permissionCodeSet);
        return relicVo;
    }

    @Override
    public RelicIdPicturePathVo addRelicByPicturePath(String tempPath) throws IllegalAccessException {
        File tempFile = new File(tempPath);
        // 获取文件真实类型
        String fileType = FileTypeUtil.getType(tempFile);
        if (!"jpg".equals(fileType) && !"png".equals(fileType)) {
            throw new BusinessException(BusinessErrorEnum.FileNotJpgOrPng);
        }
        String fileExtension = "." + fileType;
        File tempFileWithExtension = new File(tempFile + fileExtension);
        // 给文件加上真实扩展名，不然后面图片格式转换会失败
        FileUtil.move(tempFile, tempFileWithExtension, true);

        Relic relic = new Relic();
        // 设置所有字符串类型的值为空白字符串
        for (Field field : relic.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Class<?> fieldType = field.getType();
            if (fieldType == String.class) {
                field.set(relic, "");
            }
        }
        // 设置为待评估
        relic.setStatusId(1);
        relic.setCreateTime(new Date());
        relic.setUpdateTime(new Date());

        // 先插入到数据库并获取id
        relicDao.insert(relic);

        String fileName = relic.getId() + ".jpg";
        File file = FileUtil.touch(tempFile.getParentFile().getParentFile(), fileName);
        // 转换成jpg格式
        ImgUtil.convert(tempFileWithExtension, file);

        relic.setPicturePath("/api/files/relics/images/" + fileName);
        relicDao.updateById(relic);

        // 添加操作记录
        String detail = OperationLogUtil.getCreateItemDetailLog(OperationItemTypeEnum.Relic, relic.getId(), null);
        operationLogService.addOperationLog(OperationItemTypeEnum.Relic, relic.getId(),
                "增加文物", detail);

        return new RelicIdPicturePathVo(relic.getId(), relic.getPicturePath());
    }

    @Override
    public SuccessVo deleteRelicById(Integer relicId) {
        Relic relic = relicDao.selectNotDeletedByRelicId(relicId);
        if (relic == null) {
            throw new BusinessException(BusinessErrorEnum.RelicNotExists);
        }

        int result = relicDao.deleteRelicById(relicId);

        // TODO: 优化
        // 如果有正在被盘点的，就删除盘点详情记录
        Integer oldRelicWarehouseId = relic.getWarehouseId();
        Integer oldRelicShelfId = relic.getShelfId();
        Integer newRelicWarehouseId = null;
        Integer newRelicShelfId = null;
        RelicMoveDto oldPlace = new RelicMoveDto(oldRelicWarehouseId, oldRelicShelfId);
        RelicMoveDto newPlace = new RelicMoveDto(newRelicWarehouseId, newRelicShelfId);
        relicCheckDetailService.updateRelicCheckDetailAfterRelicMove(relicId, oldPlace, newPlace);

        // 添加操作记录
        String detail = OperationLogUtil.getDeleteItemDetailLog(OperationItemTypeEnum.Relic, relicId, relic.getName());
        operationLogService.addOperationLog(OperationItemTypeEnum.Relic, relicId,
                "删除文物", detail);

        return new SuccessVo(result > 0);
    }

    @Override
    public RelicVo updateRelicInfo(Integer relicId, RelicInfoDto relicInfo) {
        // FIX: 无法清空非字符串的字段
        // 根据文物编号查找未删除的文物中是否存在该文物
        Relic relic = relicDao.selectNotDeletedByRelicId(relicId);
        if (relic == null) {
            throw new BusinessException(BusinessErrorEnum.RelicNotExists);
        }

        Integer oldRelicStatusId = relic.getStatusId();
        Integer newRelicStatusId = relicInfo.getStatusId();
        if (newRelicStatusId == null) {
            newRelicStatusId = oldRelicStatusId;
        }

        Integer oldRelicWarehouseId = relic.getWarehouseId();
        Integer oldRelicShelfId = relic.getShelfId();
        Integer newRelicWarehouseId = relicInfo.getWarehouseId();
        Integer newRelicShelfId = relicInfo.getShelfId();

        // 如果文物状态不是在馆，且仓库和货架信息不为空的话，就报错提示
        if (!newRelicStatusId.equals(RelicStatusEnum.InMuseum.getStatusId()) &&
                (newRelicWarehouseId != null || newRelicShelfId != null)) {
            throw new BusinessException(BusinessErrorEnum.NotInMuseumRelicCanNotHaveLocation);
        }

        // 如果文物状态从外借、维修、离馆改为在馆，检测是否填写了仓库、货架信息
        if (oldRelicStatusId > RelicStatusEnum.InMuseum.getStatusId() &&
                newRelicStatusId.equals(RelicStatusEnum.InMuseum.getStatusId()) &&
                (newRelicWarehouseId == null || newRelicShelfId == null)) {
            throw new BusinessException(400, "文物的仓库/货架信息不能为空");
        }

        // 根据修改后状态和仓库、货架判断文物是否被移动
        boolean relicMoved = newRelicStatusId.equals(RelicStatusEnum.InMuseum.getStatusId()) &&
                (!Objects.equals(oldRelicWarehouseId, newRelicWarehouseId) ||
                        !Objects.equals(oldRelicShelfId, newRelicShelfId));

        // 获取当前登录的用户权限
        Set<String> permissionCodeSet = permissionService.listAllPermissionCodeByCurrentLoginUser();
        // 根据权限检查是否存在不能修改的字段，并把前端传来的空字符串设为null
        boolean fieldAllValid = relicInfo.checkAndClearFieldsByPermissionCode(permissionCodeSet);
        if (!fieldAllValid) {
            throw new BusinessException(BusinessErrorEnum.Unauthorized);
        }

        // 如果用户只有修改文物状态为入馆的权限
        if (permissionCodeSet.contains(PermissionCodeEnum.RELIC_ENTER_MUSEUM) &&
                !permissionCodeSet.contains(PermissionCodeEnum.EDIT_RELIC_STATUS)) {
            // 判断修改前和修改后的权限是否为待评估或入馆
            if (oldRelicStatusId > RelicStatusEnum.InMuseum.getStatusId() ||
                    newRelicStatusId > RelicStatusEnum.InMuseum.getStatusId()) {
                throw new BusinessException(BusinessErrorEnum.Unauthorized);
            }
        }

        // 如果文物的仓库和货架不为空，检测货架是否存在
        if ((newRelicWarehouseId != null || newRelicShelfId != null) &&
                shelfDao.selectNotDeletedByWarehouseIdAndShelfId(newRelicWarehouseId, newRelicShelfId) == null) {
            throw new BusinessException(BusinessErrorEnum.ShelfNotExists);
        }

        Relic oldRelic = SerializationUtils.clone(relic);
        mapperFacade.map(relicInfo, relic);

        relic.setUpdateTime(new Date());

        // 如果移动了文物则设置移动时间
        if (relicMoved) {
            relic.setMoveTime(new Date());
        }

        // 如果文物移动了或者修改了文物状态，就更新正在盘点的信息
        if (relicMoved || !newRelicStatusId.equals(oldRelicStatusId)) {
            // 如果更改前后的仓库正在被盘点
            RelicMoveDto oldPlace = new RelicMoveDto(oldRelicWarehouseId, oldRelicShelfId);
            RelicMoveDto newPlace = new RelicMoveDto(newRelicWarehouseId, newRelicShelfId);
            relicCheckDetailService.updateRelicCheckDetailAfterRelicMove(relicId, oldPlace, newPlace);
        }

        // 如果文物状态不是在馆就清除仓库和货架信息（由于实体映射配置了不映射null，所以得手动设置下）
        if (!relic.getStatusId().equals(RelicStatusEnum.InMuseum.getStatusId())) {
            relic.setWarehouseId(null);
            relic.setShelfId(null);
        }

        // 如果修改了文物状态
        if (!newRelicStatusId.equals(oldRelicStatusId)) {
            // 入馆 设置入馆时间
            if (newRelicStatusId.equals(RelicStatusEnum.InMuseum.getStatusId())) {
                relic.setEnterTime(new Date());
            } else {
                // 清除时间
                relic.setFixTime(null);
                relic.setLendTime(null);
                relic.setLeaveTime(null);
                relic.setMoveTime(null);
                // 根据状态设置时间
                if (newRelicStatusId.equals(RelicStatusEnum.Lend.getStatusId())) {
                    relic.setLendTime(new Date());
                } else if (newRelicStatusId.equals(RelicStatusEnum.Fix.getStatusId())) {
                    relic.setFixTime(new Date());
                } else if (newRelicStatusId.equals(RelicStatusEnum.LeaveMuseum.getStatusId())) {
                    relic.setLeaveTime(new Date());
                }
            }
        }

        relicDao.updateById(relic);

        try {
            Map<String, FieldDifference> fieldDifferenceMap = OperationLogUtil.getDifferenceFieldMap(oldRelic, relic, Relic.class);
            // 把状态变成字符串
            if (fieldDifferenceMap.containsKey("statusId")) {
                String oldStatusString = mapperFacade.convert(oldRelicStatusId, String.class, "relicStatusNameConverter", null);
                fieldDifferenceMap.get("statusId").setOldValue(oldStatusString);
                String statusString = mapperFacade.convert(newRelicStatusId, String.class, "relicStatusNameConverter", null);
                fieldDifferenceMap.get("statusId").setNewValue(statusString);
            }
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
            operationLogService.addOperationLog(itemType, itemId,
                    "修改文物", detail);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("获取不同成员变量错误", e);
            throw new BusinessException(BusinessErrorEnum.InternalServerError);
        }

        RelicVo relicVo = mapperFacade.map(relic, RelicVo.class);
        relicVo.clearFieldsByPermissionCode(permissionCodeSet);
        return relicVo;
    }

    @Override
    public FilePathVo exportRelicByConditionToExcel(String name, Integer status,
                                                    Integer warehouseId, Integer shelfId,
                                                    String dateType, Date startTime, Date endTime) {
        long pageSize = 500;

        Path dir = Paths.get("data", "exports", "relics")
                .toAbsolutePath();
        FileUtil.mkdir(dir.toFile());
        String fileName = "文物列表_" + DateUtil.format(new Date(), "yyyy-MM-dd_HH-mm-ss") + ".xlsx";
        File file = dir.resolve(fileName)
                .toFile();

        // 获取当前登录的用户权限
        Set<String> permissionCodeSet = permissionService.listAllPermissionCodeByCurrentLoginUser();
        List<String> excludeColumnList = new ArrayList<>();
        if (!permissionCodeSet.contains(PermissionCodeEnum.VIEW_EDIT_RELIC_PRICE)) {
            excludeColumnList.addAll(Arrays.asList("enterPrice", "leavePrice"));
        }
        if (!permissionCodeSet.contains(PermissionCodeEnum.WAREHOUSE)) {
            excludeColumnList.addAll(Arrays.asList("lastCheckTime", "enterTime", "leaveTime",
                    "moveTime", "lendTime", "fixTime"));
        }

        ExcelWriter excelWriter = EasyExcel.write(file, RelicExcelVo.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .excludeColumnFiledNames(excludeColumnList)
                .build();

        WriteSheet writeSheet = EasyExcel.writerSheet("文物列表").build();

        int pageIndex = 1;
        PageResultVo<RelicVo> pageResult;
        do {
            pageResult = searchNotDeletedRelicsByPage(name, status,
                    warehouseId, shelfId, dateType, startTime, endTime,
                    pageIndex, pageSize);
            List<RelicVo> relicVoList = pageResult.getContent();
            List<RelicExcelVo> data = mapperFacade.mapAsList(relicVoList, RelicExcelVo.class);

            excelWriter.write(data, writeSheet);
            pageIndex++;
        } while (pageResult.getHasNext());
        excelWriter.finish();

        return new FilePathVo("/api/files/exports/relics/" + fileName);
    }

}
