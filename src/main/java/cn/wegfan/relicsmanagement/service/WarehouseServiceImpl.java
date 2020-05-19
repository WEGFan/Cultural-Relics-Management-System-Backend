package cn.wegfan.relicsmanagement.service;

import cn.hutool.core.util.StrUtil;
import cn.wegfan.relicsmanagement.dto.WarehouseNameDto;
import cn.wegfan.relicsmanagement.entity.Warehouse;
import cn.wegfan.relicsmanagement.mapper.RelicDao;
import cn.wegfan.relicsmanagement.mapper.UserDao;
import cn.wegfan.relicsmanagement.mapper.WarehouseDao;
import cn.wegfan.relicsmanagement.util.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.util.BusinessException;
import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.SuccessVo;
import cn.wegfan.relicsmanagement.vo.WarehouseVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RelicDao relicDao;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    private MapperFacade mapperFacade;

    public WarehouseServiceImpl() {
        mapperFactory.classMap(Warehouse.class, WarehouseVo.class)
                .byDefault()
                .register();
        mapperFactory.classMap(WarehouseNameDto.class, Warehouse.class)
                .byDefault()
                .register();
        mapperFacade = mapperFactory.getMapperFacade();
    }

    @Override
    public PageResultVo<WarehouseVo> listNotDeletedWarehousesByNameAndPage(String name, long pageIndex, long pageSize) {
        Page<Warehouse> page = new Page<>(pageIndex, pageSize);

        if (StrUtil.isEmpty(name)) {
            name = null;
        }

        Page<Warehouse> pageResult = warehouseDao.selectPageNotDeletedByName(page, name);

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

        warehouse.setName(name);
        warehouse.setUpdateTime(new Date());

        warehouseDao.updateById(warehouse);

        WarehouseVo warehouseVo = mapperFacade.map(warehouse, WarehouseVo.class);

        return warehouseVo;
    }

    @Override
    public SuccessVo deleteWarehouse(Integer warehouseId) {
        // 检测没有被删除的仓库中是否存在仓库编号对应的仓库
        if (warehouseDao.selectNotDeletedById(warehouseId) == null) {
            throw new BusinessException(BusinessErrorEnum.WarehouseNotExists);
        }
        // 检测该仓库里是否还有文物
        if (!relicDao.selectNotDeletedByWarehouseId(warehouseId).isEmpty()) {
            throw new BusinessException(BusinessErrorEnum.WarehouseNotEmpty);
        }

        int result = warehouseDao.deleteWarehouseById(warehouseId);
        return new SuccessVo(result > 0);
    }

}
