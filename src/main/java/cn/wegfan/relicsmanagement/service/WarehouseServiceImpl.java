package cn.wegfan.relicsmanagement.service;

import cn.hutool.core.util.StrUtil;
import cn.wegfan.relicsmanagement.constant.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.constant.OperationItemTypeEnum;
import cn.wegfan.relicsmanagement.entity.Warehouse;
import cn.wegfan.relicsmanagement.mapper.RelicDao;
import cn.wegfan.relicsmanagement.mapper.ShelfDao;
import cn.wegfan.relicsmanagement.mapper.WarehouseDao;
import cn.wegfan.relicsmanagement.util.BusinessException;
import cn.wegfan.relicsmanagement.util.EscapeUtil;
import cn.wegfan.relicsmanagement.util.OperationLogUtil;
import cn.wegfan.relicsmanagement.util.OperationLogUtil.FieldDifference;
import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.SuccessVo;
import cn.wegfan.relicsmanagement.vo.WarehouseVo;
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
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private ShelfDao shelfDao;

    @Autowired
    private RelicDao relicDao;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public PageResultVo<WarehouseVo> listNotDeletedWarehousesByNameAndPage(String name, long pageIndex, long pageSize) {
        Page<Warehouse> page = new Page<>(pageIndex, pageSize);

        if (StrUtil.isEmpty(name)) {
            name = null;
        }

        Page<Warehouse> pageResult = warehouseDao.selectPageNotDeletedByName(page, EscapeUtil.escapeSqlLike(name));

        List<Warehouse> warehouseList = pageResult.getRecords();
        List<WarehouseVo> warehouseVoList = mapperFacade.mapAsList(warehouseList, WarehouseVo.class);

        return new PageResultVo<WarehouseVo>(warehouseVoList, pageResult);
    }

    @Override
    public List<WarehouseVo> listNotDeletedWarehouses() {
        List<Warehouse> warehouseList = warehouseDao.selectNotDeletedList();
        List<WarehouseVo> warehouseVoList = mapperFacade.mapAsList(warehouseList, WarehouseVo.class);

        return warehouseVoList;
    }

    @Override
    public WarehouseVo createWarehouse(String name) {
        // 检测没有被删除的仓库中是否存在相同名字的仓库
        if (warehouseDao.selectNotDeletedByExactName(name) != null) {
            throw new BusinessException(BusinessErrorEnum.DuplicateWarehouseName);
        }

        Warehouse warehouse = new Warehouse();
        warehouse.setName(name);
        warehouse.setCreateTime(new Date());
        warehouse.setUpdateTime(new Date());

        warehouseDao.insert(warehouse);

        WarehouseVo warehouseVo = mapperFacade.map(warehouse, WarehouseVo.class);

        try {
            Map<String, FieldDifference> fieldDifferenceMap = OperationLogUtil.getDifferenceFieldMap(null, warehouse, Warehouse.class);

            // 添加操作记录
            OperationItemTypeEnum itemType = OperationItemTypeEnum.Warehouse;
            Integer itemId = warehouse.getId();
            String detail = OperationLogUtil.getCreateItemDetailLog(itemType, itemId, fieldDifferenceMap);
            operationLogService.addOperationLog(itemType, itemId,
                    "新建仓库", detail);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("获取不同成员变量错误", e);
            throw new BusinessException(BusinessErrorEnum.InternalServerError);
        }

        return warehouseVo;
    }

    @Override
    public WarehouseVo updateWarehouse(Integer warehouseId, String name) {
        Warehouse warehouse = warehouseDao.selectNotDeletedById(warehouseId);

        // 检测没有被删除的仓库中是否存在仓库编号对应的仓库
        if (warehouse == null) {
            throw new BusinessException(BusinessErrorEnum.WarehouseNotExists);
        }
        // 检测没有被删除的其他仓库中是否存在相同名字的仓库
        Warehouse sameNameWarehouse = warehouseDao.selectNotDeletedByExactName(name);
        if (sameNameWarehouse != null && !sameNameWarehouse.getId().equals(warehouseId)) {
            throw new BusinessException(BusinessErrorEnum.DuplicateWarehouseName);
        }

        Warehouse oldWarehouse = SerializationUtils.clone(warehouse);

        warehouse.setName(name);
        warehouse.setUpdateTime(new Date());

        warehouseDao.updateById(warehouse);

        try {
            Map<String, FieldDifference> fieldDifferenceMap = OperationLogUtil.getDifferenceFieldMap(oldWarehouse, warehouse, Warehouse.class);

            // 添加操作记录
            OperationItemTypeEnum itemType = OperationItemTypeEnum.Warehouse;
            Integer itemId = warehouse.getId();
            String detail = OperationLogUtil.getUpdateItemDetailLog(itemType, itemId, warehouse.getName(), fieldDifferenceMap);
            operationLogService.addOperationLog(itemType, itemId,
                    "修改仓库", detail);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("获取不同成员变量错误", e);
            throw new BusinessException(BusinessErrorEnum.InternalServerError);
        }

        WarehouseVo warehouseVo = mapperFacade.map(warehouse, WarehouseVo.class);
        return warehouseVo;
    }

    @Override
    public SuccessVo deleteWarehouse(Integer warehouseId) {
        Warehouse warehouse = warehouseDao.selectNotDeletedById(warehouseId);
        // 检测没有被删除的仓库中是否存在仓库编号对应的仓库
        if (warehouse == null) {
            throw new BusinessException(BusinessErrorEnum.WarehouseNotExists);
        }
        // 检测该仓库里是否还有货架
        if (!shelfDao.selectNotDeletedListByWarehouseId(warehouseId).isEmpty()) {
            throw new BusinessException(BusinessErrorEnum.WarehouseNotEmpty);
        }

        int result = warehouseDao.deleteWarehouseById(warehouseId);

        // 添加操作记录
        String detail = OperationLogUtil.getDeleteItemDetailLog(OperationItemTypeEnum.Warehouse, warehouseId, warehouse.getName());
        operationLogService.addOperationLog(OperationItemTypeEnum.Warehouse, warehouseId,
                "删除仓库", detail);

        return new SuccessVo(result > 0);
    }

}
