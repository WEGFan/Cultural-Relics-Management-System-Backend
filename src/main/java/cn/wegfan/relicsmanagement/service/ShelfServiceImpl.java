package cn.wegfan.relicsmanagement.service;

import cn.hutool.core.util.StrUtil;
import cn.wegfan.relicsmanagement.constant.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.constant.OperationItemTypeEnum;
import cn.wegfan.relicsmanagement.dto.ShelfDto;
import cn.wegfan.relicsmanagement.entity.Shelf;
import cn.wegfan.relicsmanagement.entity.Warehouse;
import cn.wegfan.relicsmanagement.mapper.RelicDao;
import cn.wegfan.relicsmanagement.mapper.ShelfDao;
import cn.wegfan.relicsmanagement.mapper.WarehouseDao;
import cn.wegfan.relicsmanagement.util.BusinessException;
import cn.wegfan.relicsmanagement.util.EscapeUtil;
import cn.wegfan.relicsmanagement.util.OperationLogUtil;
import cn.wegfan.relicsmanagement.util.OperationLogUtil.FieldDifference;
import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.ShelfVo;
import cn.wegfan.relicsmanagement.vo.SuccessVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ShelfServiceImpl implements ShelfService {

    @Autowired
    private ShelfDao shelfDao;

    @Autowired
    private RelicDao relicDao;

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private RelicCheckDetailService relicCheckDetailService;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public PageResultVo<ShelfVo> listNotDeletedShelvesByWarehouseIdAndNameAndPage(Integer warehouseId, String name, long pageIndex, long pageSize) {
        Page<Shelf> page = new Page<>(pageIndex, pageSize);

        if (StrUtil.isEmpty(name)) {
            name = null;
        }

        Page<Shelf> pageResult = shelfDao.selectPageNotDeletedByWarehouseIdAndName(page, warehouseId, EscapeUtil.escapeSqlLike(name));

        List<Shelf> shelfList = pageResult.getRecords();
        List<ShelfVo> shelfVoList = mapperFacade.mapAsList(shelfList, ShelfVo.class);

        return new PageResultVo<ShelfVo>(shelfVoList, pageResult);
    }

    @Override
    public List<ShelfVo> listNotDeletedShelves() {
        List<Shelf> shelfList = shelfDao.selectNotDeletedList();
        List<ShelfVo> shelfVoList = mapperFacade.mapAsList(shelfList, ShelfVo.class);
        return shelfVoList;
    }

    @Override
    public List<ShelfVo> listNotDeletedShelvesByWarehouseId(Integer warehouseId) {
        List<Shelf> shelfList = shelfDao.selectNotDeletedListByWarehouseId(warehouseId);
        List<ShelfVo> shelfVoList = mapperFacade.mapAsList(shelfList, ShelfVo.class);
        return shelfVoList;
    }

    @Override
    public ShelfVo createShelf(ShelfDto dto) {
        Integer warehouseId = dto.getWarehouseId();
        String name = dto.getName();
        // 检测该仓库没有被删除的货架中是否存在相同名字的货架
        if (shelfDao.selectNotDeletedByWarehouseIdAndExactName(warehouseId, name) != null) {
            throw new BusinessException(BusinessErrorEnum.DuplicateShelfName);
        }
        Warehouse warehouse = warehouseDao.selectNotDeletedById(warehouseId);
        // 检测仓库是否存在
        if (warehouse == null) {
            throw new BusinessException(BusinessErrorEnum.WarehouseNotExists);
        }

        Shelf shelf = new Shelf();
        shelf.setWarehouseId(warehouseId);
        shelf.setName(name);
        shelf.setCreateTime(new Date());
        shelf.setUpdateTime(new Date());

        shelfDao.insert(shelf);

        try {
            Map<String, FieldDifference> fieldDifferenceMap = OperationLogUtil.getDifferenceFieldMap(null, shelf, Shelf.class);
            // 把所属仓库变成名称
            String warehouseName = warehouse.getName();
            fieldDifferenceMap.get("warehouseId").setNewValue(warehouseName);

            // 添加操作记录
            OperationItemTypeEnum itemType = OperationItemTypeEnum.Shelf;
            Integer itemId = shelf.getId();
            String detail = OperationLogUtil.getCreateItemDetailLog(itemType, itemId, fieldDifferenceMap);
            operationLogService.addOperationLog(itemType, itemId,
                    "新建货架", detail);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("获取不同成员变量错误", e);
            throw new BusinessException(BusinessErrorEnum.InternalServerError);
        }

        ShelfVo shelfVo = mapperFacade.map(shelf, ShelfVo.class);
        return shelfVo;
    }

    @Override
    public ShelfVo updateShelf(Integer shelfId, ShelfDto dto) {
        Shelf shelf = shelfDao.selectNotDeletedById(shelfId);
        Integer newWarehouseId = dto.getWarehouseId();

        // 检测没有被删除的货架中是否存在货架编号对应的货架
        if (shelf == null) {
            throw new BusinessException(BusinessErrorEnum.ShelfNotExists);
        }
        Warehouse warehouse = warehouseDao.selectNotDeletedById(newWarehouseId);
        // 检测仓库是否存在
        if (warehouse == null) {
            throw new BusinessException(BusinessErrorEnum.WarehouseNotExists);
        }

        String name = dto.getName();
        Integer oldWarehouseId = shelf.getWarehouseId();

        // 检测相同仓库里没有被删除的其他货架中是否存在相同名字的货架
        Shelf sameNameShelf = shelfDao.selectNotDeletedByWarehouseIdAndExactName(newWarehouseId, name);
        if (sameNameShelf != null && !sameNameShelf.getId().equals(shelfId)) {
            throw new BusinessException(BusinessErrorEnum.DuplicateShelfName);
        }

        // 如果移动了货架所在的仓库
        if (!shelf.getWarehouseId().equals(newWarehouseId)) {
            // 更新文物表里的仓库id
            relicDao.updateRelicWarehouse(oldWarehouseId, newWarehouseId);
            // 更新盘点详情记录
            relicCheckDetailService.updateRelicCheckDetailAfterShelfMove(shelfId, oldWarehouseId, newWarehouseId);
        }

        Shelf oldShelf = SerializationUtils.clone(shelf);

        shelf.setName(name);
        shelf.setWarehouseId(newWarehouseId);
        shelf.setUpdateTime(new Date());

        shelfDao.updateById(shelf);

        try {
            Map<String, FieldDifference> fieldDifferenceMap = OperationLogUtil.getDifferenceFieldMap(oldShelf, shelf, Shelf.class);
            // 把所属仓库变成名称
            if (fieldDifferenceMap.containsKey("warehouseId")) {
                String oldWarehouseName = warehouseDao.selectByWarehouseId(oldWarehouseId).getName();
                fieldDifferenceMap.get("warehouseId").setOldValue(oldWarehouseName);
                String warehouseName = warehouse.getName();
                fieldDifferenceMap.get("warehouseId").setNewValue(warehouseName);
            }

            // 添加操作记录
            OperationItemTypeEnum itemType = OperationItemTypeEnum.Shelf;
            Integer itemId = shelf.getId();
            String detail = OperationLogUtil.getUpdateItemDetailLog(itemType, itemId, shelf.getName(), fieldDifferenceMap);
            operationLogService.addOperationLog(itemType, itemId,
                    "修改货架", detail);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("获取不同成员变量错误", e);
            throw new BusinessException(BusinessErrorEnum.InternalServerError);
        }

        ShelfVo shelfVo = mapperFacade.map(shelf, ShelfVo.class);
        return shelfVo;
    }

    @Override
    public SuccessVo deleteShelf(Integer shelfId) {
        Shelf shelf = shelfDao.selectNotDeletedById(shelfId);
        // 检测没有被删除的仓库中是否存在仓库编号对应的仓库
        if (shelf == null) {
            throw new BusinessException(BusinessErrorEnum.ShelfNotExists);
        }
        // 检测该货架上是否还有文物
        if (!relicDao.selectNotDeletedByShelfId(shelfId).isEmpty()) {
            throw new BusinessException(BusinessErrorEnum.ShelfNotEmpty);
        }

        int result = shelfDao.deleteShelfById(shelfId);

        // 添加操作记录
        String detail = OperationLogUtil.getDeleteItemDetailLog(OperationItemTypeEnum.Shelf, shelfId, shelf.getName());
        operationLogService.addOperationLog(OperationItemTypeEnum.Shelf, shelfId,
                "删除货架", detail);

        return new SuccessVo(result > 0);
    }

}
