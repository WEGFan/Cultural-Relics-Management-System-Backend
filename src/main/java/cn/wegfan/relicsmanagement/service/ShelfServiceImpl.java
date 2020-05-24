package cn.wegfan.relicsmanagement.service;

import cn.hutool.core.util.StrUtil;
import cn.wegfan.relicsmanagement.dto.ShelfDto;
import cn.wegfan.relicsmanagement.dto.ShelfNameDto;
import cn.wegfan.relicsmanagement.entity.Shelf;
import cn.wegfan.relicsmanagement.mapper.RelicDao;
import cn.wegfan.relicsmanagement.mapper.ShelfDao;
import cn.wegfan.relicsmanagement.mapper.WarehouseDao;
import cn.wegfan.relicsmanagement.util.BusinessErrorEnum;
import cn.wegfan.relicsmanagement.util.BusinessException;
import cn.wegfan.relicsmanagement.util.EscapeUtil;
import cn.wegfan.relicsmanagement.vo.PageResultVo;
import cn.wegfan.relicsmanagement.vo.ShelfVo;
import cn.wegfan.relicsmanagement.vo.SuccessVo;
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
public class ShelfServiceImpl implements ShelfService {

    @Autowired
    private ShelfDao shelfDao;

    @Autowired
    private RelicDao relicDao;

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private RelicCheckDetailService relicCheckDetailService;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    private MapperFacade mapperFacade;

    public ShelfServiceImpl() {
        mapperFactory.classMap(Shelf.class, ShelfVo.class)
                .byDefault()
                .register();
        mapperFactory.classMap(ShelfNameDto.class, Shelf.class)
                .byDefault()
                .register();
        mapperFacade = mapperFactory.getMapperFacade();
    }

    @Override
    public PageResultVo<ShelfVo> listNotDeletedShelvesByWarehouseIdAndNameAndPage(Integer warehouseId, String name, long pageIndex, long pageSize) {
        Page<Shelf> page = new Page<>(pageIndex, pageSize);

        if (StrUtil.isEmpty(name)) {
            name = null;
        }

        Page<Shelf> pageResult = shelfDao.selectPageNotDeletedByWarehouseIdAndName(page, warehouseId, EscapeUtil.escapeSqlLike(name));
        // log.debug(String.valueOf(result.getRecords()));
        // log.debug("current={} size={} total={} pages={}", result.getCurrent(), result.getSize(), result.getTotal(), result.getPages());
        // log.debug("{} {}", result.hasPrevious(), result.hasNext());
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
    public ShelfVo createShelf(ShelfDto dto) {
        Integer warehouseId = dto.getWarehouseId();
        String name = dto.getName();
        // 检测该仓库没有被删除的货架中是否存在相同名字的货架
        if (shelfDao.selectNotDeletedByWarehouseIdAndExactName(warehouseId, name) != null) {
            throw new BusinessException(BusinessErrorEnum.DuplicateShelfName);
        }
        // 检测仓库是否存在
        if (warehouseDao.selectNotDeletedById(dto.getWarehouseId()) == null) {
            throw new BusinessException(BusinessErrorEnum.WarehouseNotExists);
        }

        Shelf shelf = new Shelf();
        shelf.setWarehouseId(warehouseId);
        shelf.setName(name);
        shelf.setCreateTime(new Date());
        shelf.setUpdateTime(new Date());

        shelfDao.insert(shelf);

        ShelfVo shelfVo = mapperFacade.map(shelf, ShelfVo.class);

        return shelfVo;
    }

    @Override
    public ShelfVo updateShelf(Integer shelfId, ShelfDto dto) {
        Shelf shelf = shelfDao.selectNotDeletedById(shelfId);

        // 检测没有被删除的货架中是否存在货架编号对应的货架
        if (shelf == null) {
            throw new BusinessException(BusinessErrorEnum.ShelfNotExists);
        }
        // 检测仓库是否存在
        if (warehouseDao.selectNotDeletedById(dto.getWarehouseId()) == null) {
            throw new BusinessException(BusinessErrorEnum.WarehouseNotExists);
        }

        String name = dto.getName();
        Integer newWarehouseId = dto.getWarehouseId();
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

        shelf.setName(name);
        shelf.setWarehouseId(newWarehouseId);
        shelf.setUpdateTime(new Date());

        shelfDao.updateById(shelf);

        ShelfVo shelfVo = mapperFacade.map(shelf, ShelfVo.class);

        return shelfVo;
    }

    @Override
    public SuccessVo deleteShelf(Integer shelfId) {
        // 检测没有被删除的仓库中是否存在仓库编号对应的仓库
        if (shelfDao.selectNotDeletedById(shelfId) == null) {
            throw new BusinessException(BusinessErrorEnum.ShelfNotExists);
        }
        // 检测该货架上是否还有文物
        if (!relicDao.selectNotDeletedByShelfId(shelfId).isEmpty()) {
            throw new BusinessException(BusinessErrorEnum.ShelfNotEmpty);
        }

        int result = shelfDao.deleteShelfById(shelfId);
        return new SuccessVo(result > 0);
    }

}
